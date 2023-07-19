package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import common.modbus.ModbusPointLoader;
import common.modbus.ModbusWatchPoint;
import common.util.FontManager;
import common.util.TableUtil;
import moon.Moon;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class ImportModbusPointFrame extends JFrame {

	private static ArrayList<ModbusWatchPoint> pointList = new ArrayList<ModbusWatchPoint>();
	
	private Color mkColor = new Color(237, 76, 55);
	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton downloadTemplateButton;
	private JButton resetButton;
	private JButton addPointButton;
	private JTable table;

	private static JRadioButton mk_V4_RaidoButton;
	private static JRadioButton mk_V10_RaidoButton;
	
	private JButton upload_protocol;
	private JButton upload_xml;
	private JButton upload_excel;
	private JButton download_template;
	
	private static JTextField search_TextField;
	private static JTextField dragAndDropField;
	private static JTable point_table;
	
	private static JCheckBox useFilter;
	private static JComboBox fc_filter;
	private static JComboBox dataType_filter;
	
	private ActionListener mkVerionListener = null;
	private KeyAdapter saveAndCloseAdpter;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					
//					new AddModbusPointFrame();
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	/**
	 * Create the frame.
	 */
	public ImportModbusPointFrame() {
		isExist = true;
		setTitle("Import Modbus Point");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 1138, 722);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY, 8));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Import Modbus Point");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 380, 55);
		actualPanel.add(currentFunction);
				
		downloadTemplateButton = new JButton();
		downloadTemplateButton.setForeground(Color.BLACK);
		downloadTemplateButton.setText("템플릿 다운로드");
		downloadTemplateButton.setFont(FontManager.getFont(Font.BOLD, 17));
		downloadTemplateButton.setFocusPainted(false);		
		downloadTemplateButton.setBorder(UIManager.getBorder("Button.border"));
		downloadTemplateButton.setBackground(Color.WHITE);
		downloadTemplateButton.setBounds(629, 11, 165, 36);		
		downloadTemplateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String mkVersion = mk_V4_RaidoButton.isSelected() ? "V4" : "V10";
					String fileName = "Modbus";
					
					if(mkVersion.equals("V10")) {
						StringBuilder msg = new StringBuilder();
						msg.append("<font color='Green'>MK119 V10 Template Type Selection</font>\n");
						msg.append("MK119 V10 템플릿의 종류를 선택해주세요" + Util.separator + Util.separator +"\n");

						int menu = Util.showOption(msg.toString(), new String[] { "  Modbus  ", "  PLC  "}, JOptionPane.QUESTION_MESSAGE);
						switch (menu) {
							case 0: // 첫 번째 버튼 : Modbus
								fileName = "Modbus";
								break;
							case 1: // 두 번째 버튼 : PLC
								fileName = "PLC";
								break;
							default :
								// 템플릿 다운로드 취소
								return;
						}
					}
					
					String filePath = String.format("%s\\%s\\%s\\%s\\%s.xlsx", 
							MainFrame.getCurrentPath(),
							"template",
							mkVersion,
							Moon.currentLanguage,
							fileName
							);
					
					File file = new File(filePath);
					if(!file.exists()) {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
						sb.append("아래의 경로에 템플릿 파일이 존재하지 않습니다" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + file.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String downloadPath = Util.getFilePath();
				
					if(downloadPath != null) {
						downloadPath += ".xlsx";
						File downloadFile =new File(downloadPath);
						FileUtil.copyFile(file, downloadFile);
						if(downloadFile.exists()) {
							StringBuilder sb = new StringBuilder();
							sb.append(Util.colorGreen("Template File Download Successful") + Util.separator + "\n");
							sb.append("아래의 경로에 템플릿 파일을 다운로드 완료하였습니다" + Util.separator + Util.separator + "\n\n");
							sb.append(Util.colorBlue("Path") + " : " + downloadFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
							Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
							return;
						}					
					}else {
						return;
					}
				}catch(Exception exception) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Failed to Download Template File") + Util.separator + "\n");
					sb.append("처리 할 수 없는 예외가 발생하여 템플릿 파일 다운로드에 실패하였습니다" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorRed("Exception Message") + " : " + exception.getMessage() + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		actualPanel.add(downloadTemplateButton);
		
		addPointButton = new JButton();
		addPointButton.setText("포인트 등록");
		addPointButton.setForeground(Color.BLUE);
		addPointButton.setFont(FontManager.getFont(Font.BOLD, 17));
		addPointButton.setFocusPainted(false);		
		addPointButton.setBorder(UIManager.getBorder("Button.border"));
		addPointButton.setBackground(Color.WHITE);
		addPointButton.setBounds(800, 11, 150, 36);
		addPointButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ModbusWatchPoint> selectedPointList = getSelectedModbusPoint(point_table);
	
				if (selectedPointList == null || selectedPointList.size() < 1) {
					return;
				} else {
	
					// 사용자에게 선택되어 모드버스 모니터에 추가되는 포인트는 해당 프레임의 관리 리스트에서 삭제된다 (중복 포인트 등록 방지)
					for (ModbusWatchPoint wp : selectedPointList) {
						pointList.remove(wp);
					}
	
					ModbusMonitor_Panel.addPointList(selectedPointList);
					ModbusMonitor_Panel.doTableFilter(false);
					ExportModbusPointFrame.updateTable();
					doTableFilter();
					search_TextField.requestFocus();
				}
			}
		});
		actualPanel.add(addPointButton);
		
		resetButton = new JButton();
		resetButton.setText("포인트 초기화");
		resetButton.setForeground(Color.RED);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 17));
		resetButton.setFocusPainted(false);		
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		resetButton.setBackground(Color.WHITE);
		resetButton.setBounds(956, 11, 150, 36);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pointList.clear();
				doTableFilter();
			}
		});
		actualPanel.add(resetButton);
		
		JPanel backGround_Panel = new JPanel();
		backGround_Panel.setBackground(Color.LIGHT_GRAY);
		backGround_Panel.setBounds(10, 57, 1096, 610);
		actualPanel.add(backGround_Panel);
		backGround_Panel.setLayout(null);
		
		JPanel mk119Version_Panel = new JPanel();
		mk119Version_Panel.setBorder(new LineBorder(Color.BLACK, 2));
		mk119Version_Panel.setBackground(Color.WHITE);
		mk119Version_Panel.setBounds(12, 10, 200, 84);
		backGround_Panel.add(mk119Version_Panel);
		mk119Version_Panel.setLayout(null);
		
		mkVerionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mk_V4_RaidoButton.isSelected()) {
					mk_V4_RaidoButton.setForeground(mkColor);
					mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
					
					upload_protocol.setEnabled(true);
					upload_xml.setEnabled(true);
					upload_excel.setEnabled(true);
				}else {
					mk_V4_RaidoButton.setForeground(Color.LIGHT_GRAY);
					mk_V10_RaidoButton.setForeground(mkColor);
					
					upload_protocol.setEnabled(false);
					upload_xml.setEnabled(false);
					upload_excel.setEnabled(true);
				}
				
			}
		};
		
		mk_V4_RaidoButton = new JRadioButton("MK119  V4");
		mk_V4_RaidoButton.setSelected(true);
		mk_V4_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V4_RaidoButton.setFont(FontManager.getFont(Font.BOLD, 20));
		mk_V4_RaidoButton.setForeground(mkColor);
		mk_V4_RaidoButton.setBackground(Color.WHITE);
		mk_V4_RaidoButton.setBounds(12, 13, 180, 23);
		mk_V4_RaidoButton.setFocusPainted(false);
		mk_V4_RaidoButton.addActionListener(mkVerionListener);
		mk119Version_Panel.add(mk_V4_RaidoButton);
		
		mk_V10_RaidoButton = new JRadioButton("MK119  V10");
		mk_V10_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V10_RaidoButton.setFont(FontManager.getFont(Font.BOLD, 20));
		mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
		mk_V10_RaidoButton.setBackground(Color.WHITE);
		mk_V10_RaidoButton.setBounds(12, 47, 180, 23);
		mk_V10_RaidoButton.setFocusPainted(false);
		mk_V10_RaidoButton.addActionListener(mkVerionListener);
		mk119Version_Panel.add(mk_V10_RaidoButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(mk_V4_RaidoButton);
		group.add(mk_V10_RaidoButton);
		
		JPanel uploadMethod_Panel = new JPanel();
		uploadMethod_Panel.setBorder(new LineBorder(Color.BLACK, 2));
		uploadMethod_Panel.setBackground(Color.WHITE);
		uploadMethod_Panel.setBounds(224, 10, 860, 84);
		backGround_Panel.add(uploadMethod_Panel);
		uploadMethod_Panel.setLayout(null);
		
		upload_protocol = new JButton(" Protocol");
		upload_protocol.setForeground(Color.BLACK);
		upload_protocol.setFont(FontManager.getFont(Font.BOLD, 20));
		upload_protocol.setFocusPainted(false);
		upload_protocol.setIcon(new Util().getFolder2Image());
		upload_protocol.setBackground(Color.WHITE);
		upload_protocol.setBounds(12, 10, 222, 66);
		upload_protocol.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.showOnionDirCheck(false, "watchPoint");
				dispose();
			}
		});
		uploadMethod_Panel.add(upload_protocol);
		
		upload_xml = new JButton( "  XML");
		upload_xml.setForeground(Color.BLACK);
		upload_xml.setFont(FontManager.getFont(Font.BOLD, 20));
		upload_xml.setFocusPainted(false);
		upload_xml.setIcon(new Util().getXMLImage());
		upload_xml.setBackground(Color.WHITE);
		upload_xml.setBounds(246, 10, 193, 66);
		upload_xml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pointUpload();
			}
		});
		uploadMethod_Panel.add(upload_xml);
		
		upload_excel = new JButton("  Excel");
		upload_excel.setForeground(Color.BLACK);
		upload_excel.setFont(FontManager.getFont(Font.BOLD, 20));
		upload_excel.setFocusPainted(false);
		upload_excel.setIcon(new Util().getExcelImage());
		upload_excel.setBackground(Color.WHITE);
		upload_excel.setBounds(451, 10, 196, 66);
		upload_excel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pointUpload();
			}
		});
		uploadMethod_Panel.add(upload_excel);
		
		dragAndDropField = new JTextField("File Drag & Drop");
		dragAndDropField.setBackground(Color.WHITE);
		dragAndDropField.setForeground(new Color(0, 128, 0));
		dragAndDropField.setHorizontalAlignment(SwingConstants.CENTER);
		dragAndDropField.setEditable(false);
		dragAndDropField.setFont(FontManager.getFont(Font.BOLD, 18));
		dragAndDropField.setBounds(659, 10, 189, 66);
		dragAndDropField.setColumns(10);
		dragAndDropField.setBorder(new LineBorder(Color.BLACK, 2));
		dragAndDropField.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						
						if(file != null && file.exists()) {
							
							int mkVersion = mk_V4_RaidoButton.isSelected() ? 4 : 10;
							
							ArrayList<ModbusWatchPoint> modbusWps  = ModbusPointLoader.load(mkVersion, file);
							
							if(modbusWps != null && modbusWps.size() > 0) {
								resetTable(point_table);
								pointList = modbusWps;								
								addRecord(point_table, pointList);
								setTableStyle(point_table);
								
								// 정상적으로 하나 이상의 모드버스 포인트를 읽었을 경우 메소드를 종료한다
								return;
							}
							
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		uploadMethod_Panel.add(dragAndDropField);
		
		JLabel lblNewLabel = new JLabel("검 색");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(FontManager.getFont(Font.BOLD, 18));
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(25, 110, 76, 28);
		backGround_Panel.add(lblNewLabel);
		
		search_TextField = new JTextField();
		search_TextField.setBounds(84, 104, 430, 39);
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setForeground(Color.BLACK);
		search_TextField.setBackground(Color.WHITE);
		search_TextField.setFont(FontManager.getFont(Font.PLAIN, 17));
		search_TextField.setColumns(10);
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.addKeyListener(new KeyAdapter() {
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
		backGround_Panel.add(search_TextField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						
						if(file != null && file.exists()) {
							
							int mkVersion = mk_V4_RaidoButton.isSelected() ? 4 : 10;
							
							ArrayList<ModbusWatchPoint> modbusWps  = ModbusPointLoader.load(mkVersion, file);
							
							if(modbusWps != null && modbusWps.size() > 0) {
								resetTable(point_table);
								pointList = modbusWps;								
								addRecord(point_table, pointList);
								setTableStyle(point_table);
								
								// 정상적으로 하나 이상의 모드버스 포인트를 읽었을 경우 메소드를 종료한다
								return;
							}
							
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(12, 147, 1072, 454);
		backGround_Panel.add(scrollPane);
		
		point_table = new JTable();
		point_table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { } // 왼쪽 클릭
				if (e.getButton() == 1 && e.getClickCount() == 2) { 
					// 왼쪽 버튼 더블 클릭
//					int row = point_table.getSelectedRow();
//					ModbusWatchPoint wp = (ModbusWatchPoint) point_table.getValueAt(row, 1);
//					ModbusWatchPoint.showInfo(wp);
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = point_table.getSelectedRow();
					ModbusWatchPoint wp = (ModbusWatchPoint) point_table.getValueAt(row, 1);
					ModbusWatchPoint.showInfo(wp);
				}
			}
		});
		scrollPane.setViewportView(point_table);
		resetTable(point_table);
		
		useFilter = new JCheckBox(" 필 터");		
		useFilter.setFocusPainted(false);
		useFilter.setForeground(Color.BLACK);
		useFilter.setHorizontalAlignment(SwingConstants.LEFT);
		useFilter.setFont(FontManager.getFont(Font.BOLD, 18));
		useFilter.setBackground(Color.LIGHT_GRAY);
		useFilter.setBounds(535, 110, 83, 28);
		useFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(useFilter.isSelected()) {
					fc_filter.setEnabled(true);
					dataType_filter.setEnabled(true);
				}else {
					fc_filter.setEnabled(false);
					dataType_filter.setEnabled(false);
				}
				
				doTableFilter();
			}
		});
		backGround_Panel.add(useFilter);
		
		fc_filter = new JComboBox();
		fc_filter.setEnabled(false);
		fc_filter.setForeground(Color.BLACK);
		fc_filter.setModel(new DefaultComboBoxModel(
				new String[] {
						"ALL", 						
						"FC 01", 
						"FC 02", 
						"FC 03", 
						"FC 04"
						}));
		fc_filter.setFont(FontManager.getFont(Font.BOLD, 17));
		fc_filter.setBackground(Color.WHITE);
		fc_filter.setBounds(618, 104, 90, 39);
		fc_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter();
			}
		});
		backGround_Panel.add(fc_filter);
		
		dataType_filter = new JComboBox();
		dataType_filter.setForeground(Color.BLACK);
		dataType_filter.setEnabled(false);
		dataType_filter.setMaximumRowCount(20);
		dataType_filter.setModel(new DefaultComboBoxModel(
				new String[] {
						"ALL",
						"",
						"BINARY",
						"",
						"TWO BYTE INT SIGNED", 
						"TWO BYTE INT UNSIGNED",
						"",						
						"FOUR BYTE INT SIGNED", 
						"FOUR BYTE INT UNSIGNED",
						"FOUR BYTE INT SIGNED SWAPPED",
						"FOUR BYTE INT UNSIGNED SWAPPED",
						"",
						"FOUR BYTE FLOAT",
						"FOUR BYTE FLOAT SWAPPED",
						"",
						"EIGHT BYTE INT SIGNED",
						"EIGHT BYTE FLOAT"
						}));
		dataType_filter.setFont(FontManager.getFont(Font.BOLD, 17));
		dataType_filter.setBackground(Color.WHITE);
		dataType_filter.setBounds(714, 104, 370, 39);
		dataType_filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTableFilter();
			}
		});
		backGround_Panel.add(dataType_filter);
		
		addKeyAdapter();
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
		
		doTableFilter();
		
		search_TextField.requestFocus();
	}

	@Override
	public void dispose() {
		isExist = false;
		super.dispose();
	}
	
	public static void resetTable(JTable table) {
		table.setModel(new DefaultTableModel(
				null,
				new String[] {
					"순 서",
					"모드버스 포인트",
					"기능코드",
					"Register",
					"Modbus",
					"데이터 타입"
				}
		) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});
		
		setTableStyle(table);
	}
	
	public static void setTableStyle(JTable table) {
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// 셀 이동 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(30); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // 성능명
		table.getColumnModel().getColumn(2).setPreferredWidth(50); // 기능 코드
		table.getColumnModel().getColumn(3).setPreferredWidth(60); // 레지스터 주소
		table.getColumnModel().getColumn(4).setPreferredWidth(60); // 모드버스 주소
		table.getColumnModel().getColumn(5).setPreferredWidth(250); // 데이터 타입
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 성능명 
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 레지스터 주소
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 모드버스 주소
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 데이터 타입
	}
	
	
	
	
	public void pointUpload() {
		try {
			String path = Util.getFilePath();
			
			if (path == null || path.length() < 1) {
				return;
			}else {
				File xmlFile = new File(path);
				
				if(xmlFile != null && xmlFile.exists()) {
					
					int mkVersion = mk_V4_RaidoButton.isSelected() ? 4 : 10;
					
					ArrayList<ModbusWatchPoint> modbusWps  = ModbusPointLoader.load(mkVersion, xmlFile);
					
					if(modbusWps != null && modbusWps.size() > 0) {
						resetTable(point_table);
						pointList = modbusWps;
						addRecord(point_table, pointList);
						setTableStyle(point_table);						
						
						// 정상적으로 하나 이상의 모드버스 포인트를 읽었을 경우 메소드를 종료한다
						return;
					}
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean pointUpload(File file) {
		try {
			if (file != null && file.exists()) {

				int mkVersion = mk_V4_RaidoButton.isSelected() ? 4 : 10;

				ArrayList<ModbusWatchPoint> modbusWps = ModbusPointLoader.load(mkVersion, file);

				if (modbusWps != null && modbusWps.size() > 0) {
					resetTable(point_table);
					pointList = modbusWps;
					addRecord(point_table, pointList);
					setTableStyle(point_table);
					
					// 정상적으로 하나 이상의 모드버스 포인트를 읽었을 경우 true 리턴
					return true;
				}
			}
			
			return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 	레코드 추가
	 */
	public static void addRecord(JTable table, ArrayList<ModbusWatchPoint> modbusWps) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < modbusWps.size(); i++) {
				
				ModbusWatchPoint modbusWp = modbusWps.get(i);
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// 테이블의 행 개수가 0개 일 경우 : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // 순 서
				/* column[1] */ record.add(modbusWp); // 성능명
				/* column[2] */ record.add(modbusWp.getFunctionCode());  // 기능코드
				/* column[3] */ record.add(modbusWp.getRegisterAddrHexString());  // 레지스터 주소
				/* column[4] */ record.add(modbusWp.getModbusAddrString()); // 모드버스 주소
				/* column[5] */ record.add(modbusWp.getDataType()); // 데이터 타입
				
				model.addRow(record);
			}
			
			if(pointList != null) {
				int total = pointList.size();
				int searched = table.getRowCount();
				String text = String.format("모드버스 포인트  ( %d / %d )", searched, total);
				TableUtil.setTableHeader(table, 1, text);
			}else {
				TableUtil.setTableHeader(table, 1, "모드버스 포인트");
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public static void fill(ArrayList<ModbusWatchPoint> list, ModbusWatchPoint[] array) {
		list.clear();
		for(ModbusWatchPoint wp : array) {
			list.add(wp);
		}
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Import Modbus Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Import Modbus Point 프레임이 이미 열려있습니다" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public static ArrayList<ModbusWatchPoint> getSelectedModbusPoint(JTable table) {
		ArrayList<ModbusWatchPoint> selectedPointlist = new ArrayList<ModbusWatchPoint>();
		int[] selectedIndex = table.getSelectedRows();
		for(int i = 0; i < selectedIndex.length; i++) {
			ModbusWatchPoint wp = (ModbusWatchPoint) table.getValueAt(selectedIndex[i], 1);
			selectedPointlist.add(wp);
		}
		return selectedPointlist;
	}
	
	public void doTableFilter() {
		if(search_TextField == null && useFilter == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		if(noSearch && !useFilter.isSelected()) {
			resetTable(point_table);
			addRecord(point_table, pointList);
			return;
		}
		
		if(!noSearch) {
			text = text.toUpperCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toUpperCase();
				
				if(text.contains(",")) {
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						if(searchElement.contains(token)) {
							isContain = true;
						}
					}
				}else if(searchElement.contains(text)) {
					isContain = true;
				}
			}else {
				isContain = true;
			}
			
			if(useFilter.isSelected()) {
				boolean fcPass = false;
				boolean dataTypePass = false;
				
				if( !(fc_filter.getSelectedItem().toString().equalsIgnoreCase("") || fc_filter.getSelectedItem().toString().equalsIgnoreCase("ALL")) ) { 
					int fc = Integer.parseInt(fc_filter.getSelectedItem().toString().split(" ")[1].trim());
					if(modbusWp.getFunctionCode() == fc) {
						fcPass = true;
					}
				}else {
					// ALL
					fcPass = true;
				}
				
				if( !(dataType_filter.getSelectedItem().toString().equalsIgnoreCase("") || dataType_filter.getSelectedItem().toString().equalsIgnoreCase("ALL")) ) { 
					String dataType = dataType_filter.getSelectedItem().toString().toUpperCase().trim();
					if(modbusWp.getDataType().toUpperCase().trim().equalsIgnoreCase(dataType)) {
						dataTypePass = true;
					}
				}else {
					// ALL
					dataTypePass = true;
				}
				
				isContain = isContain && fcPass && dataTypePass;
			}
			
			if(isContain) {
				filterList.add(modbusWp);
			}
			
		}// for loop

		resetTable(point_table);
		addRecord(point_table, filterList);
	}
	
	public void addKeyAdapter() {
		if(saveAndCloseAdpter == null) {
			saveAndCloseAdpter = new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					try {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							dispose();					
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			};
		}
		
		search_TextField.addKeyListener(saveAndCloseAdpter);
		point_table.addKeyListener(saveAndCloseAdpter);
	}
}
