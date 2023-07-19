package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import common.util.SwingUtil;
import src_ko.database.Column;
import src_ko.database.ColumnDiscriptor;
import src_ko.database.Condition;
import src_ko.database.DbUtil;
import src_ko.database.OrderCondition;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class DatabaseAccess_Panel extends JPanel {
	
	private static JLabel sqlServerInfo;
	
	public static String viewText = ""; // queryForm_textPane 텍스트 팬에 보여질 텍스트 내용 (검색 테이블, 조회 컬럼, 검색 조건, 정렬 기준 등의 정보)
	public static String viewText_table = ""; // 테이블 내용
	public static String viewText_column = ""; // 조회 항목(컬럼) 내용
	public static String viewText_condition = ""; // 검색 조건 내용
	public static String viewText_order = ""; // 정렬 기준 내용
	
	private static String DBtableName; // 쿼리 양식 전송 컨텐트 팬에 표시해줄 문자열
	private static String DBColumnQuery; // 쿼리 양식 전송 판넬의 컬럼 콤보박스 item(컬럼명)을 얻기위한 문자열
	private static String[] DBColumnNames; // 쿼리 양식 전송 판넬의 컬럼 콤보박스 item(컬럼명)
	private static int selectType = 0; // 0 : 모든 컬럼 조회, 3 : 사용자 지정 컬럼 조회	
	private static List selectedColumnItems = new ArrayList<String>(); // 조회 항목(컬럼) 콤보박스 선택 시, 선택 정보가 저장되는 문자열형 배열	
	private static boolean hasResultSet = true; // 조회 할 데이터의 존재 유무 (rowCount가 1이라도 있으면 true) DbUtil 클래스는 여러 위치에서 사용 될 수 있으므로 해당 클래스의 필드로 선언하였다
	private static String[] columnComboBoxItems = null; 
	private static JComboBox columnDiscription_comboBox;
	private JScrollPane columnDiscription_scrollPane;
	private JScrollBar columnDiscription_scrollPane_scrollBar;
	private JTextPane columnDiscription_textPane; 
	private String[] currentTable;
	
	private static ArrayList columnList = new ArrayList<Column>(); // 컬럼 항목 리스트
	private static ArrayList conditionList = new ArrayList<Condition>(); // 검색 조건 항목 리스트
	private static OrderCondition orderCondition = null; // 정렬 기준 인스턴스
	
	private int conditionCount = 0;
	
	private Font boldfont = FontManager.getFont(Font.BOLD, 15);
	private Font plainfont = FontManager.getFont(Font.PLAIN, 15);
	
	private static CardLayout querySendFormCardLayout;	
	private static CardLayout viewPanelCardLayout;
		
	private static JPanel viewPanel; 
	private static JPanel queryInputPanel; // 쿼리 양식 전송, 함수 전송 방식을 포함하는 판넬
	private JPanel formSendQueryPanel; // 쿼리 양식 전송 판넬
	private JPanel functionSendQueryPanel; // 쿼리 함수 선택 판넬
	
	
	public static JRadioButton FormSend_radioButton; // 쿼리 전송 방식 : 양식 전송 판넬 선택 라디오 버튼
	public static JRadioButton ProcedureSend_radioButton; // 쿼리 전송 방식 : 함수 선택 판넬 선택 라디오 버튼
	
	private static JTextPane queryForm_textPane; // 쿼리 양식 전송 텍스트 팬 (양식 전송 시 쿼리 내용을 정리해서 사용자에게 표시)
	
	// 쿼리 양식 전송 관련 폼
	private static JLabel form_searchColumn_label; // 컬럼 콤보박스 레이블
	private static JComboBox form_Table_ComboBox; // 테이블 선택 콤보박스
	private static JComboBox form_Column_ComboBox; // 컬럼 콤보박스
	private static JButton form_AddColumn_button; // 조회 항목(컬럼) 추가 버튼 
	private static JTextField columnSearch_textField; // 컬럼 디스크립션 검색 필드
	private static JTextField form_searchColumn_textField; // 조회 항목 컬럼 검색 필드
	private static Column lastFindColumn = null;
	private static Column lastSelectColumn = null;

	
	public DatabaseAccess_Panel() {
		// size : 1074, 628
		setBorder(new EmptyBorder(12, 12, 12, 12));
		setSize(1074, 628);
		setBackground(Color.DARK_GRAY);
		
		setLayout(new BorderLayout(0, 0));
		JPanel actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
	
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBounds(12, 10, 1026, 458);
		actualPanel.add(infoPanel);
		infoPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Database Access");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 240, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		viewPanel = new JPanel();		
		viewPanelCardLayout = new CardLayout();
		viewPanel.setBackground(Color.LIGHT_GRAY);
		viewPanel.setBounds(12, 55, 1002, 393);
		infoPanel.add(viewPanel);
		viewPanel.setLayout(viewPanelCardLayout);
		
		JPanel viewPanel_Form = new JPanel();
		viewPanel_Form.setBackground(Color.LIGHT_GRAY);
		viewPanel.add(viewPanel_Form, "viewPanel_Form");
		viewPanel_Form.setLayout(null);
		
		JScrollPane queryForm_scrollPane = new JScrollPane();
		queryForm_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		queryForm_scrollPane.setBounds(12, 10, 434, 373);
		viewPanel_Form.add(queryForm_scrollPane);
		
		queryForm_textPane = new JTextPane();
		queryForm_textPane.setEditable(false);
		queryForm_textPane.setForeground(Color.BLACK);
		queryForm_textPane.setBorder(null);
		queryForm_textPane.setBackground(Color.WHITE);
		queryForm_textPane.setFont(FontManager.getFont(Font.BOLD, 16));
		queryForm_scrollPane.setViewportView(queryForm_textPane);
		
		JLabel columnDiscription_label = new JLabel("Search");
		columnDiscription_label.setHorizontalAlignment(SwingConstants.CENTER);
		columnDiscription_label.setFont(FontManager.getFont(Font.BOLD, 19));
		columnDiscription_label.setBounds(481, 10, 82, 34);
		viewPanel_Form.add(columnDiscription_label);
				
		columnDiscription_scrollPane = new JScrollPane();
		columnDiscription_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		columnDiscription_scrollPane.setBounds(481, 54, 509, 329);		
		viewPanel_Form.add(columnDiscription_scrollPane);
		
		columnDiscription_textPane = new JTextPane();
		columnDiscription_textPane.setText("");
		columnDiscription_textPane.setForeground(Color.BLACK);
		columnDiscription_textPane.setFont(FontManager.getFont(Font.PLAIN, 16));
		columnDiscription_textPane.setEditable(false);
		columnDiscription_textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		columnDiscription_textPane.setBackground(Color.WHITE);		
		columnDiscription_scrollPane.setViewportView(columnDiscription_textPane);
		columnDiscription_scrollPane_scrollBar = columnDiscription_scrollPane.getVerticalScrollBar(); 
				
		columnDiscription_comboBox = new JComboBox();		
		columnDiscription_comboBox.setForeground(Color.BLACK);
		columnDiscription_comboBox.setInheritsPopupMenu(true);
		columnDiscription_comboBox.setFont(FontManager.getFont(Font.BOLD, 16));
		columnDiscription_comboBox.setBackground(Color.WHITE);
		columnDiscription_comboBox.setBounds(700, 10, 290, 34);		
		columnDiscription_comboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		columnDiscription_comboBox.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {								
				// Column Discription
				try {				
					JComboBox temp = (JComboBox)e.getSource();					
					Column item = (Column)temp.getSelectedItem();
					
					columnDiscription_textPane.setText(ColumnDiscriptor.getDiscription(item));															
					columnDiscription_textPane.setCaretPosition(0); // 스크롤을 맨 위로 올려주려고 너무 고생했다 ㅜㅜ 날 괴롭힌 코드이다
				}catch(Exception exception) {
					// 예외 발생 시 아무것도 수행하지 않는다
					// 컬럼 디스크립션 콤보박스에서 "테이블에 데이터 없음" 아이템을 선택 시 Column 인스턴스로 캐스팅 할 수 없어서 스윙이 멈추는 현상이 있었음
					// 위의 이슈를 해당 예외 처리 블럭으로 해결 함
				}				
			}
		});					
		viewPanel_Form.add(columnDiscription_comboBox);
		
		columnSearch_textField = new JTextField();
		columnSearch_textField.setFont(FontManager.getFont(Font.BOLD, 15));
		columnSearch_textField.setBounds(572, 11, 120, 34);		
		columnSearch_textField.setColumns(10);
		columnSearch_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}			
		});
		columnSearch_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchColumnDiscription(lastFindColumn);
			}
		});
		viewPanel_Form.add(columnSearch_textField);
		
		
		JPanel viewPanel_Function = new JPanel();
		viewPanel_Function.setLayout(null);
		viewPanel_Function.setBackground(Color.WHITE);
		viewPanel.add(viewPanel_Function, "viewPanel_Function");
		
		sqlServerInfo = new JLabel("0.0.0.0:0 [ DataBase ]");		
		sqlServerInfo.setForeground(Color.BLUE);
		sqlServerInfo.setHorizontalAlignment(SwingConstants.LEFT);
		sqlServerInfo.setFont(FontManager.getFont(Font.BOLD, 20));
		sqlServerInfo.setBackground(Color.WHITE);
		sqlServerInfo.setBounds(243, 0, 666, 55);
		infoPanel.add(sqlServerInfo);
		
		JPanel querySelectPanel = new JPanel();
		querySelectPanel.setBackground(Color.WHITE);
		querySelectPanel.setBounds(12, 478, 170, 116);
		actualPanel.add(querySelectPanel);
		querySelectPanel.setLayout(null);
		
		JLabel selectQueryLabel = new JLabel("Query \uC804\uC1A1 \uBC29\uBC95");
		selectQueryLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		selectQueryLabel.setForeground(Color.BLACK);
		selectQueryLabel.setBounds(12, 10, 146, 30);
		querySelectPanel.add(selectQueryLabel);
		
		// 쿼리 양식 전송 라디오 버튼
		FormSend_radioButton = new JRadioButton("\uC591\uC2DD \uC785\uB825");
		FormSend_radioButton.setBackground(Color.WHITE);
		FormSend_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		FormSend_radioButton.setBounds(12, 47, 146, 30);
		FormSend_radioButton.setSelected(true);
		FormSend_radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				StoredProcedure_Panel.FormSend_radioButton.setSelected(DatabaseAccess_Panel.FormSend_radioButton.isSelected());
				StoredProcedure_Panel.ProcedureSend_radioButton.setSelected(DatabaseAccess_Panel.ProcedureSend_radioButton.isSelected());								
			}
		});
		querySelectPanel.add(FormSend_radioButton);
		
		// 쿼리 프로시저 전송 라디오 버튼
		ProcedureSend_radioButton = new JRadioButton("\uC800\uC7A5 \uD504\uB85C\uC2DC\uC800");
		ProcedureSend_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		ProcedureSend_radioButton.setBackground(Color.WHITE);
		ProcedureSend_radioButton.setBounds(12, 80, 146, 30);
		ProcedureSend_radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StoredProcedure_Panel.FormSend_radioButton.setSelected(DatabaseAccess_Panel.FormSend_radioButton.isSelected());
				StoredProcedure_Panel.ProcedureSend_radioButton.setSelected(DatabaseAccess_Panel.ProcedureSend_radioButton.isSelected());
				MainFrame.showStoredProcedure();
				StoredProcedure_Panel.procedureCategory_ComboBox.setSelectedIndex(StoredProcedure_Panel.procedureCategory_ComboBox.getSelectedIndex()); 
			}
		});
		querySelectPanel.add(ProcedureSend_radioButton);
		
		
		ButtonGroup querySend_radioGroup = new ButtonGroup();
		querySend_radioGroup.add(FormSend_radioButton);
		querySend_radioGroup.add(ProcedureSend_radioButton);
		
		queryInputPanel = new JPanel();
		queryInputPanel.setBackground(Color.WHITE);
		queryInputPanel.setBounds(194, 478, 844, 116);
		actualPanel.add(queryInputPanel);
		querySendFormCardLayout = new CardLayout();
		queryInputPanel.setLayout(querySendFormCardLayout);
		
		
		
		formSendQueryPanel = new JPanel();
		formSendQueryPanel.setBackground(Color.WHITE);
		queryInputPanel.add(formSendQueryPanel, "FormSendQueryPanel");
		formSendQueryPanel.setLayout(null);
		
		functionSendQueryPanel = new JPanel();
		functionSendQueryPanel.setBackground(Color.WHITE);
		queryInputPanel.add(functionSendQueryPanel, "FunctionSendQueryPanel");
		functionSendQueryPanel.setLayout(null);
		
		// Query 전송 방식은 기본적으로 직접 입력 방식이다
		querySendFormCardLayout.show(queryInputPanel, "UserSendQueryPanel");
		
		// Query 전송 방식 라디오 버튼 : 직접 입력 / 양식 입력
//		ActionListener querySendType_radioListener = new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JRadioButton b = (JRadioButton)e.getSource();
//				String type = b.getText();
//				
//				if(type.contains("양식")) {
//					querySendFormCardLayout.show(queryInputPanel, "FormSendQueryPanel");	
//					viewPanelCardLayout.show(viewPanel, "viewPanel_Form");
//				}else {
//					querySendFormCardLayout.show(queryInputPanel, "FunctionSendQueryPanel");
//					viewPanelCardLayout.show(viewPanel, "viewPanel_Function");
//				}
//				
//			}						
//		};		
//		
//		FormSend_radioButton.addActionListener(querySendType_radioListener);
//		ProcedureSend_radioButton.addActionListener(querySendType_radioListener);
		
		JLabel queryLabel = new JLabel("SQL Query Form");
		queryLabel.setHorizontalAlignment(SwingConstants.LEFT);
		queryLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		queryLabel.setBounds(12, 10, 190, 31);
		formSendQueryPanel.add(queryLabel);
		
		JButton form_queryResetButton = new JButton("\uCD08\uAE30\uD654");
		form_queryResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetQueryFormPanel();
			}
		});
		
		JLabel from_Table_Label = new JLabel("\uAC80\uC0C9 \uD14C\uC774\uBE14");
		from_Table_Label.setHorizontalAlignment(SwingConstants.LEFT);
		from_Table_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		from_Table_Label.setBounds(12, 63, 86, 31);
		formSendQueryPanel.add(from_Table_Label);
		
		// 쿼리 양식 전송 판넬 : 검색 테이블 콤보박스
		form_Table_ComboBox = new JComboBox();
		form_Table_ComboBox.setForeground(Color.BLACK);
		form_Table_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"\uC2DC\uC124\uBB3C", "RCU", "\uC131\uB2A5", "\uC774\uBCA4\uD2B8", "\uC774\uBCA4\uD2B8 \uB0B4\uC5ED"}));
		form_Table_ComboBox.setSelectedIndex(0);
		form_Table_ComboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		form_Table_ComboBox.setBackground(Color.WHITE);
		form_Table_ComboBox.setBounds(102, 64, 112, 32);
		form_Table_ComboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_Table_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				columnList = null; // 조회 항목(컬럼) 리스트 초기화
				conditionList = null; // 조건 리스트 초기화
				columnList = new ArrayList();
				conditionList = new ArrayList();
				Condition.ConditionCount = 0; // 조건 개수 초기화
				orderCondition = null; // 정렬 기준 초기화				
				
				queryForm_textPane.setText(null); // 테이블 콤보박스 내용이 선택되면 먼저 텍스트 팬 내용을 초기화한다				
				columnSearch_textField.setText(null);
				form_searchColumn_textField.setText(null);
				
				JComboBox temp = (JComboBox)e.getSource();
				String tableName = temp.getSelectedItem().toString(); // 검색 테이블 콤보박스에서 선택된 item의 문자열
				
				try{
					switch(tableName) {				
					// 컬럼 내용을 얻기 위한 쿼리를 지정하는 switch 문이며 쿼리의 결과 테이블의 크기가 커서 부하가 걸릴 수 있으므로 상위 1개 행을 조회한다
						case "시설물": 							
							DBtableName = "SERVERINFO (SERVERINFO_FACILITY)";
							DBColumnQuery = "SELECT TOP 1 * FROM SERVERINFO si INNER JOIN SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX";
							currentTable = new String[] {"SERVERINFO", "SERVERINFO_FACILITY"};
							Column[] columns1 = Column.createColumns("SERVERINFO");
							Column[] columns2 = Column.createColumns("SERVERINFO_FACILITY");
							for(int i = 0; i < columns1.length; i++) columnList.add(columns1[i]); 
							for(int i = 0; i < columns2.length; i++) columnList.add(columns2[i]);
							break;
									
									
						case "RCU": 
							DBtableName = "SERVERINFO (SERVERINFO_RTU)";
							DBColumnQuery = "SELECT TOP 1 * FROM SERVERINFO si INNER JOIN SERVERINFO_RTU rtu ON si.nServerIndex = rtu.NODE_INDEX";
							currentTable = new String[] {"SERVERINFO", "SERVERINFO_RTU"};
							Column[] columns3 = Column.createColumns("SERVERINFO");
							Column[] columns4 = Column.createColumns("SERVERINFO_RTU");
							for(int i = 0; i < columns3.length; i++) columnList.add(columns3[i]); 
							for(int i = 0; i < columns4.length; i++) columnList.add(columns4[i]);
							break;
							
							
						case "성능": 
							DBtableName = "SERVER_PERF";
							DBColumnQuery = "SELECT TOP 1 * FROM SERVER_PERF";
							currentTable = new String[] {"SERVER_PERF"};
							Column[] columns5 = Column.createColumns("SERVER_PERF");
							for(int i = 0; i < columns5.length; i++) columnList.add(columns5[i]); 							
							break;
							
							
						case "이벤트" : 
							DBtableName = "ALARM";
							DBColumnQuery = "SELECT TOP 1 * FROM ALARM";
							currentTable = new String[] {"ALARM"};
							Column[] columns6 = Column.createColumns("ALARM");
							for(int i = 0; i < columns6.length; i++) columnList.add(columns6[i]); 		
							break;
							
							
						case "이벤트 내역" :
							DBtableName = "EVENTS";
							DBColumnQuery = "SELECT TOP 1 * FROM EVENTS";
							currentTable = new String[] {"EVENTS"};
							Column[] columns7 = Column.createColumns("EVENTS");
							for(int i = 0; i < columns7.length; i++) columnList.add(columns7[i]); 		
							break;
							
							
						default :
							break;
					}
										
					
					// 조회 항목(컬럼) 콤보박스 초기화 로직 : 사용자에게 검색 할 테이블이 선택되면 해당 테이블에 대한 항목(컬럼) 콤보박스가 아래의 로직으로 초기화 된다
					if(ONION_Info.getMk119Connection() != null) {
						Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						ResultSet rs = stmt.executeQuery(DBColumnQuery);
						
						int rowCount = DbUtil.getRowCount(rs);
						Column.setColumnIndexing(columnList);
						columnList.add(0, "모든 컬럼");
						
						if(rowCount == 0) {
							// ResultSet 인스턴스의 row 개수가 0일 때 (데이터가 없을 때)
							form_Column_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"테이블에 데이터 없음"}));
							form_Column_ComboBox.setForeground(Color.RED);
							form_Column_ComboBox.setSelectedIndex(0);
							
							// 컬럼 설명 콤보박스 내용에 "모든 컬럼" 아이템은 표시하지 않는다							
							columnList.remove(0);
							columnDiscription_comboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							columnDiscription_comboBox.setForeground(Color.BLACK);
							columnDiscription_comboBox.setSelectedIndex(0);									

							// Column Discription 내용 표시
							Column item = (Column)columnDiscription_comboBox.getItemAt(0);
							columnDiscription_textPane.setText(ColumnDiscriptor.getDiscription(item));																					
							
							// ResultSet 인스턴스의 Row Count(행 개수) : 0
							hasResultSet = false;
							form_searchColumn_label.setVisible(false);
							form_searchColumn_textField.setVisible(false);
						}else {
																					
							form_Column_ComboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							form_Column_ComboBox.setForeground(Color.BLACK);
							form_Column_ComboBox.setSelectedIndex(0);							
																	
							// 컬럼 설명 콤보박스 내용에 "모든 컬럼" 아이템은 표시하지 않는다
							columnList.remove(0);
							columnDiscription_comboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							columnDiscription_comboBox.setForeground(Color.BLACK);
							columnDiscription_comboBox.setSelectedIndex(0);		
							
							// Column Discription 내용 표시
							Column item = (Column)columnDiscription_comboBox.getItemAt(0);
							columnDiscription_textPane.setText(ColumnDiscriptor.getDiscription(item));
														
							// ResultSet 인스턴스의 Row Count(행 개수) : 최소 1 이상
							hasResultSet = true;
							form_searchColumn_label.setVisible(true);
							form_searchColumn_textField.setVisible(true);
						}
												
						// 컬럼 콤보박스 아이템 생성에 사용된 인스턴스들을 반환
						DbUtil.close(rs, stmt);						
					}
					
				}catch(SQLException exception) {
					Util.showMessage(String.format("<font color='red'>SQL Exception : MK119 DB Frame</font>\n%s%s\n\n", exception.getMessage(), Util.longSeparator), JOptionPane.ERROR_MESSAGE);
				}catch(Exception exception) {
					System.out.printf("[ MK119_Login_Ok_Panel() : 프레임 생성 중 Exception이 발생하였으나 문제 없습니다 : %s]\n", exception.getMessage());
				}
				
				setViewText_table(String.format("1. 검색 테이블%s  - %s : %s", System.lineSeparator(), tableName, DBtableName));								
				
				if(!hasResultSet) {
					// ResultSet 인스턴스의 RowCount(행 개수) : 0
					String msg = String.format("2. 조회 항목%s  - %s", System.lineSeparator(), "테이블에 조회 할 데이터가 없습니다");
					setViewText_column(msg);
				}else {
					// ResultSet 인스턴스의 RowCount(행 개수) : 행 개수가 최소 1개 이상이면 조회 항목의 기본값으로 "모든 컬럼"
					String msg = String.format("2. 조회 항목%s  - %s", System.lineSeparator(), form_Column_ComboBox.getSelectedItem().toString());
					setViewText_column(msg);
				}
				
				// 조건 내용 표시
				StringBuilder msg = new StringBuilder();
				msg.append(String.format("3. 검색 조건%s", System.lineSeparator()));
				msg.append("  - 설정된 검색 조건이 없습니다");
				setViewText_condition(msg.toString());
				
				
				// 정렬 기준 표시
				StringBuilder msg2 = new StringBuilder();
				msg2.append(String.format("4. 정렬 기준%s", System.lineSeparator()));
				msg2.append("  - 설정된 정렬 기준이 없습니다");
				setViewText_order(msg2.toString());
				
			}
		});
		formSendQueryPanel.add(form_Table_ComboBox);
		
		form_searchColumn_label = new JLabel("\uCEEC\uB7FC \uAC80\uC0C9");
		form_searchColumn_label.setHorizontalAlignment(SwingConstants.LEFT);
		form_searchColumn_label.setFont(FontManager.getFont(Font.BOLD, 16));
		form_searchColumn_label.setBounds(230, 20, 70, 31);
		formSendQueryPanel.add(form_searchColumn_label);
		
		form_searchColumn_textField = new JTextField();
		form_searchColumn_textField.setColumns(10);
		form_searchColumn_textField.setBounds(304, 20, 231, 32);	
		form_searchColumn_textField.setFont(FontManager.getFont(Font.BOLD, 15));
		form_searchColumn_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchSelectColumn(null);
			}
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchSelectColumn(null);
			}			
		});
		form_searchColumn_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchSelectColumn(lastSelectColumn);				
			}
		});
		formSendQueryPanel.add(form_searchColumn_textField);
		
		
		
		JLabel form_Column_Label = new JLabel("\uC870\uD68C \uCEEC\uB7FC");
		form_Column_Label.setHorizontalAlignment(SwingConstants.LEFT);
		form_Column_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		form_Column_Label.setBounds(230, 63, 70, 31);
		formSendQueryPanel.add(form_Column_Label);				
		
		form_AddColumn_button = new JButton("\uCD94 \uAC00");
		form_AddColumn_button.setForeground(Color.BLACK);
		form_AddColumn_button.setFont(FontManager.getFont(Font.BOLD, 14));
		form_AddColumn_button.setBackground(Color.WHITE);
		form_AddColumn_button.setBounds(540, 63, 70, 32);
		form_AddColumn_button.addActionListener(new ActionListener() {
			// 조회 항목(컬럼) 추가 버튼
			// 조회 할 컬럼 항목이 "모든 컬럼" 일 때에는 무시
			public void actionPerformed(ActionEvent e) {
				if(form_Column_ComboBox.getSelectedIndex() < 1) {
					// 조회 항목(컬럼) 콤보박스에 선택 된 아이템이 "모든 컬럼" 또는 "테이블에 데이터 없음" 일 경우
					// 기본적으로 위의 상황 일 경우 항목 "추 가" 버튼이 활성화 되지는 않는다
					return;
				} else {
					Column selectedColumn = (Column)form_Column_ComboBox.getSelectedItem();
					String item = selectedColumn.getColumnName();
					
					int index = selectedColumnItems.indexOf(item);
					
					if(index < 0) {
						selectedColumnItems.add(item);
					}else {
						Util.showMessage(String.format("<font color='red'>Add Column Exception</font>\n%s 항목(컬럼)은 이미 %d번째 항목으로 추가되어 있습니다%s\n", item, index+1 ,Util.separator), JOptionPane.ERROR_MESSAGE); 
					}
										
					StringBuilder msg = new StringBuilder(String.format("2. 조회 항목%s", System.lineSeparator()));				

					if(selectedColumnItems.size() < 1) {
						// 선택 된 컬럼 내용 리스트에 아이템이 없을 경우
						msg.append("  - 선택 된 조회 항목(컬럼)이 없습니다");
					}else {
						// 최소 1개 이상의 "조회 항목(컬럼)"이 선택 되었을 경우
						for(int i = 0; i < selectedColumnItems.size(); i++) {
							if(i == (selectedColumnItems.size()-1)) {
								msg.append(String.format("  - %d. %s", i+1, selectedColumnItems.get(i)));
							}else {
								msg.append(String.format("  - %d. %s%s", i+1, selectedColumnItems.get(i), System.lineSeparator()));	
							}
													
						}
					}
					
					setViewText_column(msg.toString());
										
				}
				
			}
		});
		formSendQueryPanel.add(form_AddColumn_button);
		
		form_Column_ComboBox = new JComboBox();		
		form_Column_ComboBox.setForeground(Color.BLACK);
		form_Column_ComboBox.setInheritsPopupMenu(true);
		form_Column_ComboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		form_Column_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();								
				String item = temp.getSelectedItem().toString();
				
				// 모든 컬럼 선택 시 조회 항목 추가 버튼을 나타내지 않는다
				// 콤보박스에서 선택 된 아이템의 인덱스가 1 이상인 아이템 항목에 대해서만 "조회 항목 추가" 버튼을 표시한다
				if(temp.getSelectedIndex() >= 1) {
					form_AddColumn_button.setVisible(true);
					
					if(selectedColumnItems.size() < 1) {
						// 선택 된 컬럼 내용 리스트에 아이템이 없을 경우
						StringBuilder msg = new StringBuilder();
						msg.append(String.format("2. 조회 항목%s  - 지정된 조회 항목(컬럼)이 없습니다", System.lineSeparator()));
						msg.append(String.format("%s  - 추가 버튼을 클릭하여 조회 항목을 추가 해주세요", System.lineSeparator()));
						setViewText_column(msg.toString());						
					}
					
					// 3 : 사용자 지정 컬럼 조회
					selectType = 3;
				} else {
					// "모든 컬럼", "테이블에 데이터 없음" 선택 시
					form_AddColumn_button.setVisible(false);
					String msg = String.format("2. 조회 항목%s  - %s", System.lineSeparator(), item, System.lineSeparator());
					setViewText_column(msg.toString());
					
					// 기존 선택되었던 개별 컬럼 내용 초기화
					selectedColumnItems.clear();
					
					// 0 : 모든 컬럼 조회
					selectType = 0;
				}
			}
		});
		
		// "모든 컬럼"은 기본적으로 검색 항목 콤보박스 내용에 포함된다
		form_Column_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"\uBAA8\uB4E0 \uCEEC\uB7FC"}));
		form_Column_ComboBox.setSelectedIndex(0);
		form_Column_ComboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		form_Column_ComboBox.setBackground(Color.WHITE);
		form_Column_ComboBox.setBounds(304, 64, 231, 32);
		formSendQueryPanel.add(form_Column_ComboBox);
		
		JButton form_AddCondition_Button = new JButton("\uAC80\uC0C9 \uC870\uAC74 \uCD94\uAC00");		
		form_AddCondition_Button.setForeground(Color.BLACK);
		form_AddCondition_Button.setFont(FontManager.getFont(Font.BOLD, 14));
		form_AddCondition_Button.setBackground(Color.WHITE);
		form_AddCondition_Button.setBounds(614, 27, 142, 31);
		form_AddCondition_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 검색 조건 추가 버튼
				
				Condition condition = null;
				
				if(!hasResultSet) {
					// 컬럼 리스트 또는 조건 리스트가 null 일 경우 아무것도 수행하지 않음
					Util.showMessage("<font color='red'>검색 조건 추가 불가능</font>\n검색 조건으로 지정할 항목(컬럼) 내용이 없습니다&nbsp;&nbsp;&nbsp;&nbsp;\n\n검색 테이블의 내용을 확인 해주세요\n", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(conditionList.size() < 1) {
					// 처음 추가되는 검색 조건 일 경우 ( 논리연산자 미포함 )
					condition = Condition.createFirstCondition();
				}else {
					// 처음 추가되는 검색 조건이 아닐 경우 ( 논리연산자 포함 )
					condition = Condition.createCondition();
				}				
				
				if(condition != null) {
					if(condition.getColumn() != null) {
						// 리턴받은 검색 조건 인스턴스가 정상적으로 초기화 되었을 경우
						conditionList.add(condition);
						
						StringBuilder msg = new StringBuilder(String.format("3. 검색 조건%s", System.lineSeparator()));				

						if(conditionList.size() < 1) {
							// 지정 된 검색 조건이 없을 경우
							msg.append(String.format("  - 설정된 검색 조건이 없습니다%s", System.lineSeparator()));
						} else {
							// 최소 1개 이상의 검색 조건이 존재 할 경우 
							for(int i = 0; i < conditionList.size(); i++) {
								StringBuilder conditionContent = new StringBuilder();
								if(i == (conditionList.size() - 1)) {									
									msg.append(String.format("  - 조건%d : %s", i+1, Condition.getConditionContent((Condition)conditionList.get(i))));	
								}else {									 
									msg.append(String.format("  - 조건%d : %s%s%s", i+1, Condition.getConditionContent((Condition)conditionList.get(i)), System.lineSeparator(), System.lineSeparator()));	
								}
							}
						}						
						setViewText_condition(msg.toString());
					}
				}								
			}
		});
		formSendQueryPanel.add(form_AddCondition_Button);
		
		JButton form_AddOrder_button = new JButton("\uC815\uB82C \uAE30\uC900 \uCD94\uAC00");
		form_AddOrder_button.setForeground(Color.BLACK);
		form_AddOrder_button.setFont(FontManager.getFont(Font.BOLD, 14));
		form_AddOrder_button.setBackground(Color.WHITE);
		form_AddOrder_button.setBounds(614, 63, 142, 31);
		form_AddOrder_button.addActionListener(new ActionListener() {						
			public void actionPerformed(ActionEvent e) {
				try {
					// 정렬 기준 추가 기능
					
					if (!hasResultSet) {
						// 조회 항목(컬럼) 리스트가 null 일 경우 아무것도 수행하지 않음
						Util.showMessage("<font color='red'>정렬 조건 추가 불가능</font>\n정렬 기준으로 지정할 항목(컬럼) 내용이 없습니다&nbsp;&nbsp;&nbsp;&nbsp;\n\n검색 테이블의 내용을 확인 해주세요\n", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// 정렬 기준 인스턴스 초기화
					orderCondition = OrderCondition.createOrderCondition();
					
					if(orderCondition != null) {
						StringBuilder msg = new StringBuilder();
						msg.append(String.format("4. 정렬 기준%s", System.lineSeparator()));
						
						Column column = orderCondition.getColumn();
						String order = orderCondition.getOrder().contains("ASC")?"오름차순":"내림차순";
						
						msg.append(String.format("  - %s 테이블의%s", column.getTableName(), System.lineSeparator()));
						msg.append(String.format("    %s 컬럼 내용을 기준으로%s", column.getColumnName(), System.lineSeparator()));
						msg.append(String.format("    %s", order));					
						setViewText_order(msg.toString());
					}else {
						// 정렬 조건 인스턴스가 null 일 경우 기존의 설정 내용은 초기화 된다
						// 정렬 기준 표시
						StringBuilder msg2 = new StringBuilder();
						msg2.append(String.format("4. 정렬 기준%s", System.lineSeparator()));
						msg2.append("  - 설정된 정렬 기준이 없습니다");
						setViewText_order(msg2.toString());					
						return;
					}
				}catch(Exception exception) {
					exception.printStackTrace();
				}
			}
			
		});
		formSendQueryPanel.add(form_AddOrder_button);
		
		JButton form_queryExcuteButton = new JButton("\uC2E4 \uD589");
		form_queryExcuteButton.setForeground(Color.BLUE);
		form_queryExcuteButton.setFont(FontManager.getFont(Font.BOLD, 13));
		form_queryExcuteButton.setBackground(Color.WHITE);
		form_queryExcuteButton.setBounds(760, 27, 77, 31);
		formSendQueryPanel.add(form_queryExcuteButton);
		form_queryResetButton.setForeground(Color.BLACK);
		form_queryResetButton.setFont(FontManager.getFont(Font.BOLD, 13));
		form_queryResetButton.setBackground(Color.WHITE);
		form_queryResetButton.setBounds(760, 63, 77, 31);
		form_queryExcuteButton.addActionListener(new ActionListener() {						
			public void actionPerformed(ActionEvent e) {
				// 기존에는 결과 테이블의 행 개수가 0개이면 해당 프레임에서 쿼리 수행 자체를 하지 않았지만,
				// 일단 쿼리는 수행하고 DbUtil 클래스에서 행 개수가 0개일때의 처리를 하도록 수정하였다
//				if(!hasResultSet) {
//					Util.showMessage(String.format("<font color='red'>SQL Exception</font>\n결과 테이블에 데이터가 없습니다\n\nTable Row Count(테이블 행 개수) : 0%s\n",Util.separator), JOptionPane.ERROR_MESSAGE);
//					return;
//				}
														
				// 	★★★ SELECT 절 ★★★
				StringBuilder select = new StringBuilder();
				
				if (selectType == 0) {
					// 모든 컬럼 조회
					select.append("SELECT * ");
				} else if (selectType == 3) {
					// 사용자 지정 컬럼 조회
					select.append("SELECT");
					
					if(selectedColumnItems.size() == 0) {
						Util.showMessage(String.format("<font color='red'>SQL Query Exception</font>\n지정 된 조회 항목(컬럼)이 없습니다\n\n조회하실 항목(컬럼)을 선택 후 \"추가\" 버튼을 클릭 해주세요%s\n",Util.longSeparator), JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					for(int i = 0; i < selectedColumnItems.size(); i++) {
						if(i != 0) {
							select.append(String.format(", %s ", selectedColumnItems.get(i)));
						}else {
							select.append(String.format(" %s ", selectedColumnItems.get(i)));	
						}						
					}
					
				} else {
					// 기본(모든 컬럼) 조회
					select.append("SELECT * ");
				}
				
				
				
				// 	★★★ FROM 절 ★★★
				StringBuilder from = new StringBuilder();
				
				if(form_Table_ComboBox.getSelectedIndex() == 0) {
					// 시설물 테이블 조회
					from.append(DbUtil.FACILITY);
				}else if(form_Table_ComboBox.getSelectedIndex() == 1) {
					// RCU 테이블 조회
					from.append(DbUtil.RCU);
				}else if(form_Table_ComboBox.getSelectedIndex() == 2) {
					// 성능 테이블 조회
					from.append(DbUtil.PERF);
				}else if(form_Table_ComboBox.getSelectedIndex() == 3) {
					// 이벤트(알람) 테이블 조회
					from.append(DbUtil.ALARM);
				}else if(form_Table_ComboBox.getSelectedIndex() == 4) {
					// 이벤트 내역 테이블 조회	
					from.append(DbUtil.EVENTS);
				}
				
				
				
				// 	★★★ WHERE 절 ★★★
				StringBuilder where = new StringBuilder();				
				if(conditionList != null) {
					if(conditionList.size() > 0) {
						for(int i = 0; i < conditionList.size(); i++) {
							Condition tempCondition = (Condition)conditionList.get(i);
							where.append(String.format(" %s ", Condition.getSQLContent(tempCondition)));
						}
					}
				}
				
				
				// 	★★★ ORDER BY 절 ★★★
				StringBuilder order_by = new StringBuilder();
				if(orderCondition != null) {
					order_by.append(OrderCondition.getOrderByContent(orderCondition));
				}
				
				
				// 쿼리 종합
				DbUtil.setSELECT(select.toString());
				DbUtil.setFROM(from.toString());
				DbUtil.setWHERE(where.toString());
				DbUtil.setORDER_BY(order_by.toString());
				
				DbUtil.executeQuery(ONION_Info.getSqlServerInfo(), DbUtil.getQuery());
				
				System.out.println("[ DbUtil.executeQuery() : " + DbUtil.getQuery() + " ]");
			}
		});
		formSendQueryPanel.add(form_queryResetButton);
		
		// 백그라운드 이미지 코드가 마지막에 와야함
		JLabel imageLabel = new JLabel(new Util().getMK119BackResource(), JLabel.CENTER);
		imageLabel.setRequestFocusEnabled(false);
		imageLabel.setInheritsPopupMenu(false);
		imageLabel.setFocusTraversalKeysEnabled(false);
		imageLabel.setFocusable(false);
		imageLabel.setBounds(0, 0, 1050, 604);		
		actualPanel.add(imageLabel);
	}
	
	// 프레임의 모든 컴포넌트 초기화
	public void componentAllClear() {
		resetQueryFormPanel(); // 쿼리 양식 전송 판넬 초기화
	}
	
	// 쿼리 양식 전송 판넬 컴포넌트 초기화
	public static void resetQueryFormPanel() {
		try {
			Condition.ConditionCount = 0; // 검색 조건 카운트 초기화
			
			if(columnList != null) columnList.clear();
			if(conditionList != null)conditionList.clear();
			if(orderCondition != null)orderCondition = null;
			
			if(FormSend_radioButton != null)FormSend_radioButton.setSelected(true); // Query 양식 전송 라디오 버튼
			if(querySendFormCardLayout != null)querySendFormCardLayout.show(queryInputPanel, "FormSendQueryPanel"); // Query 양식 전송 입력 판넬
			if(form_Table_ComboBox != null)form_Table_ComboBox.setSelectedIndex(0); // Query 양식 전송 판넬 : 테이블 선택 콤보박스 기본값, 테이블이 선택되면 조건과 정렬 기준까지 초기화된다
			if(form_Column_ComboBox != null)form_Column_ComboBox.setSelectedIndex(0); // Query 양식 전송 판넬 : 컬럼 선택 콤보박스 기본값
			if(selectedColumnItems != null) selectedColumnItems.clear(); // Query 양식 전송 판넬 : 조회 항목(컬럼) 리스트
			if(viewPanelCardLayout != null)viewPanelCardLayout.show(viewPanel, "viewPanel_Form");
			if(columnSearch_textField != null)columnSearch_textField.setText(null);
			if(form_searchColumn_textField != null)form_searchColumn_textField.setText(null);
				
			if(queryForm_textPane != null) {
				queryForm_textPane.setText(getViewText()); // Query 양식 전송 판넬 : 쿼리 정보 텍스트 팬
				queryForm_textPane.setCaretPosition(0);
			}
			
			if(form_AddColumn_button != null)form_AddColumn_button.setVisible(false);			
				
		}catch(Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void setSqlServerInfo(String sqlServerInfo) {
		DatabaseAccess_Panel.sqlServerInfo.setText(sqlServerInfo);
	}
	
	
	// 쿼리 함수 선택 판넬 컴포넌트 초기화
	public void resetQueryFunctionPanel() {
			
	}
	
	
	public void selectTable() {
		
	}			
	
	// 검색 조건 추가버튼의 condition(검색 조건 인스턴스) 생성을 위한 메소드
	public static ArrayList getColumnList() {
		if(DatabaseAccess_Panel.columnList != null) {
			return DatabaseAccess_Panel.columnList;
		}else {
			return null;
		}
	}

	
	public static String getViewText() {	
		return DatabaseAccess_Panel.viewText;
	}

	public static void setViewText() {
		DatabaseAccess_Panel.viewText = String.format(
				"%s"
				+ " %s%s%s%s%s" // 1. 검색 테이블
				+ "%s"
				+ " %s%s%s%s%s" // 2. 조회 항목
				+ "%s"
				+ " %s%s%s%s%s" // 3. 검색 조건
				+ "%s"
				+ " %s" // 4. 정렬 기준
				+ "%s"
				, System.lineSeparator()				
				, getViewText_table(), System.lineSeparator(),System.lineSeparator(),"──────────────────────────" ,System.lineSeparator() // 1. 검색 테이블
				, System.lineSeparator()
				, getViewText_column(), System.lineSeparator(),System.lineSeparator(),"──────────────────────────", System.lineSeparator() // 2. 조회 항목
				, System.lineSeparator()
				, getViewText_condition(), System.lineSeparator(),System.lineSeparator(),"──────────────────────────", System.lineSeparator() // 3. 검색 조건
				, System.lineSeparator()
				, getViewText_order() // 4. 정렬 기준
				, System.lineSeparator()
				);
		
		queryForm_textPane.setText(viewText);
		queryForm_textPane.setCaretPosition(0);
	}

	public static String getViewText_table() {
		return viewText_table;
	}

	public static void setViewText_table(String viewText_table) {
		DatabaseAccess_Panel.viewText_table = viewText_table;
		setViewText();
	}

	public static String getViewText_column() {
		return viewText_column;
	}

	public static void setViewText_column(String viewText_column) {
		DatabaseAccess_Panel.viewText_column = viewText_column;
		setViewText();
	}

	public static String getViewText_condition() {
		return viewText_condition;
	}

	public static void setViewText_condition(String viewText_condition) {
		DatabaseAccess_Panel.viewText_condition = viewText_condition;
		setViewText();
	}

	public static String getViewText_order() {
		return viewText_order;
	}

	public static void setViewText_order(String viewText_order) {
		DatabaseAccess_Panel.viewText_order = viewText_order;
		setViewText();
	}
			
	
	// 조회 컬럼 목록 검색
	public void searchSelectColumn(Column currentColumn) {
		Column findColumn = null;
		
		try {
			// 컬럼 리스트 또는 조건 리스트가 null 일 경우 아무것도 수행하지 않음
			if (!hasResultSet) {
				form_Column_ComboBox.setSelectedIndex(0);
				return;	
			}
						
			String input = form_searchColumn_textField.getText();
			
			Column[] columns = new Column[columnList.size()];
			
			for(int i = 0; i < columnList.size(); i++) {
				columns[i] = (Column)columnList.get(i);
			}
			
			if(currentColumn != null) {
				findColumn = Column.searchNextColumn(columns, input, lastSelectColumn);
			}else {
				findColumn = Column.searchColumn(columns, input);	
			}
						
			if(findColumn == null) {
				return;
			}else {				
				int itemCount = form_Column_ComboBox.getItemCount();
				
				for(int i = 0; i < itemCount; i++) {
					if(form_Column_ComboBox.getItemAt(i).toString().contains("모든")) continue;
					
					Column compareColumn = (Column)form_Column_ComboBox.getItemAt(i);
					
					if(findColumn.getTable_ColumnName().equalsIgnoreCase(compareColumn.getTable_ColumnName())) {
						form_Column_ComboBox.setSelectedIndex(i);
						lastSelectColumn = (Column)form_Column_ComboBox.getSelectedItem();
						return;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 컬럼 디스크립션 검색 기능
	public void searchColumnDiscription(Column currentColumn) {
		Column findColumn = null;
		
		try {

			// 컬럼 리스트가 null 일 경우 아무것도 수행하지 않음
			if (columnDiscription_comboBox.getItemCount() < 0) {
				columnDiscription_comboBox.setSelectedIndex(0);
				return;	
			}
						
			String input = columnSearch_textField.getText();
			
			Column[] columns = new Column[columnList.size()];
			
			for(int i = 0; i < columnList.size(); i++) {
				columns[i] = (Column)columnList.get(i);
			}
			
			if(currentColumn != null) {
				findColumn = Column.searchNextColumn(columns, input, lastFindColumn);
			}else {
				findColumn = Column.searchColumn(columns, input);	
			}
			
			
			if(findColumn == null) {
				return;
			}else {				
				int itemCount = columnDiscription_comboBox.getItemCount();
				
				for(int i = 0; i < itemCount; i++) {
					Column compareColumn = (Column)columnDiscription_comboBox.getItemAt(i);
					
					if(findColumn.getTable_ColumnName().equalsIgnoreCase(compareColumn.getTable_ColumnName())) {
						columnDiscription_comboBox.setSelectedIndex(i);
						lastFindColumn = (Column)columnDiscription_comboBox.getSelectedItem();
						return;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
