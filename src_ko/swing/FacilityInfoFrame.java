package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.codehaus.jettison.json.JSONArray;

import common.agent.PerfData;
import common.agent.RestAgent;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfLabelStatusBean;
import common.server.Facility;
import common.util.MyCalendar;
import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
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
	public static final int PERF_DATA_TABLE = 3;
	
	private String searchElement = PERF_NAME;
	
	private JTable perfListTable;
	private JTable perfInfoTable;
	private JTable perfLabelTable;
	
	private HashMap<Integer, PerfData> perfRealTimeDataMap = null;
	public static boolean isExist = false;
	private JLabel MK119;
	
	private Facility fac;
	private boolean isProtocol;
	private ArrayList<FmsPerfItem> perfs;
	private Perf selectedPerf;	
	
	private JPanel contentPane;
	private JPanel perfInfo_Panel;
	private JPanel perfData_Panel;
	private JLabel mappingLabel;
	private JPanel perfDataInfo_Panel;
	private JScrollPane perfInfo_ScrollPanel;
	private JScrollPane perfData_ScrollPanel;
	private JScrollPane perfLabelInfoPanel;
	private JComboBox searchPerf_ComboBox_1;
	private JComboBox searchPerf_ComboBox_2;
	private JTextField searchPerf_textField_1;	
	private JTextField searchPerf_textField_2;
	private JLabel FacilityInfoLabel_1;
	private JLabel FacilityInfoLabel_2;	
	private JButton dbRefreshButton;
	private JButton eventInfo_Button;
	private JButton linkMK119_Button;
	
	private JButton rcuInfo_Button;
	private JTable perfData_Table;	
	
	private JLabel currentFunction;
	
	public static JButton updatePerfData;
	public static JRadioButton perfValue_RadioButton;
	private JLabel time;
	private JLabel seprarator2;
	private JLabel seprarator3;
	private JLabel seprarator4;
	private JRadioButton before_RadioButton;
	private JRadioButton duration_radioButton;
	
	private JComboBox year_comboBox;
	private JComboBox month_comboBox;
	private JComboBox day_comboBox;
	private JComboBox hour_comboBox;
	private JComboBox minute_comboBox;
	private JComboBox second_comboBox;
	private JPanel setTime_panel;
	private JPanel after_panel;
	private JPanel duration_panel;
	private JRadioButton after_RadioButton;
	private JLabel setTime_label1;
	private JTextField setTime_textField;
	private JLabel setTime_label2;
	private JButton btnNewButton;
	private JButton setTimeOk_Button;
	
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
		this.isProtocol = fac.isProtocol();
		this.perfs = Perf.getFaciltiyPerfList(ONION_Info.getMk119Connection(), fac);
		
		FacilityInfoFrame.isExist = true;
		setTitle(String.format("Facility Information   [  Index : %d  /  Name : %s  ]", fac.getIndex(), fac.getName()));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 1260, 717);
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
		
		currentFunction = new JLabel("Facility Information");
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
		perfList_scrollPane.setBounds(12, 128, 715, 530);
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
		
		perfInfo_Panel = new JPanel();
		perfInfo_Panel.setBackground(Color.WHITE);		
		perfInfo_Panel.setBounds(739, 128, 483, 530);
		perfInfo_Panel.setLayout(null);
		perfInfo_Panel.setVisible(true);
//		actualPanel.add(perfInfo_Panel); 테스트
		
		perfInfo_ScrollPanel = new JScrollPane();
		perfInfo_ScrollPanel.setBackground(Color.WHITE);
		perfInfo_ScrollPanel.setBorder(new LineBorder(Color.BLACK, 2));
		perfInfo_ScrollPanel.setBounds(0, 0, 483, 272);
		perfInfo_Panel.add(perfInfo_ScrollPanel);
		
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
		perfInfo_ScrollPanel.setViewportView(perfInfoTable);
		
		
		perfData_Panel = new JPanel();
		perfData_Panel.setBackground(Color.WHITE);		
		perfData_Panel.setBounds(739, 276, 483, 382);
		perfData_Panel.setLayout(null);
		perfData_Panel.setVisible(false);
		actualPanel.add(perfData_Panel);
		
		perfDataInfo_Panel = new JPanel();
		perfDataInfo_Panel.setBackground(Color.WHITE);
		perfDataInfo_Panel.setBounds(739, 128, 483, 143);
		perfDataInfo_Panel.setBorder(new LineBorder(Color.BLACK, 2));
		perfDataInfo_Panel.setLayout(null);
		perfDataInfo_Panel.setVisible(false);
		actualPanel.add(perfDataInfo_Panel);
		
		JLabel perfName_label = new JLabel("성능명 : ");
		perfName_label.setForeground(Color.BLACK);
		perfName_label.setBackground(Color.WHITE);
		perfName_label.setHorizontalAlignment(SwingConstants.LEFT);
		perfName_label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		perfName_label.setBounds(12, 10, 459, 23);
		perfDataInfo_Panel.add(perfName_label);
		
		time = new JLabel("기준 시간부터");
		time.setForeground(Color.BLACK);
		time.setBackground(Color.WHITE);
		time.setHorizontalAlignment(SwingConstants.LEFT);
		time.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		time.setBounds(12, 40, 105, 24);
		time.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				MyCalendar.setTimeNow(year_comboBox, month_comboBox, day_comboBox, hour_comboBox, minute_comboBox, second_comboBox);
			}
		});
		perfDataInfo_Panel.add(time);
		
		year_comboBox = new JComboBox();
		year_comboBox.setForeground(Color.BLACK);
		year_comboBox.setBackground(Color.WHITE);		
		year_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		year_comboBox.setBounds(12, 73, 67, 24);
		year_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyCalendar.setLastDayComboBox(year_comboBox, month_comboBox, day_comboBox);
			}
		});
		perfDataInfo_Panel.add(year_comboBox);
		
		month_comboBox = new JComboBox();
		month_comboBox.setForeground(Color.BLACK);
		month_comboBox.setBackground(Color.WHITE);		
		month_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		month_comboBox.setBounds(94, 73, 52, 24);
		month_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyCalendar.setLastDayComboBox(year_comboBox, month_comboBox, day_comboBox);
			}
		});
		perfDataInfo_Panel.add(month_comboBox);
				
		day_comboBox = new JComboBox();
		day_comboBox.setForeground(Color.BLACK);
		day_comboBox.setBackground(Color.WHITE);
		day_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		day_comboBox.setBounds(162, 73, 52, 24);
		perfDataInfo_Panel.add(day_comboBox);
		
		hour_comboBox = new JComboBox();
		hour_comboBox.setForeground(Color.BLACK);
		hour_comboBox.setBackground(Color.WHITE);
		hour_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		hour_comboBox.setBounds(230, 73, 52, 24);
		perfDataInfo_Panel.add(hour_comboBox);
		
		minute_comboBox = new JComboBox();		
		minute_comboBox.setBackground(Color.WHITE);
		minute_comboBox.setForeground(Color.BLACK);
		minute_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		minute_comboBox.setBounds(298, 73, 52, 24);
		perfDataInfo_Panel.add(minute_comboBox);
		
		second_comboBox = new JComboBox();		
		second_comboBox.setForeground(Color.BLACK);
		second_comboBox.setBackground(Color.WHITE);
		second_comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		second_comboBox.setBounds(366, 73, 52, 24);
		perfDataInfo_Panel.add(second_comboBox);
		
		setTime_panel = new JPanel();
		setTime_panel.setBackground(Color.WHITE);
		setTime_panel.setBounds(12, 102, 465, 36);
		setTime_panel.setLayout(null);
		perfDataInfo_Panel.add(setTime_panel);
		
		setTime_label1 = new JLabel("기준 시간 이전부터");
		setTime_label1.setBackground(Color.WHITE);
		setTime_label1.setForeground(Color.BLACK);
		setTime_label1.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		setTime_label1.setBounds(0, 0, 134, 36);
		setTime_panel.add(setTime_label1);
		
		setTime_textField = new JTextField("1");
		setTime_textField.setHorizontalAlignment(SwingConstants.CENTER);
		setTime_textField.setBackground(Color.WHITE);
		setTime_textField.setForeground(Color.BLUE);
		setTime_textField.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		setTime_textField.setBounds(140, 7, 60, 26);
		setTime_textField.setBorder(new LineBorder(Color.BLACK, 2));
		setTime_textField.setColumns(10);
		setTime_textField.addKeyListener(new KeyAdapter() {						
			public void keyReleased(KeyEvent e) {
				String enteredTime = setTime_textField.getText().toString();
				
				try {
					int time = Integer.parseInt(enteredTime);
					
					if(time < 1) {
						throw new Exception("time exception");
					}else {
						setTime_textField.setForeground(Color.BLUE);
					}
					
				}catch(Exception ex) {
					setTime_textField.setForeground(Color.RED);
				}
				
			}
		});
		setTime_panel.add(setTime_textField);
		
		setTime_label2 = new JLabel("시간동안 수집한 데이터");
		setTime_label2.setForeground(Color.BLACK);
		setTime_label2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		setTime_label2.setBackground(Color.WHITE);
		setTime_label2.setBounds(205, 0, 165, 36);
		setTime_panel.add(setTime_label2);
		
		setTimeOk_Button = new JButton("확 인");
		setTimeOk_Button.setForeground(Color.BLACK);
		setTimeOk_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		setTimeOk_Button.setBackground(Color.WHITE);
		setTimeOk_Button.setBounds(385, 5, 74, 28);
		setTimeOk_Button.setFocusPainted(false);
		setTimeOk_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(selectedPerf == null) {
					return;
				}
				if(before_RadioButton.isSelected() || after_RadioButton.isSelected()) {
					if(setTime_textField.getForeground() == Color.RED) return;
				}
				
				String startTime = "";
				String endTime = "";
				
				if(before_RadioButton.isSelected()) {
					int beforeHour = Integer.parseInt(setTime_textField.getText().toString());
					
					long time = MyCalendar.getMilliseconds(year_comboBox, month_comboBox, day_comboBox, hour_comboBox, minute_comboBox, second_comboBox);
					 
					startTime = MyCalendar.sdf.format(MyCalendar.getCalcMilliseconds(time, beforeHour * -1));
					endTime = MyCalendar.sdf.format(time);
					
				}else if(after_RadioButton.isSelected()) {
					
				}else if(duration_radioButton.isSelected()){
					
				}else {
					
				}
				
				ArrayList<PerfData> list = RestAgent.getPerfRowData(true, selectedPerf.getIndex(), MK119_Lite_Panel.adminConsole, startTime, endTime);
				
				if(list != null) {
					Object[][] content = new Object[list.size()][];
					
					for(int i = 0; i < list.size(); i++) {
						PerfData data = list.get(i);
						content[i] = new Object[3];
						content[i][0] = i + 1;
						content[i][1] = data.getTimeString();
						content[i][2] = PerfData.getPerfLastContent(selectedPerf, data);
					}
					
					perfData_Table.setModel(new DefaultTableModel(
							new Object[][]{
								{null, null, null},
								{null, null, null},
								{null, null, null},
								{null, null, null},
								{null, null, null},
								{null, null, null},
							},
							new String[] { "순 서", "수집 시간", "수집 값"}) {
							boolean[] columnEditables = new boolean[] {
									false, // 값 : 수정 불가
									false, // 매핑 내용 : 수정 불가						
							};
							public boolean isCellEditable(int row, int column) {
								return columnEditables[column];
							}
					});
					setTableStyle(perfData_Table, PERF_DATA_TABLE);
				}// init Row Data Table
				
			}
		});
		setTime_panel.add(setTimeOk_Button);
				
		
		duration_panel = new JPanel();
		duration_panel.setBackground(Color.WHITE);
		duration_panel.setBounds(12, 100, 459, 36);
		duration_panel.setLayout(null);
//		perfDataInfo_Panel.add(duration_panel);
		
		JLabel seprarator1 = new JLabel("-");
		seprarator1.setHorizontalAlignment(SwingConstants.CENTER);
		seprarator1.setForeground(Color.BLACK);
		seprarator1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		seprarator1.setBackground(Color.WHITE);
		seprarator1.setBounds(70, 71, 34, 24);
		perfDataInfo_Panel.add(seprarator1);
		
		seprarator2 = new JLabel("-");
		seprarator2.setHorizontalAlignment(SwingConstants.CENTER);
		seprarator2.setForeground(Color.BLACK);
		seprarator2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		seprarator2.setBackground(Color.WHITE);
		seprarator2.setBounds(136, 71, 34, 24);
		perfDataInfo_Panel.add(seprarator2);
		
		seprarator3 = new JLabel(":");
		seprarator3.setHorizontalAlignment(SwingConstants.CENTER);
		seprarator3.setForeground(Color.BLACK);
		seprarator3.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		seprarator3.setBackground(Color.WHITE);
		seprarator3.setBounds(272, 71, 34, 24);
		perfDataInfo_Panel.add(seprarator3);
		
		seprarator4 = new JLabel(":");
		seprarator4.setHorizontalAlignment(SwingConstants.CENTER);
		seprarator4.setForeground(Color.BLACK);
		seprarator4.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		seprarator4.setBackground(Color.WHITE);
		seprarator4.setBounds(340, 71, 34, 24);
		perfDataInfo_Panel.add(seprarator4);
		
		before_RadioButton = new JRadioButton("최근 n시간");
		before_RadioButton.setForeground(Color.BLUE);
		before_RadioButton.setBackground(Color.WHITE);
		before_RadioButton.setHorizontalAlignment(SwingConstants.LEFT);
		before_RadioButton.setFocusPainted(false);
		before_RadioButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		before_RadioButton.setBounds(125, 42, 101, 23);
		before_RadioButton.setSelected(true);
		before_RadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if(before_RadioButton.isSelected()) {
					setTime_label1.setText("기준 시간 이전부터");
					before_RadioButton.setForeground(Color.BLUE);
					after_RadioButton.setForeground(Color.LIGHT_GRAY);
					duration_radioButton.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		perfDataInfo_Panel.add(before_RadioButton);
		
		after_RadioButton = new JRadioButton("이후 n시간");
		after_RadioButton.setSelected(true);
		after_RadioButton.setHorizontalAlignment(SwingConstants.LEFT);
		after_RadioButton.setForeground(Color.LIGHT_GRAY);
		after_RadioButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		after_RadioButton.setFocusPainted(false);
		after_RadioButton.setBackground(Color.WHITE);
		after_RadioButton.setBounds(230, 42, 101, 23);
		after_RadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(after_RadioButton.isSelected()) {
					setTime_label1.setText("기준 시간 이후부터");
					before_RadioButton.setForeground(Color.LIGHT_GRAY);
					after_RadioButton.setForeground(Color.BLUE);
					duration_radioButton.setForeground(Color.LIGHT_GRAY);
				}
				
			}
		});
		perfDataInfo_Panel.add(after_RadioButton);
		
		duration_radioButton = new JRadioButton("정해진 시간까지");
		duration_radioButton.setHorizontalAlignment(SwingConstants.LEFT);
		duration_radioButton.setFocusPainted(false);
		duration_radioButton.setForeground(Color.LIGHT_GRAY);
		duration_radioButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		duration_radioButton.setBackground(Color.WHITE);
		duration_radioButton.setBounds(335, 42, 140, 23);
		duration_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(duration_radioButton.isSelected()) {
					before_RadioButton.setForeground(Color.LIGHT_GRAY);
					after_RadioButton.setForeground(Color.LIGHT_GRAY);
					duration_radioButton.setForeground(Color.BLUE);
				}
			}
		});
		perfDataInfo_Panel.add(duration_radioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(before_RadioButton);
		group.add(after_RadioButton);
		group.add(duration_radioButton);
		
		
		perfData_ScrollPanel = new JScrollPane();
		perfData_ScrollPanel.setBackground(Color.WHITE);
		perfData_ScrollPanel.setBorder(new LineBorder(Color.BLACK, 2));
		perfData_ScrollPanel.setBounds(0, 0, 483, 382);
		perfData_Panel.add(perfData_ScrollPanel);
		
		perfData_Table = new JTable();
		perfData_Table.setModel(new DefaultTableModel(
				null,
				new String[] {"순 서", "수집 시간", "수집 값"}) {
				boolean[] columnEditables = new boolean[] {
						false, // 값 : 수정 불가
						false, // 매핑 내용 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		setTableStyle(perfData_Table, PERF_DATA_TABLE);
		perfData_ScrollPanel.setViewportView(perfData_Table);
		
		
		mappingLabel = new JLabel("");
		mappingLabel.setBackground(Color.WHITE);
		mappingLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mappingLabel.setForeground(Color.BLACK);
		mappingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		mappingLabel.setBounds(0, 288, 483, 26);
		perfInfo_Panel.add(mappingLabel);
		
		perfLabelInfoPanel = new JScrollPane();
		perfLabelInfoPanel.setBounds(0, 318, 483, 212);
		perfLabelInfoPanel.setBorder(new LineBorder(Color.BLACK, 2));
		perfInfo_Panel.add(perfLabelInfoPanel);
		
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
		searchPerf_ComboBox_1.setBounds(85, 60, 131, 30);
		searchPerf_ComboBox_1.setModel(new DefaultComboBoxModel(new String[] {
				PERF_NAME,
				PERF_INDEX,
				PERF_TYPE,
				(this.isProtocol) ? PERF_COUNTER : OID, 
				INTERVAL, 
				UNIT, 
				SCALE, 
				DATA_FORMAT
		}));		
		searchPerf_ComboBox_1.setSelectedIndex(0);
		actualPanel.add(searchPerf_ComboBox_1);
		
		searchPerf_ComboBox_2 = new JComboBox();
		searchPerf_ComboBox_2.setForeground(Color.BLACK);
		searchPerf_ComboBox_2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		searchPerf_ComboBox_2.setBackground(Color.WHITE);
		searchPerf_ComboBox_2.setBounds(85, 94, 131, 30);
		searchPerf_ComboBox_2.setModel(new DefaultComboBoxModel(new String[] {
				PERF_NAME,
				PERF_INDEX,
				PERF_TYPE,
				(this.isProtocol) ? PERF_COUNTER : OID, 
				INTERVAL, 
				UNIT, 
				SCALE, 
				DATA_FORMAT
		}));
		searchPerf_ComboBox_2.setSelectedIndex(3);
		actualPanel.add(searchPerf_ComboBox_2);
		
		
		searchPerf_textField_1 = new JTextField();
		searchPerf_textField_1.addFocusListener(Util.focusListener);
		searchPerf_textField_1.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField_1.setForeground(Color.BLACK);
		searchPerf_textField_1.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField_1.setColumns(10);
		searchPerf_textField_1.setBounds(220, 60, 318, 30);		
		actualPanel.add(searchPerf_textField_1);
		
		searchPerf_textField_2 = new JTextField();
		searchPerf_textField_2.addFocusListener(Util.focusListener);
		searchPerf_textField_2.setHorizontalAlignment(SwingConstants.LEFT);
		searchPerf_textField_2.setForeground(Color.BLACK);
		searchPerf_textField_2.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		searchPerf_textField_2.setColumns(10);
		searchPerf_textField_2.setBounds(220, 94, 318, 30);		
		actualPanel.add(searchPerf_textField_2);
				
		FacilityInfoLabel_1 = new JLabel();
		FacilityInfoLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		FacilityInfoLabel_1.setForeground(Color.BLACK);
		FacilityInfoLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		FacilityInfoLabel_1.setBackground(Color.WHITE);
		FacilityInfoLabel_1.setBounds(260, 1, 962, 30);		
		actualPanel.add(FacilityInfoLabel_1);
		
		FacilityInfoLabel_2 = new JLabel();
		FacilityInfoLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		FacilityInfoLabel_2.setForeground(Color.BLACK);
		FacilityInfoLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		FacilityInfoLabel_2.setBackground(Color.WHITE);
		FacilityInfoLabel_2.setBounds(260, 28, 962, 30);			
		actualPanel.add(FacilityInfoLabel_2);
		
		dbRefreshButton = new JButton("Database 최신화");
		dbRefreshButton.setForeground(Color.BLACK);
		dbRefreshButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dbRefreshButton.setFocusPainted(false);		
		dbRefreshButton.setBorder(UIManager.getBorder("Button.border"));
		dbRefreshButton.setBackground(Color.WHITE);
		dbRefreshButton.setBounds(875, 94, 160, 30);
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
		
		JButton formReset_Button = new JButton("Form 초기화");
		formReset_Button.setForeground(Color.BLACK);
		formReset_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		formReset_Button.setFocusPainted(false);
		formReset_Button.setBorder(UIManager.getBorder("Button.border"));
		formReset_Button.setBackground(Color.WHITE);
		formReset_Button.setBounds(543, 60, 184, 30);
		formReset_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchPerf_textField_1.setText(null);
				searchPerf_textField_2.setText(null);
				searchPerf_ComboBox_1.setSelectedIndex(0);
				searchPerf_ComboBox_2.setSelectedIndex(3);
				updatePerfListTable(perfListTable, false);
			}
		});
		actualPanel.add(formReset_Button);
		
		eventInfo_Button = new JButton("이벤트 정상");
		eventInfo_Button.setForeground(Color.BLACK);
		eventInfo_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		eventInfo_Button.setFocusPainted(false);		
		eventInfo_Button.setBorder(UIManager.getBorder("Button.border"));
		eventInfo_Button.setBackground(Color.WHITE);
		eventInfo_Button.setBounds(1038, 60, 184, 30);		
		
		// 이벤트 버튼 초기화
		MK119_Lite_Panel.initEventButton(eventInfo_Button, fac);		
		actualPanel.add(eventInfo_Button);
		
		linkMK119_Button = new JButton(" 연동");
		linkMK119_Button.setIcon(new Util().getMK2Resource());
		linkMK119_Button.setForeground(Color.BLACK);
		linkMK119_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		linkMK119_Button.setFocusPainted(false);
		linkMK119_Button.setBorder(UIManager.getBorder("Button.border"));
		linkMK119_Button.setBackground(Color.WHITE);
		linkMK119_Button.setBounds(1038, 94, 184, 30);
		linkMK119_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!LinkMK119Frame.isExist) {
					new LinkMK119Frame(MK119_Lite_Panel.linkMK119_Protocol, MK119_Lite_Panel.linkMK119_PerfData);
				}
				
			}
		});
		actualPanel.add(linkMK119_Button);
		
		updatePerfData = new JButton("성능 데이터 연동 전");
		updatePerfData.setForeground(Color.BLACK);
		updatePerfData.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		updatePerfData.setFocusPainted(false);
		updatePerfData.setBorder(UIManager.getBorder("Button.border"));
		updatePerfData.setBackground(Color.WHITE);
		updatePerfData.setBounds(543, 94, 184, 30);		
		updatePerfData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter(true);
			}
		});
		actualPanel.add(updatePerfData);
		
		rcuInfo_Button = new JButton("RCU");
		rcuInfo_Button.setForeground(Color.BLACK);
		rcuInfo_Button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		rcuInfo_Button.setFocusPainted(false);
		rcuInfo_Button.setBorder(UIManager.getBorder("Button.border"));
		rcuInfo_Button.setBackground(Color.WHITE);
		rcuInfo_Button.setBounds(875, 60, 160, 30);
		rcuInfo_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fac.isConnRCU() && fac.getRcu() != null) new RcuInfoFrame(fac.getRcu());
			}
		});
		initRcuInfoButton(); 
		actualPanel.add(rcuInfo_Button);
		
		JRadioButton perfInfo_RadioButton = new JRadioButton("성능 정보");
		perfInfo_RadioButton.setForeground(Color.BLACK);
		perfInfo_RadioButton.setBackground(Color.WHITE);
		perfInfo_RadioButton.setFocusPainted(false);
		perfInfo_RadioButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		perfInfo_RadioButton.setBounds(746, 60, 121, 30);
		perfInfo_RadioButton.setSelected(true);
		perfInfo_RadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				perfInfo_Panel.setVisible(true);
				perfData_Panel.setVisible(false);
				perfDataInfo_Panel.setVisible(false);
			}
		});
		actualPanel.add(perfInfo_RadioButton);
		
		perfValue_RadioButton = new JRadioButton("수집 데이터");
		perfValue_RadioButton.setForeground(Color.BLACK);
		perfValue_RadioButton.setBackground(Color.WHITE);
		perfValue_RadioButton.setFocusPainted(false);
		perfValue_RadioButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		perfValue_RadioButton.setBounds(746, 94, 121, 30);		
		perfValue_RadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				perfInfo_Panel.setVisible(false);
				perfData_Panel.setVisible(true);
				perfDataInfo_Panel.setVisible(true);
			}
		});
		actualPanel.add(perfValue_RadioButton);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(perfInfo_RadioButton);
		buttonGroup.add(perfValue_RadioButton);
		
		// ESC : Close Listener
		MyKeyListener myKeyListener = new MyKeyListener();
		searchPerf_ComboBox_1.addKeyListener(myKeyListener);
		searchPerf_ComboBox_2.addKeyListener(myKeyListener);
		searchPerf_textField_1.addKeyListener(myKeyListener);
		searchPerf_textField_2.addKeyListener(myKeyListener);
		
		KeyAdapter close = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();					
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();					
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		perfListTable.addKeyListener(close);
		perfInfoTable.addKeyListener(close);
		perfLabelTable.addKeyListener(close);
		
		initCopyAdapter(fac.getName().trim());
				
		// 수집 데이터 기준 시간 초기화
		MyCalendar.setTimeNow(year_comboBox, month_comboBox, day_comboBox, hour_comboBox, minute_comboBox, second_comboBox);				
		
		// 장비 정보를 표시
		updateFacilityInfo();
		
		// 테이블 로드
		updatePerfListTable(perfListTable, isConnectRestAPI());
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	@Override
	public void dispose() {
		FacilityInfoFrame.isExist = false;
		super.dispose();
	}
	
	public void initRcuInfoButton() {
		if(fac.isConnRCU()) {
			rcuInfo_Button.setEnabled(true);			
			rcuInfo_Button.setText("연결된 RCU 확인");
		}else {
			rcuInfo_Button.setEnabled(false);			
			rcuInfo_Button.setText("연결된 RCU 없음");
		}
	}
		
	// ************ XML Reload *******************************************
	public void refreshDB() {
		
		MK119_Lite_Panel.resetForm(true, false);
				
		if(MK119_Lite_Panel.serverMap.containsKey(fac.getIndex())) {
						
			this.fac = (Facility)MK119_Lite_Panel.serverMap.get(fac.getIndex());
			this.isProtocol = fac.isProtocol();
			
			MK119_Lite_Panel.initEventButton(eventInfo_Button, fac);
			initRcuInfoButton();
			initCopyAdapter(fac.getName().trim());
			updateFacilityInfo();
			
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
		
		// 성능 리스트 테이블 업데이트
		updatePerfListTable(perfListTable, false);
		
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
	public void updatePerfListTable(JTable table, boolean updatePerfData) {
		
		if (table == null || perfs == null) return;
		
		// REST API 호출
		if(updatePerfData && isConnectRestAPI()) {
			perfRealTimeDataMap = RestAgent.getFacilityPerfDataMap(true, fac.getIndex(), MK119_Lite_Panel.adminConsole);
		}
		
		Object[][] content = new Object[perfs.size()][];

		if(isConnectRestAPI()) {
			for (int i = 0; i < perfs.size(); i++) {
				Perf perf = perfs.get(i);
				content[i] = new Object[4];
				content[i][0] = i + 1; // 순 서
				content[i][1] = perf;
				// 성능 값
				if(perfRealTimeDataMap != null) {
					PerfData data = perfRealTimeDataMap.get(perf.getIndex());
					
					if(data != null && !data.getValue().equals("-")) {
						content[i][2] = PerfData.getPerfLastContent(perf, data);
					}else {
						content[i][2] = "-";
					}
					
					try {
						content[i][3] = data.getTimeString();
					}catch(Exception e) {
						content[i][3] = "-";
					}
				}else {
					content[i][2] = "-";
					content[i][3] = "-";
				}
			}
		}else {
			for (int i = 0; i < perfs.size(); i++) {
				Perf perf = perfs.get(i);
				content[i] = new Object[2];
				content[i][0] = i + 1; // 순 서
				content[i][1] = perf;
			}
		}
		
		String[] header = (isConnectRestAPI()) ? new String[] { "순 서", "성 능", "최종값", "수집 시간" } : new String[] { "순 서", "성 능" };
		
		table.setModel(new DefaultTableModel(
			content, 
			header) {
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
		content[3][0] = (this.isProtocol) ? "성능 카운터" : "OID";
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
		if(tableType == PERF_LIST_TABLE) {
			table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));	
		}else if(tableType == PERF_DATA_TABLE) {
			table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
		}else {
			table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 17));
		}
		
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
		}else if(tableType == PERF_DATA_TABLE) {
			table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			table.setRowHeight(25); 
		}else {
			table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			table.setRowHeight(30); 
		}
		
		// 테이블 셀 크기 설정
		if(tableType == PERF_LIST_TABLE) {
			
			if(isConnectRestAPI()) {
				// 성능 리스트 테이블
				table.getColumnModel().getColumn(0).setPreferredWidth(70); // 순 서
				table.getColumnModel().getColumn(1).setPreferredWidth(400); // 성 능
				table.getColumnModel().getColumn(2).setPreferredWidth(150); // 마지막 수집값
				table.getColumnModel().getColumn(3).setPreferredWidth(180); // 수집 시간			
			}else {
				table.getColumnModel().getColumn(0).setPreferredWidth(30); // 순 서
				table.getColumnModel().getColumn(1).setPreferredWidth(600); // 성 능
			}
			
		}else if(tableType == PERF_INFO_TABLE) {
			// 성능 정보 테이블
			table.getColumnModel().getColumn(0).setPreferredWidth(3); // 필 드		
			table.getColumnModel().getColumn(1).setPreferredWidth(180); // 내 용
		}else if(tableType == PERF_LABEL_TABLE) {
			// 성능 레이블 테이블
			table.getColumnModel().getColumn(0).setPreferredWidth(4); // 값	
			table.getColumnModel().getColumn(1).setPreferredWidth(350); // 매핑 내용		
		}else if(tableType == PERF_DATA_TABLE) {
			// 성능 데이터 테이블
			table.getColumnModel().getColumn(0).setPreferredWidth(80); // 순 서
			table.getColumnModel().getColumn(1).setPreferredWidth(200); // 수집 시간
			table.getColumnModel().getColumn(2).setPreferredWidth(250); // 성능 값
		}
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
		
		if(tableType == PERF_LIST_TABLE && isConnectRestAPI()) {
			tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 최종값
			tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 수집 시간
			
		}else if(tableType == PERF_LIST_TABLE && !isConnectRestAPI()) {
			// do nothing
			
		}else if(tableType == PERF_LABEL_TABLE) {
			tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
			
		}else if(tableType == PERF_DATA_TABLE) {
			tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
			tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer);
		}else {
			tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
		}
	}
		
	//******************** 테이블 필터링 관련 *********************************************************************
	public void doTableFilter(boolean updatePerfData) {
		if(searchPerf_textField_1 == null || searchPerf_textField_2 == null) return;
		
		ArrayList<FmsPerfItem> filterPerf = new ArrayList<FmsPerfItem>();
		String text_1 = searchPerf_textField_1.getText();
		String text_2 = searchPerf_textField_2.getText();
		
		boolean noSearch_1 = (text_1 == null || text_1.length() == 0 || text_1.equals(""));
		boolean noSearch_2 = (text_2 == null || text_2.length() == 0 || text_2.equals(""));
		
		if(noSearch_1 && noSearch_2) {
			updatePerfListTable(perfListTable, updatePerfData);
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
				filterPerf.add(perf);
			}// AND Operation isContains 1, 2
			
		}// for loop
		
		
		// REST API 호출
		if(updatePerfData && isConnectRestAPI()) {
			perfRealTimeDataMap = RestAgent.getFacilityPerfDataMap(true, fac.getIndex(), MK119_Lite_Panel.adminConsole);
		}
		
		Object[][] content = new Object[filterPerf.size()][];
		
		if(isConnectRestAPI()) {
			for (int i = 0; i < filterPerf.size(); i++) {
				Perf perf = filterPerf.get(i);
				content[i] = new Object[4];
				content[i][0] = i + 1; // 순 서
				content[i][1] = perf;
				// 성능 값
				if(perfRealTimeDataMap != null) {
					PerfData data = perfRealTimeDataMap.get(perf.getIndex());
					
					if(data != null && !data.getValue().equals("-")) {
						content[i][2] = PerfData.getPerfLastContent(perf, data);
					}else {
						content[i][2] = "-";
					}
					
					try {
						content[i][3] = data.getTimeString();
					}catch(Exception e) {
						content[i][3] = "-";
					}				
				}else {
					content[i][2] = "-";
					content[i][3] = "-";
				}
			}
		}else {
			for (int i = 0; i < filterPerf.size(); i++) {
				Perf perf = filterPerf.get(i);
				content[i] = new Object[2];
				content[i][0] = i + 1; // 순 서
				content[i][1] = perf;
			}
		}
		
		String[] header = (isConnectRestAPI()) ? new String[] { "순 서", "성 능", "최종값", "수집 시간" } : new String[] { "순 서", "성 능" };
		
		perfListTable.setModel(new DefaultTableModel(
			content, 
			header) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});

		setTableStyle(perfListTable, PERF_LIST_TABLE);
	}
	
	
	
	
	public void updateFacilityInfo() {
		String pName = null;
		if(MK119_Lite_Panel.linkMK119_Protocol) {
			Protocol p = MK119_Lite_Panel.protocolMap.get(fac.getProtocolKey());
			if(p != null) {
				pName = p.getName();
			}else {
				pName = "Unknown Protocol";
			}
		}else {
			pName = " - ";
		}
		
		String facInfo_1 = "";
		facInfo_1 += "<html>";
		facInfo_1 += Util.colorBlue("장비 정보 : ") + fac.getName();
		facInfo_1 += "&nbsp;&nbsp;";
		facInfo_1 += "( ";
		facInfo_1 += Util.colorGreen(fac.getTypeString() + Util.colorRed(" / ") + fac.getConnMethod() + Util.colorRed(" / ") + pName);
		facInfo_1 += Util.colorGreen(Util.colorRed(" / "));
		facInfo_1 += (fac.getState().equalsIgnoreCase("통신 오류")) ? Util.colorRed(fac.getState()) : Util.colorGreen(fac.getState());
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
	}
	
	
	public void initCopyAdapter(String content) {
		MouseAdapter copyAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				 if (e.getButton() == 1) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // 왼쪽 클릭				 
				 if (e.getButton() == 1 && e.getClickCount() == 2) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // 더블 클릭
				 if (e.getButton() == 3) { 
					 StringSelection data = new StringSelection(content);
					 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					 clipboard.setContents(data, data);
				 } // 오른쪽 클릭
			}
		};
		
		if(currentFunction != null) currentFunction.addMouseListener(copyAdapter);
		if(FacilityInfoLabel_1 != null) FacilityInfoLabel_1.addMouseListener(copyAdapter);
		if(FacilityInfoLabel_2 != null) FacilityInfoLabel_2.addMouseListener(copyAdapter);
	}
	
	// 사용자 정의 키 이벤트 리스너
	class MyKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			try {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();					
				}else if(e.getKeyCode() == KeyEvent.VK_F5) {					
					doTableFilter(true);
				}else {
					doTableFilter(false);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void keyReleased(KeyEvent e) {
			try {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();					
				}else {
					doTableFilter(false);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static boolean isConnectRestAPI() {
		boolean isConnect = (MK119_Lite_Panel.linkMK119_PerfData && MK119_Lite_Panel.adminConsole != null);
		
		if(updatePerfData != null) updatePerfData.setEnabled(isConnect);
		if(perfValue_RadioButton != null) perfValue_RadioButton.setEnabled(isConnect);
		if(updatePerfData != null ) {
			updatePerfData.setEnabled(isConnect);
			updatePerfData.setText(isConnect ? "성능 데이터 최신화" : "성능 데이터 연동 전");
		}
		
		return isConnect;
	}
}
