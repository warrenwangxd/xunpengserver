package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.domain.PhoneUpdateInfo;

public interface PhoneService {
	public boolean updatePhoneInfo(PhoneUpdateInfo phone);
	/**
	 * 根据deviceId查询按时间倒序排列的变更历史.
	 * @param deviceId
	 * @return
	 */
	public List<PhoneUpdateInfo> getPhoneUpdateInfoList(String deviceId);
	
	public PhoneUpdateInfo getLatestPhonInfo(String deviceId);
	/**
	 * 根据时间倒序排列所有设备变更情况.
	 * @return
	 */
	public List<PhoneUpdateInfo> getAllPhoneUpdateInfoListByTime();

}
