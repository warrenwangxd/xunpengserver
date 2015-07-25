package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.warren.contact.server.dal.BaseRedisDAO;
import com.warren.contact.server.dal.RedisClient;
import com.warren.contact.server.dal.RedisExecutor;
import com.warren.contact.server.dal.RedisKeyEnum;
import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.integration.SmsUtil;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.util.StringUtils;
@Service("userService")
public class UserServiceImpl extends BaseRedisDAO implements UserService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	private static Logger userLogger = Logger.getLogger(Constants.USER_LOGGER_NAME);

	private String PWD_KEY = "pwd";
	private String DEVICE_ID_KEY = "deviceId";
	private String USER_MAME_KEY = "userName";
	private String USER_SEX = "sex";
	private String USER_IMG = "userImg";
	private String LOCATION_PUBLIC = "locationPublic";
	private String REGISTER_TIME = "registerTime";
	private String UID_KEY = "uid";
	private String LOGIN_TYPE = "loginType";
	private String SCREEN_NAME = "screenName";
	private String PHONE_KEY = "phone";
	@Autowired
	private SmsUtil smsUtil;

	@Override
	public boolean saveOrUpdateUserInfo(final User user) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				Map<String, String> userInfoMap = null;
				if (!StringUtils.isEmpty(user.getPhone())) {
					userInfoMap = jedis
							.hgetAll(RedisKeyEnum.USER_BASE_INFO
									+ user.getPhone());
					if (null == userInfoMap) {
						userInfoMap = new HashMap<String, String>();
						userInfoMap.put(REGISTER_TIME, String
								.valueOf(System.currentTimeMillis()));
					}
					fillUserInfoMap(user, userInfoMap);
					jedis.hmset(
							RedisKeyEnum.USER_BASE_INFO
									+ user.getPhone(), userInfoMap);
					userLogger.info("更新phone-key用户信息:" + user);
				}
				//如果phone不为空，且uid为空的情况下，将phone作为uid冗余 存储一份数据.
				if(!StringUtils.isEmpty(user.getPhone())&&StringUtils.isEmpty(user.getUid())) {
					user.setUid(user.getPhone());
				}
				if (!StringUtils.isEmpty(user.getUid())) {
					userInfoMap = jedis
							.hgetAll(RedisKeyEnum.USER_BASE_INFO_UID_KEY.getValue()
									+ user.getUid());
					if (null == userInfoMap) {
						userInfoMap = new HashMap<String, String>();
						userInfoMap.put(REGISTER_TIME, String
								.valueOf(System.currentTimeMillis()));
					}

					fillUserInfoMap(user, userInfoMap);
					jedis.hmset(RedisKeyEnum.USER_BASE_INFO_UID_KEY.getValue()
							+ user.getUid(), userInfoMap);
					userLogger.info("uid-key用户信息:" + user);

				}

				return true;
			}
		});

	}

	private void fillUserInfoMap(User user, Map<String, String> userInfoMap) {

		if (StringUtils.isEmpty(userInfoMap.get(REGISTER_TIME))) {
			userInfoMap.put(REGISTER_TIME,
					String.valueOf(System.currentTimeMillis()));
		}
		if (!StringUtils.isEmpty(user.getPhone())) {
			userInfoMap.put(PHONE_KEY, user.getPhone());
		}
		if (!StringUtils.isEmpty(user.getDeviceId())) {
			userInfoMap.put(DEVICE_ID_KEY, user.getDeviceId());
		}
		if (!StringUtils.isEmpty(user.getPwd())) {
			userInfoMap.put(PWD_KEY, user.getPwd());
		}

		if (!StringUtils.isEmpty(user.getName())) {
			userInfoMap.put(USER_MAME_KEY, user.getName());
		}
		if (!StringUtils.isEmpty(user.getImgPath())) {
			userInfoMap.put(USER_IMG, user.getImgPath());
		}
		if (!StringUtils.isEmpty(user.getGender())) {
			userInfoMap.put(USER_SEX, user.getGender());
		}
		if (!StringUtils.isEmpty(user.getLocationPublic())) {
			userInfoMap.put(LOCATION_PUBLIC, user.getLocationPublic());
		}
		if (!StringUtils.isEmpty(user.getUid())) {
			userInfoMap.put(UID_KEY, user.getUid());
		}
		/*
		 * if (!StringUtils.isEmpty(user.getLoginType().getValue())) {
		 * userInfoMap.put(LOGIN_TYPE, user.getLoginType().getValue()); }
		 */
		if (!StringUtils.isEmpty(user.getScreenName())) {
			userInfoMap.put(SCREEN_NAME, user.getScreenName());
		}
	}

	@Override
	public User getUserBaseInfoByPhone(final String phone) {
		User emptyUser = new User();
		return (User) redisTemplate.execute(emptyUser, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				String key = RedisKeyEnum.USER_BASE_INFO + phone;
				Map<String, String> userInfoMap = jedis.hgetAll(key);
				if (userInfoMap == null || userInfoMap.isEmpty()) {
					return null;
				}
				User user = new User();
				fillUserInfo(user, userInfoMap);
				user.setPhone(phone);
				logger.info("获取用户信息:" + user);
				return user;

			}
		});

	}

	private void fillUserInfo(User user, Map<String, String> userInfoMap) {
		user.setUid(userInfoMap.get(UID_KEY));
		user.setPhone(userInfoMap.get(PHONE_KEY));
		user.setScreenName(userInfoMap.get(SCREEN_NAME));
		user.setDeviceId(userInfoMap.get(DEVICE_ID_KEY));
		user.setPwd(userInfoMap.get(PWD_KEY));
		user.setImgPath(userInfoMap.get(USER_IMG));
		user.setName(userInfoMap.get(USER_MAME_KEY));
		user.setGender(userInfoMap.get(USER_SEX));
		user.setLocationPublic(userInfoMap.get(LOCATION_PUBLIC));
		if (null != userInfoMap.get(REGISTER_TIME)) {
			user.setRegisterTime(new Date(Long.valueOf(userInfoMap
					.get(REGISTER_TIME))));
		}
	}

	@Override
	public void saveVerifyCode(final String phone, final String verifyCode) {
		redisTemplate.execute(null, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				long currentTimes = System.currentTimeMillis();
				String verifyCodeWithTime = verifyCode
						+ String.valueOf(currentTimes);
				jedis.lpush(RedisKeyEnum.USER_REGISTER_VERIFY_CODE + phone,
						verifyCodeWithTime);
				return true;
			}
		});

	}

	@Override
	public boolean needSendVerifyCode(final String phone) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				String verifyCodeWithTime = jedis.lindex(
						RedisKeyEnum.USER_REGISTER_VERIFY_CODE + phone,
						0);
				if (StringUtils.isEmpty(verifyCodeWithTime)) {
					return true;
				}
				long verifyCodeSentTime = Long
						.valueOf(verifyCodeWithTime.substring(4));
				long currentTime = System.currentTimeMillis();
				long intervalTime = currentTime - verifyCodeSentTime;
				logger.info(phone + "发送验证码的时间间隔为:" + intervalTime);
				if (intervalTime > smsUtil.getSmsIntervalSentTime()) {
					return true;
				} else {
					return false;
				}
			}
		});

	}

	@Override
	public boolean checkVerifyCode(final String phone,
			final String inputVerifyCode) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				String verifyCodeWithTime = jedis.lindex(
						RedisKeyEnum.USER_REGISTER_VERIFY_CODE + phone,
						0);
				logger.info("verifyCodeWithTime:"
						+ verifyCodeWithTime);
				if (StringUtils.isEmpty(verifyCodeWithTime)) {
					return false;
				}
				String verifyCode = verifyCodeWithTime.substring(0, 4);
				logger.info(phone + " verifyCode:" + verifyCode);
				if (verifyCode.equals(inputVerifyCode)) {
					return true;
				} else {
					return false;
				}
			}
		});

	}

	@Override
	public boolean isRegistered(String phone) {
		User user = getUserBaseInfoByPhone(phone);
		if (!user.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<User> getAllUserByPhoneKey() {
		List<User> emptyList = new ArrayList<User>();
		return (List<User>) redisTemplate.execute(emptyList, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				List<User> userList = new ArrayList<User>();
				Set<String> userKeys = jedis
						.keys(RedisKeyEnum.USER_BASE_INFO + "*");
				Iterator<String> userIterator = userKeys.iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					User user = getUserBaseInfoByPhone(key
							.substring(RedisKeyEnum.USER_BASE_INFO
									.getValue().length()));
					userList.add(user);
				}
				return userList;
			}
		});

	}

	@Override
	public boolean deleteUser(final String phone) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				jedis.del(RedisKeyEnum.USER_BASE_INFO + phone);
				// 删除相关的地理位置数据
				Set<String> locationKey = jedis
						.hkeys(RedisKeyEnum.LOCATION_PHONE_KEY + phone
								+ "*");
				List<String> keys = new ArrayList<String>(locationKey);
				for (String key : keys) {
					jedis.del(key);
				}
				userLogger.info("删除用户:" + phone);
				return true;
			}
		});
	}

	@Override
	public User getUserBaseInfoByUid(final String uid) {

		User emptyUser = new User();
		return (User) redisTemplate.execute(emptyUser, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {

				Map<String, String> userInfoMap = jedis
						.hgetAll(RedisKeyEnum.USER_BASE_INFO_UID_KEY.getValue()
								+ uid);
				logger.info("uid:" + uid + "获取用户信息:" + userInfoMap);
				if (userInfoMap == null || userInfoMap.isEmpty()) {
					return null;
				}
				User user = new User();
				fillUserInfo(user, userInfoMap);
				user.setUid(uid);
				logger.info("uid:" + uid + "获取用户信息:" + user);
				return user;
			}
		});

	}

	@Override
	public List<User> getAllUserByUidKey() {
		List<User> emptyList = new ArrayList<User>();
		return (List<User>) redisTemplate.execute(emptyList, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				List<User> userList = new ArrayList<User>();
				Set<String> userKeys = jedis
						.keys(RedisKeyEnum.USER_BASE_INFO_UID_KEY.getValue() + "*");
				Iterator<String> userIterator = userKeys.iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					User user = getUserBaseInfoByUid(key
							.substring(RedisKeyEnum.USER_BASE_INFO_UID_KEY.getValue().length()));
					userList.add(user);
				}
				return userList;
			}
		});
	}
}
