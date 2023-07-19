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

	// XML Convertiong ��� ����	
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// ������ ���� ����
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
		
		// Ŭ���� �δ��� �̿��� �̹��� �ε�
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// ������Ʈ Build Path�� �̹��� ���ҽ� ���丮�� ���Խ��Ѿ� �Ѵ�.		
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
					sb.append(String.format("���� ���̺� ���� �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\n���� ������ �����߿��� XML Convertiong ����� ��� �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("�̹� XML Convertiong �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong ������� �ߺ����� ���� �� �� �����ϴ�%s\n", Util.separator));
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // �� ���� ����� ã�� ���
		table.setBackground(Color.WHITE);		
		table.addKeyListener(new KeyAdapter() {				
			// �� ����  ����
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
					int[] selectedRows = table.getSelectedRows();
					int[] selectedColumns = table.getSelectedColumns();
					
					for(int row = 0; row < selectedRows.length; row++) {
						for(int column = 0; column < selectedColumns.length; column++) {			
					
							// ����ڰ� ���� �� �� �ִ� �� ���븸 ����
							if(table.isCellEditable(selectedRows[row], selectedColumns[column])) {
								table.setValueAt("", selectedRows[row], selectedColumns[column]);	
							}
						}
					}					
				}							
			}
		});
		ExcelAdapter ex = new ExcelAdapter(table); // ���� �� ���� �ٿ��ֱ� ���� 
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
					sb.append(String.format("%s%s\n", Util.colorBlue("���� �̺�Ʈ �ڵ� ��� ���"), Util.separator));
					sb.append("XML ��ȯ�� �ڵ� ��� �̺�Ʈ ������ ���Ե˴ϴ�\n\n");
					sb.append("�ڵ� ��� �̺�Ʈ�� �̺�Ʈ �̸��� ������ ��� ������ �����ϰ� ����Ǿ� ��ϵ˴ϴ� " + Util.separator + "\n");
					sb.append("\n�ݵ�� �̺�Ʈ ���� ������ Ȯ�����ּ��� !\n");
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
					 sb.append("�̺�Ʈ ���� �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
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
					sb.append(String.format("���� ���̺� ���� �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\n���� ������ �����߿��� ���ڵ带 �߰� �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("���� XML Convertiong �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong ������ �����߿��� ���ڵ带 �߰� �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					
					Perf perf = new Perf();
					
					perf.setDisplayName("");						
					perf.setPerfCounter("");
					perf.setInterval("60"); // �����ֱ� ( �⺻ : 60�� )
					perf.setSlot(1);
					perf.setMeasure(""); // �� ��
					perf.setScaleFunction("x"); // ������ ( �⺻ : x )
					
					addRecord(perf);
					
				}catch(Exception exception) {
					// ���̺� ���� �߰� ���� �� ���ܰ� �߻��ϸ� �ƹ��͵� �������� ����
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
					sb.append(String.format("���� ���̺� ���� �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\n���� ������ �����߿��� ���ڵ带 ���� �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("���� XML Convertiong �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong ������ �����߿��� ���ڵ带 ���� �� �� �����ϴ�%s\n", Util.separator));
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
					sb.append(String.format("�̹� ���̺� ���� �۾� �����尡 �������Դϴ�%s\n", Util.separator));					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("���� XML Convertiong �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong ������ �����߿��� ���̺� ���� �۾��� �� �� �����ϴ�%s\n", Util.separator));
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
					sb.append(String.format("%s%s\n", Util.colorBlue("���� ���� �ڵ� ���� ���"), Util.separator));
					sb.append("[ ���̺� ���� ] ��� ���� " + Util.separator + "\n\n");
					sb.append("�Էµ� ���ɸ� �˻� �� �ش� ���ɸ� ������ ������ �ڵ����� �����˴ϴ�" + Util.separator + "\n\n");					
					sb.append("�ش� ����� �ܼ� ���� ����̹Ƿ� �ݵ�� �ڵ� ������ ������ ���� ������ Ȯ�����ּ���  !"  + Util.separator +  "\n");
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
					sb.append("Item Upload �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
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
					sb.append(String.format("���� ���̺� ���� �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\n���� ������ �����߿��� ���̺��� �ʱ�ȭ �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isConverting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>XML Convertiong Thread is Working</font>\n");
					sb.append(String.format("���� XML Convertiong �۾� �����尡 �������Դϴ�%s\n", Util.separator));
					sb.append(String.format("\nXML Convertiong ������ �����߿��� ���̺��� �ʱ�ȭ �� �� �����ϴ�%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				resetTable(table);				
				AgentXmlGeneratorFrame.perfList.clear();
			}
		});
		
		
		// �������� ȭ�� ������� �����ȴ�		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		AgentXmlGeneratorFrame.isExist = false;
		super.dispose();
	}
	
	public static void resetTable(JTable table){
		// ���̺� ��� ����
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				
			},
			new String[] {
				"\uC21C \uC11C", // ����
				"\uC131\uB2A5\uBA85", // ���ɸ� 
				"���� ī����",  // ���� ī����
				"�� ��",  // ����
				"\uC218\uC9D1\uC8FC\uAE30", // �����ֱ� 
				"\uB2E8 \uC704", // ����
				"\uBCF4\uC815\uC2DD", // ������ 
				"\uC774\uC9C4 \uC0C1\uD0DC : 0", // ���� ���� : 0 
				"\uC774\uC9C4 \uC0C1\uD0DC : 1", // ���� ���� : 1
				"\uB2E4\uC911 \uC0C1\uD0DC" // ���� ����
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, // ����
				true, // ���ɸ�
				true, // ���� ī����
				true, // ����
				true, // ���� �ֱ�
				true, // ����
				true, // ������
				true, // ���� ���� : 0
				true, // ���� ���� : 1
				true // ���� ����
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(ORDER).setPreferredWidth(70); // ����
		table.getColumnModel().getColumn(PERF_NAME).setPreferredWidth(350); // ���ɸ�
		table.getColumnModel().getColumn(PERF_COUNTER).setPreferredWidth(250); // ���� ī����
		table.getColumnModel().getColumn(SLOT).setPreferredWidth(80); // ����
		table.getColumnModel().getColumn(INTERVAL).setPreferredWidth(80); // �����ֱ�
		table.getColumnModel().getColumn(MEASURE).setPreferredWidth(80); // ����
		table.getColumnModel().getColumn(SCALE_FUNCTION).setPreferredWidth(130); // ������ 
		table.getColumnModel().getColumn(LABLE_0).setPreferredWidth(120); // ���� ���� : 0
		table.getColumnModel().getColumn(LABLE_1).setPreferredWidth(120); // ���� ���� : 1
		table.getColumnModel().getColumn(LABLE_STATUS).setPreferredWidth(320); // ���� ����
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false); // �÷� ��ġ ���� ���� �Ұ�
//		table.getTableHeader().setResizingAllowed(false); // �÷� ���̵� ũ�� ���� ���� �Ұ�
		
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
								
					sb.append(String.format("Agent ���� %s�� �׸��� ��ȯ �Ͻðڽ��ϱ�?%s%s\n\n",Util.colorBlue(String.valueOf(perfs.length)) ,Util.separator, Util.separator));
					
					if(useAutoEvent.isSelected()) {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("XML ���뿡 �ڵ� ��� �̺�Ʈ ������ ���Ե˴ϴ�"), Util.separator ,Util.separator));
					}else {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("XML ���뿡 �ڵ� ��� �̺�Ʈ ������ ���Ե��� �ʽ��ϴ�"), Util.separator ,Util.separator));
					}
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// ���� �߰� ��û ���
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
						sb.append(String.format("XML ��ȯ �۾��� ����Ͽ����ϴ�%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						AgentXmlGeneratorFrame.isConverting = false;
						return;
					}else {
						StringBuilder msg = new StringBuilder();
						msg.append("<font color='Green'>XML File Encoding</font>\n");
						msg.append("XML ������ ���ڵ� ����� �������ּ���" + Util.separator + Util.separator +"\n\n");
						msg.append(String.format("MK119 4.2 Version ���� : %s%s%s\n", Util.colorBlue("EUC-KR"), Util.separator, Util.separator));
						msg.append(String.format("MK119 4.5 Version �̻� : %s%s%s\n", Util.colorBlue("UTF-8"), Util.separator, Util.separator));

						int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

						switch (menu) {
							case -1: // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� ������ ��
								sb = new StringBuilder();
								sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
								sb.append(String.format("XML ��ȯ �۾��� ����Ͽ����ϴ�%s\n", Util.separator));											
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								AgentXmlGeneratorFrame.isConverting = false;
								return;
							case 0: // ù ��° ��ư : EUC-KR
								encoding = "euc-kr";
								break;
							case 1: // �� ��° ��ư
								encoding = "utf-8";
								break;
						}
					}
					
					// ������ �߰��ϴ� ������ ���̺��� �����ʹ� ���� �� �������̴�
					resetTable(table);		
//					Arrays.sort(perfs);		
					addRecord(perfs);
					
					// �ڵ� �̺�Ʈ ��� �ɼ�
					if(useAutoEvent.isSelected()) Perf.initPerfEvent(perfs);
					
					// ���� �ּ� ǥ�� ��Ŀ� ���� strPerfCounter �ʱ�ȭ
					XmlGenerator.generateXML(perfs, useAutoEvent.isSelected(), encoding, "agent");
					
					isConverting = false;
				}
			});
		
			thread.start();
			
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to XML Converting</font>\n");
			sb.append(String.format("XML ���� ��ȯ �۾��� ���ܰ� �߻��Ͽ����ϴ�%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}finally {
			isConverting = false;
		}
	}

	/**
	 * 	���ڵ� �߰� �޼ҵ� 
	 */
	public static void addRecord(Perf... perf) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < perf.length; i++) {
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// ���̺��� �� ������ 0�� �� ��� : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// ���̺��� �� ������ �ּ� 1�� �̻� �� ��� ������ ���ڵ��� ( ���� �÷� �� + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // ����
				/* column[1] */ record.add(perf[i].getDisplayName()); // ���ɸ�
				/* column[2] */ record.add(perf[i].getPerfCounter());  // ���� ī����
				/* column[3] */ record.add(perf[i].getSlot());  // �� ��
				/* column[4] */ record.add(perf[i].getInterval()); // �����ֱ�
				/* column[5] */ record.add(perf[i].getMeasure()); // ����
				/* column[6] */ record.add(perf[i].getScaleFunction()); // ������
								
				switch(perf[i].getDataFormat()) {
				case "1" : 
					/* column[7] */ record.add(perf[i].getBinaryMap().get("0")); // ���� ���� : 0
					/* column[8] */ record.add(perf[i].getBinaryMap().get("1")); // ���� ���� : 1
					/* column[9] */ record.add(""); // ���� ����
					break;
				case "2" :
					String multiStatus = Perf.parseMultiStatusSring(perf[i].getMultiStatusMap());
					/* column[7] */ record.add(""); // ���� ���� : 0
					/* column[8] */ record.add(""); // ���� ���� : 1
					/* column[9] */ record.add(multiStatus); // ���� ����
					break;
				case "3" :
					/* column[7] */ record.add(""); // ���� ���� : 0
					/* column[8] */ record.add(""); // ���� ���� : 1
					/* column[9] */ record.add(""); // ���� ����
					break;
				}
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// ���ڵ� �߰� �� ���� �߻� �� �ƹ��͵� �������� ����
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ڵ� ���� �޼ҵ�
	 */
	public void removeRecord(int... index) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		if(index.length < 0) {
			// ���� �� ���� ���ų�
			if(table.getRowCount()==0) {
				// ���̺� ���� ���� ��� �ƹ��͵� �������� ����
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
	 * ���̺� ����
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
					Util.showMessage(String.format("%s%s%s\n���̺� ���� �Ϸ�%s\n", Util.colorBlue("Inspection Successful"), Util.separator, Util.separator, Util.separator), JOptionPane.INFORMATION_MESSAGE);
					
					isInspecting = false;
				}
			});
			
			thread.start();
			
		}catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Table Validation Exception</font>\n");
			sb.append(String.format("���̺� ���� �۾��� ���ܰ� �߻��Ͽ����ϴ�%s\n", Util.separator));
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
			
			// ���� �� -----------------------------------------------------------------------------------
			String perfName = String.valueOf(data.get(PERF_NAME)).trim();
			
			if(option == null) {
			
				// ���ɸ� �ʵ尡 ���� �� ���
				if((perfName == null) || (perfName.equals("") || perfName.length() < 1)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���ɸ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\n���ɸ��� �ݵ�� �Է��ؾ� �ϴ� �ʵ��Դϴ�%s\n", Util.separator));				
					sb.append(String.format("\n���̺� <font color='blue'>%s</font> ���� ���ɸ� ������ �Է����ּ���%s\n",String.valueOf(data.get(ORDER)), Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, PERF_NAME);
					perfCheckOk = false;
					return null;
				}
				
				// ���ɸ� Ư������ �˻�
				if(!Inspecter.isVaildName(perfName)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���ɸ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\n���ɸ� �Ʒ��� Ư�� ���ڸ� ������ Ư�� ���ڴ� ���� �� �� �����ϴ�%s\n", Util.separator));
					sb.append(String.format("\n���ɸ� ���� ��� Ư�� ���� : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
					sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ���ɸ� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfName ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, PERF_NAME);
					perfCheckOk = false;
					return null;			
				}
			
			}// end option check
			
			perfs[i].setDisplayName(perfName); 
			
			// ���� ī���� ---------------------------------------------------------------------------------
			String perfCounter = String.valueOf(data.get(PERF_COUNTER)).trim();						
			perfs[i].setPerfCounter(perfCounter);
			
			// �� �� -------------------------------------------------------------------------------------
			int slot = 0;
			
			try {
				slot = Integer.parseInt(String.valueOf(data.get(SLOT)).trim());
				
				if(slot < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>����</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n������ 1 �̻��� ���� ���� ���� �Է� �� �� �ֽ��ϴ�%s\n\n", Util.separator));
					sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(SLOT)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, SLOT);
					perfCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>����</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("�Էµ� ���� ������ <font color='blue'>���� ��</font>���� ��ȯ �� �� �����ϴ�%s\n\n", Util.separator));
				sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(SLOT)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, SLOT);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setSlot(slot);
			
			// ���� �ֱ�  ----------------------------------------------------------------------------------
			String interval = String.valueOf(data.get(INTERVAL)).trim();
			
			try{
				int actualInterval = Integer.parseInt(interval);
				
				if(actualInterval < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �ֱ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n���� �ֱ�� 1 �̻��� ���� ���� ���� �Է� �� �� �ֽ��ϴ�%s\n\n", Util.separator));
					sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� �����ֱ� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, INTERVAL);
					perfCheckOk = false;
					return null;
				}
				
			}catch (Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �ֱ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
				sb.append(String.format("\n�Էµ� �����ֱ� ������ <font color='blue'>���� ��</font>���� ��ȯ �� �� �����ϴ�%s\n\n", Util.separator));
				sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� �����ֱ� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, INTERVAL);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setInterval(interval);
			
			// �� ��  ------------------------------------------------------------------------------------
			perfs[i].setMeasure(String.valueOf(data.get(MEASURE)).trim());
			perfName = perfs[i].getDisplayName();

			// ��ȿ�� ���ɸ��� �ԷµǾ��� ������ �Էµ��� ���� ���
			if(useAutoMeasure.isSelected()) {
				if(perfName != null && perfName.length() > 1 && !perfName.equalsIgnoreCase("")) { 
					if(perfs[i].getMeasure().length() == 0 || perfs[i].getMeasure().equals("")) {					
						perfs[i].setMeasure(Perf.createMeasure(perfName).trim());
					}
				}
			}
			
			
			// ������ ------------------------------------------------------------------------------------
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
				// ������ ��ȿ�� �˻�
				Calculator.checkFormula(scaleFunction, 1);
				if (scaleFunction.equalsIgnoreCase("") || scaleFunction.length() < 1 || !scaleFunction.contains("x"))
					throw new Exception("������ ���� ����");				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>������</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ������ ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase() ,Util.separator));				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, SCALE_FUNCTION);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setScaleFunction(scaleFunction);		
			
			// ���� ���� : 0, 1 ---------------------------------------------------------------------------
			HashMap binaryMap = perfs[i].getBinaryMap();
			String label0 = String.valueOf(data.get(LABLE_0)).trim();
			String label1 = String.valueOf(data.get(LABLE_1)).trim();
			
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				// ���� ���� ������ ���Ŀ��� ���̺��� �ϳ��� ���� �� ���� ���� 
				if((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1))) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 1</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));																
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);					
					setFocusCell(table, i, LABLE_1);
					perfCheckOk = false;
					return null;
				}
			}else if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
				if((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1))) {
					// ���� ���� ������ ���Ŀ��� ���̺��� �ϳ��� ���� �� ���� ����
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 0</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
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
			
			
			// ���� ���� --------------------------------------------------------------------------			
			HashMap multiStatusMap = new HashMap();
			
			if ((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if ((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					if((String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
						StringBuilder sb = new StringBuilder();
						sb.append("<font color='red'>Table Validation Exception</font>\n");
						sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font>�� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
						sb.append(String.format("������ ������ <font color='blue'>���� ����</font> �̸鼭 ���ÿ� <font color='blue'>���� ����</font> �� ���� �����ϴ�%s\n", Util.separator));
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
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ���� ���� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(LABLE_STATUS)).trim() ,Util.separator));
				if(e instanceof NumberFormatException) {
					sb.append(String.format("\n���� ���� �Է� ��� : <font color='blue'>����1; ����1; ����2; ����2; ����3; ����3; ...</font>%s%s\n", Util.separator, Util.separator));
				}
				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, LABLE_STATUS);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setMultiStatusMap(multiStatusMap);
						
			
			// ������ ���� -------------------------------------------------------------------------------
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					
					// ������ ������ ���� �����̸鼭 ���� ���� �� ���� ����
					if((multiStatusMap.size() >= 1) || (String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
						StringBuilder sb = new StringBuilder();
						sb.append("<font color='red'>Table Validation Exception</font>\n");
						sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font>�� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
						sb.append(String.format("������ ������ <font color='blue'>���� ����</font> �̸鼭 ���ÿ� <font color='blue'>���� ����</font> �� ���� �����ϴ�%s\n", Util.separator));
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
			throw new RuntimeException("���� ���� ���� ����");
		}else {
			for(int i = 0; i < tokens.length; i+=2) {
				
				// inspect NumberFormatException : ���� ���´� ��-���� �������� ���εǾ�� �Ѵ�
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
	 * ���ڷ� �Ѱ��� JTable�� ù��° �÷��� ������ ��� �������ش�.
	 * �ַ� �ε��� �÷��� ǥ�����ֱ� ���ؼ� ����
	 */	
	public static void setCellContentCenter(JTable table) {
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // ����
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // ���ɸ�
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ���� ī����
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // ���� �ֱ�
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // ����
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // ������
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // ���� ���� : 0
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // ���� ���� : 1
//		tcmSchedule.getColumn(9).setCellRenderer(tScheduleCellRenderer); // ���� ����	
		
	}

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		AgentXmlGeneratorFrame.table = table;
	}
}
