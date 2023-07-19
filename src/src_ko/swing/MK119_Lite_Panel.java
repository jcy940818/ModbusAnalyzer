package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

import common.server.Event;
import common.server.Facility;
import common.server.MultiPortMap;
import common.server.RCU;
import common.server.Server;
import common.server.ServerGroup;
import common.server.SystemSeverity;
import common.util.FindTextRenderer;
import common.util.FontManager;
import common.util.SeverityRenderer;
import common.util.TextUtil;
import common.web.AdminConsole_Info;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.util.Util;

public class MK119_Lite_Panel extends JPanel {
	
	public static boolean isFirstLoad;
	public static final String ORDER = "순 서";
	public static final String GROUP_INFO = "그룹 정보";
	public static final String SERVER_INDEX = "장비 인덱스";
	public static final String SERVER_NAME = "장비명";
	public static final String SERVER_TYPE = "장비 종류";
	public static final String IP = "IP 주소";
	public static final String FACILITY_TYPE = "시설물 종류";
	public static final String RCU_TYPE = "RCU 종류";
	public static final String CONN_METHOD = "연결 방식";
	public static final String EVENT = "이벤트";
	public static final String SERVER_STATE = "장비 상태";	
	public static final String PROTOCOL_NUMBER = "프로토콜 번호";
	public static final String PROTOCOL_NAME = "프로토콜 이름";
	
	public static final String STATE_COMMER = "통신 오류";
	
	public static final String OVERLAPPING = "   ( 중복 장비 존재 )";
	
	public static JLabel sqlServerInfo_label;
	
	private JPanel infoPanel;
	
	// ******** [ MK119 연동 데이터 ] *********************************************************
	public static AdminConsole_Info adminConsole = null;
	
	public static boolean linkMK119_Protocol = false;
	public static HashMap<String, Protocol> protocolMap = new HashMap<String, Protocol>();

	public static boolean linkMK119_PerfData = false;
	// ************************************************************************************
	
	public static ArrayList<Server> serverList;
	public static HashMap<Integer, Server> serverMap;
	public static HashMap<Integer, ServerGroup> serverGroupMap;
	public static HashMap<Integer, Integer> serverGroupMappingInfo;
	
	public static Server selectedServer;
	private static JTextField searchFacility_textField1;
	private static JTextField searchFacility_textField2;
	private static JComboBox searchFacility_ComboBox1; 
	private static JComboBox searchFacility_ComboBox2;	
	private static JTable serverListTable;
	private static JTable serverInfoTable;
	
	private static JButton eventInfo_Button;
	private static JButton rcuInfo_Button;
	private static JButton facilityInfo_Button;
	public static JButton linkMK119_Button;
	private static JButton updateDB_Button;
	private static JButton resetForm_button;
	
	/**
	 * Create the panel.
	 */
	public MK119_Lite_Panel() {
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

		JLabel onion_Logo = new JLabel();
		onion_Logo.setForeground(Color.BLACK);
		onion_Logo.setBackground(Color.WHITE);
		onion_Logo.setIcon(new Util().getSubLogoResource());
		onion_Logo.setBounds(136, 85, 50, 50);
		onion_Logo.setHorizontalAlignment(SwingConstants.LEFT);
		onion_Logo.setFont(FontManager.getFont(Font.BOLD, 22));
//		infoPanel.add(onion_Logo);
		
		sqlServerInfo_label = new JLabel();
		sqlServerInfo_label.setIcon(new Util().getMK2Resource());
		sqlServerInfo_label.setHorizontalAlignment(SwingConstants.LEFT);
		sqlServerInfo_label.setForeground(Color.BLUE);
		sqlServerInfo_label.setFont(FontManager.getFont(Font.BOLD, 22));
		sqlServerInfo_label.setBackground(Color.WHITE);
		sqlServerInfo_label.setBounds(10, 0, 580, 48);
		infoPanel.add(sqlServerInfo_label);
		
		eventInfo_Button = new JButton("이벤트 정상");
		eventInfo_Button.setForeground(Color.BLACK);
		eventInfo_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		eventInfo_Button.setFocusPainted(false);
		eventInfo_Button.setBackground(Color.WHITE);
		eventInfo_Button.setBounds(84, 62, 173, 37);
		eventInfo_Button.setEnabled(false);		
		eventInfo_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedServer.hasEvent()) {
					Event.showSimpleEventInfo(selectedServer);
				}
			}
		});
		infoPanel.add(eventInfo_Button);
		
		rcuInfo_Button = new JButton("RCU 정보");
		rcuInfo_Button.setForeground(Color.BLACK);
		rcuInfo_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		rcuInfo_Button.setFocusPainted(false);
		rcuInfo_Button.setBackground(new Color(152, 251, 152));
		rcuInfo_Button.setBounds(262, 62, 173, 37);
		rcuInfo_Button.setEnabled(false);		
		rcuInfo_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedServer.isRCU()) {
					new RcuInfoFrame((RCU)selectedServer);
				}else if(selectedServer.isFacility()) {
					Facility fac = (Facility)selectedServer;
					if(fac.isConnRCU() && fac.getRcu() != null) new RcuInfoFrame(fac.getRcu()); 
				}
			}
		});
		infoPanel.add(rcuInfo_Button);
		
		facilityInfo_Button = new JButton("시설물 정보");
		facilityInfo_Button.setForeground(Color.BLACK);
		facilityInfo_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		facilityInfo_Button.setFocusPainted(false);
		facilityInfo_Button.setBackground(Color.ORANGE);
		facilityInfo_Button.setBounds(440, 62, 150, 37);
		facilityInfo_Button.setEnabled(false);
		facilityInfo_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(selectedServer.isFacility()) {
					new FacilityInfoFrame((Facility)selectedServer);
				}
			}
		});
		infoPanel.add(facilityInfo_Button);
		
		linkMK119_Button = new JButton(" 연동");
		linkMK119_Button.setIcon(new Util().getMK2Resource());
		linkMK119_Button.setForeground(Color.BLACK);
		linkMK119_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		linkMK119_Button.setFocusPainted(false);
		linkMK119_Button.setBackground(Color.WHITE);
		linkMK119_Button.setBounds(84, 103, 173, 37);
		linkMK119_Button.setEnabled(true);
		linkMK119_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!LinkMK119Frame.isExist) {
					new LinkMK119Frame(linkMK119_Protocol, linkMK119_PerfData);	
				}
				
			}
		});
		infoPanel.add(linkMK119_Button);
		
		
		JLabel searchFacility_Label = new JLabel("검 색");
		searchFacility_Label.setHorizontalAlignment(SwingConstants.CENTER);
		searchFacility_Label.setForeground(Color.BLACK);
		searchFacility_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		searchFacility_Label.setBackground(Color.WHITE);
		searchFacility_Label.setBounds(22, 146, 50, 64);
		infoPanel.add(searchFacility_Label);
		
		updateDB_Button = new JButton("Database 최신화");
		updateDB_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		updateDB_Button.setBackground(Color.WHITE);
		updateDB_Button.setForeground(Color.BLACK);
		updateDB_Button.setFocusPainted(false);		
		updateDB_Button.setBounds(262, 103, 173, 37);
		updateDB_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm(true, false);
			}
		});
		infoPanel.add(updateDB_Button);
		
		resetForm_button = new JButton("Form 초기화");
		resetForm_button.setForeground(Color.BLACK);
		resetForm_button.setFont(FontManager.getFont(Font.BOLD, 16));
		resetForm_button.setFocusPainted(false);
		resetForm_button.setBackground(Color.WHITE);
		resetForm_button.setBounds(440, 103, 150, 37);
		resetForm_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm(false, true);
			}
		});
		infoPanel.add(resetForm_button);
		
		searchFacility_ComboBox1 = new JComboBox();
		searchFacility_ComboBox1.setBackground(Color.WHITE);
		searchFacility_ComboBox1.setForeground(Color.BLACK);
		searchFacility_ComboBox1.setFont(FontManager.getFont(Font.BOLD, 16));
		searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_TYPE, // 시설물 종류
				SERVER_NAME, // 장비명
				SERVER_INDEX, // 장비 인덱스
				IP, // IP 주소
				CONN_METHOD, // 연결 방식
				PROTOCOL_NUMBER, // 프로토콜 번호
				SERVER_STATE, // 장비 상태
				EVENT
				}));
		searchFacility_ComboBox1.setBounds(84, 145, 173, 30);
		searchFacility_ComboBox1.setSelectedIndex(0);
		searchFacility_ComboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchFacility_ComboBox1);
		
		searchFacility_ComboBox2 = new JComboBox();
		searchFacility_ComboBox2.setBackground(Color.WHITE);
		searchFacility_ComboBox2.setForeground(Color.BLACK);
		searchFacility_ComboBox2.setFont(FontManager.getFont(Font.BOLD, 16));
		searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_TYPE, // 시설물 종류
				SERVER_NAME, // 장비명
				SERVER_INDEX, // 장비 인덱스
				IP, // IP 주소
				CONN_METHOD, // 연결 방식
				PROTOCOL_NUMBER, // 프로토콜 번호
				SERVER_STATE, // 장비 상태
				EVENT
				}));
		searchFacility_ComboBox2.setBounds(84, 181, 173, 30);
		searchFacility_ComboBox2.setSelectedIndex(2);
		searchFacility_ComboBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		infoPanel.add(searchFacility_ComboBox2);
		
		searchFacility_textField1 = new JTextField("");
		searchFacility_textField1.addFocusListener(Util.focusListener);
		searchFacility_textField1.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField1.setForeground(Color.BLACK);
		searchFacility_textField1.setFont(FontManager.getFont(Font.PLAIN, 16));
		searchFacility_textField1.setColumns(10);
		searchFacility_textField1.setBounds(262, 145, 328, 30);
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
		searchFacility_textField2.setFont(FontManager.getFont(Font.PLAIN, 16));
		searchFacility_textField2.setColumns(10);
		searchFacility_textField2.setBounds(262, 181, 328, 30);
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
		serverListPane.setBounds(10, 216, 1028, 382);
		infoPanel.add(serverListPane);
		
		serverListTable = new JTable();		
		serverListTable.setForeground(Color.BLACK);		
		serverListTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				selectServer();
			}
						
			public void keyReleased(KeyEvent e) {
				selectServer();
			}
		});
		serverListTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					selectServer();
				} // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					selectServer();
					showFacilityMenu(selectedServer, true);	
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					selectServer();
					showFacilityMenu(selectedServer, true);
				}
			}
		});
		serverListPane.setViewportView(serverListTable);
		
		JScrollPane serverInfoPane = new JScrollPane();		
		serverInfoPane.setBorder(new LineBorder(Color.BLACK, 2));
		serverInfoPane.setBounds(598, 6, 440, 205);	
		infoPanel.add(serverInfoPane);
		
		serverInfoTable = new JTable();
		serverInfoTable.setBorder(new LineBorder(Color.BLACK, 2));		
		serverInfoPane.setViewportView(serverInfoTable);
								
	}
	
	public static void setSqlServerInfo(String sqlServerInfo) {
		sqlServerInfo_label.setText(" " + sqlServerInfo);
	}
	
	public static void selectServer() {
		try {
			int row = serverListTable.getSelectedRow();
			selectedServer = (Server) serverListTable.getValueAt(row, 3);
			
			initEventButton(eventInfo_Button, selectedServer);
			
			if(selectedServer.isFacility()) {
				// 시설물이 선택 되었을 경우
				Facility fac = (Facility)selectedServer;
				if(fac.isConnRCU()) {
					rcuInfo_Button.setEnabled(true);
					rcuInfo_Button.setBackground(new Color(152, 251, 152));
					rcuInfo_Button.setText("연결된 RCU 정보");
				}else {
					rcuInfo_Button.setEnabled(false);
					rcuInfo_Button.setBackground(Color.WHITE);
					rcuInfo_Button.setText("연결된 RCU 없음");
				}
				
				facilityInfo_Button.setText("시설물 정보");
				facilityInfo_Button.setBackground(Color.ORANGE);
				facilityInfo_Button.setEnabled(true);
				
				updateFacilityInfo((Facility)selectedServer); // <- 시설물 정보 테이블 업데이트
			}else {
				// RCU가 선택 되었을 경우
				facilityInfo_Button.setText("시설물 정보");
				facilityInfo_Button.setBackground(Color.WHITE);
				facilityInfo_Button.setEnabled(false);
								
				rcuInfo_Button.setEnabled(true);
				rcuInfo_Button.setBackground(new Color(152, 251, 152));
				rcuInfo_Button.setText("연결된 장비 목록");
				
				updateRCUInfo((RCU)selectedServer); // <- RCU 정보 테이블 업데이트
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadServerInfo(){
		if(!ONION_Info.hasMk119Connection() || ONION_Info.getMk119Connection() == null) return;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			serverList = new ArrayList<Server>();
			serverMap = new HashMap<Integer, Server>();
			serverGroupMap = ServerGroup.getServerGroup(ONION_Info.getMk119Connection());
			serverGroupMappingInfo = ServerGroup.getServerGroupMapping(ONION_Info.getMk119Connection());
			
			stmt = ONION_Info.getMk119Connection().createStatement();
			
			/* 시설물 초기화 */
			rs = stmt.executeQuery(Facility.GET_FACILITY);
			while(rs.next()) {
				Facility fac = new Facility();
				
				fac.setIp(rs.getString("ip"));
				fac.setPort(rs.getInt("port"));
				fac.setRtuIndex(rs.getInt("rtuIndex"));

				fac.setAgentType(rs.getInt("agentType"));
				
				fac.setIndex(rs.getInt("index"));
				fac.setName(rs.getString("name"));
				
				int groupIndex = serverGroupMappingInfo.get(fac.getIndex());
				ServerGroup group = serverGroupMap.get(groupIndex);
				fac.setGroup(group);
				
				fac.setType(rs.getInt("facType"));
				fac.setTypeString(DbUtil.getFacilityType(fac.getType()));
				
				fac.setConnCode(rs.getInt("connMethod"));
				fac.setConnMethod(DbUtil.getConnMethod(fac.getConnCode()));
				
				fac.setCommProtocol(rs.getInt("commProtocol"));
				fac.setSnmpProtocol(rs.getInt("snmpProtocol"));
				fac.setIsProtocol((fac.getCommProtocol() > fac.getSnmpProtocol()) ? true : false);
				
				fac.setStateCode(rs.getInt("condition"));
				fac.setState(DbUtil.getState(fac.getStateCode()));

				// BACnet 연결 포트 : 47808 (0xBAC0) 
				if(fac.getConnCode() == 128) {
					fac.setPort(0xBAC0);
				}
				
				// 그룹 정보를 제외하고 인덱스를 포함한 모든 정보가 중복되는 시설물이 존재한다
				if(serverMap.containsKey(fac.getIndex())) {
					Facility originFac = (Facility)serverMap.get(fac.getIndex());
					originFac.setOverlapping(true); // 해당 장비는 중복이 존재하는 장비이다
					continue;
				}
				
				serverList.add(fac);
				serverMap.put(fac.getIndex(), fac);
			}
			
			/* RCU 초기화 */
			rs = stmt.executeQuery(RCU.GET_RTU);
			while(rs.next()) {
				RCU rcu = new RCU();
				
				rcu.setIp(rs.getString("ip"));
				rcu.setPort(rs.getInt("port"));				

				rcu.setAgentType(rs.getInt("agentType"));
				
				rcu.setIndex(rs.getInt("index"));
				rcu.setName(rs.getString("name"));
				
				int groupIndex = serverGroupMappingInfo.get(rcu.getIndex());
				ServerGroup group = serverGroupMap.get(groupIndex);
				rcu.setGroup(group);
				
				rcu.setType(rs.getInt("rtuType"));
				rcu.setTypeString("RCU");
				rcu.setRcuTypeDetail(DbUtil.getRcuType(rcu.getType()));
				
				rcu.setStateCode(rs.getInt("condition"));
				rcu.setState(DbUtil.getState(rcu.getStateCode()));
				
				switch(rcu.getType()) {
					case RCU.RTU_TYPE_DY_MUX : // MK_RCU_V1.0
					case RCU.RTU_TYPE_REM2408 : // MK119_-_REM_2408
					case RCU.RTU_TYPE_REM1204 : // MK119_-_REM_1204
					case RCU.RTU_TYPE_REM1204v103 : // MK119_-_REM_1204_v1.0.3
						rcu.setPort(RCU.DEFAULT_PORT);
						break;

					case RCU.RTU_TYPE_DUPLEXED_TCP : // TCP/IP_이중화_RCU
						rcu.setDuplexedPort(true);
						rcu.setAuxIP(rs.getString("auxIP"));
						rcu.setAuxPort(rs.getInt("auxPort"));
						break;
						
					case RCU.RTU_TYPE_MQTT_BROKER : // MQTT_Broker
						rcu.setPort(rs.getInt("mqttPort"));
						break;
				}
				
				// 그룹 정보를 제외하고 인덱스를 포함한 모든 정보가 중복되는 장비가 존재한다
				if(serverMap.containsKey(rcu.getIndex())) {
					RCU originRCU = (RCU)serverMap.get(rcu.getIndex());
					originRCU.setOverlapping(true); // 해당 RCU는 중복이 존재하는 RCU이다
					continue;
				}
				
				serverList.add(rcu);
				serverMap.put(rcu.getIndex(), rcu);
			}
			
		
			/* 멀티 포트 RCU 포트 채널 매핑 정보 초기화 */
			rs = stmt.executeQuery(MultiPortMap.GET_MULTI_PORT_MAP);
			while(rs.next()) {
				MultiPortMap map = new MultiPortMap();
				int rcuIndex = rs.getInt("rtuindex");
				int ch = rs.getInt("ch");
				int port = rs.getInt("port");
				int facIndex = rs.getInt("facIndex");
				
				map.setRtuIndex(rcuIndex);
				map.setCh(ch);
				map.setPort(port);
				map.setFacIndex(facIndex);
				
				RCU rcu = (RCU)serverMap.get(rcuIndex);
				rcu.setMultiPort(true);
				rcu.getMultiPortMapList().add(map);
				
				if(serverMap.containsKey(facIndex)) {
					Facility fac = (Facility)serverMap.get(facIndex);
					fac.setRcuPortCh(ch);
					fac.setPort(port);
				}
			}
			
			// Server List 정렬
			Collections.sort(serverList);
			
			/* RCU & 시설물 매핑 */
			for(int i = 0; i < serverList.size(); i++) {
				Server server = serverList.get(i);
				
				if(server.isFacility()) {
					Facility fac = (Facility)serverMap.get(server.getIndex());					
					int rtuIndex = fac.getRtuIndex();
						
						try {
							if(rtuIndex != 0) {
								RCU rcu = (RCU)serverMap.get(rtuIndex);
								rcu.getFacList().add(fac);
								fac.setConnRCU(true);
								fac.setRcu(rcu);								
								fac.setIp(rcu.getIp()); // 시설물의 IP를 RCU에 등록된 IP로 설정
								
								// 시설물에 RCU 포트 채널, 포트 번호 저장
								if(!rcu.isMultiPort()){
									fac.setRcuPortCh(fac.getPort());
									fac.setPort(rcu.getPort());
								}
								
							}else {
								continue;
							}
						}catch(NullPointerException e) {
//							e.printStackTrace();
							
							RCU rcu = new RCU();
							rcu.setIndex(rtuIndex);
							rcu.setName("알 수 없음");
							rcu.setTypeString("알 수 없음");
							rcu.setRcuTypeDetail("알 수 없음");
							rcu.setIp("알 수 없음");
							rcu.setState("알 수 없음");
							fac.setRcu(rcu);
							
							String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
							if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
								groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
							}else {
								groupInfo = groupInfo.replace(ServerGroup.ROOT, "장비 관리  ( 그룹 없음 )");
							}
							
							if(isFirstLoad) {
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%s%s%s\n", Util.colorRed("Can Not Found RCU"), Util.separator, Util.separator));
								
								sb.append(Util.colorRed("알 수 없는 RCU 인덱스 : ") + rtuIndex + Util.separator + Util.separator + "\n\n");
								
								sb.append(Util.colorBlue("그룹 정보 : ") + groupInfo + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("시설물 종류 : ") + server.getTypeString() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("장비명 : ") + server.getName() + Util.separator + Util.separator + "\n");							
								sb.append(Util.colorBlue("장비 인덱스 : ") + server.getIndex() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("연결 방식 : ") + ((Facility)server).getConnMethod() + Util.separator + Util.separator + "\n\n");
								
								sb.append("위의 장비가 바라보는 " + Util.colorRed("RCU") + " 장비를 찾을 수 없습니다" + Util.separator + Util.separator +"\n\n");
								sb.append("해당 현상은 " + Util.colorRed("RCU") + " 장비와 " + Util.colorBlue("시설물") + "이 연결된 상태에서 " + Util.colorRed("RCU") + " 장비가 삭제 되었을 경우 발생 할 수 있습니다" + Util.separator + Util.separator +"\n");
								
								int menu = Util.showOption(sb.toString(), new String[] { "확 인", " 동일한 내용의 메시지 그만보기 "}, JOptionPane.ERROR_MESSAGE);
								switch (menu) {
									case 1: // 동일한 내용의 메시지 그만보기
										isFirstLoad = false;
										continue;
									case 0: // 확 인	
									default :
										continue;
								}// switch
								
							}
						}
					}
			}
			
			ArrayList<Event> eventList = Event.getEvents(ONION_Info.getMk119Connection());
			for(int i = 0; i < eventList.size(); i++) {
				Event event = eventList.get(i);
				int serverIndex = event.getServerIndex();
				
				if(serverMap.containsKey(serverIndex)) {
					Server server = serverMap.get(serverIndex);
					server.getEvents().add(event);
				}
			}
			
			isFirstLoad = false;
			
			if(selectedServer != null) {
				int lastSelectedServerIndex = selectedServer.getIndex();
				selectedServer = serverMap.get(lastSelectedServerIndex);
			}
			
			if(rs != null && !rs.isClosed()) rs.close();
			if(stmt != null && !stmt.isClosed()) stmt.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("SQL Exception"), Util.separator, Util.separator));
			sb.append("데이터베이스 로드에 실패하였습니다" +  Util.separator + Util.separator + "\n\n");
			sb.append("데이터베이스 커넥션이 끊어졌을 경우 데이터베이스 재접속으로 해당 문제가 해결 될 수 있습니다" +  Util.separator + Util.separator + "\n\n");
			sb.append(Util.colorRed("Exception Message : ") + e.getMessage() + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void updateServerListTable(boolean databaseLoad) {		
		if(databaseLoad) loadServerInfo();
		if(serverList == null) return;

		Object[][] content = new Object[serverList.size()][];

		for (int i = 0; i < serverList.size(); i++) {
			Server server = serverList.get(i);
			content[i] = new Object[6];
			content[i][0] = i + 1;
			
			String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
			if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
				groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
			}else {
				groupInfo = groupInfo.replace(ServerGroup.ROOT, "장비 관리  ( 그룹 없음 )");
			}
			content[i][1] = groupInfo;
			
			content[i][2] = server.getTypeString();
			content[i][3] = server;
			content[i][4] = server.getState();
			content[i][5] = (server.hasEvent()) ? server.getEvents().get(0).getSeverityName() : "";	
		}

		serverListTable.setModel(new DefaultTableModel(
			content,
			new String[] { ORDER, GROUP_INFO, SERVER_TYPE, SERVER_NAME, SERVER_STATE, EVENT }) {
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
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(10); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // 그룹 정보
		table.getColumnModel().getColumn(2).setPreferredWidth(60); // 시설물 종류
		table.getColumnModel().getColumn(3).setPreferredWidth(150); // 장비명
		table.getColumnModel().getColumn(4).setPreferredWidth(50); // 장비 상태
		table.getColumnModel().getColumn(5).setPreferredWidth(50); //  이벤트
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		FindTextRenderer findCommerRenderer = new FindTextRenderer(4, STATE_COMMER, Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		FindTextRenderer findRCURenderer = new FindTextRenderer(2, "RCU", new Color(152, 251, 152));
		findRCURenderer.setHorizontalAlignment(SwingConstants.CENTER);
				
		SeverityRenderer severityRenderer = null;
		ArrayList<SystemSeverity> severityList = null;
		try {
			severityList= SystemSeverity.getSystemSeverity(ONION_Info.getMk119Connection());
		}catch(Exception e) {
			e.printStackTrace();
			severityList = SystemSeverity.getDefaultSystemSeverity();
		}
		severityRenderer = new SeverityRenderer(5, 15, severityList);
		severityRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 그룹 정보
		tcmSchedule.getColumn(2).setCellRenderer(findRCURenderer); // 시설물 종류
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 장비명
		tcmSchedule.getColumn(4).setCellRenderer(findCommerRenderer); // 장비 상태
		tcmSchedule.getColumn(5).setCellRenderer(severityRenderer); // 이벤트
	}
		
	public static void updateFacilityInfo(Facility fac) {
		if(fac == null) {
			eventInfo_Button.setText("이벤트 정상");
			eventInfo_Button.setEnabled(false);
			eventInfo_Button.setBackground(Color.WHITE);
			eventInfo_Button.setForeground(Color.BLACK);
			
			facilityInfo_Button.setEnabled(false);
			facilityInfo_Button.setBackground(Color.WHITE);			
			rcuInfo_Button.setEnabled(false);
			rcuInfo_Button.setBackground(Color.WHITE);
			
			serverInfoTable.setModel(new DefaultTableModel(
					new Object[][] {
						{ null, null },
						{ null, null },
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
			setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
			return;		
		}
		
		int index = 0;
		Object[][] content = new Object[7][];
		
		// 시설물 종류
		content[index] = new Object[2];
		content[index][0] = FACILITY_TYPE;
		content[index++][1] = fac.getTypeString();
		
		// 장비명
		content[index] = new Object[2];
		content[index][0] = SERVER_NAME;
		content[index++][1] = fac;
		
		// 장비 인덱스
		content[index] = new Object[2];
		content[index][0] = SERVER_INDEX;
		content[index++][1] = fac.getIndex();
		
		// IP 주소
		content[index] = new Object[2];
		content[index][0] = "연결 정보";
		String connInfo = fac.getIp() + " : " + fac.getPort();
		content[index++][1] = fac.isConnRCU() ? "( RCU ) " + connInfo : connInfo ;
				
		// 연결 방식
		content[index] = new Object[2];
		content[index][0] = CONN_METHOD;
		content[index++][1] = fac.getConnMethod();
		
		// 프로토콜 정보
		if(linkMK119_Protocol) {
			content[index] = new Object[2];
			content[index][0] = PROTOCOL_NAME;
			Protocol p = protocolMap.get(fac.getProtocolKey());
			if(p != null) {
				content[index++][1] = p.getName();
			}else {
				content[index++][1] = "Unknown ( 알 수 없음 )";
			}
		}else {
			content[index] = new Object[2];
			content[index][0] = PROTOCOL_NUMBER;
			content[index++][1] = fac.isProtocol() ? fac.getCommProtocol() : fac.getSnmpProtocol();
		}
		
		// 장비 상태
		content[index] = new Object[2];
		content[index][0] = SERVER_STATE;
		content[index++][1] = fac.getState();

		serverInfoTable.setModel(new DefaultTableModel(
			content,
			new String[] { "항 목", "내 용" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
	}
	
	public static void updateRCUInfo(RCU rcu) {
		if(rcu == null) {
			eventInfo_Button.setText("이벤트 정상");
			eventInfo_Button.setEnabled(false);
			eventInfo_Button.setBackground(Color.WHITE);
			eventInfo_Button.setForeground(Color.BLACK);
			
			facilityInfo_Button.setEnabled(false);
			facilityInfo_Button.setBackground(Color.WHITE);			
			rcuInfo_Button.setEnabled(false);
			rcuInfo_Button.setBackground(Color.WHITE);
			
			serverInfoTable.setModel(new DefaultTableModel(
					new Object[][] {
						{ null, null },
						{ null, null },
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
			setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
			return;		
		}
		
		int index = 0;
		Object[][] content = new Object[7][];
		
		// RCU 종류
		content[index] = new Object[2];
		content[index][0] = RCU_TYPE;
		content[index++][1] = rcu.getRcuTypeDetail();
		
		// RCU 이름
		content[index] = new Object[2];
		content[index][0] = "RCU 이름";
		content[index++][1] = rcu;
		
		// RCU 인덱스
		content[index] = new Object[2];
		content[index][0] = "RCU 인덱스";
		content[index++][1] = rcu.getIndex();
		
		// IP 정보
		String ipInfo = "";
		if(rcu.isDuplexedPort()) {
			ipInfo += rcu.getIp();
			ipInfo += " & ";
			ipInfo += rcu.getAuxIP();
		}else {
			ipInfo += rcu.getIp();
		}
		
		content[index] = new Object[2];
		content[index][0] = "IP 정보";
		content[index++][1] = ipInfo;
		
		// Port 정보
		
		String portInfo = "";			
		if(rcu.isMultiPort()) {
			ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
			MultiPortMap start = portMap.get(0);
			MultiPortMap end = portMap.get(portMap.size() - 1);
			
			portInfo += start.getCh() + " ( " + start.getPort() + " )";
			portInfo += " ~ ";
			portInfo += end.getCh() + " ( " + end.getPort() + " ) ";
		}else if(rcu.isDuplexedPort()) {
			portInfo += rcu.getPort();
			portInfo += " & ";
			portInfo += rcu.getAuxPort();
		}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
			portInfo += rcu.getPort();
		}else if(!rcu.isMultiPort() && rcu.getPort() == 0){
			portInfo += "Unknown";
		}else {
			portInfo += "Unknown";
		}
		
		content[index] = new Object[2];
		content[index][0] = "Port 정보";
		content[index++][1] = portInfo;
		
		// 연결된 장비 개수
		content[index] = new Object[2];
		content[index][0] = "연결된 장비 개수";
		content[index++][1] = rcu.getFacList().size();
		
		// RCU 상태
		content[index] = new Object[2];
		content[index][0] = "RCU 상태";
		content[index++][1] = rcu.getState();

		serverInfoTable.setModel(new DefaultTableModel(
			content,
			new String[] { "항 목", "내 용" }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setServerInfoTableStyle(serverInfoTable, new Color(152, 251, 152));
	}
	
	public static void setServerInfoTableStyle(JTable table, Color headerColor) {
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(headerColor);
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowMargin(3);
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 필 드
		table.getColumnModel().getColumn(1).setPreferredWidth(190); // 내 용
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		FindTextRenderer findCommerRenderer = new FindTextRenderer(1, STATE_COMMER, Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 필 드
		tcmSchedule.getColumn(1).setCellRenderer(findCommerRenderer); // 내 용
	}
		
	public static void showFacilityMenu(Server server, boolean showRcuInfo) {
		if(server == null) return;

		int menu = -1;
		String separator = Util.separator + Util.separator;
		
		if(server.isFacility()) {
			Facility fac = (Facility)server;
			
			// 시설물
			StringBuilder msg = new StringBuilder();
						
			msg.append(String.format("%s%s%s\n", Util.colorBlue("──────────[ 시설물 정보 ]──────────"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("시설물 종류"), fac.getTypeString(), separator, separator));
			
			if(linkMK119_Protocol) {
				Protocol p = protocolMap.get(fac.getProtocolKey());
				String pName = null;
				if(p != null) {
					pName = p.getName();
				}else {
					pName = "Unknown ( 알 수 없음 )";
				}
				msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("프로토콜"), pName, separator, separator));
			}
			
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("장비명"), fac.getName(), separator, separator));
			msg.append(String.format("%s : %d%s%s\n", Util.colorBlue("장비 인덱스"), fac.getIndex(), separator, separator));
			
			String connInfo = "";
			connInfo += Util.colorRed("IP") + " : " + fac.getIp();
			
			// TCP/IP 이중화 RCU
			if(fac.isConnRCU() && fac.getRcu() != null) {
				if(fac.getRcu().isDuplexedPort()) {
					connInfo += Util.colorBlue(" & ") + fac.getRcu().getAuxIP();
				}
			}
			
			connInfo += "&nbsp;&nbsp;" + Util.colorBlue("/") + "&nbsp;&nbsp;";
			connInfo += Util.colorRed("Port") + " : "+ fac.getPort();
			if(fac.isConnRCU()) {
				connInfo = Util.colorRed("( RCU ) ") + connInfo;
				
				// TCP/IP 이중화 RCU
				if(fac.getRcu() != null) {
					if(fac.getRcu().isDuplexedPort()) {
						connInfo += Util.colorBlue(" & ") + fac.getRcu().getAuxPort();
					}
				}
			}
			
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("연결 정보"), connInfo, separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("연결 방식"), fac.getConnMethod(), separator, separator));
			
			if(fac.hasEvent()) {
				Event e = fac.getEvents().get(0);
				String status = (e.getStatus() == 0) ? Util.colorRed("이벤트 발생 상태") : Util.colorRed("이벤트 인지 상태");
				String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
				msg.append(String.format("%s : %s %s%s%s\n", Util.colorBlue("이벤트 상태"), eventInfo, status,separator, separator));
			}
			
			if(fac.getRcu() != null) {
				
				RCU rcu = fac.getRcu();
				String unknown = "알 수 없음";
				boolean unknownRCU = false;
				
				if(rcu.getName().equals(unknown) 
						&& rcu.getTypeString().equals(unknown) 
						&& rcu.getRcuTypeDetail().equals(unknown) 
						&& rcu.getIp().equals(unknown) 
						&& rcu.getState().equals(unknown)) {
					unknownRCU = true;
				}
				
				if(unknownRCU) {
					msg.append(String.format("\n%s%s%s\n", Util.colorRed("──────────[ 알 수 없는 RCU 정보 ]──────────"), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 종류"), rcu.getRcuTypeDetail(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 이름"), rcu.getName(), separator, separator));
					msg.append(String.format("%s : %d%s%s\n", Util.colorRed("RCU 인덱스"), rcu.getIndex(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU IP"), rcu.getIp(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU Port"), unknown, separator, separator));
					msg.append(String.format("%s : %d개%s%s\n\n", Util.colorRed("연결된 장비 개수"), rcu.getFacList().size(), separator, separator));
					msg.append(String.format("%s%s%s\n", "해당 " + Util.colorBlue("시설물") + "은 " + Util.colorRed("RCU") + " 장비와 " + Util.colorBlue("시설물") + "이 연결된 상태에서 " + Util.colorRed("RCU") + " 장비가 삭제 되었을 수 있습니다", separator, separator));
				}else {
					msg.append(String.format("\n%s%s%s\n", Util.colorGreen("──────────[ RCU 정보 ]──────────"), separator, separator));		
					msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 종류"), rcu.getRcuTypeDetail(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 이름"), rcu.getName(), separator, separator));
					msg.append(String.format("%s : %d%s%s\n", Util.colorGreen("RCU 인덱스"), rcu.getIndex(), separator, separator));
					
					// ****** [ RCU 연결 정보 ] ********************************************************************************************
					
					String ipInfo = "";
					if(rcu.isDuplexedPort()) {
						ipInfo += rcu.getIp();
						ipInfo += Util.colorBlue(" & ");
						ipInfo += rcu.getAuxIP();
					}else {
						ipInfo += rcu.getIp();
					}
					msg.append(String.format("%s%s : %s%s%s\n", Util.colorGreen("RCU "), Util.colorRed("IP") ,ipInfo, separator, separator));
					
					String portInfo = "";			
					if(rcu.isMultiPort()) {
						ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
						MultiPortMap start = portMap.get(0);
						MultiPortMap end = portMap.get(portMap.size() - 1);
						
						portInfo += start.getCh() + " ( " + start.getPort() + " )";
						portInfo += Util.colorBlue(" ~ ");
						portInfo += end.getCh() + " ( " + end.getPort() + " ) ";
					}else if(rcu.isDuplexedPort()) {
						portInfo += rcu.getPort();
						portInfo += Util.colorBlue(" & ");
						portInfo += rcu.getAuxPort();
					}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
						portInfo += rcu.getPort();
					}else if(!rcu.isMultiPort() && rcu.getPort() == 0){
						portInfo += "Unknown";
					}else {
						portInfo += "Unknown";
					}
					msg.append(String.format("%s%s : %s%s%s\n", Util.colorGreen("RCU "), Util.colorRed("Port") , portInfo, separator, separator));
					// ***********************************************************************************************************************
										
					msg.append(String.format("%s : %d개%s%s\n", Util.colorGreen("연결된 장비 개수"), rcu.getFacList().size(), separator, separator));
					
					if(rcu.hasEvent()) {
						Event e = rcu.getEvents().get(0);
						String status = (e.getStatus() == 0) ? Util.colorRed("이벤트 발생 상태") : Util.colorRed("이벤트 인지 상태");
						String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
						msg.append(String.format("%s : %s %s%s%s\n", Util.colorGreen("이벤트 상태"), eventInfo, status,separator, separator));
					}
					
				}
			}
			
			if(fac.isConnRCU() && fac.getRcu() != null && showRcuInfo) {
				
				if(fac.hasEvent()) {
					menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "RCU 정보 보기",  "이벤트 내용 보기", " 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);
				}else {
					menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "RCU 정보 보기",  " 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);	
				}
				
				switch (menu) {
					case 0:// 성능 정보 보기
						new FacilityInfoFrame(fac);
						return;
						
					case 1:
						// RCU 정보 보기
						new RcuInfoFrame(fac.getRcu());
						return;
						
					case 2:
						// 이벤트 내용 보기
						if(fac.hasEvent()) {
							Event.showDetailEventInfo(fac);
						}
						return;
						
					default :
						return;
				}
			}else {
				
				if(fac.hasEvent()) {
					menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "이벤트 내용 보기", " 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);
				}else {
					menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", " 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);	
				}
				
				switch (menu) {
					case 0: // 성능 정보 보기
						new FacilityInfoFrame(fac);
						return;			
						
					case 1:
						// 이벤트 내용 보기
						if(fac.hasEvent()) {
							Event.showDetailEventInfo(fac);
						}
						return;
						
					default :
						return;
				}
			}
			
		}else {
			// RCU
			RCU rcu = (RCU)server;
			
			StringBuilder msg = new StringBuilder();
			msg.append(String.format("%s%s%s\n", Util.colorGreen("──────────[ RCU 정보 ]──────────"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 종류"), rcu.getRcuTypeDetail(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 이름"), rcu.getName(), separator, separator));
			msg.append(String.format("%s : %d%s%s\n", Util.colorGreen("RCU 인덱스"), rcu.getIndex(), separator, separator));
			// ****** [ RCU 연결 정보 ] ********************************************************************************************
			
			String ipInfo = "";
			if(rcu.isDuplexedPort()) {
				ipInfo += rcu.getIp();
				ipInfo += Util.colorBlue(" & ");
				ipInfo += rcu.getAuxIP();
			}else {
				ipInfo += rcu.getIp();
			}
			msg.append(String.format("%s%s : %s%s%s\n", Util.colorGreen("RCU "), Util.colorRed("IP") ,ipInfo, separator, separator));
			
			String portInfo = "";
			if(rcu.isMultiPort()) {
				ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
				MultiPortMap start = portMap.get(0);
				MultiPortMap end = portMap.get(portMap.size() - 1);
				
				portInfo += start.getCh() + " ( " + start.getPort() + " )";
				portInfo += Util.colorBlue(" ~ ");
				portInfo += end.getCh() + " ( " + end.getPort() + " ) ";
			}else if(rcu.isDuplexedPort()) {
				portInfo += rcu.getPort();
				portInfo += Util.colorBlue(" & ");
				portInfo += rcu.getAuxPort();
			}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
				portInfo += rcu.getPort();
			}else if(!rcu.isMultiPort() && rcu.getPort() == 0){
				portInfo += "Unknown";
			}else {
				portInfo += "Unknown";
			}
			msg.append(String.format("%s%s : %s%s%s\n", Util.colorGreen("RCU "), Util.colorRed("Port") , portInfo, separator, separator));
			// ***********************************************************************************************************************			
			msg.append(String.format("%s : %d개%s%s\n", Util.colorGreen("연결된 장비 개수"), rcu.getFacList().size(), separator, separator));
			
			if(rcu.hasEvent()) {
				Event e = rcu.getEvents().get(0);
				String status = (e.getStatus() == 0) ? Util.colorRed("이벤트 발생 상태") : Util.colorRed("이벤트 인지 상태");
				String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
				msg.append(String.format("%s : %s %s%s%s\n", Util.colorGreen("이벤트 상태"), eventInfo, status,separator, separator));
			}
			
			if(rcu.hasEvent()) {
				menu = Util.showOption(msg.toString(), new String[] { "연결된 장비 목록 보기", "이벤트 내용 보기"," 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);
			}else {
				menu = Util.showOption(msg.toString(), new String[] { "연결된 장비 목록 보기", " 취 소 "}, JOptionPane.INFORMATION_MESSAGE, false);	
			}
			
			switch (menu) {
				case 0: // 연결된 장비 목록 보기
					new RcuInfoFrame(rcu);
					return;
					
				case 1 : // 이벤트 내용 보기
					if(rcu.hasEvent()) {
						Event.showDetailEventInfo(rcu);
					}
					return;
					
				default :
					return;
			}
		}
	}
	
	public static void resetForm(boolean databaseLoad, boolean allComponentReset) {
		updateServerListTable(databaseLoad);
		
		if(selectedServer != null && selectedServer.isFacility()) {
			updateFacilityInfo((Facility)selectedServer);
		}else if(selectedServer != null && selectedServer.isRCU()){
			updateRCUInfo((RCU)selectedServer);
		}else {
			updateFacilityInfo(null);
		}
		
		if(allComponentReset) {
			if(searchFacility_textField1 != null) searchFacility_textField1.setText(null);
			if(searchFacility_textField2 != null) searchFacility_textField2.setText(null);
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedIndex(0);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedIndex(2);
		}
		
		doTableFilter();
	}
	
	public static void doTableFilter() {
		ArrayList<Server> filteredServer = new ArrayList<Server>();
		String text_1 = searchFacility_textField1.getText().trim();
		String text_2 = searchFacility_textField2.getText().trim();
		
		boolean noSearch_1 = (text_1 == null || text_1.length() == 0 || text_1.equals(""));
		boolean noSearch_2 = (text_2 == null || text_2.length() == 0 || text_2.equals(""));
		
		if(noSearch_1 && noSearch_2) {	
			updateServerListTable(false);
			return;
		}
		
		text_1 = text_1.toUpperCase();
		text_2 = text_2.toUpperCase();
		
		for(int i = 0; i < serverList.size(); i++) {
			Server server = serverList.get(i);
			
			String searchElement_1 = null;
			String searchElement_2 = null;
			
			switch(searchFacility_ComboBox1.getSelectedItem().toString()) {
				case IP :  // IP 주소
					searchElement_1 = server.getIp();
					break;
				case GROUP_INFO :  // 그룹 정보
					String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
					if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
						groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
					}else {
						groupInfo = groupInfo.replace(ServerGroup.ROOT, "장비 관리  ( 그룹 없음 )");
					}
					searchElement_1 = groupInfo;
					break;
				case SERVER_INDEX : // 장비 인덱스
					searchElement_1 = String.valueOf(server.getIndex());
					break;
				case SERVER_NAME : // 장비명
					searchElement_1 = server.getName();
					break;
				case SERVER_TYPE : // 시설물 종류
					searchElement_1 = server.getTypeString();
					break;
				case CONN_METHOD : // 연결 방식
					if(server.isFacility()) {
						searchElement_1 = ((Facility)server).getConnMethod();
					}else {
						searchElement_1 = "";
					}
					break;
				case SERVER_STATE : // 장비 상태
					searchElement_1 = server.getState();
					break;
				case PROTOCOL_NUMBER : // 프로토콜 번호
					if(server.isFacility()) {						
						searchElement_1 = String.valueOf((((Facility)server).isProtocol()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_1 = "";
					}					
					break;
				case PROTOCOL_NAME : // 프로토콜 이름
					if(server.isFacility()) {
						Facility fac = (Facility)server;
						Protocol p = protocolMap.get(fac.getProtocolKey());
						if(p != null) {
							searchElement_1 = p.getName();	
						}else {
							searchElement_1 = "";
						}
					}else {
						searchElement_1 = "";
					}
					break;
				case EVENT :
					searchElement_1 = (server.hasEvent()) ? server.getEvents().get(0).getSeverityName() : "";
					break;
			}// switch - searchElement_1
			
			switch(searchFacility_ComboBox2.getSelectedItem().toString()) {
				case IP :  // IP 주소
					searchElement_2 = server.getIp();
					break;
				case GROUP_INFO :  // 그룹 정보
					String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
					if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
						groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
					}else {
						groupInfo = groupInfo.replace(ServerGroup.ROOT, "장비 관리  ( 그룹 없음 )");
					}
					searchElement_2 = groupInfo;
					break;
				case SERVER_INDEX : // 장비 인덱스
					searchElement_2 = String.valueOf(server.getIndex());
					break;
				case SERVER_NAME : // 장비명
					searchElement_2 = server.getName();
					break;
				case SERVER_TYPE : // 시설물 종류
					searchElement_2 = server.getTypeString();
					break;
				case CONN_METHOD : // 연결 방식
					if(server.isFacility()) {
						searchElement_2 = ((Facility)server).getConnMethod();	
					}else {
						searchElement_2 = "";
					}
					break;
				case SERVER_STATE : // 장비 상태
					searchElement_2 = server.getState();
					break;
				case PROTOCOL_NUMBER : // 프로토콜 번호
					if(server.isFacility()) {						
						searchElement_2 = String.valueOf((((Facility)server).isProtocol()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_2 = "";
					}
					break;
				case PROTOCOL_NAME : // 프로토콜 이름
					if(server.isFacility()) {
						Facility fac = (Facility)server;
						Protocol p = protocolMap.get(fac.getProtocolKey());
						if(p != null) {
							searchElement_2 = p.getName();	
						}else {
							searchElement_2 = "";
						}
					}else {
						searchElement_2 = "";
					}
					break;
				case EVENT :
					searchElement_2 = (server.hasEvent()) ? server.getEvents().get(0).getSeverityName() : "";
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
					String token = textToken[i2].trim();
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
					String token = textToken[i2].trim();
					if(searchElement_2.contains(token)) {
						isContain_2 = true;
					}
				}
			}else if(searchElement_2.contains(text_2)) {
				isContain_2 = true;
			}// set isContain_2
			
			if(isContain_1 && isContain_2) {
				filteredServer.add(server);
			}// AND Operation isContains 1, 2
			
		}// for loop
		
		Object[][] content = new Object[filteredServer.size()][];
		
		for (int i = 0; i < filteredServer.size(); i++) {
			Server server = filteredServer.get(i);
			content[i] = new Object[6];
			content[i][0] = i + 1;
			
			String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
			if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
				groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
			}else {
				groupInfo = groupInfo.replace(ServerGroup.ROOT, "장비 관리  ( 그룹 없음 )");
			}
			content[i][1] = groupInfo;
			
			content[i][2] = server.getTypeString();
			content[i][3] = server;
			content[i][4] = server.getState();
			content[i][5] = (server.hasEvent()) ? server.getEvents().get(0).getSeverityName() : "";
		}

		serverListTable.setModel(new DefaultTableModel(
			content, 			
			new String[] { ORDER, GROUP_INFO, SERVER_TYPE, SERVER_NAME, SERVER_STATE, EVENT }) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(serverListTable);
	}
	
	
	public static void printRcuInformation() {
		// 멀티 포트 RCU 확인용
		for (int i = 0; i < serverList.size(); i++) {
			Server server = serverList.get(i);
			if (server.isRCU()) {
				RCU rcu = (RCU) serverMap.get(server.getIndex());

				System.out.println("================================================================\n");
				System.out.println("index : " + rcu.getIndex());
				System.out.println("name : " + rcu.getName());
				System.out.println("type : " + ((RCU) rcu).getRcuTypeDetail());
				System.out.println("ip : " + rcu.getIp());

				if (!rcu.isMultiPort()) {
					System.out.println("port : " + rcu.getPort());
				} else {
					ArrayList<MultiPortMap> mapList = rcu.getMultiPortMapList();
					for (int k = 0; k < mapList.size(); k++) {
						MultiPortMap map = mapList.get(k);
						System.out.printf("%d. port ch [ %d ] : %d", k + 1, map.getCh(), map.getPort());

						if (map.getFacIndex() != 0) {
							Facility fac = (Facility) serverMap.get(map.getFacIndex());
							System.out.printf("  <=> Facility : %d ( %s )\n", fac.getIndex(), fac.getName());
						} else {
							System.out.println();
						}

					}
				}

				System.out.println("[ Connected Facility List ]");

				ArrayList<Server> rcuFacList = rcu.getFacList();
				Collections.sort(rcuFacList);
				for (int j = 0; j < rcuFacList.size(); j++) {
					Server fac = rcuFacList.get(j);
					System.out.print((j + 1) + ". FacIndex : " + fac.getIndex());
					System.out.println(" | Name : " + fac.getName() + " | Type : " + fac.getTypeString());
				}

				System.out.println();
			}
		}
	}
	
	public static void initEventButton(JButton eventButton, Server server) {
		if(server.hasEvent()) {
			Event event = server.getEvents().get(0);
			
			eventButton.setEnabled(true);
			eventButton.setBackground(new Color(event.getSeverityBkColor()));
			eventButton.setForeground(new Color(event.getSeverityTextColor()));
			
			String status = (event.getStatus() == 0) ? "발생" : "인지";
			status = String.format("%s ( %s )", status, event.getSeverityName());
			eventButton.setText(status);
		}else {
			eventButton.setEnabled(false);
			eventButton.setBackground(Color.WHITE);
			eventButton.setForeground(Color.BLACK);
			eventButton.setText("이벤트 정상");
		}
	}
	
	public static void updateItem_searchComboBox(boolean isConnectProtocol) {
		if(isConnectProtocol) {
			String item_1 = GROUP_INFO;
			String item_2 = SERVER_NAME;
			if(searchFacility_ComboBox1 != null) item_1 = searchFacility_ComboBox1.getSelectedItem().toString();
			if(searchFacility_ComboBox2 != null) item_2 = searchFacility_ComboBox2.getSelectedItem().toString();
			
			searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // 그룹 정보
					SERVER_TYPE, // 시설물 종류
					SERVER_NAME, // 장비명
					SERVER_INDEX, // 장비 인덱스
					IP, // IP 주소
					CONN_METHOD, // 연결 방식
					PROTOCOL_NUMBER, // 프로토콜 번호
					PROTOCOL_NAME, // 프로토콜 이름
					SERVER_STATE, // 장비 상태
					EVENT
					}));
			searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // 그룹 정보
					SERVER_TYPE, // 시설물 종류
					SERVER_NAME, // 장비명
					SERVER_INDEX, // 장비 인덱스
					IP, // IP 주소
					CONN_METHOD, // 연결 방식
					PROTOCOL_NUMBER, // 프로토콜 번호
					PROTOCOL_NAME, // 프로토콜 이름
					SERVER_STATE, // 장비 상태
					EVENT
					}));
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedItem(item_1);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedItem(item_2);
		}else {
			searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // 그룹 정보
					SERVER_TYPE, // 시설물 종류
					SERVER_NAME, // 장비명
					SERVER_INDEX, // 장비 인덱스
					IP, // IP 주소
					CONN_METHOD, // 연결 방식
					PROTOCOL_NUMBER, // 프로토콜 번호
					SERVER_STATE, // 장비 상태
					EVENT
					}));
			searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // 그룹 정보
					SERVER_TYPE, // 시설물 종류
					SERVER_NAME, // 장비명
					SERVER_INDEX, // 장비 인덱스
					IP, // IP 주소
					CONN_METHOD, // 연결 방식
					PROTOCOL_NUMBER, // 프로토콜 번호
					SERVER_STATE, // 장비 상태
					EVENT
					}));
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedItem(GROUP_INFO);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedItem(SERVER_NAME);
		}
	}
	
	
}

