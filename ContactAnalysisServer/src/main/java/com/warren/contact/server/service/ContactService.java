package com.warren.contact.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.ContactChangedLog;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.User;


public interface ContactService {

	/**
	 * 根据owner获取联系人数量
	 * @return
	 */
	public int getContactsSize(String owner);
	
	/**
	 * 更新联系人
	 * @param fullContact
	 * @return
	 */
	public boolean saveAndUpdateContacts(PhoneRawInfo fullContact);
	
	/**
	 * 根据deviceId且key为name的方式获取联系人信息。
	 * @param owner
	 * @return
	 */
	public List<Contact> getAllContactsByOwnerNameKey(String owner);
	
	/**
	 * 根据deviceId且key为phone的方式获取联系人信息。
	 * @param owner
	 * @return
	 */
	public List<Contact> getAllContactsByOwnerPhoneKey(String owner);
	
	
	/**
	 * 获取所有的手机联系人数据.
	 * @return
	 */
	public List<PhoneRawInfo> getAllPhoneContact() ;
	
	/**
	 * 查找以phone为key存储的用户联系人数据.
	 * @return
	 */
	public List<User> getAllUserContactByPhone();
	/**
	 * 查找以 uid为key存储的用户联系人数据.
	 * @return
	 */
	public List<User> getAllUserContactByUid();
	
	/**
	 * 获取某人联系人有相同联系人的数据.
	 * @param Map中key为phoneNo
	 * @return
	 */
	public Map<String,List<Contact>> getSameContact(String phone);
	
	public boolean saveOrUpdateUserContacts(User user);
	
	public List<Contact> getAllContactsByPhone(String phone);
	
	public List<Contact> getAllContactsByUid(String uid);
	
	public Map<String,List<Contact>> getSameContactByUidForTask(String uid);
	
	/**
	 * 提供给应用层使用的服务，直接查询异步任务计算好的结果,提供较高的查询性能.
	 * @param uid
	 * @return
	 */
	public Map<String,List<Contact>> getSameContactByUid(String uid);
	
	public boolean saveOrUpdateUserSameContacts(String uid, Map<String,List<Contact>> sameContacts);
	
	/**
	 * 
	 * @param uid
	 * @param time
	 * @param noticed 是否通知到用户.
	 * @return
	 */
	public boolean  updateUserSameContactChangedLog(String uid, Date time,boolean noticed);
	
	/**
	 * 按照时间倒序排序.
	 * @param uid
	 * @return
	 */
	public List<ContactChangedLog> getUserSameContactChangedLog(String uid);
	
}