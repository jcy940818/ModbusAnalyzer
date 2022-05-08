package src_ko.swing;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

import src_ko.agent.ClientSocket;
import src_ko.agent.ModbusAgent;
import src_ko.agent.Perf;
import src_ko.info.RX_Info;
import src_ko.info.TX_Info;
import src_ko.util.ExceptionProvider;
import src_ko.util.Util;

public class ModbusMonitor_Panel extends JPanel {

	// 클라이언트 소켓
	public static Socket socket_ko = ModbusAgent.clientSocket;
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
	
	// TX Form 전송 관련 컴포넌트
	private CardLayout inputPanel_layout;
	private JTextField transactionId_text; // Modbus TCP : TransactionID 필드
	
	private JButton form_sendPacketButton;
	private static JButton form_resetButton;
	private static ButtonGroup radioGroup;
	private static JRadioButton radio_modbusTCP;
	private static JRadioButton radio_modbusRTU;
	
	// 통신 기록
	public static JScrollPane packetLog_scrollPane;
	public static JTextArea packetLog;
	public static MessageFrame packetlog_Frame;
	public TX_Info tx;
	public RX_Info rx;
	
	/**
	 * Create the panel.
	 */
	public ModbusMonitor_Panel(){	
		setBorder(new EmptyBorder(0, 0, 0, 0));
	
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(Color.DARK_GRAY);
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 608);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		actualPanel.add(infoPanel);
		
		JLabel currentFunction = new JLabel("Modbus Monitor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 255, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBounds(0, 55, 1050, 551);
		imagePanel.setLayout(new BorderLayout(0, 0));
//		infoPanel.add(imagePanel); // 테스트
		
		JLabel imageLabel = new JLabel();
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.WHITE);		
		imageLabel.setIcon(new Util().getOnionScreenResource());
		
		resultPanel = new JPanel();
		resultPanel.setBounds(10, 56, 1028, 542);
		resultPanel.setBackground(Color.LIGHT_GRAY);
		resultPanel.setLayout(null);
		infoPanel.add(resultPanel);
		
		JScrollPane resultTable_ScrollPane = new JScrollPane();
		resultTable_ScrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		resultTable_ScrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		resultTable_ScrollPane.setBackground(Color.WHITE);
		resultTable_ScrollPane.setBounds(578, 127, 438, 405);
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
					Perf.showBitStatus(table, selectedIndex, "TWO BYTE INT SIGNED");
				}
			}
		});
		resetTable(table);
		
		resultTable_ScrollPane.setViewportView(table);
		
		packetLog_scrollPane = new JScrollPane();
		packetLog_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		packetLog_scrollPane.setBounds(12, 127, 553, 405);
		resultPanel.add(packetLog_scrollPane);
		
		
		packetLog = new JTextArea();		
		packetLog.setFont(new Font("맑은 고딕", Font.PLAIN, 16));				
		packetLog_scrollPane.setViewportView(packetLog);				
		
		typePanel = new JPanel();
		typePanel.setBounds(12, 10, 141, 107);
		resultPanel.add(typePanel);
		typePanel.setBackground(Color.WHITE);
		typePanel.setLayout(null);
		
		JLabel modbusType = new JLabel("Modbus Type");
		modbusType.setForeground(Color.BLACK);
		modbusType.setHorizontalAlignment(SwingConstants.LEFT);
		modbusType.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		modbusType.setBounds(12, 10, 129, 31);
		typePanel.add(modbusType);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setSelected(true);
		radio_modbusTCP.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 43, 125, 30);
		typePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 72, 125, 30);
		typePanel.add(radio_modbusRTU);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		inputPanel_layout = new CardLayout(0, 0);		
		inputFormPanel = new JPanel();
		inputFormPanel.setBounds(165, 10, 851, 107);
		resultPanel.add(inputFormPanel);
		inputFormPanel.setBackground(Color.WHITE);
		inputFormPanel.setLayout(inputPanel_layout);
		
		JPanel form_InputPanel = new JPanel();
		form_InputPanel.setLayout(null);
		form_InputPanel.setBackground(Color.WHITE);
		inputFormPanel.add(form_InputPanel, "form_InputPanel");
		
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
		
		form_resetButton = new JButton("초기화");
		form_resetButton.setForeground(Color.BLACK);
		form_resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		form_resetButton.setBackground(Color.WHITE);
		form_resetButton.setBounds(751, 50, 88, 31);
		form_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				global_rx = null;
				packetLog.setText(null);
				resetTable(table);
								
				transactionId_text.setText("1");
				transactionId_text.setForeground(Color.BLUE);				
			}						
		});
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// TX Form 전송 버튼
		form_sendPacketButton = new JButton("\uC2DC \uC791");
		// 전송 버튼 클릭시 발생하는 이벤트
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				
				// 수집 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
				if(!checkReadRequestForm(isRTU)) return;
				
				
				try {						
					
					// 한번 초기화된 TX 내용으로 계속해서 수집
					tx = null;
					tx.setAgentType("ModbusMonitor"); // 패킷로그 에이전트 확인용
					
					
					/**
					 * Monitoring Start ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
					 */
					
					new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								rx = ModbusAgent.communicate(socket_ko, tx, isRTU, 5000);
								
								// 유효하지 않은 응답은 패스한다
								if(rx == null) return;
								if(rx.isException()) return;
								if(rx.isCRCError()) return;							
								if(rx.getScanResult() == null) return;
								if(ExceptionProvider.CompareTxRx(tx, rx) != null) return;																																																			
																																													
								// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
								global_rx = rx;
								updateTable(table, rx);
								ModbusAgent.isRTU = isRTU;
								ModbusAgent.lastFunctionCode = rx.getFunctionCode();
									
							}catch(Exception e) {
								e.printStackTrace();
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("Real-Time Monitoring Error\n"));
								sb.append(Util.colorBlue("Real-Time Monitoring") + " 기능 수행중 처리 할 수 없는 예외가 발생하였습니다" + Util.separator + "\n\n");
								sb.append(String.format("Exception Message : %s\n", e.getMessage()));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
							}
							
						}
					}).start();
					
				}catch(Exception exception) {
					resetTable(table);
					exception.printStackTrace();
				}
				
			}			
		});
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(751, 10, 88, 31);		
		form_InputPanel.add(form_sendPacketButton);
		form_InputPanel.add(form_resetButton);
		
		// 라디오 버튼(TCP/RTU)에 리스너 추가
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		radio_modbusRTU.doClick();
			
		currentState = new JLabel();		
		currentState.setOpaque(true);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(265, 5, 145, 45);
		infoPanel.add(currentState);
		
		connectButton = new JButton("연결 정보 입력");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(415, 11, 160, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {							
				
				// 클라이언트 소켓의 마지막 커넥션 정보
				/*
				String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
				
				try {
					socket_ko = ModbusAgent.clientSocket;
					src_en.swing.ModbusMonitor_Panel.socket_en = socket_ko;
					
					if( (socket_ko == null || socket_ko.isClosed()) && ClientSocket.getIsFirst()) {						
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_en.swing.ModbusMonitor_Panel.IP = IP;
						src_en.swing.ModbusMonitor_Panel.PORT = PORT;
						
					}else if(socket_ko == null) {
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_en.swing.ModbusMonitor_Panel.IP = IP;
						src_en.swing.ModbusMonitor_Panel.PORT = PORT;
					}else {
						// 기존 연결되어있는 소켓일 경우 연결을 끊고 재접속을 시도한다.						
						// 클라이언트 소켓 : 접속 끊김
						socket_ko.close();						
						ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
					}
				}catch(IOException exception) {
					return;
				}
				
				try {
					socket_ko = ClientSocket.getClientSocket(IP, PORT);
					src_en.swing.ModbusMonitor_Panel.socket_en = socket_ko;
					
				}catch(Exception exception) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='red'>접속 실패</font>\n");
					msg.append("입력하신 연결 정보를 확인해주세요" + Util.separator + "\n");					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}				
				
				if(socket_ko != null || ClientSocket.isCurrentConnected(socket_ko)) {
					// 접속 성공 : 컴포넌트 내용들을 모두 초기화한다	
					ModbusAgent.clientSocket = socket_ko;
					src_en.agent.ModbusAgent.clientSocket = socket_ko;
					
					panel_ON();
					
					// 마지막 커넥션 정보와 다른 정보로 세션을  생성시 컴포넌트 초기화
					if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
						componentAllClear();
						src_en.swing.ModbusMonitor_Panel.componentAllClear();
					}
					
					// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장					
					ClientSocket.setHasLastConnectionInfo(true);
				}
				*/
				
			}
		});
		
		infoPanel.add(connectButton);
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "번";
		}

		
		
		
		
		
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
						socket_ko = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
		
		panel_ON(); // 테스트
		
	}// end ModbusMonitor_Panel()
	
	
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
				"순 서", "Register", "Modbus", "Value"
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
		
			// 테이블 레코드를 초기화
			for (int i = 0; i < tableRow; i++) {
				content[i] = new Object[4];
				content[i][0] = new Integer(i + 1); // 순 서
				content[i][1] = String.format("0x%04X", rx.getPerfInfo()[i].getRegisterAddress()); // 레지스터 주소
				content[i][2] = Integer.parseInt(String.format("%s%04d", rx.getModbusAddress(), rx.getPerfInfo()[i].getRegisterAddress() + 1)); // 모드버스 주소
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
						"순 서", "Register", "Modbus", "Value"
						// 순서 , 레지스터 값
					}
			) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
		setTableStyle(table);
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
				
		if(!isRTU && transactionId_text.getText().length() == 0) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>입력 필드 양식 오류</font>\n");
			
			// 트랜잭션 ID null 검사
			if(!isRTU && transactionId_text.getText().length() == 0) {
				nullCount++;
				sb.append(Util.colorBlue("트랜잭션 ID"));					
			}
			
			sb.append(" 정보를 입력 해주세요" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;			
			
			return isValid;
		}
		
		// 유효하지 않은 startAddress 입력 시 메시지 출력 후 리턴
		if(!isRTU && transactionId_text.getForeground() == Color.RED) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>입력 필드 양식 오류</font>\n");
			sb.append("입력하신 ");								
			
			// 시작주소 양식 검사
			if(!isRTU && transactionId_text.getForeground() == Color.RED) {
				invalidCount++;
				sb.append(Util.colorBlue("트랜잭션 ID"));
			}
							
			sb.append(" 정보를 확인 해주세요" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;					
			
			return isValid;
		}
		
		return isValid;
	}	
	
	// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
	ActionListener radioListener = new ActionListener() {			
		@Override
		public void actionPerformed(ActionEvent e) {

			JRadioButton b = (JRadioButton)e.getSource();	
			
			// Modbus RTU, TCP 라디오 버튼 이동 시 
			global_rx = null;
			transactionId_text.setText(null);
			resetTable(table);

			if (b.getText().contains("RTU")) {
				isRTU = true;					
				transactionId_text.setVisible(false);
			} else {
				isRTU = false;					
				transactionId_text.setVisible(true);
				transactionId_text.setText("1");
				transactionId_text.setForeground(Color.BLUE);
			}								
		}						
	};
	
	
	public static void setTableStyle(JTable table) {
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
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // 모드버스 주소
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // 스캔 결과
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 모드버스 주소
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 결과
	}
		
}
