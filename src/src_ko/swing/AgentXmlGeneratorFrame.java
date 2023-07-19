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
import java.util.HashMap;
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

import common.util.Calculator;
import common.util.FontManager;
import src_ko.agent.Perf;
import src_ko.util.ExcelAdapter;
import src_ko.util.Inspecter;
import src_ko.util.Util;
import src_ko.util.XmlGenerator;

public class AgentXmlGeneratorFrame extends JFrame {
	
	private static final int ORDER = 0;
	private static final int PERF_NAME = 1;
	private static final int PERF_COUNTER = 2;
	private static final int SLOT = 3;
	private static final int INTERVAL = 4;
	private static final int MEASURE = 5;
	private static final int SCALE_FUNCTION = 6;	
	private static final int LABLE_0 = 7;
	private static final int LABLE_1 = 8;
	private static final int LABLE_STATUS = 9;

	// XML Convertiong 기능 세팅	
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// 스레드 수행 여부
	public static boolean isConverting = false;
	public static boolean isInspecting = false;
	
	private JPanel contentPane;
	private static JButton convertXML_Button;
	public static boolean isExist = false;
	private static List perfList;
	private static JTable table;	
	private static boolean perfCheckOk = true;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					AgentXmlGeneratorFrame frame = new AgentXmlGeneratorFrame();
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
	public AgentXmlGeneratorFrame() {
		AgentXmlGeneratorFrame.isExist = true;
		perfList = new ArrayList();		
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("ModbusAnalyzer : Agent XML Generator");
		
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
		
		JLabel agentXmlGenerator = new JLabel("Agent XML Generator");
		agentXmlGenerator.setForeground(Color.BLACK);
		agentXmlGenerator.setIcon(new Util().getSubLogoResource());
		agentXmlGenerator.setHorizontalAlignment(SwingConstants.LEFT);
		agentXmlGenerator.setFont(FontManager.getFont(Font.BOLD, 22));
		agentXmlGenerator.setBackground(Color.WHITE);
		agentXmlGenerator.setBounds(0, 0, 415, 55);
		actualPanel.add(agentXmlGenerator);			
		
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
				
				convertAgentXML();
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
		
		JLabel addressInfo = new JLabel("Tag \uAC04\uD3B8 \uC5C5\uB85C\uB4DC");
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
		EventInfo.setForeground(Color.BLACK);
		EventInfo.setFont(FontManager.getFont(Font.BOLD, 16));
		EventInfo.setBounds(178, 10, 68, 30);
		formPanel.add(EventInfo);
		
		useAutoEvent = new JCheckBox("\uC774\uBCA4\uD2B8 \uC790\uB3D9 \uB4F1\uB85D \uC0AC\uC6A9");
		useAutoEvent.setForeground(Color.BLACK);
		useAutoEvent.setBackground(Color.WHITE);
		useAutoEvent.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoEvent.setBounds(195, 78, 189, 23);
//		useAutoEvent.setSelected(Event.useAutoEvent);
		useAutoEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoEvent.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("성능 이벤트 자동 등록 사용"), Util.separator));
					sb.append("XML 변환시 자동 등록 이벤트 내용이 포함됩니다\n\n");
					sb.append("자동 등록 이벤트는 이벤트 이름을 제외한 모든 설정이 동일하게 적용되어 등록됩니다 " + Util.separator + "\n");
					sb.append("\n반드시 이벤트 설정 내용을 확인해주세요 !\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		formPanel.add(useAutoEvent);
		
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
		
		formPanel.add(eventInfoButton);
		
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
					
					Perf perf = new Perf();
					
					perf.setDisplayName("");						
					perf.setPerfCounter("");
					perf.setInterval("60"); // 수집주기 ( 기본 : 60초 )
					perf.setSlot(1);
					perf.setMeasure(""); // 단 위
					perf.setScaleFunction("x"); // 보정식 ( 기본 : x )
					
					addRecord(perf);
					
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
		useAutoMeasure.setForeground(Color.BLACK);
		useAutoMeasure.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoMeasure.setBackground(Color.WHITE);
		useAutoMeasure.setBounds(854, 15, 161, 23);
		useAutoMeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoMeasure.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("성능 단위 자동 생성 사용"), Util.separator));
					sb.append("[ 테이블 검증 ] 기능 사용시 " + Util.separator + "\n\n");
					sb.append("입력된 성능명 검사 후 해당 성능명에 적절한 단위가 자동으로 생성됩니다" + Util.separator + "\n\n");					
					sb.append("해당 기능은 단순 보조 기능이므로 반드시 자동 생성된 성능의 단위 내용을 확인해주세요  !"  + Util.separator +  "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		formPanel.add(useAutoMeasure);
		
		JButton btnNewButton = new JButton("Tag \uC785\uB825");
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(FontManager.getFont(Font.BOLD, 15));
		btnNewButton.setBounds(14, 60, 140, 36);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!ItemUploadFrame.isExist) {
					new ItemUploadFrame("Agent Tag Upload", "Agent Tag Upload", "agent");
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
				AgentXmlGeneratorFrame.perfList.clear();
			}
		});
		
		
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		AgentXmlGeneratorFrame.isExist = false;
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
				"\uC21C \uC11C", // 순서
				"\uC131\uB2A5\uBA85", // 성능명 
				"성능 카운터",  // 성능 카운터
				"슬 롯",  // 슬롯
				"\uC218\uC9D1\uC8FC\uAE30", // 수집주기 
				"\uB2E8 \uC704", // 단위
				"\uBCF4\uC815\uC2DD", // 보정식 
				"\uC774\uC9C4 \uC0C1\uD0DC : 0", // 이진 상태 : 0 
				"\uC774\uC9C4 \uC0C1\uD0DC : 1", // 이진 상태 : 1
				"\uB2E4\uC911 \uC0C1\uD0DC" // 다중 상태
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, // 순서
				true, // 성능명
				true, // 성능 카운터
				true, // 슬롯
				true, // 수집 주기
				true, // 단위
				true, // 보정식
				true, // 이진 상태 : 0
				true, // 이진 상태 : 1
				true // 다중 상태
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(ORDER).setPreferredWidth(70); // 순서
		table.getColumnModel().getColumn(PERF_NAME).setPreferredWidth(350); // 성능명
		table.getColumnModel().getColumn(PERF_COUNTER).setPreferredWidth(250); // 성능 카운터
		table.getColumnModel().getColumn(SLOT).setPreferredWidth(80); // 슬롯
		table.getColumnModel().getColumn(INTERVAL).setPreferredWidth(80); // 수집주기
		table.getColumnModel().getColumn(MEASURE).setPreferredWidth(80); // 단위
		table.getColumnModel().getColumn(SCALE_FUNCTION).setPreferredWidth(130); // 보정식 
		table.getColumnModel().getColumn(LABLE_0).setPreferredWidth(120); // 이진 상태 : 0
		table.getColumnModel().getColumn(LABLE_1).setPreferredWidth(120); // 이진 상태 : 1
		table.getColumnModel().getColumn(LABLE_STATUS).setPreferredWidth(320); // 다중 상태
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
//		table.getTableHeader().setResizingAllowed(false); // 컬럼 와이드 크기 임의 변경 불가
		
		setCellContentCenter(table);
	}
	
	/**
	 *  XML Convertiong
	 */
	public void convertAgentXML() {
		
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
								
					sb.append(String.format("Agent 성능 %s개 항목을 변환 하시겠습니까?%s%s\n\n",Util.colorBlue(String.valueOf(perfs.length)) ,Util.separator, Util.separator));
					
					if(useAutoEvent.isSelected()) {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("XML 내용에 자동 등록 이벤트 내용이 포함됩니다"), Util.separator ,Util.separator));
					}else {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("XML 내용에 자동 등록 이벤트 내용이 포함되지 않습니다"), Util.separator ,Util.separator));
					}
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// 성능 추가 요청 취소
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
						sb.append(String.format("XML 변환 작업을 취소하였습니다%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						AgentXmlGeneratorFrame.isConverting = false;
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
								AgentXmlGeneratorFrame.isConverting = false;
								return;
							case 0: // 첫 번째 버튼 : EUC-KR
								encoding = "euc-kr";
								break;
							case 1: // 두 번째 버튼
								encoding = "utf-8";
								break;
						}
					}
					
					// 성능을 추가하는 시점에 테이블의 데이터는 검증 된 데이터이다
					resetTable(table);		
//					Arrays.sort(perfs);		
					addRecord(perfs);
					
					// 자동 이벤트 등록 옵션
					if(useAutoEvent.isSelected()) Perf.initPerfEvent(perfs);
					
					// 성능 주소 표기 방식에 따른 strPerfCounter 초기화
					XmlGenerator.generateXML(perfs, useAutoEvent.isSelected(), encoding, "agent");
					
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
				/* column[2] */ record.add(perf[i].getPerfCounter());  // 성능 카운터
				/* column[3] */ record.add(perf[i].getSlot());  // 슬 롯
				/* column[4] */ record.add(perf[i].getInterval()); // 수집주기
				/* column[5] */ record.add(perf[i].getMeasure()); // 단위
				/* column[6] */ record.add(perf[i].getScaleFunction()); // 보정식
								
				switch(perf[i].getDataFormat()) {
				case "1" : 
					/* column[7] */ record.add(perf[i].getBinaryMap().get("0")); // 이진 상태 : 0
					/* column[8] */ record.add(perf[i].getBinaryMap().get("1")); // 이진 상태 : 1
					/* column[9] */ record.add(""); // 다중 상태
					break;
				case "2" :
					String multiStatus = Perf.parseMultiStatusSring(perf[i].getMultiStatusMap());
					/* column[7] */ record.add(""); // 이진 상태 : 0
					/* column[8] */ record.add(""); // 이진 상태 : 1
					/* column[9] */ record.add(multiStatus); // 다중 상태
					break;
				case "3" :
					/* column[7] */ record.add(""); // 이진 상태 : 0
					/* column[8] */ record.add(""); // 이진 상태 : 1
					/* column[9] */ record.add(""); // 다중 상태
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
			int num = Integer.parseInt(table.getValueAt(i, 0).toString().trim());
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
//					Arrays.sort(perfs);
					addRecord(perfs);
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
	
	
	public Perf[] getCollectionPerfList(String option) {
		perfCheckOk = true;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		
		if(model.getRowCount() <= 0) {
			perfCheckOk = false;
			return null;
		}
		
		
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
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>성능명</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\n성능명은 반드시 입력해야 하는 필드입니다%s\n", Util.separator));				
					sb.append(String.format("\n테이블 <font color='blue'>%s</font> 행의 성능명 내용을 입력해주세요%s\n",String.valueOf(data.get(ORDER)), Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, PERF_NAME);
					perfCheckOk = false;
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
					perfCheckOk = false;
					return null;			
				}
			
			}// end option check
			
			perfs[i].setDisplayName(perfName); 
			
			// 성능 카운터 ---------------------------------------------------------------------------------
			String perfCounter = String.valueOf(data.get(PERF_COUNTER)).trim();						
			perfs[i].setPerfCounter(perfCounter);
			
			// 슬 롯 -------------------------------------------------------------------------------------
			int slot = 0;
			
			try {
				slot = Integer.parseInt(String.valueOf(data.get(SLOT)).trim());
				
				if(slot < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>슬롯</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n슬롯은 1 이상의 양의 정수 값만 입력 할 수 있습니다%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 슬롯 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(SLOT)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, SLOT);
					perfCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>슬롯</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("입력된 슬롯 내용을 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 슬롯 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(SLOT)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, SLOT);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setSlot(slot);
			
			// 수집 주기  ----------------------------------------------------------------------------------
			String interval = String.valueOf(data.get(INTERVAL)).trim();
			
			try{
				int actualInterval = Integer.parseInt(interval);
				
				if(actualInterval < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>수집 주기</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n수집 주기는 1 이상의 양의 정수 값만 입력 할 수 있습니다%s\n\n", Util.separator));
					sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 수집주기 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, INTERVAL);
					perfCheckOk = false;
					return null;
				}
				
			}catch (Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>수집 주기</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
				sb.append(String.format("\n입력된 수집주기 내용을 <font color='blue'>정수 값</font>으로 변환 할 수 없습니다%s\n\n", Util.separator));
				sb.append(String.format("현재 테이블 <font color='blue'>%s</font> 행의 수집주기 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
				Calculator.checkFormula(scaleFunction, 1);
				if (scaleFunction.equalsIgnoreCase("") || scaleFunction.length() < 1 || !scaleFunction.contains("x"))
					throw new Exception("보정식 내용 오류");				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>보정식</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 보정식 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase() ,Util.separator));				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, SCALE_FUNCTION);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setScaleFunction(scaleFunction);		
			
			// 이진 상태 : 0, 1 ---------------------------------------------------------------------------
			HashMap binaryMap = perfs[i].getBinaryMap();
			String label0 = String.valueOf(data.get(LABLE_0)).trim();
			String label1 = String.valueOf(data.get(LABLE_1)).trim();
			
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				// 이진 상태 데이터 형식에서 레이블이 하나만 존재 할 수는 없다 
				if((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1))) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 1</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));																
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);					
					setFocusCell(table, i, LABLE_1);
					perfCheckOk = false;
					return null;
				}
			}else if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
				if((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1))) {
					// 이진 상태 데이터 형식에서 레이블이 하나만 존재 할 수는 없다
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태 : 0</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, LABLE_0);
					perfCheckOk = false;
					return null;
				}
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
						StringBuilder sb = new StringBuilder();
						sb.append("<font color='red'>Table Validation Exception</font>\n");
						sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태</font>와 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
						sb.append(String.format("데이터 형식이 <font color='blue'>이진 상태</font> 이면서 동시에 <font color='blue'>다중 상태</font> 일 수는 없습니다%s\n", Util.separator));
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n현재 테이블 <font color='blue'>%s</font> 행의 다중 상태 내용 : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(LABLE_STATUS)).trim() ,Util.separator));
				if(e instanceof NumberFormatException) {
					sb.append(String.format("\n다중 상태 입력 양식 : <font color='blue'>숫자1; 문자1; 숫자2; 문자2; 숫자3; 문자3; ...</font>%s%s\n", Util.separator, Util.separator));
				}
				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
						StringBuilder sb = new StringBuilder();
						sb.append("<font color='red'>Table Validation Exception</font>\n");
						sb.append(String.format("테이블 <font color='blue'>%s</font> 행의 <font color='blue'>이진 상태</font>와 <font color='blue'>다중 상태</font> 내용에 문제가 있습니다%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
						sb.append(String.format("데이터 형식이 <font color='blue'>이진 상태</font> 이면서 동시에 <font color='blue'>다중 상태</font> 일 수는 없습니다%s\n", Util.separator));
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
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
			throw new RuntimeException("다중 상태 내용 오류");
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
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 성능 카운터
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // 슬롯
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // 수집 주기
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // 단위
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // 보정식
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 0
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // 이진 상태 : 1
//		tcmSchedule.getColumn(9).setCellRenderer(tScheduleCellRenderer); // 다중 상태	
		
	}

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		AgentXmlGeneratorFrame.table = table;
	}
}
