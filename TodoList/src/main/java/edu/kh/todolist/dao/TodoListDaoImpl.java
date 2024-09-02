package edu.kh.todolist.dao;

import static edu.kh.todolist.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.todolist.dto.Todo;

public class TodoListDaoImpl implements TodoListDao{
	
	// JDBC 객체 참조 변수 + Properties 참조 변수 선언
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Properties prop; 
	
	public TodoListDaoImpl() {
		try {
			String filePath = 
					TodoListDaoImpl.class
					.getResource("/edu/kh/todolist/sql/sql.xml").getPath();
			
			// 지정된 경로의 XML 파일 내용을 읽어와
			// Properties 객체에 K:V 세팅
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(filePath));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public List<Todo> todoListFullView(Connection conn) throws Exception{
		List<Todo> todoList = new ArrayList<Todo>();
		
		try {
			String sql = prop.getProperty("todoListFullView");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				// Builder 패턴 : 특정 값으로 초기화된 객체를 쉽게 만드는 방법
				// -> Lombok에서 제공하는 @Builder 어노테이션을 DTO에 작성하면 사용 가능
				boolean complete = rs.getInt("TODO_COMPLETE") == 1;
				
				Todo todo = Todo.builder()
							.todoNo(rs.getInt("TODO_NO"))
							.title(rs.getString("TODO_TITLE"))
							.complete(complete)
							.regDate(rs.getString("REG_DATE"))
							.build();
				
				todoList.add(todo);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}		
		return todoList;
	}
	
	
	@Override
	public int getCompleteCount(Connection conn) throws Exception{
		int completeCount = 0;
		
		try {
			String sql = prop.getProperty("getCompleteCount");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				completeCount = rs.getInt(1);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}	
		
		return completeCount;
	}
	
	
	@Override
	public int todoAdd(Connection conn, String title, String detail) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoAdd");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, detail);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}	
		
		return result;
	}
	
	
	@Override
	public Todo todoDetailView(Connection conn, int todoNo) throws Exception {
		Todo todo = null;
		
		try {
			String sql = prop.getProperty("todoDetailView");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				boolean complete = rs.getInt("TODO_COMPLETE") == 1;
				
				todo = Todo.builder()
							.todoNo(rs.getInt("TODO_NO"))
							.title(rs.getString("TODO_TITLE"))
							.detail(rs.getString("TODO_DETAIL"))
							.complete(complete)
							.regDate(rs.getString("REG_DATE"))
							.build();
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}	
		
		return todo;
	}
	
	
	@Override
	public int todoComplete(Connection conn, int todoNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoComplete");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, todoNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}	
		
		return result;
	}
	
	
	@Override
	public int todoUpdate(Connection conn, int todoNo, String title, String detail) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoUpdate");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, detail);
			pstmt.setInt(3, todoNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}	
		
		return result;
	}
	
	
	@Override
	public int todoDelete(Connection conn, int todoNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoDelete");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, todoNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}	
		
		return result;
	}
}