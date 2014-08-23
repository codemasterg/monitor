package org.cmg.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * Implements all motion detection monitor services
 * 
 * @author greg
 *
 */
public class MonitorServiceImpl implements MonitorService {
	
	private static final Logger logger = Logger.getLogger(MonitorServiceImpl.class.getName());
	
	// Injected DBMaker, the others are obtained from it.
	private DBMaker databaseFactory;
	private DB database;
	private Map<MonitorDBKey,MonitorData> monitorDataMap;
	
	@Value(value="${org.cmg.data.log}")
	private String logFilePath;
	
	@Value(value="${org.cmg.data.log.maxrecs}")
	private static final int MAX_LOG_RECORDS_TO_RETURN = 500; // if prop set, its value overrides despite this being a final var!
	
	/**
	 * Although the DB factory is injected and its factory method is called by the Spring Container,
	 * some additional static methods need to be called from the factory so PostConstruct is used.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		  this.database = databaseFactory.closeOnJvmShutdown().make(); 
		  this.monitorDataMap = database.getTreeMap("monitorDataMap");
		  MonitorData monitorData = this.monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		  if (monitorData == null)
		  {
			  this.monitorDataMap.put(MonitorDBKey.MONITOR_DATA, new MonitorData(new DateTime()));  // initialize with an empty record
		  }
		  else
		  {
			  monitorData.setStartTime(new DateTime());
		  }
		  this.database.commit();
	}

	@Override
	public MonitorData getMonitorData()
	{
		
		// TODO replace dummies with service call
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		DateTime now = new DateTime();
		
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss.SSS");  // August 20, 2014 22:08:13.123
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
		
		String msg = "No log file path configured, please check monitor.properties file for org.cmg.data.log";
		if (StringUtils.isEmpty(logFilePath) == false)
		{
			File logFile = new File(logFilePath);
			if (logFile.isFile())
			{
				logger.log(Level.INFO, "Opening log file " + logFilePath);
				try 
				{
					FileReader fr = new FileReader(logFile);
					BufferedReader br = new BufferedReader(fr);
					
					String logRecord = null;
					List<String> logRecordList = new ArrayList<String>();
					int recordCount = 0;
					while((logRecord = br.readLine()) != null && ++recordCount < MAX_LOG_RECORDS_TO_RETURN)
					{
						logRecordList.add(String.format("%s%n", logRecord));  // add OS neutral newline char at end
					}
					return logRecordList;
				} 
				catch (IOException e)
				{
					logger.log(Level.WARNING, e.getMessage(), e);
					msg = e.getMessage();
				}
			}
			else
			{
				msg = "Cannot access log file: " + logFilePath;
			}
		}
		
		return Collections.nCopies(1, msg); 
		
	}

	public DBMaker getDatabaseFactory() {
		return databaseFactory;
	}

	public void setDatabaseFactory(DBMaker databaseFactory) {
		this.databaseFactory = databaseFactory;
	}
	
}