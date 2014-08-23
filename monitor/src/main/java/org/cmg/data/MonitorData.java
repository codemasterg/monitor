package org.cmg.data;

import java.io.Serializable;

import org.joda.time.DateTime;

public class MonitorData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2594098402066991346L;
	
	private int daysUp;
	private int numDetection;
	private String mostRecentDetectionDate;
	private MonitorStatus status;
	

	public int getDaysUp() {
		return daysUp;
	}
	public void setDaysUp(int daysUp) {
		this.daysUp = daysUp;
	}
	public int getNumDetection() {
		return numDetection;
	}
	public void setNumDetection(int numDetection) {
		this.numDetection = numDetection;
	}
	public String getMostRecentDetectionDate() {
		return mostRecentDetectionDate;
	}
	public void setMostRecentDetectionDate(String mostRecentDetectionDate) {
		this.mostRecentDetectionDate = mostRecentDetectionDate;
	}
	public MonitorStatus getStatus() {
		return status;
	}
	public void setStatus(MonitorStatus status) {
		this.status = status;
	}
	
	
}
