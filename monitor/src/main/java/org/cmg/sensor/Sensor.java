package org.cmg.sensor;

import java.util.Observer;

/**
 * Generic interface for sensors supported on the Raspberry Pi
 * 
 * @author greg
 *
 */
public interface Sensor {

	void addObserver(Observer observer);

	void registerForSensorEvents(Observer observer);
	

}
