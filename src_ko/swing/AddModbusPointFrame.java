package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

import common.modbus.ModbusWatchPoint;
import common.modbus.ModbusWatchPointInitException;
import common.modbus.ModbusWatchPointLoader;
import moon.Moon;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class AddModbusPointFrame extends JFrame {

	private ArrayList<ModbusWatchPoint> pointList = new ArrayList<ModbusWatchPoint>();
	
	private Color mkColor = new Color(237, 76, 55);
	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable table;

	private JRadioButton mk_V4_RaidoButton;
	private JRadioButton mk_V10_RaidoButton;
	
	private JButton upload_protocol;
	private JButton upload_xml;
	private JButton upload_excel;
	private JButton download_template;
	
	private JTextField search_textField;
	private JTextField dragAndDropField;
	private JTable point_table;
	
	private ArrayList<ModbusWatchPoint> left_point = new ArrayList<ModbusWatchPoint>();
	private ArrayList<ModbusWatchPoint> right_point = new ArrayList<ModbusWatchPoint>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddModbusPointFrame frame = new AddModbusPointFrame();
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
	public AddModbusPointFrame() {
		isExist = true;
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 1140, 733);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 8));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Modbus Watch Point Upload");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 380, 55);
		actualPanel.add(currentFunction);
		
				
		mk119Button = new JButton(new Util().getMK2Resource());
		mk119Button.setForeground(Color.BLACK);
		mk119Button.setText(" 템플릿 다운로드");
		mk119Button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		mk119Button.setFocusPainted(false);
		mk119Button.setContentAreaFilled(false);
		mk119Button.setBorder(UIManager.getBorder("Button.border"));
		mk119Button.setBackground(Color.WHITE);
		mk119Button.setBounds(804, 11, 302, 36);		
		mk119Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File("경로\\템플릿 파일.xlsx");
				String path = Util.getFilePath();
				
				if(path != null) {
					path += ".xlsx";
					FileUtil.copyFile(file, new File(path));
					System.out.println(path);
				}else {
					return;
				}
			}
		});
		actualPanel.add(mk119Button);
		
		JPanel backGround_Panel = new JPanel();
		backGround_Panel.setBackground(Color.LIGHT_GRAY);
		backGround_Panel.setBounds(10, 57, 1096, 621);
		actualPanel.add(backGround_Panel);
		backGround_Panel.setLayout(null);
		
		JPanel mk119Version_Panel = new JPanel();
		mk119Version_Panel.setBorder(new LineBorder(Color.BLACK, 2));
		mk119Version_Panel.setBackground(Color.WHITE);
		mk119Version_Panel.setBounds(12, 10, 200, 84);
		backGround_Panel.add(mk119Version_Panel);
		mk119Version_Panel.setLayout(null);
		
		mk_V4_RaidoButton = new JRadioButton("MK119  V4");
		mk_V4_RaidoButton.setSelected(true);
		mk_V4_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V4_RaidoButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		mk_V4_RaidoButton.setForeground(mkColor);
		mk_V4_RaidoButton.setBackground(Color.WHITE);
		mk_V4_RaidoButton.setBounds(12, 13, 170, 23);
		mk_V4_RaidoButton.setFocusPainted(false);
		mk_V4_RaidoButton.addActionListener(mkVerionListener);
		mk119Version_Panel.add(mk_V4_RaidoButton);
		
		mk_V10_RaidoButton = new JRadioButton("MK119  V10");
		mk_V10_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V10_RaidoButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
		mk_V10_RaidoButton.setBackground(Color.WHITE);
		mk_V10_RaidoButton.setBounds(12, 47, 170, 23);
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
		upload_protocol.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		upload_protocol.setFocusPainted(false);
		upload_protocol.setIcon(new Util().getFolder2Image());
		upload_protocol.setBackground(Color.WHITE);
		upload_protocol.setBounds(12, 10, 222, 66);
		uploadMethod_Panel.add(upload_protocol);
		
		upload_xml = new JButton( "  XML");
		upload_xml.setForeground(Color.BLACK);
		upload_xml.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		upload_xml.setFocusPainted(false);
		upload_xml.setIcon(new Util().getXMLImage());
		upload_xml.setBackground(Color.WHITE);
		upload_xml.setBounds(246, 10, 193, 66);
		upload_xml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pointUploadXml();
			}
		});
		uploadMethod_Panel.add(upload_xml);
		
		upload_excel = new JButton("  Excel");
		upload_excel.setForeground(Color.BLACK);
		upload_excel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		upload_excel.setFocusPainted(false);
		upload_excel.setIcon(new Util().getExcelImage());
		upload_excel.setBackground(Color.WHITE);
		upload_excel.setBounds(451, 10, 196, 66);
		uploadMethod_Panel.add(upload_excel);
		
		dragAndDropField = new JTextField("File Drag & Drop");
		dragAndDropField.setBackground(Color.WHITE);
		dragAndDropField.setForeground(new Color(0, 128, 0));
		dragAndDropField.setHorizontalAlignment(SwingConstants.CENTER);
		dragAndDropField.setEditable(false);
		dragAndDropField.setFont(new Font("맑은 고딕", Font.BOLD, 18));
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
							
							ModbusWatchPoint[] modbusWps  = null;
							
							// 인코딩 선택 해야함
							try {								
								String encoding = "euc-kr";
									
								if(file == null || !file.exists()) {
									return;
								}
								
								if(file.getAbsolutePath().toLowerCase().endsWith(".xml")) {
									StringBuilder msg = new StringBuilder();
									msg.append("<font color='Green'>XML File Encoding</font>\n");
									msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n");

									int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

									switch (menu) {
										case 0: // 첫 번째 버튼 : EUC-KR
											encoding = "euc-kr";
											break;
											
										case 1: // 두 번째 버튼
											encoding = "utf-8";
											break;
											
										default :
											return;
									}								

									modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXML(file, encoding);
								}else {
									modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXlsx(file);
								}
								
								if(modbusWps != null) {
									resetTable(point_table);
									addRecord(point_table, modbusWps);
								}
								
								setTableStyle(point_table);
								
								if(modbusWps != null && modbusWps.length > 0) {
									// 정상적으로 하나 이상의 모드버스 포인트를 읽었을 경우 메소드를 종료한다
									setTitle("ModbusAnalyzer : " + file.getName());
									return;
								}
								
							}catch(ModbusWatchPointInitException e) {
								System.out.println(e.getMessage());
								
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
								sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("모드버스 포인트"), e.getMessage(), Util.separator, Util.separator));
								sb.append(String.format("위의 모드버스 포인트 정보를 초기화 하는중 오류가 발생하였습니다%s%s\n", Util.separator, Util.separator));

								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								
							}catch(IOException e) {								
								if( !(e.getMessage().startsWith("point") || e.getMessage().startsWith("event"))) {
									e.printStackTrace();
									return;
								}
								
								boolean isPoint = e.getMessage().startsWith("point");
								
								String[] info = e.getMessage().split(",");
								boolean hasPointName = !info[3].equalsIgnoreCase("null");
								
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
								sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("행 번호"), info[1], Util.separator, Util.separator));
								sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("에러 필드"), info[2], Util.separator, Util.separator));
								
								if(hasPointName) {
									sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("모드버스 포인트"), info[3], Util.separator, Util.separator));
								}
								
								sb.append(String.format("%s번 행의 %s %s 필드 파싱 과정에서 에러가 발생하였습니다%s%s\n", 
												Util.colorRed(info[1]),
												isPoint ? "모드버스" : "이벤트",
												Util.colorRed(info[2]),
												Util.separator,
												Util.separator));
								
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(25, 114, 76, 28);
		backGround_Panel.add(lblNewLabel);
		
		search_textField = new JTextField();
		search_textField.setBounds(84, 115, 294, 28);
		search_textField.setHorizontalAlignment(SwingConstants.LEFT);
		search_textField.setForeground(Color.BLACK);
		search_textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		search_textField.setColumns(10);
		search_textField.setBorder(new LineBorder(Color.BLACK, 2));
		backGround_Panel.add(search_textField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(12, 147, 1072, 464);
		backGround_Panel.add(scrollPane);
		
		point_table = new JTable();
		scrollPane.setViewportView(point_table);
		resetTable(point_table);
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		isExist = false;
		super.dispose();
	}
	
	public void resetTable(JTable table) {
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
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		// 셀 이동 불가
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
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
	
	// 라디오 버튼 액션 이벤트 리스너
	ActionListener mkVerionListener = new ActionListener() {
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
	
	
	
	public void pointUploadXml() {
		try {
			String path = Util.getFilePath();
			String encoding = "euc-kr";
			File xmlFile = null;
			
			if (path == null || path.length() < 1) {
				return;
			}else {
				xmlFile = new File(path);
				
				if(xmlFile == null || !xmlFile.exists()) {
					return;
				}
				
				StringBuilder msg = new StringBuilder();
				msg.append("<font color='Green'>XML File Encoding</font>\n");
				msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n");

				int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

				switch (menu) {
					case 0: // 첫 번째 버튼 : EUC-KR
						encoding = "euc-kr";
						break;
						
					case 1: // 두 번째 버튼
						encoding = "utf-8";
						break;
						
					default :
						return;
				}
			}

			ModbusWatchPoint[] modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXML(xmlFile, encoding);
			
			if(modbusWps != null) {
				resetTable(point_table);
				addRecord(point_table, modbusWps);
			}
			
			setTableStyle(point_table);
			
		}catch(ModbusWatchPointInitException e) {
			System.out.println(e.getMessage());
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));				
			sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("모드버스 포인트"), e.getMessage(), Util.separator, Util.separator));			
			sb.append(String.format("위의 모드버스 포인트 정보를 초기화 하는중 오류가 발생하였습니다%s%s\n", Util.separator, Util.separator));

			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	/**
	 * 	레코드 추가
	 */
	public void addRecord(JTable table, ModbusWatchPoint... modbusWps) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < modbusWps.length; i++) {
				
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
				/* column[1] */ record.add(modbusWps[i]); // 성능명
				/* column[2] */ record.add(modbusWps[i].getFunctionCode());  // 기능코드
				/* column[3] */ record.add(modbusWps[i].getRegisterAddrHexString());  // 레지스터 주소
				/* column[4] */ record.add(modbusWps[i].getModbusAddr()); // 모드버스 주소
				/* column[5] */ record.add(modbusWps[i].getDataType()); // 데이터 타입
				
				model.addRow(record);
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	/**
	 * 레코드 삭제 
	 * 삭제시 for문으로  삭제 할 것이 아니라 선택된 포인트의 인덱스를 검사하여 삭제하도록 구현하자
	 */
	public void removeRecord(JTable table, int... index) {
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
}
