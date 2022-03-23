package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import common.perf.FmsPerfConf;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfLabelStatusBean;
import common.perf.SnmpPerfConf;
import common.perf.SnmpPerfItem;
import src_ko.info.Protocol;
import src_ko.util.FileUtil;
import src_ko.util.Util;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class XmlEditorFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable perfListTable; // frame마다 XML 인스턴스를 가져야 하므로 table 필드는 static 속성을 가질 수 없다
 
	private File xmlFile;
	private Protocol protocol;
	private boolean isCommon;	
	private ArrayList<Perf> perfs;	
	private Perf selectedPerf;
	private String encoding = "euc-kr";
	
	private JPanel view_Panel;
	private JTextField searchPerf_textField;
	private JScrollPane perfInfoPanel;
	private JTable perfInfoTable;
	private JLabel mappingLabel;
	private JScrollPane perfLabelInfoPanel;
	private JTable perfLabelMappingTable;
	
	
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
//					XmlEditorFrame frame = new XmlEditorFrame(p.getName(), null, p);
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
	public XmlEditorFrame(String protocolName, File xmlFile, Protocol protocol) {
		this.xmlFile = xmlFile;
		this.protocol = protocol;
		this.isCommon = (protocol.getProtocolType() == Protocol.COMMON_PROTOCOL) ? true : false;
		
		if (this.isCommon) {
			this.perfs = FmsPerfConf.getFmsPerfList(xmlFile, this.encoding);
		} else {
			this.perfs = SnmpPerfConf.getSnmpPerfList(xmlFile, this.encoding);
		}
		
		XmlEditorFrame.isExist = true;
		setTitle(String.format("XML Editor : [ %s ] %s", protocol.getFacType(), protocolName));
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
		
		JLabel currentFunction = new JLabel("XML Editor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 267, 55);
		actualPanel.add(currentFunction);
		
				
		mk119Button = new JButton(new Util().getMK2Resource());
		mk119Button.setForeground(Color.BLACK);
		mk119Button.setText(" Button");
		mk119Button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		mk119Button.setFocusPainted(false);
		mk119Button.setContentAreaFilled(false);
		mk119Button.setBorder(UIManager.getBorder("Button.border"));
		mk119Button.setBackground(Color.WHITE);
		mk119Button.setBounds(850, 11, 189, 36);		
		actualPanel.add(mk119Button);
		
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
				new String[] { "필 드", "내 용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						true, // 내 용 : 수정 가능						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setPerfInfoTableStyle(perfInfoTable);
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
		
		perfLabelMappingTable = new JTable();
		perfLabelMappingTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] { "값", "매핑 내용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						true, // 내 용 : 수정 가능						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setPerfInfoTableStyle(perfLabelMappingTable);
		perfLabelInfoPanel.setViewportView(perfLabelMappingTable);
		
		JLabel searchPerf_label = new JLabel("성능 검색");
		searchPerf_label.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_label.setForeground(Color.BLACK);
		searchPerf_label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchPerf_label.setBackground(Color.WHITE);
		searchPerf_label.setBounds(15, 82, 90, 36);
		actualPanel.add(searchPerf_label);
		
		searchPerf_textField = new JTextField();
		searchPerf_textField.addFocusListener(Util.focusListener);
		searchPerf_textField.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField.setForeground(Color.BLACK);
		searchPerf_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField.setColumns(10);
		searchPerf_textField.setBounds(100, 85, 447, 35);
		searchPerf_textField.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				try {
					String text = searchPerf_textField.getText();				
					if(text == null || text.length() == 0 || text.equals("")) {
						tableReload(perfListTable);
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
						tableReload(perfListTable);
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
		tableReload(perfListTable);
				
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
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
		XmlEditorFrame.isExist = false;
		super.dispose();
	}
	
	
	
	
	//*************** 테이블 관련 기능 *********************************************************************************
	public void tableReload(JTable table) {
		updatePerfListTable(table);
	}

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
			new String[] { "순 서", "성 능"}) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});		

		setPerfListTableStyle(table);
	}
	
	
	public void setPerfListTableStyle(JTable table) {
		
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
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // 성 능		
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 성 능
	}
	
	
	public void doTableFilter(String text) {
		text = text.toUpperCase();
		ArrayList<Perf> filterPerfs = new ArrayList<Perf>();

		for(int i = 0; i < perfs.size(); i++) {
			Perf p = perfs.get(i);

			String perfName = p.getDisplayName();
			String perfIndex = String.valueOf(p.getIndex());
			
			if(perfName != null) {
				perfName = perfName.toUpperCase();
			}else {
				perfName = "";
			}

			if(text.contains(",")) {
				String[] textToken = text.split(",");
				for(int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2];
					if(perfName.contains(token) || perfIndex.contains(token)) {
						filterPerfs.add(p);
					}
				}
			}else if(perfName.contains(text) || perfIndex.contains(text)) {
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
				new String[] { "순 서", "성 능"}) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
		});

		setPerfListTableStyle(perfListTable);
	}
	
	//******************** 성능 정보 테이블 관련 *********************************************************************
	public void updatePerfInfoTable(JTable table, Perf perf) {		

		if (table == null || perf == null) return;

		Object[][] content = new Object[7][];

		content[0] = new Object[2];
		content[0][0] = "성능명";
		content[0][1] = perf.getDisplayName();
		
		content[1] = new Object[2];
		content[1][0] = "성능 카운터";
		content[1][1] = perf.getCounter();
		
		content[2] = new Object[2];
		content[2][0] = "수집 주기";
		content[2][1] = perf.getInterval();
		
		content[3] = new Object[2];
		content[3][0] = "단 위";
		content[3][1] = perf.getMeasure();
		
		content[4] = new Object[2];
		content[4][0] = "보정식";
		content[4][1] = perf.getScaleFunction();
		
		content[5] = new Object[2];
		content[5][0] = "데이터 형식";
		content[5][1] = perf.getDataFormat();
		
		content[6] = new Object[2];
		content[6][0] = "데이터 형식(상세)";
		String type = null;
		int format = perf.getDataFormat();
		if(format == 1) {
			type = "이진 상태 ( DI )";
			mappingLabel.setText("이진 상태 매핑 정보");
			perfLabelMappingTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelMappingTable, perf);
		}else if(format == 2) {
			type = "다중 상태 성능";
			mappingLabel.setText("다중 상태 매핑 정보");
			perfLabelMappingTable.setVisible(true);
			updatePerfLabelMappingTable(perfLabelMappingTable, perf);
		}else {
			type = "성능 데이터 ( Analog )";
			mappingLabel.setText("상태 매핑 정보 없음");
			perfLabelMappingTable.setVisible(false);
		}
		content[6][1] = type;

		table.setModel(new DefaultTableModel(
				content,
				new String[] { "필 드", "내 용"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 필 드 : 수정 불가
						true, // 내 용 : 수정 가능						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setPerfInfoTableStyle(table);
	}
	
	
	public void setPerfInfoTableStyle(JTable table) {
		
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
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		table.setRowHeight(30);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(3); // 필 드		
		table.getColumnModel().getColumn(1).setPreferredWidth(180); // 내 용		
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 필 드
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 내 용
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
							true, // 내 용 : 수정 가능						
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
			});
			
			setPerfLabelMappingTable(table);
		}
		
		public void setPerfLabelMappingTable(JTable table) {
			
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
			table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			table.setRowHeight(30);
			
			
			// 테이블 셀 크기 설정
			table.getColumnModel().getColumn(0).setPreferredWidth(3); // 값	
			table.getColumnModel().getColumn(1).setPreferredWidth(350); // 매핑 내용		
			
			// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
			DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

			// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
			tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 ColumnModel을 가져옴
			TableColumnModel tcmSchedule = table.getColumnModel();
			tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 값 
			tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 매핑 내용
		}
	
}
