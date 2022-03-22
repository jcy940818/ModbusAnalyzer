package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import src_ko.swing.XmlEditor_Panel;
import src_ko.analyzer.RX.DataType;
import src_ko.database.StoredProcedure;
import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.util.Util;

public class MainFrame extends JFrame {
	
	// 프로그램 실행 경로
	private static String currentPath = null;
	
	private static JPanel actualPanel;
	private static JPanel contentPane;
	private static CardLayout cardLayout;		
	private static JFrame mainFrame = null;	
	
	private static JMenuBar menuBar;
	private static JMenu moonMenu;
	private static JMenu connectionMenu;
	private static JMenu DatabaseMenu;
	private static JMenu utilMenu;
	private static JMenu xmlGeneratorMenu;
	
	private static JMenuItem exceptionScan;
	private static JMenuItem simpleValueScan;
	private static JMenuItem realTime;
	
	/**
	 * Launch the application. 
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainFrame frame = new MainFrame();
//					contentPane.setPreferredSize(new Dimension(1074,628));
//					frame.pack();
//					frame.setVisible(true);
//					
//					// 테스트 코드 : 사용자 인증 패스
////					 new PremiumLoginFrame(true).loginSuccess(true);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame(){
		setBackground(Color.WHITE);				
		setResizable(false);
		setTitle("ModbusAnalyzer");
			
		// 클래스 로더를 이용한 이미지 로딩
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// 프로젝트 Build Path에 이미지 리소스 디렉토리를 포함시켜야 한다.		
		setIconImage(new Util().getIconResource().getImage());				
		
		// JFrame : 1080x680
		// ContentPane : 1050x610
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		setBounds(100, 100, 1080, 680);		
		getContentPane().setLayout(null);
		
		menuBar = new JMenuBar();
		menuBar.setBorder(null);
		menuBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		menuBar.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		setJMenuBar(menuBar);

		
		
		// Analysis 메뉴
		JMenu analysisMenu = new JMenu("  Analysis  ");
		analysisMenu.setForeground(Color.BLACK);
		analysisMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		analysisMenu.setHorizontalAlignment(SwingConstants.CENTER);
		analysisMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		menuBar.add(analysisMenu);
		
		// Analysis 메뉴 - TX Analysis : TX 분석
		JMenuItem TX_Analysis = new JMenuItem("TX Analysis : \uC694\uCCAD \uD328\uD0B7 \uBD84\uC11D");
		TX_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		TX_Analysis.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		analysisMenu.add(TX_Analysis);
				
		analysisMenu.add(new JSeparator());
		
		// Analysis 메뉴 - RX Analysis : RX 분석
		JMenuItem RX_Analysis = new JMenuItem("RX Analysis : \uC751\uB2F5 \uD328\uD0B7 \uBD84\uC11D");
		RX_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		RX_Analysis.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		analysisMenu.add(RX_Analysis);
				
		analysisMenu.add(new JSeparator());
		
		// Analysis 메뉴 - TX-RX Analysis : TX-RX 분석
		JMenuItem Multi_Analysis = new JMenuItem("TX-RX Analysis : \uC694\uCCAD, \uC751\uB2F5 \uD328\uD0B7 \uBD84\uC11D");
		Multi_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		Multi_Analysis.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		analysisMenu.add(Multi_Analysis);

		
		
		
		
		// Connection 메뉴
		connectionMenu = new JMenu("  Connection  ");
		connectionMenu.setForeground(Color.BLACK);
		connectionMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		connectionMenu.setHorizontalAlignment(SwingConstants.CENTER);
		connectionMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		menuBar.add(connectionMenu);

		// Connection 메뉴 - ModbusAgent : TCP/IP Client 통신
		JMenuItem modbusAgent = new JMenuItem("Modbus Agent : TCP/IP Client 통신");
		modbusAgent.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAgent.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		connectionMenu.add(modbusAgent);
		
		
		// Connection 메뉴 - Expression Value Scan (조건식 스캔)
		simpleValueScan = new JMenuItem("Simple Value Scan : 레지스터 값 스캔");
		simpleValueScan.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		
		
		// Connection 메뉴 - Exception Scan (예외 스캔)
		exceptionScan = new JMenuItem("Exception Scan : 예외 스캔");
		exceptionScan.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		
		
		// Connection 메뉴 - Real Time Monitoring (실시간 모니터링)
		realTime = new JMenuItem("Real-Time Monitoring : 실시간 모니터링");
		realTime.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		
		
		
		
		
		// Database 메뉴
		DatabaseMenu = new JMenu("  Database  ");
		DatabaseMenu.setForeground(Color.BLACK);
		DatabaseMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		DatabaseMenu.setHorizontalAlignment(SwingConstants.CENTER);
		DatabaseMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));
								
		// Database 메뉴 - MK119 : 데이터베이스 조회
		JMenuItem mk119Link = new JMenuItem("Database : MK119 데이터베이스 조회");
		mk119Link.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Link.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		DatabaseMenu.add(mk119Link);				
		DatabaseMenu.add(new JSeparator());
		
		// Database 메뉴 - Stored Procedure : 저장 프로시저
		JMenuItem storedProcedure = new JMenuItem("Stored Procedure : 저장 프로시저 수행");
		storedProcedure.setHorizontalAlignment(SwingConstants.LEFT);
		storedProcedure.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		DatabaseMenu.add(storedProcedure);
		DatabaseMenu.add(new JSeparator());
		
		// Database 메뉴 - Procedure Generator : 저장 프로시저 생성
		JMenuItem procedureGenerator = new JMenuItem("Procedure Generator : 신규 프로시저 생성");
		procedureGenerator.setHorizontalAlignment(SwingConstants.LEFT);
		procedureGenerator.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		procedureGenerator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ProcedureGeneratorFrame.isExist) {
					new ProcedureGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Procedure Generator Frame Already Exists") + Util.separator + "\n");
					 sb.append("프로시저 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
		DatabaseMenu.add(procedureGenerator);
		
		
		// Util 메뉴
		utilMenu = new JMenu("   Util   ");
		utilMenu.setForeground(Color.BLACK);
		utilMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		utilMenu.setHorizontalAlignment(SwingConstants.CENTER);
		utilMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		
		
		// Util 메뉴 : 프로토콜 리스트 다운로드
		JMenuItem protocolListDownload = new JMenuItem("Protocol Download : MK119 프로토콜 리스트 다운로드");
		protocolListDownload.setHorizontalAlignment(SwingConstants.LEFT);
		protocolListDownload.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		utilMenu.add(protocolListDownload);
		utilMenu.add(new JSeparator());
				
		// Util 메뉴 : XML 편집기
		JMenuItem xmlEditor = new JMenuItem("Protocol XML Editor : 성능 XML 편집기");
		xmlEditor.setHorizontalAlignment(SwingConstants.LEFT);
		xmlEditor.setFont(new Font("맑은 고딕", Font.PLAIN, 13));		
		utilMenu.add(xmlEditor);
		utilMenu.add(new JSeparator());
		
		
		
		// Util 메뉴 : XML Generator 메뉴
		xmlGeneratorMenu = new JMenu("XML Generator : XML 생성  ");
		xmlGeneratorMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		xmlGeneratorMenu.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGeneratorMenu.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGeneratorMenu.setBorderPainted(false);
		utilMenu.add(xmlGeneratorMenu);
		
		// Util 메뉴 - XML Generator : Modbus
		JMenuItem xmlGenerator_Modbus = new JMenuItem("XML Generator : Modbus");
		xmlGenerator_Modbus.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Modbus.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_Modbus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ModbusXmlGeneratorFrame.isExist) {
					new ModbusXmlGeneratorFrame();		
					showModbusAgent();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Modbus XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Modbus XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_Modbus);
		xmlGeneratorMenu.add(new JSeparator());
		
		
		// Util 메뉴 - XML Generator : Custom Modbus
		JMenuItem xmlGenerator_CustomModbus = new JMenuItem("XML Generator : Custom Modbus");
		xmlGenerator_CustomModbus.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_CustomModbus.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_CustomModbus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!CustomModbusXmlGeneratorFrame.isExist) {
					new CustomModbusXmlGeneratorFrame();		
					showModbusAgent();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Custom Modbus XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Custom Modbus XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_CustomModbus);
		xmlGeneratorMenu.add(new JSeparator());
		
		
		// Util 메뉴 - XML Generator : Common
		JMenuItem xmlGenerator_Common = new JMenuItem("XML Generator : Common");
		xmlGenerator_Common.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Common.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_Common.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!CommonXmlGeneratorFrame.isExist) {
					new CommonXmlGeneratorFrame();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Common XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Common XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_Common);
		xmlGeneratorMenu.add(new JSeparator());
		
		
		// Util 메뉴 - XML Generator : SNMP
		JMenuItem xmlGenerator_SNMP = new JMenuItem("XML Generator : SNMP");
		xmlGenerator_SNMP.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_SNMP.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_SNMP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!SnmpXmlGeneratorFrame.isExist) {
					new SnmpXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("SNMP XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("SNMP XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_SNMP);		
		xmlGeneratorMenu.add(new JSeparator());
		
		// Util 메뉴 - XML Generator : Agent
		JMenuItem xmlGenerator_Agent = new JMenuItem("XML Generator : Agent");
		xmlGenerator_Agent.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Agent.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_Agent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!AgentXmlGeneratorFrame.isExist) {
					new AgentXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Agent XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Agent XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_Agent);
		xmlGeneratorMenu.add(new JSeparator());
		
		
		// Util 메뉴 - XML Generator : Control
		JMenuItem xmlGenerator_Control = new JMenuItem("XML Generator : Control");
		xmlGenerator_Control.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Control.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		xmlGenerator_Control.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ControlXmlGeneratorFrame.isExist) {
					new ControlXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Control XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Control XML 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
		xmlGeneratorMenu.add(xmlGenerator_Control);
		
		
		
		// Moon 메뉴 (히든 메뉴)
		moonMenu = new JMenu("  Moon  ");
		moonMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		moonMenu.setForeground(Color.BLUE);
		moonMenu.setHorizontalAlignment(SwingConstants.CENTER);
		moonMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));				
		
		// Moon 메뉴 : 프로토콜 리스트 다운로드
		JMenuItem moonProtocolListDownload = new JMenuItem("Protocol : MK119 프로토콜 리스트 다운로드");
		moonProtocolListDownload.setHorizontalAlignment(SwingConstants.LEFT);
		moonProtocolListDownload.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		moonProtocolListDownload.setForeground(Color.BLUE);
		moonMenu.add(moonProtocolListDownload);
//		moonMenu.add(new JSeparator());
		
		
		
		
		// Information 메뉴
		JMenu informationMenu = new JMenu("  Information  ");
		informationMenu.setForeground(Color.BLACK);
		informationMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		informationMenu.setHorizontalAlignment(SwingConstants.CENTER);
		informationMenu.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		menuBar.add(informationMenu);
		
		// Information 메뉴 - Application (프로그램 정보)
		JMenuItem information = new JMenuItem("Application : \uD504\uB85C\uADF8\uB7A8 \uC815\uBCF4");
		information.setHorizontalAlignment(SwingConstants.LEFT);
		information.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		informationMenu.add(information);
		
		// Information 메뉴 - Application (프로그램 정보)
		JMenuItem modbusInfo = new JMenuItem("Modbus : \uD504\uB85C\uADF8\uB7A8 \uC815\uBCF4");
		information.setHorizontalAlignment(SwingConstants.LEFT);
		information.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		informationMenu.add(information);
		
		
		
		
		
		
		// ContentPane
		contentPane = new JPanel();		
		contentPane.setBackground(new Color(255, 140, 0));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		actualPanel = new JPanel();
		cardLayout = new CardLayout(0, 0);		
		actualPanel.setLayout(cardLayout);		
		contentPane.add(actualPanel, BorderLayout.CENTER);
		
		
		/** Image Panel *******************************************************/ 
		// 메인 화면 : ONION
		Image_Panel image_panel = new Image_Panel();
		actualPanel.add(image_panel, "image_panel");
		cardLayout.show(actualPanel, "image_panel");
		
		
		
		/** TX Analysis Panel *******************************************************/ 
		TX_Analysis_Panel tx_analysis_panel = new TX_Analysis_Panel();
		actualPanel.add(tx_analysis_panel, "tx_analysis_panel");						
		TX_Analysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTX();
			}
		});
		
		
		/** RX Analysis Panel *******************************************************/
		RX_Analysis_Panel rx_analysis_panel = new RX_Analysis_Panel();
		actualPanel.add(rx_analysis_panel, "rx_analysis_panel"); 
				
		RX_Analysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showRX();
			}
		});
		
		
		/** TX-RX Analysis Panel (Multi) *******************************************************/
		Multi_Analysis_Panel multi_analysis_panel = new Multi_Analysis_Panel();
		actualPanel.add(multi_analysis_panel, "multi_analysis_panel"); 
				
		Multi_Analysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTXRX();
			}
		});
		
		
		/** ModbusAgent Panel *******************************************************/
		ModbusAgent_Panel modbusAgent_panel = new ModbusAgent_Panel();
		actualPanel.add(modbusAgent_panel, "modbusAgent_panel"); 
				
		modbusAgent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showModbusAgent();
			}
		});

		
		
		
		/** MK119 Login Panel ***********************************/
		MK119_Login_Panel mk119_login_panel = new MK119_Login_Panel();
		actualPanel.add(mk119_login_panel, "mk119_login_panel"); 
		
		
		/** DatabaseAccess_Panel **********************/
		mk119Link.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				showMK119Login("DatabaseAgent");
			}
		});
		
		/** StoredProcedure_Panel *************************************/
		storedProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMK119Login("storedProcedure");				
			}
		});		
		
		
		/** DatabaseAccess_Panel ***********************************/
		DatabaseAccess_Panel databaseAccess_Panel = new DatabaseAccess_Panel();
		actualPanel.add(databaseAccess_Panel, "DatabaseAccess_Panel");
		
		
		/** StoredProcedure_Panel ***********************************/
		StoredProcedure_Panel storedProcedure_Panel = new StoredProcedure_Panel();
		actualPanel.add(storedProcedure_Panel, "StoredProcedure_Panel");
						
		
		/** SimpleValueScan_Panel ***********************************/
		SimpleValueScan_Panel simpleValueScan_Panel = new SimpleValueScan_Panel();
		actualPanel.add(simpleValueScan_Panel, "SimpleValueScan_Panel");			
		simpleValueScan.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				showSimpleValueScan();
			}
		});		
		
		/** ExceptionDetection_Panel ***********************************/
		ExceptionScan_Panel exceptionScan_Panel = new ExceptionScan_Panel();
		actualPanel.add(exceptionScan_Panel, "ExceptionScan_Panel");			
		exceptionScan.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				showExceptionScan();
			}
		});
		
		/** RealTime_Panel ***********************************/
		RealTime_Panel realTime_Panel = new RealTime_Panel();
		actualPanel.add(realTime_Panel, "RealTime_Panel");			
		realTime.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				showRealTime();
			}
		});		
		
		/** ProtocolDownload Panel ***********************************/
		ProtocolDownloadPanel protocolDownloadPanel = new ProtocolDownloadPanel();
		actualPanel.add(protocolDownloadPanel, "protocolDownloadPanel");
		protocolListDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// OnionSoftware 디렉토리 경로 프로토콜 다운로드				
				showProtocolDownload(false);				
			}
		});
		
		/** OnionDirCheck Panel ***********************************/
		OnionDirCheck_Panel onionDirCheck_Panel = new OnionDirCheck_Panel();
		actualPanel.add(onionDirCheck_Panel, "onionDirCheck_Panel");
		xmlEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// OnionSoftware 디렉토리 경로 프로토콜 다운로드				
				showOnionDirCheck(false);				
			}
		});
		
		/** XmlEditor Panel ***********************************/
		XmlEditor_Panel xmlEditor_Panel = new XmlEditor_Panel();
		actualPanel.add(xmlEditor_Panel, "xmlEditor_Panel");
		
		
		
		/** ProtocolDownload Panel : Moon *******************************/				
		moonProtocolListDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// MK119 프로젝트 디렉토리 경로 프로토콜 다운로드 : 라디오 버튼 옵션으로 수정 가능				
				showProtocolDownload(true);
			}
		});
		
		
		/** Information Panel ***********************************/
		Information_Panel information_panel = new Information_Panel();
		actualPanel.add(information_panel, "Information_panel"); 
		information.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInformation();				
			}
		});


		
		DataType.initTypeMap();
		DataType.initCustomTypeMap();
		
		try {
			// 현재 실행 경로 로드
			currentPath = Util.getCurrentPath(MainFrame.class);			
		}catch(Exception e) {
			// 현재 실행 경로를 찾지못함
		}
		
		mainFrame = this;
		setLocationRelativeTo(null); // 프레임 생성자의 마지막에 넣어줘야 프레임이 화면 가운데에서 실행된다.
	}
	
	public static String getCurrentPath() {
		return MainFrame.currentPath;
	}
	
	public static JFrame getMainFrame() {
		return mainFrame;
	}
	
	public static void showTX() {
		cardLayout.show(actualPanel, "tx_analysis_panel");						
	}
	
	public static void showRX() {
		cardLayout.show(actualPanel, "rx_analysis_panel");
	}
	
	public static void showTXRX() {
		cardLayout.show(actualPanel, "multi_analysis_panel");
	}
	
	public static void showModbusAgent() {
		cardLayout.show(actualPanel, "modbusAgent_panel");
	}
	
	public static void showProtocolDownload(boolean isProject) {
		// isProject : MK119 프로젝트의 프로토콜 리스트 다운 여부
		ProtocolDownloadPanel.isProject = isProject;
		ProtocolDownloadPanel.isProject_checkBox.setVisible(isProject);
		ProtocolDownloadPanel.changeSatate();
		cardLayout.show(actualPanel, "protocolDownloadPanel");
	}
	
	public static void showOnionDirCheck(boolean isProject) {
		// isProject : MK119 프로젝트의 XML 편집
		OnionDirCheck_Panel.isProject = isProject;
		OnionDirCheck_Panel.isProject_checkBox.setVisible(isProject);
		OnionDirCheck_Panel.changeSatate();
		cardLayout.show(actualPanel, "onionDirCheck_Panel");
	}
	
	public static void showXmlEditor(File xmlDir, ArrayList<Protocol> protocols) {
		XmlEditor_Panel.isKorean = true;
		if(XmlEditor_Panel.languageButton != null) XmlEditor_Panel.languageButton.setText("한글명");
		XmlEditor_Panel.xmlDir = xmlDir;
		XmlEditor_Panel.protocols = protocols;
		XmlEditor_Panel.setFacilityComboBox(protocols, true);
		XmlEditor_Panel.setTableContent("COMMON", "전 체", protocols, XmlEditor_Panel.isKorean);
		cardLayout.show(actualPanel, "xmlEditor_Panel");
	}
	
	public static void showMK119Login(String agentType) {
		try {
			if(ONION_Info.hasMk119Connection()) ONION_Info.closeMk119Connection();
		}catch(SQLException e) {
			System.out.println("[ MainFrame.showMK119Login() : SQL Exception ]");
		}
		
		// 파라미터 agentType 내용에 따라 데이터베이스 연동 후 수행하는 내용이 다르다
		MK119_Login_Panel.agentType = agentType;
		
		switch(agentType) {
			case "ModbusAgent" : MK119_Login_Panel.setCurrentAgent("Modbus Collection"); break;
			case "DatabaseAgent" : MK119_Login_Panel.setCurrentAgent("DataBase Access"); break;
			case "storedProcedure" : MK119_Login_Panel.setCurrentAgent("Stored Procedure"); break;
			default : MK119_Login_Panel.setCurrentAgent("DataBase Access"); break;
		}
		
		cardLayout.show(actualPanel, "mk119_login_panel");
	}
	
	public static void showDatabaseAccess() {
		cardLayout.show(actualPanel, "DatabaseAccess_Panel");		
	}
	
	public static void showStoredProcedure() {
		StoredProcedure.init();
		StoredProcedure.loadStoredProcedureMap();		
		cardLayout.show(actualPanel, "StoredProcedure_Panel");		
	}
	
	public static void showSimpleValueScan() {
		cardLayout.show(actualPanel, "SimpleValueScan_Panel");		
	}
	
	public static void showExceptionScan() {
		cardLayout.show(actualPanel, "ExceptionScan_Panel");
	}
	
	public static void showRealTime() {
		cardLayout.show(actualPanel, "RealTime_Panel");
	}
	
	public static void showInformation() {
		cardLayout.show(actualPanel, "Information_panel");
	}	
	
	public static void activeConnection() {
		connectionMenu.add(new JSeparator());
		connectionMenu.add(simpleValueScan);
		connectionMenu.add(new JSeparator());
		connectionMenu.add(exceptionScan);								
		connectionMenu.add(new JSeparator());
		connectionMenu.add(realTime);
	}
	
	public static void activeDatabase() {
		menuBar.add(DatabaseMenu, 2);
		menuBar.doLayout();
	}
	
	public static void activeUtil() {
		menuBar.add(utilMenu, 3);
		menuBar.doLayout();
	}
	
	public static void activeMoon() {
		menuBar.add(moonMenu, 4);
		menuBar.doLayout();
	}
	
	public static JPanel getActualPanel() {
		return contentPane;
	}
	
}
