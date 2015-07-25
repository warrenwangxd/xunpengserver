package com.warren.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.implement.ContactServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
import com.warren.contact.server.util.StringUtils;

public class FindSameContactServlet extends HttpServlet {

	Logger logger = Logger.getLogger(FindSameContactServlet.class);
	private ContactService contactService = new ContactServiceImpl();

	/**
	 * Json返回数据格式形如： { "18081978345": [
	 * {"13800456781":[{phoneNo="15678091231",name
	 * ="小张"},{phoneNo="13890907865",name="小李"}]},
	 * {"13908976543":[{phoneNo="15678091231"
	 * ,name="小张"},{phoneNo="13890907865",name="小李"}]} ] }
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		logger.info("查找相同的联系人:" + json);

		try {
			JSONObject jsonObj = new JSONObject(json);
			try {
				String owner = jsonObj.getString(JsonUtil.PHONE_NODE);
				if (!StringUtils.isEmpty(owner)) {
					Map<String, List<Contact>> contactsMap = contactService
							.getSameContact(owner);
					doResponse(response, contactsMap,owner);
					return;
				}

			} catch (JSONException e) {
				logger.warn(e.getMessage(), e);
			}
			try {
				String uid = jsonObj.getString(JsonUtil.UID_NODE);
				if (!StringUtils.isEmpty(uid)) {
					Map<String, List<Contact>> contactsMap = contactService
							.getSameContactByUid(uid);
					doResponse(response, contactsMap,uid);
					return;
				}

			} catch (JSONException e) {
				logger.warn(e.getMessage(), e);
			}

		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void doResponse(HttpServletResponse response,
			Map<String, List<Contact>> contactsMap, String owner)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder repStr = new StringBuilder();
		repStr.append("{\"").append(owner).append("\":[");
		Set<String> sameContactKeySet = contactsMap.keySet();
		Iterator<String> sameContactKeyIter = sameContactKeySet.iterator();
		while (sameContactKeyIter.hasNext()) {
			repStr.append("{\"");
			String key = sameContactKeyIter.next();
			repStr.append(key).append("\":[");
			List<Contact> contactList = contactsMap.get(key);
			for (Contact contact : contactList) {
				repStr.append("{").append(JsonUtil.PHONE_NODE).append(":\"")
						.append(contact.getPhoneNumber()).append("\",");
				repStr.append(JsonUtil.USER_NAME_NODE).append(":\"")
						.append(contact.getName()).append("\"},");
			}
			if (contactList.size() != 0) {
				repStr.deleteCharAt(repStr.length() - 1);
			}

			repStr.append("]},");

		}
		if (sameContactKeySet.size() != 0) {
			repStr.deleteCharAt(repStr.length() - 1);
		}
		repStr.append("]}");
		logger.info(owner + "相同的联系人数据：" + repStr);
		out.println(repStr);
	}
}
