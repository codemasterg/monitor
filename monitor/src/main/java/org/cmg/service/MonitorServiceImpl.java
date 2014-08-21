package org.cmg.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.cmg.data.MonitorData;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Implements all motion detection monitor services
 * 
 * @author greg
 *
 */
public class MonitorServiceImpl implements MonitorService {
	
	private static final Logger logger = Logger.getLogger(MonitorServiceImpl.class.getName());

	@Override
	public MonitorData getMonitorData()
	{
		
		// TODO replace dummies with service call
		MonitorData historyData = new MonitorData();
		DateTime now = new DateTime();
		DateTime startTime = now.minusMonths(2);
		
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy HH:mm:ss.SSS");  // 2014, August 20 22:08:13.123
		 String str = fmt.print(now);
		 
		historyData.setMostRecentDetectionDate(str);
		historyData.setNumDetection(3);
		historyData.setDaysUp(Days.daysBetween(startTime, now).getDays());
		
		return historyData;
	}
	
	@Override
	public void performMonitorControlAction(ControlAction action)
	{
		// TODO 
		logger.log(Level.INFO, "Performing control action " + action);
	}
}
