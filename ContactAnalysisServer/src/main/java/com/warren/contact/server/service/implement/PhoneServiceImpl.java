package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.warren.contact.server.dal.BaseRedisDAO;
import com.warren.contact.server.dal.RedisClient;
import com.warren.contact.server.dal.RedisExecutor;
import com.warren.contact.server.dal.RedisKeyEnum;
import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.PhoneUpdateInfo;
import com.warren.contact.server.service.PhoneService;
import com.warren.contact.server.util.SerializeUtil;

public class PhoneServiceImpl extends BaseRedisDAO implements PhoneService {
	private static Logger logger = Logger.getLogger(Constants.PHONE_LOGGER_NAME);

	@Override
	public boolean updatePhoneInfo(final PhoneUpdateInfo phone) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {
			
			@Override
			public Object execute(Jedis jedis) {
				Date currentTime = new Date();
				PhoneUpdateInfo latestUpdateInfo = getLatestPhonInfo(phone
						.getDeviceId());
				if (latestUpdateInfo != null) {
					latestUpdateInfo.updatePhoneInfo(phone);
					latestUpdateInfo.setUpdateTime(currentTime);
					jedis.lpush((RedisKeyEnum.PHONE_UPDATE_INFO_KEY + phone
							.getDeviceId()).getBytes(), SerializeUtil
							.serialize(latestUpdateInfo));
					logger.info("设备id" + latestUpdateInfo.getDeviceId() + "成功更新设备信息:"
							+ latestUpdateInfo.toString());
				} else {
					phone.setUpdateTime(currentTime);
					jedis.lpush((RedisKeyEnum.PHONE_UPDATE_INFO_KEY + phone
							.getDeviceId()).getBytes(), SerializeUtil.serialize(phone));
					logger.info("设备id" + phone.getDeviceId() + "成功更新设备信息:"
							+ phone.toString());
				}
				return true;
			}
		});
		

	}

	@Override
	public List<PhoneUpdateInfo> getPhoneUpdateInfoList(final String deviceId) {
		List<PhoneUpdateInfo> emptyList = new ArrayList<PhoneUpdateInfo>();
		return (List<PhoneUpdateInfo>)redisTemplate.execute(emptyList, new RedisExecutor() {
			
			@Override
			public Object execute(Jedis jedis) {
				List<PhoneUpdateInfo> phoneUpdateList = new ArrayList<PhoneUpdateInfo>();
				for (int i = 0; i < jedis
						.llen((RedisKeyEnum.PHONE_UPDATE_INFO_KEY + deviceId)
								.getBytes()); i++) {
					byte[] phoneUpdateInfo = jedis.lindex(
							(RedisKeyEnum.PHONE_UPDATE_INFO_KEY + deviceId).getBytes(),
							i);
					PhoneUpdateInfo updateInfo = (PhoneUpdateInfo) SerializeUtil
							.unserialize(phoneUpdateInfo);
					phoneUpdateList.add(updateInfo);
				}
				return phoneUpdateList;
			}
		});
		

	}

	public PhoneUpdateInfo getLatestPhonInfo(final String deviceId) {
		PhoneUpdateInfo emptyPhoneUpdateInfo = new PhoneUpdateInfo();
		return (PhoneUpdateInfo) redisTemplate.execute(emptyPhoneUpdateInfo, new RedisExecutor() {
			
			@Override
			public Object execute(Jedis jedis) {
				long length = jedis
						.llen((RedisKeyEnum.PHONE_UPDATE_INFO_KEY + deviceId)
								.getBytes());
				if (length > 0) {
					byte[] phoneUpdateInfo = jedis.lindex(
							(RedisKeyEnum.PHONE_UPDATE_INFO_KEY + deviceId).getBytes(),
							0);
					return (PhoneUpdateInfo) SerializeUtil.unserialize(phoneUpdateInfo);
				} else {
					return null;
				}
			}
		});
		
	}

	@Override
	public List<PhoneUpdateInfo> getAllPhoneUpdateInfoListByTime() {
		List<PhoneUpdateInfo> emptyList = new ArrayList<PhoneUpdateInfo>();
		return (List<PhoneUpdateInfo>)redisTemplate.execute(emptyList, new RedisExecutor() {
			
			@Override
			public Object execute(Jedis jedis) {
				List<PhoneUpdateInfo> phoneUpdateInfos = new ArrayList<PhoneUpdateInfo>();
				Set<byte[]> keysSet = jedis
						.keys((RedisKeyEnum.PHONE_UPDATE_INFO_KEY + "*").getBytes());
				Iterator<byte[]> keysIter = keysSet.iterator();
				while (keysIter.hasNext()) {
					byte[] key = keysIter.next();
					PhoneUpdateInfo updateInfo = (PhoneUpdateInfo) SerializeUtil
							.unserialize(jedis.lindex(key, 0));
					phoneUpdateInfos.add(updateInfo);
				}
				Collections.sort(phoneUpdateInfos, new Comparator<PhoneUpdateInfo>() {
					public int compare(PhoneUpdateInfo o1, PhoneUpdateInfo o2) {
						if (o1.getUpdateTime() ==o2.getUpdateTime() && o1.getUpdateTime()== null) {
							return 0;
						} else if (o1.getUpdateTime() == null) {
							return 1;
						} else if(o2.getUpdateTime()==null) {
							return -1;
						}else if (o1.getUpdateTime().getTime() < o2.getUpdateTime()
								.getTime()) {
							return 1;
						}  else if(o1.getUpdateTime().getTime() == o2.getUpdateTime()
								.getTime()){
							return 0;
						} else {
							return -1;
						}

					}

				});
				return phoneUpdateInfos;
			}
		});
		
	}
}
