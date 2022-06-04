package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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

import common.agent.PerfData;
import common.modbus.ModbusWatchPoint;
import src_ko.agent.ClientSocket;
import src_ko.agent.ModbusAgent;
import src_ko.info.RX_Info;
import src_ko.info.TX_Info;
import src_ko.util.ExceptionProvider;
import src_ko.util.Util;

public class ModbusMonitor_Panel extends JPanel {

	// 클라이언트 소켓
	public static Socket socket_ko = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;
	
	// Modbus Point List
	private static JTable point_table;
	private static ArrayList<ModbusWatchPoint> pointList = new ArrayList<ModbusWatchPoint>();	
	
	// information Component
	JPanel infoPanel; // 클라이언트 소켓이 서버와 연결 된 상태일때만 인포메이션 컴포넌트들을 활성화 시킨다.
	JPanel viewTypePanel;
	JPanel modbusTypePanel;
	JPanel form_InputPanel;
	JPanel function_Panel;
	JPanel viewPanel;	
	JPanel resultPanel;
	JPanel imagePanel; /* ONION Image */
	
	private static JComboBox unitID_comboBox;
	private JButton connectButton; // 연결 정보 입력버튼 (중요)
	private static boolean isRTU = true; // Default : Modbus TCP (아주 중요한 변수)
	private static RX_Info global_rx = null;
	private static JLabel currentState;
	private static JLabel TID;
	private static JLabel UNIT_ID;
	private static JTextField transactionId_text; // Modbus TCP : TransactionID 필드
	
	private static CardLayout cardLayout;
	private JButton send_Button;
	private static JButton reset_Button;
	private static ButtonGroup radioGroup;
	private static ButtonGroup radioGroup2;
	private static JRadioButton radio_pointList;
	private static JRadioButton radio_packetLog;	
	private static JRadioButton radio_modbusTCP;
	private static JRadioButton radio_modbusRTU;
	
	
	// 통신 기록
	public static JScrollPane packetLog_ScrollPane;
	public static JScrollPane pointList_ScrollPane;
	public static JTextArea packetLog;
	public static MessageFrame packetlog_Frame;
	public TX_Info tx;
	public RX_Info rx;
	
	private JPanel addrTypePanel;
	private JButton importButton;
	private JButton exportButton;
	private JLabel search;
	private JLabel addrType;
	private static JTextField search_TextField;
	private static JCheckBox useFilter;
	private static JComboBox fc_filter;
	private static JComboBox dataType_filter;
	private static JComboBox addrTypeComboBox;
	private ActionListener radioListener;
	private JButton update_button;
	
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
		infoPanel.setBounds(12, 10, 1050, 606);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		actualPanel.add(infoPanel);
		
		JLabel currentFunction = new JLabel("Modbus Monitor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 243, 55);
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
		
		viewTypePanel = new JPanel();
		viewTypePanel.setBorder(new LineBorder(Color.BLACK, 2));
		viewTypePanel.setBackground(Color.WHITE);
		viewTypePanel.setBounds(12, 10, 140, 72);
		viewTypePanel.setLayout(null);
		resultPanel.add(viewTypePanel);
		
		radio_pointList = new JRadioButton("Point List");
		radio_pointList.setBounds(8, 6, 125, 30);
		radio_pointList.setSelected(true);
		radio_pointList.setHorizontalAlignment(SwingConstants.LEFT);
		radio_pointList.setForeground(Color.BLACK);
		radio_pointList.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_pointList.setBackground(Color.WHITE);
		radio_pointList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(viewPanel, radio_pointList.getText());
			}
		});
		viewTypePanel.add(radio_pointList);
		
		radio_packetLog = new JRadioButton("Packet Log");
		radio_packetLog.setBounds(8, 35, 125, 30);
		radio_packetLog.setHorizontalAlignment(SwingConstants.LEFT);
		radio_packetLog.setForeground(Color.BLACK);
		radio_packetLog.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_packetLog.setBackground(Color.WHITE);
		radio_packetLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(viewPanel, radio_packetLog.getText());
			}
		});
		viewTypePanel.add(radio_packetLog);
		
		radioGroup2 = new ButtonGroup();		
		radioGroup2.add(radio_pointList);
		radioGroup2.add(radio_packetLog);
		
		modbusTypePanel = new JPanel();
		modbusTypePanel.setBorder(new LineBorder(Color.BLACK, 2));
		modbusTypePanel.setBounds(160, 10, 140, 72);
		modbusTypePanel.setBackground(Color.WHITE);
		modbusTypePanel.setLayout(null);
		resultPanel.add(modbusTypePanel);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 6, 125, 30);
		modbusTypePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setSelected(true);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 35, 125, 30);
		modbusTypePanel.add(radio_modbusRTU);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();	
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				global_rx = null;			

				if (b.getText().contains("RTU")) {
					isRTU = true;
					TID.setEnabled(false);
					transactionId_text.setEnabled(false);
				} else {
					isRTU = false;			
					TID.setEnabled(true);
					transactionId_text.setEnabled(true);
					transactionId_text.setText("1");
					transactionId_text.setForeground(Color.BLUE);
				}								
			}						
		};
		
		// 라디오 버튼(TCP/RTU)에 리스너 추가
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		addrTypePanel = new JPanel();
		addrTypePanel.setBorder(new LineBorder(Color.BLACK, 2));		
		addrTypePanel.setBackground(Color.WHITE);
		addrTypePanel.setLayout(null);
		addrTypePanel.setBounds(308, 10, 150, 72);
		resultPanel.add(addrTypePanel);
		
		addrType = new JLabel("Address Type");
		addrType.setHorizontalAlignment(SwingConstants.LEFT);
		addrType.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrType.setBackground(Color.WHITE);
		addrType.setForeground(Color.BLACK);
		addrType.setBounds(10, 8, 126, 20);
		addrTypePanel.add(addrType);
		
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(2);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(8, 36, 134, 29);
		addrTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doTableFilter();
			}
		});
		addrTypePanel.add(addrTypeComboBox);
		
		form_InputPanel = new JPanel();
		form_InputPanel.setBounds(465, 10, 234, 72);
		form_InputPanel.setBorder(new LineBorder(Color.BLACK, 2));
		form_InputPanel.setLayout(null);
		form_InputPanel.setBackground(Color.WHITE);
		resultPanel.add(form_InputPanel);
		
		transactionId_text = new JTextField();
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(10, 34, 103, 31);
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
		
		function_Panel = new JPanel();
		function_Panel.setBackground(Color.WHITE);
		function_Panel.setBounds(707, 10, 309, 72);
		function_Panel.setBorder(new LineBorder(Color.BLACK, 2));
		function_Panel.setLayout(null);
		resultPanel.add(function_Panel);
		
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// TX Form 전송 버튼
		send_Button = new JButton("전 송");
		send_Button.setFocusPainted(false);
		send_Button.setMargin(new Insets(2, 0, 2, 0));
		send_Button.setBounds(232, 8, 70, 27);
		function_Panel.add(send_Button);
		
		// 전송 버튼 클릭시 발생하는 이벤트
		send_Button.addActionListener(new ActionListener() {
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
								updateTable(point_table, rx);
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
					resetTable(point_table);
					exception.printStackTrace();
				}
				
			}			
		});
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		send_Button.setForeground(Color.BLUE);
		send_Button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		send_Button.setBackground(Color.WHITE);
		
		reset_Button = new JButton("초기화");
		reset_Button.setFocusPainted(false);
		reset_Button.setMargin(new Insets(2, 0, 2, 0));
		reset_Button.setBounds(232, 40, 70, 27);
		reset_Button.setForeground(Color.RED);
		reset_Button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		reset_Button.setBackground(Color.WHITE);
		function_Panel.add(reset_Button);
		
		JButton add_button = new JButton("추 가");
		add_button.setFocusPainted(false);
		add_button.setMargin(new Insets(2, 0, 2, 0));
		add_button.setForeground(Color.BLACK);
		add_button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		add_button.setBackground(Color.WHITE);
		add_button.setBounds(8, 20, 70, 39);
		add_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!AddFormModbusPointFrame.isExist) {
					new AddFormModbusPointFrame();					
				 }else {
					 AddFormModbusPointFrame.existsFrame();
				 }
			}
		});
		function_Panel.add(add_button);
		
		JButton delete_button = new JButton("삭 제");
		delete_button.setFocusPainted(false);
		delete_button.setMargin(new Insets(2, 0, 2, 0));
		delete_button.setForeground(Color.BLACK);
		delete_button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		delete_button.setBackground(Color.WHITE);
		delete_button.setBounds(82, 20, 70, 39);
		delete_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ModbusWatchPoint> selectedPointList = getSelectedModbusPoint(point_table);

				if (selectedPointList == null || selectedPointList.size() < 1) {
					return;
				} else {

					for (ModbusWatchPoint wp : selectedPointList) {
						pointList.remove(wp);
					}

					doTableFilter();
				}
			}
		});
		function_Panel.add(delete_button);
		
		update_button = new JButton("수 정");
		update_button.setFocusPainted(false);
		update_button.setMargin(new Insets(2, 0, 2, 0));
		update_button.setForeground(Color.BLACK);
		update_button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		update_button.setBackground(Color.WHITE);
		update_button.setBounds(156, 20, 70, 39);
		function_Panel.add(update_button);
		reset_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				global_rx = null;
				
				transactionId_text.setText("1");
				transactionId_text.setForeground(Color.BLUE);
				
				radio_pointList.doClick();
				packetLog.setText(null);
				radio_modbusRTU.doClick();
				addrTypeComboBox.setSelectedIndex(0);
				unitID_comboBox.setSelectedIndex(0);
				
				search_TextField.setText(null);
				fc_filter.setSelectedIndex(0);
				fc_filter.setEnabled(false);
				dataType_filter.setSelectedIndex(0);
				dataType_filter.setEnabled(false);
				useFilter.setSelected(false);
				
				pointList.clear();
				resetTable(point_table);
			}
		});
		
		TID = new JLabel("Transaction ID");
		TID.setForeground(Color.BLACK);
		TID.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		TID.setBounds(11, 9, 100, 15);
		form_InputPanel.add(TID);
		
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "번";
		}		
		
		unitID_comboBox = new JComboBox();
		unitID_comboBox.setForeground(Color.BLACK);
		unitID_comboBox.setBackground(Color.WHITE);
		unitID_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));		
		unitID_comboBox.setBounds(133, 34, 90, 30);
		unitID_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		form_InputPanel.add(unitID_comboBox);
		
		UNIT_ID = new JLabel("Unit ID");
		UNIT_ID.setForeground(Color.BLACK);
		UNIT_ID.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		UNIT_ID.setBounds(134, 9, 57, 15);
		form_InputPanel.add(UNIT_ID);
		
		pointList_ScrollPane = new JScrollPane();		
		pointList_ScrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		pointList_ScrollPane.setBackground(Color.WHITE);
		pointList_ScrollPane.setBounds(578, 360, 438, 172);
		
		// 테이블 생성 부분
		point_table = new JTable();
		point_table.setBackground(Color.WHITE);		
		point_table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					int row = point_table.getSelectedRow();
					ModbusWatchPoint wp = (ModbusWatchPoint) point_table.getValueAt(row, 1);
					
					ModbusWatchPoint clone = wp.getClone();
					if(clone != null) {
						pointList.add(clone);
						doTableFilter();
					}
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					
					int row = point_table.getSelectedRow();
					ModbusWatchPoint wp = (ModbusWatchPoint) point_table.getValueAt(row, 1);
					ModbusWatchPoint.showInfo(wp);
					
					/* 비트구조 확인
					int column = pointListTable.columnAtPoint(e.getPoint());
					int row = pointListTable.rowAtPoint(e.getPoint());
					pointListTable.changeSelection(row, column, false, false);
					pointListTable.requestFocus();
					int[] selectedIndex = pointListTable.getSelectedRows();
					Perf.showBitStatus(pointListTable, selectedIndex, "TWO BYTE INT SIGNED");
					*/
				}
			}
		});
				
		resetTable(point_table);
		
		pointList_ScrollPane.setViewportView(point_table);
		
		packetLog_ScrollPane = new JScrollPane();		
		packetLog_ScrollPane.setBounds(12, 360, 553, 172);
		
		packetLog = new JTextArea();		
		packetLog.setFont(new Font("맑은 고딕", Font.PLAIN, 16));				
		packetLog_ScrollPane.setViewportView(packetLog);				
		
		cardLayout = new CardLayout(0, 0);
		viewPanel = new JPanel();
		viewPanel.setBorder(new LineBorder(Color.BLACK, 2));
		viewPanel.setBackground(Color.WHITE);
		viewPanel.setBounds(0, 135, 1028, 407);
		viewPanel.setLayout(cardLayout);
		viewPanel.add(pointList_ScrollPane, radio_pointList.getText());
		viewPanel.add(packetLog_ScrollPane, radio_packetLog.getText());
		resultPanel.add(viewPanel);
		
		search = new JLabel("검 색");
		search.setForeground(Color.BLACK);
		search.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		search.setBackground(Color.LIGHT_GRAY);
		search.setBounds(15, 105, 57, 25);
		resultPanel.add(search);
		
		search_TextField = new JTextField();
		search_TextField.setColumns(10);
		search_TextField.setForeground(Color.BLACK);
		search_TextField.setBackground(Color.WHITE);
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setFont(new Font("맑은 고딕", Font.PLAIN, 17));
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		search_TextField.setBounds(71, 102, 374, 28);
		resultPanel.add(search_TextField);
		
		useFilter = new JCheckBox(" 필 터");
		useFilter.setFocusPainted(false);
		useFilter.setForeground(Color.BLACK);
		useFilter.setBackground(Color.LIGHT_GRAY);
		useFilter.setHorizontalAlignment(SwingConstants.LEFT);
		useFilter.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		useFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(useFilter.isSelected()) {
					fc_filter.setEnabled(true);
					dataType_filter.setEnabled(true);
				}else {
					fc_filter.setEnabled(false);
					dataType_filter.setEnabled(false);
				}
				
				doTableFilter();
			}
		});
		useFilter.setBounds(465, 105, 78, 25);
		resultPanel.add(useFilter);
		
		fc_filter = new JComboBox();
		fc_filter.setEnabled(false);
		fc_filter.setForeground(Color.BLACK);
		fc_filter.setBackground(Color.WHITE);
		fc_filter.setModel(new DefaultComboBoxModel(
				new String[] {
						"ALL", 						
						"FC 01", 
						"FC 02", 
						"FC 03", 
						"FC 04"
						}));
		fc_filter.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter();
			}
		});
		fc_filter.setBounds(548, 102, 92, 28);
		resultPanel.add(fc_filter);
		
		dataType_filter = new JComboBox();
		dataType_filter.setForeground(Color.BLACK);
		dataType_filter.setBackground(Color.WHITE);
		dataType_filter.setEnabled(false);
		dataType_filter.setMaximumRowCount(20);
		dataType_filter.setModel(new DefaultComboBoxModel(
				new String[] {
						"ALL",
						"",
						"BINARY",
						"",
						"TWO BYTE INT SIGNED", 
						"TWO BYTE INT UNSIGNED",
						"",						
						"FOUR BYTE INT SIGNED", 
						"FOUR BYTE INT UNSIGNED",
						"FOUR BYTE INT SIGNED SWAPPED",
						"FOUR BYTE INT UNSIGNED SWAPPED",
						"",
						"FOUR BYTE FLOAT",
						"FOUR BYTE FLOAT SWAPPED",
						"",
						"EIGHT BYTE INT SIGNED",
						"EIGHT BYTE FLOAT"
						}));
		dataType_filter.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter();
			}
		});
		dataType_filter.setBounds(646, 102, 370, 28);
		resultPanel.add(dataType_filter);
			
		currentState = new JLabel();
		currentState.setOpaque(true);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(250, 5, 145, 45);
		infoPanel.add(currentState);
		
		connectButton = new JButton("연결 정보 입력");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(400, 11, 160, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 클라이언트 소켓의 마지막 커넥션 정보
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
//						src_en.swing.ModbusMonitor_Panel.componentAllClear(); 영문버전 추가시 주석 해제
					}
					
					// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장					
					ClientSocket.setHasLastConnectionInfo(true);
				}
				
				
			}
		});
		
		infoPanel.add(connectButton);
		
		importButton = new JButton("↓ Import");
		importButton.setHorizontalAlignment(SwingConstants.LEFT);
		importButton.setFocusPainted(false);
		importButton.setForeground(new Color(0, 128, 0));
		importButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		importButton.setFocusPainted(false);
		importButton.setContentAreaFilled(false);
		importButton.setBorder(UIManager.getBorder("Button.border"));
		importButton.setBackground(Color.WHITE);
		importButton.setBounds(790, 11, 120, 36);
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!AddModbusPointFrame.isExist) {
					new AddModbusPointFrame();					
				 }else {
					 AddModbusPointFrame.existsFrame();
				 }
			}
		});
		infoPanel.add(importButton);
		
		exportButton = new JButton("↑ Export");
		exportButton.setHorizontalAlignment(SwingConstants.LEFT);
		exportButton.setFocusPainted(false);
		exportButton.setForeground(new Color(0, 128, 0));
		exportButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		exportButton.setFocusPainted(false);
		exportButton.setContentAreaFilled(false);
		exportButton.setBorder(UIManager.getBorder("Button.border"));
		exportButton.setBackground(Color.WHITE);
		exportButton.setBounds(918, 11, 120, 36);
		infoPanel.add(exportButton);
	
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
		radio_modbusRTU.doClick();
		
	}// end ModbusMonitor_Panel()
	
	
	public void panel_ON() {
		// 접속 전에는 판넬 컴포넌트들을 사용하지 않는다
		modbusTypePanel.setVisible(true);
		modbusTypePanel.setEnabled(true);		
		resultPanel.setVisible(true);
		resultPanel.setEnabled(true);			
		form_InputPanel.setVisible(true);
		form_InputPanel.setEnabled(true);
		imagePanel.setVisible(false);
		imagePanel.setEnabled(false);
		importButton.setVisible(true);
		importButton.setEnabled(true);
		exportButton.setVisible(true);
		exportButton.setEnabled(true);
		
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle(String.format("ModbusAnalyzer : %s", ClientSocket.getSimpleConnectedInfo()));
		}
	}
	
	
	public void panel_OFF() {
		// 접속 전에는 판넬 컴포넌트들을 사용하지 않는다
		modbusTypePanel.setVisible(false);
		modbusTypePanel.setEnabled(false);		
		resultPanel.setVisible(false);
		resultPanel.setEnabled(false);
		form_InputPanel.setVisible(false);
		form_InputPanel.setEnabled(false);
		imagePanel.setVisible(true);
		imagePanel.setEnabled(true);		
		importButton.setVisible(false);
		importButton.setEnabled(false);
		exportButton.setVisible(false);
		exportButton.setEnabled(false);
		
		if(packetlog_Frame != null) {
			packetlog_Frame.dispose();
			packetlog_Frame = null;
		}
		
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle("ModbusAnalyzer");
		}
	}
	
	
	public static void resetTable(JTable table){
		
		table.setModel(new DefaultTableModel(
				new Object[][] {
					
				},
				new String[] {
						"순 서",
						"모드버스 포인트",
						"기능코드",
						"주 소",
						"데이터 타입",
						"결 과"
					}) {
				boolean[] columnEditables = new boolean[] {
						false, // 순 서 : 수정 불가
						false, // 모드버스 포인트 : 수정 불가
						false, // 기능코드 : 수정 불가
						false, // 주 소 : 수정 불가
						false, // 데이터 타입 : 수정 불가
						false, // 결 과 : 수정 불가
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(table);
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
		reset_Button.doClick();		
	}
	
	public static JTextArea getPacketLog() {
		return packetLog;
	}
	
	public static JTable getViewTable() {
		return point_table;
	}
	
	public static void scrollUp() {
		packetLog_ScrollPane.getVerticalScrollBar().setValue(packetLog_ScrollPane.getVerticalScrollBar().getMaximum());		
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
	
	
	
	
	
	
	
	
	/**
	 * 	레코드 추가
	 */
	public static void addRecord(JTable table, ArrayList<ModbusWatchPoint> modbusWps) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			// 기능코드, 주소, 보정식 순서로 정렬
			Collections.sort(modbusWps);
			
			for(int i = 0; i < modbusWps.size(); i++) {
				
				ModbusWatchPoint modbusWp = modbusWps.get(i);
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// 테이블의 행 개수가 0개 일 경우 : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // 순 서
				/* column[1] */ record.add(modbusWp); // 모드버스 포인트
				/* column[2] */ record.add(modbusWp.getFunctionCode());  // 기능코드
				
				Object addr = null;
				switch(addrTypeComboBox.getSelectedItem().toString()) {
					case "Modbus (DEC)" :
						addr = modbusWp.getModbusAddrString();
						break;
					case "Register (DEC)" :
						addr = modbusWp.getRegisterAddr();
						break;
					case "Register (HEX)" :
						addr = modbusWp.getRegisterAddrHexString();
						break;
					default : 
						addr = modbusWp.getModbusAddrString();
						break;
				}
				
				/* column[3] */ record.add(addr);  // 주소
				/* column[4] */ record.add(modbusWp.getDataType()); // 데이터 타입
				/* column[5] */ record.add(PerfData.getPerfLastContent(modbusWp, modbusWp.getData())); // 결 과
				
				model.addRow(record);
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void setTableStyle(JTable table) {
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(50); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(350); // 모드버스 포인트
		table.getColumnModel().getColumn(2).setPreferredWidth(65); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(80); // 주 소
		table.getColumnModel().getColumn(4).setPreferredWidth(250); // 데이터 타입
		table.getColumnModel().getColumn(5).setPreferredWidth(100); // 결 과
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 모드버스 포인트
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 기능코드
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 주 소
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 데이터 타입
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 결 과
	}
	
	public static void doTableFilter() {
		if(search_TextField == null && useFilter == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		if(noSearch && !useFilter.isSelected()) {
			resetTable(point_table);
			addRecord(point_table, pointList);
			return;
		}
		
		if(!noSearch) {
			text = text.toUpperCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toUpperCase();
				
				if(text.contains(",")) {
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						if(searchElement.contains(token)) {
							isContain = true;
						}
					}
				}else if(searchElement.contains(text)) {
					isContain = true;
				}
			}else {
				isContain = true;
			}
			
			if(useFilter.isSelected()) {
				boolean fcPass = false;
				boolean dataTypePass = false;
				
				if( !(fc_filter.getSelectedItem().toString().equalsIgnoreCase("") || fc_filter.getSelectedItem().toString().equalsIgnoreCase("ALL")) ) { 
					int fc = Integer.parseInt(fc_filter.getSelectedItem().toString().split(" ")[1].trim());
					if(modbusWp.getFunctionCode() == fc) {
						fcPass = true;
					}
				}else {
					// ALL
					fcPass = true;
				}
				
				if( !(dataType_filter.getSelectedItem().toString().equalsIgnoreCase("") || dataType_filter.getSelectedItem().toString().equalsIgnoreCase("ALL")) ) { 
					String dataType = dataType_filter.getSelectedItem().toString().toUpperCase().trim();
					if(modbusWp.getDataType().toUpperCase().trim().equalsIgnoreCase(dataType)) {
						dataTypePass = true;
					}
				}else {
					// ALL
					dataTypePass = true;
				}
				
				isContain = isContain && fcPass && dataTypePass;
			}
			
			if(isContain) {
				filterList.add(modbusWp);
			}
			
		}// for loop

		resetTable(point_table);
		addRecord(point_table, filterList);
	}
	
	public static void setPointList(ArrayList<ModbusWatchPoint> list) {
		pointList = list;
	}
	
	public static void addPointList(ArrayList<ModbusWatchPoint> list) {
		for(ModbusWatchPoint wp : list) {
			pointList.add(wp);
		}
		Collections.sort(pointList);
	}
	
	public static ArrayList<ModbusWatchPoint> getPointList(){
		return pointList;
	}
	
	public static ArrayList<ModbusWatchPoint> getSelectedModbusPoint(JTable table) {
		ArrayList<ModbusWatchPoint> selectedPointlist = new ArrayList<ModbusWatchPoint>();
		int[] selectedIndex = table.getSelectedRows();
		for(int i = 0; i < selectedIndex.length; i++) {
			ModbusWatchPoint wp = (ModbusWatchPoint) table.getValueAt(selectedIndex[i], 1);
			selectedPointlist.add(wp);
		}
		return selectedPointlist;
	}
}
