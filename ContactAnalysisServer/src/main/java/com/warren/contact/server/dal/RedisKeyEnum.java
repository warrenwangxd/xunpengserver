package com.warren.contact.server.dal;

/**
 * Redis存储的key前缀枚举，用于区分不同类型的存储数据.
 * 
 * @author dong.wangxd
 * 
 */
public enum RedisKeyEnum {
	USER_BASE_INFO("USER_BASE_INFO", "用于存储以phone为key的用户注册信息的数据"),
	USER_BASE_INFO_UID_KEY("USER_UID_KEY","存储以uid为key的用户注册信息"),
	CONTACT_DEVICE_PHONE_KEY("CONTACT_PHONE_KEY","用于存储以deviceId作为key,map中以phone作为key的联系人数据"),
	CONTACT_DEVICE_NAME_KEY("CONTACT_NAME_KEY","用于存储以deviceId作为key,map中以姓名作为key的联系人数据"),
	CONTACT_USER_ID_KEY("CONTACT_USER_ID_KEY","用于存储用户id作为key的联系人数据"),
	CONTACT_USER_PHONE_KEY("CONTACT_USER_PHONE_KEY","用于存储以用户手机号为key的联系人数据"),
	CONTACT_USER_SAME_CONTACT_KEY("CONTACT_USER_SAME_CONTACT_KEY","用于存储以uid为key的用户的联系人中共同的朋友数据"),
	CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY("CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY","用于存储以uid为key的联系人共同朋友数据变更记录"),
	USER_REGISTER_VERIFY_CODE("USER_REG_VERIFY_CODE","用于存储以phone为key的用户注册验证码数据"),
	LOCATION_DEVICEID_KEY("LOCATION_DEVICEID_KEY","用于存储以deviceId为key的位置信息"),
	LOCATION_PHONE_KEY("LOCATION_PHONE_KEY","用于存储以phone为key的位置信息"),
	LOCATION_UID_KEY("LOCATION_UID_KEY","用于存储以UID为key的位置信息"),
	PHONE_UPDATE_INFO_KEY("PHONE_UPDATE_INFO_KEY","用以存储以deviceId为key的手机设备更新信息");
	
	private String value;
	private String dec;

	RedisKeyEnum(String value, String dec) {
		this.value = value;
		this.dec = dec;
	}

	public String getValue() {
		return this.value;
	}

}
