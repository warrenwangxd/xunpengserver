package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.dal.RedisTemplate;
import com.warren.contact.server.domain.PhoneUpdateInfo;
import com.warren.contact.server.service.implement.ContactServiceImpl;
import com.warren.contact.server.service.implement.LocationServiceImpl;
import com.warren.contact.server.service.implement.PhoneServiceImpl;
import com.warren.contact.server.service.implement.UserServiceImpl;
/**
 * 服务工厂。
 * 所有的服务实例从该工厂类获取。后续考虑用spring替换.
 * @author dong.wangxd
 *
 */
public class ServiceFactory {
	private static UserService userService ;
	private static ContactService contactService ;
	private static PhoneService phoneService ;
	private static LocationService locationService;
	
	private static RedisTemplate redisTemplate;
	static {
		redisTemplate= new RedisTemplate();
		phoneService=new PhoneServiceImpl();
		contactService=new ContactServiceImpl();
		locationService=new LocationServiceImpl();
		userService=new UserServiceImpl();
	}
			
	
	
	public static UserService getUserService() {
		return userService;
	}
	
	public static ContactService getContactService() {
		return contactService;
	}
	
	public static PhoneService getPhoneService() {
		return phoneService;
	}
	
	public static LocationService getLocationService() {
		return locationService;
	}
	
	public synchronized static RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
}
