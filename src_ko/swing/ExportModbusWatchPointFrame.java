package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
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

import common.agent.PerfData;
import common.modbus.ModbusCellRenderer;
import common.modbus.ModbusWatchPoint;
import common.util.JavaScript;
import src_ko.util.Util;

public class ExportModbusWatchPointFrame extends JFrame {

	public static ModbusWatchPoint selectedPoint = null;
	public static ArrayList<ModbusWatchPoint> pointList;
	public static JTable pointTable;	
	
	public static boolean isExist = false;
	
	private JPanel contentPane;
	private JPanel actualPanel;
	
	public static JTextField search_textField;	
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
		addrTypeComboBox.setSelectedIndex(1);
	}
	
	private JPanel cardPanel;
	private static CardLayout cardLayout;
	private Rectangle r = new Rectangle(100, 100, 1080, 720);
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExportModbusWatchPointFrame frame = new ExportModbusWatchPointFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ExportModbusWatchPointFrame() {
		ExportModbusWatchPointFrame.isExist = true;		
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
		
		table_scrollPane = new JScrollPane();
		table_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		table_scrollPane.setBounds(0, 107, 1044, 554);
		actualPanel.add(table_scrollPane);
		
		pointTable = new JTable();
		pointTable.setRowSelectionAllowed(false);
		pointTable.setCellSelectionEnabled(true);
		pointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ★ 내가 그토록 찾던 기능
		pointTable.setCellSelectionEnabled(true);
		pointTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) {
				
			}
						
			public void keyReleased(KeyEvent e) {
				
			}
		});
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
		addrTypeComboBox.setSelectedIndex(1);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(515, 68, 150, 30);
		actualPanel.add(addrTypeComboBox);
		
		
		search_textField = new JTextField();
		search_textField.setHorizontalAlignment(SwingConstants.LEFT);
		search_textField.setForeground(Color.BLACK);
		search_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 17));
		search_textField.setColumns(10);
		search_textField.setBorder(new LineBorder(Color.BLACK, 2));
		search_textField.setBackground(Color.WHITE);
		search_textField.setBounds(72, 68, 437, 32);
		search_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					if(pointList != null && (pointList.size() != pointTable.getRowCount())) {
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
					}
					
					setTableStyle(pointTable, search_textField.getText());
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent e) {
				try {
					if(pointList != null && (pointList.size() != pointTable.getRowCount())) {
						resetTable(pointTable, null);
						addRecord(pointTable, pointList);
					}
					
					String formula = search_textField.getText().toLowerCase();
					
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(formula.contains("only")) {
							formula = formula.replace("only", "");
							if(!formula.contains("x")) {
								try {
									int value = Integer.parseInt(formula.trim());
									formula = ("x == " + formula);
								}catch(Exception exception) {
									// do nothing
								}
							}
							onlyValueFormulaPoint(formula);
						}
						
					}else {
						setTableStyle(pointTable, formula);	
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(search_textField);
		
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
				"다중 상태",
				"마지막 내용",
				"마지막 데이터"
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
						false,
						false,
						false
						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(table, null);
	}
	
	public static void setTableStyle(JTable table, String valueFormula) {
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
		table.getColumnModel().getColumn(1).setPreferredWidth(350); // 모드버스 포인트
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(130); // 주 소
		table.getColumnModel().getColumn(4).setPreferredWidth(280); // 데이터 타입
		table.getColumnModel().getColumn(5).setPreferredWidth(80); // 단 위
		table.getColumnModel().getColumn(6).setPreferredWidth(130); // 보정식
		table.getColumnModel().getColumn(7).setPreferredWidth(120); // 이진 상태 : 0
		table.getColumnModel().getColumn(8).setPreferredWidth(120); // 이진 상태 : 1
		table.getColumnModel().getColumn(9).setPreferredWidth(300); // 다중 상태
		table.getColumnModel().getColumn(10).setPreferredWidth(150); // 마지막 내용
		table.getColumnModel().getColumn(11).setPreferredWidth(150); // 마지막 데이터
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		// 값
		DefaultTableCellRenderer valueCellRenderer = null;
		if(valueFormula == null || valueFormula.length() == 0 || valueFormula.equalsIgnoreCase("")) {
			valueCellRenderer = new DefaultTableCellRenderer();
			
		}else {
			if(!valueFormula.toLowerCase().contains("x")) {
				try {
					double value = Double.parseDouble(valueFormula.trim());
					valueFormula = ("x == " + valueFormula);
				}catch(Exception e) {
					// do nothing
				}
			}
			
			valueCellRenderer = new ModbusCellRenderer(valueFormula, "value", pointList);
		}
		valueCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 기능코드
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 주소
		tcmSchedule.getColumn(3).setCellRenderer(valueCellRenderer); // 값		
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
				/* column[0] */ record.add(point.getIndex()); // 순 서
				/* column[1] */ record.add(point.getFunctionCode()); // 기능코드
				
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
				
				/* column[2] */ record.add(addr);  // 주소
				/* column[3] */ record.add(PerfData.getPerfPureValue(point.getData())); // 결 과
				
				model.addRow(record);
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void updateTable(JTable table) {
		
		if(pointList == null || pointList.size() < 1) {
			resetTable(table, null);
			return;
		}
		
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		
		Object[][] content = new Object[rowCount][];
		
		for(int i = 0; i < rowCount; i++) {
			content[i] = new Object[columnCount];
			
			int index = Integer.parseInt(pointTable.getValueAt(i, 0).toString());
			ModbusWatchPoint point = pointList.get(index-1);
			
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
			
			content[i][0] = table.getValueAt(i, 0);
			content[i][1] = point.getFunctionCode();
			content[i][2] = addr;
			content[i][3] = PerfData.getPerfPureValue(point.getData());			
		}
		
		resetTable(table, content);
		
		
		try {
			String formula = search_textField.getText().toLowerCase();
			
			if(formula.contains("only")) {
				formula = formula.replace("only", "");
				if(!formula.contains("x")) {
					try {
						int value = Integer.parseInt(formula.trim());
						formula = ("x == " + formula);
					}catch(Exception exception) {
						// do nothing
					}
				}
				onlyValueFormulaPoint(formula);
				
			}else {
				setTableStyle(pointTable, formula);	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void onlyValueFormulaPoint(String formula) {
    	ArrayList<ModbusWatchPoint> findPointList = new ArrayList<ModbusWatchPoint>();
		
		for(ModbusWatchPoint p : pointList) {
			String pureData = p.getData().getPureValue().toString();
			if(!pureData.equalsIgnoreCase("none")) {
				try {
					boolean validFormula =  (boolean)JavaScript.eval(formula, pureData);
					if(validFormula) findPointList.add(p);
				}catch(Exception exp) {
					// Do Nothing
				}
			}
		}
		
		resetTable(pointTable, null);
		addRecord(pointTable, findPointList);
		setTableStyle(pointTable, formula);
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
}