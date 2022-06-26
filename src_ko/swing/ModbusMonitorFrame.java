package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.agent.PerfData;
import common.modbus.ModbusCellRenderer;
import common.modbus.ModbusMonitor;
import common.modbus.ModbusWatchPoint;
import common.util.JavaScript;
import common.util.TableUtil;
import src_ko.agent.ClientSocket;
import src_ko.agent.ModbusAgent;
import src_ko.util.Timer;
import src_ko.util.Util;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

public class ModbusMonitorFrame extends JFrame {

	public static ArrayList<ModbusWatchPoint> pointList;
	public static JTable pointTable;	
	
	public static boolean isExist = false;
	
	private JPanel contentPane;
	private JPanel actualPanel;
	
	// 클라이언트 소켓
	public static Socket socket_ko = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;
	
	public static JScrollPane log_scrollPane;
	private JScrollPane table_scrollPane;
	public static JTextArea log;	
	private int fontSize = 18;
	
	private JButton connectButton;
	
	// 요청 정보 컴포넌트
	public static JRadioButton radio_modbusTCP; // TCP 라디오 버튼
	public static JRadioButton radio_modbusRTU; // RTU 라디오 버튼
	public static JComboBox addrTypeComboBox; // 주소 형식 콤보박스
	public static JTextField transactionId_text; // 트랜잭션 아이디 텍스트 필드
	public static JComboBox unitID_comboBox; // 장비 번호 콤보박스
	public static JTextField timeout_text; // 타임아웃 텍스트 필드
	public static JTextField maxCount_text; // 최대 요청 개수 텍스트 필드
	
	// 모드버스 모니터 폼 컴포넌트
	private JComboBox fc_comboBox; // 기능코드 : 콤보박스
	private JTextField startAddr_textField; // 시작주소 : 텍스트 필드
	private JButton method_Button; // 전송방법 : 버튼
	private JTextField method_textField; // 전송방법 : 텍스트 필드
	private JComboBox dataType_comboBox; // 데이터타입 : 콤보박스
	private JTextField fontSize_text; // 글자크기 : 텍스트 필드
	private JButton sendButton; // 전송 버튼 : 버튼
	private JButton resetButton; // 리셋 버튼 : 버튼
		
	// 요청 정보 레이블
	private JLabel addrFormat_label;
	private JLabel transactionID_label;
	private JLabel unitID_label;
	private JLabel timeout_label;
	private JLabel maxCount_label;
	
	// 모드버스 모니터 레이블
	private JPanel reqFormPanel;
	private JLabel fc_label;
	private JLabel startAddr_label;
	private JLabel dataType_label;
	private JLabel range_label;
	private JLabel fontSize_label;
	private JLabel currentState;
	
	private String lastAddrFormat = "Register (DEC)";
	private ActionListener radioListener;	
	private ButtonGroup radioGroup = null;
	private Rectangle r = new Rectangle(100, 100, 1080, 720);
	
	private JPanel cardPanel;
	private static CardLayout cardLayout;
	public static JTextField search_textField;
	
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ModbusMonitorFrame frame = new ModbusMonitorFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ModbusMonitorFrame() {
		ModbusMonitorFrame.isExist = true;		
		setTitle("Modbus Monitor");
		setMinimumSize(new Dimension(r.width, r.height));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new Util().getIconResource().getImage());
		setResizable(true);
				
		setBounds(100, 100, 1080, 720);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY, 10));
		contentPane.setLayout(new BorderLayout(0, 0));		
		setContentPane(contentPane);
		
		radioListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();

				if (b.getText().contains("RTU")) {					
					transactionID_label.setEnabled(false);
					transactionId_text.setEnabled(false);
				} else {
					transactionID_label.setEnabled(true);
					transactionId_text.setEnabled(true);
					transactionId_text.setText("1");
					transactionId_text.setForeground(Color.BLUE);
				}
			}
		};
		
		radioGroup = new ButtonGroup();
		
		transactionId_text = new JTextField();
		
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "번";
		}
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
    			addComponentListener(new ComponentAdapter() {
    				@Override
    				public void componentResized(ComponentEvent e) {
    					log_scrollPane.setSize(contentPane.getWidth() - (log_scrollPane.getX() + 20), contentPane.getHeight() - (log_scrollPane.getY() + 20));
    					table_scrollPane.setSize(table_scrollPane.getWidth(), contentPane.getHeight() - (table_scrollPane.getY() + 20));
    					reqFormPanel.setSize(contentPane.getWidth() - (reqFormPanel.getX() + 20), reqFormPanel.getHeight());
    					super.componentResized(e);
    				}
    			});
            }
        });
		
		cardPanel = new JPanel();
		cardLayout = new CardLayout(0, 0);
		cardPanel.setLayout(cardLayout);	
		contentPane.add(cardPanel, BorderLayout.CENTER);
		
		actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setLayout(null);
		actualPanel.setBackground(Color.WHITE);
		cardPanel.add(actualPanel, "actualPanel");
		
		Image_Panel image_panel = new Image_Panel();
		image_panel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				connect();
			}
		});
		cardPanel.add(image_panel, "image");
		cardLayout.show(cardPanel, "image");
		
		JLabel currentFunction = new JLabel("Modbus Monitor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 267, 55);
		actualPanel.add(currentFunction);
		
		log_scrollPane = new JScrollPane();
		log_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		log_scrollPane.setBounds(435, 154, 609, 507);
		actualPanel.add(log_scrollPane);
		
		log = new JTextArea();
		log.setBorder(new EmptyBorder(5, 5, 0, 0));
		log.setForeground(Color.BLACK);
		log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
		log_scrollPane.setViewportView(log);
		
		table_scrollPane = new JScrollPane();
		table_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		table_scrollPane.setBounds(0, 184, 437, 477);
		actualPanel.add(table_scrollPane);
		
		pointTable = new JTable();
		pointTable.setCellSelectionEnabled(true);
		pointTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				int row = pointTable.getSelectedRow();
				String index = pointTable.getValueAt(row, 0).toString().trim();						
				
				String findIndex = String.format("%s.", index);
				int textLength = findIndex.length();
				
				int start = log.getText().indexOf(findIndex);
				int end = start + textLength;
				
				log.getCaret().setSelectionVisible(true);
				log.select(start, end);
			}
						
			public void keyReleased(KeyEvent e) {
				int row = pointTable.getSelectedRow();
				String index = pointTable.getValueAt(row, 0).toString().trim();					
				
				String findIndex = String.format("%s.", index);
				int textLength = findIndex.length();
				
				int start = log.getText().indexOf(findIndex);
				int end = start + textLength;
				
				log.getCaret().setSelectionVisible(true);
				log.select(start, end);
			}
		});
		pointTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					
					int column = pointTable.columnAtPoint(e.getPoint());
					int row = pointTable.rowAtPoint(e.getPoint());
					pointTable.changeSelection(row, column, false, false);
					pointTable.requestFocus();
					int[] selectedIndex = pointTable.getSelectedRows();
					
					String index = pointTable.getValueAt(selectedIndex[0], 0).toString().trim();
					
					String findIndex = String.format("%s.", index);
					int textLength = findIndex.length();
					
					int start = log.getText().indexOf(findIndex);
					int end = start + textLength;
					
					log.getCaret().setSelectionVisible(true);
					log.select(start, end);
					
				} // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
										
					if(pointList == null || pointList.size() < 1) return;
					int row = pointTable.getSelectedRow();
					int index = Integer.parseInt(pointTable.getValueAt(row, 0).toString());
					ModbusWatchPoint point = pointList.get(index-1);
					
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
					
					if(pointList == null || pointList.size() < 1) return;
					int row = pointTable.getSelectedRow();
					int index = Integer.parseInt(pointTable.getValueAt(row, 0).toString());
					ModbusWatchPoint point = pointList.get(index-1);
					ModbusWatchPoint.showInfo(point);
					
				}
			}
		});
		resetTable(pointTable, null);
		table_scrollPane.setViewportView(pointTable);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setFocusPainted(false);
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setFont(new Font("맑은 고딕", Font.BOLD, 16));		
		radio_modbusTCP.setBounds(262, 11, 135, 23);
		radio_modbusTCP.addActionListener(radioListener);
		actualPanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setFocusPainted(false);
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setSelected(true);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("맑은 고딕", Font.BOLD, 16));		
		radio_modbusRTU.setBounds(262, 45, 135, 23);		
		radio_modbusRTU.addActionListener(radioListener);
		actualPanel.add(radio_modbusRTU);
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		
		addrFormat_label = new JLabel("Address Format");
		addrFormat_label.setBackground(Color.WHITE);
		addrFormat_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addrFormat_label.setForeground(Color.BLACK);
		addrFormat_label.setBounds(406, 10, 150, 24);
		actualPanel.add(addrFormat_label);
		
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(1);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(405, 40, 150, 30);
		addrTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				syncAddr();
				
				getAddress(startAddr_textField);
				getMethodValue();
				
				lastAddrFormat = addrTypeComboBox.getSelectedItem().toString();
				
				updateTable(pointTable);
			}
		});
		actualPanel.add(addrTypeComboBox);
		
		transactionID_label = new JLabel("Transaction ID");
		transactionID_label.setBackground(Color.WHITE);
		transactionID_label.setForeground(Color.BLACK);
		transactionID_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		transactionID_label.setBounds(576, 10, 120, 24);
		transactionID_label.setEnabled(false);		
		actualPanel.add(transactionID_label);
		
		transactionId_text = new JTextField();
		transactionId_text.setForeground(Color.BLUE);
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(575, 40, 120, 30);
		transactionId_text.setEnabled(false);
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
		actualPanel.add(transactionId_text);
		
		unitID_label = new JLabel("Unit ID");
		unitID_label.setForeground(Color.BLACK);
		unitID_label.setBackground(Color.WHITE);
		unitID_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		unitID_label.setBounds(717, 10, 90, 24);
		actualPanel.add(unitID_label);
		
		unitID_comboBox = new JComboBox();
		unitID_comboBox.setForeground(Color.BLACK);
		unitID_comboBox.setBackground(Color.WHITE);
		unitID_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));				
		unitID_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		unitID_comboBox.setBounds(716, 40, 90, 30);
		actualPanel.add(unitID_comboBox);
		
		timeout_text = new JTextField();
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setText("5000");
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(825, 40, 90, 30);
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
		actualPanel.add(timeout_text);
		
		timeout_label = new JLabel("Timeout");
		timeout_label.setForeground(Color.BLACK);
		timeout_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		timeout_label.setBackground(Color.WHITE);
		timeout_label.setBounds(826, 10, 90, 24);
		actualPanel.add(timeout_label);
		
		maxCount_label = new JLabel("Max Count");
		maxCount_label.setForeground(Color.BLACK);
		maxCount_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		maxCount_label.setBackground(Color.WHITE);
		maxCount_label.setBounds(934, 10, 100, 24);
		actualPanel.add(maxCount_label);
		
		maxCount_text = new JTextField();
		maxCount_text.setForeground(Color.BLUE);
		maxCount_text.setText("125");
		maxCount_text.setHorizontalAlignment(SwingConstants.LEFT);
		maxCount_text.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		maxCount_text.setColumns(10);
		maxCount_text.setBorder(UIManager.getBorder("TextField.border"));
		maxCount_text.setBounds(933, 40, 100, 30);
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
		actualPanel.add(maxCount_text);
		
		reqFormPanel = new JPanel();
		reqFormPanel.setBorder(new LineBorder(Color.BLACK, 2));
		reqFormPanel.setBackground(Color.LIGHT_GRAY);
		reqFormPanel.setBounds(0, 76, 1044, 80);
		reqFormPanel.setLayout(null);
		actualPanel.add(reqFormPanel);
		
		fc_label = new JLabel("Function Code");
		fc_label.setHorizontalAlignment(SwingConstants.LEFT);
		fc_label.setForeground(Color.BLACK);
		fc_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc_label.setBackground(Color.WHITE);
		fc_label.setBounds(15, 10, 124, 24);
		reqFormPanel.add(fc_label);
		
		fc_comboBox = new JComboBox();
		fc_comboBox.setBounds(14, 43, 125, 30);
		fc_comboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"FC 01",
						"FC 02", 
						"FC 03",
						"FC 04"
						}));
		fc_comboBox.setSelectedIndex(2);
		fc_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				syncAddr();
				getAddress(startAddr_textField);
				getMethodValue();
				
				int fc = Integer.parseInt(fc_comboBox.getSelectedItem().toString().split(" ")[1]);
				
				if(fc >= 3) {
					dataType_comboBox.setModel(new DefaultComboBoxModel(
							new String[] {
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
				}else {
					dataType_comboBox.setModel(new DefaultComboBoxModel(
							new String[] {
									"BINARY"
									}));
				}
								
				dataType_comboBox.setSelectedIndex(0);
			}
		});
		fc_comboBox.setForeground(Color.BLACK);
		fc_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc_comboBox.setBackground(Color.WHITE);
		reqFormPanel.add(fc_comboBox);
		
		startAddr_label = new JLabel("Start Address");
		startAddr_label.setHorizontalAlignment(SwingConstants.LEFT);
		startAddr_label.setForeground(Color.BLACK);
		startAddr_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		startAddr_label.setBackground(Color.WHITE);
		startAddr_label.setBounds(157, 10, 117, 24);
		reqFormPanel.add(startAddr_label);
		
		startAddr_textField = new JTextField();
		startAddr_textField.setText("0");
		startAddr_textField.setHorizontalAlignment(SwingConstants.LEFT);
		startAddr_textField.setForeground(Color.BLUE);
		startAddr_textField.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		startAddr_textField.setColumns(10);
		startAddr_textField.setBorder(UIManager.getBorder("TextField.border"));
		startAddr_textField.setBounds(156, 43, 120, 30);
		startAddr_textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});
		startAddr_textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				getAddress(startAddr_textField);
				getMethodValue();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				getAddress(startAddr_textField);
				getMethodValue();
			}
			
		});
		reqFormPanel.add(startAddr_textField);
		
		range_label = new JLabel("~");
		range_label.setHorizontalAlignment(SwingConstants.CENTER);
		range_label.setForeground(Color.BLACK);
		range_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		range_label.setBackground(Color.WHITE);
		range_label.setBounds(267, 45, 40, 24);
		range_label.setEnabled(false);
		range_label.setVisible(false);
		reqFormPanel.add(range_label);
		
		method_Button = new JButton("Req Count");
		method_Button.setMargin(new Insets(2, 0, 2, 0));
		method_Button.setFocusPainted(false);
		method_Button.setForeground(Color.BLACK);
		method_Button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		method_Button.setBackground(Color.LIGHT_GRAY);
		method_Button.setBounds(300, 8, 120, 30);
		method_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dataType = dataType_comboBox.getSelectedItem().toString().toUpperCase().trim();
				int step = 1;
				
				if(dataType.startsWith("BIN") || dataType.startsWith("TWO")) {
					step  = 1;			
				}else if(dataType.startsWith("FOUR")) {
					step = 2;
				}else if(dataType.startsWith("EIGHT")) {
					step = 4;
				}
				
				if(method_Button.getText().equals("Req Count")) {
					range_label.setEnabled(true);
					range_label.setVisible(true);
					
					int startAddr = getAddress(startAddr_textField);
					int reqCount = getMethodValue();
					
					if(reqCount != -1) {
						reqCount *= step;
						int endAddress = (startAddr + reqCount) - 1;
						endAddress -= (step - 1); // 테스트
						String endAddrString = getIntegerAddress(endAddress, "Register (DEC)");
						method_textField.setText(endAddrString);
					}else {
						method_textField.setText("Invalid");
						method_textField.setForeground(Color.RED);
					}
					method_Button.setText("End Address");
					
				}else {
					range_label.setEnabled(false);
					range_label.setVisible(false);
					
					int reqCount = getMethodValue();
					if(reqCount != -1) {
						reqCount /= step;
						if(step > 1) reqCount++; // 테스트
						method_textField.setText(String.valueOf(reqCount));
					}else {
						method_textField.setText("Invalid");
						method_textField.setForeground(Color.RED);
					}
					
					method_Button.setText("Req Count");
				}
				
				syncAddr();
				getMethodValue();
				method_textField.requestFocus();
			}
		});
		reqFormPanel.add(method_Button);
		
		method_textField = new JTextField();
		method_textField.setText("1");
		method_textField.setHorizontalAlignment(SwingConstants.LEFT);
		method_textField.setForeground(Color.BLUE);
		method_textField.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		method_textField.setColumns(10);
		method_textField.setBorder(UIManager.getBorder("TextField.border"));
		method_textField.setBounds(300, 43, 120, 30);
		method_textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});
		method_textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				getMethodValue();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				getMethodValue();
			}
			
		});
		reqFormPanel.add(method_textField);
		
		dataType_label = new JLabel("Data Type");
		dataType_label.setHorizontalAlignment(SwingConstants.LEFT);
		dataType_label.setForeground(Color.BLACK);
		dataType_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType_label.setBackground(Color.WHITE);
		dataType_label.setBounds(436, 10, 117, 24);
		reqFormPanel.add(dataType_label);
		
		dataType_comboBox = new JComboBox();
		dataType_comboBox.setMaximumRowCount(30);
		dataType_comboBox.setModel(new DefaultComboBoxModel(
				new String[] {
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
		dataType_comboBox.setSelectedIndex(0);
		dataType_comboBox.setForeground(Color.BLACK);
		dataType_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		dataType_comboBox.setBackground(Color.WHITE);
		dataType_comboBox.setBounds(434, 43, 382, 30);
		reqFormPanel.add(dataType_comboBox);
		
		sendButton = new JButton("전 송");
		sendButton.setForeground(Color.BLUE);
		sendButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		sendButton.setFocusPainted(false);
		sendButton.setBackground(Color.WHITE);
		sendButton.setBounds(828, 43, 100, 30);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean isRTU = radio_modbusRTU.isSelected();
				
				// 수집 요청 TX 생성에 필요한 Form 에 정보가 모두 입력되어 있는지 체크
				if(!checkReadRequestForm(isRTU)) return;
				
				try {
					if(checkFormValidation()) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									// 현재 모니터가 통신중이라면 현재 요청은 무시
									if(ModbusMonitor.isRunning) return;
									
									pointList = null;
									
									ArrayList<ModbusWatchPoint> pointList = getPointList();
									
									if(pointList != null) {
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
										
										// 모드버스 통신 설정 정보
										ModbusMonitor monitor = new ModbusMonitor();
										monitor.setType((isRTU) ? ModbusMonitor.TYPE_RTU : ModbusMonitor.TYPE_TCP);
										monitor.setUnitID(getMonitorUnitID());
										if(monitor.getType() == ModbusMonitor.TYPE_TCP) monitor.setTransactionID(getTid());
															
										// 모드버스 요청 전송
										ModbusMonitor.sendRequest(socket_ko, monitor, pointList, timeout, maxCount);
										
									}
							
								}catch(Exception e) {
									e.printStackTrace();
									StringBuilder sb = new StringBuilder();
									sb.append(Util.colorRed("Modbus Monitor Error\n"));
									sb.append(Util.colorBlue("Modbus Monitor") + " 기능 수행중 처리 할 수 없는 예외가 발생하였습니다" + Util.separator + "\n\n");
									sb.append(String.format("Exception Message : %s\n", e.getMessage()));
									Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
									
								}finally {
									ModbusMonitor.isRunning = false;				
									
								}
							
							}
						}).start(); // 스레드 종료
					}
				
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		reqFormPanel.add(sendButton);
		
		resetButton = new JButton("초기화");
		resetButton.setBounds(935, 43, 100, 30);
		resetButton.setFocusPainted(false);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		resetButton.setForeground(Color.RED);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetComponent();
			}
		});
		reqFormPanel.add(resetButton);
		
		connectButton = new JButton("연결 정보 입력");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		connectButton.setFocusPainted(false);
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(828, 8, 207, 30);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		reqFormPanel.add(connectButton);
		
		fontSize_label = new JLabel("Font Size");
		fontSize_label.setBounds(1055, 10, 100, 24);
		fontSize_label.setHorizontalAlignment(SwingConstants.LEFT);
		fontSize_label.setForeground(Color.BLACK);
		fontSize_label.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fontSize_label.setBackground(Color.WHITE);
		actualPanel.add(fontSize_label);
		
		fontSize_text = new JTextField();
		fontSize_text.setBounds(1055, 40, 100, 30);
		fontSize_text.setText("16");
		fontSize_text.setHorizontalAlignment(SwingConstants.LEFT);
		fontSize_text.setForeground(Color.BLACK);
		fontSize_text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		fontSize_text.setColumns(10);
		fontSize_text.setBorder(UIManager.getBorder("TextField.border"));
		fontSize_text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					int size = Integer.parseInt(fontSize_text.getText().trim());
					fontSize = size;
					log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
				}catch(Exception ex) {
					ex.printStackTrace();
					log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					int size = Integer.parseInt(fontSize_text.getText().trim());
					fontSize = size;
					log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
				}catch(Exception ex) {					
					log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
				}
			}
		});
		actualPanel.add(fontSize_text);
		
		currentState = new JLabel("state");		
		currentState.setBounds(10, 48, 244, 24);
		currentState.setHorizontalAlignment(SwingConstants.CENTER);
		currentState.setForeground(Color.BLACK);
		currentState.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		currentState.setBackground(Color.LIGHT_GRAY);
		actualPanel.add(currentState);
		
		search_textField = new JTextField();
		search_textField.setHorizontalAlignment(SwingConstants.LEFT);
		search_textField.setForeground(Color.BLACK);
		search_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 17));
		search_textField.setColumns(10);
		search_textField.setBorder(new LineBorder(Color.BLACK, 2));
		search_textField.setBackground(Color.WHITE);
		search_textField.setBounds(0, 154, 437, 32);
		search_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					if(pointList != null && (pointList.size() != pointTable.getRowCount())) {
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
					}
					
					setTableStyle(pointTable, search_textField.getText());
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent e) {
				try {
					if(pointList != null && (pointList.size() != pointTable.getRowCount())) {
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
					}
					
					String formula = search_textField.getText().toLowerCase();
					
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(formula.contains("only")) {
							formula = formula.replace("only", "");
							if(!formula.contains("x")) {
								try {
									int value = Integer.parseInt(formula.trim());
									formula = ("x == " + formula);
								}catch(Exception exception) {
									// do nothing
								}
							}
							onlyValueFormulaPoint(formula);
						}
						
					}else {
						setTableStyle(pointTable, formula);	
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(search_textField);
		
		new Thread() {
			public void run() {
				String lastState = "";
				
				while (true) {					
					try {
						Thread.sleep(500);
							
						if(lastState.equalsIgnoreCase(ClientSocket.getCurrentState())) {
							switch(lastState) {
								case ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING : setComponentEnabled(false); break;
								case ClientSocket.SOCKET_STATUS_CONNECTED : setComponentEnabled(true); break;
								case ClientSocket.SOCKET_STATUS_CONNECTING : setComponentEnabled(false); break;
								case ClientSocket.SOCKET_STATUS_COMMUNICATING : setComponentEnabled(true); break;
								case ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR : setComponentEnabled(true); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED : setComponentEnabled(false); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_FAILED : setComponentEnabled(false); break;
								case ClientSocket.SOCKET_STATUS_PING_FAILED : setComponentEnabled(false); break;
								case ClientSocket.SOCKET_STATUS_WAITING_RESPONSE : setComponentEnabled(true); break;
								case ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF : setComponentEnabled(false); break;
								default : setComponentEnabled(false);  break;
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
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ModbusMonitorFrame.isExist = false;
		super.dispose();
	}
	
	public int getMethodValue() {
		int methodValue = -1;
		
		try {
			if(method_Button.getText().equals("Req Count")) {
				methodValue = Integer.parseInt(method_textField.getText().trim());
				method_textField.setForeground(Color.BLUE);
				if(methodValue < 1 || methodValue > 10000) throw new NumberFormatException();

			}else {
				methodValue = getAddress(method_textField);
				int startAddr = getAddress(startAddr_textField);
				
				if(startAddr != -1) {
					if(startAddr > methodValue) {
						method_textField.setForeground(Color.RED);
						return -1;
					}else {
						// 실제 요청 개수
						methodValue = (methodValue - startAddr) + 1;
					}
				}
				
			}
		}catch(Exception e) {
			method_textField.setForeground(Color.RED);
			methodValue = -1;
		}
			
		return methodValue;
	}
	
	public int getMonitorUnitID() {
		return Integer.parseInt(unitID_comboBox.getSelectedItem().toString().replace("번", "").trim());
	}
	
	public int getAddress(JTextField addr_textField) {
		int fc = Integer.parseInt(fc_comboBox.getSelectedItem().toString().split(" ")[1]);
		int startAddress = 0;
		String modbusAddress = "";
		String addr;
		
		switch(fc) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		
		try {
			
			switch(addrTypeComboBox.getSelectedItem().toString()) {
				case "Modbus (DEC)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return -1;
					startAddress = Integer.parseInt(addr);
					startAddress = (startAddress % 10000) - 1;
					if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
					break;
					
				case "Register (DEC)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return -1;				
					startAddress = Integer.parseInt(addr);
					if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
					break;
					
				case "Register (HEX)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return -1;
					
					if(addr.startsWith("0x")||addr.startsWith("0X")) {
						startAddress = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}else {
						addr_textField.setText("0x" + addr);
						startAddress = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}
					if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
					break;
					
				default :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return -1;
					startAddress = Integer.parseInt(addr);
					startAddress = (startAddress % 10000) - 1;
					if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
					break;
			}
		
			String modbusAddr = String.format("%s%04d", modbusAddress, (startAddress & 0xffff) + 1);
			String registerAddr_Hex = String.format("0x%04X", startAddress);
			
			addr_textField.setForeground(Color.BLUE);
			
			return startAddress;
		
		}catch(NumberFormatException e) {
			addr_textField.setForeground(Color.RED);
			return -1;
		}
	}
	
	public String getIntegerAddress(int address, String addrFormat) {
		int fc = Integer.parseInt(fc_comboBox.getSelectedItem().toString().split(" ")[1]);		
		String modbusAddress = "";
		String addr = String.valueOf(address);
		
		switch(fc) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		
		try {
			switch(addrFormat) {
				case "Modbus (DEC)" :					
					if(addr.length() < 1 || addr.equals("")) return "Invalid";
					address = Integer.parseInt(addr);
					address = (address % 10000) - 1;
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
					
				case "Register (DEC)" :
					if(addr.length() < 1 || addr.equals("")) return "Invalid";			
					address = Integer.parseInt(addr);
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
					
				case "Register (HEX)" :					
					if(addr.length() < 1 || addr.equals("")) return "Invalid";
					
					if(addr.startsWith("0x")||addr.startsWith("0X")) {
						address = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}else {						
						address = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
			}
		
			String modbusAddr = String.format("%s%04d", modbusAddress, (address & 0xffff) + 1);
			String registerAddr_Hex = String.format("0x%04X", address);
			
			switch(addrTypeComboBox.getSelectedItem().toString()) {
				case "Modbus (DEC)" :
					return modbusAddr;
					
				case "Register (DEC)" :
					return String.valueOf(address);
					
				case "Register (HEX)" :
					return registerAddr_Hex;
			}
			
			return "Invalid";
		
		}catch(NumberFormatException e) {
			return "Invalid";
		}
	}
	
	public String getStringAddress(JTextField addr_textField, String addrFormat) {
		int fc = Integer.parseInt(fc_comboBox.getSelectedItem().toString().split(" ")[1]);
		int address = 0;
		String modbusAddress = "";
		String addr;
		
		switch(fc) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		
		try {
			switch(addrFormat) {
				case "Modbus (DEC)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return "Invalid";
					address = Integer.parseInt(addr);
					address = (address % 10000) - 1;
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
					
				case "Register (DEC)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return "Invalid";			
					address = Integer.parseInt(addr);
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
					
				case "Register (HEX)" :
					addr = addr_textField.getText().trim();
					if(addr.length() < 1 || addr.equals("")) return "Invalid";
					
					if(addr.startsWith("0x")||addr.startsWith("0X")) {
						address = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}else {
						addr_textField.setText("0x" + addr);
						address = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
					}
					if(address > 0xffff || address < 0) throw new NumberFormatException();
					break;
			}
		
			String modbusAddr = String.format("%s%04d", modbusAddress, (address & 0xffff) + 1);
			String registerAddr_Hex = String.format("0x%04X", address);
			
			switch(addrTypeComboBox.getSelectedItem().toString()) {
				case "Modbus (DEC)" :
					return modbusAddr;
					
				case "Register (DEC)" :
					return String.valueOf(address);
					
				case "Register (HEX)" :
					return registerAddr_Hex;
			}
			
			return "Invalid";
		
		}catch(NumberFormatException e) {
			return "Invalid";
		}
	}
	
	public void syncAddr() {
		startAddr_textField.setText(getStringAddress(startAddr_textField, lastAddrFormat));
		
		if(method_Button.getText().equalsIgnoreCase("End Address")) {
			method_textField.setText(getStringAddress(method_textField, lastAddrFormat));
		}
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
		
	public String getRegisterAddrHex(int registerAddr) {
		return String.format("0x%04X", registerAddr);
	}
	
	public boolean checkFormValidation() {
		boolean formValid = true;
		formValid = formValid && !(startAddr_textField.getForeground() == Color.RED);
		formValid = formValid && !(startAddr_textField.getText().length() < 1 || startAddr_textField.getText().equals(""));
		formValid = formValid && !(startAddr_textField.getText().trim().equals("Invalid"));
		
		if(!formValid) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "모드버스 포인트의 " + Util.colorBlue("요청 시작 주소(Start Address)") +  " 정보를 확인해주세요"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			startAddr_textField.requestFocus();
			return formValid;
		}
		
		formValid = formValid && !(method_textField.getText().length() < 1 || method_textField.getText().equals(""));
		formValid = formValid && !(method_textField.getForeground() == Color.RED);

		if(!formValid) {
			String method = (method_Button.getText().equals("Req Count")) ? "요청 개수(Req Count)" : "마지막 요청 주소(End Address)";
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "모드버스 포인트의 " + Util.colorBlue(method) + " 정보를 확인해주세요"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			
			method_textField.requestFocus();
			return formValid;
		}
		
		return formValid;
	}
	
	public ArrayList<ModbusWatchPoint> getPointList() {
		int fc = Integer.parseInt(fc_comboBox.getSelectedItem().toString().split(" ")[1]);
		
		String dataType = dataType_comboBox.getSelectedItem().toString().toUpperCase().trim();
		int step = 1;
		if(dataType.startsWith("BIN") || dataType.startsWith("TWO")) {
			step  = 1;			
		}else if(dataType.startsWith("FOUR")) {
			step = 2;
		}else if(dataType.startsWith("EIGHT")) {
			step = 4;
		}
		
		try {
			int addr = getAddress(startAddr_textField);
			if(addr < 0) throw new Exception();
			
			int count = getMethodValue();
			if(count == -1) { 
				throw new Exception();
				
			}else if(method_Button.getText().equalsIgnoreCase("End Address")){
				boolean isEven = (count % step) == 0;
				count = isEven ? (count / step) : (count / step) + 1;
			}
			
			int[] addrArray = new int[count];
			addrArray[0] = addr;
			
			for(int i = 1; i < addrArray.length; i++) {
				addrArray[i] = addrArray[i-1] + step;
			}
			
			ArrayList<ModbusWatchPoint> list = new ArrayList<ModbusWatchPoint>();
			for(int i = 0; i < addrArray.length; i++) {
				ModbusWatchPoint wp = new ModbusWatchPoint();
				wp.displayName = "";
				wp.scaleFunc = "x";
				wp.interval = 60;
				wp.measure = "";
				wp.dataFormat = 3;
				String counter = fc + "_" + getRegisterAddrHex(addrArray[i]) + "_" + dataType;
				wp.setCounter(counter);
				wp.init();
				list.add(wp);
			}
			
			return list;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void cleaerLog() {
		log.setText("");
	}
	
	public static void writeLog(String content) {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override public void run() {		    	
				log.append(content);
				log.append(System.lineSeparator());
				log_scrollPane.getVerticalScrollBar().setValue(log_scrollPane.getVerticalScrollBar().getMaximum());
		    }
		});
	}
	
	public void setComponentEnabled(boolean enabled) {
		// 소켓 접속 전에는 컴포넌트들을 사용하지 않는다
		
		try {
			if(enabled) {
				cardLayout.show(cardPanel, "actualPanel");
			}else {
				cardLayout.show(cardPanel, "image");
				search_textField.setText("");
			}
		}catch(Exception e) {
			// Do nothing
		}
		
		currentState.setEnabled(enabled);
		currentState.setVisible(enabled);
		
		reqFormPanel.setEnabled(enabled);
		reqFormPanel.setVisible(enabled);
		
		log_scrollPane.setEnabled(enabled);
		log_scrollPane.setVisible(enabled);
		
		// 레이블
		addrFormat_label.setEnabled(enabled);
		addrFormat_label.setVisible(enabled);
		
		transactionID_label.setEnabled(enabled);
		transactionID_label.setVisible(enabled);
		
		unitID_label.setEnabled(enabled);
		unitID_label.setVisible(enabled);
		
		timeout_label.setEnabled(enabled);
		timeout_label.setVisible(enabled);

		maxCount_label.setEnabled(enabled);
		maxCount_label.setVisible(enabled);
		
		fontSize_label.setEnabled(enabled);
		fontSize_label.setVisible(enabled);
		
		// 컴포넌트
		radio_modbusTCP.setEnabled(enabled);
		radio_modbusTCP.setVisible(enabled);
		
		radio_modbusRTU.setEnabled(enabled);
		radio_modbusRTU.setVisible(enabled);
		
		addrTypeComboBox.setEnabled(enabled);
		addrTypeComboBox.setVisible(enabled);
		
		transactionId_text.setEnabled(enabled);
		transactionId_text.setVisible(enabled);
		
		unitID_comboBox.setEnabled(enabled);
		unitID_comboBox.setVisible(enabled);
		
		timeout_text.setEnabled(enabled);
		timeout_text.setVisible(enabled);
		
		maxCount_text.setEnabled(enabled);
		maxCount_text.setVisible(enabled);
		
		fontSize_text.setEnabled(enabled);
		fontSize_text.setVisible(enabled);
		
		if(enabled) {
			setTitle(String.format("Modbus Monitor : %s", ClientSocket.getSimpleConnectedInfo()));
		}else {
			setTitle(String.format("Modbus Monitor"));
		}
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Modbus Monitor Frame Already Exists") + Util.separator + "\n");
		sb.append("Modbus Monitor 프레임이 이미 열려있습니다" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public void resetComponent() {		
		addrTypeComboBox.setSelectedIndex(1);
		
		transactionId_text.setText("1");
		transactionId_text.setForeground(Color.BLUE);
		
		unitID_comboBox.setSelectedIndex(0);
		
		timeout_text.setText("5000");
		timeout_text.setForeground(Color.BLUE);
		
		maxCount_text.setText("125");
		maxCount_text.setForeground(Color.BLUE);
		
		fc_comboBox.setSelectedIndex(2);
		
		startAddr_textField.setText("0");
		startAddr_textField.setForeground(Color.BLUE);
				
		method_Button.setText("Req Count");
		method_textField.setText("1");
		method_textField.setForeground(Color.BLUE);
		
		dataType_comboBox.setSelectedIndex(0);
		
		fontSize_text.setText("16");
		
		fontSize = 16;
		log.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));		
		log.setText("");
		log.requestFocus();
		
		search_textField.setText("");
		
		pointList = null;
		resetTable(pointTable, null);
	}
	
	public static void resetTable(JTable table, Object[][] content){
		String[] header = new String[] { "순 서", "기능코드", "주 소", "값" };
		table.setModel(new DefaultTableModel(content, header) {
				boolean[] columnEditables = new boolean[] {
						false, // 순 서 : 수정 불가
						false, // 기능코드 : 수정 불가
						false, // 주 소 : 수정 불가
						false // 값 : 수정 불가
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(table, null);
	}
	
	public static void setTableStyle(JTable table, String valueFormula) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setForeground(Color.BLACK);		
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(80); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // 기능코드
		table.getColumnModel().getColumn(2).setPreferredWidth(120); // 주 소
		table.getColumnModel().getColumn(3).setPreferredWidth(200); // 값
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		// 값
		DefaultTableCellRenderer valueCellRenderer = null;
		if(valueFormula == null || valueFormula.length() == 0 || valueFormula.equalsIgnoreCase("")) {
			valueCellRenderer = new DefaultTableCellRenderer();
			
		}else {
			if(!valueFormula.toLowerCase().contains("x")) {
				try {
					int value = Integer.parseInt(valueFormula.trim());
					valueFormula = ("x == " + valueFormula);
				}catch(Exception e) {
					// do nothing
				}
			}
			
			valueCellRenderer = new ModbusCellRenderer(valueFormula, "value", pointList);
		}
		valueCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 기능코드
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 주소
		tcmSchedule.getColumn(3).setCellRenderer(valueCellRenderer); // 값		
	}
	
	/**
	 * 	레코드 추가
	 */
	public static void addRecord(JTable table, ArrayList<ModbusWatchPoint> pointList) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			// 기능코드, 주소, 보정식 순서로 정렬
			Collections.sort(pointList);			
			
			for(int i = 0; i < pointList.size(); i++) {
				
				ModbusWatchPoint point = pointList.get(i);
				record = new Vector();
				/* column[0] */ record.add(point.getIndex()); // 순 서
				/* column[1] */ record.add(point.getFunctionCode()); // 기능코드
				
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
				
				/* column[2] */ record.add(addr);  // 주소
				/* column[3] */ record.add(PerfData.getPerfPureValue(point.getData())); // 결 과
				
				model.addRow(record);
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void updateTable(JTable table) {
		
		if(pointList == null || pointList.size() < 1) {
			resetTable(table, null);
			return;
		}
		
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		
		Object[][] content = new Object[rowCount][];
		
		for(int i = 0; i < rowCount; i++) {
			content[i] = new Object[columnCount];
			
			int index = Integer.parseInt(pointTable.getValueAt(i, 0).toString());
			ModbusWatchPoint point = pointList.get(index-1);
			
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
			content[i][1] = point.getFunctionCode();
			content[i][2] = addr;
			content[i][3] = PerfData.getPerfPureValue(point.getData());			
		}
		
		resetTable(table, content);
		
		
		try {
			String formula = search_textField.getText().toLowerCase();
			
			if(formula.contains("only")) {
				formula = formula.replace("only", "");
				if(!formula.contains("x")) {
					try {
						int value = Integer.parseInt(formula.trim());
						formula = ("x == " + formula);
					}catch(Exception exception) {
						// do nothing
					}
				}
				onlyValueFormulaPoint(formula);
				
			}else {
				setTableStyle(pointTable, formula);	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void onlyValueFormulaPoint(String formula) {
    	ArrayList<ModbusWatchPoint> findPointList = new ArrayList<ModbusWatchPoint>();
		
		for(ModbusWatchPoint p : pointList) {
			String pureData = p.getData().getPureValue().toString();
			if(!pureData.equalsIgnoreCase("none")) {
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
		setTableStyle(pointTable, formula);
	}
	
	public void connect() {
		// 클라이언트 소켓의 마지막 커넥션 정보
		String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
		
		try {
			socket_ko = ModbusAgent.clientSocket;
			src_en.swing.ModbusAgent_Panel.socket_en = socket_ko;
			
			if( (socket_ko == null || socket_ko.isClosed()) && ClientSocket.getIsFirst()) {				
				String[] connectionInfo = ClientSocket.getConnectionInfo();
				IP = connectionInfo[0];
				PORT = Integer.parseInt(connectionInfo[1]);
				
				src_en.swing.ModbusAgent_Panel.IP = IP;
				src_en.swing.ModbusAgent_Panel.PORT = PORT;
				
			}else if(socket_ko == null) {
				String[] connectionInfo = ClientSocket.getConnectionInfo();
				IP = connectionInfo[0];
				PORT = Integer.parseInt(connectionInfo[1]);
				
				src_en.swing.ModbusAgent_Panel.IP = IP;
				src_en.swing.ModbusAgent_Panel.PORT = PORT;
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
			src_en.swing.ModbusAgent_Panel.socket_en = socket_ko;
			
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
			
			setComponentEnabled(true);
			
			// 마지막 커넥션 정보와 다른 정보로 세션을  생성시 컴포넌트 초기화
			if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
				resetComponent();
//				src_en.swing.ModbusMonitorFrame.resetComponent(); 영문버전 추가시 주석 해제
			}
			
			// 사용자가 입력한 IP, port를 클라이언트 소켓의 마지막 연결 성공 정보에 저장
			ClientSocket.setHasLastConnectionInfo(true);
		}
	}
}