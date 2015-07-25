package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.domain.PhoneUpdateInfo;

public interface PhoneService {
	public boolean updatePhoneInfo(PhoneUpdateInfo phone);
	/**
	 * ����deviceId��ѯ��ʱ�䵹�����еı����ʷ.
	 * @param deviceId
	 * @return
	 */
	public List<PhoneUpdateInfo> getPhoneUpdateInfoList(String deviceId);
	
	public PhoneUpdateInfo getLatestPhonInfo(String deviceId);
	/**
	 * ����ʱ�䵹�����������豸������.
	 * @return
	 */
	public List<PhoneUpdateInfo> getAllPhoneUpdateInfoListByTime();

}
