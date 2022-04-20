package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.util.HttpUtil;
import src_ko.info.AdminConsole_Info;
import src_ko.info.ONION_Info;
import src_ko.main.MoonInspector;
import src_ko.util.Util;

public class LinkMK119Frame extends JFrame{
	
//	public static void main(String[] args) {
//		new LinkMK119Frame(false, false);
//	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static JButton linkMK119Protocol_Button;
	private static JLabel mk119ProtocolVersion;
	
	private static JButton linkMK119PerfData_Button;
	private static JLabel mk119Server;
	private static JLabel mk119SessionID;
	private static JLabel mk119LastReqAPI;
	private static JLabel mk119LastReqTime;
	private static JLabel mk119HttpStatusCode;	
	
	private static JButton refreshSession_Button;
	
	public static String protocolVersion = String.format("<html>%s : </html>", Util.colorBlue("Version"));
	
	public static AdminConsole_Info adminConsole;
	public static String lastReqTime = "";
	public static String lastReqAPI = "";
	
	/**
	 * Create the panel.
	 */
	public LinkMK119Frame(boolean linkProtocol, boolean linkPerfData) {
		setTitle("MK119 Lite");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new Util().getIconResource().getImage());
		setResizable(false);
		
		setSize(700, 750);
		setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel actualPanel = new JPanel();		
		actualPanel.setBorder(new LineBorder(new Color(255, 140, 0), 5));
		actualPanel.setBackground(Color.WHITE);
		getContentPane().add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel mk119Link = new JLabel("MK119 Data Link");
		mk119Link.setForeground(new Color(237, 76, 55));
		mk119Link.setBackground(Color.WHITE);
		mk119Link.setIcon(new Util().getSubLogoResource());
		mk119Link.setBounds(12, 10, 515, 50);
		mk119Link.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Link.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 23));
		actualPanel.add(mk119Link);
				
		linkMK119Protocol_Button = new JButton("<html>&nbsp;<font color='green'>Protocol</font> µ•¿Ã≈Õ ø¨µø</html>");
		linkMK119Protocol_Button.setIcon(new Util().getMK2Resource());
		linkMK119Protocol_Button.setForeground(Color.BLACK);
		linkMK119Protocol_Button.setBackground(Color.WHITE);
		linkMK119Protocol_Button.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 16));
		linkMK119Protocol_Button.setFocusPainted(false);
		linkMK119Protocol_Button.setBounds(63, 317, 365, 37);		
		linkMK119Protocol_Button.setEnabled(true);
		linkMK119Protocol_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OnionDirCheck_Panel.connectServerList = true;
				OnionDirCheck_Panel.back_button.setVisible(true);
				MainFrame.showOnionDirCheck(MoonInspector.isMoon());
				dispose();
			}
		});
		actualPanel.add(linkMK119Protocol_Button);
		
		linkMK119PerfData_Button = new JButton("<html>&nbsp;<font color='green'>REST API</font> µ•¿Ã≈Õ ø¨µø</html>");
		linkMK119PerfData_Button.setIcon(new Util().getMK2Resource());
		linkMK119PerfData_Button.setForeground(Color.BLACK);
		linkMK119PerfData_Button.setBackground(Color.WHITE);
		linkMK119PerfData_Button.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 16));
		linkMK119PerfData_Button.setFocusPainted(false);
		linkMK119PerfData_Button.setBounds(63, 657, 365, 37);
		linkMK119PerfData_Button.setEnabled(true);
		linkMK119PerfData_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminConsole_LoginFrame.showLoginForm(ONION_Info.getSimpleSqlServerInfo(), "MK119Lite");
				dispose();
			}
		});
		actualPanel.add(linkMK119PerfData_Button);
		
		JLabel mk119DB = new JLabel("1. MK119 Database");
		mk119DB.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DB.setForeground(new Color(237, 76, 55));
		mk119DB.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119DB.setBackground(Color.WHITE);
		mk119DB.setBounds(27, 82, 500, 37);
		actualPanel.add(mk119DB);
		
		JLabel mk119SqlServer = new JLabel(String.format("<html>%s : %s</html>", Util.colorBlue("SQL Server"), ONION_Info.getSimpleSqlServerInfo()));
		mk119SqlServer.setHorizontalAlignment(SwingConstants.LEFT);
		mk119SqlServer.setForeground(Color.BLACK);
		mk119SqlServer.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119SqlServer.setBackground(Color.WHITE);
		mk119SqlServer.setBounds(63, 126, 489, 37);
		actualPanel.add(mk119SqlServer);
		
		JLabel mk119DbName = new JLabel(String.format("<html>%s : %s</html>", Util.colorBlue("DB Name"), ONION_Info.getDataBaseName()));
		mk119DbName.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DbName.setForeground(Color.BLACK);
		mk119DbName.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119DbName.setBackground(Color.WHITE);
		mk119DbName.setBounds(63, 163, 489, 37);
		actualPanel.add(mk119DbName);
		
		JLabel mk119ProtocolDataLink = new JLabel("2. MK119 Protocol");
		mk119ProtocolDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolDataLink.setForeground(new Color(237, 76, 55));
		mk119ProtocolDataLink.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119ProtocolDataLink.setBackground(Color.WHITE);
		mk119ProtocolDataLink.setBounds(27, 223, 500, 37);
		actualPanel.add(mk119ProtocolDataLink);
		
		mk119ProtocolVersion = new JLabel(protocolVersion);
		mk119ProtocolVersion.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolVersion.setForeground(Color.BLACK);
		mk119ProtocolVersion.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119ProtocolVersion.setBackground(Color.WHITE);
		mk119ProtocolVersion.setBounds(63, 271, 489, 37);
		actualPanel.add(mk119ProtocolVersion);
		
		JLabel mk119RestDataLink = new JLabel("3. MK119 REST API");
		mk119RestDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119RestDataLink.setForeground(new Color(237, 76, 55));
		mk119RestDataLink.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119RestDataLink.setBackground(Color.WHITE);
		mk119RestDataLink.setBounds(27, 397, 204, 37);
		actualPanel.add(mk119RestDataLink);
		
		
		
		mk119Server = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("MK119 Server")));
		mk119Server.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Server.setForeground(Color.BLACK);
		mk119Server.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119Server.setBackground(Color.WHITE);
		mk119Server.setBounds(63, 450, 617, 37);
		actualPanel.add(mk119Server);
		
		mk119SessionID = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Session ID")));
		mk119SessionID.setHorizontalAlignment(SwingConstants.LEFT);
		mk119SessionID.setForeground(Color.BLACK);
		mk119SessionID.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119SessionID.setBackground(Color.WHITE);
		mk119SessionID.setBounds(63, 490, 617, 37);
		mk119SessionID.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					 if (e.getButton() == 1) {  } // øﬁ¬  ≈¨∏Ø				 
					 if (e.getButton() == 1 && e.getClickCount() == 2) {
						 if(adminConsole != null) {
							 refreshSession(adminConsole);
						 }
					 } // ¥ı∫Ì ≈¨∏Ø
					 if (e.getButton() == 3) { 
						 if(adminConsole != null) {
							 refreshSession(adminConsole);
						 }
					 } // ø¿∏•¬  ≈¨∏Ø
				}
			});
		actualPanel.add(mk119SessionID);
		
		mk119LastReqAPI = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Last Request API")));
		mk119LastReqAPI.setHorizontalAlignment(SwingConstants.LEFT);
		mk119LastReqAPI.setForeground(Color.BLACK);
		mk119LastReqAPI.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119LastReqAPI.setBackground(Color.WHITE);
		mk119LastReqAPI.setBounds(63, 530, 617, 37);
		actualPanel.add(mk119LastReqAPI);
		
		mk119LastReqTime = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Last Request Time")));
		mk119LastReqTime.setHorizontalAlignment(SwingConstants.LEFT);
		mk119LastReqTime.setForeground(Color.BLACK);
		mk119LastReqTime.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119LastReqTime.setBackground(Color.WHITE);
		mk119LastReqTime.setBounds(63, 570, 617, 37);
		actualPanel.add(mk119LastReqTime);
		
		mk119HttpStatusCode = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Http Status Code")));
		mk119HttpStatusCode.setHorizontalAlignment(SwingConstants.LEFT);
		mk119HttpStatusCode.setForeground(Color.BLACK);
		mk119HttpStatusCode.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119HttpStatusCode.setBackground(Color.WHITE);
		mk119HttpStatusCode.setBounds(63, 610, 617, 37);
		actualPanel.add(mk119HttpStatusCode);
		
		refreshSession_Button = new JButton("Refresh Session");
		refreshSession_Button.setForeground(new Color(0, 128, 0));
		refreshSession_Button.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 16));
		refreshSession_Button.setFocusPainted(false);
		refreshSession_Button.setEnabled(true);
		refreshSession_Button.setBackground(Color.WHITE);
		refreshSession_Button.setBounds(515, 393, 165, 37);
		refreshSession_Button.setEnabled(false);
		refreshSession_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(adminConsole != null) {
					refreshSession(adminConsole);
				}
			}
		});
		actualPanel.add(refreshSession_Button);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.GRAY);
		separator.setBounds(5, 210, 684, 19);
		actualPanel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.GRAY);
		separator_1.setBounds(5, 380, 684, 19);
		actualPanel.add(separator_1);
		
		if(linkProtocol && linkPerfData) {
			mk119Link.setText("MK119 Data Link Completed");
		}
		if(linkProtocol) {
			linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> µ•¿Ã≈Õ ø¨µø øœ∑·</html>");
			linkMK119Protocol_Button.setEnabled(false);
			
			mk119ProtocolVersion.setText(protocolVersion);
		}
		if(linkPerfData && (this.adminConsole != null)) {
			
			if(this.adminConsole.get_SESSION_ID() != null && this.adminConsole.get_SESSION_ID().length() > 0) {
				linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> µ•¿Ã≈Õ ø¨µø øœ∑·</html>");
				linkMK119PerfData_Button.setEnabled(false);
				refreshSession_Button.setEnabled(true);
			}else {
				MK119_Lite_Panel.linkMK119_PerfData = false;
				refreshSession_Button.setEnabled(false);
			}
			
			mk119Server.setText(String.format("<html>%s : %s:%s</html>", Util.colorBlue("MK119 Server"), adminConsole.get_IP(), adminConsole.get_PORT()));
			mk119SessionID.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Session ID"), adminConsole.get_SESSION_ID()));
			mk119LastReqAPI.setText(String.format("<html>%s : %s</html>" ,Util.colorBlue("Last Request API"), lastReqAPI));
			mk119LastReqTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Request Time"), lastReqTime));		
			mk119HttpStatusCode.setText(String.format("<html>%s : %d ( %s )</html>", Util.colorBlue("HTTP Status Code"), adminConsole.getHttpStatusCode(), adminConsole.getHttpStatus()));
		}
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void linkProtocol(String version) {
		protocolVersion = String.format("<html>%s : %s</html>", Util.colorBlue("Version"), version);
		mk119ProtocolVersion.setText(protocolVersion);
		linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> µ•¿Ã≈Õ ø¨µø øœ∑·</html>");		
		linkMK119Protocol_Button.setEnabled(false);
	}
	
	public static void linkRestAPI(AdminConsole_Info admin, String api) {
		adminConsole = admin;
		lastReqAPI = api;	
		lastReqTime = sdf.format(new Date());
		
		if(admin.get_SESSION_ID() != null && admin.get_SESSION_ID().length() > 0) {
			linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> µ•¿Ã≈Õ ø¨µø øœ∑·</html>");
			linkMK119PerfData_Button.setEnabled(false);
			refreshSession_Button.setEnabled(true);
		}else {
			MK119_Lite_Panel.linkMK119_PerfData = false;
			
			linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> µ•¿Ã≈Õ ø¨µø</html>");
			linkMK119PerfData_Button.setEnabled(true);
			refreshSession_Button.setEnabled(false);
		}
		
		mk119Server.setText(String.format("<html>%s : %s:%s</html>", Util.colorBlue("MK119 Server"), adminConsole.get_IP(), adminConsole.get_PORT()));
		mk119SessionID.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Session ID"), adminConsole.get_SESSION_ID()));
		mk119LastReqAPI.setText(String.format("<html>%s : %s</html>" ,Util.colorBlue("Last Request API"), lastReqAPI));
		mk119LastReqTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Request Time"), lastReqTime));		
		mk119HttpStatusCode.setText(String.format("<html>%s : %d ( %s )</html>", Util.colorBlue("HTTP Status Code"), adminConsole.getHttpStatusCode(), adminConsole.getHttpStatus()));
	}
	
	public static void refreshSession(AdminConsole_Info admin) {
		AdminConsole_Info.refreshSession(admin);
		linkRestAPI(admin, "/midknight/adminConsole " + Util.colorGreen("( Refresh Session )"));
	}
	
	public static void linkReset() {
		MK119_Lite_Panel.adminConsole = null;
		MK119_Lite_Panel.linkMK119_Protocol = false;
		MK119_Lite_Panel.linkMK119_PerfData = false;
		MK119_Lite_Panel.updateItem_searchComboBox(false);
		MK119_Lite_Panel.linkMK119_Button.setEnabled(true);
		
		protocolVersion = String.format("<html>%s : </html>", Util.colorBlue("Version"));
	}
}
