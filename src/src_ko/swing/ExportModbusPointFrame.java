package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.agent.RestAgent;
import common.modbus.ModbusPointExporter;
import common.modbus.ModbusWatchPoint;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import common.util.FontManager;
import common.util.SwingUtil;
import common.web.AdminConsole_Info;
import src_ko.agent.HttpAgent;
import src_ko.agent.ModbusAgent;
import src_ko.agent.ModbusFacility;
import src_ko.agent.Perf;
import src_ko.database.DbUtil;
import src_ko.util.Util;

public class ExportModbusPointFrame extends JFrame {

	public static AdminConsole_Info adminConsole = null;
	public static boolean working = false;
	
	public static ModbusFacility facility = null;
	public static HashMap<String, ModbusFacility> facilityMap = null;
	
	public static ModbusWatchPoint selectedPoint = null;
	public static ArrayList<ModbusWatchPoint> pointList;
	public static JTable pointTable;
	
	public static boolean isExist = false;
	
	private JPanel contentPane;
	private JPanel actualPanel;
	
	private JRadioButton download_radioButton;
	private JRadioButton directAdd_radioButton;
	private JRadioButton mk_V4_RaidoButton;
	private JRadioButton mk_V10_RaidoButton;
	private JButton exportXML_Button;
	private JButton exportExcel_Button;
	
	public static JTextField search_TextField;
	private JScrollPane table_scrollPane = new JScrollPane();	
	
	public static JComboBox addrTypeComboBox; // �ּ� ���� �޺��ڽ�
	static {
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(0);
	}
	
	private JPanel cardPanel;
	private static CardLayout cardLayout;
	private Rectangle r = new Rectangle(100, 100, 1080, 720);
	private Color mkColor = new Color(237, 76, 55);
	
	private static JButton mk119_Button;
	private static JLabel adminConsoleInfo;
	private static JLabel serverName_Label;
	private static JTextField serverName_TextField;
	private static JButton connect_Button;
	private static JLabel serverInfo_Label;
	private static JButton addPoint_Button;
	private JSeparator separator3;
	private JLabel autoEvent_Label;
	
	private static JCheckBox useAutoEvent_CheckBox;
	private JButton updatePoint_Button;
	private JButton setEvent_Button;
	private JSeparator separator5;
	private JSeparator separator2;
	private JLabel deleteLabel;
	private JButton deleteButton;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ExportModbusPointFrame frame = new ExportModbusPointFrame();
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
	public ExportModbusPointFrame() {
		ExportModbusPointFrame.isExist = true;		
		ExportModbusPointFrame.pointList = ModbusMonitor_Panel.pointList;
		
		adminConsole = null;
		facility = null;
		facilityMap = null;
		
		setTitle("Export Modbus Point");
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
			@Override
			public void componentResized(ComponentEvent e) {
				table_scrollPane.setSize(contentPane.getWidth() - (table_scrollPane.getX() + 20), contentPane.getHeight() - (table_scrollPane.getY() + 20));    				
				table_scrollPane.setViewportView(pointTable);
				super.componentResized(e);    					
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
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 350, 55);
		actualPanel.add(currentFunction);
		
		JLabel searchLabel = new JLabel("�� ��");
		searchLabel.setBackground(Color.WHITE);
		searchLabel.setForeground(Color.BLACK);
		searchLabel.setFont(FontManager.getFont(Font.BOLD, 18));
		searchLabel.setBounds(18, 74, 50, 32);
		actualPanel.add(searchLabel);
		
		table_scrollPane = new JScrollPane();
		table_scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		table_scrollPane.setBounds(0, 111, 1044, 550);
		actualPanel.add(table_scrollPane);
		
		pointTable = new JTable();
		pointTable.setRowSelectionAllowed(false);
		pointTable.setCellSelectionEnabled(true);
		pointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pointTable.setCellSelectionEnabled(true);
		pointTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					// ���� Ŭ��
				} 
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// ���� ��ư ���� Ŭ��
				}
				if (e.getButton() == 3) {
					// ������ Ŭ��
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint point = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					ModbusWatchPoint.showInfo(point);
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
		addrTypeComboBox.setSelectedIndex(0);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(FontManager.getFont(Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(318, 38, 150, 32);
		addrTypeComboBox.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
		addrTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
			}
		});
		actualPanel.add(addrTypeComboBox);
		
		search_TextField = new JTextField();
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setForeground(Color.BLACK);
		search_TextField.setFont(FontManager.getFont(Font.PLAIN, 17));
		search_TextField.setColumns(10);
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.setBackground(Color.WHITE);
		search_TextField.setBounds(72, 75, 397, 32);
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
		actualPanel.add(search_TextField);
		
		download_radioButton = new JRadioButton("�ٿ�ε�");
		download_radioButton.setSelected(true);
		download_radioButton.setHorizontalAlignment(SwingConstants.LEFT);
		download_radioButton.setForeground(new Color(237, 76, 55));
		download_radioButton.setFont(FontManager.getFont(Font.BOLD, 20));
		download_radioButton.setFocusPainted(false);
		download_radioButton.setBackground(Color.WHITE);
		download_radioButton.setBounds(480, 11, 110, 23);
		download_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWork();				
			}
		});
		actualPanel.add(download_radioButton);
		
		directAdd_radioButton = new JRadioButton("�ٷ��߰�");
		directAdd_radioButton.setHorizontalAlignment(SwingConstants.LEFT);
		directAdd_radioButton.setForeground(Color.LIGHT_GRAY);
		directAdd_radioButton.setFont(FontManager.getFont(Font.BOLD, 20));
		directAdd_radioButton.setFocusPainted(false);
		directAdd_radioButton.setBackground(Color.WHITE);
		directAdd_radioButton.setBounds(480, 44, 110, 23);
		directAdd_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWork();
			}
		});
		actualPanel.add(directAdd_radioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(download_radioButton);
		group.add(directAdd_radioButton);
		
		mk119_Button = new JButton(new Util().getMK2Resource());		
		mk119_Button.setBounds(485, 75, 102, 30);
		mk119_Button.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119_Button.setFocusPainted(false);
		mk119_Button.setContentAreaFilled(false);
		mk119_Button.setBorder(UIManager.getBorder("Button.border"));
		mk119_Button.setBackground(Color.WHITE);
		mk119_Button.setEnabled(false);
		mk119_Button.setVisible(false);
		mk119_Button.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!AdminConsole_LoginFrame.isExist) {					
					new AdminConsole_LoginFrame(null, "ModbusExport");
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("AdminConsole Login Already Exists") + Util.separator + "\n");
					sb.append("AdminConsole Login �������� �̹� �����մϴ�" + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});		
		actualPanel.add(mk119_Button);
		
		adminConsoleInfo = new JLabel();
		adminConsoleInfo.setText("<html><font color='black'>Admin Console : </font>" + "���� ��" + "</html>");
		adminConsoleInfo.setFont(FontManager.getFont(Font.BOLD, 18));
		adminConsoleInfo.setForeground(mkColor);
		adminConsoleInfo.setBackground(Color.WHITE);
		adminConsoleInfo.setBounds(598, 7, 437, 28);
		adminConsoleInfo.setEnabled(false);
		adminConsoleInfo.setVisible(false);
		adminConsoleInfo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(adminConsole != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s", Util.colorGreen("linked with MK119 AdminConsole")));
					sb.append(Util.separator + Util.separator + "\n");
					
					String mkVersionInfo = RestAgent.getMK119Version(adminConsole);
					
					if(mkVersionInfo == null) {
						String newSession = AdminConsole_Info.refreshSession(adminConsole);
						
						if (newSession != null) {
							mkVersionInfo = RestAgent.getMK119Version(adminConsole);
						}else {
							mkVersionInfo = "���� ����";
						}
					}
					
					sb.append(String.format("%s : %s", Util.colorBlue("AdminConsole"), mkVersionInfo));
					sb.append(Util.separator + Util.separator + "\n\n");
					
					sb.append(String.format("%s : %s", Util.colorBlue("IP"), adminConsole.get_IP()));
					sb.append(Util.separator + Util.separator + "\n");
					
					sb.append(String.format("%s : %s", Util.colorBlue("Port"), adminConsole.get_PORT()));
					sb.append(Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		});
		actualPanel.add(adminConsoleInfo);
		
		serverName_Label = new JLabel("����");
		serverName_Label.setHorizontalAlignment(SwingConstants.LEFT);		
		serverName_Label.setForeground(Color.BLACK);
		serverName_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		serverName_Label.setBackground(Color.WHITE);
		serverName_Label.setBounds(598, 43, 63, 28);
		serverName_Label.setEnabled(false);
		serverName_Label.setVisible(false);
		actualPanel.add(serverName_Label);
		
		serverName_TextField = new JTextField();
		serverName_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		serverName_TextField.setForeground(Color.BLACK);
		serverName_TextField.setFont(FontManager.getFont(Font.PLAIN, 18));
		serverName_TextField.setBounds(663, 44, 308, 28);
		serverName_TextField.setColumns(10);
		serverName_TextField.setEnabled(false);
		serverName_TextField.setVisible(false);
		serverName_TextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect_Button.doClick();
			}
		});
		actualPanel.add(serverName_TextField);
		
		connect_Button = new JButton("�� ��");
		connect_Button.setForeground(Color.BLACK);
		connect_Button.setFocusPainted(false);
		connect_Button.setMargin(new Insets(2, 0, 2, 0));
		connect_Button.setFont(FontManager.getFont(Font.BOLD, 17));
		connect_Button.setFocusPainted(false);
		connect_Button.setContentAreaFilled(false);
		connect_Button.setBorder(UIManager.getBorder("Button.border"));
		connect_Button.setBackground(Color.WHITE);
		connect_Button.setBounds(975, 44, 63, 28);
		connect_Button.setEnabled(false);
		connect_Button.setVisible(false);
		connect_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String serverName = serverName_TextField.getText();
				connectFacility(serverName);		
			}
		});
		actualPanel.add(connect_Button);
		
		serverInfo_Label = new JLabel();
		serverInfo_Label.setText(String.format("<html><font color='black'>���� ��� :</font> %s</html>", "���� ��"));
		serverInfo_Label.setForeground(Color.BLUE);
		serverInfo_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		serverInfo_Label.setBackground(Color.WHITE);
		serverInfo_Label.setBounds(598, 78, 373, 28);
		serverInfo_Label.setEnabled(false);
		serverInfo_Label.setVisible(false);
		actualPanel.add(serverInfo_Label);
		
		addPoint_Button = new JButton("�� ��");
		addPoint_Button.setMargin(new Insets(2, 0, 2, 0));
		addPoint_Button.setForeground(Color.BLACK);
		addPoint_Button.setFont(FontManager.getFont(Font.BOLD, 17));
		addPoint_Button.setFocusPainted(false);
		addPoint_Button.setContentAreaFilled(false);
		addPoint_Button.setBorder(UIManager.getBorder("Button.border"));
		addPoint_Button.setBackground(Color.WHITE);
		addPoint_Button.setBounds(975, 78, 63, 28);
		addPoint_Button.setEnabled(false);
		addPoint_Button.setVisible(false);
		addPoint_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addModbusPoint(facility);
			}
		});
		actualPanel.add(addPoint_Button);
		
		JSeparator separator1 = new JSeparator();
		separator1.setOrientation(SwingConstants.VERTICAL);
		separator1.setBackground(Color.BLACK);
		separator1.setForeground(Color.BLACK);
		separator1.setBounds(1050, 0, 1, 111);
		actualPanel.add(separator1);
		
		JLabel modbusPoint_Label = new JLabel("Modify Point");
		modbusPoint_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		modbusPoint_Label.setForeground(Color.BLACK);
		modbusPoint_Label.setBackground(Color.WHITE);
		modbusPoint_Label.setBounds(1062, 7, 135, 34);
		actualPanel.add(modbusPoint_Label);
		
		updatePoint_Button = new JButton("����Ʈ ����");
		updatePoint_Button.setForeground(Color.BLACK);
		updatePoint_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		updatePoint_Button.setBackground(Color.WHITE);
		updatePoint_Button.setBounds(1065, 60, 120, 42);
		updatePoint_Button.setFocusPainted(false);
		updatePoint_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ArrayList<ModbusWatchPoint> selectedPointList = ModbusMonitor_Panel.getSelectedModbusPoint(pointTable);
					if (selectedPointList == null || selectedPointList.size() < 1) {
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s", Util.colorBlue("���õ� ����Ʈ ����")));
						sb.append(Util.separator + Util.separator + "\n");					
						
						sb.append("���̺��� �����Ͻ� ������ ����Ʈ�� ���� �� �ٽ� �õ����ּ���");
						sb.append(Util.separator + Util.separator + "\n");
						
						Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if(!ModifyModbusWatchPointFrame.isExist) {
						new ModifyModbusWatchPointFrame(selectedPointList, "ExportModbusPointFrame");
						
					 }else {
						 ModifyModbusWatchPointFrame.addPointList(selectedPointList);
						 ModifyModbusWatchPointFrame.doTableFilter();
					 }
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		actualPanel.add(updatePoint_Button);
		
		separator2 = new JSeparator();
		separator2.setOrientation(SwingConstants.VERTICAL);
		separator2.setForeground(Color.BLACK);
		separator2.setBackground(Color.BLACK);
		separator2.setBounds(1200, 0, 1, 111);
		actualPanel.add(separator2);
		
		deleteLabel = new JLabel("Delete Point");
		deleteLabel.setForeground(Color.BLACK);
		deleteLabel.setFont(FontManager.getFont(Font.BOLD, 18));
		deleteLabel.setBackground(Color.WHITE);
		deleteLabel.setBounds(1214, 7, 125, 34);
		actualPanel.add(deleteLabel);
		
		deleteButton = new JButton("����Ʈ ����");
		deleteButton.setForeground(Color.BLACK);
		deleteButton.setFont(FontManager.getFont(Font.BOLD, 16));
		deleteButton.setFocusPainted(false);
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setBounds(1215, 60, 120, 42);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ArrayList<ModbusWatchPoint> selectedPointList = ModbusMonitor_Panel.getSelectedModbusPoint(pointTable);
					if (selectedPointList == null || selectedPointList.size() < 1) {
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s", Util.colorBlue("���õ� ����Ʈ ����")));
						sb.append(Util.separator + Util.separator + "\n");					
						
						sb.append("���̺��� �����Ͻ� ������ ����Ʈ�� ���� �� �ٽ� �õ����ּ���");
						sb.append(Util.separator + Util.separator + "\n");
						
						Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if (selectedPointList == null || selectedPointList.size() < 1) {
						return;
						
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s", Util.colorGreen("Delete the selected Modbus Point")));
						sb.append(Util.separator + Util.separator + "\n");
						
						sb.append(String.format("�����Ͻ� ������ ����Ʈ %s�� �׸��� ���� �����Ͻðڽ��ϱ�?", Util.colorBlue(String.valueOf(selectedPointList.size()))));
						sb.append(Util.separator + Util.separator + "\n");
						
						int menu = Util.showConfirm(sb.toString());
						
						if(menu != JOptionPane.OK_OPTION) {
							// ������ ����Ʈ ���� ���
							return;
						}
						
						for (ModbusWatchPoint wp : selectedPointList) {
							ModbusMonitor_Panel.pointList.remove(wp);
						}

						ModbusMonitor_Panel.doTableFilter(false);
						ModifyModbusWatchPointFrame.doTableFilter();
						ExportModbusPointFrame.updateTable();
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		actualPanel.add(deleteButton);
		
		separator3 = new JSeparator();
		separator3.setOrientation(SwingConstants.VERTICAL);
		separator3.setForeground(Color.BLACK);
		separator3.setBackground(Color.BLACK);
		separator3.setBounds(1349, 0, 1, 111);
		actualPanel.add(separator3);
		
		autoEvent_Label = new JLabel("Set Auto Event");
		autoEvent_Label.setForeground(Color.BLACK);
		autoEvent_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		autoEvent_Label.setBackground(Color.WHITE);
		autoEvent_Label.setBounds(1359, 6, 183, 34);
		actualPanel.add(autoEvent_Label);
		
		useAutoEvent_CheckBox = new JCheckBox("�̺�Ʈ �ڵ� ��� ���");
		useAutoEvent_CheckBox.setFont(FontManager.getFont(Font.BOLD, 15));
		useAutoEvent_CheckBox.setForeground(Color.BLACK);
		useAutoEvent_CheckBox.setBackground(Color.WHITE);
		useAutoEvent_CheckBox.setBounds(1361, 45, 183, 23);
		useAutoEvent_CheckBox.setFocusPainted(false);
		useAutoEvent_CheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(useAutoEvent_CheckBox.isSelected()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s\n", Util.colorBlue("���� �̺�Ʈ �ڵ� ��� �ɼ� ���"), Util.separator));
					sb.append("Export ��� ���� �ڵ� ��� �̺�Ʈ ������ ���Ե˴ϴ�\n\n");
					sb.append("�ڵ� ��� �̺�Ʈ�� �̺�Ʈ �̸��� ������ ��� ������ �����ϰ� ����Ǿ� ��ϵ˴ϴ� " + Util.separator + Util.separator + "\n\n");
					sb.append("�ݵ�� �̺�Ʈ ���� ������ Ȯ�����ּ��� !" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorGreen("( �ش� �ɼ��� MK119 V4 ����Ʈ���� ����˴ϴ� )" + Util.separator + Util.separator + "\n"));
					
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				}				
			}
		});
		actualPanel.add(useAutoEvent_CheckBox);
		
		setEvent_Button = new JButton("�̺�Ʈ ����");
		setEvent_Button.setForeground(Color.BLACK);
		setEvent_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		setEvent_Button.setFocusPainted(false);
		setEvent_Button.setBackground(Color.WHITE);
		setEvent_Button.setBounds(1362, 74, 182, 32);
		setEvent_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(!EventInfoFrame.isExist) {
					 new EventInfoFrame(Color.DARK_GRAY);
				 }else {
					 StringBuilder sb = new StringBuilder();
					 sb.append(Util.colorRed("Event Frame Already Exists") + Util.separator + Util.separator + "\n");
					 sb.append("�̺�Ʈ ���� �������� �̹� �����ֽ��ϴ�" + Util.separator + Util.separator + "\n");
					 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					 return;
				 }
			}
		});
		actualPanel.add(setEvent_Button);
		
		ActionListener workListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(download_radioButton.isSelected()) {
					download_radioButton.setForeground(mkColor);
					directAdd_radioButton.setForeground(Color.LIGHT_GRAY);
					
				}else {
					download_radioButton.setForeground(Color.LIGHT_GRAY);
					directAdd_radioButton.setForeground(mkColor);
					
				}
			}
		};
		download_radioButton.addActionListener(workListener);
		directAdd_radioButton.addActionListener(workListener);
		
		mk_V4_RaidoButton = new JRadioButton("MK119  V4");
		mk_V4_RaidoButton.setSelected(true);
		mk_V4_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V4_RaidoButton.setForeground(new Color(237, 76, 55));
		mk_V4_RaidoButton.setFont(FontManager.getFont(Font.BOLD, 20));
		mk_V4_RaidoButton.setFocusPainted(false);
		mk_V4_RaidoButton.setBackground(Color.WHITE);
		mk_V4_RaidoButton.setBounds(587, 11, 151, 23);
		actualPanel.add(mk_V4_RaidoButton);
		
		mk_V10_RaidoButton = new JRadioButton("MK119  V10");
		mk_V10_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
		mk_V10_RaidoButton.setFont(FontManager.getFont(Font.BOLD, 20));
		mk_V10_RaidoButton.setFocusPainted(false);
		mk_V10_RaidoButton.setBackground(Color.WHITE);
		mk_V10_RaidoButton.setBounds(587, 44, 151, 23);
		actualPanel.add(mk_V10_RaidoButton);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(mk_V4_RaidoButton);
		group2.add(mk_V10_RaidoButton);
		
		ActionListener mkVerionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mk_V4_RaidoButton.isSelected()) {
					mk_V4_RaidoButton.setForeground(mkColor);
					mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
					exportXML_Button.setEnabled(true);
				}else {
					mk_V4_RaidoButton.setForeground(Color.LIGHT_GRAY);
					mk_V10_RaidoButton.setForeground(mkColor);
					exportXML_Button.setEnabled(false);
				}
			}
		};
		mk_V4_RaidoButton.addActionListener(mkVerionListener);
		mk_V10_RaidoButton.addActionListener(mkVerionListener);
		
		exportXML_Button = new JButton(" XML");
		exportXML_Button.setIcon(new Util().getXMLImage());
		exportXML_Button.setForeground(Color.BLACK);
		exportXML_Button.setFont(FontManager.getFont(Font.BOLD, 20));
		exportXML_Button.setFocusPainted(false);
		exportXML_Button.setBackground(Color.WHITE);
		exportXML_Button.setBounds(740, 8, 140, 66);
		exportXML_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ModbusPointExporter.exportXML(addrTypeComboBox.getSelectedItem().toString(), useAutoEvent_CheckBox.isSelected(), pointList);
				
			}
		});
		actualPanel.add(exportXML_Button);
		
		exportExcel_Button = new JButton(" Excel");
		exportExcel_Button.setIcon(new Util().getExcelImage());
		exportExcel_Button.setForeground(Color.BLACK);
		exportExcel_Button.setFont(FontManager.getFont(Font.BOLD, 20));
		exportExcel_Button.setFocusPainted(false);
		exportExcel_Button.setBackground(Color.WHITE);
		exportExcel_Button.setBounds(887, 8, 150, 66);
		exportExcel_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int mkVersion = (mk_V4_RaidoButton.isSelected()) ? 4 : 10;
				
				ModbusPointExporter.exportExcel(mkVersion, addrTypeComboBox.getSelectedItem().toString(), useAutoEvent_CheckBox.isSelected(), false, pointList);
				
			}
		});
		actualPanel.add(exportExcel_Button);
		
		JSeparator separator4 = new JSeparator();
		separator4.setOrientation(SwingConstants.VERTICAL);
		separator4.setForeground(Color.BLACK);
		separator4.setBackground(Color.BLACK);
		separator4.setBounds(1554, 0, 1, 111);
		actualPanel.add(separator4);
		
		JLabel setAutoMeasure_label = new JLabel("Set Auto Measure");
		setAutoMeasure_label.setForeground(Color.BLACK);
		setAutoMeasure_label.setFont(FontManager.getFont(Font.BOLD, 18));
		setAutoMeasure_label.setBackground(Color.WHITE);
		setAutoMeasure_label.setBounds(1564, 6, 183, 34);
		actualPanel.add(setAutoMeasure_label);
		
		JButton autoMeasure_Button = new JButton("���� �ڵ� ����");
		autoMeasure_Button.setForeground(Color.BLACK);
		autoMeasure_Button.setFont(FontManager.getFont(Font.BOLD, 16));
		autoMeasure_Button.setFocusPainted(false);
		autoMeasure_Button.setBackground(Color.WHITE);
		autoMeasure_Button.setBounds(1567, 60, 150, 42);
		autoMeasure_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<ModbusWatchPoint> selectedPointList = ModbusMonitor_Panel.getSelectedModbusPoint(pointTable);
				
				StringBuilder sb = new StringBuilder();
				
				if (selectedPointList == null || selectedPointList.size() < 1) {					
					sb.append(String.format("%s", Util.colorBlue("���õ� ����Ʈ ����")));
					sb.append(Util.separator + Util.separator + "\n");					
					
					sb.append("���̺��� ���� ������ �����Ͻ� ������ ����Ʈ�� ���� �� �ٽ� �õ����ּ���");
					sb.append(Util.separator + Util.separator + "\n");
					
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
								
				sb.append(String.format("%s%s\n", Util.colorBlue("����Ʈ ���� ���� �ڵ� ����"), Util.separator));
								
				sb.append(String.format("�����Ͻ� ������ ����Ʈ %s�� �׸��� ���� ������ �ڵ� �����Ͻðڽ��ϱ�?", Util.colorBlue(String.valueOf(selectedPointList.size()))));
				sb.append(Util.separator + Util.separator + "\n\n");
				
				sb.append("�ش� ��� ���� ���õ� ����Ʈ �׸���� �̸��� �˻� �� �ش� ����Ʈ�� ������ ������ �ڵ����� �����˴ϴ�");
				sb.append(Util.separator + Util.separator + "\n\n");
				
				sb.append("�ش� ����� �ܼ� ���� ����̹Ƿ� ��� �� �ݵ�� �ڵ����� ������ ����Ʈ�� ���� ������ Ȯ�����ּ���  !");
				sb.append(Util.separator + Util.separator + "\n\n");
				
				sb.append(Util.colorGreen("( �ش� ����� ����Ʈ�� ������ ������ �Ƴ��α� ������(DataFormat:3) �̸鼭 ���� ������ �������� ���� �׸񿡸� ����˴ϴ� )"));
				sb.append(Util.separator + Util.separator + "\n");
				
				int menu = Util.showConfirm(sb.toString());
				
				if(menu == JOptionPane.OK_OPTION) {
					for(ModbusWatchPoint point : selectedPointList) {
						if(point.getDataFormat() == PerfConf.DATA_FORMAT_MEASURE && (point.getMeasure() == null || point.getMeasure().trim().equals(""))) {
							point.measure = Perf.createMeasure(point.getDisplayName().trim());
						}
					}
					
					ModbusMonitor_Panel.doTableFilter(false);
					ModifyModbusWatchPointFrame.doTableFilter();
					ExportModbusPointFrame.updateTable();
					
					sb = new StringBuilder();
					sb.append(String.format("%s", Util.colorBlue("����Ʈ ���� ���� �ڵ� ���� �Ϸ�")));
					sb.append(Util.separator + Util.separator + "\n");
					
					sb.append("�����Ͻ� ������ ����Ʈ " + Util.colorBlue(String.valueOf(selectedPointList.size())) + "�� �׸��� ���� ���� �ڵ� ������ �Ϸ�Ǿ����ϴ�");
					sb.append(Util.separator + Util.separator + "\n");
					
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
			}
		});
		actualPanel.add(autoMeasure_Button);
		
		separator5 = new JSeparator();
		separator5.setOrientation(SwingConstants.VERTICAL);
		separator5.setForeground(Color.BLACK);
		separator5.setBackground(Color.BLACK);
		separator5.setBounds(1732, 0, 1, 111);
		actualPanel.add(separator5);
		
		tableDataInit();
		
		// �������� ȭ�� ������� �����ȴ�
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ExportModbusPointFrame.isExist = false;
		super.dispose();
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Export Modbus Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Export Modbus Point �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public static void resetTable(JTable table, Object[][] content){
		String[] header = new String[] {
				"�� ��",
				"������ ����Ʈ",
				"����ڵ�",
				"�� ��",
				"������ Ÿ��",
				"�� ��",
				"������",
				"���� ���� : 0",
				"���� ���� : 1",
				"���� ����"
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
						false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(pointTable);
	}
	
	public static void setTableStyle(JTable table) {
		// �̵� �Ұ�, �� ũ�� ���� �Ұ�
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setForeground(Color.BLACK);		
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// ���̺� ��� width ����
		table.getColumnModel().getColumn(0).setPreferredWidth(70); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(400); // ������ ����Ʈ
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // ����ڵ�
		table.getColumnModel().getColumn(3).setPreferredWidth(130); // �� ��
		table.getColumnModel().getColumn(4).setPreferredWidth(300); // ������ Ÿ��
		table.getColumnModel().getColumn(5).setPreferredWidth(120); // �� ��
		table.getColumnModel().getColumn(6).setPreferredWidth(145); // ������
		table.getColumnModel().getColumn(7).setPreferredWidth(150); // ���� ���� : 0
		table.getColumnModel().getColumn(8).setPreferredWidth(150); // ���� ���� : 1
		table.getColumnModel().getColumn(9).setPreferredWidth(800); // ���� ����
				
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �� ��
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // ������ ����Ʈ
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // ����ڵ�
		tcmSchedule.getColumn(3).setCellRenderer(tScheduleCellRenderer); // �� ��
		tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer); // ������ Ÿ��
		tcmSchedule.getColumn(5).setCellRenderer(tScheduleCellRenderer); // �� ��
		tcmSchedule.getColumn(6).setCellRenderer(tScheduleCellRenderer); // ������
		tcmSchedule.getColumn(7).setCellRenderer(tScheduleCellRenderer); // ���� ���� : 0
		tcmSchedule.getColumn(8).setCellRenderer(tScheduleCellRenderer); // ���� ���� : 1
//		tcmSchedule.getColumn(9).setCellRenderer(tScheduleCellRenderer); // ���� ����
	}
	
	public static void tableDataInit() {
		addRecord(pointTable, ModbusMonitor_Panel.pointList);
	}
	
	public void changeWork() {
		mk_V4_RaidoButton.setEnabled(download_radioButton.isSelected());
		mk_V4_RaidoButton.setVisible(download_radioButton.isSelected());
		
		mk_V10_RaidoButton.setEnabled(download_radioButton.isSelected());
		mk_V10_RaidoButton.setVisible(download_radioButton.isSelected());
		
		exportXML_Button.setEnabled(mk_V4_RaidoButton.isSelected());
		exportXML_Button.setVisible(download_radioButton.isSelected());
		
		exportExcel_Button.setEnabled(download_radioButton.isSelected());
		exportExcel_Button.setVisible(download_radioButton.isSelected());
		
		mk119_Button.setEnabled(directAdd_radioButton.isSelected());
		mk119_Button.setVisible(directAdd_radioButton.isSelected());
				
		adminConsoleInfo.setEnabled(directAdd_radioButton.isSelected());
		adminConsoleInfo.setVisible(directAdd_radioButton.isSelected());
		
		serverName_Label.setEnabled(directAdd_radioButton.isSelected());
		serverName_Label.setVisible(directAdd_radioButton.isSelected());
		
		serverName_TextField.setEnabled(directAdd_radioButton.isSelected());
		serverName_TextField.setVisible(directAdd_radioButton.isSelected());
		
		connect_Button.setEnabled(directAdd_radioButton.isSelected());
		connect_Button.setVisible(directAdd_radioButton.isSelected());
		
		serverInfo_Label.setEnabled(directAdd_radioButton.isSelected());
		serverInfo_Label.setVisible(directAdd_radioButton.isSelected());		
		
		addPoint_Button.setEnabled(directAdd_radioButton.isSelected());
		addPoint_Button.setVisible(directAdd_radioButton.isSelected());
	}
	
	/**
	 * 	���ڵ� �߰�
	 */
	public static void addRecord(JTable table, ArrayList<ModbusWatchPoint> pointList) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			// ����ڵ�, �ּ�, ������ ������ ����
			Collections.sort(pointList);
			
			for(int i = 0; i < pointList.size(); i++) {
				
				ModbusWatchPoint point = pointList.get(i);
				record = new Vector();
				
				int index = 0;
				if(table.getRowCount() <= 0) {
					// ���̺��� �� ������ 0�� �� ��� : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// ���̺��� �� ������ �ּ� 1�� �̻� �� ��� ������ ���ڵ��� ( ���� �÷� �� + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
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
				
				PerfLabelStatusBean[] labels =  point.getStatusLabels();
				String multiLabel = "";
				if(labels != null) {
					for(PerfLabelStatusBean label : labels) {
						multiLabel += label.value + "; " + label.label + "; ";
					}
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // �� �� 
				/* column[1] */ record.add(point); // ������ ����Ʈ
				/* column[2] */ record.add(point.getFunctionCode()); // ����ڵ�
				/* column[3] */ record.add(addr);  // �ּ�
				/* column[4] */ record.add(point.getDataType());  // ������ Ÿ��
				/* column[5] */ record.add(point.getMeasure());  // ����
				/* column[6] */ record.add(point.getScaleFunction());  // ������
				/* column[7] */ record.add(point.getBinLabel()[0]);  // ���� ���� : 0
				/* column[8] */ record.add(point.getBinLabel()[1]);  // ���� ���� : 1
				/* column[9] */ record.add(multiLabel);  // ���� ����		
				model.addRow(record);
			}
			
		}catch(Exception e) {
			// ���ڵ� �߰� �� ���� �߻� �� �ƹ��͵� �������� ����
			e.printStackTrace();
		}
	}
	
	public static void updateTable() {
		if(ExportModbusPointFrame.isExist) {
			resetTable(pointTable, null);
			addRecord(pointTable, ModbusMonitor_Panel.pointList);
		}
	}
	
	public static void connectFacility(String facilityName) {
		
		if(adminConsole == null && facilityMap == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorRed("It is not linked with MK119 AdminConsole")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append("MK119 Admin Console ���񽺿� �������� �ʾҽ��ϴ�");
			sb.append(Util.separator + Util.separator + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ModbusFacility facility = facilityMap.get(facilityName);
		
		if(facility == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorRed("Not Found Modbus Connected Facility")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("����"), facilityName));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append("�Է��Ͻ� ������ ���� �ü����� �������� �ʽ��ϴ�");
			sb.append(Util.separator + Util.separator + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		facility = RestAgent.getFacilityDetail(adminConsole, facility.getnServerIndex());
		
		if(facility == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorRed("Not Found Modbus Connected Facility")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("����"), facilityName));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append("�Է��Ͻ� ������ ���� �ü����� �������� �ʽ��ϴ�");
			sb.append(Util.separator + Util.separator + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int connMethod = facility.getCONN_METHOD();
		int commPorotocol = facility.getCOMM_PROTOCOL();
		
		if(connMethod == ModbusAgent.CONN_METHOD_MODBUS && (commPorotocol == ModbusAgent.MODBUS_TYPE_RTU || commPorotocol == ModbusAgent.MODBUS_TYPE_TCP)) {
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorGreen("Modbus Connected Facility")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("����"), facility.getStrServerName()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("�ü��� ����"), facility.getFACILITY_TYPE_String()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("���� ���"), DbUtil.getConnMethod(connMethod)));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("������ Ÿ��"), (commPorotocol == ModbusAgent.MODBUS_TYPE_RTU) ? "Modbus-RTU" : "Modbus-TCP"));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append("���� ������ ���� ���� �����Ͻðڽ��ϱ�?");
			sb.append(Util.separator + Util.separator + "\n");
			
			int menu = Util.showConfirm(sb.toString());
			
			if(menu == JOptionPane.OK_OPTION) {
				/***********************************************************************************************/
				ExportModbusPointFrame.facility = facility;				
				serverInfo_Label.setText(String.format("<html><font color='black'>���� ��� :</font> %s</html>", facility.getStrServerName()));
				
				sb = new StringBuilder();
				sb.append(String.format("%s", Util.colorGreen("Modbus Connected Facility")));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append(String.format("%s : %s", Util.colorBlue("����"), facility.getStrServerName()));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append(String.format("%s : %s", Util.colorBlue("�ü��� ����"), facility.getFACILITY_TYPE_String()));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append(String.format("%s : %s", Util.colorBlue("���� ���"), DbUtil.getConnMethod(connMethod)));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append(String.format("%s : %s", Util.colorBlue("������ Ÿ��"), (commPorotocol == ModbusAgent.MODBUS_TYPE_RTU) ? "Modbus-RTU" : "Modbus-TCP"));
				sb.append(Util.separator + Util.separator + "\n\n");
				
				sb.append("���� ������ ���� ���� ���������� ���� �Ǿ����ϴ�");
				sb.append(Util.separator + Util.separator + "\n");
				
				Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				return;
				/***********************************************************************************************/
			}else {
				// ����ڰ� ������ �����
				return;
			}
			
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorRed("Not a Modbus Connected Facility")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("����"), facility.getStrServerName()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("�ü��� ����"), facility.getFACILITY_TYPE_String()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("���� ���"), DbUtil.getConnMethod(connMethod)));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append("���� ���� �𽺹��� ���� ������� ��ϵ��� �ʾұ� ������ ������ �Ұ����մϴ�");
			sb.append(Util.separator + Util.separator + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void linkSuccess(AdminConsole_Info adminConsole) {
		String mkVersionInfo = RestAgent.getMK119Version(adminConsole);
		adminConsoleInfo.setText("<html><font color='black'>Admin Console : </font>" + mkVersionInfo + "</html>");
		
		ExportModbusPointFrame.facility = null;
		serverInfo_Label.setText(String.format("<html><font color='black'>���� ��� :</font> %s</html>", "���� ��"));
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s", Util.colorGreen("MK119 Admin Console Login successful")));
		sb.append(Util.separator + Util.separator + "\n");
		
		sb.append(String.format("%s : %s", Util.colorBlue("Admin Console"), mkVersionInfo));
		sb.append(Util.separator + Util.separator + "\n\n");
		
		sb.append(String.format("%s �α��ο� �����Ͽ����ϴ�", Util.colorBlue(adminConsole.get_IP() + ":" + adminConsole.get_PORT() + " Admin Console")));
		sb.append(Util.separator + Util.separator + "\n");
		
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		
		serverName_TextField.requestFocus();
	}
	
	public static void loadFacilityInfo(AdminConsole_Info adminConsole) {
		HashMap<String, ModbusFacility> map = RestAgent.getFacilityAll(adminConsole);
		
		if(map != null) {
			facilityMap = map;
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s", Util.colorRed("MK119 Facility Info load Fail")));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append("MK119 ��� ���� �ε忡 �����Ͽ����ϴ�");
			sb.append(Util.separator + Util.separator + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void addModbusPoint(ModbusFacility facility){
		try {
			
			if(facility == null) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s", Util.colorRed("It is not linked with Modbus Facility")));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append("���� ������ ������ ���� ��� �����ϴ�");
				sb.append(Util.separator + Util.separator + "\n");
				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(pointTable.getSelectedRowCount() == 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s", Util.colorRed("No points selected")));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append("���̺��� �߰��Ͻ� ������ ����Ʈ�� ���� �� �ٽ� �õ����ּ���");
				sb.append(Util.separator + Util.separator + "\n");
				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(ExportModbusPointFrame.working) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s", Util.colorRed("Performing the last requested operation")));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append("���� �����尡 ������ ��û�� ó�����Դϴ�");
				sb.append(Util.separator + Util.separator + "\n");
				
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ArrayList<ModbusWatchPoint> selectedPointList = ModbusMonitor_Panel.getSelectedModbusPoint(pointTable);
			if(selectedPointList == null || selectedPointList.size() < 1) return;
			
			// **********************************************************************************************************************************************************
			Thread thread = new Thread(new Runnable() {
				public void run() {
					boolean addPerf = false;
					boolean addControl = false;
					
					boolean allFcCode = false;
					boolean generateDesc = false;
			    	boolean concatMeasure = false;
			    	
					Font font = FontManager.getFont(Font.BOLD, 15);
					
					JLabel title = new JLabel();
					title.setText("<html><font color='green'>Add Modbus Point to MK119</font>" + Util.longSeparator +"<br><br></html>");
					title.setFont(font);
					
					JLabel serverName = new JLabel();
					serverName.setText("<html><font color='black'>����</font> : " + Util.colorBlue(facility.getStrServerName()) + Util.longSeparator +"<br></html>");
					serverName.setFont(font);
					
					JLabel facType = new JLabel();
					facType.setText("<html><font color='black'>�ü��� ����</font> : " + Util.colorBlue(facility.getFACILITY_TYPE_String()) + Util.longSeparator +"<br><br></html>");
					facType.setFont(font);
					
					JLabel message = new JLabel();
					message.setText("<html><font color='black'>�����Ͻ� ������ ����Ʈ " + Util.colorBlue(String.valueOf(selectedPointList.size())) + "�� �׸� ���Ͽ� � �۾��� �����Ͻðڽ��ϱ�?</font>" + Util.longSeparator +"<br><br></html>");
					message.setFont(font);
					
					String useEvent = "";
					if(useAutoEvent_CheckBox.isSelected()) {
						useEvent = Util.colorGreen("&nbsp;&nbsp;( ���� �̺�Ʈ �ڵ� ��� �ɼ� ��� )");
					}else {
//						useEvent = Util.colorGreen("&nbsp;&nbsp;( ���� �̺�Ʈ �ڵ� ��� �ɼ� ������ )");
					}
					
					JCheckBox addModbusPerf = new JCheckBox();
					addModbusPerf.setText("<html>&nbsp;���� �߰�" + useEvent + Util.longSeparator +"<br></html>");
					addModbusPerf.setSelected(true);
					addModbusPerf.setFocusPainted(false);
					addModbusPerf.setForeground(Color.BLACK);
					addModbusPerf.setFont(font);
					
					JCheckBox addModbusControl = new JCheckBox();
					addModbusControl.setText("<html>&nbsp;���� �߰�" + Util.longSeparator +"<br></html>");
					addModbusControl.setSelected(false);
					addModbusControl.setFocusPainted(false);
					addModbusControl.setForeground(Color.BLACK);
					addModbusControl.setFont(font);
					
					JLabel addControlOption = new JLabel();
					addControlOption.setText("<html><font color='blue'>���� �߰� �ɼ�</font><br></html>");
					addControlOption.setEnabled(false);
					addControlOption.setFont(font);
					
					JRadioButton selectFc = new JRadioButton();
					selectFc.setText(" ����ڵ� 1 �Ǵ� 3 �׸� ���Ͽ� ���� �߰�");
					selectFc.setForeground(Color.BLACK);
					selectFc.setFont(font);
					selectFc.setFocusPainted(false);
					selectFc.setEnabled(false);
					selectFc.setSelected(true);
					
					JRadioButton allFc = new JRadioButton();
					allFc.setText(" ��� ����ڵ� �׸� ���Ͽ� ���� �߰�");
					allFc.setForeground(Color.BLACK);
					allFc.setFont(font);
					allFc.setFocusPainted(false);
					allFc.setEnabled(false);
					
					ButtonGroup group = new ButtonGroup();
					group.add(selectFc);
					group.add(allFc);
					
					JCheckBox concatMeasure_check = new JCheckBox();
					concatMeasure_check.setText(" ���� ���� ���뿡 ������ ���� ���� ǥ��");					
					concatMeasure_check.setForeground(Color.BLACK);
					concatMeasure_check.setFont(font);
					concatMeasure_check.setFocusPainted(false);
					concatMeasure_check.setEnabled(false);
					
					
					JCheckBox generateDesc_check = new JCheckBox();
					generateDesc_check.setText(" ���� ���� ���� �ڵ� ���� ���");
					generateDesc_check.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(generateDesc_check.isSelected()) {					
								concatMeasure_check.setEnabled(true);
								
							}else {								
								concatMeasure_check.setEnabled(false);
							}
						}
					});
					generateDesc_check.setSelected(true);
					generateDesc_check.setForeground(Color.BLACK);
					generateDesc_check.setFont(font);
					generateDesc_check.setFocusPainted(false);
					generateDesc_check.setEnabled(false);
					
					
					addModbusControl.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(addModbusControl.isSelected()) {
								addControlOption.setEnabled(true);
								selectFc.setEnabled(true);
								allFc.setEnabled(true);
								generateDesc_check.setEnabled(true);								
								concatMeasure_check.setEnabled(generateDesc_check.isSelected());
							}else {
								addControlOption.setEnabled(false);
								selectFc.setEnabled(false);
								allFc.setEnabled(false);
								generateDesc_check.setEnabled(false);								
								concatMeasure_check.setEnabled(false);
								
							}
						}
					});
					
					Object[] showMessage = {
							   title, // ����
							   
							   serverName, // ���� 
							   facType, // ��� ����
							   
							   message, // � �۾��� �����Ͻðڽ��ϱ�?
							   
							   addModbusPerf, // ���� �߰�
							   addModbusControl, // ���� �߰�

							   new JLabel("<html><br></html>"), // �� �ٲ�
							   
							   addControlOption, // ���� �߰� �ɼ�
							   selectFc, // ����ڵ� 1 �Ǵ� 3 �׸� ���Ͽ� ���� �߰�
							   allFc, //  ��� ����ڵ� �׸� ���Ͽ� ���� �߰�
							   generateDesc_check, // ���� ���� ���� : üũ�ڽ�
							   concatMeasure_check, // ���� ���� ���� ǥ�� : üũ�ڽ�
							   
							   new JLabel("<html><br></html>"), // �� �ٲ�
						};
						
					int	option = JOptionPane.showConfirmDialog(null, showMessage, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					
					if(option == JOptionPane.OK_OPTION) {
						addPerf = addModbusPerf.isSelected();
						addControl = addModbusControl.isSelected();
						allFcCode = addControl && allFc.isSelected();
						generateDesc = addControl && generateDesc_check.isSelected();
						concatMeasure = addControl && generateDesc && concatMeasure_check.isSelected();
						
					} else {				
						return;
						
					}
					
					if( !addPerf && !addControl ) {
						return;
					}
					
					try {
						ExportModbusPointFrame.working = true;
						
						if(addPerf) {
							Perf[] perfs = new Perf[selectedPointList.size()];
							
							for(int i = 0; i < selectedPointList.size(); i++) {
								ModbusWatchPoint point = selectedPointList.get(i);
								perfs[i] = point.parsePerf_ko();
								
								if (addrTypeComboBox.getSelectedIndex() == 2) {
									perfs[i].setPerfCounter(point.getHexCounter() + "\\\\{1}");
								}else {
									perfs[i].setPerfCounter(point.getDecCounter() + "\\\\{1}");
								}
							}
							
							// �ڵ� �̺�Ʈ ��� �ɼ�
							if(useAutoEvent_CheckBox.isSelected()) Perf.initPerfEvent(perfs);
							
							// JSON ������ �ʱ�ȭ
							Perf.parseJSON(useAutoEvent_CheckBox.isSelected(), perfs);
							
							new HttpAgent().addModbusPerfs(adminConsole, facility, perfs, false);
						}
						
						
						if(addControl) {
							new HttpAgent().addModbusControls(adminConsole, facility, selectedPointList, allFcCode, generateDesc, concatMeasure);
						}
						
					}finally {
						ExportModbusPointFrame.working = false;
						
					}
					
				}
			});

			thread.start();
			// **********************************************************************************************************************************************************
			
		}catch(Exception ex) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
			sb.append(String.format("MK119 ���� �߰� �۾��� ���ܰ� �߻��Ͽ����ϴ�%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", ex.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	// ���̺� ��� ����
	public static void doTableFilter() {
		if(search_TextField == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		if(noSearch) {
			resetTable(pointTable, null);
			addRecord(pointTable, pointList);
			return;
		}
		if(!noSearch) {
			text = text.toLowerCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toLowerCase();
				
				if(text.contains(",")) {
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						if(searchElement.contains(token)) {
							isContain = true;
						}
					}
				}else{
					if (searchElement.contains(text)) {
						isContain = true;
					}
				}		
			}else {
				isContain = true;
			}
			
			if(isContain) {
				filterList.add(modbusWp);
			}
			
		}// for loop
		
		resetTable(pointTable, null);
		addRecord(pointTable, filterList);
	}
}