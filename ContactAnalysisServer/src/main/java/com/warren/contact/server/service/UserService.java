package com.warren.contact.server.service;

import java.util.List;

import com.warren.contact.server.domain.User;

/**
 * ��Աע�ᣬ��¼����ط���.
 * @author dong.wangxd
 *
 */
public interface UserService {
	
	public boolean saveOrUpdateUserInfo(User user);
	
	public boolean isRegistered(String phone);
	
	public User getUserBaseInfoByPhone(String phone);
	
	public User getUserBaseInfoByUid(String uid);
	
	public void saveVerifyCode(String phone,String verifyCode);
	
	public boolean needSendVerifyCode(String phone);
	
	public boolean checkVerifyCode(String phone, String inputVerifyCode);
	
	public  List<User> getAllUserByPhoneKey();
	
	public List<User> getAllUserByUidKey();
	
	public boolean deleteUser(String phone);


}
