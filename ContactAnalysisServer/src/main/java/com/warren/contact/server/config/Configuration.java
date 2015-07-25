package com.warren.contact.server.config;

/**
 * ϵͳ��һЩ����������Ϣ.
 * 
 * @author dong.wangxd
 * 
 */
public class Configuration {

	/**
	 * λ����Ϣȫ�ֿ��ƿ���.���ȼ����ڸ�������.
	 */
	public static String LOCATION_PUBLIC_SWITCH_ON = "on";

	/**
	 * λ����Ϣȫ�ֿ��ƿ���.���ȼ����ڸ�������.
	 */
	public static String LOCATION_PUBLIC_SWITCH_OFF = "off";
	
	public static String USER_REG_CHECKCODE_SWITCH_ON="on";
	public static String USER_REG_CHECKCODE_SWITCH_OFF="off";
	
	/**
	 * �û�ע��ʱ�Ƿ���Ҫ������֤��У�鿪��.
	 * Ĭ�ϴ�.
	 */
   private String userRegCheckCodeSwitch=USER_REG_CHECKCODE_SWITCH_ON;
	public String getUserRegCheckCodeSwitch() {
		return userRegCheckCodeSwitch;
	}

	public void setUserRegCheckCodeSwitch(String userRegCheckCodeSwitch) {
		this.userRegCheckCodeSwitch = userRegCheckCodeSwitch;
	}
	/**
	 * λ����Ϣȫ�ֿ��ƿ���.Ĭ�Ϲر�.
	 */
	private String locationPublicSwitch=LOCATION_PUBLIC_SWITCH_OFF;

	private static Configuration configuration;

	public String getLocationPublicSwitch() {
		return locationPublicSwitch;
	}

	public void setLocationPublicSwitch(String locationPublicSwitch) {
		this.locationPublicSwitch = locationPublicSwitch;
	}

	private Configuration() {

	}

	public static synchronized Configuration getConfiguration() {
		if (configuration == null) {
			configuration = new Configuration();
		}
		return configuration;
	}

}
