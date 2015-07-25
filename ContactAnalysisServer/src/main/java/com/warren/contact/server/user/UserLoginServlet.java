package com.warren.contact.server.user;

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
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;

public class UserLoginServlet extends HttpServlet {
	private static Logger logger  =Logger.getLogger(UserLoginServlet.class);
	UserService userService = new UserServiceImpl();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String phone = jsonObj.getString(JsonUtil.PHONE_NODE);
			String deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
			String pwd = jsonObj.getString(JsonUtil.USER_PWD_NODE);
			StringBuffer repStr = new StringBuffer();
			User user = userService.getUserBaseInfoByPhone(phone);
			if (user.isEmpty()) {
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
						.append(JsonUtil.RESULT_PHONE_NOT_EXIST).append("\"}");
			} else if (user.getPwd().equals(pwd)) {
				if (!user.getDeviceId().equals(deviceId)) {
					logger.warn(phone + "登录的设备发生变化");

				}
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
						.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");
				//设置session.
				HttpSession session = request.getSession();
				session.setAttribute(Constants.SESSION_PHONE, phone);;
				session.setAttribute(Constants.SESSION_DEVICE_ID, deviceId);

			} else {
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
						.append(JsonUtil.RESULT_PWD_ERROR).append("\"}");
			}
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(repStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
