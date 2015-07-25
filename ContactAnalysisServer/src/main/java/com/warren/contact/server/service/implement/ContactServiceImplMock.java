package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.ContactChangedLog;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;

public class ContactServiceImplMock implements ContactService {

	@Override
	public int getContactsSize(String owner) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean saveAndUpdateContacts(PhoneRawInfo fullContact) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Contact> getAllContactsByOwnerNameKey(String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> getAllContactsByOwnerPhoneKey(String owner) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<PhoneRawInfo> getAllPhoneContact() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<Contact>> getSameContact(String phone) {
		Map<String,List<Contact>> sameContactMap = new HashMap<String,List<Contact>>();
		List<Contact> contactList = new ArrayList<Contact>();
		Contact contact = new Contact();
		contact.setName("п║уе");
		contact.setPhoneNumber("13910123345");
		contactList.add(contact);
		
		sameContactMap.put("13809123456", contactList);
		sameContactMap.put("1598012345", contactList);

		return sameContactMap;
	}

	@Override
	public boolean saveOrUpdateUserContacts(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Contact> getAllContactsByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> getAllContactsByUid(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<Contact>> getSameContactByUidForTask(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUserContactByPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUserContactByUid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveOrUpdateUserSameContacts(String uid,
			Map<String, List<Contact>> sameContacts) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, List<Contact>> getSameContactByUid(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUserSameContactChangedLog(String uid, Date time,
			boolean noticed) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ContactChangedLog> getUserSameContactChangedLog(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
