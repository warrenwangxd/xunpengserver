package com.warren.contact.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test1Controller {
	@RequestMapping("/test1.action")
	public String pagefor(HttpServletRequest request,HttpServletResponse response){
	return "index";
	}

}
