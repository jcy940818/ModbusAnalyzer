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

	// ���� ��� Ȱ��ȭ ��ư
	private static JRadioButton writeCommand_radioButton;
	
	// Ŭ���̾�Ʈ ����
	public static Socket socket_en = ModbusAgent.clientSocket;
	public static String IP;
	public static int PORT;	
	
	// information Component
	JPanel infoPanel; // Ŭ���̾�Ʈ ������ ������ ���� �� �����϶��� �������̼� ������Ʈ���� Ȱ��ȭ ��Ų��.  
	JPanel inputFormPanel;
	JPanel typePanel;
	JPanel inputTypePanel;
	JPanel resultPanel;
	JPanel dataTypePanel;
	JPanel imagePanel; /* ONION Image */
	
	private JButton connectButton; // ���� ���� �Է¹�ư (�߿�)
	private static boolean isRTU = false; // Default : Modbus TCP (���� �߿��� ����)
	public static JComboBox dataTypeComboBox = null;
	public static TX_Info global_tx = null;
	public static RX_Info global_rx = null;
	public static JTable table;
	private static JLabel currentState;
	private static JLabel modbusAddress_label;
	private JTextField TXinputTextField;
	
	// TX Form ���� ���� ������Ʈ
	private CardLayout inputPanel_layout;
	private JTextField transactionId_text; // Modbus TCP : TransactionID �ʵ�
	private JComboBox unitId_comboBox; // ����ȣ �޺��ڽ�
	private JComboBox functionCode_comboBox; // ����ڵ� �ʵ�
	private JTextField startAddress_text; // �����ּ�, �����ּ� �ʵ�
	private JComboBox requestCount_comboBox; // ��û����
	private JTextField controlValue; // ���� �� �ʵ� (FunctionCode : 06)
	private JRadioButton controlStatus_ON_Button; // ���� ���� �� ON �ʵ� (FunctionCode : 05)
	private JRadioButton controlStatus_OFF_Button; // ���� ���� �� OFF �ʵ� (FunctionCode : 05)	
	private JRadioButton readCommand_radioButton; 
	private JButton user_sendPacketButton;
	private JButton form_sendPacketButton;		
	private JRadioButton formInput_radioButton;
	
	// ���ǽ� ���� ������Ʈ (����� ���� �� ��� ����)
	private static JLabel user_expression_label;
	private static JLabel form_expression_label;
	private static JTextField form_expression_textField;
	private static JTextField user_expression_textField;
	
	// �ʱ�ȭ ��ư : Ŭ���̾�Ʈ ���� ���ӿ� �����ϸ� ������Ʈ ������� ��� �ʱ�ȭ�Ѵ�
	private static JButton user_resetButton;
	private static JButton form_resetButton;
	
	// ��� ���
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
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����				
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
		
		// ���̺� ���� �κ�
		table = new JTable();
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // ���� Ŭ��
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// ���� ��ư ���� Ŭ��
					if (ONION_Info.onionLogin && CustomModbusXmlGeneratorFrame.isExist) {
						int[] selectedIndex = ModbusAgent_Panel.table.getSelectedRows();				
						Perf[] perfs = Perf.getCustomModbusPerfs(ModbusAgent_Panel.table, selectedIndex);			 
						if(perfs == null) return;			
						CustomModbusXmlGeneratorFrame.addRecord(perfs);
					}
				}
				if (e.getButton() == 3) {
					// ������ Ŭ��
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
				
				// Ŭ���̾�Ʈ ������ ������ Ŀ�ؼ� ����
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
						// ���� ����Ǿ��ִ� ������ ��� ������ ���� �������� �õ��Ѵ�.						
						// Ŭ���̾�Ʈ ���� : ���� ����
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
					// ���� ���� : ������Ʈ ������� ��� �ʱ�ȭ�Ѵ�	
					ModbusAgent.clientSocket = socket_en;
					src_ko.agent.ModbusAgent.clientSocket = socket_en;
					
					panel_ON();
					
					// ������ Ŀ�ؼ� ������ �ٸ� ������ ������  ������ ������Ʈ �ʱ�ȭ
					if(!ClientSocket.getSimpleConnectedInfo().equalsIgnoreCase(lastConnectionInfo)) {
						componentAllClear();
						src_ko.swing.ModbusAgent_Panel.componentAllClear();
					}
					
					// ����ڰ� �Է��� IP, port�� Ŭ���̾�Ʈ ������ ������ ���� ���� ������ ����					
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
		
		dataTypePanel.setVisible(false); // functionCode 3, 4 �϶��� ������ Ÿ�� �޺��ڽ� ǥ�� (functionCdoe 1, 2 : ON/OFF �����̱� ������)		
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
		
		// �ʱ�ȭ ��ư
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
		
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		// TX user ���� ���� ��ư
		user_sendPacketButton = new JButton("Send");
		user_sendPacketButton.setFocusPainted(false);
		// ���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ
		user_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Mdobus TCP, RTU Common				
				
				// �ؽ�Ʈ �ʵ忡 �м� �� ��Ŷ ������ �Է����� �ʰ� �м� ��ư Ŭ�� �� ���� ��� �� ����
				if((TXinputTextField.getText() == null)|| (TXinputTextField.getText().length() == 0)) {
					Util.showMessage("<font color='red'>There's no TX</font>\nThere's no TX input" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// ����ڿ��� �Է¹��� TX Pure Packet (��û ��Ŷ)
				tx = new TX_Info();
				tx.setContent(TXinputTextField.getText().replaceAll(" ", "").trim());
												
				rx = null;
								
				if(isRTU) {
					// Modbus RTU
					try {						
						tx = new TX_Analyzer().rtuAnalysis(tx);
						tx.setAgentType("ModbusAgent");
						
						// TX ���� �Է� ��Ŀ����� ���� ��Ŷ ������ �Ұ����ϴ� (�ǵ����� ���� ��� �����ϱ� ���ؼ�)
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
						
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX�� RX ���� �� �� ��� �� �޽����� �ִٸ� ��� �� ����
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length �˻�
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();
						System.out.println("Modbus RTU Info init Error : " + exception.getMessage());						
					}
										
					if(rx.isRead()) {
						// Modbus RTU : ���� ��ɾ� �м�
															
						// ����ڵ尡 3, 4 �϶��� ������ Ÿ�� ���� �޺��ڽ��� ǥ��
						setDataType(rx);																																																							
																																		
						// RX�� ���� ������ ���
						if(rx.isException()) printExceptionContent(rx);						
																		
						// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();						
					}else {
						// Modbus RTU : ���� ��ɾ� �м�
						// �ش� ������ ���� �� ���� �������� ���Ŀ� ���ο� ����� �߰� �� �� �����Ƿ� ���ܳ���
						canNotControl();
						return;
					}
					
				}else {
					
					// Modbus TCP
					try {						
						tx = new TX_Analyzer().tcpAnalysis(tx);			
						tx.setAgentType("ModbusAgent");
						
						// TX ���� �Է� ��Ŀ����� ���� ��Ŷ ������ �Ұ����ϴ� (�ǵ����� ���� ��� �����ϱ� ���ؼ�)
						if(tx.getFunctionCode() == 0x05 || tx.getFunctionCode() == 0x06) {
							canNotControl();
							return;
						}
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);
												
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX�� RX ���� �� �� ��� �� �޽����� �ִٸ� ��� �� ����
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length �˻�
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();
						System.out.println("Modbus RTU Info init Error : " + exception.getMessage());						
					}
										
					if(rx.isRead()) {
						// Modbus TCP : ���� ��ɾ� �м�
												
						// ����ڵ尡 3, 4 �϶��� ������ Ÿ�� ���� �޺��ڽ��� ǥ��
						setDataType(rx);																																																			
																																		
						// RX�� ���� ������ ���
						if(rx.isException()) printExceptionContent(rx);
																		
						// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);					
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();
					}else {
						// Modbus TCP : ���� ��ɾ� �м�
						// �ش� ������ ���� �� ���� �������� ���Ŀ� ���ο� ����� �߰� �� �� �����Ƿ� ���ܳ���
						canNotControl();
						return;						
					}					
																					
				}
				
				setExpressionTable(table);
				if(packetlog_Frame != null) packetlog_Frame.updateMessage(packetLog.getText());				
			}
						
		});
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		
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
					// 16���� �ּ� ǥ��� (0x0000)
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
					// �Ϲ� 10���� �ּ� ǥ���
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
					// 16���� �ּ� ǥ��� (0x0000)
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
					// �Ϲ� 10���� �ּ� ǥ���
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
				
				// ����ڵ� �޺��ڽ� ���� ����� ����ڿ��� ǥ������ ������ �����ּҸ� �������־�� �ϱ� ������
				// �����ּ� �ؽ�Ʈ�ʵ��� keyEvent�� �߻������ش�
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
				/* JTextField */ transactionId_text.setText("1"); // Modbus TCP : TransactionID �ʵ�
				transactionId_text.setForeground(Color.BLUE);
				/* JComboBox */ unitId_comboBox.setSelectedIndex(0); // ����ȣ �޺��ڽ�				
				/* JTextField */ startAddress_text.setText(null); // �����ּ�, �����ּ� �ʵ�
				/* JComboBox */ requestCount_comboBox.setSelectedIndex(0); // ��û ����				
				/* JTextField */ controlValue.setText(null); // ���� �� �ʵ� (FunctionCode : 06)
				/* JRadioButton */ controlStatus_ON_Button.setSelected(true); // ���� ���� �� ON �ʵ� (FunctionCode : 05)												
				modbusAddress_label.setText("Address preview");
				modbusAddress_label.setForeground(Color.DARK_GRAY);
				
				if(readCommand_radioButton.isSelected()) {
					/* JComboBox */ functionCode_comboBox.setSelectedIndex(2); // ����ڵ� �ʵ�	
				}else {
					/* JComboBox */ functionCode_comboBox.setSelectedIndex(1); // ����ڵ� �ʵ�
				}
				if(packetlog_Frame != null)packetlog_Frame.updateMessage(packetLog.getText());
				
				// �����ּҿ� ��Ŀ��
				startAddress_text.requestFocus();
			}						
		});
		form_InputPanel.add(form_resetButton);
		
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		// TX Form ���� ��ư
		form_sendPacketButton = new JButton("Send");
		form_sendPacketButton.setFocusPainted(false);
		// ���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ
		form_sendPacketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				// ����, ���� ��û���� �˻�
				if(readCommand_radioButton.isSelected()) {
					// ���� ��û ����
					
					// ���� ��û TX ������ �ʿ��� Form �� ������ ��� �ԷµǾ� �ִ��� üũ
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
						
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX�� RX ���� �� �� ��� �� �޽����� �ִٸ� ��� �� ����
						if(hasCompareMessage(sb)) {
							return;
						}else {
							// RX Length �˻�
							ExceptionProvider.checkRxLength(tx, rx);
						}
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();												
					}
										
					if(rx.isRead()) {
						// ���� ��ɾ� �м�
															
						// ����ڵ尡 3, 4 �϶��� ������ Ÿ�� ���� �޺��ڽ��� ǥ��
						setDataType(rx);																																																				
																																		
						// RX�� ���� ������ ���
						if(rx.isException()) printExceptionContent(rx);
																																							
						// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
						global_tx = tx;
						global_rx = rx;
						updateTable(table, rx);			
						ModbusAgent.isRTU = isRTU;
						ModbusAgent.lastFunctionCode = rx.getFunctionCode();
					}else {						
						resetTable(table); // ����� ǥ������ ���� ���� ������ ��� ���̺� ������ �ʱ�ȭ �Ѵ�.
						dataTypePanel.setVisible(false);											
						
						// readCommand_radioButton.isSelected() : ���� ��û
						// ���� ��û ���� ��ư�� ���õǾ��� ������ �⺻������ ���� �� �� ���� �����̴�.
						// ������� ���� ���ܳ��´�							
					}
								
				} else {
					// ���� ��û ����					
					// Mdobus TCP, RTU ���� ���� : ���� ��Ŷ ������ �ʿ��� Form ���� Ȯ��
					if(!checkControlForm()) return;				
					
					// ���� ��û TX ������ �ʿ��� Form �� ������ ��� �ԷµǾ� �ִ��� üũ
					if(!checkWriteRequestForm(isRTU)) return;			
																			
					try {						
						tx = initWriteTX(isRTU);			
						tx.setAgentType("ModbusAgent");
						
						rx = ModbusAgent.communicate(socket_en, tx, isRTU, ClientSocket.RESPONSE_TIMEOUT);
						
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						// TX�� RX ���� �� �� ��� �� �޽����� �ִٸ� ��� �� ����
						if(hasCompareMessage(sb)) return;
						
					}catch(Exception exception) {
						resetTable(table);
						exception.printStackTrace();						
					}	
					
					// ���� ���� ���� ���
					controlWasSuccessful(tx, rx);													
					setDataType(rx);							
				}						
				
				setExpressionTable(table);
				if(packetlog_Frame != null) packetlog_Frame.updateMessage(packetLog.getText());
			}			
		}); // end formSendPacketButton Action
		// �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		form_sendPacketButton.setForeground(Color.BLACK);
		form_sendPacketButton.setFont(FontManager.getFont(Font.BOLD, 14));
		form_sendPacketButton.setBackground(Color.WHITE);
		form_sendPacketButton.setBounds(656, 31, 77, 31);		
		form_InputPanel.add(form_sendPacketButton);
		
		// ���� ��ɾ� ���� ��ư
		readCommand_radioButton = new JRadioButton("Read");
		readCommand_radioButton.setForeground(Color.BLACK);
		readCommand_radioButton.setFocusPainted(false);
		readCommand_radioButton.setSelected(true);
		readCommand_radioButton.setBackground(Color.WHITE);
		readCommand_radioButton.setFont(FontManager.getFont(Font.BOLD, 13));
		readCommand_radioButton.setBounds(8, 48, 61, 23);
		form_InputPanel.add(readCommand_radioButton);
		
		// ���� ��ɾ� ���� ��ư
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
		
		
		// TX ���� ��� ���� ��ư : ���� �Է� / ��� �Է�
		ActionListener commandType_radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				String type = b.getText();
				
				if(type.contains("Read")) {
					functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04"}));
					functionCode_comboBox.setSelectedIndex(2); // ���� �⺻ ����ڵ� : 03 (Read Holding Register)
					requestCount_comboBox.setSelectedIndex(0);
					startAddress_label.setText("Start Addr");
					requestCount_label.setText("Req Count");
					requestCount_comboBox.setVisible(true);
					controlStatus_ON_Button.setVisible(false);
					controlStatus_OFF_Button.setVisible(false);
					controlValue.setVisible(false);
				} else {
					functionCode_comboBox.setModel(new DefaultComboBoxModel(new String[] {"05", "06"}));
					functionCode_comboBox.setSelectedIndex(1); // ���� �⺻ ����ڵ� : 06 (Preset Single Register)
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
		
		// TX ���� ��� ���� ��ư : ���� �Է� / ��� �Է�
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
		
		// Modbus Ÿ���� TCP���� RTU������ �����ϴ� ���� ��ư �̺�Ʈ
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel2.setText(b.getText()); // ���� �Է� �ǳ�
				typeLabel.setText(b.getText()); // ��� �Է� �ǳ�
				
				// Modbus RTU, TCP ���� ��ư �̵� �� 
				// ������ Ÿ�� �ǳ� ����� , ������ Ÿ�� �޺��ڽ� ���� �ʱ�ȭ
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() ���� ȣ���				
				global_rx = null;
				modbusAddress_label.setText("Address preview");
				transactionId_text.setText(null);
				resetTable(table);
				
				// Modbus RTU, TCP ���� ��ư �̵� �� 
				// �ؽ�Ʈ �ʵ� �ʱ�ȭ
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
						
						// ModbusAgent <=> ExceptionScan : Socket ����ȭ
						socket_en = ModbusAgent.clientSocket;
						
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
				
		// �⺻ ��Ŷ ���� ��� : Modbus-RTU ��� �Է�
		formInput_radioButton.doClick();
		radio_modbusRTU.doClick();
		
	}// end ModbusAgent_Panel()
	
	
	public void panel_ON() {
		// ���� ������ �ǳ� ������Ʈ���� ������� �ʴ´�
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
		infoPanel.setBounds(12, 10, 1050, 489); // �������̼� �ǳ� ũ�� ����ȭ
		
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle(String.format("ModbusAnalyzer : %s", ClientSocket.getSimpleConnectedInfo()));
		}
	}
	
	
	public void panel_OFF() {
		// ���� ������ �ǳ� ������Ʈ���� ������� �ʴ´�
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
		infoPanel.setBounds(12, 10, 1050, 606); // �������̼� �ǳ� ��ü���
		
		if(packetlog_Frame != null) {
			packetlog_Frame.dispose();
			packetlog_Frame = null;
		}
		
		if (MainFrame.getMainFrame() != null) {
			MainFrame.getMainFrame().setTitle("ModbusAnalyzer");
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
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(30); // �������� �ּ�
		table.getColumnModel().getColumn(2).setPreferredWidth(30); // ������ �ּ�
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // ��
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
	}
	
	
	public static void updateTable(JTable table, RX_Info rx) {
		
		slotMap = new HashMap();
		
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
								
			int slotIndex = 1;
			
			// ���̺� ���ڵ带 �ʱ�ȭ
			for (int i = 0; i < tableRow; i++) {
				content[i] = new Object[4];
				content[i][0] = new Integer(i + 1); // �� ��
				content[i][1] = String.format("0x%04X", rx.getPerfInfo()[i].getRegisterAddress()); // �������� �ּ�
				content[i][2] = Integer.parseInt(String.format("%s%04d", rx.getModbusAddress(), rx.getPerfInfo()[i].getRegisterAddress() + 1)); // ������ �ּ� 
				content[i][3] = value[i]; // ��
				
				// Custom Modbus ���� ���� : Custom Modbus XML PerfCounter �ۼ��� ���ȴ�
				if(!value[i].toString().equalsIgnoreCase("---")) {
					slotMap.put(content[i][1].toString(), slotIndex++);
				}
				
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
						"index", "Register", "Modbus", rx.getCommandType()
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
		table.getColumnModel().getColumn(3).setPreferredWidth(120); // ��
				
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();

		// �ݺ����� �̿��Ͽ� ���̺��� ��� ���ķ� ����
//		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
//			tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);				
//		}					
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����		
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �������� �ּ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ������ �ּ�
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // �������� ��
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
	
	
	
	// ���� ��û ��Ŷ ���� ���� ��ȿ�� Ȯ��
	public boolean checkReadRequestForm(boolean isRTU) {
		boolean isValid = true;				
		int nullCount = 0;
		int invalidCount = 0;
				
		if(startAddress_text.getText().length() == 0 				
			|| (!isRTU && transactionId_text.getText().length() == 0)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
			
			// Ʈ����� ID null �˻�
			if(!isRTU && transactionId_text.getText().length() == 0) {
				nullCount++;
				sb.append(Util.colorBlue("Transaction ID"));					
			}
			
			
			// ���� �ּ� null �˻�
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
		
		// ��ȿ���� ���� startAddress �Է� �� �޽��� ��� �� ����
		if(startAddress_text.getForeground() == Color.RED 				
			|| (!isRTU && transactionId_text.getForeground() == Color.RED)) {
			
			StringBuilder sb = new StringBuilder("<font color='red'>TX input field form error</font>\n");
			sb.append("Please check the ");								
			
			// �����ּ� ��� �˻�
			if(!isRTU && transactionId_text.getForeground() == Color.RED) {
				invalidCount++;
				sb.append(Util.colorBlue("Transaction ID"));
			}
			
			// �����ּ� ��� �˻�
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
	
	
	
	
	// ���� ��û ��Ŷ ���� ���� ��ȿ�� Ȯ��
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
			
			// ��ȿ���� ���� startAddress �Է� �� �޽��� ��� �� ����
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
			
			// ��ȿ���� ���� TransactionId or startAddress �Է� �� �޽��� ��� �� ����
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
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
					unitId = Integer.parseInt((String)unitId_comboBox.getSelectedItem().toString().replaceAll("No. ", "").trim());
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
	
	
	
	
	// ���� ��û TX �ʱ�ȭ
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
			// ���� ó��
			return null;
		}
		
		return tx;
	}
	
	public void printException(RX_Info rx) {
		resetTable(table);
		Util.showMessage("Exception : " + rx.getExceptionContent(), JOptionPane.ERROR_MESSAGE);		
	}
	
	// ���ܳ��� ���
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
	
	
	
	// ��û��Ŷ, ������Ŷ �� ���  ���
	public boolean hasCompareMessage(StringBuilder sb) {
		if(sb != null) {
			resetTable(table);
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	// ���� �Ұ��� �޽��� ���
	public void canNotControl() {
		resetTable(table); // ����� ǥ������ ���� ���� ������ ��� ���̺� ������ �ʱ�ȭ �Ѵ�.
		dataTypePanel.setVisible(false);											
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Control packet transmission impossible</font>\n");
		msg.append("To prevent unintended control" + Util.separator + Util.separator + "\n\n");
		msg.append("Control packets can only be sent in a form input method" + Util.separator + Util.separator + "\n");
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);		
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
	
	
	
	// ���� �� ��ȿ�� üũ
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
	
	
	
	// ���� ��Ŷ�� tx(��û��Ŷ) �� rx(������Ŷ) �� pure Packet ������ ������ ���� �����̴�	
	public void controlWasSuccessful(TX_Info tx, RX_Info rx) {
		resetTable(table); // ����� ǥ������ ���� ���� ������ ��� ���̺� ������ �ʱ�ȭ �Ѵ�
		dataTypePanel.setVisible(false);
		
		if(tx.getContent().equalsIgnoreCase(rx.getContent())) {
			controlSuccess();
		}else {
			controlFail();
		}
	}
	
	
	
	// ���� ���� �޽��� ���
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
	
	
	// ���� ���� �޽��� ���
	public void controlFail() {
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Failed to Control</font>\n");
		msg.append("Have not received a response from the device for successful control" + Util.separator + Util.separator + "\n");														
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public void setExpressionTable(JTable table) {
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
		
		
		// Expression Check (���ǽ��� �����ϴ� ���� ǥ��)
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
		
		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);		
		scanCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �������� �ּ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ������ �ּ�
		tcmSchedule.getColumn(3).setCellRenderer(scanCellRenderer); // ��ĵ ���
	}


	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 
			// �̺�Ʈ �ߵ� ���� : ���콺 ������ Ŭ��
			if (e.getButton() == 3) {
				if (packetlog_Frame != null) {
					// ������ ���� �� ��Ŷ�α� �������� �����Ѵٸ� ����
					Util.showMessage("<font color='blue'>Modbus Client</font>\nAn existing packet log frame exists" + Util.longSeparator + "\n", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					// ���� ���� �� ��Ŷ�α� �������� ���ٸ� ���� ����
					String title = String.format("<html>Modbus Client : <font color='blue'>%s</font></html>",
							ClientSocket.getSimpleConnectedInfo());
					
					// MessageFrame.dispose() �������̵�
					packetlog_Frame = new MessageFrame(title, packetLog.getText()) {						
						public void dispose() {
							super.dispose();
							packetlog_Frame = null;
						}
						
					};
				}
			}			 
//			 if (e.getButton() == 1) {  } // ���� Ŭ��
//			 if (e.getButton() == 3) { } // ������ Ŭ��				
		}
	}
}
