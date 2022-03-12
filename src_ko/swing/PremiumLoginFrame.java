package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;

import src_ko.info.ONION_Info;
import src_ko.info.Program_Info;
import src_ko.main.Moon;
import src_ko.swing.TableFilterFrame.MyKeyListener;
import src_ko.util.Util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class PremiumLoginFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JTextField loginId;
	private JTextField loginPassword;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					LoginFrame frame = new LoginFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public PremiumLoginFrame(boolean isMoon) {
		PremiumLoginFrame.isExist = true;
		setResizable(false);
		setTitle("ModbusAnalyzer");		
			
		// 클래스 로더를 이용한 이미지 로딩
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// 프로젝트 Build Path에 이미지 리소스 디렉토리를 포함시켜야 한다.		
		setIconImage(new Util().getIconResource().getImage());		
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 695, 380);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		loginId = new JTextField();
		loginId.setForeground(Color.BLACK);
		loginId.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		loginId.setBackground(Color.WHITE);
		loginId.setBounds(349, 93, 296, 47);		
		loginId.setColumns(10);
		loginId.addFocusListener(Util.focusListener);
		loginId.setFocusTraversalKeysEnabled(false);
		loginId.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				String id = loginId.getText().trim();
				
				if(id.equalsIgnoreCase("Moon")) {
					loginId.setForeground(Color.BLUE);
				}else {
					loginId.setForeground(Color.BLACK);
				}
			}

			public void keyPressed(KeyEvent e) {
				String id = loginId.getText().trim();
				
				if(id.equalsIgnoreCase("Moon")) {
					loginId.setForeground(Color.BLUE);
				}else {
					loginId.setForeground(Color.BLACK);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					loginPassword.requestFocus();
			}
		});
		loginId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		actualPanel.add(loginId);		
				
		
		loginPassword = new JPasswordField();
		loginPassword.setForeground(Color.BLACK);
		loginPassword.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		loginPassword.setBackground(Color.WHITE);
		loginPassword.setColumns(10);
		loginPassword.setBounds(349, 152, 296, 47);
		loginPassword.addFocusListener(Util.focusListener);
		loginPassword.setFocusTraversalKeysEnabled(false);
		loginPassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					loginId.requestFocus();
			}
		});
		loginPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		actualPanel.add(loginPassword);
		
		JLabel ModbusAnalyzer = new JLabel("Modbus Analyzer");
		ModbusAnalyzer.setBackground(Color.WHITE);
		ModbusAnalyzer.setHorizontalAlignment(SwingConstants.CENTER);
		ModbusAnalyzer.setForeground(Color.BLACK);
		ModbusAnalyzer.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		ModbusAnalyzer.setBounds(380, 30, 206, 47);
		actualPanel.add(ModbusAnalyzer);
		
		JLabel ID = new JLabel("ID");
		ID.setBackground(Color.WHITE);
		ID.setForeground(Color.BLACK);
		ID.setHorizontalAlignment(SwingConstants.LEFT);
		ID.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		ID.setBounds(302, 92, 41, 47);
		actualPanel.add(ID);
		
		JLabel PW = new JLabel("PW");
		PW.setBackground(Color.WHITE);
		PW.setHorizontalAlignment(SwingConstants.LEFT);
		PW.setForeground(Color.BLACK);
		PW.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		PW.setBounds(300, 152, 41, 47);
		actualPanel.add(PW);
		
		JLabel imageLabel = new JLabel(new Util().getLoginImage(), JLabel.CENTER);
		imageLabel.setRequestFocusEnabled(false);
		imageLabel.setInheritsPopupMenu(false);
		imageLabel.setFocusTraversalKeysEnabled(false);
		imageLabel.setFocusable(false);
		imageLabel.setBounds(0, 0, 676, 346);		
		actualPanel.add(imageLabel);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setOpaque(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBounds(349, 248, 285, 47);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		actualPanel.add(btnNewButton);
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		// Moon 관리자 로그인
		if(isMoon) {
			loginId.setText("Moon");
			loginId.setForeground(Color.BLUE);
			loginPassword.requestFocus();
		}
		
		// 프레임 포커싱 상태에서 "ESC" 버튼을 클릭하면 프레임이 닫힌다
		MyKeyListener listener = new MyKeyListener();
		loginId.addKeyListener(listener);
		loginPassword.addKeyListener(listener);
		addKeyListener(listener);		
	}
	
	public void login() {
		try {
			if(!ONION_Info.loginId.equalsIgnoreCase(loginId.getText())) {				
				if(ONION_Info.isOnionMeber(loginId.getText().trim())) {
					
					if(loginId.getText().trim().equalsIgnoreCase("Moon")) {
						if(loginPassword.getText().trim().equalsIgnoreCase("940818")) {
							loginSuccess(true);
						}else {
							Util.showMessage("<font color='red'>관리자 인증 실패</font>\n입력하신 Password 내용을 확인해주세요" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						if(loginPassword.getText().trim().equalsIgnoreCase("1")) {
							loginSuccess(true);
						}else {
							Util.showMessage("<font color='red'>사용자 인증 실패</font>\n입력하신 Password 내용을 확인해주세요" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
						}
					}
													
				}else {
					Util.showMessage("<font color='red'>사용자 인증 실패</font>\n입력하신 ID 내용을 확인해주세요" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);	
				}				
			} else if(!ONION_Info.loginPassword.equalsIgnoreCase(loginPassword.getText())) {
				Util.showMessage("<font color='red'>사용자 인증 실패</font>\n입력하신 Password 내용을 확인해주세요" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			} else {				
				loginSuccess(false);
			}
		}catch(Exception e) {
			// 로그인 중 예외 발생 시 아무것도 수행하지 않음
		}		
	}
	
	
	public void loginSuccess(boolean isOnionMember) {
		Information_Panel.setVersion(Program_Info.PROGRAM_VERSION + " Premium");						
		
		ONION_Info.onionLogin = true;
		
		Font font = new Font("맑은 고딕", Font.BOLD, 22);
		Font font2 = new Font("맑은 고딕", Font.BOLD, 18);
		
		if(isOnionMember) {
			String longSeparator = Util.separator + Util.separator + Util.separator + Util.separator;
			
			String member = ONION_Info.getMemberName(loginId.getText().trim());
			Information_Panel.loginOnionMemeber(loginId.getText().trim());	
			
			JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>Modbus Analyzer 사용자 인증 완료%s%s</span></font><br></html>", Util.separator, Util.separator));
			ImageIcon i = new Util().getIconResource2();
			msg.setIcon(i);
			msg.setFont(font);
						
			JLabel msg2 = new JLabel(String.format("<html><br>%s&nbsp;<font color='orange'>ONION Member</font> %s님 환영합니다%s<br><br></html>",longSeparator ,member ,longSeparator));
			msg2.setForeground(Color.BLACK);
			
			msg2.setFont(font2);
			Object[] message = { msg, msg2 };
			
			JOptionPane.showMessageDialog(null, message, "ModbusAnalyer", JOptionPane.PLAIN_MESSAGE);
		}else {
			ONION_Info.userName = "ONION";
			
			JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>Modbus Analyzer 사용자 인증 완료%s</span></font><br></html>", Util.separator));
			ImageIcon i = new Util().getIconResource2();
			msg.setIcon(i);
			msg.setFont(font);
			Object[] message = { msg };
			
			JOptionPane.showMessageDialog(null, message, "ModbusAnalyer", JOptionPane.PLAIN_MESSAGE);
		}
						
		activeHiddenMenu();		
		super.dispose();
	}
	
	public void activeHiddenMenu() {
		MainFrame.activeDatabase(); // 데이터베이스 메뉴 활성화
		MainFrame.activeUtil(); // 유틸 메뉴 활성화 ( 현재 유틸 기능 미사용 )
		MainFrame.activeConnection(); // Connection 프리미엄 기능 활성화 (조건식 스캔, 예외 스캔)
		ModbusAgent_Panel.activationControl(); // 모드버스 제어 기능 활성화
		ModbusAgent_Panel.activationMK119(); // MK119 연동 기능 활성화		
		ModbusAgent_Panel.activationExpression(); // ModbusAgent 조건식 기능 활성화
		RX_Analysis_Panel.activationExpression(); // RX_Analysis 조건식 기능 활성화
		Multi_Analysis_Panel.activationExcpression(); // Multi_Analysis 조건식 기능 활성화
		RealTime_Panel.activationScaleFunction(); // RealTime_Panel 조건식 기능 활성화
		if(Moon.isMoon()) MainFrame.activeMoon(); // Moon 히든 메뉴 활성화
	}

	// 사용자 정의 키 이벤트 리스너
	class MyKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {				
				dispose();
			}
		}		
		
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				dispose();
			}
		}		
	}
	
	
	@Override
	public void dispose() {
		PremiumLoginFrame.isExist = false;
		super.dispose();
	}
}
