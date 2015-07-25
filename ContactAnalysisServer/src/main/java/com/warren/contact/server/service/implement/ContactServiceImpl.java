package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.warren.contact.server.dal.BaseRedisDAO;
import com.warren.contact.server.dal.RedisClient;
import com.warren.contact.server.dal.RedisExecutor;
import com.warren.contact.server.dal.RedisKeyEnum;
import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.ContactChangedLog;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.PhoneUpdateInfo;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.PhoneService;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.util.StringUtils;

@Service("contactService")
public class ContactServiceImpl extends BaseRedisDAO implements ContactService {
	Logger logger = Logger.getLogger(ContactServiceImpl.class);
	Logger performanceLogger = Logger
			.getLogger(Constants.PERFORMANCE_LOGGER_NAME);

	private UserService userService = ServiceFactory.getUserService();
	private PhoneService phoneService = ServiceFactory.getPhoneService();

	@Override
	public int getContactsSize(final String owner) {
		return (Integer) redisTemplate.execute(-1, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {

				Map<String, String> contactsMap = jedis
						.hgetAll(RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY
								.getValue() + owner);
				return contactsMap.size();

			}
		});

	}

	@Override
	public boolean saveAndUpdateContacts(final PhoneRawInfo fullContact) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				String owner = fullContact.getDeviceId();
				if (logger.isInfoEnabled()) {
					logger.info("存储的设备id:" + owner + "的手机联系人");
				}
				Map<String, String> nameKeyMap = new HashMap<String, String>();
				Map<String, String> phoneKeyMap = new HashMap<String, String>();
				for (int i = 0; i < fullContact.getContacts().size(); i++) {
					Contact contact = fullContact.getContacts().get(i);
					nameKeyMap.put(contact.getName(), contact.getPhoneNumber());
					phoneKeyMap.put(contact.getPhoneNumber(), contact.getName());

				}

				// 以name为key存储
				jedis.hmset(RedisKeyEnum.CONTACT_DEVICE_NAME_KEY.getValue()
						+ owner, nameKeyMap);
				// 以phone为key存储
				jedis.hmset(RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY.getValue()
						+ owner, phoneKeyMap);
				// 更新phone变更记录(key为当前时间，value为变更内容)
				PhoneUpdateInfo updateInfo = new PhoneUpdateInfo();
				updateInfo.setContactList(fullContact.getContacts());
				updateInfo.setDeviceId(owner);
				phoneService.updatePhoneInfo(updateInfo);

				logger.info("name key的个数："
						+ jedis.hgetAll(
								RedisKeyEnum.CONTACT_DEVICE_NAME_KEY.getValue()
										+ owner).size());
				logger.info("phone key的个数："
						+ jedis.hgetAll(
								RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY
										.getValue() + owner).size());
				return true;
			}
		});

	}

	public List<Contact> getAllContactsByOwnerNameKey(final String owner) {

		List<Contact> emptyList = new ArrayList<Contact>();
		return (List<Contact>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<Contact> contacts = new ArrayList<Contact>();

						Map<String, String> contactsMap = jedis
								.hgetAll(RedisKeyEnum.CONTACT_DEVICE_NAME_KEY
										.getValue() + owner);

						Set<String> keys = contactsMap.keySet();
						Iterator iter = keys.iterator();
						while (iter.hasNext()) {
							Contact contact = new Contact();
							String name = (String) iter.next();
							contact.setName(name);
							contact.setPhoneNumber(contactsMap.get(name));
							contacts.add(contact);
						}

						return contacts;
					}
				});

	}

	@Override
	public List<Contact> getAllContactsByOwnerPhoneKey(final String owner) {
		List<Contact> emptyList = new ArrayList<Contact>();
		return (List<Contact>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<Contact> contacts = new ArrayList<Contact>();

						Map<String, String> contactsMap = jedis
								.hgetAll(RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY
										.getValue() + owner);

						Set<String> keys = contactsMap.keySet();
						Iterator iter = keys.iterator();
						while (iter.hasNext()) {
							Contact contact = new Contact();
							String phone = (String) iter.next();
							contact.setName(contactsMap.get(phone));
							contact.setPhoneNumber(phone);
							contacts.add(contact);
						}

						return contacts;
					}
				});

	}

	@Override
	public List<PhoneRawInfo> getAllPhoneContact() {
		List<PhoneRawInfo> emptyList = new ArrayList<PhoneRawInfo>();
		return (List<PhoneRawInfo>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						Set<String> keySet = jedis
								.keys(RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY
										.getValue() + "*");
						List<PhoneRawInfo> fullContactList = new ArrayList<PhoneRawInfo>();
						Iterator<String> keyIter = keySet.iterator();
						while (keyIter.hasNext()) {
							String key = (String) keyIter.next();
							PhoneRawInfo fullContact = new PhoneRawInfo();
							fullContact.setDeviceId(key
									.substring(RedisKeyEnum.CONTACT_DEVICE_PHONE_KEY
											.getValue().length()));
							Map<String, String> valueMap = jedis.hgetAll(key);
							List<Contact> contactList = new ArrayList<Contact>();
							Set<String> phoneKeySet = valueMap.keySet();
							Iterator<String> phoneKeyIter = phoneKeySet
									.iterator();
							while (phoneKeyIter.hasNext()) {
								String phone = (String) phoneKeyIter.next();
								Contact contact = new Contact();
								contact.setPhoneNumber(phone);
								contact.setName(valueMap.get(phone));
								contactList.add(contact);
							}
							fullContact.setContacts(contactList);
							fullContactList.add(fullContact);

						}
						return fullContactList;
					}
				});

	}

	@Override
	public Map<String, List<Contact>> getSameContact(String phone) {
		long startTime = System.currentTimeMillis();
		Map<String, List<Contact>> sameContactMap = new HashMap<String, List<Contact>>();
		User user1 = userService.getUserBaseInfoByPhone(phone);
		String deviceId1 = user1.getDeviceId();
		List<Contact> contactList = getAllContactsByOwnerPhoneKey(deviceId1);
		for (Contact contact : contactList) {
			User user = userService.getUserBaseInfoByPhone(contact
					.getPhoneNumber());
			if (user != null) {
				List<Contact> sameContactList = compareContact(contactList,
						user);
				if (sameContactList.size() != 0) {
					String contactFullInfo = contact.getName() + "("
							+ contact.getPhoneNumber() + ")";
					sameContactMap.put(contactFullInfo, sameContactList);
				}
			}
		}
		printLog(phone, sameContactMap);
		performanceLogger.info("getSameContactByPhone:"
				+ (System.currentTimeMillis() - startTime) + " ms");
		return sameContactMap;
	}

	private List<Contact> compareContact(List<Contact> phone1Contacts,
			User user2) {

		List<Contact> phone2Contacts = getAllContactsByOwnerPhoneKey(user2
				.getDeviceId());
		List<Contact> sameContacts = new ArrayList<Contact>();
		for (Contact contact1 : phone1Contacts) {
			for (Contact contact2 : phone2Contacts) {
				if (contact1.getPhoneNumber().equals(contact2.getPhoneNumber())) {
					sameContacts.add(contact1);
					phone2Contacts.remove(contact2);
					break;
				}
			}
		}
		// phone1Contacts.retainAll(phone2Contacts);
		return sameContacts;

	}

	@Override
	public boolean saveOrUpdateUserContacts(final User user) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {
				List<Contact> contacts = user.getContacts();
				String uid = user.getUid();

				Map<String, String> phoneKeyMap = new HashMap<String, String>();
				for (int i = 0; i < contacts.size(); i++) {
					Contact contact = contacts.get(i);
					phoneKeyMap.put(contact.getPhoneNumber(), contact.getName());
				}
				// 如果uid为空，则将phone设置为uid.冗余存储一份数据.
				if (!StringUtils.isEmpty(user.getPhone())
						&& StringUtils.isEmpty(uid)) {
					uid = user.getPhone();
				}
				if (!StringUtils.isEmpty(uid)) {
					jedis.hmset(RedisKeyEnum.CONTACT_USER_ID_KEY + uid,
							phoneKeyMap);
				}
				// 存储以phone为key的联系人数据.
				String phone = user.getPhone();
				if (!StringUtils.isEmpty(phone)) {
					jedis.hmset(RedisKeyEnum.CONTACT_USER_PHONE_KEY + phone,
							phoneKeyMap);
				}
				return true;
			}
		});
	}

	@Override
	public List<Contact> getAllContactsByPhone(final String phone) {
		List<Contact> emptyList = new ArrayList<Contact>();
		return (List<Contact>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<Contact> contacts = new ArrayList<Contact>();

						Map<String, String> contactsMap = jedis
								.hgetAll(RedisKeyEnum.CONTACT_USER_PHONE_KEY
										+ phone);

						Set<String> keys = contactsMap.keySet();
						Iterator iter = keys.iterator();
						while (iter.hasNext()) {
							Contact contact = new Contact();
							String phone = (String) iter.next();
							contact.setName(contactsMap.get(phone));
							contact.setPhoneNumber(phone);
							contacts.add(contact);
						}
						return contacts;
					}
				});
	}

	@Override
	public List<Contact> getAllContactsByUid(final String uid) {
		List<Contact> emptyList = new ArrayList<Contact>();
		return (List<Contact>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<Contact> contacts = new ArrayList<Contact>();

						Map<String, String> contactsMap = jedis
								.hgetAll(RedisKeyEnum.CONTACT_USER_ID_KEY + uid);

						Set<String> keys = contactsMap.keySet();
						Iterator iter = keys.iterator();
						while (iter.hasNext()) {
							Contact contact = new Contact();
							String phone = (String) iter.next();
							contact.setName(contactsMap.get(phone));
							contact.setPhoneNumber(phone);
							contacts.add(contact);
						}
						return contacts;
					}
				});
	}

	@Override
	public Map<String, List<Contact>> getSameContactByUidForTask(String uid) {
		long startTime = System.currentTimeMillis();
		Map<String, List<Contact>> sameContactMap = new HashMap<String, List<Contact>>();

		List<Contact> contactList = getAllContactsByUid(uid);
		for (Contact contact : contactList) {
			User user = userService.getUserBaseInfoByPhone(contact
					.getPhoneNumber());
			if (user != null) {
				List<Contact> sameContactList = compareContact(contactList,
						user);
				if (sameContactList.size() != 0) {
					String contactFullInfo = contact.getName() + "-"
							+ contact.getPhoneNumber();
					sameContactMap.put(contactFullInfo, sameContactList);
				}
			}
		}
		performanceLogger.info("getSameContactByUidForTask:"
				+ (System.currentTimeMillis() - startTime) + " ms");
		return sameContactMap;
	}

	@Override
	public List<User> getAllUserContactByPhone() {
		List<User> emptyList = new ArrayList<User>();
		return (List<User>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<User> userList = new ArrayList<User>();
						Set<String> keySet = jedis
								.keys(RedisKeyEnum.CONTACT_USER_PHONE_KEY + "*");
						Iterator iter = keySet.iterator();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							Map<String, String> valueMap = jedis.hgetAll(key);
							List<Contact> contactList = new ArrayList<Contact>();
							Set<String> phoneKeySet = valueMap.keySet();
							Iterator<String> phoneKeyIter = phoneKeySet
									.iterator();
							while (phoneKeyIter.hasNext()) {
								String phone = (String) phoneKeyIter.next();
								Contact contact = new Contact();
								contact.setPhoneNumber(phone);
								contact.setName(valueMap.get(phone));
								contactList.add(contact);
							}
							// 获取phone值
							String phone = key
									.substring(RedisKeyEnum.CONTACT_USER_PHONE_KEY
											.getValue().length());
							User user = new User();
							user.setContacts(contactList);
							user.setPhone(phone);
							userList.add(user);

						}
						return userList;
					}
				});
	}

	@Override
	public List<User> getAllUserContactByUid() {
		List<User> emptyList = new ArrayList<User>();
		return (List<User>) redisTemplate.execute(emptyList,
				new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						List<User> userList = new ArrayList<User>();
						Set<String> keySet = jedis
								.keys(RedisKeyEnum.CONTACT_USER_ID_KEY + "*");
						Iterator iter = keySet.iterator();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							Map<String, String> valueMap = jedis.hgetAll(key);
							List<Contact> contactList = new ArrayList<Contact>();
							Set<String> phoneKeySet = valueMap.keySet();
							Iterator<String> phoneKeyIter = phoneKeySet
									.iterator();
							while (phoneKeyIter.hasNext()) {
								String phone = (String) phoneKeyIter.next();
								Contact contact = new Contact();
								contact.setPhoneNumber(phone);
								contact.setName(valueMap.get(phone));
								contactList.add(contact);
							}
							// 获取phone值
							String uid = key
									.substring(RedisKeyEnum.CONTACT_USER_ID_KEY
											.getValue().length());
							User user = new User();
							user.setContacts(contactList);
							user.setUid(uid);
							userList.add(user);

						}
						return userList;
					}
				});
	}

	@Override
	public boolean saveOrUpdateUserSameContacts(final String uid,
			final Map<String, List<Contact>> sameContacts) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {

				Set<String> keySet = sameContacts.keySet();
				Iterator<String> keyIter = keySet.iterator();
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					List<Contact> contacts = sameContacts.get(key);
					Map<String, String> contactMap = new HashMap<String, String>();
					for (Contact contact : contacts) {
						contactMap.put(contact.getPhoneNumber(),
								contact.getName());
					}
					// 以name为key存储
					jedis.hmset(
							RedisKeyEnum.CONTACT_USER_SAME_CONTACT_KEY
									.getValue()
									+ Constants.JEDIS_KEY_SEPERATOR
									+ uid + Constants.JEDIS_KEY_SEPERATOR + key,
							contactMap);
				}

				return true;
			}
		});

	}

	@Override
	public Map<String, List<Contact>> getSameContactByUid(final String uid) {

		Map<String, List<Contact>> emptyContactMap = new HashMap<String, List<Contact>>();
		return (Map<String, List<Contact>>) redisTemplate.execute(
				emptyContactMap, new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						long startTime = System.currentTimeMillis();
						Map<String, List<Contact>> userSameContactMap = new HashMap<String, List<Contact>>();
						Set<String> keySet = jedis
								.keys(RedisKeyEnum.CONTACT_USER_SAME_CONTACT_KEY
										+ Constants.JEDIS_KEY_SEPERATOR
										+ uid
										+ "*");
						Iterator<String> keyIter = keySet.iterator();
						while (keyIter.hasNext()) {
							List<Contact> sameContactList = new ArrayList<Contact>();
							String key = (String) keyIter.next();
							Map<String, String> sameContactMap = jedis
									.hgetAll(key);

							Set<String> phonekeySet = sameContactMap.keySet();
							Iterator<String> phoneKeyIter = phonekeySet
									.iterator();
							while (phoneKeyIter.hasNext()) {
								String phone = (String) phoneKeyIter.next();
								Contact contact = new Contact();
								contact.setName(sameContactMap.get(phone));
								contact.setPhoneNumber(phone);
								sameContactList.add(contact);
							}
							String[] keyArray = key
									.split(Constants.JEDIS_KEY_SEPERATOR);
							userSameContactMap
									.put(keyArray[2], sameContactList);
						}
						performanceLogger.info("getSameContactByUid:"
								+ (System.currentTimeMillis() - startTime)
								+ " ms");
						// 打印日志
						printLog(uid, userSameContactMap);

						return userSameContactMap;
					}

				});

	}

	private void printLog(String uid,
			Map<String, List<Contact>> userSameContactMap) {
		Set<String> keyLogSet = userSameContactMap.keySet();
		Iterator<String> keyLogIter = keyLogSet.iterator();
		while (keyLogIter.hasNext()) {
			String key = (String) keyLogIter.next();

			List<Contact> contacts = userSameContactMap.get(key);
			logger.info(uid + "与" + key + "共同的联系人有：" + contacts.size() + "个:");
			logger.info(contacts);
		}

	}

	@Override
	public boolean updateUserSameContactChangedLog(final String uid,
			final Date time, final boolean noticed) {
		return (Boolean) redisTemplate.execute(false, new RedisExecutor() {

			@Override
			public Object execute(Jedis jedis) {

				// 以name为key存储
				jedis.hset(
						RedisKeyEnum.CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY
								.getValue()
								+ Constants.JEDIS_KEY_SEPERATOR
								+ uid, String.valueOf(time.getTime()), String
								.valueOf(noticed));

				return true;
			}
		});

	}

	@Override
	public List<ContactChangedLog> getUserSameContactChangedLog(final String uid) {
		List<ContactChangedLog> contactChangedLogs = new ArrayList<ContactChangedLog>();
		final Map<String, String> changedLogMap = (Map<String, String>) redisTemplate
				.execute(new HashMap<String, String>(), new RedisExecutor() {

					@Override
					public Object execute(Jedis jedis) {
						Map<String, String> changedLog = jedis
								.hgetAll(RedisKeyEnum.CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY
										.getValue()
										+ Constants.JEDIS_KEY_SEPERATOR + uid);
						return changedLog;

					}
				});
		Set<String> timeSet = changedLogMap.keySet();
		List<String> timeSortList = new ArrayList<String>(timeSet);
		Collections.sort(timeSortList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {

				return (int) (Long.valueOf(o2) - Long.valueOf(o1));
			}

		});
		for (int i = 0; i < timeSortList.size(); i++) {
			ContactChangedLog changeLog = new ContactChangedLog();
			changeLog
					.setChangedTime(new Date(Long.valueOf(timeSortList.get(i))));
			changeLog.setNoticed(Boolean.valueOf(changedLogMap.get(timeSortList
					.get(i))));
			changeLog.setUid(uid);
			contactChangedLogs.add(changeLog);
		}
		return contactChangedLogs;
	}
}
