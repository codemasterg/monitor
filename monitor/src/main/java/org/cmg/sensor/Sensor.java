package org.cmg.sensor;

import java.util.List;
import java.util.Observer;

/**
 * Generic interface for sensors supported on the Raspberry Pi
 * 
 * @author greg
 *
 */
public interface Sensor {

	/**
	 * Register the given list of observers for a sensor.  Observers are updated when sensor is not in MonitorStatus.DISABLED state.
	 * 
	 * @param observerList
	 */
	void registerForSensorEvents(List<Observer> observerList);

}
