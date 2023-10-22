package ezen.ams.exception;

/**
 * 사용자 정의 예외 클래스
 * 
 * @author 박상훈
 */
public class NotBalanceException extends Exception {
	private int errorCode; 
	public NotBalanceException() {
		super("예기치않은 오류가 발생하였습니다..");
	}
	public NotBalanceException(String message) {
		super(message);
	}
	
	public NotBalanceException(String message, int errorCode){
		super(message);
		this.errorCode = errorCode;
		
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public String toString() {
		return getMessage()+"[에러코드 : "+getErrorCode()+"]";
		}
}
