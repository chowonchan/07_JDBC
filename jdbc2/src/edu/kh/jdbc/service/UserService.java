package edu.kh.jdbc.service;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
//		클래스명.메서드명()이 아닌 메서드명() 만 작성해도 호출 가능하게 함
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDao;
import edu.kh.jdbc.dto.User;


// Service : 비지니스 로직 처리
// - DB에 CRUD 후 결과 반환 받기
//	 + DML 성공 여부에 따른 트랜잭션 제어 처리(commit/rollback)
//		--> commit/rollback에는 Connection 객체가 필요하기 때문에
//			Connection 객체를 Service에서 생성 후
//			Dao에 전달하는 형식의 코드를 작성하게됨


public class UserService {

	// 필드
	private UserDao dao = new UserDao();
	
	// 메서드
	
	/** 전달 받은 아이디와 일치하는 User 정보 반환
	 * @param input = 입력된 아이디
	 * @return 아이디가 일치하는 회원 정보, 없으면 null
	 */
	public User selectId(String input) {
		// 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// Dao 메서드 호출 후 결과 반환 받기
		User user = dao.selectId(conn, input);
		
		
		// 다 쓴 커넥션 닫기
		JDBCTemplate.close(conn);
		
		return user; // DB 조회 결과 반환
		
	}
	/**
	 * User 등록 서비스
	 * @param user : 입력 받은 id, pw, name
	 * @return 삽입 성공한 결과 행의 개수
	 * @throws Exception 
	 */
	public int insertUser(User user) throws Exception {
		
		// 1. Connection 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공(할게 없으면 넘어감)
		
		// 3. DAO 메서드(INSERT 수행) 호출 후 
		// 결과(삽입된 행의 개수, int) 반환 받기
		int result = dao.insertUser(conn, user);
		
		// 4. INSERT 결과에 따라 트랜잭션 제어 처리
		if(result > 0) { // INSERT 성공
			commit(conn);
		} else {
			rollback(conn);
		}
		
		// 5. Connection 반환 하기
		close(conn);
		
		// 6. 결과 반환
		return result;
	}
	
	
	/** User 전체 조회
	 * @return userList : 조회된 User가 담긴 List
	 * @throws Exception
	 */
	public List<User> selectAll() throws Exception{
		
		// 1. Connection 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공(없으면 넘어감)
		
		// 3. DAO 메서드(SELECT) 호출 후 
		//    결과(List<User>) 반환 받기
		List<User> userList = dao.selectAll(conn);
		
		// 4. DML인 경우 트랜잭션 처리
		//	  SELECT는 안해도 된다!
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return userList;
	}
	
	
	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회(SELECT)
	 * @return
	 * @throws Exception
	 */
	public List<User> selectName(String keyword) throws Exception{
		
		// 1. Connection 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공(없으면 넘어감)
		
		// 3. DAO 메서드(SELECT) 호출 후 
		//    결과(List<User>) 반환 받기
		List<User> searchList = dao.selectName(conn, keyword);
		
		// 4. DML인 경우 트랜잭션 처리
		//	  SELECT는 안해도 된다!
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return searchList;
	}
	
	
	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<User> selectUserNo(int number) throws Exception {
		
		Connection conn = getConnection();
		
		List<User> searchnumberList = dao.selectUserNo(conn, number);
		
		close(conn);
		
		// 6. 결과 반환
		return searchnumberList;
	}
	
	/**
	 * 5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)
	 * @param rem
	 * @return
	 * @throws Exception
	 */
	public int deleteNo(int input) throws Exception {
		Connection conn = getConnection();
		
		int result = dao.deleteNo(conn, input);
		
		// 결과에 따라 트랜잭션 제어 처리

		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}
	
	/** 아이디, 비밀번호가 일치하는 User의 USER_NO 조회
	 * @param userId
	 * @param userPw
	 * @return userNo
	 * @throws Exception
	 */
	public int selectUserNo(String userId, String userPw) throws Exception {
		
		Connection conn = getConnection(); // 커넥션 생성
		
		// DAO 호출 후 결과 반환 받기
		int userNo = dao.selectUser(conn, userId, userPw);
		
		close(conn);
		
		return userNo;
		
	}
	
	/**
	 * userNo가 일치하는 User의 이름 수정
	 * @param userName
	 * @param userNo
	 * @return result
	 * @throws Exception
	 */
	public int updateName(String userName, int userNo)throws Exception {
		Connection conn = getConnection(); // 커넥션 생성
		
		int result = dao.updateName(conn, userName, userNo);
		
		if(result > 0 )commit(conn);
		else		   rollback(conn);
		
		close(conn);
		
		return result;
	}
	
	/**
	 * 7. 아이디 중복 체크
	 * @param userId
	 * @return count
	 * @throws Exception
	 */
	public int idCheck(String userId) throws Exception {
		Connection conn = getConnection();
		
		int count = dao.idCheck(conn, userId);
		
		close(conn);		
		
		return count;
	}
	
	/** userList에 있는 모든 user INSERT하기 
	 * @param userList
	 * @return result : 삽입된 행의 개수
	 * @throws Exception
	 */
	public int multiInsertUser(List<User> userList) throws Exception {
		
		Connection conn = getConnection();
		
		// 다중 insert 방법
		// 1) SQL을 이용한 다중 INSERT (속도빠름)
		// 2) Java 반복문을 이용한 다중 INSERT (이거 사용 !!) (속도 느림)

		int count = 0; // 삽입 성공한 행의 개수 count
		
		for(User user : userList) {
			int result = dao.insertUser(conn, user);
			
			count += result; // 삽입 성공한 행의 개수를 count 누적
		}
		
		// 전체 삽입 성공시 commit / 아니면 rollback
		if(count == userList.size()) commit(conn);
		else 						 rollback(conn);
		
		return count;
	}
	
	
	
	
	
	
	
}
