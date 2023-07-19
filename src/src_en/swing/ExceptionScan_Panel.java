package src_en.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.FontManager;
import common.util.SwingUtil;
import src_en.agent.ClientSocket;
import src_en.agent.ModbusAgent;
import src_en.info.RX_Info;
import src_en.info.TX_Info;
import src_en.util.ExceptionProvider;
import src_en.util.TX_Generator;
import src_en.util.Util;

public class ExceptionScan_Panel extends JPanel {
	
	private static boolean scanRunning = false;
	
	// 클라이언트 소켓
	public static Socket socket_en = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;	
	
	// information Component
	JPanel infoPanel; // 클라이언트 소켓이 서버와 연결 된 상태일때만 인포메이션 컴포넌트들을 활성화 시킨다.  
	JPanel inputFormPanel;
	JPanel typePanel;
	JPanel resultPanel;
	JPanel imagePanel; /* ONION Image */
	
	private JButton connectButton; // 연결 정보 입력버튼 (중요)
	private static boolean isRTU = false; // Default : Modbus TCP (아주 중요한 변수)	
	private static RX_Info global_rx = null;
	public static JTable table;
	private static JLabel currentState;
	private static JLabel modbusAddress_label;
	
	// TX Form 전송 관련 컴포넌트
	private CardLayout inputPanel_layout;
	private JTextField transactionId_text; // Modbus TCP : TransactionID 필드
	private JComboBox unitId_comboBox; // 장비번호 콤보박스
	private JComboBox functionCode_comboBox; // 기능코드 필드
	private JTextField startAddress_text; // 시작주소, 제어주소 필드
	private JComboBox requestCount_comboBox; // 요청개수
	private JButton form_sendPacketButton;		
	private static JButton form_resetButton;
	private JCheckBox autoTid_CheckBox;
	private static JRadioButton radio_modbusTCP;
	private static JRadioButton radio_modbusRTU;
	
	// 통신 기록
	public static JScrollPane packetLog_scrollPane;
	public static JScrollPane scrollPane;
	public static JTextArea packetLog;
	public TX_Info tx;
	public RX_Info rx;
	
	private KeyEvent lastKeyEvent;
	private JTextField timeout_text;
	private JTextField interval_text;
	
	/**
	 * Create the panel.
	 */
	public ExceptionScan_Panel(){	
		setBorder(new EmptyBorder(0, 0, 0, 0));
	
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(new Color(0, 100, 0));
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 606);
		actualPanel.add(infoPanel);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Exception Scan");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 219, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBounds(0, 55, 1050, 551);
		imagePanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(imagePanel);
		
		JLabel imageLabel = new JLabel();
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.WHITE);		
		imageLabel.setIcon(new Util().getOnionScreenResource());
		
		resultPanel = new JPanel();
		resultPanel.setBounds(10, 56, 1028, 425);
		infoPanel.add(resultPanel);
		resultPanel.setBackground(Color.LIGHT_GRAY);
		resultPanel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setFont(FontManager.getFont(Font.PLAIN, 13));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(547, 10, 469, 405);
		resultPanel.add(scrollPane);
		
		// 테이블 생성 부분
		table = new JTable();
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				try {
					
					if (e.getButton() == 1) { } // 왼쪽 클릭
					if (e.getButton() == 1 && e.getClickCount() == 2) { } // 왼쪽 버튼 더블 클릭
					if (e.getButton() == 3) {
						// 오른쪽 클릭
						int column = table.columnAtPoint(e.getPoint());
						int row = table.rowAtPoint(e.getPoint());
						table.changeSelection(row, column, false, false);
						table.requestFocus();
						int[] selectedIndex = table.getSelectedRows();
						
						String registerAddress = (String)table.getValueAt(selectedIndex[0], 1);
						String modbusAddress = (String)table.getValueAt(selectedIndex[0], 2);
						
						String findAddress = String.format("Address : %s (%s)", modbusAddress, registerAddress);
						int textLength = findAddress.length();
						
						packetLog.getCaret().setSelectionVisible(true);
						int start = packetLog.getText().indexOf(findAddress);
						int end = start + textLength;
	
						packetLog.select(start, end);									
					}
				}catch(Exception exception) {
					exception.printStackTrace();				
				}
			}			
		});
		resetTable(table);
		
		scrollPane.setViewportView(table);
		
		packetLog_scrollPane = new JScrollPane();
		packetLog_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		packetLog_scrollPane.setBounds(12, 10, 523, 405);
		resultPanel.add(packetLog_scrollPane);
		
		
		packetLog = new JTextArea();		
		packetLog.setFont(FontManager.getFont(Font.PLAIN, 16));		
		packetLog.addMouseListener(new MyMouseListener());
		packetLog_scrollPane.setViewportView(packetLog);
		
		currentState = new JLabel();		
		currentState.setOpaque(true);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setFont(FontManager.getFont(Font.BOLD, 17));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(218, 6, 193, 45);
		infoPanel.add(currentState);
		
		connectButton = new JButton("Connect");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(FontManager.getFont(Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(420, 11, 110, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {												
				
				// 클라이언트 소켓의 마지막 커넥션 정보
				String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
				
				try {
					socket_en = ModbusAgent.clientSocket;
					src_ko.swing.ExceptionScan_Panel.socket_ko = socket_en;
					
					if( (socket_en == null || socket_en.isClosed()) && ClientSocket.getIsFirst()) {						
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_ko.swing.ExceptionScan_Panel.IP = IP;
						src_ko.swing.ExceptionScan_Panel.PORT = PORT;
						
					}else if(socket_en == null) {
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_ko.swing.ExceptionScan_Panel.IP = IP;
						src_ko.swing.ExceptionScan_Panel.PORT = PORT;
					}else {
						// 기존 연결되어있는 소켓일 경우 연결을 끊고 재접속을 시도한다.						
						// 클라이언트 소켓 : 접속 끊김
						socket_en.close();
						ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
					}
				}catch(IOException exception) {
					return;
				}
				
				try {
					socket_en = ClientSocket.getClientSocket(IP, PORT);
					src_ko.swing.ExceptionScan_Panel.socket_ko = socket_en;
					
				}catch(Exception exception) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='red'>Failed to connect</font>\n");
					msg.append("Please check the connection information you entered" + Util.separator + Util.separator + "\n");					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}				
				
				if(socket_en != null || ClientSocket.isCurrentConnected(socket_en)) {
					// 접속 성공 : 컴포넌트 내용들을 모두 초기화한다	
					ModbusAgent.clientSocket = socket_en;
					src_ko.agent.ModbusAgent.clientSocket = socket_en;
					
					panel_ON();
					
					// 마지막 커넥션 정보와 다른 정보로 세션을  생성시 컴포넌트 초기화
					if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
						componentAllClear();
						src_ko.swing.ExceptionScan_Panel.componentAllClear();
					}
					
					// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장					
					ClientSocket.setHasLastConnectionInfo(true);
				}
			}
		});
		
		infoPanel.add(connectButton);
		inputFormPanel = new JPanel();
		inputFormPanel.setBackground(Color.WHITE);
		inputFormPanel.setBounds(175, 509, 887, 107);
		actualPanel.add(inputFormPanel);
		inputPanel_layout = new CardLayout(0, 0);
		inputFormPanel.setLayout(inputPanel_layout);
		
		JPanel form_InputPanel = new JPanel();
		form_InputPanel.setLayout(null);
		form_InputPanel.setBackground(Color.WHITE);
		inputFormPanel.add(form_InputPanel, "form_InputPanel");
		
		JLabel typeLabel2 = new JLabel("Modbus TCP");
		typeLabel2.setForeground(Color.BLACK);
		typeLabel2.setHorizontalAlignment(SwingConstants.LEFT);
		typeLabel2.setFont(FontManager.getFont(Font.BOLD, 16));
		typeLabel2.setBounds(12, 10, 129, 31);
		form_InputPanel.add(typeLabel2);
		
		JLabel transactionId_label = new JLabel("TID");
		transactionId_label.setForeground(Color.BLACK);
		transactionId_label.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_label.setFont(FontManager.getFont(Font.BOLD, 16));
		transactionId_label.setBounds(131, 28, 26, 31);
		form_InputPanel.add(transactionId_label);
		
		transactionId_text = new JTextField();
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(FontManager.getFont(Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(163, 28, 85, 31);
		transactionId_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int transactionId = 0;
				
				if(transactionId_text.getText().startsWith("0x")||transactionId_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(transactionId_text.getText().startsWith("0x")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0x", ""),16); 
						if(transactionId_text.getText().startsWith("0X")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						transactionId = Integer.parseInt(transactionId_text.getText());
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(transactionId > Short.MAX_VALUE) {
					transactionId_text.setForeground(Color.RED);
				}else {
					transactionId_text.setForeground(Color.BLUE);
				}	
			}			
		});
		transactionId_text.addKeyListener(new KeyAdapter() {						
			public void keyReleased(KeyEvent e) {
				int transactionId = 0;
			
				if(transactionId_text.getText().startsWith("0x")||transactionId_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(transactionId_text.getText().startsWith("0x")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0x", ""),16); 
						if(transactionId_text.getText().startsWith("0X")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						transactionId = Integer.parseInt(transactionId_text.getText());
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(transactionId > Short.MAX_VALUE) {
					transactionId_text.setForeground(Color.RED);
				}else {
					transactionId_text.setForeground(Color.BLUE);
				}				
			}
		});
		
		form_InputPanel.add(transactionId_text);
		
		JLabel unitId_label = new JLabel("Unit ID");
		unitId_label.setForeground(Color.BLACK);
		unitId_label.setHorizontalAlignment(SwingConstants.LEFT);
		unitId_label.setFont(FontManager.getFont(Font.BOLD, 16));
		unitId_label.setBounds(260, 28, 77, 31);
		form_InputPanel.add(unitId_label);
		
		unitId_comboBox = new JComboBox();
		unitId_comboBox.setForeground(Color.BLACK);
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1);
		}		
		unitId_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		unitId_comboBox.setSelectedIndex(0);
		unitId_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		unitId_comboBox.setBackground(Color.WHITE);
		unitId_comboBox.setBounds(332, 28, 86, 32);
		unitId_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(unitId_comboBox);
		
		JLabel startAddress_label = new JLabel("Start Addr");
		startAddress_label.setForeground(Color.BLACK);
		startAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_label.setFont(FontManager.getFont(Font.BOLD, 16));
		startAddress_label.setBounds(75, 69, 86, 31);
		form_InputPanel.add(startAddress_label);
		
		startAddress_text = new JTextField();
		startAddress_text.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_text.setFont(FontManager.getFont(Font.BOLD, 15));
		startAddress_text.setColumns(10);
		startAddress_text.setBorder(UIManager.getBorder("TextField.border"));
		startAddress_text.setBounds(163, 69, 85, 31);	
		startAddress_text.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				form_sendPacketButton.doClick();				
			}
		});
		startAddress_text.addFocusListener(new FocusAdapter() {			
			public void focusLost(FocusEvent e) {				
				String modbusAddress = "";
				int startAddress = 0;
				
				if(startAddress_text.getText().length() == 0) {
					modbusAddress_label.setText("Address preview");
					modbusAddress_label.setForeground(Color.DARK_GRAY);
					return;
				}
				
				if(startAddress_text.getText().startsWith("0x")||startAddress_text.getText().startsWith("0X")) {
					// 16진수 주소 표기법 (0x0000)
					try {
						if(startAddress_text.getText().startsWith("0x")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0x", ""),16); 
						if(startAddress_text.getText().startsWith("0X")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0X", ""),16);
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("Invalid address");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}
				}else {
					// 일반 10진수 주소 표기법
					try {
						startAddress = Integer.parseInt(startAddress_text.getText());
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("Invalid address");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}				
				}
				
				switch(Integer.parseInt((String)functionCode_comboBox.getSelectedItem())) {
					case 1: modbusAddress = "0"; break;
					case 2: modbusAddress = "1"; break;
					case 3: modbusAddress = "4"; break;
					case 4: modbusAddress = "3"; break;
					case 5: modbusAddress = "0"; break;
					case 6: modbusAddress = "4"; break;					
				}
				
				modbusAddress_label.setText(String.format("%s%04d ( 0x%04X )", modbusAddress, (startAddress&0xffff)+1 , startAddress));
				startAddress_text.setForeground(Color.BLUE);
				modbusAddress_label.setForeground(Color.BLUE);							
			}			
		});
		startAddress_text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				lastKeyEvent = e;
				String modbusAddress = "";
				int startAddress = 0;
				
				if(startAddress_text.getText().length() == 0) {
					modbusAddress_label.setText("Address preview");
					modbusAddress_label.setForeground(Color.DARK_GRAY);
					return;
				}
				
				if(startAddress_text.getText().startsWith("0x")||startAddress_text.getText().startsWith("0X")) {
					// 16진수 주소 표기법 (0x0000)
					try {
						if(startAddress_text.getText().startsWith("0x")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0x", ""),16); 
						if(startAddress_text.getText().startsWith("0X")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0X", ""),16);
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("Invalid address");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}
				}else {
					// 일반 10진수 주소 표기법
					try {
						startAddress = Integer.parseInt(startAddress_text.getText());
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("Invalid address");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}				
				}
				
				switch(Integer.parseInt((String)functionCode_comboBox.getSelectedItem())) {
					case 1: modbusAddress = "0"; break;
					case 2: modbusAddress = "1"; break;
					case 3: modbusAddress = "4"; break;
					case 4: modbusAddress = "3"; break;
					case 5: modbusAddress = "0"; break;
					case 6: modbusAddress = "4"; break;					
				}
				
				modbusAddress_label.setText(String.format("%s%04d ( 0x%04X )", modbusAddress, (startAddress&0xffff)+1 , startAddress));
				startAddress_text.setForeground(Color.BLUE);
				modbusAddress_label.setForeground(Color.BLUE);				
			}
		});
		form_InputPanel.add(startAddress_text);
		
		JLabel functionCode_label = new JLabel("Function");
		functionCode_label.setForeground(Color.BLACK);
		functionCode_label.setHorizontalAlignment(SwingConstants.LEFT);
		functionCode_label.setFont(FontManager.getFont(Font.BOLD, 16));
		functionCode_label.setBounds(432, 28, 77, 31);
		form_InputPanel.add(functionCode_label);
		
		functionCode_comboBox = new JComboBox();
		functionCode_comboBox.setForeground(Color.BLACK);
		functionCode_comboBox.setBackground(Color.WHITE);		
		functionCode_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
		functionCode_comboBox.setSelectedIndex(2);
		functionCode_comboBox.setBounds(504, 28, 87, 32);
		functionCode_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		functionCode_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();				
				if(Integer.parseInt(temp.getSelectedItem().toString()) == 0x05) {
					// Force Single Coil : 0x05					
				} else if(Integer.parseInt(temp.getSelectedItem().toString()) == 0x06){
					// Preset Single Register : 0x06
					
				}
				
				// 기능코드 콤보박스 내용 변경시 사용자에게 표시해줄 모드버스 시작주소를 변경해주어야 하기 때문에
				// 시작주소 텍스트필드의 keyEvent를 발생시켜준다
				KeyListener keyListener =  startAddress_text.getKeyListeners()[0];
				if(keyListener != null) keyListener.keyReleased(lastKeyEvent);				
			}
		});
		form_InputPanel.add(functionCode_comboBox);
		
		
		JLabel requestCount_label = new JLabel("Req Count");
		requestCount_label.setForeground(Color.BLACK);
		requestCount_label.setHorizontalAlignment(SwingConstants.LEFT);
		requestCount_label.setFont(FontManager.getFont(Font.BOLD, 15));
		requestCount_label.setBounds(416, 69, 85, 31);
		form_InputPanel.add(requestCount_label);
		
		requestCount_comboBox = new JComboBox();		
		requestCount_comboBox.setForeground(Color.BLACK);
		String[] requestValue = new String[125];
		for(int i = 0; i < requestValue.length; i++) {
			requestValue[i] = String.valueOf(i+1);
		}		
		requestCount_comboBox.setModel(new DefaultComboBoxModel(requestValue));
		requestCount_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		requestCount_comboBox.setBackground(Color.WHITE);
		requestCount_comboBox.setBounds(504, 68, 87, 32);
		requestCount_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(requestCount_comboBox);
		
		form_resetButton = new JButton("Reset");
		form_resetButton.setFocusPainted(false);
		form_resetButton.setForeground(Color.BLACK);
		form_resetButton.setFont(FontManager.getFont(Font.BOLD, 14));
		form_resetButton.setBackground(Color.WHITE);
		form_resetButton.setBounds(798, 69, 77, 31);
		form_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				// 이미 Exception 스캔 작업이 진행중이라면 기존 수행중인 스캔 작업을 중지할지 물어본다.
				if(ExceptionScan_Panel.scanRunning) {
					scanStopConfirm();
					return;
				}
				
				global_rx = null;
				packetLog.setText(null);
				resetTable(table);
				
				autoTid_CheckBox.setSelected(false); // auto TID 해제
				autoTid_CheckBox.setText("Auto TID OFF"); // auto TID 해제
				autoTid_CheckBox.setForeground(Color.BLACK); // auto TID 해제
				
				/* JTextField */ transactionId_text.setText("1"); // Modbus TCP : TransactionID 필드
				transactionId_text.setForeground(Color.BLUE);
				/* JComboBox */ unitId_comboBox.setSelectedIndex(0); // 장비번호 콤보박스			
				/* JComboBox */ functionCode_comboBox.setSelectedIndex(2); // 기능코드
				/* JTextField */ startAddress_text.setText(null); // 시작주소, 제어주소 필드
				/* JComboBox */ requestCount_comboBox.setSelectedIndex(0); // 요청 개수																			
				modbusAddress_label.setText("Address preview");
				modbusAddress_label.setForeground(Color.DARK_GRAY);							
				
				timeout_text.setText("500"); // Timeout 필드
				timeout_text.setForeground(Color.BLUE);
				interval_text.setText("0"); // Interval 필드
				interval_text.setForeground(Color.BLUE);
				
				// 시작주소 필드에 포커스를 준다
				startAddress_text.requestFocus();
			}						
		});
		form_InputPanel.add(form_resetButton);
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// 검사 버튼
		form_sendPacketButton = new JButton("Scan");
		form_sendPacketButton.setFocusPainted(false);
		// 검사 버튼 클릭시 발생하는 이벤트
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 이미 Exception 스캔 작업이 진행중이라면 기존 수행중인 스캔 작업을 중지할지 물어본다.
				if(ExceptionScan_Panel.scanRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Exception Scanning Already in Progress\n"));
					sb.append(String.format("%s is already in progress%s%s%s",Util.colorBlue("Exception Scan") ,Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("If you want to stop what you're doing, please click %s%s%s%s", Util.colorRed("[ Stop ]") ,Util.separator, Util.separator, "\n"));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
				if(!checkReadRequestForm(isRTU)) return;
				int timeout = Integer.parseInt(timeout_text.getText().trim());
				if(timeout == 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Infinite Timeout?\n"));
					sb.append(String.format("If the timeout value is set to " + Util.colorBlue("0ms") + ", it waits indefinitely before receiving the response packet%s%s%s", Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("Do you really want to set the timeout to infinity and start %s?%s%s%s",Util.colorBlue("Exception Scan") ,Util.separator, Util.separator, "\n"));
					
					int isInfiniteTimeout = Util.showConfirm(sb.toString());
					
					if(isInfiniteTimeout == JOptionPane.YES_OPTION) {
						// YES
					} else {
						return; // NO
					}
				}
				if(timeout < 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Timeout Field Error\n"));					
					sb.append(String.format("Response timeout can only enter numeric values greater than " + Util.colorBlue("0ms") + "%s%s%s", Util.separator, Util.separator, "\n"));	
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int interval = Integer.parseInt(interval_text.getText().trim());
				if(interval < 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Interval Field Error\n"));					
					sb.append(String.format("Scan Interval can only enter numeric values greater than " + Util.colorBlue("0ms") + "%s%s%s", Util.separator, Util.separator, "\n"));	
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}				
				
				
				try {
					// Scan 작업 수행전 결과 테이블과 패킷로그 내용을 Reset
					resetTable(table);
					packetLog.setText(null);
					
					ExceptionScan_Panel.scanRunning = true;
					
					tx = initReadTX(isRTU);
					ArrayList<TX_Info> txList = TX_Info.getTxList(tx, autoTid_CheckBox.isSelected());
					
					new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								for(int i = 0; i < txList.size(); i++) {
											
									if(!ExceptionScan_Panel.scanRunning) {
										StringBuilder sb = new StringBuilder();
										sb.append(Util.colorRed("Stop Exception Scan\n"));
										sb.append(String.format("%s has been stopped%s%s%s",Util.colorBlue("Exception Scan") ,Util.separator, Util.separator, "\n"));
										Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
										return;
									}
									
									TX_Info tx = txList.get(i);
									tx.setAgentType("ExceptionScan");
									
									if(!isRTU) initTid(tx.getTransactionId());
									
									rx = ModbusAgent.registerScan(socket_en, tx, isRTU, timeout);
									
									if(rx == null) {
										// 전달받은 rx 가 없을 경우
										// ModbusAgent.exceptionScan() 에서 Timeout(패킷 못받음) 처리도 하였기 때문에
										// 해당 로직이 수행되는 경우는 응답 처리중 처리할 수 없는 예외가 발생 하였을 가능성이 높다
										rx = new RX_Info();
										rx.setTxInfo(tx);
										rx.setScanResult("Unprocessable Error");
									}else if(rx.isCRCError()) {
										// Modbus RTU : CRC16 Error (잘못된 CRC를 수신함)
										rx.setScanResult("CRC Error");
									}else if(rx.isException()) {
										// RX가 예외 응답일 경우
										rx.setScanResult(String.format("Exception : 0x%02X", rx.getExceptionCode()));
									}else if(rx.getScanResult() == null){
										// rx는 존재하는데 scanResult 내용이 존재하지 않는 경우
										// 해당 로직이 수행 될 일은 없을것같다
										rx.setScanResult("Unknown");
									}else if(rx.getScanResult().equalsIgnoreCase("Good")){
										// TX와 RX 정보 비교 (트랜잭션ID, 장비번호, 기능코드)
										StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
										
										// TX와 RX 정보 비교 후 출력 할 메시지가 있다면 출력 후 리턴
										if(hasCompareMessage(sb)) rx.setScanResult("TX, RX Data Mismatch");
									}
									
									addRecord(table, rx);
									
									// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 Reset를 해줘야한다.
									global_rx = rx;
									
									try {
										// 설정된 검사 간격(ms) 동안 대기 
										Thread.sleep(interval);
									}catch(Exception e) {
										e.printStackTrace();
									}
									
								}// end Send Packet (for loop) 
								
							}catch(Exception e) {
								e.printStackTrace();
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("ExceptionScan Error\n"));
								sb.append("An unprocessable exception occurred during the " + Util.colorBlue("Exception Scan") + Util.separator + "\n\n");
								sb.append(String.format("Exception Message : %s\n", e.getMessage()));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);																
							}finally {
								ExceptionScan_Panel.scanRunning = false;
							}
						}
					}).start();
					
				}catch(Exception exception) {
					exception.printStackTrace();
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorBlue("ExceptionScan Error\n"));
					sb.append("An unprocessable exception occurred during the " + Util.colorBlue("Exception Scan") + Util.separator + "\n\n");
					sb.append(String.format("Exception Message : %s\n", exception.getMessage()));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				}
								
			}
		}); // end formSendPacketButton Action
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(FontManager.getFont(Font.BOLD, 14));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(798, 28, 77, 31);		
		form_InputPanel.add(form_sendPacketButton);
		
		ButtonGroup commandTypeGroup= new ButtonGroup();
		
		modbusAddress_label = new JLabel("Address preview");		
		modbusAddress_label.setBackground(Color.WHITE);
		modbusAddress_label.setForeground(Color.DARK_GRAY);
		modbusAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAddress_label.setFont(FontManager.getFont(Font.BOLD, 16));
		modbusAddress_label.setBounds(260, 67, 157, 31);
		modbusAddress_label.setOpaque(true);
		form_InputPanel.add(modbusAddress_label);
		
		JLabel timeout_label = new JLabel("Timeout");
		timeout_label.setForeground(Color.BLACK);
		timeout_label.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_label.setFont(FontManager.getFont(Font.BOLD, 16));
		timeout_label.setBounds(608, 28, 77, 31);
		form_InputPanel.add(timeout_label);
		
		timeout_text = new JTextField();
		timeout_text.setText("500");
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(FontManager.getFont(Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(679, 28, 77, 31);
		timeout_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int timeout = 0;
				
				if(timeout_text.getText().startsWith("0x")||timeout_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(timeout_text.getText().startsWith("0x")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0x", ""),16); 
						if(timeout_text.getText().startsWith("0X")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						timeout = Integer.parseInt(timeout_text.getText());
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(timeout > Short.MAX_VALUE || timeout < 0) {
					timeout_text.setForeground(Color.RED);
				}else {
					timeout_text.setForeground(Color.BLUE);
				}
			}
		});
		timeout_text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int timeout = 0;
				
				if(timeout_text.getText().startsWith("0x")||timeout_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(timeout_text.getText().startsWith("0x")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0x", ""),16); 
						if(timeout_text.getText().startsWith("0X")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						timeout = Integer.parseInt(timeout_text.getText());
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(timeout > Short.MAX_VALUE || timeout < 0) {
					timeout_text.setForeground(Color.RED);
				}else {
					timeout_text.setForeground(Color.BLUE);
				}
			}
		});
		form_InputPanel.add(timeout_text);
		
		JLabel interval_label = new JLabel("Interval");
		interval_label.setForeground(Color.BLACK);
		interval_label.setHorizontalAlignment(SwingConstants.LEFT);
		interval_label.setFont(FontManager.getFont(Font.BOLD, 16));
		interval_label.setBounds(612, 69, 77, 31);
		form_InputPanel.add(interval_label);
		
		interval_text = new JTextField();
		interval_text.setText("0");
		interval_text.setForeground(Color.BLUE);
		interval_text.setHorizontalAlignment(SwingConstants.LEFT);
		interval_text.setFont(FontManager.getFont(Font.BOLD, 15));
		interval_text.setColumns(10);
		interval_text.setBorder(UIManager.getBorder("TextField.border"));
		interval_text.setBounds(679, 69, 77, 31);
		interval_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int interval = 0;
				
				if(interval_text.getText().startsWith("0x")||interval_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(interval_text.getText().startsWith("0x")) interval = Integer.parseInt(interval_text.getText().replaceAll("0x", ""),16); 
						if(interval_text.getText().startsWith("0X")) interval = Integer.parseInt(interval_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						interval = Integer.parseInt(interval_text.getText());
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}
				
				if (interval > Short.MAX_VALUE || interval < 0) {
					interval_text.setForeground(Color.RED);
				}else {
					interval_text.setForeground(Color.BLUE);
				}
			}
		});
		interval_text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int interval = 0;
				
				if(interval_text.getText().startsWith("0x")||interval_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(interval_text.getText().startsWith("0x")) interval = Integer.parseInt(interval_text.getText().replaceAll("0x", ""),16); 
						if(interval_text.getText().startsWith("0X")) interval = Integer.parseInt(interval_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						interval = Integer.parseInt(interval_text.getText());
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}
				
				if (interval > Short.MAX_VALUE || interval < 0) {
					interval_text.setForeground(Color.RED);
				}else {
					interval_text.setForeground(Color.BLUE);
				}
			}
		});
		form_InputPanel.add(interval_text);
		
		JLabel measureLabel = new JLabel("ms");
		measureLabel.setForeground(Color.BLACK);
		measureLabel.setHorizontalAlignment(SwingConstants.LEFT);
		measureLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		measureLabel.setBounds(759, 28, 35, 31);
		form_InputPanel.add(measureLabel);
		
		JLabel measureLable2 = new JLabel("ms");
		measureLable2.setForeground(Color.BLACK);
		measureLable2.setHorizontalAlignment(SwingConstants.LEFT);
		measureLable2.setFont(FontManager.getFont(Font.BOLD, 16));
		measureLable2.setBounds(759, 69, 35, 31);
		form_InputPanel.add(measureLable2);
		
		autoTid_CheckBox = new JCheckBox("Auto TID OFF");
		autoTid_CheckBox.setForeground(Color.BLACK);
		autoTid_CheckBox.setFocusPainted(false);
		autoTid_CheckBox.setFont(FontManager.getFont(Font.BOLD, 14));
		autoTid_CheckBox.setBackground(Color.WHITE);
		autoTid_CheckBox.setBounds(130, 4, 150, 20);
		autoTid_CheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(ExceptionScan_Panel.scanRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Auto TID can't set it up\n"));
					sb.append(String.format("%s You can't set the %s option while performing the function%s%s%s",Util.colorBlue("Exception Scan"), Util.colorBlue("Auto TID") ,Util.separator, Util.separator, "\n"));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					autoTid_CheckBox.setSelected(!autoTid_CheckBox.isSelected());
					
					return;
				}				
				
				if(autoTid_CheckBox.isSelected()) {
					autoTid_CheckBox.setText("Auto TID ON");
					autoTid_CheckBox.setForeground(Color.RED);
				}else {
					autoTid_CheckBox.setText("Auto TID OFF");
					autoTid_CheckBox.setForeground(Color.BLACK);
				}
			}
		});
		form_InputPanel.add(autoTid_CheckBox);
				
		typePanel = new JPanel();
		typePanel.setBackground(Color.WHITE);
		typePanel.setBounds(12, 509, 151, 107);
		actualPanel.add(typePanel);
		typePanel.setLayout(null);
		
		JLabel modbusType = new JLabel("Modbus Type");
		modbusType.setForeground(Color.BLACK);
		modbusType.setHorizontalAlignment(SwingConstants.LEFT);
		modbusType.setFont(FontManager.getFont(Font.BOLD, 16));
		modbusType.setBounds(12, 10, 129, 31);
		typePanel.add(modbusType);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setSelected(true);
		radio_modbusTCP.setFont(FontManager.getFont(Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 43, 171, 30);
		typePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(FontManager.getFont(Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 72, 171, 30);
		typePanel.add(radio_modbusRTU);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);			
		
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel2.setText(b.getText()); // 직접 입력 판넬
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				global_rx = null;
				modbusAddress_label.setText("Address preview");
				transactionId_text.setText(null);
				resetTable(table);				

				if (b.getText().contains("RTU")) {
					isRTU = true;
					autoTid_CheckBox.setVisible(false);
					transactionId_label.setVisible(false);
					transactionId_text.setVisible(false);
					startAddress_text.requestFocus();
				} else {
					isRTU = false;
					autoTid_CheckBox.setVisible(true);
					transactionId_label.setVisible(true);
					transactionId_text.setVisible(true);
					transactionId_text.setText("1");
					transactionId_text.setForeground(Color.BLUE);
					startAddress_text.requestFocus();
				}								
			}						
		};
		
		// 라디오 버튼(TCP/RTU)에 리스너 추가
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		panel_OFF();
		
		// 클라이언트 소켓이 접속중일때만 프레임에 정보를 표시한다.
		// 스레드
		new Thread() {
			public void run() {
				String lastState = "";
				
				while (true) {					
					try {
						Thread.sleep(500);
							
						if(lastState.equalsIgnoreCase(ClientSocket.getCurrentState())) {
							switch(lastState) {
								case ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING : panel_OFF(); break;
								case ClientSocket.SOCKET_STATUS_CONNECTED : panel_ON(); break;
								case ClientSocket.SOCKET_STATUS_CONNECTING : panel_OFF(); break;
								case ClientSocket.SOCKET_STATUS_COMMUNICATING : panel_ON(); break;
								case ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR : panel_ON(); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED : panel_OFF(); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_FAILED : panel_OFF(); break;
								case ClientSocket.SOCKET_STATUS_PING_FAILED : panel_OFF(); break;
								case ClientSocket.SOCKET_STATUS_WAITING_RESPONSE : panel_ON(); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF : panel_OFF(); break;
								default : panel_OFF();  break;
							}
						}
						
						switch(ClientSocket.getCurrentState()) {
							case ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_CONNECTED : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_CONNECTING : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_COMMUNICATING : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_FAILED : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_PING_FAILED : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_WAITING_RESPONSE : lastState = ClientSocket.getCurrentState(); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF : lastState = ClientSocket.getCurrentState(); break;
							default : lastState = ClientSocket.getCurrentState(); break;
						}
																
						currentState.setText(ClientSocket.getCurrentState());
						
						if(!resultPanel.isVisible()) {
							
						}
						
						switch(currentState.getText()) {
							case ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING : currentState.setForeground(Color.BLACK); break;
							case ClientSocket.SOCKET_STATUS_CONNECTED : currentState.setForeground(Color.BLUE); break;
							case ClientSocket.SOCKET_STATUS_CONNECTING : currentState.setForeground(Color.BLACK); break;
							case ClientSocket.SOCKET_STATUS_COMMUNICATING : currentState.setForeground(Color.BLUE); break;
							case ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR : currentState.setForeground(Color.RED); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED : currentState.setForeground(Color.BLACK); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_FAILED : currentState.setForeground(Color.RED); break;
							case ClientSocket.SOCKET_STATUS_PING_FAILED : currentState.setForeground(Color.RED); break;
							case ClientSocket.SOCKET_STATUS_WAITING_RESPONSE : currentState.setForeground(Color.BLUE); break;
							case ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF : currentState.setForeground(Color.RED); break;
							default : currentState.setForeground(Color.BLACK); break;
						}
						
						
						if(ExceptionScan_Panel.scanRunning) {
							// 예외 스캔 진행중
							form_resetButton.setText("Stop");
							form_resetButton.setForeground(Color.RED);
							formDisable();
						}else {
							// 예외 스캔 멈춤 상태
							form_resetButton.setText("Reset");
							form_resetButton.setForeground(Color.BLACK);
							formEnable();
						}						
						
						// ModbusAgent <=> ExceptionScan : Socket 동기화
						socket_en = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();					
		
		// 기본 패킷 전송 모드 : Modbus-RTU
		radio_modbusRTU.doClick();
		
	}// end ExeptionScan_Panel()
	
	
	public void panel_ON() {
		// 접속 전에는 판넬 컴포넌트들을 사용하지 않는다
		typePanel.setVisible(true);
		typePanel.setEnabled(true);
		inputFormPanel.setVisible(true);
		inputFormPanel.setEnabled(true);
		resultPanel.setVisible(true);
		resultPanel.setEnabled(true);				
		imagePanel.setVisible(false);
		imagePanel.setEnabled(false);
		infoPanel.setBounds(12, 10, 1050, 489); // 인포메이션 판넬 크기 정상화
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle(String.format("ModbusAnalyzer : %s", ClientSocket.getSimpleConnectedInfo()));
		}		
	}
	
	
	public void panel_OFF() {
		// 접속 전에는 판넬 컴포넌트들을 사용하지 않는다
		typePanel.setVisible(false);
		typePanel.setEnabled(false);
		inputFormPanel.setVisible(false);
		inputFormPanel.setEnabled(false);
		resultPanel.setVisible(false);
		resultPanel.setEnabled(false);
		imagePanel.setVisible(true);
		imagePanel.setEnabled(true);
		infoPanel.setBounds(12, 10, 1050, 606); // 인포메이션 판넬 전체모드
				
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle("ModbusAnalyzer");
		}		
				
	}
	
	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {				
			},
			new String[] {
				"index", "Register", "Modbus", "Scan Result"
			}){
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
			});
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(30);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);			
	}
	
	
	/**
	 * 2021-11-12
	 * 테이블 레코드 추가 메소드 : ExceptionScan_Panel 버전으로 작업 예정  
	 */
	public static void addRecord(JTable table, RX_Info rx) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			record = new Vector();
			int index = 0;
			
			if(table.getRowCount() <= 0) {
				// 테이블의 행 개수가 0개 일 경우 : index = 1
				index = 1;
			}else if(table.getRowCount() >= 1){
				// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
				index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;
			}
			
			/* column[0] */ record.add(String.valueOf(index)); // 순서
			/* column[1] */ record.add(String.format("0x%04X", rx.getTxInfo().getStartAddress())); // Register 주소
			/* column[2] */ record.add(String.format("%s%04d", rx.getTxInfo().getModbusAddress(), rx.getTxInfo().getStartAddress() + 1)); // Modbus 주소
			/* column[3] */ record.add(rx.getScanResult()); // Scan Result			
			
			model.addRow(record);
			
			setTable(table);
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();			
		}
	}

	
	
	public static void setTable(JTable table) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // 레지스터 주소
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // Address preview
		table.getColumnModel().getColumn(3).setPreferredWidth(150); // 스캔 결과
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		ScanCellRenderer resultScanCellRenderer = new ScanCellRenderer();		
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);		
		resultScanCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서		
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // Address preview
		tcmSchedule.getColumn(3).setCellRenderer(resultScanCellRenderer); // 스캔 결과			
	}
	
	
	public static void setConnectionInfo() {
		String[] connectionInfo = ClientSocket.getConnectionInfo();
		IP = connectionInfo[0];
		PORT = Integer.parseInt(connectionInfo[1]);
	}
	
	
	public static void componentAllClear() {
		form_resetButton.doClick();
	}
	
	
	public static JTextArea getPacketLog() {
		return packetLog;
	}
	
	
	public static JTable getResultTable() {
		return table;
	}
	
	
	public static void scrollUp() {
//		int pos = packetLog.getText().length();
//		packetLog.setCaretPosition(pos);
		packetLog_scrollPane.getVerticalScrollBar().setValue(packetLog_scrollPane.getVerticalScrollBar().getMaximum());
		scrollPane.getVerticalScrollBar().setValue(packetLog_scrollPane.getVerticalScrollBar().getMaximum());
	}
		
	
	public void initTid(int tid) {		
		if(transactionId_text.getText().trim().startsWith("0x") || transactionId_text.getText().trim().startsWith("0X")){
			transactionId_text.setText(String.format("0x%04X",tid));										
		}else {
			transactionId_text.setText(String.format("%d",tid));
		}				
	}
	
	
	// 수집 요청 패킷 생성 정보 유효성 확인
	public boolean checkReadRequestForm(boolean isRTU) {
		boolean isValid = true;				
		int nullCount = 0;
		int invalidCount = 0;
				
		if(startAddress_text.getText().length() == 0 
			|| timeout_text.getText().length() == 0 
			|| interval_text.getText().length() == 0
			|| (!isRTU && transactionId_text.getText().length() == 0)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
			
			// Transaction ID null 검사
			if(!isRTU && transactionId_text.getText().length() == 0) {
				nullCount++;
				sb.append(Util.colorBlue("Transaction ID"));					
			}
			
			
			// 시작 주소 null 검사
			if(startAddress_text.getText().length() == 0) {
				if(nullCount > 0)
					sb.append(Util.colorBlue(", Start address"));
				else						
					sb.append(Util.colorBlue("Start address"));
				
				nullCount++;
			}
			
			// Timeout null 검사
			if(timeout_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", Timeout"));
				else						
					sb.append(Util.colorBlue("Timeout"));
				
				nullCount++;
			}
			
			// Interval null 검사
			if(interval_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", Interval"));
				else
					sb.append(Util.colorBlue("Interval"));
				
				nullCount++;
			}
			
			sb.append(" information is missing" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;			
			
			return isValid;
		}
		
		// 유효하지 않은 startAddress 입력 시 메시지 출력 후 리턴
		if(startAddress_text.getForeground() == Color.RED 
			|| timeout_text.getForeground() == Color.RED
			|| interval_text.getForeground() == Color.RED
			|| (!isRTU && transactionId_text.getForeground() == Color.RED)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
			sb.append("Please check the ");								
			
			// 시작주소 양식 검사
			if(!isRTU && transactionId_text.getForeground() == Color.RED) {
				invalidCount++;
				sb.append(Util.colorBlue("Transaction ID"));
			}
			
			// 시작주소 양식 검사
			if(startAddress_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", Start address"));
				else
					sb.append(Util.colorBlue("Start address"));
				
				invalidCount++;
			}
			
			// Timeout 양식 검사			
			if(timeout_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", Timeout"));
				else
					sb.append(Util.colorBlue("Timeout"));
				
				invalidCount++;
			}
			
			// Interval 양식 검사				
			if(interval_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", Interval"));
				else
					sb.append(Util.colorBlue("Interval"));
				
				invalidCount++;
			}
							
			sb.append(" information you entered" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;					
			
			return isValid;
		}
		
		return isValid;
	}
		
	
		
	
	public void scanStopConfirm() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Stop Exception Scanning?\n"));		
		sb.append(String.format("Shall we stop %s that we are currently performing?%s%s%s",Util.colorBlue("Exception Scan") ,Util.separator, Util.separator, "\n"));
		
		int scanStop = Util.showConfirm(sb.toString());
		
		if(scanStop == JOptionPane.YES_OPTION) {
			ExceptionScan_Panel.scanRunning = false;
		} else {
			return;
		}
	}
	
		
		
	// 수집 요청 TX Reset
	public TX_Info initReadTX(boolean isRTU) {
		TX_Info tx = null;		
		int transactionId;
		int unitId;
		int functionCode;
		int startAddress;
		int requestCount;
		
		try {
			if(isRTU) {
				// ModbusRTU : TX
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("번", "").trim());
					functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
					if(startAddress_text.getText().trim().startsWith("0x")){
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
					}else if(startAddress_text.getText().trim().startsWith("0X")) {
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
					}else {
						startAddress = Integer.parseInt(startAddress_text.getText());
					}														
					requestCount = Integer.parseInt((String)requestCount_comboBox.getSelectedItem().toString().replaceAll("개", "").trim());					
					tx = new TX_Generator().generateReadRTU(unitId, functionCode, startAddress, requestCount);					
					return tx;
				
			}else {
				// ModbusTCP : TX
					if(transactionId_text.getText().trim().startsWith("0x")){
						transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0x", ""),16);								
					}else if(transactionId_text.getText().trim().startsWith("0X")) {
						transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0X", ""),16);
					}else {
						transactionId = Integer.parseInt(transactionId_text.getText());
					}	
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("번", "").trim());
					functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
					if(startAddress_text.getText().trim().startsWith("0x")){
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
					}else if(startAddress_text.getText().trim().startsWith("0X")) {
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
					}else {
						startAddress = Integer.parseInt(startAddress_text.getText());
					}														
					requestCount = Integer.parseInt((String)requestCount_comboBox.getSelectedItem().toString().replaceAll("개", "").trim());					
					tx = new TX_Generator().generateReadTCP(transactionId, 0x0000, 0x0006, unitId, functionCode, startAddress, requestCount);					
					return tx;
			}
		}
		catch(Exception e) {
			// TX Reset 중 예외발생 시 null 리턴
			return null;
		}		
	}
	
	
	
	
	// 요청패킷, 응답패킷 비교 결과  출력
	public boolean hasCompareMessage(StringBuilder sb) {
		if(sb != null) {			
			// 요청패킷과 응답패킷 정보가 다르다
			return true;
		}else {
			// 요청패킷과 응답패킷 정보가 같다
			return false;
		}
	}

	public void formDisable() {			
		radio_modbusTCP.setEnabled(false);
		radio_modbusRTU.setEnabled(false);
		transactionId_text.setEnabled(false);
		unitId_comboBox.setEnabled(false);
		functionCode_comboBox.setEnabled(false);
		startAddress_text.setEnabled(false);
		requestCount_comboBox.setEnabled(false);
		timeout_text.setEnabled(false);
		interval_text.setEnabled(false);
	}
	
	public void formEnable() {
		radio_modbusTCP.setEnabled(true);
		radio_modbusRTU.setEnabled(true);
		transactionId_text.setEnabled(true);
		unitId_comboBox.setEnabled(true);
		functionCode_comboBox.setEnabled(true);
		startAddress_text.setEnabled(true);
		requestCount_comboBox.setEnabled(true);
		timeout_text.setEnabled(true);
		interval_text.setEnabled(true);
	}
	

	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 
			// 이벤트 발동 조건 : 마우스 오른쪽 클릭
			if (e.getButton() == 3) {
				
			}			 
//			 if (e.getButton() == 1) {  } // 왼쪽 클릭
//			 if (e.getButton() == 3) { } // 오른쪽 클릭				
		}
	}
}

