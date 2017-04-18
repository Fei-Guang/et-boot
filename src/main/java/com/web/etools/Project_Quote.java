package com.web.etools;

import java.util.Date;

public class Project_Quote {

	private Integer queryprojectid;
	private String name;
	private Date createDate;
	private String mainCon;
	private String contacts;
	private String telephone;
	private String timeZone;
	private Date dueTime;
	private String status;

	public Integer getQueryprojectid() {
		return queryprojectid;
	}

	public void setQueryprojectid(Integer queryprojectid) {
		this.queryprojectid = queryprojectid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMainCon() {
		return mainCon;
	}

	public void setMainCon(String mainCon) {
		this.mainCon = mainCon;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
