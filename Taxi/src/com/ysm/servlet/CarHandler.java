package com.ysm.servlet;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ysm.model.CarInfo;
import com.ysm.model.CarMap;
import com.ysm.model.FileHandlerThread;
import com.ysm.model.FromUserInfo;
import com.ysm.model.UserMap;


@SuppressWarnings("serial")
public class CarHandler extends HttpServlet {
	private String tell = "yes";
	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String lon1 = (String) req.getParameter("lon");
		String lat1 = (String) req.getParameter("lat");
		String carID = (String)req.getParameter("carID");

		double lon2 = Double.parseDouble(lon1);
		double lat2 = Double.parseDouble(lat1);
		
		//将上传的车信息保存在一个新的CarInfo对象中，并将其存入CarMap中
		CarInfo carInfo = new CarInfo();
		carInfo.setCarID(carID);
		carInfo.setLat(lat2);
		carInfo.setLon(lon2);
		CarMap.getCarMap().getMap().put(carID, carInfo);
		
		/*将车信息写入文件*/
		String path = req.getRealPath("/date");
		File file = new File(path+"\\carData.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.seek(raf.length());
		String content = carID+" "+lon2+" "+lat2;
		raf.write((content+System.getProperty("line.separator")).getBytes());
		raf.close();

		//在该Servlet第一次被访问时，启动线程(每两分钟更新文件)
		String temp = req.getSession().getServletContext().getInitParameter("isFirstTime");
		if(temp.equals(tell)){
			tell = "no";
			new FileHandlerThread(path+"\\carData.txt",file).start();
		}
		
		
		//查看是否有用户的请求信息
		PrintWriter pw = resp.getWriter();
		if(UserMap.getUserMap().getMap().containsKey(carID)){
			FromUserInfo c = UserMap.getUserMap().getMap().get(carID);
			double lon3 = c.getLon();
			double lat3 = c.getLat();
			String addr = c.getUserAddr();
			String userID = c.getUserID();
			String destination = c.getDestination();
			String someone = lon3+"&"+lat3+"&"+userID+"&"+addr+"&"+destination;
			
			UserMap.getUserMap().getMap().remove(carID);
			pw.write(someone);
		}else{
			pw.write("");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
