package com.warren.contact.server.dal;

/**
 * Redis�洢��keyǰ׺ö�٣��������ֲ�ͬ���͵Ĵ洢����.
 * 
 * @author dong.wangxd
 * 
 */
public enum RedisKeyEnum {
	USER_BASE_INFO("USER_BASE_INFO", "���ڴ洢��phoneΪkey���û�ע����Ϣ������"),
	USER_BASE_INFO_UID_KEY("USER_UID_KEY","�洢��uidΪkey���û�ע����Ϣ"),
	CONTACT_DEVICE_PHONE_KEY("CONTACT_PHONE_KEY","���ڴ洢��deviceId��Ϊkey,map����phone��Ϊkey����ϵ������"),
	CONTACT_DEVICE_NAME_KEY("CONTACT_NAME_KEY","���ڴ洢��deviceId��Ϊkey,map����������Ϊkey����ϵ������"),
	CONTACT_USER_ID_KEY("CONTACT_USER_ID_KEY","���ڴ洢�û�id��Ϊkey����ϵ������"),
	CONTACT_USER_PHONE_KEY("CONTACT_USER_PHONE_KEY","���ڴ洢���û��ֻ���Ϊkey����ϵ������"),
	CONTACT_USER_SAME_CONTACT_KEY("CONTACT_USER_SAME_CONTACT_KEY","���ڴ洢��uidΪkey���û�����ϵ���й�ͬ����������"),
	CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY("CONTACT_USER_SAME_CONTACT_CHANGED_LOG_KEY","���ڴ洢��uidΪkey����ϵ�˹�ͬ�������ݱ����¼"),
	USER_REGISTER_VERIFY_CODE("USER_REG_VERIFY_CODE","���ڴ洢��phoneΪkey���û�ע����֤������"),
	LOCATION_DEVICEID_KEY("LOCATION_DEVICEID_KEY","���ڴ洢��deviceIdΪkey��λ����Ϣ"),
	LOCATION_PHONE_KEY("LOCATION_PHONE_KEY","���ڴ洢��phoneΪkey��λ����Ϣ"),
	LOCATION_UID_KEY("LOCATION_UID_KEY","���ڴ洢��UIDΪkey��λ����Ϣ"),
	PHONE_UPDATE_INFO_KEY("PHONE_UPDATE_INFO_KEY","���Դ洢��deviceIdΪkey���ֻ��豸������Ϣ");
	
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
