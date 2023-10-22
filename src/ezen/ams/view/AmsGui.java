package ezen.ams.view;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.List;

import javax.swing.JOptionPane;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.AccountType;
import ezen.ams.domain.JdbcAccountRepository;
import ezen.ams.domain.MinusAccount;

/**
 * AMS GUI (GridBagLayout)
 * @author 박상훈
 *
 */
public class AmsGui extends Frame {

	Label amsk, amsNum, amsName, pwd, inputm, depom, amsList, moneywonL;
	Button refer, delt, search, regi, allre;
	Choice amskind;
	TextField numTF, nameTF, pwdTF, inputTF, depoTF;
	TextArea listTA;

	GridBagLayout grid;
	GridBagConstraints con;
	
	
	private AccountRepository repository;
	

	public AmsGui() {
		this("No-Title");
	}

	public AmsGui(String title) {
		super(title);
		
		
		try {
			repository = new JdbcAccountRepository();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		// 라벨
		amsk = new Label("계좌종류", Label.CENTER);
		amsNum = new Label("계좌번호", Label.CENTER);
		amsName = new Label("예금주명", Label.CENTER);
		pwd = new Label("비밀번호", Label.CENTER);
		inputm = new Label("입금금액", Label.CENTER);
		depom = new Label("대출금액", Label.CENTER);
		amsList = new Label("계좌목록", Label.CENTER);
		moneywonL = new Label("(단위 : 원)", Label.RIGHT);
		// 버튼
		refer = new Button("조회");
		delt = new Button("삭제");
		search = new Button("검색");
		regi = new Button("신규등록");
		allre = new Button("전체조회");
		// 텍스트프레임
		numTF = new TextField();
		nameTF = new TextField();
		pwdTF = new TextField();
		inputTF = new TextField();
		depoTF = new TextField();
		// 초이스 칸
		amskind = new Choice();
		AccountType[] accountTypes = AccountType.values();
		for (AccountType accountType : accountTypes) {
			amskind.add(accountType.getName());
		}
		listTA = new TextArea();

	}

//   컴포넌트 배치 (엑셀 칸 제일 작게해서 나눠보면 정확함
	public void init() {
		grid = new GridBagLayout();
		con = new GridBagConstraints();
		setLayout(grid);

		// 계좌 종류
		addComponent(amsk, 0, 0, 1, 1, 0.0);
		addComponent(amskind, 1, 0, 1, 1, 1.0);
		// 계좌 번호
		addComponent(amsNum, 0, 1, 1, 1, 0.0);
		addComponent(numTF, 1, 1, 1, 1, 1.0);
		addComponent(refer, 2, 1, 1, 1, 0.0);
		addComponent(delt, 3, 1, 1, 1, 0.0);
		// 예금주명
		addComponent(amsName, 0, 2, 1, 1, 0.0);
		addComponent(nameTF, 1, 2, 1, 1, 1.0);
		addComponent(search, 2, 2, 1, 1, 0.0);
		// 비밀번호
		addComponent(pwd, 0, 3, 1, 1, 0.0);
		addComponent(pwdTF, 1, 3, 1, 1, 1.0);
		pwdTF.setEchoChar('*');
		// 입금금액
		addComponent(inputm, 2, 3, 1, 1, 0.0);
		addComponent(inputTF, 3, 3, 2, 1, 1.0);
		// 대출금액
		addComponent(depom, 0, 4, 1, 1, 0.0);
		addComponent(depoTF, 1, 4, 1, 1, 1.0);
		addComponent(regi, 2, 4, 1, 1, 0.0);
		addComponent(allre, 3, 4, 1, 1, 0.0);
		// 계좌목록
		addComponent(amsList, 0, 5, 1, 1, 0.0);
		addComponent(moneywonL, 4, 5, 1, 1, 0.0);
		addComponent(listTA, 0, 6, 5, 4, 0.0);

	}

	// GridBagLayout을 이용한 컴포넌트 배치
	private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight,
			double weightx) {

		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(5, 5, 5, 5);
		con.weightx = weightx; // 가중치
		con.weighty = 0.0;
		con.gridx = gridx;
		con.gridy = gridy;
		con.gridwidth = gridwidth;
		con.gridheight = gridheight;
		grid.setConstraints(component, con);
		add(component);
	}

//  목록 헤더
	private void printHeader() {
		Formatter formatter = new Formatter();
		listTA.append("---------------------------------------------------------------------\n");
		formatter.format("%1$6s%2$6s%3$6s%4$6s%5$10s%6$14s\n", "계좌종류", "계좌번호", "예금주명", "비밀번호", "잔액", "대출금액");
		String str = formatter.toString();
		listTA.append(str);
		listTA.append("=====================================================================\n");
	}

// 	계좌 전체 목록 리스트불러오기
	public void allList() {
		resetTF();
		printHeader();
		List<Account> list = repository.getAccounts();
		for (Account account : list) {
			listTA.append(account.toString() + "\n");
		}
	}
	
	//계좌 등록하기
		public void addAccount() throws ClassNotFoundException, SQLException {
			Account account = null;
			String accountOwner = nameTF.getText();
			int password = Integer.parseInt(pwdTF.getText());
			long inputMoney = Long.parseLong(inputTF.getText());

			String selectedItem = amskind.getSelectedItem();
			// 입출금 계좌 등록
			if (selectedItem.equals(AccountType.INDE_ACCOUNT.getName())) {
				account = new Account(accountOwner, password, inputMoney);
			} else if (selectedItem.equals(AccountType.MINUS_ACCOUNT.getName())) {
				long loanMoney = Long.parseLong(depoTF.getText());
				account = new MinusAccount(accountOwner, password, inputMoney, loanMoney);
			}
			repository.addAccount(account);
			JOptionPane.showMessageDialog(this, "정상 등록 처리되었습니다.");
		}

			
// 	계좌번호로 계좌 조회
	public void searchAccNum() {
		String searchNum = numTF.getText(); 
		Account account = repository.searchAccount(searchNum);
		if (account != null) {
			String ownerName = account.getAccountOwner();
			listTA.setText("");
			printHeader();
			listTA.append(account.toString()+"\n");
			resetTF();
			JOptionPane.showMessageDialog(this, searchNum + "번 계좌는 " + ownerName + "님의 계좌입니다..");
		}else {
			JOptionPane.showMessageDialog(this, searchNum + "번 계좌는 존재하지 않는 계좌입니다..");
		}
	}
// 예금주 명으로 계좌 조회
	 public void searchAccountOwner() {
	        String accountOwner = nameTF.getText();
	        int count = 0;
	        List<Account> searchAccounts = repository.searchAcountByOwner(accountOwner);
	        if (searchAccounts.isEmpty()) {
	            JOptionPane.showMessageDialog(this, accountOwner + "님의 계좌가 존재하지 않습니다.");
	        } else {
	            resetTF();
	            printHeader();
	            for (Account account : searchAccounts) {
	                listTA.append(account.toString() + "\n");
	                count++;
	            }
	            JOptionPane.showMessageDialog(this, accountOwner + "님 계좌를"+count +"개 찾았습니다.");
	        }
	    }
//	 계좌 번호로 계좌 삭제하기
	 public void removeAccount() {
		String targetAcc = numTF.getText(); 
		boolean removed = repository.removeAccount(targetAcc);
		resetTF();
		if(removed) {
			JOptionPane.showMessageDialog(this, targetAcc + "번 계좌는 정상 삭제 되었습니다..");
		}else {
			JOptionPane.showMessageDialog(this, targetAcc +"번 계좌는 존재하지 않는 계좌입니다..");
		}
		
	 }
	
	// 계좌 종류별 입력창 활성화 
	public void selectAccountType(AccountType accountType) {
		switch (accountType) {
		case INDE_ACCOUNT:
			numTF.setEnabled(true);
			nameTF.setEnabled(true);
			inputTF.setEnabled(true);
			pwdTF.setEnabled(true);
			depoTF.setEnabled(false);
			break;
		case MINUS_ACCOUNT:
			depoTF.setEnabled(true);
			numTF.setEnabled(true);
			nameTF.setEnabled(true);
			inputTF.setEnabled(true);
			pwdTF.setEnabled(true);
			break;
		case ALL_ACCOUNT:
			depoTF.setEnabled(false);
			numTF.setEnabled(true);
			nameTF.setEnabled(true);
			inputTF.setEnabled(false);
			pwdTF.setEnabled(false);
			break;
		}

	}
	

//	종료버튼
	public void exit() {
		((JdbcAccountRepository)repository).close();
		setVisible(false);
		dispose();
		System.exit(0);
	}

//	※이벤트 소스에 이벤트 리스너 등록※
	public void eventHandling() {
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				//신규 등록
				if (eventSource == regi) {
					try {
						addAccount();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				//목록 전체 검색
				} else if (eventSource == allre) {
					allList();
			    //계좌 번호로 조회
				} else if (eventSource == refer) {
					searchAccNum();
				//예금주 명으로 조회	
				} else if (eventSource == search) {
					searchAccountOwner();
				//계좌 삭제	
				} else if (eventSource == delt) {
					removeAccount();
				}
					
			}
		}
		ActionListener actionListener = new ActionHandler();
		//종료처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		// 창이 열릴때
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				selectAccountType(AccountType.ALL_ACCOUNT);
				allList();
			}
		});

		// 계좌 등록
		regi.addActionListener(actionListener);
		// 계좌 조회
		refer.addActionListener(actionListener);
		// 계좌 삭제
		delt.addActionListener(actionListener);
		// 계좌 검색
		search.addActionListener(actionListener);
		// 계좌 전체 조회
		allre.addActionListener(actionListener);
		// 계좌 선택
		amskind.addItemListener(new ItemListener() {

		// 계좌 종류별 아이템 이벤트
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (amskind.getSelectedItem().equals("입출금 계좌")) {
						selectAccountType(AccountType.INDE_ACCOUNT);
					} else if (amskind.getSelectedItem().equals("마이너스 계좌")) {
						selectAccountType(AccountType.MINUS_ACCOUNT);
					} else if (amskind.getSelectedItem().equals("전체")) {
						selectAccountType(AccountType.ALL_ACCOUNT);
					}
				}

			}
		});


	}

	// 텍스트필드 초기화 메소드
	public void resetTF() {
		numTF.setText("");
		nameTF.setText("");
		pwdTF.setText("");
		inputTF.setText("");
		depoTF.setText("");
		listTA.setText("");
	}


}