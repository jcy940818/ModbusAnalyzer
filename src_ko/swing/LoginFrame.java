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
import src_ko.util.Util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

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
	public LoginFrame() {
		LoginFrame.isExist = true;
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
		loginId.setForeground(Color.DARK_GRAY);
		loginId.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		loginId.setBackground(Color.WHITE);
		loginId.setBounds(349, 93, 283, 47);		
		loginId.setColumns(10);
		loginId.addFocusListener(Util.focusListener);
		loginId.setFocusTraversalKeysEnabled(false);
		loginId.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
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
		loginPassword.setForeground(Color.DARK_GRAY);
		loginPassword.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		loginPassword.setBackground(Color.WHITE);
		loginPassword.setColumns(10);
		loginPassword.setBounds(349, 152, 283, 47);
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
		
		JLabel ID = new JLabel("ID");
		ID.setForeground(Color.DARK_GRAY);
		ID.setHorizontalAlignment(SwingConstants.CENTER);
		ID.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		ID.setBounds(300, 92, 41, 47);
		actualPanel.add(ID);
		
		JLabel PW = new JLabel("PW");
		PW.setHorizontalAlignment(SwingConstants.CENTER);
		PW.setForeground(Color.DARK_GRAY);
		PW.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		PW.setBounds(302, 152, 41, 47);
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
	}
	
	public void login() {				
		try {
			if(!ONION_Info.loginId.equalsIgnoreCase(loginId.getText())) {				
				if(ONION_Info.isOnionMeber(loginId.getText().trim())) {
					if(loginPassword.getText().trim().equalsIgnoreCase("1")) {
						loginSuccess(true);
					}else {
						Util.showMessage("<font color='red'>사용자 인증 실패</font>\n입력하신 Password 내용을 확인해주세요" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
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
			String member = ONION_Info.getMemberName(loginId.getText().trim());
			Information_Panel.loginOnionMemeber(loginId.getText().trim());	
			
			JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>Modbus Analyzer 사용자 인증 완료%s%s</span></font><br></html>", Util.separator, Util.separator));
			ImageIcon i = new Util().getIconResource2();
			msg.setIcon(i);
			msg.setFont(font);
						
			JLabel msg2 = new JLabel(String.format("<html><br><span text-align=center>%s%s%s%s&nbsp;환영합니다 %s님<br><br></span></html>",Util.separator, Util.separator, Util.separator, Util.separator ,member ,Util.separator));
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
		
		super.dispose();
	}

	
	@Override
	public void dispose() {
		LoginFrame.isExist = false;
		super.dispose();
	}
}
