package com.ysm.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginCheck extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//String user = (String)req.getParameter("user");
		//String passwd = (String)req.getParameter("passwd");
		
		PrintWriter pw = resp.getWriter();
		
//		if("a".equals(user) && "b".equals(passwd)){
			
			pw.write("success");
			
//		} else {
//			pw.write("");
//		}
		
		pw.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
