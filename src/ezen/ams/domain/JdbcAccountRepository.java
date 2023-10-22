package ezen.ams.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RDB를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제)
 * 
 * @author 박상훈
 * 
 */
public class JdbcAccountRepository implements AccountRepository{
	
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String userid = "hr";
	private static String pwd = "hr";
	
	private Connection con;
	

	public JdbcAccountRepository() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, userid, pwd);
	}
	
	/**
	 * 전체 계좌 목록 수 반환
	 * @return 목록수
	 * @throws SQLException 
	 */
	public int getCount()  {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT count(*) cnt")
			.append(" FROM account");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
		pstmt= con.prepareStatement(sb.toString());
		rs= pstmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt("cnt");
		}
		}catch (Exception e) {
			//컴파일 예외를 런타임예외로  변환
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch (Exception e) {}
		}return count;
		
	}
		
	


	/**
	 * 전체 계좌 목록 조회
	 * 
	 * @return 전체계좌 목록
	 */
	public List<Account> getAccounts() {
		List<Account> list = null;
		Account account = null;
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT * ")
			.append(" FROM account");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		pstmt= con.prepareStatement(sb.toString());
		rs = pstmt.executeQuery();
		list = new ArrayList<Account>();
		while(rs.next()) {
			String accountNum = rs.getString("account_num");
			String accountOwner = rs.getString("name");
			int pwd = rs.getInt("password");
			long accountRestMoney = rs.getLong("rest_money");
			long accountBorrowMoney = rs.getLong("borrow_money");
			int accountType = rs.getInt("type_id");
			
			if (accountType == 0) {
				account = new Account();
			}else if(accountType ==1) {
				account = new MinusAccount();
			}
			account.setAccountNum(accountNum);
			account.setAccountOwner(accountOwner);
			account.setPasswd(pwd);
			if (accountType == 1) {
				account.setRestMoney(accountRestMoney + accountBorrowMoney);
				((MinusAccount) account).setBorrowMoney(accountBorrowMoney);
			} else {
				account.setRestMoney(accountRestMoney);
			}
			list.add(account);
		}
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch (Exception e) {}
		}
		return list;
	}
	
	/**
	 * 신규계좌 등록
	 * @param account 신규계좌
	 * @return 등록여부	
	 */
	public boolean addAccount(Account account){
		
		
		boolean success = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO account (")
		  .append("     account_num,")
		  .append("     name,")
		  .append("     password,")
		  .append("     rest_money,")
		  .append("     borrow_money,")
		  .append("    type_id) ")
		  .append(" VALUES (account_num_seq.NEXTVAL, ?, ?, ?, ?, ?)");
		
		PreparedStatement pstmt = null;
			
		try {
		pstmt= con.prepareStatement(sb.toString());
		pstmt.setString(1, account.getAccountOwner());
		pstmt.setInt(2, account.getPasswd());
		pstmt.setLong(3, account.getRestMoney());
		
		if(account instanceof MinusAccount) {
			pstmt.setLong(4, ((MinusAccount)account).getBorrowMoney());
			pstmt.setInt(5, 1);
		}else {
			pstmt.setLong(4, 0);
			pstmt.setInt(5, 0);
		}
		int count = pstmt.executeUpdate(); 
		success =count > 0;
		}catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			try {
				if(pstmt != null) pstmt.close();
			}catch (Exception e) {
			}
		}return success;
	}
	
	/**
	 * 
	 * @param 계좌번호로 계좌 검색
	 * @return 검색된 계좌
	 */
	public Account searchAccount(String accountNum) {
		Account account = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT")
		  .append("     account_num,")
		  .append("     name,")
		  .append("     password,")
		  .append("     rest_money,")
		  .append("     borrow_money,")
		  .append("     type_id")
		  .append(" FROM")
		  .append("     account")
		  .append(" WHERE")
		  .append("     account_num = ?");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {			
				String accNum = rs.getString("account_num");
				String accountOwner = rs.getString("name");
				int password = rs.getInt("password");
				long restMoney = rs.getLong("rest_money");
				long borrowMoney = rs.getLong("borrow_money");
				int accountType = rs.getInt("type_id");
				
				if (accountType == 0) {
					account = new Account();
				} else if (accountType == 1) {
					account = new MinusAccount();
				}
				account.setAccountNum(accNum);
				account.setAccountOwner(accountOwner);
				account.setPasswd(password);
				account.setRestMoney(restMoney);
				if (accountType == 1) {
					((MinusAccount) account).setBorrowMoney(borrowMoney);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
		return account;
	}
	
		
		/**
		 * 예금주 명으로 계좌 검색 기능
		 * @param accountNum 검색 예금주명
		 * @return 검색된 계좌목록
		 */
		public List<Account> searchAcountByOwner(String accountOwner) {
			Account account = null;
			List<Account> list = null;
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT")
			  .append("     account_num,")
			  .append("     name,")
			  .append("     password,")
			  .append("     rest_money,")
			  .append("     borrow_money,")
			  .append("     type_id")
			  .append(" FROM")
			  .append("     account")
			  .append(" WHERE")
			  .append("     name = ?");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
			pstmt= con.prepareStatement(sb.toString());
			pstmt.setString(1, accountOwner);
			rs = pstmt.executeQuery();
			list = new ArrayList<Account>();
			while(rs.next()) {
				String accountNum = rs.getString("account_num");
				String accOwner = rs.getString("name");
				int password = rs.getInt("password");
				long restMoney = rs.getLong("rest_money");
				long borrowMoney = rs.getLong("borrow_money");
				int accountType = rs.getInt("type_id");
				
				if (accountType == 0) {
					account = new Account();
				} else if (accountType == 1) {
					account = new MinusAccount();
				}
				account.setAccountNum(accountNum);
				account.setAccountOwner(accOwner);
				account.setPasswd(password);
				account.setRestMoney(restMoney);
				
				if (accountType == 1) {
					((MinusAccount) account).setBorrowMoney(borrowMoney);
				}
				list.add(account);
			}
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
				}catch (Exception e) {}
			}
			return list;
		}

	
		/**
		 * 계좌번호로 계좌 삭제 기능
		 * @param accountOwner 검색 계좌번호
		 * @return 계좌 삭제 여부
		 */
		public boolean removeAccount(String accountNum) {
		
			boolean success = false;
			StringBuilder sb = new StringBuilder();
			sb.append(" DELETE FROM account")
				.append(" WHERE account_num = ?");
			
			PreparedStatement pstmt = null;
			
			try {
			pstmt= con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			int count = pstmt.executeUpdate(); 
			success =count > 0;
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}finally {
				try {
					if(pstmt != null) pstmt.close();
				}catch (Exception e) {
				}
			}return success;
			
		}
		public void close() {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		//테스트용 main
//		public static void main(String[] args) throws ClassNotFoundException, SQLException {
//			AccountRepository accountRepository = new JdbcAccountRepository();
//			Account account = new Account();
//			account.setAccountOwner("김우빈");
//			account.setPasswd(1111);
//			account.setRestMoney(10000);
//			accountRepository.addAccount(account);
//			
////			MinusAccount ma = new MinusAccount();
////			ma.setAccountOwner("이대출");
////			ma.setPasswd(1111);
////			ma.setRestMoney(10000);
////			ma.setBorrowMoney(10000000);
////			accountRepository.addAccount(ma);
//			
//			System.out.println("계좌등록 완료...");
			
		
			
			
			
	
	
		}

