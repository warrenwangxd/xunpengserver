package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;

public interface LocationService {
	/**
	 * �ֱ���deviceid��phone��uid����ά�ȴ洢λ����Ϣ���������.
	 * @param deviceId
	 * @param phone
	 * @param locaiton
	 * @return
	 */
	public boolean updateLocation(User user,Location locaiton);
	
	public Location getLatestLocation(String deviceId);
	
	public Location getLatestLocationByPhone(String phone);
	
	/**
	 * ����ʱ�䵹������λ����Ϣ.
	 * @param deviceId
	 * @return
	 */
	public List<Location> getAllLocation(String deviceId);
	
	public List<Location> getAllLocationByPhone(String phone);
	
	/**
	 * ��ѯ���ѵĵ���λ��.
	 * @param phone
	 * @return
	 */
	public List<FriendLocation> getFriendsLocation(String phone);
	
	public List<FriendLocation> getFriendsLocationByUid(String uid);

}
