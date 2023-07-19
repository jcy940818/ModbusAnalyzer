package src_ko.swing;

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
import src_ko.analyzer.TX.TX_Analyzer;
import src_ko.info.TX_Info;
import src_ko.util.Util;

public class TX_Analysis_Panel extends JPanel {
	
	private boolean isRTU = false; // Default : Modbus TCP
	private JTextField inputTextField; // 분석할 데이터를 입력하는 필드
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
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자		
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
		contentPanel.setBounds(12, 10, 565, 329);
		resultPanel.add(contentPanel);
		CardLayout contentPanel_CardLayout = new CardLayout(0,0);
		contentPanel.setLayout(contentPanel_CardLayout);
				
		JPanel Info_panel = new JPanel();
		contentPanel.add(Info_panel, "RTU_Info_panel");
		Info_panel.setBackground(Color.WHITE);
		Info_panel.setLayout(null);
		
		JLabel info_label0 = new JLabel("Modbus Type(\uBAA8\uB4DC\uBC84\uC2A4 \uD0C0\uC785)");
		info_label0.setForeground(Color.BLACK);
		info_label0.setHorizontalAlignment(SwingConstants.LEFT);
		info_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label0.setBounds(0, 0, 292, 40);
		Info_panel.add(info_label0);
		
		JLabel info_value_label0 = new JLabel();
		info_value_label0.setForeground(Color.BLACK);
		info_value_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		info_value_label0.setBounds(334, 0, 225, 40);
		Info_panel.add(info_value_label0);
		
		JLabel info_label1 = new JLabel("Command Type(\uC694\uCCAD \uC885\uB958)");
		info_label1.setForeground(Color.BLACK);
		info_label1.setHorizontalAlignment(SwingConstants.LEFT);
		info_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label1.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label1.setBounds(0, 42, 292, 40);
		Info_panel.add(info_label1);
		
		JLabel info_value_label1 = new JLabel();
		info_value_label1.setForeground(Color.BLACK);
		info_value_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_value_label1.setBounds(334, 42, 225, 40);
		Info_panel.add(info_value_label1);
		
		JLabel info_label2 = new JLabel("Transaction ID(\uD2B8\uB79C\uC7AD\uC158 ID)");
		info_label2.setForeground(Color.BLACK);
		info_label2.setBounds(0, 84, 292, 40);
		info_label2.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label2.setHorizontalAlignment(SwingConstants.LEFT);
		info_label2.setFont(FontManager.getFont(Font.BOLD, 15));		
		Info_panel.add(info_label2);
		
		JLabel info_value_label2 = new JLabel();
		info_value_label2.setForeground(Color.BLACK);
		info_value_label2.setBounds(334, 84, 225, 40);
		info_value_label2.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label2);
		
		JLabel info_label3 = new JLabel("Unit ID(\uC7A5\uBE44\uBC88\uD638)");
		info_label3.setForeground(Color.BLACK);
		info_label3.setBounds(0, 124, 292, 40);
		info_label3.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label3.setHorizontalAlignment(SwingConstants.LEFT);
		info_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label3);
		
		JLabel info_value_label3 = new JLabel();
		info_value_label3.setForeground(Color.BLACK);
		info_value_label3.setBounds(334, 124, 225, 40);
		info_value_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label3);
		
		JLabel info_label4 = new JLabel("Function Code(\uAE30\uB2A5\uCF54\uB4DC)");
		info_label4.setForeground(Color.BLACK);
		info_label4.setBounds(0, 164, 292, 40);
		info_label4.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label4.setHorizontalAlignment(SwingConstants.LEFT);
		info_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label4);
		
		JLabel info_value_label4 = new JLabel();
		info_value_label4.setForeground(Color.BLACK);
		info_value_label4.setBounds(334, 164, 225, 40);
		info_value_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label4);
		
		JLabel info_label5 = new JLabel("Start Register Address(\uC694\uCCAD \uC2DC\uC791\uC8FC\uC18C)");
		info_label5.setForeground(Color.BLACK);
		info_label5.setBounds(0, 205, 292, 40);
		info_label5.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label5.setHorizontalAlignment(SwingConstants.LEFT);
		info_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label5);
		
		JLabel info_value_label5 = new JLabel();
		info_value_label5.setForeground(Color.BLACK);
		info_value_label5.setBounds(334, 205, 225, 40);
		info_value_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label5);
		
		JLabel info_label6 = new JLabel("Start Modbus Address(\uC694\uCCAD \uC2DC\uC791\uC8FC\uC18C)");
		info_label6.setForeground(Color.BLACK);
		info_label6.setBounds(0, 245, 292, 40);
		info_label6.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label6.setHorizontalAlignment(SwingConstants.LEFT);
		info_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label6);
		
		JLabel info_value_label6 = new JLabel();
		info_value_label6.setForeground(Color.BLACK);
		info_value_label6.setBounds(334, 245, 225, 40);
		info_value_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label6);
		
		JLabel info_label7 = new JLabel("Request Count(\uB808\uC9C0\uC2A4\uD130 \uC694\uCCAD \uAC1C\uC218)");
		info_label7.setForeground(Color.BLACK);
		info_label7.setBounds(0, 280, 292, 44);
		info_label7.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label7.setHorizontalAlignment(SwingConstants.LEFT);
		info_label7.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label7);
		
		JLabel info_value_label7 = new JLabel();
		info_value_label7.setForeground(Color.BLACK);
		info_value_label7.setBounds(334, 280, 225, 44);
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
		
		JLabel summary_label0 = new JLabel("Register Address Based Summary (\uB808\uC9C0\uC2A4\uD130 \uC8FC\uC18C \uAE30\uC900 \uC694\uC57D)");
		summary_label0.setForeground(Color.BLACK);
		summary_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		summary_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		summary_label0.setBounds(0, 0, 998, 28);
		summary_panel.add(summary_label0);
		
		JLabel summary_label1 = new JLabel("Modbus Address Based Summary (\uBAA8\uB4DC\uBC84\uC2A4 \uC8FC\uC18C \uAE30\uC900 \uC694\uC57D)");
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
		TX.setBounds(11, 53, 26, 31);
		inputPanel.add(TX);
		
		inputTextField = new JTextField();
		inputTextField.setForeground(Color.BLACK);
		inputTextField.setBorder(UIManager.getBorder("TextField.border"));		
		inputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		inputTextField.setBounds(38, 55, 640, 31);
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
		
		// 초기화 버튼
		JButton resetButton = new JButton("\uCD08\uAE30\uD654");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inputTextField.setText(null);
				
				// JLabel의 텍스트를 초기화
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
		resetButton.setFont(FontManager.getFont(Font.BOLD, 13));
		resetButton.setBounds(685, 53, 77, 31);
		inputPanel.add(resetButton);
		
		// 분석 버튼
		analysisButton = new JButton("\uBD84 \uC11D");
		
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
						// Modbus RTU : 수집 명령어 분석
																		
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type(모드버스 타입)");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Command Type(요청 종류)");
						info_value_label1.setText(String.format("%s 요청", tx.getCommandType()));
						
						// 장비 번호
						info_label2.setText("Unit ID(장비번호)");
						info_value_label2.setText(String.format("%d번 장비", tx.getUnitId()));
						
						// 기능 코드
						info_label3.setText("Function Code(기능코드)");
						info_value_label3.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// 시작 레지스터 주소
						info_label4.setText("Start Register Address(요청 시작주소)");
						info_value_label4.setText(String.format("0x%04x (레지스터 주소)", tx.getStartAddress()&0xffff ));											
						
						// 시작 모드버스 주소
						info_label5.setText("Start Modbus Address(요청 시작주소)");
						info_value_label5.setText(String.format("%s%04d (모드버스 주소)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// 요청 개수						
						info_label6.setText("Request Count(레지스터 요청 개수)");
						info_value_label6.setText(String.format("%d개", tx.getRequestCount()));
						
						// CRC
						info_label7.setText("CRC Code");
						info_value_label7.setText(String.format("0x%04x", tx.getCrc()&0xffff ));
						
						// 수집 TX 요약
						summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지부터 0x%04x번지까지 %s %d개 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getCommandType() ,tx.getRequestCount()));
						summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지부터 %s%04d번지까지 %s %d개 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getCommandType() ,tx.getRequestCount()));
						
					}else {
						// Modbus RTU : 제어 명령어 분석
						
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type(모드버스 타입)");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Command Type(요청 종류)");
						info_value_label1.setText(String.format("%s 요청", tx.getCommandType()));
						
						// 장비 번호
						info_label2.setText("Unit ID(장비번호)");
						info_value_label2.setText(String.format("%d번 장비", tx.getUnitId()));
						
						// 기능 코드
						info_label3.setText("Function Code(기능코드)");
						info_value_label3.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// 제어 요청한 레지스터 주소
						info_label4.setText("Control Register Address(제어 주소)");
						info_value_label4.setText(String.format("0x%04x (레지스터 주소)", tx.getStartAddress()&0xffff ));												
						
						// 제어 요청한 모드버스 주소
						info_label5.setText("Control Modbus Address(제어 주소)");
						info_value_label5.setText(String.format("%s%04d (모드버스 주소)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// 제어 요청한 값									
						info_label6.setText((tx.getFunctionCode()==0x05)?"Control Status(요청한 제어 상태)":"Control Value(요청한 제어 값)");
						info_value_label6.setText(String.format("%s", (tx.getFunctionCode()==0x05)?tx.getControlStatus():String.format("%d", tx.getControlValue())));
						
						// CRC
						info_label7.setText("CRC Code");
						info_value_label7.setText(String.format("0x%04x", tx.getCrc()&0xffff ));
												
						// 제어 TX 요약
						if(tx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s를 \"%s\"으로 제어 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlStatus()));
							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s를 \"%s\"으로 제어 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlStatus()));	
						}else {
							// functionCode == 0x06
							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s을 \"%d\"으로 제어 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlValue()));
							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s을 \"%d\"으로 제어 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlValue()));					
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
						// Modbus TCP : 수집 명령어 분석																							
						
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type(모드버스 타입)");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Command Type(요청 종류)");
						info_value_label1.setText(String.format("%s 요청", tx.getCommandType()));
						
						// Transaction ID
						info_label2.setText("Transaction ID(트랜잭션 ID)");
						info_value_label2.setText(String.format("0x%04x", tx.getTransactionId()&0xffff ));
						
						// 장비 번호
						info_label3.setText("Unit ID(장비번호)");
						info_value_label3.setText(String.format("%d번 장비", tx.getUnitId()));
						
						// 기능 코드
						info_label4.setText("Function Code(기능코드)");
						info_value_label4.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// 시작 레지스터 주소
						info_label5.setText("Start Register Address(요청 시작주소)");
						info_value_label5.setText(String.format("0x%04x (레지스터 주소)", tx.getStartAddress()&0xffff ));												
						
						// 시작 모드버스 주소
						info_label6.setText("Start Modbus Address(요청 시작주소)");
						info_value_label6.setText(String.format("%s%04d (모드버스 주소)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// 요청 개수						
						info_label7.setText("Request Count(레지스터 요청 개수)");
						info_value_label7.setText(String.format("%d개", tx.getRequestCount()));
												
						
						// 수집 TX 요약
						summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지부터 0x%04x번지까지 %s %d개 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getEndAddress()&0xffff, tx.getCommandType() ,tx.getRequestCount()));
						summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지부터 %s%04d번지까지 %s %d개 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getModbusAddress() ,(tx.getEndAddress()&0xffff)+1, tx.getCommandType() ,tx.getRequestCount()));
						
					}else {
						// Modbus TCP : 제어 명령어 분석
						
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type(모드버스 타입)");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Command Type(요청 종류)");
						info_value_label1.setText(String.format("%s 요청", tx.getCommandType()));
						
						// Transaction ID
						info_label2.setText("Transaction ID(트랜잭션 ID)");
						info_value_label2.setText(String.format("0x%04x", tx.getTransactionId()&0xffff ));
						
						// 장비 번호
						info_label3.setText("Unit ID(장비번호)");
						info_value_label3.setText(String.format("%d번 장비", tx.getUnitId()));
						
						// 기능 코드
						info_label4.setText("Function Code(기능코드)");
						info_value_label4.setText(String.format("0x%02x (%s)", tx.getFunctionCode(), tx.getFunctionContent()));
						
						// 제어 요청한 레지스터 주소
						info_label5.setText("Control Register Address(제어 주소)");
						info_value_label5.setText(String.format("0x%04x (레지스터 주소)", tx.getStartAddress()&0xffff ));						
						
						// 제어 요청한 모드버스 주소
						info_label6.setText("Control Modbus Address(제어 주소)");
						info_value_label6.setText(String.format("%s%04d (모드버스 주소)", tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1));
						
						// 제어 요청한 값									
						info_label7.setText((tx.getFunctionCode()==0x05)?"Control Status(요청한 제어 상태)":"Control Value(요청한 제어 값)");
						info_value_label7.setText(String.format("%s", (tx.getFunctionCode()==0x05)?tx.getControlStatus():String.format("%d", tx.getControlValue())));
						
																		
						// 제어 TX 요약
						if(tx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s를 \"%s\"으로 제어 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlStatus()));
							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s를 \"%s\"으로 제어 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlStatus()));	
						}else {
							// functionCode == 0x06
							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s을 \"%d\"으로 제어 요청", tx.getUnitId(), tx.getStartAddress()&0xffff, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlValue()));
							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s을 \"%d\"으로 제어 요청", tx.getUnitId(), tx.getModbusAddress(), (tx.getStartAddress()&0xffff)+1, tx.getCommandType().replaceAll(" 제어", "") ,tx.getControlValue()));					
					}						
						
					}					
										
				}
			}
		});
		
		analysisButton.setForeground(Color.BLACK);
		analysisButton.setBackground(Color.WHITE);
		analysisButton.setFont(FontManager.getFont(Font.BOLD, 13));
		analysisButton.setBounds(768, 53, 71, 31);
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
				
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// inputTextField : 기존 입력된 패킷정보 초기화
//				inputTextField.setText(null);
				
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel.setText(b.getText());
				
				if(b.getText().contains("RTU")) {
					isRTU = true;
					// JLabel의 텍스트를 초기화
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
					// JLabel의 텍스트를 초기화
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
		
		// 라디오 버튼(TCP/RTU)에 리스너 추가
		radio_modbusTCP.addActionListener(radioListener);
		radio_modbusRTU.addActionListener(radioListener);
		
		// 기본 모드 : Modbus-RTU
		radio_modbusRTU.doClick();
	}
	
	public static void resetLabel(boolean isRTU, JLabel... label) {
		if(isRTU) {
			// Modbus RTU : JLabel Text init
			label[0].setText("Modbus Type(모드버스 타입)");
			label[1].setText(null);
			
			// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
			label[2].setText("Command Type(요청 종류)");
			label[3].setText(null);
			
			// 장비 번호
			label[4].setText("Unit ID(장비번호)");
			label[5].setText(null);
			
			// 기능 코드
			label[6].setText("Function Code(기능코드)");
			label[7].setText(null);
			
			// 시작 레지스터 주소
			label[8].setText("Start Register Address(요청 시작주소)");
			label[9].setText(null);			
			
			// 시작 모드버스 주소
			label[10].setText("Start Modbus Address(요청 시작주소)");
			label[11].setText(null);
			
			// 요청 개수						
			label[12].setText("Request Count(레지스터 요청 개수)");
			label[13].setText(null);
			
			// CRC
			label[14].setText("CRC Code");
			label[15].setText(null);
			
			// Summary
			label[16].setText("Register Address Based Summary (레지스터 주소 기준 요약)");
			label[17].setText("Modbus Address Based Summary (모드버스 주소 기준 요약)");
		}else {
			// Modbus TCP : JLabel Text init
			label[0].setText("Modbus Type(모드버스 타입)");
			label[1].setText(null);
			
			// 요청 종류  (코일 상태 요청, 레지스터 값 요청..)
			label[2].setText("Command Type(요청 종류)");
			label[3].setText(null);
			
			// Transaction ID
			label[4].setText("Transaction ID(트랜잭션 ID)");
			label[5].setText(null);			
			
			// 장비 번호
			label[6].setText("Unit ID(장비번호)");
			label[7].setText(null);
			
			// 기능 코드
			label[8].setText("Function Code(기능코드)");
			label[9].setText(null);
			
			// 시작 레지스터 주소
			label[10].setText("Start Register Address(요청 시작주소)");
			label[11].setText(null);			
			
			// 시작 모드버스 주소
			label[12].setText("Start Modbus Address(요청 시작주소)");
			label[13].setText(null);
			
			// 요청 개수						
			label[14].setText("Request Count(레지스터 요청 개수)");
			label[15].setText(null);
									
			// Summary
			label[16].setText("Register Address Based Summary (레지스터 주소 기준 요약)");
			label[17].setText("Modbus Address Based Summary (모드버스 주소 기준 요약)");
		}
	}
	
}
