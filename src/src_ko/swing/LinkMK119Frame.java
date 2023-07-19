package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.agent.RestAgent;
import common.util.FontManager;
import common.web.AdminConsole_Info;
import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.main.MoonInspector;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class LinkMK119Frame extends JFrame{
	
//	public static void main(String[] args) {
//		new LinkMK119Frame(false, false);
//	}
	
	public static boolean isExist = false;
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static JLabel mk119ProtocolDataLink;
	public static JLabel mk119RestDataLink;
	
	public static JButton linkMK119Protocol_Button;
	public static JLabel mk119ProtocolVersion;
	
	public static AdminConsole_Info adminConsole;
	private static JButton linkMK119PerfData_Button;
	private static JLabel mk119Server;
	private static JLabel mk119SessionID;
	private static JLabel mk119LastReqAPI;
	private static JLabel mk119LastReqTime;
	private static JLabel mk119LastResTime;
	private static JLabel mk119HttpStatusCode;
	public static String lastRequestTime = "";
	public static String lastResponseTime = "";
	public static String lastReqApiContent = "";
	public static String lastReqApiURL = "";
	
	private static JButton refreshSession_Button;
	private static JButton terminateSession_Button;
	
	public static String protocolVersionInfo = String.format("<html>%s : </html>", Util.colorBlue("Version"));
	
	public static String onionDirPath = "";
	public static JCheckBox isProject;
	public static JTextField onionDir_Path_TextField;
	public static JButton onionDir_Path_Button;
	public static JButton onionDir_Check_Button;
	public static JButton onionDir_Auto_Button;
	private JLabel mk119Logo;
	
	public static JFrame linkFrame = null;
	
	/**
	 * Create the panel.
	 */
	public LinkMK119Frame(boolean linkProtocol, boolean linkPerfData) {
		LinkMK119Frame.isExist = true;
		
		setTitle("MK119 Lite");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new Util().getIconResource().getImage());
		setResizable(false);
		
		setSize(700, 761);
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
		mk119Link.setFont(FontManager.getFont(Font.BOLD, 23));
		actualPanel.add(mk119Link);
				
		linkMK119Protocol_Button = new JButton("<html>&nbsp;<font color='green'>Protocol</font> 데이터 연동</html>");
		linkMK119Protocol_Button.setIcon(new Util().getMK2Resource());
		linkMK119Protocol_Button.setForeground(Color.BLACK);
		linkMK119Protocol_Button.setBackground(Color.WHITE);
		linkMK119Protocol_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		linkMK119Protocol_Button.setFocusPainted(false);
		linkMK119Protocol_Button.setBounds(63, 331, 365, 37);		
		linkMK119Protocol_Button.setEnabled(false);		
		linkMK119Protocol_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				linkProtocol();
				
				mk119ProtocolDataLink.setText("2. MK119 Protocol Link Completed");
				linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> 데이터 연동 완료</html>");		
				linkMK119Protocol_Button.setEnabled(false);		
				
				isProject.setEnabled(false);
				onionDir_Path_TextField.setEnabled(false);
				onionDir_Path_Button.setEnabled(false);
				onionDir_Check_Button.setEnabled(false);
				onionDir_Auto_Button.setEnabled(false);
				
				if(FacilityInfoFrame.dbRefreshButton != null) FacilityInfoFrame.dbRefreshButton.doClick();
				dispose();
			}
		});
		
		mk119Logo = new JLabel();
		mk119Logo.setHorizontalAlignment(SwingConstants.RIGHT);
		mk119Logo.setIcon(new Util().getMK2Resource());
		mk119Logo.setBounds(595, 10, 81, 37);
		actualPanel.add(mk119Logo);
		actualPanel.add(linkMK119Protocol_Button);
		
		linkMK119PerfData_Button = new JButton("<html>&nbsp;<font color='green'>REST API</font> 데이터 연동</html>");
		linkMK119PerfData_Button.setIcon(new Util().getMK2Resource());
		linkMK119PerfData_Button.setForeground(Color.BLACK);
		linkMK119PerfData_Button.setBackground(Color.WHITE);
		linkMK119PerfData_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		linkMK119PerfData_Button.setFocusPainted(false);
		linkMK119PerfData_Button.setBounds(63, 680, 365, 37);
		linkMK119PerfData_Button.setEnabled(true);
		linkMK119PerfData_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				AdminConsole_LoginFrame.showLoginForm(ONION_Info.getSimpleSqlServerInfo(), "MK119Lite");

			}
		});
		actualPanel.add(linkMK119PerfData_Button);
		
		JLabel mk119DB = new JLabel("1. MK119 Database Link Completed");
		mk119DB.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DB.setForeground(new Color(237, 76, 55));
		mk119DB.setFont(FontManager.getFont(Font.BOLD, 20));
		mk119DB.setBackground(Color.WHITE);
		mk119DB.setBounds(27, 82, 500, 37);
		actualPanel.add(mk119DB);
		
		JLabel mk119SqlServer = new JLabel(String.format("<html>%s : %s</html>", Util.colorBlue("SQL Server"), ONION_Info.getSimpleSqlServerInfo()));
		mk119SqlServer.setHorizontalAlignment(SwingConstants.LEFT);
		mk119SqlServer.setForeground(Color.BLACK);
		mk119SqlServer.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119SqlServer.setBackground(Color.WHITE);
		mk119SqlServer.setBounds(63, 120, 489, 37);
		actualPanel.add(mk119SqlServer);
		
		JLabel mk119DbName = new JLabel(String.format("<html>%s : %s</html>", Util.colorBlue("DB Name"), ONION_Info.getDataBaseName()));
		mk119DbName.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DbName.setForeground(Color.BLACK);
		mk119DbName.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119DbName.setBackground(Color.WHITE);
		mk119DbName.setBounds(63, 157, 489, 37);
		actualPanel.add(mk119DbName);
		
		mk119ProtocolDataLink = new JLabel("2. MK119 Protocol");
		mk119ProtocolDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolDataLink.setForeground(new Color(237, 76, 55));
		mk119ProtocolDataLink.setFont(FontManager.getFont(Font.BOLD, 20));
		mk119ProtocolDataLink.setBackground(Color.WHITE);
		mk119ProtocolDataLink.setBounds(27, 204, 500, 37);
		actualPanel.add(mk119ProtocolDataLink);
		
		JLabel onionDir_Label = new JLabel("ONION Directory");
		onionDir_Label.setHorizontalAlignment(SwingConstants.LEFT);
		onionDir_Label.setForeground(Color.BLUE);
		onionDir_Label.setFont(FontManager.getFont(Font.BOLD, 17));
		onionDir_Label.setBackground(Color.WHITE);
		onionDir_Label.setBounds(63, 245, 153, 37);
		actualPanel.add(onionDir_Label);
		
		onionDir_Path_TextField = new JTextField(onionDirPath);
		onionDir_Path_TextField.setForeground(Color.BLACK);
		onionDir_Path_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		onionDir_Path_TextField.setFont(FontManager.getFont(Font.PLAIN, 18));
		onionDir_Path_TextField.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		onionDir_Path_TextField.setBounds(214, 250, 377, 32);		
		onionDir_Path_TextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onionDir_Check_Button.doClick();
			}
		});
		onionDir_Path_TextField.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						if(file != null) {
							onionDir_Path_TextField.setText(file.getAbsolutePath());
							onionDir_Check_Button.doClick();
							return;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(onionDir_Path_TextField);
		
		onionDir_Check_Button = new JButton("Check");
		onionDir_Check_Button.setForeground(Color.BLACK);
		onionDir_Check_Button.setFont(FontManager.getFont(Font.BOLD, 15));
		onionDir_Check_Button.setFocusPainted(false);
		onionDir_Check_Button.setBackground(Color.WHITE);
		onionDir_Check_Button.setBounds(595, 250, 87, 32);
		onionDir_Check_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isOnion = checkOnionDir(onionDir_Path_TextField.getText(), isProject.isSelected());
				
				if(isOnion) {
					linkMK119Protocol_Button.setEnabled(true);
				}else{
					linkMK119Protocol_Button.setEnabled(false);
				}
				
			}
		});				
		actualPanel.add(onionDir_Check_Button);
		
		onionDir_Path_Button = new JButton("Path");
		onionDir_Path_Button.setForeground(Color.BLACK);
		onionDir_Path_Button.setFont(FontManager.getFont(Font.BOLD, 15));
		onionDir_Path_Button.setFocusPainted(false);
		onionDir_Path_Button.setBackground(Color.WHITE);
		onionDir_Path_Button.setBounds(610, 335, 72, 32);	
		onionDir_Path_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = Util.getFilePath();
				if(path != null) {
					onionDir_Path_TextField.setText(path);
				}	
				onionDir_Check_Button.doClick();
			}
		});
		actualPanel.add(onionDir_Path_Button);
		
		onionDir_Auto_Button = new JButton("Auto");
		onionDir_Auto_Button.setForeground(Color.BLACK);
		onionDir_Auto_Button.setFont(FontManager.getFont(Font.BOLD, 15));
		onionDir_Auto_Button.setFocusPainted(false);
		onionDir_Auto_Button.setBackground(Color.WHITE);
		onionDir_Auto_Button.setBounds(535, 335, 72, 32);
		onionDir_Auto_Button.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPath();
			}
		});
		actualPanel.add(onionDir_Auto_Button);
		
		mk119ProtocolVersion = new JLabel(protocolVersionInfo);
		mk119ProtocolVersion.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolVersion.setForeground(Color.BLACK);
		mk119ProtocolVersion.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119ProtocolVersion.setBackground(Color.WHITE);
		mk119ProtocolVersion.setBounds(63, 285, 619, 37);
		actualPanel.add(mk119ProtocolVersion);
		
		mk119RestDataLink = new JLabel("3. MK119 REST API");
		mk119RestDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119RestDataLink.setForeground(new Color(237, 76, 55));
		mk119RestDataLink.setFont(FontManager.getFont(Font.BOLD, 20));
		mk119RestDataLink.setBackground(Color.WHITE);
		mk119RestDataLink.setBounds(27, 395, 452, 37);
		mk119RestDataLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				if(MoonInspector.isMoon()) {
					StringSelection data = new StringSelection(RestAgent.responseBody);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(data, data);
				}
			}
		});
		actualPanel.add(mk119RestDataLink);
		
		
		mk119Server = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("MK119 Server")));
		mk119Server.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Server.setForeground(Color.BLACK);
		mk119Server.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119Server.setBackground(Color.WHITE);
		mk119Server.setBounds(63, 445, 416, 37);
		actualPanel.add(mk119Server);
		
		mk119SessionID = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Session ID")));
		mk119SessionID.setHorizontalAlignment(SwingConstants.LEFT);
		mk119SessionID.setForeground(Color.BLACK);
		mk119SessionID.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119SessionID.setBackground(Color.WHITE);
		mk119SessionID.setBounds(63, 482, 617, 37);
		mk119SessionID.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					 if (e.getButton() == 1) {  } // 왼쪽 클릭				 
					 if (e.getButton() == 1 && e.getClickCount() == 2) {
						 if(adminConsole != null) {
							 refreshSession(adminConsole);
						 }
					 } // 더블 클릭
					 if (e.getButton() == 3) { 
						 if(adminConsole != null) {
							 refreshSession(adminConsole);
						 }
					 } // 오른쪽 클릭
				}
			});
		actualPanel.add(mk119SessionID);
		
		mk119LastReqAPI = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Last Request API")));
		mk119LastReqAPI.setHorizontalAlignment(SwingConstants.LEFT);
		mk119LastReqAPI.setForeground(Color.BLACK);
		mk119LastReqAPI.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119LastReqAPI.setBackground(Color.WHITE);
		mk119LastReqAPI.setBounds(63, 519, 617, 37);
		mk119LastReqAPI.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(MoonInspector.isMoon()) {
					 if (e.getButton() == 1) {  
						// 왼쪽 클릭		
						 if(adminConsole != null) {
							 String API = String.format("http://%s:%s%s", adminConsole.get_IP(), adminConsole.get_PORT(), lastReqApiURL);						 
							 StringSelection data = new StringSelection(API);
							 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							 clipboard.setContents(data, data);
						 }
					 }
					 if (e.getButton() == 1 && e.getClickCount() == 2) {
						// 더블 클릭
						 if(adminConsole != null) {
							 String API = String.format("http://%s:%s%s", adminConsole.get_IP(), adminConsole.get_PORT(), lastReqApiURL);						 
							 StringSelection data = new StringSelection(API);
							 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							 clipboard.setContents(data, data);
						 }
					 }
					 if (e.getButton() == 3) {
						// 오른쪽 클릭
						 if(adminConsole != null) {
							 String API = String.format("http://%s:%s%s", adminConsole.get_IP(), adminConsole.get_PORT(), lastReqApiURL);						 
							 StringSelection data = new StringSelection(API);
							 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							 clipboard.setContents(data, data);
						 }
					 } 
				}
			}
		});
		actualPanel.add(mk119LastReqAPI);
		
		mk119LastReqTime = new JLabel("<html><font color='blue'>Last Request Time</font> : </html>");
		mk119LastReqTime.setHorizontalAlignment(SwingConstants.LEFT);
		mk119LastReqTime.setForeground(Color.BLACK);
		mk119LastReqTime.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119LastReqTime.setBackground(Color.WHITE);
		mk119LastReqTime.setBounds(63, 558, 617, 37);
		actualPanel.add(mk119LastReqTime);
		
		mk119LastResTime = new JLabel("<html><font color='blue'>Last Response Time</font> : </html>");
		mk119LastResTime.setHorizontalAlignment(SwingConstants.LEFT);
		mk119LastResTime.setForeground(Color.BLACK);
		mk119LastResTime.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119LastResTime.setBackground(Color.WHITE);
		mk119LastResTime.setBounds(63, 597, 617, 37);
		actualPanel.add(mk119LastResTime);
		
		mk119HttpStatusCode = new JLabel(String.format("<html>%s : </html>", Util.colorBlue("Http Status Code")));
		mk119HttpStatusCode.setHorizontalAlignment(SwingConstants.LEFT);
		mk119HttpStatusCode.setForeground(Color.BLACK);
		mk119HttpStatusCode.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119HttpStatusCode.setBackground(Color.WHITE);
		mk119HttpStatusCode.setBounds(63, 636, 617, 37);
		actualPanel.add(mk119HttpStatusCode);
		
		refreshSession_Button = new JButton("Refresh Session");
		refreshSession_Button.setForeground(new Color(0, 128, 0));
		refreshSession_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		refreshSession_Button.setFocusPainted(false);
		refreshSession_Button.setEnabled(true);
		refreshSession_Button.setBackground(Color.WHITE);
		refreshSession_Button.setBounds(491, 391, 189, 37);
		refreshSession_Button.setEnabled(false);
		refreshSession_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(adminConsole != null) {
					refreshSession(adminConsole);
				}
			}
		});
		actualPanel.add(refreshSession_Button);
		
		terminateSession_Button = new JButton("Terminate Session");
		terminateSession_Button.setForeground(Color.RED);
		terminateSession_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		terminateSession_Button.setFocusPainted(false);
		terminateSession_Button.setEnabled(true);
		terminateSession_Button.setBackground(Color.WHITE);
		terminateSession_Button.setBounds(491, 438, 189, 37);
		terminateSession_Button.setEnabled(false);
		terminateSession_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lastReqApiContent = "";
				lastReqApiURL = "";
				lastRequestTime = "";
				lastResponseTime = "";
					
				mk119RestDataLink.setText("3. MK119 REST API");
				linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> 데이터 연동</html>");
				linkMK119PerfData_Button.setEnabled(true);
				refreshSession_Button.setEnabled(false);
				terminateSession_Button.setEnabled(false);
				
				mk119Server.setText(String.format("<html>%s : %s:%s</html>", Util.colorBlue("MK119 Server"), Util.colorRed(adminConsole.get_IP()), Util.colorRed(adminConsole.get_PORT())));
				mk119SessionID.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Session ID"), Util.colorRed(adminConsole.get_SESSION_ID())));
				mk119LastReqAPI.setText(String.format("<html>%s : %s</html>" ,Util.colorBlue("Last Request API"), Util.colorRed("Terminate Session")));
				mk119LastReqTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Request Time"), Util.colorRed(sdf.format(new Date()))));
				mk119LastResTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Response Time"), Util.colorRed("-")));
				mk119HttpStatusCode.setText(String.format("<html>%s : %s</html>", Util.colorBlue("HTTP Status Code"), Util.colorRed("-")));
				
				MK119_Lite_Panel.linkMK119_PerfData = false;
				MK119_Lite_Panel.adminConsole = null;				
				adminConsole = null;
				
				FacilityInfoFrame.isConnectRestAPI();
			}
		});
		actualPanel.add(terminateSession_Button);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.GRAY);
		separator.setBounds(5, 198, 684, 19);
		actualPanel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.GRAY);
		separator_1.setBounds(5, 380, 684, 19);
		actualPanel.add(separator_1);
		
		isProject = new JCheckBox(" MK119 Project");
		isProject.setHorizontalAlignment(SwingConstants.CENTER);		
		isProject.setForeground(Color.BLACK);
		isProject.setFont(FontManager.getFont(Font.BOLD, 15));
		isProject.setFocusPainted(false);
		isProject.setBackground(Color.WHITE);
		isProject.setBounds(540, 200, 147, 37);
		if(MoonInspector.isMoon()) {
			isProject.setVisible(true);
		}else {
			isProject.setSelected(false);
			isProject.setVisible(false);			
		}		
		actualPanel.add(isProject);
		
		
		// 생성자 마지막 부분
		if(linkProtocol && linkPerfData) {
			mk119Link.setText("MK119 Data Link Completed");
		}
		// 프로토콜 연동 여부 검사
		if(linkProtocol) {
			mk119ProtocolDataLink.setText("2. MK119 Protocol Link Completed");
			linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> 데이터 연동 완료</html>");
			linkMK119Protocol_Button.setEnabled(false);
			
			isProject.setEnabled(false);
			onionDir_Path_TextField.setEnabled(false);
			onionDir_Check_Button.setEnabled(false);
			onionDir_Path_Button.setEnabled(false);
			onionDir_Auto_Button.setEnabled(false);
		}
		// REST API 연동 여부 검사
		if(linkPerfData && (this.adminConsole != null)) {
			
			if(this.adminConsole.get_SESSION_ID() != null && this.adminConsole.get_SESSION_ID().length() > 0) {
				mk119RestDataLink.setText("3. MK119 REST API Link Completed");
				linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> 데이터 연동 완료</html>");
				linkMK119PerfData_Button.setEnabled(false);
				refreshSession_Button.setEnabled(true);
				terminateSession_Button.setEnabled(true);
			}else {
				mk119RestDataLink.setText("3. MK119 REST API");
				MK119_Lite_Panel.linkMK119_PerfData = false;				
				refreshSession_Button.setEnabled(false);
				terminateSession_Button.setEnabled(false);
			}
			
			mk119Server.setText(String.format("<html>%s : %s:%s</html>", Util.colorBlue("MK119 Server"), adminConsole.get_IP(), adminConsole.get_PORT()));
			mk119SessionID.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Session ID"), adminConsole.get_SESSION_ID()));
			mk119LastReqAPI.setText(String.format("<html>%s : %s</html>" ,Util.colorBlue("Last Request API"), lastReqApiContent));
			mk119LastReqTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Request Time"), lastRequestTime));
			mk119HttpStatusCode.setText(String.format("<html>%s : %d ( %s )</html>", Util.colorBlue("HTTP Status Code"), adminConsole.getHttpStatusCode(), adminConsole.getHttpStatus()));
			
			try {
				Date req = sdf.parse(lastRequestTime);
				Date res = sdf.parse(lastResponseTime);
				long amount = ((res.getTime() - req.getTime()) / 1000);
				
				mk119LastResTime.setText(String.format("<html>%s : %s ( %d seconds )</html>",Util.colorBlue("Last Response Time"), lastResponseTime, amount));
				
			}catch (ParseException e) {				
				mk119LastResTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Response Time"), lastResponseTime));					
			}
		}
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		linkFrame = this;
	}
	
	public static void linkProtocol() {
		MK119_Lite_Panel.linkMK119_Protocol = true;
		MK119_Lite_Panel.updateItem_searchComboBox(true);
		MK119_Lite_Panel.resetForm(false, false);
	}
	
	public static void linkRestAPI(boolean isRequest, AdminConsole_Info admin, String apiContent, String apiURL) {
		try {
			adminConsole = admin;
			lastReqApiContent = apiContent;
			lastReqApiURL = apiURL;
			
			if(isRequest) {
				lastRequestTime = sdf.format(new Date());
			}else {
				lastResponseTime = sdf.format(new Date());
			}
			
			if(admin.get_SESSION_ID() != null && admin.get_SESSION_ID().length() > 0) {
				mk119RestDataLink.setText("3. MK119 REST API Link Completed");
				linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> 데이터 연동 완료</html>");
				linkMK119PerfData_Button.setEnabled(false);
				refreshSession_Button.setEnabled(true);
				terminateSession_Button.setEnabled(true);
			}else {
				MK119_Lite_Panel.linkMK119_PerfData = false;
				
				mk119RestDataLink.setText("3. MK119 REST API");
				linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> 데이터 연동</html>");
				linkMK119PerfData_Button.setEnabled(true);
				refreshSession_Button.setEnabled(false);
				terminateSession_Button.setEnabled(false);
			}
			
			mk119Server.setText(String.format("<html>%s : %s:%s</html>", Util.colorBlue("MK119 Server"), adminConsole.get_IP(), adminConsole.get_PORT()));
			mk119SessionID.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Session ID"), adminConsole.get_SESSION_ID()));		
			mk119LastReqAPI.setText(String.format("<html>%s : %s</html>" ,Util.colorBlue("Last Request API"), lastReqApiContent));
			
			
			if(isRequest) {
				mk119LastReqTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Request Time"), lastRequestTime));
				mk119LastResTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Response Time"), "Waiting . . . "));
				mk119HttpStatusCode.setText(String.format("<html>%s : %s</html>", Util.colorBlue("HTTP Status Code"), "Waiting . . . "));	
				
			}else {
				try {
					Date req = sdf.parse(lastRequestTime);
					Date res = sdf.parse(lastResponseTime);				
					long amount = ((res.getTime() - req.getTime()) / 1000);				
					mk119LastResTime.setText(String.format("<html>%s : %s ( %d seconds )</html>",Util.colorBlue("Last Response Time"), lastResponseTime, amount));
					mk119HttpStatusCode.setText(String.format("<html>%s : %d ( %s )</html>", Util.colorBlue("HTTP Status Code"), adminConsole.getHttpStatusCode(), adminConsole.getHttpStatus()));			
				}catch (ParseException e) {			
					mk119LastResTime.setText(String.format("<html>%s : %s</html>",Util.colorBlue("Last Response Time"), lastResponseTime));
					mk119HttpStatusCode.setText(String.format("<html>%s : %d ( %s )</html>", Util.colorBlue("HTTP Status Code"), adminConsole.getHttpStatusCode(), adminConsole.getHttpStatus()));				
				}
				
			}
		}catch(Exception e) {
		
		}
	}
	
	public static void refreshSession(AdminConsole_Info admin) {
		linkRestAPI(true, admin, Util.colorGreen("Refresh Session"), "/midknight/adminConsole");
		AdminConsole_Info.refreshSession(admin);
		linkRestAPI(false, admin, Util.colorGreen("Refresh Session"), "/midknight/adminConsole");
	}
	
	public static void linkReset() {
		MK119_Lite_Panel.adminConsole = null;
		MK119_Lite_Panel.linkMK119_Protocol = false;
		MK119_Lite_Panel.linkMK119_PerfData = false;
		MK119_Lite_Panel.updateItem_searchComboBox(false);
		MK119_Lite_Panel.linkMK119_Button.setEnabled(true);
		
		onionDirPath = "";
		protocolVersionInfo = String.format("<html>%s : </html>", Util.colorBlue("Version"));
	}
	
	public static boolean checkOnionDir(String path, boolean isProject) {
		try {			
			
			if(path == null || path.length() < 1) {				
				return false;
			}else {
				path = path.trim(); 
			}
							
			boolean isOnionDir = OnionDirCheck_Panel.checkOnionDir(path, isProject);
			
			if(isOnionDir) {
				ONION_Info.setOnionDirPath(path);
				ONION_Info.setProjectDirPath(path);
				
				String version = FileUtil.getMK119BuildVersion(isProject).replace("build", "Build");
				
				if(version.contains("Fail")) {
					System.out.println("MK119 VersionInfo Load Fail");
					protocolVersionInfo = String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorRed("Not a OnionSoftware Directory"));
					mk119ProtocolVersion.setText(protocolVersionInfo);
					return false;
				}
				
				// ****** [ 서버 리스트에 프로토콜 정보 연동 ] ******************************************				
				ArrayList<Protocol> protocolList = FileUtil.getProtocolList(isProject);
				HashMap<String, Protocol> protocolMap = new HashMap<String, Protocol>();
				
				for(Protocol p : protocolList) {
					int protocolType = p.getProtocolType();
					int facCode = p.getFacCode();
					int protocolNumber = p.getNumber();
					
					String key = String.format("%d-%d-%d", protocolType, facCode, protocolNumber);
					protocolMap.put(key, p);
				}
				
				MK119_Lite_Panel.protocolMap = protocolMap;
				
				onionDirPath = path;
				protocolVersionInfo = String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorGreen(version));
				mk119ProtocolVersion.setText(protocolVersionInfo);
				return true;
				// *****************************************************************************
				
			}else {
				// 어니언 디렉토리 아님
				protocolVersionInfo = String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorRed("Not a OnionSoftware Directory"));
				mk119ProtocolVersion.setText(protocolVersionInfo);
				return false;
			}
		}catch(Exception ex) {
			// 디렉토리 확인중 예외 발생
			ex.printStackTrace();
			protocolVersionInfo = String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorRed("Not a OnionSoftware Directory"));
			mk119ProtocolVersion.setText(protocolVersionInfo);
			return false;
		}
	}
	
	public static void setPath() {
		String C = "C:\\";
		String D = "D:\\";
		
		// D 드라이브 우선
		boolean hasOnionDir = FileUtil.hasOnionDir(D);
		
		if(hasOnionDir) {
			onionDir_Path_TextField.setText(D + "OnionSoftware");
			onionDir_Check_Button.doClick();
			return;
		}else {
			mk119ProtocolVersion.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorRed("Not Found OnionSoftware Directory")));			
		}
		
		hasOnionDir = FileUtil.hasOnionDir(C);
		
		if(hasOnionDir) {
			onionDir_Path_TextField.setText(C + "OnionSoftware");
			onionDir_Check_Button.doClick();
			return;
		}else {
			mk119ProtocolVersion.setText(String.format("<html>%s : %s</html>", Util.colorBlue("Version"), Util.colorRed("Not Found OnionSoftware Directory")));
		}
	}
	
	@Override
	public void dispose() {
		if(!MK119_Lite_Panel.linkMK119_Protocol) {
			onionDirPath = "";
			protocolVersionInfo = String.format("<html>%s : </html>", Util.colorBlue("Version"));
		}
		
		LinkMK119Frame.isExist = false;
		super.dispose();
	}
	
	public static JFrame getLinkeFrame() {
		return linkFrame;
	}
	
}
