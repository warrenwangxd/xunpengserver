package com.warren.contact.server.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
import com.warren.contact.server.util.StringUtils;

public class UserInfoGetServlet extends HttpServlet {
	Logger logger = Logger.getLogger(UserInfoGetServlet.class);
	UserService userService = ServiceFactory.getUserService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		logger.info("查询用户基本信息:"+json);
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String phone = null;
			String uid = null;
			User user = null;
			try {
				phone = jsonObj.getString(JsonUtil.PHONE_NODE);
				if (!StringUtils.isEmpty(phone)) {
					user = userService.getUserBaseInfoByPhone(phone);
					doResponse(user, response);
					return;
				}
			} catch (JSONException e) {
				logger.warn(e.getMessage(), e);
			}
			try {
				uid = jsonObj.getString(JsonUtil.UID_NODE);
				logger.info("根据uid:"+uid+"查询用户信息");
				if (!StringUtils.isEmpty(uid)) {
					user = userService.getUserBaseInfoByUid(uid);
					doResponse(user, response);
				}
			} catch (JSONException e) {
				logger.warn(e.getMessage(), e);
			}

		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}

	}

	private void doResponse(User user, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		StringBuffer repStr = new StringBuffer();
		repStr.append("{");
		if (!StringUtils.isEmpty(user.getDeviceId())) {
			repStr.append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(user.getDeviceId()).append("\",");
		}
		if (!StringUtils.isEmpty(user.getPhone())) {
			repStr.append(JsonUtil.PHONE_NODE).append(":\"")
					.append(user.getPhone()).append("\",");
		} else {
			repStr.append(JsonUtil.PHONE_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getUid())) {
			repStr.append(JsonUtil.UID_NODE).append(":\"")
					.append(user.getUid()).append("\",");
		} else {
			repStr.append(JsonUtil.UID_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getScreenName())) {
			repStr.append(JsonUtil.USER_SCREEN_NAME).append(":\"")
					.append(user.getScreenName()).append("\",");
		} else {
			repStr.append(JsonUtil.USER_SCREEN_NAME).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getName())) {
			repStr.append(JsonUtil.USER_NAME_NODE).append(":\"")
					.append(user.getName()).append("\",");
		} else {
			repStr.append(JsonUtil.USER_NAME_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getGender())) {
			repStr.append(JsonUtil.USER_SEX_NODE).append(":\"")
					.append(user.getGender()).append("\",");
		} else {
			repStr.append(JsonUtil.USER_SEX_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getLocationPublic())) {
			repStr.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":\"")
					.append(user.getLocationPublic()).append("\",");
		} else {
			repStr.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":\"")
					.append("").append("\",");
		}
		if (!StringUtils.isEmpty(user.getImgPath())) {
			repStr.append(JsonUtil.USER_IMG_NODE).append(":\"")
					.append(user.getImgPath()).append("\"}");
		} else {
			repStr.append(JsonUtil.USER_IMG_NODE).append(":\"").append("")
					.append("\"}");
		}

		out.print(repStr);
		logger.info("返回用户信息：" + repStr);
	}
}
