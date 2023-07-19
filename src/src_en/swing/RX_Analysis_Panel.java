package src_en.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
import src_en.analyzer.RX.DataType;
import src_en.analyzer.RX.RX_Analyzer;
import src_en.info.RX_Info;
import src_en.util.JavaScript;
import src_en.util.Util;

public class RX_Analysis_Panel extends JPanel {
	
	private static boolean isRTU = false; // Default : Modbus TCP
	private static JComboBox dataTypeComboBox = null;
	private static RX_Info global_rx = null;
	private static JTextField inputTextField; // 분석할 데이터를 입력하는 필드
	private static JTable table;
	private JButton analysisButton;
	private static boolean userReset = true;
	
	// 조건식 관련 필드
	private static JLabel expression_label;
	private static JTextField expression_textField;
	
	/**
	 * Create the panel.
	 */
	public RX_Analysis_Panel(){
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
		
		JLabel currentFunction = new JLabel("RX Analysis");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
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
		
		JLabel info_label0 = new JLabel("Packet Status (Exception Check)");
		info_label0.setForeground(Color.BLACK);
		info_label0.setHorizontalAlignment(SwingConstants.LEFT);
		info_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label0.setBounds(0, 0, 322, 40);
		Info_panel.add(info_label0);
		
		JLabel info_value_label0 = new JLabel();		
		info_value_label0.setForeground(Color.BLACK);
		info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);
		info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
		info_value_label0.setBounds(275, 0, 284, 40);
		Info_panel.add(info_value_label0);
		
		JLabel info_label1 = new JLabel("Modbus Type");
		info_label1.setForeground(Color.BLACK);
		info_label1.setHorizontalAlignment(SwingConstants.LEFT);
		info_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_label1.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label1.setBounds(0, 42, 322, 40);
		Info_panel.add(info_label1);
		
		JLabel info_value_label1 = new JLabel();
		info_value_label1.setForeground(Color.BLACK);
		info_value_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		info_value_label1.setBounds(275, 42, 284, 40);
		Info_panel.add(info_value_label1);
		
		JLabel info_label2 = new JLabel("Response Content");
		info_label2.setForeground(Color.BLACK);
		info_label2.setBounds(0, 84, 322, 40);
		info_label2.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label2.setHorizontalAlignment(SwingConstants.LEFT);
		info_label2.setFont(FontManager.getFont(Font.BOLD, 15));		
		Info_panel.add(info_label2);
		
		JLabel info_value_label2 = new JLabel();
		info_value_label2.setForeground(Color.BLACK);
		info_value_label2.setBounds(275, 84, 284, 40);
		info_value_label2.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label2);
		
		JLabel info_label3 = new JLabel("Transaction ID (Only Modbus-TCP)");
		info_label3.setForeground(Color.BLACK);
		info_label3.setBounds(0, 124, 322, 40);
		info_label3.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label3.setHorizontalAlignment(SwingConstants.LEFT);
		info_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label3);
		
		JLabel info_value_label3 = new JLabel();
		info_value_label3.setForeground(Color.BLACK);
		info_value_label3.setBounds(275, 124, 284, 40);
		info_value_label3.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label3);
		
		JLabel info_label4 = new JLabel("Unit ID (Slave ID)");
		info_label4.setForeground(Color.BLACK);
		info_label4.setBounds(0, 164, 322, 40);
		info_label4.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label4.setHorizontalAlignment(SwingConstants.LEFT);
		info_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label4);
		
		JLabel info_value_label4 = new JLabel();
		info_value_label4.setForeground(Color.BLACK);
		info_value_label4.setBounds(275, 164, 284, 40);
		info_value_label4.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label4);
		
		JLabel info_label5 = new JLabel("Function Code");
		info_label5.setForeground(Color.BLACK);
		info_label5.setBounds(0, 205, 322, 40);
		info_label5.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label5.setHorizontalAlignment(SwingConstants.LEFT);
		info_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label5);
		
		JLabel info_value_label5 = new JLabel();
		info_value_label5.setForeground(Color.BLACK);
		info_value_label5.setBounds(275, 205, 284, 40);
		info_value_label5.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label5);
		
		JLabel info_label6 = new JLabel("Read Register Count");
		info_label6.setForeground(Color.BLACK);
		info_label6.setBounds(0, 245, 322, 40);
		info_label6.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label6.setHorizontalAlignment(SwingConstants.LEFT);
		info_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label6);
		
		JLabel info_value_label6 = new JLabel();
		info_value_label6.setForeground(Color.BLACK);
		info_value_label6.setBounds(275, 245, 284, 40);
		info_value_label6.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_value_label6);
		
		JLabel info_label7 = new JLabel("Read Register Length");
		info_label7.setForeground(Color.BLACK);
		info_label7.setBounds(0, 280, 322, 44);
		info_label7.setBorder(new EmptyBorder(0, 10, 0, 0));
		info_label7.setHorizontalAlignment(SwingConstants.LEFT);
		info_label7.setFont(FontManager.getFont(Font.BOLD, 15));
		Info_panel.add(info_label7);
		
		JLabel info_value_label7 = new JLabel();		
		info_value_label7.setForeground(Color.BLACK);
		info_value_label7.setHorizontalAlignment(SwingConstants.LEFT);
		info_value_label7.setBounds(275, 280, 284, 44);
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
		
		JLabel summary_label0 = new JLabel("");
		summary_label0.setForeground(Color.BLACK);
		summary_label0.setFont(FontManager.getFont(Font.BOLD, 15));
		summary_label0.setBorder(new EmptyBorder(0, 10, 0, 0));
		summary_label0.setBounds(0, 0, 998, 28);
		summary_panel.add(summary_label0);
		
		JLabel summary_label1 = new JLabel("");
		summary_label1.setForeground(Color.BLACK);
		summary_label1.setBorder(new EmptyBorder(0, 10, 0, 0));
		summary_label1.setFont(FontManager.getFont(Font.BOLD, 15));
		summary_label1.setBounds(0, 30, 998, 28);
		summary_panel.add(summary_label1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setFont(FontManager.getFont(Font.PLAIN, 13));
		summaryPanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(589, 10, 427, 329);
		resultPanel.add(scrollPane);
		
		table = new JTable();
		table.setBackground(Color.WHITE);
		
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
		dataTypePanel.setVisible(false); // functionCode 3, 4 일때만 데이터 타입 콤보박스 표현 (functionCdoe 1, 2 : ON/OFF 상태이기 때문에)
								
		
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
		
		JLabel RX = new JLabel("RX");
		RX.setForeground(Color.BLACK);
		RX.setHorizontalAlignment(SwingConstants.LEFT);
		RX.setFont(FontManager.getFont(Font.BOLD, 16));
		RX.setBounds(11, 60, 26, 31);
		inputPanel.add(RX);
		
		inputTextField = new JTextField();
		inputTextField.setForeground(Color.BLACK);
		inputTextField.setBorder(UIManager.getBorder("TextField.border"));		
		inputTextField.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextField.setFont(FontManager.getFont(Font.BOLD, 15));
		inputTextField.setBounds(38, 62, 640, 31);
		inputTextField.addActionListener(new ActionListener() {
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
		JButton resetButton = new JButton("Reset");
		resetButton.setFocusPainted(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(userReset) inputTextField.setText(null);
				expression_textField.setText(null);
				global_rx = null;
				dataTypePanel.setVisible(false);
				
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
				
				resetTable(table);
				
				userReset = true;
			}
		});
		
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 12));
		resetButton.setBounds(685, 60, 67, 31);
		inputPanel.add(resetButton);
		
		// 분석 버튼
		analysisButton = new JButton("Analysis");
		analysisButton.setFocusPainted(false);
		// 분석 버튼 클릭시 발생하는 이벤트
		analysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 텍스트 필드에 분석 할 패킷 정보를 입력하지 않고 분석 버튼 클릭 시 내용 출력 후 리턴
				if((inputTextField.getText() == null)|| (inputTextField.getText().length() == 0)) {
					System.out.println("inputTextField is Null");
					return;
				}
				
				RX_Info rx = new RX_Info();
				rx.setContent(inputTextField.getText().replaceAll(" ", "").trim());
												
				if(isRTU) {
					try {
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
						
						if(rx == null) { 
							userReset = false;
							resetButton.doClick(); 
							return; 
						}
						
					}catch(Exception exception) {
						exception.printStackTrace();
						System.out.println(exception.getMessage());
						Util.showMessage(exception.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
					
					if(rx.isCRCError()) { 
						userReset = false;
						resetButton.doClick(); 
						return; 
						}
					
					
					
					if(rx.isRead()) {
						// Modbus RTU : 수집 명령어 분석
																		
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label1.setText("Modbus Type");
						info_value_label1.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
						info_label2.setText("Response Content");
						info_value_label2.setText(String.format("%s", rx.getCommandType()));
						
						// 장비 번호
						info_label3.setText("Unit ID (Slave ID)");
						info_value_label3.setText(String.format("Device No. %d", rx.getUnitId()));
						
						// 기능 코드
						info_label4.setText("Function Code");
						info_value_label4.setText(String.format("0x%02x (%s)", rx.getFunctionCode(), rx.getFunctionContent()));												
						
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
						
						
						String registerName = "";
						
						switch(rx.getFunctionCode()) {
							case 1 : registerName="Coil Status"; break;
							default : registerName="Register"; break;
						}
						
						// 읽은 레지스터 개수
						if (rx.isException()) {
							// 예외 응답 처리
							info_label5.setText("Packet Status (Exception Check)");
							info_value_label5.setText(String.format("0x%02x", rx.getExceptionCode()));
							
							// 예외 응답 처리
							info_label6.setText("Exception Content");
							info_value_label6.setText(rx.getExceptionContent());
						}else {
							// 읽은 레지스터 개수
							info_label5.setText("Read Register Count");
							info_value_label5.setText(String.format("%d", rx.getRequestCount()));
							
							// 읽은 레지스터 길이
							info_label6.setText("Read Register Length");
							info_value_label6.setText(String.format("%d Byte", rx.getReadByteCount()));
						}																	
												
						// CRC			
						info_label7.setText("CRC Code (Only Modbus-RTU)");
						info_value_label7.setText(String.format("0x%04X", rx.getCrc()&0xffff));
																		
					
						
						// 예외 패킷 여부
						/** 주의! : 예외 패킷 여부는 info_label0 이다 **/
						info_label0.setText("Packet Status (Exception Check)");
						info_value_label0.setText(String.format("%s", RX_Analyzer.getPacketState(rx.getFunctionCode())));
						
						String rxState = RX_Analyzer.getPacketState(rx.getFunctionCode());
						
						if(rxState.contains("Normal")) {
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.green);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);			
						}else if(rxState.contains("Exception")) {
							resetTable(table);
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.red);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);	
						}else if(rxState.contains("Unknown")) {
							userReset = false;
							resetButton.doClick(); 
							info_value_label0.setText(String.format("%s", RX_Analyzer.getPacketState(rx.getFunctionCode())));
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.YELLOW);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);	
							return;
						}
																						
						// 수집 RX 요약
						if(rx.isException()) {
							summary_label0.setText(String.format("Exception : %s (0x%02x)", rx.getExceptionContent(), rx.getExceptionCode()));
							
							String msg = printExceptionContent(rx);							
							
							summary_label1.setText((msg!=null?msg:""));
						}else {
//							summary_label0.setText(String.format("%d번 장비 %s %d개를 %dbyte 만큼 읽음", rx.getUnitId(), rx.getCommandType(), rx.getRequestCount(), rx.getReadByteCount()));
							summary_label0.setText(String.format("%d %s were Responsed by Device No. %d, and the length of the data is %dBytes", rx.getRequestCount(), rx.getCommandType(), rx.getUnitId(), rx.getReadByteCount()));
							summary_label1.setText(null);	
						}
																		
						// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
						global_rx = rx;
						updateTable(table, rx);
					}else {
						// Modbus RTU : 제어 명령어 분석
						
						resetTable(table); // 제어는 표시해줄 값이 없기 때문에 결과 테이블 내용을 초기화 한다.
						dataTypePanel.setVisible(false);
						
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						info_value_label0.setOpaque(false);
						info_value_label0.setForeground(null);
						info_value_label0.setFont(FontManager.getFont(Font.BOLD, 15));
						info_value_label0.setHorizontalAlignment(SwingConstants.LEFT);				
						
						
						// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Response Content");
						info_value_label1.setText(String.format("%s", rx.getCommandType()));
						
						// 장비 번호
						info_label2.setText("Unit ID (Slave ID)");
						info_value_label2.setText(String.format("Device No. %d", rx.getUnitId()));
						
						// 기능 코드
						info_label3.setText("Function Code");
						info_value_label3.setText(String.format("0x%02x (%s)", rx.getFunctionCode(), rx.getFunctionContent()));
						
						// 제어 요청한 레지스터 주소
						info_label4.setText("Control Register Address");
						info_value_label4.setText(String.format("0x%04x (Based on Register Address)", rx.getStartAddress()&0xffff ));											
						
						// 제어 요청한 모드버스 주소
						info_label5.setText("Control Modbus Address");
						info_value_label5.setText(String.format("%s%04d (Based on Modbus Address)", rx.getModbusAddress(), rx.getStartAddress()+1));
						
						// 제어 요청한 값									
						info_label6.setText((rx.getFunctionCode()==0x05)?"The applied control status":"The applied control value");
						info_value_label6.setText(String.format("%s", (rx.getFunctionCode()==0x05)?rx.getControlStatus():String.format("%d", rx.getControlValue())));
						
						// CRC
						info_label7.setText("CRC Code (Only Modbus-RTU)");
						info_value_label7.setText(String.format("0x%04X", rx.getCrc()&0xffff ));
												
						// 제어 TX 요약
						if(rx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
//							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s를 \"%s\"으로 제어 성공", rx.getUnitId(), rx.getStartAddress()&0xffff, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlStatus()));
							summary_label0.setText(String.format("The %s of the Register address 0x%04x of the Device No. %d was Controlled to \"%s\"", rx.getCommandType().replaceAll(" Control", ""), rx.getStartAddress()&0xffff, rx.getUnitId(), rx.getControlStatus()));
							
//							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s를 \"%s\"으로 제어 성공", rx.getUnitId(), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlStatus()));	
							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of the Device No. %d was Controlled to \"%s\"", rx.getCommandType().replaceAll(" Control", ""), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getUnitId(), rx.getControlStatus()));
						}else {
							// functionCode == 0x06
//							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s을 \"%d\"으로 제어 성공", rx.getUnitId(), rx.getStartAddress()&0xffff, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlValue()));
							summary_label0.setText(String.format("The %s of the Register address 0x%04x of the Device No. %d was Controlled to \"%d\"", rx.getCommandType().replaceAll(" Control", "") ,rx.getStartAddress()&0xffff, rx.getUnitId(), rx.getControlValue()));
							
//							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s을 \"%d\"으로 제어 성공", rx.getUnitId(), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlValue()));					
							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of the Device No. %d was Controlled to \"%d\"", rx.getCommandType().replaceAll(" Control", ""), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1,rx.getUnitId(), rx.getControlValue()));
					}
						
					}
					
				}else {
					
					// Modbus TCP
					try {
						rx = new RX_Analyzer().tcpAnalysis(rx);
						if(rx == null)  { 
							userReset = false;
							resetButton.doClick();
							return; 
							} 
					}catch(Exception exception) {
						exception.printStackTrace();
						System.out.println(exception.getMessage());
						Util.showMessage(exception.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
								
					if(rx.isRead()) {
						// Modbus TCP : 수집 명령어 분석
																		
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label1.setText("Modbus Type");
						info_value_label1.setText(isRTU?"Modbus RTU":"Modbus TCP");
						
						// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
						info_label2.setText("Response Content");
						info_value_label2.setText(String.format("%s", rx.getCommandType()));
						
						// Transaction ID (트랜잭션 ID)	
						info_label3.setText("Transaction ID (Only Modbus-TCP)");
						info_value_label3.setText(String.format("0x%04X", rx.getTransactionId()&0xffff));
						
						// 장비 번호
						info_label4.setText("Unit ID (Slave ID)");
						info_value_label4.setText(String.format("Device No. %d", rx.getUnitId()));
						
						// 기능 코드
						info_label5.setText("Function Code");
						info_value_label5.setText(String.format("0x%02x (%s)", rx.getFunctionCode(), rx.getFunctionContent()));												
						
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
						
						
						String registerName = "";
						
						switch(rx.getFunctionCode()) {
							case 1 : registerName="Coil Status"; break;
							default : registerName="Register"; break;
						}
						
						// 읽은 레지스터 개수
						if (rx.isException()) {
							// 예외 응답 처리
							info_label6.setText("Packet Status (Exception Check)");
							info_value_label6.setText(String.format("0x%02x", rx.getExceptionCode()));
							
							info_label7.setText("Exception Content");
							info_value_label7.setText(rx.getExceptionContent());
						}else {
							info_label6.setText("Read Register Count");
							info_value_label6.setText(String.format("%d", rx.getRequestCount()));
							
							info_label7.setText("Read Register Length");
							info_value_label7.setText(String.format("%d Byte", rx.getReadByteCount()));
						}
				
				
						// 예외 패킷 여부
						/** 주의! : 예외 패킷 여부는 info_label0 이다 **/
						info_label0.setText("Packet Status (Exception Check)");
						info_value_label0.setText(String.format("%s", RX_Analyzer.getPacketState(rx.getFunctionCode())));
						
						String rxState = RX_Analyzer.getPacketState(rx.getFunctionCode());
						
						if(rxState.contains("Normal")) {
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.green);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);			
						}else if(rxState.contains("Exception")) {
							resetTable(table);
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.red);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);	
						}else if(rxState.contains("Unknown")) {
							userReset = false;
							resetButton.doClick();
							info_value_label0.setText(String.format("%s", RX_Analyzer.getPacketState(rx.getFunctionCode())));
							info_value_label0.setOpaque(true);
							info_value_label0.setForeground(Color.BLACK);
							info_value_label0.setBackground(Color.YELLOW);
							info_value_label0.setFont(FontManager.getFont(Font.BOLD, 16));
							info_value_label0.setHorizontalAlignment(SwingConstants.CENTER);
							return;
						}
																						
						// 수집 RX 요약
						if(rx.isException()) {
							summary_label0.setText(String.format("Exception : %s (0x%02x)", rx.getExceptionContent(), rx.getExceptionCode()));
							
							String msg = printExceptionContent(rx);							
							
							summary_label1.setText((msg!=null?msg:""));
						}else {
//							summary_label0.setText(String.format("%d번 장비 %s %d개를 %dbyte 만큼 읽음", rx.getUnitId(), rx.getCommandType(), rx.getRequestCount(), rx.getReadByteCount()));
							summary_label0.setText(String.format("%d %s were Responsed by Device No. %d, and the length of the data is %dBytes", rx.getRequestCount(), rx.getCommandType(), rx.getUnitId(), rx.getReadByteCount()));
							summary_label1.setText(null);	
						}
																		
						// updataTable() 에 넘겨줄 RX_Info 인스턴스 먼저 초기화를 해줘야한다.
						global_rx = rx;
						updateTable(table, rx);
					}else {
						// Modbus TCP : 제어 명령어 분석
						
						resetTable(table); // 제어는 표시해줄 값이 없기 때문에 결과 테이블 내용을 초기화 한다.
						dataTypePanel.setVisible(false);
						
						// 모드버스 타입 (Modbus TCP / Modbus RTU)
						info_label0.setText("Modbus Type");
						info_value_label0.setText(isRTU?"Modbus RTU":"Modbus TCP");
						info_value_label0.setOpaque(false);
						info_value_label0.setForeground(null);
						info_value_label0.setFont(FontManager.getFont(Font.BOLD, 15));
						info_value_label0.setHorizontalAlignment(SwingConstants.LEFT);				
						
						
						// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
						info_label1.setText("Response Content");
						info_value_label1.setText(String.format("%s", rx.getCommandType()));
						
						// Transaction ID (Only Modbus-TCP)
						info_label2.setText("Transaction ID (Only Modbus-TCP)");
						info_value_label2.setText(String.format("0x%04X", rx.getTransactionId()&0xffff ));
						
						// 장비 번호
						info_label3.setText("Unit ID (Slave ID)");
						info_value_label3.setText(String.format("Device No. %d", rx.getUnitId()));
						
						// 기능 코드
						info_label4.setText("Function Code");
						info_value_label4.setText(String.format("0x%02x (%s)", rx.getFunctionCode(), rx.getFunctionContent()));
						
						// 제어 요청한 레지스터 주소
						info_label5.setText("Control Register Address");
						info_value_label5.setText(String.format("0x%04x (Based on Register Address)", rx.getStartAddress()&0xffff ));											
						
						// 제어 요청한 모드버스 주소
						info_label6.setText("Control Modbus Address");
						info_value_label6.setText(String.format("%s%04d (Based on Modbus Address)", rx.getModbusAddress(), rx.getStartAddress()+1));
						
						// 제어 요청한 값									
						info_label7.setText((rx.getFunctionCode()==0x05)?"The applied control status":"The applied control value");
						info_value_label7.setText(String.format("%s", (rx.getFunctionCode()==0x05)?rx.getControlStatus():String.format("%d", rx.getControlValue())));
						
											
						// 제어 TX 요약
						if(rx.getFunctionCode() == 0x05) {
							// functionCode == 0x05
//							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s를 \"%s\"으로 제어 성공", rx.getUnitId(), rx.getStartAddress()&0xffff, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlStatus()));
							summary_label0.setText(String.format("The %s of the Register address 0x%04x of the Device No. %d was Controlled to \"%s\"", rx.getCommandType().replaceAll(" Control", ""), rx.getStartAddress()&0xffff, rx.getUnitId(), rx.getControlStatus()));
							
//							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s를 \"%s\"으로 제어 성공", rx.getUnitId(), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlStatus()));
							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of the Device No. %d was Controlled to \"%s\"", rx.getCommandType().replaceAll(" Control", ""), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getUnitId(), rx.getControlStatus()));
						}else {
							// functionCode == 0x06
//							summary_label0.setText(String.format("%d번 장비의 레지스터 주소 0x%04x번지 %s을 \"%d\"으로 제어 성공", rx.getUnitId(), rx.getStartAddress()&0xffff, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlValue()));
							summary_label0.setText(String.format("The %s of the Register address 0x%04x of the Device No. %d was Controlled to \"%d\"", rx.getCommandType().replaceAll(" Control", "") ,rx.getStartAddress()&0xffff, rx.getUnitId(), rx.getControlValue()));
							
//							summary_label1.setText(String.format("%d번 장비의 모드버스 주소 %s%04d번지 %s을 \"%d\"으로 제어 성공", rx.getUnitId(), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1, rx.getCommandType().replaceAll(" 제어", "") ,rx.getControlValue()));
							summary_label1.setText(String.format("The %s of the Modbus address %s%04d of the Device No. %d was Controlled to \"%d\"", rx.getCommandType().replaceAll(" Control", ""), rx.getModbusAddress(), (rx.getStartAddress()&0xffff)+1,rx.getUnitId(), rx.getControlValue()));
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
		
		expression_label = new JLabel("Expression");		
		expression_label.setForeground(new Color(0, 128, 0));
		expression_label.setBackground(Color.WHITE);
		expression_label.setHorizontalAlignment(SwingConstants.CENTER);
		expression_label.setFont(FontManager.getFont(Font.BOLD, 17));
		expression_label.setBounds(422, 12, 105, 28);
		expression_label.setVisible(false);
		inputPanel.add(expression_label);
		
		expression_textField = new JTextField();		
		expression_textField.setForeground(Color.BLACK);
		expression_textField.setBackground(Color.WHITE);
		expression_textField.setHorizontalAlignment(SwingConstants.LEFT);
		expression_textField.setFont(FontManager.getFont(Font.BOLD, 16));
		expression_textField.setBounds(527, 12, 312, 31);
		expression_textField.setColumns(10);
		expression_textField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setExpressionTable(table);				
			}
		});
		expression_textField.setVisible(false);
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
				
		// Modbus 타입이 TCP인지 RTU인지를 결정하는 라디오 버튼 이벤트
		ActionListener radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton b = (JRadioButton)e.getSource();
				typeLabel.setText(b.getText());		
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				// 데이터 타입 판넬 숨기기 , 데이터 타입 콤보박스 내용 초기화
				dataTypePanel.setVisible(false);
				dataTypeComboBox.setSelectedIndex(6); // updateTable() 까지 호출됨
				global_rx = null;
				
				// Modbus RTU, TCP 라디오 버튼 이동 시 
				// 텍스트 필드 초기화
//				inputTextField.setText(null);
				
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
	
	public static void activationExpression() {
		expression_label.setVisible(true);
		expression_textField.setVisible(true);
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
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"index", "Register Value"
				// 열 이름 : 순 서, 레지스터 값
			}
		) {
			// 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
			
			});
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		
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
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));

		Object[][] content = null;
		
		if(isRTU) {
			// Modbus RTU : 테이블의 마지막 셀에 CRC 내용을 표시해주기 위해서 성능 개수보다 셀을 한개 더 많도록 설정			
			content = new Object[rx.getPerfInfo().length+1][];
		}else {
			// Modbus TCP
			content = new Object[rx.getPerfInfo().length][];
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
		
		
		
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				content[i] = new Object[2];
				content[i][0] = new Integer(i + 1); // 순 서
				content[i][1] = value[i]; // 값
			}
					
			if(isRTU) {
				// Modbus RTU : 테이블의 마지막 셀에 CRC 내용 추가
				content[rx.getPerfInfo().length] = new Object[2];
				content[rx.getPerfInfo().length][0] = "CRC16";
				content[rx.getPerfInfo().length][1] = String.format("0x%04X", rx.getCrc()& 0xffff );
			}
			
			
			table.setModel(new DefaultTableModel(
					content,
					new String[] {
						"index", rx.getCommandType()
						// 순서 , 레지스터 값
					}
			) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
			setExpressionTable(table);
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
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		
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
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);		
	}
	
	
	public static void setExpressionTable(JTable table) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();				
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
		
		
		//////////		
		// Expression Check (조건식을 만족하는 셀을 표시)
		CustomScanCellRenderer customScanCellRenderer = null;
		String expression = expression_textField.getText().toLowerCase();
		
		if(expression == null || expression.length() == 0 || expression.equalsIgnoreCase("") || !expression.contains("x")) {
			customScanCellRenderer = new CustomScanCellRenderer();
		}else {
			expression = expression.toLowerCase();
			expression = expression.replace("X", "x");
			expression = expression.replace("and", "&&").replace("or", "||");
			expression = expression.replace("AND", "&&").replace("OR", "||");
			
			customScanCellRenderer = new CustomScanCellRenderer(expression);
		}
		
		tcmSchedule.getColumn(1).setCellRenderer(customScanCellRenderer); // 스캔 결과
	}
	
	
	public static void resetLabel(boolean isRTU, JLabel... label) {
		if(isRTU) {
			// Modbus RTU : JLabel Text init
			
			resetTable(table);
			
			// Packet Status (Exception Check)
			label[0].setText("Packet Status (Exception Check)");
			label[1].setText(null);
			label[1].setOpaque(false);
			
			// 모드버스 타입
			label[2].setText("Modbus Type");
			label[3].setText(null);
			
			// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
			label[4].setText("Response Content");
			label[5].setText(null);
			
			// 장비 번호
			label[6].setText("Unit ID (Slave ID)");
			label[7].setText(null);
			
			// 기능 코드
			label[8].setText("Function Code");
			label[9].setText(null);
			
			// 읽은 레지스터 개수
			label[10].setText("Read Register Count");
			label[11].setText(null);			
			
			// 읽은 레지스터 길이
			label[12].setText("Read Register Length");
			label[13].setText(null);
			
			// CRC
			label[14].setText("CRC Code (Only Modbus-RTU)");
			label[15].setText(null);						
			
			// Summary
			label[16].setText(null);
			label[17].setText(null);						
		}else {
			// Modbus TCP : JLabel Text init
			
			resetTable(table);
			
			// 예외 패킷 여부			
			label[0].setText("Packet Status (Exception Check)");
			label[1].setText(null);
			label[1].setOpaque(false);
			
			// 모드버스 타입
			label[2].setText("Modbus Type");
			label[3].setText(null);
			
			// 응답 내용  (코일 상태 요청, 레지스터 값 요청..)
			label[4].setText("Response Content");
			label[5].setText(null);
			
			// Transaction ID
			label[6].setText("Transaction ID (Only Modbus-TCP)");
			label[7].setText(null);			
			
			// 장비 번호
			label[8].setText("Unit ID (Slave ID)");
			label[9].setText(null);
			
			// 기능 코드
			label[10].setText("Function Code");
			label[11].setText(null);
			
			// 읽은 레지스터 개수
			label[12].setText("Read Register Count");
			label[13].setText(null);			
			
			// 읽은 레지스터 길이
			label[14].setText("Read Register Length");
			label[15].setText(null);
									
			// Summary
			label[16].setText(null);
			label[17].setText(null);
		}
	}
	
	// 예외내용 출력
	public String printExceptionContent(RX_Info rx) {
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
				
		return msg;
	}
	
}




class CustomScanCellRenderer extends DefaultTableCellRenderer {
	
	private String expression = null;
	
	public CustomScanCellRenderer() { };
	
	public CustomScanCellRenderer(String search){
		this.expression = search;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		String scanReulst = null;
		
		try {
			scanReulst = table.getValueAt(row, 1).toString();
		}catch(Exception e) {
			// 테이블 셀에 내용이 없을 경우 (제어 성공 결과 등)
			return c;
		}		
		
			try {												
				if (scanReulst.contains("Exception") || 
					scanReulst.contains("Response Timeout") ||
					scanReulst.contains("CRC Error") ||
					scanReulst.contains("Unknown")) {
					// Not Good
					if (isSelected) {
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					}else{
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setForeground(Color.BLACK);
						c.setBackground(Color.RED);
						return c;
					}
				}else {
					// Good
					if (isSelected) {				
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
					}else{
						c.setFont(FontManager.getFont(Font.PLAIN, 15));
						c.setForeground(new Color(0, 0, 0));
						c.setBackground(Color.WHITE);
					}
				}
			
			try {
				// Search Value Scan
				if(expression != null) {
					boolean result = false;					
					
					result = (boolean)JavaScript.eval(expression.trim() , scanReulst);
					
					if(result) {
						if (isSelected) {
							c.setFont(FontManager.getFont(Font.BOLD, 15));
							c.setBackground(table.getSelectionBackground());
							c.setForeground(table.getSelectionForeground());
						}else{				
							c.setFont(FontManager.getFont(Font.BOLD, 15));
							c.setForeground(Color.BLACK);
							c.setBackground(Color.GREEN);
						}
					}
				}
			}catch(Exception e) {
				// Search Value Scan Expression Error
				e.printStackTrace();
			}
	
			return c;			
			
		}catch(Exception e) {
			e.printStackTrace();
			return c;
		}			
	}
		
}
