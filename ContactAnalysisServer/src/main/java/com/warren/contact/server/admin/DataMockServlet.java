package com.warren.contact.server.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.PhoneRawInfo;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.util.MD5Util;
import com.warren.contact.server.util.StringUtils;

/**
 * 产生测试数据的服务.
 * 
 * @author dong.wangxd
 * 
 */
public class DataMockServlet extends HttpServlet {
	private static User user0 = new User();
	private static User user1 = new User();
	private static User user2 = new User();

	static {
		user0.setDeviceId("mockDeviceId");
		user0.setPhone("13540888888");
		user0.setLocationPublic("是");
		user0.setName("小强");
		user0.setRegisterTime(new Date());
		user0.setGender("男");
		user0.setPwd(MD5Util.getMD5Code("888888"));

		// ==============
		user1.setDeviceId("mockDeviceId1");
		user1.setPhone("13540222222");

		user1.setLocationPublic("是");
		user1.setName("李珊");
		user1.setRegisterTime(new Date());
		user1.setGender("女");

		user1.setPwd(MD5Util.getMD5Code("888888"));
		// ===========================
		user2.setDeviceId("mockDeviceId2");
		user2.setPhone("13540333333");
		user2.setLocationPublic("是");
		user2.setName("王萍");
		user2.setRegisterTime(new Date());
		user2.setGender("女");
		user2.setPwd(MD5Util.getMD5Code("888888"));

	}

	private UserService userService = ServiceFactory.getUserService();
	private ContactService contactService = ServiceFactory.getContactService();
	private LocationService locationService = ServiceFactory
			.getLocationService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (StringUtils.isEmpty((String) session.getAttribute("adminLogin"))) {
			// ===response相关设置
			response.setContentType("text/html; charset=utf-8");
			ServletContext sc = getServletContext();
			RequestDispatcher rd = null;
			rd = sc.getRequestDispatcher("/index_admin.jsp"); // 定向的页面
			rd.forward(request, response);
			return;
		}

		mockContact0();
		mockContact1();
		mockContact2();

		// ===response相关设置
		request.setAttribute("mockResult", "true");
		response.setContentType("text/html; charset=utf-8");
		ServletContext sc = getServletContext();
		RequestDispatcher rd = null;
		rd = sc.getRequestDispatcher("/mock_admin.jsp"); // 定向的页面
		rd.forward(request, response);

	}

	private void mockContact0() {// 构造用户数据
		userService.saveOrUpdateUserInfo(user0);
		// 构成联系人数据
		PhoneRawInfo phoneRawInfo = new PhoneRawInfo();
		phoneRawInfo.setDeviceId(user0.getDeviceId());
		phoneRawInfo.setPhoneType("APPLE");
		List<Contact> contactList = new ArrayList<Contact>();
		Contact contact1 = new Contact();
		contact1.setName(user1.getName());
		contact1.setPhoneNumber(user1.getPhone());
		contactList.add(contact1);
		Contact contact2 = new Contact();
		contact2.setName(user2.getName());
		contact2.setPhoneNumber(user2.getPhone());
		contactList.add(contact2);
		phoneRawInfo.setContacts(contactList);
		contactService.saveAndUpdateContacts(phoneRawInfo);
		// 构造地理位置数据.
		Location location = new Location();
		location.setLocTime(new Date());
		location.setLatitude("30.5328628192244");
		location.setLogitude("104.06435421309348");
		location.setFullAddress("美年广场");
		locationService.updateLocation(user0, location);
	}

	private void mockContact1() {
		userService.saveOrUpdateUserInfo(user1);
		// 构成联系人数据
		PhoneRawInfo phoneRawInfo1 = new PhoneRawInfo();
		phoneRawInfo1.setDeviceId(user1.getDeviceId());
		phoneRawInfo1.setPhoneType("APPLE");
		List<Contact> contactList1 = new ArrayList<Contact>();
		Contact contact11 = new Contact();
		contact11.setName(user0.getName());
		contact11.setPhoneNumber(user0.getPhone());
		contactList1.add(contact11);
		Contact contact12 = new Contact();
		contact12.setName(user2.getName());
		contact12.setPhoneNumber(user2.getPhone());
		contactList1.add(contact12);
		phoneRawInfo1.setContacts(contactList1);
		contactService.saveAndUpdateContacts(phoneRawInfo1);
		// 构造地理位置数据.
		Location location = new Location();
		location.setLocTime(new Date());
		location.setLatitude("39.2621628192244");
		location.setLogitude("115.06535421309348"); // 北京
		location.setFullAddress("北京");
		locationService.updateLocation(user1, location);
	}

	private void mockContact2() {
		userService.saveOrUpdateUserInfo(user2);
		// 构成联系人数据
		PhoneRawInfo phoneRawInfo2 = new PhoneRawInfo();
		phoneRawInfo2.setDeviceId(user2.getDeviceId());
		phoneRawInfo2.setPhoneType("APPLE");
		List<Contact> contactList2 = new ArrayList<Contact>();
		Contact contact21 = new Contact();
		contact21.setName(user0.getName());
		contact21.setPhoneNumber(user0.getPhone());
		contactList2.add(contact21);
		Contact contact22 = new Contact();
		contact22.setName(user1.getName());
		contact22.setPhoneNumber(user1.getPhone());
		contactList2.add(contact22);
		phoneRawInfo2.setContacts(contactList2);
		contactService.saveAndUpdateContacts(phoneRawInfo2);
		// 构造地理位置数据.
		Location location = new Location();
		location.setLocTime(new Date());
		location.setLatitude("18.1558628192244");// 三亚
		location.setLogitude("109.06135421309348");
		location.setFullAddress("三亚");
		locationService.updateLocation(user2, location);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
