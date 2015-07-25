package com.warren.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.implement.ContactServiceImpl;
import com.warren.contact.server.util.StreamUtil;

/**
 * 根据owner名获取所有的联系人。
 * 
 * @author dong.wangxd
 * 
 */
public class GetAllContactsServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");

		try {
			JSONObject jsonObj = new JSONObject(json);
			String owner = jsonObj.getString("owner");
			ContactService contactService = new ContactServiceImpl();
			List<Contact> contacts = contactService
					.getAllContactsByOwnerPhoneKey(owner);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuilder repStr = new StringBuilder();
			repStr.append("{owner:\"").append(owner).append("\"");
			repStr.append(",contacts:");
			repStr.append('[');
			for (Contact contact : contacts) {
				repStr.append('{');
				repStr.append("name:\"").append(contact.getName())
						.append("\",");
				repStr.append("phone:\"").append(contact.getPhoneNumber())
						.append("\"");

				repStr.append("},");
			}
			repStr.deleteCharAt(repStr.length() - 1);
			repStr.append(']');
			repStr.append("}");
			out.println(repStr);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
				doPost(request, response);
			}
}
