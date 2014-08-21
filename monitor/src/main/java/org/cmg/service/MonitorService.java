/**
 * 
 */
package org.cmg.service;

import org.cmg.data.MonitorData;

/**
 * @author greg
 *
 */
public interface MonitorService {

	MonitorData getMonitorData();

	void performMonitorControlAction(ControlAction action);

}
