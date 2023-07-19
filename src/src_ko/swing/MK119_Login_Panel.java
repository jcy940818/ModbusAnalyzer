package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import common.util.FontManager;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class MK119_Login_Panel extends JPanel {
	
	// 데이터베이스 접속 변수
	public static String agentType = null;
	private static JLabel agentType_Label;
	
	private String MK119_version;
	public static JTextField MK119_ip;
	public static JTextField MK119_port;

	private static JButton MK119_LoginButton;
	
	public MK119_Login_Panel() {
		// size : 1074, 628
		setBorder(new EmptyBorder(12, 12, 12, 12));
		setSize(1074, 628);
		setBackground(Color.DARK_GRAY);

		setLayout(new BorderLayout(0, 0));
		JPanel actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		// Default : localhost
		MK119_ip = new JTextField();
		MK119_ip.setText("localhost");
		MK119_ip.setFocusTraversalKeysEnabled(false);
		MK119_ip.setColumns(10);
		MK119_ip.setBounds(429, 305, 155, 21);
		MK119_ip.addFocusListener(Util.focusListener);
		MK119_ip.setFocusTraversalKeysEnabled(false);
		MK119_ip.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					MK119_port.requestFocus();
			}
		});
		MK119_ip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MK119_LoginButton.doClick();
			}
		});
		actualPanel.add(MK119_ip);
		
		// MSSQL Default : 1433
		MK119_port = new JTextField();
		MK119_port.setText("1433");
		MK119_port.setFocusTraversalKeysEnabled(false);
		MK119_port.setColumns(10);
		MK119_port.setBounds(429, 333, 155, 21);
		MK119_port.addFocusListener(Util.focusListener);
		MK119_port.setFocusTraversalKeysEnabled(false);

		MK119_port.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MK119_LoginButton.doClick();
			}
		});
		actualPanel.add(MK119_port);
		
		agentType_Label = new JLabel("Agent Type");
		agentType_Label.setForeground(new Color(255, 76, 55));		
		agentType_Label.setHorizontalAlignment(SwingConstants.LEFT);
		agentType_Label.setFont(FontManager.getFont(Font.BOLD, 25));		
		agentType_Label.setBackground(Color.WHITE);
		agentType_Label.setBounds(315, 180, 305, 52);
		actualPanel.add(agentType_Label);
		
		
		JLabel imageLabel = new JLabel(new Util().getMK119Resource(), JLabel.CENTER);
		imageLabel.setRequestFocusEnabled(false);
		imageLabel.setInheritsPopupMenu(false);
		imageLabel.setFocusTraversalKeysEnabled(false);
		imageLabel.setFocusable(false);
		imageLabel.setBounds(0, 0, 1050, 604);		
		actualPanel.add(imageLabel);
		
		
		// MK119 로그인 버튼
		MK119_LoginButton = new JButton("");
		MK119_LoginButton.setOpaque(false);
		MK119_LoginButton.setFocusPainted(false);
		MK119_LoginButton.setBorderPainted(false);
		MK119_LoginButton.setBounds(594, 302, 101, 52);
		MK119_LoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loginMK119();
				} catch (Exception exception) {
					exception.printStackTrace();
					System.out.println(exception.getMessage());
					return;
				}
			}
		});
		actualPanel.add(MK119_LoginButton);
		
	}// end MK119_Login_Panel()
		
			
	public static void loginMK119() throws Exception {

		if(agentType.equals("ModbusAgent") && AdminConsole_LoginFrame.isExist) {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("AdminConsole Login Already Exists") + Util.separator + "\n");
			sb.append("AdminConsole Login 프레임이 이미 존재합니다" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// MK119 Database Connection
		Connection mk119Connection = null;
		Statement stmt = null;

		if (!DbUtil.loadDriver()) return;
		mk119Connection = DbUtil.getCustomMk119Connection();

		if ((mk119Connection == null) || (ONION_Info.getMk119Connection() == null)) {
			Util.showMessage("<font color='red'>데이터베이스 연동 실패</font>\n데이터베이스 Connection 생성에 실패하였습니다" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			stmt = mk119Connection.createStatement();
			System.out.println("[ MK119_Login_Panel.loginMK119() : Statement 객체 생성 성공 ]");
		} catch (SQLException e) {
			System.out.println("[ MK119_Login_Panel.loginMK119() : Statement 객체 생성 실패 ]");
		}
		
		if(DbUtil.checkMK119Db(mk119Connection)) {
			System.out.println("[ MK119_Login_Panel.checkMK119Db() : MK119 DB 로그인 완료 ]");
			
			// 데이터베이스 커넥션 생성 후 요청한 에이전트를 검사하여 처리 내용 수행
			if(agentType != null) {
				switch(agentType) {
					case "ModbusAgent" : agent_ModbusAgent(); break;
					case "DatabaseAgent" : agent_DatabaseAgent(); break;
					case "storedProcedure" : agent_storedProcedure(); break;
					case "MK119Lite" : agent_MK119Lite(); break;
					default : agent_DatabaseAgent(); break;
				}
			}else {
				agent_DatabaseAgent();
			}
			
		}else {
			System.out.println("[ MK119_Login_Panel.checkMK119Db() : MK119 DB 로그인 실패 ]");
		}
						
		// 메소드 종료 전 Statement, Connection 객체를 닫아준다
		stmt.close();
//		mk119Connection.close();
	}
	
	public static void setCurrentAgent(String currentAgent) {
		agentType_Label.setText(currentAgent);
	}
	
	// Modbus Client ----------------------------------------------------
	public static void agent_ModbusAgent() {
		if(!AdminConsole_LoginFrame.isExist) {
			new AdminConsole_LoginFrame(ONION_Info.getSimpleSqlServerInfo(), agentType);			
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("AdminConsole Login Already Exists") + Util.separator + "\n");
			sb.append("AdminConsole Login 프레임이 이미 존재합니다" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Database Agent --------------------------------------------------
	public static void agent_DatabaseAgent() {
		// 연동에 성공한 데이터베이스 정보 출력
		mk119ConnSuccess();			
		// 메인 프레임 화면을 DB 로그인 성공 화면으로 변경			
		MainFrame.showDatabaseAccess();
		StoredProcedure_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());
		DatabaseAccess_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());
		DatabaseAccess_Panel.resetQueryFormPanel();
	}
	
	
	// Stored Procedure --------------------------------------------------
	public static void agent_storedProcedure() {
		// 연동에 성공한 데이터베이스 정보 출력
		mk119ConnSuccess();			
		// 메인 프레임 화면을 DB 로그인 성공 화면으로 변경			
		MainFrame.showStoredProcedure();
		DatabaseAccess_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());
		StoredProcedure_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());
		StoredProcedure_Panel.resetQueryFormPanel();
	}
	
	// Server List --------------------------------------------------
	public static void agent_MK119Lite() {
		// 연동에 성공한 데이터베이스 정보 출력
		mk119ConnSuccess();
		// 메인 프레임 화면을 DB 로그인 성공 화면으로 변경
		MK119_Lite_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());
		MK119_Lite_Panel.isFirstLoad = true;
		MK119_Lite_Panel.selectedServer = null;
		MK119_Lite_Panel.resetForm(true, true);
		
		// 이전에 연동되었던 프로토콜 정보는  사라진다
		LinkMK119Frame.linkReset();
		
		MainFrame.showMK119Lite();
	}
	
	
	public static void mk119ConnSuccess() {
		String longSeparator = Util.separator + Util.separator + Util.separator + Util.separator; 
		
		// 데이터베이스 연동 성공 메시지
		Font font = FontManager.getFont(Font.BOLD, 22);
		Font font2 = FontManager.getFont(Font.BOLD, 18);
		
		JLabel msg = new JLabel(String.format("<html>&nbsp;<font color='orange'><span text-align=center>MK119 데이터베이스 연동 성공%s%s</span></font><br></html>", Util.separator, Util.separator));
		ImageIcon i = new Util().getIconResource2();
		msg.setIcon(i);
		msg.setFont(font);
		
		StringBuilder msg2 = new StringBuilder("<html><br>");
		msg2.append(String.format("%s&nbsp;1. SQL Server : <font color='blue'>%s</font>%s<br><br>", longSeparator, ONION_Info.getSimpleSqlServerInfo() ,longSeparator));
		msg2.append(String.format("%s&nbsp;2. 데이터베이스 : <font color='blue'>%s</font>%s<br><br>", longSeparator, ONION_Info.getDataBaseName() ,longSeparator));
		msg2.append(String.format("%s&nbsp;3. MK119 버전 : <font color='blue'>%s</font>%s<br>", longSeparator, DbUtil.getMK119VersionInfo() ,longSeparator));				
		msg2.append("<br></html>");		
		JLabel MK119_VersionInfo = new JLabel(msg2.toString());
		
		MK119_VersionInfo.setFont(font2);													
		Object[] message = { msg, MK119_VersionInfo };
		
		JOptionPane.showMessageDialog(null, message, "ModbusAnalyzer", JOptionPane.PLAIN_MESSAGE);
	}
}
