package com.warren.contact.server.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.warren.contact.server.util.MD5Util;
import com.warren.contact.server.util.StringUtils;

public class AdminLoginServlet extends HttpServlet {
	public static String LOGIN_TOKEN="51255a558a479975ab8a937aeba0d654";
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		    String token = request.getParameter("token");
		    if(!StringUtils.isEmpty(token)) {
		    	if(LOGIN_TOKEN.equals(MD5Util.getMD5Code(token))) {
		    		HttpSession sesson= request.getSession();
		    		sesson.setAttribute("adminLogin", "login");
		    		//===response相关设置
					response.setContentType("text/html; charset=utf-8");
					ServletContext sc = getServletContext();
					RequestDispatcher rd = null;
					rd = sc.getRequestDispatcher("/index_admin.jsp"); // 定向的页面
					rd.forward(request, response);
		    	} 
		    	
		    
		    }
	
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
