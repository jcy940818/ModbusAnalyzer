package src_en.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_en.analyzer.TX.TX_Analyzer;
import src_en.info.TX_Info;
import src_en.util.Util;

public class TX_Analysis_Panel extends JPanel {
	
	private boolean isRTU = false; // Default : Modbus TCP
	private JTextField inputTextField; // �м��� �����͸� �Է��ϴ� �ʵ�
	private JButton analysisButton;
	/**
	 * Create the panel.
	 */
	public TX_Analysis_Panel(){
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
		
		JLabel currentFunction = new JLabel("TX Analysis");
		currentFunction.setForeground(Color.BLACK);
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����		
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 207, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBounds(10, 56, 1028, 425);
		infoPanel.add(resultPanel);
		resultPanel.setBackground(Color.LIGHT_GRAY);
		resultPanel.setLayout(null);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(12, 10, 638, 329);
		resultPanel.add(contentPanel);
		CardLayout contentPanel_CardLayout = new CardLayout(0,0);
		contentPanel.setLayout(contentPanel_CardLayout);
				
		JPanel Info_panel = new JPanel();
		contentPanel.add(Info_panel, "RTU_Info_panel");
		Info_panel.setBackground(Color.WHITE);
		Info_panel.setLayout(null);
		
		JLabel info_label0 = new JLabel("Modbus Type");
		info_label0.setForeground(Color.BLACK);
		info_label0.setHorizontalAlignment(SwingConstants.LEFT);
		info_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label0.setBounds(0, 0, 292, 40);
		Info_panel.add(info_label0);
		
		JLabel info_value_label0 = new JLabel();
		info_value_label0.setForeground(Color.BLACK);
		info_value_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		info_value_label0.setBounds(334, 0, 286, 40);
		Info_panel.add(info_value_label0);
		
		JLabel info_label1 = new JLabel("Command Type (Request Content)");
		info_label1.setForeground(Color.BLACK);
		info_label1.setHorizontalAlignment(SwingConstants.LEFT);
		info_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label1.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label1.setBounds(0, 42, 292, 40);
		Info_panel.add(info_label1);
		
		JLabel info_value_label1 = new JLabel();
		info_value_label1.setForeground(Color.BLACK);
		info_value_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_value_label1.setBounds(334, 42, 286, 40);
		Info_panel.add(info_value_label1);
		
		JLabel info_label2 = new JLabel("Transaction ID (Only Modbus-TCP)");
		info_label2.setForeground(Color.BLACK);
		info_label2.setBounds(0, 84, 292, 40);
		info_label2.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label2.setHorizontalAlignment(SwingConstants.LEFT);
		info_label2.setFont(FontManager.getFont(Font.BOLD, 15));		
		Info_panel.add(info_label2);
		
		JLabel info_value_label2 = new JLabel();
		info_value_label2.setForeground(Color.BLACK);
		info_value_label2.setBounds(334, 84, 286, 40);
		info_value_label2.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label2);
		
		JLabel info_label3 = new JLabel("Unit ID (Slave ID)");
		info_label3.setForeground(Color.BLACK);
		info_label3.setBounds(0, 124, 292, 40);
		info_label3.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label3.setHorizontalAlignment(SwingConstants.LEFT);
		info_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label3);
		
		JLabel info_value_label3 = new JLabel();
		info_value_label3.setForeground(Color.BLACK);
		info_value_label3.setBounds(334, 124, 286, 40);
		info_value_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label3);
		
		JLabel info_label4 = new JLabel("Function Code");
		info_label4.setForeground(Color.BLACK);
		info_label4.setBounds(0, 164, 292, 40);
		info_label4.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label4.setHorizontalAlignment(SwingConstants.LEFT);
		info_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label4);
		
		JLabel info_value_label4 = new JLabel();
		info_value_label4.setForeground(Color.BLACK);
		info_value_label4.setBounds(334, 164, 286, 40);
		info_value_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label4);
		
		JLabel info_label5 = new JLabel("Start Register Address");
		info_label5.setForeground(Color.BLACK);
		info_label5.setBounds(0, 205, 292, 40);
		info_label5.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label5.setHorizontalAlignment(SwingConstants.LEFT);
		info_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label5);
		
		JLabel info_value_label5 = new JLabel();
		info_value_label5.setForeground(Color.BLACK);
		info_value_label5.setBounds(334, 205, 286, 40);
		info_value_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label5);
		
		JLabel info_label6 = new JLabel("Start Modbus Address");
		info_label6.setForeground(Color.BLACK);
		info_label6.setBounds(0, 245, 292, 40);
		info_label6.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label6.setHorizontalAlignment(SwingConstants.LEFT);
		info_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label6);
		
		JLabel info_value_label6 = new JLabel();
		info_value_label6.setForeground(Color.BLACK);
		info_value_label6.setBounds(334, 245, 286, 40);
		info_value_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label6);
		
		JLabel info_label7 = new JLabel("Request Count");
		info_label7.setForeground(Color.BLACK);
		info_label7.setBounds(0, 280, 292, 44);
		info_label7.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label7.setHorizontalAlignment(SwingConstants.LEFT);
		info_label7.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label7);
		
		JLabel info_value_label7 = new JLabel();
		info_value_label7.setForeground(Color.BLACK);
		info_value_label7.setBounds(334, 280, 286, 44);
		info_value_label7.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label7);
		
		JPanel summaryPanel = new JPanel();
		summaryPanel.setBackground(Color.WHITE);
		summaryPanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		summaryPanel.setBounds(12, 349, 1004, 66);
		resultPanel.add(summaryPanel);
		summaryPanel.setLayout(new CardLayout(0, 0));
		
		JPanel summary_panel = new JPanel();
		summary_panel.setBackground(Color.WHITE);
		summaryPanel.add(summary_panel, "name_78046202148100");
		summary_panel.setLayout(null);
		
		JLabel summary_label0 = new JLabel("Register Address Based Summary");
		summary_label0.setForeground(Color.BLACK);
		summary_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		summary_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		summary_label0.setBounds(0, 0, 998, 28);
		summary_panel.add(summary_label0);
		
		JLabel summary_label1 = new JLabel("Modbus Address Based Summary");
		summary_label1.setForeground(Color.BLACK);
		summary_label1.setBorder(new EmptyBorder(0, 10, 0, 0));
		summary_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		summary_label1.setBounds(0, 30, 998, 28);
		summary_panel.add(summary_label1);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBounds(211, 509, 851, 107);
		actualPanel.add(inputPanel);
		inputPanel.setBackground(Color.WHITE);
		inputPanel.setLayout(null);
		
		JLabel typeLabel = new JLabel("Modbus TCP");
		typeLabel.setForeground(Color.BLACK);
		typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		typeLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		typeLabel.setBounds(12, 10, 129, 31);
		inputPanel.add(typeLabel);
		
		JLabel TX = new JLabel("TX");
		TX.setForeground(Color.BLACK);
		TX.setHorizontalAlignment(SwingConstants.LEFT);
		TX.setFont(FontManager.getFont(Font.BOLD, 16));
		TX.setBounds(11, 60, 26, 31);
		inputPanel.add(TX);
		
		inputTextField = new JTextField();
		inputTextField.setForeground(Color.BLACK);
		inputTextField.setBorder(UIManager.getBorder("TextField.border"));		
		inputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		inputTextField.setBounds(38, 62, 640, 31);
		inputTextField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				analysisButton.doClick();
			}
		});
		inputTextField.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent e) {
				inputTextField.setBorder(UIManager.getBorder("TextField.border"));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				inputTextField.setBorder(new LineBorder(new Color(255, 140, 0), 3));			
			}
		});
		inputPanel.add(inputTextField);
		inputTextField.setColumns(10);
		
		// �ʱ�ȭ ��ư
		JButton resetButton = new JButton("Reset");
		resetButton.setFocusPainted(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inputTextField.setText(null);
				
				// JLabel�� �ؽ�Ʈ�� �ʱ�ȭ
				resetLabel(isRTU ,
						new JLabel[] {info_label0, info_value_label0,												
						info_label1, info_value_label1,
						info_label2, info_value_label2,
						info_label3, info_value_label3,
						info_label4, info_value_label4,
						info_label5, info_value_label5,
						info_label6, info_value_label6,
						info_label7, info_value_label7,
						summary_label0, summary_label1}
						);
			}
		});
		
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 12));
		resetButton.setBounds(685, 60, 67, 31);
		inputPanel.add(resetButton);
		
		// �м� ��ư
		analysisButton = new JButton("Analysis");
		analysisButton.setFocusPainted(false);
		
		analysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TX_Info tx = new TX_Info();				
				tx.setContent(inputTextField.getText().replaceAll(" ", "").trim());				
												
				if(isRTU) {
					try {
						tx = new TX_Analyzer().rtuAnalysis(tx);
						if(tx == null) resetButton.doClick();
					}catch(Exception exception) {
						exception.printStackTrace();
						System.out.println(exception.getMessage());
					}
					
					if(tx.isRead()) {
						// Modbus RTU : ���� ��ɾ� �м�
																		
						// ������ Ÿ�� (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// ��û ����  (���� ���� ��û, �������� �� ��û..)
						info_label1.setText("Command Type (Request Content)");
						info_value_label1.setText(String.format("%s Request", tx.getCommandType()));
						
						// ��� ��ȣ
						info_label2.setText("Unit ID (Slave ID)");
						info_value_label2.setText(String.format("Device No. %d", tx.getUnitId()));
						
						// ��� �ڵ�
						info_label3.setText("Function Code");
						info_value_label3.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// ���� �������� �ּ�
						info_label4.setText("Start Register Address");
						info_value_label4.setText(String.format("0x%04x (Based on Register Address)", tx.getStartAddress()&0xffff ));											
						
						// ���� ������ �ּ�
						info_label5.setText("Start Modbus Address");
						info_value_label5.setText(String.format("%s%04d (Based on Modbus Address)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// ��û ����						
						info_label6.setText("Request Count");
						info_value_label6.setText(String.format("%d Requests", tx.getRequestCount()));
						
						// CRC
						info_label7.setText("CRC Code (Only Modbus-RTU)");
						info_value_label7.setText(String.format("0x%04X", tx.getCrc()&0xffff ));
						
						// ���� TX ���
//						summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x�������� 0x%04x�������� %s %d�� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getCommandType() ,tx.getRequestCount()));
						summary_label0.setText(String.format("Requested %d %s from 0x%04x to 0x%04x Register addresses of Device No. %d", tx.getRequestCount(), tx.getCommandType(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getUnitId()));
						
//						summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d�������� %s%04d�������� %s %d�� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getCommandType() ,tx.getRequestCount()));						
						summary_label1.setText(String.format("Requested %d %s from %s%04d to %s%04d Modbus addresses of Device No. %d", tx.getRequestCount(), tx.getCommandType(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getUnitId()));						
						
					}else {
						// Modbus RTU : ���� ��ɾ� �м�
						
						// ������ Ÿ�� (Modbus TCP / Modbus RTU)
//						info_label0.setText("Modbus Type(������ Ÿ��)");
						info_label0.setText("Modbus Type");
						
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// ��û ����  (���� ���� ��û, �������� �� ��û..)
//						info_label1.setText("Command Type(��û ����)");
						info_label1.setText("Command Type (Request Content)");
						
//						info_value_label1.setText(String.format("%s ��û", tx.getCommandType()));
						info_value_label1.setText(String.format("%s Request", tx.getCommandType()));
						
						// ��� ��ȣ
//						info_label2.setText("Unit ID(����ȣ)");
						info_label2.setText("Unit ID (Slave ID)");
						
//						info_value_label2.setText(String.format("%d�� ���", tx.getUnitId()));
						info_value_label2.setText(String.format("Device No. %d", tx.getUnitId()));
						
						// ��� �ڵ�
//						info_label3.setText("Function Code(����ڵ�)");
						info_label3.setText("Function Code");
						
						info_value_label3.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// ���� ��û�� �������� �ּ�
//						info_label4.setText("Control Register Address(���� �ּ�)");
						info_label4.setText("Control Register Address");
						
//						info_value_label4.setText(String.format("0x%04x (�������� �ּ�)", tx.getStartAddress()&0xffff ));												
						info_value_label4.setText(String.format("0x%04x (Based on Register Address)", tx.getStartAddress()&0xffff ));
						
						// ���� ��û�� ������ �ּ�
//						info_label5.setText("Control Modbus Address(���� �ּ�)");
						info_label5.setText("Control Modbus Address");
						
//						info_value_label5.setText(String.format("%s%04d (������ �ּ�)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						info_value_label5.setText(String.format("%s%04d (Based on Modbus Address)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// ���� ��û�� ��									
//						info_label6.setText((tx.getFunctionCode()==0x05)?"Control Status(��û�� ���� ����)":"Control Value(��û�� ���� ��)");
						info_label6.setText((tx.getFunctionCode()==0x05)?"The requested control status":"The requested control value");
						
						info_value_label6.setText(String.format("%s", (tx.getFunctionCode()==0x05)?tx.getControlStatus():String.format("%d", tx.getControlValue())));
						
						// CRC
						info_label7.setText("CRC Code (Only Modbus-RTU)");
						info_value_label7.setText(String.format("0x%04X", tx.getCrc()&0xffff ));
												
						// ���� TX ���
						if(tx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
//							summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x���� %s�� \"%s\"���� ���� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlStatus()));
//							summary_label0.setText(String.format("The %s of the Register address 0x%04x of Device No. %d is Controlled to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlStatus()));
							summary_label0.setText(String.format("A request for controlling a %s of the Register address 0x%04x of Device No. %d to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlStatus()));
							
//							summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d���� %s�� \"%s\"���� ���� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlStatus()));	
							summary_label1.setText(String.format("A request for controlling a %s of the Modbus address %s%04d of Device No. %d to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId() ,tx.getControlStatus()));
						}else {
							// functionCode == 0x06
//							summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x���� %s�� \"%d\"���� ���� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlValue()));
							summary_label0.setText(String.format("A request for controlling a %s of the Register address 0x%04x of Device No. %d to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlValue()));
							
//							summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d���� %s�� \"%d\"���� ���� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlValue()));					
							summary_label1.setText(String.format("A request for controlling a %s of the Modbus address %s%04d of Device No. %d to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId(), tx.getControlValue()));
					}						
						
					}
					
				}else {
					// Modbus TCP
					try {
						tx = new TX_Analyzer().tcpAnalysis(tx);
						if(tx == null) resetButton.doClick();
					}catch(Exception exception) {
						exception.printStackTrace();
						System.out.println(exception.getMessage());						
					}
					
					
					if(tx.isRead()) {
						// Modbus TCP : ���� ��ɾ� �м�																							
						
						// ������ Ÿ�� (Modbus TCP / Modbus RTU)
//						info_label0.setText("Modbus Type(������ Ÿ��)");
						info_label0.setText("Modbus Type");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// ��û ����  (���� ���� ��û, �������� �� ��û..)
//						info_label1.setText("Command Type(��û ����)");
						info_label1.setText("Command Type (Request Content)");
						
//						info_value_label1.setText(String.format("%s ��û", tx.getCommandType()));
						info_value_label1.setText(String.format("%s Request", tx.getCommandType()));
						
						// Transaction ID
//						info_label2.setText("Transaction ID(Ʈ����� ID)");
						info_label2.setText("Transaction ID (Only Modbus-TCP)");
						info_value_label2.setText(String.format("0x%04X", tx.getTransactionId()&0xffff ));
						
						// ��� ��ȣ
//						info_label3.setText("Unit ID(����ȣ)");
						info_label3.setText("Unit ID (Slave ID)");
						
//						info_value_label3.setText(String.format("%d�� ���", tx.getUnitId()));
						info_value_label3.setText(String.format("Device No. %d", tx.getUnitId()));
						
						// ��� �ڵ�
//						info_label4.setText("Function Code(����ڵ�)");
						info_label4.setText("Function Code");
						
						info_value_label4.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// ���� �������� �ּ�
//						info_label5.setText("Start Register Address(��û �����ּ�)");
						info_label5.setText("Start Register Address");
						
//						info_value_label5.setText(String.format("0x%04x (�������� �ּ�)", tx.getStartAddress()&0xffff ));												
						info_value_label5.setText(String.format("0x%04x (Based on Register Address)", tx.getStartAddress()&0xffff ));
						
						// ���� ������ �ּ�
//						info_label6.setText("Start Modbus Address(��û �����ּ�)");
						info_label6.setText("Start Modbus Address");
						
//						info_value_label6.setText(String.format("%s%04d (������ �ּ�)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						info_value_label6.setText(String.format("%s%04d (Based on Modbus Address)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// ��û ����						
//						info_label7.setText("Request Count(�������� ��û ����)");
						info_label7.setText("Request Count");
						
//						info_value_label7.setText(String.format("%d��", tx.getRequestCount()));
						info_value_label7.setText(String.format("%d Requests", tx.getRequestCount()));						
						
						
						// ���� TX ���
//						summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x�������� 0x%04x�������� %s %d�� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getCommandType() ,tx.getRequestCount()));
						summary_label0.setText(String.format("Requested %d %s from 0x%04x to 0x%04x Register addresses of Device No. %d", tx.getRequestCount(), tx.getCommandType(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getUnitId()));
						
//						summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d�������� %s%04d�������� %s %d�� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getCommandType() ,tx.getRequestCount()));
						summary_label1.setText(String.format("Requested %d %s from %s%04d to %s%04d Modbus addresses of Device No. %d", tx.getRequestCount(), tx.getCommandType(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getUnitId()));						
						
					}else {
						// Modbus TCP : ���� ��ɾ� �м�
						
						// ������ Ÿ�� (Modbus TCP / Modbus RTU)
//						info_label0.setText("Modbus Type(������ Ÿ��)");
						info_label0.setText("Modbus Type");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// ��û ����  (���� ���� ��û, �������� �� ��û..)
//						info_label1.setText("Command Type(��û ����)");
						info_label1.setText("Command Type (Request Content)");
						
//						info_value_label1.setText(String.format("%s ��û", tx.getCommandType()));
						info_value_label1.setText(String.format("%s Request", tx.getCommandType()));
						
						// Transaction ID
//						info_label2.setText("Transaction ID(Ʈ����� ID)");
						info_label2.setText("Transaction ID (Only Modbus-TCP)");
						info_value_label2.setText(String.format("0x%04X", tx.getTransactionId()&0xffff ));
						
						// ��� ��ȣ
//						info_label3.setText("Unit ID(����ȣ)");
						info_label3.setText("Unit ID (Slave ID)");
						
//						info_value_label3.setText(String.format("%d�� ���", tx.getUnitId()));
						info_value_label3.setText(String.format("Device No. %d", tx.getUnitId()));
						
						// ��� �ڵ�
//						info_label4.setText("Function Code(����ڵ�)");
						info_label4.setText("Function Code");
						
						info_value_label4.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// ���� ��û�� �������� �ּ�
//						info_label5.setText("Control Register Address(���� �ּ�)");
						info_label5.setText("Control Register Address");
						
//						info_value_label5.setText(String.format("0x%04x (�������� �ּ�)", tx.getStartAddress()&0xffff ));						
						info_value_label5.setText(String.format("0x%04x (Based on Register Address)", tx.getStartAddress()&0xffff ));
						
						// ���� ��û�� ������ �ּ�
//						info_label6.setText("Control Modbus Address(���� �ּ�)");
						info_label6.setText("Control Modbus Address");
						
//						info_value_label6.setText(String.format("%s%04d (������ �ּ�)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						info_value_label6.setText(String.format("%s%04d (Based on Modbus Address)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// ���� ��û�� ��									
//						info_label7.setText((tx.getFunctionCode()==0x05)?"Control Status(��û�� ���� ����)":"Control Value(��û�� ���� ��)");
						info_label7.setText((tx.getFunctionCode()==0x05)?"The requested control status":"The requested control value");
						info_value_label7.setText(String.format("%s", (tx.getFunctionCode()==0x05)?tx.getControlStatus():String.format("%d", tx.getControlValue())));
						
																		
						// ���� TX ���
						if(tx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
//							summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x���� %s�� \"%s\"���� ���� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlStatus()));
//							summary_label0.setText(String.format("The %s of the Register address 0x%04x of Device No. %d is Controlled to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlStatus()));
							summary_label0.setText(String.format("A request for controlling a %s of the Register address 0x%04x of Device No. %d to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlStatus()));
							
//							summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d���� %s�� \"%s\"���� ���� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlStatus()));	
//							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of Device No. %d is Controlled to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId() ,tx.getControlStatus()));
							summary_label1.setText(String.format("A request for controlling a %s of the Modbus address %s%04d of Device No. %d to be \"%s\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId() ,tx.getControlStatus()));
						}else {
							// functionCode == 0x06
//							summary_label0.setText(String.format("%d�� ����� �������� �ּ� 0x%04x���� %s�� \"%d\"���� ���� ��û", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlValue()));
//							summary_label0.setText(String.format("The %s of the Register address 0x%04x of Device No. %d is Controlled to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlValue()));
							summary_label0.setText(String.format("A request for controlling a %s of the Register address 0x%04x of Device No. %d to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getStartAddress()&0xffff, tx.getUnitId(), tx.getControlValue()));
							
//							summary_label1.setText(String.format("%d�� ����� ������ �ּ� %s%04d���� %s�� \"%d\"���� ���� ��û", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" ����", "") ,tx.getControlValue()));
//							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of Device No. %d is Controlled to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId(), tx.getControlValue()));
							summary_label1.setText(String.format("A request for controlling a %s of the Modbus address %s%04d of Device No. %d to be \"%d\"", tx.getCommandType().replaceAll(" Control", ""), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getUnitId(), tx.getControlValue()));
					}						
						
					}					
										
				}
			}
		});
		
		analysisButton.setForeground(Color.BLACK);
		analysisButton.setBackground(Color.WHITE);
		analysisButton.setFont(FontManager.getFont(Font.BOLD, 12));
		analysisButton.setBounds(758, 60, 83, 31);
		inputPanel.add(analysisButton);
		
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
				// inputTextField : ���� �Էµ� ��Ŷ���� �ʱ�ȭ
//				inputTextField.setText(null);
				
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel.setText(b.getText());
				
				if(b.getText().contains("RTU")) {
					isRTU = true;
					// JLabel�� �ؽ�Ʈ�� �ʱ�ȭ
					resetLabel(isRTU ,
							new JLabel[] {info_label0, info_value_label0,												
							info_label1, info_value_label1,
							info_label2, info_value_label2,
							info_label3, info_value_label3,
							info_label4, info_value_label4,
							info_label5, info_value_label5,
							info_label6, info_value_label6,
							info_label7, info_value_label7,
							summary_label0, summary_label1}
							);
				}else {
					isRTU = false;
					// JLabel�� �ؽ�Ʈ�� �ʱ�ȭ
					resetLabel(isRTU ,
							new JLabel[] {info_label0, info_value_label0,												
							info_label1, info_value_label1,
							info_label2, info_value_label2,
							info_label3, info_value_label3,
							info_label4, info_value_label4,
							info_label5, info_value_label5,
							info_label6, info_value_label6,
							info_label7, info_value_label7,
							summary_label0, summary_label1}
							);
				}
			}						
		};
		
		// ���� ��ư(TCP/RTU)�� ������ �߰�
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		// �⺻ ��� : Modbus-RTU
		radio_modbusRTU.doClick();
	}
	
	public static void resetLabel(boolean isRTU, JLabel... label) {
		if(isRTU) {
			// Modbus RTU : JLabel Text init
//			label[0].setText("Modbus Type(������ Ÿ��)");
			label[0].setText("Modbus Type");
			label[1].setText(null);
			
			// ��û ����  (���� ���� ��û, �������� �� ��û..)
//			label[2].setText("Command Type(��û ����)");
			label[2].setText("Command Type (Request Content)");
			label[3].setText(null);
			
			// ��� ��ȣ
//			label[4].setText("Unit ID(����ȣ)");
			label[4].setText("Unit ID (Slave ID)");
			label[5].setText(null);
			
			// ��� �ڵ�
//			label[6].setText("Function Code(����ڵ�)");
			label[6].setText("Function Code");
			label[7].setText(null);
			
			// ���� �������� �ּ�
//			label[8].setText("Start Register Address(��û �����ּ�)");
			label[8].setText("Start Register Address");
			label[9].setText(null);			
			
			// ���� ������ �ּ�
//			label[10].setText("Start Modbus Address(��û �����ּ�)");
			label[10].setText("Start Modbus Address");
			label[11].setText(null);
			
			// ��û ����						
//			label[12].setText("Request Count(�������� ��û ����)");
			label[12].setText("Request Count");
			label[13].setText(null);
			
			// CRC
//			label[14].setText("CRC Code");
			label[14].setText("CRC Code (Only Modbus-RTU)");
			label[15].setText(null);
			
			// Summary
//			label[16].setText("Register Address Based Summary (�������� �ּ� ���� ���)");
			label[16].setText("Register Address Based Summary");
			
//			label[17].setText("Modbus Address Based Summary (������ �ּ� ���� ���)");
			label[17].setText("Modbus Address Based Summary");
		}else {
			// Modbus TCP : JLabel Text init
			label[0].setText("Modbus Type");
			label[1].setText(null);
			
			// ��û ����  (���� ���� ��û, �������� �� ��û..)
//			label[2].setText("Command Type(��û ����)");
			label[2].setText("Command Type (Request Content)");
			label[3].setText(null);
			
			// Transaction ID
//			label[4].setText("Transaction ID(Ʈ����� ID)");
			label[4].setText("Transaction ID (Only Modbus-TCP)");
			label[5].setText(null);			
			
			// ��� ��ȣ
//			label[6].setText("Unit ID(����ȣ)");
			label[6].setText("Unit ID (Slave ID)");
			label[7].setText(null);
			
			// ��� �ڵ�
//			label[8].setText("Function Code(����ڵ�)");
			label[8].setText("Function Code");
			label[9].setText(null);
			
			// ���� �������� �ּ�
//			label[10].setText("Start Register Address(��û �����ּ�)");
			label[10].setText("Start Register Address");
			label[11].setText(null);			
			
			// ���� ������ �ּ�
//			label[12].setText("Start Modbus Address(��û �����ּ�)");
			label[12].setText("Start Modbus Address");
			label[13].setText(null);
			
			// ��û ����						
//			label[14].setText("Request Count(�������� ��û ����)");
			label[14].setText("Request Count");
			label[15].setText(null);
									
			// Summary
//			label[16].setText("Register Address Based Summary (�������� �ּ� ���� ���)");
			label[16].setText("Register Address Based Summary");
			
//			label[17].setText("Modbus Address Based Summary (������ �ּ� ���� ���)");
			label[17].setText("Modbus Address Based Summary");
		}
	}
	
}
