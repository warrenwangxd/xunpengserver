package com.warren.contact.task;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.integration.SmsUtil;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.UserService;

@Component("taskJob")
public class ContactAnalysisTask {
	@Autowired
	private ContactService contactService;
	@Autowired
	private UserService userService;
	@Autowired
	private SmsUtil smsUtil;

	private Logger logger = Logger.getLogger(Constants.TASK_LOGGER_NAME);

	@Scheduled(cron = "0 0 0 * * ?")
	public void computeSameContact() {
		logger.info("===���㹲ͬ��ϵ��====");
		List<User> users = userService.getAllUserByUidKey();
		for (User user : users) {
			Map<String, List<Contact>> contactsMap = contactService
					.getSameContactByUidForTask(user.getUid());
			// ��ѯ���е���ϵ�����ݣ��Ƚ��Ƿ���Ҫ����
			Map<String, List<Contact>> oldContactsMap = contactService
					.getSameContactByUid(user.getUid());
			if (!isEqual(contactsMap, oldContactsMap)) {
				//���ӱ����־��¼
               contactService.updateUserSameContactChangedLog(user.getUid(), new Date(), false);
				contactService.saveOrUpdateUserSameContacts(user.getUid(),
						contactsMap);
				Set<String> keySet = contactsMap.keySet();
				Iterator<String> keyIter = keySet.iterator();
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					List<Contact> contacts = contactsMap.get(key);
					logger.info(user.getUid()+"-"+user.getName() + "-" + user.getPhone()
							+ "��" + key + "��ͬ����ϵ��,�У�" + contacts.size()
							+ "��:");
					logger.info(contacts);
				}
			} else {
				logger.info(user.getUid()+"-"+user.getName() + "-" + user.getPhone() + "��ϵ�˹�ϵû�з����仯");
			}
		}

	}
	/*@Scheduled(cron = "0 0/1 * * * ?")
	public void testTask() {
		logger.info("���������Զ�ע��:"+smsUtil.getUserName()+ smsUtil.getPasswd()+smsUtil.getSmsServerUrl());
	}*/

	private boolean isEqual(Map<String, List<Contact>> newContactsMap,
			Map<String, List<Contact>> oldContactsMap) {
		if(newContactsMap.size()!=oldContactsMap.size()) {
			return false;
		}
		Set<String> newKeySet = newContactsMap.keySet();
		Set<String> oldKeySet = oldContactsMap.keySet();
		if(!newKeySet.equals(oldKeySet)) {
			return false;
		}
		Iterator<String> newIter = newKeySet.iterator();
		while(newIter.hasNext()) {
			String newKey = (String)newIter.next();
			List<Contact> newContact = newContactsMap.get(newKey);
			List<Contact> oldContact = oldContactsMap.get(newKey);
			if(!newContact.containsAll(oldContact)&&oldContact.containsAll(newContact)) {
				logger.info(newKey+"��ͬ����ϵ�����ݲ���");
				logger.info("newContact:"+newContact);
				logger.info("oldContact:"+oldContact);
				return false;
			}
		}
		return true;
	}
	

}
