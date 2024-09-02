package edu.kh.todolist.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import edu.kh.todolist.common.JDBCTemplate;

public class JDBCTemplate {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		
		try {
			// 이전에 참조하던 Connection 객체가 존재하고
			// 아직 close된 상태가 아니라면
			if(conn != null && !conn.isClosed()) {
				return conn; // 새로 만들지 않고 기존 Connection반환
			}
			
			
			Properties prop = new Properties();

			String filePath = 
					JDBCTemplate.class
					.getResource("/edu/kh/jdbc/sql/driver.xml").getPath();
				
			prop.loadFromXML(new FileInputStream(filePath));
		
			Class.forName(prop.getProperty("driver") );
			
			String url = prop.getProperty("url");
			String userName = prop.getProperty("userName");
			String password = prop.getProperty("password");
			
			conn = DriverManager.getConnection(url, userName, password);
			
			// 만들어진 Connection에 AutoCommit 끄기
			conn.setAutoCommit(false);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}

}
