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
 * ���ŷ���.
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
		// SmsUtil.sendMeg("18081967335", "��֤��Ϊ1223");
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
	 * ����: duanxin.cm JAVA HTTP�ӿ� ���Ͷ��� �޸�����: 2014-03-19 ˵��:
	 * http://api.duanxin.cm
	 * /?ac=send&username=�˺�&password=MD5λ32����&phone=����&content=���� ״̬: 100 ���ͳɹ�
	 * 101 ��֤ʧ�� 102 ���Ų��� 103 ����ʧ�� 104 �Ƿ��ַ� 105 ���ݹ��� 106 ������� 107 Ƶ�ʹ��� 108 �������ݿ�
	 * 109 �˺Ŷ��� 110 ��ֹƵ���������� 111 ϵͳ�ݶ����� 112 ���벻��ȷ 120 ϵͳ����
	 * 
	 * @throws IOException
	 */
	public  boolean sendMeg(String phone, String content)
			throws IOException {
		if(!isMobileNO(phone)) {
			logger.warn("�ֻ��Ÿ�ʽ����:"+phone);
			return false;
		}

		// ����StringBuffer�������������ַ���
		StringBuffer sb = new StringBuffer(smsServerUrl);

		// ��StringBuffer׷���û���
		sb.append("action=send&username=").append(userName);

		// ��StringBuffer׷�����루�������MD5 32λ Сд��
		sb.append("&password=").append(passwd);

		// ��StringBuffer׷���ֻ�����
		sb.append("&phone=").append(phone);

		// ��StringBuffer׷����Ϣ����תURL��׼��
		sb.append("&content=" + URLEncoder.encode(content, "GBK"));
		if (logger.isInfoEnabled()) {
			logger.info("���͵Ķ������ݣ�" + sb.toString());
		}

		// ����url����
		URL url = new URL(sb.toString());

		// ��url����
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// ����url����ʽ 'get' ���� 'post'
		connection.setRequestMethod("POST");

		// ����
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		// ���ط��ͽ��
		String inputline = in.readLine();

		if (inputline.equals("100")) {
			return true;
		} else {
			logger.error("���ŷ��ͽ����:" + inputline);
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
	 * ��ȡ��
	 * 
	 * @return
	 * @throws IOException
	 */
	public  String getBalance() throws IOException {

		// ����StringBuffer�������������ַ���
		StringBuffer sb = new StringBuffer(smsServerUrl);

		// ��StringBuffer׷���û���
		sb.append("action=getBlanace&username=").append(userName);

		// ��StringBuffer׷�����루�������MD5 32λ Сд��
		sb.append("&password=").append(passwd);

		// ����url����
		URL url = new URL(sb.toString());

		// ��url����
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// ����url����ʽ 'get' ���� 'post'
		connection.setRequestMethod("POST");

		// ����
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		// ���ط��ͽ��
		String inputline = in.readLine();

		return inputline;
	}

}
