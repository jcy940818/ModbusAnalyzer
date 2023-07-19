package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.OnionMember;
import common.Program_Info;
import common.util.FontManager;
import src_en.info.ONION_Info;
import src_en.main.MoonInspector;
import src_en.util.Util;

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
	public PremiumLoginFrame(boolean isMoon, boolean isVisible) {
		PremiumLoginFrame.isExist = true;
		setResizable(false);
		setTitle("ModbusAnalyzer");		
			
		// Ŭ���� �δ��� �̿��� �̹��� �ε�
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// ������Ʈ Build Path�� �̹��� ���ҽ� ���丮�� ���Խ��Ѿ� �Ѵ�.		
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
		loginId.setFont(FontManager.getFont(Font.BOLD, 20));
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
		loginPassword.setFont(FontManager.getFont(Font.BOLD, 20));
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
		ModbusAnalyzer.setFont(FontManager.getFont(Font.BOLD, 24));
		ModbusAnalyzer.setBounds(380, 30, 206, 47);
		actualPanel.add(ModbusAnalyzer);
		
		JLabel ID = new JLabel("ID");
		ID.setBackground(Color.WHITE);
		ID.setForeground(Color.BLACK);
		ID.setHorizontalAlignment(SwingConstants.LEFT);
		ID.setFont(FontManager.getFont(Font.BOLD, 22));
		ID.setBounds(302, 92, 41, 47);
		actualPanel.add(ID);
		
		JLabel PW = new JLabel("PW");
		PW.setBackground(Color.WHITE);
		PW.setHorizontalAlignment(SwingConstants.LEFT);
		PW.setForeground(Color.BLACK);
		PW.setFont(FontManager.getFont(Font.BOLD, 20));
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
		
		// �������� ȭ�� ������� �����ȴ�		
		setLocationRelativeTo(null);
		setVisible(isVisible);
		
		
		// Moon ������ �α���
		if(isMoon) {
			loginId.setText("Moon");
			loginId.setForeground(Color.BLUE);
			loginPassword.requestFocus();
		}
		
		// ������ ��Ŀ�� ���¿��� "ESC" ��ư�� Ŭ���ϸ� �������� ������
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
							Util.showMessage("<font color='red'>Administrator authentication Fail</font>\nPlease check your password" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						if(loginPassword.getText().trim().equalsIgnoreCase("1")) {
							loginSuccess(true);
						}else {
							Util.showMessage("<font color='red'>User authentication failed</font>\nPlease check your password" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
						}
					}
													
				}else {
					Util.showMessage("<font color='red'>User authentication failed</font>\nPlease check your ID" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);	
				}				
			} else if(!ONION_Info.loginPassword.equalsIgnoreCase(loginPassword.getText())) {
				Util.showMessage("<font color='red'>User authentication failed</font>\nPlease check your password" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			} else {				
				loginSuccess(false);
			}
		}catch(Exception e) {
			// �α��� �� ���� �߻� �� �ƹ��͵� �������� ����
		}		
	}
	
	
	public void loginSuccess(boolean isOnionMember) {
		Information_Panel.setVersion(Program_Info.PROGRAM_VERSION + " Premium");
		src_ko.swing.Information_Panel.setVersion(Program_Info.PROGRAM_VERSION + " Premium");
		
		ONION_Info.onionLogin = true;
		src_ko.info.ONION_Info.onionLogin = true;
		
		Font font = FontManager.getFont(Font.BOLD, 22);
		Font font2 = FontManager.getFont(Font.BOLD, 18);
		
		if(isOnionMember) {
			String longSeparator = Util.separator + Util.separator + Util.separator + Util.separator;
			
			OnionMember.loginUser = OnionMember.memberMap.get(loginId.getText().trim().toUpperCase());
			
			String member = ONION_Info.getMemberName(loginId.getText().trim());
			Information_Panel.loginOnionMemeber(loginId.getText().trim());	
			
			JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>Modbus Analyzer %s%s</span></font><br></html>", Util.separator, Util.separator));
			ImageIcon i = new Util().getIconResource2();
			msg.setIcon(i);
			msg.setFont(font);
						
			if(!OnionMember.memberMap.get(loginId.getText().toUpperCase().trim()).getTeam().contains("�̰���")) {
				member += "��";
			}
			
			JLabel msg2 = new JLabel(String.format("<html>%s&nbsp;<font color='orange'>Welcome! ONION Member</font> %s%s<br><br></html>",longSeparator ,member ,longSeparator));
			
			msg2.setFont(font2);
			Object[] message = { msg, msg2 };
			
			JOptionPane.showMessageDialog(null, message, "ModbusAnalyer", JOptionPane.PLAIN_MESSAGE);
		}else {
			ONION_Info.userName = "ONION";
			src_ko.info.ONION_Info.userName = "ONION";
			
			JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>Modbus Analyzer User authentication completed%s</span></font><br></html>", Util.separator));
			ImageIcon i = new Util().getIconResource2();
			msg.setIcon(i);
			msg.setFont(font);
			Object[] message = { msg };
			
			JOptionPane.showMessageDialog(null, message, "ModbusAnalyer", JOptionPane.PLAIN_MESSAGE);
		}
						
		activeHiddenMenu();
		super.dispose();
		
		src_ko.swing.PremiumLoginFrame.activeHiddenMenu();
	}
	
	public static void activeHiddenMenu() {
		//MainFrame.activeUtil(); // ��ƿ �޴� Ȱ��ȭ ( ���� ��ƿ ��� �̻�� )
		MainFrame.activeMK119(); // �����ͺ��̽� �޴� Ȱ��ȭ
		MainFrame.activeConnection(); // Connection �����̾� ��� Ȱ��ȭ (���ǽ� ��ĵ, ���� ��ĵ)
		ModbusAgent_Panel.activationControl(); // ������ ���� ��� Ȱ��ȭ
		ModbusAgent_Panel.activationMK119(); // MK119 ���� ��� Ȱ��ȭ		
		ModbusAgent_Panel.activationExpression(); // ModbusAgent ���ǽ� ��� Ȱ��ȭ
		RX_Analysis_Panel.activationExpression(); // RX_Analysis ���ǽ� ��� Ȱ��ȭ
		Multi_Analysis_Panel.activationExcpression(); // Multi_Analysis ���ǽ� ��� Ȱ��ȭ
		RealTime_Panel.activationScaleFunction(); // RealTime_Panel ���ǽ� ��� Ȱ��ȭ

		if(OnionMember.loginUser != null && OnionMember.loginUser.getTeam().equals("������")) {
			if(!MoonInspector.isMoon()) MainFrame.moonMenu.setText("   Development   ");
			MainFrame.activeMoon(); // Moon ���� �޴� Ȱ��ȭ
		}
	}

	// ����� ���� Ű �̺�Ʈ ������
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
