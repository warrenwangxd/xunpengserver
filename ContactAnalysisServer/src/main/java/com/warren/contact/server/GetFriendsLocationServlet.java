package com.warren.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.implement.LocationServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
import com.warren.contact.server.util.StringUtils;

public class GetFriendsLocationServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(GetFriendsLocationServlet.class);
	private static LocationService locationService = new LocationServiceImpl();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		logger.info("【请求】查询朋友位置信息:"+json);

		try {
			JSONObject jsonObj = new JSONObject(json);
			try{
				String phone = jsonObj.getString(JsonUtil.PHONE_NODE);
				if(!StringUtils.isEmpty(phone)) {
					List<FriendLocation> friendLocationList = locationService
							.getFriendsLocation(phone);
					doResponse(response, friendLocationList, phone);
					return;
				}
			}catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
			try{
				String uid = jsonObj.getString(JsonUtil.UID_NODE);
				if(!StringUtils.isEmpty(uid)) {
					List<FriendLocation> friendLocationList = locationService
							.getFriendsLocationByUid(uid);
					doResponse(response, friendLocationList, uid);
					return;
				}
			
			}catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
		

		} catch (JSONException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	private void doResponse(HttpServletResponse response, List<FriendLocation> friendLocationList,String phone) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder repStr = new StringBuilder();
		repStr.append("{\"").append(phone).append("\"").append(":[");
		for (FriendLocation friendLocation : friendLocationList) {
			repStr.append('{');
			repStr.append(JsonUtil.PHONE_NODE).append(":\"").append(friendLocation.getUser().getPhone())
					.append("\",");
			repStr.append(JsonUtil.USER_NAME_NODE).append(":\"").append(friendLocation.getUser().getName())
					.append("\",");
			repStr.append(JsonUtil.USER_SEX_NODE).append(":\"").append(friendLocation.getUser().getGender()).append("\",");
			repStr.append(JsonUtil.LOCATION_LATITUDE).append(":\"").append(friendLocation.getLocation().getLatitude()).append("\",");
			repStr.append(JsonUtil.LOCATION_LONGITUDE).append(":\"").append(friendLocation.getLocation().getLogitude()).append("\",");
			repStr.append(JsonUtil.LOCATION_TIME).append(":\"").append(friendLocation.getLocation().getLocTime().getTime()).append("\",");
			repStr.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":\"").append(friendLocation.getUser().getLocationPublic()).append("\",");
			repStr.append(JsonUtil.LOCATION_FEATURE).append(":\"").append(friendLocation.getLocation().getFullAddress()).append("\"");
			repStr.append("},");
		}
		if(friendLocationList.size()!=0) {
			repStr.deleteCharAt(repStr.length() - 1);
		}
	
		repStr.append(']');
		repStr.append("}");
		logger.info("【响应】查询朋友位置信息:"+repStr);
		
		out.println(repStr);
	}

}
