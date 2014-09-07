package org.cmg.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.cmg.data.MonitorStatus;
import org.cmg.sensor.Sensor;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
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
	
	// Injected database the map is then obtained from it.
	private DB database;
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;
	
	// Injected observers
	private List<Observer> observerList;
	
	private Sensor pirSensor;  // Injected passive infrared sensor that is being monitored
	
	@Value(value="${org.cmg.data.log}")
	private String logFilePath;
	
	@Value(value="${org.cmg.data.log.maxrecs}")
	private static final int MAX_LOG_RECORDS_TO_RETURN = 500; // if prop value set, its value overrides despite this being a final var!
	
	/**
	 * Check to see if DB has existing monitor data, create default entry if not.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
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
		  database.commit();
		  
		  if (isWinOS() == false)  // PIR interface only supported on raspbian
		  {
			  pirSensor.registerForSensorEvents(this.observerList);  // depends on native GPIO libs, so only for raspbian
		  }
		  
	}

	@Override
	public MonitorData getMonitorData()
	{
		
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
			
		DateTime now = new DateTime();
		
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
		
		logger.log(Level.FINE, "Log records requested.");
		
		String msg = "No log file path configured, please check monitor.properties file for org.cmg.data.log";
		if (StringUtils.isEmpty(logFilePath) == false)
		{
			File logFile = new File(logFilePath);
			if (logFile.isFile())
			{
				logger.log(Level.FINE, "Opening log file " + logFilePath);
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
	

	@Override
	public void performReset() {
		// get current monitor data
		MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
		
		monitorData.setMostRecentDetectionDate(null);
		monitorData.setNumDetection(0);
		
		// loop thru photo file list and remove files
		for(File photoFile : monitorData.getPhotoList())
		{
			photoFile.delete();
			logger.log(Level.INFO, "Deleted file: " + photoFile);
		}
		
		monitorData.getPhotoList().clear();  /// remove file path list
		
		database.commit();
	}
	
	/**
	 * 
	 * @return true if running on a Windows OS, false otherwise.
	 */
	static public boolean isWinOS()
	{
		// Check OS - this is done so this webapp can be run on windows platform for testing UI and other non raspi specific functions
		  String OS = System.getProperty("os.name").toLowerCase();
		  
		  return OS.contains("win");
		  
	}

	public Sensor getPirSensor() {
		return pirSensor;
	}

	public void setPirSensor(Sensor pirSensor) {
		this.pirSensor = pirSensor;
	}

	public List<Observer> getObserverList() {
		return observerList;
	}

	public void setObserverList(List<Observer> observerList) {
		this.observerList = observerList;
	}

	public void setDatabase(DB database) {
		this.database = database;
	}
}
