package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import src_en.agent.HttpAgent;
import src_en.agent.ModbusFacility;
import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.info.AdminConsole_Info;
import src_en.util.ExcelAdapter;
import src_en.util.Inspecter;
import src_en.util.JavaScript;
import src_en.util.MessageUtil_en;
import src_en.util.Util;
import src_en.util.XmlGenerator;

public class CustomModbusXmlGeneratorFrame extends JFrame {
	
	private static final int ORDER = 0;
	private static final int PERF_NAME = 1;
	private static final int FUNCTION_CODE = 2;
	private static final int REGISTER_ADDRESS = 3;
	private static final int MODBUS_ADDRESS = 4;
	private static final int PERF_COUNTER = 5;
	private static final int INTERVAL = 6;
	private static final int MEASURE = 7;
	private static final int SCALE_FUNCTION = 8;
	private static final int LAST_VALUE = 9;
	private static final int LABLE_0 = 10;
	private static final int LABLE_1 = 11;
	private static final int LABLE_STATUS = 12;

	private static final String[] COLUMN_NAME = {
			"Index", // 0 : 순서 
			"Performance Name", // 1 : 성능명
			"Function Code", // 2 : 기능코드
			"Register Address", // 3 : 레지스터 주소
			"Modbus Address", // 4 : 모드버스 주소
			"Perf Counter", // 5 : 성능 카운터
			"Interval", // 6 : 수집 주기
			"Units", // 7 : 단위
			"Scale", // 8 : 보정식
			"Last Value", // 9 : 마지막 수집 값
			"Label : 0", // 10 : 이진 0
			"Label : 1", // 11 : 이진 1
			"Multiple States" // 12 : 다중 상태
	};
	
	// XML Convertiong 기능 세팅
	private static JRadioButton registerAddress_radiobutton;
	private static JRadioButton modbusAddress_radiobutton;
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// 스레드 수행 여부
	public static boolean isConverting = false;
	public static boolean isInspecting = false;
	
	private JPanel contentPane;
	private static JButton addModbusPerf_Button;
	public static boolean isExist = false;
	private static List modbusPerfList;
	private static JTable table;	
	private static boolean perfCheckOk = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomModbusXmlGeneratorFrame frame = new CustomModbusXmlGeneratorFrame();
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
	public CustomModbusXmlGeneratorFrame() {
		CustomModbusXmlGeneratorFrame.isExist = true;
		modbusPerfList = new ArrayList();
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("ModbusAnalyzer : Custom Modbus XML Generator");
		
		// 클래스 로더를 이용한 이미지 로딩
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// 프로젝트 Build Path에 이미지 리소스 디렉토리를 포함시켜야 한다.		
		setIconImage(new Util().getIconResource().getImage());		
				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		ImageIcon image = new Util().getIconResource();
		actualPanel.setLayout(null);
		
		JLabel modbusXmlGenerator = new JLabel("Custom Modbus XML Generator");
		modbusXmlGenerator.setForeground(Color.BLACK);
		modbusXmlGenerator.setIcon(new Util().getSubLogoResource());
		modbusXmlGenerator.setHorizontalAlignment(SwingConstants.LEFT);
		modbusXmlGenerator.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		modbusXmlGenerator.setBackground(Color.WHITE);
		modbusXmlGenerator.setBounds(0, 0, 415, 55);
		actualPanel.add(modbusXmlGenerator);			
		
		addModbusPerf_Button = new JButton(new Util().getMK2Resource());
		addModbusPerf_Button.setForeground(new Color(0, 128, 0));
		addModbusPerf_Button.setText(" XML Generate");
		addModbusPerf_Button.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		addModbusPerf_Button.setFocusPainted(false);
		addModbusPerf_Button.setContentAreaFilled(false);
		addModbusPerf_Button.setBorder(UIManager.getBorder("Button.border"));
		addModbusPerf_Button.setBackground(Color.WHITE);
		addModbusPerf_Button.setBounds(782, 11, 257, 36);
		addModbusPerf_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				if(isInspecting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
//					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 XML Convertiong 기능을 사용 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					sortingAndValidation();
					return;
				}
				
				if(isConverting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
//					sb.append(String.format("이미 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\nXML Convertiong 스레드는 중복으로 실행 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					converting();
					return;
				}
				
				convertCustomModbusXML();
			}
		});
		actualPanel.add(addModbusPerf_Button);
		
		JPanel collectionPanel = new JPanel();
		collectionPanel.setBackground(new Color(255, 255, 255));
		collectionPanel.setBounds(12, 56, 1030, 481);
		actualPanel.add(collectionPanel);
		collectionPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.control);
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		collectionPanel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ★ 내가 그토록 찾던 기능
		table.setBackground(Color.WHITE);		
		table.addKeyListener(new KeyAdapter() {				
			// 셀 내용  삭제
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
					int[] selectedRows = table.getSelectedRows();
					int[] selectedColumns = table.getSelectedColumns();
					
					for(int row = 0; row < selectedRows.length; row++) {
						for(int column = 0; column < selectedColumns.length; column++) {			
					
							// 사용자가 수정 할 수 있는 셀 내용만 삭제
							if(table.isCellEditable(selectedRows[row], selectedColumns[column])) {
								table.setValueAt("", selectedRows[row], selectedColumns[column]);	
							}
						}
					}					
				}							
			}
		});
		ExcelAdapter ex = new ExcelAdapter(table); // 여러 열 복사 붙여넣기 가능 
		scrollPane.setViewportView(table);
		resetTable(table);
		
		JPanel formPanel = new JPanel();
		formPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		formPanel.setBackground(Color.WHITE);
		formPanel.setBounds(12, 547, 1030, 109);		
		actualPanel.add(formPanel);
		formPanel.setLayout(null);
		
		JLabel addressInfo = new JLabel("Address Type");
		addressInfo.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		addressInfo.setBounds(14, 10, 130, 30);
		formPanel.add(addressInfo);
		
		registerAddress_radiobutton = new JRadioButton("Register Addr");
		registerAddress_radiobutton.setBackground(Color.WHITE);
		registerAddress_radiobutton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		registerAddress_radiobutton.setForeground(Color.BLACK);
		registerAddress_radiobutton.setBounds(20, 48, 120, 23);
		registerAddress_radiobutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeAddressType();
			}
		});
		formPanel.add(registerAddress_radiobutton);
		
		modbusAddress_radiobutton = new JRadioButton("Modbus Addr");
		modbusAddress_radiobutton.setBackground(Color.WHITE);
		modbusAddress_radiobutton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		modbusAddress_radiobutton.setForeground(Color.BLACK);
		modbusAddress_radiobutton.setBounds(20, 77, 120, 23);
		modbusAddress_radiobutton.setSelected(true);
		modbusAddress_radiobutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeAddressType();				
			}
		});
		formPanel.add(modbusAddress_radiobutton);
		
		ButtonGroup AddressTypeGroup= new ButtonGroup();
		AddressTypeGroup.add(registerAddress_radiobutton);
		AddressTypeGroup.add(modbusAddress_radiobutton);
		
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBounds(166, 2, 2, 105);
		formPanel.add(separator);
		
		JLabel EventInfo = new JLabel("Event");
		EventInfo.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		EventInfo.setBounds(178, 10, 68, 30);
		formPanel.add(EventInfo);
		
		useAutoEvent = new JCheckBox("Use Auto Event");
		useAutoEvent.setBackground(Color.WHITE);
		useAutoEvent.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		useAutoEvent.setBounds(215, 78, 150, 23);
//		useAutoEvent.setSelected(Event.useAutoEvent);
		useAutoEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoEvent.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("Use Auto Event"), Util.separator));
					sb.append("If you add MK119 performance, an event will be added\n\n");
					sb.append("Automatic events are registered with the same settings except for the event name" + Util.separator + Util.separator + "\n");
					sb.append("\nPlease check the event settings !\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		formPanel.add(useAutoEvent);
		
		JButton eventInfoButton = new JButton("Event setting");
		eventInfoButton.setBounds(195, 44, 174, 29);		
		eventInfoButton.setBackground(Color.WHITE);
		eventInfoButton.setForeground(Color.BLACK);		
		eventInfoButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		eventInfoButton.setBorder(UIManager.getBorder("Button.border"));		
		eventInfoButton.setFocusPainted(false);
		eventInfoButton.setContentAreaFilled(false);
		eventInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(!EventInfoFrame.isExist) {
					 new EventInfoFrame();
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Event Frame Already Exists") + Util.separator + "\n");
					 sb.append("Event Setting Frame is already open" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
		
		formPanel.add(eventInfoButton);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(401, 2, 2, 105);
		formPanel.add(separator_1);
		
		JLabel tableSet = new JLabel("Table Operation");
		tableSet.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tableSet.setBounds(415, 10, 150, 30);
		formPanel.add(tableSet);
		
		
		JButton addButton = new JButton("Add Row");
		addButton.setBounds(425, 52, 120, 44);
		addButton.setBackground(Color.WHITE);
		addButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		addButton.setForeground(Color.BLACK);
		addButton.setBorder(UIManager.getBorder("Button.border"));		
		addButton.setFocusPainted(false);
		addButton.setContentAreaFilled(false);
		addButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
//					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					sortingAndValidation();
					return;
				}
				
				if(isConverting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
//					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					converting();
					return;
				}
				
				try {
					int[] selectedIndex = ModbusAgent_Panel.table.getSelectedRows();				
					Perf[] perfs = Perf.getCustomModbusPerfs(ModbusAgent_Panel.table, selectedIndex);			 
					if(perfs == null) return;			
					addRecord(perfs);
				}catch(Exception exception) {
					// 테이블 내용 추가 수행 중 예외가 발생하면 아무것도 수행하지 않음
					exception.printStackTrace();
				}				
			}
		});
		formPanel.add(addButton);
		
		
		JButton deleteButton = new JButton("Delete Row");
		deleteButton.setBounds(554, 52, 120, 44);
		deleteButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		deleteButton.setFocusPainted(false);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setBorder(UIManager.getBorder("Button.border"));
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setForeground(Color.BLACK);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
//					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					sortingAndValidation();
					return;
				}
				
				if(isConverting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
//					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					converting();
					return;
				}
				
				int[] selected = table.getSelectedRows();					
				removeRecord(selected);
			}
		});
		formPanel.add(deleteButton);
		
		
		JButton sortButton = new JButton("Sorting and Validation");
		sortButton.setForeground(Color.BLACK);
		sortButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sortButton.setFocusPainted(false);
		sortButton.setContentAreaFilled(false);
		sortButton.setBorder(UIManager.getBorder("Button.border"));
		sortButton.setBackground(Color.WHITE);
		sortButton.setBounds(682, 52, 207, 44);
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Inspect Thread is Already Working</font>\n");
//					sb.append(String.format("이미 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					sortingAndValidation();
					return;
				}
				
				if(isConverting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
//					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 테이블 정렬 및 검증 작업을 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					converting();
					return;
				}
				
				sortRecord();
			}
		});
		formPanel.add(sortButton);
		
		
		JButton resetButton = new JButton("Table Reset");
		resetButton.setBounds(895, 52, 120, 44);
		resetButton.setBackground(Color.WHITE);
		resetButton.setForeground(Color.BLACK);
		resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		formPanel.add(resetButton);
		
		useAutoMeasure = new JCheckBox("Automatic generation of performance units");
		useAutoMeasure.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		useAutoMeasure.setBackground(Color.WHITE);
		useAutoMeasure.setBounds(670, 15, 345, 23);
		useAutoMeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoMeasure.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("Use unit automatic generation"), Util.separator));
					sb.append("[ Sorting and Validation ] When you use the functions " + Util.separator + "\n\n");
					sb.append("After the input performance name is checked,"  + Util.separator + "\n\n"); 
					sb.append("the appropriate unit is automatically generated for the performance name" + Util.separator + "\n\n");					
					sb.append("This function is a simple auxiliary function, so please check the unit contents of the automatically generated performance !"  + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		formPanel.add(useAutoMeasure);
		
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
//					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					sortingAndValidation();
					return;
				}
				
				if(isConverting) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
//					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
//					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					converting();
					return;
				}
				
				resetTable(table);				
				CustomModbusXmlGeneratorFrame.modbusPerfList.clear();
			}
		});
		
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		CustomModbusXmlGeneratorFrame.isExist = false;
		super.dispose();
	}	
	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
//			new String[] {
//			"순 서", 
//			"성능명", 
//			"기능코드", 
//			"Register 주소", 
//			"Modbus 주소",
//			"성능 카운터", 
//			"수집주기", 
//			"단 위", 
//			"보정식", 
//			"마지막 수집 값", 
//			"이진 상태 : 0", 
//			"이진 상태 : 1", 
//			"다중 상태"
//			}
			
			COLUMN_NAME
		) {
			boolean[] columnEditables = new boolean[] {
				false, // 순서
				true, // 성능명
				false, // 기능코드
				false, // Register 주소
				false, // Modbus 주소
				false, // 성능 카운터 (perfCounter)
				true, // 수집주기
				true, // 단위
				true, // 보정식
				false, // 마지막 수집 값
				true, // 이진 상태 : 0
				true, // 이진 상태 : 1
				true // 다중 상태
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(70); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(250); // 성능명
		table.getColumnModel().getColumn(2).setPreferredWidth(130); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(150); // Register 주소
		table.getColumnModel().getColumn(4).setPreferredWidth(150); // Modbus 주소
		table.getColumnModel().getColumn(5).setPreferredWidth(280); // 성능 카운터
		table.getColumnModel().getColumn(6).setPreferredWidth(80); // 수집주기
		table.getColumnModel().getColumn(7).setPreferredWidth(80); // 단위
		table.getColumnModel().getColumn(8).setPreferredWidth(130); // 보정식
		table.getColumnModel().getColumn(9).setPreferredWidth(150); // 마지막 수집 값
		table.getColumnModel().getColumn(10).setPreferredWidth(120); // 이진 상태 : 0
		table.getColumnModel().getColumn(11).setPreferredWidth(120); // 이진 상태 : 1
		table.getColumnModel().getColumn(12).setPreferredWidth(300); // 다중 상태
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
//		table.getTableHeader().setResizingAllowed(false); // 컬럼 와이드 크기 임의 변경 불가
		
		setCellContentCenter(table);
	}
	
	/**
	 *  XML Convertiong
	 */
	public void convertCustomModbusXML() {
		
		try {
		
			Thread thread = new Thread(new Runnable() {
				String encoding;
				
				public void run() {
					
					isConverting = true;
					
					Perf[] perfs = getCollectionPerfList(null);		
					
					if(!perfCheckOk) {
						isConverting = false;
						return;
					}
					if(perfs == null) {
						isConverting = false;
						return;
					}
					
					StringBuilder sb = new StringBuilder();				
					sb.append("<font color='green'>Convert to XML File?</font>\n");
								
					sb.append(String.format("Do you want to convert %s performance items?%s%s\n\n",Util.colorBlue(String.valueOf(perfs.length)) ,Util.separator, Util.separator));
					
					if(useAutoEvent.isSelected()) {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("Use Auto Event"), Util.separator ,Util.separator));
					}else {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("Don't use Use Auto Event"), Util.separator ,Util.separator));
					}
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// 성능 추가 요청 취소
//						sb = new StringBuilder();
//						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
//						sb.append(String.format("XML 변환 작업을 취소하였습니다%s\n", Util.separator));											
//						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
						sb.append(String.format("XML conversion operation has been canceled%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						
						CustomModbusXmlGeneratorFrame.isConverting = false;
						return;
					}else {
						StringBuilder msg = new StringBuilder();
						msg.append("<font color='Green'>XML File Encoding</font>\n");
						msg.append("Select the encoding method of the XML file" + Util.separator + Util.separator +"\n\n");
						msg.append(String.format("It's the same as or lower than MK119 %s Version : %s%s%s\n", Util.colorGreen("4.2") ,Util.colorBlue("EUC-KR"), Util.separator, Util.separator));
						msg.append(String.format("It's the same as or higher than MK119 %s Version : %s%s%s\n", Util.colorGreen("4.5"), Util.colorBlue("UTF-8"), Util.separator, Util.separator));
						
						int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

						switch (menu) {
							case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때
								sb = new StringBuilder();
								sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
								sb.append(String.format("XML conversion operation has been canceled%s\n", Util.separator));											
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								CustomModbusXmlGeneratorFrame.isConverting = false;
								return;
							case 0: // 첫 번째 버튼 : EUC-KR
								encoding = "euc-kr";
								break;
							case 1: // 두 번째 버튼
								encoding = "utf-8";
								break;
						}
					}
					
					// 성능을 추가하는 시점에 테이블의 데이터는 정렬 및 검증 된 데이터이다
					resetTable(table);		
//					Arrays.sort(perfs);		
					addRecord(perfs);
					
					// 자동 이벤트 등록 옵션
					if(useAutoEvent.isSelected()) Perf.initPerfEvent(perfs);															
					
					// 성능 주소 표기 방식에 따른 strPerfCounter 초기화
					// ModbusAddress : 3_1_TWO BYTE INT SIGNED
					// RegisterAddress : 3_0x0000_TWO BYTE INT SIGNED
					if(modbusAddress_radiobutton.isSelected()) {
						Perf.convertCustomPerfCounter(true, perfs);
					}else if (registerAddress_radiobutton.isSelected()) {
						Perf.convertCustomPerfCounter(false, perfs);
					}
					
					XmlGenerator.generateXML(perfs, useAutoEvent.isSelected(), encoding, "modbus");
					
					isConverting = false;
				}
			});
		
			thread.start();
			
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to XML Converting</font>\n");
			sb.append(String.format("An exception occurred during XML conversion operation%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}finally {
			isConverting = false;
		}
	}

	/**
	 * 	레코드 추가 메소드 
	 */
	public static void addRecord(Perf... perf) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < perf.length; i++) {
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// 테이블의 행 개수가 0개 일 경우 : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // 순서
				/* column[1] */ record.add(perf[i].getDisplayName()); // 성능명
				/* column[2] */ record.add(perf[i].getFunctionCode());  // 기능코드
				/* column[3] */ record.add(perf[i].getRegisterAddress()); // Register 주소
				/* column[4] */ record.add(perf[i].getModbusAddress()); // Modbus 주소
				
				if(registerAddress_radiobutton.isSelected()) {
					/* column[5] */ record.add(perf[i].getCustomPerfCounter_HEX()); // 성능 카운터 (HEX)
				}else {
					/* column[5] */ record.add(perf[i].getCustomPerfCounter_DEC()); // 성능 카운터 (DEC)
				}				
				
				/* column[6] */ record.add(perf[i].getInterval()); // 수집 주기
				/* column[7] */ record.add(perf[i].getMeasure()); // 단위
				/* column[8] */ record.add(perf[i].getScaleFunction()); // 보정식
				/* column[9] */ record.add(perf[i].getLastValue()); // 마지막 수집 값

				switch(perf[i].getDataFormat()) {
				case "1" : 
					/* column[10] */ record.add(perf[i].getBinaryMap().get("0")); // 이진 상태 : 0
					/* column[11] */ record.add(perf[i].getBinaryMap().get("1")); // 이진 상태 : 1
					/* column[12] */ record.add(""); // 다중 상태
					break;
				case "2" :
					String multiStatus = Perf.parseMultiStatusSring(perf[i].getMultiStatusMap());
					/* column[10] */ record.add(""); // 이진 상태 : 0
					/* column[11] */ record.add(""); // 이진 상태 : 1					
					/* column[12] */ record.add(multiStatus); // 다중 상태
					break;
				case "3" :
					/* column[10] */ record.add(""); // 이진 상태 : 0
					/* column[11] */ record.add(""); // 이진 상태 : 1					
					/* column[12] */ record.add(""); // 다중 상태
					break;
				}
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	/**
	 * 레코드 삭제 메소드
	 */
	public void removeRecord(int... index) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		if(index.length < 0) {
			// 선택 된 행이 없거나
			if(table.getRowCount()==0) {
				// 테이블에 행이 없을 경우 아무것도 수행하지 않음
				return;
			}
		}
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for(int i = 0; i < index.length; i++) {
			int rowIndex = Integer.parseInt(table.getValueAt(index[i], 0).toString().trim());
			indexList.add(rowIndex);	
		}
		
		for(Integer removeIndex : indexList) {
			removeRow(removeIndex);
		}
	}
	
	public void removeRow(Integer rowNum) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = table.getRowCount();
		
		for(int i = 0; i < rowCount; i++) {
			Integer num = Integer.parseInt(table.getValueAt(i, 0).toString().trim());
			if(rowNum == num) {
				model.removeRow(i);				
				return;
			}
		}
	}
	
	/**
	 * 테이블 정렬 및 검증
	 */
	public void sortRecord() {
		try {
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					isInspecting = true;
					
					Perf[] perfs = getCollectionPerfList(null);		
					
					if(!perfCheckOk) {
						isInspecting = false;
						return;
					}
					if(perfs == null) {
						isInspecting = false;
						return;
					}
					
					resetTable(table);		
					Arrays.sort(perfs);		
					addRecord(perfs);
					Util.showMessage(String.format("%s%s%s\nTable Sorting and Validation Completed%s\n", Util.colorBlue("Inspection Successful"), Util.separator, Util.separator, Util.separator), JOptionPane.INFORMATION_MESSAGE);
					
					isInspecting = false;
				}
			});
			
			thread.start();
			
		}catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Table Validation Exception</font>\n");
			sb.append(String.format("An exception occurred during validation of the table%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}finally {
			isInspecting = false;
		}
	}
	
	public void changeAddressType() {
		try {
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					isInspecting = true;
					
					Perf[] perfs = getCollectionPerfList("don't check perfName");		
					
					if(!perfCheckOk) {
						isInspecting = false;
						return;
					}
					if(perfs == null) {
						isInspecting = false;
						return;
					}
					
					resetTable(table);			
					addRecord(perfs);
					
					isInspecting = false;
				}
			});
			
			thread.start();
			
		}catch (Exception e) {
			isInspecting = false;
		}
	}
	
	public Perf[] getCollectionPerfList(String option) {
		perfCheckOk = true;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		
		if(model.getRowCount() <= 0) return null;
		
		Perf[] perfs = new Perf[model.getRowCount()];
		
		Vector rowVector = model.getDataVector();
		Vector data;
		
		for(int i = 0; i < model.getRowCount(); i++) {
			data = (Vector)rowVector.get(i);
			
			perfs[i] = new Perf();
			
			// 성능 명 -----------------------------------------------------------------------------------
			String perfName = String.valueOf(data.get(PERF_NAME)).trim();
			
			if(option == null) {
			
				// 성능명 필드가 공백 일 경우
				if((perfName == null) || (perfName.equals("") || perfName.length() < 1)) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Table Validation Exception</font>\n");
//					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>성능명</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//					sb.append(String.format("\n성능명은 반드시 입력해야 하는 필드입니다%s\n", Util.separator));				
//					sb.append(String.format("\n테이블 <font color='blue'>%s</font> 행의 성능명 내용을 입력해주세요%s\n",String.valueOf(data.get(ORDER)), Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.perfName_1(String.valueOf(data.get(ORDER)));
					setFocusCell(table, i, PERF_NAME);
					perfCheckOk = false;
					return null;
				}
				
				// 성능명 특수문자 검사
				if(!Inspecter.isVaildName(perfName)) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Table Validation Exception</font>\n");
//					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>성능명</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//					sb.append(String.format("\n성능명에 아래의 특수 문자를 제외한 특수 문자는 포함 할 수 없습니다%s\n", Util.separator));
//					sb.append(String.format("\n성능명 포함 허용 특수 문자 : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
//					sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 성능명 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfName ,Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.perfName_2(String.valueOf(data.get(ORDER)), perfName);
					setFocusCell(table, i, PERF_NAME);
					perfCheckOk = false;
					return null;			
				}
			
			}// end option check
			
			perfs[i].setDisplayName(perfName); 
			
			
			// 기능 코드 ----------------------------------------------------------------------------------
			try {
				int functionCode = Integer.parseInt(String.valueOf(data.get(FUNCTION_CODE)).trim());
				if((functionCode <= 0) || (functionCode > 4)) throw new Exception("Function Code Validation Error");
			}catch(Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>기능코드</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n<font color='blue'>Modbus XML Generator 지원 기능코드</font>\n"));
//				sb.append(String.format("FC 01 : Read Coil Status\n"));
//				sb.append(String.format("FC 02 : Read Input Status\n"));
//				sb.append(String.format("FC 03 : Read Holding Registers\n"));
//				sb.append(String.format("FC 04 : Read Input Registers\n\n"));
//				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 기능코드 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(FUNCTION_CODE)).trim() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.functionCode_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(FUNCTION_CODE)).trim());
				setFocusCell(table, i, FUNCTION_CODE);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setFunctionCode(String.valueOf(data.get(FUNCTION_CODE)).trim());
			
			// 레지스터 주소 --------------------------------------------------------------------------------
			int registerAddress;
			
			/*
			if(!String.valueOf(data.get(REGISTER_ADDRESS)).toLowerCase().contains("0x")) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Register 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\nRegister 주소 입력 양식 : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, REGISTER_ADDRESS);
				perfCheckOk = false;
				return null;
			}else {
				try {
					registerAddress = Integer.parseInt(String.valueOf(data.get(REGISTER_ADDRESS)).toLowerCase().replace("0x", ""), 16);
					if(registerAddress < 0 || registerAddress > 65535) throw new Exception("Register 주소 내용 오류");
				}catch(NumberFormatException e) {
					// 레지스터 주소를 정수로 파싱 할 수 없을 경우
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Register 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\nRegister 주소를 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, REGISTER_ADDRESS);
					perfCheckOk = false;
					return null;
				} catch(Exception e) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Register 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\nRegister 주소 범위 : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, REGISTER_ADDRESS);
					perfCheckOk = false;
					return null;
				}
			}		
			*/
				
			// toLowerCase() 사용하면 HEX 레지스터 값도 소문자로 변경되어서 아래처럼 개발함
			perfs[i].setRegisterAddress(String.valueOf(data.get(REGISTER_ADDRESS)).replace("X", "x").trim());
			
			
			// 모드버스 주소 --------------------------------------------------------------------------------
			int modbusAddress;
			
			/*
			try {
	 			modbusAddress = Integer.parseInt(String.valueOf(data.get(MODBUS_ADDRESS)).trim());
	 			modbusAddress %= 10000;
	 			if((registerAddress < 10000) && ((registerAddress + 1) != (modbusAddress))) throw new RuntimeException("Modbus 주소 내용 오류");
			}catch(NumberFormatException e) {
				// 모드버스 주소를 정수로 파싱 할 수 없을 경우
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Modbus 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
				sb.append(String.format("\nModbus 주소를 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Modbus 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}catch (RuntimeException e) {
				// 레지스터 주소가 10000 미만이면서 모드버스 주소와 그 값이 일치하지 않을 경우
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Modbus 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\nModbus 주소에서 나누기 <font color='blue'>10000</font>을 하고 남은 나머지 값이\nRegister 주소보다 <font color='blue'>1</font>만큼 높아야 합니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s ( %s )</font>%s\n",String.valueOf(data.get(ORDER)), registerAddress, perfs[i].getRegisterAddress(),Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Modbus 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				sb.append(String.format("\n예) FC 01&nbsp;&nbsp;→&nbsp;&nbsp;Register 주소 : 0x0000  /  Modbus 주소 : 1%s\n", Util.separator));
				sb.append(String.format("예) FC 02&nbsp;&nbsp;→&nbsp;&nbsp;Register 주소 : 0x0123  /  Modbus 주소 : 10292%s\n", Util.separator));
				sb.append(String.format("예) FC 03&nbsp;&nbsp;→&nbsp;&nbsp;Register 주소 : 0x0ABC  /  Modbus 주소 : 42749%s\n", Util.separator));
				sb.append(String.format("예) FC 04&nbsp;&nbsp;→&nbsp;&nbsp;Register 주소 : 0x12AB  /  Modbus 주소 : 34780%s\n", Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}catch(Exception e) {
				// 위에서 처리 할 수 없는 예외 발생 시
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Modbus 주소</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Modbus 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}
			*/			
			
			perfs[i].setModbusAddress(String.valueOf(data.get(MODBUS_ADDRESS)).trim());
			
			// 성능 카운터 ---------------------------------------------------------------------------------
			String customPerfCounter = String.valueOf(data.get(PERF_COUNTER)).trim().toUpperCase();			
			
			String token[] = customPerfCounter.split("_");
			String startAddress_HEX = null; 
			String startAddress_DEC = null;
			
			if(token[1].contains("0x") || token[1].contains("0X")) {				
				startAddress_HEX = token[1].replace("0X", "0x");
				startAddress_DEC = String.format("%d", Integer.parseInt(token[1].replace("0x", "").replace("0X", ""), 16));
			}else {
				startAddress_HEX = String.format("0x%04X", Integer.parseInt(token[1]));
				startAddress_DEC = token[1];
			}
			
			perfs[i].setCustomPerfCounter_HEX(String.format("%s_%s_%s_%s", token[0], startAddress_HEX, token[2], token[3]));
			perfs[i].setCustomPerfCounter_DEC(String.format("%s_%s_%s_%s", token[0], startAddress_DEC, token[2], token[3]));
			
			// 수집 주기  ----------------------------------------------------------------------------------
			String interval = String.valueOf(data.get(INTERVAL)).trim();
			
			try{
				int actualInterval = Integer.parseInt(interval);
				
				if(actualInterval < 1) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Table Validation Exception</font>\n");
//					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>수집 주기</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
//					sb.append(String.format("\n수집 주기는 1 이상의 양의 정수 값만 입력 할 수 있습니다%s\n\n", Util.separator));
//					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 수집주기 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.interval_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim());
					setFocusCell(table, i, INTERVAL);
					perfCheckOk = false;
					return null;
				}
				
			}catch (Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>수집 주기</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
//				sb.append(String.format("\n입력된 수집주기 내용을 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
//				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 수집주기 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.interval_2(String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim());
				setFocusCell(table, i, INTERVAL);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setInterval(interval);
			
			// 단 위  ------------------------------------------------------------------------------------
			perfs[i].setMeasure(String.valueOf(data.get(MEASURE)).trim());
						
			perfName = perfs[i].getDisplayName();

			// 유효한 성능명이 입력되었고 단위가 입력되지 않은 경우
			if(useAutoMeasure.isSelected()) {
				if(perfName != null && perfName.length() > 1 && !perfName.equalsIgnoreCase("")) { 
					if(perfs[i].getMeasure().length() == 0 || perfs[i].getMeasure().equals("")) {					
						perfs[i].setMeasure(Perf.createMeasure(perfName).trim());
					}
				}
			}
			
			
			// 보정식 ------------------------------------------------------------------------------------
			String scaleFunction = null;
			
			if(!String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase().contains("x")) {
				scaleFunction = String.format("(x>>%s)&1", String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase());
			}else {				
				scaleFunction = String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase();
			}
			
			if (scaleFunction.contains("&amp;")) scaleFunction = scaleFunction.replace("&amp;", "&");
			if (scaleFunction.contains("&gt;")) scaleFunction = scaleFunction.replace("&gt;", ">");
			if (scaleFunction.contains("&lt;")) scaleFunction = scaleFunction.replace("&lt;", "<");
			
			try {
				// 보정식 유효성 검사
				JavaScript.eval(scaleFunction, "1");
				if (scaleFunction.equalsIgnoreCase("") || scaleFunction.length() < 1 || !scaleFunction.contains("x"))
					throw new Exception("Scale Validation Error");				
			}catch(Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>보정식</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 보정식 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase() ,Util.separator));				
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.scaleFunction_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase());
				setFocusCell(table, i, SCALE_FUNCTION);
				perfCheckOk = false;
				return null;
			}			
			perfs[i].setScaleFunction(scaleFunction);
			
			// 마지막 수집 값 ---------------------------------------------------------------------------------
			perfs[i].setLastValue(String.valueOf(data.get(LAST_VALUE)));
			
			
			// 이진 상태 : 0, 1 ---------------------------------------------------------------------------
			HashMap binaryMap = perfs[i].getBinaryMap();
			String label0 = String.valueOf(data.get(LABLE_0)).trim();
			String label1 = String.valueOf(data.get(LABLE_1)).trim();
			
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				// 이진 상태 데이터 형식에서 레이블이 하나만 존재 할 수는 없다 
				if((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1))) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Table Validation Exception</font>\n");
//					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 1</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));	
//					
//					if (perfs[i].getFunctionCode().equals("1") || perfs[i].getFunctionCode().equals("2"))
//						sb.append(String.format("\n기능코드 1, 2번을 사용하는 레지스터는\n\n반드시 <font color='blue'>이진 상태 : 0, 1</font> 내용을 입력해야 합니다%s\n", Util.separator));
//					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.binary_1(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode());
					setFocusCell(table, i, LABLE_1);
					perfCheckOk = false;
					return null;
				}
			}else if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
				if((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1))) {
					// 이진 상태 데이터 형식에서 레이블이 하나만 존재 할 수는 없다
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Table Validation Exception</font>\n");
//					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 0</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//					
//					if (perfs[i].getFunctionCode().equals("1") || perfs[i].getFunctionCode().equals("2"))
//						sb.append(String.format("\n기능코드 1, 2번을 사용하는 레지스터는\n\n반드시 <font color='blue'>이진 상태 : 0, 1</font> 내용을 입력해야 합니다%s\n", Util.separator));
//					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.binary_2(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode());
					setFocusCell(table, i, LABLE_0);
					perfCheckOk = false;
					return null;
				}
			}
			
			// 레지스터의 기능코드가 1, 2번 이면서 이진 상태 : 0, 이진 상태 : 1 내용이 없을 경우
			if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2"))
				&& ((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1)))){
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 0</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));			
//				sb.append(String.format("\n기능코드 1, 2번을 사용하는 레지스터는\n\n반드시 <font color='blue'>이진 상태 : 0, 1</font> 내용을 입력해야 합니다%s\n", Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.binary_3(String.valueOf(data.get(ORDER)));
				setFocusCell(table, i, LABLE_0);
				perfCheckOk = false;
				return null;
			}else if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2"))
				&& ((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1)))){
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 1</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));			
//				sb.append(String.format("\n기능코드 1, 2번을 사용하는 레지스터는\n\n반드시 <font color='blue'>이진 상태 : 0, 1</font> 내용을 입력해야 합니다%s\n", Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.binary_4(String.valueOf(data.get(ORDER)));
				setFocusCell(table, i, LABLE_1);
				perfCheckOk = false;
				return null;
			}
			
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					binaryMap.put("0", label0);
					binaryMap.put("1", label1);
				}
			}
			perfs[i].setBinaryMap(binaryMap);
			
			
			// 다중 상태 --------------------------------------------------------------------------			
			HashMap multiStatusMap = new HashMap();
			
			if ((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if ((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					if((String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
//						StringBuilder sb = new StringBuilder();
//						sb.append("<font color='red'>Table Validation Exception</font>\n");
//						sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태</font>와 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
//						sb.append(String.format("데이터 형식이 <font color='blue'>이진 상태</font> 이면서 동시에 <font color='blue'>다중 상태</font> 일 수는 없습니다%s\n", Util.separator));
//						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						
						MessageUtil_en.multiStatus_1(String.valueOf(data.get(ORDER)));
						setFocusCell(table, i, LABLE_STATUS);
						perfCheckOk = false;
						return null;
					}
				}
			}
			
			try{
				multiStatusMap = parseMultiStatusMap(String.valueOf(data.get(LABLE_STATUS)));
			}catch(Exception e) {
				e.printStackTrace();				
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Table Validation Exception</font>\n");
//				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 다중 상태 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(LABLE_STATUS)).trim() ,Util.separator));
//				if(e instanceof NumberFormatException) {
//					sb.append(String.format("\n다중 상태 입력 양식 : <font color='blue'>숫자1; 문자1; 숫자2; 문자2; 숫자3; 문자3; ...</font>%s%s\n", Util.separator, Util.separator));
//				}
//				
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.multiStatus_2(String.valueOf(data.get(ORDER)), String.valueOf(data.get(LABLE_STATUS)).trim(), e);
				setFocusCell(table, i, LABLE_STATUS);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setMultiStatusMap(multiStatusMap);
						
			
			// 데이터 형식 -------------------------------------------------------------------------------
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					
					// 데이터 형식이 이진 상태이면서 다중 상태 일 수는 없다
					if((multiStatusMap.size() >= 1) || (String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
//						StringBuilder sb = new StringBuilder();
//						sb.append("<font color='red'>Table Validation Exception</font>\n");
//						sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태</font>와 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
//						sb.append(String.format("데이터 형식이 <font color='blue'>이진 상태</font> 이면서 동시에 <font color='blue'>다중 상태</font> 일 수는 없습니다%s\n", Util.separator));
//						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						
						MessageUtil_en.multiStatus_3(String.valueOf(data.get(ORDER)));
						setFocusCell(table, i, LABLE_STATUS);
						perfCheckOk = false;
						return null;
					}
	
					perfs[i].setDataFormat("1");
				}
			}else if(multiStatusMap.size() >= 1){
				perfs[i].setDataFormat("2");
			}else {
				perfs[i].setDataFormat("3");
			}			
						
			if(perfs[i].getDisplayName() == null) perfs[i].setDisplayName("");
			if(perfs[i].getMeasure() == null) perfs[i].setMeasure("");
		}
		
		return perfs;
	}
	
	
	
	
	public HashMap parseMultiStatusMap(String pattern) {
		if(pattern == null || pattern.length() == 0 || pattern.equalsIgnoreCase("")) {
			return new HashMap();
		}
		
		HashMap multiStatusMap = new HashMap();		
		String[] tokens = pattern.replace("; ",";").split(";");
		
		if(tokens.length % 2 != 0) {
			throw new RuntimeException("Multi-state content error");
		}else {
			for(int i = 0; i < tokens.length; i+=2) {
				
				// inspect NumberFormatException : 다중 상태는 값-문자 형식으로 매핑되어야 한다
				Integer.parseInt(tokens[i].trim()); 
				
				multiStatusMap.put(tokens[i].trim(), tokens[i+1].trim());
			}
			return multiStatusMap;
		}
	}
	
	public void setFocusCell(JTable table, int row, int column) {
		table.changeSelection(row, column, false, false);				
		table.requestFocus();
	}
	
	
	
	
	/**
	 * 인자로 넘겨준 JTable의 첫번째 컬럼의 내용을 가운데 정렬해준다.
	 * 주로 인덱스 컬럼을 표시해주기 위해서 구현
	 */	
	public static void setCellContentCenter(JTable table) {
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 성능명
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 기능코드
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // Register 주소
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // Modbus 주소
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 성능 카운터
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // 수집주기
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // 단위
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // 보정식
		tcmSchedule.getColumn(9).setCellRenderer(tScheduleCellRenderer); // 마지막 수집 값
		tcmSchedule.getColumn(10).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 0
		tcmSchedule.getColumn(11).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 1		
//		tcmSchedule.getColumn(12).setCellRenderer(tScheduleCellRenderer); // 다중 상태
	}
	
	public void sortingAndValidation(){
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Inspection Thread is Working</font>\n");
		sb.append(String.format("Table sorting and Validation is currently being carried out%s\n", Util.separator));
		sb.append(String.format("\nNo other requests can be processed during the operation%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public void converting() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
		sb.append(String.format("XML conversion is currently underway%s\n", Util.separator));
		sb.append(String.format("\nNo other requests can be processed during the operation%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
}
