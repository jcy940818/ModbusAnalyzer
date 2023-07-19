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

import common.util.FontManager;
import common.util.SwingUtil;
import src_ko.agent.ClientSocket;
import src_ko.agent.ModbusAgent;
import src_ko.agent.Perf;
import src_ko.analyzer.RX.DataType;
import src_ko.info.RX_Info;
import src_ko.info.TX_Info;
import src_ko.util.ExceptionProvider;
import src_ko.util.JavaScript;
import src_ko.util.TX_Generator;
import src_ko.util.Util;

public class RealTime_Panel extends JPanel {
	
	private static boolean monitoringRunning = false;
	
	// Ŭ���̾�Ʈ ����
	public static Socket socket_ko = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;	
	
	// information Component
	JPanel infoPanel; // Ŭ���̾�Ʈ ������ ������ ���� �� �����϶��� �������̼� ������Ʈ���� Ȱ��ȭ ��Ų��.  
	JPanel inputFormPanel;
	JPanel typePanel;
	JPanel resultPanel;
	JPanel dataTypePanel;
	JPanel imagePanel; /* ONION Image */
	
	private JButton connectButton; // ���� ���� �Է¹�ư (�߿�)
	private static boolean isRTU = false; // Default : Modbus TCP (���� �߿��� ����)
	public static JComboBox dataTypeComboBox = null;
	private static RX_Info global_rx = null;
	public static JTable table;
	private static JLabel currentState;
	private static JLabel modbusAddress_label;
	
	// TX Form ���� ���� ������Ʈ
	private CardLayout inputPanel_layout;
	private JTextField transactionId_text; // Modbus TCP : TransactionID �ʵ�
	private JComboBox unitId_comboBox; // ����ȣ �޺��ڽ�
	private JComboBox functionCode_comboBox; // ����ڵ� �ʵ�
	private JTextField startAddress_text; // �����ּ�, �����ּ� �ʵ�
	private JComboBox requestCount_comboBox; // ��û����
	private JButton form_sendPacketButton;		
	private static JLabel form_scale_label;
	private static JTextField form_scale_textField;
	private static JButton form_resetButton;
	private static JRadioButton radio_modbusTCP;
	private static JRadioButton radio_modbusRTU;
	
	// ��� ���
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
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 266, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 20));
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
		resultTable_ScrollPane.setFont(FontManager.getFont(Font.PLAIN, 13));
		resultTable_ScrollPane.setBackground(Color.WHITE);
		resultTable_ScrollPane.setBounds(578, 10, 438, 405);
		resultPanel.add(resultTable_ScrollPane);
		
		// ���̺� ���� �κ�
		table = new JTable();
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // ���� Ŭ��
				if (e.getButton() == 1 && e.getClickCount() == 2) { } // ���� ��ư ���� Ŭ��
				if (e.getButton() == 3) {
					// ������ Ŭ��
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
		packetLog.setFont(FontManager.getFont(Font.PLAIN, 16));				
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
		currentState.setFont(FontManager.getFont(Font.BOLD, 22));
		currentState.setBackground(Color.WHITE);
		currentState.setBounds(267, 5, 125, 45);
		infoPanel.add(currentState);
		
		connectButton = new JButton("\uC5F0\uACB0 \uC815\uBCF4 \uC785\uB825");
		connectButton.setForeground(Color.BLACK);
		connectButton.setFocusPainted(false);
		connectButton.setContentAreaFilled(false);
		connectButton.setBorder(UIManager.getBorder("Button.border"));		
		connectButton.setFont(FontManager.getFont(Font.BOLD, 17));
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(395, 11, 160, 36);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {												
				
				// Ŭ���̾�Ʈ ������ ������ Ŀ�ؼ� ����
				String lastConnectionInfo = ClientSocket.getSimpleConnectedInfo();
				
				try {
					socket_ko = ModbusAgent.clientSocket;
					src_en.swing.RealTime_Panel.socket_en = socket_ko;
					
					if( (socket_ko == null || socket_ko.isClosed()) && ClientSocket.getIsFirst()) {						
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_en.swing.RealTime_Panel.IP = IP;
						src_en.swing.RealTime_Panel.PORT = PORT;
						
					}else if(socket_ko == null) {
						String[] connectionInfo = ClientSocket.getConnectionInfo();
						IP = connectionInfo[0];
						PORT = Integer.parseInt(connectionInfo[1]);
						
						src_en.swing.RealTime_Panel.IP = IP;
						src_en.swing.RealTime_Panel.PORT = PORT;
					}else {
						// ���� ����Ǿ��ִ� ������ ��� ������ ���� �������� �õ��Ѵ�.						
						// Ŭ���̾�Ʈ ���� : ���� ����
						socket_ko.close();						
						ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
					}
				}catch(IOException exception) {
					return;
				}
				
				try {
					socket_ko = ClientSocket.getClientSocket(IP, PORT);
					src_en.swing.RealTime_Panel.socket_en = socket_ko;
					
				}catch(Exception exception) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='red'>���� ����</font>\n");
					msg.append("�Է��Ͻ� ���� ������ Ȯ�����ּ���" + Util.separator + "\n");					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}				
				
				if(socket_ko != null || ClientSocket.isCurrentConnected(socket_ko)) {
					// ���� ���� : ������Ʈ ������� ��� �ʱ�ȭ�Ѵ�	
					ModbusAgent.clientSocket = socket_ko;
					src_en.agent.ModbusAgent.clientSocket = socket_ko;
					
					panel_ON();
					
					// ������ Ŀ�ؼ� ������ �ٸ� ������ ������  ������ ������Ʈ �ʱ�ȭ
					if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
						componentAllClear();
						src_en.swing.RealTime_Panel.componentAllClear();
					}
					
					// ����ڰ� �Է��� IP, port�� Ŭ���̾�Ʈ ������ ������ ���� ���� ������ ����					
					ClientSocket.setHasLastConnectionInfo(true);
				}
			}
		});
		
		infoPanel.add(connectButton);
		
		dataTypePanel.setVisible(false); // functionCode 3, 4 �϶��� ������ Ÿ�� �޺��ڽ� ǥ�� (functionCdoe 1, 2 : ON/OFF �����̱� ������)		
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
		transactionId_text.setBounds(164, 31, 85, 31);
		transactionId_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int transactionId = 0;
				
				if(transactionId_text.getText().startsWith("0x")||transactionId_text.getText().startsWith("0X")) {
					// 16���� ǥ��� (0x0000)
					try {
						if(transactionId_text.getText().startsWith("0x")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0x", ""),16); 
						if(transactionId_text.getText().startsWith("0X")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
					// 16���� ǥ��� (0x0000)
					try {
						if(transactionId_text.getText().startsWith("0x")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0x", ""),16); 
						if(transactionId_text.getText().startsWith("0X")) transactionId = Integer.parseInt(transactionId_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						transactionId_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
		
		JLabel unitId_label = new JLabel("\uC7A5\uBE44\uBC88\uD638");
		unitId_label.setForeground(Color.BLACK);
		unitId_label.setHorizontalAlignment(SwingConstants.LEFT);
		unitId_label.setFont(FontManager.getFont(Font.BOLD, 16));
		unitId_label.setBounds(266, 31, 77, 31);
		form_InputPanel.add(unitId_label);
		
		unitId_comboBox = new JComboBox();
		unitId_comboBox.setForeground(Color.BLACK);
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "��";
		}		
		unitId_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		unitId_comboBox.setSelectedIndex(0);
		unitId_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		unitId_comboBox.setBackground(Color.WHITE);
		unitId_comboBox.setBounds(338, 31, 85, 32);
		unitId_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(unitId_comboBox);
		
		JLabel startAddress_label = new JLabel("\uBAA8\uB2C8\uD130\uB9C1 \uC2DC\uC791\uC8FC\uC18C");
		startAddress_label.setForeground(Color.BLACK);
		startAddress_label.setHorizontalAlignment(SwingConstants.RIGHT);
		startAddress_label.setFont(FontManager.getFont(Font.BOLD, 16));
		startAddress_label.setBounds(17, 71, 141, 31);
		form_InputPanel.add(startAddress_label);
		
		startAddress_text = new JTextField();
		startAddress_text.setHorizontalAlignment(SwingConstants.LEFT);
		startAddress_text.setFont(FontManager.getFont(Font.BOLD, 15));
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
					modbusAddress_label.setText("������ �ּ�");
					modbusAddress_label.setForeground(Color.DARK_GRAY);
					return;
				}
				
				if(startAddress_text.getText().startsWith("0x")||startAddress_text.getText().startsWith("0X")) {
					// 16���� �ּ� ǥ��� (0x0000)
					try {
						if(startAddress_text.getText().startsWith("0x")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0x", ""),16); 
						if(startAddress_text.getText().startsWith("0X")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0X", ""),16);
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("��ȿ���� ���� �ּ�");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}
				}else {
					// �Ϲ� 10���� �ּ� ǥ���
					try {
						startAddress = Integer.parseInt(startAddress_text.getText());
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("��ȿ���� ���� �ּ�");
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
					modbusAddress_label.setText("������ �ּ�");
					modbusAddress_label.setForeground(Color.DARK_GRAY);
					return;
				}
				
				if(startAddress_text.getText().startsWith("0x")||startAddress_text.getText().startsWith("0X")) {
					// 16���� �ּ� ǥ��� (0x0000)
					try {
						if(startAddress_text.getText().startsWith("0x")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0x", ""),16); 
						if(startAddress_text.getText().startsWith("0X")) startAddress = Integer.parseInt(startAddress_text.getText().replaceAll("0X", ""),16);
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("��ȿ���� ���� �ּ�");
						startAddress_text.setForeground(Color.RED);
						modbusAddress_label.setForeground(Color.RED);
						return;
					}
				}else {
					// �Ϲ� 10���� �ּ� ǥ���
					try {
						startAddress = Integer.parseInt(startAddress_text.getText());
						if(startAddress > (Short.MAX_VALUE | 0xffff)) throw new NumberFormatException();
					}catch(NumberFormatException exception) {
						modbusAddress_label.setText("��ȿ���� ���� �ּ�");
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
		
		JLabel functionCode_label = new JLabel("\uAE30\uB2A5\uCF54\uB4DC");
		functionCode_label.setForeground(Color.BLACK);
		functionCode_label.setHorizontalAlignment(SwingConstants.LEFT);
		functionCode_label.setFont(FontManager.getFont(Font.BOLD, 16));
		functionCode_label.setBounds(435, 31, 77, 31);
		form_InputPanel.add(functionCode_label);
		
		functionCode_comboBox = new JComboBox();
		functionCode_comboBox.setForeground(Color.BLACK);
		functionCode_comboBox.setBackground(Color.WHITE);		
		functionCode_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
		functionCode_comboBox.setSelectedIndex(2);
		functionCode_comboBox.setBounds(507, 31, 85, 32);
		functionCode_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		functionCode_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();								
				
				// ����ڵ� �޺��ڽ� ���� ����� ����ڿ��� ǥ������ ������ �����ּҸ� �������־�� �ϱ� ������
				// �����ּ� �ؽ�Ʈ�ʵ��� keyEvent�� �߻������ش�
				KeyListener keyListener =  startAddress_text.getKeyListeners()[0];
				if(keyListener != null) keyListener.keyReleased(lastKeyEvent);				
			}
		});
		form_InputPanel.add(functionCode_comboBox);
		
		
		JLabel requestCount_label = new JLabel("\uC694\uCCAD\uAC1C\uC218");
		requestCount_label.setForeground(Color.BLACK);
		requestCount_label.setHorizontalAlignment(SwingConstants.LEFT);
		requestCount_label.setFont(FontManager.getFont(Font.BOLD, 16));
		requestCount_label.setBounds(435, 71, 77, 31);
		form_InputPanel.add(requestCount_label);
		
		requestCount_comboBox = new JComboBox();		
		requestCount_comboBox.setForeground(Color.BLACK);
		String[] requestValue = new String[125];
		for(int i = 0; i < 125; i++) {
			requestValue[i] = String.valueOf(i+1) + "��";
		}		
		requestCount_comboBox.setModel(new DefaultComboBoxModel(requestValue));
		requestCount_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		requestCount_comboBox.setBackground(Color.WHITE);
		requestCount_comboBox.setBounds(507, 71, 85, 32);
		requestCount_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_InputPanel.add(requestCount_comboBox);
		
		form_resetButton = new JButton("\uCD08\uAE30\uD654");
		form_resetButton.setForeground(Color.BLACK);
		form_resetButton.setFont(FontManager.getFont(Font.BOLD, 13));
		form_resetButton.setBackground(Color.WHITE);
		form_resetButton.setBounds(797, 71, 88, 31);
		form_resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				
				// �̹� Exception ��ĵ �۾��� �������̶�� ���� �������� ��ĵ �۾��� �������� �����.
				if(RealTime_Panel.monitoringRunning) {
					monitoringStopConfirm();
					return;
				}
				
				global_rx = null;
				dataTypePanel.setVisible(false);
				packetLog.setText(null);
				resetTable(table);
								
				autoTid_CheckBox.setSelected(false); // auto TID ����
				autoTid_CheckBox.setText("Auto TID OFF"); // auto TID ����
				autoTid_CheckBox.setForeground(Color.BLACK); // auto TID ����
				
				form_scale_textField.setText("");				
				/* JTextField */ transactionId_text.setText("1"); // Modbus TCP : TransactionID �ʵ�
				transactionId_text.setForeground(Color.BLUE);
				/* JComboBox */ unitId_comboBox.setSelectedIndex(0); // ����ȣ �޺��ڽ�	
				/* JComboBox */ functionCode_comboBox.setSelectedIndex(2); // ����ڵ�
				/* JTextField */ startAddress_text.setText(null); // �����ּ�, �����ּ� �ʵ�
				/* JComboBox */ requestCount_comboBox.setSelectedIndex(0); // ��û ����																			
				modbusAddress_label.setText("������ �ּ�");
				modbusAddress_label.setForeground(Color.DARK_GRAY);
								
				timeout_text.setText("500"); // Ÿ�Ӿƿ� �ʵ�
				timeout_text.setForeground(Color.BLUE);
				interval_text.setText("1000"); // �����ֱ� �ʵ�
				interval_text.setForeground(Color.BLUE);								
				
				// �����ּҿ� ��Ŀ��
				startAddress_text.requestFocus();
			}
		});
		
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		// TX Form ���� ��ư
		form_sendPacketButton = new JButton("\uC2DC \uC791");
		// ���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
							
				// �̹� Exception ��ĵ �۾��� �������̶�� ���� �������� ��ĵ �۾��� �������� �����.
				if(RealTime_Panel.monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Real-Time Monitoring Already in Progress\n"));
					sb.append(String.format("%s �۾��� �̹� �������Դϴ�%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("���� �������� �۾��� ����ϱ� ���ؼ��� %s ��ư�� Ŭ�����ּ���%s%s%s", Util.colorRed("[ �� �� ]") ,Util.separator, Util.separator, "\n"));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				// ���� ��û TX ������ �ʿ��� Form �� ������ ��� �ԷµǾ� �ִ��� üũ
				if(!checkReadRequestForm(isRTU)) return;
				int timeout = Integer.parseInt(timeout_text.getText().trim());																
				if(timeout == 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Infinite Timeout?\n"));
					sb.append(String.format("Ÿ�Ӿƿ� �������� " + Util.colorBlue("0ms") + " ���� �����ϸ� ���� ��Ŷ�� �����ϱ� ������ ������ ����մϴ�%s%s%s", Util.separator, Util.separator, "\n\n"));
					sb.append(String.format("�̴�� Ÿ�Ӿƿ� �������� �������� �����ϰ� %s �Ͻðڽ��ϱ�?%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
					
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
					sb.append(String.format("���� Ÿ�Ӿƿ��� " + Util.colorBlue("0ms") + " �̻��� ������ �Է� �� �� �ֽ��ϴ�%s%s%s", Util.separator, Util.separator, "\n"));	
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				int interval = Integer.parseInt(interval_text.getText().trim());
				if(interval < 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Interval Field Error\n"));					
					sb.append(String.format("���� �ֱ�� " + Util.colorBlue("0ms") + " �̻��� ������ �Է� �� �� �ֽ��ϴ�%s%s%s", Util.separator, Util.separator, "\n"));	
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}				
				
				
				try {						
					
					// ����͸� ���� : ����
					RealTime_Panel.monitoringRunning = true;
					
					// �ѹ� �ʱ�ȭ�� TX �������� ����ؼ� ����
					tx = initReadTX(isRTU);
					tx.setAgentType("RealTime");
					
					
					/**
					 * Monitoring Start �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
					 */
					
					new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								while(RealTime_Panel.monitoringRunning) {
											
									if(!RealTime_Panel.monitoringRunning) {
										StringBuilder sb = new StringBuilder();
										sb.append(Util.colorRed("Stop Real-Time Monitoring\n"));
										sb.append(String.format("%s �۾��� ���� �Ǿ����ϴ�%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
										Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
										return;
									}
									
									// Modbus TCP : TID �ڵ� ����
									if(!isRTU && autoTid_CheckBox.isSelected()) {
										// ������ TID �ݿ��Ͽ� �籸��										
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
									
									rx = ModbusAgent.communicate(socket_ko, tx, isRTU, timeout);
									
									// ��ȿ���� ���� ������ �н��Ѵ�
									if(rx == null) continue;
									if(rx.isException()) continue;
									if(rx.isCRCError()) continue; 										
									if(rx.getScanResult() == null) continue;									
									if(ExceptionProvider.CompareTxRx(tx, rx) != null) continue;									
									
									// ����ڵ尡 3, 4 �϶��� ������ Ÿ�� ���� �޺��ڽ��� ǥ��
									setDataType(rx);																																																				
																																														
									// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
									global_rx = rx;
									updateTable(table, rx);
									ModbusAgent.isRTU = isRTU;
									ModbusAgent.lastFunctionCode = rx.getFunctionCode();
																		
									try {
										// ������ �˻� ����(ms) ���� ��� 
										Thread.sleep(interval);
									}catch(Exception e) {
										e.printStackTrace();
									}
									
								}// end Send Packet (for loop) 
								
							}catch(Exception e) {
								e.printStackTrace();
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("Real-Time Monitoring Error\n"));
								sb.append(Util.colorBlue("Real-Time Monitoring") + " ��� ������ ó�� �� �� ���� ���ܰ� �߻��Ͽ����ϴ�" + Util.separator + "\n\n");
								sb.append(String.format("Exception Message : %s\n", e.getMessage()));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);																
							}finally {
								RealTime_Panel.monitoringRunning = false;
							}
						}
					}).start();					
					
					/**
					 * Monitoring End �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
					 */					
					
					
				}catch(Exception exception) {
					resetTable(table);
					exception.printStackTrace();
					RealTime_Panel.monitoringRunning = false;
				}				
				
			}			
		}); // end formSendPacketButton Action
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(FontManager.getFont(Font.BOLD, 13));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(797, 31, 88, 31);		
		form_InputPanel.add(form_sendPacketButton);
		form_InputPanel.add(form_resetButton);
				
		
		modbusAddress_label = new JLabel("\uBAA8\uB4DC\uBC84\uC2A4 \uC8FC\uC18C");		
		modbusAddress_label.setBackground(Color.WHITE);
		modbusAddress_label.setForeground(Color.DARK_GRAY);
		modbusAddress_label.setHorizontalAlignment(SwingConstants.LEFT);
		modbusAddress_label.setFont(FontManager.getFont(Font.BOLD, 16));
		modbusAddress_label.setBounds(266, 72, 160, 31);
		modbusAddress_label.setOpaque(true);
		form_InputPanel.add(modbusAddress_label);
				
		
		
		form_scale_label = new JLabel("Scale Function");
		form_scale_label.setBackground(Color.WHITE);
		form_scale_label.setFont(FontManager.getFont(Font.BOLD, 14));
		form_scale_label.setForeground(new Color(0, 128, 0));
		form_scale_label.setHorizontalAlignment(SwingConstants.CENTER);
		form_scale_label.setBounds(398, 4, 109, 23);
		form_scale_label.setVisible(false);
		form_InputPanel.add(form_scale_label);
		
		form_scale_textField = new JTextField();
		form_scale_textField.setForeground(Color.BLACK);
		form_scale_textField.setText("");		
		form_scale_textField.setFont(FontManager.getFont(Font.BOLD, 14));
		form_scale_textField.setBackground(Color.WHITE);
		form_scale_textField.setHorizontalAlignment(SwingConstants.LEFT);		
		form_scale_textField.setBounds(507, 5, 381, 21);
		form_scale_textField.setColumns(10);
		form_scale_textField.setVisible(false);
		form_InputPanel.add(form_scale_textField);
		
		JLabel timeout_Label = new JLabel("\uD0C0\uC784\uC544\uC6C3");
		timeout_Label.setForeground(Color.BLACK);
		timeout_Label.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		timeout_Label.setBounds(604, 31, 77, 31);
		form_InputPanel.add(timeout_Label);
		
		timeout_text = new JTextField();
		timeout_text.setText("500");
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(FontManager.getFont(Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(675, 31, 77, 31);
		timeout_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int timeout = 0;
				
				if(timeout_text.getText().startsWith("0x")||timeout_text.getText().startsWith("0X")) {
					// 16���� ǥ��� (0x0000)
					try {
						if(timeout_text.getText().startsWith("0x")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0x", ""),16); 
						if(timeout_text.getText().startsWith("0X")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
					// 16���� ǥ��� (0x0000)
					try {
						if(timeout_text.getText().startsWith("0x")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0x", ""),16); 
						if(timeout_text.getText().startsWith("0X")) timeout = Integer.parseInt(timeout_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						timeout_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
		
		JLabel interval_Label = new JLabel("\uC218\uC9D1\uC8FC\uAE30");
		interval_Label.setForeground(Color.BLACK);
		interval_Label.setHorizontalAlignment(SwingConstants.LEFT);
		interval_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		interval_Label.setBounds(604, 71, 77, 31);
		form_InputPanel.add(interval_Label);
		
		interval_text = new JTextField();
		interval_text.setText("1000");
		interval_text.setForeground(Color.BLUE);
		interval_text.setHorizontalAlignment(SwingConstants.LEFT);
		interval_text.setFont(FontManager.getFont(Font.BOLD, 15));
		interval_text.setColumns(10);
		interval_text.setBorder(UIManager.getBorder("TextField.border"));
		interval_text.setBounds(675, 71, 77, 31);
		interval_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				int interval = 0;
				
				if(interval_text.getText().startsWith("0x")||interval_text.getText().startsWith("0X")) {
					// 16���� ǥ��� (0x0000)
					try {
						if(interval_text.getText().startsWith("0x")) interval = Integer.parseInt(interval_text.getText().replaceAll("0x", ""),16); 
						if(interval_text.getText().startsWith("0X")) interval = Integer.parseInt(interval_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
					// 16���� ǥ��� (0x0000)
					try {
						if(interval_text.getText().startsWith("0x")) interval = Integer.parseInt(interval_text.getText().replaceAll("0x", ""),16); 
						if(interval_text.getText().startsWith("0X")) interval = Integer.parseInt(interval_text.getText().replaceAll("0X", ""),16);
					}catch(NumberFormatException exception) {
						interval_text.setForeground(Color.RED);
						return;
					}
				}else {
					// 10���� ǥ���
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
		timeoutMeasure_Label.setForeground(Color.BLACK);
		timeoutMeasure_Label.setHorizontalAlignment(SwingConstants.LEFT);
		timeoutMeasure_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		timeoutMeasure_Label.setBounds(755, 31, 35, 31);
		form_InputPanel.add(timeoutMeasure_Label);
		
		JLabel intervalMeasure_Label = new JLabel("ms");
		intervalMeasure_Label.setForeground(Color.BLACK);
		intervalMeasure_Label.setHorizontalAlignment(SwingConstants.LEFT);
		intervalMeasure_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		intervalMeasure_Label.setBounds(755, 71, 35, 31);
		form_InputPanel.add(intervalMeasure_Label);
		
		autoTid_CheckBox = new JCheckBox("Auto TID OFF");
		autoTid_CheckBox.setForeground(Color.BLACK);
		autoTid_CheckBox.setFont(FontManager.getFont(Font.BOLD, 14));
		autoTid_CheckBox.setFocusPainted(false);
		autoTid_CheckBox.setBackground(Color.WHITE);
		autoTid_CheckBox.setBounds(130, 7, 129, 20);
		autoTid_CheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(RealTime_Panel.monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Auto TID can't set it up\n"));
					sb.append(String.format("%s ��� �����߿��� %s �ɼ��� ���� �� �� �����ϴ�%s%s%s",Util.colorBlue("Real-Time Monitoring"), Util.colorBlue("Auto TID") ,Util.separator, Util.separator, "\n"));
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
		radio_modbusTCP.setBounds(8, 43, 125, 30);
		typePanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(FontManager.getFont(Font.BOLD, 15));
		radio_modbusRTU.setBounds(8, 72, 125, 30);
		typePanel.add(radio_modbusRTU);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		
		// Modbus Ÿ���� TCP���� RTU������ �����ϴ� ���� ��ư �̺�Ʈ
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel2.setText(b.getText()); // ���� �Է� �ǳ�	
				
				// Modbus RTU, TCP ���� ��ư �̵� �� 
				// ������ Ÿ�� �ǳ� ����� , ������ Ÿ�� �޺��ڽ� ���� �ʱ�ȭ
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() ���� ȣ���
				global_rx = null;
				modbusAddress_label.setText("������ �ּ�");
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
		
		// ���� ��ư(TCP/RTU)�� ������ �߰�
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		panel_OFF();
		
		// Ŭ���̾�Ʈ ������ �������϶��� �����ӿ� ������ ǥ���Ѵ�.
		// ������
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
							// resultPanel�� isVisible() ���θ� �˻��Ͽ� ������ �������� ��
							// dataTypePanel �� ������Ʈ���� �����.
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
							// �ǽð� ����͸� ������
							form_resetButton.setText("�� ��");
							form_resetButton.setForeground(Color.RED);
							formDisable();
							
						}else {
							// �ǽð� ����͸� ����
							form_resetButton.setText("�ʱ�ȭ");
							form_resetButton.setForeground(Color.BLACK);
							formEnable();
						}
						
						// ModbusAgent <=> ExceptionScan : Socket ����ȭ
						socket_ko = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
		
		radio_modbusRTU.doClick();
		
	}// end RealTime_Panel()
	
	
	public void panel_ON() {
		// ���� ������ �ǳ� ������Ʈ���� ������� �ʴ´�
		typePanel.setVisible(true);
		typePanel.setEnabled(true);
		inputFormPanel.setVisible(true);
		inputFormPanel.setEnabled(true);
		resultPanel.setVisible(true);
		resultPanel.setEnabled(true);							
		imagePanel.setVisible(false);
		imagePanel.setEnabled(false);
		infoPanel.setBounds(12, 10, 1050, 489); // �������̼� �ǳ� ũ�� ����ȭ
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle(String.format("ModbusAnalyzer : %s", ClientSocket.getSimpleConnectedInfo()));
		}
	}
	
	
	public void panel_OFF() {
		// ���� ������ �ǳ� ������Ʈ���� ������� �ʴ´�
		typePanel.setVisible(false);
		typePanel.setEnabled(false);
		inputFormPanel.setVisible(false);
		inputFormPanel.setEnabled(false);
		resultPanel.setVisible(false);
		resultPanel.setEnabled(false);
		imagePanel.setVisible(true);
		imagePanel.setEnabled(true);
		infoPanel.setBounds(12, 10, 1050, 606); // �������̼� �ǳ� ��ü���
		
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
		sb.append(String.format("���� �������� %s �۾��� �����ұ��?%s%s%s",Util.colorBlue("Real-Time Monitoring") ,Util.separator, Util.separator, "\n"));
		
		int monitoringStop = Util.showConfirm(sb.toString());
		
		if(monitoringStop == JOptionPane.YES_OPTION) {
			RealTime_Panel.monitoringRunning = false;
		} else {
			return;
		}
	}
	
	
	
	public static void resetTable(JTable table){
		// ���̺� ��� ����
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				// ���̺� �⺻ �� ����
			},
			new String[] {
				"\uC21C \uC11C", "Register", "Modbus", "\uB808\uC9C0\uC2A4\uD130 \uAC12"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // �������� �ּ�
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // ������ �ּ�
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // ��
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);			
	}
	
	
	public static void updateTable(JTable table, RX_Info rx) {
		
		if((table == null)||(rx == null)||(rx.getPerfInfo() == null)) {
			return;
		}
		
		// ���̺� ��� ����
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));

		Object[][] content = null;
		
		// ��� ���̺��� ���� �������ִ� ����
		int tableRow;
		
		if(rx.getFunctionCode() == 0x01 || rx.getFunctionCode() == 0x02) {
			// ����ڵ� 01, 02 ���� ��쿡�� ������ 8�� �̸����� ��û�Ͽ��� ������ ����Ʈ ������ �о ��Ʈ���� �ϱ⶧���� ��� ���̺� �� ������ ������ �����Ѵ�
			// ������� ���� 3������ ��û�Ͽ��� ����Ʈ ������ �о� 8bit�� ǥ���Ѵ�.		
			// ���� 3���� ��û������ ���� 8���� ǥ�����־ �� ���� ������ ������ ����� ������ 3���� ��û�ϰ� ���� 8��Ʈ �� 3���� ���������� ǥ�õǰ�
			// ������ 5�� ��Ʈ�� ���ؼ��� ��� OFF ���� �ֱ⶧���� ��û�� ������ŭ ���� ǥ�����ֱ�� �����ߴ�
			tableRow = rx.getTxInfo().getRequestCount();
		}else {
			// FC 03 , 04
			tableRow = rx.getPerfInfo().length;
		}
				
		if(isRTU) {
			// Modbus RTU : ���̺��� ������ ���� CRC ������ ǥ�����ֱ� ���ؼ� ���� �������� ���� �Ѱ� �� ������ ����			
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
					
					// �޺��ڽ����� ���� ���� ���� �� "TWO BYTE INT SIGNED" �� �ν�
					case "" : dataTypeComboBox.setSelectedIndex(6); /* Default */ value = DataType.init_TWO_BYTE_INT_SIGNED(rx); break;
					default : break;
				}
			}
								
		
			// ���̺� ���ڵ带 �ʱ�ȭ
			for (int i = 0; i < tableRow; i++) {
				content[i] = new Object[4];
				content[i][0] = new Integer(i + 1); // �� ��
				content[i][1] = String.format("0x%04X", rx.getPerfInfo()[i].getRegisterAddress()); // �������� �ּ�
				content[i][2] = Integer.parseInt(String.format("%s%04d", rx.getModbusAddress(), rx.getPerfInfo()[i].getRegisterAddress() + 1)); // ������ �ּ�
								
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
				
				content[i][3] = value[i]; // ��
			}
					
			if(isRTU) {
				// Modbus RTU : ���̺��� ������ ���� CRC ���� �߰�
				content[tableRow] = new Object[4];
				content[tableRow][0] = "CRC16";
				content[tableRow][1] = "---";
				content[tableRow][2] = "---";
				content[tableRow][3] = String.format("0x%04X", rx.getCrc()& 0xffff );
			}
						
			table.setModel(new DefaultTableModel(
					content,
					new String[] {
						"\uC21C \uC11C", "Register", "Modbus", rx.getCommandType()
						// ���� , �������� ��
					}
			) {
				// ���̺� �� ���� ���� ����
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
	
	
	// ���� ��û ��Ŷ ���� ���� ��ȿ�� Ȯ��
	public boolean checkReadRequestForm(boolean isRTU) {
		boolean isValid = true;				
		int nullCount = 0;
		int invalidCount = 0;
				
		if(startAddress_text.getText().length() == 0 
			|| timeout_text.getText().length() == 0 
			|| interval_text.getText().length() == 0
			|| (!isRTU && transactionId_text.getText().length() == 0)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>�Է� �ʵ� ��� ����</font>\n");
			
			// Ʈ����� ID null �˻�
			if(!isRTU && transactionId_text.getText().length() == 0) {
				nullCount++;
				sb.append(Util.colorBlue("Ʈ����� ID"));					
			}
			
			
			// ���� �ּ� null �˻�
			if(startAddress_text.getText().length() == 0) {
				if(nullCount > 0)
					sb.append(Util.colorBlue(", ����͸� �����ּ�"));
				else						
					sb.append(Util.colorBlue("����͸� �����ּ�"));
				
				nullCount++;
			}
			
			// Ÿ�Ӿƿ� null �˻�
			if(timeout_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", Ÿ�Ӿƿ�"));
				else						
					sb.append(Util.colorBlue("Ÿ�Ӿƿ�"));
				
				nullCount++;
			}
			
			// �˻簣�� null �˻�
			if(interval_text.getText().length() == 0) {					
				if(nullCount > 0)
					sb.append(Util.colorBlue(", �����ֱ�"));
				else
					sb.append(Util.colorBlue("�����ֱ�"));
				
				nullCount++;
			}
			
			sb.append(" ������ �Է� ���ּ���" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;			
			
			return isValid;
		}
		
		// ��ȿ���� ���� startAddress �Է� �� �޽��� ��� �� ����
		if(startAddress_text.getForeground() == Color.RED 
			|| timeout_text.getForeground() == Color.RED
			|| interval_text.getForeground() == Color.RED
			|| (!isRTU && transactionId_text.getForeground() == Color.RED)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>�Է� �ʵ� ��� ����</font>\n");
			sb.append("�Է��Ͻ� ");								
			
			// �����ּ� ��� �˻�
			if(!isRTU && transactionId_text.getForeground() == Color.RED) {
				invalidCount++;
				sb.append(Util.colorBlue("Ʈ����� ID"));
			}
			
			// �����ּ� ��� �˻�
			if(startAddress_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", ����͸� �����ּ�"));
				else
					sb.append(Util.colorBlue("����͸� �����ּ�"));
				
				invalidCount++;
			}
			
			// Ÿ�Ӿƿ� ��� �˻�			
			if(timeout_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", Ÿ�Ӿƿ�"));
				else
					sb.append(Util.colorBlue("Ÿ�Ӿƿ�"));
				
				invalidCount++;
			}
			
			// �˻簣�� ��� �˻�				
			if(interval_text.getForeground() == Color.RED) {
				if(invalidCount > 0)
					sb.append(Util.colorBlue(", �����ֱ�"));
				else
					sb.append(Util.colorBlue("�����ֱ�"));
				
				invalidCount++;
			}
							
			sb.append(" ������ Ȯ�� ���ּ���" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			isValid = false;					
			
			return isValid;
		}
		
		return isValid;
	}
	
		
		
	// ���� ��û TX �ʱ�ȭ
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("��", "").trim());
					functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
					if(startAddress_text.getText().trim().startsWith("0x")){
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
					}else if(startAddress_text.getText().trim().startsWith("0X")) {
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
					}else {
						startAddress = Integer.parseInt(startAddress_text.getText());
					}														
					requestCount = Integer.parseInt((String)requestCount_comboBox.getSelectedItem().toString().replaceAll("��", "").trim());					
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("��", "").trim());
					functionCode = Integer.parseInt((String)functionCode_comboBox.getSelectedItem().toString().trim());							
					if(startAddress_text.getText().trim().startsWith("0x")){
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0x", ""),16);								
					}else if(startAddress_text.getText().trim().startsWith("0X")) {
						startAddress = Integer.parseInt(startAddress_text.getText().trim().replaceAll("0X", ""),16);
					}else {
						startAddress = Integer.parseInt(startAddress_text.getText());
					}														
					requestCount = Integer.parseInt((String)requestCount_comboBox.getSelectedItem().toString().replaceAll("��", "").trim());					
					tx = new TX_Generator().generateReadTCP(transactionId, 0x0000, 0x0006, unitId, functionCode, startAddress, requestCount);					
					return tx;
			}
		}
		catch(Exception e) {
			// TX �ʱ�ȭ �� ���ܹ߻� �� null ����
			return null;
		}		
	}
	
	
	// ����ڵ� ���뿡���� ������ Ÿ�� �޺��ڽ��� ǥ��
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
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// ���̺� �� ũ�� ����
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // �������� �ּ�
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // ������ �ּ�
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // ��ĵ ���
				
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �������� �ּ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ������ �ּ�
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // ���
	}
		
}

