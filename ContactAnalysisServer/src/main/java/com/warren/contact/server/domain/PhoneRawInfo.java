package com.warren.contact.server.domain;

import java.util.List;

public class PhoneRawInfo {
	private String phoneType;
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String contactOwner) {
		this.deviceId = contactOwner;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	private String deviceId;
	private  List<Contact> contacts;
	
	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("deviceId:\"").append(deviceId+"\",contacts:{\n");
		for(Contact contact: contacts) {
			strBuffer.append(contact.toString());
			
		}
		strBuffer.append("}");
		return strBuffer.toString();
	}

}
