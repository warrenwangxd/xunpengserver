package com.warren.contact.server.config;

/**
 * 系统的一些参数配置信息.
 * 
 * @author dong.wangxd
 * 
 */
public class Configuration {

	/**
	 * 位置信息全局控制开关.优先级高于个人设置.
	 */
	public static String LOCATION_PUBLIC_SWITCH_ON = "on";

	/**
	 * 位置信息全局控制开关.优先级低于个人设置.
	 */
	public static String LOCATION_PUBLIC_SWITCH_OFF = "off";
	
	public static String USER_REG_CHECKCODE_SWITCH_ON="on";
	public static String USER_REG_CHECKCODE_SWITCH_OFF="off";
	
	/**
	 * 用户注册时是否需要短信验证码校验开关.
	 * 默认打开.
	 */
   private String userRegCheckCodeSwitch=USER_REG_CHECKCODE_SWITCH_ON;
	public String getUserRegCheckCodeSwitch() {
		return userRegCheckCodeSwitch;
	}

	public void setUserRegCheckCodeSwitch(String userRegCheckCodeSwitch) {
		this.userRegCheckCodeSwitch = userRegCheckCodeSwitch;
	}
	/**
	 * 位置信息全局控制开关.默认关闭.
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
