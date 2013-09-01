package com.ysm.dbutility;

import java.sql.Connection;


public class DBUtil {

	
	static JDBCFactoryImp fac = new JDBCFactoryImp();
	
	static Connection conn = null;
	
	public static Connection getConnection() throws Exception{
		
		//可选项为 "mysql" 和  "sql server"(2000)
		conn = fac.getConn("mysql");
			return conn;
		
	}
	
//	public static void closeConnection(Connection conn){
//		try {
//			if(conn != null){
//				conn.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
