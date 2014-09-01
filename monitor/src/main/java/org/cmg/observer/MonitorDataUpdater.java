package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.joda.time.DateTime;
import org.mapdb.BTreeMap;
import org.mapdb.DB;

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

			int numDetection = monitorData.getNumDetection();
			monitorData.setNumDetection(++numDetection);

			DateTime now = new DateTime();
			monitorData.setMostRecentDetectionDate(now);

			monitorDataMap.put(MonitorDBKey.MONITOR_DATA, monitorData);
			database.commit();
			logger.log(Level.INFO, "Motion detected, updated DB: " + monitorData);

		}
	}

	public void setDatabase(DB database) {
		this.database = database;
	}
}
