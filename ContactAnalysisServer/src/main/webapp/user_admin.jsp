<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.warren.contact.server.domain.Contact"%>
<%@ page import="com.warren.contact.server.domain.FriendLocation"%>
<%@ page import="com.warren.contact.server.domain.Location"%>
<%@ page import="com.warren.contact.server.domain.User"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Set"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>寻朋管理后台</title>
</head>
<body>
<form action="/userAdminServlet" method="POST">
请输入要查询的手机:
<input name="phone" type="text"/>
<input type="hidden" name="operate" value="singleQuery"/>
<input type="submit" value="查询"/> 
</form>
<b>
基本信息：
</b>
<table border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<%
User user = (User)request.getAttribute("user");
if(null!=user) {
%>
<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
<td>手机：<%=user.getPhone()%></td>
<td>姓名：<%=user.getName()%></td>
</tr>
<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
<td>性别：<%=user.getGender()%></td>
<td>是否公开地理位置:<%=user.getLocationPublic()%></td>
</tr>
<%}%>
</table>
<b>
历史地理位置信息:
</b>
<table  border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<%
List<Location> locationList= (List<Location>)request.getAttribute("locationList");
if(null!=locationList) {
	%>
<%for(int i=0;i<locationList.size()&& i<10;i++) {
		Location loc = locationList.get(i);
		String locTime="";
		if(null!=loc.getLocTime()) {
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分",Locale.CHINA); 
			 locTime= dateformat.format(loc.getLocTime());
		}
		
		%>
		<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		<td>时间:<%=locTime%></td><td>地址:<%=loc.getCommunity()%></td>
		</tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		<tr>
		<td>经度:<%=loc.getLatitude()%></td><td>维度:<%=loc.getLogitude()%></td>
		</tr>
<%}
}%>
</table>
<b>
朋友最近位置信息:
</b>
<table border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<%
List<FriendLocation> friendLocationList= (List<FriendLocation>)request.getAttribute("friendLocationList");
if(null!=friendLocationList) {
for(int i=0;i<friendLocationList.size();i++) {
	FriendLocation friendLocation=friendLocationList.get(i);
    Location loc = friendLocation.getLocation();
	String locTime="";
		if(null!=loc.getLocTime()) {
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分",Locale.CHINA); 
			 locTime= dateformat.format(loc.getLocTime());
		}
%>
<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		<td>姓名:<%=friendLocation.getUser().getName()%></td><td>时间:<%=locTime%></td><td>地址:<%=loc.getCommunity()%></td>
		</tr>
		<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		<td>经度:<%=loc.getLatitude()%></td><td>维度:<%=loc.getLogitude()%></td>
		</tr>
<%}
}%>
</table>

<b>
与联系人共同的朋友:
</b>
<table border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<%
Map<String,List<Contact>> sameFriendMap= (Map<String,List<Contact>>)request.getAttribute("sameFriendMap");
if(null!=sameFriendMap) {
	Set<String> phoneSet= sameFriendMap.keySet();
						Iterator phoneIter =phoneSet.iterator();
						while(phoneIter.hasNext()) {
							String samePhoneKey = (String)phoneIter.next();			
							List<Contact> contacts =sameFriendMap.get(samePhoneKey);
							%>
<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold"><td><%=samePhoneKey%></td>
<td><%=contacts%></td></tr>
<%		
}}
%>
</table>
<b>查询所有的注册用户:</b>
<table border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<form name="form2" action="/userAdminServlet">
<input type="hidden" name="operate" value="getAllUser"/>
<input type="submit" value="查询"/>
</form>
</table>
<table  border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
<%
    List<User> userList = (List<User>)request.getAttribute("userList");
	if(userList!=null) {
		%>
		<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		<center><b>共计注册用户：<%=userList.size()%></b></center>
		</tr>
<%		for(int i=0;i<userList.size();i++) {
			User userInfo= userList.get(i);
			String regTime="无";
			if(userInfo.getRegisterTime()!=null) {
             SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分",Locale.CHINA); 
		     regTime= dateformat.format(userInfo.getRegisterTime());}
%>
<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
<td>姓名:<%=userInfo.getName()%></td><td>手机:<%=userInfo.getPhone()%></td><td>性别:<%=userInfo.getGender()%></td><td>设备号:<%=userInfo.getDeviceId()%></td><td>是否公开位置:<%=userInfo.getLocationPublic()%></td><td>注册时间:<%=regTime%></td>
<form name="deleteUserForm" id="deleteUserForm" action="/userAdminServlet" method="post">
<td>
<input type="hidden" name="phone" value="<%=userInfo.getPhone()%>">
<input type="hidden" name="operate" value="deleteUser">
<input type="submit" value="删除">
</td>
</form>
</tr>
<%}}%>
</table>

<b>全局位置信息开关:</b>
<form name="locationSwitchForm" action="/userAdminServlet">
<%
String locationPublic = (String)request.getAttribute("locationPublic");
%>
<input type="radio" name="locationPublic" value="on" <%="on".equals(locationPublic)?"checked":""%> />打开
<input type="radio" name="locationPublic" value="off" <%="off".equals(locationPublic)?"checked":""%>/>关闭 
<input type="submit" value="修改">
</form>

<b>用户注册短信校验码开关:</b>
<form name="userRegCheckCodeSwitchForm" action="/userAdminServlet">
<%
String userRegCheckCodeSwitch = (String)request.getAttribute("userRegCheckCodeSwitch");
%>
<input type="radio" name="userRegCheckCodeSwitch" value="on" <%="on".equals(userRegCheckCodeSwitch)?"checked":""%> />打开
<input type="radio" name="userRegCheckCodeSwitch" value="off" <%="off".equals(userRegCheckCodeSwitch)?"checked":""%>/>关闭 
<input type="submit" value="修改">
</form>
</body>
</html>