package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.warren.contact.server.config.Configuration;
import com.warren.contact.server.dal.BaseRedisDAO;
import com.warren.contact.server.dal.RedisClient;
import com.warren.contact.server.dal.RedisExecutor;
import com.warren.contact.server.dal.RedisKeyEnum;
import com.warren.contact.server.dal.RedisTemplate;
import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StringUtils;

public class LocationServiceImpl extends BaseRedisDAO implements
		LocationService {
	private static Logger logger = Logger.getLogger(LocationServiceImpl.class);

	private static UserService userService = new UserServiceImpl();
	private static ContactService contactService = new ContactServiceImpl();

	private RedisTemplate redisTemplate = ServiceFactory.getRedisTemplate();

	private static String PROVICE_KEY = "provice";
	private static String CITY_KEY = "city";
	private static String COUNTRY_KEY = "country";
	private static String COMMUNITY_KEY = "community";
	private static String FULL_ADDRESS = "fullAddress";
	private static String LATITUDE = "latitude";
	private static String LOGITUDE = "logitude";
	private static String LOC_TIME = "locTime";

	@Override
	public boolean updateLocation(final User user, final Location location) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				long currentTime = System.currentTimeMillis();
				Map<String, String> locationMap = new HashMap<String, String>();
				if (!StringUtils.isEmpty(location.getLatitude())) {
					locationMap.put(LATITUDE, location.getLatitude());
				}
				if (!StringUtils.isEmpty(location.getLogitude())) {
					locationMap.put(LOGITUDE, location.getLogitude());
				}
				if (!StringUtils.isEmpty(location.getCity())) {
					locationMap.put(CITY_KEY, location.getCity());
				}
				if (!StringUtils.isEmpty(location.getCommunity())) {
					locationMap.put(COMMUNITY_KEY, location.getCommunity());
				}
				if (!StringUtils.isEmpty(location.getCountry())) {
					locationMap.put(COUNTRY_KEY, location.getCountry());
				}
				if (!StringUtils.isEmpty(location.getProvince())) {
					locationMap.put(PROVICE_KEY, location.getProvince());
				}

				locationMap.put(LOC_TIME, String.valueOf(currentTime));
				// 以deviceId维度存储数据.
				jedis.hmset(
						RedisKeyEnum.LOCATION_DEVICEID_KEY + user.getDeviceId()
								+ currentTime, locationMap);
				logger.info("设备" + user.getDeviceId() + "|" + currentTime
						+ ", 更新地址信息成功:" + location.toString());
				// 删除超过最大存储条数的地址，按先进先出原则.
				Set<String> locationSet = jedis
						.keys(RedisKeyEnum.LOCATION_DEVICEID_KEY
								+ user.getDeviceId() + "*");
				int locationSetSize = locationSet.size();
				if (locationSetSize > Location.MAX_SAVE_SIZE) {
					List<String> sortedLocationList = new ArrayList<String>(
							locationSet);
					Collections.sort(sortedLocationList);
					for (int i = 0; i < locationSetSize
							- Location.MAX_SAVE_SIZE; i++)
						jedis.del(sortedLocationList.get(i));
				}

				storePhoneKeyLocation(user, locationMap, location, currentTime);
				storeUidKeyLocation(user, locationMap, location, currentTime);

				return true;
			}
		});

	}

	private void storePhoneKeyLocation(final User user,
			final Map<String, String> locationMap, final Location location,
			final long currentTime) {
		redisTemplate.execute(null, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				// 若phone不为空，则以phone维度存储一份数据.
				if (!StringUtils.isEmpty(user.getPhone())) {
					jedis.hmset(
							RedisKeyEnum.LOCATION_PHONE_KEY + user.getPhone()
									+ currentTime, locationMap);
					logger.info("手机" + user.getPhone() + "|" + currentTime
							+ ", 更新地址信息成功:" + location.toString());
					// 删除超过最大存储条数的地址，按先进先出原则.
					Set<String> phoneLocationSet = jedis
							.keys(RedisKeyEnum.LOCATION_PHONE_KEY
									+ user.getPhone() + "*");
					int phoneLocationSetSize = phoneLocationSet.size();
					if (phoneLocationSetSize > Location.MAX_SAVE_SIZE) {
						List<String> sortedLocationList = new ArrayList<String>(
								phoneLocationSet);
						Collections.sort(sortedLocationList);
						for (int i = 0; i < phoneLocationSetSize
								- Location.MAX_SAVE_SIZE; i++) {
							jedis.del(sortedLocationList.get(i));
							logger.info("删除地址信息：" + sortedLocationList.get(i));
						}
					}

				}
				return null;
			}
		});

	}

	private void storeUidKeyLocation(final User user,
			final Map<String, String> locationMap, final Location location,
			final long currentTime) {
		redisTemplate.execute(null, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				// 若uid不为空，则以uid维度存储一份数据.
				if (!StringUtils.isEmpty(user.getUid())) {
					jedis.hmset(RedisKeyEnum.LOCATION_UID_KEY + user.getUid()
							+ currentTime, locationMap);
					logger.info("手机" + user.getUid() + "|" + currentTime
							+ ", 更新地址信息成功:" + location.toString());
					// 删除超过最大存储条数的地址，按先进先出原则.
					Set<String> uidLocationSet = jedis
							.keys(RedisKeyEnum.LOCATION_UID_KEY + user.getUid()
									+ "*");
					int uidLocationSetSize = uidLocationSet.size();
					if (uidLocationSetSize > Location.MAX_SAVE_SIZE) {
						List<String> sortedLocationList = new ArrayList<String>(
								uidLocationSet);
						Collections.sort(sortedLocationList);
						for (int i = 0; i < uidLocationSetSize
								- Location.MAX_SAVE_SIZE; i++) {
							jedis.del(sortedLocationList.get(i));
							logger.info("删除地址信息：" + sortedLocationList.get(i));
						}
					}

				}

				return null;
			}
		});

	}

	@Override
	public Location getLatestLocation(final String deviceId) {
		Location emptyLocation = new Location();
		return (Location) redisTemplate.execute(emptyLocation,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						Set<String> locationSet = jedis
								.keys(RedisKeyEnum.LOCATION_DEVICEID_KEY
										+ deviceId + "*");
						if (locationSet == null | locationSet.size() == 0) {
							return null;
						}
						List<String> sortedLocationList = new ArrayList<String>(
								locationSet);

						// 倒序排列
						Collections.sort(sortedLocationList,
								new Comparator<String>() {

									@Override
									public int compare(String o1, String o2) {
										return o2.compareTo(o1);
									}
								});
						Collections.sort(sortedLocationList);
						Map<String, String> locationMap = jedis
								.hgetAll(sortedLocationList.get(0));
						Location location = new Location();
						location.setCity(locationMap.get(CITY_KEY));
						location.setCommunity(locationMap.get(COMMUNITY_KEY));
						location.setCountry(locationMap.get(COUNTRY_KEY));
						location.setFullAddress(locationMap.get(FULL_ADDRESS));
						location.setProvince(locationMap.get(PROVICE_KEY));
						location.setLatitude(locationMap.get(LATITUDE));
						location.setLogitude(locationMap.get(LOGITUDE));
						if (!StringUtils.isEmpty(locationMap.get(LOC_TIME))) {
							location.setLocTime(new Date(Long
									.valueOf(locationMap.get(LOC_TIME))));
						}
						return location;
					}
				});

	}

	@Override
	public List<Location> getAllLocation(final String deviceId) {
		List<Location> emptyList = new ArrayList<Location>();
		return (List<Location>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<Location> locationList = new ArrayList<Location>();
						Set<String> locationSet = jedis
								.keys(RedisKeyEnum.LOCATION_DEVICEID_KEY
										+ deviceId + "*");
						if (locationSet == null | locationSet.size() == 0) {
							return null;
						}
						List<String> sortedLocationKeyList = new ArrayList<String>(
								locationSet);
						// 倒序排列
						Collections.sort(sortedLocationKeyList,
								new Comparator<String>() {

									@Override
									public int compare(String o1, String o2) {
										return o2.compareTo(o1);
									}
								});
						for (int i = 0; i < sortedLocationKeyList.size(); i++) {
							Map<String, String> locationMap = jedis
									.hgetAll(sortedLocationKeyList.get(i));
							Location location = new Location();
							location.setCity(locationMap.get(CITY_KEY));
							location.setCommunity(locationMap
									.get(COMMUNITY_KEY));
							location.setCountry(locationMap.get(COUNTRY_KEY));
							location.setFullAddress(locationMap
									.get(FULL_ADDRESS));
							location.setProvince(locationMap.get(PROVICE_KEY));
							location.setLatitude(locationMap.get(LATITUDE));
							location.setLogitude(locationMap.get(LOGITUDE));
							if (!StringUtils.isEmpty(locationMap.get(LOC_TIME))) {
								location.setLocTime(new Date(Long
										.valueOf(locationMap.get(LOC_TIME))));
							}

							locationList.add(location);
						}
						return locationList;
					}
				});

	}

	@Override
	public List<FriendLocation> getFriendsLocation(final String phone) {
		logger.info("根据手机号" + phone + "查询朋友位置信息");
		List<FriendLocation> emptyList = new ArrayList<FriendLocation>();
		return (List<FriendLocation>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<FriendLocation> friendLocationList = new ArrayList<FriendLocation>();
						List<Contact> contactList = contactService
								.getAllContactsByPhone(phone);
						if (contactList.size() == 0) {
							return null;
						}
						for (int i = 0; i < contactList.size(); i++) {
							Contact contact = contactList.get(i);
							User friend = userService
									.getUserBaseInfoByPhone(contact
											.getPhoneNumber());
							if (friend.isEmpty()) {
								continue;
							}
							Location location = getLatestLocationByPhone(contact
									.getPhoneNumber());
							if (location.isEmpty()) {
								continue;
							}

							logger.info("全局位置信息开关："
									+ Configuration.getConfiguration()
											.getLocationPublicSwitch());
							if (Configuration.LOCATION_PUBLIC_SWITCH_ON
									.equals(Configuration.getConfiguration()
											.getLocationPublicSwitch())) {
								FriendLocation friendLocation = new FriendLocation();
								friendLocation.setUser(friend);
								// 把用户的名字替换为联系人中的名字，方便用户识别.
								friend.setName(contact.getName());
								friendLocation.setLocation(location);
								friendLocationList.add(friendLocation);
							} else if (!JsonUtil.RESULT_NO.equals(friend
									.getLocationPublic())) {
								FriendLocation friendLocation = new FriendLocation();
								friendLocation.setUser(friend);
								// 把用户的名字替换为联系人中的名字，方便用户识别.
								friend.setName(contact.getName());
								friendLocation.setLocation(location);
								friendLocationList.add(friendLocation);
							}
						}
						logger.info(phone + "的朋友最近的位置数据：" + friendLocationList);
						return friendLocationList;
					}
				});

	}

	@Override
	public Location getLatestLocationByPhone(final String phone) {
		Location emptyLocation = new Location();
		return (Location) redisTemplate.execute(emptyLocation,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						Set<String> locationSet = jedis
								.keys(RedisKeyEnum.LOCATION_PHONE_KEY + phone
										+ "*");
						if (locationSet == null | locationSet.size() == 0) {
							return null;
						}
						List<String> sortedLocationList = new ArrayList<String>(
								locationSet);
						// 倒序排列
						Collections.sort(sortedLocationList,
								new Comparator<String>() {

									@Override
									public int compare(String o1, String o2) {
										return o2.compareTo(o1);
									}
								});
						Map<String, String> locationMap = jedis
								.hgetAll(sortedLocationList.get(0));
						Location location = new Location();
						location.setCity(locationMap.get(CITY_KEY));
						location.setCommunity(locationMap.get(COMMUNITY_KEY));
						location.setCountry(locationMap.get(COUNTRY_KEY));
						location.setFullAddress(locationMap.get(FULL_ADDRESS));
						location.setProvince(locationMap.get(PROVICE_KEY));
						location.setLatitude(locationMap.get(LATITUDE));
						location.setLogitude(locationMap.get(LOGITUDE));
						if (!StringUtils.isEmpty(locationMap.get(LOC_TIME))) {
							location.setLocTime(new Date(Long
									.valueOf(locationMap.get(LOC_TIME))));
						}
						return location;
					}
				});

	}

	@Override
	public List<Location> getAllLocationByPhone(final String phone) {
		final List<Location> locationList = new ArrayList<Location>();
		return (List<Location>) redisTemplate.execute(locationList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						Set<String> locationSet = jedis
								.keys(RedisKeyEnum.LOCATION_PHONE_KEY + phone
										+ "*");
						if (locationSet == null | locationSet.size() == 0) {
							return null;
						}
						List<String> sortedLocationKeyList = new ArrayList<String>(
								locationSet);
						// 倒序排列
						Collections.sort(sortedLocationKeyList,
								new Comparator<String>() {

									@Override
									public int compare(String o1, String o2) {
										return o2.compareTo(o1);
									}
								});
						for (int i = 0; i < sortedLocationKeyList.size(); i++) {
							Map<String, String> locationMap = jedis
									.hgetAll(sortedLocationKeyList.get(i));
							Location location = new Location();
							location.setCity(locationMap.get(CITY_KEY));
							location.setCommunity(locationMap
									.get(COMMUNITY_KEY));
							location.setCountry(locationMap.get(COUNTRY_KEY));
							location.setFullAddress(locationMap
									.get(FULL_ADDRESS));
							location.setProvince(locationMap.get(PROVICE_KEY));
							location.setLatitude(locationMap.get(LATITUDE));
							location.setLogitude(locationMap.get(LOGITUDE));
							if (!StringUtils.isEmpty(locationMap.get(LOC_TIME))) {
								location.setLocTime(new Date(Long
										.valueOf(locationMap.get(LOC_TIME))));
							}

							locationList.add(location);
						}
						return locationList;
					}
				});

	}

	@Override
	public List<FriendLocation> getFriendsLocationByUid(final String uid) {
		logger.info("根据uid" + uid + "查询朋友位置信息");
		List<FriendLocation> emptyList = new ArrayList<FriendLocation>();
		return (List<FriendLocation>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<FriendLocation> friendLocationList = new ArrayList<FriendLocation>();
						List<Contact> contactList = contactService
								.getAllContactsByUid(uid);
						if (contactList.size() == 0) {
							return null;
						}
						for (int i = 0; i < contactList.size(); i++) {
							Contact contact = contactList.get(i);
							User friend = userService
									.getUserBaseInfoByPhone(contact
											.getPhoneNumber());
							if (friend.isEmpty()) {
								continue;
							}
							Location location = getLatestLocationByPhone(contact
									.getPhoneNumber());
							if (location.isEmpty()) {
								continue;
							}
							logger.info("全局位置信息开关："
									+ Configuration.getConfiguration()
											.getLocationPublicSwitch());
							if (Configuration.LOCATION_PUBLIC_SWITCH_ON
									.equals(Configuration.getConfiguration()
											.getLocationPublicSwitch())) {
								FriendLocation friendLocation = new FriendLocation();
								friendLocation.setUser(friend);
								// 把用户的名字替换为联系人中的名字，方便用户识别.
								friend.setName(contact.getName());
								friendLocation.setLocation(location);
								friendLocationList.add(friendLocation);
							} else if (!JsonUtil.RESULT_NO.equals(friend
									.getLocationPublic())) {
								FriendLocation friendLocation = new FriendLocation();
								friendLocation.setUser(friend);
								// 把用户的名字替换为联系人中的名字，方便用户识别.
								friend.setName(contact.getName());
								friendLocation.setLocation(location);
								friendLocationList.add(friendLocation);
							}
						}
						logger.info(uid + "的朋友最近的位置数据：" + friendLocationList);
						return friendLocationList;
					}
				});
	}
}
