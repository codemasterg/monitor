package org.cmg.sensor;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Abstraction of a PIR sensor that allows caller to register as an Observer for a given GPIO pin.  
 * When an event is detected on the given PIN, each observer is notified.  Additional observers 
 * can be added by calling addObserver().
 * 
 * @author greg
 *
 */
public class PassiveIRSensor extends Observable implements Sensor  {

	private static final Logger logger = Logger.getLogger(PassiveIRSensor.class.getName());
	private Pin sensorPin;
	
	
	public PassiveIRSensor(Pin sensorPin)
	{
		this.sensorPin = sensorPin;
	}
	
	@Override
	public void registerForSensorEvents(Observer observer)
	{
		this.addObserver(observer);
		
		// create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput sensor = gpio.provisionDigitalInputPin(this.sensorPin, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        sensor.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                logger.log(Level.INFO, "GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());           

                setChanged();
                notifyObservers(event);    
            }
            
        });
        
        logger.log(Level.INFO, "Pin " + sensorPin.getName() + " has been registered to observer: ." + observer.toString());
	}

	/**
	 * @see java.util.Observerable
	 * 
	 * @param observer
	 */
	@Override
	public void addNewObserver(Observer observer)
	{
		this.addObserver(observer);
	}
}
