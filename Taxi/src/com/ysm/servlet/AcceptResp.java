package com.ysm.servlet;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ysm.model.CarBackListener;

@SuppressWarnings("serial")
public class AcceptResp extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userID = (String)req.getParameter("userID");
		
		Map<String,String> map = CarBackListener.getCarBackListener().getMap();
		
		if(map.containsKey(userID)){
			map.remove(userID);
		}
		map.put(userID, "yes");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
