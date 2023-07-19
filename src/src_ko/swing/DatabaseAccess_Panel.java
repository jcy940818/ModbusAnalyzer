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
	
	public static String viewText = ""; // queryForm_textPane �ؽ�Ʈ �ҿ� ������ �ؽ�Ʈ ���� (�˻� ���̺�, ��ȸ �÷�, �˻� ����, ���� ���� ���� ����)
	public static String viewText_table = ""; // ���̺� ����
	public static String viewText_column = ""; // ��ȸ �׸�(�÷�) ����
	public static String viewText_condition = ""; // �˻� ���� ����
	public static String viewText_order = ""; // ���� ���� ����
	
	private static String DBtableName; // ���� ��� ���� ����Ʈ �ҿ� ǥ������ ���ڿ�
	private static String DBColumnQuery; // ���� ��� ���� �ǳ��� �÷� �޺��ڽ� item(�÷���)�� ������� ���ڿ�
	private static String[] DBColumnNames; // ���� ��� ���� �ǳ��� �÷� �޺��ڽ� item(�÷���)
	private static int selectType = 0; // 0 : ��� �÷� ��ȸ, 3 : ����� ���� �÷� ��ȸ	
	private static List selectedColumnItems = new ArrayList<String>(); // ��ȸ �׸�(�÷�) �޺��ڽ� ���� ��, ���� ������ ����Ǵ� ���ڿ��� �迭	
	private static boolean hasResultSet = true; // ��ȸ �� �������� ���� ���� (rowCount�� 1�̶� ������ true) DbUtil Ŭ������ ���� ��ġ���� ��� �� �� �����Ƿ� �ش� Ŭ������ �ʵ�� �����Ͽ���
	private static String[] columnComboBoxItems = null; 
	private static JComboBox columnDiscription_comboBox;
	private JScrollPane columnDiscription_scrollPane;
	private JScrollBar columnDiscription_scrollPane_scrollBar;
	private JTextPane columnDiscription_textPane; 
	private String[] currentTable;
	
	private static ArrayList columnList = new ArrayList<Column>(); // �÷� �׸� ����Ʈ
	private static ArrayList conditionList = new ArrayList<Condition>(); // �˻� ���� �׸� ����Ʈ
	private static OrderCondition orderCondition = null; // ���� ���� �ν��Ͻ�
	
	private int conditionCount = 0;
	
	private Font boldfont = FontManager.getFont(Font.BOLD, 15);
	private Font plainfont = FontManager.getFont(Font.PLAIN, 15);
	
	private static CardLayout querySendFormCardLayout;	
	private static CardLayout viewPanelCardLayout;
		
	private static JPanel viewPanel; 
	private static JPanel queryInputPanel; // ���� ��� ����, �Լ� ���� ����� �����ϴ� �ǳ�
	private JPanel formSendQueryPanel; // ���� ��� ���� �ǳ�
	private JPanel functionSendQueryPanel; // ���� �Լ� ���� �ǳ�
	
	
	public static JRadioButton FormSend_radioButton; // ���� ���� ��� : ��� ���� �ǳ� ���� ���� ��ư
	public static JRadioButton ProcedureSend_radioButton; // ���� ���� ��� : �Լ� ���� �ǳ� ���� ���� ��ư
	
	private static JTextPane queryForm_textPane; // ���� ��� ���� �ؽ�Ʈ �� (��� ���� �� ���� ������ �����ؼ� ����ڿ��� ǥ��)
	
	// ���� ��� ���� ���� ��
	private static JLabel form_searchColumn_label; // �÷� �޺��ڽ� ���̺�
	private static JComboBox form_Table_ComboBox; // ���̺� ���� �޺��ڽ�
	private static JComboBox form_Column_ComboBox; // �÷� �޺��ڽ�
	private static JButton form_AddColumn_button; // ��ȸ �׸�(�÷�) �߰� ��ư 
	private static JTextField columnSearch_textField; // �÷� ��ũ���� �˻� �ʵ�
	private static JTextField form_searchColumn_textField; // ��ȸ �׸� �÷� �˻� �ʵ�
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
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����				
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
					columnDiscription_textPane.setCaretPosition(0); // ��ũ���� �� ���� �÷��ַ��� �ʹ� ����ߴ� �̤� �� ������ �ڵ��̴�
				}catch(Exception exception) {
					// ���� �߻� �� �ƹ��͵� �������� �ʴ´�
					// �÷� ��ũ���� �޺��ڽ����� "���̺� ������ ����" �������� ���� �� Column �ν��Ͻ��� ĳ���� �� �� ��� ������ ���ߴ� ������ �־���
					// ���� �̽��� �ش� ���� ó�� ������ �ذ� ��
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
		
		// ���� ��� ���� ���� ��ư
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
		
		// ���� ���ν��� ���� ���� ��ư
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
		
		// Query ���� ����� �⺻������ ���� �Է� ����̴�
		querySendFormCardLayout.show(queryInputPanel, "UserSendQueryPanel");
		
		// Query ���� ��� ���� ��ư : ���� �Է� / ��� �Է�
//		ActionListener querySendType_radioListener = new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JRadioButton b = (JRadioButton)e.getSource();
//				String type = b.getText();
//				
//				if(type.contains("���")) {
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
		
		// ���� ��� ���� �ǳ� : �˻� ���̺� �޺��ڽ�
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
								
				columnList = null; // ��ȸ �׸�(�÷�) ����Ʈ �ʱ�ȭ
				conditionList = null; // ���� ����Ʈ �ʱ�ȭ
				columnList = new ArrayList();
				conditionList = new ArrayList();
				Condition.ConditionCount = 0; // ���� ���� �ʱ�ȭ
				orderCondition = null; // ���� ���� �ʱ�ȭ				
				
				queryForm_textPane.setText(null); // ���̺� �޺��ڽ� ������ ���õǸ� ���� �ؽ�Ʈ �� ������ �ʱ�ȭ�Ѵ�				
				columnSearch_textField.setText(null);
				form_searchColumn_textField.setText(null);
				
				JComboBox temp = (JComboBox)e.getSource();
				String tableName = temp.getSelectedItem().toString(); // �˻� ���̺� �޺��ڽ����� ���õ� item�� ���ڿ�
				
				try{
					switch(tableName) {				
					// �÷� ������ ��� ���� ������ �����ϴ� switch ���̸� ������ ��� ���̺��� ũ�Ⱑ Ŀ�� ���ϰ� �ɸ� �� �����Ƿ� ���� 1�� ���� ��ȸ�Ѵ�
						case "�ü���": 							
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
							
							
						case "����": 
							DBtableName = "SERVER_PERF";
							DBColumnQuery = "SELECT TOP 1 * FROM SERVER_PERF";
							currentTable = new String[] {"SERVER_PERF"};
							Column[] columns5 = Column.createColumns("SERVER_PERF");
							for(int i = 0; i < columns5.length; i++) columnList.add(columns5[i]); 							
							break;
							
							
						case "�̺�Ʈ" : 
							DBtableName = "ALARM";
							DBColumnQuery = "SELECT TOP 1 * FROM ALARM";
							currentTable = new String[] {"ALARM"};
							Column[] columns6 = Column.createColumns("ALARM");
							for(int i = 0; i < columns6.length; i++) columnList.add(columns6[i]); 		
							break;
							
							
						case "�̺�Ʈ ����" :
							DBtableName = "EVENTS";
							DBColumnQuery = "SELECT TOP 1 * FROM EVENTS";
							currentTable = new String[] {"EVENTS"};
							Column[] columns7 = Column.createColumns("EVENTS");
							for(int i = 0; i < columns7.length; i++) columnList.add(columns7[i]); 		
							break;
							
							
						default :
							break;
					}
										
					
					// ��ȸ �׸�(�÷�) �޺��ڽ� �ʱ�ȭ ���� : ����ڿ��� �˻� �� ���̺��� ���õǸ� �ش� ���̺� ���� �׸�(�÷�) �޺��ڽ��� �Ʒ��� �������� �ʱ�ȭ �ȴ�
					if(ONION_Info.getMk119Connection() != null) {
						Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						ResultSet rs = stmt.executeQuery(DBColumnQuery);
						
						int rowCount = DbUtil.getRowCount(rs);
						Column.setColumnIndexing(columnList);
						columnList.add(0, "��� �÷�");
						
						if(rowCount == 0) {
							// ResultSet �ν��Ͻ��� row ������ 0�� �� (�����Ͱ� ���� ��)
							form_Column_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"���̺� ������ ����"}));
							form_Column_ComboBox.setForeground(Color.RED);
							form_Column_ComboBox.setSelectedIndex(0);
							
							// �÷� ���� �޺��ڽ� ���뿡 "��� �÷�" �������� ǥ������ �ʴ´�							
							columnList.remove(0);
							columnDiscription_comboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							columnDiscription_comboBox.setForeground(Color.BLACK);
							columnDiscription_comboBox.setSelectedIndex(0);									

							// Column Discription ���� ǥ��
							Column item = (Column)columnDiscription_comboBox.getItemAt(0);
							columnDiscription_textPane.setText(ColumnDiscriptor.getDiscription(item));																					
							
							// ResultSet �ν��Ͻ��� Row Count(�� ����) : 0
							hasResultSet = false;
							form_searchColumn_label.setVisible(false);
							form_searchColumn_textField.setVisible(false);
						}else {
																					
							form_Column_ComboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							form_Column_ComboBox.setForeground(Color.BLACK);
							form_Column_ComboBox.setSelectedIndex(0);							
																	
							// �÷� ���� �޺��ڽ� ���뿡 "��� �÷�" �������� ǥ������ �ʴ´�
							columnList.remove(0);
							columnDiscription_comboBox.setModel(new DefaultComboBoxModel(columnList.toArray()));
							columnDiscription_comboBox.setForeground(Color.BLACK);
							columnDiscription_comboBox.setSelectedIndex(0);		
							
							// Column Discription ���� ǥ��
							Column item = (Column)columnDiscription_comboBox.getItemAt(0);
							columnDiscription_textPane.setText(ColumnDiscriptor.getDiscription(item));
														
							// ResultSet �ν��Ͻ��� Row Count(�� ����) : �ּ� 1 �̻�
							hasResultSet = true;
							form_searchColumn_label.setVisible(true);
							form_searchColumn_textField.setVisible(true);
						}
												
						// �÷� �޺��ڽ� ������ ������ ���� �ν��Ͻ����� ��ȯ
						DbUtil.close(rs, stmt);						
					}
					
				}catch(SQLException exception) {
					Util.showMessage(String.format("<font color='red'>SQL Exception : MK119 DB Frame</font>\n%s%s\n\n", exception.getMessage(), Util.longSeparator), JOptionPane.ERROR_MESSAGE);
				}catch(Exception exception) {
					System.out.printf("[ MK119_Login_Ok_Panel() : ������ ���� �� Exception�� �߻��Ͽ����� ���� �����ϴ� : %s]\n", exception.getMessage());
				}
				
				setViewText_table(String.format("1. �˻� ���̺�%s  - %s : %s", System.lineSeparator(), tableName, DBtableName));								
				
				if(!hasResultSet) {
					// ResultSet �ν��Ͻ��� RowCount(�� ����) : 0
					String msg = String.format("2. ��ȸ �׸�%s  - %s", System.lineSeparator(), "���̺� ��ȸ �� �����Ͱ� �����ϴ�");
					setViewText_column(msg);
				}else {
					// ResultSet �ν��Ͻ��� RowCount(�� ����) : �� ������ �ּ� 1�� �̻��̸� ��ȸ �׸��� �⺻������ "��� �÷�"
					String msg = String.format("2. ��ȸ �׸�%s  - %s", System.lineSeparator(), form_Column_ComboBox.getSelectedItem().toString());
					setViewText_column(msg);
				}
				
				// ���� ���� ǥ��
				StringBuilder msg = new StringBuilder();
				msg.append(String.format("3. �˻� ����%s", System.lineSeparator()));
				msg.append("  - ������ �˻� ������ �����ϴ�");
				setViewText_condition(msg.toString());
				
				
				// ���� ���� ǥ��
				StringBuilder msg2 = new StringBuilder();
				msg2.append(String.format("4. ���� ����%s", System.lineSeparator()));
				msg2.append("  - ������ ���� ������ �����ϴ�");
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
			// ��ȸ �׸�(�÷�) �߰� ��ư
			// ��ȸ �� �÷� �׸��� "��� �÷�" �� ������ ����
			public void actionPerformed(ActionEvent e) {
				if(form_Column_ComboBox.getSelectedIndex() < 1) {
					// ��ȸ �׸�(�÷�) �޺��ڽ��� ���� �� �������� "��� �÷�" �Ǵ� "���̺� ������ ����" �� ���
					// �⺻������ ���� ��Ȳ �� ��� �׸� "�� ��" ��ư�� Ȱ��ȭ ������ �ʴ´�
					return;
				} else {
					Column selectedColumn = (Column)form_Column_ComboBox.getSelectedItem();
					String item = selectedColumn.getColumnName();
					
					int index = selectedColumnItems.indexOf(item);
					
					if(index < 0) {
						selectedColumnItems.add(item);
					}else {
						Util.showMessage(String.format("<font color='red'>Add Column Exception</font>\n%s �׸�(�÷�)�� �̹� %d��° �׸����� �߰��Ǿ� �ֽ��ϴ�%s\n", item, index+1 ,Util.separator), JOptionPane.ERROR_MESSAGE); 
					}
										
					StringBuilder msg = new StringBuilder(String.format("2. ��ȸ �׸�%s", System.lineSeparator()));				

					if(selectedColumnItems.size() < 1) {
						// ���� �� �÷� ���� ����Ʈ�� �������� ���� ���
						msg.append("  - ���� �� ��ȸ �׸�(�÷�)�� �����ϴ�");
					}else {
						// �ּ� 1�� �̻��� "��ȸ �׸�(�÷�)"�� ���� �Ǿ��� ���
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
				
				// ��� �÷� ���� �� ��ȸ �׸� �߰� ��ư�� ��Ÿ���� �ʴ´�
				// �޺��ڽ����� ���� �� �������� �ε����� 1 �̻��� ������ �׸� ���ؼ��� "��ȸ �׸� �߰�" ��ư�� ǥ���Ѵ�
				if(temp.getSelectedIndex() >= 1) {
					form_AddColumn_button.setVisible(true);
					
					if(selectedColumnItems.size() < 1) {
						// ���� �� �÷� ���� ����Ʈ�� �������� ���� ���
						StringBuilder msg = new StringBuilder();
						msg.append(String.format("2. ��ȸ �׸�%s  - ������ ��ȸ �׸�(�÷�)�� �����ϴ�", System.lineSeparator()));
						msg.append(String.format("%s  - �߰� ��ư�� Ŭ���Ͽ� ��ȸ �׸��� �߰� ���ּ���", System.lineSeparator()));
						setViewText_column(msg.toString());						
					}
					
					// 3 : ����� ���� �÷� ��ȸ
					selectType = 3;
				} else {
					// "��� �÷�", "���̺� ������ ����" ���� ��
					form_AddColumn_button.setVisible(false);
					String msg = String.format("2. ��ȸ �׸�%s  - %s", System.lineSeparator(), item, System.lineSeparator());
					setViewText_column(msg.toString());
					
					// ���� ���õǾ��� ���� �÷� ���� �ʱ�ȭ
					selectedColumnItems.clear();
					
					// 0 : ��� �÷� ��ȸ
					selectType = 0;
				}
			}
		});
		
		// "��� �÷�"�� �⺻������ �˻� �׸� �޺��ڽ� ���뿡 ���Եȴ�
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
				// �˻� ���� �߰� ��ư
				
				Condition condition = null;
				
				if(!hasResultSet) {
					// �÷� ����Ʈ �Ǵ� ���� ����Ʈ�� null �� ��� �ƹ��͵� �������� ����
					Util.showMessage("<font color='red'>�˻� ���� �߰� �Ұ���</font>\n�˻� �������� ������ �׸�(�÷�) ������ �����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;\n\n�˻� ���̺��� ������ Ȯ�� ���ּ���\n", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(conditionList.size() < 1) {
					// ó�� �߰��Ǵ� �˻� ���� �� ��� ( �������� ������ )
					condition = Condition.createFirstCondition();
				}else {
					// ó�� �߰��Ǵ� �˻� ������ �ƴ� ��� ( �������� ���� )
					condition = Condition.createCondition();
				}				
				
				if(condition != null) {
					if(condition.getColumn() != null) {
						// ���Ϲ��� �˻� ���� �ν��Ͻ��� ���������� �ʱ�ȭ �Ǿ��� ���
						conditionList.add(condition);
						
						StringBuilder msg = new StringBuilder(String.format("3. �˻� ����%s", System.lineSeparator()));				

						if(conditionList.size() < 1) {
							// ���� �� �˻� ������ ���� ���
							msg.append(String.format("  - ������ �˻� ������ �����ϴ�%s", System.lineSeparator()));
						} else {
							// �ּ� 1�� �̻��� �˻� ������ ���� �� ��� 
							for(int i = 0; i < conditionList.size(); i++) {
								StringBuilder conditionContent = new StringBuilder();
								if(i == (conditionList.size() - 1)) {									
									msg.append(String.format("  - ����%d : %s", i+1, Condition.getConditionContent((Condition)conditionList.get(i))));	
								}else {									 
									msg.append(String.format("  - ����%d : %s%s%s", i+1, Condition.getConditionContent((Condition)conditionList.get(i)), System.lineSeparator(), System.lineSeparator()));	
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
					// ���� ���� �߰� ���
					
					if (!hasResultSet) {
						// ��ȸ �׸�(�÷�) ����Ʈ�� null �� ��� �ƹ��͵� �������� ����
						Util.showMessage("<font color='red'>���� ���� �߰� �Ұ���</font>\n���� �������� ������ �׸�(�÷�) ������ �����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;\n\n�˻� ���̺��� ������ Ȯ�� ���ּ���\n", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// ���� ���� �ν��Ͻ� �ʱ�ȭ
					orderCondition = OrderCondition.createOrderCondition();
					
					if(orderCondition != null) {
						StringBuilder msg = new StringBuilder();
						msg.append(String.format("4. ���� ����%s", System.lineSeparator()));
						
						Column column = orderCondition.getColumn();
						String order = orderCondition.getOrder().contains("ASC")?"��������":"��������";
						
						msg.append(String.format("  - %s ���̺���%s", column.getTableName(), System.lineSeparator()));
						msg.append(String.format("    %s �÷� ������ ��������%s", column.getColumnName(), System.lineSeparator()));
						msg.append(String.format("    %s", order));					
						setViewText_order(msg.toString());
					}else {
						// ���� ���� �ν��Ͻ��� null �� ��� ������ ���� ������ �ʱ�ȭ �ȴ�
						// ���� ���� ǥ��
						StringBuilder msg2 = new StringBuilder();
						msg2.append(String.format("4. ���� ����%s", System.lineSeparator()));
						msg2.append("  - ������ ���� ������ �����ϴ�");
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
				// �������� ��� ���̺��� �� ������ 0���̸� �ش� �����ӿ��� ���� ���� ��ü�� ���� �ʾ�����,
				// �ϴ� ������ �����ϰ� DbUtil Ŭ�������� �� ������ 0���϶��� ó���� �ϵ��� �����Ͽ���
//				if(!hasResultSet) {
//					Util.showMessage(String.format("<font color='red'>SQL Exception</font>\n��� ���̺� �����Ͱ� �����ϴ�\n\nTable Row Count(���̺� �� ����) : 0%s\n",Util.separator), JOptionPane.ERROR_MESSAGE);
//					return;
//				}
														
				// 	�ڡڡ� SELECT �� �ڡڡ�
				StringBuilder select = new StringBuilder();
				
				if (selectType == 0) {
					// ��� �÷� ��ȸ
					select.append("SELECT * ");
				} else if (selectType == 3) {
					// ����� ���� �÷� ��ȸ
					select.append("SELECT");
					
					if(selectedColumnItems.size() == 0) {
						Util.showMessage(String.format("<font color='red'>SQL Query Exception</font>\n���� �� ��ȸ �׸�(�÷�)�� �����ϴ�\n\n��ȸ�Ͻ� �׸�(�÷�)�� ���� �� \"�߰�\" ��ư�� Ŭ�� ���ּ���%s\n",Util.longSeparator), JOptionPane.ERROR_MESSAGE);
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
					// �⺻(��� �÷�) ��ȸ
					select.append("SELECT * ");
				}
				
				
				
				// 	�ڡڡ� FROM �� �ڡڡ�
				StringBuilder from = new StringBuilder();
				
				if(form_Table_ComboBox.getSelectedIndex() == 0) {
					// �ü��� ���̺� ��ȸ
					from.append(DbUtil.FACILITY);
				}else if(form_Table_ComboBox.getSelectedIndex() == 1) {
					// RCU ���̺� ��ȸ
					from.append(DbUtil.RCU);
				}else if(form_Table_ComboBox.getSelectedIndex() == 2) {
					// ���� ���̺� ��ȸ
					from.append(DbUtil.PERF);
				}else if(form_Table_ComboBox.getSelectedIndex() == 3) {
					// �̺�Ʈ(�˶�) ���̺� ��ȸ
					from.append(DbUtil.ALARM);
				}else if(form_Table_ComboBox.getSelectedIndex() == 4) {
					// �̺�Ʈ ���� ���̺� ��ȸ	
					from.append(DbUtil.EVENTS);
				}
				
				
				
				// 	�ڡڡ� WHERE �� �ڡڡ�
				StringBuilder where = new StringBuilder();				
				if(conditionList != null) {
					if(conditionList.size() > 0) {
						for(int i = 0; i < conditionList.size(); i++) {
							Condition tempCondition = (Condition)conditionList.get(i);
							where.append(String.format(" %s ", Condition.getSQLContent(tempCondition)));
						}
					}
				}
				
				
				// 	�ڡڡ� ORDER BY �� �ڡڡ�
				StringBuilder order_by = new StringBuilder();
				if(orderCondition != null) {
					order_by.append(OrderCondition.getOrderByContent(orderCondition));
				}
				
				
				// ���� ����
				DbUtil.setSELECT(select.toString());
				DbUtil.setFROM(from.toString());
				DbUtil.setWHERE(where.toString());
				DbUtil.setORDER_BY(order_by.toString());
				
				DbUtil.executeQuery(ONION_Info.getSqlServerInfo(), DbUtil.getQuery());
				
				System.out.println("[ DbUtil.executeQuery() : " + DbUtil.getQuery() + " ]");
			}
		});
		formSendQueryPanel.add(form_queryResetButton);
		
		// ��׶��� �̹��� �ڵ尡 �������� �;���
		JLabel imageLabel = new JLabel(new Util().getMK119BackResource(), JLabel.CENTER);
		imageLabel.setRequestFocusEnabled(false);
		imageLabel.setInheritsPopupMenu(false);
		imageLabel.setFocusTraversalKeysEnabled(false);
		imageLabel.setFocusable(false);
		imageLabel.setBounds(0, 0, 1050, 604);		
		actualPanel.add(imageLabel);
	}
	
	// �������� ��� ������Ʈ �ʱ�ȭ
	public void componentAllClear() {
		resetQueryFormPanel(); // ���� ��� ���� �ǳ� �ʱ�ȭ
	}
	
	// ���� ��� ���� �ǳ� ������Ʈ �ʱ�ȭ
	public static void resetQueryFormPanel() {
		try {
			Condition.ConditionCount = 0; // �˻� ���� ī��Ʈ �ʱ�ȭ
			
			if(columnList != null) columnList.clear();
			if(conditionList != null)conditionList.clear();
			if(orderCondition != null)orderCondition = null;
			
			if(FormSend_radioButton != null)FormSend_radioButton.setSelected(true); // Query ��� ���� ���� ��ư
			if(querySendFormCardLayout != null)querySendFormCardLayout.show(queryInputPanel, "FormSendQueryPanel"); // Query ��� ���� �Է� �ǳ�
			if(form_Table_ComboBox != null)form_Table_ComboBox.setSelectedIndex(0); // Query ��� ���� �ǳ� : ���̺� ���� �޺��ڽ� �⺻��, ���̺��� ���õǸ� ���ǰ� ���� ���ر��� �ʱ�ȭ�ȴ�
			if(form_Column_ComboBox != null)form_Column_ComboBox.setSelectedIndex(0); // Query ��� ���� �ǳ� : �÷� ���� �޺��ڽ� �⺻��
			if(selectedColumnItems != null) selectedColumnItems.clear(); // Query ��� ���� �ǳ� : ��ȸ �׸�(�÷�) ����Ʈ
			if(viewPanelCardLayout != null)viewPanelCardLayout.show(viewPanel, "viewPanel_Form");
			if(columnSearch_textField != null)columnSearch_textField.setText(null);
			if(form_searchColumn_textField != null)form_searchColumn_textField.setText(null);
				
			if(queryForm_textPane != null) {
				queryForm_textPane.setText(getViewText()); // Query ��� ���� �ǳ� : ���� ���� �ؽ�Ʈ ��
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
	
	
	// ���� �Լ� ���� �ǳ� ������Ʈ �ʱ�ȭ
	public void resetQueryFunctionPanel() {
			
	}
	
	
	public void selectTable() {
		
	}			
	
	// �˻� ���� �߰���ư�� condition(�˻� ���� �ν��Ͻ�) ������ ���� �޼ҵ�
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
				+ " %s%s%s%s%s" // 1. �˻� ���̺�
				+ "%s"
				+ " %s%s%s%s%s" // 2. ��ȸ �׸�
				+ "%s"
				+ " %s%s%s%s%s" // 3. �˻� ����
				+ "%s"
				+ " %s" // 4. ���� ����
				+ "%s"
				, System.lineSeparator()				
				, getViewText_table(), System.lineSeparator(),System.lineSeparator(),"����������������������������������������������������" ,System.lineSeparator() // 1. �˻� ���̺�
				, System.lineSeparator()
				, getViewText_column(), System.lineSeparator(),System.lineSeparator(),"����������������������������������������������������", System.lineSeparator() // 2. ��ȸ �׸�
				, System.lineSeparator()
				, getViewText_condition(), System.lineSeparator(),System.lineSeparator(),"����������������������������������������������������", System.lineSeparator() // 3. �˻� ����
				, System.lineSeparator()
				, getViewText_order() // 4. ���� ����
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
			
	
	// ��ȸ �÷� ��� �˻�
	public void searchSelectColumn(Column currentColumn) {
		Column findColumn = null;
		
		try {
			// �÷� ����Ʈ �Ǵ� ���� ����Ʈ�� null �� ��� �ƹ��͵� �������� ����
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
					if(form_Column_ComboBox.getItemAt(i).toString().contains("���")) continue;
					
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
	
	
	// �÷� ��ũ���� �˻� ���
	public void searchColumnDiscription(Column currentColumn) {
		Column findColumn = null;
		
		try {

			// �÷� ����Ʈ�� null �� ��� �ƹ��͵� �������� ����
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
