<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>寻朋管理后台-数据mock</title>
</head>
<body>
<form action="/dataMockServlet">
<input type="submit" value="mock数据"/>
</form>
<%
String mockResult= (String)request.getAttribute("mockResult");
if(mockResult!=null&&mockResult.equals("true")) {
	%>
	<font color="blue" size="15"> mock数据完成</font>
<%
}
%>
</body>
</html>