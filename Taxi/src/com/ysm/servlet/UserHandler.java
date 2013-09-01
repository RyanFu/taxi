package com.ysm.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ysm.model.FromUserInfo;
import com.ysm.model.UserMap;

@SuppressWarnings("serial")
public class UserHandler extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String lon1 = (String) req.getParameter("lon");
		String lat1 = (String) req.getParameter("lat");
		String userAddr = (String)req.getParameter("userAddr");
		String carID = (String)req.getParameter("carID");
		String userID = (String)req.getParameter("userID");
		String destination = (String)req.getParameter("destination");
		double lon2 = Double.parseDouble(lon1);
		double lat2 = Double.parseDouble(lat1);
		
		//将用户信息保存在一个新的FromUserInfo对象中，并将其与被请求车的carID存入UserMap
		FromUserInfo fui = new FromUserInfo();
		fui.setLon(lon2);
		fui.setLat(lat2);
		fui.setUserAddr(userAddr);
		fui.setUserID(userID);
		fui.setDestination(destination);
		UserMap.getUserMap().map.put(carID, fui);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
