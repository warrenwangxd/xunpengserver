package com.warren.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
import com.warren.contact.server.util.StringUtils;

/**
 * 收集手机联系人
 * 
 * @author dong.wangxd
 * 
 */
public class UploadContactServlet extends HttpServlet {

	private static Logger userlogger = Logger.getLogger(Constants.USER_LOGGER_NAME);
	private static Logger phoneLogger = Logger.getLogger(Constants.PHONE_LOGGER_NAME);
	
	
	private ContactService contactService= ServiceFactory.getContactService();

	/**
	 * 格式形如:
	 * {deviceId:"1244988838",phone:"",uid:"",contacts:[{name:"",phone:""},
	 * {name:"",phone:""}]}
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();

		PhoneRawInfo fullContact = new PhoneRawInfo();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONArray contactArray = jsonObj
					.getJSONArray(JsonUtil.CONTACTS_NODE);
			// 兼容老的客户端版本需要做异常处理.
			String deviceId = null;
			try {
				deviceId = jsonObj.getString("owner");
			} catch (JSONException e) {
				phoneLogger.warn(e.getMessage(), e);
			}
			if (StringUtils.isEmpty(deviceId)) {
				try {
					deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
				} catch (JSONException e) {
					phoneLogger.warn(e.getMessage(), e);
				}
			}
			String uid = null;
			try {
				uid = jsonObj.getString(JsonUtil.UID_NODE);
			} catch (JSONException e) {
				phoneLogger.warn(e.getMessage(), e);
			}
			String ownerPhone = null;
			try {
				ownerPhone = jsonObj.getString(JsonUtil.PHONE_NODE);
			} catch (JSONException e) {
				phoneLogger.warn(e.getMessage(), e);
			}
			phoneLogger.info("准备更新联系人: [deviceId:" + deviceId + ",uid:" + uid
					+ ",phone:" + ownerPhone + "]" + json);
			List<Contact> contacts = new ArrayList<Contact>();

			for (int i = 0; i < contactArray.length(); i++) {
				JSONObject jsonObject = contactArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				String phone = jsonObject.getString("phone");
				Contact contact = new Contact();
				contact.setName(name);
				contact.setPhoneNumber(phone);
				contacts.add(contact);
			}
			//更新以deviceId为key的联系人数据
			if(!StringUtils.isEmpty(deviceId)) {
				fullContact.setDeviceId(deviceId);
				fullContact.setContacts(contacts);		
				contactService.saveAndUpdateContacts(fullContact);
			}
			
			//更新以phone和uid为key的联系人数据.
			if (!StringUtils.isEmpty(ownerPhone)||!StringUtils.isEmpty(uid)) {
				User user = new User();
				user.setUid(uid);
				user.setPhone(ownerPhone);
				user.setContacts(contacts);	
                contactService.saveOrUpdateUserContacts(user);
                userlogger.info("更新联系人成功"+user);
			}

		} catch (JSONException e) {
			phoneLogger.error(json+e.getMessage(), e);
		}

	}
}
