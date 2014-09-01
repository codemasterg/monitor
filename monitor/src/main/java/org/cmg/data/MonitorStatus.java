package org.cmg.data;

/**
 * States that the motion monitor can be in, values are mutually exclusive.
 * 
 * @author greg
 *
 */
public enum MonitorStatus {
	UNKNOWN,
	ENABLED,
	DISABLED,   // admin has disabled notifications, can only be enabled by admin
	FAILED

}
