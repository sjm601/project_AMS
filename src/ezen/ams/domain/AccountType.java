package ezen.ams.domain;

/**
 * 계좌 종류 Enum 클래스
 * 
 * @author 박상훈
 */
public enum AccountType {
	ALL_ACCOUNT("전체"), INDE_ACCOUNT("입출금 계좌"), MINUS_ACCOUNT("마이너스 계좌");
	
	private final String name;
	private AccountType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
