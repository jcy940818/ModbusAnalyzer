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

import src_ko.info.Protocol;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class XmlEditor_Panel extends JPanel {
	
	public static File xmlDir;
	
	private JPanel infoPanel;
	private static JComboBox protocolType_comboBox;
	private static JComboBox facilityType_comboBox;
	
	private static ArrayList<String> facTypeList;	
	public static ArrayList<Protocol> protocols = null;
	public static ArrayList<Protocol> selectedProtocols = null;
	public static Protocol selctedProtocol = null;
	
	private static boolean isKorean = true;
	private JTextField seartFacility_textField;
	private JLabel searchProtocol_Label;
	private JTextField searchProtocol_textField;
	
	private static JTable table;		
	private JButton goXmlEditor;
	private JButton openXmlFile;
	
	/**
	 * Create the panel.
	 */
	public XmlEditor_Panel() {
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

		JLabel currentFunction = new JLabel("XML Editor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 187, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		JLabel protocolType_Label = new JLabel("프로토콜 타입");
		protocolType_Label.setHorizontalAlignment(SwingConstants.LEFT);
		protocolType_Label.setForeground(Color.BLACK);
		protocolType_Label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		protocolType_Label.setBackground(Color.WHITE);
		protocolType_Label.setBounds(232, 0, 120, 55);
		infoPanel.add(protocolType_Label);
		
		protocolType_comboBox = new JComboBox();
		protocolType_comboBox.setForeground(Color.BLACK);
		protocolType_comboBox.setBackground(Color.WHITE);
		protocolType_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		protocolType_comboBox.setBounds(358, 12, 157, 35);
		protocolType_comboBox.setModel(new DefaultComboBoxModel(new String[] {"COMMON", "SNMP"}));
		protocolType_comboBox.setSelectedIndex(0);		
		protocolType_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				searchProtocol_textField.setText(null);
				tableReload(isKorean);
			}
		});
		infoPanel.add(protocolType_comboBox);
		
		JLabel facilityType_Label = new JLabel("시설물 종류");
		facilityType_Label.setHorizontalAlignment(SwingConstants.LEFT);
		facilityType_Label.setForeground(Color.BLACK);
		facilityType_Label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		facilityType_Label.setBackground(Color.WHITE);
		facilityType_Label.setBounds(553, 0, 106, 55);
		infoPanel.add(facilityType_Label);
		
		facilityType_comboBox = new JComboBox();		
		facilityType_comboBox.setForeground(Color.BLACK);
		facilityType_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		facilityType_comboBox.setBackground(Color.WHITE);
		facilityType_comboBox.setBounds(787, 12, 251, 35);
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
		seartFacility_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		seartFacility_textField.setBounds(665, 12, 116, 35);
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
		
		searchProtocol_Label = new JLabel("프로토콜 검색");
		searchProtocol_Label.setHorizontalAlignment(SwingConstants.LEFT);
		searchProtocol_Label.setForeground(Color.BLACK);
		searchProtocol_Label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchProtocol_Label.setBackground(Color.WHITE);
		searchProtocol_Label.setBounds(15, 87, 120, 41);
		infoPanel.add(searchProtocol_Label);
		
		searchProtocol_textField = new JTextField("");
		searchProtocol_textField.addFocusListener(Util.focusListener);
		searchProtocol_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchProtocol_textField.setForeground(Color.BLACK);
		searchProtocol_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchProtocol_textField.setColumns(10);
		searchProtocol_textField.setBounds(138, 92, 521, 35);
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
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					getSelectedProtocol(number, facType, name, xml);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void focusGained(FocusEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					getSelectedProtocol(number, facType, name, xml);
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
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					getSelectedProtocol(number, facType, name, xml);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
						
			public void keyReleased(KeyEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					getSelectedProtocol(number, facType, name, xml);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					showFunction(getSelectedProtocol(number, facType, name, xml));
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					showFunction(getSelectedProtocol(number, facType, name, xml));					
				}
			}
		});
		scrollPane.setViewportView(table);
		
		JButton languageButton = new JButton("한글명");		
		languageButton.setBounds(665, 92, 97, 35);		
		languageButton.setForeground(Color.BLACK);
		languageButton.setBackground(Color.WHITE);
		languageButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		languageButton.setFocusPainted(false);		
		languageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String language = languageButton.getText();
				
				if(language.contains("한글")) {
					languageButton.setText("영문명");
					isKorean = false;
					tableReload(isKorean);
				}else {
					languageButton.setText("한글명");
					isKorean = true;
					tableReload(isKorean);
				}
				
			}
		});
		infoPanel.add(languageButton);
		
		goXmlEditor = new JButton("XML Editor");
		goXmlEditor.setForeground(Color.BLUE);
		goXmlEditor.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		goXmlEditor.setFocusPainted(false);
		goXmlEditor.setBackground(Color.WHITE);
		goXmlEditor.setBounds(792, 92, 120, 35);
		infoPanel.add(goXmlEditor);
		
		openXmlFile = new JButton("XML 열기");
		openXmlFile.setForeground(new Color(0, 128, 0));
		openXmlFile.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		openXmlFile.setFocusPainted(false);
		openXmlFile.setBackground(Color.WHITE);
		openXmlFile.setBounds(919, 92, 119, 35);
		openXmlFile.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					String name = table.getValueAt(row, 2).toString();
					String xml = table.getValueAt(row, 3).toString();
					Protocol p = getSelectedProtocol(number, facType, name, xml);
				
					String xmlPath = xmlDir.getPath() + "\\" + p.getXml();
					File xmlFile = new File(xmlPath);
					if(!xmlFile.exists()) {
						StringBuilder msg = new StringBuilder();
						msg.append(Util.colorRed("XML file not found"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
						
						msg.append("아래의 경로에서 XML 파일을 찾을 수 없습니다");
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						
						msg.append("XML 파일 경로 : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						msg.append("XML 파일 이름 : " + Util.colorRed(xmlFile.getName()));
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
		text = text.toUpperCase();
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
			
			if(number.contains(text) || facType.contains(text) || name.contains(text) || xml.contains(text)) {
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
			new String[] { "번 호", "시설물 종류", "프로토콜", "성능 XML" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
	
	public static void tableReload(boolean isKorean) {				
		String protocolType = protocolType_comboBox.getSelectedItem().toString();				
		String facType = facilityType_comboBox.getSelectedItem().toString();
		
		if(!facType.equalsIgnoreCase("전 체")) {
			facType = facType.split(". ")[1].trim();
		}
		
		setTableContent(protocolType, facType, XmlEditor_Panel.protocols, isKorean);
	}
	
	
	
	public static void setTableContent(String protocolType, String facType, ArrayList<Protocol> protocols, boolean isKorean) {
		selectedProtocols = null;
		selectedProtocols = new ArrayList<Protocol>();
				
		for(int i = 0; i < protocols.size(); i++) {
			Protocol p = protocols.get(i);			
			
			if(protocolType == null) { // 프로토콜 타입 검사
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) { // 시설물 종류 검사
					selectedProtocols.add(p);
				}
				
			}else if(protocolType.equalsIgnoreCase("COMMON") && p.getProtocolType() == Protocol.COMMON_PROTOCOL) {
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) {
					selectedProtocols.add(p);
				}
				
			}else if(protocolType.equalsIgnoreCase("SNMP") && p.getProtocolType() == Protocol.SNMP_PROTOCOL) {
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) {
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
			new String[] { "번 호", "시설물 종류", "프로토콜", "성능 XML" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
	
	
	public static void setTableStyle(JTable table) {
		
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(1); // 프로토콜 번호
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // 시설물 종류
		table.getColumnModel().getColumn(2).setPreferredWidth(320); // 프로토콜
		table.getColumnModel().getColumn(3).setPreferredWidth(320); // 프로토콜
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 프로토콜 번호
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 시설물 종류
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
		
		facTypeList.add("전 체");
		for(int i = 0; i < facCodeList.size(); i++) {
			facTypeList.add(sortMap.get(facCodeList.get(i)));
		}
		
		XmlEditor_Panel.facTypeList = facTypeList; // 시설물 종류 검색 용도
		facilityType_comboBox.setModel(new DefaultComboBoxModel(facTypeList.toArray()));
	}
	
	public static Protocol getSelectedProtocol(int number, String facType, String name, String xml) {
		boolean isSelected = false;
		
		for(int i = 0; i < selectedProtocols.size(); i++) {
			Protocol p = selectedProtocols.get(i);
			isSelected = (number == p.getNumber()) && (facType.equalsIgnoreCase(p.getFacType())) && (name.equalsIgnoreCase(p.getName())) && (xml.equalsIgnoreCase(p.getXml()));
			
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
		msg.append("<font color='Green'>XML Execution Task Menu</font>\n");
		msg.append("어떤 작업을 수행 하시겠습니까?" + Util.longSeparator + Util.separator +"\n\n");
		
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("시설물 종류"), p.getFacType(), separator, separator));
		msg.append(String.format("%s : %d%s%s\n\n", Util.colorBlue("프로토콜 번호"), p.getNumber(), separator, separator));
		
		String pName = isKorean ? p.getName() : p.getEnName();
		if(pName == null) {
			pName = p.getName();
			if(pName == null) {
				pName = "Unknown";
			}
		}		
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("프로토콜 이름"),pName , separator, separator));
		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("성능 XML"), p.getXml(), separator, separator));
		
		int menu = Util.showOption(msg.toString(), new String[] { "XML Editor 열기", "XML 파일 바로 열기"}, JOptionPane.QUESTION_MESSAGE);

		switch (menu) {
			case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때				
				return;
				
			case 0: // XML Editor 열기
				// 에디터 열기 작업
				break;
				
			case 1: // XML 파일 바로 열기
				String xmlPath = xmlDir.getPath() + "\\" + p.getXml();
				File xmlFile = new File(xmlPath);
				if(!xmlFile.exists()) {
					msg = new StringBuilder();
					msg.append(Util.colorRed("XML file not found"));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n");
					
					msg.append("아래의 경로에서 XML 파일을 찾을 수 없습니다");
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n\n");
					
					msg.append("XML 파일 경로 : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n\n");
					msg.append("XML 파일 이름 : " + Util.colorRed(xmlFile.getName()));
					msg.append(String.format("%s%s", Util.separator, Util.separator));
					msg.append("\n");
					
					Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				}else {
					FileUtil.editFile(xmlFile);
				}
				break;
		}
	}
	
}
