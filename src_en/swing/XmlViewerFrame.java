package src_en.swing;

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
import java.io.File;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.perf.FmsPerfConf;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfLabelStatusBean;
import common.perf.SnmpPerfConf;
import common.perf.SnmpPerfItem;
import src_en.info.ONION_Info;
import src_en.info.Protocol;
import src_en.util.FileUtil;
import src_en.util.Util;

public class XmlViewerFrame extends JFrame {
	
	private static final String PERF_NAME = "Perf Name";
	private static final String PERF_COUNTER =  "Perf Counter";
	private static final String OID = "OID";
	private static final String INTERVAL = "Check Interval";
	private static final String UNIT = "Unit";
	private static final String SCALE = "Scale Function";
	private static final String DATA_FORMAT = "Data Format";
	
	public static final int PERF_LIST_TABLE = 0;
	public static final int PERF_INFO_TABLE = 1;
	public static final int PERF_LABEL_TABLE = 2;
	
	private String searchElement = PERF_NAME;
	
	private JTable perfListTable;
	private JTable perfInfoTable;
	private JTable perfLabelTable;
	
	public static boolean isExist = false;
	private JButton xmlPathOpenButton;
	 
	private File xmlFile;
	private Protocol protocol;
	private boolean isCommon;
	private ArrayList<Perf> perfs;
	private Perf selectedPerf;
	private String encoding = "EUC-KR";
	
	private JPanel contentPane;
	private JPanel view_Panel;
	private JLabel mappingLabel;
	private JScrollPane perfInfoPanel;
	private JScrollPane perfLabelInfoPanel;
	private JComboBox searchPerf_ComboBox;
	private JTextField searchPerf_textField;
	private JLabel protocolNameLabel;
	private JButton xmlReloadButton;
	private JButton encodingButton;
	
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
//					XmlViewerFrame frame = new XmlViewerFrame(p.getName(), null, p);
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
	public XmlViewerFrame(String protocolName, File xmlFile, Protocol protocol) {
		this.encoding = (ONION_Info.getMK119Version() >= 4.5) ? "UTF-8" : "EUC-KR";
		this.xmlFile = xmlFile;
		this.protocol = protocol;
		this.isCommon = (protocol.getProtocolType() == Protocol.COMMON_PROTOCOL) ? true : false;
		
		if (this.isCommon) {
			this.perfs = FmsPerfConf.getFmsPerfList(xmlFile, this.encoding);
		} else {
			this.perfs = SnmpPerfConf.getSnmpPerfList(xmlFile, this.encoding);
		}
		
		XmlViewerFrame.isExist = true;
		setTitle(String.format("XML Viewer : [ %s ] %s ( %s )", protocol.getFacType(), protocolName, xmlFile.getName()));
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
		
		JLabel currentFunction = new JLabel("XML Viewer");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 181, 55);
		actualPanel.add(currentFunction);
		
				
		xmlPathOpenButton = new JButton(new Util().getMK2Resource());
		xmlPathOpenButton.setText(" Open XML Path");
		xmlPathOpenButton.setForeground(Color.BLACK);
		xmlPathOpenButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		xmlPathOpenButton.setFocusPainted(false);
		xmlPathOpenButton.setContentAreaFilled(false);
		xmlPathOpenButton.setBorder(UIManager.getBorder("Button.border"));
		xmlPathOpenButton.setBackground(Color.WHITE);
		xmlPathOpenButton.setBounds(793, 9, 249, 36);		
		xmlPathOpenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File dirPath = new File(xmlFile.getParent());
					
					if (!dirPath.exists()) {
						StringBuilder msg = new StringBuilder();
						msg.append(Util.colorRed("Can not found XML Directory Path"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
	
						msg.append(String.format("XML directory not found in the following path"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
	
						msg.append("Path : " + dirPath.getPath().replace("\\", Util.colorRed("\\")));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");						
	
						Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
					}else {
						FileUtil.openFile(dirPath);
					}	
				}catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		actualPanel.add(xmlPathOpenButton);
		
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
		perfInfoPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		perfInfoPanel.setBackground(Color.WHITE);
		perfInfoPanel.setBorder(new LineBorder(Color.BLACK, 2));
		perfInfoPanel.setBounds(0, 0, 483, 240);
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
				},
				new String[] { "Element", "Content"}) {
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
		mappingLabel.setBounds(0, 252, 483, 26);
		view_Panel.add(mappingLabel);
		
		perfLabelInfoPanel = new JScrollPane();
		perfLabelInfoPanel.setBounds(0, 286, 483, 242);
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
					{null, null},
					{null, null},
				},
				new String[] { "Value", "Mapping Label"}) {
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
		
		searchPerf_ComboBox = new JComboBox();
		searchPerf_ComboBox.setForeground(Color.BLACK);
		searchPerf_ComboBox.setBackground(Color.WHITE);		
		searchPerf_ComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		searchPerf_ComboBox.setBounds(88, 52, 200, 30);
		searchPerf_ComboBox.setModel(new DefaultComboBoxModel(new String[] {PERF_NAME,(this.isCommon) ? PERF_COUNTER : OID, INTERVAL, UNIT, SCALE, DATA_FORMAT}));
		searchPerf_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String text = searchPerf_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						updatePerfListTable(perfListTable);
					}else {					
						doTableFilter(text);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(searchPerf_ComboBox);
		
		JLabel searchPerf_label = new JLabel("Search");
		searchPerf_label.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_label.setForeground(Color.BLACK);
		searchPerf_label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchPerf_label.setBackground(Color.WHITE);
		searchPerf_label.setBounds(23, 84, 72, 36);
		actualPanel.add(searchPerf_label);
		
		searchPerf_textField = new JTextField();
		searchPerf_textField.addFocusListener(Util.focusListener);
		searchPerf_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField.setForeground(Color.BLACK);
		searchPerf_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField.setColumns(10);
		searchPerf_textField.setBounds(88, 87, 459, 35);
		searchPerf_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					String text = searchPerf_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						updatePerfListTable(perfListTable);
					}else {					
						doTableFilter(text);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					String text = searchPerf_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						updatePerfListTable(perfListTable);
					}else {					
						doTableFilter(text);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(searchPerf_textField);				
		
		// 테이블 로드
		updatePerfListTable(perfListTable);
		
		protocolNameLabel = new JLabel(protocolName);
		protocolNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		protocolNameLabel.setForeground(Color.BLUE);
		protocolNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		protocolNameLabel.setBackground(Color.WHITE);
		protocolNameLabel.setBounds(193, 10, 596, 35);
		actualPanel.add(protocolNameLabel);
		
		JButton xmlOpenButton = new JButton("Open XML File");
		xmlOpenButton.setForeground(Color.BLACK);
		xmlOpenButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		xmlOpenButton.setFocusPainted(false);
		xmlOpenButton.setContentAreaFilled(false);
		xmlOpenButton.setBorder(UIManager.getBorder("Button.border"));
		xmlOpenButton.setBackground(Color.WHITE);
		xmlOpenButton.setBounds(559, 87, 160, 35);
		xmlOpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!xmlFile.exists()) {
						StringBuilder msg = new StringBuilder();
						msg.append(Util.colorRed("XML file not found"));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
	
						msg.append("XML File not found in the following path");
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
	
						msg.append("XML File Path : " + xmlFile.getParent().replace("\\", Util.colorRed("\\")));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n\n");
						msg.append("XML File Name : " + Util.colorRed(xmlFile.getName()));
						msg.append(String.format("%s%s", Util.separator, Util.separator));
						msg.append("\n");
	
						Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
					}else {
						FileUtil.editFile(xmlFile);
					}	
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(xmlOpenButton);
		
		xmlReloadButton = new JButton("XML Reload");
		xmlReloadButton.setForeground(Color.BLACK);
		xmlReloadButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		xmlReloadButton.setFocusPainted(false);
		xmlReloadButton.setContentAreaFilled(false);
		xmlReloadButton.setBorder(UIManager.getBorder("Button.border"));
		xmlReloadButton.setBackground(Color.WHITE);
		xmlReloadButton.setBounds(725, 87, 160, 35);
		xmlReloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshXML();					
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(xmlReloadButton);
		
		encodingButton = new JButton(this.encoding);
		encodingButton.setForeground(new Color(0, 128, 0));
		encodingButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		encodingButton.setFocusPainted(false);
		encodingButton.setContentAreaFilled(false);
		encodingButton.setBorder(UIManager.getBorder("Button.border"));
		encodingButton.setBackground(Color.WHITE);
		encodingButton.setBounds(936, 87, 106, 35);
		encodingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentEncoding = encodingButton.getText();
				
				if(currentEncoding.equalsIgnoreCase("EUC-KR")) {
					encodingButton.setText("UTF-8");
					setEncoding("UTF-8");
					refreshXML();
				}else if(currentEncoding.equalsIgnoreCase("UTF-8")){
					encodingButton.setText("EUC-KR");
					setEncoding("EUC-KR");
					refreshXML();
				}
				
			}
		});
		actualPanel.add(encodingButton);
				
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setXmlSource(File xmlFile, JTextArea textArea) {
		String xmlContent = null;
		try {
			xmlContent = FileUtil.getFileContent(xmlFile, this.encoding);
		}catch(Exception e) {
			e.printStackTrace();
		}
		textArea.setText(xmlContent);
	}
	
	
	@Override
	public void dispose() {
		XmlViewerFrame.isExist = false;
		super.dispose();
	}
	
	
	// ************ XML Reload *******************************************
	public void refreshXML() {
		if (this.isCommon) {
			this.perfs = FmsPerfConf.getFmsPerfList(xmlFile, this.encoding);
		} else {
			this.perfs = SnmpPerfConf.getSnmpPerfList(xmlFile, this.encoding);
		}		
	
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
				},
				new String[] { "Element", "Content"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						false, // 내 용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		perfLabelTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] { "Value", "Mapping Label"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 값 : 수정 불가
						false, // 매핑 내용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		searchPerf_textField.setText(null);
		mappingLabel.setText(null);
	}
	
	public void setFocusCell(JTable table, int row, int column) {
		table.changeSelection(row, column, false, false);				
		table.requestFocus();
	}
	
	
	//*************** 성능 리스트 테이블  *********************************************************************************
	public void updatePerfListTable(JTable table) {		

		if (table == null || perfs == null) return;

		Object[][] content = new Object[perfs.size()][];

		for (int i = 0; i < perfs.size(); i++) {
			Perf perf = (this.isCommon) ? (FmsPerfItem) perfs.get(i) : (SnmpPerfItem) perfs.get(i);
			perf.setIndex(i + 1);
			
			content[i] = new Object[2];
			content[i][0] = perf.getIndex(); // 순 서
			content[i][1] = perf;
		}

		table.setModel(new DefaultTableModel(
			content,
			new String[] { "Index", "Watch Point ( Performance )"}) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(table, PERF_LIST_TABLE);
	}
	
	public void doTableFilter(String text) {
		text = text.toUpperCase();
		ArrayList<Perf> filterPerfs = new ArrayList<Perf>();

		for(int i = 0; i < perfs.size(); i++) {
			Perf p = perfs.get(i);

			String searchElement = null;
			
			switch(searchPerf_ComboBox.getSelectedItem().toString()) {
				case PERF_NAME :
					searchElement = p.getDisplayName();
					break;
				case PERF_COUNTER :
				case OID :
					searchElement = p.getCounter();
					break;
				case INTERVAL :
					searchElement = String.valueOf(p.getInterval());
					break;
				case UNIT :
					searchElement = p.getMeasure();
					break;
				case SCALE :
					searchElement = p.getScaleFunction();
					break;
				case DATA_FORMAT :
					searchElement = String.valueOf(p.getDataFormat());
					break;
				default : 
					searchElement = p.getDisplayName();
					break;
			}
			
			if(searchElement != null) {
				searchElement = searchElement.toUpperCase();
			}else {
				searchElement = "";
			}

			if(text.contains(",")) {
				String[] textToken = text.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2];
					if(searchElement.contains(token)) {
						filterPerfs.add(p);
					}
				}
			}else if(searchElement.contains(text)) {
				filterPerfs.add(p);
			}
			
		}// end for-loof
		
		Object[][] content = new Object[filterPerfs.size()][];

		for (int i = 0; i < filterPerfs.size(); i++) {
			Perf perf = filterPerfs.get(i);
			content[i] = new Object[2];
			content[i][0] = perf.getIndex();
			content[i][1] = perf;
		}

		perfListTable.setModel(new DefaultTableModel(
				content,
				new String[] { "Index", "Watch Point ( Performance )"}) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
		});

		setTableStyle(perfListTable, PERF_LIST_TABLE);
	}
	
	//******************** 성능 정보 테이블 관련 *********************************************************************
	public void updatePerfInfoTable(JTable table, Perf perf) {		

		if (table == null || perf == null) return;
		
		Object[][] content = new Object[7][];

		content[0] = new Object[2];
		content[0][0] = "Perf Name";
		content[0][1] = perf.getDisplayName();
		
		content[1] = new Object[2];
		content[1][0] = (this.isCommon) ? "Perf Counter" : "OID";
		content[1][1] = perf.getCounter();
		
		content[2] = new Object[2];
		content[2][0] = "Check Interval";
		content[2][1] = perf.getInterval();
		
		content[3] = new Object[2];
		content[3][0] = "Unit";
		content[3][1] = perf.getMeasure();
		
		content[4] = new Object[2];
		content[4][0] = "Scale Function";
		content[4][1] = perf.getScaleFunction();
		
		content[5] = new Object[2];
		content[5][0] = "Data Format";
		content[5][1] = perf.getDataFormat();
		
		content[6] = new Object[2];
		content[6][0] = "Perf Type";
		String type = null;
		
		int format = perf.getDataFormat();
		
		if(format == 1) {
			type = "Binary State ( DI )";
			mappingLabel.setText("Binary State Mapping Content");
			perfLabelTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelTable, perf);
		}else if(format == 2) {
			type = "Multiple State";
			mappingLabel.setText("Multiple State Mapping Content");
			perfLabelTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelTable, perf);
		}else {
			type = "Value ( Analog )";
			mappingLabel.setText("No State Mapping");
			perfLabelTable.setVisible(false);
		}
		content[6][1] = type;

		table.setModel(new DefaultTableModel(
				content,
				new String[] { "Element", "Content"}) {
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
					new String[] { "Value", "Mapping Label"}) {
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
			table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
			
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
//				tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
			}
		}
}
