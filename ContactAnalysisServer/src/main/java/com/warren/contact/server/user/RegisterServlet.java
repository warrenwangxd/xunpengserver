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

import com.warren.contact.server.config.Configuration;
import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;

public class RegisterServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(Constants.USER_LOGGER_NAME);
	UserService userService = ServiceFactory.getUserService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		logger.info("用户注册:" + json);
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String phone = jsonObj.getString(JsonUtil.PHONE_NODE);
			String deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
			String pwd = jsonObj.getString(JsonUtil.USER_PWD_NODE);
			String inputVerifyCode = jsonObj.getString(JsonUtil.VERIFY_CODE);

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuffer repStr = new StringBuffer();
			if (Configuration.USER_REG_CHECKCODE_SWITCH_ON.equals(Configuration
					.getConfiguration().getUserRegCheckCodeSwitch())) {
				if (!userService.checkVerifyCode(phone, inputVerifyCode)) {
					repStr.append("{").append(JsonUtil.RESULT_NODE)
							.append(":\"")
							.append(JsonUtil.RESULT_CHECK_VERIFYCODE_FAILED)
							.append("\"}");
					out.print(repStr);
					logger.info("用户注册结果:" + repStr);
					return;
				}

			} else if (userService.isRegistered(phone)) {
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
						.append(JsonUtil.RESULT_PHONE_HAS_REGISTERED)
						.append("\"}");
				out.print(repStr);
				logger.info("用户注册结果:" + repStr);
				return;
			}

			User user = new User();
			user.setDeviceId(deviceId);
			user.setPhone(phone);
			user.setPwd(pwd);
			userService.saveOrUpdateUserInfo(user);
			logger.info("通过手机注册用户:" + user);

			repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
					.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");
			out.print(repStr);
			logger.info("用户注册结果:" + repStr);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}

	}

}
