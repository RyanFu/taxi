package com.ysm.dbutility;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCFactoryImp implements JDBCFactory
{
	//name is the type of dbms
	public Connection getConn(String name) throws Exception
	{
		Connection conn = null;
		if(name.equalsIgnoreCase("sql server"))
		{
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance(); 
			String url="jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=djdb"; 
			String user="sa"; 
			String password="123456"; 
			conn= DriverManager.getConnection(url,user,password);
			return conn;
		}
		else if(name.equalsIgnoreCase("mysql"))
		{
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver);
			String url = "jdbc:mysql://localhost:3306/test";
			String user = "root";
			String password = "qinyuan323";
			conn= DriverManager.getConnection(url,user,password); 
			return conn;
		}
		
		return conn;
	}
}
