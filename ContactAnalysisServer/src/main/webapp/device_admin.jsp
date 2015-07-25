<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.warren.contact.server.domain.Contact"%>
<%@ page import="com.warren.contact.server.domain.User"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.warren.contact.server.domain.PhoneRawInfo"%>
<%@ page import="com.warren.contact.server.domain.PhoneUpdateInfo"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style type="text/css">
<!--

.scrollbar { 
    FONT-SIZE: 12px; 
 scrollbar-face-color:#FAFAFA; 
 scrollbar-shadow-color:#E0E0E0; 
 scrollbar-highlight-color:#E0E0E0; 
 scrollbar-3dlight-color:#FAFAFA; 
 scrollbar-darkshadow-color:#FAFAFA; 
 scrollbar-track-color:#FAFAFA; 
 scrollbar-arrow-color:#E0E0E0;
 overflow-y:auto;
 width:99%;
}
-->
</style>                        
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>寻朋管理后台-设备信息</title>
</head>
<b>
所有的设备以及联系人信息：
</b>
<form action="/phoneRawInfoAdminServlet" method="post">
<input type="submit" value="查询">
</form>
<table border="0" cellpadding="3" cellspacing="1"  align="center" style="background-color: #b9d8f3;" scrolling="yes">
<%
List<PhoneUpdateInfo> fullContactList = (List<PhoneUpdateInfo>)request.getAttribute("phoneUpdateList");
if(null!=fullContactList) {
	%>
	<br><b>一共有记录<%=fullContactList.size()%></b>
	<%
	 for(int i=0;i<fullContactList.size();i++) {
		 
%>
<tr style="text-align: left; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
<td>设备号：<%=fullContactList.get(i).getDeviceId()%></td>
<td>最近变更时间:<br><%
	Date time = fullContactList.get(i).getUpdateTime();
    String timeString = "";
if(time!=null) {
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分",Locale.CHINA); 
			 timeString= dateformat.format(time);
}
%>
<%=timeString%>
</td>
<td>联系人列表：<br><%=request.getAttribute(fullContactList.get(i).getDeviceId())%>

</td>
<td >变更记录:
</td>
</tr>

<%} }%>
</table>

</body>
</html>