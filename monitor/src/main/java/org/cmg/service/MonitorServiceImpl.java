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
	private Map<MonitorDBKey,MonitorData> monitorDataMap;
	
	@PostConstruct
	public void initIt() throws Exception {
		  this.database = databaseFactory.closeOnJvmShutdown().make(); 
		  this.monitorDataMap = database.getTreeMap("monitorDataMap");
		  this.monitorDataMap.put(MonitorDBKey.MONITOR_DATA, new MonitorData(new DateTime()));  // initialize with an empty record
		  this.database.commit();
	}

	@Override
	public MonitorData getMonitorData()
	{
		
		// TODO replace dummies with service call
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		DateTime now = new DateTime();
		
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy HH:mm:ss.SSS");  // 2014, August 20 22:08:13.123
		 String str = fmt.print(now);
		 
		monitorData.setMostRecentDetectionDate(str);
		monitorData.setNumDetection(3);
		monitorData.setDaysUp(Days.daysBetween(monitorData.getStartTime(), now).getDays());
		
		if (monitorData == null || monitorData.getStatus() == null)
		{
			monitorData.setStatus(MonitorStatus.UNKNOWN);
		}
		else
		{
			monitorData.setStatus(monitorData.getStatus());
		}
			
		return monitorData;
	}
	
	@Override
	public void performMonitorControlAction(ControlAction action) throws Exception
	{
		// TODO 
		logger.log(Level.INFO, "Performing control action " + action);
		
		// get current monitor data
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		
		// now update the status based on action requested.  
		switch(action)
		{
		case ENABLE:
			monitorData.setStatus(MonitorStatus.ENABLED);
			break;
		case DISABLE:
			monitorData.setStatus(MonitorStatus.DISABLED);			
			break;
		default:
			throw new Exception("Unsuppported control action: " + action.name());
		}
		monitorDataMap.put(MonitorDBKey.MONITOR_DATA, monitorData);
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
