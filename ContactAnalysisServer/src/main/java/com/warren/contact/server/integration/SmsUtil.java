package com.warren.contact.server.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

/**
 * 短信服务.
 * 
 * @author dong.wangxd
 * 
 */
@EnableAutoConfiguration
@Service("smsUtil")
public class SmsUtil {
	private static Logger logger = Logger.getLogger(SmsUtil.class);
    @Value("${sms_user_name}")
    private  String userName  ;
    @Value("${sms_passwd}")
	private  String passwd;
    @Value("${sms_server_url}")
	private  String smsServerUrl;
    
    @Value("sms_verify_code_sent_interval_time")
	private String smsIntervalSentTime;
    @Value("sms_verify_code_prefix")
    private String smsVerifyCodeContentPrefix;
    @Value("website_url")
    private String webSiteUrl;

	public Long getSmsIntervalSentTime() {
		return Long.valueOf(smsIntervalSentTime);
	}




	public String getSmsVerifyCodeContentPrefix() {
		return smsVerifyCodeContentPrefix;
	}



	public void setSmsVerifyCodeContentPrefix(String smsVerifyCodeContentPrefix) {
		this.smsVerifyCodeContentPrefix = smsVerifyCodeContentPrefix;
	}



	public String getWebSiteUrl() {
		return webSiteUrl;
	}



	public void setWebSiteUrl(String webSiteUrl) {
		this.webSiteUrl = webSiteUrl;
	}



	public static void main(String[] args) throws IOException {
		// SmsUtil.sendMeg("18081967335", "验证码为1223");
		System.out.println(new SmsUtil().getBalance());

	}
	


	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPasswd() {
		return passwd;
	}



	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}



	public String getSmsServerUrl() {
		return smsServerUrl;
	}



	public void setSmsServerUrl(String smsServerUrl) {
		this.smsServerUrl = smsServerUrl;
	}



	public static String genVerifyCode() {
		int min = 1000;
		int max = 9999;
		Random random = new Random();
		return String.valueOf(random.nextInt(max - min + 1) + min);

	}

	/**
	 * 功能: duanxin.cm JAVA HTTP接口 发送短信 修改日期: 2014-03-19 说明:
	 * http://api.duanxin.cm
	 * /?ac=send&username=账号&password=MD5位32密码&phone=号码&content=内容 状态: 100 发送成功
	 * 101 验证失败 102 短信不足 103 操作失败 104 非法字符 105 内容过多 106 号码过多 107 频率过快 108 号码内容空
	 * 109 账号冻结 110 禁止频繁单条发送 111 系统暂定发送 112 号码不正确 120 系统升级
	 * 
	 * @throws IOException
	 */
	public  boolean sendMeg(String phone, String content)
			throws IOException {
		if(!isMobileNO(phone)) {
			logger.warn("手机号格式错误:"+phone);
			return false;
		}

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(smsServerUrl);

		// 向StringBuffer追加用户名
		sb.append("action=send&username=").append(userName);

		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
		sb.append("&password=").append(passwd);

		// 向StringBuffer追加手机号码
		sb.append("&phone=").append(phone);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode(content, "GBK"));
		if (logger.isInfoEnabled()) {
			logger.info("发送的短信内容：" + sb.toString());
		}

		// 创建url对象
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 'get' 或者 'post'
		connection.setRequestMethod("POST");

		// 发送
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		// 返回发送结果
		String inputline = in.readLine();

		if (inputline.equals("100")) {
			return true;
		} else {
			logger.error("短信发送结果码:" + inputline);
			return false;
		}
	}

	public  boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 获取余额。
	 * 
	 * @return
	 * @throws IOException
	 */
	public  String getBalance() throws IOException {

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(smsServerUrl);

		// 向StringBuffer追加用户名
		sb.append("action=getBlanace&username=").append(userName);

		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
		sb.append("&password=").append(passwd);

		// 创建url对象
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 'get' 或者 'post'
		connection.setRequestMethod("POST");

		// 发送
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		// 返回发送结果
		String inputline = in.readLine();

		return inputline;
	}

}
