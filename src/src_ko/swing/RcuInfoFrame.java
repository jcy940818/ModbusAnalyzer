package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import common.server.Event;
import common.server.Facility;
import common.server.MultiPortMap;
import common.server.RCU;
import common.server.SystemSeverity;
import common.util.FindTextRenderer;
import common.util.FontManager;
import common.util.SeverityRenderer;
import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.util.Util;

public class RcuInfoFrame extends JFrame {;
	
	public static final String ORDER = "�� ��";	
	public static final String SERVER_INDEX = "��� �ε���";
	public static final String SERVER_NAME = "����";	
	public static final String IP = "IP �ּ�";
	public static final String FACILITY_TYPE = "�ü��� ����";
	public static final String RCU_TYPE = "RCU ����";
	public static final String CONN_METHOD = "���� ���";	
	public static final String PROTOCOL_NUMBER = "�������� ��ȣ";
	public static final String PROTOCOL_NAME = "�������� �̸�";
	public static final String SERVER_STATE = "��� ����";
	public static final String EVENT = "�̺�Ʈ";
	
	private String searchElement = SERVER_NAME;
	
	public static boolean isExist = false;
	private JLabel MK119;
		
	private RCU rcu;
	private JTable FacListTable;	
	
	private JPanel contentPane;
	private JComboBox searchFacility_ComboBox;
	private JTextField searchFacility_textField;	
	
	
	private JButton dbRefresh_Button;
	private JButton eventInfo_Button;
	
	private JLabel currentFunction;
	private JLabel RCUInfoLabel_1;
	private JLabel RCUInfoLabel_2;
	
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
		setTitle(String.format("RCU Information   [  Index : %d  /  Name : %s  ]", rcu.getIndex(), rcu.getName()));
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
		
		currentFunction = new JLabel();
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.CENTER);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 70, 60);
		actualPanel.add(currentFunction);
		
		MK119 = new JLabel();
		MK119.setHorizontalAlignment(SwingConstants.CENTER);
		MK119.setIcon(new Util().getMK2Resource());
		MK119.setForeground(Color.BLACK);
		MK119.setBackground(Color.WHITE);		
		MK119.setFont(FontManager.getFont(Font.BOLD, 17));
		MK119.setBounds(782, 8, 85, 36);
//		actualPanel.add(MK119);
		
		JScrollPane perfList_scrollPane = new JScrollPane();
		perfList_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		perfList_scrollPane.setBounds(12, 128, 850, 530);
		actualPanel.add(perfList_scrollPane);
		
		FacListTable = new JTable();		
		FacListTable.setForeground(Color.BLACK);		
		FacListTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					// ���� Ŭ��					
				} 
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// ���� ��ư ���� Ŭ��			
					int row = FacListTable.getSelectedRow();
					Facility fac = (Facility)FacListTable.getValueAt(row, 2);
					MK119_Lite_Panel.showFacilityMenu(fac, false);
				}
				if (e.getButton() == 3) {
					// ������ Ŭ��
					int row = FacListTable.getSelectedRow();
					Facility fac = (Facility)FacListTable.getValueAt(row, 2);
					MK119_Lite_Panel.showFacilityMenu(fac, false);
				}
			}
		});
		perfList_scrollPane.setViewportView(FacListTable);
		
		JLabel searchPerf_label = new JLabel("�� ��");
		searchPerf_label.setHorizontalAlignment(SwingConstants.CENTER);
		searchPerf_label.setForeground(Color.BLACK);
		searchPerf_label.setFont(FontManager.getFont(Font.BOLD, 18));
		searchPerf_label.setBackground(Color.WHITE);
		searchPerf_label.setBounds(15, 85, 63, 35);
		actualPanel.add(searchPerf_label);
		
		searchFacility_ComboBox = new JComboBox();
		searchFacility_ComboBox.setForeground(Color.BLACK);
		searchFacility_ComboBox.setBackground(Color.WHITE);		
		searchFacility_ComboBox.setFont(FontManager.getFont(Font.BOLD, 16));
		searchFacility_ComboBox.setBounds(81, 86, 185, 35);
		if(MK119_Lite_Panel.linkMK119_Protocol) {
			searchFacility_ComboBox.setModel(new DefaultComboBoxModel(new String[] {				
					FACILITY_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					PROTOCOL_NUMBER, // �������� ��ȣ
					PROTOCOL_NAME, // �������� �̸�
					SERVER_STATE, // ��� ����
					EVENT, // ��� ����
			}));
		}else {
			searchFacility_ComboBox.setModel(new DefaultComboBoxModel(new String[] {				
					FACILITY_TYPE, // �ü��� ����
					SERVER_NAME, // ����
					SERVER_INDEX, // ��� �ε���
					PROTOCOL_NUMBER, // �������� ��ȣ
					SERVER_STATE, // ��� ����
					EVENT, // ��� ����
			}));
		}
		searchFacility_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		searchFacility_ComboBox.setSelectedIndex(1);
		actualPanel.add(searchFacility_ComboBox);
		
		searchFacility_textField = new JTextField();
		searchFacility_textField.addFocusListener(Util.focusListener);
		searchFacility_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchFacility_textField.setForeground(Color.BLACK);
		searchFacility_textField.setFont(FontManager.getFont(Font.PLAIN, 16));
		searchFacility_textField.setColumns(10);
		searchFacility_textField.setBounds(272, 86, 416, 35);
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
		RcuInfo_2 += Util.colorBlue("IP : ");
		if(rcu.isDuplexedPort()) {
			RcuInfo_2 += rcu.getIp();
			RcuInfo_2 += Util.colorGreen(" & ");
			RcuInfo_2 += rcu.getAuxIP();
		}else {
			RcuInfo_2 += rcu.getIp();
		}
		RcuInfo_2 += Util.separator + Util.colorRed("/") + Util.separator;
		RcuInfo_2 += Util.colorBlue("Port : " );
		
		if(rcu.isMultiPort()) {
			ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
			MultiPortMap start = portMap.get(0);
			MultiPortMap end = portMap.get(portMap.size() - 1);
			
			RcuInfo_2 += start.getCh() + " ( " + start.getPort() + " )";
			RcuInfo_2 += Util.colorGreen(" ~ ");
			RcuInfo_2 += end.getCh() + " ( " + end.getPort() + " ) ";
		}else if(rcu.isDuplexedPort()) {			
			RcuInfo_2 += rcu.getPort();
			RcuInfo_2 += Util.colorGreen(" & ");
			RcuInfo_2 += rcu.getAuxPort();			
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
		RCUInfoLabel_1.setFont(FontManager.getFont(Font.BOLD, 18));
		RCUInfoLabel_1.setBackground(Color.WHITE);
		RCUInfoLabel_1.setBounds(81, 6, 592, 35);
		actualPanel.add(RCUInfoLabel_1);
		
		RCUInfoLabel_2 = new JLabel(RcuInfo_2);
		RCUInfoLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		RCUInfoLabel_2.setForeground(Color.BLACK);
		RCUInfoLabel_2.setFont(FontManager.getFont(Font.BOLD, 18));
		RCUInfoLabel_2.setBackground(Color.WHITE);
		RCUInfoLabel_2.setBounds(101, 41, 572, 35);
		actualPanel.add(RCUInfoLabel_2);
		
		dbRefresh_Button = new JButton("Database �ֽ�ȭ");
		dbRefresh_Button.setFocusPainted(false);
		dbRefresh_Button.setBackground(Color.WHITE);
		dbRefresh_Button.setFont(FontManager.getFont(Font.BOLD, 15));
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
		
		// ���̺� �ε�
		updateFacilityTable(FacListTable);		
		
		eventInfo_Button = new JButton();
		eventInfo_Button.setForeground(Color.BLACK);
		eventInfo_Button.setFont(FontManager.getFont(Font.BOLD, 15));
		eventInfo_Button.setFocusPainted(false);
		eventInfo_Button.setBackground(Color.WHITE);
		eventInfo_Button.setBounds(677, 6, 185, 35);
		MK119_Lite_Panel.initEventButton(eventInfo_Button, rcu);
		eventInfo_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getRCU() != null) {
					if(getRCU().hasEvent()) {
						Event.showSimpleEventInfo(getRCU());
					}
				}
			}
		});
		actualPanel.add(eventInfo_Button);

		// ESC : Close Listener
		CloseListener close = new CloseListener();
		this.addKeyListener(close);
		getContentPane().addKeyListener(close);
		searchFacility_ComboBox.addKeyListener(close);
		searchFacility_textField.addKeyListener(close);		
		FacListTable.addKeyListener(close);
		

		initCopyAdapter(rcu.getName().trim());
		
		// �������� ȭ�� ������� �����ȴ�
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	@Override
	public void dispose() {
		RcuInfoFrame.isExist = false;
		super.dispose();
	}
	
	
	// ************ DB �ֽ�ȭ *******************************************
	public void refreshDB() {
		
		MK119_Lite_Panel.resetForm(true, false);		
		
		if(MK119_Lite_Panel.serverMap.containsKey(rcu.getIndex())) {
			
			this.rcu = (RCU)MK119_Lite_Panel.serverMap.get(rcu.getIndex());
			initCopyAdapter(rcu.getName().trim());
			
			MK119_Lite_Panel.initEventButton(eventInfo_Button, rcu);
			
			String RcuInfo_1 = "<html>";
			RcuInfo_1 += Util.colorBlue("RCU : ") + rcu.getName();
			RcuInfo_1 += "&nbsp;&nbsp;";
			RcuInfo_1 += " ( " + Util.colorGreen(rcu.getRcuTypeDetail()) + " )";
			RcuInfo_1 += "</html>";
			
			String RcuInfo_2 = "<html>";
			RcuInfo_2 += Util.colorBlue("IP : ");
			if(rcu.isDuplexedPort()) {
				RcuInfo_2 += rcu.getIp();
				RcuInfo_2 += Util.colorGreen(" & ");
				RcuInfo_2 += rcu.getAuxIP();
			}else {
				RcuInfo_2 += rcu.getIp();
			}
			RcuInfo_2 += Util.separator + Util.colorRed("/") + Util.separator;
			RcuInfo_2 += Util.colorBlue("Port : " );
			
			if(rcu.isMultiPort()) {
				ArrayList<MultiPortMap> portMap = rcu.getMultiPortMapList();
				MultiPortMap start = portMap.get(0);
				MultiPortMap end = portMap.get(portMap.size() - 1);
				
				RcuInfo_2 += start.getCh() + " ( " + start.getPort() + " )";
				RcuInfo_2 += Util.colorGreen(" ~ ");
				RcuInfo_2 += end.getCh() + " ( " + end.getPort() + " ) ";
			}else if(rcu.isDuplexedPort()) {			
				RcuInfo_2 += rcu.getPort();
				RcuInfo_2 += Util.colorGreen(" & ");
				RcuInfo_2 += rcu.getAuxPort();			
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
			
			sb.append(String.format("%s%s%s\n", Util.colorRed("��������������������[ ���� RCU ���� ]��������������������"), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU ����"), rcu.getRcuTypeDetail(), separator, separator));
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU �̸�"), rcu.getName(), separator, separator));
			sb.append(String.format("%s : %d%s%s\n", Util.colorRed("RCU �ε���"), rcu.getIndex(), separator, separator));
			
			String ipInfo = "";			
			if(rcu.isDuplexedPort()) {
				ipInfo += rcu.getIp();
				ipInfo += Util.colorBlue(" & ");
				ipInfo += rcu.getAuxIP();
			}else {
				ipInfo += rcu.getIp();
			}
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU IP"), ipInfo, separator, separator));
			
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
			sb.append(String.format("%s : %s%s%s\n", Util.colorRed("RCU Port"), portInfo, separator, separator));
			sb.append(String.format("%s : %d��%s%s\n", Util.colorRed("����� ��� ����"), rcu.getFacList().size(), separator, separator));
			
			sb.append(String.format("\n�ֽ� �����ͺ��̽� ���뿡�� ���� RCU ����� " + Util.colorRed("�ε���") + " ������ ã�� �� �����ϴ�%s%s\n", Util.separator, Util.separator));
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			this.dispose();
			return;
		}
		
		searchFacility_textField.setText(null);
		searchFacility_ComboBox.setSelectedIndex(0);
		
		updateFacilityTable(FacListTable);
	}
	
	
	//*************** �ü��� ��� ���̺�  *********************************************************************************
	public void updateFacilityTable(JTable table) {		

		if (table == null || rcu == null) return;
		Object[][] content = new Object[rcu.getFacList().size()][];

		if(rcu.isMultiPort()) {
			int index = 0;
			ArrayList<MultiPortMap> portMappingList = rcu.getMultiPortMapList();
			
			for (int i = 0; i < portMappingList.size(); i++) {
				MultiPortMap map = portMappingList.get(i);							
				int facIndex = map.getFacIndex();
				
				if(MK119_Lite_Panel.serverMap.containsKey(facIndex)) {
					Facility fac = (Facility)MK119_Lite_Panel.serverMap.get(facIndex);
					content[index] = new Object[6];
					content[index][0] = index + 1;
					content[index][1] = fac.getTypeString();
					content[index][2] = fac;
					content[index][3] = (fac.getRcuPortCh() != 0 && fac.getPort() != 0) ? String.format("%d ( %d )",  map.getCh(), map.getPort()) : "Unknown"; 	
					content[index][4] = fac.getState();
					content[index][5] = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
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
					// RCU�� ������ �Ǿ������� ��Ƽ ��Ʈ ���� ���̺��� ������ ���� �ü���
					content[index] = new Object[6];
					content[index][0] = index + 1;
					content[index][1] = fac.getTypeString();
					content[index][2] = fac;
					content[index][3] = (fac.getRcuPortCh() != 0 && fac.getPort() != 0) ? String.format("%d ( %d )",  fac.getRcuPortCh(), fac.getPort()) : "Unknown"; 	
					content[index][4] = fac.getState();
					content[index][5] = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
					index++;
				}
			}
			
		}else if(rcu.isDuplexedPort()) {
			for (int i = 0; i < rcu.getFacList().size(); i++) {
				Facility fac = (Facility)rcu.getFacList().get(i);
				content[i] = new Object[6];
				content[i][0] = i + 1;
				content[i][1] = fac.getTypeString();
				content[i][2] = fac;
				content[i][3] = String.format("%d / %d",  rcu.getPort(), rcu.getAuxPort());
				content[i][4] = fac.getState();
				content[i][5] = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
			}
		}else{
			for (int i = 0; i < rcu.getFacList().size(); i++) {
				Facility fac = (Facility)rcu.getFacList().get(i);
				content[i] = new Object[6];
				content[i][0] = i + 1;
				content[i][1] = fac.getTypeString();
				content[i][2] = fac;
				content[i][3] = (fac.getPort() != 0) ? String.format("%d ( %d )",  fac.getRcuPortCh(), fac.getPort()) : "Unknown";
				content[i][4] = fac.getState();
				content[i][5] = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
			}
		}
		

		table.setModel(new DefaultTableModel(
			content,
			new String[] {
				"�� ��",
				"�ü��� ����",
				"����",
				"�� Ʈ",
				"��� ����",
				"�̺�Ʈ"
			}) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table);
	}
		
	//******************** ���̺� ��Ÿ�� ���� *********************************************************************
	public void setTableStyle(JTable table) {
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
		
		// ���� ����Ʈ ���̺�
		table.getColumnModel().getColumn(0).setPreferredWidth(25); // �� ��
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // �ü��� ����		
		table.getColumnModel().getColumn(2).setPreferredWidth(320); // ����
		table.getColumnModel().getColumn(3).setPreferredWidth(80); // ��Ʈ
		table.getColumnModel().getColumn(4).setPreferredWidth(60); // ��� ����
		table.getColumnModel().getColumn(5).setPreferredWidth(60); // �̺�Ʈ
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		FindTextRenderer findCommerRenderer = new FindTextRenderer(4, "��� ����", Color.RED);
		findCommerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
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
		
		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �� ��
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �ü��� ����
//		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // ��Ʈ
		tcmSchedule.getColumn(4).setCellRenderer(findCommerRenderer); // ��� ����
		tcmSchedule.getColumn(5).setCellRenderer(severityRenderer); // ��� ����
	}
	
	//******************** ���̺� ���͸� ���� *********************************************************************
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
				case SERVER_NAME : // ����
					searchElement = fac.getName();
					break;
				case SERVER_INDEX : // ��� �ε���
					searchElement = String.valueOf(fac.getIndex());
					break;			
				case FACILITY_TYPE : // �ü��� ����
					searchElement = fac.getTypeString();
					break;
				case PROTOCOL_NUMBER : // �������� ��ȣ
					searchElement = String.valueOf(fac.isProtocol() ? fac.getCommProtocol() : fac.getSnmpProtocol());
					break;
				case PROTOCOL_NAME : // �������� �̸�
					Protocol p = MK119_Lite_Panel.protocolMap.get(fac.getProtocolKey());
					searchElement = (p != null) ?  p.getName() : "";
					break;
				case SERVER_STATE : // ��� ����
					searchElement = fac.getState();
					break;
				case EVENT : // �̺�Ʈ
					searchElement = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
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
			content[i] = new Object[6];
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
			content[i][5] = (fac.hasEvent()) ? fac.getEvents().get(0).getSeverityName() : "";
		}

		FacListTable.setModel(new DefaultTableModel(
				content,
				new String[] { 
						"�� ��",
						"�ü��� ����",
						"����",
						"�� Ʈ",
						"��� ����",
						"�̺�Ʈ"
						}) {
				// ���̺� �� ���� ���� ����
				public boolean isCellEditable(int i, int c) {
					return false;
				}
		});

		setTableStyle(FacListTable);
	}
	
	// ����� ���� Ű �̺�Ʈ ������
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
	
	public void initCopyAdapter(String content) {
		MouseAdapter copyAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				 if (e.getButton() == 1) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // ���� Ŭ��				 
				 if (e.getButton() == 1 && e.getClickCount() == 2) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // ���� Ŭ��
				 if (e.getButton() == 3) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // ������ Ŭ��
			}
		};
		
		if(currentFunction != null) currentFunction.addMouseListener(copyAdapter);
		if(RCUInfoLabel_1 != null) RCUInfoLabel_1.addMouseListener(copyAdapter);
		if(RCUInfoLabel_2 != null) RCUInfoLabel_2.addMouseListener(copyAdapter);
	}
	
	public RCU getRCU() {
		return this.rcu;
	}
	
}
