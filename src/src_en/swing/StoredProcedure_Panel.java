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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.FontManager;
import common.util.SwingUtil;
import src_en.database.DbUtil;
import src_en.database.QueryParameter;
import src_en.database.StoredProcedure;
import src_en.info.ONION_Info;
import src_en.util.FileUtil;
import src_en.util.Util;

public class StoredProcedure_Panel extends JPanel {
	
	private static JLabel sqlServerInfo;	
	private StoredProcedure selectedProcedure;
	private File procedureFile;
			
	private JScrollPane procedureDiscription_scrollPane;
	private JScrollBar columnDiscription_scrollPane_scrollBar;
	private JTextPane procedureDiscription_textPane; 	
	
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
	
	private static JTable table; // 저장  프로시저 리스트 테이블
	public static JComboBox procedureCategory_ComboBox; // 프로시저 카테고리 선택 콤보박스
	public static JTextField procedureSearch_textField;
	private JLabel selectedSP_label;
	
	public StoredProcedure_Panel() {
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
		
		JLabel currentFunction = new JLabel("Stored Procedure");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		// 이미지 사용 시 클래스 경로로 사용하여 배포하여서도 이미지가 유지되도록 하자				
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 254, 55);
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
		
		JScrollPane procedureList_scrollPane = new JScrollPane();
		procedureList_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		procedureList_scrollPane.setBounds(12, 10, 493, 373);
		viewPanel_Form.add(procedureList_scrollPane);
		
		table = new JTable();
		table.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent e) {
				try {
					StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
					selectProcedure(sp);
				}catch(Exception ex) {
					
				}
			}
			
			public void focusGained(FocusEvent e) {
				try {
					StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
					selectProcedure(sp);
				}catch(Exception ex) {
					
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
					selectProcedure(sp);
				}catch(Exception ex) {
					
				}
			}
						
			public void keyReleased(KeyEvent e) {
				try {
					StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
					selectProcedure(sp);
				}catch(Exception ex) {
					
				}
			}
		});
		table.addMouseListener(new ProcedureMouseAdapter());
		resetTable(table);
		
		procedureList_scrollPane.setViewportView(table);
				
		procedureDiscription_scrollPane = new JScrollPane();
		procedureDiscription_scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		procedureDiscription_scrollPane.setBounds(517, 10, 473, 373);		
		viewPanel_Form.add(procedureDiscription_scrollPane);
		
		procedureDiscription_textPane = new JTextPane();
		procedureDiscription_textPane.setText("");
		procedureDiscription_textPane.setForeground(Color.BLACK);
		procedureDiscription_textPane.setFont(FontManager.getFont(Font.PLAIN, 16));
		procedureDiscription_textPane.setEditable(false);
		procedureDiscription_textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		procedureDiscription_textPane.setBackground(Color.WHITE);		
		procedureDiscription_scrollPane.setViewportView(procedureDiscription_textPane);
		columnDiscription_scrollPane_scrollBar = procedureDiscription_scrollPane.getVerticalScrollBar();
		
		
		JPanel viewPanel_Function = new JPanel();
		viewPanel_Function.setLayout(null);
		viewPanel_Function.setBackground(Color.WHITE);
		viewPanel.add(viewPanel_Function, "viewPanel_Function");
		
		sqlServerInfo = new JLabel("0.0.0.0:0 [ DataBase ]");		
		sqlServerInfo.setForeground(Color.BLUE);
		sqlServerInfo.setHorizontalAlignment(SwingConstants.LEFT);
		sqlServerInfo.setFont(FontManager.getFont(Font.BOLD, 20));
		sqlServerInfo.setBackground(Color.WHITE);
		sqlServerInfo.setBounds(255, 0, 455, 55);
		infoPanel.add(sqlServerInfo);
		
		JButton xmlReload_Button = new JButton("XML Reload");		
		xmlReload_Button.setFocusPainted(false);
		xmlReload_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		xmlReload_Button.setForeground(new Color(0, 128, 0));
		xmlReload_Button.setBackground(Color.WHITE);
		xmlReload_Button.setBounds(798, 10, 130, 35);
		xmlReload_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					StoredProcedure.loadStoredProcedureMap();	
					procedureCategory_ComboBox.setSelectedIndex(procedureCategory_ComboBox.getSelectedIndex());
				}catch(Exception ex) {
					// 저장 프로시저 xml 로드중 예외 발생시 아무것도 수행하지 않는다
				}
			}
		});
		
		JButton addProcedureButton = new JButton("Add");
		addProcedureButton.setForeground(new Color(0, 128, 0));
		addProcedureButton.setFont(FontManager.getFont(Font.BOLD, 16));
		addProcedureButton.setFocusPainted(false);
		addProcedureButton.setBackground(Color.WHITE);
		addProcedureButton.setBounds(722, 10, 70, 35);
		addProcedureButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ProcedureGeneratorFrame.isExist) {
					new ProcedureGeneratorFrame();							
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Procedure Generator Frame Already Exists") + Util.separator + "\n");
					 sb.append("The Procedure Generator Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }				
			}
		});
		infoPanel.add(addProcedureButton);
		infoPanel.add(xmlReload_Button);
		
		JButton help_button = new JButton("Help");
		help_button.setForeground(new Color(0, 128, 0));
		help_button.setFont(FontManager.getFont(Font.BOLD, 16));
		help_button.setFocusPainted(false);
		help_button.setBackground(Color.WHITE);
		help_button.setBounds(934, 10, 80, 35);
		help_button.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				new MessageFrame("<html>" + Util.colorBlue("Stored Procedure") + " 기능 설명</html>", description);
			}
		});
//		infoPanel.add(help_button);
		
		JButton form_queryResetButton = new JButton("Reset");
		form_queryResetButton.setForeground(new Color(0, 128, 0));
		form_queryResetButton.setFont(FontManager.getFont(Font.BOLD, 16));
		form_queryResetButton.setFocusPainted(false);
		form_queryResetButton.setBackground(Color.WHITE);
		form_queryResetButton.setBounds(934, 10, 80, 35);
		infoPanel.add(form_queryResetButton);
		form_queryResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetQueryFormPanel();
			}
		});		
		form_queryResetButton.setBackground(Color.WHITE);
		
		JPanel querySelectPanel = new JPanel();
		querySelectPanel.setBackground(Color.WHITE);
		querySelectPanel.setBounds(12, 478, 170, 116);
		actualPanel.add(querySelectPanel);
		querySelectPanel.setLayout(null);
		
		JLabel selectQueryLabel = new JLabel("Query Method");
		selectQueryLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		selectQueryLabel.setForeground(Color.BLACK);
		selectQueryLabel.setBounds(12, 10, 146, 30);
		querySelectPanel.add(selectQueryLabel);
		
		// 쿼리 양식 전송 라디오 버튼
		FormSend_radioButton = new JRadioButton("Use form");
		FormSend_radioButton.setBackground(Color.WHITE);
		FormSend_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		FormSend_radioButton.setBounds(12, 47, 146, 30);		
		FormSend_radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				DatabaseAccess_Panel.FormSend_radioButton.setSelected(StoredProcedure_Panel.FormSend_radioButton.isSelected());
				DatabaseAccess_Panel.ProcedureSend_radioButton.setSelected(StoredProcedure_Panel.ProcedureSend_radioButton.isSelected());
				
				MainFrame.showDatabaseAccess();
				DatabaseAccess_Panel.setSqlServerInfo(ONION_Info.getSqlServerInfo());							
				DatabaseAccess_Panel.resetQueryFormPanel();
			}
		});
		querySelectPanel.add(FormSend_radioButton);
		
		// 프로시저 전송 라디오 버튼
		ProcedureSend_radioButton = new JRadioButton("Stored Procedure");
		ProcedureSend_radioButton.setSelected(true);
		ProcedureSend_radioButton.setFont(FontManager.getFont(Font.BOLD, 15));
		ProcedureSend_radioButton.setBackground(Color.WHITE);
		ProcedureSend_radioButton.setBounds(12, 80, 150, 30);
		ProcedureSend_radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				DatabaseAccess_Panel.FormSend_radioButton.setSelected(StoredProcedure_Panel.FormSend_radioButton.isSelected());
				DatabaseAccess_Panel.ProcedureSend_radioButton.setSelected(StoredProcedure_Panel.ProcedureSend_radioButton.isSelected());								
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
		
		JLabel queryLabel = new JLabel("Stored Procedure Form");
		queryLabel.setHorizontalAlignment(SwingConstants.LEFT);
		queryLabel.setFont(FontManager.getFont(Font.BOLD, 16));
		queryLabel.setBounds(12, 10, 190, 31);
		formSendQueryPanel.add(queryLabel);
		
		JLabel procedureCategory_Label = new JLabel("Category");
		procedureCategory_Label.setHorizontalAlignment(SwingConstants.RIGHT);
		procedureCategory_Label.setFont(FontManager.getFont(Font.BOLD, 16));
		procedureCategory_Label.setBounds(214, 27, 77, 31);
		formSendQueryPanel.add(procedureCategory_Label);
		
		// 프로시저 카테고리 콤보박스
		procedureCategory_ComboBox = new JComboBox();
		procedureCategory_ComboBox.setForeground(Color.BLACK);
//		procedureCategory_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"공통","시설물", "RCU", "성능", "이벤트", "이벤트 내역", "통계 데이터"}));
		procedureCategory_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"Common","Facility", "RCU", "Performance", "Event", "Event History", "Statistics Data"}));
		procedureCategory_ComboBox.setSelectedIndex(0);
		procedureCategory_ComboBox.setFont(FontManager.getFont(Font.BOLD, 16));
		procedureCategory_ComboBox.setBackground(Color.WHITE);
		procedureCategory_ComboBox.setBounds(301, 26, 280, 32);
		procedureCategory_ComboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		procedureCategory_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
										
				JComboBox temp = (JComboBox)e.getSource();
				String tableName = temp.getSelectedItem().toString(); // 검색 테이블 콤보박스에서 선택된 item의 문자열
				
				ArrayList<StoredProcedure> procedureList;
				
				try {
					switch (tableName) {

					case "Common":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.COMMON);
						updateTable(table, procedureList);
						break;
					
					case "Facility":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.SERVERINFO_FACILITY);
						updateTable(table, procedureList);
						break;

					case "RCU":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.SERVERINFO_RTU);
						updateTable(table, procedureList);
						break;

					case "Performance":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.SERVER_PERF);
						updateTable(table, procedureList);
						break;

					case "Event":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.ALARM);
						updateTable(table, procedureList);
						break;

					case "Event History":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.EVENTS);
						updateTable(table, procedureList);
						break;
						
					case "Statistics Data":
						procedureList = StoredProcedure.getProcedureMap().get(StoredProcedure.SERVER_PERFREPORT);
						updateTable(table, procedureList);
						break;

					default:
						break;
					}					
										
					
					// 카테고리가 선택되면 첫번째 셀 선택
					selectFirstCell(table);							
					
				}catch(Exception exception) {
					exception.printStackTrace();
				}
				
			}
		});
		formSendQueryPanel.add(procedureCategory_ComboBox);
		
		JButton showQueryButton = new JButton("Show Query");
		showQueryButton.setForeground(Color.BLACK);
		showQueryButton.setFont(FontManager.getFont(Font.BOLD, 14));
		showQueryButton.setFocusPainted(false);
		showQueryButton.setBackground(Color.WHITE);
		showQueryButton.setBounds(593, 27, 136, 32);
		showQueryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MessageFrame(String.format("<html><font color='blue'>%s</font></html>", selectedProcedure.getName()), selectedProcedure.getQuery());				
			}
		});
		formSendQueryPanel.add(showQueryButton);
		
		JButton openFilebutton = new JButton("Open File");
		openFilebutton.setForeground(Color.BLACK);
		openFilebutton.setFont(FontManager.getFont(Font.BOLD, 14));
		openFilebutton.setFocusPainted(false);
		openFilebutton.setBackground(Color.WHITE);
		openFilebutton.setBounds(593, 69, 136, 32);
		openFilebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileUtil.editFile(procedureFile);
			}
		});
		formSendQueryPanel.add(openFilebutton);
		
		JButton procedureExcute_Button = new JButton("Execute");
		procedureExcute_Button.setFocusPainted(false);
		procedureExcute_Button.setForeground(Color.BLUE);
		procedureExcute_Button.setFont(FontManager.getFont(Font.BOLD, 14));
		procedureExcute_Button.setBackground(Color.WHITE);
		procedureExcute_Button.setBounds(736, 27, 96, 32);
		formSendQueryPanel.add(procedureExcute_Button);
		procedureExcute_Button.addActionListener(new ActionListener() {						
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					// 프로시저 수행 전 사용자가 입력한 파라미터 내용을 매핑 , 파라미터 매핑중 문제가 발생하면 false
					boolean ret = StoredProcedure.mappingParamter(selectedProcedure);				
									
					System.out.println("================================================================");
					System.out.println(selectedProcedure.getExecuteQuery());
					System.out.println("================================================================");
					
					if(ret) DbUtil.executeProcedure(ONION_Info.getSqlServerInfo(), selectedProcedure.getExecuteQuery());	
					
				}catch(Exception exception) {
					// 프로시저 수행중 발생한 예외를 모두 처리
					exception.printStackTrace();					
				}
				
			}
		});
		
		JButton procedureDeleteButton = new JButton("Delete");
		procedureDeleteButton.setFocusPainted(false);
		procedureDeleteButton.setForeground(Color.RED);
		procedureDeleteButton.setFont(FontManager.getFont(Font.BOLD, 14));
		procedureDeleteButton.setBackground(Color.WHITE);
		procedureDeleteButton.setBounds(736, 69, 96, 32);
		procedureDeleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(selectedProcedure == null || procedureFile == null) return;
				
				StringBuilder msg = new StringBuilder();			
				msg.append(String.format("%s", Util.colorRed("Delete Procedure")));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n");
				
				msg.append("Do you really want to delete the Procedure?");				
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n\n");
				
				msg.append("Procedure Index : " + Util.colorBlue(String.valueOf(selectedProcedure.getIndex()+1)));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n");
				
				msg.append("Procedure Name : " + Util.colorBlue(selectedProcedure.getName()));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n");
				
				int userOption = Util.showConfirm(msg.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {
					// 프로시저 삭제
					
					if(!procedureFile.exists()) {
						// 경로상에서 프로시저 파일을 찾을 수 없는 경우
						msg = new StringBuilder();
						msg.append(Util.colorRed("Failed to delete the procedure"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
						
						msg.append("The procedure file could not be found in the path below");
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						
						msg.append("Procedure Name : " + Util.colorRed(selectedProcedure.getName()));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");						
						msg.append("File Path : " + Util.colorRed(procedureFile.getParent()));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
						msg.append("File Name : " + Util.colorRed(procedureFile.getName()));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");						
						
						Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);						
					}else {
						FileUtil.deleteFile(procedureFile);
						
						if(!procedureFile.exists()) {
							// 파일 삭제 성공
							msg = new StringBuilder();
							msg.append(Util.colorRed("Successfully deleted the Procedure"));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");
							
							msg.append("Successfully deleted the procedure file from the path below");
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n\n");
							
							msg.append("Procedure Name : " + Util.colorBlue(selectedProcedure.getName()));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n\n");
							msg.append("File Path : " + Util.colorBlue(procedureFile.getParent()).replace("\\", Util.colorRed("\\")));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");
							msg.append("File Name : " + Util.colorBlue(procedureFile.getName()));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");
														
							Util.showMessage(msg.toString(), JOptionPane.INFORMATION_MESSAGE);
						}else {
							// 파일 삭제 실패
							msg = new StringBuilder();
							msg.append(Util.colorRed("Failed to delete the procedure"));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");
							
							msg.append("Attempted to delete the procedure file from the path below, but failed");
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n\n");
							
							msg.append("Procedure Name : " + Util.colorRed(selectedProcedure.getName()));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n\n");
							msg.append("File Path : " + Util.colorRed(procedureFile.getParent()));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");
							msg.append("File Name : " + Util.colorRed(procedureFile.getName()));
							msg.append(String.format("%s%s", Util.separator, Util.separator));
							msg.append("\n");							
							
							Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);					
						}
					}
					
					xmlReload_Button.doClick();
					
				}else {
					// 프로시저 삭제 취소
					return;
				}
								
			}
		});
		formSendQueryPanel.add(procedureDeleteButton);
		
		JLabel procedureSearch_label = new JLabel("Search");
		procedureSearch_label.setHorizontalAlignment(SwingConstants.RIGHT);
		procedureSearch_label.setFont(FontManager.getFont(Font.BOLD, 16));
		procedureSearch_label.setBounds(214, 72, 77, 31);
		formSendQueryPanel.add(procedureSearch_label);
		
		procedureSearch_textField = new JTextField();
		procedureSearch_textField.setForeground(Color.BLACK);
		procedureSearch_textField.setFont(FontManager.getFont(Font.BOLD, 15));
		procedureSearch_textField.setHorizontalAlignment(SwingConstants.LEFT);
		procedureSearch_textField.setBackground(Color.WHITE);
		procedureSearch_textField.setBounds(301, 71, 280, 32);
		procedureSearch_textField.setColumns(10);
		procedureSearch_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				setTable(table);	
			}
			public void keyReleased(KeyEvent e) {
				setTable(table);
			}
		});
		procedureSearch_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTable(table);				
			}
		});
		formSendQueryPanel.add(procedureSearch_textField);
		
		JLabel selectedIndex_label = new JLabel("Selected Index : ");
		selectedIndex_label.setBackground(Color.WHITE);
		selectedIndex_label.setHorizontalAlignment(SwingConstants.LEFT);
		selectedIndex_label.setFont(FontManager.getFont(Font.BOLD, 16));
		selectedIndex_label.setBounds(12, 62, 129, 31);
		formSendQueryPanel.add(selectedIndex_label);
		
		selectedSP_label = new JLabel("1");
		selectedSP_label.setForeground(Color.BLUE);
		selectedSP_label.setBackground(Color.WHITE);
		selectedSP_label.setHorizontalAlignment(SwingConstants.LEFT);
		selectedSP_label.setFont(FontManager.getFont(Font.BOLD, 25));
		selectedSP_label.setBounds(140, 52, 77, 49);
		formSendQueryPanel.add(selectedSP_label);
		
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
					
			if(ProcedureSend_radioButton != null)ProcedureSend_radioButton.setSelected(true); // Query 프로시저 전송 라디오 버튼						
			if(querySendFormCardLayout != null)querySendFormCardLayout.show(queryInputPanel, "FormSendQueryPanel"); // Query 양식 전송 입력 판넬
			if(procedureCategory_ComboBox != null)procedureCategory_ComboBox.setSelectedIndex(0); // 프로시저 카테고리 콤보박스
			if(viewPanelCardLayout != null)viewPanelCardLayout.show(viewPanel, "viewPanel_Form");
													
		}catch(Exception e) {			
			e.printStackTrace();
		}
	}
	
	
	// 쿼리 함수 선택 판넬 컴포넌트 초기화
	public void resetQueryFunctionPanel() {
			
	}
	
	
	public static void setSqlServerInfo(String sqlServerInfo) {
		StoredProcedure_Panel.sqlServerInfo.setText(sqlServerInfo);
	}

	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
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
				{null, null},
				{null, null},
			},
			new String[] {
				"Index", "Stored Procedure"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(380);
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
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
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(380);
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
		
		
		// Expression Check (조건식을 만족하는 셀을 표시)				
		String searchName = procedureSearch_textField.getText().trim();
		searchName = searchName.toLowerCase();
		
		ProcedureCellRenderer procedureCellRenderer = new ProcedureCellRenderer(searchName);
		
		tcmSchedule.getColumn(1).setCellRenderer(procedureCellRenderer); // 검색 결과
	}
	
	
	public static void updateTable(JTable table, ArrayList<StoredProcedure> procedureList) {

		if ((table == null) || (procedureList == null)) {
			return;
		}

		// 테이블 헤더 설정
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));

		Object[][] content = new Object[procedureList.size()][];

		for (int i = 0; i < procedureList.size(); i++) {
			content[i] = new Object[2];
			content[i][0] = procedureList.get(i).getIndex() + 1; // 순 서
			content[i][1] = procedureList.get(i); // 프로시저 인스턴스를 아이템으로 갖는다
		}

		table.setModel(new DefaultTableModel(content, new String[] { "Index", "Stored Procedure" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTable(table);
	}
	
	
	public void selectProcedure(StoredProcedure sp) {
		selectedProcedure = sp;
		procedureFile = new File(selectedProcedure.getFilePath());
		
		selectedSP_label.setText(String.valueOf(sp.getIndex() + 1));
		selectedSP_label.setForeground(Color.BLUE);
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Procedure File : %s%s", sp.getFileName(), System.lineSeparator()));
		sb.append(String.format("Procedure Name : %s%s%s", sp.getName(), System.lineSeparator(), System.lineSeparator()));
		
		ArrayList<QueryParameter> paramList = sp.getParamList();
		
		if(sp.isUseParam() && paramList != null) {
			// StoredProcedure.useParam : true (파라미터를 사용하는 프로시저일 경우)
			for(int i = 0; i < paramList.size(); i++) {
				QueryParameter param = paramList.get(i);
				sb.append(String.format("Parameter %d : %s", i+1, param.getName()));
				
				if(param.getExample().length() > 0) {
					sb.append(String.format(" ( EX : %s )%s", param.getExample() ,System.lineSeparator()));
				}else {
					sb.append(System.lineSeparator());
				}				
			}			
		}else {
			// StoredProcedure.useParam : false (프로시저가 파라미터를 사용하지 않는 경우)
			sb.append(String.format("Parameter : %s%s", "not use", System.lineSeparator()));
		}
		
		sb.append(System.lineSeparator());		
		sb.append(sp.getContent());
		procedureDiscription_textPane.setText(sb.toString());
	}
	
	public void selectFirstCell(JTable table) {
		// 카테고리가 선택되면 프로시저 테이블의 첫번째 행 선택
		try {
			table.changeSelection(0, 1, false, false);
			table.requestFocus();
			int[] selectedIndex = table.getSelectedRows();										
			
			StoredProcedure sp = (StoredProcedure)table.getValueAt(selectedIndex[0], 1);
			selectProcedure(sp); 
			table.clearSelection();
			selectedSP_label.setForeground(Color.BLUE);
		}catch(Exception e) {
			e.printStackTrace();
			selectedSP_label.setText("??");
			selectedSP_label.setForeground(Color.RED);
			procedureDiscription_textPane.setText(null);
		}
	}
	
	public void setFocusCell(JTable table, int row, int column) {
		table.changeSelection(row, column, false, false);				
		table.requestFocus();
		table.clearSelection();
	}
	
	
	class ProcedureMouseAdapter extends MouseAdapter{
				
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == 1) { 
				// 왼쪽 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// 왼쪽 버튼 더블 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// 오른쪽 클릭
				int column = table.columnAtPoint(e.getPoint());
				int row = table.rowAtPoint(e.getPoint());
				table.changeSelection(row, column, false, false);
				table.requestFocus();
				int[] selectedIndex = table.getSelectedRows();
				
				StoredProcedure sp = (StoredProcedure)table.getValueAt(selectedIndex[0], 1);
				selectProcedure(sp);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == 1) { 
				// 왼쪽 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// 왼쪽 버튼 더블 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// 오른쪽 클릭
				int column = table.columnAtPoint(e.getPoint());
				int row = table.rowAtPoint(e.getPoint());
				table.changeSelection(row, column, false, false);
				table.requestFocus();
				int[] selectedIndex = table.getSelectedRows();
				
				StoredProcedure sp = (StoredProcedure)table.getValueAt(selectedIndex[0], 1);
				selectProcedure(sp);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 1) { 
				// 왼쪽 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// 왼쪽 버튼 더블 클릭
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// 오른쪽 클릭
				int column = table.columnAtPoint(e.getPoint());
				int row = table.rowAtPoint(e.getPoint());
				table.changeSelection(row, column, false, false);
				table.requestFocus();
				int[] selectedIndex = table.getSelectedRows();
				
				StoredProcedure sp = (StoredProcedure)table.getValueAt(selectedIndex[0], 1);
				selectProcedure(sp);
			}
		}
	}
	
	
	
	private static String description = 
			"\r\n" + 
			"[ #. 01 ] Stored Procedure 기능이란?\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Stored Procedure 기능은 자주 사용되는 쿼리문(프로시저)을 .xml 파일로 저장 및 관리하며\r\n" + 
			"\r\n" + 
			"사용자가 상황에 맞는 파라미터(변수)를 입력하여\r\n" + 
			"\r\n" + 
			"동적으로(Dynamic) 쿼리를 사용 할 수 있도록 해주는 기능입니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 02 ] Stored Procedure 디렉토리 저장위치\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer 실행 파일(.exe) 경로의 storedProcedure 디렉토리 아래에서\r\n" + 
			"\r\n" + 
			"각 프로시저 종류별(디렉토리별)로 관리하게 됩니다.\r\n" + 
			"\r\n" + 
			"현재 관리하는 프로시저 디렉토리 종류는 아래와 같습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			" 1. 공통 프로시저     ( COMMON 디렉토리 )\r\n" + 
			" 2. 시설물 관련 프로시저     ( SERVERINFO_FACILITY 디렉토리 )\r\n" + 
			" 3. RCU 관련 프로시저     ( SERVERINFO_RTU 디렉토리 )\r\n" + 
			" 4. 성능 관련 프로시저     ( SERVER_PERF 디렉토리 )\r\n" + 
			" 5. 이벤트 관련 프로시저     ( ALARM 디렉토리 )\r\n" + 
			" 6. 이벤트 내역 관련 프로시저     ( EVENTS 디렉토리 )\r\n" + 
			" 7. 통계 데이터 관련 프로시저     ( SERVER_PERFREPORT 디렉토리 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위의 내용을 간단한 트리로 표현하자면 아래와 같습니다\r\n" + 
			"\r\n" + 
			"  [ ModbusAnalyzer 실행 파일(.exe) 경로 ]\r\n" + 
			"              │\r\n" + 
			"  [ storedProcedure ]\r\n" + 
			"              │\r\n" + 
			"              ├ [ COMMON ]                   ← 공통 프로시저의 모음\r\n" + 
			"              ├ [ SERVERINFO_FACILITY ]    ← 시설물 관련 프로시저의 모음\r\n" + 
			"              ├ [ SERVERINFO_RTU ]          ← RCU 관련 프로시저의 모음\r\n" + 
			"              ├ [ SERVER_PERF ]               ← 성능 관련 프로시저의 모음\r\n" + 
			"              ├ [ ALARM ]                       ← 이벤트 관련 프로시저의 모음\r\n" + 
			"              ├ [ EVENTS ]                      ← 이벤트 내역 관련 프로시저의 모음\r\n" + 
			"              └ [ SERVER_PERFREPORT ]     ← 통계 데이터 관련 프로시저의 모음\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로시저 디렉토리를 종류별로 관리하는 이유는 관리의 용이함을 위함이며\r\n" + 
			"\r\n" + 
			"프로그램에서 프로시저를 수행하는 로직에는 영향을 주지 않습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 03 ] Stored Procedure 로드\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer 프로그램에서 Stored Procedure 로드 작업이란\r\n" + 
			"\r\n" + 
			"프로시저 디렉토리 내부에 존재하는 .xml 파일을 검사하여\r\n" + 
			"\r\n" + 
			"수행 가능한 프로시저 인스턴스로 메모리에 저장하는 행위를 의미합니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로그램 실행 후 프로시저(.xml 파일) 내용이 추가 또는 수정되었다면\r\n" + 
			"\r\n" + 
			"Stored Procedure 프레임의 [ XML Reload ] 버튼으로 프로시저를 다시 로드 할 수 있습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 04 ] Stored Procedure 로드 에러\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Stored Procedure 로드중 에러가 발생하는 경우는 크게 아래와 같습니다.\r\n" + 
			"\r\n" + 
			" 1. ModbusAnalyzer 실행 파일(.exe) 경로에 storedProcedure 디렉토리가 존재하지 않을 경우\r\n" + 
			"\r\n" + 
			" 2. storedProcedure 디렉토리 경로에 [ #. 02 ] 에서 명시한 카테고리별 디렉토리가 존재하지 않는 경우\r\n" + 
			"\r\n" + 
			" 3. 프로시저 .xml 파일 내용에 문제가 있을 경우\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"1, 2 번의 경우( 경로상에 로드 작업에 필요한 디렉토리가 존재하지 않을 경우 ) \r\n" + 
			"\r\n" + 
			"존재하지 않는 디렉토리를 제외하고 정상적으로 존재하는 디렉토리만 로드 할 수 있으며,\r\n" + 
			"\r\n" + 
			"사용자에게 직접 프로시저 디렉토리 경로를 입력받아 프로시저 디렉토리 경로를 지정 할 수도 있습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 05 ] Stored Procedure XML 파일 \r\n" + 
			"\r\n" + 
			"\r\n" + 
			"각 프로시저 디렉토리에 존재하는 프로시저 설정 파일(.xml) 입니다.\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer 프로그램은 [ #. 02 ] 에서 설명한 경로에 위치하는 XML 파일을 검사하여\r\n" + 
			"\r\n" + 
			"수행 가능한 프로시저 인스턴스로 메모리에 저장합니다. ( [ #. 03 ] )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로시저 파일(.xml)의 이름에는 별도의 규칙이 없지만\r\n" + 
			"\r\n" + 
			"프로시저 파일(.xml)의 내용은 반드시 아래의 구조를 준수해야 합니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<storedProcedureInfo>\r\n" + 
			"	<storedProcedure>\r\n" + 
			"		<category>SERVERINFO_FACILITY</category>\r\n" + 
			"		<name>프로시저 이름</name>\r\n" + 
			"		<content>프로시저에 대한 설명</content>\r\n" + 
			"		<query>프로시저 수행시 동작하는 쿼리</query>		\r\n" + 
			"		<params>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>파라미터 1</paramName>\r\n" + 
			"				<paramExample>파라미터 1 입력 예시</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>파라미터 2</paramName>\r\n" + 
			"				<paramExample>파라미터 2 입력 예시</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>파라미터 3</paramName>\r\n" + 
			"				<paramExample>파라미터 3 입력 예시</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"		</params>\r\n" + 
			"	</storedProcedure>\r\n" + 
			"</storedProcedureInfo>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 06 ] Stored Procedure XML 구조\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<storedProcedureInfo> 태그 내부에 \r\n" + 
			"\r\n" + 
			"<storedProcedure> 태그로 하나의 프로시저를 정의하며\r\n" + 
			"\r\n" + 
			"<storedProcedure> 태그의 구성(프로시저 인스턴스의 필드)은 아래와 같습니다.\r\n" + 
			"\r\n" + 
			"1. <category> - 프로시저 종류\r\n" + 
			"2. <name> - 프로시저 이름\r\n" + 
			"3. <content> - 프로시저에 대한 설명\r\n" + 
			"4. <query> - 실제로 수행 될 쿼리\r\n" + 
			"5. <params> - 프로시저에 사용 될 파라미터의 모음으로 자식 태그로 <param> 를 가지고 있습니다.\r\n" + 
			"6. <param> - <params> 태그의 자식 태그이며 <paramName>, <paramExample> 를 가지고 있습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			" ! 주의\r\n" + 
			"  프로시저에 별도의 파라미터가 사용되지 않는다면 <param> 태그는 정의하지 않아도 상관없지만\r\n" + 
			"  파라미터 사용 여부에 관계없이 <params> 태그는 존재해야 합니다.\r\n" + 
			"  \r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 07 ] Stored Procedure XML 구조 2 : 파라미터\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로시저의 파라미터란 실제 쿼리 수행에 필요한 매개 변수를 의미합니다.\r\n" + 
			"\r\n" + 
			"예를들어 아래처럼 성능 번호가 1번인 내용을 조회하는 쿼리가 있습니다.\r\n" + 
			"\r\n" + 
			" \"SELECT * FROM SERVER_PERF WHERE nPerfIndex = 1\"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위의 쿼리는 항상 성능 번호가 1번인 항목만을 조회 할 것 입니다.\r\n" + 
			"\r\n" + 
			"하지만 성능 번호라는 저 파라미터를 상황에 맞게 사용자가 직접 입력 할 수 있다면\r\n" + 
			"\r\n" + 
			"사용자는 언제든 원하는 성능 번호를 가진 항목을 조회 할 수 있을 것 입니다.\r\n" + 
			"\r\n" + 
			" \"SELECT * FROM SERVER_PERF WHERE nPerfIndex = n\"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로시저 XML 파일에서 \r\n" + 
			"\r\n" + 
			"파라미터는 <params> 태그의 자식 태그로 관리합니다.\r\n" + 
			"\r\n" + 
			"즉 <param> 태그 하나가 파라미터 1개를 의미하며\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<param> 태그는 \r\n" + 
			"\r\n" + 
			"파라미터 이름 ( <paramName> ), \r\n" + 
			"파라미터 입력 예시 ( <paramExample> )를 자식 태그로 갖습니다.\r\n" + 
			"\r\n" + 
			"파라미터 이름 태그( <paramName> )는 필수 입력 항목이며, \r\n" + 
			"파라미터 입력 예시 태그( <paramExample> )는 내용 입력이 필수는 아니지만 반드시 태그는 존재해야 합니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 08 ] Stored Procedure XML 구조 3 : 파라미터 매핑\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 07 ] 에서 파라미터에 대해서 설명하였습니다.\r\n" + 
			"\r\n" + 
			"이번에는 파라미터의 매핑입니다.\r\n" + 
			"\r\n" + 
			"파라미터는 프로시저<storedProcedure>마다 <param> 태그가 정의된 순서대로 인덱스(번호)를 가지며\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"		<params>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>성능명</paramName>\r\n" + 
			"				<paramExample>Voltage</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>성능 번호</paramName>\r\n" + 
			"				<paramExample>1</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>단위</paramName>\r\n" + 
			"				<paramExample>V</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"		</params>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위의 경우를 예로 들면\r\n" + 
			"\r\n" + 
			"성능명 파라미터가 [param1], \r\n" + 
			"성능 번호 파라미터가 [param2],\r\n" + 
			"단위 파라미터가 [param3]\r\n" + 
			"\r\n" + 
			"입니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위의 파라미터를 프로시저의 쿼리 태그( <query> )에 아래처럼 \r\n" + 
			"\r\n" + 
			"[param1], [param2], [param3] 으로 매핑 할 수 있습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"	 SELECT * FROM SERVER_PERF \r\n" + 
			"\r\n" + 
			"	WHERE \r\n" + 
			"\r\n" + 
			"	strDisplayName = [param1] AND\r\n" + 
			"\r\n" + 
			"	nPerfIndex = [param2] AND\r\n" + 
			"\r\n" + 
			"	strMeasure = [param3]\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위처럼 매핑하면 사용자는 해당 프로시저를 수행 할 때 마다\r\n" + 
			"\r\n" + 
			"성능명, 성능번호, 성능의 단위를 입력하고 조건에 맞는 항목을 조회 할 수 있습니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 09 ] Stored Procedure Query 작성시 주의사항  : 부등호( >, >= , = , <= , < ) 에러\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로시저를 수행하면 아래와 같이 예외 메세지를 출력하고 수행하지 못하는 경우가 있습니다.\r\n" + 
			"\r\n" + 
			"\"Exception Message : 요소 콘텐츠는 올바른 형식의 문자 데이터 또는 마크업으로 구성되어야 합니다\"\r\n" + 
			"\r\n" + 
			"해당 에러의 원인은 쿼리 내용에 부등호( >, >= , = , <= , < 등)가 존재하기 때문입니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"쿼리 내부에 있는 부등호를 프로그램에서 태그( <query> , <param> 등)의 시작 또는 끝으로 인식하여\r\n" + 
			"\r\n" + 
			"에러가 발생하는 것 입니다.\r\n" + 
			"\r\n" + 
			"해당 에러의 해결 방법으로는 쿼리 내부의 부등호를 특별한 명령어(<![CDATA[ 부등호 ]]>)로 감싸면 됩니다.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"쿼리 내부 부등호 작성 방법 : <![CDATA[ 부등호 ]]>\r\n" + 
			"\r\n" + 
			"아래의 쿼리 내용 : 서버 인덱스가 10 이상인 항목을 조회\r\n" + 
			"적용 전 (에러 발생) : SELECT * FROM SERVERINFO WHERE nServerIndex >= 10\r\n" + 
			"적용 후 (에러 해결) : SELECT * FROM SERVERINFO WHERE nServerIndex <![CDATA[ >= ]]> 10\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"정 리\r\n" + 
			"\r\n" + 
			"적용 전 (에러 발생)\r\n" + 
			"~이상 : >=\r\n" + 
			"~초과 : >\r\n" + 
			"같다 : =\r\n" + 
			"미만 : <\r\n" + 
			"이하 : <=\r\n" + 
			"\r\n" + 
			"적용 후 (에러 해결)\r\n" + 
			"~이상 : <![CDATA[ >= ]]>\r\n" + 
			"~초과 : <![CDATA[ > ]]>\r\n" + 
			"같다 : <![CDATA[ = ]]>\r\n" + 
			"미만 : <![CDATA[ < ]]>\r\n" + 
			"이하 : <![CDATA[ <= ]]>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"위와 같은 이유로 기본 프로시저 템플릿(.xml) 파일에 <query> 태그 내용은\r\n" + 
			"<![CDATA[   ]]> 명령어가 쿼리 내용(<query> 태그 내용)을 감싸는 형태로 제공됩니다.\r\n" + 
			"\r\n" + 
			"( <query> 태그 내용 전체를 <![CDATA[   ]]> 명령어가 감싸기 때문에 쿼리 내용에 부등호를 자유롭게 사용 할 수 있습니다. )\r\n" + 
			"\r\n" + 
			"────────────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			" 프로그램 관련 문의사항은\r\n" + 
			"\r\n" + 
			" Moon(정창용)에게 부탁드립니다.\r\n" + 
			"";
}// end Constructor




class ProcedureCellRenderer extends DefaultTableCellRenderer {

	private String searchName = null;

	public ProcedureCellRenderer(String search) {
		this.searchName = search;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		String procedureName = table.getValueAt(row, 1).toString();

		try {

			if (searchName.length() < 1 || searchName.equalsIgnoreCase("")) {
				// 검색어가 존재하지 않을 경우
				if (isSelected) {				
					c.setBackground(table.getSelectionBackground());
					c.setForeground(table.getSelectionForeground());
					return c;
				}else{
					c.setFont(FontManager.getFont(Font.PLAIN, 15));
					c.setForeground(new Color(0, 0, 0));
					c.setBackground(Color.WHITE);
					return c;
				}
				
			}else {
				boolean result = procedureName.toUpperCase().contains(searchName.toUpperCase());

				if (result) {
					
					// ★ 검색 문자열을 포함할 경우
					if (isSelected) {
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					} else {
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setBackground(Color.GREEN);
						c.setForeground(Color.BLACK);
						return c;
					}
					
				}else {
					
					// 검색 문자열을 포함하지 않을 경우
					if (isSelected) {				
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					}else{
						c.setFont(FontManager.getFont(Font.PLAIN, 15));
						c.setForeground(new Color(0, 0, 0));
						c.setBackground(Color.WHITE);
						return c;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return c;
		}
		
	}

}
