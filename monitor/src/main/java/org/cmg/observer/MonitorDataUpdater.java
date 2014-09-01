package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.cmg.data.MonitorStatus;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.springframework.beans.factory.annotation.Value;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class is responsible for sending emails to a distribution list defined by property org.cmg.notify.email
 * @author greg
 *
 */
public class MonitorDataUpdater implements Observer {

	private static final Logger logger = Logger.getLogger(MonitorDataUpdater.class.getName());
	
	// Injected database the map are obtained from it.
	private DB database;
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;
	
	@Value(value="${org.cmg.sensor.rearm}")
	int notificationThresholdInSeconds;
	
	@PostConstruct
	public void init() throws Exception {
		  this.monitorDataMap = database.getTreeMap("monitorDataMap");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;
		
		if (event.getState() == PinState.HIGH)
		{
			MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
			
			DateTime now = new DateTime();
			int secondsSinceMostRecentUpdate = Seconds.secondsBetween(monitorData.getMostRecentDetectionDate(), now).getSeconds();
			
			// only update if time since most recent updates exceeds configurable threshold.
			// this is done to avoid constant updates resulting from repeated movement detected by the sensor within the threshold.
			if (secondsSinceMostRecentUpdate > notificationThresholdInSeconds)
			{
				int numDetection = monitorData.getNumDetection();
				monitorData.setNumDetection(++numDetection);

				monitorData.setMostRecentDetectionDate(now);
				monitorData.setStatus(MonitorStatus.TRIPPED);

				monitorDataMap.put(MonitorDBKey.MONITOR_DATA, monitorData);
				database.commit();
				logger.log(Level.INFO, "Motion detected, updated DB: " + monitorData);
			}
		}
	}

	public void setDatabase(DB database) {
		this.database = database;
	}
}
