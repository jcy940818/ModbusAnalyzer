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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfLabelStatusBean;
import common.server.Facility;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class FacilityInfoFrame extends JFrame {
			
	private static final String PERF_NAME = "성능명";
	private static final String PERF_INDEX = "성능 인덱스";
	private static final String PERF_TYPE = "성능 종류";
	private static final String PERF_COUNTER =  "성능 카운터";
	private static final String OID = "OID";
	private static final String INTERVAL = "수집 주기";
	private static final String UNIT = "단 위";
	private static final String SCALE = "보정식";
	private static final String DATA_FORMAT = "데이터 형식";
	
	public static final int PERF_LIST_TABLE = 0;
	public static final int PERF_INFO_TABLE = 1;
	public static final int PERF_LABEL_TABLE = 2;
	
	private String searchElement = PERF_NAME;
	
	private JTable perfListTable;
	private JTable perfInfoTable;
	private JTable perfLabelTable;
	
	public static boolean isExist = false;
	private JLabel MK119;
	 
//	private File xmlFile;
//	private Protocol protocol;
	
	private Facility fac;
	private boolean isCommon;
	private ArrayList<FmsPerfItem> perfs;
	private Perf selectedPerf;	
	
	private JPanel contentPane;
	private JPanel view_Panel;
	private JLabel mappingLabel;
	private JScrollPane perfInfoPanel;
	private JScrollPane perfLabelInfoPanel;
	private JComboBox searchPerf_ComboBox_1;
	private JComboBox searchPerf_ComboBox_2;
	private JTextField searchPerf_textField_1;	
	private JTextField searchPerf_textField_2;
	private JLabel FacilityInfoLabel_1;
	private JLabel FacilityInfoLabel_2;
	private JButton dbRefreshButton;
	
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
	public FacilityInfoFrame(Facility fac) {		
		this.fac = fac;
		this.isCommon = fac.isCommon();
		this.perfs = Perf.getFaciltiyPerfList(ONION_Info.getMk119Connection(), fac);
		
		FacilityInfoFrame.isExist = true;
		setTitle("Facility Information");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 1080, 717);
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
		
		JLabel currentFunction = new JLabel("Facility Information");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 250, 55);
		actualPanel.add(currentFunction);
		
		MK119 = new JLabel();
		MK119.setHorizontalAlignment(SwingConstants.CENTER);
		MK119.setIcon(new Util().getMK2Resource());
		MK119.setForeground(Color.BLACK);
		MK119.setBackground(Color.WHITE);		
		MK119.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		MK119.setBounds(962, 6, 85, 36);
//		actualPanel.add(MK119);
		
		JScrollPane perfList_scrollPane = new JScrollPane();
		perfList_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		perfList_scrollPane.setBounds(12, 128, 535, 530);
		actualPanel.add(perfList_scrollPane);
		
		perfListTable = new JTable();		
		perfListTable.setForeground(Color.BLACK);
		perfListTable.addFocusListener(new FocusListener() {			
			public void focusLost(FocusEvent e) {
				int row = perfListTable.getSelectedRow();
				Perf perf = (Perf) perfListTable.getValueAt(row, 1);
				selectedPerf = perf;
				updatePerfInfoTable(perfInfoTable, perf);
			}
			public void focusGained(FocusEvent e) {
				int row = perfListTable.getSelectedRow();
				Perf perf = (Perf) perfListTable.getValueAt(row, 1);
				selectedPerf = perf;
				updatePerfInfoTable(perfInfoTable, perf);	
			}
		});
		perfListTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) { 
				int row = perfListTable.getSelectedRow();				
				Perf perf = (Perf) perfListTable.getValueAt(row, 1);	
				selectedPerf = perf;
				updatePerfInfoTable(perfInfoTable, perf);
			}
			public void keyReleased(KeyEvent e) { 
				int row = perfListTable.getSelectedRow();				
				Perf perf = (Perf) perfListTable.getValueAt(row, 1);	
				selectedPerf = perf;
				updatePerfInfoTable(perfInfoTable, perf);
			}
		});
		perfListTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { 
					int row = perfListTable.getSelectedRow();				
					Perf perf = (Perf) perfListTable.getValueAt(row, 1);
					selectedPerf = perf;
					updatePerfInfoTable(perfInfoTable, perf);
				} // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) { /* Not Implement */ }
				if (e.getButton() == 3) { /* Not Implement */ }
			}
		});
		perfList_scrollPane.setViewportView(perfListTable);
		
		view_Panel = new JPanel();
		view_Panel.setBackground(Color.WHITE);		
		view_Panel.setBounds(559, 128, 483, 530);
		view_Panel.setLayout(null);
		actualPanel.add(view_Panel);
		
		perfInfoPanel = new JScrollPane();
		perfInfoPanel.setBackground(Color.WHITE);
		perfInfoPanel.setBorder(new LineBorder(Color.BLACK, 2));
		perfInfoPanel.setBounds(0, 0, 483, 272);
		view_Panel.add(perfInfoPanel);		
		
		perfInfoTable = new JTable();
		perfInfoTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] { "필 드", "내 용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						false, // 내 용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setTableStyle(perfInfoTable, PERF_INFO_TABLE);
		perfInfoPanel.setViewportView(perfInfoTable);
		
		mappingLabel = new JLabel("");
		mappingLabel.setBackground(Color.WHITE);
		mappingLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mappingLabel.setForeground(Color.BLACK);
		mappingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		mappingLabel.setBounds(0, 288, 483, 26);
		view_Panel.add(mappingLabel);
		
		perfLabelInfoPanel = new JScrollPane();
		perfLabelInfoPanel.setBounds(0, 318, 483, 212);
		perfLabelInfoPanel.setBorder(new LineBorder(Color.BLACK, 2));
		view_Panel.add(perfLabelInfoPanel);
		
		perfLabelTable = new JTable();
		perfLabelTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null}
				},
				new String[] { "값", "매핑 내용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 값 : 수정 불가
						false, // 매핑 내용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setTableStyle(perfLabelTable, PERF_LABEL_TABLE);		
		perfLabelInfoPanel.setViewportView(perfLabelTable);
		
		JLabel searchPerf_label = new JLabel("검 색");
		searchPerf_label.setHorizontalAlignment(SwingConstants.CENTER);
		searchPerf_label.setForeground(Color.BLACK);
		searchPerf_label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchPerf_label.setBackground(Color.WHITE);
		searchPerf_label.setBounds(18, 58, 63, 68);
		actualPanel.add(searchPerf_label);		
		
		searchPerf_ComboBox_1 = new JComboBox();
		searchPerf_ComboBox_1.setForeground(Color.BLACK);
		searchPerf_ComboBox_1.setBackground(Color.WHITE);		
		searchPerf_ComboBox_1.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		searchPerf_ComboBox_1.setBounds(85, 60, 120, 30);
		searchPerf_ComboBox_1.setModel(new DefaultComboBoxModel(new String[] {
				PERF_NAME,
				PERF_INDEX,
				PERF_TYPE,
				(this.isCommon) ? PERF_COUNTER : OID, 
				INTERVAL, 
				UNIT, 
				SCALE, 
				DATA_FORMAT
		}));
		searchPerf_ComboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		searchPerf_ComboBox_1.setSelectedIndex(0);
		actualPanel.add(searchPerf_ComboBox_1);
		
		searchPerf_ComboBox_2 = new JComboBox();
		searchPerf_ComboBox_2.setForeground(Color.BLACK);
		searchPerf_ComboBox_2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		searchPerf_ComboBox_2.setBackground(Color.WHITE);
		searchPerf_ComboBox_2.setBounds(85, 94, 120, 30);
		searchPerf_ComboBox_2.setModel(new DefaultComboBoxModel(new String[] {
				PERF_NAME,
				PERF_INDEX,
				PERF_TYPE,
				(this.isCommon) ? PERF_COUNTER : OID, 
				INTERVAL, 
				UNIT, 
				SCALE, 
				DATA_FORMAT
		}));
		searchPerf_ComboBox_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		searchPerf_ComboBox_2.setSelectedIndex(3);
		actualPanel.add(searchPerf_ComboBox_2);
		
		
		searchPerf_textField_1 = new JTextField();
		searchPerf_textField_1.addFocusListener(Util.focusListener);
		searchPerf_textField_1.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField_1.setForeground(Color.BLACK);
		searchPerf_textField_1.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField_1.setColumns(10);
		searchPerf_textField_1.setBounds(209, 60, 338, 30);
		searchPerf_textField_1.addKeyListener(new KeyAdapter() {			
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
		actualPanel.add(searchPerf_textField_1);
		
		searchPerf_textField_2 = new JTextField();
		searchPerf_textField_2.addFocusListener(Util.focusListener);
		searchPerf_textField_2.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField_2.setForeground(Color.BLACK);
		searchPerf_textField_2.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField_2.setColumns(10);
		searchPerf_textField_2.setBounds(209, 94, 338, 30);
		searchPerf_textField_2.addKeyListener(new KeyAdapter() {			
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
		actualPanel.add(searchPerf_textField_2);
		
		String facInfo_1 = "";
		facInfo_1 += "<html>";
		facInfo_1 += Util.colorBlue("시설물 : ") + fac.getName();
		facInfo_1 += "&nbsp;&nbsp;";
		facInfo_1 += "( ";
		facInfo_1 += Util.colorGreen(fac.getTypeString() + Util.colorRed(" / ") + fac.getConnMethod());
		facInfo_1 += " )";
		facInfo_1 += "</html>";
		
		String facInfo_2 = "";
		if(fac.isConnRCU() && fac.getRcu() != null) {
			facInfo_2 += "<html>";
			
			// TCP/IP 이중화 RCU
			if(fac.getRcu().isDuplexedPort()) {
				facInfo_2 += Util.colorBlue("RCU : ") + fac.getRcu().getName();
				facInfo_2 += "&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;";

				facInfo_2 += Util.colorBlue("RCU IP : ") + fac.getRcu().getIp();
				facInfo_2 += Util.colorGreen(" & ")  + fac.getRcu().getAuxIP();
				facInfo_2 += "&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;";

				facInfo_2 += Util.colorBlue("RCU Port : ");
				
				if(fac.getPort() != 0) {
					facInfo_2 += fac.getPort() + Util.colorGreen(" & ") + fac.getRcu().getAuxPort();
				}else {
					facInfo_2 += "Unknown";
				}
			}else {
				// 일반 RCU
				facInfo_2 += Util.colorBlue("RCU : ") + fac.getRcu().getName();
				facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);

				facInfo_2 += Util.colorBlue("RCU IP : ") + fac.getRcu().getIp();
				facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);

				facInfo_2 += Util.colorBlue("RCU Port : ");
				
				if(fac.getPort() != 0) {
					facInfo_2 += fac.getRcuPortCh() + " ( " + fac.getPort() + " )";
				}else {
					facInfo_2 += "Unknown";
				}
			}
			
			facInfo_2 += "</html>";
		}else if(!fac.isConnRCU() && fac.getRcu() != null){
			facInfo_2 += "<html>";
			facInfo_2 += Util.colorRed("장비와 연결된 RCU 정보를 찾을 수 없습니다");
			facInfo_2 += "</html>";
		}else {
			facInfo_2 = "<html>";
			facInfo_2 += Util.colorBlue("IP : ") + fac.getIp();
			facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);
			facInfo_2 += Util.colorBlue("Port : ") + fac.getPort(); 
			facInfo_2 += "</html>";
		}
		
		FacilityInfoLabel_1 = new JLabel(facInfo_1);
		FacilityInfoLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		FacilityInfoLabel_1.setForeground(Color.BLACK);
		FacilityInfoLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		FacilityInfoLabel_1.setBackground(Color.WHITE);
		FacilityInfoLabel_1.setBounds(252, 1, 802, 30);
		actualPanel.add(FacilityInfoLabel_1);
		
		FacilityInfoLabel_2 = new JLabel(facInfo_2);
		FacilityInfoLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		FacilityInfoLabel_2.setForeground(Color.BLACK);
		FacilityInfoLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		FacilityInfoLabel_2.setBackground(Color.WHITE);
		FacilityInfoLabel_2.setBounds(252, 28, 802, 30);		
		actualPanel.add(FacilityInfoLabel_2);
		
		dbRefreshButton = new JButton("Database 최신화");
		dbRefreshButton.setForeground(Color.BLACK);
		dbRefreshButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		dbRefreshButton.setFocusPainted(false);
		dbRefreshButton.setContentAreaFilled(false);
		dbRefreshButton.setBorder(UIManager.getBorder("Button.border"));
		dbRefreshButton.setBackground(Color.WHITE);
		dbRefreshButton.setBounds(560, 89, 160, 35);
		dbRefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshDB();
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(dbRefreshButton);
		
		// 테이블 로드
		updatePerfListTable(perfListTable);
		
		JButton formReset_Button = new JButton("Form 초기화");
		formReset_Button.setForeground(Color.BLACK);
		formReset_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		formReset_Button.setFocusPainted(false);
		formReset_Button.setContentAreaFilled(false);
		formReset_Button.setBorder(UIManager.getBorder("Button.border"));
		formReset_Button.setBackground(Color.WHITE);
		formReset_Button.setBounds(725, 89, 136, 35);
		formReset_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchPerf_textField_1.setText(null);
				searchPerf_textField_2.setText(null);
				searchPerf_ComboBox_1.setSelectedIndex(0);
				searchPerf_ComboBox_2.setSelectedIndex(3);
			}
		});
		actualPanel.add(formReset_Button);
		
		// ESC : Close Listener
		CloseListener close = new CloseListener();
		this.addKeyListener(close);
		getContentPane().addKeyListener(close);
		searchPerf_ComboBox_1.addKeyListener(close);
		searchPerf_ComboBox_2.addKeyListener(close);
		searchPerf_textField_1.addKeyListener(close);
		searchPerf_textField_2.addKeyListener(close);
		perfListTable.addKeyListener(close);
		perfInfoTable.addKeyListener(close);
		perfLabelTable.addKeyListener(close);
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	@Override
	public void dispose() {
		FacilityInfoFrame.isExist = false;
		super.dispose();
	}
	
	
	// ************ XML Reload *******************************************
	public void refreshDB() {
		
		ServerList_Panel.resetForm(true, false);
		
		if(ServerList_Panel.serverMap.containsKey(fac.getIndex())) {
			this.fac = (Facility)ServerList_Panel.serverMap.get(fac.getIndex());
			this.isCommon = fac.isCommon();
			
			String facInfo_1 = "";
			facInfo_1 += "<html>";
			facInfo_1 += Util.colorBlue("시설물 : ") + fac.getName();
			facInfo_1 += "&nbsp;&nbsp;";
			facInfo_1 += "( ";
			facInfo_1 += Util.colorGreen(fac.getTypeString() + Util.colorRed(" / ") + fac.getConnMethod());
			facInfo_1 += " )";
			facInfo_1 += "</html>";
			
			String facInfo_2 = "";
			if(fac.isConnRCU() && fac.getRcu() != null) {
				facInfo_2 += "<html>";
				
				// TCP/IP 이중화 RCU
				if(fac.getRcu().isDuplexedPort()) {
					facInfo_2 += Util.colorBlue("RCU : ") + fac.getRcu().getName();
					facInfo_2 += "&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;";

					facInfo_2 += Util.colorBlue("RCU IP : ") + fac.getRcu().getIp();
					facInfo_2 += Util.colorGreen(" & ")  + fac.getRcu().getAuxIP();
					facInfo_2 += "&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;";

					facInfo_2 += Util.colorBlue("RCU Port : ");
					
					if(fac.getPort() != 0) {
						facInfo_2 += fac.getPort() + Util.colorGreen(" & ") + fac.getRcu().getAuxPort();
					}else {
						facInfo_2 += "Unknown";
					}
				}else {
					// 일반 RCU
					facInfo_2 += Util.colorBlue("RCU : ") + fac.getRcu().getName();
					facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);

					facInfo_2 += Util.colorBlue("RCU IP : ") + fac.getRcu().getIp();
					facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);

					facInfo_2 += Util.colorBlue("RCU Port : ");
					
					if(fac.getPort() != 0) {
						facInfo_2 += fac.getRcuPortCh() + " ( " + fac.getPort() + " )";
					}else {
						facInfo_2 += "Unknown";
					}
				}
				
				facInfo_2 += "</html>";
			}else if(!fac.isConnRCU() && fac.getRcu() != null){
				facInfo_2 += "<html>";
				facInfo_2 += Util.colorRed("장비와 연결된 RCU 정보를 찾을 수 없습니다");
				facInfo_2 += "</html>";
			}else {
				facInfo_2 = "<html>";
				facInfo_2 += Util.colorBlue("IP : ") + fac.getIp();
				facInfo_2 += Util.colorRed(Util.separator + "/" + Util.separator);
				facInfo_2 += Util.colorBlue("Port : ") + fac.getPort(); 
				facInfo_2 += "</html>";
			}
			
			FacilityInfoLabel_1.setText(facInfo_1);
			FacilityInfoLabel_2.setText(facInfo_2);
		}else {
			String separator = Util.separator + Util.separator;
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Can Not Found Facility") , Util.separator, Util.separator));
			
			sb.append(String.format("%s%s%s\n", Util.colorRed("──────────[ 기존 시설물 정보 ]──────────"), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("시설물 종류"), fac.getTypeString(), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("장비명"), fac.getName(), separator, separator));
			sb.append(String.format("%s : %d%s%s\n", Util.colorRed("장비 인덱스"), fac.getIndex(), separator, separator));
			
			String connInfo = "";
			connInfo += Util.colorBlue("IP") + " : " + fac.getIp();
			connInfo += "&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;";
			connInfo += Util.colorBlue("Port") + " : "+ fac.getPort();
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("연결 정보"), fac.isConnRCU() ? Util.colorRed("( RCU ) ") + connInfo : connInfo, separator, separator));			
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("연결 방식"), fac.getConnMethod(), separator, separator));
			
			sb.append(String.format("\n최신 데이터베이스 내용에서 기존 장비의 " + Util.colorRed("인덱스") + " 정보를 찾을 수 없습니다%s%s\n", Util.separator, Util.separator));	
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			this.dispose();
			return;
		}
		
		this.perfs = Perf.getFaciltiyPerfList(ONION_Info.getMk119Connection(), this.fac);
		
		updatePerfListTable(perfListTable);
		
		perfInfoTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] { "필 드", "내 용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						false, // 내 용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setTableStyle(perfInfoTable, PERF_INFO_TABLE);
		
		perfLabelTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},					
				},
				new String[] { "값", "매핑 내용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 값 : 수정 불가
						false, // 매핑 내용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setTableStyle(perfLabelTable, PERF_LABEL_TABLE);
		
		searchPerf_textField_1.setText(null);
		searchPerf_textField_2.setText(null);
		mappingLabel.setText(null);
	}
	
	
	//*************** 성능 리스트 테이블  *********************************************************************************
	public void updatePerfListTable(JTable table) {		

		if (table == null || perfs == null) return;

		Object[][] content = new Object[perfs.size()][];

		for (int i = 0; i < perfs.size(); i++) {
			Perf perf = perfs.get(i);
//			perf.setIndex(i + 1);
			
			content[i] = new Object[2];
			content[i][0] = i + 1; // 순 서
			content[i][1] = perf;
		}

		table.setModel(new DefaultTableModel(
			content,
			new String[] { "순 서", "성 능"}) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table, PERF_LIST_TABLE);
	}
	
	
	//******************** 성능 정보 테이블 관련 *********************************************************************
	public void updatePerfInfoTable(JTable table, Perf perf) {

		if (table == null || perf == null) return;
		
		Object[][] content = new Object[8][];

		content[0] = new Object[2];
		content[0][0] = "성능명";
		content[0][1] = perf.getDisplayName();
		
		content[1] = new Object[2];
		content[1][0] = "성능 인덱스";
		content[1][1] = perf.getIndex();
		
		content[2] = new Object[2];
		content[2][0] = "성능 종류";
		content[2][1] = perf.getPerfTypeString();
		
		content[3] = new Object[2];
		content[3][0] = (this.isCommon) ? "성능 카운터" : "OID";
		content[3][1] = perf.getCounter();
		
		content[4] = new Object[2];
		content[4][0] = "수집 주기";
		content[4][1] = perf.getInterval();
		
		content[5] = new Object[2];
		content[5][0] = "단 위";
		content[5][1] = perf.getMeasure();
		
		content[6] = new Object[2];
		content[6][0] = "보정식";
		content[6][1] = perf.getScaleFunction();
		
		content[7] = new Object[2];
		content[7][0] = "데이터 형식";

		String dataFormat = String.valueOf(perf.getDataFormat() + " : ");
		
		int format = perf.getDataFormat();
		
		if(format == 1) {
			dataFormat += "이진 상태 ( DI )";
			mappingLabel.setText("이진 상태 매핑 정보");
			perfLabelTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelTable, perf);
		}else if(format == 2) {
			dataFormat += "다중 상태 성능";
			mappingLabel.setText("다중 상태 매핑 정보");
			perfLabelTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelTable, perf);
		}else {
			dataFormat += "성능 데이터 ( Analog )";
			mappingLabel.setText("상태 매핑 정보 없음");
			perfLabelTable.setVisible(false);
		}
		content[7][1] = dataFormat;

		table.setModel(new DefaultTableModel(
				content,
				new String[] { "필 드", "내 용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						false, // 내 용 : 수정 가능						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(table, PERF_INFO_TABLE);
	}
	
	//******************** 성능 레이블 매핑 정보 테이블 관련 *********************************************************************
		public void updatePerfLabelMappingTable(JTable table, Perf perf) {		
			
			if (table == null || perf == null) return;
			
			Object[][] content;
			String[] binLabel;
			PerfLabelStatusBean[] labels;
			
			if(perf.getDataFormat() == 1) {
				binLabel = perf.getBinLabel();
				content = new Object[binLabel.length][];
				
				content[0] = new Object[2];
				content[0][0] = 0;
				content[0][1] = binLabel[0];
				
				content[1] = new Object[2];
				content[1][0] = 1;
				content[1][1] = binLabel[1];				
			}else {
				labels = perf.getStatusLabels();
				content = new Object[labels.length][];
				for(int i = 0; i < labels.length; i++) {
					content[i] = new Object[2];
					content[i][0] = labels[i].value;
					content[i][1] = labels[i].label;
				}
			}

			table.setModel(new DefaultTableModel(
					content,
					new String[] { "값", "매핑 내용"}) {
					boolean[] columnEditables = new boolean[] {
							false, // 필 드 : 수정 불가
							false, // 내 용 : 수정 가능						
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
			});
			
			setTableStyle(table, PERF_LABEL_TABLE);
		}
		
		//******************** 테이블 스타일 관련 *********************************************************************
		public void setTableStyle(JTable table, int tableType) {
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
			if(tableType == PERF_LIST_TABLE) {
				table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
				table.setRowHeight(25); 
			}else {
				table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
				table.setRowHeight(30); 
			}
			
			// 테이블 셀 크기 설정
			if(tableType == PERF_LIST_TABLE) {
				// 성능 리스트 테이블
				table.getColumnModel().getColumn(0).setPreferredWidth(5); // 순 서
				table.getColumnModel().getColumn(1).setPreferredWidth(400); // 성 능		
			}else if(tableType == PERF_INFO_TABLE) {
				// 성능 정보 테이블
				table.getColumnModel().getColumn(0).setPreferredWidth(3); // 필 드		
				table.getColumnModel().getColumn(1).setPreferredWidth(180); // 내 용
			}else if(tableType == PERF_LABEL_TABLE) {
				// 성능 레이블 테이블
				table.getColumnModel().getColumn(0).setPreferredWidth(4); // 값	
				table.getColumnModel().getColumn(1).setPreferredWidth(350); // 매핑 내용		
			}
			
			// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
			DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

			// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
			tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 ColumnModel을 가져옴
			TableColumnModel tcmSchedule = table.getColumnModel();
			tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
			
			if(tableType == PERF_LABEL_TABLE) {
				tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 성능 레이블 테이블만 가운데 정렬
			}else {
				// tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
			}
		}
		
		//******************** 테이블 필터링 관련 *********************************************************************
		public void doTableFilter() {
			if(searchPerf_textField_1 == null || searchPerf_textField_2 == null) return;
			
			ArrayList<FmsPerfItem> filteredPerf = new ArrayList<FmsPerfItem>();
			String text_1 = searchPerf_textField_1.getText();
			String text_2 = searchPerf_textField_2.getText();
			
			boolean noSearch_1 = (text_1 == null || text_1.length() == 0 || text_1.equals(""));
			boolean noSearch_2 = (text_2 == null || text_2.length() == 0 || text_2.equals(""));
			
			if(noSearch_1 && noSearch_2) {	
				updatePerfListTable(perfListTable);
				return;
			}
			
			text_1 = text_1.toUpperCase().trim();
			text_2 = text_2.toUpperCase().trim();
			
			for(int i = 0; i < perfs.size(); i++) {
				FmsPerfItem perf = perfs.get(i);
				
				String searchElement_1 = null;
				String searchElement_2 = null;
				
				switch(searchPerf_ComboBox_1.getSelectedItem().toString()) {
					case PERF_NAME :
						searchElement_1 = perf.getDisplayName();
						break;
					case PERF_INDEX :
						searchElement_1 = String.valueOf(perf.getIndex());
						break;
					case PERF_TYPE :
						searchElement_1 = perf.getPerfTypeString();
						break;
					case PERF_COUNTER :
					case OID :
						searchElement_1 = perf.getCounter();
						break;
					case INTERVAL :
						searchElement_1 = String.valueOf(perf.getInterval());
						break;
					case UNIT :
						searchElement_1 = perf.getMeasure();
						break;
					case SCALE :
						searchElement_1 = perf.getScaleFunction();
						break;
					case DATA_FORMAT :
						searchElement_1 = String.valueOf(perf.getDataFormat());
						break;
					default : 
						searchElement_1 = perf.getDisplayName();
						break;
				}// switch - searchElement_1
				
				switch(searchPerf_ComboBox_2.getSelectedItem().toString()) {
					case PERF_NAME :
						searchElement_2 = perf.getDisplayName();
						break;
					case PERF_INDEX :
						searchElement_2 = String.valueOf(perf.getIndex());
						break;
					case PERF_TYPE :
						searchElement_2 = perf.getPerfTypeString();
						break;
					case PERF_COUNTER :
					case OID :
						searchElement_2 = perf.getCounter();
						break;
					case INTERVAL :
						searchElement_2 = String.valueOf(perf.getInterval());
						break;
					case UNIT :
						searchElement_2 = perf.getMeasure();
						break;
					case SCALE :
						searchElement_2 = perf.getScaleFunction();
						break;
					case DATA_FORMAT :
						searchElement_2 = String.valueOf(perf.getDataFormat());
						break;
					default : 
						searchElement_2 = perf.getDisplayName();
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
					filteredPerf.add(perf);
				}// AND Operation isContains 1, 2
				
			}// for loop
			
			Object[][] content = new Object[filteredPerf.size()][];
			
			for (int i = 0; i < filteredPerf.size(); i++) {
				FmsPerfItem perf = filteredPerf.get(i);
				content[i] = new Object[2];
				content[i][0] = perf.getIndex();
				content[i][1] = perf;
			}

			perfListTable.setModel(new DefaultTableModel(
					content,
					new String[] { "순 서", "성 능"}) {
					// 테이블 셀 내용 수정 금지
					public boolean isCellEditable(int i, int c) {
						return false;
					}
			});

			setTableStyle(perfListTable, PERF_LIST_TABLE);
		}
		
		
		// 사용자 정의 키 이벤트 리스너
		class CloseListener extends KeyAdapter{	
			public void keyPressed(KeyEvent e) {			
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}		
			
			public void keyReleased(KeyEvent e) {			
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		}
		
}
