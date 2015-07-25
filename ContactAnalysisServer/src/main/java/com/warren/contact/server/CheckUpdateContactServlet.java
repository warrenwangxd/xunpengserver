package com.warren.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.implement.ContactServiceImpl;
import com.warren.contact.server.util.StreamUtil;
/**
 * 检查是否需要更新联系人信息。
 * 返回格式形如：
 * {owner:"warren",contactsSize:"10"}
 * 
 * @author dong.wangxd
 *
 */
public class CheckUpdateContactServlet extends HttpServlet{
	   ContactService contactService = new ContactServiceImpl();
	Logger logger = Logger.getLogger(CheckUpdateContactServlet.class);
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
	
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data,"UTF-8");
		logger.info("检查是否需要更新联系人:"+json);
		
		try {
			JSONObject  jsonObj = new JSONObject(json);
		    String owner = jsonObj.getString("owner");
		 
			int contactsSize = contactService.getContactsSize(owner);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuilder repStr = new StringBuilder();
			repStr.append("{owner:\"").append(owner).append("\",");
			repStr.append("contactsSize:\"");
			repStr.append(0).append("\"}");
			out.println(repStr);
	
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	

}
