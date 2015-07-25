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
		logger.info("===计算共同联系人====");
		List<User> users = userService.getAllUserByUidKey();
		for (User user : users) {
			Map<String, List<Contact>> contactsMap = contactService
					.getSameContactByUidForTask(user.getUid());
			// 查询现有的联系人数据，比较是否需要更新
			Map<String, List<Contact>> oldContactsMap = contactService
					.getSameContactByUid(user.getUid());
			if (!isEqual(contactsMap, oldContactsMap)) {
				//增加变更日志记录
               contactService.updateUserSameContactChangedLog(user.getUid(), new Date(), false);
				contactService.saveOrUpdateUserSameContacts(user.getUid(),
						contactsMap);
				Set<String> keySet = contactsMap.keySet();
				Iterator<String> keyIter = keySet.iterator();
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					List<Contact> contacts = contactsMap.get(key);
					logger.info(user.getUid()+"-"+user.getName() + "-" + user.getPhone()
							+ "与" + key + "共同的联系人,有：" + contacts.size()
							+ "个:");
					logger.info(contacts);
				}
			} else {
				logger.info(user.getUid()+"-"+user.getName() + "-" + user.getPhone() + "联系人关系没有发生变化");
			}
		}

	}
	/*@Scheduled(cron = "0 0/1 * * * ?")
	public void testTask() {
		logger.info("测试配置自动注入:"+smsUtil.getUserName()+ smsUtil.getPasswd()+smsUtil.getSmsServerUrl());
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
				logger.info(newKey+"共同的联系人数据不等");
				logger.info("newContact:"+newContact);
				logger.info("oldContact:"+oldContact);
				return false;
			}
		}
		return true;
	}
	

}
