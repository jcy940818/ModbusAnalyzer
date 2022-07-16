package src_ko.swing;

import java.awt.BorderLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.omg.CORBA.TIMEOUT;

import common.agent.PerfData;
import common.modbus.ModbusCellRenderer;
import common.modbus.ModbusMonitor;
import common.modbus.ModbusWatchPoint;
import common.util.JavaScript;
import common.util.TableUtil;
import src_ko.agent.ClientSocket;
import src_ko.agent.ModbusAgent;
import src_ko.util.Util;

public class ModbusMonitor_Panel extends JPanel {

	private static boolean isRTU = true;
	public static boolean modeComm = true;  
	
	// 클라이언트 소켓
	public static Socket socket_ko = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;
	
	JButton modeButton;
	
	// Modbus Point List	
	public static JComboBox resultType;
	public static JScrollPane pointList_ScrollPane;
	public static JTable pointTable;	
	public static ArrayList<ModbusWatchPoint> pointList = new ArrayList<ModbusWatchPoint>();
	
	public static String fc_formula = null;
	public static String addr_formula = null;
	public static String value_formula = null;
	
	// information Component
	JPanel infoPanel; // 클라이언트 소켓이 서버와 연결 된 상태일때만 인포메이션 컴포넌트들을 활성화 시킨다.	
	JPanel modbusTypePanel;
	JPanel form_InputPanel;
	JPanel function_Panel;
	JPanel resultPanel;
	JPanel imagePanel; /* ONION Image */
	JPanel addrTypePanel;
	
	private JButton connectButton; // 연결 정보 입력버튼 (중요)
	private JButton send_Button;
	private JButton update_button;
	private JButton monitorV1Button;
	private JButton importButton;
	private JButton exportButton;
	private static JButton reset_Button;
	private static ButtonGroup radioGroup;
	private ActionListener radioListener;
	
	private JLabel search;
	private JLabel addrType;
	private static JLabel currentState;
	private static JLabel TID;
	private static JLabel UNIT_ID;
	private static JLabel TIME_OUT;
	private static JLabel MAX_COUNT;
	
	private static JTextField search_TextField;
	private static JCheckBox useFilter;
	private static JComboBox fc_filter;
	public static JComboBox dataType_filter;
	
	// 요청 정보 컴포넌트
	public static JRadioButton radio_modbusTCP; // TCP 라디오 버튼
	public static JRadioButton radio_modbusRTU; // RTU 라디오 버튼
	public static JComboBox addrTypeComboBox; // 주소 형식 콤보박스
	public static JTextField transactionId_text; // 트랜잭션 아이디 텍스트 필드
	public static JComboBox unitID_comboBox; // 장비 번호 콤보박스
	public static JTextField timeout_text; // 타임아웃 텍스트 필드
	public static JTextField maxCount_text; // 최대 요청 개수 텍스트 필드
	
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
		infoPanel.add(imagePanel);
		
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
		
		modbusTypePanel = new JPanel();
		modbusTypePanel.setBorder(new LineBorder(Color.BLACK, 2));
		modbusTypePanel.setBounds(12, 10, 140, 74);
		modbusTypePanel.setBackground(Color.WHITE);
		modbusTypePanel.setLayout(null);
		resultPanel.add(modbusTypePanel);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setFocusPainted(false);
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusTCP.setBounds(8, 6, 125, 30);
		modbusTypePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setFocusPainted(false);
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setSelected(true);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 37, 125, 30);
		modbusTypePanel.add(radio_modbusRTU);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();

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
		addrTypePanel.setBounds(159, 10, 150, 74);
		resultPanel.add(addrTypePanel);
		
		addrType = new JLabel("Address Format");
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
		addrTypeComboBox.setSelectedIndex(0);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(8, 36, 134, 29);
		addrTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable(pointTable);
			}
		});
		addrTypePanel.add(addrTypeComboBox);
		
		form_InputPanel = new JPanel();
		form_InputPanel.setBounds(316, 10, 385, 74);
		form_InputPanel.setBorder(new LineBorder(Color.BLACK, 2));
		form_InputPanel.setLayout(null);
		form_InputPanel.setBackground(Color.WHITE);
		resultPanel.add(form_InputPanel);
		
		transactionId_text = new JTextField();
		transactionId_text.setForeground(Color.BLUE);
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(11, 34, 103, 31);
		transactionId_text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						send_Button.doClick();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
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
		function_Panel.setBounds(707, 10, 309, 74);
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
				
				if(!modeComm) return;
				
				if(ModbusMonitor.isRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s%s\n", Util.colorRed("Modbus Monitor Already in communication"), Util.separator, Util.separator));
					
					sb.append("현재 모드버스 모니터가 마지막 요청에 대한 통신을 수행중입니다");
					sb.append(Util.separator + Util.separator + "\n\n");
					
					sb.append(Util.colorBlue("현재 수행중인 통신을 중지 하시겠습니까?"));
					sb.append(Util.separator + Util.separator + "\n");
					
					int userOption= Util.showConfirm(sb.toString());
					
					// 사용자의 요청에 의해 통신이 중지
					if(userOption == JOptionPane.YES_OPTION) {
						ModbusMonitor.isRunning = false;
						return;
					}
				}
				
				// 수집 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
				if(!checkReadRequestForm(isRTU)) return;
				
				try {
					
					new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								// 현재 모니터가 통신중이라면 현재 요청은 무시
								if(ModbusMonitor.isRunning) return;
								
								int timeout = Integer.parseInt(timeout_text.getText().trim());																
								if(timeout == 0) {
									StringBuilder sb = new StringBuilder();
									sb.append(Util.colorRed("Infinite Timeout?\n"));
									sb.append(String.format("타임아웃 설정값을 " + Util.colorBlue("0ms") + " 으로 설정하면 응답 패킷을 수신하기 전까지 무한히 대기합니다%s%s%s", Util.separator, Util.separator, "\n\n"));
									sb.append(String.format("타임아웃 설정값을 무한으로 설정하고  통신하시겠습니까?%s%s%s",Util.separator, Util.separator, "\n"));
									
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
									sb.append(String.format("응답 타임아웃은 " + Util.colorBlue("0ms") + " 이상의 정수만 입력 할 수 있습니다%s%s%s", Util.separator, Util.separator, "\n"));	
									Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
									return;
								}
								
								int maxCount = Integer.parseInt(maxCount_text.getText().trim());
								if(maxCount < 0) {
									StringBuilder sb = new StringBuilder();
									sb.append(Util.colorRed("Max Request Count Error\n"));
									sb.append(String.format("최대 요청 개수는 " + Util.colorBlue("0개") + " 이상의 정수만 입력 할 수 있습니다%s%s%s", Util.separator, Util.separator, "\n"));
									Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
									return;
								}
								
								int[] selectedRows = pointTable.getSelectedRows();
								
								ArrayList<ModbusWatchPoint> pointList = getSelectedModbusPoint(pointTable);								
								
								// 인덱스를 초기화
								for(ModbusWatchPoint point : pointList) {
									point.setIndex(0);
								}
								
								ModbusMonitor monitor = new ModbusMonitor();
								monitor.setType((isRTU) ? ModbusMonitor.TYPE_RTU : ModbusMonitor.TYPE_TCP);
								monitor.setUnitID(getMonitorUnitID());
								if(monitor.getType() == ModbusMonitor.TYPE_TCP) monitor.setTransactionID(getTid());
																
								ModbusMonitor.sendRequest(socket_ko, monitor, pointList, timeout, maxCount);
								
								SwingUtilities.invokeLater(new Runnable() {
								    @Override public void run() {
								    	updateTable(pointTable);
								    	TableUtil.setFocusMultipleRows(pointTable, selectedRows);
								    }
								});
								
							}catch(Exception e) {
								e.printStackTrace();
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("Modbus Monitor Error\n"));
								sb.append(Util.colorBlue("Modbus Monitor") + " 기능 수행중 처리 할 수 없는 예외가 발생하였습니다" + Util.separator + "\n\n");
								sb.append(String.format("Exception Message : %s\n", e.getMessage()));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
							}
							
						}
					}).start();
					
				}catch(Exception exception) {				
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
		reset_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultType.setSelectedIndex(0);
				
				transactionId_text.setText("1");
				transactionId_text.setForeground(Color.BLUE);
				timeout_text.setText("5000");
				timeout_text.setForeground(Color.BLUE);
				maxCount_text.setText("125");
				maxCount_text.setForeground(Color.BLUE);
				
				addrTypeComboBox.setSelectedIndex(0);
				unitID_comboBox.setSelectedIndex(0);
				
				search_TextField.setText(null);
				fc_filter.setSelectedIndex(0);
				fc_filter.setEnabled(false);
				dataType_filter.setSelectedIndex(0);
				dataType_filter.setEnabled(false);
				useFilter.setSelected(false);
				
				pointList.clear();
				ExportModbusPointFrame.updateTable();
				doTableFilter(false);
			}
		});
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
				ArrayList<ModbusWatchPoint> selectedPointList = getSelectedModbusPoint(pointTable);
				if (selectedPointList == null || selectedPointList.size() < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s", Util.colorBlue("선택된 포인트 없음")));
					sb.append(Util.separator + Util.separator + "\n");					
					
					sb.append("테이블에서 삭제하실 모드버스 포인트를 선택 후 다시 시도해주세요");
					sb.append(Util.separator + Util.separator + "\n");
					
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				if (selectedPointList == null || selectedPointList.size() < 1) {
					return;
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s", Util.colorGreen("Delete the selected Modbus Point")));
					sb.append(Util.separator + Util.separator + "\n");
					
					sb.append(String.format("선택하신 모드버스 포인트 %s개 항목을 정말 삭제하시겠습니까?", Util.colorBlue(String.valueOf(selectedPointList.size()))));
					sb.append(Util.separator + Util.separator + "\n");
					
					int menu = Util.showConfirm(sb.toString());
					
					if(menu != JOptionPane.OK_OPTION) {
						// 모드버스 포인트 삭제 취소
						return;
					}
					
					for (ModbusWatchPoint wp : selectedPointList) {
						pointList.remove(wp);
					}

					doTableFilter(false);
					ExportModbusPointFrame.updateTable();
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
		update_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ArrayList<ModbusWatchPoint> selectedPointList = getSelectedModbusPoint(pointTable);
					if (selectedPointList == null || selectedPointList.size() < 1) {
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s", Util.colorBlue("선택된 포인트 없음")));
						sb.append(Util.separator + Util.separator + "\n");					
						
						sb.append("테이블에서 수정하실 모드버스 포인트를 선택 후 다시 시도해주세요");
						sb.append(Util.separator + Util.separator + "\n");
						
						Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if(!ModifyModbusWatchPointFrame.isExist) {
						new ModifyModbusWatchPointFrame(selectedPointList, "ModbusMonitor_Panel");
						
					 }else {
						 ModifyModbusWatchPointFrame.addPointList(selectedPointList);
						 ModifyModbusWatchPointFrame.doTableFilter();
					 }
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		function_Panel.add(update_button);
		
		
		
		TID = new JLabel("Transaction ID");
		TID.setForeground(Color.BLACK);
		TID.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		TID.setBounds(12, 11, 100, 15);
		form_InputPanel.add(TID);
		
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "번";
		}		
		
		unitID_comboBox = new JComboBox();
		unitID_comboBox.setForeground(Color.BLACK);
		unitID_comboBox.setBackground(Color.WHITE);
		unitID_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));		
		unitID_comboBox.setBounds(126, 34, 75, 30);
		unitID_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		form_InputPanel.add(unitID_comboBox);
		
		UNIT_ID = new JLabel("Unit ID");
		UNIT_ID.setForeground(Color.BLACK);
		UNIT_ID.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		UNIT_ID.setBounds(130, 11, 57, 15);
		form_InputPanel.add(UNIT_ID);
		
		TIME_OUT = new JLabel("Timeout");
		TIME_OUT.setForeground(Color.BLACK);
		TIME_OUT.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		TIME_OUT.setBounds(215, 11, 57, 15);
		form_InputPanel.add(TIME_OUT);
		
		timeout_text = new JTextField();
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setText("5000");
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(214, 34, 65, 31);
		timeout_text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						send_Button.doClick();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
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
		
		MAX_COUNT = new JLabel("Max Count");
		MAX_COUNT.setForeground(Color.BLACK);
		MAX_COUNT.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		MAX_COUNT.setBounds(290, 11, 85, 15);
		form_InputPanel.add(MAX_COUNT);
		
		maxCount_text = new JTextField();
		maxCount_text.setForeground(Color.BLUE);
		maxCount_text.setText("125");
		maxCount_text.setHorizontalAlignment(SwingConstants.LEFT);
		maxCount_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		maxCount_text.setColumns(10);
		maxCount_text.setBorder(UIManager.getBorder("TextField.border"));
		maxCount_text.setBounds(290, 34, 84, 31);
		maxCount_text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						send_Button.doClick();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		maxCount_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int maxCount = 0;
				
				if(maxCount_text.getText().startsWith("0x")||maxCount_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(maxCount_text.getText().startsWith("0x")) maxCount = Integer.parseInt(maxCount_text.getText().replaceAll("0x", ""),16); 
						if(maxCount_text.getText().startsWith("0X")) maxCount = Integer.parseInt(maxCount_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						maxCount_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						maxCount = Integer.parseInt(maxCount_text.getText());
					}catch(NumberFormatException exception) {
						maxCount_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(maxCount > 2000 || maxCount < 0) {
					maxCount_text.setForeground(Color.RED);
				}else {
					maxCount_text.setForeground(Color.BLUE);
				}
			}
		});
		maxCount_text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int maxCount = 0;
				
				if(maxCount_text.getText().startsWith("0x")||maxCount_text.getText().startsWith("0X")) {
					// 16진수 표기법 (0x0000)
					try {
						if(maxCount_text.getText().startsWith("0x")) maxCount = Integer.parseInt(maxCount_text.getText().replaceAll("0x", ""),16); 
						if(maxCount_text.getText().startsWith("0X")) maxCount = Integer.parseInt(maxCount_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						maxCount_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10진수 표기법
					try {
						maxCount = Integer.parseInt(maxCount_text.getText());
					}catch(NumberFormatException exception) {
						maxCount_text.setForeground(Color.RED);
						return;
					}
				}
				
				if(maxCount > 2000 || maxCount < 0) {
					maxCount_text.setForeground(Color.RED);
				}else {
					maxCount_text.setForeground(Color.BLUE);
				}
			}
		});
		form_InputPanel.add(maxCount_text);
		
		pointList_ScrollPane = new JScrollPane();		
		pointList_ScrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		pointList_ScrollPane.setBackground(Color.WHITE);
		pointList_ScrollPane.setBounds(0, 137, 1028, 405);
		pointList_ScrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		resultPanel.add(pointList_ScrollPane);
		
		// 테이블 생성 부분
		pointTable = new JTable();
		pointTable.setCellSelectionEnabled(true);
		pointTable.setBackground(Color.WHITE);
		pointTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) send_Button.doClick();
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					if(point != null)ModbusMonitorFrame.focusPoint(point);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				int row = pointTable.getSelectedRow();
				ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
				if(point != null)ModbusMonitorFrame.focusPoint(point);
			}
		});
		pointTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getButton() == 1) { 
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					if(point != null)ModbusMonitorFrame.focusPoint(point);
				} // 왼쪽 클릭
				
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭

					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					
					String pureData = point.getData().getPureValue().toString();
					if(!pureData.equalsIgnoreCase("none")) {
						try {
							double doubleValue = Double.parseDouble(pureData);
							long longValue = (long)doubleValue;
							ModbusWatchPoint.showBitStatus(point, longValue);
						}catch(Exception exp) {
							// Do Nothing
						}
					}
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					ModbusWatchPoint.showInfo(point);
					
				}
			}
		});
				
		resetTable(pointTable, null);
		pointList_ScrollPane.setViewportView(pointTable);
		
		search = new JLabel("검 색");
		search.setForeground(Color.BLACK);
		search.setFont(new Font("맑은 고딕", Font.BOLD, 17));
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
					doTableFilter(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent e) {
				try {
					
					boolean enter = (e.getKeyCode() == KeyEvent.VK_ENTER);
					doTableFilter(enter);					
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		search_TextField.setBounds(71, 102, 373, 32);
		resultPanel.add(search_TextField);
		
		useFilter = new JCheckBox(" 필 터");
		useFilter.setFocusPainted(false);
		useFilter.setForeground(Color.BLACK);
		useFilter.setBackground(Color.LIGHT_GRAY);
		useFilter.setHorizontalAlignment(SwingConstants.LEFT);
		useFilter.setFont(new Font("맑은 고딕", Font.BOLD, 17));
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
				
				doTableFilter(false);
			}
		});
		useFilter.setBounds(450, 105, 72, 25);
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
		fc_filter.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		fc_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter(false);
			}
		});
		fc_filter.setBounds(528, 102, 80, 32);
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
		dataType_filter.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dataType_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter(false);
			}
		});
		dataType_filter.setBounds(610, 102, 310, 32);
		resultPanel.add(dataType_filter);
		
		resultType = new JComboBox();
		resultType.setModel(new DefaultComboBoxModel(
				new String[] {
						"Point",
						"Value"
						}));
		resultType.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
				    @Override public void run() {
//				    	int[] selectedRows = pointTable.getSelectedRows();
				    	updateTable(pointTable);
//				    	setFocusMultipleRows(pointTable, selectedRows);
				    }
				});
			}
		});		
		resultType.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						send_Button.doClick();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		resultType.setForeground(Color.BLACK);
		resultType.setBackground(Color.WHITE);
		resultType.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		resultType.setBounds(922, 102, 102, 32);
		resultType.setSelectedIndex(0);
		resultPanel.add(resultType);
			
		currentState = new JLabel();
		currentState.setOpaque(true);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(250, 6, 145, 45);
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
//						componentAllClear();
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
				if(!ImportModbusPointFrame.isExist) {
					new ImportModbusPointFrame();
				 }else {
					 ImportModbusPointFrame.existsFrame();
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
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ExportModbusPointFrame.isExist) {
					new ExportModbusPointFrame();
				}else {
					ExportModbusPointFrame.existsFrame();
				}
				
			}
		});
		infoPanel.add(exportButton);
		
		monitorV1Button = new JButton("Monitor V1");
		monitorV1Button.setForeground(Color.BLACK);
		monitorV1Button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		monitorV1Button.setFocusPainted(false);
		monitorV1Button.setContentAreaFilled(false);
		monitorV1Button.setBorder(UIManager.getBorder("Button.border"));
		monitorV1Button.setBackground(Color.WHITE);
		monitorV1Button.setBounds(621, 11, 160, 36);
		monitorV1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ModbusMonitorFrame.isExist) {
					new ModbusMonitorFrame();							
				 }else {
					 ModbusMonitorFrame.existsFrame();
				 }
			}
		});
		infoPanel.add(monitorV1Button);
		
		modeButton = new JButton("작업 모드로 전환");
		modeButton.setBackground(Color.WHITE);
		modeButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		modeButton.setForeground(Color.BLACK);
		modeButton.setBounds(570, 11, 180, 36);
		modeButton.setFocusPainted(false);
		modeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(modeComm) {
					modeComm = false;
					modeButton.setText("통신 모드로 전환");
										
					panel_ON();
					resultType.setSelectedIndex(0);
					connectButton.setEnabled(false);
					monitorV1Button.setVisible(false);
					monitorV1Button.setEnabled(false);
					
				}else {
					modeComm = true;
					modeButton.setText("작업 모드로 전환");
				}
				
			}
		});
		infoPanel.add(modeButton);
	
		panel_OFF();
		
		// 클라이언트 소켓이 접속중일때만 프레임에 정보를 표시한다.
		// 스레드
		new Thread() {
			public void run() {
				String lastState = "";
				
				while (true) {					
					try {
						Thread.sleep(500);
						
						checkMode(modeComm);
						
						if(modeComm) {
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
						}
						
						// ModbusAgent <=> ExceptionScan : Socket 동기화
						socket_ko = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
				
		radio_modbusRTU.doClick();
		
		
	}// end ModbusMonitor_Panel()
	
	public void checkMode(boolean isComm) {
		if(!isComm) {
			currentState.setText("작업 모드");
			currentState.setForeground(Color.BLUE);
			
			TID.setEnabled(isComm);
			transactionId_text.setEnabled(isComm);
		}
		
		radio_modbusTCP.setEnabled(isComm);
		radio_modbusRTU.setEnabled(isComm);
		
		UNIT_ID.setEnabled(isComm);
		unitID_comboBox.setEnabled(isComm);
		
		TIME_OUT.setEnabled(isComm);
		timeout_text.setEnabled(isComm);
		
		MAX_COUNT.setEnabled(isComm);
		maxCount_text.setEnabled(isComm);
		
		monitorV1Button.setEnabled(isComm);
		monitorV1Button.setVisible(isComm);
		
		send_Button.setEnabled(isComm);
		connectButton.setEnabled(isComm);
				
		resultType.setEnabled(isComm);
	}
	
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
		monitorV1Button.setVisible(true);
		monitorV1Button.setEnabled(true);
		if(modeComm) {
			modeButton.setEnabled(false);
			modeButton.setVisible(false);	
		}
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
		monitorV1Button.setVisible(false);
		monitorV1Button.setEnabled(false);
		modeButton.setEnabled(true);
		modeButton.setVisible(true);
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle("ModbusAnalyzer");
		}
	}
	
	
	public static void resetTable(JTable table, Object[][] content){
		
		String[] header = null;
		if(resultType != null) {
			if(resultType.getSelectedIndex() == 0) {
				// Point 기준
				header = new String[] {
						"순 서",
						"모드버스 포인트",
						"기능코드",
						"주 소",
						"데이터 타입",
						"결 과"
					};
			}else {
				// Value 기준
				header = new String[] {
						"순 서",
						"모드버스 포인트",
						"기능코드",
						"주 소",
						"응답 처리 시간",
						"결 과"
					};
			}
		}else {
			header = new String[] {
					"순 서",
					"모드버스 포인트",
					"기능코드",
					"주 소",
					"데이터 타입",
					"결 과"
				};
		}
		
		table.setModel(new DefaultTableModel(content, header) {
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
		
		setTableStyle(table, fc_formula, addr_formula, value_formula);
	}
	
	public static void setConnectionInfo() {
		String[] connectionInfo = ClientSocket.getConnectionInfo();
		IP = connectionInfo[0];
		PORT = Integer.parseInt(connectionInfo[1]);
	}
	
	public static void componentAllClear() {
		reset_Button.doClick();		
	}
	
	public static JTable getViewTable() {
		return pointTable;
	}
	
	public int getTid() {
		try {
			int transactionId = 0;
			
			if(transactionId_text.getText().trim().startsWith("0x")){
				transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0x", ""),16);
			}else if(transactionId_text.getText().trim().startsWith("0X")) {
				transactionId = Integer.parseInt(transactionId_text.getText().trim().replaceAll("0X", ""),16);
			}else {
				transactionId = Integer.parseInt(transactionId_text.getText());
			}
			
			return transactionId;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	// 수집 요청 패킷 생성 정보 유효성 확인
	public boolean checkReadRequestForm(boolean isRTU) {
		boolean isValid = true;				
		int nullCount = 0;
		int invalidCount = 0;
				
		if(!isRTU && transactionId_text.getText().length() == 0
			|| timeout_text.getText().length() == 0
			|| maxCount_text.getText().length() == 0) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>입력 필드 양식 오류</font>\n");
			
			// 트랜잭션 ID null 검사
			if(!isRTU && transactionId_text.getText().length() == 0) {
				nullCount++;
				sb.append(Util.colorBlue("트랜잭션 ID"));					
			}
			
			// 타임아웃 null 검사
			if(timeout_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", 타임아웃"));
				else						
					sb.append(Util.colorBlue("타임아웃"));
				
				nullCount++;
			}
			
			// 최대 요청 개수 null 검사
			if(maxCount_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", 최대 요청 개수"));
				else						
					sb.append(Util.colorBlue("최대 요청 개수"));
				
				nullCount++;
			}
			
			sb.append(" 정보를 입력 해주세요" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;			
			
			return isValid;
		}
		
		// 유효하지 않은 startAddress 입력 시 메시지 출력 후 리턴
		if(!isRTU && transactionId_text.getForeground() == Color.RED
				|| timeout_text.getForeground() == Color.RED
				|| maxCount_text.getForeground() == Color.RED) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>입력 필드 양식 오류</font>\n");
			sb.append("입력하신 ");								
			
			// 시작주소 양식 검사
			if(!isRTU && transactionId_text.getForeground() == Color.RED) {
				invalidCount++;
				sb.append(Util.colorBlue("트랜잭션 ID"));
			}
			
			// 타임아웃 양식 검사
			if(timeout_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", 타임아웃"));
				else
					sb.append(Util.colorBlue("타임아웃"));
				
				invalidCount++;
			}
			
			// 최대 요청 개수 양식 검사
			if(maxCount_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", 최대 요청 개수"));
				else
					sb.append(Util.colorBlue("최대 요청 개수"));
				
				invalidCount++;
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
			boolean isPoint = resultType.getSelectedIndex() == 0;
			
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
				
				if(isPoint) {
					// Point 기준
					/* column[4] */ record.add(modbusWp.getDataType()); // 데이터 타입
					/* column[5] */ record.add(PerfData.getPerfContent(modbusWp, modbusWp.getData())); // 결 과
				}else {
					// Value 기준
					/* column[4] */ record.add(modbusWp.getData().getTimeString()); // 응답 처리 시간
					/* column[5] */ record.add(PerfData.getPerfPureValue(modbusWp.getData())); // 결 과
				}
				
				model.addRow(record);
			}
			
			if(pointList != null) {
				int total = pointList.size();
				int searched = table.getRowCount();
				String text = String.format("모드버스 포인트  ( %d / %d )", searched, total);
				TableUtil.setTableHeader(table, 1, text);
			}else {
				TableUtil.setTableHeader(table, 1, "모드버스 포인트");
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void setTableStyle(JTable table, String fc, String addr, String value) {
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setForeground(Color.BLACK);		
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
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
		table.getColumnModel().getColumn(2).setPreferredWidth(50); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(50); // 주 소		
		
		if(resultType != null) {
			if(resultType.getSelectedIndex() == 0) {
				table.getColumnModel().getColumn(4).setPreferredWidth(250); // 데이터 타입
				table.getColumnModel().getColumn(5).setPreferredWidth(100); // 결 과	
			}else {
				table.getColumnModel().getColumn(4).setPreferredWidth(150); // 데이터 타입
				table.getColumnModel().getColumn(5).setPreferredWidth(200); // 결 과
			}
		}else {
			table.getColumnModel().getColumn(4).setPreferredWidth(250); // 데이터 타입
			table.getColumnModel().getColumn(5).setPreferredWidth(100); // 결 과	
		}
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		// 기능 코드
		DefaultTableCellRenderer fcCellRenderer = null;
		if(fc == null || fc.length() == 0 || fc.equalsIgnoreCase("") || !fc.contains("x")) {
			fcCellRenderer = new DefaultTableCellRenderer();
		}else {			
			fcCellRenderer = new ModbusCellRenderer(fc, "fc");
		}
		fcCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 주소
		DefaultTableCellRenderer addrCellRenderer = null;
		if(addr == null || addr.length() == 0 || addr.equalsIgnoreCase("") || !addr.contains("x")) {
			addrCellRenderer = new DefaultTableCellRenderer();
		}else {			
			addrCellRenderer = new ModbusCellRenderer(addr, "addr");
		}
		addrCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 값
		DefaultTableCellRenderer valueCellRenderer = null;
		if(value == null || value.length() == 0 || value.equalsIgnoreCase("") || !value.contains("x")) {
			valueCellRenderer = new DefaultTableCellRenderer();
		}else {			
			valueCellRenderer = new ModbusCellRenderer(value, "value");
		}
		valueCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 모드버스 포인트
		tcmSchedule.getColumn(2).setCellRenderer(fcCellRenderer); // 기능코드
		tcmSchedule.getColumn(3).setCellRenderer(addrCellRenderer); // 주 소
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 데이터 타입
		tcmSchedule.getColumn(5).setCellRenderer(valueCellRenderer); // 결 과
		
		if(pointList != null) {
			int total = pointList.size();
			int searched = table.getRowCount();
			String text = String.format("모드버스 포인트  ( %d / %d )", searched, total);
			TableUtil.setTableHeader(table, 1, text);
		}else {
			TableUtil.setTableHeader(table, 1, "모드버스 포인트");
		}
	}
	
	public static void updateTable(JTable table) {
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		
		Object[][] content = new Object[rowCount][];
		
		boolean isPoint = resultType.getSelectedIndex() == 0;
		
		for(int i = 0; i < rowCount; i++) {
			content[i] = new Object[columnCount];
			
			ModbusWatchPoint point = (ModbusWatchPoint)table.getValueAt(i, 1);
			Object addr = null;
			switch(addrTypeComboBox.getSelectedItem().toString()) {
				case "Modbus (DEC)" :
					addr = point.getModbusAddrString();
					break;
				case "Register (DEC)" :
					addr = point.getRegisterAddr();
					break;
				case "Register (HEX)" :
					addr = point.getRegisterAddrHexString();
					break;
				default : 
					addr = point.getModbusAddrString();
					break;
			}
			
			content[i][0] = table.getValueAt(i, 0);
			content[i][1] = point;
			content[i][2] = point.getFunctionCode();
			content[i][3] = addr;
			content[i][4] = (isPoint) ? point.getDataType() : point.getData().getTimeString();
			content[i][5] = (isPoint) ? PerfData.getPerfContent(point, point.getData()) : PerfData.getPerfPureValue(point.getData());
		}
		
		resetTable(table, content);
	}	
	
	public static void doTableFilter(boolean clickedEnter) {
		if(search_TextField == null && useFilter == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		boolean showAll = false;
		
		boolean fc_Formula = false;
		boolean addr_Formula = false;
		boolean value_Formula = false;
		
		boolean fc_only = false;
		boolean addr_only = false;
		boolean value_only = false;
		
		if(noSearch && !useFilter.isSelected()) {
			fc_formula = null;
			addr_formula = null;
			value_formula = null;
			resetTable(pointTable, null);
			addRecord(pointTable, pointList);
			return;
		}
		if(!noSearch) {
			text = text.toLowerCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toLowerCase();
				
				if(text.contains(",")) {
					// ********************** 검색어에 콤마(,) 포함 *****************************************************************
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						
						// 포인트 이름
						if(searchElement.contains(token)) {
							isContain = true;
						}
						
						// 기능코드
						if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("fc") && token.contains("only")) {
							fc_Formula = true;
							fc_only = true;
							fc_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.replace("fc", "x")
									.trim();
							
						}else if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("fc")) {
							fc_Formula = true;
							fc_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.replace("fc", "x")
									.trim();
						}
						
						// 주소
						if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("addr") && token.contains("only")) {
							addr_Formula = true;
							addr_only = true;
							addr_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.replace("addr", "x")
									.trim();
							
						}else if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("addr")) {
							addr_Formula = true;
							addr_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.replace("addr", "x")
									.trim();
						}
						
						// 값
						if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("x") && token.contains("only")) {
							value_Formula = true;
							value_only = true;
							value_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.trim();
							
						}else if(clickedEnter && token.startsWith("[") && token.endsWith("]") && token.contains("x")) {
							value_Formula = true;
							value_formula = token
									.replace("[", "")
									.replace("]", "")
									.replace("only", "")
									.replace("*", "")
									.replace("all", "")
									.trim();
						}
						
						if(token.startsWith("[") && token.endsWith("]") && (token.contains("all") || token.contains("*"))) {
							showAll = true;
						}
					}
					
					if(fc_Formula && fc_only) {
						onlyFcFormulaPoint(fc_formula);
						return;
						
					}else if(addr_Formula && addr_only) {
						onlyAddrFormulaPoint(addr_formula); 
						return;
						
					}else if(value_Formula && value_only) {
						onlyValueFormulaPoint(value_formula);
						return;
					}
					
				}else{
					// ********************** 검색어에 콤마(,) 미포함 *****************************************************************
					
					// 포인트 이름
					if (searchElement.contains(text)) {
						isContain = true;
					}
					
					// 기능코드
					if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("fc") && text.contains("only")) {
						fc_Formula = true;
						fc_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.replace("fc", "x")
								.trim();
						onlyFcFormulaPoint(fc_formula);
						return;
						
					}else if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("fc")) {
						fc_Formula = true;
						fc_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.replace("fc", "x")
								.trim();
						
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
						setTableStyle(pointTable, fc_formula, addr_formula, value_formula);
						return;
					}
					
					// 주소
					if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("addr") && text.contains("only")) {
						addr_Formula = true;
						addr_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.replace("addr", "x")
								.trim();
						onlyAddrFormulaPoint(addr_formula);
						return;
						
					}else if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("addr")) {
						addr_Formula = true;
						addr_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.replace("addr", "x")
								.trim();
						
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
						setTableStyle(pointTable, fc_formula, addr_formula, value_formula);
						return;
					}
					
					// 값
					if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("x") && text.contains("only")) {
						value_Formula = true;
						value_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.trim();
						onlyValueFormulaPoint(value_formula);
						return;
						
					}else if(clickedEnter && text.startsWith("[") && text.endsWith("]") && text.contains("x")) {
						value_Formula = true;
						value_formula = text
								.replace("[", "")
								.replace("]", "")
								.replace("only", "")
								.replace("*", "")
								.replace("all", "")
								.trim();
						
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
						setTableStyle(pointTable, fc_formula, addr_formula, value_formula);
						return;
					}
					
					if(text.startsWith("[") && text.endsWith("]") && (text.contains("all") || text.contains("*"))) {
						showAll = true;
					}
				}
				// ****************************************************************************************************************
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
					String dataType = dataType_filter.getSelectedItem().toString().toLowerCase().trim();
					if(modbusWp.getDataType().toLowerCase().trim().equalsIgnoreCase(dataType)) {
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

		if(!fc_Formula) fc_formula = null;
		if(!addr_Formula) addr_formula = null;
		if(!value_Formula) value_formula = null;
		resetTable(pointTable, null);
		addRecord(pointTable, (showAll) ? pointList : filterList);
	}
	
	public static void onlyFcFormulaPoint(String formula) {
    	ArrayList<ModbusWatchPoint> findPointList = new ArrayList<ModbusWatchPoint>();
		
		for(ModbusWatchPoint p : pointList) {
			String fc = String.valueOf(p.getFunctionCode());
			try {
				boolean validFormula =  (boolean)JavaScript.eval(formula, fc);
				if(validFormula) findPointList.add(p);
			}catch(Exception exp) {
				// Do Nothing
			}
		}
		
		resetTable(pointTable, null);
		addRecord(pointTable, findPointList);
	}
	
	public static void onlyAddrFormulaPoint(String formula) {
    	ArrayList<ModbusWatchPoint> findPointList = new ArrayList<ModbusWatchPoint>();
		
		for(ModbusWatchPoint p : pointList) {
			String addr = null;
			
			switch(addrTypeComboBox.getSelectedItem().toString()) {
				case "Modbus (DEC)" :
					addr = p.getModbusAddrString();
					break;
				case "Register (DEC)" :
					addr = String.valueOf(p.getRegisterAddr());
					break;
				case "Register (HEX)" :
					addr = p.getRegisterAddrHexString();
					break;
				default : 
					addr = String.valueOf(p.getRegisterAddr());
					break;
			}
			
			try {
				boolean validFormula =  (boolean)JavaScript.eval(formula, addr);
				if(validFormula) findPointList.add(p);
			}catch(Exception exp) {
				// Do Nothing
			}
		}
		
		resetTable(pointTable, null);
		addRecord(pointTable, findPointList);
	}
	
	public static void onlyValueFormulaPoint(String formula) {
    	ArrayList<ModbusWatchPoint> findPointList = new ArrayList<ModbusWatchPoint>();
		
		for(ModbusWatchPoint p : pointList) {
			String pureData = p.getData().getPureValue().toString();
			if(!pureData.equalsIgnoreCase("none")) {
				if(resultType.getSelectedIndex() == 0) {
					pureData = String.valueOf(p.getComputedValue(Double.parseDouble(pureData)));
				}
				try {
					boolean validFormula =  (boolean)JavaScript.eval(formula, pureData);
					if(validFormula) findPointList.add(p);
				}catch(Exception exp) {
					// Do Nothing
				}
			}
		}
		
		resetTable(pointTable, null);
		addRecord(pointTable, findPointList);
	}
	
	public int getMonitorUnitID() {
		return Integer.parseInt(unitID_comboBox.getSelectedItem().toString().replace("번", "").trim());
	}
	
	public static void addPointList(ArrayList<ModbusWatchPoint> list) {
		for(ModbusWatchPoint wp : list) {
			pointList.add(wp);
		}
		Collections.sort(pointList);
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
