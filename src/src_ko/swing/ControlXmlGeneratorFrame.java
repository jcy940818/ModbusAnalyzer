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
	
	private static final int ORDER = 0; // �� ��
	private static final int CONTROL_NAME = 1; // ���� �̸�
//	private static final int CONTROL_COUNTER = 2; // ���� ī���� (������� �ʴ� �ʵ�)
	private static final int CONTROL_COMMAND = 2; // ���� ��ɾ�
	private static final int CONTROL_DESC = 3; // ���� ����
	private static final int CONTROL_USE_PARAM = 4; // �Ķ���� ��뿩��
	private static final int CONTROL_WAIT_TIME = 5; // ���� Ÿ�Ӿƿ�

	// XML Convertiong ��� ����	
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// ������ ���� ����
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
					sb.append(String.format("%s%s\n", Util.colorBlue("���� �̺�Ʈ �ڵ� ��� ���"), Util.separator));
					sb.append("XML ��ȯ�� �ڵ� ��� �̺�Ʈ ������ ���Ե˴ϴ�\n\n");
					sb.append("�ڵ� ��� �̺�Ʈ�� �̺�Ʈ �̸��� ������ ��� ������ �����ϰ� ����Ǿ� ��ϵ˴ϴ� " + Util.separator + "\n");
					sb.append("\n�ݵ�� �̺�Ʈ ���� ������ Ȯ�����ּ��� !\n");
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
					 sb.append("�̺�Ʈ ���� �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
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
					
					ControlAction control = new ControlAction();
					control.setControlName("");
					control.setControlCounter("CONTROL");
					control.setCommand("");
					control.setDesc("");
					control.setUseParam(1);
					control.setWaitTime(1);
					
					addRecord(control);
					
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
		useAutoMeasure.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoMeasure.setBackground(Color.WHITE);
		useAutoMeasure.setBounds(854, 15, 161, 23);
		useAutoMeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoMeasure.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("���� ���� �ڵ� ���� ���"), Util.separator));
					sb.append("[ ���̺� ���� ] ��� ���� " + Util.separator + "\n\n");
					sb.append("�Էµ� ����� �˻� �� �ش� ����� ������ ������ �ڵ����� �����˴ϴ�" + Util.separator + "\n\n");					
					sb.append("�ش� ����� �ܼ� ���� ����̹Ƿ� �ݵ�� �ڵ� ������ ������ ���� ������ Ȯ�����ּ���  !"  + Util.separator +  "\n");
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
				ControlXmlGeneratorFrame.controlActionfList.clear();
			}
		});
		
		
		// �������� ȭ�� ������� �����ȴ�		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ControlXmlGeneratorFrame.isExist = false;
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
				"�� ��", // ����
				"���� �̸�", // ���� �̸� 
//				"���� ī����",  // ���� ī����
				"���� ��ɾ�",  // ���� ��ɾ�
				"���� ����", // ���� ����(����) 
				"�Ķ���� ��뿩��", // �Ķ���� ��뿩��
				"���� Ÿ�Ӿƿ�" // ���� Ÿ�Ӿƿ�
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, // ����
				true, // ���� �̸�
//				true, // ���� ī����
				true, // ���� ��ɾ�
				true, // ���� ����
				true, // �Ķ���� ��뿩��
				true, // ���� Ÿ�Ӿƿ�
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table.getColumnModel().getColumn(ORDER).setPreferredWidth(70); // ����
		table.getColumnModel().getColumn(CONTROL_NAME).setPreferredWidth(260); // ���� �̸�
//		table.getColumnModel().getColumn(CONTROL_COUNTER).setPreferredWidth(100); // ���� ī����
		table.getColumnModel().getColumn(CONTROL_COMMAND).setPreferredWidth(280); // ���� ��ɾ�
		table.getColumnModel().getColumn(CONTROL_DESC).setPreferredWidth(350); // ���� ����
		table.getColumnModel().getColumn(CONTROL_USE_PARAM).setPreferredWidth(150); // ���� �Ķ���� ��뿩��
		table.getColumnModel().getColumn(CONTROL_WAIT_TIME).setPreferredWidth(120); // ���� Ÿ�Ӿƿ�
		
		// �� ũ�� ���� ���� �Ұ�
		table.getTableHeader().setReorderingAllowed(false); // �÷� ��ġ ���� ���� �Ұ�
//		table.getTableHeader().setResizingAllowed(false); // �÷� ���̵� ũ�� ���� ���� �Ұ�
		
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
								
					sb.append(String.format("���� %s�� �׸��� ��ȯ �Ͻðڽ��ϱ�?%s%s\n",Util.colorBlue(String.valueOf(controls.length)) ,Util.separator, Util.separator));
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// ���� �߰� ��û ���
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel Convert to XML File</font>%s\n", Util.separator));
						sb.append(String.format("XML ��ȯ �۾��� ����Ͽ����ϴ�%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						ControlXmlGeneratorFrame.isConverting = false;
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
								ControlXmlGeneratorFrame.isConverting = false;
								return;
							case 0: // ù ��° ��ư : EUC-KR
								encoding = "euc-kr";
								break;
							case 1: // �� ��° ��ư
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
	public static void addRecord(ControlAction... control) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < control.length; i++) {
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// ���̺��� �� ������ 0�� �� ��� : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// ���̺��� �� ������ �ּ� 1�� �̻� �� ��� ������ ���ڵ��� ( ���� �÷� �� + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // �� ��
				/* column[1] */ record.add(control[i].getControlName()); // ���� �̸�
//				/* column[2] */ record.add(control[i].getControlCounter());  // ���� ī����
				/* column[3] */ record.add(control[i].getCommand());  // ���� ��ɾ�
				/* column[4] */ record.add(control[i].getDesc()); // ���� ����
				/* column[5] */ record.add(control[i].getUseParam()); // ���� �Ķ���� ��뿩��
				/* column[6] */ record.add(control[i].getWaitTime()); // ���� Ÿ�Ӿƿ�												
				
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
			Integer num = Integer.parseInt(table.getValueAt(i, 0).toString().trim());
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
			
			
			// ���� �̸� -----------------------------------------------------------------------------------
			String controlName = String.valueOf(data.get(CONTROL_NAME)).trim();
			
		
			// ����� �ʵ尡 ���� �� ���
			if((controlName == null) || (controlName.equals("") || controlName.length() < 1)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �̸�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n���� �̸��� �ݵ�� �Է��ؾ� �ϴ� �ʵ��Դϴ�%s\n", Util.separator));				
				sb.append(String.format("\n���̺� <font color='blue'>%s</font> ���� ���� �̸� ������ �Է����ּ���%s\n",String.valueOf(data.get(ORDER)), Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_NAME);
				ControlCheckOk = false;
				return null;
			}
			
			// ����� Ư������ �˻�
			if(!Inspecter.isVaildName(controlName)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �̸�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\n���� �̸����� �Ʒ��� Ư�� ���ڸ� ������ Ư�� ���ڴ� ���� �� �� �����ϴ�%s\n", Util.separator));
				sb.append(String.format("\n���� �̸� ���� ��� Ư�� ���� : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
				sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ���� �̸� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), controlName ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_NAME);
				ControlCheckOk = false;
				return null;
			}
			
			controls[i].setControlName(controlName);
			
			// ���� ī���� ---------------------------------------------------------------------------------
//			String controlCounter = String.valueOf(data.get(CONTROL_COUNTER)).trim();
//			controls[i].setControlCounter(controlCounter);
			
			// ���� ��ɾ� -------------------------------------------------------------------------------------
			String controlCommand = String.valueOf(data.get(CONTROL_COMMAND)).trim();
			controls[i].setCommand(controlCommand);
			
			// ���� ����  ----------------------------------------------------------------------------------
			String controlDesc = String.valueOf(data.get(CONTROL_DESC)).trim();
			controls[i].setDesc(controlDesc);
			
			// �Ķ���� ��뿩��  ------------------------------------------------------------------------------------
			int useParam;
						
			try {
				useParam = Integer.parseInt(String.valueOf(data.get(CONTROL_USE_PARAM)).trim());
				
				if(!(useParam <= 1 && useParam >= 0)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �Ķ���� ��뿩��</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n������ �Ķ���� ��� ���δ� 0(�̻��), 1(���) �ΰ��� ���� ���� �Է� �� �� �ֽ��ϴ�%s\n\n", Util.separator));
					sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� �Ķ���� ��뿩�� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_USE_PARAM)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, CONTROL_USE_PARAM);
					ControlCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �Ķ���� ��뿩��</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("�Էµ� ������  �Ķ���� ��뿩�� ������ <font color='blue'>���� ��</font>���� ��ȯ �� �� �����ϴ�%s\n\n", Util.separator));
				sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� �Ķ���� ��뿩�� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_USE_PARAM)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, CONTROL_USE_PARAM);
				ControlCheckOk = false;
				return null;
			}
			
			controls[i].setUseParam(useParam);
			
			// ���� Ÿ�Ӿƿ� ------------------------------------------------------------------------------------
			int waitTime;
			
			try {
				waitTime = Integer.parseInt(String.valueOf(data.get(CONTROL_WAIT_TIME)).trim());
				
				if(waitTime < 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Table Validation Exception</font>\n");
					sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� Ÿ�Ӿƿ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\n���� Ÿ�Ӿƿ� ���� 1 �̻��� ���� ���� ���� �Է� �� �� �ֽ��ϴ�%s\n\n", Util.separator));
					sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� Ÿ�Ӿƿ� ���� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_WAIT_TIME)).trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, CONTROL_WAIT_TIME);
					ControlCheckOk = false;
					return null;
				}
				
			}catch(Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Table Validation Exception</font>\n");
				sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� Ÿ�Ӿƿ�</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("�Էµ� ���� Ÿ�Ӿƿ� ������ <font color='blue'>���� ��</font>���� ��ȯ �� �� �����ϴ�%s\n\n", Util.separator));
				sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ���� Ÿ�Ӿƿ� �� : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(CONTROL_WAIT_TIME)).trim() ,Util.separator));
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
		
		tcmSchedule.getColumn(ORDER).setCellRenderer(tScheduleCellRenderer); // ����
//		tcmSchedule.getColumn(CONTROL_NAME).setCellRenderer(tScheduleCellRenderer); // ���� �̸�
//		tcmSchedule.getColumn(CONTROL_COUNTER).setCellRenderer(tScheduleCellRenderer); // ���� ī����
		tcmSchedule.getColumn(CONTROL_COMMAND).setCellRenderer(tScheduleCellRenderer); // ���� ��ɾ�
//		tcmSchedule.getColumn(CONTROL_DESC).setCellRenderer(tScheduleCellRenderer); // ���� ����
		tcmSchedule.getColumn(CONTROL_USE_PARAM).setCellRenderer(tScheduleCellRenderer); // ���� �Ķ���� ��뿩��
		tcmSchedule.getColumn(CONTROL_WAIT_TIME).setCellRenderer(tScheduleCellRenderer); // ���� Ÿ�Ӿƿ�
		
	}

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		ControlXmlGeneratorFrame.table = table;
	}
}
