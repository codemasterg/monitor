/**
 * 
 */
package org.cmg.service;

import java.util.List;
import java.util.logging.Level;

import org.cmg.data.MonitorData;

/**
 * @author greg
 *
 */
public interface MonitorService {

	MonitorData getMonitorData();

	void performMonitorControlAction(ControlAction action) throws Exception;

	List<String> getLogRecords(Level all);
	
	void performReset();
	void performSilence();

}
