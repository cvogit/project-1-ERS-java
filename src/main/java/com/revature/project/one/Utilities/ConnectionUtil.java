package com.revature.project.one.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		String url = System.getenv("address");
		String port = System.getenv("port");
		String dbName = System.getenv("database_name");
		String dbSchema = "public";
		String username = System.getenv("username");
		String password = System.getenv("password");

		String dataSource = "jdbc:postgresql://" + url + ":" + port + "/" + dbName + "?currentSchema=" + dbSchema;

		return DriverManager.getConnection(dataSource, username, password);
	}
}
