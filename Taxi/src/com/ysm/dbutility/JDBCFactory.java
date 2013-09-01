package com.ysm.dbutility;

import java.sql.Connection;

public interface JDBCFactory
{
	
	Connection getConn(String name) throws Exception; 

}
