package com.warren.contact.server.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.warren.contact.server.config.Configuration;
import com.warren.contact.server.domain.Contact;
import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.ContactServiceImpl;
import com.warren.contact.server.service.implement.LocationServiceImpl;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.StringUtils;

public class UserAdminServlet extends HttpServlet {
	static UserService userService = new UserServiceImpl();
	static ContactService contactService = new ContactServiceImpl();
	static LocationService locationService = new LocationServiceImpl();
	Logger logger = Logger.getLogger(UserAdminServlet.class);

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
		// ===查询单个用户的各维度信息.
		if (!StringUtils.isEmpty(request.getParameter("operate"))
				&& StringUtils.equals("singleQuery",
						request.getParameter("operate"))) {
			doSingleQuery(request, response);
		} else if (!StringUtils.isEmpty(request.getParameter("locationPublic"))) {
			doLocationSetting(request, response);
		} else if (!StringUtils.isEmpty(request.getParameter("operate"))
				&& StringUtils.equals("getAllUser",
						request.getParameter("operate"))) {
			doQueryAll(request, response);
		} else if (!StringUtils.isEmpty(request
				.getParameter("userRegCheckCodeSwitch"))) {
			doUserRegCheckCodeSetting(request, response);
		} else if (!StringUtils.isEmpty(request.getParameter("operate"))
				&& StringUtils.equals("deleteUser",
						request.getParameter("operate"))) {
			doDeleteUser(request, response);
		}
		// ===response相关设置
		request.setAttribute("userRegCheckCodeSwitch", Configuration
				.getConfiguration().getUserRegCheckCodeSwitch());
		request.setAttribute("locationPublic", Configuration.getConfiguration()
				.getLocationPublicSwitch());
		response.setContentType("text/html; charset=utf-8");
		ServletContext sc = getServletContext();
		RequestDispatcher rd = null;
		rd = sc.getRequestDispatcher("/user_admin.jsp"); // 定向的页面
		rd.forward(request, response);

	}

	private void doDeleteUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String phone = request.getParameter("phone");
		logger.info("===删除用户:" + phone);
		userService.deleteUser(phone);
	}

	private void doUserRegCheckCodeSetting(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("userRegCheckCodeSwitch");
		if (!StringUtils.isEmpty(action)
				&& action.equals(Configuration.USER_REG_CHECKCODE_SWITCH_OFF)) {
			Configuration.getConfiguration().setUserRegCheckCodeSwitch(
					Configuration.USER_REG_CHECKCODE_SWITCH_OFF);
			logger.info("修改用户注册短信校验码开关："
					+ Configuration.getConfiguration()
							.getUserRegCheckCodeSwitch());
		} else if (!StringUtils.isEmpty(action)
				&& action.equals(Configuration.USER_REG_CHECKCODE_SWITCH_ON)) {
			Configuration.getConfiguration().setUserRegCheckCodeSwitch(
					Configuration.USER_REG_CHECKCODE_SWITCH_ON);
			logger.info("修改用户注册短信校验码开关："
					+ Configuration.getConfiguration()
							.getUserRegCheckCodeSwitch());
		}

	}

	private void doQueryAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<User> allUser = userService.getAllUserByPhoneKey();
		request.setAttribute("userList", allUser);
	}

	private void doSingleQuery(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String phone = request.getParameter("phone");
		logger.info("【管理】查询用户" + phone + "的相关信息:");
		User user = userService.getUserBaseInfoByPhone(phone);
		if (user != null) {
			List<Contact> contactList = contactService
					.getAllContactsByOwnerPhoneKey(user.getDeviceId());
			List<Location> locationList = locationService
					.getAllLocationByPhone(user.getPhone());
			List<FriendLocation> friendLocationList = locationService
					.getFriendsLocation(phone);
			Map<String, List<Contact>> sameContactMap = contactService
					.getSameContact(phone);
			request.setAttribute("user", user);
			request.setAttribute("contacts", contactList);
			request.setAttribute("locationList", locationList);
			request.setAttribute("friendLocationList", friendLocationList);
			request.setAttribute("sameFriendMap", sameContactMap);
		}
	}

	private void doLocationSetting(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("locationPublic");
		if (!StringUtils.isEmpty(action)
				&& action.equals(Configuration.LOCATION_PUBLIC_SWITCH_OFF)) {
			Configuration.getConfiguration().setLocationPublicSwitch(
					Configuration.LOCATION_PUBLIC_SWITCH_OFF);
			logger.info("修改全局位置信息开关："
					+ Configuration.getConfiguration()
							.getLocationPublicSwitch());
		} else if (!StringUtils.isEmpty(action)
				&& action.equals(Configuration.LOCATION_PUBLIC_SWITCH_ON)) {
			Configuration.getConfiguration().setLocationPublicSwitch(
					Configuration.LOCATION_PUBLIC_SWITCH_ON);
			logger.info("修改全局位置信息开关："
					+ Configuration.getConfiguration()
							.getLocationPublicSwitch());
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
