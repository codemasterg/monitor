package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.service.MonitorServiceImpl;
import org.springframework.beans.factory.annotation.Value;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class implements the Observer interface, it's responsible for closing a relay for a configurable duration.
 * 
 * @author greg
 *
 */
public class RelayActivator extends Thread implements Observer {

	private static final Logger logger = Logger.getLogger(RelayActivator.class.getName());
	
	// Injected
	private Pin relayPin;	// illuminated when sensor goes high
	
	@Value(value="${org.cmg.relay.duration}")
	private String  closesureDurationString;
	private int closureDurationInMS;  // derived from injected duration string, in milliseconds
	
	private static boolean relayClosed;
	
	// GPIO members
	private GpioController gpio;

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		
		this.closureDurationInMS = Integer.parseInt(closesureDurationString) * 1000;  // secs to ms
		
		// create gpio controller
		if (MonitorServiceImpl.isWinOS() == false)
		{
			this.gpio = GpioFactory.getInstance();
		}
	}

	/**
	 * Spawns a thread that controls a pin defined by the property org.cmg.relay.pin for the duration defined by org.cmg.relay.duration.
	 */
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		// if relay already closed, do nothing on HIGH pin state
		if (event.getState() == PinState.HIGH && relayClosed == false)
		{
			try 
			{				
				// Close the relay for the configured duration.  A thread is used so the Observable is not blocked when notifying other observers
				this.start();	
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Close a relay for configurable duration.
	 */
	@Override
	public void run()
	{
		relayClosed = true;
		logger.log(Level.INFO, "Closing relay connected to " + this.relayPin.getName() + " for " + this.closesureDurationString + " seconds.");
		
		// set pin state so that the relay closes
		GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(relayPin, "Relay Pin", PinState.HIGH);
		
		// sleep until it's time to re-open the relay
		try {
			Thread.sleep(this.closureDurationInMS);
		} 
		catch (InterruptedException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		finally
		{
			pin.toggle();	// close relay
			
			// reset relay state
			relayClosed = false;
			logger.log(Level.INFO, "Opening relay connected to " + this.relayPin.getName());
		}
	}

	public void setRelayPin(Pin relayPin) {
		this.relayPin = relayPin;
	}
}
