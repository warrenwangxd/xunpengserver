package com.warren.contact.server.domain;

import java.util.Date;

import com.warren.contact.server.util.StringUtils;

public class Location {
	private String fullAddress;
	private String country;
	private String province;
	private String city;
	private String community;
	private String latitude;
	private String logitude;
	private Date locTime;//增加冗余字段，用于减少查询。

	public static int MAX_SAVE_SIZE=1000;
	
	

	public Date getLocTime() {
		return locTime;
	}

	public void setLocTime(Date locTime) {
		this.locTime = locTime;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLogitude() {
		return logitude;
	}

	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
	public boolean isEmpty() {
		if(StringUtils.isEmpty(this.latitude)||StringUtils.isEmpty(this.logitude)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.latitude).append("|").append(this.logitude).append("|")
				.append(this.fullAddress).append("|").append(this.country)
				.append("|").append(this.province).append("|")
				.append(this.city).append("|").append(this.community);
		return str.toString();
	}

}
