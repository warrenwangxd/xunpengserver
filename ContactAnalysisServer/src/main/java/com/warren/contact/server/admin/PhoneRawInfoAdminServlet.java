package com.warren.contact.server.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.PhoneUpdateInfo;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.PhoneService;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.util.StringUtils;

public class PhoneRawInfoAdminServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(PhoneRawInfoAdminServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(StringUtils.isEmpty((String)session.getAttribute("adminLogin"))) {
			//===response相关设置
			response.setContentType("text/html; charset=utf-8");
			ServletContext sc = getServletContext();
			RequestDispatcher rd = null;
			rd = sc.getRequestDispatcher("/index_admin.jsp"); // 定向的页面
			rd.forward(request, response);
			return;
		}
		ContactService contactService= ServiceFactory.getContactService();
		PhoneService phoneService = ServiceFactory.getPhoneService();
		List<PhoneUpdateInfo> fullContactList = phoneService.getAllPhoneUpdateInfoListByTime();
		logger.info("获取所有的设备及联系人信息，一共有："+fullContactList.size());
		request.setAttribute("phoneUpdateList", fullContactList);
		//获取设备原始信息历史变更记录.
	
		for(int i=0;i<fullContactList.size();i++) {
			List<Contact> contactList = contactService.getAllContactsByOwnerPhoneKey(fullContactList.get(i).getDeviceId());
			request.setAttribute(fullContactList.get(i).getDeviceId(), contactList);
		}
			
		//===response相关设置
				response.setContentType("text/html; charset=utf-8");
				ServletContext sc = getServletContext();
				RequestDispatcher rd = null;
				rd = sc.getRequestDispatcher("/device_admin.jsp"); // 定向的页面
				rd.forward(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
