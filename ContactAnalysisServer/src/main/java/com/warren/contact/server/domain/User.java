package com.warren.contact.server.domain;

import java.util.Date;
import java.util.List;

import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StringUtils;

public class User {
	private String phone;
	private String pwd;
	private String deviceId;
	private String name;
	private String gender;
	private String imgPath;
	private String locationPublic;// 是否公开地址信息.
	private Date registerTime;
	private String uid;
	private UserLoginTypeEnum loginType;
	private String screenName;

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String openId) {
		this.uid = openId;
	}

	public UserLoginTypeEnum getLoginType() {
		return loginType;
	}

	public void setLoginType(UserLoginTypeEnum loginType) {
		this.loginType = loginType;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	private List<Contact> contacts;

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getLocationPublic() {
		return locationPublic;
	}

	public void setLocationPublic(String locationPublic) {
		this.locationPublic = locationPublic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String sex) {
		this.gender = sex;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.deviceId)
				&& StringUtils.isEmpty(this.phone)
				&& StringUtils.isEmpty(this.uid)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("{");
		if (!StringUtils.isEmpty(this.deviceId)) {
			strBuffer.append(JsonUtil.DEVICE_NODE).append(":")
					.append(this.deviceId).append(",");
		}
		if (!StringUtils.isEmpty(this.imgPath)) {
			strBuffer.append(JsonUtil.USER_IMG_NODE).append(":")
					.append(this.imgPath).append(",");
		}
		if (!StringUtils.isEmpty(this.name)) {
			strBuffer.append(JsonUtil.USER_NAME_NODE).append(":")
					.append(this.name).append(",");
		}
		if (!StringUtils.isEmpty(this.locationPublic)) {
			strBuffer.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":")
					.append(this.locationPublic).append(",");
		}
		if (!StringUtils.isEmpty(this.phone)) {
			strBuffer.append(JsonUtil.PHONE_NODE).append(":")
					.append(this.phone).append(",");

		}
		if (!StringUtils.isEmpty(this.pwd)) {
			strBuffer.append(JsonUtil.USER_PWD_NODE).append(":")
					.append(this.pwd).append(",");
		}
		if (!StringUtils.isEmpty(this.gender)) {
			strBuffer.append(JsonUtil.USER_SEX_NODE).append(":")
					.append(this.gender).append(",");
		}
		if (null != this.registerTime) {
			strBuffer.append(JsonUtil.USER_REG_TIME).append(":")
					.append(this.registerTime).append(",");
		}
		if (!StringUtils.isEmpty(this.uid)) {
			strBuffer.append(JsonUtil.UID_NODE).append(":").append(this.uid)
					.append(",");
		}
		if (!StringUtils.isEmpty(this.screenName)) {
			strBuffer.append(JsonUtil.USER_SCREEN_NAME).append(":")
					.append(this.screenName).append(",");
		}
		if (this.contacts != null) {
			strBuffer.append(JsonUtil.CONTACTS_NODE).append(":")
					.append(this.contacts).append(",");
		}
		strBuffer.append("}");
		return strBuffer.toString();

	}
}
