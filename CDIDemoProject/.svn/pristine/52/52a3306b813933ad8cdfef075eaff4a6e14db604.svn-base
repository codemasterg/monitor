package com.demo;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Demonstrate JEE6 timer service.
 * Demonstrate CDI Events
 * 
 * @author greg
 *
 */

@Stateless
public class CountdownTimer {
	
	@Resource private TimerService timerService;	// inject JEE6 timer service
	
	@Inject Event<TimeoutEvent> timeoutEvent;		// inject bean used to signal a timeout event
	
	Date currentTimeDate = new Date();
	
	/**
	 * Start a single timeout for the given duration.
	 * 
	 * @param durationInSeconds
	 */
	public void startCountdown(int durationInSeconds)
	{
		timerService.createSingleActionTimer(durationInSeconds * 1000, new TimerConfig());
		
	}
    
	/**
	 * Handle a timeout for this EJB.
	 * 
	 * @param timer instance that has timed out.
	 */
	@Timeout
	public void timeout(Timer timer) {
		
		TimeoutEvent event = new TimeoutEvent();
		event.setMessage("Timeout at " + new Date() );
	    timeoutEvent.fire(event);	// Signal a timeout event, notice how no reference to observers of this event exists!
	}
}
