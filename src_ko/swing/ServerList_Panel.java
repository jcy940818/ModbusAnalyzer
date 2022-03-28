package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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

import common.server.Facility;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class ServerList_Panel extends JPanel {
		
	private static final String serverQuery = 
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
	
	public static final String ORDER = "순 서";
	public static final String GROUP_INFO = "그룹 정보";
	public static final String SERVER_INDEX = "장비 인덱스";
	public static final String SERVER_NAME = "장비명";
	public static final String FAC_TYPE = "시설물 종류";
	public static final String CONN_METHOD = "연결 방식";
	public static final String SERVER_STATE = "장비 상태";
	public static final String PROTOCOL_NUMBER = "프로토콜 번호";
	
	private JPanel infoPanel;
		
	private static ArrayList<Facility> facList;
	private static Facility selectedFac;
	private static JTextField searchFacility_textField1;
	private static JTextField searchFacility_textField2;
	private static JComboBox searchFacility_ComboBox1; 
	private static JComboBox searchFacility_ComboBox2;
	
	private static JTable serverListTable;
	private static JTable serverInfoTable;
	
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
		
		JLabel MK119 = new JLabel();
		MK119.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		MK119.setForeground(Color.BLACK);
		MK119.setHorizontalAlignment(SwingConstants.CENTER);
		MK119.setIcon(new Util().getMK2Resource());
		MK119.setBackground(Color.WHITE);
		MK119.setBounds(958, 0, 80, 30);
		infoPanel.add(MK119);

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
		searchFacility_Label.setBounds(22, 120, 50, 64);
		infoPanel.add(searchFacility_Label);
		
		
		
		searchFacility_ComboBox1 = new JComboBox();
		searchFacility_ComboBox1.setBackground(Color.WHITE);
		searchFacility_ComboBox1.setForeground(Color.BLACK);
		searchFacility_ComboBox1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_INDEX, // 장비 인덱스
				SERVER_NAME, // 장비명
				FAC_TYPE, // 시설물 종류
				CONN_METHOD, // 연결 방식
				SERVER_STATE, // 장비 상태
				PROTOCOL_NUMBER, // 프로토콜 번호
				}));
		searchFacility_ComboBox1.setBounds(76, 121, 150, 30);
		searchFacility_ComboBox1.setSelectedIndex(0);
		infoPanel.add(searchFacility_ComboBox1);
		
		searchFacility_ComboBox2 = new JComboBox();
		searchFacility_ComboBox2.setBackground(Color.WHITE);
		searchFacility_ComboBox2.setForeground(Color.BLACK);
		searchFacility_ComboBox2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_INDEX, // 장비 인덱스
				SERVER_NAME, // 장비명
				FAC_TYPE, // 시설물 종류
				CONN_METHOD, // 연결 방식
				SERVER_STATE, // 장비 상태
				PROTOCOL_NUMBER, // 프로토콜 번호
				}));
		searchFacility_ComboBox2.setBounds(76, 156, 150, 30);
		searchFacility_ComboBox2.setSelectedIndex(2);
		infoPanel.add(searchFacility_ComboBox2);
		
		searchFacility_textField1 = new JTextField("");
		searchFacility_textField1.addFocusListener(Util.focusListener);
		searchFacility_textField1.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField1.setForeground(Color.BLACK);
		searchFacility_textField1.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchFacility_textField1.setColumns(10);
		searchFacility_textField1.setBounds(231, 121, 423, 30);
		searchFacility_textField1.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchFacility_textField1);
		
		searchFacility_textField2 = new JTextField("");
		searchFacility_textField2.addFocusListener(Util.focusListener);
		searchFacility_textField2.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField2.setForeground(Color.BLACK);
		searchFacility_textField2.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchFacility_textField2.setColumns(10);
		searchFacility_textField2.setBounds(231, 156, 423, 30);
		searchFacility_textField2.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchFacility_textField2);
		
		JScrollPane serverListPane = new JScrollPane();
		serverListPane.setBorder(new LineBorder(Color.BLACK, 3));
		serverListPane.setBounds(12, 191, 1026, 406);
		infoPanel.add(serverListPane);
		
		serverListTable = new JTable();		
		serverListTable.setForeground(Color.BLACK);
		serverListTable.addFocusListener(new FocusListener() {			
			public void focusLost(FocusEvent e) {
				try {
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);			
					updateServerInfoTable(selectedFac);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void focusGained(FocusEvent e) {
				try {
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		serverListTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
						
			public void keyReleased(KeyEvent e) {
				try {
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		serverListTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
				} // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
					showFunction(selectedFac);	
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = serverListTable.getSelectedRow();
					selectedFac = (Facility) serverListTable.getValueAt(row, 3);
					updateServerInfoTable(selectedFac);
					showFunction(selectedFac);
				}
			}
		});
		serverListPane.setViewportView(serverListTable);
		
		JScrollPane serverInfoPane = new JScrollPane();		
		serverInfoPane.setBorder(new LineBorder(Color.BLACK, 2));
		serverInfoPane.setBounds(658, 31, 380, 155);	
		infoPanel.add(serverInfoPane);
		
		serverInfoTable = new JTable();
		serverInfoTable.setBorder(new LineBorder(Color.BLACK, 2));
		serverInfoPane.setViewportView(serverInfoTable);
				
		updateServerListTable();
		updateServerInfoTable(null);
	}
	
	public static void selectFacility(Facility fac) {
		
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
	
	
	public static void updateServerListTable() {
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

		serverListTable.setModel(new DefaultTableModel(
			content, 			
			new String[] { ORDER, GROUP_INFO, FAC_TYPE, SERVER_NAME, SERVER_STATE }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(serverListTable);
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
		
	public static void updateServerInfoTable(Facility fac) {
		if(fac == null) {
			serverInfoTable.setModel(new DefaultTableModel(
					new Object[][] {
						{ null, null },
						{ null, null },
						{ null, null },
						{ null, null },
						{ null, null }
					},
					new String[] { "항 목", "내 용" }) {
					// 테이블 셀 내용 수정 금지
					public boolean isCellEditable(int i, int c) {
						return false;
					}
			});
			setServerInfoTableStyle(serverInfoTable);
			return;		
		}
		
		Object[][] content = new Object[5][];
		
		content[0] = new Object[2];
		content[0][0] = FAC_TYPE;
		content[0][1] = fac.getFacTypeString();
		
		content[1] = new Object[2];
		content[1][0] = SERVER_INDEX;
		content[1][1] = fac.getIndex();
		
		content[2] = new Object[2];
		content[2][0] = SERVER_NAME;
		content[2][1] = fac.getName();
		
		content[3] = new Object[2];
		content[3][0] = CONN_METHOD;
		content[3][1] = fac.getConnMethod();
		
		content[4] = new Object[2];
		content[4][0] = SERVER_STATE;
		content[4][1] = fac.getState();

		serverInfoTable.setModel(new DefaultTableModel(
			content,
			new String[] { "항 목", "내 용" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setServerInfoTableStyle(serverInfoTable);
	}
	
	public static void setServerInfoTableStyle(JTable table) {
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
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 필 드
		table.getColumnModel().getColumn(1).setPreferredWidth(100); // 내 용		
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 필 드
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 내 용
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
		if(searchFacility_textField1 != null) searchFacility_textField1.setText(null);
		if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedIndex(0);
		if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedIndex(2);
	}
	
	public static void doTableFilter() {		
		ArrayList<Facility> filterFacilitys = new ArrayList<Facility>();
		String text_1 = searchFacility_textField1.getText();
		String text_2 = searchFacility_textField2.getText();
		
		boolean noSearch_1 = (text_1 == null || text_1.length() == 0 || text_1.equals(""));
		boolean noSearch_2 = (text_2 == null || text_2.length() == 0 || text_2.equals(""));
		
		if(noSearch_1 && noSearch_2) {
			updateServerListTable();
			return;
		}
		
		text_1 = text_1.toUpperCase();
		text_2 = text_2.toUpperCase();
		
		for(int i = 0; i < facList.size(); i++) {
			Facility fac = facList.get(i);
			
			String searchElement_1 = null;
			String searchElement_2 = null;
			
			switch(searchFacility_ComboBox1.getSelectedItem().toString()) {
				case GROUP_INFO :  // 그룹 정보
					searchElement_1 = fac.getGroupInfo();
					break;
				case SERVER_INDEX : // 장비 인덱스
					searchElement_1 = String.valueOf(fac.getIndex());
					break;
				case SERVER_NAME : // 장비명
					searchElement_1 = fac.getName();
					break;
				case FAC_TYPE : // 시설물 종류
					searchElement_1 = fac.getFacTypeString();
					break;
				case CONN_METHOD : // 연결 방식
					searchElement_1 = fac.getConnMethod();
					break;
				case SERVER_STATE : // 장비 상태
					searchElement_1 = fac.getState();
					break;
				case PROTOCOL_NUMBER : // 프로토콜 번호
					searchElement_1 = String.valueOf((fac.isCommon()?fac.getCommProtocol():fac.getSnmpProtocol()));
					break;
			}// switch - searchElement_1
			
			switch(searchFacility_ComboBox2.getSelectedItem().toString()) {
				case GROUP_INFO :  // 그룹 정보
					searchElement_2 = fac.getGroupInfo();
					break;
				case SERVER_INDEX : // 장비 인덱스
					searchElement_2 = String.valueOf(fac.getIndex());
					break;
				case SERVER_NAME : // 장비명
					searchElement_2 = fac.getName();
					break;
				case FAC_TYPE : // 시설물 종류
					searchElement_2 = fac.getFacTypeString();
					break;
				case CONN_METHOD : // 연결 방식
					searchElement_2 = fac.getConnMethod();
					break;
				case SERVER_STATE : // 장비 상태
					searchElement_2 = fac.getState();
					break;
				case PROTOCOL_NUMBER : // 프로토콜 번호
					searchElement_2 = String.valueOf((fac.isCommon()?fac.getCommProtocol():fac.getSnmpProtocol()));
					break;
			}// switch - searchElement_2
			
			if(searchElement_1 != null) {
				searchElement_1 = searchElement_1.toUpperCase();
			}else {
				searchElement_1 = "";
			}
			
			if(searchElement_2 != null) {
				searchElement_2 = searchElement_2.toUpperCase();
			}else {
				searchElement_2 = "";
			}
			
			boolean isContain_1 = false;
			boolean isContain_2 = false;
			
			if(text_1.contains(",")) {
				String[] textToken = text_1.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2];
					if(searchElement_1.contains(token)) {
						isContain_1 = true;
					}
				}
			}else if(searchElement_1.contains(text_1)) {
				isContain_1 = true;
			}// set isContain_1
			
			if(text_2.contains(",")) {
				String[] textToken = text_2.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2];
					if(searchElement_2.contains(token)) {
						isContain_2 = true;
					}
				}
			}else if(searchElement_2.contains(text_2)) {
				isContain_2 = true;
			}// set isContain_2
			
			if(isContain_1 && isContain_2) {
				filterFacilitys.add(fac);
			}// AND Operation isContains 1, 2
			
		}// for loop
		
		Object[][] content = new Object[filterFacilitys.size()][];
		
		for (int i = 0; i < filterFacilitys.size(); i++) {
			Facility fac = filterFacilitys.get(i);
			content[i] = new Object[5];
			content[i][0] = i + 1;
			content[i][1] = fac.getGroupInfo();
			content[i][2] = fac.getFacTypeString();
			content[i][3] = fac;
			content[i][4] = fac.getState();
		}

		serverListTable.setModel(new DefaultTableModel(
			content, 			
			new String[] { ORDER, GROUP_INFO, FAC_TYPE, SERVER_NAME, SERVER_STATE }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(serverListTable);
	}
}
