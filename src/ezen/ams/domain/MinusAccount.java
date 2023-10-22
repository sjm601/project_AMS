package ezen.ams.domain;

import java.util.Formatter;

import ezen.ams.exception.NotBalanceException;

/**
 * 마이너스 계좌 클래스
 * 
 * @author 박상훈
 */
public class MinusAccount extends Account {
	
	private long  borrowMoney;
	
	public MinusAccount() {
	}
	
	public MinusAccount( String accountOwner, int passwd, long restMoney, long borrowMoney) {
		super(accountOwner, passwd, restMoney); 
		this.borrowMoney = borrowMoney;
	}
	
	
//	메소드 추가
	public long getBorrowMoney( ) {
		return borrowMoney;
	}	
	
	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	// 부모클래스 클래스의 메소드 재정의(Overriding)
	@Override 
	public long getRestMoney() {
	        return super.getRestMoney() - borrowMoney;
	    }

	@Override
	@SuppressWarnings("resource")
	public String toString() {
		Formatter formatter = new Formatter();
        formatter.format("%1$6s%2$6s%3$8s%4$12s%5$,18d%6$,15d", "마이너스계좌", getAccountNum(), getAccountOwner(), "****", getRestMoney(), borrowMoney);
        
        String str = formatter.toString();
		return str;
	}
	
	/**
	 * 대출 상환 기능
	 */
	@Override
	public long deposit(long money) throws NotBalanceException {

		if (money <= 0) {
			throw new NotBalanceException("상환금액은 0이거나 음수일 수 없습니다.");
		}
		return borrowMoney -= money;
	}
	
	/**
	 * 대출 기능
	 */
	@Override
	public long withdraw(long money) throws NotBalanceException {

		if (money <= 0) {
			throw new NotBalanceException("대출금액은 0이거나 음수일 수 없습니다.");
		}
		return borrowMoney += money;
	}
}


																																																																																																																																																							