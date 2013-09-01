package com.ysm.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GeoPointServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String lon1 = (String) req.getParameter("lon");
		String lat1 = (String) req.getParameter("lat");

		double lon2 = Double.parseDouble(lon1);
		double lat2 = Double.parseDouble(lat1);

		String backJson = "[{'lon':" + (lon2 + 0.005) + ",'lat':"
				+ (lat2 - 0.005) + ",'carID':'116622'}," + "{'lon':" + (lon2 - 0.0026)
				+ ",'lat':" + (lat2 - 0.0032) + ",'carID':'116622'}" + ",{'lon':"
				+ (lon2 - 0.00081) + ",'lat':" + (lat2 + 0.0121) + ",'carID':'116622'}"
				+ ",{'lon':" + (lon2 + 0.0083) + ",'lat':" + (lat2 + 0.002)
				+ ",'carID':'116622'}" + ",{'lon':" + (lon2 + 0.0006) + ",'lat':"
				+ (lat2 + 0.0058) + ",'carID':'116622'}" + "]";
		
		//算法执行，搜索出周围的计程车
//		RunJobs job = new RunJobs();
//		String cmd1[] = new String[]{"cmd.exe","/C","java -jar C:\\Rtree.jar C:\\data.txt C:\\rtree.dat"};
//		job.toInvoke(cmd1);
//		
//		String cmd2[] = new String[]{"cmd.exe","/C","java -jar C:\\Query.jar C:\\rtree.dat 80.0 40.0"};
//		String jobResult = job.toInvoke(cmd2);
//
//		System.out.println(jobResult);
		PrintWriter pw = resp.getWriter();
		pw.write(backJson);
		pw.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
