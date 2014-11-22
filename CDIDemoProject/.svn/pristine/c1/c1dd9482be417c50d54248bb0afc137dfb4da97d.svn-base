package com.demo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;

/**
 * Demonstrate event monitoring using CDI Observer
 * 
 * @author greg
 *
 */
public class TimerObserver {
	
	private static Logger logger = Logger.getLogger(TimerObserver.class.getName());
	
	/**
	 * Log the given timeout event
	 * 
	 * @param event any timeout event
	 */
	public void timeoutLogger(@Observes TimeoutEvent event)
	{
		logger.log(Level.INFO, event.getMessage());
	}

}
