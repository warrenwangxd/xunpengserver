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
	 * ����owner��ȡ��ϵ������
	 * @return
	 */
	public int getContactsSize(String owner);
	
	/**
	 * ������ϵ��
	 * @param fullContact
	 * @return
	 */
	public boolean saveAndUpdateContacts(PhoneRawInfo fullContact);
	
	/**
	 * ����deviceId��keyΪname�ķ�ʽ��ȡ��ϵ����Ϣ��
	 * @param owner
	 * @return
	 */
	public List<Contact> getAllContactsByOwnerNameKey(String owner);
	
	/**
	 * ����deviceId��keyΪphone�ķ�ʽ��ȡ��ϵ����Ϣ��
	 * @param owner
	 * @return
	 */
	public List<Contact> getAllContactsByOwnerPhoneKey(String owner);
	
	
	/**
	 * ��ȡ���е��ֻ���ϵ������.
	 * @return
	 */
	public List<PhoneRawInfo> getAllPhoneContact() ;
	
	/**
	 * ������phoneΪkey�洢���û���ϵ������.
	 * @return
	 */
	public List<User> getAllUserContactByPhone();
	/**
	 * ������ uidΪkey�洢���û���ϵ������.
	 * @return
	 */
	public List<User> getAllUserContactByUid();
	
	/**
	 * ��ȡĳ����ϵ������ͬ��ϵ�˵�����.
	 * @param Map��keyΪphoneNo
	 * @return
	 */
	public Map<String,List<Contact>> getSameContact(String phone);
	
	public boolean saveOrUpdateUserContacts(User user);
	
	public List<Contact> getAllContactsByPhone(String phone);
	
	public List<Contact> getAllContactsByUid(String uid);
	
	public Map<String,List<Contact>> getSameContactByUidForTask(String uid);
	
	/**
	 * �ṩ��Ӧ�ò�ʹ�õķ���ֱ�Ӳ�ѯ�첽�������õĽ��,�ṩ�ϸߵĲ�ѯ����.
	 * @param uid
	 * @return
	 */
	public Map<String,List<Contact>> getSameContactByUid(String uid);
	
	public boolean saveOrUpdateUserSameContacts(String uid, Map<String,List<Contact>> sameContacts);
	
	/**
	 * 
	 * @param uid
	 * @param time
	 * @param noticed �Ƿ�֪ͨ���û�.
	 * @return
	 */
	public boolean  updateUserSameContactChangedLog(String uid, Date time,boolean noticed);
	
	/**
	 * ����ʱ�䵹������.
	 * @param uid
	 * @return
	 */
	public List<ContactChangedLog> getUserSameContactChangedLog(String uid);
	
}