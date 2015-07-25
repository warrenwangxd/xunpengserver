package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;

public interface LocationService {
	/**
	 * 分别以deviceid，phone，uid三个维度存储位置信息，方便检索.
	 * @param deviceId
	 * @param phone
	 * @param locaiton
	 * @return
	 */
	public boolean updateLocation(User user,Location locaiton);
	
	public Location getLatestLocation(String deviceId);
	
	public Location getLatestLocationByPhone(String phone);
	
	/**
	 * 按照时间倒序排列位置信息.
	 * @param deviceId
	 * @return
	 */
	public List<Location> getAllLocation(String deviceId);
	
	public List<Location> getAllLocationByPhone(String phone);
	
	/**
	 * 查询朋友的地理位置.
	 * @param phone
	 * @return
	 */
	public List<FriendLocation> getFriendsLocation(String phone);
	
	public List<FriendLocation> getFriendsLocationByUid(String uid);

}
