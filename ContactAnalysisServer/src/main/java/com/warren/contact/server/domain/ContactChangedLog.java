package com.warren.contact.server.domain;

import java.util.Date;

public class ContactChangedLog {
	private String uid;
	private Date changedTime;
	private boolean noticed;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Date getChangedTime() {
		return changedTime;
	}
	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}
	public boolean isNoticed() {
		return noticed;
	}
	public void setNoticed(boolean noticed) {
		this.noticed = noticed;
	}
	

}
