package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Practice1 {

	public static void main(String[] args) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs 	= null;
		
		int gender = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "KH_CWC";
			String password = "KH1234";
			
			conn = DriverManager.getConnection(url, userName, password);
			
			conn.setAutoCommit(false);
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("조회할 성별(M/F) : ");
			String gen = sc.next();
			
			System.out.print("급여 범위(최소, 최대 순서로 작성) :  ");
			int min = sc.nextInt();
			int max = sc.nextInt();
			
			System.out.print("급여 정렬(1.ASC, 2.DESC) : ");
			int input = sc.nextInt();

			
			String sql = """
				SELECT EMP_ID, EMP_NAME, DECODE(SUBSTR(EMP_NO,8,1),'1','F','2','M') "성별", 
				SALARY, JOB_NAME, NVL(DEPT_TITLE,'없음') DEPT_TITLE
				FROM EMPLOYEE
				JOIN JOB USING(JOB_CODE)
				JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)
				WHERE DECODE(SUBSTR(EMP_NO,8,1),'1','F','2','M') = ?
				AND SALARY BETWEEN ? AND ?
				ORDER BY SALARY 
				""";
			
			if(input == 1) sql += " ASC";
			if(input == 2) sql += " DESC";
				

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, gen);
			pstmt.setInt(2, min);
			pstmt.setInt(3, max);
						
			rs = pstmt.executeQuery();
			
			System.out.println("사번 | 이름   | 성별 | 급여    | 직급명 | 부서명");
			System.out.println("---------------------------------------------------");
			
			boolean flag = true;
			
			while(rs.next()) {
				flag = false;
				
				String empId 	 = rs.getString("EMP_ID");
				String empName   = rs.getString("EMP_NAME");
				String ge	 	 = rs.getString("성별");
				int salary		 = rs.getInt("SALARY");
				String jobName   = rs.getString("JOB_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				
				System.out.printf("%s | %s | %s | %d | %s | %s \n"
						,empId, empName, ge, salary, jobName ,deptTitle);
			}
			
			if(flag) { // flag == true인 경우 -> 조회 결과 없음
				System.out.println("조회 결과 없음");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn  != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
