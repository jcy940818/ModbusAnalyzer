package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.analyzer.RX.RX_Analyzer;
import src_en.analyzer.TX.TX_Analyzer;
import src_en.info.RX_Info;
import src_en.info.TX_Info;
import src_en.util.ExceptionProvider;
import src_en.util.Util;

public class Multi_Analysis_Panel extends JPanel {
	
	private static boolean isRTU = false; // Default : Modbus TCP
	private static JComboBox dataTypeComboBox = null;
	private static RX_Info global_rx = null;
	private static JTable table;
	private JTextField TXinputTextField;
	private JTextField RXinputTextField;
	private JButton analysisButton;
	private static boolean userReset = true;
	
	// ���ǽ� ���� �ʵ�
	private static JLabel expression_label;
	private static JTextField expression_textField;
	
	/**
	 * Create the panel.
	 */
	public Multi_Analysis_Panel(){
		setBorder(new EmptyBorder(0, 0, 0, 0));
	
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(new Color(255, 140, 0));
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 489);
		actualPanel.add(infoPanel);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("TX-RX Analysis");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 228, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBounds(10, 56, 1028, 425);
		infoPanel.add(resultPanel);
		resultPanel.setBackground(Color.LIGHT_GRAY);
		resultPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setFont(FontManager.getFont(Font.PLAIN, 13));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(12, 10, 1004, 405);
		resultPanel.add(scrollPane);
		
		// ���̺� ���� �κ�
		table = new JTable();
		table.setBackground(Color.WHITE);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // ���� Ŭ��
				if (e.getButton() == 1 && e.getClickCount() == 2) {} // ���� ��ư ���� Ŭ��									
				if (e.getButton() == 3) {
					// ������ Ŭ��
					int column = table.columnAtPoint(e.getPoint());
					int row = table.rowAtPoint(e.getPoint());
					table.changeSelection(row, column, false, false);
					table.requestFocus();
					int[] selectedIndex = table.getSelectedRows();
					Perf.showBitStatus(table, selectedIndex, Multi_Analysis_Panel.dataTypeComboBox.getSelectedItem().toString());
				}
			}
		});
		resetTable(table);
		
		scrollPane.setViewportView(table);
		
		JPanel dataTypePanel = new JPanel();
		dataTypePanel.setBackground(Color.WHITE);
		dataTypePanel.setBounds(601, 10, 437, 39);
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
		dataTypePanel.setVisible(false); // functionCode 3, 4 �϶��� ������ Ÿ�� �޺��ڽ� ǥ�� (functionCdoe 1, 2 : ON/OFF �����̱� ������)
		
						
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBounds(211, 509, 851, 107);
		actualPanel.add(inputPanel);
		inputPanel.setBackground(Color.WHITE);
		inputPanel.setLayout(null);
		
		JLabel typeLabel = new JLabel("Modbus TCP");
		typeLabel.setForeground(Color.BLACK);
		typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		typeLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		typeLabel.setBounds(12, 0, 129, 31);
		inputPanel.add(typeLabel);
		
		JLabel TX = new JLabel("TX");
		TX.setForeground(Color.BLACK);
		TX.setHorizontalAlignment(SwingConstants.LEFT);
		TX.setFont(FontManager.getFont(Font.BOLD, 16));
		TX.setBounds(12, 30, 26, 31);
		inputPanel.add(TX);
		
		TXinputTextField = new JTextField();
		TXinputTextField.setForeground(Color.BLACK);
		TXinputTextField.setBorder(UIManager.getBorder("TextField.border"));		
		TXinputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		TXinputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		TXinputTextField.setBounds(39, 32, 640, 31);
		TXinputTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				analysisButton.doClick();
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
		inputPanel.add(TXinputTextField);
		TXinputTextField.setColumns(10);
		
		// �ʱ�ȭ ��ư
		JButton resetButton = new JButton("Reset");
		resetButton.setFocusPainted(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(userReset) {
					TXinputTextField.setText(null);
					RXinputTextField.setText(null);
					expression_textField.setText(null);
				}
				
				global_rx = null;
				dataTypePanel.setVisible(false);
				
				resetTable(table);				
				
				userReset = true;
			}
		});
		
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 12));
		resetButton.setBounds(685, 53, 67, 31);
		inputPanel.add(resetButton);
		
		// �м� ��ư
		analysisButton = new JButton("Analysis");
		analysisButton.setFocusPainted(false);
		// �м� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ
		analysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Mdobus TCP, RTU Common
				
				// �ؽ�Ʈ �ʵ忡 �м� �� ��Ŷ ������ �Է����� �ʰ� �м� ��ư Ŭ�� �� ���� ��� �� ����
				if((TXinputTextField.getText().length()==0)&&(RXinputTextField.getText().length()==0)) {
					Util.showMessage("There's no TX, RX content", JOptionPane.ERROR_MESSAGE);
					return;
				}else if((TXinputTextField.getText() == null)|| (TXinputTextField.getText().length() == 0)) {
					Util.showMessage("There's no TX content", JOptionPane.ERROR_MESSAGE);
					return;
				}else if((RXinputTextField.getText() == null)|| (RXinputTextField.getText().length() == 0)) {
					Util.showMessage("There's no RX content", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				TX_Info tx = new TX_Info();
				tx.setContent(TXinputTextField.getText().replaceAll(" ", "").trim());
												
				RX_Info rx = new RX_Info();
				rx.setContent(RXinputTextField.getText().replaceAll(" ", "").trim());								
				
				
				if(isRTU) {
					// Modbus RTU
					try {						
						tx = new TX_Analyzer().rtuAnalysis(tx);
						
						rx.setTxInfo(tx);
						rx = new RX_Analyzer().rtuAnalysis(rx);												
						
						if(rx.isCRCError()) {							
							StringBuilder msg = new StringBuilder();
							msg.append("<font color='red'>RX is Incorrect CRC</font>\n");
							msg.append(String.format("%s : 0x%04x%s\n\n",Util.colorRed("Actual Read Incorrect CRC") ,rx.getCrc() & 0xffff, Util.longSeparator));
							msg.append(String.format("%s : 0x%04x%s\n",Util.colorBlue("Expected Correct CRC") ,rx.getExpectedCrc() & 0xffff, Util.longSeparator));
							Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
							
							userReset = false;
							resetButton.doClick();
							return;
						}
						
						
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						if(sb != null) {
							resetTable(table);
							Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
																																																			
																																		
						// RX�� ���� ������ ���
						if(rx.isException()) printExceptionContent(rx);
																		
						// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
						global_rx = rx;
						updateTable(table, rx);												
					}else {
						// Modbus RTU : ���� ��ɾ� �м�
						
						resetTable(table); // ����� ǥ������ ���� ���� ������ ��� ���̺� ������ �ʱ�ȭ �Ѵ�.
						dataTypePanel.setVisible(false);
						Util.showMessage("TX-RX Analysis function does not support control packet analysis" + Util.separator + Util.separator, JOptionPane.ERROR_MESSAGE);
					}
					
				}else {
					
					// Modbus TCP
					try {						
						tx = new TX_Analyzer().tcpAnalysis(tx);
						
						rx.setTxInfo(tx);
						rx = new RX_Analyzer().tcpAnalysis(rx);
						
						// TX�� RX ���� �� (Ʈ�����ID, ����ȣ, ����ڵ�)
						StringBuilder sb = ExceptionProvider.CompareTxRx(tx, rx);
						
						if(sb != null) {
							resetTable(table);
							Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
																																																			
																																		
						// RX�� ���� ������ ���
						if(rx.isException()) printExceptionContent(rx);												
																		
						// updataTable() �� �Ѱ��� RX_Info �ν��Ͻ� ���� �ʱ�ȭ�� ������Ѵ�.
						global_rx = rx;
						updateTable(table, rx);												
					}else {
						// Modbus TCP : ���� ��ɾ� �м�
						
						resetTable(table); // ����� ǥ������ ���� ���� ������ ��� ���̺� ������ �ʱ�ȭ �Ѵ�.
						dataTypePanel.setVisible(false);
						Util.showMessage("TX-RX Analysis function does not support control packet analysis" + Util.separator + Util.separator, JOptionPane.ERROR_MESSAGE);
					}					
																					
				}
			}
						
		});
		
		analysisButton.setForeground(Color.BLACK);
		analysisButton.setBackground(Color.WHITE);
		analysisButton.setFont(FontManager.getFont(Font.BOLD, 12));
		analysisButton.setBounds(758, 53, 83, 31);
		inputPanel.add(analysisButton);
		
		JLabel RX = new JLabel("RX");
		RX.setForeground(Color.BLACK);
		RX.setHorizontalAlignment(SwingConstants.LEFT);
		RX.setFont(FontManager.getFont(Font.BOLD, 16));
		RX.setBounds(12, 68, 26, 31);
		inputPanel.add(RX);
		
		RXinputTextField = new JTextField();
		RXinputTextField.setForeground(Color.BLACK);
		RXinputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		RXinputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		RXinputTextField.setColumns(10);
		RXinputTextField.setBorder(UIManager.getBorder("TextField.border"));
		RXinputTextField.setBounds(39, 70, 640, 31);
		RXinputTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				analysisButton.doClick();
			}
		});
		RXinputTextField.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent e) {
				RXinputTextField.setBorder(UIManager.getBorder("TextField.border"));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				RXinputTextField.setBorder(new LineBorder(new Color(255, 140, 0), 3));			
			}
		});
		inputPanel.add(RXinputTextField);
		
		expression_label = new JLabel("Expression");
		expression_label.setForeground(new Color(0, 128, 0));
		expression_label.setHorizontalAlignment(SwingConstants.CENTER);
		expression_label.setFont(FontManager.getFont(Font.BOLD, 15));
		expression_label.setBackground(Color.WHITE);
		expression_label.setBounds(490, 7, 94, 18);
		expression_label.setVisible(false);
		inputPanel.add(expression_label);
		
		expression_textField = new JTextField();
		expression_textField.setForeground(Color.BLACK);
		expression_textField.setFont(FontManager.getFont(Font.BOLD, 13));
		expression_textField.setHorizontalAlignment(SwingConstants.LEFT);
		expression_textField.setBackground(Color.WHITE);
		expression_textField.setBounds(582, 7, 257, 21);
		expression_textField.setColumns(10);
		expression_textField.setVisible(false);
		expression_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExpressionTable(table);				
			}
		});		
		inputPanel.add(expression_textField);
		
		JPanel typePanel = new JPanel();
		typePanel.setBackground(Color.WHITE);
		typePanel.setBounds(12, 509, 187, 107);
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
				
		// Modbus Ÿ���� TCP���� RTU������ �����ϴ� ���� ��ư �̺�Ʈ
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel.setText(b.getText());		
				
				// Modbus RTU, TCP ���� ��ư �̵� �� 
				// ������ Ÿ�� �ǳ� ����� , ������ Ÿ�� �޺��ڽ� ���� �ʱ�ȭ
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() ���� ȣ���
				global_rx = null;
				resetTable(table);
				
				// Modbus RTU, TCP ���� ��ư �̵� �� 
				// �ؽ�Ʈ �ʵ� �ʱ�ȭ
//				TXinputTextField.setText(null);
//				RXinputTextField.setText(null);

				if (b.getText().contains("RTU")) {
					isRTU = true;
				} else {
					isRTU = false;
				}
				
				
			}						
		};
		
		// ���� ��ư(TCP/RTU)�� ������ �߰�
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		// �⺻ ��� : Modbus-RTU
		radio_modbusRTU.doClick();
	}
	
	public static void resetTable(JTable table){
		// ���̺� ��� ����
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
				"index", "Register Address", "Modbus Address", "Register Value"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(3); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // �������� �ּ�
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // ������ �ּ�
		table.getColumnModel().getColumn(3).setPreferredWidth(500); // ��
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
	}
	
	public static void activationExcpression() {
		expression_label.setVisible(true);
		expression_textField.setVisible(true);
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
			// ����ڵ� 01, 02 ���� ��쿡�� 8�� �̸����� ��û�Ͽ��� ������ ����Ʈ ������ �о ��Ʈ���� �ϱ⶧���� ���� ������ ���� �����Ѵ�
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
						"index", "Register", "Modbus", rx.getCommandType()
						// ���� , �������� ��
					}
			) {
				// ���̺� �� ���� ���� ����
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
		setExpressionTable(table);		
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
		table.getColumnModel().getColumn(0).setPreferredWidth(3);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(500);
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
							
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����		
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �������� �ּ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ������ �ּ�
	}
	
	public static void setExpressionTable(JTable table) {
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// ���̺� �� ũ�� ����
		table.getColumnModel().getColumn(0).setPreferredWidth(3);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(500);
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����		
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �������� �ּ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ������ �ּ�
		
		// Expression Check (���ǽ��� �����ϴ� ���� ǥ��)
		ScanCellRenderer scanCellRenderer = null;
		String expression = expression_textField.getText().toLowerCase();
		
		if(expression == null || expression.length() == 0 || expression.equalsIgnoreCase("") || !expression.contains("x")) {
			scanCellRenderer = new ScanCellRenderer();
		}else {
			expression = expression.toLowerCase();
			expression = expression.replace("X", "x");
			expression = expression.replace("and", "&&").replace("or", "||");
			expression = expression.replace("AND", "&&").replace("OR", "||");
			
			scanCellRenderer = new ScanCellRenderer(expression);
		}
		
		tcmSchedule.getColumn(3).setCellRenderer(scanCellRenderer); // ��ĵ ���
	}
	
}
