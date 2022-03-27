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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
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

import common.server.Facility;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class ServerList_Panel extends JPanel {
	
	private JPanel infoPanel;
		
	private static ArrayList<Facility> facList;
	private Facility selectedFac;
	private static JTextField searchFacility_textField;
	
	private static JTable table;		
	private JButton goPerfViewer;	
	
	/**
	 * Create the panel.
	 */
	public ServerList_Panel() {
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

		JLabel currentFunction = new JLabel("Server List");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setBounds(0, 0, 180, 55);
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		infoPanel.add(currentFunction);
		
		JLabel searchFacility_Label = new JLabel("검 색");
		searchFacility_Label.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_Label.setForeground(Color.BLACK);
		searchFacility_Label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchFacility_Label.setBackground(Color.WHITE);
		searchFacility_Label.setBounds(16, 92, 50, 41);
		infoPanel.add(searchFacility_Label);
		
		searchFacility_textField = new JTextField("");
		searchFacility_textField.addFocusListener(Util.focusListener);
		searchFacility_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField.setForeground(Color.BLACK);
		searchFacility_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchFacility_textField.setColumns(10);
		searchFacility_textField.setBounds(70, 97, 522, 35);
		searchFacility_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					String text = searchFacility_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						
					}else {					
						doTableFilter(text);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					String text = searchFacility_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						
					}else {					
						doTableFilter(text);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchFacility_textField);
		
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
					getSelectedFacility(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void focusGained(FocusEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();					
					getSelectedFacility(number, facType);
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
					getSelectedFacility(number, facType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
						
			public void keyReleased(KeyEvent e) {
				try {
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					getSelectedFacility(number, facType);
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
					showFunction(getSelectedFacility(number, facType));
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = table.getSelectedRow();
					int number = Integer.parseInt(table.getValueAt(row, 0).toString());
					String facType = table.getValueAt(row, 1).toString();
					showFunction(getSelectedFacility(number, facType));		
				}
			}
		});
		scrollPane.setViewportView(table);
				
		
		goPerfViewer = new JButton("\uC131\uB2A5 \uC815\uBCF4");
		goPerfViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPerfViewer();
			}
		});
		goPerfViewer.setForeground(Color.BLUE);
		goPerfViewer.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		goPerfViewer.setFocusPainted(false);
		goPerfViewer.setBackground(Color.WHITE);
		goPerfViewer.setBounds(925, 97, 113, 35);
		infoPanel.add(goPerfViewer);
				
		updateTable();
	}
	
	
	public static void doTableFilter(String text) {
		
	}
	
	public static void updateTable() {
		loadFacility();
		if(facList == null) return; 
		
		Object[][] content = new Object[facList.size()][];

		for (int i = 0; i < facList.size(); i++) {
			Facility fac = facList.get(i);
			content[i] = new Object[4];
			content[i][0] = fac.getGroup();
			content[i][1] = fac.getFacTypeString();
			content[i][2] = fac;
			content[i][3] = fac.getState();
		}

		table.setModel(new DefaultTableModel(
			content, 			
			new String[] { "그 룹", "시설물 종류", "장비명", "상 태" }) {
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
		table.getColumnModel().getColumn(0).setPreferredWidth(120); // 그룹 정보
		table.getColumnModel().getColumn(1).setPreferredWidth(120); // 시설물 종류
		table.getColumnModel().getColumn(2).setPreferredWidth(300); // 장비명
		table.getColumnModel().getColumn(3).setPreferredWidth(60); // 상 태	
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 그룹 정보
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 시설물 종류
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 장비명
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 상 태
	}
	
	
	
	
	public static Facility getSelectedFacility(int number, String facType) {
		return null;
	}
	
	
	public static void showFunction(Facility fac) {
//		if(fac == null) return;
//		
//		String separator = Util.separator + Util.separator; 
//		StringBuilder msg = new StringBuilder();
//		msg.append("<font color='Green'>Protocol Information</font>\n");		
//		
//		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("시설물 종류"), p.getFacType(), separator, separator));
//		msg.append(String.format("%s : %d%s%s\n\n", Util.colorBlue("프로토콜 번호"), p.getNumber(), separator, separator));
//		
//		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("프로토콜 이름"),pName , separator, separator));
//		msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("성능 XML"), p.getXml(), separator, separator));
//		
//		int menu = Util.showOption(msg.toString(), new String[] { "XML Viewer 열기", "XML 파일 열기"}, JOptionPane.QUESTION_MESSAGE);
//
//		switch (menu) {
//			case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때				
//				return;
//				
//			case 0: // XML Viewer 열기
//				// 에디터 열기 작업
//				showXmlViewer();
//				break;
//				
//			case 1: // XML 파일 바로 열기
//				
//				break;
//		}
	}
	
	public static void showPerfViewer() {
		try {			
			
		}catch(Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void resetForm() {
		if(searchFacility_textField != null) searchFacility_textField.setText(null);
	}
	
	public static void loadFacility(){
		if(!ONION_Info.hasMk119Connection() || ONION_Info.getMk119Connection() == null) return;
		
		try {
			Statement stmt = ONION_Info.getMk119Connection().createStatement();
			ResultSet rs = stmt.executeQuery(serverQuery);
			
			facList = new ArrayList<Facility>();
			
			while(rs.next()) {
				Facility fac = new Facility();
				
				fac.setGroup(rs.getString("group"));
				
				fac.setIndex(rs.getInt("index"));
				fac.setName(rs.getString("name"));
				
				fac.setFacType(rs.getInt("facType"));
				fac.setFacTypeString(DbUtil.getFacilityType(fac.getFacType()));
				
				fac.setConnCode(rs.getInt("connMethod"));
				fac.setConnMethod(DbUtil.getConnMethod(fac.getConnCode()));
				
				fac.setCommProtocol(rs.getInt("commProtocol"));
				fac.setSnmpProtocol(rs.getInt("snmpProtocol"));
				fac.setCommon((fac.getCommProtocol() > fac.getSnmpProtocol()) ? true : false);
				
				fac.setConditionCode(rs.getInt("condition"));
				fac.setState(DbUtil.getState(fac.getConditionCode()));
				
				facList.add(fac);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String serverQuery = 
			"SELECT\r\n" + 
			"	grp.nGroupIndex as 'groupIndex',\r\n" + 
			"	grp.strGroupName as 'group',	\r\n" + 
			"	si.nServerIndex as 'index',\r\n" + 
			"	si.strServerName as 'name',\r\n" + 
			"	si.SERVER_CONDITION as 'condition',\r\n" + 
			"	fac.FACILITY_TYPE as 'facType',\r\n" + 
			"	fac.CONN_METHOD as 'connMethod',\r\n" + 
			"	fac.COMM_PROTOCOL as 'commProtocol',\r\n" + 
			"	fac.SNMP_MIB as 'snmpProtocol'\r\n" + 
			" FROM \r\n" + 
			"	SERVERINFO si INNER JOIN SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX\r\n" + 
			"	INNER JOIN SERVERGROUPMAP map ON si.nServerIndex = map.nServerIndex\r\n" + 
			"	INNER JOIN SERVERGROUP grp ON map.nGroupIndex = grp.nGroupIndex";
	
}
