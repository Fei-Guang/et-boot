package com.web.etools;

import java.util.Date;

public class QuoteRecord {

	private String subProjectName;
	private String mainConName;
	private String mainConEmail;
	private Date timeSent;
	private Date timeReceived;
	private Date startTime;
	private Date endTime;
	private Date dueTime;
	private String timeZone;
	private Date reviewedAt;

	public String getSubProjectName() {
		return subProjectName;
	}

	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
	}

	public String getMainConName() {
		return mainConName;
	}

	public void setMainConName(String mainConName) {
		this.mainConName = mainConName;
	}

	public String getMainConEmail() {
		return mainConEmail;
	}

	public void setMainConEmail(String mainConEmail) {
		this.mainConEmail = mainConEmail;
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

	public Date getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(Date reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

}
