package com.ysm.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ysm.model.CarBackListener;

@SuppressWarnings("serial")
public class UserResp extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userID = (String)req.getParameter("userID");
		
		PrintWriter pw = resp.getWriter();
		
		CarBackListener cbl = CarBackListener.getCarBackListener();
		if(!cbl.getMap().containsKey(userID)){
			pw.write("wait");
		}else if("yes".equals(cbl.getMap().get(userID))){
			pw.write("yes");
			cbl.getMap().remove(userID);
		}else{
			pw.write("no");
			cbl.getMap().remove(userID);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
