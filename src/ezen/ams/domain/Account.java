package ezen.ams.domain;

import java.io.Serializable;
import java.util.Formatter;

import ezen.ams.exception.NotBalanceException;

/**
 * 은행 계좌 추상화
 * 자바의 변수들 - 지역변수, 인스턴스변수, 스태틱(정적)변수
 * @author 박상훈
 */
public  class Account implements Serializable/* extends Object = 모든 표준 API에 부모*/ {
 
	//필드 캡슐화(은닉화)
	//인스턴스 변수들...
    private String accountNum;
    private String accountOwner;
    private int passwd;
    private long restMoney;
    
    public static final String BANK_NAME;
    private static int accountId;
    
//    초기화 블록
//    static 초기화 블록 
    static {
    	BANK_NAME = "상훈은행";
    	accountId=1000;
    }
    
    
//생성자 오버로딩
    public Account() {	}

    
	public Account( String accountOwner, int passwd, long restMoney) {
		this.accountNum = (accountId++) +"";
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}
	

	// setter/getter 메소드
    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public int getPasswd() {
        return passwd;
    }

    public void setPasswd(int passwd) {
        this.passwd = passwd;
    }

    public void setRestMoney(long restMoney) {
        this.restMoney = restMoney;
    }
    
    
    //기능 메소드

    /*
     * 입금 기능
     */
    public long deposit(long money) throws NotBalanceException{
    	//데이타 검증 및 예외처리
    	if (money <= 0) {
			throw new NotBalanceException("입금금액은 0이거나 음수일 수 없습니다.");
		}else if(money >= 100000000) {
			throw new NotBalanceException("1억 이상 입금할 수 없습니다.");
		}else if (money > restMoney) {
			throw new NotBalanceException("잔액이 부족합니다.");
		}
        return restMoney += money;
    }

    /*
     * 출금 기능
     */
    public long withdraw(long money) throws NotBalanceException{
    	//데이타 검증 및 예외처리
    	if (money <= 0) {
			throw new NotBalanceException("출금금액은 0이거나 음수일 수 없습니다.");
		}else if(money >= 100000000) {
			throw new NotBalanceException("1억 이상 출금할 수 없습니다.");
		}else if (money > restMoney) {
			throw new NotBalanceException("잔액이 부족합니다.");
		}
        return restMoney -= money;
    }

    /*
     * 잔액조회 기능
     */
    public long getRestMoney() {
        return restMoney;
    }

    /*
     * 비밀번호 확인 기능
     */
    public boolean checkPasswd(int pwd) {
        return passwd == pwd;
    }
    public void printInfo() {
    	System.out.println(accountNum+"\t"+accountOwner+"\t*****\t"+restMoney);
    }
    
    
    
    
    @Override
	@SuppressWarnings("resource")
	public String toString() {
		Formatter formatter = new Formatter();
        formatter.format("%1$6s%2$8s%3$8s%4$12s%5$,18d", "입출금계좌", accountNum, accountOwner, "****", restMoney);
        
        String str = formatter.toString();
		return str;
	}






@Override
public boolean equals(Object obj) {
		return toString().equals(obj.toString());
}


	//   스태틱(클래스) 메소드
    public static int getAccountId() {
    	return accountId;
    	
    }
    
 
}
    

