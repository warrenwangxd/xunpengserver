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

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ServiceFactory;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
import com.warren.contact.server.util.StringUtils;

public class UserInfoModifyServlet extends HttpServlet {

	Logger logger = Logger.getLogger(UserInfoModifyServlet.class);
	Logger userLogger = Logger.getLogger(Constants.USER_LOGGER_NAME);
	UserService userService = ServiceFactory.getUserService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		userLogger.info("修改用户信息："+json);
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String phone = jsonObj.getString(JsonUtil.PHONE_NODE);
			String deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
			String pwd = jsonObj.getString(JsonUtil.USER_PWD_NODE);
			String userName = jsonObj.getString(JsonUtil.USER_NAME_NODE);
			String userSex = jsonObj.getString(JsonUtil.USER_SEX_NODE);
			String userImg = jsonObj.getString(JsonUtil.USER_IMG_NODE);
			String locationPublic = jsonObj.getString(JsonUtil.LOCATION_PUBLIC_NODE);
			String uid = jsonObj.getString(JsonUtil.UID_NODE);
			String screenName=jsonObj.getString(JsonUtil.USER_SCREEN_NAME);
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuffer repStr = new StringBuffer();
			try{
				String verifyCode = jsonObj.getString(JsonUtil.VERIFY_CODE);
				if (!userService.checkVerifyCode(phone, verifyCode)) {
					repStr.append("{").append(JsonUtil.RESULT_NODE)
							.append(":\"")
							.append(JsonUtil.RESULT_CHECK_VERIFYCODE_FAILED)
							.append("\"}");
					out.print(repStr);
					return;
				}
			}catch (JSONException e) {
				userLogger.error(e.getMessage(),e);
			}
			
			User user = new User();
			user.setDeviceId(deviceId);
			user.setPhone(phone);
			user.setPwd(pwd);
			user.setUid(uid);
			user.setScreenName(screenName);
			user.setImgPath(userImg);
			user.setName(userName);
			user.setGender(userSex);
			user.setLocationPublic(locationPublic);
			userService.saveOrUpdateUserInfo(user);
			repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
					.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");
			out.print(repStr);
			
		} catch (JSONException e) {
			userLogger.error(e.getMessage(),e);
		}

	}

}
