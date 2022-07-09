package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.modbus.ModbusWatchPoint;
import common.perf.PerfLabelStatusBean;
import src_ko.util.Util;

public class ExportModbusWatchPointFrame extends JFrame {

	public static ModbusWatchPoint selectedPoint = null;
	public static ArrayList<ModbusWatchPoint> pointList;
	public static JTable pointTable;
	
	public static boolean isExist = false;
	
	private JPanel contentPane;
	private JPanel actualPanel;
	
	private JRadioButton mk_V4_RaidoButton;
	private JRadioButton mk_V10_RaidoButton;
	
	public static JTextField search_TextField;
	private JScrollPane table_scrollPane = new JScrollPane();	
	
	public static JComboBox addrTypeComboBox; // 주소 형식 콤보박스
	static {
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(0);
	}
	
	private JPanel cardPanel;
	private static CardLayout cardLayout;
	private Rectangle r = new Rectangle(100, 100, 1080, 720);
	private Color mkColor = new Color(237, 76, 55);
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ExportModbusWatchPointFrame frame = new ExportModbusWatchPointFrame();
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
	public ExportModbusWatchPointFrame() {
		ExportModbusWatchPointFrame.isExist = true;		
		ExportModbusWatchPointFrame.pointList = ModbusMonitor_Panel.pointList;
		
		setTitle("Modbus Monitor");
		setMinimumSize(new Dimension(r.width, r.height));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new Util().getIconResource().getImage());
		setResizable(true);
				
		setBounds(100, 100, 1080, 720);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY, 10));
		contentPane.setLayout(new BorderLayout(0, 0));		
		setContentPane(contentPane);
		
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
    			addComponentListener(new ComponentAdapter() {
    				@Override
    				public void componentResized(ComponentEvent e) {
    					table_scrollPane.setSize(contentPane.getWidth() - (table_scrollPane.getX() + 20), contentPane.getHeight() - (table_scrollPane.getY() + 20));    					
    					super.componentResized(e);    					
    				}
    			});
            }
        });
		
		cardPanel = new JPanel();
		cardLayout = new CardLayout(0, 0);
		cardPanel.setLayout(cardLayout);	
		contentPane.add(cardPanel, BorderLayout.CENTER);
		
		actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setLayout(null);
		actualPanel.setBackground(Color.WHITE);
		cardPanel.add(actualPanel, "actualPanel");
		
		JLabel currentFunction = new JLabel("Export Modbus Point");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 350, 55);
		actualPanel.add(currentFunction);
		
		JLabel searchLabel = new JLabel("검 색");
		searchLabel.setBackground(Color.WHITE);
		searchLabel.setForeground(Color.BLACK);
		searchLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		searchLabel.setBounds(18, 66, 50, 32);
		actualPanel.add(searchLabel);
		
		table_scrollPane = new JScrollPane();
		table_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		table_scrollPane.setBounds(0, 107, 1044, 554);
		actualPanel.add(table_scrollPane);
		
		pointTable = new JTable();
		pointTable.setRowSelectionAllowed(false);
		pointTable.setCellSelectionEnabled(true);
		pointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ★ 내가 그토록 찾던 기능
		pointTable.setCellSelectionEnabled(true);		
		pointTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					// 왼쪽 클릭
				} 
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					ModbusWatchPoint.showInfo(point);
				}
			}
		});
		resetTable(pointTable, null);
		table_scrollPane.setViewportView(pointTable);
		
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(0);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(397, 30, 150, 32);
		addrTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
			}
		});
		actualPanel.add(addrTypeComboBox);
		
		search_TextField = new JTextField();
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setForeground(Color.BLACK);
		search_TextField.setFont(new Font("맑은 고딕", Font.PLAIN, 17));
		search_TextField.setColumns(10);
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.setBackground(Color.WHITE);
		search_TextField.setBounds(72, 68, 475, 32);
		search_TextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					doTableFilter(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent e) {
				try {
					
					boolean enter = (e.getKeyCode() == KeyEvent.VK_ENTER);
					doTableFilter(enter);
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(search_TextField);
		
		JButton exportButton = new JButton(" Export");
		exportButton.setIcon(new Util().getExcelImage());
		exportButton.setForeground(Color.BLACK);
		exportButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		exportButton.setFocusPainted(false);
		exportButton.setBackground(Color.WHITE);
		exportButton.setBounds(841, 8, 196, 66);
		actualPanel.add(exportButton);
		
		mk_V4_RaidoButton = new JRadioButton("MK119  V4");
		mk_V4_RaidoButton.setSelected(true);
		mk_V4_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V4_RaidoButton.setForeground(new Color(237, 76, 55));
		mk_V4_RaidoButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		mk_V4_RaidoButton.setFocusPainted(false);
		mk_V4_RaidoButton.setBackground(Color.WHITE);
		mk_V4_RaidoButton.setBounds(686, 12, 151, 23);
		actualPanel.add(mk_V4_RaidoButton);
		
		mk_V10_RaidoButton = new JRadioButton("MK119  V10");
		mk_V10_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
		mk_V10_RaidoButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		mk_V10_RaidoButton.setFocusPainted(false);
		mk_V10_RaidoButton.setBackground(Color.WHITE);
		mk_V10_RaidoButton.setBounds(686, 45, 151, 23);
		actualPanel.add(mk_V10_RaidoButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(mk_V4_RaidoButton);
		group.add(mk_V10_RaidoButton);
		
		ActionListener mkVerionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mk_V4_RaidoButton.isSelected()) {
					mk_V4_RaidoButton.setForeground(mkColor);
					mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
				}else {
					mk_V4_RaidoButton.setForeground(Color.LIGHT_GRAY);
					mk_V10_RaidoButton.setForeground(mkColor);
				}
			}
		};
		mk_V4_RaidoButton.addActionListener(mkVerionListener);
		mk_V10_RaidoButton.addActionListener(mkVerionListener);
		
		tableDataInit();
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ExportModbusWatchPointFrame.isExist = false;
		super.dispose();
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Export Modbus Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Export Modbus Point 프레임이 이미 열려있습니다" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public void tableResize(JTable table) {
		int width = contentPane.getWidth() - (table_scrollPane.getX() + 20);
		
	}
	
	public static void resetTable(JTable table, Object[][] content){
		String[] header = new String[] {
				"순 서",
				"모드버스 포인트",
				"기능코드",
				"주 소",
				"데이터 타입",
				"단 위",
				"보정식",
				"이진 상태 : 0",
				"이진 상태 : 1",
				"다중 상태"
		};
		
		table.setModel(new DefaultTableModel(content, header) {
				boolean[] columnEditables = new boolean[] {
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(pointTable);
	}
	
	public static void setTableStyle(JTable table) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setForeground(Color.BLACK);		
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 헤더 width 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(70); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // 모드버스 포인트
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(130); // 주 소
		table.getColumnModel().getColumn(4).setPreferredWidth(300); // 데이터 타입
		table.getColumnModel().getColumn(5).setPreferredWidth(120); // 단 위
		table.getColumnModel().getColumn(6).setPreferredWidth(145); // 보정식
		table.getColumnModel().getColumn(7).setPreferredWidth(150); // 이진 상태 : 0
		table.getColumnModel().getColumn(8).setPreferredWidth(150); // 이진 상태 : 1
		table.getColumnModel().getColumn(9).setPreferredWidth(800); // 다중 상태
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 모드버스 포인트
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 기능코드
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 주 소
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 데이터 타입
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 단 위
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // 보정식
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 0
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 1
//		tcmSchedule.getColumn(9).setCellRenderer(tScheduleCellRenderer); // 다중 상태
	}
	
	public static void tableDataInit() {
		addRecord(pointTable, ModbusMonitor_Panel.pointList);
	}
	
	/**
	 * 	레코드 추가
	 */
	public static void addRecord(JTable table, ArrayList<ModbusWatchPoint> pointList) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			// 기능코드, 주소, 보정식 순서로 정렬
			Collections.sort(pointList);
			
			for(int i = 0; i < pointList.size(); i++) {
				
				ModbusWatchPoint point = pointList.get(i);
				record = new Vector();
				
				int index = 0;
				if(table.getRowCount() <= 0) {
					// 테이블의 행 개수가 0개 일 경우 : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				Object addr = null;
				switch(addrTypeComboBox.getSelectedItem().toString()) {
					case "Modbus (DEC)" :
						addr = point.getModbusAddrString();
						break;
					case "Register (DEC)" :
						addr = point.getRegisterAddr();
						break;
					case "Register (HEX)" :
						addr = point.getRegisterAddrHexString();
						break;
					default : 
						addr = point.getModbusAddrString();
						break;
				}
				
				PerfLabelStatusBean[] labels =  point.getStatusLabels();
				String multiLabel = "";
				if(labels != null) {
					for(PerfLabelStatusBean label : labels) {
						multiLabel += label.value + "; " + label.label + "; ";
					}
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // 순 서 
				/* column[1] */ record.add(point); // 모드버스 포인트
				/* column[2] */ record.add(point.getFunctionCode()); // 기능코드
				/* column[3] */ record.add(addr);  // 주소
				/* column[4] */ record.add(point.getDataType());  // 데이터 타입
				/* column[5] */ record.add(point.getMeasure());  // 단위
				/* column[6] */ record.add(point.getScaleFunction());  // 보정식
				/* column[7] */ record.add(point.getBinLabel()[0]);  // 이진 상태 : 0
				/* column[8] */ record.add(point.getBinLabel()[1]);  // 이진 상태 : 1
				/* column[9] */ record.add(multiLabel);  // 다중 상태		
				model.addRow(record);
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void updateTable() {
		resetTable(pointTable, null);
		addRecord(pointTable, ModbusMonitor_Panel.pointList);
	}
	
	public static ArrayList<ModbusWatchPoint> getSelectedPointList(){
		try {
			
			ArrayList<ModbusWatchPoint> selectedList = null;
			
			if(pointList != null
				&& pointList.size() >= 1
				&& pointTable.getRowCount() >= 1
				&& pointTable.getSelectedRows() != null
				&& pointTable.getSelectedRows().length >= 1) {
				
				selectedList = new ArrayList<ModbusWatchPoint>();
				
				for(int row : pointTable.getSelectedRows()) {
					int index = Integer.parseInt(pointTable.getValueAt(row, 0).toString());
					// 새로운 포인트를 생성해서 Copy 하는 이유는 새로 생성한 포인트 객체의 주소를 참조하기 위해서임
					ModbusWatchPoint point = new ModbusWatchPoint();
					ModbusWatchPoint selectedPoint = pointList.get(index-1);
					point.copy(selectedPoint);
					selectedList.add(point);
				}
			}
			
			return selectedList;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	// 테이블 기능 관련
	public static void doTableFilter(boolean clickedEnter) {
		if(search_TextField == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		if(noSearch) {
			resetTable(pointTable, null);
			addRecord(pointTable, pointList);
			return;
		}
		if(!noSearch) {
			text = text.toLowerCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toLowerCase();
				
				if(text.contains(",")) {
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						if(searchElement.contains(token)) {
							isContain = true;
						}
					}
				}else{
					if (searchElement.contains(text)) {
						isContain = true;
					}
				}		
			}else {
				isContain = true;
			}
			
			if(isContain) {
				filterList.add(modbusWp);
			}
			
		}// for loop
		
		resetTable(pointTable, null);
		addRecord(pointTable, filterList);
	}
}