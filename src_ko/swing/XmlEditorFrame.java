package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
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
import common.perf.SnmpPerfConf;
import common.perf.SnmpPerfItem;
import src_ko.info.Protocol;
import src_ko.util.Util;

public class XmlEditorFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable table; // frame마다 XML 인스턴스를 가져야 하므로 table 필드는 static 속성을 가질 수 없다
 
	private File xmlFile;
	private Protocol protocol;
	private boolean isCommon;	
	private ArrayList<Perf> perfs;	
	private Perf selectedPerf;
	private String encoding = "euc-kr";
	
	private static CardLayout cardLayout;
	private JTextField searchPerf_textField;
	
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
		
		table = new JTable();		
		table.setForeground(Color.BLACK);
		table.addFocusListener(new FocusListener() {			
			public void focusLost(FocusEvent e) { /* Not Implement */ }			
			public void focusGained(FocusEvent e) { /* Not Implement */ }
		});
		table.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) { /* Not Implement */ }						
			public void keyReleased(KeyEvent e) { /* Not Implement */ }
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { /* Not Implement */ } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) { /* Not Implement */ }
				if (e.getButton() == 3) { /* Not Implement */ }
			}
		});
		perfList_scrollPane.setViewportView(table);
		
		JPanel view_panel = new JPanel();
		view_panel.setBorder(new LineBorder(Color.BLACK, 2));
		view_panel.setBounds(559, 128, 483, 530);
		actualPanel.add(view_panel);
		
		cardLayout = new CardLayout(0, 0);
		view_panel.setLayout(cardLayout);
//		view_panel.add(null, "source");
//		view_panel.add(null, "form");
		
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
						tableReload(table);
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
						tableReload(table);
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
		tableReload(table);
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		XmlEditorFrame.isExist = false;
		super.dispose();
	}
	
	
	
	//*************** 테이블 관련 기능 *********************************************************************************
	public void tableReload(JTable table) {
		updatePerfTable(table);
	}

	public void updatePerfTable(JTable table) {		

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

		setTableStyle(table);
	}
	
	
	public void setTableStyle(JTable table) {
		
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

		table.setModel(new DefaultTableModel(
				content,
				new String[] { "순 서", "성 능"}) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
		});

		setTableStyle(table);
	}
	
}
