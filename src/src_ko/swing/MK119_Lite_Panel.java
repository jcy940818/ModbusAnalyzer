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
	public static final String ORDER = "�� ��";
	public static final String GROUP_INFO = "�׷� ����";
	public static final String SERVER_INDEX = "��� �ε���";
	public static final String SERVER_NAME = "����";
	public static final String SERVER_TYPE = "��� ����";
	public static final String IP = "IP �ּ�";
	public static final String FACILITY_TYPE = "�ü��� ����";
	public static final String RCU_TYPE = "RCU ����";
	public static final String CONN_METHOD = "���� ���";
	public static final String EVENT = "�̺�Ʈ";
	public static final String SERVER_STATE = "��� ����";	
	public static final String PROTOCOL_NUMBER = "�������� ��ȣ";
	public static final String PROTOCOL_NAME = "�������� �̸�";
	
	public static final String STATE_COMMER = "��� ����";
	
	public static final String OVERLAPPING = "   ( �ߺ� ��� ���� )";
	
	public static JLabel sqlServerInfo_label;
	
	private JPanel infoPanel;
	
	// ******** [ MK119 ���� ������ ] *********************************************************
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
		
		eventInfo_Button = new JButton("�̺�Ʈ ����");
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
		
		rcuInfo_Button = new JButton("RCU ����");
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
		
		facilityInfo_Button = new JButton("�ü��� ����");
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
		
		linkMK119_Button = new JButton(" ����");
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
		
		
		JLabel searchFacility_Label = new JLabel("�� ��");
		searchFacility_Label.setHorizontalAlignment(SwingConstants.CENTER);
		searchFacility_Label.setForeground(Color.BLACK);
		searchFacility_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		searchFacility_Label.setBackground(Color.WHITE);
		searchFacility_Label.setBounds(22, 146, 50, 64);
		infoPanel.add(searchFacility_Label);
		
		updateDB_Button = new JButton("Database �ֽ�ȭ");
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
		
		resetForm_button = new JButton("Form �ʱ�ȭ");
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
				GROUP_INFO, // �׷� ����
				SERVER_TYPE, // �ü��� ����
				SERVER_NAME, // ����
				SERVER_INDEX, // ��� �ε���
				IP, // IP �ּ�
				CONN_METHOD, // ���� ���
				PROTOCOL_NUMBER, // �������� ��ȣ
				SERVER_STATE, // ��� ����
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
				GROUP_INFO, // �׷� ����
				SERVER_TYPE, // �ü��� ����
				SERVER_NAME, // ����
				SERVER_INDEX, // ��� �ε���
				IP, // IP �ּ�
				CONN_METHOD, // ���� ���
				PROTOCOL_NUMBER, // �������� ��ȣ
				SERVER_STATE, // ��� ����
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
				} // ���� Ŭ��
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// ���� ��ư ���� Ŭ��
					selectServer();
					showFacilityMenu(selectedServer, true);	
				}
				if (e.getButton() == 3) {
					// ������ Ŭ��
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
				// �ü����� ���� �Ǿ��� ���
				Facility fac = (Facility)selectedServer;
				if(fac.isConnRCU()) {
					rcuInfo_Button.setEnabled(true);
					rcuInfo_Button.setBackground(new Color(152, 251, 152));
					rcuInfo_Button.setText("����� RCU ����");
				}else {
					rcuInfo_Button.setEnabled(false);
					rcuInfo_Button.setBackground(Color.WHITE);
					rcuInfo_Button.setText("����� RCU ����");
				}
				
				facilityInfo_Button.setText("�ü��� ����");
				facilityInfo_Button.setBackground(Color.ORANGE);
				facilityInfo_Button.setEnabled(true);
				
				updateFacilityInfo((Facility)selectedServer); // <- �ü��� ���� ���̺� ������Ʈ
			}else {
				// RCU�� ���� �Ǿ��� ���
				facilityInfo_Button.setText("�ü��� ����");
				facilityInfo_Button.setBackground(Color.WHITE);
				facilityInfo_Button.setEnabled(false);
								
				rcuInfo_Button.setEnabled(true);
				rcuInfo_Button.setBackground(new Color(152, 251, 152));
				rcuInfo_Button.setText("����� ��� ���");
				
				updateRCUInfo((RCU)selectedServer); // <- RCU ���� ���̺� ������Ʈ
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
			
			/* �ü��� �ʱ�ȭ */
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

				// BACnet ���� ��Ʈ : 47808 (0xBAC0) 
				if(fac.getConnCode() == 128) {
					fac.setPort(0xBAC0);
				}
				
				// �׷� ������ �����ϰ� �ε����� ������ ��� ������ �ߺ��Ǵ� �ü����� �����Ѵ�
				if(serverMap.containsKey(fac.getIndex())) {
					Facility originFac = (Facility)serverMap.get(fac.getIndex());
					originFac.setOverlapping(true); // �ش� ���� �ߺ��� �����ϴ� ����̴�
					continue;
				}
				
				serverList.add(fac);
				serverMap.put(fac.getIndex(), fac);
			}
			
			/* RCU �ʱ�ȭ */
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

					case RCU.RTU_TYPE_DUPLEXED_TCP : // TCP/IP_����ȭ_RCU
						rcu.setDuplexedPort(true);
						rcu.setAuxIP(rs.getString("auxIP"));
						rcu.setAuxPort(rs.getInt("auxPort"));
						break;
						
					case RCU.RTU_TYPE_MQTT_BROKER : // MQTT_Broker
						rcu.setPort(rs.getInt("mqttPort"));
						break;
				}
				
				// �׷� ������ �����ϰ� �ε����� ������ ��� ������ �ߺ��Ǵ� ��� �����Ѵ�
				if(serverMap.containsKey(rcu.getIndex())) {
					RCU originRCU = (RCU)serverMap.get(rcu.getIndex());
					originRCU.setOverlapping(true); // �ش� RCU�� �ߺ��� �����ϴ� RCU�̴�
					continue;
				}
				
				serverList.add(rcu);
				serverMap.put(rcu.getIndex(), rcu);
			}
			
		
			/* ��Ƽ ��Ʈ RCU ��Ʈ ä�� ���� ���� �ʱ�ȭ */
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
			
			// Server List ����
			Collections.sort(serverList);
			
			/* RCU & �ü��� ���� */
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
								fac.setIp(rcu.getIp()); // �ü����� IP�� RCU�� ��ϵ� IP�� ����
								
								// �ü����� RCU ��Ʈ ä��, ��Ʈ ��ȣ ����
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
							rcu.setName("�� �� ����");
							rcu.setTypeString("�� �� ����");
							rcu.setRcuTypeDetail("�� �� ����");
							rcu.setIp("�� �� ����");
							rcu.setState("�� �� ����");
							fac.setRcu(rcu);
							
							String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
							if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
								groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
							}else {
								groupInfo = groupInfo.replace(ServerGroup.ROOT, "��� ����  ( �׷� ���� )");
							}
							
							if(isFirstLoad) {
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%s%s%s\n", Util.colorRed("Can Not Found RCU"), Util.separator, Util.separator));
								
								sb.append(Util.colorRed("�� �� ���� RCU �ε��� : ") + rtuIndex + Util.separator + Util.separator + "\n\n");
								
								sb.append(Util.colorBlue("�׷� ���� : ") + groupInfo + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("�ü��� ���� : ") + server.getTypeString() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("���� : ") + server.getName() + Util.separator + Util.separator + "\n");							
								sb.append(Util.colorBlue("��� �ε��� : ") + server.getIndex() + Util.separator + Util.separator + "\n");
								sb.append(Util.colorBlue("���� ��� : ") + ((Facility)server).getConnMethod() + Util.separator + Util.separator + "\n\n");
								
								sb.append("���� ��� �ٶ󺸴� " + Util.colorRed("RCU") + " ��� ã�� �� �����ϴ�" + Util.separator + Util.separator +"\n\n");
								sb.append("�ش� ������ " + Util.colorRed("RCU") + " ���� " + Util.colorBlue("�ü���") + "�� ����� ���¿��� " + Util.colorRed("RCU") + " ��� ���� �Ǿ��� ��� �߻� �� �� �ֽ��ϴ�" + Util.separator + Util.separator +"\n");
								
								int menu = Util.showOption(sb.toString(), new String[] { "Ȯ ��", " ������ ������ �޽��� �׸����� "}, JOptionPane.ERROR_MESSAGE);
								switch (menu) {
									case 1: // ������ ������ �޽��� �׸�����
										isFirstLoad = false;
										continue;
									case 0: // Ȯ ��	
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
			sb.append("�����ͺ��̽� �ε忡 �����Ͽ����ϴ�" +  Util.separator + Util.separator + "\n\n");
			sb.append("�����ͺ��̽� Ŀ�ؼ��� �������� ��� �����ͺ��̽� ���������� �ش� ������ �ذ� �� �� �ֽ��ϴ�" +  Util.separator + Util.separator + "\n\n");
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
				groupInfo = groupInfo.replace(ServerGroup.ROOT, "��� ����  ( �׷� ���� )");
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
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});
		
		setTableStyle(serverListTable);
	}
	
	public static void setTableStyle(JTable table) {
		
		// ���̺� ��� ����
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// ���̺� �� ũ�� ����
		table.getColumnModel().getColumn(0).setPreferredWidth(10); // �� ��
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // �׷� ����
		table.getColumnModel().getColumn(2).setPreferredWidth(60); // �ü��� ����
		table.getColumnModel().getColumn(3).setPreferredWidth(150); // ����
		table.getColumnModel().getColumn(4).setPreferredWidth(50); // ��� ����
		table.getColumnModel().getColumn(5).setPreferredWidth(50); //  �̺�Ʈ
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
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
		
		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �� ��
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �׷� ����
		tcmSchedule.getColumn(2).setCellRenderer(findRCURenderer); // �ü��� ����
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(4).setCellRenderer(findCommerRenderer); // ��� ����
		tcmSchedule.getColumn(5).setCellRenderer(severityRenderer); // �̺�Ʈ
	}
		
	public static void updateFacilityInfo(Facility fac) {
		if(fac == null) {
			eventInfo_Button.setText("�̺�Ʈ ����");
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
					new String[] { "�� ��", "�� ��" }) {
					// ���̺� �� ���� ���� ����
					public boolean isCellEditable(int i, int c) {
						return false;
					}
			});
			setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
			return;		
		}
		
		int index = 0;
		Object[][] content = new Object[7][];
		
		// �ü��� ����
		content[index] = new Object[2];
		content[index][0] = FACILITY_TYPE;
		content[index++][1] = fac.getTypeString();
		
		// ����
		content[index] = new Object[2];
		content[index][0] = SERVER_NAME;
		content[index++][1] = fac;
		
		// ��� �ε���
		content[index] = new Object[2];
		content[index][0] = SERVER_INDEX;
		content[index++][1] = fac.getIndex();
		
		// IP �ּ�
		content[index] = new Object[2];
		content[index][0] = "���� ����";
		String connInfo = fac.getIp() + " : " + fac.getPort();
		content[index++][1] = fac.isConnRCU() ? "( RCU ) " + connInfo : connInfo ;
				
		// ���� ���
		content[index] = new Object[2];
		content[index][0] = CONN_METHOD;
		content[index++][1] = fac.getConnMethod();
		
		// �������� ����
		if(linkMK119_Protocol) {
			content[index] = new Object[2];
			content[index][0] = PROTOCOL_NAME;
			Protocol p = protocolMap.get(fac.getProtocolKey());
			if(p != null) {
				content[index++][1] = p.getName();
			}else {
				content[index++][1] = "Unknown ( �� �� ���� )";
			}
		}else {
			content[index] = new Object[2];
			content[index][0] = PROTOCOL_NUMBER;
			content[index++][1] = fac.isProtocol() ? fac.getCommProtocol() : fac.getSnmpProtocol();
		}
		
		// ��� ����
		content[index] = new Object[2];
		content[index][0] = SERVER_STATE;
		content[index++][1] = fac.getState();

		serverInfoTable.setModel(new DefaultTableModel(
			content,
			new String[] { "�� ��", "�� ��" }) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
	}
	
	public static void updateRCUInfo(RCU rcu) {
		if(rcu == null) {
			eventInfo_Button.setText("�̺�Ʈ ����");
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
					new String[] { "�� ��", "�� ��" }) {
					// ���̺� �� ���� ���� ����
					public boolean isCellEditable(int i, int c) {
						return false;
					}
			});
			setServerInfoTableStyle(serverInfoTable, Color.ORANGE);
			return;		
		}
		
		int index = 0;
		Object[][] content = new Object[7][];
		
		// RCU ����
		content[index] = new Object[2];
		content[index][0] = RCU_TYPE;
		content[index++][1] = rcu.getRcuTypeDetail();
		
		// RCU �̸�
		content[index] = new Object[2];
		content[index][0] = "RCU �̸�";
		content[index++][1] = rcu;
		
		// RCU �ε���
		content[index] = new Object[2];
		content[index][0] = "RCU �ε���";
		content[index++][1] = rcu.getIndex();
		
		// IP ����
		String ipInfo = "";
		if(rcu.isDuplexedPort()) {
			ipInfo += rcu.getIp();
			ipInfo += " & ";
			ipInfo += rcu.getAuxIP();
		}else {
			ipInfo += rcu.getIp();
		}
		
		content[index] = new Object[2];
		content[index][0] = "IP ����";
		content[index++][1] = ipInfo;
		
		// Port ����
		
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
		content[index][0] = "Port ����";
		content[index++][1] = portInfo;
		
		// ����� ��� ����
		content[index] = new Object[2];
		content[index][0] = "����� ��� ����";
		content[index++][1] = rcu.getFacList().size();
		
		// RCU ����
		content[index] = new Object[2];
		content[index][0] = "RCU ����";
		content[index++][1] = rcu.getState();

		serverInfoTable.setModel(new DefaultTableModel(
			content,
			new String[] { "�� ��", "�� ��" }) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setServerInfoTableStyle(serverInfoTable, new Color(152, 251, 152));
	}
	
	public static void setServerInfoTableStyle(JTable table, Color headerColor) {
		// ���̺� ��� ����
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(headerColor);
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowMargin(3);
		table.setRowHeight(25);
		
		// ���̺� �� ũ�� ����
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // �� ��
		table.getColumnModel().getColumn(1).setPreferredWidth(190); // �� ��
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		FindTextRenderer findCommerRenderer = new FindTextRenderer(1, STATE_COMMER, Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �� ��
		tcmSchedule.getColumn(1).setCellRenderer(findCommerRenderer); // �� ��
	}
		
	public static void showFacilityMenu(Server server, boolean showRcuInfo) {
		if(server == null) return;

		int menu = -1;
		String separator = Util.separator + Util.separator;
		
		if(server.isFacility()) {
			Facility fac = (Facility)server;
			
			// �ü���
			StringBuilder msg = new StringBuilder();
						
			msg.append(String.format("%s%s%s\n", Util.colorBlue("��������������������[ �ü��� ���� ]��������������������"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("�ü��� ����"), fac.getTypeString(), separator, separator));
			
			if(linkMK119_Protocol) {
				Protocol p = protocolMap.get(fac.getProtocolKey());
				String pName = null;
				if(p != null) {
					pName = p.getName();
				}else {
					pName = "Unknown ( �� �� ���� )";
				}
				msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("��������"), pName, separator, separator));
			}
			
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("����"), fac.getName(), separator, separator));
			msg.append(String.format("%s : %d%s%s\n", Util.colorBlue("��� �ε���"), fac.getIndex(), separator, separator));
			
			String connInfo = "";
			connInfo += Util.colorRed("IP") + " : " + fac.getIp();
			
			// TCP/IP ����ȭ RCU
			if(fac.isConnRCU() && fac.getRcu() != null) {
				if(fac.getRcu().isDuplexedPort()) {
					connInfo += Util.colorBlue(" & ") + fac.getRcu().getAuxIP();
				}
			}
			
			connInfo += "&nbsp;&nbsp;" + Util.colorBlue("/") + "&nbsp;&nbsp;";
			connInfo += Util.colorRed("Port") + " : "+ fac.getPort();
			if(fac.isConnRCU()) {
				connInfo = Util.colorRed("( RCU ) ") + connInfo;
				
				// TCP/IP ����ȭ RCU
				if(fac.getRcu() != null) {
					if(fac.getRcu().isDuplexedPort()) {
						connInfo += Util.colorBlue(" & ") + fac.getRcu().getAuxPort();
					}
				}
			}
			
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("���� ����"), connInfo, separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("���� ���"), fac.getConnMethod(), separator, separator));
			
			if(fac.hasEvent()) {
				Event e = fac.getEvents().get(0);
				String status = (e.getStatus() == 0) ? Util.colorRed("�̺�Ʈ �߻� ����") : Util.colorRed("�̺�Ʈ ���� ����");
				String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
				msg.append(String.format("%s : %s %s%s%s\n", Util.colorBlue("�̺�Ʈ ����"), eventInfo, status,separator, separator));
			}
			
			if(fac.getRcu() != null) {
				
				RCU rcu = fac.getRcu();
				String unknown = "�� �� ����";
				boolean unknownRCU = false;
				
				if(rcu.getName().equals(unknown) 
						&& rcu.getTypeString().equals(unknown) 
						&& rcu.getRcuTypeDetail().equals(unknown) 
						&& rcu.getIp().equals(unknown) 
						&& rcu.getState().equals(unknown)) {
					unknownRCU = true;
				}
				
				if(unknownRCU) {
					msg.append(String.format("\n%s%s%s\n", Util.colorRed("��������������������[ �� �� ���� RCU ���� ]��������������������"), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU ����"), rcu.getRcuTypeDetail(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU �̸�"), rcu.getName(), separator, separator));
					msg.append(String.format("%s : %d%s%s\n", Util.colorRed("RCU �ε���"), rcu.getIndex(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU IP"), rcu.getIp(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU Port"), unknown, separator, separator));
					msg.append(String.format("%s : %d��%s%s\n\n", Util.colorRed("����� ��� ����"), rcu.getFacList().size(), separator, separator));
					msg.append(String.format("%s%s%s\n", "�ش� " + Util.colorBlue("�ü���") + "�� " + Util.colorRed("RCU") + " ���� " + Util.colorBlue("�ü���") + "�� ����� ���¿��� " + Util.colorRed("RCU") + " ��� ���� �Ǿ��� �� �ֽ��ϴ�", separator, separator));
				}else {
					msg.append(String.format("\n%s%s%s\n", Util.colorGreen("��������������������[ RCU ���� ]��������������������"), separator, separator));		
					msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU ����"), rcu.getRcuTypeDetail(), separator, separator));
					msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU �̸�"), rcu.getName(), separator, separator));
					msg.append(String.format("%s : %d%s%s\n", Util.colorGreen("RCU �ε���"), rcu.getIndex(), separator, separator));
					
					// ****** [ RCU ���� ���� ] ********************************************************************************************
					
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
										
					msg.append(String.format("%s : %d��%s%s\n", Util.colorGreen("����� ��� ����"), rcu.getFacList().size(), separator, separator));
					
					if(rcu.hasEvent()) {
						Event e = rcu.getEvents().get(0);
						String status = (e.getStatus() == 0) ? Util.colorRed("�̺�Ʈ �߻� ����") : Util.colorRed("�̺�Ʈ ���� ����");
						String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
						msg.append(String.format("%s : %s %s%s%s\n", Util.colorGreen("�̺�Ʈ ����"), eventInfo, status,separator, separator));
					}
					
				}
			}
			
			if(fac.isConnRCU() && fac.getRcu() != null && showRcuInfo) {
				
				if(fac.hasEvent()) {
					menu = Util.showOption(msg.toString(), new String[] { "���� ���� ����", "RCU ���� ����",  "�̺�Ʈ ���� ����", " �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);
				}else {
					menu = Util.showOption(msg.toString(), new String[] { "���� ���� ����", "RCU ���� ����",  " �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);	
				}
				
				switch (menu) {
					case 0:// ���� ���� ����
						new FacilityInfoFrame(fac);
						return;
						
					case 1:
						// RCU ���� ����
						new RcuInfoFrame(fac.getRcu());
						return;
						
					case 2:
						// �̺�Ʈ ���� ����
						if(fac.hasEvent()) {
							Event.showDetailEventInfo(fac);
						}
						return;
						
					default :
						return;
				}
			}else {
				
				if(fac.hasEvent()) {
					menu = Util.showOption(msg.toString(), new String[] { "���� ���� ����", "�̺�Ʈ ���� ����", " �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);
				}else {
					menu = Util.showOption(msg.toString(), new String[] { "���� ���� ����", " �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);	
				}
				
				switch (menu) {
					case 0: // ���� ���� ����
						new FacilityInfoFrame(fac);
						return;			
						
					case 1:
						// �̺�Ʈ ���� ����
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
			msg.append(String.format("%s%s%s\n", Util.colorGreen("��������������������[ RCU ���� ]��������������������"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU ����"), rcu.getRcuTypeDetail(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU �̸�"), rcu.getName(), separator, separator));
			msg.append(String.format("%s : %d%s%s\n", Util.colorGreen("RCU �ε���"), rcu.getIndex(), separator, separator));
			// ****** [ RCU ���� ���� ] ********************************************************************************************
			
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
			msg.append(String.format("%s : %d��%s%s\n", Util.colorGreen("����� ��� ����"), rcu.getFacList().size(), separator, separator));
			
			if(rcu.hasEvent()) {
				Event e = rcu.getEvents().get(0);
				String status = (e.getStatus() == 0) ? Util.colorRed("�̺�Ʈ �߻� ����") : Util.colorRed("�̺�Ʈ ���� ����");
				String eventInfo = TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor());
				msg.append(String.format("%s : %s %s%s%s\n", Util.colorGreen("�̺�Ʈ ����"), eventInfo, status,separator, separator));
			}
			
			if(rcu.hasEvent()) {
				menu = Util.showOption(msg.toString(), new String[] { "����� ��� ��� ����", "�̺�Ʈ ���� ����"," �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);
			}else {
				menu = Util.showOption(msg.toString(), new String[] { "����� ��� ��� ����", " �� �� "}, JOptionPane.INFORMATION_MESSAGE, false);	
			}
			
			switch (menu) {
				case 0: // ����� ��� ��� ����
					new RcuInfoFrame(rcu);
					return;
					
				case 1 : // �̺�Ʈ ���� ����
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
				case IP :  // IP �ּ�
					searchElement_1 = server.getIp();
					break;
				case GROUP_INFO :  // �׷� ����
					String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
					if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
						groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
					}else {
						groupInfo = groupInfo.replace(ServerGroup.ROOT, "��� ����  ( �׷� ���� )");
					}
					searchElement_1 = groupInfo;
					break;
				case SERVER_INDEX : // ��� �ε���
					searchElement_1 = String.valueOf(server.getIndex());
					break;
				case SERVER_NAME : // ����
					searchElement_1 = server.getName();
					break;
				case SERVER_TYPE : // �ü��� ����
					searchElement_1 = server.getTypeString();
					break;
				case CONN_METHOD : // ���� ���
					if(server.isFacility()) {
						searchElement_1 = ((Facility)server).getConnMethod();
					}else {
						searchElement_1 = "";
					}
					break;
				case SERVER_STATE : // ��� ����
					searchElement_1 = server.getState();
					break;
				case PROTOCOL_NUMBER : // �������� ��ȣ
					if(server.isFacility()) {						
						searchElement_1 = String.valueOf((((Facility)server).isProtocol()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_1 = "";
					}					
					break;
				case PROTOCOL_NAME : // �������� �̸�
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
				case IP :  // IP �ּ�
					searchElement_2 = server.getIp();
					break;
				case GROUP_INFO :  // �׷� ����
					String groupInfo = (server.isOverlapping()) ? server.getGroup().getTree() + OVERLAPPING : server.getGroup().getTree();
					if(groupInfo.startsWith(ServerGroup.ROOT + " > ")) {
						groupInfo = groupInfo.replace(ServerGroup.ROOT + " > ", "");
					}else {
						groupInfo = groupInfo.replace(ServerGroup.ROOT, "��� ����  ( �׷� ���� )");
					}
					searchElement_2 = groupInfo;
					break;
				case SERVER_INDEX : // ��� �ε���
					searchElement_2 = String.valueOf(server.getIndex());
					break;
				case SERVER_NAME : // ����
					searchElement_2 = server.getName();
					break;
				case SERVER_TYPE : // �ü��� ����
					searchElement_2 = server.getTypeString();
					break;
				case CONN_METHOD : // ���� ���
					if(server.isFacility()) {
						searchElement_2 = ((Facility)server).getConnMethod();	
					}else {
						searchElement_2 = "";
					}
					break;
				case SERVER_STATE : // ��� ����
					searchElement_2 = server.getState();
					break;
				case PROTOCOL_NUMBER : // �������� ��ȣ
					if(server.isFacility()) {						
						searchElement_2 = String.valueOf((((Facility)server).isProtocol()?((Facility)server).getCommProtocol():((Facility)server).getSnmpProtocol()));	
					}else {
						searchElement_2 = "";
					}
					break;
				case PROTOCOL_NAME : // �������� �̸�
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
				groupInfo = groupInfo.replace(ServerGroup.ROOT, "��� ����  ( �׷� ���� )");
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
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(serverListTable);
	}
	
	
	public static void printRcuInformation() {
		// ��Ƽ ��Ʈ RCU Ȯ�ο�
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
			
			String status = (event.getStatus() == 0) ? "�߻�" : "����";
			status = String.format("%s ( %s )", status, event.getSeverityName());
			eventButton.setText(status);
		}else {
			eventButton.setEnabled(false);
			eventButton.setBackground(Color.WHITE);
			eventButton.setForeground(Color.BLACK);
			eventButton.setText("�̺�Ʈ ����");
		}
	}
	
	public static void updateItem_searchComboBox(boolean isConnectProtocol) {
		if(isConnectProtocol) {
			String item_1 = GROUP_INFO;
			String item_2 = SERVER_NAME;
			if(searchFacility_ComboBox1 != null) item_1 = searchFacility_ComboBox1.getSelectedItem().toString();
			if(searchFacility_ComboBox2 != null) item_2 = searchFacility_ComboBox2.getSelectedItem().toString();
			
			searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // �׷� ����
					SERVER_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					IP, // IP �ּ�
					CONN_METHOD, // ���� ���
					PROTOCOL_NUMBER, // �������� ��ȣ
					PROTOCOL_NAME, // �������� �̸�
					SERVER_STATE, // ��� ����
					EVENT
					}));
			searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // �׷� ����
					SERVER_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					IP, // IP �ּ�
					CONN_METHOD, // ���� ���
					PROTOCOL_NUMBER, // �������� ��ȣ
					PROTOCOL_NAME, // �������� �̸�
					SERVER_STATE, // ��� ����
					EVENT
					}));
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedItem(item_1);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedItem(item_2);
		}else {
			searchFacility_ComboBox1.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // �׷� ����
					SERVER_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					IP, // IP �ּ�
					CONN_METHOD, // ���� ���
					PROTOCOL_NUMBER, // �������� ��ȣ
					SERVER_STATE, // ��� ����
					EVENT
					}));
			searchFacility_ComboBox2.setModel(new DefaultComboBoxModel(new String[] {
					GROUP_INFO, // �׷� ����
					SERVER_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					IP, // IP �ּ�
					CONN_METHOD, // ���� ���
					PROTOCOL_NUMBER, // �������� ��ȣ
					SERVER_STATE, // ��� ����
					EVENT
					}));
			if(searchFacility_ComboBox1 != null) searchFacility_ComboBox1.setSelectedItem(GROUP_INFO);
			if(searchFacility_ComboBox2 != null) searchFacility_ComboBox2.setSelectedItem(SERVER_NAME);
		}
	}
	
	
}

