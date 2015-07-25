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
<title>寻朋管理后台-首页</title>
</head>
<body>
<form action="/adminLoginServlet">
登录口令:<input type="text" name="token"/>
<input type="submit" value="登录"/>
</form>
<a href="/userAdminServlet"><font size="5">用户管理</font></a>
<br>
<a href="/phoneRawInfoAdminServlet"><font size="5">手机设备原始信息管理</font></a>
<br>
<a href="/mock_admin.jsp"><font size="5">数据mock</font></a>
</body>
</html>