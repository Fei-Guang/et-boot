package com.web.etools;

import java.util.Date;

public class InquireRecord {

	private String subProjectName;
	private String subConName;
	private String subConEmail;
	private Date timeSent;
	private Date timeReceived;
	private Date startTime;
	private Date endTime;
	private Date dueTime;
	private String timeZone;

	public String getSubProjectName() {
		return subProjectName;
	}

	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
	}

	public String getSubConName() {
		return subConName;
	}

	public void setSubConName(String subConName) {
		this.subConName = subConName;
	}

	public String getSubConEmail() {
		return subConEmail;
	}

	public void setSubConEmail(String subConEmail) {
		this.subConEmail = subConEmail;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public Date getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Date timeReceived) {
		this.timeReceived = timeReceived;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
