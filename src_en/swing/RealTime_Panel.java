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

import src_en.agent.ClientSocket;
import src_en.agent.ModbusAgent;
import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.info.ONION_Info;
import src_en.info.RX_Info;
import src_en.info.TX_Info;
import src_en.util.ExceptionProvider;
import src_en.util.JavaScript;
import src_en.util.TX_Generator;
import src_en.util.Util;

public class RealTime_Panel extends JPanel {
	
	private static boolean monitoringRunning = false;
	
	// 클라이언트 소켓
	private Socket socket = ModbusAgent.clientSocket;
	private static String IP;
	private static int PORT;	
	
	// information Component
	JPanel infoPanel; // 클라이언트 소켓이 서버와 연결 된 상태일때만 인포메이션 컴포넌트들을 활성화 시킨다.  
	JPanel inputFormPanel;
	JPanel typePanel;
	JPanel resultPanel;
	JPanel dataTypePanel;
	JPanel imagePanel; /* ONION Image */
	
	private JButton connectButton; // 연결 정보 입력버튼 (중요)
	private static boolean isRTU = false; // Default : Modbus TCP (아주 중요한 변수)
	public static JComboBox dataTypeComboBox = null;
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
	private static JLabel form_scale_label;
	private static JTextField form_scale_textField;
	private static JButton form_resetButton;
	private static JRadioButton radio_modbusTCP;
	private static JRadioButton radio_modbusRTU;
	
	// 통신 기록
	public static JScrollPane packetLog_scrollPane;
	public static JTextArea packetLog;
	public static MessageFrame packetlog_Frame;
	public TX_Info tx;
	public RX_Info rx;
	
	private KeyEvent lastKeyEvent;	
	private JTextField timeout_text;
	private JTextField interval_text;

	private static JCheckBox autoTid_CheckBox;
	
//	private WatchPointSettingFrame watchPointSettingFrame;
	
	/**
	 * Create the panel.
	 */
	public RealTime_Panel(){	
		setBorder(new EmptyBorder(0, 0, 0, 0));
	
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(new Color(139, 0, 0));
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 606);
		actualPanel.add(infoPanel);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Real-Time Monitoring");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 266, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 20));
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
		
		JScrollPane resultTable_ScrollPane = new JScrollPane();
		resultTable_ScrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		resultTable_ScrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		resultTable_ScrollPane.setBackground(Color.WHITE);
		resultTable_ScrollPane.setBounds(578, 10, 438, 405);
		resultPanel.add(resultTable_ScrollPane);
		
		// 테이블 생성 부분
		table = new JTable();
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) { } // 왼쪽 버튼 더블 클릭
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int column = table.columnAtPoint(e.getPoint());
					int row = table.rowAtPoint(e.getPoint());
					table.changeSelection(row, column, false, false);
					table.requestFocus();
					int[] selectedIndex = table.getSelectedRows();
					Perf.showBitStatus(table, selectedIndex, RealTime_Panel.dataTypeComboBox.getSelectedItem().toString());
				}
			}
		});
		resetTable(table);
		
		resultTable_ScrollPane.setViewportView(table);
		
		packetLog_scrollPane = new JScrollPane();
		packetLog_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		packetLog_scrollPane.setBounds(12, 10, 553, 405);
		resultPanel.add(packetLog_scrollPane);
		
		
		packetLog = new JTextArea();		
		packetLog.setFont(new Font("맑은 고딕", Font.PLAIN, 16));				
		packetLog_scrollPane.setViewportView(packetLog);				
		
		dataTypePanel = new JPanel();
		dataTypePanel.setBackground(Color.WHITE);
		dataTypePanel.setBounds(612, 10, 426, 39);
		infoPanel.add(dataTypePanel);
		dataTypePanel.setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("Data Type");
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 89, 39);
		dataTypePanel.add(lblNewLabel);
		
		dataTypeComboBox = new JComboBox();
		dataTypeComboBox.setBackground(Color.WHITE);
		dataTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"ASCII CODE", "UNI CODE", "", "BINARY", "HEX", "", "TWO BYTE INT SIGNED", "TWO BYTE INT UNSIGNED", "", "FOUR BYTE INT SIGNED (A B C D)", "FOUR BYTE INT SIGNED (D C B A)", "FOUR BYTE INT SIGNED (B A D C)", "FOUR BYTE INT SIGNED (C D A B)", "", "FOUR BYTE INT UNSIGNED (A B C D)", "FOUR BYTE INT UNSIGNED (D C B A)", "FOUR BYTE INT UNSIGNED (B A D C)", "FOUR BYTE INT UNSIGNED (C D A B)", "", "FOUR BYTE FLOAT (A B C D)", "FOUR BYTE FLOAT (D C B A)", "FOUR BYTE FLOAT (B A D C)", "FOUR BYTE FLOAT (C D A B)", "", "EIGHT BYTE INT SIGNED (A B C D)", "EIGHT BYTE INT SIGNED (D C B A)", "EIGHT BYTE INT SIGNED (B A D C)", "EIGHT BYTE INT SIGNED (C D A B)", "", "EIGHT BYTE INT UNSIGNED (A B C D)", "EIGHT BYTE INT UNSIGNED (D C B A)", "EIGHT BYTE INT UNSIGNED (B A D C)", "EIGHT BYTE INT UNSIGNED (C D A B)", "", "EIGHT BYTE DOUBLE (A B C D)", "EIGHT BYTE DOUBLE (D C B A)", "EIGHT BYTE DOUBLE (B A D C)", "EIGHT BYTE DOUBLE (C D A B)"}));
		dataTypeComboBox.setSelectedIndex(6);
		dataTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dataTypeComboBox.setBounds(88, 5, 337, 30);
		dataTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();				
				updateTable(table, global_rx);
			}
		});
		
		
		dataTypePanel.add(dataTypeComboBox);
		
		currentState = new JLabel();		
		currentState.setOpaque(true);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(267, 5, 193, 45);
		infoPanel.add(currentState);
		
		connectButton = new JButton("Connect");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(468, 11, 110, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 클라이언트 소켓의 마지막 커넥션 정보
				String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
				
				try {
					socket = ModbusAgent.clientSocket;
					
					if( (socket == null || socket.isClosed()) && ClientSocket.getIsFirst()) {						
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
					}else if(socket == null) {
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
					}else {
						// 기존 연결되어있는 소켓일 경우 연결을 끊고 재접속을 시도한다.						
						// 클라이언트 소켓 : 접속 끊김
						socket.close();
						ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
					}
				}catch(IOException exception) {
					return;
				}
				
				try {
					socket = ClientSocket.getClientSocket(IP, PORT);
				}catch(Exception exception) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='red'>Failed to connect</font>\n");
					msg.append("Please check the connection information you entered" + Util.separator + Util.separator + "\n");					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}				
				
				if(socket != null || ClientSocket.isCurrentConnected(socket)) {
					// 접속 성공 : 컴포넌트 내용들을 모두 Reset한다	
					ModbusAgent.clientSocket = socket;
					panel_ON();
					
					// 마지막 커넥션 정보와 다른 정보로 세션을  생성시 컴포넌트 Reset
					if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
						componentAllClear();
					}
					
					// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장					
					ClientSocket.setHasLastConnectionInfo(true);
				}
			}
		});
		
		infoPanel.add(connectButton);
		
		dataTypePanel.setVisible(false); // functionCode 3, 4 일때만 데이터 타입 콤보박스 표현 (functionCdoe 1, 2 : ON/OFF 상태이기 때문에)		
		inputFormPanel = new JPanel();
		inputFormPanel.setBackground(Color.WHITE);
		inputFormPanel.setBounds(165, 509, 897, 107);
		actualPanel.add(inputFormPanel);
		inputPanel_layout = new CardLayout(0, 0);
		inputFormPanel.setLayout(inputPanel_layout);
		
		JPanel form_InputPanel = new JPanel();
		form_InputPanel.setLayout(null);
		form_InputPanel.setBackground(Color.WHITE);
		inputFormPanel.add(form_InputPanel, "form_InputPanel");
		
		JLabel typeLabel2 = new JLabel("Modbus TCP");
		typeLabel2.setHorizontalAlignment(SwingConstants.LEFT);
		typeLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		typeLabel2.setBounds(12, 10, 129, 31);
		form_InputPanel.add(typeLabel2);
		
		JLabel transactionId_label = new JLabel("TID");
		transactionId_label.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		transactionId_label.setBounds(132, 31, 26, 31);
		form_InputPanel.add(transactionId_label);
		
		transactionId_text = new JTextField();
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(164, 31, 85, 31);
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
		unitId_label.setHorizontalAlignment(SwingConstants.LEFT);
		unitId_label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		unitId_label.setBounds(266, 31, 77, 31);
		form_InputPanel.add(unitId_label);
		
		unitId_comboBox = new JComboBox();
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1);
		}		
		unitId_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		unitId_comboBox.setSelectedIndex(0);
		unitId_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		unitId_comboBox.setBackground(Color.WHITE);
		unitId_comboBox.setBounds(338, 31, 85, 32);
		form_InputPanel.add(unitId_comboBox);
		
		JLabel startAddress_label = new JLabel("Start Addr");
		startAddress_label.setHorizontalAlignment(SwingConstants.RIGHT);
		startAddress_label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		startAddress_label.setBounds(17, 71, 141, 31);
		form_InputPanel.add(startAddress_label);
		
		startAddress_text = new JTextField();
		startAddress_text.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		startAddress_text.setColumns(10);
		startAddress_text.setBorder(UIManager.getBorder("TextField.border"));
		startAddress_text.setBounds(164, 72, 85, 31);	
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
		functionCode_label.setHorizontalAlignment(SwingConstants.LEFT);
		functionCode_label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		functionCode_label.setBounds(435, 31, 77, 31);
		form_InputPanel.add(functionCode_label);
		
		functionCode_comboBox = new JComboBox();
		functionCode_comboBox.setBackground(Color.WHITE);		
		functionCode_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
		functionCode_comboBox.setSelectedIndex(2);
		functionCode_comboBox.setBounds(507, 31, 85, 32);
		functionCode_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();								
				
				// 기능코드 콤보박스 내용 변경시 사용자에게 표시해줄 모드버스 시작주소를 변경해주어야 하기 때문에
				// 시작주소 텍스트필드의 keyEvent를 발생시켜준다
				KeyListener keyListener =  startAddress_text.getKeyListeners()[0];
				if(keyListener != null) keyListener.keyReleased(lastKeyEvent);				
			}
		});
		form_InputPanel.add(functionCode_comboBox);
		
		
		JLabel requestCount_label = new JLabel("Req Count");
		requestCount_label.setHorizontalAlignment(SwingConstants.LEFT);
		requestCount_label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		requestCount_label.setBounds(424, 71, 88, 31);
		form_InputPanel.add(requestCount_label);
		
		requestCount_comboBox = new JComboBox();		
		String[] requestValue = new String[125];
		for(int i = 0; i < 125; i++) {
			requestValue[i] = String.valueOf(i+1);
		}		
		requestCount_comboBox.setModel(new DefaultComboBoxModel(requestValue));
		requestCount_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		requestCount_comboBox.setBackground(Color.WHITE);
		requestCount_comboBox.setBounds(507, 71, 85, 32);
		form_InputPanel.add(requestCount_comboBox);
		
		form_resetButton = new JButton("Reset");
		form_resetButton.setFocusPainted(false);
		form_resetButton.setForeground(Color.BLACK);
		form_resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		form_resetButton.setBackground(Color.WHITE);
		form_resetButton.setBounds(797, 71, 88, 31);
		form_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				
				// 이미 Exception 스캔 작업이 진행중이라면 기존 수행중인 스캔 작업을 중지할지 물어본다.
				if(RealTime_Panel.monitoringRunning) {
					monitoringStopConfirm();
					return;
				}
				
				global_rx = null;
				dataTypePanel.setVisible(false);
				packetLog.setText(null);
				resetTable(table);
								
				autoTid_CheckBox.setSelected(false); // auto TID 해제
				autoTid_CheckBox.setText("Auto TID OFF"); // auto TID 해제
				autoTid_CheckBox.setForeground(Color.BLACK); // auto TID 해제
				
				form_scale_textField.setText("");				
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
				interval_text.setText("1000"); // Interval 필드
				interval_text.setForeground(Color.BLUE);								
				
				// 시작주소에 포커스
				startAddress_text.requestFocus();
			}						
		});
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// TX Form 전송 버튼
		form_sendPacketButton = new JButton("Execute");
		form_sendPacketButton.setFocusPainted(false);
		// 전송 버튼 클릭시 발생하는 이벤트
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
							
				// 이미 Exception 스캔 작업이 진행중이라면 기존 수행중인 스캔 작업을 중지할지 물어본다.
				if(RealTime_Panel.monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Real-Time Monitoring Already in Progress\n"));
					sb.append(String.format("%s is already in progress%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("If you want to stop what you're doing, please click %s%s%s%s", Util.colorRed("[ Stop ]") ,Util.separator, Util.separator, "\n"));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				// 수집 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
				if(!checkReadRequestForm(isRTU)) return;
				int timeout = Integer.parseInt(timeout_text.getText().trim());																
				if(timeout == 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Infinite Timeout?\n"));
					sb.append(String.format("If the timeout value is set to " + Util.colorBlue("0ms") + ", it waits indefinitely before receiving the response packet%s%s%s", Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("Do you really want to set the timeout to infinity and start %s?%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
					
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
					sb.append(String.format("Monitoring Interval can only enter numeric values greater than " + Util.colorBlue("0ms") + "%s%s%s", Util.separator, Util.separator, "\n"));	
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}				
				
				
				try {						
					
					// 모니터링 상태 : 시작
					RealTime_Panel.monitoringRunning = true;
					
					// 한번 Reset된 TX 내용으로 계속해서 수집
					tx = initReadTX(isRTU);
					tx.setAgentType("RealTime");
					
					
					/**
					 * Monitoring Start ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
					 */
					
					new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								while(RealTime_Panel.monitoringRunning) {
											
									if(!RealTime_Panel.monitoringRunning) {
										StringBuilder sb = new StringBuilder();
										sb.append(Util.colorRed("Stop Real-Time Monitoring\n"));
										sb.append(String.format("%s has been stopped%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
										Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
										return;
									}
									
									// Modbus TCP : TID 자동 증가
									if(!isRTU && autoTid_CheckBox.isSelected()) {
										// 증가된 TID 반영하여 재구성										
										int tid = tx.getTransactionId();
										
										if(tid >= Short.MAX_VALUE) {
											tid = 1;
											initTid(tid);
										}else {
											initTid(tid + 1);	
										}
										
										tx = initReadTX(isRTU);
										tx.setAgentType("RealTime");
									}
									
									rx = ModbusAgent.communicate(socket, tx, isRTU, timeout);
									
									// 유효하지 않은 응답은 패스한다
									if(rx == null) continue;
									if(rx.isException()) continue;
									if(rx.isCRCError()) continue; 										
									if(rx.getScanResult() == null) continue;									
									if(ExceptionProvider.CompareTxRx(tx, rx) != null) continue;									
									
									// 기능코드가 3, 4 일때만 데이터 타입 선택 콤보박스를 표시
									setDataType(rx);																																																				
																																														
									// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 Reset를 해줘야한다.
									global_rx = rx;
									updateTable(table, rx);
									ModbusAgent.isRTU = isRTU;
									ModbusAgent.lastFunctionCode = rx.getFunctionCode();
																		
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
								sb.append(Util.colorRed("Real-Time Monitoring Error\n"));
								sb.append("An unprocessable exception occurred during the " + Util.colorBlue("Real-Time Monitoring") + Util.separator + "\n\n");
								sb.append(String.format("Exception Message : %s\n", e.getMessage()));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);																
							}finally {
								RealTime_Panel.monitoringRunning = false;
							}
						}
					}).start();					
					
					/**
					 * Monitoring End ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
					 */					
					
					
				}catch(Exception exception) {
					resetTable(table);
					exception.printStackTrace();
					RealTime_Panel.monitoringRunning = false;
				}				
				
			}			
		}); // end formSendPacketButton Action
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(797, 31, 88, 31);		
		form_InputPanel.add(form_sendPacketButton);
		form_InputPanel.add(form_resetButton);
				
		
		modbusAddress_label = new JLabel("Address preview");		
		modbusAddress_label.setBackground(Color.WHITE);
		modbusAddress_label.setForeground(Color.DARK_GRAY);
		modbusAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAddress_label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		modbusAddress_label.setBounds(266, 70, 160, 31);
		modbusAddress_label.setOpaque(true);
		form_InputPanel.add(modbusAddress_label);
				
		
		
		form_scale_label = new JLabel("Scale");
		form_scale_label.setBackground(Color.WHITE);
		form_scale_label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		form_scale_label.setForeground(new Color(0, 128, 0));
		form_scale_label.setHorizontalAlignment(SwingConstants.CENTER);
		form_scale_label.setBounds(447, 4, 57, 23);
		form_scale_label.setVisible(false);
		form_InputPanel.add(form_scale_label);
		
		form_scale_textField = new JTextField();
		form_scale_textField.setText("");		
		form_scale_textField.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		form_scale_textField.setBackground(Color.WHITE);
		form_scale_textField.setHorizontalAlignment(SwingConstants.LEFT);		
		form_scale_textField.setBounds(507, 5, 381, 21);
		form_scale_textField.setColumns(10);
		form_scale_textField.setVisible(false);
		form_InputPanel.add(form_scale_textField);
		
		JLabel timeout_Label = new JLabel("Timeout");
		timeout_Label.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_Label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		timeout_Label.setBounds(604, 31, 77, 31);
		form_InputPanel.add(timeout_Label);
		
		timeout_text = new JTextField();
		timeout_text.setText("500");
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(675, 31, 77, 31);
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
		
		JLabel interval_Label = new JLabel("Interval");
		interval_Label.setHorizontalAlignment(SwingConstants.LEFT);
		interval_Label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		interval_Label.setBounds(609, 71, 77, 31);
		form_InputPanel.add(interval_Label);
		
		interval_text = new JTextField();
		interval_text.setText("1000");
		interval_text.setForeground(Color.BLUE);
		interval_text.setHorizontalAlignment(SwingConstants.LEFT);
		interval_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		interval_text.setColumns(10);
		interval_text.setBorder(UIManager.getBorder("TextField.border"));
		interval_text.setBounds(675, 71, 77, 31);
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
		
		JLabel timeoutMeasure_Label = new JLabel("ms");
		timeoutMeasure_Label.setHorizontalAlignment(SwingConstants.LEFT);
		timeoutMeasure_Label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		timeoutMeasure_Label.setBounds(755, 31, 35, 31);
		form_InputPanel.add(timeoutMeasure_Label);
		
		JLabel intervalMeasure_Label = new JLabel("ms");
		intervalMeasure_Label.setHorizontalAlignment(SwingConstants.LEFT);
		intervalMeasure_Label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		intervalMeasure_Label.setBounds(755, 71, 35, 31);
		form_InputPanel.add(intervalMeasure_Label);
		
		autoTid_CheckBox = new JCheckBox("Auto TID OFF");
		autoTid_CheckBox.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		autoTid_CheckBox.setFocusPainted(false);
		autoTid_CheckBox.setBackground(Color.WHITE);
		autoTid_CheckBox.setBounds(130, 7, 129, 20);
		autoTid_CheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(RealTime_Panel.monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Auto TID can't set it up\n"));
					sb.append(String.format("%s You can't set the %s option while performing the function%s%s%s",Util.colorBlue("Real-Time Monitoring"), Util.colorBlue("Auto TID") ,Util.separator, Util.separator, "\n"));
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
		typePanel.setBounds(12, 509, 141, 107);
		actualPanel.add(typePanel);
		typePanel.setLayout(null);
		
		JLabel modbusType = new JLabel("Modbus Type");
		modbusType.setHorizontalAlignment(SwingConstants.LEFT);
		modbusType.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		modbusType.setBounds(12, 10, 129, 31);
		typePanel.add(modbusType);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setSelected(true);
		radio_modbusTCP.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 43, 125, 30);
		typePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 72, 125, 30);
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
				// 데이터 타입 판넬 숨기기 , 데이터 타입 콤보박스 내용 Reset
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() 까지 호출됨
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
							// resultPanel의 isVisible() 여부를 검사하여 접속이 끊어졌을 때
							// dataTypePanel 도 컴포넌트에서 숨긴다.
							dataTypePanel.setVisible(false);
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
						
						
						if(RealTime_Panel.monitoringRunning) {
							// 실시간 모니터링 진행중
							form_resetButton.setText("Stop");
							form_resetButton.setForeground(Color.RED);
							formDisable();
							
						}else {
							// 실시간 모니터링 상태
							form_resetButton.setText("Reset");
							form_resetButton.setForeground(Color.BLACK);
							formEnable();
						}
						
						// ModbusAgent <=> ExceptionScan : Socket 동기화
						socket = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
		
		radio_modbusRTU.doClick();
		
	}// end RealTime_Panel()
	
	
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
		
		if(packetlog_Frame != null) {
			packetlog_Frame.dispose();
			packetlog_Frame = null;
		}
		
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle("ModbusAnalyzer");
		}
	}
	
	
	public void monitoringStopConfirm() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Stop Monitoring?\n"));		
		sb.append(String.format("Shall we stop %s that we are currently performing?%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
		
		int monitoringStop = Util.showConfirm(sb.toString());
		
		if(monitoringStop == JOptionPane.YES_OPTION) {
			RealTime_Panel.monitoringRunning = false;
		} else {
			return;
		}
	}
	
	
	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				// 테이블 기본 셀 없음
			},
			new String[] {
				"index", "Register", "Modbus", "Register Value"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // 레지스터 주소
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // Address preview
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 값
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);			
	}
	
	
	public static void updateTable(JTable table, RX_Info rx) {
		
		if((table == null)||(rx == null)||(rx.getPerfInfo() == null)) {
			return;
		}
		
		// 테이블 헤더 설정
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));

		Object[][] content = null;
		
		// 결과 테이블의 행을 결정해주는 변수
		int tableRow;
		
		if(rx.getFunctionCode() == 0x01 || rx.getFunctionCode() == 0x02) {
			// 기능코드 01, 02 같은 경우에는 성능을 8개 미만으로 요청하여도 무조건 바이트 단위로 읽어서 비트연산 하기때문에 결과 테이블 행 개수를 별도로 설정한다
			// 예를들어 성능 3개만을 요청하여도 바이트 단위로 읽어 8bit를 표시한다.		
			// 성능 3개만 요청했을때 읽은 8개를 표시해주어도 그 값에 문제만 없으면 상관이 없지만 3개만 요청하고 읽은 8비트 중 3개만 정상적으로 표시되고
			// 나머지 5개 비트에 대해서는 모두 OFF 값을 주기때문에 요청한 개수만큼 행을 표시해주기로 결정했다
			tableRow = rx.getTxInfo().getRequestCount();
		}else {
			// FC 03 , 04
			tableRow = rx.getPerfInfo().length;
		}
				
		if(isRTU) {
			// Modbus RTU : 테이블의 마지막 셀에 CRC 내용을 표시해주기 위해서 성능 개수보다 셀을 한개 더 많도록 설정			
			content = new Object[tableRow + 1][];			
		}else {
			// Modbus TCP
			content = new Object[tableRow][];
		}
		
		Object[] value = null;
				
			if((rx.getFunctionCode()==1)||(rx.getFunctionCode()==2)) {
				value = DataType.init_STATUS(rx); /* FC 01, 02 (ON/OFF) */ 
			} else {
				switch (dataTypeComboBox.getSelectedItem().toString()) {
					case "ASCII CODE": value = DataType.init_ASCII_CODE(rx); break;
					case "UNI CODE": value = DataType.init_UNI_CODE(rx); break;
					
					case "BINARY": value = DataType.init_BINARY(rx); break;
					case "HEX": value = DataType.init_HEX(rx); break;
			
					case "TWO BYTE INT SIGNED": /* Default */ value = DataType.init_TWO_BYTE_INT_SIGNED(rx); break;
					case "TWO BYTE INT UNSIGNED": value = DataType.init_TWO_BYTE_INT_UNSIGNED(rx); break;
			
					case "FOUR BYTE INT SIGNED (A B C D)": /* A B C D */ value = DataType.init_FOUR_BYTE_INT_SIGNED_ABCD(rx); break;
					case "FOUR BYTE INT SIGNED (D C B A)": /* D C B A */ value = DataType.init_FOUR_BYTE_INT_SIGNED_DCBA(rx); break;
					case "FOUR BYTE INT SIGNED (B A D C)": /* B A D C */ value = DataType.init_FOUR_BYTE_INT_SIGNED_BADC(rx); break;
					case "FOUR BYTE INT SIGNED (C D A B)": /* C D A B */ value = DataType.init_FOUR_BYTE_INT_SIGNED_CDAB(rx); break;
			
					case "FOUR BYTE INT UNSIGNED (A B C D)": /* A B C D */ value = DataType.init_FOUR_BYTE_INT_UNSIGNED_ABCD(rx); break;
					case "FOUR BYTE INT UNSIGNED (D C B A)": /* D C B A */ value = DataType.init_FOUR_BYTE_INT_UNSIGNED_DCBA(rx); break;
					case "FOUR BYTE INT UNSIGNED (B A D C)": /* B A D C */ value = DataType.init_FOUR_BYTE_INT_UNSIGNED_BADC(rx); break;
					case "FOUR BYTE INT UNSIGNED (C D A B)": /* C D A B */ value = DataType.init_FOUR_BYTE_INT_UNSIGNED_CDAB(rx); break;
			
					case "FOUR BYTE FLOAT (A B C D)": /* A B C D */ value = DataType.init_FOUR_BYTE_FLOAT_ABCD(rx); break;
					case "FOUR BYTE FLOAT (D C B A)": /* D C B A */ value = DataType.init_FOUR_BYTE_FLOAT_DCBA(rx); break;
					case "FOUR BYTE FLOAT (B A D C)": /* B A D C */ value = DataType.init_FOUR_BYTE_FLOAT_BADC(rx); break;
					case "FOUR BYTE FLOAT (C D A B)": /* C D A B */ value = DataType.init_FOUR_BYTE_FLOAT_CDAB(rx); break;
					
					case "EIGHT BYTE INT SIGNED (A B C D)": /* A B C D */ value = DataType.init_EIGHT_BYTE_INT_SIGNED_ABCD(rx); break;
					case "EIGHT BYTE INT SIGNED (D C B A)": /* D C B A */ value = DataType.init_EIGHT_BYTE_INT_SIGNED_DCBA(rx); break;
					case "EIGHT BYTE INT SIGNED (B A D C)": /* B A D C */ value = DataType.init_EIGHT_BYTE_INT_SIGNED_BADC(rx); break;
					case "EIGHT BYTE INT SIGNED (C D A B)": /* C D A B */ value = DataType.init_EIGHT_BYTE_INT_SIGNED_CDAB(rx); break;
					
					case "EIGHT BYTE INT UNSIGNED (A B C D)": /* A B C D */ value = DataType.init_EIGHT_BYTE_INT_UNSIGNED_ABCD(rx); break;
					case "EIGHT BYTE INT UNSIGNED (D C B A)": /* D C B A */ value = DataType.init_EIGHT_BYTE_INT_UNSIGNED_DCBA(rx); break;
					case "EIGHT BYTE INT UNSIGNED (B A D C)": /* B A D C */ value = DataType.init_EIGHT_BYTE_INT_UNSIGNED_BADC(rx); break;
					case "EIGHT BYTE INT UNSIGNED (C D A B)": /* C D A B */ value = DataType.init_EIGHT_BYTE_INT_UNSIGNED_CDAB(rx); break;
					
					case "EIGHT BYTE DOUBLE (A B C D)": /* A B C D */ value = DataType.init_EIGHT_BYTE_DOUBLE_ABCD(rx); break;
					case "EIGHT BYTE DOUBLE (D C B A)": /* D C B A */ value = DataType.init_EIGHT_BYTE_DOUBLE_DCBA(rx); break;
					case "EIGHT BYTE DOUBLE (B A D C)": /* B A D C */ value = DataType.init_EIGHT_BYTE_DOUBLE_BADC(rx); break;
					case "EIGHT BYTE DOUBLE (C D A B)": /* C D A B */ value = DataType.init_EIGHT_BYTE_DOUBLE_CDAB(rx); break;
					
					// 콤보박스에서 공백 셀을 선택 시 "TWO BYTE INT SIGNED" 로 인식
					case "" : dataTypeComboBox.setSelectedIndex(6); /* Default */ value = DataType.init_TWO_BYTE_INT_SIGNED(rx); break;
					default : break;
				}
			}
								
		
			// 테이블 레코드를 Reset
			for (int i = 0; i < tableRow; i++) {
				content[i] = new Object[4];
				content[i][0] = new Integer(i + 1); // 순 서
				content[i][1] = String.format("0x%04X", rx.getPerfInfo()[i].getRegisterAddress()); // 레지스터 주소
				content[i][2] = Integer.parseInt(String.format("%s%04d", rx.getModbusAddress(), rx.getPerfInfo()[i].getRegisterAddress() + 1)); // Address preview
								
				Object copy = value[i];				
				String scaleFunction = form_scale_textField.getText().toLowerCase();
				
				try {
					if(scaleFunction.length() != 0 && scaleFunction.contains("x")) {
						switch (dataTypeComboBox.getSelectedItem().toString()) {
							case "ASCII CODE": break;
							case "UNI CODE": break;					
							case "BINARY": break;
							case "HEX": break;			
							default : value[i] = (Object)JavaScript.eval(scaleFunction, value[i].toString()); break;
						}
					}
				}catch(Exception e) {
					value[i] = copy;
				}
				
				if(copy.toString().equalsIgnoreCase("---")) {
					value[i] = copy;
				}else if(value[i].toString().equalsIgnoreCase("true") || value[i].toString().equalsIgnoreCase("false")) {
					value[i] = copy;
				}
				
				content[i][3] = value[i]; // 값
			}
					
			if(isRTU) {
				// Modbus RTU : 테이블의 마지막 셀에 CRC 내용 추가
				content[tableRow] = new Object[4];
				content[tableRow][0] = "CRC16";
				content[tableRow][1] = "---";
				content[tableRow][2] = "---";
				content[tableRow][3] = String.format("0x%04X", rx.getCrc()& 0xffff );
			}
						
			table.setModel(new DefaultTableModel(
					content,
					new String[] {
						"index", "Register", "Modbus", rx.getCommandType()
						// 순서 , 레지스터 값
					}
			) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
		setTable(table);
	}
	
	
	public static void pushRawData() {
		
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
	
	
	public static void clearPacketLog() {		
		if(packetLog.getText().length() >= 300000) packetLog.setText(null);
	}
	
	
	public static JTable getResultTable() {
		return table;
	}
	
	
	public static void scrollUp() {
//		int pos = packetLog.getText().length();
//		packetLog.setCaretPosition(pos);
		packetLog_scrollPane.getVerticalScrollBar().setValue(packetLog_scrollPane.getVerticalScrollBar().getMaximum());		
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
	
	
	// 기능코드 내용에따라 데이터 타입 콤보박스를 표시
	public void setDataType(RX_Info rx) {
		switch(rx.getFunctionCode()) {
			case 1 : 
			case 2 : dataTypePanel.setVisible(false); break;
			case 3 :						
			case 4 : dataTypePanel.setVisible(true); break;
			case 5 :
			case 6 :
			case 15 :
			case 16 :
			default : dataTypePanel.setVisible(false); break;							
		}
	}
	
	
	public static void activationScaleFunction() {
		form_scale_label.setVisible(true);
		form_scale_textField.setVisible(true);
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
		
	
	public static void setTable(JTable table) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // 레지스터 주소
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // Address preview
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 스캔 결과
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // Address preview
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 결과
	}
		
}

