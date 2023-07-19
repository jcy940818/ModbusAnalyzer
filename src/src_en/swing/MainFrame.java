package src_en.swing;

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

import common.util.FontManager;
import src_en.analyzer.RX.DataType;
import src_en.database.StoredProcedure;
import src_en.info.ONION_Info;
import src_en.info.Protocol;
import src_en.main.MoonInspector;
import src_en.util.Util;

public class MainFrame extends JFrame {
	
	// 프로그램 실행 경로
	private static String currentPath = null;
	
	private static JPanel actualPanel;
	private static JPanel contentPane;
	private static CardLayout cardLayout;		
	private static JFrame mainFrame = null;	
	
	private static JMenuBar menuBar;
	public static JMenu moonMenu;
	private static JMenu connectionMenu;
	private static JMenu mk119Menu;
	private static JMenu utilMenu;
	private static JMenuItem xmlGeneratorMenu;
	
	private static JMenuItem modbusMonitor_V1;
	private static JMenuItem modbusMonitor_V2;
	private static JMenuItem exceptionScan;
	private static JMenuItem simpleValueScan;
	private static JMenuItem realTime;
	
	private static JMenuItem mk119Link;
	
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
		menuBar.setFont(FontManager.getFont(Font.PLAIN, 15));
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		setJMenuBar(menuBar);

		
		
		// Analysis 메뉴
		JMenu analysisMenu = new JMenu("   Analysis   ");
		analysisMenu.setForeground(Color.BLACK);
		analysisMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		analysisMenu.setHorizontalAlignment(SwingConstants.CENTER);
		analysisMenu.setFont(FontManager.getFont(Font.BOLD, 15));
		menuBar.add(analysisMenu);
		
		// Analysis 메뉴 - TX Analysis : TX 분석
		JMenuItem TX_Analysis = new JMenuItem("TX Analysis : Request Packet Analysis");
		TX_Analysis.setForeground(Color.BLACK);
		TX_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		TX_Analysis.setFont(FontManager.getFont(Font.PLAIN, 14));
		analysisMenu.add(TX_Analysis);
				
		analysisMenu.add(new JSeparator());
		
		// Analysis 메뉴 - RX Analysis : RX 분석
		JMenuItem RX_Analysis = new JMenuItem("RX Analysis : Response Packet Analysis");
		RX_Analysis.setForeground(Color.BLACK);
		RX_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		RX_Analysis.setFont(FontManager.getFont(Font.PLAIN, 14));
		analysisMenu.add(RX_Analysis);
				
		analysisMenu.add(new JSeparator());
		
		// Analysis 메뉴 - TX-RX Analysis : TX-RX 분석
		JMenuItem Multi_Analysis = new JMenuItem("TX-RX Analysis : TX-RX Transaction Analysis");
		Multi_Analysis.setForeground(Color.BLACK);
		Multi_Analysis.setHorizontalAlignment(SwingConstants.LEFT);
		Multi_Analysis.setFont(FontManager.getFont(Font.PLAIN, 14));
		analysisMenu.add(Multi_Analysis);
		
		// Connection 메뉴
		connectionMenu = new JMenu("   Connection   ");
		connectionMenu.setForeground(Color.BLACK);
		connectionMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		connectionMenu.setHorizontalAlignment(SwingConstants.CENTER);
		connectionMenu.setFont(FontManager.getFont(Font.BOLD, 15));
		menuBar.add(connectionMenu);

		// Connection 메뉴 - ModbusAgent : TCP/IP Client 통신
		JMenuItem modbusAgent = new JMenuItem("Modbus Client : TCP/IP Client Communication");
		modbusAgent.setForeground(Color.BLACK);
		modbusAgent.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAgent.setFont(FontManager.getFont(Font.PLAIN, 14));
		connectionMenu.add(modbusAgent);
		
		// Connection 메뉴 - Modbus Monitor (모드버스 모니터 V1)
		modbusMonitor_V1 = new JMenuItem("Modbus Monitor : Modbus Monitor V1");
		modbusMonitor_V1.setForeground(Color.BLUE);
		modbusMonitor_V1.setFont(FontManager.getFont(Font.PLAIN, 14));
		modbusMonitor_V1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ModbusMonitorFrame.isExist) {
					new ModbusMonitorFrame();							
				 }else {
					 ModbusMonitorFrame.existsFrame();
				 }
			}
		});
		
		// Connection 메뉴 - Simple Value Scan (조건식 스캔)
		simpleValueScan = new JMenuItem("Simple Value Scan : Check Register Value");
		simpleValueScan.setForeground(Color.BLACK);
		simpleValueScan.setFont(FontManager.getFont(Font.PLAIN, 14));		
		
		
		// Connection 메뉴 - Exception Scan (예외 스캔)
		exceptionScan = new JMenuItem("Exception Scan : Check Exception Response");
		exceptionScan.setForeground(Color.BLACK);
		exceptionScan.setFont(FontManager.getFont(Font.PLAIN, 14));		
		
		
		// Connection 메뉴 - Real Time Monitoring (실시간 모니터링)
		realTime = new JMenuItem("Real-Time Monitoring : Check Real-time Data");
		realTime.setForeground(Color.BLACK);
		realTime.setFont(FontManager.getFont(Font.PLAIN, 14));
				
		
		
		// Util 메뉴
		utilMenu = new JMenu("   Util   ");
		utilMenu.setForeground(Color.BLACK);
		utilMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		utilMenu.setHorizontalAlignment(SwingConstants.CENTER);
		utilMenu.setFont(FontManager.getFont(Font.BOLD, 15));

		
		
		
		// MK119 메뉴
		mk119Menu = new JMenu("   MK119   ");
		mk119Menu.setForeground(Color.BLUE);
		mk119Menu.setBorder(new LineBorder(new Color(0, 0, 0)));
		mk119Menu.setHorizontalAlignment(SwingConstants.CENTER);
		mk119Menu.setFont(FontManager.getFont(Font.BOLD, 15));
		
		// MK119 메뉴 : XML 뷰어
		JMenuItem xmlViewer = new JMenuItem("MK119 : Protocol Search     ");
		xmlViewer.setForeground(Color.BLUE);
		xmlViewer.setHorizontalAlignment(SwingConstants.LEFT);
		xmlViewer.setFont(FontManager.getFont(Font.BOLD, 14));		
		mk119Menu.add(xmlViewer);
		mk119Menu.add(new JSeparator());
		
		// MK119 메뉴 : XML Generator 메뉴
		xmlGeneratorMenu = new JMenuItem("MK119 : Protocol XML Generator   ");
		xmlGeneratorMenu.setForeground(Color.BLUE);
		xmlGeneratorMenu.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGeneratorMenu.setFont(FontManager.getFont(Font.BOLD, 14));
		xmlGeneratorMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		xmlGeneratorMenu.setBorderPainted(false);
		xmlGeneratorMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFormExcelConvertXml();
			}
		});
		mk119Menu.add(xmlGeneratorMenu);
		mk119Menu.add(new JSeparator());
		
		// MK119 메뉴 : 프로토콜 리스트 다운로드
		JMenuItem protocolListDownload = new JMenuItem("MK119 : Protocol List Download");
		protocolListDownload.setForeground(Color.BLUE);
		protocolListDownload.setHorizontalAlignment(SwingConstants.LEFT);
		protocolListDownload.setFont(FontManager.getFont(Font.BOLD, 14));		
		mk119Menu.add(protocolListDownload);
		mk119Menu.add(new JSeparator());
		
		
		// MK119 메뉴 : XML Generator : Form Excel
		JMenuItem xmlGenerator_formExcel = new JMenuItem("XML Generator : Form Excel");
		xmlGenerator_formExcel.setForeground(Color.BLUE);
		xmlGenerator_formExcel.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_formExcel.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_formExcel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFormExcelConvertXml();
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_formExcel);
//		xmlGeneratorMenu.add(new JSeparator());
		
		
		// MK119 메뉴 : XML Generator : Common
		JMenuItem xmlGenerator_Common = new JMenuItem("XML Generator : Common");
		xmlGenerator_Common.setForeground(Color.BLACK);
		xmlGenerator_Common.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Common.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_Common.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!CommonXmlGeneratorFrame.isExist) {
					new CommonXmlGeneratorFrame();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Common XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Common XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_Common);
//		xmlGeneratorMenu.add(new JSeparator());
		
		
		// MK119 메뉴 : XML Generator : Modbus
		JMenuItem xmlGenerator_Modbus = new JMenuItem("XML Generator : Modbus");
		xmlGenerator_Modbus.setForeground(Color.BLACK);
		xmlGenerator_Modbus.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Modbus.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_Modbus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ModbusXmlGeneratorFrame.isExist) {
					new ModbusXmlGeneratorFrame();		
					showModbusAgent();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Modbus XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Modbus XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_Modbus);
//		xmlGeneratorMenu.add(new JSeparator());
		
		
		// MK119 메뉴 : XML Generator : Custom Modbus
		JMenuItem xmlGenerator_CustomModbus = new JMenuItem("XML Generator : Custom Modbus");
		xmlGenerator_CustomModbus.setForeground(Color.BLACK);
		xmlGenerator_CustomModbus.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_CustomModbus.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_CustomModbus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!CustomModbusXmlGeneratorFrame.isExist) {
					new CustomModbusXmlGeneratorFrame();		
					showModbusAgent();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Custom Modbus XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Custom Modbus XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_CustomModbus);
		
		
		
		
		
		
		// MK119 메뉴 : XML Generator : SNMP
		JMenuItem xmlGenerator_SNMP = new JMenuItem("XML Generator : SNMP");
		xmlGenerator_SNMP.setForeground(Color.BLACK);
		xmlGenerator_SNMP.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_SNMP.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_SNMP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!SnmpXmlGeneratorFrame.isExist) {
					new SnmpXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("SNMP XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("SNMP XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_SNMP);		
//		xmlGeneratorMenu.add(new JSeparator());
		
		// MK119 메뉴 : XML Generator : Agent
		JMenuItem xmlGenerator_Agent = new JMenuItem("XML Generator : Agent");
		xmlGenerator_Agent.setForeground(Color.BLACK);
		xmlGenerator_Agent.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Agent.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_Agent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!AgentXmlGeneratorFrame.isExist) {
					new AgentXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Agent XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Agent XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_Agent);
//		xmlGeneratorMenu.add(new JSeparator());
		
		
		// MK119 메뉴 :  - XML Generator : Control
		JMenuItem xmlGenerator_Control = new JMenuItem("XML Generator : Control");
		xmlGenerator_Control.setForeground(Color.BLACK);
		xmlGenerator_Control.setHorizontalAlignment(SwingConstants.LEFT);
		xmlGenerator_Control.setFont(FontManager.getFont(Font.PLAIN, 14));
		xmlGenerator_Control.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ControlXmlGeneratorFrame.isExist) {
					new ControlXmlGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Control XML Frame Already Exists") + Util.separator + "\n");
					 sb.append("Control XML Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				
			}
		});
//		xmlGeneratorMenu.add(xmlGenerator_Control);
								
		// MK119 메뉴 :  - MK119 : 데이터베이스 조회
		mk119Link = new JMenuItem("MK119 : Database inquiry");
		mk119Link.setForeground(Color.BLUE);
		mk119Link.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Link.setFont(FontManager.getFont(Font.BOLD, 14));
		
		
		// MK119 메뉴 :  - Stored Procedure : 저장 프로시저
		JMenuItem storedProcedure = new JMenuItem("MK119 : Execute Stored Procedure");
		storedProcedure.setForeground(Color.BLUE);
		storedProcedure.setHorizontalAlignment(SwingConstants.LEFT);
		storedProcedure.setFont(FontManager.getFont(Font.BOLD, 14));	
		mk119Menu.add(storedProcedure);
		
		
		// Moon 메뉴 (히든 메뉴)
		moonMenu = new JMenu("   Moon   ");
		moonMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		moonMenu.setForeground(Color.RED);
		moonMenu.setHorizontalAlignment(SwingConstants.CENTER);
		moonMenu.setFont(FontManager.getFont(Font.BOLD, 15));				
		
		// Moon 메뉴 : 간편한 XML 조회
		JMenuItem moonXmlViewer = new JMenuItem("MK119 : Protocol Search");
		moonXmlViewer.setHorizontalAlignment(SwingConstants.LEFT);
		moonXmlViewer.setFont(FontManager.getFont(Font.BOLD, 14));
		moonXmlViewer.setForeground(Color.RED);
		moonMenu.add(moonXmlViewer);
		moonMenu.add(new JSeparator());
		
		// Moon 메뉴 : 프로토콜 리스트 다운로드
		JMenuItem moonProtocolListDownload = new JMenuItem("MK119 : Protocol List Download");
		moonProtocolListDownload.setHorizontalAlignment(SwingConstants.LEFT);
		moonProtocolListDownload.setFont(FontManager.getFont(Font.BOLD, 14));
		moonProtocolListDownload.setForeground(Color.RED);
		moonMenu.add(moonProtocolListDownload);
		
		
		
		// Information 메뉴
		JMenu informationMenu = new JMenu("   Information   ");
		informationMenu.setForeground(Color.BLACK);
		informationMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		informationMenu.setHorizontalAlignment(SwingConstants.CENTER);
		informationMenu.setFont(FontManager.getFont(Font.BOLD, 15));
		menuBar.add(informationMenu);
		
		// Information 메뉴 - Application (프로그램 정보)
		JMenuItem information = new JMenuItem("Information : Program information");
		information.setForeground(Color.BLACK);
		information.setHorizontalAlignment(SwingConstants.LEFT);
		information.setFont(FontManager.getFont(Font.PLAIN, 14));
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
						
		/** MK119_Lite_Panel ***********************************/
		MK119_Lite_Panel mk119_Lite_Panel = new MK119_Lite_Panel();
		actualPanel.add(mk119_Lite_Panel, "MK119_Lite_Panel");
		
		/** SimpleValueScan_Panel ***********************************/
		SimpleValueScan_Panel SimpleValueScan_Panel = new SimpleValueScan_Panel();
		actualPanel.add(SimpleValueScan_Panel, "SimpleValueScan_Panel");			
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
		xmlViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// OnionSoftware 디렉토리 경로 프로토콜 다운로드				
				showOnionDirCheck(false, null);				
			}
		});
		
		/** XmlEditor Panel ***********************************/
		ProtocolList_Panel xmlEditor_Panel = new ProtocolList_Panel();
		actualPanel.add(xmlEditor_Panel, "xmlEditor_Panel");
		
		
		/** XML Generator : Form Excel Convert XML ***********************************/
		FormExcelConvertXmlPanel formExcelConvertXmlPanel = new FormExcelConvertXmlPanel();
		actualPanel.add(formExcelConvertXmlPanel, "FormExcelConvertXmlPanel");
		
		/** ProtocolDownload Panel : Moon *******************************/				
		moonProtocolListDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// MK119 프로젝트 디렉토리 경로 프로토콜 다운로드 : 라디오 버튼 옵션으로 수정 가능				
				showProtocolDownload(true);
			}
		});
		
		/** XmlEditor Panel : Moon ***********************************/
		moonXmlViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// MK119 XML 편집 : 라디오 버튼 옵션으로 수정 가능				
				showOnionDirCheck(true, null);
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
		
		showInformation();
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
	
	public static void showOnionDirCheck(boolean isProject, String agent) {
		// isProject : MK119 프로젝트의 XML 편집
		OnionDirCheck_Panel.isProject = isProject;
		OnionDirCheck_Panel.isProject_checkBox.setVisible(isProject);
		OnionDirCheck_Panel.changeSatate();
		
		OnionDirCheck_Panel.agent = agent;
		if(agent != null && agent.equalsIgnoreCase("watchPoint")) {
			ProtocolList_Panel.goXmlViewer.setText("Add Watch Point");
		}else {
			ProtocolList_Panel.goXmlViewer.setText("Open XML Viewer");
		}
		
		cardLayout.show(actualPanel, "onionDirCheck_Panel");
	}
	
	public static void showXmlEditor(File xmlDir, ArrayList<Protocol> protocols) {
		ProtocolList_Panel.isKorean = false;
		if(ProtocolList_Panel.languageButton != null) ProtocolList_Panel.languageButton.setText("English");
		ProtocolList_Panel.xmlDir = xmlDir;
		ProtocolList_Panel.protocols = protocols;
		ProtocolList_Panel.setFacilityComboBox(protocols, true);
		ProtocolList_Panel.setTableContent("PROTOCOL", "All Types", protocols, ProtocolList_Panel.isKorean);
		ProtocolList_Panel.resetForm();
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
			case "MK119Lite" : MK119_Login_Panel.setCurrentAgent("MK119 Lite"); break;
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
	
	public static void showMK119Lite() {		
		cardLayout.show(actualPanel, "MK119_Lite_Panel");		
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
	
	public static void showFormExcelConvertXml() {
		cardLayout.show(actualPanel, "FormExcelConvertXmlPanel");
	}
	
	public static void showInformation() {
		cardLayout.show(actualPanel, "Information_panel");
	}	
	
	public static void activeConnection() {
//		connectionMenu.add(new JSeparator());
//		connectionMenu.add(simpleValueScan);
		connectionMenu.add(new JSeparator());
		connectionMenu.add(exceptionScan);								
		connectionMenu.add(new JSeparator());
		connectionMenu.add(realTime);
		connectionMenu.add(new JSeparator());
		connectionMenu.add(modbusMonitor_V1);
	}
	
	public static void activeUtil() {
		menuBar.add(utilMenu, 2);
		menuBar.doLayout();
	}
	
	public static void activeMK119() {
		
		if(MoonInspector.isMoon()) {
			mk119Menu.add(new JSeparator());
			mk119Menu.add(mk119Link);
		}
		
		menuBar.add(mk119Menu, 2);
		menuBar.doLayout();
	}
	
	public static void activeMoon() {
		menuBar.add(moonMenu, 3);
		menuBar.doLayout();
	}
	
	public static JPanel getActualPanel() {
		return contentPane;
	}
	
}
