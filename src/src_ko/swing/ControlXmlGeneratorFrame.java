package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

import common.util.FontManager;
import src_ko.agent.ControlAction;
import src_ko.util.ExcelAdapter;
import src_ko.util.Inspecter;
import src_ko.util.Util;
import src_ko.util.XmlGenerator;

public class ControlXmlGeneratorFrame extends JFrame {
	
	private static final int ORDER = 0; // 순 서
	private static final int CONTROL_NAME = 1; // 제어 이름
//	private static final int CONTROL_COUNTER = 2; // 제어 카운터 (사용하지 않는 필드)
	private static final int CONTROL_COMMAND = 2; // 제어 명령어
	private static final int CONTROL_DESC = 3; // 제어 설명
	private static final int CONTROL_USE_PARAM = 4; // 파라미터 사용여부
	private static final int CONTROL_WAIT_TIME = 5; // 제어 타임아웃

	// XML Convertiong 기능 세팅	
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// 스레드 수행 여부
	public static boolean isConverting = false;
	public static boolean isInspecting = false;
	
	private JPanel contentPane;
	private static JButton convertXML_Button;
	public static boolean isExist = false;
	private static List controlActionfList;
	private static JTable table;	
	private static boolean ControlCheckOk = true;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ControlXmlGeneratorFrame frame = new ControlXmlGeneratorFrame();
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
	public ControlXmlGeneratorFrame() {
		ControlXmlGeneratorFrame.isExist = true;
		controlActionfList = new ArrayList();		
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("ModbusAnalyzer : Control XML Generator");
		
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
		
		JLabel generalXmlGenerator = new JLabel("Control XML Generator");
		generalXmlGenerator.setForeground(Color.BLACK);
		generalXmlGenerator.setIcon(new Util().getSubLogoResource());
		generalXmlGenerator.setHorizontalAlignment(SwingConstants.LEFT);
		generalXmlGenerator.setFont(FontManager.getFont(Font.BOLD, 22));
		generalXmlGenerator.setBackground(Color.WHITE);
		generalXmlGenerator.setBounds(0, 0, 415, 55);
		actualPanel.add(generalXmlGenerator);			
		
		convertXML_Button = new JButton(new Util().getMK2Resource());
		convertXML_Button.setForeground(new Color(0, 128, 0));
		convertXML_Button.setText("  XML \uC0DD\uC131");
		convertXML_Button.setFont(FontManager.getFont(Font.BOLD, 18));
		convertXML_Button.setFocusPainted(false);
		convertXML_Button.setContentAreaFilled(false);
		convertXML_Button.setBorder(UIManager.getBorder("Button.border"));
		convertXML_Button.setBackground(Color.WHITE);
		convertXML_Button.setBounds(837, 11, 202, 36);
		convertXML_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n검증 스레드 수행중에는 XML Convertiong 기능을 사용 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("이미 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong 스레드는 중복으로 실행 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				convertGeneralXML();
			}
		});
		actualPanel.add(convertXML_Button);
		
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
		
		JLabel addressInfo = new JLabel("\uC81C\uC5B4 \uAC04\uD3B8 \uC5C5\uB85C\uB4DC");
		addressInfo.setForeground(Color.BLACK);
		addressInfo.setFont(FontManager.getFont(Font.BOLD, 16));
		addressInfo.setBounds(14, 10, 140, 30);
		formPanel.add(addressInfo);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBounds(166, 2, 2, 105);
		formPanel.add(separator);
		
		JLabel EventInfo = new JLabel("\uC774\uBCA4\uD2B8");
		EventInfo.setFont(FontManager.getFont(Font.BOLD, 16));
		EventInfo.setBounds(178, 10, 68, 30);
//		formPanel.add(EventInfo);
		
		useAutoEvent = new JCheckBox("\uC774\uBCA4\uD2B8 \uC790\uB3D9 \uB4F1\uB85D \uC0AC\uC6A9");
		useAutoEvent.setBackground(Color.WHITE);
		useAutoEvent.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoEvent.setBounds(195, 78, 189, 23);
//		useAutoEvent.setSelected(Event.useAutoEvent);
		useAutoEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoEvent.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("제어 이벤트 자동 등록 사용"), Util.separator));
					sb.append("XML 변환시 자동 등록 이벤트 내용이 포함됩니다\n\n");
					sb.append("자동 등록 이벤트는 이벤트 이름을 제외한 모든 설정이 동일하게 적용되어 등록됩니다 " + Util.separator + "\n");
					sb.append("\n반드시 이벤트 설정 내용을 확인해주세요 !\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
//		formPanel.add(useAutoEvent);
		
		JButton eventInfoButton = new JButton("\uC774\uBCA4\uD2B8 \uC124\uC815");
		eventInfoButton.setBounds(195, 44, 174, 29);		
		eventInfoButton.setBackground(Color.WHITE);
		eventInfoButton.setForeground(Color.BLACK);		
		eventInfoButton.setFont(FontManager.getFont(Font.BOLD, 15));
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
					 sb.append("이벤트 설정 프레임이 이미 열려있습니다" + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});		
//		formPanel.add(eventInfoButton);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(401, 2, 2, 105);
		formPanel.add(separator_1);
		
		JLabel tableSet = new JLabel("\uD14C\uC774\uBE14 \uC870\uC791");
		tableSet.setForeground(Color.BLACK);
		tableSet.setFont(FontManager.getFont(Font.BOLD, 16));
		tableSet.setBounds(415, 10, 111, 30);
		formPanel.add(tableSet);
		
		
		JButton addButton = new JButton("\uB808\uCF54\uB4DC \uCD94\uAC00");
		addButton.setBounds(425, 52, 120, 44);
		addButton.setBackground(Color.WHITE);
		addButton.setFont(FontManager.getFont(Font.BOLD, 15));
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
					sb.append(String.format("현재 테이블 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n검증 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 레코드를 추가 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					
					ControlAction control = new ControlAction();
					control.setControlName("");
					control.setControlCounter("CONTROL");
					control.setCommand("");
					control.setDesc("");
					control.setUseParam(1);
					control.setWaitTime(1);
					
					addRecord(control);
					
				}catch(Exception exception) {
					// 테이블 내용 추가 수행 중 예외가 발생하면 아무것도 수행하지 않음
					exception.printStackTrace();
				}
				
			}
		});
		formPanel.add(addButton);
		
		
		JButton deleteButton = new JButton("\uB808\uCF54\uB4DC \uC0AD\uC81C");
		deleteButton.setBounds(554, 52, 120, 44);
		deleteButton.setFont(FontManager.getFont(Font.BOLD, 15));
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
					sb.append(String.format("현재 테이블 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n검증 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 레코드를 삭제 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int[] selected = table.getSelectedRows();					
				removeRecord(selected);
			}
		});
		formPanel.add(deleteButton);
		
		
		JButton inspectButton = new JButton("\uD14C\uC774\uBE14 \uAC80\uC99D");
		inspectButton.setForeground(Color.BLACK);
		inspectButton.setFont(FontManager.getFont(Font.BOLD, 15));
		inspectButton.setFocusPainted(false);
		inspectButton.setContentAreaFilled(false);
		inspectButton.setBorder(UIManager.getBorder("Button.border"));
		inspectButton.setBackground(Color.WHITE);
		inspectButton.setBounds(682, 52, 175, 44);
		inspectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspect Thread is Already Working</font>\n");
					sb.append(String.format("이미 테이블 검증 작업 스레드가 수행중입니다%s\n", Util.separator));					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 테이블 검증 작업을 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				inspectRecord();
			}
		});
		formPanel.add(inspectButton);
		
		
		JButton resetButton = new JButton("\uD14C\uC774\uBE14 \uCD08\uAE30\uD654");
		resetButton.setBounds(865, 52, 150, 44);
		resetButton.setBackground(Color.WHITE);
		resetButton.setForeground(Color.BLACK);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 15));
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		formPanel.add(resetButton);
		
		useAutoMeasure = new JCheckBox("\uC131\uB2A5 \uB2E8\uC704 \uC790\uB3D9 \uC0DD\uC131");
		useAutoMeasure.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoMeasure.setBackground(Color.WHITE);
		useAutoMeasure.setBounds(854, 15, 161, 23);
		useAutoMeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoMeasure.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("제어 단위 자동 생성 사용"), Util.separator));
					sb.append("[ 테이블 검증 ] 기능 사용시 " + Util.separator + "\n\n");
					sb.append("입력된 제어명 검사 후 해당 제어명에 적절한 단위가 자동으로 생성됩니다" + Util.separator + "\n\n");					
					sb.append("해당 기능은 단순 보조 기능이므로 반드시 자동 생성된 제어의 단위 내용을 확인해주세요  !"  + Util.separator +  "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
//		formPanel.add(useAutoMeasure);
		
		JButton btnNewButton = new JButton("\uC81C\uC5B4 \uC815\uBCF4 \uC785\uB825");
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(FontManager.getFont(Font.BOLD, 15));
		btnNewButton.setBounds(14, 60, 140, 36);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ItemUploadFrame.isExist) {
					new ItemUploadFrame("Control Action Upload", "Control Action Upload", "control");
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Item Upload Frame Already Exists") + Util.separator + "\n");
					sb.append("Item Upload 프레임이 이미 열려있습니다" + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;	
				}
				
			}
		});
		formPanel.add(btnNewButton);
		
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspection Thread is Working</font>\n");
					sb.append(String.format("현재 테이블 검증 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\n검증 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("현재 XML Convertiong 작업 스레드가 수행중입니다%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong 스레드 수행중에는 테이블을 초기화 할 수 없습니다%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				resetTable(table);				
				ControlXmlGeneratorFrame.controlActionfList.clear();
			}
		});
		
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ControlXmlGeneratorFrame.isExist = false;
		super.dispose();
	}
	
	public static void resetTable(JTable table){
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				
			},
			new String[] {
				"순 서", // 순서
				"제어 이름", // 제어 이름 
//				"제어 카운터",  // 제어 카운터
				"제어 명령어",  // 제어 명령어
				"제어 내용", // 제어 내용(설명) 
				"파라미터 사용여부", // 파라미터 사용여부
				"제어 타임아웃" // 제어 타임아웃
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, // 순서
				true, // 제어 이름
//				true, // 제어 카운터
				true, // 제어 명령어
				true, // 제어 설명
				true, // 파라미터 사용여부
				true, // 제어 타임아웃
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table.getColumnModel().getColumn(ORDER).setPreferredWidth(70); // 순서
		table.getColumnModel().getColumn(CONTROL_NAME).setPreferredWidth(260); // 제어 이름
//		table.getColumnModel().getColumn(CONTROL_COUNTER).setPreferredWidth(100); // 제어 카운터
		table.getColumnModel().getColumn(CONTROL_COMMAND).setPreferredWidth(280); // 제어 명령어
		table.getColumnModel().getColumn(CONTROL_DESC).setPreferredWidth(350); // 제어 설명
		table.getColumnModel().getColumn(CONTROL_USE_PARAM).setPreferredWidth(150); // 제어 파라미터 사용여부
		table.getColumnModel().getColumn(CONTROL_WAIT_TIME).setPreferredWidth(120); // 제어 타임아웃
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
//		table.getTableHeader().setResizingAllowed(false); // 컬럼 와이드 크기 임의 변경 불가
		
		setCellContentCenter(table);
	}
	
	/**
	 *  XML Convertiong
	 */
	public void convertGeneralXML() {
		
		try {
		
			Thread thread = new Thread(new Runnable() {
				String encoding;
				
				public void run() {
					
					isConverting = true;
					
					ControlAction[] controls = getControlActionList();		
					
					if(!ControlCheckOk) {
						isConverting = false;
						return;
					}
					if(controls == null) {
						isConverting = false;
						return;
					}
					
					StringBuilder sb = new StringBuilder();				
					sb.append("<font color='green'>Convert to XML File?</font>\n");
								
					sb.append(String.format("제어 %s개 항목을 변환 하시겠습니까?%s%s\n",Util.colorBlue(String.valueOf(controls.length)) ,Util.separator, Util.separator));
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// 제어 추가 요청 취소
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
						sb.append(String.format("XML 변환 작업을 취소하였습니다%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						ControlXmlGeneratorFrame.isConverting = false;
						return;
					}else {
						StringBuilder msg = new StringBuilder();
						msg.append("<font color='Green'>XML File Encoding</font>\n");
						msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n\n");
						msg.append(String.format("MK119 4.2 Version 이하 : %s%s%s\n", Util.colorBlue("EUC-KR"), Util.separator, Util.separator));
						msg.append(String.format("MK119 4.5 Version 이상 : %s%s%s\n", Util.colorBlue("UTF-8"), Util.separator, Util.separator));

						int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

						switch (menu) {
							case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때
								sb = new StringBuilder();
								sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
								sb.append(String.format("XML 변환 작업을 취소하였습니다%s\n", Util.separator));											
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								ControlXmlGeneratorFrame.isConverting = false;
								return;
							case 0: // 첫 번째 버튼 : EUC-KR
								encoding = "euc-kr";
								break;
							case 1: // 두 번째 버튼
								encoding = "utf-8";
								break;
						}
					}
					
					resetTable(table);
					addRecord(controls);
					
					XmlGenerator.generateControlXML(controls, encoding, "control");
					
					isConverting = false;
				}
			});
		
			thread.start();
			
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to XML Converting</font>\n");
			sb.append(String.format("XML 파일 변환 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
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
	public static void addRecord(ControlAction... control) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < control.length; i++) {
				
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
				/* column[1] */ record.add(control[i].getControlName()); // 제어 이름
//				/* column[2] */ record.add(control[i].getControlCounter());  // 제어 카운터
				/* column[3] */ record.add(control[i].getCommand());  // 제어 명령어
				/* column[4] */ record.add(control[i].getDesc()); // 제어 설명
				/* column[5] */ record.add(control[i].getUseParam()); // 제어 파라미터 사용여부
				/* column[6] */ record.add(control[i].getWaitTime()); // 제어 타임아웃												
				
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
	 * 테이블 검증
	 */
	public void inspectRecord() {
		try {
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					isInspecting = true;
					
					ControlAction[] controls = getControlActionList();
					
					if(!ControlCheckOk) {
						isInspecting = false;
						return;
					}
					if(controls == null) {
						isInspecting = false;
						return;
					}
					
					resetTable(table);
					addRecord(controls);
					
					Util.showMessage(String.format("%s%s%s\n테이블 검증 완료%s\n", Util.colorBlue("Inspection Successful"), Util.separator, Util.separator, Util.separator), JOptionPane.INFORMATION_MESSAGE);
					
					isInspecting = false;
				}
			});
			
			thread.start();
			
		}catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Table Validation Exception</font>\n");
			sb.append(String.format("테이블 검증 작업중 예외가 발생하였습니다%s\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}finally {
			isInspecting = false;
		}
	}
	
	
	public ControlAction[] getControlActionList() {
		ControlCheckOk = true;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();		
		
		if(model.getRowCount() <= 0) {
			ControlCheckOk = false;
			return null;
		}
		
		ControlAction[] controls = new ControlAction[model.getRowCount()];
		
		Vector rowVector = model.getDataVector();
		Vector data;
		
		for(int i = 0; i < model.getRowCount(); i++) {
			data = (Vector)rowVector.get(i);
			
			controls[i] = new ControlAction();
			controls[i].setControlCounter("CONTROL");
			
			
			// 제어 이름 -----------------------------------------------------------------------------------
			String controlName = String.valueOf(data.get(CONTROL_NAME)).trim();
			
		
			// 제어명 필드가 공백 일 경우
			if((controlName == null) || (controlName.equals("") || controlName.length() < 1)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 이름</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n제어 이름은 반드시 입력해야 하는 필드입니다%s\n", Util.separator));				
				sb.append(String.format("\n테이블 <font color='blue'>%s</font> 행의 제어 이름 내용을 입력해주세요%s\n",String.valueOf(data.get(ORDER)), Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_NAME);
				ControlCheckOk = false;
				return null;
			}
			
			// 제어명 특수문자 검사
			if(!Inspecter.isVaildName(controlName)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 이름</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n제어 이름에는 아래의 특수 문자를 제외한 특수 문자는 포함 할 수 없습니다%s\n", Util.separator));
				sb.append(String.format("\n제어 이름 포함 허용 특수 문자 : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 제어 이름 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), controlName ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_NAME);
				ControlCheckOk = false;
				return null;
			}
			
			controls[i].setControlName(controlName);
			
			// 제어 카운터 ---------------------------------------------------------------------------------
//			String controlCounter = String.valueOf(data.get(CONTROL_COUNTER)).trim();
//			controls[i].setControlCounter(controlCounter);
			
			// 제어 명령어 -------------------------------------------------------------------------------------
			String controlCommand = String.valueOf(data.get(CONTROL_COMMAND)).trim();
			controls[i].setCommand(controlCommand);
			
			// 제어 설명  ----------------------------------------------------------------------------------
			String controlDesc = String.valueOf(data.get(CONTROL_DESC)).trim();
			controls[i].setDesc(controlDesc);
			
			// 파라미터 사용여부  ------------------------------------------------------------------------------------
			int useParam;
						
			try {
				useParam = Integer.parseInt(String.valueOf(data.get(CONTROL_USE_PARAM)).trim());
				
				if(!(useParam <= 1 && useParam >= 0)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 파라미터 사용여부</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n제어의 파라미터 사용 여부는 0(미사용), 1(사용) 두가지 정수 값만 입력 할 수 있습니다%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 제어 파라미터 사용여부 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_USE_PARAM)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, CONTROL_USE_PARAM);
					ControlCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 파라미터 사용여부</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("입력된 제어의  파라미터 사용여부 내용을 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 제어 파라미터 사용여부 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_USE_PARAM)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_USE_PARAM);
				ControlCheckOk = false;
				return null;
			}
			
			controls[i].setUseParam(useParam);
			
			// 제어 타임아웃 ------------------------------------------------------------------------------------
			int waitTime;
			
			try {
				waitTime = Integer.parseInt(String.valueOf(data.get(CONTROL_WAIT_TIME)).trim());
				
				if(waitTime < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 타임아웃</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n제어 타임아웃 값은 1 이상의 양의 정수 값만 입력 할 수 있습니다%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 제어 타임아웃 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_WAIT_TIME)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, CONTROL_WAIT_TIME);
					ControlCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>제어 타임아웃</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("입력된 제어 타임아웃 내용을 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 제어 타임아웃 값 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_WAIT_TIME)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_WAIT_TIME);
				ControlCheckOk = false;
				return null;
			}
			
			controls[i].setWaitTime(waitTime);					
		}
		
		return controls;
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
		
		tcmSchedule.getColumn(ORDER).setCellRenderer(tScheduleCellRenderer); // 순서
//		tcmSchedule.getColumn(CONTROL_NAME).setCellRenderer(tScheduleCellRenderer); // 제어 이름
//		tcmSchedule.getColumn(CONTROL_COUNTER).setCellRenderer(tScheduleCellRenderer); // 제어 카운터
		tcmSchedule.getColumn(CONTROL_COMMAND).setCellRenderer(tScheduleCellRenderer); // 제어 명령어
//		tcmSchedule.getColumn(CONTROL_DESC).setCellRenderer(tScheduleCellRenderer); // 제어 설명
		tcmSchedule.getColumn(CONTROL_USE_PARAM).setCellRenderer(tScheduleCellRenderer); // 제어 파라미터 사용여부
		tcmSchedule.getColumn(CONTROL_WAIT_TIME).setCellRenderer(tScheduleCellRenderer); // 제어 타임아웃
		
	}

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		ControlXmlGeneratorFrame.table = table;
	}
}
