package org.cmg.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.cmg.data.MonitorStatus;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.util.StringUtils;

/**
 * Implements all motion detection monitor services
 * 
 * @author greg
 *
 */
public class MonitorServiceImpl implements MonitorService {
	
	private static final Logger logger = Logger.getLogger(MonitorServiceImpl.class.getName());
	private DBMaker databaseFactory;
	private DB database;
	private Map<String,String> dbMap;
	
	@PostConstruct
	public void initIt() throws Exception {
		  this.database = databaseFactory.closeOnJvmShutdown().make(); 
		  this.dbMap = database.getTreeMap("monitorMao");
	}

	@Override
	public MonitorData getMonitorData()
	{
		
		// TODO replace dummies with service call
		MonitorData monitorData = new MonitorData();
		DateTime now = new DateTime();
		DateTime startTime = now.minusMonths(2);
		
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy HH:mm:ss.SSS");  // 2014, August 20 22:08:13.123
		 String str = fmt.print(now);
		 
		monitorData.setMostRecentDetectionDate(str);
		monitorData.setNumDetection(3);
		monitorData.setDaysUp(Days.daysBetween(startTime, now).getDays());
		
		String monitorStatus = dbMap.get(MonitorDBKey.MONITOR_STATE.name());
		if (StringUtils.isEmpty(monitorStatus))
		{
			monitorData.setStatus(MonitorStatus.UNKNOWN);
		}
		else
		{
			monitorData.setStatus(MonitorStatus.valueOf(monitorStatus));
		}
		
		
		return monitorData;
	}
	
	@Override
	public void performMonitorControlAction(ControlAction action) throws Exception
	{
		// TODO 
		logger.log(Level.INFO, "Performing control action " + action);
		
		switch(action)
		{
		case ENABLE:
			dbMap.put(MonitorDBKey.MONITOR_STATE.name(), MonitorStatus.ENABLED.name());
			break;
		case DISABLE:
			dbMap.put(MonitorDBKey.MONITOR_STATE.name(), MonitorStatus.DISABLED.name());
			break;
		default:
			throw new Exception("Unsuppported control action: " + action.name());
		}
		database.commit();
	}

	@Override
	public List<String> getLogRecords(Level all) {
		
		logger.log(Level.INFO, "Log records requested.");
		
		return Collections.nCopies(211, new DateTime().toString() + "\n"); 
	}

	public DBMaker getDatabaseFactory() {
		return databaseFactory;
	}

	public void setDatabaseFactory(DBMaker databaseFactory) {
		this.databaseFactory = databaseFactory;
	}
	
}
