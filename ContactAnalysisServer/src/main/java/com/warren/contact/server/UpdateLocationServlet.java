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

import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.implement.LocationServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;

public class UpdateLocationServlet extends HttpServlet {
	private LocationService locationService = new LocationServiceImpl();
	private static Logger logger = Logger.getLogger(UpdateLocationServlet.class);
	
	private String deviceId=null;
	private String phone=null;
	private String uid=null;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		Location location = new Location();

		try {
			JSONObject jsonObj = new JSONObject(json);
			try {
				 deviceId = jsonObj.getString(JsonUtil.DEVICE_NODE);
			}catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
			try {
				uid = jsonObj.getString(JsonUtil.UID_NODE);
			}catch (JSONException e) {
				logger.warn(e.getMessage(),e);
			}
			try {//兼容客户端v0.2版没有传递经纬度json数据，需要做异常catch.
			location.setLatitude(jsonObj.getString(JsonUtil.LOCATION_LATITUDE));
			location.setLogitude(jsonObj.getString(JsonUtil.LOCATION_LONGITUDE));
			phone= jsonObj.getString(JsonUtil.PHONE_NODE);
			}catch(JSONException e) {
				logger.warn("兼容v0.3版本之前没有传递位置数据,做了异常处理，不影响主程序",e);
			}
			location.setCity(jsonObj.getString(JsonUtil.LOCATION_CITY_NODE));
			location.setCommunity(jsonObj
					.getString(JsonUtil.LOCATION_COMMUNITY_NODE));
			location.setCountry(jsonObj
					.getString(JsonUtil.LOCATION_COUNTRY_NODE));
			location.setFullAddress(jsonObj
					.getString(JsonUtil.LOCATION_ADDRESS_NODE));
			location.setProvince(jsonObj
					.getString(JsonUtil.LOCATION_PROVINCE_NODE));
			User user = new User();
			user.setDeviceId(deviceId);
			user.setPhone(phone);
			user.setUid(uid);
			locationService.updateLocation(user,location);

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuilder repStr = new StringBuilder();
			repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
					.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");

			out.println(repStr);

		} catch (JSONException e) {
			logger.error(e);
		}
	}
}
