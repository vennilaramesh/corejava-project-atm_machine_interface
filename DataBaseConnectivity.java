package com.core.java.sqlconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnectivity {
	static Connection con=null;
	static String url="jdbc:mysql://localhost:3306/atm_database";
	static String username="root";
	static String password="root123";
	public static Connection getConnection() {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");	
		con=DriverManager.getConnection(url, username, password);
		System.out.println("Connected");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;	
	}
}