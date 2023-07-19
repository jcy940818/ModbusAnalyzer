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
	private static JPanel queryInputPanel; // ���� ��� ����, �Լ� ���� ����� �����ϴ� �ǳ�	
	private JPanel formSendQueryPanel; // ���� ��� ���� �ǳ�
	private JPanel functionSendQueryPanel; // ���� �Լ� ���� �ǳ�
		
	public static JRadioButton FormSend_radioButton; // ���� ���� ��� : ��� ���� �ǳ� ���� ���� ��ư
	public static JRadioButton ProcedureSend_radioButton; // ���� ���� ��� : �Լ� ���� �ǳ� ���� ���� ��ư
	
	private static JTable table; // ����  ���ν��� ����Ʈ ���̺�
	public static JComboBox procedureCategory_ComboBox; // ���ν��� ī�װ� ���� �޺��ڽ�
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
		// �̹��� ��� �� Ŭ���� ��η� ����Ͽ� �����Ͽ����� �̹����� �����ǵ��� ����				
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
					// ���� ���ν��� xml �ε��� ���� �߻��� �ƹ��͵� �������� �ʴ´�
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
				new MessageFrame("<html>" + Util.colorBlue("Stored Procedure") + " ��� ����</html>", description);
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
		
		// ���� ��� ���� ���� ��ư
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
		
		// ���ν��� ���� ���� ��ư
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
		
		// ���ν��� ī�װ� �޺��ڽ�
		procedureCategory_ComboBox = new JComboBox();
		procedureCategory_ComboBox.setForeground(Color.BLACK);
//		procedureCategory_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"����","�ü���", "RCU", "����", "�̺�Ʈ", "�̺�Ʈ ����", "��� ������"}));
		procedureCategory_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"Common","Facility", "RCU", "Performance", "Event", "Event History", "Statistics Data"}));
		procedureCategory_ComboBox.setSelectedIndex(0);
		procedureCategory_ComboBox.setFont(FontManager.getFont(Font.BOLD, 16));
		procedureCategory_ComboBox.setBackground(Color.WHITE);
		procedureCategory_ComboBox.setBounds(301, 26, 280, 32);
		procedureCategory_ComboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		procedureCategory_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
										
				JComboBox temp = (JComboBox)e.getSource();
				String tableName = temp.getSelectedItem().toString(); // �˻� ���̺� �޺��ڽ����� ���õ� item�� ���ڿ�
				
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
										
					
					// ī�װ��� ���õǸ� ù��° �� ����
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
					
					// ���ν��� ���� �� ����ڰ� �Է��� �Ķ���� ������ ���� , �Ķ���� ������ ������ �߻��ϸ� false
					boolean ret = StoredProcedure.mappingParamter(selectedProcedure);				
									
					System.out.println("================================================================");
					System.out.println(selectedProcedure.getExecuteQuery());
					System.out.println("================================================================");
					
					if(ret) DbUtil.executeProcedure(ONION_Info.getSqlServerInfo(), selectedProcedure.getExecuteQuery());	
					
				}catch(Exception exception) {
					// ���ν��� ������ �߻��� ���ܸ� ��� ó��
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
					// ���ν��� ����
					
					if(!procedureFile.exists()) {
						// ��λ󿡼� ���ν��� ������ ã�� �� ���� ���
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
							// ���� ���� ����
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
							// ���� ���� ����
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
					// ���ν��� ���� ���
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
					
			if(ProcedureSend_radioButton != null)ProcedureSend_radioButton.setSelected(true); // Query ���ν��� ���� ���� ��ư						
			if(querySendFormCardLayout != null)querySendFormCardLayout.show(queryInputPanel, "FormSendQueryPanel"); // Query ��� ���� �Է� �ǳ�
			if(procedureCategory_ComboBox != null)procedureCategory_ComboBox.setSelectedIndex(0); // ���ν��� ī�װ� �޺��ڽ�
			if(viewPanelCardLayout != null)viewPanelCardLayout.show(viewPanel, "viewPanel_Form");
													
		}catch(Exception e) {			
			e.printStackTrace();
		}
	}
	
	
	// ���� �Լ� ���� �ǳ� ������Ʈ �ʱ�ȭ
	public void resetQueryFunctionPanel() {
			
	}
	
	
	public static void setSqlServerInfo(String sqlServerInfo) {
		StoredProcedure_Panel.sqlServerInfo.setText(sqlServerInfo);
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
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
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
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(380);
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
		
		
		// Expression Check (���ǽ��� �����ϴ� ���� ǥ��)				
		String searchName = procedureSearch_textField.getText().trim();
		searchName = searchName.toLowerCase();
		
		ProcedureCellRenderer procedureCellRenderer = new ProcedureCellRenderer(searchName);
		
		tcmSchedule.getColumn(1).setCellRenderer(procedureCellRenderer); // �˻� ���
	}
	
	
	public static void updateTable(JTable table, ArrayList<StoredProcedure> procedureList) {

		if ((table == null) || (procedureList == null)) {
			return;
		}

		// ���̺� ��� ����
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));

		Object[][] content = new Object[procedureList.size()][];

		for (int i = 0; i < procedureList.size(); i++) {
			content[i] = new Object[2];
			content[i][0] = procedureList.get(i).getIndex() + 1; // �� ��
			content[i][1] = procedureList.get(i); // ���ν��� �ν��Ͻ��� ���������� ���´�
		}

		table.setModel(new DefaultTableModel(content, new String[] { "Index", "Stored Procedure" }) {
			// ���̺� �� ���� ���� ����
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
			// StoredProcedure.useParam : true (�Ķ���͸� ����ϴ� ���ν����� ���)
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
			// StoredProcedure.useParam : false (���ν����� �Ķ���͸� ������� �ʴ� ���)
			sb.append(String.format("Parameter : %s%s", "not use", System.lineSeparator()));
		}
		
		sb.append(System.lineSeparator());		
		sb.append(sp.getContent());
		procedureDiscription_textPane.setText(sb.toString());
	}
	
	public void selectFirstCell(JTable table) {
		// ī�װ��� ���õǸ� ���ν��� ���̺��� ù��° �� ����
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
				// ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// ���� ��ư ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// ������ Ŭ��
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
				// ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// ���� ��ư ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// ������ Ŭ��
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
				// ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			} 
			if (e.getButton() == 1 && e.getClickCount() == 2) {
				// ���� ��ư ���� Ŭ��
				StoredProcedure sp = (StoredProcedure)table.getValueAt(table.getSelectedRow(), 1);
				selectProcedure(sp);
			}
			if (e.getButton() == 3) {
				// ������ Ŭ��
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
			"[ #. 01 ] Stored Procedure ����̶�?\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Stored Procedure ����� ���� ���Ǵ� ������(���ν���)�� .xml ���Ϸ� ���� �� �����ϸ�\r\n" + 
			"\r\n" + 
			"����ڰ� ��Ȳ�� �´� �Ķ����(����)�� �Է��Ͽ�\r\n" + 
			"\r\n" + 
			"��������(Dynamic) ������ ��� �� �� �ֵ��� ���ִ� ����Դϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 02 ] Stored Procedure ���丮 ������ġ\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer ���� ����(.exe) ����� storedProcedure ���丮 �Ʒ�����\r\n" + 
			"\r\n" + 
			"�� ���ν��� ������(���丮��)�� �����ϰ� �˴ϴ�.\r\n" + 
			"\r\n" + 
			"���� �����ϴ� ���ν��� ���丮 ������ �Ʒ��� �����ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			" 1. ���� ���ν���     ( COMMON ���丮 )\r\n" + 
			" 2. �ü��� ���� ���ν���     ( SERVERINFO_FACILITY ���丮 )\r\n" + 
			" 3. RCU ���� ���ν���     ( SERVERINFO_RTU ���丮 )\r\n" + 
			" 4. ���� ���� ���ν���     ( SERVER_PERF ���丮 )\r\n" + 
			" 5. �̺�Ʈ ���� ���ν���     ( ALARM ���丮 )\r\n" + 
			" 6. �̺�Ʈ ���� ���� ���ν���     ( EVENTS ���丮 )\r\n" + 
			" 7. ��� ������ ���� ���ν���     ( SERVER_PERFREPORT ���丮 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ������ ������ Ʈ���� ǥ�����ڸ� �Ʒ��� �����ϴ�\r\n" + 
			"\r\n" + 
			"  [ ModbusAnalyzer ���� ����(.exe) ��� ]\r\n" + 
			"              ��\r\n" + 
			"  [ storedProcedure ]\r\n" + 
			"              ��\r\n" + 
			"              �� [ COMMON ]                   �� ���� ���ν����� ����\r\n" + 
			"              �� [ SERVERINFO_FACILITY ]    �� �ü��� ���� ���ν����� ����\r\n" + 
			"              �� [ SERVERINFO_RTU ]          �� RCU ���� ���ν����� ����\r\n" + 
			"              �� [ SERVER_PERF ]               �� ���� ���� ���ν����� ����\r\n" + 
			"              �� [ ALARM ]                       �� �̺�Ʈ ���� ���ν����� ����\r\n" + 
			"              �� [ EVENTS ]                      �� �̺�Ʈ ���� ���� ���ν����� ����\r\n" + 
			"              �� [ SERVER_PERFREPORT ]     �� ��� ������ ���� ���ν����� ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���ν��� ���丮�� �������� �����ϴ� ������ ������ �������� �����̸�\r\n" + 
			"\r\n" + 
			"���α׷����� ���ν����� �����ϴ� �������� ������ ���� �ʽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 03 ] Stored Procedure �ε�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer ���α׷����� Stored Procedure �ε� �۾��̶�\r\n" + 
			"\r\n" + 
			"���ν��� ���丮 ���ο� �����ϴ� .xml ������ �˻��Ͽ�\r\n" + 
			"\r\n" + 
			"���� ������ ���ν��� �ν��Ͻ��� �޸𸮿� �����ϴ� ������ �ǹ��մϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���α׷� ���� �� ���ν���(.xml ����) ������ �߰� �Ǵ� �����Ǿ��ٸ�\r\n" + 
			"\r\n" + 
			"Stored Procedure �������� [ XML Reload ] ��ư���� ���ν����� �ٽ� �ε� �� �� �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 04 ] Stored Procedure �ε� ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Stored Procedure �ε��� ������ �߻��ϴ� ���� ũ�� �Ʒ��� �����ϴ�.\r\n" + 
			"\r\n" + 
			" 1. ModbusAnalyzer ���� ����(.exe) ��ο� storedProcedure ���丮�� �������� ���� ���\r\n" + 
			"\r\n" + 
			" 2. storedProcedure ���丮 ��ο� [ #. 02 ] ���� ����� ī�װ��� ���丮�� �������� �ʴ� ���\r\n" + 
			"\r\n" + 
			" 3. ���ν��� .xml ���� ���뿡 ������ ���� ���\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"1, 2 ���� ���( ��λ� �ε� �۾��� �ʿ��� ���丮�� �������� ���� ��� ) \r\n" + 
			"\r\n" + 
			"�������� �ʴ� ���丮�� �����ϰ� ���������� �����ϴ� ���丮�� �ε� �� �� ������,\r\n" + 
			"\r\n" + 
			"����ڿ��� ���� ���ν��� ���丮 ��θ� �Է¹޾� ���ν��� ���丮 ��θ� ���� �� ���� �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 05 ] Stored Procedure XML ���� \r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�� ���ν��� ���丮�� �����ϴ� ���ν��� ���� ����(.xml) �Դϴ�.\r\n" + 
			"\r\n" + 
			"ModbusAnalyzer ���α׷��� [ #. 02 ] ���� ������ ��ο� ��ġ�ϴ� XML ������ �˻��Ͽ�\r\n" + 
			"\r\n" + 
			"���� ������ ���ν��� �ν��Ͻ��� �޸𸮿� �����մϴ�. ( [ #. 03 ] )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���ν��� ����(.xml)�� �̸����� ������ ��Ģ�� ������\r\n" + 
			"\r\n" + 
			"���ν��� ����(.xml)�� ������ �ݵ�� �Ʒ��� ������ �ؼ��ؾ� �մϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<storedProcedureInfo>\r\n" + 
			"	<storedProcedure>\r\n" + 
			"		<category>SERVERINFO_FACILITY</category>\r\n" + 
			"		<name>���ν��� �̸�</name>\r\n" + 
			"		<content>���ν����� ���� ����</content>\r\n" + 
			"		<query>���ν��� ����� �����ϴ� ����</query>		\r\n" + 
			"		<params>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>�Ķ���� 1</paramName>\r\n" + 
			"				<paramExample>�Ķ���� 1 �Է� ����</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>�Ķ���� 2</paramName>\r\n" + 
			"				<paramExample>�Ķ���� 2 �Է� ����</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>�Ķ���� 3</paramName>\r\n" + 
			"				<paramExample>�Ķ���� 3 �Է� ����</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"		</params>\r\n" + 
			"	</storedProcedure>\r\n" + 
			"</storedProcedureInfo>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 06 ] Stored Procedure XML ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<storedProcedureInfo> �±� ���ο� \r\n" + 
			"\r\n" + 
			"<storedProcedure> �±׷� �ϳ��� ���ν����� �����ϸ�\r\n" + 
			"\r\n" + 
			"<storedProcedure> �±��� ����(���ν��� �ν��Ͻ��� �ʵ�)�� �Ʒ��� �����ϴ�.\r\n" + 
			"\r\n" + 
			"1. <category> - ���ν��� ����\r\n" + 
			"2. <name> - ���ν��� �̸�\r\n" + 
			"3. <content> - ���ν����� ���� ����\r\n" + 
			"4. <query> - ������ ���� �� ����\r\n" + 
			"5. <params> - ���ν����� ��� �� �Ķ������ �������� �ڽ� �±׷� <param> �� ������ �ֽ��ϴ�.\r\n" + 
			"6. <param> - <params> �±��� �ڽ� �±��̸� <paramName>, <paramExample> �� ������ �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			" ! ����\r\n" + 
			"  ���ν����� ������ �Ķ���Ͱ� ������ �ʴ´ٸ� <param> �±״� �������� �ʾƵ� ���������\r\n" + 
			"  �Ķ���� ��� ���ο� ������� <params> �±״� �����ؾ� �մϴ�.\r\n" + 
			"  \r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 07 ] Stored Procedure XML ���� 2 : �Ķ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���ν����� �Ķ���Ͷ� ���� ���� ���࿡ �ʿ��� �Ű� ������ �ǹ��մϴ�.\r\n" + 
			"\r\n" + 
			"������� �Ʒ�ó�� ���� ��ȣ�� 1���� ������ ��ȸ�ϴ� ������ �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			" \"SELECT * FROM SERVER_PERF WHERE nPerfIndex = 1\"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ������ �׻� ���� ��ȣ�� 1���� �׸��� ��ȸ �� �� �Դϴ�.\r\n" + 
			"\r\n" + 
			"������ ���� ��ȣ��� �� �Ķ���͸� ��Ȳ�� �°� ����ڰ� ���� �Է� �� �� �ִٸ�\r\n" + 
			"\r\n" + 
			"����ڴ� ������ ���ϴ� ���� ��ȣ�� ���� �׸��� ��ȸ �� �� ���� �� �Դϴ�.\r\n" + 
			"\r\n" + 
			" \"SELECT * FROM SERVER_PERF WHERE nPerfIndex = n\"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���ν��� XML ���Ͽ��� \r\n" + 
			"\r\n" + 
			"�Ķ���ʹ� <params> �±��� �ڽ� �±׷� �����մϴ�.\r\n" + 
			"\r\n" + 
			"�� <param> �±� �ϳ��� �Ķ���� 1���� �ǹ��ϸ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"<param> �±״� \r\n" + 
			"\r\n" + 
			"�Ķ���� �̸� ( <paramName> ), \r\n" + 
			"�Ķ���� �Է� ���� ( <paramExample> )�� �ڽ� �±׷� �����ϴ�.\r\n" + 
			"\r\n" + 
			"�Ķ���� �̸� �±�( <paramName> )�� �ʼ� �Է� �׸��̸�, \r\n" + 
			"�Ķ���� �Է� ���� �±�( <paramExample> )�� ���� �Է��� �ʼ��� �ƴ����� �ݵ�� �±״� �����ؾ� �մϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 08 ] Stored Procedure XML ���� 3 : �Ķ���� ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 07 ] ���� �Ķ���Ϳ� ���ؼ� �����Ͽ����ϴ�.\r\n" + 
			"\r\n" + 
			"�̹����� �Ķ������ �����Դϴ�.\r\n" + 
			"\r\n" + 
			"�Ķ���ʹ� ���ν���<storedProcedure>���� <param> �±װ� ���ǵ� ������� �ε���(��ȣ)�� ������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"		<params>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>���ɸ�</paramName>\r\n" + 
			"				<paramExample>Voltage</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>���� ��ȣ</paramName>\r\n" + 
			"				<paramExample>1</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"			<param>\r\n" + 
			"				<paramName>����</paramName>\r\n" + 
			"				<paramExample>V</paramExample>				\r\n" + 
			"			</param>\r\n" + 
			"		</params>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ��츦 ���� ���\r\n" + 
			"\r\n" + 
			"���ɸ� �Ķ���Ͱ� [param1], \r\n" + 
			"���� ��ȣ �Ķ���Ͱ� [param2],\r\n" + 
			"���� �Ķ���Ͱ� [param3]\r\n" + 
			"\r\n" + 
			"�Դϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� �Ķ���͸� ���ν����� ���� �±�( <query> )�� �Ʒ�ó�� \r\n" + 
			"\r\n" + 
			"[param1], [param2], [param3] ���� ���� �� �� �ֽ��ϴ�.\r\n" + 
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
			"��ó�� �����ϸ� ����ڴ� �ش� ���ν����� ���� �� �� ����\r\n" + 
			"\r\n" + 
			"���ɸ�, ���ɹ�ȣ, ������ ������ �Է��ϰ� ���ǿ� �´� �׸��� ��ȸ �� �� �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"[ #. 09 ] Stored Procedure Query �ۼ��� ���ǻ���  : �ε�ȣ( >, >= , = , <= , < ) ����\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���ν����� �����ϸ� �Ʒ��� ���� ���� �޼����� ����ϰ� �������� ���ϴ� ��찡 �ֽ��ϴ�.\r\n" + 
			"\r\n" + 
			"\"Exception Message : ��� �������� �ùٸ� ������ ���� ������ �Ǵ� ��ũ������ �����Ǿ�� �մϴ�\"\r\n" + 
			"\r\n" + 
			"�ش� ������ ������ ���� ���뿡 �ε�ȣ( >, >= , = , <= , < ��)�� �����ϱ� �����Դϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ���ο� �ִ� �ε�ȣ�� ���α׷����� �±�( <query> , <param> ��)�� ���� �Ǵ� ������ �ν��Ͽ�\r\n" + 
			"\r\n" + 
			"������ �߻��ϴ� �� �Դϴ�.\r\n" + 
			"\r\n" + 
			"�ش� ������ �ذ� ������δ� ���� ������ �ε�ȣ�� Ư���� ��ɾ�(<![CDATA[ �ε�ȣ ]]>)�� ���θ� �˴ϴ�.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ���� �ε�ȣ �ۼ� ��� : <![CDATA[ �ε�ȣ ]]>\r\n" + 
			"\r\n" + 
			"�Ʒ��� ���� ���� : ���� �ε����� 10 �̻��� �׸��� ��ȸ\r\n" + 
			"���� �� (���� �߻�) : SELECT * FROM SERVERINFO WHERE nServerIndex >= 10\r\n" + 
			"���� �� (���� �ذ�) : SELECT * FROM SERVERINFO WHERE nServerIndex <![CDATA[ >= ]]> 10\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�� ��\r\n" + 
			"\r\n" + 
			"���� �� (���� �߻�)\r\n" + 
			"~�̻� : >=\r\n" + 
			"~�ʰ� : >\r\n" + 
			"���� : =\r\n" + 
			"�̸� : <\r\n" + 
			"���� : <=\r\n" + 
			"\r\n" + 
			"���� �� (���� �ذ�)\r\n" + 
			"~�̻� : <![CDATA[ >= ]]>\r\n" + 
			"~�ʰ� : <![CDATA[ > ]]>\r\n" + 
			"���� : <![CDATA[ = ]]>\r\n" + 
			"�̸� : <![CDATA[ < ]]>\r\n" + 
			"���� : <![CDATA[ <= ]]>\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ���� ������ �⺻ ���ν��� ���ø�(.xml) ���Ͽ� <query> �±� ������\r\n" + 
			"<![CDATA[   ]]> ��ɾ ���� ����(<query> �±� ����)�� ���δ� ���·� �����˴ϴ�.\r\n" + 
			"\r\n" + 
			"( <query> �±� ���� ��ü�� <![CDATA[   ]]> ��ɾ ���α� ������ ���� ���뿡 �ε�ȣ�� �����Ӱ� ��� �� �� �ֽ��ϴ�. )\r\n" + 
			"\r\n" + 
			"����������������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			" ���α׷� ���� ���ǻ�����\r\n" + 
			"\r\n" + 
			" Moon(��â��)���� ��Ź�帳�ϴ�.\r\n" + 
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
				// �˻�� �������� ���� ���
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
					
					// �� �˻� ���ڿ��� ������ ���
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
					
					// �˻� ���ڿ��� �������� ���� ���
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
