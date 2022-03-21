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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.util.TableFilter;
import src_ko.util.Util;

public class XmlEditor_Panel extends JPanel {
	
	private JPanel infoPanel;	
	private static JComboBox protocolType_comboBox;
	private static JComboBox facilityType_comboBox;
	private static ArrayList<String> facTypeList;
	
	public static ArrayList<Protocol> protocols = null;
	public static ArrayList<Protocol> selectedProtocols = null;
	public static Protocol selctedProtocol = null;
	
	private JTextField seartFacility_textField;
	private JLabel searchProtocol_Label;
	private JTextField searchProtocol_textField;
	
	private static JTable table;
	private static boolean isFirst = true;
	public static boolean setFilter = false;
	
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
				
				System.out.println("protocolType_comboBox : " + protocolType_comboBox.getItemCount() + ", " + protocolType_comboBox.getSelectedIndex());
				System.out.println("facilityType_comboBox : " + facilityType_comboBox.getItemCount() + ", " + facilityType_comboBox.getSelectedIndex());
				
				String protocolType = protocolType_comboBox.getSelectedItem().toString();				
				String facType = facilityType_comboBox.getSelectedItem().toString();
				
				if(!facType.equalsIgnoreCase("전 체")) {
					facType = facType.split(". ")[1].trim();
				}
				
				setFacilityComboBox(protocolType, facType, XmlEditor_Panel.protocols, true);				
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
				
				System.out.println("protocolType_comboBox : " + protocolType_comboBox.getItemCount() + ", " + protocolType_comboBox.getSelectedIndex());
				System.out.println("facilityType_comboBox : " + facilityType_comboBox.getItemCount() + ", " + facilityType_comboBox.getSelectedIndex());
				
				String protocolType = protocolType_comboBox.getSelectedItem().toString();				
				String facType = facilityType_comboBox.getSelectedItem().toString();
				
				if(!facType.equalsIgnoreCase("전 체")) {
					facType = facType.split(". ")[1].trim();
				}
				
				setFacilityComboBox(protocolType, facType, XmlEditor_Panel.protocols, true);								
			}
		});
		infoPanel.add(facilityType_comboBox);
		
		seartFacility_textField = new JTextField();
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
		searchProtocol_Label.setBounds(15, 84, 120, 41);
		infoPanel.add(searchProtocol_Label);
		
		searchProtocol_textField = new JTextField("");
		searchProtocol_textField.addFocusListener(Util.focusListener);
		searchProtocol_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchProtocol_textField.setForeground(Color.BLACK);
		searchProtocol_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchProtocol_textField.setColumns(10);
		searchProtocol_textField.setBounds(138, 89, 521, 35);
		searchProtocol_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				if(setFilter == false) {
					setFilter();	
				}				
			}
			public void keyReleased(KeyEvent e) {
//				if(setFilter == false) {
//					filter();	
//				}
			}
		});		
		
		infoPanel.add(searchProtocol_textField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		scrollPane.setBounds(12, 135, 1026, 461);
		infoPanel.add(scrollPane);
		
		table = new JTable();
		table.setForeground(Color.BLACK);
		scrollPane.setViewportView(table);
		updateTable(table, true);
	}
	
	public void setFilter() {		
		new TableFilter(table, searchProtocol_textField);
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

		setTable(table);
	}
	
	
	public static void setTable(JTable table) {
		// 테이블 헤더 설정
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
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
	
	
	public static void setFacilityComboBox(String protocolType, String facType, ArrayList<Protocol> protocols, boolean isKorean) {
		selectedProtocols = null;
		selectedProtocols = new ArrayList<Protocol>();
		
		ArrayList<Integer> facCodeList = new ArrayList<Integer>();
		ArrayList<String> facTypeList = new ArrayList<String>();
		HashMap<Integer, String> sortMap = new HashMap<>();
		
		for(int i = 0; i < protocols.size(); i++) {
			Protocol p = protocols.get(i);			
			
			if(protocolType == null) { // 프로토콜 타입 검사
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) { // 시설물 종류 검사
					selectedProtocols.add(p);
					if(sortMap.containsKey(p.getFacCode())) continue;			
					String item = String.format("%02d. %s", p.getFacCode(), p.getFacType());			
					sortMap.put(p.getFacCode(), item);	
				}
				
			}else if(protocolType.equalsIgnoreCase("COMMON") && p.getProtocolType() == Protocol.COMMON_PROTOCOL) {
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) {
					selectedProtocols.add(p);
					if(sortMap.containsKey(p.getFacCode())) continue;
					String item = String.format("%02d. %s", p.getFacCode(), p.getFacType());
					sortMap.put(p.getFacCode(), item);
				}
				
			}else if(protocolType.equalsIgnoreCase("SNMP") && p.getProtocolType() == Protocol.SNMP_PROTOCOL) {
				if(facType.equalsIgnoreCase("전 체") || facType.equalsIgnoreCase(p.getFacType())) {
					selectedProtocols.add(p);
					if(sortMap.containsKey(p.getFacCode())) continue;
					String item = String.format("%02d. %s", p.getFacCode(), p.getFacType());
					sortMap.put(p.getFacCode(), item);
				}
			}
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
		
		if(isFirst) {
			XmlEditor_Panel.facTypeList = facTypeList;
			facilityType_comboBox.setModel(new DefaultComboBoxModel(facTypeList.toArray()));
			isFirst = false;
		}
		
		updateTable(table, isKorean);
	}
}
