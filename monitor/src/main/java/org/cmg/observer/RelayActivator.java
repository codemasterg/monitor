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
	private Pin relayPin;	// set high when sensor goes high so that a normally open relay is closed
	
	@Value(value="${org.cmg.relay.duration}")
	private String  closesureDurationString;
	private int closureDurationInMS;  // derived from injected duration string, in milliseconds
	
	// GPIO members
	private GpioController gpio;
	private GpioPinDigitalOutput relay; 

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
			relay = gpio.provisionDigitalOutputPin(relayPin, "Relay Pin", PinState.LOW);
		}
	}

	/**
	 * Spawns a thread that controls a pin defined by the property org.cmg.relay.pin for the duration defined by org.cmg.relay.duration.
	 */
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		// if relay already closed, do nothing on HIGH pin state
		if (event.getState() == PinState.HIGH)
		{
			try 
			{				
				// Close the relay for the configured duration.  A thread is used so the Observable is not blocked when notifying other observers
				Activator relay = new Activator();
				relay.start();
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Control pin state associated with a relay. 
	 * 
	 * @author greg
	 *
	 */
	private class Activator extends Thread
	{
		/**
		 * Close a relay for configurable duration.
		 */
		@Override
		public void run()
		{
			logger.log(Level.INFO, "Closing relay connected to " + relay.getName() + " for " + closesureDurationString + " seconds.");
			
			// set pin state so that the relay closes
			relay.high();
			
			// sleep until it's time to re-open the relay
			try {
				Thread.sleep(closureDurationInMS);
			} 
			catch (InterruptedException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			finally
			{
				relay.low();	// open relay
				logger.log(Level.INFO, "Opening relay connected to " + relay.getName());
			}
		}	
	}

	public void setRelayPin(Pin relayPin) {
		this.relayPin = relayPin;
	}
}
