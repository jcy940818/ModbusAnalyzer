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
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.util.ExcelAdapter;
import src_en.util.Inspecter;
import src_en.util.JavaScript;
import src_en.util.Util;

public class WatchPointSettingFrame extends JFrame {
	
	private static final int ORDER = 0;
	private static final int PERF_NAME = 1;
	private static final int FUNCTION_CODE = 2;
	private static final int REGISTER_ADDRESS = 3;
	private static final int MODBUS_ADDRESS = 4;
	private static final int DATA_TYPE = 5;	
	private static final int MEASURE = 6;
	private static final int SCALE_FUNCTION = 7;
	private static final int LAST_VALUE = 8;	
	public static JCheckBox useAutoMeasure;
	
	// 스레드 수행 여부
	public static boolean monitoringRunning = false;
	public static boolean isInspecting = false;
	
	private JPanel contentPane;
	private static JButton startMonitoring_Button;
	public static boolean isExist = false;
	private static List modbusPerfList;
	private static JTable table;	
	private static boolean watchPointCheckOk = true;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					WatchPointSettingFrame frame = new WatchPointSettingFrame("127.0.0.1:502");
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
	public WatchPointSettingFrame(String connectionInfo) {
		WatchPointSettingFrame.isExist = true;
		modbusPerfList = new ArrayList();
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle(String.format("Server : %s", connectionInfo));
		
		// 클래스 로더를 이용한 이미지 로딩
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// 프로젝트 Build Path에 이미지 리소스 디렉토리를 포함시켜야 한다.		
		setIconImage(new Util().getIconResource().getImage());
				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1080, 717);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(139, 0, 0), 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		ImageIcon image = new Util().getIconResource();
		actualPanel.setLayout(null);
		
		JLabel watchPointSetting = new JLabel("Watch Point Setting");
		watchPointSetting.setIcon(new Util().getSubLogoResource());
		watchPointSetting.setHorizontalAlignment(SwingConstants.LEFT);
		watchPointSetting.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		watchPointSetting.setBackground(Color.WHITE);
		watchPointSetting.setBounds(0, 0, 280, 55);
		actualPanel.add(watchPointSetting);
		
		JLabel monitoringServer = new JLabel();
		monitoringServer.setForeground(Color.BLUE);
		monitoringServer.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		monitoringServer.setBounds(292, 0, 484, 55);
		actualPanel.add(monitoringServer);
		
		startMonitoring_Button = new JButton(new Util().getMK2Resource());
		startMonitoring_Button.setText(" \uC131\uB2A5 \uCD94\uAC00");
		startMonitoring_Button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		startMonitoring_Button.setFocusPainted(false);
		startMonitoring_Button.setContentAreaFilled(false);
		startMonitoring_Button.setBorder(UIManager.getBorder("Button.border"));
		startMonitoring_Button.setBackground(Color.WHITE);
		startMonitoring_Button.setBounds(850, 11, 189, 36);
		startMonitoring_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 실시간 모니터링 기능을 사용 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Real-Time Monitoring Thread is Working</font>\n");
					sb.append(String.format("이미 모니터링 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n모니터링 스레드는 중복으로 실행 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				startMonitoring();
			}
		});
		actualPanel.add(startMonitoring_Button);
		
		JPanel monitoringPanel = new JPanel();
		monitoringPanel.setBackground(new Color(255, 255, 255));
		monitoringPanel.setBounds(12, 56, 1030, 481);
		actualPanel.add(monitoringPanel);
		monitoringPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.control);
		scrollPane.setBorder(new LineBorder(Color.BLACK, 3));
		monitoringPanel.add(scrollPane, BorderLayout.CENTER);
		
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
		
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBounds(650, 2, 2, 105);
		formPanel.add(separator);
		
		JLabel tableSet = new JLabel("\uD14C\uC774\uBE14 \uC870\uC791");
		tableSet.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tableSet.setBounds(22, 10, 111, 30);
		formPanel.add(tableSet);
		
		
		JButton addButton = new JButton("\uB808\uCF54\uB4DC \uCD94\uAC00");
		addButton.setBounds(32, 52, 120, 44);
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
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Real-Time Monitoring Thread is Working</font>\n");
					sb.append(String.format("현재 모니터링 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n모니터링 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					int[] selectedIndex = RealTime_Panel.table.getSelectedRows();				
					Perf[] perfs = Perf.getModbusPerfs(RealTime_Panel.table, selectedIndex);			 
					if(perfs == null) return;			
					addRecord(perfs);
				}catch(Exception exception) {
					// 테이블 내용 추가 수행 중 예외가 발생하면 아무것도 수행하지 않음
					exception.printStackTrace();
				}				
			}
		});
		formPanel.add(addButton);
		
		
		JButton deleteButton = new JButton("\uB808\uCF54\uB4DC \uC0AD\uC81C");
		deleteButton.setBounds(161, 52, 120, 44);
		deleteButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		deleteButton.setFocusPainted(false);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setBorder(UIManager.getBorder("Button.border"));
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setForeground(Color.BLACK);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Real-Time Monitoring Thread is Working</font>\n");
					sb.append(String.format("현재 모니터링 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n모니터링 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int[] selected = table.getSelectedRows();					
				removeRecord(selected);
			}
		});
		formPanel.add(deleteButton);
		
		
		JButton sortButton = new JButton("\uD14C\uC774\uBE14 \uC815\uB82C / \uAC80\uC99D");
		sortButton.setForeground(Color.BLACK);
		sortButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sortButton.setFocusPainted(false);
		sortButton.setContentAreaFilled(false);
		sortButton.setBorder(UIManager.getBorder("Button.border"));
		sortButton.setBackground(Color.WHITE);
		sortButton.setBounds(289, 52, 175, 44);
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspect Thread is Already Working</font>\n");
					sb.append(String.format("이미 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Real-Time Monitoring Thread is Working</font>\n");
					sb.append(String.format("현재 모니터링 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n모니터링 스레드 수행중에는 테이블 정렬 및 검증 작업을 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				sortRecord();
			}
		});
		formPanel.add(sortButton);
		
		
		JButton resetButton = new JButton("\uD14C\uC774\uBE14 \uCD08\uAE30\uD654");
		resetButton.setBounds(472, 52, 150, 44);
		resetButton.setBackground(Color.WHITE);
		resetButton.setForeground(Color.BLACK);
		resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		formPanel.add(resetButton);
		
		useAutoMeasure = new JCheckBox("\uC131\uB2A5 \uB2E8\uC704 \uC790\uB3D9 \uC0DD\uC131");
		useAutoMeasure.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		useAutoMeasure.setBackground(Color.WHITE);
		useAutoMeasure.setBounds(461, 15, 161, 23);
		useAutoMeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoMeasure.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("성능 단위 자동 생성 사용"), Util.separator));
					sb.append("[ 테이블 정렬 / 검증 ] 기능 사용시 " + Util.separator + "\n\n");
					sb.append("입력된 성능명 검사 후 해당 성능명에 적절한 단위가 자동으로 생성됩니다" + Util.separator + "\n\n");					
					sb.append("해당 기능은 단순 보조 기능이므로 반드시 자동 생성된 성능의 단위 내용을 확인해주세요  !"  + Util.separator +  "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		formPanel.add(useAutoMeasure);
		
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 정렬 및 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n정렬 및 검증 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(monitoringRunning) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Real-Time Monitoring Thread is Working</font>\n");
					sb.append(String.format("현재 모니터링 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n모니터링 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				resetTable(table);				
				WatchPointSettingFrame.modbusPerfList.clear();
			}
		});
		
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		WatchPointSettingFrame.isExist = false;
		super.dispose();
	}	
	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
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
			new String[] {
				"\uC21C \uC11C", "\uC131\uB2A5\uBA85", "\uAE30\uB2A5\uCF54\uB4DC", "Register \uC8FC\uC18C", "Modbus \uC8FC\uC18C", "\uB370\uC774\uD130 \uD0C0\uC785", "\uB2E8 \uC704", "\uBCF4\uC815\uC2DD", "\uB9C8\uC9C0\uB9C9 \uC218\uC9D1 \uAC12"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true, true, true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(70); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(250); // 성능명
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // 기능코드
		table.getColumnModel().getColumn(3).setPreferredWidth(130); // Register 주소
		table.getColumnModel().getColumn(4).setPreferredWidth(130); // Modbus 주소
		table.getColumnModel().getColumn(5).setPreferredWidth(280); // 데이터 타입
		table.getColumnModel().getColumn(6).setPreferredWidth(80); // 단위
		table.getColumnModel().getColumn(7).setPreferredWidth(130); // 보정식
		table.getColumnModel().getColumn(8).setPreferredWidth(150); // 마지막 수집 값
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
//		table.getTableHeader().setResizingAllowed(false); // 컬럼 와이드 크기 임의 변경 불가
		
		setCellContentCenter(table);
	}
	
	/**
	 *  실시간 모니터링 시작 버튼
	 */
	public void startMonitoring() {
		
		try {
		
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					monitoringRunning = true;
					
					Perf[] perfs = getWatchPointList();
					
					if(!watchPointCheckOk) {
						monitoringRunning = false;
						return;
					}
					if(perfs == null) {
						monitoringRunning = false;
						return;
					}
					
					StringBuilder sb = new StringBuilder();				
					sb.append("<font color='green'>Do Start Real-Time Monitoring?</font>\n");
								
					sb.append(String.format("Watch Point %s개 항목을 실시간 모니터링 하시겠습니까?%s%s\n",Util.colorBlue(String.valueOf(perfs.length)) ,Util.separator, Util.separator));
										
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// 성능 추가 요청 취소
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Monitoring Watch Point</font>%s\n", Util.separator));
						sb.append(String.format("실시간 모니터링 요청을 취소하였습니다%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						WatchPointSettingFrame.monitoringRunning = false;
						return;
					}
					
					// 성능을 추가하는 시점에 테이블의 데이터는 정렬 및 검증 된 데이터이다
					resetTable(table);		
					Arrays.sort(perfs);		
					addRecord(perfs);
										
					// 어떻게할까?
					
					
				}
			});
		
			thread.start();
			
		}catch(Exception e) {			
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to Monitoring Watch Point</font>\n");
			sb.append(String.format("실시간 모니터링 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}finally {
			monitoringRunning = false;
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
				/* column[5] */ record.add(perf[i].getDataType()); // 데이터 타입				
				/* column[6] */ record.add(perf[i].getMeasure()); // 단위
				/* column[7] */ record.add(perf[i].getScaleFunction()); // 보정식
				/* column[8] */ record.add(perf[i].getLastValue()); // 마지막 수집 값
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
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

		// index[0] : 1번째 레코드
		// index[1] : 2번째 레코드
		// index[2] : 3번째 레코드
		// 위의 경우 index[0] (첫번째 레코드)를 삭제하면
		// index[1] (두번째 레코드)이 index[0] (두번째 레코드)가 되기 때문에
		// model.revmoe(index[0]) 로직을 수행한다
		for(int i = 0; i < index.length; i++) {
			model.removeRow(index[0]);
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
					
					Perf[] perfs = getWatchPointList();		
					
					if(!watchPointCheckOk) {
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
					Util.showMessage(String.format("%s%s%s\n테이블 정렬 및 검증 완료%s\n", Util.colorBlue("Inspection Successful"), Util.separator, Util.separator, Util.separator), JOptionPane.INFORMATION_MESSAGE);
					
					isInspecting = false;
				}
			});
			
			thread.start();
			
		}catch (Exception e) {
			isInspecting = false;
			
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Table Validation Exception</font>\n");
			sb.append(String.format("테이블 검증 작업중 예외가 발생하였습니다%s\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Perf[] getWatchPointList() {
		watchPointCheckOk = true;
		
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
			
			// 성능명 필드가 공백 일 경우
			if((perfName == null) || (perfName.equals("") || perfName.length() < 1)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>성능명</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n성능명은 반드시 입력해야 하는 필드입니다%s\n", Util.separator));				
				sb.append(String.format("\n테이블 <font color='blue'>%s</font> 행의 성능명 내용을 입력해주세요%s\n",String.valueOf(data.get(ORDER)), Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, PERF_NAME);
				watchPointCheckOk = false;
				return null;
			}
			
			// 성능명 특수문자 검사
			if(!Inspecter.isVaildName(perfName)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>성능명</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n성능명에 아래의 특수 문자를 제외한 특수 문자는 포함 할 수 없습니다%s\n", Util.separator));
				sb.append(String.format("\n성능명 포함 허용 특수 문자 : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 성능명 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfName ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, PERF_NAME);
				watchPointCheckOk = false;
				return null;
			}
			
			perfs[i].setDisplayName(perfName); 
			
			
			// 기능 코드 ----------------------------------------------------------------------------------
			try {
				int functionCode = Integer.parseInt(String.valueOf(data.get(FUNCTION_CODE)).trim());
				if((functionCode <= 0) || (functionCode > 4)) throw new Exception("기능 코드 내용 오류");
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>기능코드</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n<font color='blue'>Real-Time Monitoring 지원 기능코드</font>\n"));
				sb.append(String.format("FC 01 : Read Coil Status\n"));
				sb.append(String.format("FC 02 : Read Input Status\n"));
				sb.append(String.format("FC 03 : Read Holding Registers\n"));
				sb.append(String.format("FC 04 : Read Input Registers\n\n"));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 기능코드 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(FUNCTION_CODE)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, FUNCTION_CODE);
				watchPointCheckOk = false;
				return null;
			}
			perfs[i].setFunctionCode(String.valueOf(data.get(FUNCTION_CODE)).trim());
			
			// 레지스터 주소 --------------------------------------------------------------------------------
			int registerAddress;
			if(!String.valueOf(data.get(REGISTER_ADDRESS)).toLowerCase().contains("0x")) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Register 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\nRegister 주소 입력 양식 : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, REGISTER_ADDRESS);
				watchPointCheckOk = false;
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
					watchPointCheckOk = false;
					return null;
				} catch(Exception e) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Register 주소</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\nRegister 주소 범위 : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Register 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, REGISTER_ADDRESS);
					watchPointCheckOk = false;
					return null;
				}
			}			
			// toLowerCase() 사용하면 HEX 레지스터 값도 소문자로 변경되어서 아래처럼 개발함
			perfs[i].setRegisterAddress(String.valueOf(data.get(REGISTER_ADDRESS)).replace("X", "x").trim());
			
			
			// 모드버스 주소 --------------------------------------------------------------------------------
			int modbusAddress;
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
				watchPointCheckOk = false;
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
				watchPointCheckOk = false;
				return null;
			}catch(Exception e) {
				// 위에서 처리 할 수 없는 예외 발생 시
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>Modbus 주소</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 Modbus 주소 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				watchPointCheckOk = false;
				return null;
			}
			perfs[i].setModbusAddress(String.valueOf(data.get(MODBUS_ADDRESS)).trim());
			
			// 데이터 타입 ---------------------------------------------------------------------------------
			String dataType = String.valueOf(data.get(DATA_TYPE)).trim().toUpperCase();
			
			if(!DataType.typeMap.containsKey(dataType)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>DataType Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>데이터 타입</font> 내용에 문제가 있습니다%s%s%s\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));
				
				sb.append(String.format("\n<font color='blue'>Real-Time Monitoring 지원 데이터 타입</font>%s\n", Util.separator));				
				sb.append(String.format("1. %s%s%s\n","BINARY" , Util.separator, Util.separator));				
				sb.append(String.format("2. %s%s%s\n","TWO BYTE INT SIGNED" , Util.separator, Util.separator));
				sb.append(String.format("3. %s%s%s\n","TWO BYTE INT UNSIGNED" , Util.separator, Util.separator));
				sb.append(String.format("4. %s%s%s\n","FOUR BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
				sb.append(String.format("5. %s%s%s\n","FOUR BYTE INT SIGNED (C D A B)" , Util.separator, Util.separator));
				sb.append(String.format("6. %s%s%s\n","FOUR BYTE INT UNSIGNED (A B C D)" , Util.separator, Util.separator));
				sb.append(String.format("7. %s%s%s\n","FOUR BYTE INT UNSIGNED (C D A B)" , Util.separator, Util.separator));
				sb.append(String.format("8. %s%s%s\n","FOUR BYTE FLOAT (A B C D)" , Util.separator, Util.separator));
				sb.append(String.format("9. %s%s%s\n","FOUR BYTE FLOAT (C D A B)" , Util.separator, Util.separator));
				sb.append(String.format("10. %s%s%s\n","EIGHT BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
				sb.append(String.format("11. %s%s%s\n","EIGHT BYTE DOUBLE (A B C D)" , Util.separator, Util.separator));				
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 데이터 타입 내용 : <font color='red'>%s</font>%s%s\n",String.valueOf(data.get(ORDER)), dataType ,Util.separator,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				setFocusCell(table, i, DATA_TYPE);
				watchPointCheckOk = false;
				return null;
			}			
			perfs[i].setDataType(dataType);
			perfs[i].setMK119_DataType(DataType.typeMap.get(dataType));
			
			// 데이터 타입 내용이 BINARY 인데 기능코드가 1, 2번이 아닌 경우
			if(perfs[i].getDataType().equalsIgnoreCase("BINARY") && !( perfs[i].getFunctionCode().equalsIgnoreCase("1") || perfs[i].getFunctionCode().equalsIgnoreCase("2"))) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>데이터 타입</font> 내용에 문제가 있습니다%s%s%s\n\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));	
				sb.append(String.format("<font color='blue'>BINARY</font> 데이터 타입은 ON/OFF 상태를 나타내는 데이터 타입으로%s%s\n\n", Util.separator, Util.separator));					
				sb.append(String.format("기능코드 1, 2번을 사용하는 레지스터 항목만 사용 할 수 있습니다%s%s\n\n", Util.separator, Util.separator));				
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 기능코드 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode() ,Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 데이터 타입 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getDataType() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);				
				setFocusCell(table, i, DATA_TYPE);
				watchPointCheckOk = false;
				return null;
			}
			
			// 기능코드가 1, 2번인데 데이터 타입 내용이 BINARY가 아닌 경우
			if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2")) && (!perfs[i].getDataType().equalsIgnoreCase("BINARY")) ) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>데이터 타입</font> 내용에 문제가 있습니다%s%s%s\n\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));									
				sb.append(String.format("기능코드 1, 2번을 사용하는 레지스터는\n\n데이터 타입으로 오직 <font color='blue'>BINARY</font> 타입만을 사용 할 수 있습니다%s%s\n\n", Util.separator, Util.separator));				
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 기능코드 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getFunctionCode() ,Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 데이터 타입 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getDataType() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, DATA_TYPE);
				watchPointCheckOk = false;
				return null;
			}
			
			
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
					throw new Exception("보정식 내용 오류");				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>보정식</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 보정식 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase() ,Util.separator));				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, SCALE_FUNCTION);
				watchPointCheckOk = false;
				return null;
			}			
			perfs[i].setScaleFunction(scaleFunction);
			
			// 마지막 수집 값 ---------------------------------------------------------------------------------
			perfs[i].setLastValue(String.valueOf(data.get(LAST_VALUE)));
									
			if(perfs[i].getDisplayName() == null) perfs[i].setDisplayName("");
			if(perfs[i].getMeasure() == null) perfs[i].setMeasure("");
		}
		
		return perfs;
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
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 데이터 타입
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // 단위
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // 보정식
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // 마지막 수집 값
	}
	
}
