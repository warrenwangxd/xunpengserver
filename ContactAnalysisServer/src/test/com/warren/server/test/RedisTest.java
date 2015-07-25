package com.warren.server.test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.warren.contact.server.dal.RedisClient;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.StringUtils;

public class RedisTest {

	public static void main(String[] args) {
		UserService userService = new UserServiceImpl();
		User user = userService.getUserBaseInfoByPhone("18081967335");
		System.out.println(user.getDeviceId()+" "+user.getPwd());

	}
	
	public static void findSameContact() {
		Jedis jedis = new RedisClient().getJedis();
		List<String> warrenList = jedis.hvals("99000628558373");
		List<String> xiaojuList = jedis.hvals("99000520333659");
		Map<String, String> warrenHashMap = jedis.hgetAll("99000628558373");
		uniformPhoneMap(warrenHashMap);
		Map<String, String> xiaojuHashMap = jedis.hgetAll("99000520333659");
		uniformPhoneMap(xiaojuHashMap);

		if (warrenList.retainAll(xiaojuList)) {
			for (String warrenPhone : warrenList) {
				Set<Entry<String, String>> warrenEntrySet = warrenHashMap
						.entrySet();
				Iterator<Entry<String, String>> warrenEntryIterator = warrenEntrySet
						.iterator();
				while (warrenEntryIterator.hasNext()) {
					Entry<String, String> warrenEntry = warrenEntryIterator
							.next();
					if (warrenPhone.equals(warrenEntry.getValue())) {
						break;
					}

				}
			}
		}
		System.out.println();

		if (xiaojuList.retainAll(warrenList)) {
			for (String xiaojuListPhone : xiaojuList) {
				Set<Entry<String, String>> warrenEntrySet = xiaojuHashMap
						.entrySet();
				Iterator<Entry<String, String>> warrenEntryIterator = warrenEntrySet
						.iterator();
				while (warrenEntryIterator.hasNext()) {
					Entry<String, String> warrenEntry = warrenEntryIterator
							.next();
					if (xiaojuListPhone.equals(warrenEntry.getValue())) {
						
						break;
					}

				}
			}
		}
	}

	public static String uniform(String phone) {
		phone = StringUtils.replaceBlank(phone);

		if (phone.startsWith("+86")) {
			phone = phone.substring(3);
		}

		return phone;
	}

	public static void uniformPhoneMap(Map<String, String> phoneMap) {
		Set<Entry<String, String>> warrenEntrySet = phoneMap.entrySet();
		Iterator<Entry<String, String>> warrenEntryIterator = warrenEntrySet
				.iterator();
		while (warrenEntryIterator.hasNext()) {
			Entry<String, String> warrenEntry = warrenEntryIterator.next();
			warrenEntry.setValue(uniform(warrenEntry.getValue()));
		}
	}

}
