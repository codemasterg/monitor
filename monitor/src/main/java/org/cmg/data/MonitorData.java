package org.cmg.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MonitorData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2594098402066991346L;
	
	private int daysUp;
	private int numDetection;
	private DateTime mostRecentDetectionDate;
	private MonitorStatus status;
	private DateTime startTime;
	private List<File> photoList;
	
	public MonitorData(DateTime startTime)
	{
		this.startTime = startTime;
		this.photoList = new ArrayList<File>();
	}
	
	public String getFormattedMostRecentDetectionDate()
	{
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss.SSS");  // August 20, 2014 22:08:13.123
		return fmt.print(this.mostRecentDetectionDate);
	}

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
	public DateTime getMostRecentDetectionDate() {
		return mostRecentDetectionDate;
	}
	public void setMostRecentDetectionDate(DateTime mostRecentDetectionDate) {
		this.mostRecentDetectionDate = mostRecentDetectionDate;
	}
	public MonitorStatus getStatus() {
		return status;
	}
	public void setStatus(MonitorStatus status) {
		this.status = status;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public List<File> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<File> photoList) {
		this.photoList = photoList;
	}

	@Override
	public String toString() {
		return "MonitorData [daysUp=" + daysUp + ", numDetection="
				+ numDetection + ", mostRecentDetectionDate="
				+ mostRecentDetectionDate + ", status=" + status
				+ ", startTime=" + startTime + ", photoList=" + photoList + "]";
	}

	
}
