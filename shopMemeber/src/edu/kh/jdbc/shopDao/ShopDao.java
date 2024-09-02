package edu.kh.jdbc.shopDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.kh.jdbc.dto.ShopMember;

public class ShopDao {
	private PreparedStatement pstmt = null;
	
	private ResultSet rs = null;
	
	public ShopMember selectMember(Connection conn, String memberId) {
		ShopMember sm = null;
		
		try {
			String sql = "SELECT * FROM SHOP_MEMBER WHERE MEMBER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			
		//	pstmt.setString(1, memberId);
			
			// SQL 수행 후 결과 반환 받기
			//rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String id = rs.getString("MEMBER_ID");
				String pw = rs.getString("MEMBER_PW");
				String phone = rs.getString("PHONE");
				String gender = rs.getString("성별");
				// 사용되지 않은 별칭으로 성별이 아닌 GENDER 를 사용해야 한다
				
				sm = new ShopMember(id, pw, phone, gender);
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				/* 8. 사용한 JDBC 객체 자원 반환 */
				if(pstmt != null) pstmt.close();

				if(rs  != null) rs.close();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		return sm;
		
	}
	
	
}
