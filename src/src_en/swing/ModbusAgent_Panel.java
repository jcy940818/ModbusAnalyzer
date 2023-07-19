package src_en.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.analyzer.TX.TX_Analyzer;
import src_en.info.ONION_Info;
import src_en.info.RX_Info;
import src_en.info.TX_Info;
import src_en.util.ExceptionProvider;
import src_en.util.TX_Generator;
import src_en.util.Util;

public class ModbusAgent_Panel extends JPanel {			

	// 제어 기능 활성화 버튼
	private static JRadioButton writeCommand_radioButton;
	
	// 클라이언트 소켓
	public static Socket socket_en = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;	
	
	// information Component
	JPanel infoPanel; // 클라이언트 소켓이 서버와 연결 된 상태일때만 인포메이션 컴포넌트들을 활성화 시킨다.  
	JPanel inputFormPanel;
	JPanel typePanel;
	JPanel inputTypePanel;
	JPanel resultPanel;
	JPanel dataTypePanel;
	JPanel imagePanel; /* ONION Image */
	
	private JButton connectButton; // 연결 정보 입력버튼 (중요)
	private static boolean isRTU = false; // Default : Modbus TCP (아주 중요한 변수)
	public static JComboBox dataTypeComboBox = null;
	public static TX_Info global_tx = null;
	public static RX_Info global_rx = null;
	public static JTable table;
	private static JLabel currentState;
	private static JLabel modbusAddress_label;
	private JTextField TXinputTextField;
	
	// TX Form 전송 관련 컴포넌트
	private CardLayout inputPanel_layout;
	private JTextField transactionId_text; // Modbus TCP : TransactionID 필드
	private JComboBox unitId_comboBox; // 장비번호 콤보박스
	private JComboBox functionCode_comboBox; // 기능코드 필드
	private JTextField startAddress_text; // 시작주소, 제어주소 필드
	private JComboBox requestCount_comboBox; // 요청개수
	private JTextField controlValue; // 제어 값 필드 (FunctionCode : 06)
	private JRadioButton controlStatus_ON_Button; // 제어 상태 값 ON 필드 (FunctionCode : 05)
	private JRadioButton controlStatus_OFF_Button; // 제어 상태 값 OFF 필드 (FunctionCode : 05)	
	private JRadioButton readCommand_radioButton; 
	private JButton user_sendPacketButton;
	private JButton form_sendPacketButton;		
	private JRadioButton formInput_radioButton;
	
	// 조건식 관련 컴포넌트 (사용자 인증 후 사용 가능)
	private static JLabel user_expression_label;
	private static JLabel form_expression_label;
	private static JTextField form_expression_textField;
	private static JTextField user_expression_textField;
	
	// 초기화 버튼 : 클라이언트 소켓 접속에 성공하면 컴포넌트 내용들을 모두 초기화한다
	private static JButton user_resetButton;
	private static JButton form_resetButton;
	
	// 통신 기록
	public static JScrollPane packetLog_scrollPane;
	public static JTextArea packetLog;
	public static MessageFrame packetlog_Frame;
	public TX_Info tx;
	public RX_Info rx;
	
	private ModbusCollectionFrame modbusCollection;
	private static JButton addModbusPerf_Button;
	private KeyEvent lastKeyEvent;
	
	// Custom Modbus Slot Map
	public static HashMap<String, Integer> slotMap = null;
	
	
	/**
	 * Create the panel.
	 */
	public ModbusAgent_Panel(){	
		setBorder(new EmptyBorder(0, 0, 0, 0));
	
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(new Color(255, 140, 0));
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 606);
		actualPanel.add(infoPanel);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Modbus Client");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 218, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBounds(0, 55, 1050, 551);
		imagePanel.setLayout(new BorderLayout(0, 0));		
		infoPanel.add(imagePanel);
		
		JLabel imageLabel = new JLabel();
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.WHITE);		
		imageLabel.setIcon(new Util().getOnionScreenResource());
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		
		resultPanel = new JPanel();
		resultPanel.setBounds(10, 56, 1028, 425);
		infoPanel.add(resultPanel);
		resultPanel.setBackground(Color.LIGHT_GRAY);
		resultPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setFont(FontManager.getFont(Font.PLAIN, 13));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(578, 10, 438, 405);
		resultPanel.add(scrollPane);
		
		// 테이블 생성 부분
		table = new JTable();
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					if (ONION_Info.onionLogin && CustomModbusXmlGeneratorFrame.isExist) {
						int[] selectedIndex = ModbusAgent_Panel.table.getSelectedRows();				
						Perf[] perfs = Perf.getCustomModbusPerfs(ModbusAgent_Panel.table, selectedIndex);			 
						if(perfs == null) return;			
						CustomModbusXmlGeneratorFrame.addRecord(perfs);
					}
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int column = table.columnAtPoint(e.getPoint());
					int row = table.rowAtPoint(e.getPoint());
					table.changeSelection(row, column, false, false);
					table.requestFocus();
					int[] selectedIndex = table.getSelectedRows();
					Perf.showBitStatus(table, selectedIndex, ModbusAgent_Panel.dataTypeComboBox.getSelectedItem().toString());
				}
			}
		});
		resetTable(table);
		
		scrollPane.setViewportView(table);
		
		packetLog_scrollPane = new JScrollPane();
		packetLog_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		packetLog_scrollPane.setBounds(12, 10, 553, 405);
		resultPanel.add(packetLog_scrollPane);
		
		
		packetLog = new JTextArea();		
		packetLog.setFont(FontManager.getFont(Font.PLAIN, 16));		
		packetLog.addMouseListener(new MyMouseListener());
		packetLog_scrollPane.setViewportView(packetLog);				
		
		dataTypePanel = new JPanel();
		dataTypePanel.setBackground(Color.WHITE);
		dataTypePanel.setBounds(612, 10, 426, 39);
		infoPanel.add(dataTypePanel);
		dataTypePanel.setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("Data Type");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(FontManager.getFont(Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 89, 39);
		dataTypePanel.add(lblNewLabel);
		
		dataTypeComboBox = new JComboBox();
		dataTypeComboBox.setForeground(Color.BLACK);
		dataTypeComboBox.setBackground(Color.WHITE);
		dataTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"ASCII CODE", "UNI CODE", "", "BINARY", "HEX", "", "TWO BYTE INT SIGNED", "TWO BYTE INT UNSIGNED", "", "FOUR BYTE INT SIGNED (A B C D)", "FOUR BYTE INT SIGNED (D C B A)", "FOUR BYTE INT SIGNED (B A D C)", "FOUR BYTE INT SIGNED (C D A B)", "", "FOUR BYTE INT UNSIGNED (A B C D)", "FOUR BYTE INT UNSIGNED (D C B A)", "FOUR BYTE INT UNSIGNED (B A D C)", "FOUR BYTE INT UNSIGNED (C D A B)", "", "FOUR BYTE FLOAT (A B C D)", "FOUR BYTE FLOAT (D C B A)", "FOUR BYTE FLOAT (B A D C)", "FOUR BYTE FLOAT (C D A B)", "", "EIGHT BYTE INT SIGNED (A B C D)", "EIGHT BYTE INT SIGNED (D C B A)", "EIGHT BYTE INT SIGNED (B A D C)", "EIGHT BYTE INT SIGNED (C D A B)", "", "EIGHT BYTE INT UNSIGNED (A B C D)", "EIGHT BYTE INT UNSIGNED (D C B A)", "EIGHT BYTE INT UNSIGNED (B A D C)", "EIGHT BYTE INT UNSIGNED (C D A B)", "", "EIGHT BYTE DOUBLE (A B C D)", "EIGHT BYTE DOUBLE (D C B A)", "EIGHT BYTE DOUBLE (B A D C)", "EIGHT BYTE DOUBLE (C D A B)"}));
		dataTypeComboBox.setSelectedIndex(6);
		dataTypeComboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		dataTypeComboBox.setBounds(88, 5, 337, 30);
		dataTypeComboBox.addMouseWheelListener(SwingUtil.getPassNullComboBoxWheelListener());
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
		currentState.setFont(FontManager.getFont(Font.BOLD, 17));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(220, 8, 192, 40);
		infoPanel.add(currentState);
		
		connectButton = new JButton("Connect");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(FontManager.getFont(Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(417, 11, 100, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {												
				
				// 클라이언트 소켓의 마지막 커넥션 정보
				String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
				
				try {
					socket_en = ModbusAgent.clientSocket;
					src_ko.swing.ModbusAgent_Panel.socket_ko = socket_en;
					
					if( (socket_en == null || socket_en.isClosed()) && ClientSocket.getIsFirst()) {						
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_ko.swing.ModbusAgent_Panel.IP = IP;
						src_ko.swing.ModbusAgent_Panel.PORT = PORT;
						
					}else if(socket_en == null) {
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_ko.swing.ModbusAgent_Panel.IP = IP;
						src_ko.swing.ModbusAgent_Panel.PORT = PORT;
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
					src_ko.swing.ModbusAgent_Panel.socket_ko = socket_en;
					
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
						src_ko.swing.ModbusAgent_Panel.componentAllClear();
					}
					
					// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장					
					ClientSocket.setHasLastConnectionInfo(true);
				}
			}
		});
		
		infoPanel.add(connectButton);		
		addModbusPerf_Button = new JButton(new Util().getMK2Resource());
		addModbusPerf_Button.setFont(FontManager.getFont(Font.BOLD, 17));
		addModbusPerf_Button.setFocusPainted(false);
		addModbusPerf_Button.setContentAreaFilled(false);
		addModbusPerf_Button.setBorder(UIManager.getBorder("Button.border"));
		addModbusPerf_Button.setBackground(Color.WHITE);
		addModbusPerf_Button.setBounds(525, 11, 80, 36);					
		addModbusPerf_Button.setEnabled(false);
		addModbusPerf_Button.setVisible(false);
		addModbusPerf_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(CustomModbusXmlGeneratorFrame.instance != null) {
					CustomModbusXmlGeneratorFrame.instance.setVisible(true);
				}else {
					CustomModbusXmlGeneratorFrame.instance = new CustomModbusXmlGeneratorFrame();
					CustomModbusXmlGeneratorFrame.instance.setVisible(true);
				}
			}
		});
		infoPanel.add(addModbusPerf_Button);
		
		dataTypePanel.setVisible(false); // functionCode 3, 4 일때만 데이터 타입 콤보박스 표현 (functionCdoe 1, 2 : ON/OFF 상태이기 때문에)		
		inputFormPanel = new JPanel();
		inputFormPanel.setBackground(Color.WHITE);
		inputFormPanel.setBounds(318, 509, 744, 107);
		actualPanel.add(inputFormPanel);
		inputPanel_layout = new CardLayout(0, 0);
		inputFormPanel.setLayout(inputPanel_layout);		
						
		
		JPanel user_InputPanel = new JPanel();
		inputFormPanel.add(user_InputPanel, "user_InputPanel");
		user_InputPanel.setBackground(Color.WHITE);
		user_InputPanel.setLayout(null);
		
		JLabel typeLabel = new JLabel("Modbus TCP");
		typeLabel.setForeground(Color.BLACK);
		typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		typeLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		typeLabel.setBounds(12, 10, 129, 31);
		user_InputPanel.add(typeLabel);
		
		JLabel TX = new JLabel("TX");
		TX.setForeground(Color.BLACK);
		TX.setHorizontalAlignment(SwingConstants.LEFT);
		TX.setFont(FontManager.getFont(Font.BOLD, 16));
		TX.setBounds(12, 58, 26, 31);
		user_InputPanel.add(TX);
		
		TXinputTextField = new JTextField();
		TXinputTextField.setForeground(Color.BLACK);
		TXinputTextField.setBorder(UIManager.getBorder("TextField.border"));		
		TXinputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		TXinputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		TXinputTextField.setBounds(38, 58, 528, 31);
		TXinputTextField.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				user_sendPacketButton.doClick();
			}
		});
		TXinputTextField.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent e) {
				TXinputTextField.setBorder(UIManager.getBorder("TextField.border"));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				TXinputTextField.setBorder(new LineBorder(new Color(255, 140, 0), 3));			
			}
		});
		user_InputPanel.add(TXinputTextField);
		TXinputTextField.setColumns(10);
		
		// 초기화 버튼
		user_resetButton = new JButton("Reset");
		user_resetButton.setFocusPainted(false);
		user_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TXinputTextField.setText(null);
				user_expression_textField.setText(null);
				global_rx = null;
				dataTypePanel.setVisible(false);
				packetLog.setText(null);
				resetTable(table);		
				if(packetlog_Frame != null)packetlog_Frame.updateMessage(packetLog.getText());
			}
		});
		
		user_resetButton.setForeground(Color.BLACK);
		user_resetButton.setBackground(Color.WHITE);
		user_resetButton.setFont(FontManager.getFont(Font.BOLD, 14));
		user_resetButton.setBounds(578, 58, 77, 31);
		user_InputPanel.add(user_resetButton);
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// TX user 직접 전송 버튼
		user_sendPacketButton = new JButton("Send");
		user_sendPacketButton.setFocusPainted(false);
		// 전송 버튼 클릭시 발생하는 이벤트
		user_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Mdobus TCP, RTU Common				
				
				// 텍스트 필드에 분석 할 패킷 정보를 입력하지 않고 분석 버튼 클릭 시 내용 출력 후 리턴
				if((TXinputTextField.getText() == null)|| (TXinputTextField.getText().length() == 0)) {
					Util.showMessage("<font color='red'>There's no TX</font>\nThere's no TX input" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// 사용자에게 입력받은 TX Pure Packet (요청 패킷)
				tx = new TX_Info();
				tx.setContent(TXinputTextField.getText().replaceAll(" ", "").trim());
												
				rx = null;
								
				if(isRTU) {
					// Modbus RTU
					try {						
						tx = new TX_Analyzer().rtuAnalysis(tx);
						tx.setAgentType("ModbusAgent");
						
						// TX 직접 입력 방식에서는 제어 패킷 전송이 불가능하다 (의도하지 않은 제어를 방지하기 위해서)
						if(tx.getFunctionCode() == 0x05 || tx.getFunctionCode() == 0x06) {
							canNotControl();
							return;
						}
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);												
						
						if(rx.isCRCError()) {
							StringBuilder msg = new StringBuilder();
							msg.append("<font color='red'>RX is Incorrect CRC</font>\n");
							msg.append(String.format("%s : 0x%04x%s\n\n",Util.colorRed("Actual Read Incorrect CRC") ,rx.getCrc() & 0xffff, Util.longSeparator));
							msg.append(String.format("%s : 0x%04x%s\n",Util.colorBlue("Expected Correct CRC") ,rx.getExpectedCrc() & 0xffff, Util.longSeparator));
							Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
							resetTable(table);
							return;
						}							
						
						// TX와 RX 정보 비교 (트랜잭션ID, 장비번호, 기능코드)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX와 RX 정보 비교 후 출력 할 메시지가 있다면 출력 후 리턴
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length 검사
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();
						System.out.println("Modbus RTU Info init Error : " + exception.getMessage());						
					}
										
					if(rx.isRead()) {
						// Modbus RTU : 수집 명령어 분석
															
						// 기능코드가 3, 4 일때만 데이터 타입 선택 콤보박스를 표시
						setDataType(rx);																																																							
																																		
						// RX가 예외 응답일 경우
						if(rx.isException()) printExceptionContent(rx);						
																		
						// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();						
					}else {
						// Modbus RTU : 제어 명령어 분석
						// 해당 로직이 실행 될 일은 없겠지만 추후에 새로운 기능을 추가 할 수 있으므로 남겨놓음
						canNotControl();
						return;
					}
					
				}else {
					
					// Modbus TCP
					try {						
						tx = new TX_Analyzer().tcpAnalysis(tx);			
						tx.setAgentType("ModbusAgent");
						
						// TX 직접 입력 방식에서는 제어 패킷 전송이 불가능하다 (의도하지 않은 제어를 방지하기 위해서)
						if(tx.getFunctionCode() == 0x05 || tx.getFunctionCode() == 0x06) {
							canNotControl();
							return;
						}
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);
												
						// TX와 RX 정보 비교 (트랜잭션ID, 장비번호, 기능코드)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX와 RX 정보 비교 후 출력 할 메시지가 있다면 출력 후 리턴
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length 검사
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();
						System.out.println("Modbus RTU Info init Error : " + exception.getMessage());						
					}
										
					if(rx.isRead()) {
						// Modbus TCP : 수집 명령어 분석
												
						// 기능코드가 3, 4 일때만 데이터 타입 선택 콤보박스를 표시
						setDataType(rx);																																																			
																																		
						// RX가 예외 응답일 경우
						if(rx.isException()) printExceptionContent(rx);
																		
						// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);					
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();
					}else {
						// Modbus TCP : 제어 명령어 분석
						// 해당 로직이 실행 될 일은 없겠지만 추후에 새로운 기능을 추가 할 수 있으므로 남겨놓음
						canNotControl();
						return;						
					}					
																					
				}
				
				setExpressionTable(table);
				if(packetlog_Frame != null) packetlog_Frame.updateMessage(packetLog.getText());				
			}
						
		});
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		
		user_sendPacketButton.setForeground(Color.BLACK);
		user_sendPacketButton.setBackground(Color.WHITE);
		user_sendPacketButton.setFont(FontManager.getFont(Font.BOLD, 14));
		user_sendPacketButton.setBounds(661, 58, 71, 31);
		user_InputPanel.add(user_sendPacketButton);
		
		user_expression_label = new JLabel("Expression");
		user_expression_label.setForeground(new Color(0, 128, 0));
		user_expression_label.setBackground(Color.WHITE);
		user_expression_label.setHorizontalAlignment(SwingConstants.CENTER);
		user_expression_label.setFont(FontManager.getFont(Font.BOLD, 17));
		user_expression_label.setBounds(343, 12, 105, 28);
		user_expression_label.setVisible(false);
		user_InputPanel.add(user_expression_label);
		
		user_expression_textField = new JTextField();
		user_expression_textField.setForeground(Color.BLACK);
		user_expression_textField.setHorizontalAlignment(SwingConstants.LEFT);
		user_expression_textField.setFont(FontManager.getFont(Font.BOLD, 16));
		user_expression_textField.setBounds(450, 12, 282, 31);
		user_expression_textField.setColumns(10);
		user_expression_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExpressionTable(table);
			}
		});
		user_expression_textField.setVisible(false);
		user_InputPanel.add(user_expression_textField);
		
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
		transactionId_label.setBounds(132, 31, 26, 31);
		form_InputPanel.add(transactionId_label);
		
		transactionId_text = new JTextField();
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(FontManager.getFont(Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(164, 31, 103, 31);
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
		unitId_label.setBounds(279, 31, 77, 31);
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
		unitId_comboBox.setBounds(351, 31, 103, 32);
		unitId_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(unitId_comboBox);
		
		JLabel startAddress_label = new JLabel("Start Addr");
		startAddress_label.setForeground(Color.BLACK);
		startAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_label.setFont(FontManager.getFont(Font.BOLD, 15));
		startAddress_label.setBounds(85, 72, 77, 31);
		form_InputPanel.add(startAddress_label);
		
		startAddress_text = new JTextField();
		startAddress_text.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_text.setFont(FontManager.getFont(Font.BOLD, 15));
		startAddress_text.setColumns(10);
		startAddress_text.setBorder(UIManager.getBorder("TextField.border"));
		startAddress_text.setBounds(164, 72, 103, 31);	
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
		functionCode_label.setBounds(465, 31, 77, 31);
		form_InputPanel.add(functionCode_label);
		
		String[] reqCount125 = new String[125];
		for(int i = 0; i < 125; i++) {
			reqCount125[i] = String.valueOf(i+1);
		}
		
		String[] reqCount2000 = new String[2000];
		for(int i = 0; i < 2000; i++) {
			reqCount2000[i] = String.valueOf(i+1);
		}
		
		functionCode_comboBox = new JComboBox();
		functionCode_comboBox.setForeground(Color.BLACK);
		functionCode_comboBox.setBackground(Color.WHITE);		
		functionCode_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
		functionCode_comboBox.setSelectedIndex(2);
		functionCode_comboBox.setBounds(537, 31, 103, 32);
		functionCode_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		functionCode_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();
				
				int functionCode = Integer.parseInt(temp.getSelectedItem().toString());
				Object lastSelected = requestCount_comboBox.getSelectedItem();
				
				if(functionCode == 0x01 || functionCode == 0x02) {
					requestCount_comboBox.setModel(new DefaultComboBoxModel(reqCount2000));
					
				}else if(functionCode == 0x03 || functionCode == 0x04) {
					requestCount_comboBox.setModel(new DefaultComboBoxModel(reqCount125));
					
				}else if(functionCode == 0x05) {
					// Force Single Coil : 0x05
					controlStatus_ON_Button.setVisible(true);
					controlStatus_OFF_Button.setVisible(true);
					controlValue.setVisible(false);
					
				} else if(functionCode == 0x06){
					// Preset Single Register : 0x06
					controlStatus_ON_Button.setVisible(false);
					controlStatus_OFF_Button.setVisible(false);
					controlValue.setVisible(true);
				}
				
				requestCount_comboBox.setSelectedItem(lastSelected);
				
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
		requestCount_label.setBounds(455, 71, 77, 31);
		form_InputPanel.add(requestCount_label);
		
		requestCount_comboBox = new JComboBox();		
		requestCount_comboBox.setForeground(Color.BLACK);
		requestCount_comboBox.setModel(new DefaultComboBoxModel(reqCount125));
		requestCount_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		requestCount_comboBox.setBackground(Color.WHITE);
		requestCount_comboBox.setBounds(537, 71, 103, 32);
		requestCount_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(requestCount_comboBox);
		
		form_resetButton = new JButton("Reset");
		form_resetButton.setFocusPainted(false);
		form_resetButton.setForeground(Color.BLACK);
		form_resetButton.setFont(FontManager.getFont(Font.BOLD, 14));
		form_resetButton.setBackground(Color.WHITE);
		form_resetButton.setBounds(656, 72, 77, 31);
		form_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				global_rx = null;
				dataTypePanel.setVisible(false);
				packetLog.setText(null);
				resetTable(table);
				
				form_expression_textField.setText(null);
				/* JTextField */ transactionId_text.setText("1"); // Modbus TCP : TransactionID 필드
				transactionId_text.setForeground(Color.BLUE);
				/* JComboBox */ unitId_comboBox.setSelectedIndex(0); // 장비번호 콤보박스				
				/* JTextField */ startAddress_text.setText(null); // 시작주소, 제어주소 필드
				/* JComboBox */ requestCount_comboBox.setSelectedIndex(0); // 요청 개수				
				/* JTextField */ controlValue.setText(null); // 제어 값 필드 (FunctionCode : 06)
				/* JRadioButton */ controlStatus_ON_Button.setSelected(true); // 제어 상태 값 ON 필드 (FunctionCode : 05)												
				modbusAddress_label.setText("Address preview");
				modbusAddress_label.setForeground(Color.DARK_GRAY);
				
				if(readCommand_radioButton.isSelected()) {
					/* JComboBox */ functionCode_comboBox.setSelectedIndex(2); // 기능코드 필드	
				}else {
					/* JComboBox */ functionCode_comboBox.setSelectedIndex(1); // 기능코드 필드
				}
				if(packetlog_Frame != null)packetlog_Frame.updateMessage(packetLog.getText());
				
				// 시작주소에 포커스
				startAddress_text.requestFocus();
			}						
		});
		form_InputPanel.add(form_resetButton);
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// TX Form 전송 버튼
		form_sendPacketButton = new JButton("Send");
		form_sendPacketButton.setFocusPainted(false);
		// 전송 버튼 클릭시 발생하는 이벤트
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				// 수집, 제어 요청인지 검사
				if(readCommand_radioButton.isSelected()) {
					// 수집 요청 로직
					
					// 수집 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
					if(!checkReadRequestForm(isRTU)) return;
																					
					try {						
						tx = initReadTX(isRTU);
						tx.setAgentType("ModbusAgent");
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);
						
						if(rx.isRTU() && rx.isCRCError()) {
							StringBuilder msg = new StringBuilder();
							msg.append("<font color='red'>RX is Incorrect CRC</font>\n");
							msg.append(String.format("%s : 0x%04x%s\n\n",Util.colorRed("Actual Read Incorrect CRC") ,rx.getCrc() & 0xffff, Util.longSeparator));
							msg.append(String.format("%s : 0x%04x%s\n",Util.colorBlue("Expected Correct CRC") ,rx.getExpectedCrc() & 0xffff, Util.longSeparator));
							Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
							resetTable(table);
							return;
						}
						
						// TX와 RX 정보 비교 (트랜잭션ID, 장비번호, 기능코드)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX와 RX 정보 비교 후 출력 할 메시지가 있다면 출력 후 리턴
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length 검사
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();												
					}
										
					if(rx.isRead()) {
						// 수집 명령어 분석
															
						// 기능코드가 3, 4 일때만 데이터 타입 선택 콤보박스를 표시
						setDataType(rx);																																																				
																																		
						// RX가 예외 응답일 경우
						if(rx.isException()) printExceptionContent(rx);
																																							
						// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);			
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();
					}else {						
						resetTable(table); // 제어는 표시해줄 값이 없기 때문에 결과 테이블 내용을 초기화 한다.
						dataTypePanel.setVisible(false);											
						
						// readCommand_radioButton.isSelected() : 수집 요청
						// 수집 요청 라디오 버튼이 선택되었기 때문에 기본적으로 실행 될 수 없는 조건이다.
						// 디버깅을 위해 남겨놓는다							
					}
								
				} else {
					// 제어 요청 로직					
					// Mdobus TCP, RTU 공통 로직 : 제어 패킷 생성에 필요한 Form 정보 확인
					if(!checkControlForm()) return;				
					
					// 제어 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
					if(!checkWriteRequestForm(isRTU)) return;			
																			
					try {						
						tx = initWriteTX(isRTU);			
						tx.setAgentType("ModbusAgent");
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);
						
						// TX와 RX 정보 비교 (트랜잭션ID, 장비번호, 기능코드)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX와 RX 정보 비교 후 출력 할 메시지가 있다면 출력 후 리턴
						if(hasCompareMessage(sb)) return;
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();						
					}	
					
					// 제어 성공 여부 출력
					controlWasSuccessful(tx, rx);													
					setDataType(rx);							
				}						
				
				setExpressionTable(table);
				if(packetlog_Frame != null) packetlog_Frame.updateMessage(packetLog.getText());
			}			
		}); // end formSendPacketButton Action
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(FontManager.getFont(Font.BOLD, 14));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(656, 31, 77, 31);		
		form_InputPanel.add(form_sendPacketButton);
		
		// 수집 명령어 라디오 버튼
		readCommand_radioButton = new JRadioButton("Read");
		readCommand_radioButton.setForeground(Color.BLACK);
		readCommand_radioButton.setFocusPainted(false);
		readCommand_radioButton.setSelected(true);
		readCommand_radioButton.setBackground(Color.WHITE);
		readCommand_radioButton.setFont(FontManager.getFont(Font.BOLD, 13));
		readCommand_radioButton.setBounds(8, 48, 61, 23);
		form_InputPanel.add(readCommand_radioButton);
		
		// 제어 명령어 라디오 버튼
		writeCommand_radioButton = new JRadioButton("Write");
		writeCommand_radioButton.setForeground(Color.BLACK);
		writeCommand_radioButton.setFocusPainted(false);
		writeCommand_radioButton.setFont(FontManager.getFont(Font.BOLD, 13));
		writeCommand_radioButton.setBackground(Color.WHITE);
		writeCommand_radioButton.setBounds(8, 73, 61, 23);
		writeCommand_radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					writeCommand_radioButton.setSelected(true);		
			}
		});
		writeCommand_radioButton.setEnabled(false);
		form_InputPanel.add(writeCommand_radioButton);
		
		ButtonGroup commandTypeGroup= new ButtonGroup();
		commandTypeGroup.add(readCommand_radioButton);
		commandTypeGroup.add(writeCommand_radioButton);
		
		modbusAddress_label = new JLabel("Address preview");		
		modbusAddress_label.setBackground(Color.WHITE);
		modbusAddress_label.setForeground(Color.BLACK);
		modbusAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAddress_label.setFont(FontManager.getFont(Font.BOLD, 16));
		modbusAddress_label.setBounds(279, 72, 175, 31);
		modbusAddress_label.setOpaque(true);
		form_InputPanel.add(modbusAddress_label);
		
		controlStatus_ON_Button = new JRadioButton("ON");
		controlStatus_ON_Button.setSelected(true);
		controlStatus_ON_Button.setHorizontalAlignment(SwingConstants.CENTER);
		controlStatus_ON_Button.setFont(FontManager.getFont(Font.BOLD, 14));
		controlStatus_ON_Button.setBackground(Color.WHITE);
		controlStatus_ON_Button.setBounds(534, 72, 54, 31);
		controlStatus_ON_Button.setVisible(false);
		form_InputPanel.add(controlStatus_ON_Button);
		
		controlStatus_OFF_Button = new JRadioButton("OFF");
		controlStatus_OFF_Button.setHorizontalAlignment(SwingConstants.CENTER);
		controlStatus_OFF_Button.setFont(FontManager.getFont(Font.BOLD, 14));
		controlStatus_OFF_Button.setBackground(Color.WHITE);
		controlStatus_OFF_Button.setBounds(587, 72, 54, 31);
		controlStatus_OFF_Button.setVisible(false);
		form_InputPanel.add(controlStatus_OFF_Button);
		
		ButtonGroup controlStateGroup= new ButtonGroup();
		controlStateGroup.add(controlStatus_ON_Button);
		controlStateGroup.add(controlStatus_OFF_Button);
		
		controlValue = new JTextField();
		controlValue.setHorizontalAlignment(SwingConstants.LEFT);
		controlValue.setFont(FontManager.getFont(Font.BOLD, 15));
		controlValue.setColumns(10);
		controlValue.setBorder(UIManager.getBorder("TextField.border"));
		controlValue.setBounds(537, 72, 103, 31);	
		controlValue.setVisible(false);
		controlValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				form_sendPacketButton.doClick();				
			}
		});
		controlValue.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				try {
					int value = Integer.parseInt(controlValue.getText());
						if((value > 32767)||(value < -32768)) {
							controlValue.setForeground(Color.RED);
						}else {
							controlValue.setForeground(Color.BLUE);
						}					
					}catch(Exception exception) {
						controlValue.setForeground(Color.RED);
						return;
					}							
			}			
		});
		controlValue.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
				int value = Integer.parseInt(controlValue.getText());
					if((value > 32767)||(value < -32768)) {
						controlValue.setForeground(Color.RED);
					}else {
						controlValue.setForeground(Color.BLUE);
					}					
				}catch(Exception exception) {
					controlValue.setForeground(Color.RED);
					return;
				}			
			}			
		});
		form_InputPanel.add(controlValue);
		
		
		form_expression_label = new JLabel("Expression");
		form_expression_label.setBackground(Color.WHITE);
		form_expression_label.setFont(FontManager.getFont(Font.BOLD, 14));
		form_expression_label.setForeground(new Color(0, 128, 0));
		form_expression_label.setHorizontalAlignment(SwingConstants.CENTER);
		form_expression_label.setBounds(265, 4, 86, 23);
		form_expression_label.setVisible(false);
		form_InputPanel.add(form_expression_label);
		
		form_expression_textField = new JTextField();
		form_expression_textField.setForeground(Color.BLACK);
		form_expression_textField.setFont(FontManager.getFont(Font.BOLD, 14));
		form_expression_textField.setBackground(Color.WHITE);
		form_expression_textField.setHorizontalAlignment(SwingConstants.LEFT);		
		form_expression_textField.setBounds(351, 5, 381, 21);
		form_expression_textField.setColumns(10);
		form_expression_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExpressionTable(table);								
			}
		});
		form_expression_textField.setVisible(false);
		form_InputPanel.add(form_expression_textField);
		
		
		// TX 전송 방식 라디오 버튼 : 직접 입력 / 양식 입력
		ActionListener commandType_radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				String type = b.getText();
				
				if(type.contains("Read")) {
					functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
					functionCode_comboBox.setSelectedIndex(2); // 수집 기본 기능코드 : 03 (Read Holding Register)
					requestCount_comboBox.setSelectedIndex(0);
					startAddress_label.setText("Start Addr");
					requestCount_label.setText("Req Count");
					requestCount_comboBox.setVisible(true);
					controlStatus_ON_Button.setVisible(false);
					controlStatus_OFF_Button.setVisible(false);
					controlValue.setVisible(false);
				} else {
					functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"05", "06"}));
					functionCode_comboBox.setSelectedIndex(1); // 수집 기본 기능코드 : 06 (Preset Single Register)
					startAddress_label.setText(" Ctrl Addr");
					requestCount_label.setText(" Ctrl Value");					
					requestCount_comboBox.setVisible(false);
					controlStatus_ON_Button.setVisible(false);
					controlStatus_OFF_Button.setVisible(false);
					controlValue.setVisible(true);
				}
			}
		};
		
		readCommand_radioButton.addActionListener(commandType_radioListener);
		writeCommand_radioButton.addActionListener(commandType_radioListener);
		
		typePanel = new JPanel();
		typePanel.setBackground(Color.WHITE);
		typePanel.setBounds(165, 509, 141, 107);
		actualPanel.add(typePanel);
		typePanel.setLayout(null);
		
		JLabel modbusType = new JLabel("Modbus Type");
		modbusType.setForeground(Color.BLACK);
		modbusType.setHorizontalAlignment(SwingConstants.LEFT);
		modbusType.setFont(FontManager.getFont(Font.BOLD, 16));
		modbusType.setBounds(12, 10, 129, 31);
		typePanel.add(modbusType);
		
		JRadioButton radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setSelected(true);
		radio_modbusTCP.setFont(FontManager.getFont(Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 43, 171, 30);
		typePanel.add(radio_modbusTCP);
		
		JRadioButton radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(FontManager.getFont(Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 72, 171, 30);
		typePanel.add(radio_modbusRTU);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		inputTypePanel = new JPanel();
		inputTypePanel.setLayout(null);
		inputTypePanel.setBackground(Color.WHITE);
		inputTypePanel.setBounds(12, 509, 141, 107);
		actualPanel.add(inputTypePanel);
		
		JLabel sendType = new JLabel("TX Send Method");
		sendType.setForeground(Color.BLACK);
		sendType.setHorizontalAlignment(SwingConstants.LEFT);
		sendType.setFont(FontManager.getFont(Font.BOLD, 15));
		sendType.setBounds(8, 10, 129, 31);
		inputTypePanel.add(sendType);
		
		JRadioButton userInput_radioButton = new JRadioButton("Pure packet");
		userInput_radioButton.setForeground(Color.BLACK);
		userInput_radioButton.setSelected(true);
		userInput_radioButton.setHorizontalAlignment(SwingConstants.LEFT);
		userInput_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		userInput_radioButton.setBackground(Color.WHITE);
		userInput_radioButton.setBounds(8, 43, 171, 30);
		inputTypePanel.add(userInput_radioButton);
		
		formInput_radioButton = new JRadioButton("Use form");
		formInput_radioButton.setForeground(Color.BLACK);
		formInput_radioButton.setHorizontalAlignment(SwingConstants.LEFT);
		formInput_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		formInput_radioButton.setBackground(Color.WHITE);
		formInput_radioButton.setBounds(8, 72, 171, 30);
		inputTypePanel.add(formInput_radioButton);
		
		ButtonGroup inputType_radioGroup = new ButtonGroup();
		inputType_radioGroup.add(userInput_radioButton);
		inputType_radioGroup.add(formInput_radioButton);
		
		// TX 전송 방식 라디오 버튼 : 직접 입력 / 양식 입력
		ActionListener inputType_radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				String type = b.getText();
				
				if(type.contains("Pure")) {
					inputPanel_layout.show(inputFormPanel, "user_InputPanel");
					TXinputTextField.requestFocus();
				}else {
					inputPanel_layout.show(inputFormPanel, "form_InputPanel");
					startAddress_text.requestFocus();
				}
				
			}
		};
		
		userInput_radioButton.addActionListener(inputType_radioListener);
		formInput_radioButton.addActionListener(inputType_radioListener);
		
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel2.setText(b.getText()); // 직접 입력 판넬
				typeLabel.setText(b.getText()); // 양식 입력 판넬
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				// 데이터 타입 판넬 숨기기 , 데이터 타입 콤보박스 내용 초기화
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() 까지 호출됨				
				global_rx = null;
				modbusAddress_label.setText("Address preview");
				transactionId_text.setText(null);
				resetTable(table);
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				// 텍스트 필드 초기화
				TXinputTextField.setText(null);
//				RXinputTextField.setText(null);

				if (b.getText().contains("RTU")) {
					isRTU = true;
					transactionId_label.setVisible(false);
					transactionId_text.setVisible(false);
					startAddress_text.requestFocus();
				} else {
					isRTU = false;
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
						
						// ModbusAgent <=> ExceptionScan : Socket 동기화
						socket_en = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
				
		// 기본 패킷 전송 모드 : Modbus-RTU 양식 입력
		formInput_radioButton.doClick();
		radio_modbusRTU.doClick();
		
	}// end ModbusAgent_Panel()
	
	
	public void panel_ON() {
		// 접속 전에는 판넬 컴포넌트들을 사용하지 않는다
		typePanel.setVisible(true);
		typePanel.setEnabled(true);
		inputTypePanel.setVisible(true);
		inputTypePanel.setEnabled(true);
		inputFormPanel.setVisible(true);
		inputFormPanel.setEnabled(true);
		resultPanel.setVisible(true);
		resultPanel.setEnabled(true);				
		if(ONION_Info.onionLogin) { addModbusPerf_Button.setVisible(true); }		
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
		inputTypePanel.setVisible(false);
		inputTypePanel.setEnabled(false);
		inputFormPanel.setVisible(false);
		inputFormPanel.setEnabled(false);
		resultPanel.setVisible(false);
		resultPanel.setEnabled(false);
		if(ONION_Info.onionLogin) { addModbusPerf_Button.setVisible(false); }
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
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"index", "Register", "Modbus", "Register Value"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // 레지스터 주소
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // 모드버스 주소
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 값
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
	}
	
	
	public static void updateTable(JTable table, RX_Info rx) {
		
		slotMap = new HashMap();
		
		if((table == null)||(rx == null)||(rx.getPerfInfo() == null)) {
			return;
		}
		
		// 테이블 헤더 설정
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));

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
								
			int slotIndex = 1;
			
			// 테이블 레코드를 초기화
			for (int i = 0; i < tableRow; i++) {
				content[i] = new Object[4];
				content[i][0] = new Integer(i + 1); // 순 서
				content[i][1] = String.format("0x%04X", rx.getPerfInfo()[i].getRegisterAddress()); // 레지스터 주소
				content[i][2] = Integer.parseInt(String.format("%s%04d", rx.getModbusAddress(), rx.getPerfInfo()[i].getRegisterAddress() + 1)); // 모드버스 주소 
				content[i][3] = value[i]; // 값
				
				// Custom Modbus 전용 슬롯 : Custom Modbus XML PerfCounter 작성에 사용된다
				if(!value[i].toString().equalsIgnoreCase("---")) {
					slotMap.put(content[i][1].toString(), slotIndex++);
				}
				
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
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // 모드버스 주소
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 값
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();

		// 반복문을 이용하여 테이블을 가운데 정렬로 지정
//		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
//			tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);				
//		}					
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서		
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 모드버스 주소
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 레지스터 값
	}
	
	
	public static void setConnectionInfo() {
		String[] connectionInfo = ClientSocket.getConnectionInfo();
		IP = connectionInfo[0];
		PORT = Integer.parseInt(connectionInfo[1]);
	}
	
	
	public static void componentAllClear() {
		form_resetButton.doClick();
		user_resetButton.doClick();
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
	}
	
	
	
	// 수집 요청 패킷 생성 정보 유효성 확인
	public boolean checkReadRequestForm(boolean isRTU) {
		boolean isValid = true;				
		int nullCount = 0;
		int invalidCount = 0;
				
		if(startAddress_text.getText().length() == 0 				
			|| (!isRTU && transactionId_text.getText().length() == 0)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
			
			// 트랜잭션 ID null 검사
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
							
			sb.append(" information is missing" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;			
			
			return isValid;
		}
		
		// 유효하지 않은 startAddress 입력 시 메시지 출력 후 리턴
		if(startAddress_text.getForeground() == Color.RED 				
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
				
			sb.append(" information you entered" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;					
			
			return isValid;
		}
		
		return isValid;
	}
	
	
	
	
	// 제어 요청 패킷 생성 정보 유효성 확인
	public boolean checkWriteRequestForm(boolean isRTU) {
		boolean isValid = true;
		
		if(isRTU) {
			// Modbus RTU
			if(startAddress_text.getText().length() == 0) {				
				StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");																	
				if(startAddress_text.getText().length() == 0) sb.append(Util.colorBlue("Control Address"));
				sb.append(" information is missing" + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				isValid = false;					
			}
			
			// 유효하지 않은 startAddress 입력 시 메시지 출력 후 리턴
			if(startAddress_text.getForeground() == Color.RED) {
				StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
				sb.append("Please check the ");								
				if(startAddress_text.getForeground() == Color.RED) sb.append(Util.colorBlue("Control Address"));
				sb.append(" information you entered" + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				isValid = false;					
			}
			
		}else {			
			// Modbus TCP			
			if(transactionId_text.getText().length() == 0 || startAddress_text.getText().length() == 0) {
				StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");					
				if(transactionId_text.getText().length() == 0) sb.append(Util.colorBlue("Transaction ID"));
				if(transactionId_text.getText().length() == 0 && startAddress_text.getText().length() == 0) sb.append(", " + Util.colorBlue("Control Address"));
				if(!(transactionId_text.getText().length() == 0) && startAddress_text.getText().length() == 0) sb.append(Util.colorBlue("Control Address"));
				sb.append(" information is missing" + Util.separator + "\n");									
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				isValid = false;		
			}
			
			// 유효하지 않은 TransactionId or startAddress 입력 시 메시지 출력 후 리턴
			if(transactionId_text.getForeground() == Color.RED || startAddress_text.getForeground() == Color.RED) {
				StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
				sb.append("Please check the ");
				if(transactionId_text.getForeground() == Color.RED) sb.append(Util.colorBlue("Transaction ID"));
				if(transactionId_text.getForeground() == Color.RED && startAddress_text.getForeground() == Color.RED) sb.append(", " + Util.colorBlue("Control Address"));
				if(!(transactionId_text.getForeground() == Color.RED) && startAddress_text.getForeground() == Color.RED) sb.append(Util.colorBlue("Control Address"));
				sb.append(" information you entered" + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				isValid = false;				
			}
		}
		return isValid;		
	}
	
	
	
		
		
	// 수집 요청 TX 초기화
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
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
			// TX 초기화 중 예외발생 시 null 리턴
			return null;
		}		
	}
	
	
	
	
	// 제어 요청 TX 초기화
	public TX_Info initWriteTX(boolean isRTU) {
		TX_Info tx = null;
		int transactionId;
		int unitId;
		int functionCode;
		int startAddress;
		int controlActualValue = 0;
		
		try {
			if (isRTU) {
				// Modbus RTU
				unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
				functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
				if(startAddress_text.getText().trim().startsWith("0x")){
					startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
				}else if(startAddress_text.getText().trim().startsWith("0X")) {
					startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
				}else {
					startAddress = Integer.parseInt(startAddress_text.getText());
				}
				
				if(functionCode == 0x06) {
					// FunctionCode : 0x06 = Preset Single Register
					controlActualValue = Integer.parseInt(controlValue.getText());
				}else if(functionCode == 0x05){
					// FunctionCode : 0x05 = Force Single Coil
					controlActualValue = (controlStatus_ON_Button.isSelected())? 0xFF00 /*ON*/: 0x0000 /*OFF*/;	
				}					
				
				tx = new TX_Generator().generateWriteRTU(unitId, functionCode, startAddress, controlActualValue);
			} else {
				// Modbus TCP
				if(transactionId_text.getText().trim().startsWith("0x")){
					transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0x", ""),16);								
				}else if(transactionId_text.getText().trim().startsWith("0X")) {
					transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0X", ""),16);
				}else {
					transactionId = Integer.parseInt(transactionId_text.getText());
				}
				unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
				functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
				if(startAddress_text.getText().trim().startsWith("0x")){
					startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
				}else if(startAddress_text.getText().trim().startsWith("0X")) {
					startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
				}else {
					startAddress = Integer.parseInt(startAddress_text.getText());
				}
				
				if(functionCode == 0x06) {
					// FunctionCode : 0x06 = Preset Single Register
					controlActualValue = Integer.parseInt(controlValue.getText());
				}else if(functionCode == 0x05){
					// FunctionCode : 0x05 = Force Single Coil
					controlActualValue = (controlStatus_ON_Button.isSelected())? 0xFF00 /*ON*/: 0x0000 /*OFF*/;	
				}
				
				tx = new TX_Generator().generateWriteTCP(transactionId, 0x0000, 0x0006, unitId, functionCode, startAddress, controlActualValue);
			}			
		}catch(Exception e) {
			// 예외 처리
			return null;
		}
		
		return tx;
	}
	
	public void printException(RX_Info rx) {
		resetTable(table);
		Util.showMessage("Exception : " + rx.getExceptionContent(), JOptionPane.ERROR_MESSAGE);		
	}
	
	// 예외내용 출력
	public static void printExceptionContent(RX_Info rx) {
		resetTable(table);
		
		String msg =  null;
		switch(rx.getExceptionCode()) {
			// Illegal Function
			case 0x01 : msg = "Requested a function code that is not set on the device"; break;									
			// Illegal Data Address
			case 0x02 : msg = "Requested a Register Address that is not set on the device"; break;									
			// Illegal Data Value
			case 0x03 : msg = "No Description"; break;									
			// Slave Device Failure
			case 0x04 : msg = "The request was sent normally, but an error occurred that the device could not handle"; break;
			// Acknowledge
			case 0x05 : msg = "No Description"; break;
			// Slave Device Busy
			case 0x06 : msg = "The request was sent normally, but device is too busy"; break;
			// Negative Acknowledge
			case 0x07 : msg = "No Description"; break;
			// Memory Parity Error
			case 0x08 : msg = "No Description"; break;
			// Gateway Path Unavailable
			case 0x0a :	msg = "There's a problem with the Gateway(RCU)"; break;
			// Gateway Target Device Failed to Respond
			case 0x0b : msg = "There's a problem with the Gateway(RCU)"; break;
			default : msg = null; break;
		}
		
		StringBuilder exceptionMsg = new StringBuilder(); 
		exceptionMsg.append(String.format("<font color='red'>RX is Exception Response</font>\n"));		
		exceptionMsg.append(Util.colorBlue(String.format("Exception : %s(0x%02x)%s%s\n\n", rx.getExceptionContent(), rx.getExceptionCode(), Util.separator, Util.separator)));
		exceptionMsg.append(String.format("%s%s%s\n", msg, Util.separator, Util.separator));
		Util.showMessage(exceptionMsg.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	// 요청패킷, 응답패킷 비교 결과  출력
	public boolean hasCompareMessage(StringBuilder sb) {
		if(sb != null) {
			resetTable(table);
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	// 제어 불가능 메시지 출력
	public void canNotControl() {
		resetTable(table); // 제어는 표시해줄 값이 없기 때문에 결과 테이블 내용을 초기화 한다.
		dataTypePanel.setVisible(false);											
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Control packet transmission impossible</font>\n");
		msg.append("To prevent unintended control" + Util.separator + Util.separator + "\n\n");
		msg.append("Control packets can only be sent in a form input method" + Util.separator + Util.separator + "\n");
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);		
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
	
	
	
	// 제어 값 유효성 체크
	public boolean checkControlForm() {
		boolean isValid = true;
		
		try {
			if(Integer.parseInt(functionCode_comboBox.getSelectedItem().toString()) == 0x06){
				int value = Integer.parseInt(controlValue.getText());
				if((value > 32767)||(value < -32768)) {
					Util.showMessage("<p><font color='red'>Control Value Validation Error</font>\nPlease enter the control value with in the range"
				  + Util.separator + "\n\nControl Value Range : <span color='blue'>-32768 ~ 32767</span>\n\n" + "The control value you entered :  <span color='red'>" + value + "</span>\n", JOptionPane.ERROR_MESSAGE);
					isValid = false;
				}
			}						 
		}catch(Exception exception) {
			Util.showMessage("<font color='red'>Control Value Validation Error</font>\nControl values can only be entered with numbers"
		  + Util.separator + "\n\nThe control value you entered : <span color='red'>" + controlValue.getText() + Util.separator + "</span>\n", JOptionPane.ERROR_MESSAGE);
			isValid = false;
		}
		return isValid;
	}
	
	
	
	// 제어 패킷은 tx(요청패킷) 와 rx(응답패킷) 의 pure Packet 내용이 같으면 제어 성공이다	
	public void controlWasSuccessful(TX_Info tx, RX_Info rx) {
		resetTable(table); // 제어는 표시해줄 값이 없기 때문에 결과 테이블 내용을 초기화 한다
		dataTypePanel.setVisible(false);
		
		if(tx.getContent().equalsIgnoreCase(rx.getContent())) {
			controlSuccess();
		}else {
			controlFail();
		}
	}
	
	
	
	// 제어 성공 메시지 출력
	public void controlSuccess() {
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='blue'>Control Success</font>\n");
		msg.append("Received a control complete response from the device" + Util.longSeparator +"\n\n");
		msg.append(String.format("Controlled address : <font color='blue'>0x%04x ( %s%04d )</font>\n\n", rx.getStartAddress()&0xffff , rx.getModbusAddress(), rx.getStartAddress()+1));
		if(tx.getFunctionCode() == 0x05) {
			msg.append(String.format("Applied control status : <span color='blue'>%s</span>\n", rx.getControlStatus()));
		}else {
			msg.append(String.format("Applied control value : <span color='blue'>%d</span>\n", rx.getControlValue()));	
		}					
		Util.showMessage(msg.toString(), JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	public static void activationControl() {
		writeCommand_radioButton.setEnabled(true);
	}
	
	public static void activationMK119() {
		addModbusPerf_Button.setVisible(true);
		addModbusPerf_Button.setEnabled(true);
	}
	
	public static void activationExpression() {
		user_expression_label.setVisible(true);
		user_expression_textField.setVisible(true);
		form_expression_label.setVisible(true);
		form_expression_textField.setVisible(true);
	}
	
	
	// 제어 실패 메시지 출력
	public void controlFail() {
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Failed to Control</font>\n");
		msg.append("Have not received a response from the device for successful control" + Util.separator + Util.separator + "\n");														
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public void setExpressionTable(JTable table) {
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
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // 모드버스 주소
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 스캔 결과
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		
		// Expression Check (조건식을 만족하는 셀을 표시)
		ScanCellRenderer scanCellRenderer = null;
		String expression = null;
		if(formInput_radioButton.isSelected()) {
			expression = form_expression_textField.getText().toLowerCase();
		}else {
			expression = user_expression_textField.getText().toLowerCase();
		}
		
		if(expression == null || expression.length() == 0 || expression.equalsIgnoreCase("") || !expression.contains("x")) {
			scanCellRenderer = new ScanCellRenderer();
		}else {
			expression = expression.toLowerCase();
			expression = expression.replace("X", "x");
			expression = expression.replace("and", "&&").replace("or", "||");
			expression = expression.replace("AND", "&&").replace("OR", "||");
			
			scanCellRenderer = new ScanCellRenderer(expression);
		}
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);		
		scanCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 모드버스 주소
		tcmSchedule.getColumn(3).setCellRenderer(scanCellRenderer); // 스캔 결과
	}


	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 
			// 이벤트 발동 조건 : 마우스 오른쪽 클릭
			if (e.getButton() == 3) {
				if (packetlog_Frame != null) {
					// 기존에 생성 된 패킷로그 프레임이 존재한다면 리턴
					Util.showMessage("<font color='blue'>Modbus Client</font>\nAn existing packet log frame exists" + Util.longSeparator + "\n", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					// 기존 생성 된 패킷로그 프레임이 없다면 새로 생성
					String title = String.format("<html>Modbus Client : <font color='blue'>%s</font></html>",
							ClientSocket.getSimpleConnectedInfo());
					
					// MessageFrame.dispose() 오버라이딩
					packetlog_Frame = new MessageFrame(title, packetLog.getText()) {						
						public void dispose() {
							super.dispose();
							packetlog_Frame = null;
						}
						
					};
				}
			}			 
//			 if (e.getButton() == 1) {  } // 왼쪽 클릭
//			 if (e.getButton() == 3) { } // 오른쪽 클릭				
		}
	}
}
