package com.warren.contact.server;

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
import com.warren.contact.server.domain.PhoneUpdateInfo;
import com.warren.contact.server.service.PhoneService;
import com.warren.contact.server.service.implement.PhoneServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;

public class UpdatePhoneInfoServlet extends HttpServlet {
	Logger logger = Logger.getLogger(UpdatePhoneInfoServlet.class);
	Logger phoneLogger = Logger.getLogger(Constants.PHONE_LOGGER_NAME);
	private PhoneService phoneService = new PhoneServiceImpl();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		try {
			PhoneUpdateInfo phone = new PhoneUpdateInfo();
			JSONObject jsonObj = new JSONObject(json);
			String deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
			String phoneType = jsonObj.getString(JsonUtil.PHONE_TYPE_NODE);
			String phoneNo = jsonObj.getString(JsonUtil.PHONE_NODE);
			phone.setDeviceId(deviceId);
			phone.setPhoneNo(phoneNo);
			phone.setPhoneType(phoneType);
			phoneService.updatePhoneInfo(phone);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuilder repStr = new StringBuilder();
			repStr.append("{");
			repStr.append(JsonUtil.RESULT_NODE).append(":\"")
					.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");
			out.println(repStr);

		} catch (JSONException e) {
			logger.error(json + e.getMessage(), e);
		}
	}

}
