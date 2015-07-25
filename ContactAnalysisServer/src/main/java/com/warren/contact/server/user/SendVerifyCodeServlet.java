package com.warren.contact.server.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.integration.SmsUtil;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;
import com.warren.contact.server.util.JsonUtil;
import com.warren.contact.server.util.StreamUtil;
/**
 * 发送验证码服务.
 * @author dong.wangxd
 *
 */
public class SendVerifyCodeServlet extends HttpServlet {
	@Autowired
	private UserService userService;
	@Autowired
	private SmsUtil smsUtil;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream jsonStream = request.getInputStream();
		byte[] data = StreamUtil.read(jsonStream);
		String json = new String(data, "UTF-8");
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String phone = jsonObj.getString(JsonUtil.PHONE_NODE);
			StringBuffer repStr = new StringBuffer();
			if(userService.isRegistered(phone)) {
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
				.append(JsonUtil.RESULT_PHONE_HAS_REGISTERED)
				.append("\"}");
			}
			else if(!userService.needSendVerifyCode(phone)) {
				repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
				.append(JsonUtil.RESULT_SEND_VERIFYCODE_HIGH_FREQUENCY)
				.append("\"}");
			} else {
				String verifyCode = SmsUtil.genVerifyCode();
				String sendContent = smsUtil.getSmsVerifyCodeContentPrefix()+verifyCode;
				if (smsUtil.sendMeg(phone, sendContent)) {
					userService.saveVerifyCode(phone, verifyCode);
					repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
							.append(JsonUtil.RESULT_TRUE_VALUE).append("\"}");
				} else {
					repStr.append("{").append(JsonUtil.RESULT_NODE).append(":\"")
							.append(JsonUtil.RESULT_SEND_VERIFYCODE_ERROR)
							.append("\"}");
				}
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
