package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.FontManager;
import src_ko.info.Protocol;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class ProtocolList_Panel extends JPanel {
	
	public static File xmlDir;
	
	private JPanel infoPanel;
	private static JComboBox protocolType_comboBox;
	private static JComboBox facilityType_comboBox;
	
	private static ArrayList<String> facTypeList;	
	public static ArrayList<Protocol> protocols = null;
	public static ArrayList<Protocol> selectedProtocols = null;
	public static Protocol selctedProtocol = null;
	
	public static JButton languageButton;
	public static boolean isKorean = true;
	private static JTextField seartFacility_textField;
	private JLabel searchProtocol_Label;
	private static JTextField searchProtocol_textField;
	
	public static JPanel actualPanel;
	private static JTable table;		
	public static JButton goXmlViewer;
	private JButton openXmlFile;
	private static JLabel protocolVersion;
	
	/**
	 * Create the panel.
	 */
	public ProtocolList_Panel() {
		setBorder(new EmptyBorder(0, 0, 0, 0));

		// size : 1074, 628
		setSize(1074, 628);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		actualPanel = new JPanel();
		actualPanel.setBackground(new Color(255, 140, 0));
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);

		infoPanel = new JPanel();
		infoPanel.setBounds(12, 10, 1050, 606);
		actualPanel.add(infoPanel);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null);

		JLabel currentFunction = new JLabel("Protocol List");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 220, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		JLabel protocolType_Label = new JLabel("�������� Ÿ��");
		protocolType_Label.setHorizontalAlignment(SwingConstants.LEFT);
		protocolType_Label.setForeground(Color.BLACK);
		protocolType_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		protocolType_Label.setBackground(Color.WHITE);
		protocolType_Label.setBounds(232, 0, 120, 55);
		infoPanel.add(protocolType_Label);
		
		protocolType_comboBox = new JComboBox();
		protocolType_comboBox.setForeground(Color.BLACK);
		protocolType_comboBox.setBackground(Color.WHITE);
		protocolType_comboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		protocolType_comboBox.setBounds(358, 12, 157, 35);
		protocolType_comboBox.setModel(new DefaultComboBoxModel(new String[] {"PROTOCOL", "SNMP"}));
		protocolType_comboBox.setSelectedIndex(0);		
		protocolType_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				searchProtocol_textField.setText(null);
				tableReload(isKorean);
			}
		});
		infoPanel.add(protocolType_comboBox);
		
		JLabel facilityType_Label = new JLabel("�ü��� ����");
		facilityType_Label.setHorizontalAlignment(SwingConstants.LEFT);
		facilityType_Label.setForeground(Color.BLACK);
		facilityType_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		facilityType_Label.setBackground(Color.WHITE);
		facilityType_Label.setBounds(550, 0, 100, 55);
		infoPanel.add(facilityType_Label);
		
		facilityType_comboBox = new JComboBox();		
		facilityType_comboBox.setForeground(Color.BLACK);
		facilityType_comboBox.setFont(FontManager.getFont(Font.BOLD, 16));
		facilityType_comboBox.setBackground(Color.WHITE);
		facilityType_comboBox.setBounds(780, 12, 258, 35);
		facilityType_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchProtocol_textField.setText(null);
				tableReload(isKorean);
			}
		});
		infoPanel.add(facilityType_comboBox);
		
		seartFacility_textField = new JTextField();
		seartFacility_textField.addFocusListener(Util.focusListener);
		seartFacility_textField.setHorizontalAlignment(SwingConstants.LEFT);
		seartFacility_textField.setForeground(Color.BLACK);
		seartFacility_textField.setFont(FontManager.getFont(Font.PLAIN, 16));
		seartFacility_textField.setBounds(654, 12, 120, 35);
		seartFacility_textField.setColumns(10);
		seartFacility_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				String keyword = seartFacility_textField.getText().toUpperCase().trim();
				
				for(int i = 0; i < facTypeList.size(); i++) {
					String item = facTypeList.get(i);
					if(item.toUpperCase().contains(keyword)) {
						facilityType_comboBox.getModel().setSelectedItem(item);
						return;
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {
				String keyword = seartFacility_textField.getText().toUpperCase().trim();
				
				for(int i = 0; i < facTypeList.size(); i++) {
					String item = facTypeList.get(i);
					if(item.toUpperCase().contains(keyword)) {
						facilityType_comboBox.getModel().setSelectedItem(item);
						return;
					}
				}				
			}
		});
		infoPanel.add(seartFacility_textField);
		
		protocolVersion = new JLabel("�������� ���� : ");
		protocolVersion.setHorizontalAlignment(SwingConstants.LEFT);
		protocolVersion.setForeground(Color.BLACK);
		protocolVersion.setFont(FontManager.getFont(Font.BOLD, 18));
		protocolVersion.setBackground(Color.WHITE);
		protocolVersion.setBounds(15, 54, 1023, 41);
		infoPanel.add(protocolVersion);
		
		searchProtocol_Label = new JLabel("�������� �˻�");
		searchProtocol_Label.setHorizontalAlignment(SwingConstants.LEFT);
		searchProtocol_Label.setForeground(Color.BLACK);
		searchProtocol_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		searchProtocol_Label.setBackground(Color.WHITE);
		searchProtocol_Label.setBounds(15, 94, 120, 41);
		infoPanel.add(searchProtocol_Label);
		
		searchProtocol_textField = new JTextField("");
		searchProtocol_textField.addFocusListener(Util.focusListener);
		searchProtocol_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchProtocol_textField.setForeground(Color.BLACK);
		searchProtocol_textField.setFont(FontManager.getFont(Font.PLAIN, 16));
		searchProtocol_textField.setColumns(10);
		searchProtocol_textField.setBounds(138, 97, 454, 35);
		searchProtocol_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					String text = searchProtocol_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						tableReload(isKorean);
					}else {					
						doTableFilter(text, isKorean);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					String text = searchProtocol_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						tableReload(isKorean);
					}else {					
						doTableFilter(text, isKorean);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchProtocol_textField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setBounds(12, 140, 1026, 457);
		infoPanel.add(scrollPane);
		
		table = new JTable();		
		table.setForeground(Color.BLACK);
		table.addFocusListener(new FocusListener() {			
			public void focusLost(FocusEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();					
					getSelectedProtocol(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void focusGained(FocusEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();					
					getSelectedProtocol(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();					
					getSelectedProtocol(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
						
			public void keyReleased(KeyEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					getSelectedProtocol(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // ���� Ŭ��
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// ���� ��ư ���� Ŭ��
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					showFunction(getSelectedProtocol(number, facType));
				}
				if (e.getButton() == 3) {
					// ������ Ŭ��
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					showFunction(getSelectedProtocol(number, facType));		
				}
			}
		});
		scrollPane.setViewportView(table);
		
		languageButton = new JButton("�ѱ۸�");		
		languageButton.setBounds(599, 96, 97, 35);		
		languageButton.setForeground(Color.BLACK);
		languageButton.setBackground(Color.WHITE);
		languageButton.setFont(FontManager.getFont(Font.BOLD, 16));
		languageButton.setFocusPainted(false);		
		languageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String language = languageButton.getText();
				
				if(language.contains("�ѱ�")) {
					languageButton.setText("������");
					isKorean = false;
					tableReload(isKorean);
				}else {
					languageButton.setText("�ѱ۸�");
					isKorean = true;
					tableReload(isKorean);
				}
				
			}
		});
		infoPanel.add(languageButton);
		
		goXmlViewer = new JButton("XML Viewer ����");
		goXmlViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showXmlViewer();
			}
		});
		goXmlViewer.setForeground(Color.BLUE);
		goXmlViewer.setFont(FontManager.getFont(Font.BOLD, 15));
		goXmlViewer.setFocusPainted(false);
		goXmlViewer.setBackground(Color.WHITE);
		goXmlViewer.setBounds(728, 96, 157, 35);
		infoPanel.add(goXmlViewer);
		
		openXmlFile = new JButton("XML ���� ����");
		openXmlFile.setForeground(new Color(0, 128, 0));
		openXmlFile.setFont(FontManager.getFont(Font.BOLD, 15));
		openXmlFile.setFocusPainted(false);
		openXmlFile.setBackground(Color.WHITE);
		openXmlFile.setBounds(891, 96, 147, 35);
		openXmlFile.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();					
					Protocol p = getSelectedProtocol(number, facType);
				
					String xmlPath = xmlDir.getPath() + "\\" + p.getXml();
					File xmlFile = new File(xmlPath);
					if(!xmlFile.exists()) {
						StringBuilder msg = new StringBuilder();
						msg.append(Util.colorRed("XML file not found"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
						
						msg.append("�Ʒ��� ��ο��� XML ������ ã�� �� �����ϴ�");
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						
						msg.append("XML ���� ��� : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						msg.append("XML ���� �̸� : " + Util.colorRed(xmlFile.getName()));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
						
						Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
					}else {
						FileUtil.editFile(xmlFile);
					}		
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(openXmlFile);
	}
	
	
	public static void doTableFilter(String text, boolean isKorean) {
		text = text.toUpperCase().trim();
		ArrayList<Protocol> filterProtocol = new ArrayList<Protocol>();
		
		for(int i = 0; i < selectedProtocols.size(); i++) {
			Protocol p = selectedProtocols.get(i);			
			
			String number = String.valueOf(p.getNumber()).toUpperCase();
			String facType = p.getFacType().toUpperCase();						
			String name = (isKorean) ? p.getName() : p.getEnName();
			if(name != null) {
				name = name.toUpperCase();
			}else {
				name = "";
			}			
			String xml = p.getXml().toUpperCase();
			
			if(text.contains(",")) {
				String[] textToken = text.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2].trim();
					if(number.contains(token) || facType.contains(token) || name.contains(token) || xml.contains(token)) {
						filterProtocol.add(p);
					}
				}
			}else if(number.contains(text) || facType.contains(text) || name.contains(text) || xml.contains(text)) {
				filterProtocol.add(p);
			}
		}
		
		Object[][] content = new Object[filterProtocol.size()][];

		for (int i = 0; i < filterProtocol.size(); i++) {
			Protocol p = filterProtocol.get(i);
			content[i] = new Object[4];
			content[i][0] = p.getNumber();
			content[i][1] = p.getFacType();
			content[i][2] = (isKorean) ? p.getName() : p.getEnName();
			content[i][3] = p.getXml();
		}

		table.setModel(new DefaultTableModel(
			content, 			
			new String[] { "�� ȣ", "�ü��� ����", "��������", "���� XML" }) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
	
	public static void tableReload(boolean isKorean) {				
		String protocolType = protocolType_comboBox.getSelectedItem().toString();				
		String facType = facilityType_comboBox.getSelectedItem().toString();
		
		if(!facType.equalsIgnoreCase("�� ü")) {
			String[] tokens = facType.split(" ");
			facType = "";
			
			for(int i = 1; i < tokens.length; i++) {
				facType += tokens[i] + " ";
			}
			
			facType = facType.trim();
		}
		
		setTableContent(protocolType, facType, ProtocolList_Panel.protocols, isKorean);
	}
	
	
	
	public static void setTableContent(String protocolType, String facType, ArrayList<Protocol> protocols, boolean isKorean) {
		selectedProtocols = null;
		selectedProtocols = new ArrayList<Protocol>();
				
		for(int i = 0; i < protocols.size(); i++) {
			Protocol p = protocols.get(i);			
			
			if(protocolType == null) { // �������� Ÿ�� �˻�
				if(facType.equalsIgnoreCase("�� ü") || facType.equalsIgnoreCase(p.getFacType())) { // �ü��� ���� �˻�
					selectedProtocols.add(p);
				}
				
			}else if(protocolType.equalsIgnoreCase("PROTOCOL") && p.getProtocolType() == Protocol.PROTOCOL) {
				if(facType.equalsIgnoreCase("�� ü") || facType.equalsIgnoreCase(p.getFacType())) {
					selectedProtocols.add(p);
				}
				
			}else if(protocolType.equalsIgnoreCase("SNMP") && p.getProtocolType() == Protocol.SNMP) {
				if(facType.equalsIgnoreCase("�� ü") || facType.equalsIgnoreCase(p.getFacType())) {
					selectedProtocols.add(p);
				}
			}
		}
				
		updateTable(table, isKorean);
	}
	
	
	public static void updateTable(JTable table, boolean isKorean) {		

		if (table == null || selectedProtocols == null)
			return;

		Object[][] content = new Object[selectedProtocols.size()][];

		for (int i = 0; i < selectedProtocols.size(); i++) {
			Protocol p = selectedProtocols.get(i);
			content[i] = new Object[4];
			content[i][0] = p.getNumber();
			content[i][1] = p.getFacType();
			content[i][2] = (isKorean) ? p.getName() : p.getEnName();
			content[i][3] = p.getXml();
		}

		table.setModel(new DefaultTableModel(
			content, 			
			new String[] { "�� ȣ", "�ü��� ����", "��������", "���� XML" }) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
	
	
	public static void setTableStyle(JTable table) {
		
		// ���̺� ��� ����
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// ���̺� �� ũ�� ����
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // �������� ��ȣ
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // �ü��� ����
		table.getColumnModel().getColumn(2).setPreferredWidth(320); // ��������
		table.getColumnModel().getColumn(3).setPreferredWidth(320); // ��������
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �������� ��ȣ
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �ü��� ����
	}
	
	
	public static void setFacilityComboBox(ArrayList<Protocol> protocols, boolean isKorean) {		
		ArrayList<Integer> facCodeList = new ArrayList<Integer>();
		ArrayList<String> facTypeList = new ArrayList<String>();
		HashMap<Integer, String> sortMap = new HashMap<>();
		
		for(int i = 0; i < protocols.size(); i++) {
			Protocol p = protocols.get(i);
			if(sortMap.containsKey(p.getFacCode())) continue;
			String item = String.format("%02d. %s", p.getFacCode(), p.getFacType());
			sortMap.put(p.getFacCode(), item);
		}
		
		Set<Integer> keys = sortMap.keySet();
		for(int facCode : keys) {
			facCodeList.add(facCode);
		}
		Collections.sort(facCodeList);
		
		facTypeList.add("�� ü");
		for(int i = 0; i < facCodeList.size(); i++) {
			facTypeList.add(sortMap.get(facCodeList.get(i)));
		}
		
		ProtocolList_Panel.facTypeList = facTypeList; // �ü��� ���� �˻� �뵵
		facilityType_comboBox.setModel(new DefaultComboBoxModel(facTypeList.toArray()));
	}
	
	public static Protocol getSelectedProtocol(int number, String facType) {
		boolean isSelected = false;
		
		for(int i = 0; i < selectedProtocols.size(); i++) {
			Protocol p = selectedProtocols.get(i);
			isSelected = (number == p.getNumber()) && (facType.equalsIgnoreCase(p.getFacType()));
			
			if(isSelected) {
				selctedProtocol = p;
				return p;				
			}
		}		
		return null;
	}
	
	
	public static void showFunction(Protocol p) {
		if(p == null) return;
		
		String separator = Util.separator + Util.separator; 
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='Green'>Protocol Information</font>\n");		
		
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("�ü��� ����"), p.getFacType(), separator, separator));
		msg.append(String.format("%s : %d%s%s\n\n", Util.colorBlue("�������� ��ȣ"), p.getNumber(), separator, separator));
		
		String pName = isKorean ? p.getName() : p.getEnName();
		
		if(!isKorean && pName == null && p.getName() != null) {			
			pName = p.getName() + Util.colorRed( " ( ������ ���� )");
		}else if(isKorean && pName == null && p.getEnName() != null) {
			pName = p.getEnName() + Util.colorRed( " ( �ѱ۸� ���� )");
		}
			
		if(pName == null) {
			pName = Util.colorRed("Unknown");
		}
		
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("�������� �̸�"),pName , separator, separator));
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("���� XML"), p.getXml(), separator, separator));
		
		int menu = -1;
		
		if(OnionDirCheck_Panel.agent != null && OnionDirCheck_Panel.agent.equalsIgnoreCase("watchPoint")) {
			menu = Util.showOption(msg.toString(), new String[] { "������ ����Ʈ �߰�", "XML ���� ����"}, JOptionPane.QUESTION_MESSAGE);
		}else {
			menu = Util.showOption(msg.toString(), new String[] { "XML Viewer ����", "XML ���� ����"}, JOptionPane.QUESTION_MESSAGE);	
		}

		switch (menu) {
			case -1: // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� ������ ��		
				return;
				
			case 0: // XML Viewer ����
				// ������ ���� �۾�
				showXmlViewer();
				break;
				
			case 1: // XML ���� �ٷ� ����
				String xmlPath = xmlDir.getPath() + "\\" + p.getXml();
				File xmlFile = new File(xmlPath);
				if(!xmlFile.exists()) {
					msg = new StringBuilder();
					msg.append(Util.colorRed("XML file not found"));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n");
					
					msg.append("�Ʒ��� ��ο��� XML ������ ã�� �� �����ϴ�");
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n\n");
					
					msg.append("XML ���� ��� : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n\n");
					msg.append("XML ���� �̸� : " + Util.colorRed(xmlFile.getName()));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n");
					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}else {
					FileUtil.editFile(xmlFile);
				}
				break;
		}
	}
	
	public static void showXmlViewer() {
		try {			
			int row = table.getSelectedRow();
			int number = Integer.parseInt(table.getValueAt(row, 0).toString());
			String facType = table.getValueAt(row, 1).toString();					
			Protocol protocol = getSelectedProtocol(number, facType);
			
			String xmlPath = xmlDir.getPath() + "\\" + protocol.getXml();
			File xmlFile = new File(xmlPath);
			
			String pName = isKorean ? protocol.getName() : protocol.getEnName();
			
			if(!isKorean && pName == null && protocol.getName() != null) {
				pName = protocol.getName();
			}else if(isKorean && pName == null && protocol.getEnName() != null) {
				pName = protocol.getEnName();
			}
			
			// �Ʒ��� ������ �����Ѵٸ� �ƹ��͵� �������� �ʴ´�
			if(pName == null) {
				return;
			}
			
			if(protocol.getXml().equals("-")) {
				StringBuilder msg = new StringBuilder();
				msg.append(Util.colorRed("The XML File does not Exist"));
				msg.append(String.format("%s%s%s", Util.separator, Util.separator, "\n"));
				
				msg.append("�����Ͻ� ���������� ���� XML ������ �������� �ʽ��ϴ�");
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n\n");
				
				msg.append(Util.colorBlue("�ü��� ���� : ") + protocol.getFacType());
				msg.append(String.format("%s%s%s", Util.separator, Util.separator, "\n"));
				
				msg.append(Util.colorBlue("�������� ��ȣ : ") + protocol.getNumber());
				msg.append(String.format("%s%s%s", Util.separator, Util.separator, "\n"));
				
				msg.append(Util.colorBlue("�������� �̸�: ") + pName);
				msg.append(String.format("%s%s%s", Util.separator, Util.separator, "\n"));
				
				Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}else if(!xmlFile.exists()) {
				StringBuilder msg = new StringBuilder();
				msg.append(Util.colorRed("XML file not found"));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n");
				
				msg.append("�Ʒ��� ��ο��� XML ������ ã�� �� �����ϴ�");
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n\n");
				
				msg.append("XML ���� ��� : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n\n");
				msg.append("XML ���� �̸� : " + Util.colorRed(xmlFile.getName()));
				msg.append(String.format("%s%s", Util.separator, Util.separator));
				msg.append("\n");
				
				Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(OnionDirCheck_Panel.agent != null && OnionDirCheck_Panel.agent.equalsIgnoreCase("watchPoint")) {
				ImportModbusPointFrame addModbusPointFrame= null;
				
				if(!ImportModbusPointFrame.isExist) {
					addModbusPointFrame = new ImportModbusPointFrame();
					
					if(ImportModbusPointFrame.pointUpload(xmlFile)) {
						MainFrame.showModbusMonitor();
					}else {
						addModbusPointFrame.dispose();
					}
					
				 }else {
					 ImportModbusPointFrame.existsFrame();
				 }
				
			}else {
				// XML Viewer Frame ����
				new XmlViewerFrame(pName, xmlFile, protocol);				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setProtocolVersion(String version) {
		if(protocolVersion != null) protocolVersion.setText(String.format("<html>�������� ����&nbsp;&nbsp;%s</html>", Util.colorBlue(version)));
	}
	
	public static void resetForm() {
		if(protocolType_comboBox != null) protocolType_comboBox.setSelectedIndex(0);
		if(facilityType_comboBox != null) facilityType_comboBox.setSelectedIndex(0);
		if(seartFacility_textField != null) seartFacility_textField.setText(null);
		if(searchProtocol_textField != null) searchProtocol_textField.setText(null);
		if(searchProtocol_textField != null) searchProtocol_textField.requestFocus();
	}
	
}
