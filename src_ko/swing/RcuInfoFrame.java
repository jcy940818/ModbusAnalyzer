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
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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

import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.server.Facility;
import common.server.MultiPortMap;
import common.server.RCU;
import common.server.Server;
import common.util.FindTextRenderer;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class RcuInfoFrame extends JFrame {;
	
	public static final String ORDER = "순 서";	
	public static final String SERVER_INDEX = "장비 인덱스";
	public static final String SERVER_NAME = "장비명";	
	public static final String IP = "IP 주소";
	public static final String FACILITY_TYPE = "시설물 종류";
	public static final String RCU_TYPE = "RCU 종류";
	public static final String CONN_METHOD = "연결 방식";	
	public static final String PROTOCOL_NUMBER = "프로토콜 번호";
	public static final String SERVER_STATE = "장비 상태";
	
	private String searchElement = SERVER_NAME;
	
	public static boolean isExist = false;
	private JLabel MK119;
		
	private RCU rcu;
	private JTable FacListTable;	
	
	private JPanel contentPane;
	private JComboBox searchFacility_ComboBox;
	private JTextField searchFacility_textField;	
	private JLabel RCUInfoLabel_1;
	private JLabel RCUInfoLabel_2;
	private JButton dbRefresh_Button;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Protocol p = new Protocol();
//					p.setFacType("Moon Facility");
//					p.setName("Moon Protocol");
//					
//					PerfListFrame frame = new PerfListFrame(p.getName(), null, p);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public RcuInfoFrame(RCU  rcu) {		
		this.rcu = rcu;
		
		RcuInfoFrame.isExist = true;
		setTitle("RCU Information");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 900, 717);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("RCU Information");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 240, 55);
		actualPanel.add(currentFunction);
		
		MK119 = new JLabel();
		MK119.setHorizontalAlignment(SwingConstants.CENTER);
		MK119.setIcon(new Util().getMK2Resource());
		MK119.setForeground(Color.BLACK);
		MK119.setBackground(Color.WHITE);		
		MK119.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		MK119.setBounds(782, 8, 85, 36);
		actualPanel.add(MK119);
		
		JScrollPane perfList_scrollPane = new JScrollPane();
		perfList_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		perfList_scrollPane.setBounds(12, 128, 850, 530);
		actualPanel.add(perfList_scrollPane);
		
		FacListTable = new JTable();		
		FacListTable.setForeground(Color.BLACK);		
		FacListTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					// 왼쪽 클릭					
				} 
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭			
					int row = FacListTable.getSelectedRow();
					Facility fac = (Facility)FacListTable.getValueAt(row, 2);
					showFacilityMenu(fac);	
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = FacListTable.getSelectedRow();
					Facility fac = (Facility)FacListTable.getValueAt(row, 2);
					showFacilityMenu(fac);	
				}
			}
		});
		perfList_scrollPane.setViewportView(FacListTable);
		
		JLabel searchPerf_label = new JLabel("검 색");
		searchPerf_label.setHorizontalAlignment(SwingConstants.CENTER);
		searchPerf_label.setForeground(Color.BLACK);
		searchPerf_label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchPerf_label.setBackground(Color.WHITE);
		searchPerf_label.setBounds(15, 86, 63, 35);
		actualPanel.add(searchPerf_label);
		
		searchFacility_ComboBox = new JComboBox();
		searchFacility_ComboBox.setForeground(Color.BLACK);
		searchFacility_ComboBox.setBackground(Color.WHITE);		
		searchFacility_ComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchFacility_ComboBox.setBounds(81, 86, 142, 35);
		searchFacility_ComboBox.setModel(new DefaultComboBoxModel(new String[] {				
				SERVER_NAME, // 장비명
				SERVER_INDEX, // 장비 인덱스				
				FACILITY_TYPE, // 시설물 종류				
				PROTOCOL_NUMBER, // 프로토콜 번호
				SERVER_STATE, // 장비 상태
		}));
		searchFacility_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		searchFacility_ComboBox.setSelectedIndex(0);
		actualPanel.add(searchFacility_ComboBox);
		
		searchFacility_textField = new JTextField();
		searchFacility_textField.addFocusListener(Util.focusListener);
		searchFacility_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField.setForeground(Color.BLACK);
		searchFacility_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchFacility_textField.setColumns(10);
		searchFacility_textField.setBounds(228, 86, 460, 35);
		searchFacility_textField.addKeyListener(new KeyAdapter() {			
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
		actualPanel.add(searchFacility_textField);
		
		String RcuInfo_1 = "<html>";
		RcuInfo_1 += Util.colorBlue("RCU : ") + rcu.getName();
		RcuInfo_1 += "&nbsp;&nbsp;";
		RcuInfo_1 += " ( " + Util.colorGreen(rcu.getRcuTypeDetail()) + " )";
		RcuInfo_1 += "</html>";
		
		String RcuInfo_2 = "<html>";
		RcuInfo_2 += Util.colorBlue("IP : ") + rcu.getIp();
		RcuInfo_2 += Util.separator + Util.colorRed("/") + Util.separator;
		RcuInfo_2 += Util.colorBlue("Port : " );
		if(rcu.isMultiPort() && rcu.getPort() == 0) {
			ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
			MultiPortMap start = portMap.get(0);
			MultiPortMap end = portMap.get(portMap.size() - 1);
			
			RcuInfo_2 += start.getCh() + " ( " + start.getPort() + " )";
			RcuInfo_2 += " ~ ";
			RcuInfo_2 += end.getCh() + " ( " + end.getPort() + " ) ";
		}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
			RcuInfo_2 += rcu.getPort();
		}else if(!rcu.isMultiPort() && rcu.getPort() == 0){
			RcuInfo_2 += "Unknown";
		}else {
			RcuInfo_2 += "Unknown";
		}
		RcuInfo_2 += "</html>";
		
		RCUInfoLabel_1 = new JLabel(RcuInfo_1);
		RCUInfoLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		RCUInfoLabel_1.setForeground(Color.BLACK);
		RCUInfoLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		RCUInfoLabel_1.setBackground(Color.WHITE);
		RCUInfoLabel_1.setBounds(251, 8, 530, 35);
		actualPanel.add(RCUInfoLabel_1);
		
		RCUInfoLabel_2 = new JLabel(RcuInfo_2);
		RCUInfoLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		RCUInfoLabel_2.setForeground(Color.BLACK);
		RCUInfoLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		RCUInfoLabel_2.setBackground(Color.WHITE);
		RCUInfoLabel_2.setBounds(271, 43, 510, 35);
		actualPanel.add(RCUInfoLabel_2);
		
		dbRefresh_Button = new JButton("Database 최신화");
		dbRefresh_Button.setFocusPainted(false);
		dbRefresh_Button.setBackground(Color.WHITE);
		dbRefresh_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dbRefresh_Button.setForeground(Color.BLACK);
		dbRefresh_Button.setBounds(694, 86, 168, 35);
		dbRefresh_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshDB();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(dbRefresh_Button);
		
		// 테이블 로드
		updateFacilityTable(FacListTable);		
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	@Override
	public void dispose() {
		RcuInfoFrame.isExist = false;
		super.dispose();
	}
	
	
	// ************ DB 최신화 *******************************************
	public void refreshDB() {
		
		ServerList_Panel.resetForm(true, false);
		
		if(ServerList_Panel.serverMap.containsKey(rcu.getIndex())) {
			this.rcu = (RCU)ServerList_Panel.serverMap.get(rcu.getIndex());			
			
			String RcuInfo_1 = "<html>";
			RcuInfo_1 += Util.colorBlue("RCU : ") + rcu.getName();
			RcuInfo_1 += "&nbsp;&nbsp;";
			RcuInfo_1 += " ( " + Util.colorGreen(rcu.getRcuTypeDetail()) + " )";
			RcuInfo_1 += "</html>";
			
			String RcuInfo_2 = "<html>";
			RcuInfo_2 += Util.colorBlue("IP : ") + rcu.getIp();
			RcuInfo_2 += Util.separator + Util.colorRed("/") + Util.separator;
			RcuInfo_2 += Util.colorBlue("Port : " );
			if(rcu.isMultiPort() && rcu.getPort() == 0) {
				ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
				MultiPortMap start = portMap.get(0);
				MultiPortMap end = portMap.get(portMap.size() - 1);
				
				RcuInfo_2 += start.getCh() + " ( " + start.getPort() + " )";
				RcuInfo_2 += " ~ ";
				RcuInfo_2 += end.getCh() + " ( " + end.getPort() + " ) ";
			}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
				RcuInfo_2 += rcu.getPort();
			}else if(!rcu.isMultiPort() && rcu.getPort() == 0){
				RcuInfo_2 += "Unknown";
			}else {
				RcuInfo_2 += "Unknown";
			}
			RcuInfo_2 += "</html>";
			
			RCUInfoLabel_1.setText(RcuInfo_1);
			RCUInfoLabel_2.setText(RcuInfo_2);
		}else {
			String separator = Util.separator + Util.separator;
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Can Not Found RCU") , Util.separator, Util.separator));
			
			sb.append(String.format("%s%s%s\n", Util.colorRed("──────────[ 기존 RCU 정보 ]──────────"), separator, separator));				
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 이름"), rcu.getName(), separator, separator));
			sb.append(String.format("%s : %d%s%s\n", Util.colorRed("RCU 인덱스"), rcu.getIndex(), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU IP 주소"), rcu.getIp(), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 종류"), rcu.getRcuTypeDetail(), separator, separator));
			sb.append(String.format("%s : %d개%s%s\n", Util.colorRed("연결된 장비 개수"), rcu.getFacList().size(), separator, separator));
			
			sb.append(String.format("\n최신 데이터베이스 내용에서 기존 RCU 장비의 " + Util.colorRed("인덱스") + " 정보를 찾을 수 없습니다%s%s\n", Util.separator, Util.separator));
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			this.dispose();
			return;
		}
		
		searchFacility_textField.setText(null);
		searchFacility_ComboBox.setSelectedIndex(0);
		
		updateFacilityTable(FacListTable);
	}
	
	
	//*************** 시설물 목록 테이블  *********************************************************************************
	public void updateFacilityTable(JTable table) {		

		if (table == null || rcu == null) return;
		Object[][] content = new Object[rcu.getFacList().size()][];

		if(rcu.isMultiPort() && rcu.getPort() == 0) {
			int index = 0;
			ArrayList<MultiPortMap> portMappingList = rcu.getMultiPortMapList();
			
			for (int i = 0; i < portMappingList.size(); i++) {
				MultiPortMap map = portMappingList.get(i);							
				int facIndex = map.getFacIndex();
				
				if(ServerList_Panel.serverMap.containsKey(facIndex)) {
					Facility fac = (Facility)ServerList_Panel.serverMap.get(facIndex);
					content[index] = new Object[5];
					content[index][0] = index + 1;
					content[index][1] = fac.getTypeString();
					content[index][2] = fac;
					content[index][3] = (fac.getRcuPortCh() != 0 && fac.getPort() != 0) ? String.format("%d ( %d )",  map.getCh(), map.getPort()) : "Unknown"; 	
					content[index][4] = fac.getState();
					index++;
				}
			}
			
			for(int i = 0; i < rcu.getFacList().size(); i++) {
				boolean hasMapping = false;
				Facility fac = (Facility)rcu.getFacList().get(i);
				
				for(int i2 = 0; i2 < portMappingList.size(); i2++) {
					MultiPortMap map = portMappingList.get(i2);
					if(fac.getIndex() == map.getFacIndex()) {
						hasMapping = true;
						break;
					}
				}
				
				if(hasMapping) {
					continue;
				}else {
					// RCU와 연결은 되어있지만 멀티 포트 매핑 테이블에는 정보가 없는 시설물
					content[index] = new Object[5];
					content[index][0] = index + 1;
					content[index][1] = fac.getTypeString();
					content[index][2] = fac;
					content[index][3] = (fac.getRcuPortCh() != 0 && fac.getPort() != 0) ? String.format("%d ( %d )",  fac.getRcuPortCh(), fac.getPort()) : "Unknown"; 	
					content[index][4] = fac.getState();
					index++;
				}
			}
			
		}else {
			for (int i = 0; i < rcu.getFacList().size(); i++) {
				Facility fac = (Facility)rcu.getFacList().get(i);
				content[i] = new Object[5];
				content[i][0] = i + 1;
				content[i][1] = fac.getTypeString();
				content[i][2] = fac;
				content[i][3] = (fac.getRcuPortCh() != 0 && fac.getPort() != 0) ? String.format("%d ( %d )",  fac.getRcuPortCh(), fac.getPort()) : "Unknown";
				content[i][4] = fac.getState();
			}
		}
		

		table.setModel(new DefaultTableModel(
			content,
			new String[] {
				"순 서",
				"시설물 종류",
				"장비명",
				"포 트",
				"장비 상태"
			}) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
		
	//******************** 테이블 스타일 관련 *********************************************************************
	public void setTableStyle(JTable table) {
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 17));
		
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
		
		// 성능 리스트 테이블
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(60); // 시설물 종류		
		table.getColumnModel().getColumn(2).setPreferredWidth(400); // 장비명
		table.getColumnModel().getColumn(3).setPreferredWidth(50); // 포트
		table.getColumnModel().getColumn(4).setPreferredWidth(50); // 장비 상태
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		FindTextRenderer findCommerRenderer = new FindTextRenderer(4, "통신 오류", Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 시설물 종류
//		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 장비명
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 포트
		tcmSchedule.getColumn(4).setCellRenderer(findCommerRenderer); // 장비 상태
	}
	
	//******************** 테이블 필터링 관련 *********************************************************************
	public void doTableFilter() {
		if(searchFacility_textField == null) return;
		
		ArrayList<Facility> filteredFac = new ArrayList<Facility>();			
		
		String text = searchFacility_textField.getText();
		
		if(text == null || text.length() == 0 || text.equals("")) {	
			updateFacilityTable(FacListTable);
			return;
		}
		
		text = text.toUpperCase().trim();
		
		for(int i = 0; i < rcu.getFacList().size(); i++) {
			Facility fac = (Facility)rcu.getFacList().get(i);
			
			String searchElement = null;
			
			switch(searchFacility_ComboBox.getSelectedItem().toString()) {
				case SERVER_NAME : // 장비명
					searchElement = fac.getName();
					break;
				case SERVER_INDEX : // 장비 인덱스
					searchElement = String.valueOf(fac.getIndex());
					break;			
				case FACILITY_TYPE : // 시설물 종류
					searchElement = fac.getTypeString();
					break;
				case PROTOCOL_NUMBER : // 프로토콜 번호
					searchElement = String.valueOf(fac.isCommon() ? fac.getCommProtocol() : fac.getSnmpProtocol());
					break;
				case SERVER_STATE : // 장비 상태
					searchElement = fac.getState();
					break;
				default : 
					searchElement = fac.getName();
					break;
			}// switch - searchElement_1
			
			
			if(searchElement != null) {
				searchElement = searchElement.toUpperCase();
			}else {
				searchElement = "";
			}
			
			boolean isContain_1 = false;
			
			if(text.contains(",")) {
				String[] textToken = text.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2].trim();
					if(searchElement.contains(token)) {
						isContain_1 = true;
					}
				}
			}else if(searchElement.contains(text)) {
				isContain_1 = true;
			}			
			
			if(isContain_1) {
				filteredFac.add(fac);
			}
			
		}// for loop
		
		Object[][] content = new Object[filteredFac.size()][];
		
		for (int i = 0; i < filteredFac.size(); i++) {
			Facility fac = (Facility)filteredFac.get(i);
			content[i] = new Object[5];
			content[i][0] = i + 1;
			content[i][1] = fac.getTypeString();
			content[i][2] = fac;
			
			String port = null;
			
			if(rcu.isMultiPort() && rcu.getPort() == 0) {
				port = String.format("%d ( %d )", fac.getRcuPortCh(), fac.getPort());
			}else if(!rcu.isMultiPort() && rcu.getPort() != 0) {
				port = String.format("%d ( %d )", fac.getRcuPortCh(), fac.getPort());
			}else {
				port = "Unknown";
			}
			content[i][3] = port;
			
			content[i][4] = fac.getState();
		}

		FacListTable.setModel(new DefaultTableModel(
				content,
				new String[] { 
						"순 서",
						"시설물 종류",
						"장비명",
						"포 트",
						"장비 상태"
						}) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
		});

		setTableStyle(FacListTable);
	}
	
	// ******************************************************************************************
	public static void showFacilityMenu(Facility fac) {
		if(fac == null) return;

		int menu = -1;
		String separator = Util.separator + Util.separator;
		
		if(fac.isFacility()) {			
			// 시설물
			StringBuilder msg = new StringBuilder();
						
			msg.append(String.format("%s%s%s\n", Util.colorBlue("──────────[ 시설물 정보 ]──────────"), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("장비명"), fac.getName(), separator, separator));
			msg.append(String.format("%s : %d%s%s\n", Util.colorBlue("장비 인덱스"), fac.getIndex(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("IP 주소"), fac.isConnRCU() ? "( RCU IP ) " + fac.getIp() : fac.getIp(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("시설물 종류"), fac.getTypeString(), separator, separator));
			msg.append(String.format("%s : %s%s%s\n", Util.colorBlue("연결 방식"), fac.getConnMethod(), separator, separator));
			
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
				msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 이름"), fac.getRcu().getName(), separator, separator));
				msg.append(String.format("%s : %d%s%s\n", Util.colorRed("RCU 인덱스"), fac.getRcu().getIndex(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU IP 주소"), fac.getRcu().getIp(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU 종류"), fac.getRcu().getRcuTypeDetail(), separator, separator));
				msg.append(String.format("%s : %d개%s%s\n\n", Util.colorRed("연결된 장비 개수"), fac.getRcu().getFacList().size(), separator, separator));
				msg.append(String.format("%s%s%s\n", Util.colorRed("RCU") + " 장비와 " + Util.colorBlue("시설물") + "이 연결된 상태에서 " + Util.colorRed("RCU") + " 장비가 삭제되었을 수 있습니다", separator, separator));
			}else {
				msg.append(String.format("\n%s%s%s\n", Util.colorGreen("──────────[ RCU 정보 ]──────────"), separator, separator));				
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 이름"), fac.getRcu().getName(), separator, separator));
				msg.append(String.format("%s : %d%s%s\n", Util.colorGreen("RCU 인덱스"), fac.getRcu().getIndex(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU IP 주소"), fac.getRcu().getIp(), separator, separator));
				msg.append(String.format("%s : %s%s%s\n", Util.colorGreen("RCU 종류"), fac.getRcu().getRcuTypeDetail(), separator, separator));
				msg.append(String.format("%s : %d개%s%s\n", Util.colorGreen("연결된 장비 개수"), fac.getRcu().getFacList().size(), separator, separator));
			}
			
			menu = Util.showOption(msg.toString(), new String[] { "성능 정보 보기", "취 소"}, JOptionPane.INFORMATION_MESSAGE, false);
			switch (menu) {		
				case 0: // 성능 정보 보기
					new WatchPointListFrame(fac);
					return;
				default :
					return;
			}
		}
	}
}
