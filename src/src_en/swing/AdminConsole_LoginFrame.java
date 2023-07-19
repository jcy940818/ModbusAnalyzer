package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.jsoup.HttpStatusException;

import common.util.FontManager;
import common.web.AdminConsole_Info;
import src_en.agent.HttpAgent;
import src_en.database.DbUtil;
import src_en.util.Util;

public class AdminConsole_LoginFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;	
	
	private AdminConsole_Info adminConsole;
	private String mkSqlServerInfo;
	private String mkDatabaseIp;
	private String mkDatabasePort;	
	
	private JTextField loginId;
	private JTextField loginPassword;
	private JTextField serverIp;
	private JTextField serverPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminConsole_LoginFrame frame = new AdminConsole_LoginFrame("127.0.0.1:1433");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminConsole_LoginFrame(String SqlServerInfo) {
		AdminConsole_LoginFrame.isExist = true;
		
		this.mkSqlServerInfo = SqlServerInfo;
		this.mkDatabaseIp = SqlServerInfo.split(":")[0];
		this.mkDatabasePort = SqlServerInfo.split(":")[1];
		
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
		
		// 서버 IP 정보 -------------------------------------------------------
		serverIp = new JTextField();
		serverIp.setHorizontalAlignment(SwingConstants.CENTER);		
		serverIp.setText(String.format("%s", this.mkDatabaseIp));
		serverIp.setForeground(Color.BLACK);
		serverIp.setFont(FontManager.getFont(Font.BOLD, 20));
		serverIp.setFocusTraversalKeysEnabled(false);
		serverIp.addFocusListener(Util.focusListener);
		serverIp.setColumns(10);
		serverIp.setBackground(Color.WHITE);
		serverIp.setBounds(399, 70, 154, 47);
		serverIp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					serverPort.requestFocus();
			}
		});
		serverIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});	
		actualPanel.add(serverIp);
		
		// 서버 PORT 정보 ---------------------------------------------------
		serverPort = new JTextField();
		serverPort.setHorizontalAlignment(SwingConstants.CENTER);
		serverPort.setBackground(Color.WHITE);
		serverPort.setForeground(Color.BLACK);
		serverPort.setFont(FontManager.getFont(Font.BOLD, 20));
		serverPort.setFocusTraversalKeysEnabled(false);
		serverPort.addFocusListener(Util.focusListener);
		serverPort.setText("8080");		
		serverPort.setColumns(10);
		serverPort.setBounds(576, 70, 91, 47);
		serverPort.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					loginId.requestFocus();
			}
		});
		serverPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});	
		actualPanel.add(serverPort);
		
		JLabel label = new JLabel(":");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.DARK_GRAY);
		label.setFont(FontManager.getFont(Font.BOLD, 30));
		label.setBackground(Color.WHITE);
		label.setBounds(553, 68, 24, 40);
		actualPanel.add(label);
		
		
		// ID 정보 -------------------------------------------------------
		loginId = new JTextField();
		loginId.setForeground(Color.BLACK);
		loginId.setFont(FontManager.getFont(Font.BOLD, 20));
		loginId.setBackground(Color.WHITE);
		loginId.setBounds(399, 125, 268, 47);		
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
		loginId.requestFocus();
		actualPanel.add(loginId);
		
		
		// PW 정보 ---------------------------------------------------------
		loginPassword = new JPasswordField();
		loginPassword.setForeground(Color.BLACK);
		loginPassword.setFont(FontManager.getFont(Font.BOLD, 20));
		loginPassword.setBackground(Color.WHITE);
		loginPassword.setColumns(10);
		loginPassword.setBounds(399, 180, 268, 47);
		loginPassword.addFocusListener(Util.focusListener);
		loginPassword.setFocusTraversalKeysEnabled(false);
		loginPassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					serverIp.requestFocus();
			}
		});
		loginPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		actualPanel.add(loginPassword);
		
		JLabel MK119 = new JLabel("");
		MK119.setHorizontalAlignment(SwingConstants.CENTER);
		MK119.setForeground(Color.DARK_GRAY);
		MK119.setIcon(new Util().getMK2Resource());
		MK119.setFont(FontManager.getFont(Font.BOLD, 24));
		MK119.setBackground(Color.WHITE);
		MK119.setBounds(334, 14, 75, 47);
		actualPanel.add(MK119);
		
		
		// MK119 버전 정보 -----------------------------------------------------------------
		JLabel MK119_Version = new JLabel(DbUtil.getMK119Version());
//		JLabel MK119_Version = new JLabel("4.5");
		MK119_Version.setHorizontalAlignment(SwingConstants.LEFT);
		MK119_Version.setForeground(Color.DARK_GRAY);
		MK119_Version.setFont(FontManager.getFont(Font.BOLD, 25));
		MK119_Version.setBackground(Color.WHITE);
		MK119_Version.setBounds(412, 15, 52, 40);
		MK119_Version.setForeground(new Color(237, 76, 55));
		actualPanel.add(MK119_Version);
		
		JLabel AdminConsole = new JLabel("Admin Console");		
		AdminConsole.setBackground(Color.WHITE);
		AdminConsole.setHorizontalAlignment(SwingConstants.CENTER);
		AdminConsole.setForeground(Color.BLACK);
		AdminConsole.setFont(FontManager.getFont(Font.BOLD, 24));
		AdminConsole.setBounds(456, 11, 183, 47);
		actualPanel.add(AdminConsole);
		
		JLabel SERVER = new JLabel("SERVER");
		SERVER.setHorizontalAlignment(SwingConstants.LEFT);
		SERVER.setForeground(Color.BLACK);
		SERVER.setFont(FontManager.getFont(Font.BOLD, 22));
		SERVER.setBackground(Color.WHITE);
		SERVER.setBounds(308, 70, 91, 47);
		actualPanel.add(SERVER);
		
		JLabel ID = new JLabel("ID");
		ID.setBackground(Color.WHITE);
		ID.setForeground(Color.BLACK);
		ID.setHorizontalAlignment(SwingConstants.LEFT);
		ID.setFont(FontManager.getFont(Font.BOLD, 22));
		ID.setBounds(308, 124, 91, 47);
		actualPanel.add(ID);
		
		JLabel PW = new JLabel("PW");
		PW.setBackground(Color.WHITE);
		PW.setHorizontalAlignment(SwingConstants.LEFT);
		PW.setForeground(Color.BLACK);
		PW.setFont(FontManager.getFont(Font.BOLD, 20));
		PW.setBounds(308, 180, 91, 47);
		actualPanel.add(PW);
		
		JLabel imageLabel = new JLabel(new Util().getLoginFormImage(), JLabel.CENTER);
		imageLabel.setRequestFocusEnabled(false);
		imageLabel.setInheritsPopupMenu(false);
		imageLabel.setFocusTraversalKeysEnabled(false);
		imageLabel.setFocusable(false);
		imageLabel.setBounds(0, 0, 676, 346);		
		actualPanel.add(imageLabel);
		
		JButton loginButton = new JButton("");
		loginButton.setOpaque(false);
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setBounds(349, 264, 285, 47);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		actualPanel.add(loginButton);
		
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
		loginId.requestFocus();
	}
	
	public void login() {		
		// trim 처리
		// id, pw 문자 길이 체크		
		
		
		String IP = serverIp.getText().trim();
		String PORT = serverPort.getText().trim();
		String ID = loginId.getText().trim();
		String PW = loginPassword.getText().trim();
		String sessionId = null;
				
		this.adminConsole = new AdminConsole_Info(IP, PORT, ID, PW, sessionId);
		
		try {
			sessionId = new HttpAgent().getMK119SessionId(this.adminConsole);
		}catch(HttpStatusException e) {
			this.adminConsole.handleException(e);
			
		}catch(SocketTimeoutException e) {
			e.printStackTrace();
			System.out.println("응답 타임아웃 초과");
			this.adminConsole.handleException(e);
			
		}catch(ConnectException e) {
			e.printStackTrace();
			System.out.println("서버가 실행 되어있지 않음");
			this.adminConsole.handleException(e);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("예외 발생");
			this.adminConsole.handleException(e);			
		}
		
		if(sessionId != null) {
			// MK119 AdminConsole 로그인 성공			
			this.adminConsole.setVersionInfo(DbUtil.getMK119VersionInfo());
			this.adminConsole.setVersion(DbUtil.getMK119Version());
			this.adminConsole.setBuild(DbUtil.getMK119Build());
			loginSuccess();
		}else {
			// MK119 AdminConsole 로그인 실패
			// 실패 처리 로직
		}
		
	}
	
	
	public void loginSuccess() {
		
		MainFrame.showModbusAgent(); // 기존 프레임은 ModbusAgnet 을 보여준다
				
		if(!SearchFrame.isExist) {			
			new SearchFrame(this.adminConsole, this.mkSqlServerInfo);
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("Facility Search Already Exists") + Util.separator + "\n");
			sb.append("Modbus Facility Search Frame is already open" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}				
		
		AdminConsole_LoginFrame.isExist = false;
		
		dispose();
	}

	
	@Override
	public void dispose() {
		AdminConsole_LoginFrame.isExist = false;
		super.dispose();
	}
}
