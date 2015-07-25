package com.warren.contact.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.warren.contact.server.util.StringUtils;

public class PhoneUpdateInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5009340143209842270L;
	private String deviceId;
	private String phoneType;
	private String phoneNo;
	private List<Contact> contactList;
	private Date updateTime;

	public List<Contact> getContactList() {
		return contactList;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void updatePhoneInfo(PhoneUpdateInfo updateInfo) {
		if (updateInfo.getContactList() != null
				&& updateInfo.getContactList().size() > 0) {
			this.contactList = updateInfo.getContactList();
		}
		if (!StringUtils.isEmpty(updateInfo.getPhoneNo())) {
			this.phoneNo = updateInfo.getPhoneNo();
		}
		if (!StringUtils.isEmpty(getPhoneType())) {
			this.phoneType = updateInfo.getPhoneType();
		}
	}

	public boolean equals(PhoneUpdateInfo updateInfo) {
		if(updateInfo.getContactList()==null && this.contactList!=null ||updateInfo.getContactList()!=null&&this.contactList==null) {
                      return false;			
		}
		if(!updateInfo.getContactList().equals(this.contactList)) {
			 return false;
		}
		if(!StringUtils.equals(updateInfo.getDeviceId(), this.deviceId)) {
			return false;
		}
		if(!StringUtils.equals(updateInfo.getPhoneNo(), this.phoneNo)) {
			return false;
		}
		if(!StringUtils.equals(updateInfo.getPhoneType(), this.phoneType)) {
			return false;
		}
		return true;
	}

	public String toString() {
		return this.deviceId + "|" + this.phoneType + "|" + this.phoneNo+"|"+this.updateTime;
	}

}
