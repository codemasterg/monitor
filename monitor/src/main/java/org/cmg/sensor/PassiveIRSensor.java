package org.cmg.sensor;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.cmg.data.MonitorStatus;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

/**
 * Abstraction of a PIR sensor that allows caller to register as an Observer for a given GPIO pin.  
 * When an event is detected on the given PIN, each observer is notified.
 * 
 * @author greg
 *
 */
@EnableScheduling
public class PassiveIRSensor extends Observable implements Sensor  {

	private static final Logger logger = Logger.getLogger(PassiveIRSensor.class.getName());
	
	// Injected
	private Pin sensorPin;
	private Pin ledPin;	// illuminated when sensor goes high
	private DB database;

	@Value(value="${org.cmg.sensor.rearm}")
	int notificationThresholdInSeconds;
	
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;
	
	// GPIO members
	private GpioController gpio;
	private GpioPinDigitalOutput sensorLED;
	private GpioPinDigitalInput sensor;
	
	public PassiveIRSensor(Pin sensorPin)
	{
		this.sensorPin = sensorPin;
		// create gpio controller
        this.gpio = GpioFactory.getInstance();

        // provision gpio pin as an input pin with its internal pull down resistor enabled
        this.sensor = gpio.provisionDigitalInputPin(this.sensorPin, PinPullResistance.PULL_DOWN);
	}
	
	/**
	 * Initialize the DB map associated with the injected DB.
	 * 
	 * If org.cmg.led.pin is set, the pin is chained to the state of the sensor so that when the 
	 * sensor goes HIGH, the LED attached to org.cmg.led.pin is illuminated.
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception 
	{
		this.monitorDataMap = database.getTreeMap("monitorDataMap");

		// provision gpio LED pin so it's on when sensor is HIGH, off when sensor is LOW
		if (this.ledPin != null)
		{
			this.sensorLED = gpio.provisionDigitalOutputPin(ledPin, "Sensor LED", PinState.LOW);
			this.sensor.addTrigger(new GpioSyncStateTrigger(sensorLED));
			logger.info("Chained " + ledPin.getName() + " to sensor associated with " + this.sensorPin.getName());
		}
	}
	
	/**
	 * Adds Observers to this sensor and listens for events on GPIO pin the sensor is attached to.
	 *  - If the sensor is MonitorStatus.ENABLED, each Observer is notified of the pin's state change
	 * (HIGH | LOW).
	 * 
	 */
	@Override
	public void registerForSensorEvents(List<Observer> observerList)
	{
		for(Observer observer : observerList)
		{
			this.addObserver(observer);
			logger.log(Level.INFO, "Pin " + sensorPin.getName() + " has been registered to observer: ." + observer.toString());
		}
        
        // create and register gpio pin listener
        sensor.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                logger.log(Level.INFO, "GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());           

                MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
    			if (monitorData.getStatus() != MonitorStatus.DISABLED )
    			{
    				DateTime now = new DateTime();
    				
    				int secondsSinceMostRecentUpdate = Integer.MAX_VALUE;
    				if (monitorData.getMostRecentDetectionDate() != null)  // should be null only after DB reset or new install
    				{
    					 secondsSinceMostRecentUpdate = Seconds.secondsBetween(monitorData.getMostRecentDetectionDate(), now).getSeconds();
    				}
    				
    				// only notify observers if time since most recent updates exceeds configurable threshold.
    				// this is done to avoid constant updates resulting from repeated movement detected by the sensor within the threshold.
    				if (secondsSinceMostRecentUpdate > notificationThresholdInSeconds)
    				{
    					setChanged();
    					notifyObservers(event);
    				}
    				else
    				{
    					if (event.getState() == PinState.HIGH)
    					{
    						monitorData.setMostRecentDetectionDate(now);
    						database.commit();
    						logger.log(Level.WARNING, "Motion detected, but it's too soon since most recent detection.");
    					}
    				}
    			}
    			else
    			{
    				logger.log(Level.INFO, "Sensor changed state, but sensor is " + monitorData.getStatus());
    			}
            }
            
        });
	}
	
	/**
	 * Periodically check to see if sensor has been administratively disabled, if so pulse sensor LED.
	 */
	@Scheduled(fixedDelay=3000)
	public void pulseLEDWhenSensorDisabled()
	{
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		if (monitorData.getStatus() == MonitorStatus.DISABLED )
		{
	        this.sensorLED.pulse(1000, true); // set second argument to 'true' use a blocking call	        	
		}
	}

	public DB getDatabase() {
		return database;
	}

	public void setDatabase(DB database) {
		this.database = database;
	}

	public Pin getLedPin() {
		return ledPin;
	}

	public void setLedPin(Pin ledPin) {
		this.ledPin = ledPin;
	}

}
