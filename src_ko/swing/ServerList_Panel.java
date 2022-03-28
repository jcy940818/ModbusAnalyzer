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
import java.util.Collections;

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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ServerList_Panel extends JPanel {
		
	public static final String GROUP_INFO = "그룹 정보";
	public static final String FAC_TYPE = "시설물 종류";
	public static final String CONN_METHOD = "연결 방식";
	public static final String SERVER_INDEX = "장비 인덱스";
	public static final String SERVER_NAME = "장비명";
	public static final String SERVER_STATE = "장비 상태";
	public static final String PROTOCOL_NUMBER = "프로토콜 번호";
	
	private JPanel infoPanel;
		
	private static ArrayList<Facility> facList;
	private Facility selectedFac;
	private static JTextField searchFacility_textField;
	private static JComboBox searchFacility_ComboBox; 
	
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
		searchFacility_textField.setBounds(70, 97, 447, 35);
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
		
		searchFacility_ComboBox = new JComboBox();
		searchFacility_ComboBox.setBackground(Color.WHITE);
		searchFacility_ComboBox.setForeground(Color.BLACK);
		searchFacility_ComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		searchFacility_ComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						SERVER_NAME, // 장비명
						SERVER_INDEX, // 장비 인덱스
						GROUP_INFO, // 그룹 정보
						FAC_TYPE, // 시설물 종류
						CONN_METHOD, // 연결 방식
						SERVER_STATE, // 장비 상태
						PROTOCOL_NUMBER, // 프로토콜 번호
						}));
		searchFacility_ComboBox.setBounds(70, 60, 200, 30);
		infoPanel.add(searchFacility_ComboBox);
				
		updateTable();
		searchFacility_textField.requestFocus();
	}
	
	
	public static void doTableFilter(String text) {
		
	}
	
	public static void updateTable() {
		loadFacility();
		if(facList == null) return; 
		
		Object[][] content = new Object[facList.size()][];

		for (int i = 0; i < facList.size(); i++) {
			Facility fac = facList.get(i);
			content[i] = new Object[5];
			content[i][0] = i + 1;
			content[i][1] = fac.getGroupInfo();
			content[i][2] = fac.getFacTypeString();
			content[i][3] = fac;
			content[i][4] = fac.getState();
		}

		table.setModel(new DefaultTableModel(
			content, 			
			new String[] { "순 서", GROUP_INFO, FAC_TYPE, SERVER_NAME, SERVER_STATE }) {
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
		table.getColumnModel().getColumn(0).setPreferredWidth(10); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(450); // 그룹 정보
		table.getColumnModel().getColumn(2).setPreferredWidth(60); // 시설물 종류
		table.getColumnModel().getColumn(3).setPreferredWidth(150); // 장비명
		table.getColumnModel().getColumn(4).setPreferredWidth(40); // 상 태	
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 그룹 정보
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 시설물 종류
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 장비명
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 상 태
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
		if(searchFacility_ComboBox != null) searchFacility_ComboBox.setSelectedIndex(0);
	}
	
	public static void loadFacility(){
		if(!ONION_Info.hasMk119Connection() || ONION_Info.getMk119Connection() == null) return;
		
		try {
			Statement stmt = ONION_Info.getMk119Connection().createStatement();
			ResultSet rs = stmt.executeQuery(serverQuery);
			
			facList = new ArrayList<Facility>();
			
			while(rs.next()) {
				Facility fac = new Facility();
				
				fac.setGroupInfo(rs.getString("groupInfo"));				
				
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
			
			Collections.sort(facList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String serverQuery = 
			"WITH tree_query AS \r\n" + 
			"( SELECT nGroupIndex , nParentIndex , strGroupName \r\n" + 
			", convert(varchar(255), nGroupIndex) sort \r\n" + 
			", convert(varchar(255), strGroupName) depth_fullname \r\n" + 
			"FROM SERVERGROUP WHERE nParentIndex = -1\r\n" + 
			"UNION ALL SELECT B.nGroupIndex , B.nParentIndex , B.strGroupName \r\n" + 
			", convert(varchar(255), convert(nvarchar,C.sort) + ' > ' + convert(varchar(255), B.nGroupIndex)) sort\r\n" + 
			", convert(varchar(255), convert(nvarchar,C.depth_fullname) + ' > ' + convert(varchar(255), B.strGroupName)) depth_fullname \r\n" + 
			"FROM SERVERGROUP B, tree_query C \r\n" + 
			"WHERE B.nParentIndex = C.nGroupIndex) \r\n" + 
			"\r\n" + 
			"select \r\n" + 
			"	replace(c.depth_fullname,'<ROOT>','장비 관리 ( 그룹 없음 )') as 'groupInfo',	\r\n" + 
			"	a.nServerIndex as 'index',\r\n" + 
			"	f.FACILITY_TYPE as 'facType',\r\n" + 
			"	a.strServerName as 'name',\r\n" + 
			"	f.CONN_METHOD as 'connMethod',\r\n" + 
			"	f.COMM_PROTOCOL as 'commProtocol',\r\n" + 
			"	f.SNMP_MIB as 'snmpProtocol',\r\n" + 
			"	a.SERVER_CONDITION as 'condition'\r\n" + 
			" \r\n" + 
			"from SERVERINFO a inner join SERVERGROUPMAP b on a.nServerIndex=b.nServerIndex\r\n" + 
			"	inner join tree_query c on b.nGroupIndex = c.ngroupIndex\r\n" + 
			"	inner join SERVERINFO_FACILITY f ON a.nServerIndex = f.NODE_INDEX\r\n" + 
			" order by a.nServerIndex";
}
