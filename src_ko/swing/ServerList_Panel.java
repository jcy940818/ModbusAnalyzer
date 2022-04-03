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

import common.server.Facility;
import common.server.MultiPortMap;
import common.server.RCU;
import common.server.Server;
import common.util.FindTextRenderer;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;
import sun.nio.ch.SelChImpl;

public class ServerList_Panel extends JPanel {
	
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
	public static final String SERVER_STATE = "장비 상태";	
	public static final String PROTOCOL_NUMBER = "프로토콜 번호";
	
	public static final String STATE_COMMER = "통신 오류";
	
	public static JLabel sqlServerInfo_label;
	
	private JPanel infoPanel;
		
	public static ArrayList<Server> serverList;
	public static HashMap<Integer, Server> serverMap;
	private static Server selectedServer;
	private static JTextField searchFacility_textField1;
	private static JTextField searchFacility_textField2;
	private static JComboBox searchFacility_ComboBox1; 
	private static JComboBox searchFacility_ComboBox2;	
	private static JTable serverListTable;
	private static JTable serverInfoTable;
	
	private static JButton rcuInfo_Button;
	private static JButton perfInfo_Button;
	private static JButton updateDB_Button;
	private static JButton resetForm_button;
	
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

		JLabel onion_Logo = new JLabel();
		onion_Logo.setForeground(Color.BLACK);
		onion_Logo.setBackground(Color.WHITE);
		onion_Logo.setIcon(new Util().getSubLogoResource());
		onion_Logo.setBounds(136, 85, 50, 50);
		onion_Logo.setHorizontalAlignment(SwingConstants.LEFT);
		onion_Logo.setFont(new Font("맑은 고딕", Font.BOLD, 22));
//		infoPanel.add(onion_Logo);
		
		sqlServerInfo_label = new JLabel();
		sqlServerInfo_label.setIcon(new Util().getMK2Resource());
		sqlServerInfo_label.setHorizontalAlignment(SwingConstants.LEFT);
		sqlServerInfo_label.setForeground(Color.BLUE);
		sqlServerInfo_label.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		sqlServerInfo_label.setBackground(Color.WHITE);
		sqlServerInfo_label.setBounds(10, 0, 580, 48);
		infoPanel.add(sqlServerInfo_label);
		
		rcuInfo_Button = new JButton("RCU 정보");
		rcuInfo_Button.setForeground(Color.BLACK);
		rcuInfo_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		rcuInfo_Button.setFocusPainted(false);
		rcuInfo_Button.setBackground(new Color(152, 251, 152));
		rcuInfo_Button.setBounds(245, 62, 190, 37);
		rcuInfo_Button.setEnabled(false);
		infoPanel.add(rcuInfo_Button);
		
		perfInfo_Button = new JButton("성능 정보");
		perfInfo_Button.setForeground(Color.BLACK);
		perfInfo_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		perfInfo_Button.setFocusPainted(false);
		perfInfo_Button.setBackground(Color.ORANGE);
		perfInfo_Button.setBounds(440, 62, 150, 37);
		perfInfo_Button.setEnabled(false);
		perfInfo_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(selectedServer.isFacility()) {
					new WatchPointListFrame((Facility)selectedServer);
				}
			}
		});
		infoPanel.add(perfInfo_Button);
		
		JLabel searchFacility_Label = new JLabel("검 색");
		searchFacility_Label.setHorizontalAlignment(SwingConstants.CENTER);
		searchFacility_Label.setForeground(Color.BLACK);
		searchFacility_Label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchFacility_Label.setBackground(Color.WHITE);
		searchFacility_Label.setBounds(25, 146, 50, 64);
		infoPanel.add(searchFacility_Label);
		
		updateDB_Button = new JButton("Database 최신화");
		updateDB_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		updateDB_Button.setBackground(Color.WHITE);
		updateDB_Button.setForeground(Color.BLACK);
		updateDB_Button.setFocusPainted(false);		
		updateDB_Button.setBounds(245, 103, 190, 37);
		updateDB_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm(true, false);
			}
		});
		infoPanel.add(updateDB_Button);
		
		resetForm_button = new JButton("Form 초기화");
		resetForm_button.setForeground(Color.BLACK);
		resetForm_button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
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
		searchFacility_ComboBox1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_NAME, // 장비명
				SERVER_INDEX, // 장비 인덱스
				IP, // IP 주소
				SERVER_TYPE, // 시설물 종류
				CONN_METHOD, // 연결 방식
				PROTOCOL_NUMBER, // 프로토콜 번호								
				SERVER_STATE, // 장비 상태
				}));
		searchFacility_ComboBox1.setBounds(90, 145, 150, 30);
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
		searchFacility_ComboBox2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
				GROUP_INFO, // 그룹 정보
				SERVER_NAME, // 장비명
				SERVER_INDEX, // 장비 인덱스
				IP, // IP 주소
				SERVER_TYPE, // 시설물 종류
				CONN_METHOD, // 연결 방식
				PROTOCOL_NUMBER, // 프로토콜 번호								
				SERVER_STATE, // 장비 상태
				}));
		searchFacility_ComboBox2.setBounds(90, 181, 150, 30);
		searchFacility_ComboBox2.setSelectedIndex(1);
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
		searchFacility_textField1.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchFacility_textField1.setColumns(10);
		searchFacility_textField1.setBounds(245, 145, 345, 30);
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
		searchFacility_textField2.setBounds(245, 181, 345, 30);
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
		serverListTable.addFocusListener(new FocusListener() {			
			public void focusLost(FocusEvent e) {
				selectServer();
			}
			
			public void focusGained(FocusEvent e) {
				selectServer();
			}
		});
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
					showFacilityMenu(selectedServer);	
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					selectServer();
					showFacilityMenu(selectedServer);
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
				
				perfInfo_Button.setText("성능 정보");
				perfInfo_Button.setBackground(Color.ORANGE);
				perfInfo_Button.setEnabled(true);
				
				updateFacilityInfo((Facility)selectedServer); // <- 시설물 정보 테이블 업데이트
			}else {
				// RCU가 선택 되었을 경우				
				perfInfo_Button.setText("성능 정보 없음");
				perfInfo_Button.setBackground(Color.WHITE);
				perfInfo_Button.setEnabled(false);
								
				rcuInfo_Button.setEnabled(true);
				rcuInfo_Button.setBackground(new Color(135, 206, 250));
				rcuInfo_Button.setText("연결된 장비 목록");
				
				updateRCUInfo((RCU)selectedServer); // <- RCU 정보 테이블 업데이트
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadServerInfo(){
		if(!ONION_Info.hasMk119Connection() || ONION_Info.getMk119Connection() == null) return;
		
		try {
			serverList = new ArrayList<Server>();
			serverMap = new HashMap<Integer, Server>();
			
			Statement stmt = ONION_Info.getMk119Connection().createStatement();
		
			/* 시설물 초기화 */
			ResultSet rs = stmt.executeQuery(Facility.GET_FACILITY);
			while(rs.next()) {
				Facility fac = new Facility();
				
				fac.setGroupInfo(rs.getString("groupInfo"));				
				
				fac.setIp(rs.getString("ip"));
				fac.setPort(rs.getInt("port"));
				fac.setRtuIndex(rs.getInt("rtuIndex"));

				fac.setAgentType(rs.getInt("agentType"));
				
				fac.setIndex(rs.getInt("index"));
				fac.setName(rs.getString("name"));
				
				fac.setType(rs.getInt("facType"));
				fac.setTypeString(DbUtil.getFacilityType(fac.getType()));
				
				fac.setConnCode(rs.getInt("connMethod"));
				fac.setConnMethod(DbUtil.getConnMethod(fac.getConnCode()));
				
				fac.setCommProtocol(rs.getInt("commProtocol"));
				fac.setSnmpProtocol(rs.getInt("snmpProtocol"));
				fac.setCommon((fac.getCommProtocol() > fac.getSnmpProtocol()) ? true : false);
				
				fac.setStateCode(rs.getInt("condition"));
				fac.setState(DbUtil.getState(fac.getStateCode()));

				serverList.add(fac);
				serverMap.put(fac.getIndex(), fac);
			}
			
			/* RCU 초기화 */
			rs = stmt.executeQuery(RCU.GET_RTU);
			while(rs.next()) {
				RCU rcu = new RCU();
				
				rcu.setGroupInfo(rs.getString("groupInfo"));
				
				rcu.setIp(rs.getString("ip"));
				rcu.setPort(rs.getInt("port"));				

				rcu.setAgentType(rs.getInt("agentType"));
				
				rcu.setIndex(rs.getInt("index"));
				rcu.setName(rs.getString("name"));
				
				rcu.setType(rs.getInt("rtuType"));
				rcu.setTypeString("RCU");
				rcu.setRcuTypeDetail(DbUtil.getRcuType(rcu.getType()));
				
				rcu.setStateCode(rs.getInt("condition"));
				rcu.setState(DbUtil.getState(rcu.getStateCode()));

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
							e.printStackTrace();
							
							RCU rcu = new RCU();
							rcu.setIndex(rtuIndex);							
							rcu.setName("알 수 없음");
							rcu.setTypeString("알 수 없음");
							rcu.setRcuTypeDetail("알 수 없음");
							rcu.setIp("알 수 없음");
							rcu.setState("알 수 없음");
							fac.setRcu(rcu);
							
							if(isFirstLoad) {
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%s%s%s\n", Util.colorRed("Can Not Found RCU"), Util.separator, Util.separator));
								
								sb.append(Util.colorRed("알 수 없는 RCU 인덱스 : ") + rtuIndex + Util.separator + Util.separator + "\n\n");
								
								sb.append(Util.colorBlue("그룹 정보 : ") + server.getGroupInfo() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("장비 인덱스 : ") + server.getIndex() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("시설물 종류 : ") + server.getTypeString() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("연결 방식 : ") + ((Facility)server).getConnMethod() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("장비명 : ") + server.getName() + Util.separator + Util.separator + "\n\n");							
								
								sb.append("위의 장비가 바라보는 " + Util.colorRed("RCU") + " 장비를 찾을 수 없습니다" + Util.separator + Util.separator +"\n");
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
							}
						}
				}
			}
			
			Collections.sort(serverList);
			
			isFirstLoad = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void updateServerListTable(boolean databaseLoad) {		
		if(databaseLoad) loadServerInfo();
		if(serverList == null) return;
		
		Object[][] content = new Object[serverList.size()][];

		for (int i = 0; i < serverList.size(); i++) {
			Server fac = serverList.get(i);
			content[i] = new Object[5];
			content[i][0] = i + 1;
			content[i][1] = fac.getGroupInfo();
			content[i][2] = fac.getTypeString();
			content[i][3] = fac;
			content[i][4] = fac.getState();
		}

		serverListTable.setModel(new DefaultTableModel(
			content, 			
			new String[] { ORDER, GROUP_INFO, SERVER_TYPE, SERVER_NAME, SERVER_STATE }) {
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
		FindTextRenderer findCommerRenderer = new FindTextRenderer(4, STATE_COMMER, Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		FindTextRenderer findRCURenderer = new FindTextRenderer(2, "RCU", new Color(152, 251, 152));
		findRCURenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 그룹 정보
		tcmSchedule.getColumn(2).setCellRenderer(findRCURenderer); // 시설물 종류
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 장비명
		tcmSchedule.getColumn(4).setCellRenderer(findCommerRenderer); // 상 태		
	}
		
	public static void updateFacilityInfo(Facility fac) {
		if(fac == null) {	
			perfInfo_Button.setEnabled(false);
			perfInfo_Button.setBackground(Color.WHITE);			
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
		
		Object[][] content = new Object[7][];
		
		// 장비명
		content[0] = new Object[2];
		content[0][0] = SERVER_NAME;
		content[0][1] = fac;
		
		// 장비 인덱스
		content[1] = new Object[2];
		content[1][0] = SERVER_INDEX;
		content[1][1] = fac.getIndex();
		
		// IP 주소
		content[2] = new Object[2];
		content[2][0] = IP;
		content[2][1] = fac.isConnRCU() ? "( RCU IP ) " + fac.getIp() : fac.getIp();
		
		// 시설물 종류
		content[3] = new Object[2];
		content[3][0] = FACILITY_TYPE;
		content[3][1] = fac.getTypeString();
		
		// 연결 방식
		content[4] = new Object[2];
		content[4][0] = CONN_METHOD;
		content[4][1] = fac.getConnMethod();
		
		// 프로토콜 번호
		content[5] = new Object[2];
		content[5][0] = PROTOCOL_NUMBER;
		content[5][1] = fac.isCommon() ? fac.getCommProtocol() : fac.getSnmpProtocol();		
		
		// 장비 상태
		content[6] = new Object[2];
		content[6][0] = SERVER_STATE;
		content[6][1] = fac.getState();

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
			perfInfo_Button.setEnabled(false);
			perfInfo_Button.setBackground(Color.WHITE);			
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
		
		Object[][] content = new Object[6][];
		
		// RCU 이름
		content[0] = new Object[2];
		content[0][0] = "RCU 이름";
		content[0][1] = rcu;
		
		// RCU 인덱스
		content[1] = new Object[2];
		content[1][0] = "RCU 인덱스";
		content[1][1] = rcu.getIndex();
		
		// IP 주소
		content[2] = new Object[2];
		content[2][0] = IP;
		content[2][1] = rcu.getIp();
		
		// RCU 종류
		content[3] = new Object[2];
		content[3][0] = RCU_TYPE;
		content[3][1] = rcu.getRcuTypeDetail();
		
		// 연결된 장비 개수
		content[4] = new Object[2];
		content[4][0] = "연결된 장비 개수";
		content[4][1] = rcu.getFacList().size();
		
		// RCU 상태
		content[5] = new Object[2];
		content[5][0] = "RCU 상태";
		content[5][1] = rcu.getState();

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
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowMargin(3);
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(3); // 필 드
		table.getColumnModel().getColumn(1).setPreferredWidth(120); // 내 용		
		
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
		
	public static void showFacilityMenu(Server server) {
		if(server == null) return;

		int menu = -1;
		String separator = Util.separator + Util.separator;
		
		if(server.isFacility()) {
			Facility fac = (Facility)server;
			
			// 시설물
			StringBuilder msg = new StringBuilder();
						
			msg.append(String.format("%s%s%s\n", Util.colorBlue("──────────[ 시설물 정보 ]──────────"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("장비명"), fac.getName(), separator, separator));			
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("IP 주소"), fac.isConnRCU() ? "( RCU IP ) " + fac.getIp() : fac.getIp(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("시설물 종류"), fac.getTypeString(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("연결 방식"), fac.getConnMethod(), separator, separator));
			
			if(fac.isConnRCU() && fac.getRcu() != null) {
				msg.append(String.format("\n%s%s%s\n", Util.colorGreen("──────────[ RCU 정보 ]──────────"), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 이름"), fac.getRcu().getName(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU IP 주소"), fac.getRcu().getIp(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 종류"), fac.getRcu().getRcuTypeDetail(), separator, separator));
				msg.append(String.format("%s : %d개%s%s\n", Util.colorGreen("연결된 장비 개수"), fac.getRcu().getFacList().size(), separator, separator));
			}
			
			if(fac.isConnRCU() && fac.getRcu() != null) {
				menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "RCU 정보 보기", "취 소"}, JOptionPane.INFORMATION_MESSAGE, false);
				switch (menu) {				
					case 0: // 성능 정보 보기
						new WatchPointListFrame(fac);
						return;
						
					case 1: // 연결된 RCU 정보 보기
						// RCU 정보 표시
						new RcuInfoFrame(fac.getRcu());
						break;
						
					default :
						return;
				}
			}else {
				menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "취 소"}, JOptionPane.INFORMATION_MESSAGE, false);
				switch (menu) {					
					case 0: // 성능 정보 보기
						new WatchPointListFrame(fac);
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
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 이름"), rcu.getName(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU IP 주소"), rcu.getIp(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 종류"), rcu.getRcuTypeDetail(), separator, separator));
			msg.append(String.format("%s : %d개%s%s\n", Util.colorGreen("연결된 장비 개수"), rcu.getFacList().size(), separator, separator));
			
			menu = Util.showOption(msg.toString(), new String[] { "연결된 장비 목록 보기", "취 소"}, JOptionPane.INFORMATION_MESSAGE, false);		
			switch (menu) {				
				case 0: // 연결된 장비 목록 보기
					new RcuInfoFrame(rcu);
					return;									
				default :
					return;
			}
		}
	}
	
	public static void resetForm(boolean databaseLoad, boolean allComponentReset) {		
		updateServerListTable(databaseLoad);
		
		rcuInfo_Button.setText("RCU 정보");
		perfInfo_Button.setText("성능 정보");
		
		updateFacilityInfo(null);
		
		if(allComponentReset) {
			if(searchFacility_textField1 != null) searchFacility_textField1.setText(null);
			if(searchFacility_textField2 != null) searchFacility_textField2.setText(null);
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedIndex(0);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedIndex(1);	
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
					searchElement_1 = server.getGroupInfo();
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
						searchElement_1 = String.valueOf((((Facility)server).isCommon()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_1 = "";
					}
					break;
			}// switch - searchElement_1
			
			switch(searchFacility_ComboBox2.getSelectedItem().toString()) {
				case IP :  // IP 주소
					searchElement_2 = server.getIp();
					break;
				case GROUP_INFO :  // 그룹 정보
					searchElement_2 = server.getGroupInfo();
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
						searchElement_2 = String.valueOf((((Facility)server).isCommon()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_2 = "";
					}
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
			content[i] = new Object[5];
			content[i][0] = i + 1;
			content[i][1] = server.getGroupInfo();
			content[i][2] = server.getTypeString();
			content[i][3] = server;
			content[i][4] = server.getState();
		}

		serverListTable.setModel(new DefaultTableModel(
			content, 			
			new String[] { ORDER, GROUP_INFO, SERVER_TYPE, SERVER_NAME, SERVER_STATE }) {
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
	
	public void setFocusCell(JTable table, int row, int column) {
		table.changeSelection(row, column, false, false);				
		table.requestFocus();
	}
}

