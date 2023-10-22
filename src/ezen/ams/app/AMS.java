package ezen.ams.app;

import ezen.ams.view.AmsGui;

/**
 * 은행 계좌 관리 APP
 * 
 * @author 박상훈
 */
public class AMS {
	

	public static void main(String[] args) {
		AmsGui frame = new AmsGui("SH_BANK_AMS");
		frame.init();
		frame.setSize(600, 450);
		frame.setResizable(false);
		frame.eventHandling();
		frame.setVisible(true);
		
	
	}	
}
