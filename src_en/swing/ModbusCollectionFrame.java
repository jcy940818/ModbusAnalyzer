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
import java.util.List;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.Calculator;
import common.web.AdminConsole_Info;
import src_en.agent.HttpAgent;
import src_en.agent.ModbusFacility;
import src_en.agent.Perf;
import src_en.analyzer.RX.DataType;
import src_en.util.ExcelAdapter;
import src_en.util.Inspecter;
import src_en.util.MessageUtil_en;
import src_en.util.Util;

public class ModbusCollectionFrame extends JFrame {

	private ModbusFacility modbusFacility;
	private AdminConsole_Info adminConsole = null;
	
	private static final int ORDER = 0;
	private static final int PERF_NAME = 1;
	private static final int FUNCTION_CODE = 2;
	private static final int REGISTER_ADDRESS = 3;
	private static final int MODBUS_ADDRESS = 4;
	private static final int DATA_TYPE = 5;
	private static final int INTERVAL = 6;
	private static final int MEASURE = 7;
	private static final int SCALE_FUNCTION = 8;
	private static final int LAST_VALUE = 9;
	private static final int LABLE_0 = 10;
	private static final int LABLE_1 = 11;
	private static final int LABLE_STATUS = 12;

	private static final String[] COLUMN_NAME = {
			"Index", // 0 : ???? 
			"Performance Name", // 1 : ??????
			"Function Code", // 2 : ????????
			"Register Address", // 3 : ???????? ????
			"Modbus Address", // 4 : ???????? ????
			"Data Type", // 5 : ?????? ????
			"Interval", // 6 : ???? ????
			"Units", // 7 : ????
			"Scale", // 8 : ??????
			"Last Value", // 9 : ?????? ???? ??
			"Label : 0", // 10 : ???? 0
			"Label : 1", // 11 : ???? 1
			"Multiple States" // 12 : ???? ????
	};
	
	// MK119 ???? ???? ???? ????
	private JRadioButton registerAddress_radiobutton;
	private JRadioButton modbusAddress_radiobutton;
	public static JCheckBox useAutoEvent;
	public static JCheckBox useAutoMeasure;
	
	// ?????? ???? ????
	public static boolean isMK119Adding = false;
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
					ModbusCollectionFrame frame = new ModbusCollectionFrame(new AdminConsole_Info("localhost","8080","Moon","940818","Moon940818"), 
							new ModbusFacility(1,"Modbus Facility Name",1 ,"Moon",1, 1));
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
	public ModbusCollectionFrame(AdminConsole_Info AdminConsole, ModbusFacility modbusFacility) {
		ModbusCollectionFrame.isExist = true;
		modbusPerfList = new ArrayList();
		
		this.adminConsole = AdminConsole;
		this.modbusFacility = modbusFacility;
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle(String.format("ModbusAnalyzer : %s:%s %s [ Login : %s ]", adminConsole.get_IP(), adminConsole.get_PORT() , adminConsole.getVersionInfo() ,adminConsole.get_ID()));
		
		// ?????? ?????? ?????? ?????? ????
		// String ImageFile = "Moon.png";
		// ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		// ???????? Build Path?? ?????? ?????? ?????????? ?????????? ????.		
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
		
		JLabel modbusCollection = new JLabel("Modbus Collection");
		modbusCollection.setForeground(Color.BLACK);
		modbusCollection.setIcon(new Util().getSubLogoResource());
		modbusCollection.setHorizontalAlignment(SwingConstants.LEFT);
		modbusCollection.setFont(new Font("???? ????", Font.BOLD, 22));
		modbusCollection.setBackground(Color.WHITE);
		modbusCollection.setBounds(0, 0, 267, 55);
		actualPanel.add(modbusCollection);
		
		JLabel facilityName = new JLabel(this.modbusFacility.getStrServerName());
		facilityName.setForeground(Color.BLUE);
		facilityName.setFont(new Font("???? ????", Font.BOLD, 22));
		facilityName.setBounds(273, 0, 573, 55);
		actualPanel.add(facilityName);
		
		addModbusPerf_Button = new JButton(new Util().getMK2Resource());
		addModbusPerf_Button.setForeground(Color.BLACK);
		addModbusPerf_Button.setText(" Add Perf");
		addModbusPerf_Button.setFont(new Font("???? ????", Font.BOLD, 17));
		addModbusPerf_Button.setFocusPainted(false);
		addModbusPerf_Button.setContentAreaFilled(false);
		addModbusPerf_Button.setBorder(UIManager.getBorder("Button.border"));
		addModbusPerf_Button.setBackground(Color.WHITE);
		addModbusPerf_Button.setBounds(850, 11, 189, 36);
		addModbusPerf_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				if(isInspecting) {
					sortingAndValidation();
					return;
				}
				
				if(isMK119Adding) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>MK119 Adding Thread is Working</font>\n");
					sb.append(String.format("MK119 performance additional work is already underway%s\n", Util.separator));
					sb.append(String.format("\nThe work cannot be performed in duplicate%s\n", Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				addMK119Performance();
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?? ???? ?????? ???? ????
		table.setBackground(Color.WHITE);		
		table.addKeyListener(new KeyAdapter() {				
			// ?? ????  ????
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
					int[] selectedRows = table.getSelectedRows();
					int[] selectedColumns = table.getSelectedColumns();
					
					for(int row = 0; row < selectedRows.length; row++) {
						for(int column = 0; column < selectedColumns.length; column++) {			
					
							// ???????? ???? ?? ?? ???? ?? ?????? ????
							if(table.isCellEditable(selectedRows[row], selectedColumns[column])) {
								table.setValueAt("", selectedRows[row], selectedColumns[column]);	
							}
						}
					}					
				}							
			}
		});
		ExcelAdapter ex = new ExcelAdapter(table); // ???? ?? ???? ???????? ???? 
		scrollPane.setViewportView(table);
		resetTable(table);
		
		JPanel formPanel = new JPanel();
		formPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		formPanel.setBackground(Color.WHITE);
		formPanel.setBounds(12, 547, 1030, 109);		
		actualPanel.add(formPanel);
		formPanel.setLayout(null);
		
		JLabel addressInfo = new JLabel("Address Type");
		addressInfo.setForeground(Color.BLACK);
		addressInfo.setFont(new Font("???? ????", Font.BOLD, 16));
		addressInfo.setBounds(14, 10, 130, 30);
		formPanel.add(addressInfo);
		
		registerAddress_radiobutton = new JRadioButton("Register Addr");
		registerAddress_radiobutton.setBackground(Color.WHITE);
		registerAddress_radiobutton.setFont(new Font("???? ????", Font.BOLD, 14));
		registerAddress_radiobutton.setForeground(Color.BLACK);
		registerAddress_radiobutton.setBounds(20, 48, 120, 23);
		formPanel.add(registerAddress_radiobutton);
		
		modbusAddress_radiobutton = new JRadioButton("Modbus Addr");
		modbusAddress_radiobutton.setBackground(Color.WHITE);
		modbusAddress_radiobutton.setFont(new Font("???? ????", Font.BOLD, 14));
		modbusAddress_radiobutton.setForeground(Color.BLACK);
		modbusAddress_radiobutton.setBounds(20, 77, 120, 23);
		modbusAddress_radiobutton.setSelected(true);
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
		EventInfo.setForeground(Color.BLACK);
		EventInfo.setFont(new Font("???? ????", Font.BOLD, 16));
		EventInfo.setBounds(178, 10, 68, 30);
		formPanel.add(EventInfo);
		
		useAutoEvent = new JCheckBox("Use Auto Event");
		useAutoEvent.setForeground(Color.BLACK);
		useAutoEvent.setBackground(Color.WHITE);
		useAutoEvent.setFont(new Font("???? ????", Font.BOLD, 15));
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
		eventInfoButton.setFont(new Font("???? ????", Font.BOLD, 15));
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
		tableSet.setForeground(Color.BLACK);
		tableSet.setFont(new Font("???? ????", Font.BOLD, 16));
		tableSet.setBounds(415, 10, 150, 30);
		formPanel.add(tableSet);
		
		
		JButton addButton = new JButton("Add Row");
		addButton.setBounds(425, 52, 120, 44);
		addButton.setBackground(Color.WHITE);
		addButton.setFont(new Font("???? ????", Font.BOLD, 15));
		addButton.setForeground(Color.BLACK);
		addButton.setBorder(UIManager.getBorder("Button.border"));		
		addButton.setFocusPainted(false);
		addButton.setContentAreaFilled(false);
		addButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					sortingAndValidation();
					return;
				}
				
				if(isMK119Adding) {
					addingMK119Perf();
					return;
				}
				
				try {
					int[] selectedIndex = ModbusAgent_Panel.table.getSelectedRows();				
					Perf[] perfs = Perf.getModbusPerfs(ModbusAgent_Panel.table, selectedIndex);			 
					if(perfs == null) return;			
					addRecord(perfs);
				}catch(Exception exception) {
					// ?????? ???? ???? ???? ?? ?????? ???????? ???????? ???????? ????
					exception.printStackTrace();
				}				
			}
		});
		formPanel.add(addButton);
		
		
		JButton deleteButton = new JButton("Delete Row");
		deleteButton.setBounds(554, 52, 120, 44);
		deleteButton.setFont(new Font("???? ????", Font.BOLD, 15));
		deleteButton.setFocusPainted(false);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setBorder(UIManager.getBorder("Button.border"));
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setForeground(Color.BLACK);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					sortingAndValidation();
					return;
				}
				
				if(isMK119Adding) {
					addingMK119Perf();
					return;
				}
				
				int[] selected = table.getSelectedRows();					
				removeRecord(selected);
			}
		});
		formPanel.add(deleteButton);
		
		
		JButton sortButton = new JButton("Sorting and Validation");
		sortButton.setForeground(Color.BLACK);
		sortButton.setFont(new Font("???? ????", Font.BOLD, 15));
		sortButton.setFocusPainted(false);
		sortButton.setContentAreaFilled(false);
		sortButton.setBorder(UIManager.getBorder("Button.border"));
		sortButton.setBackground(Color.WHITE);
		sortButton.setBounds(682, 52, 207, 44);
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isInspecting) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Inspect Thread is Already Working</font>" + Util.separator +"\n");
					sb.append(String.format("It's already being carried out%s\n", Util.separator));					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isMK119Adding) {
					addingMK119Perf();
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
		resetButton.setFont(new Font("???? ????", Font.BOLD, 15));
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		formPanel.add(resetButton);
		
		useAutoMeasure = new JCheckBox("Automatic generation of performance units");
		useAutoMeasure.setForeground(Color.BLACK);
		useAutoMeasure.setFont(new Font("???? ????", Font.BOLD, 15));
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
					sortingAndValidation();
					return;
				}
				
				if(isMK119Adding) {
					addingMK119Perf();
					return;
				}
				
				resetTable(table);				
				ModbusCollectionFrame.modbusPerfList.clear();
			}
		});
		
		
		// ???????? ???? ?????????? ????????		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		ModbusCollectionFrame.isExist = false;
		super.dispose();
	}	
	
	public static void resetTable(JTable table){
		// ?????? ???? ????
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("???? ????", Font.BOLD, 15));
		
		// ?????? ?? ????
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("???? ????", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			
			COLUMN_NAME
			
//			new String[] {
//				"\uC21C \uC11C", 
//				"\uC131\uB2A5\uBA85",
//				"\uAE30\uB2A5\uCF54\uB4DC", 
//				"Register \uC8FC\uC18C", 
//				"Modbus \uC8FC\uC18C", 
//				"\uB370\uC774\uD130 \uD0C0\uC785", 
//				"\uC218\uC9D1\uC8FC\uAE30", 
//				"\uB2E8 \uC704", "\uBCF4\uC815\uC2DD", 
//				"\uB9C8\uC9C0\uB9C9 \uC218\uC9D1 \uAC12", 
//				"\uC774\uC9C4 \uC0C1\uD0DC : 0", 
//				"\uC774\uC9C4 \uC0C1\uD0DC : 1", 
//				"\uB2E4\uC911 \uC0C1\uD0DC"
//			}
		) {
			boolean[] columnEditables = new boolean[] {
					false, // ????
					true, // ??????
					false, // ????????
					false, // Register ????
					false, // Modbus ????
					false, // ???? ?????? (perfCounter)
					true, // ????????
					true, // ????
					true, // ??????
					false, // ?????? ???? ??
					true, // ???? ???? : 0
					true, // ???? ???? : 1
					true // ???? ????
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(ORDER).setPreferredWidth(70);
		table.getColumnModel().getColumn(PERF_NAME).setPreferredWidth(250);
		table.getColumnModel().getColumn(FUNCTION_CODE).setPreferredWidth(130);
		table.getColumnModel().getColumn(REGISTER_ADDRESS).setPreferredWidth(150);
		table.getColumnModel().getColumn(MODBUS_ADDRESS).setPreferredWidth(150);
		table.getColumnModel().getColumn(DATA_TYPE).setPreferredWidth(280);
		table.getColumnModel().getColumn(INTERVAL).setPreferredWidth(80);
		table.getColumnModel().getColumn(MEASURE).setPreferredWidth(80);
		table.getColumnModel().getColumn(SCALE_FUNCTION).setPreferredWidth(130);
		table.getColumnModel().getColumn(LAST_VALUE).setPreferredWidth(150);
		table.getColumnModel().getColumn(LABLE_0).setPreferredWidth(120);
		table.getColumnModel().getColumn(LABLE_1).setPreferredWidth(120);
		table.getColumnModel().getColumn(LABLE_STATUS).setPreferredWidth(300);
		
		// ?? ???? ???? ???? ????
		table.getTableHeader().setReorderingAllowed(false); // ???? ???? ???? ???? ????
//		table.getTableHeader().setResizingAllowed(false); // ???? ?????? ???? ???? ???? ????
		
		setCellContentCenter(table);
	}
	
	/**
	 *  MK119 ???? ????
	 */
	public void addMK119Performance() {
		
		try {
		
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					isMK119Adding = true;
					
					Perf[] perfs = getCollectionPerfList();		
					
					if(!perfCheckOk) {
						isMK119Adding = false;
						return;
					}
					if(perfs == null) {
						isMK119Adding = false;
						return;
					}
					
					StringBuilder sb = new StringBuilder();				
					sb.append("<font color='green'>Add MK119 Watch Point?</font>\n");
					sb.append(String.format("Facility Type : %s%s%s\n", Util.colorBlue(modbusFacility.getFACILITY_TYPE_String()), Util.separator ,Util.separator));
					sb.append(String.format("Device Name : %s%s%s\n\n", Util.colorBlue(modbusFacility.getStrServerName()), Util.separator ,Util.separator));					
					sb.append(String.format("Do you want to add %s Performance items to the device above?%s%s\n\n",Util.colorBlue(String.valueOf(perfs.length)) ,Util.separator, Util.separator));
					
					if(useAutoEvent.isSelected()) {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("Use Auto Event"), Util.separator ,Util.separator));
					}else {
						sb.append(String.format("( %s )%s%s\n", Util.colorBlue("Don't use Use Auto Event"), Util.separator ,Util.separator));
					}
					
					int userOption= Util.showConfirm(sb.toString());
					
					if(userOption != JOptionPane.YES_OPTION) {															
						// ???? ???? ???? ????
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Cancel MK119 Add Watch Point</font>%s\n", Util.separator));
						sb.append(String.format("MK119 performance addition request has been canceled%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						ModbusCollectionFrame.isMK119Adding = false;
						return;
					}
					
					// ?????? ???????? ?????? ???????? ???????? ???? ?? ???? ?? ??????????
					resetTable(table);		
					Arrays.sort(perfs);		
					addRecord(perfs);
					
					// ???? ?????? ???? ????
					if(useAutoEvent.isSelected()) Perf.initPerfEvent(perfs);					
					
					// ???? ???? ???? ?????? ???? strPerfCounter ??????
					// ModbusAddress : 3_1_TWO BYTE INT SIGNED
					// RegisterAddress : 3_0x0000_TWO BYTE INT SIGNED
					if(modbusAddress_radiobutton.isSelected()) {
						Perf.initPerfCounter(true, perfs);
					}else if (registerAddress_radiobutton.isSelected()) {
						Perf.initPerfCounter(false, perfs);
					}
					
					Perf.parseJSON(useAutoEvent.isSelected());
					
					new HttpAgent().addModbusPerfs(adminConsole, modbusFacility, perfs, useAutoEvent.isSelected());
					
					// HttpAgent ???? ???? ?????? ???? ???? ?????? ???? ?? ?? ??????????
					// ModbusCollection.isMK119Adding = false; ?????? HttpAgent ???? ????????
				}
			});
		
			thread.start();
			
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
			sb.append(String.format("An exception occurred while adding MK119 performance%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}finally {
			isMK119Adding = false;
		}
	}

	/**
	 * 	?????? ???? ?????? 
	 */
	public static void addRecord(Perf... perf) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < perf.length; i++) {
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// ???????? ?? ?????? 0?? ?? ???? : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// ???????? ?? ?????? ???? 1?? ???? ?? ???? ?????? ???????? ( ???? ???? ?? + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // ????
				/* column[1] */ record.add(perf[i].getDisplayName()); // ??????
				/* column[2] */ record.add(perf[i].getFunctionCode());  // ????????
				/* column[3] */ record.add(perf[i].getRegisterAddress()); // Register ????
				/* column[4] */ record.add(perf[i].getModbusAddress()); // Modbus ????
				/* column[5] */ record.add(perf[i].getDataType()); // ?????? ????
				/* column[6] */ record.add(perf[i].getInterval()); // ???? ????
				/* column[7] */ record.add(perf[i].getMeasure()); // ????
				/* column[8] */ record.add(perf[i].getScaleFunction()); // ??????
				/* column[9] */ record.add(perf[i].getLastValue()); // ?????? ???? ??

				switch(perf[i].getDataFormat()) {
				case "1" : 
					/* column[10] */ record.add(perf[i].getBinaryMap().get("0")); // ???? ???? : 0
					/* column[11] */ record.add(perf[i].getBinaryMap().get("1")); // ???? ???? : 1
					/* column[12] */ record.add(""); // ???? ????
					break;
				case "2" :
					String multiStatus = Perf.parseMultiStatusSring(perf[i].getMultiStatusMap());
					/* column[10] */ record.add(""); // ???? ???? : 0
					/* column[11] */ record.add(""); // ???? ???? : 1					
					/* column[12] */ record.add(multiStatus); // ???? ????
					break;
				case "3" :
					/* column[10] */ record.add(""); // ???? ???? : 0
					/* column[11] */ record.add(""); // ???? ???? : 1					
					/* column[12] */ record.add(""); // ???? ????
					break;
				}
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// ?????? ???? ?? ???? ???? ?? ???????? ???????? ????
			e.printStackTrace();
		}
	}
	
	/**
	 * ?????? ???? ??????
	 */
	public void removeRecord(int... index) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		if(index.length < 0) {
			// ???? ?? ???? ??????
			if(table.getRowCount()==0) {
				// ???????? ???? ???? ???? ???????? ???????? ????
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
	 * ?????? ???? ?? ????
	 */
	public void sortRecord() {
		try {
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					
					isInspecting = true;
					
					Perf[] perfs = getCollectionPerfList();		
					
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
	
	public Perf[] getCollectionPerfList() {
		perfCheckOk = true;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		
		if(model.getRowCount() <= 0) return null;
		
		Perf[] perfs = new Perf[model.getRowCount()];
		
		Vector rowVector = model.getDataVector();
		Vector data;
		
		for(int i = 0; i < model.getRowCount(); i++) {
			data = (Vector)rowVector.get(i);
			
			perfs[i] = new Perf();
			
			// ???? ?? -----------------------------------------------------------------------------------
			String perfName = String.valueOf(data.get(PERF_NAME)).trim();
			
			// ?????? ?????? ???? ?? ????
			if((perfName == null) || (perfName.equals("") || perfName.length() < 1)) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>??????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n???????? ?????? ???????? ???? ??????????%s\n", Util.separator));				
//				sb.append(String.format("\n?????? <font color='blue'>%s</font> ???? ?????? ?????? ????????????%s\n",String.valueOf(data.get(ORDER)), Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.perfName_1(String.valueOf(data.get(ORDER)));
				setFocusCell(table, i, PERF_NAME);
				perfCheckOk = false;
				return null;
			}
			
			// ?????? ???????? ????
			if(!Inspecter.isVaildName(perfName)) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>??????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n???????? ?????? ???? ?????? ?????? ???? ?????? ???? ?? ?? ????????%s\n", Util.separator));
//				sb.append(String.format("\n?????? ???? ???? ???? ???? : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
//				sb.append(String.format("\n???? ?????? <font color='blue'>%s</font> ???? ?????? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfName ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.perfName_2(String.valueOf(data.get(ORDER)), perfName);
				setFocusCell(table, i, PERF_NAME);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setDisplayName(perfName); 
			
			
			// ???? ???? ----------------------------------------------------------------------------------
			try {
				int functionCode = Integer.parseInt(String.valueOf(data.get(FUNCTION_CODE)).trim());
				if((functionCode <= 0) || (functionCode > 4)) throw new Exception("Function Code Validation Error");
			}catch(Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>????????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n<font color='blue'>Modbus Collection ???? ????????</font>\n"));
//				sb.append(String.format("FC 01 : Read Coil Status\n"));
//				sb.append(String.format("FC 02 : Read Input Status\n"));
//				sb.append(String.format("FC 03 : Read Holding Registers\n"));
//				sb.append(String.format("FC 04 : Read Input Registers\n\n"));
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ???????? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(FUNCTION_CODE)).trim() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.functionCode_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(FUNCTION_CODE)).trim());
				setFocusCell(table, i, FUNCTION_CODE);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setFunctionCode(String.valueOf(data.get(FUNCTION_CODE)).trim());
			
			// ???????? ???? --------------------------------------------------------------------------------
			int registerAddress;
			
			/*
			if(!String.valueOf(data.get(REGISTER_ADDRESS)).toLowerCase().contains("0x")) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Collection Validation Exception</font>\n");
				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Register ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\nRegister ???? ???? ???? : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Register ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, REGISTER_ADDRESS);
				perfCheckOk = false;
				return null;
			}else {
				try {
					registerAddress = Integer.parseInt(String.valueOf(data.get(REGISTER_ADDRESS)).toLowerCase().replace("0x", ""), 16);
					if(registerAddress < 0 || registerAddress > 65535) throw new Exception("Register ???? ???? ????");
				}catch(NumberFormatException e) {
					// ???????? ?????? ?????? ???? ?? ?? ???? ????
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Collection Validation Exception</font>\n");
					sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Register ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
					sb.append(String.format("\nRegister ?????? <font color='blue'>???? ??</font>???? ???? ?? ?? ????????%s\n\n", Util.separator));
					sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Register ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, REGISTER_ADDRESS);
					perfCheckOk = false;
					return null;
				} catch(Exception e) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Collection Validation Exception</font>\n");
					sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Register ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
					sb.append(String.format("\nRegister ???? ???? : <font color='blue'>0x0000 ~ 0xFFFF</font>%s\n\n", Util.separator));
					sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Register ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(REGISTER_ADDRESS)).replace("0X", "0x").trim() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					setFocusCell(table, i, REGISTER_ADDRESS);
					perfCheckOk = false;
					return null;
				}
			}		
			*/
			
			// toLowerCase() ???????? HEX ???????? ???? ???????? ?????????? ???????? ??????
			perfs[i].setRegisterAddress(String.valueOf(data.get(REGISTER_ADDRESS)).replace("X", "x").trim());
			
			
			// ???????? ???? --------------------------------------------------------------------------------
			int modbusAddress;
			
			/*
			try {
	 			modbusAddress = Integer.parseInt(String.valueOf(data.get(MODBUS_ADDRESS)).trim());
	 			modbusAddress %= 10000;
	 			if((registerAddress < 10000) && ((registerAddress + 1) != (modbusAddress))) throw new RuntimeException("Modbus ???? ???? ????");
			}catch(NumberFormatException e) {
				// ???????? ?????? ?????? ???? ?? ?? ???? ????
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Collection Validation Exception</font>\n");
				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Modbus ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
				sb.append(String.format("\nModbus ?????? <font color='blue'>???? ??</font>???? ???? ?? ?? ????????%s\n\n", Util.separator));
				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Modbus ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}catch (RuntimeException e) {
				// ???????? ?????? 10000 ?????????? ???????? ?????? ?? ???? ???????? ???? ????
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Collection Validation Exception</font>\n");
				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Modbus ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("\nModbus ???????? ?????? <font color='blue'>10000</font>?? ???? ???? ?????? ????\nRegister ???????? <font color='blue'>1</font>???? ?????? ??????%s\n\n", Util.separator));
				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Register ???? ???? : <font color='red'>%s ( %s )</font>%s\n",String.valueOf(data.get(ORDER)), registerAddress, perfs[i].getRegisterAddress(),Util.separator));
				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Modbus ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				sb.append(String.format("\n??) FC 01&nbsp;&nbsp;??&nbsp;&nbsp;Register ???? : 0x0000  /  Modbus ???? : 1%s\n", Util.separator));
				sb.append(String.format("??) FC 02&nbsp;&nbsp;??&nbsp;&nbsp;Register ???? : 0x0123  /  Modbus ???? : 10292%s\n", Util.separator));
				sb.append(String.format("??) FC 03&nbsp;&nbsp;??&nbsp;&nbsp;Register ???? : 0x0ABC  /  Modbus ???? : 42749%s\n", Util.separator));
				sb.append(String.format("??) FC 04&nbsp;&nbsp;??&nbsp;&nbsp;Register ???? : 0x12AB  /  Modbus ???? : 34780%s\n", Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}catch(Exception e) {
				// ?????? ???? ?? ?? ???? ???? ???? ??
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Collection Validation Exception</font>\n");
				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>Modbus ????</font> ?????? ?????? ????????%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? Modbus ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(MODBUS_ADDRESS)).trim() ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				setFocusCell(table, i, MODBUS_ADDRESS);
				perfCheckOk = false;
				return null;
			}
			*/
			
			perfs[i].setModbusAddress(String.valueOf(data.get(MODBUS_ADDRESS)).trim());
			
			// ?????? ???? ---------------------------------------------------------------------------------
			String dataType = String.valueOf(data.get(DATA_TYPE)).trim().toUpperCase();
			
			if(!DataType.typeMap.containsKey(dataType)) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>?????? ????</font> ?????? ?????? ????????%s%s%s\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));
//				
//				sb.append(String.format("\n<font color='blue'>Modbus Collection ???? ?????? ????</font>%s\n", Util.separator));				
//				sb.append(String.format("1. %s%s%s\n","BINARY" , Util.separator, Util.separator));				
//				sb.append(String.format("2. %s%s%s\n","TWO BYTE INT SIGNED" , Util.separator, Util.separator));
//				sb.append(String.format("3. %s%s%s\n","TWO BYTE INT UNSIGNED" , Util.separator, Util.separator));
//				sb.append(String.format("4. %s%s%s\n","FOUR BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
//				sb.append(String.format("5. %s%s%s\n","FOUR BYTE INT SIGNED (C D A B)" , Util.separator, Util.separator));
//				sb.append(String.format("6. %s%s%s\n","FOUR BYTE INT UNSIGNED (A B C D)" , Util.separator, Util.separator));
//				sb.append(String.format("7. %s%s%s\n","FOUR BYTE INT UNSIGNED (C D A B)" , Util.separator, Util.separator));
//				sb.append(String.format("8. %s%s%s\n","FOUR BYTE FLOAT (A B C D)" , Util.separator, Util.separator));
//				sb.append(String.format("9. %s%s%s\n","FOUR BYTE FLOAT (C D A B)" , Util.separator, Util.separator));
//				sb.append(String.format("10. %s%s%s\n","EIGHT BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
//				sb.append(String.format("11. %s%s%s\n","EIGHT BYTE DOUBLE (A B C D)" , Util.separator, Util.separator));				
//				sb.append(String.format("\n???? ?????? <font color='blue'>%s</font> ???? ?????? ???? ???? : <font color='red'>%s</font>%s%s\n",String.valueOf(data.get(ORDER)), dataType ,Util.separator,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.dataType_1(String.valueOf(data.get(ORDER)), dataType);				
				setFocusCell(table, i, DATA_TYPE);
				perfCheckOk = false;
				return null;
			}			
			perfs[i].setDataType(dataType);
			perfs[i].setMK119_DataType(DataType.typeMap.get(dataType));
			
			// ?????? ???? ?????? BINARY ???? ?????????? 1, 2???? ???? ????
			if(perfs[i].getDataType().equalsIgnoreCase("BINARY") && !( perfs[i].getFunctionCode().equalsIgnoreCase("1") || perfs[i].getFunctionCode().equalsIgnoreCase("2"))) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>?????? ????</font> ?????? ?????? ????????%s%s%s\n\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));	
//				sb.append(String.format("<font color='blue'>BINARY</font> ?????? ?????? ON/OFF ?????? ???????? ?????? ????????%s%s\n\n", Util.separator, Util.separator));					
//				sb.append(String.format("???????? 1, 2???? ???????? ???????? ?????? ???? ?? ?? ????????%s%s\n\n", Util.separator, Util.separator));				
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ???????? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode() ,Util.separator));
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ?????? ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getDataType() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);				
				
				MessageUtil_en.dataType_2(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode(), perfs[i].getDataType());
				setFocusCell(table, i, DATA_TYPE);
				perfCheckOk = false;
				return null;
			}
			
			// ?????????? 1, 2?????? ?????? ???? ?????? BINARY?? ???? ????
			if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2")) && (!perfs[i].getDataType().equalsIgnoreCase("BINARY")) ) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>?????? ????</font> ?????? ?????? ????????%s%s%s\n\n", String.valueOf(data.get(ORDER)), Util.separator, Util.separator, Util.separator));									
//				sb.append(String.format("???????? 1, 2???? ???????? ??????????\n\n?????? ???????? ???? <font color='blue'>BINARY</font> ???????? ???? ?? ?? ????????%s%s\n\n", Util.separator, Util.separator));				
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ???????? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getFunctionCode() ,Util.separator));
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ?????? ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) ,perfs[i].getDataType() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.dataType_3(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode(), perfs[i].getDataType());
				setFocusCell(table, i, DATA_TYPE);
				perfCheckOk = false;
				return null;
			}
			
			// ???? ????  ----------------------------------------------------------------------------------
			String interval = String.valueOf(data.get(INTERVAL)).trim();
			
			try{
				int actualInterval = Integer.parseInt(interval);
				
				if(actualInterval < 1) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Collection Validation Exception</font>\n");
//					sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
//					sb.append(String.format("\n???? ?????? 1 ?????? ???? ???? ???? ???? ?? ?? ????????%s\n\n", Util.separator));
//					sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ???????? ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.interval_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim());
					setFocusCell(table, i, INTERVAL);
					perfCheckOk = false;
					return null;
				}
				
			}catch (Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));				
//				sb.append(String.format("\n?????? ???????? ?????? <font color='blue'>???? ??</font>???? ???? ?? ?? ????????%s\n\n", Util.separator));
//				sb.append(String.format("???? ?????? <font color='blue'>%s</font> ???? ???????? ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)), String.valueOf(data.get(INTERVAL)).trim() ,Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.interval_2(String.valueOf(data.get(ORDER)) ,String.valueOf(data.get(INTERVAL)).trim());
				setFocusCell(table, i, INTERVAL);
				perfCheckOk = false;
				return null;
			}
			
			perfs[i].setInterval(interval);
			
			// ?? ??  ------------------------------------------------------------------------------------
			perfs[i].setMeasure(String.valueOf(data.get(MEASURE)).trim());
						
			perfName = perfs[i].getDisplayName();

			// ?????? ???????? ?????????? ?????? ???????? ???? ????
			if(useAutoMeasure.isSelected()) {
				if(perfName != null && perfName.length() > 1 && !perfName.equalsIgnoreCase("")) { 
					if(perfs[i].getMeasure().length() == 0 || perfs[i].getMeasure().equals("")) {					
						perfs[i].setMeasure(Perf.createMeasure(perfName).trim());
					}
				}
			}
			
			
			// ?????? ------------------------------------------------------------------------------------
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
				// ?????? ?????? ????
				Calculator.checkFormula(scaleFunction, 1);
				if (scaleFunction.equalsIgnoreCase("") || scaleFunction.length() < 1 || !scaleFunction.contains("x"))
					throw new Exception("Scale Validation Error");				
			}catch(Exception e) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>??????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n???? ?????? <font color='blue'>%s</font> ???? ?????? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase() ,Util.separator));				
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.scaleFunction_1(String.valueOf(data.get(ORDER)), String.valueOf(data.get(SCALE_FUNCTION)).trim().toLowerCase());
				setFocusCell(table, i, SCALE_FUNCTION);
				perfCheckOk = false;
				return null;
			}			
			perfs[i].setScaleFunction(scaleFunction);
			
			// ?????? ???? ?? ---------------------------------------------------------------------------------
			perfs[i].setLastValue(String.valueOf(data.get(LAST_VALUE)));
			
			
			// ???? ???? : 0, 1 ---------------------------------------------------------------------------
			HashMap binaryMap = perfs[i].getBinaryMap();
			String label0 = String.valueOf(data.get(LABLE_0)).trim();
			String label1 = String.valueOf(data.get(LABLE_1)).trim();
			
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				// ???? ???? ?????? ???????? ???????? ?????? ???? ?? ???? ???? 
				if((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1))) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Collection Validation Exception</font>\n");
//					sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ???? : 1</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));	
//					
//					if (perfs[i].getFunctionCode().equals("1") || perfs[i].getFunctionCode().equals("2"))
//						sb.append(String.format("\n???????? 1, 2???? ???????? ??????????\n\n?????? <font color='blue'>???? ???? : 0, 1</font> ?????? ???????? ??????%s\n", Util.separator));
//					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.binary_1(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode());
					setFocusCell(table, i, LABLE_1);
					perfCheckOk = false;
					return null;
				}
			}else if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
				if((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1))) {
					// ???? ???? ?????? ???????? ???????? ?????? ???? ?? ???? ????
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Collection Validation Exception</font>\n");
//					sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ???? : 0</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//					
//					if (perfs[i].getFunctionCode().equals("1") || perfs[i].getFunctionCode().equals("2"))
//						sb.append(String.format("\n???????? 1, 2???? ???????? ??????????\n\n?????? <font color='blue'>???? ???? : 0, 1</font> ?????? ???????? ??????%s\n", Util.separator));
//					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
					MessageUtil_en.binary_2(String.valueOf(data.get(ORDER)), perfs[i].getFunctionCode());
					setFocusCell(table, i, LABLE_0);
					perfCheckOk = false;
					return null;
				}
			}
			
			// ?????????? ?????????? 1, 2?? ?????? ???? ???? : 0, ???? ???? : 1 ?????? ???? ????
			if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2"))
				&& ((label0 == null) || (label0.equalsIgnoreCase("") || (label0.length() < 1)))){
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ???? : 0</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));			
//				sb.append(String.format("\n???????? 1, 2???? ???????? ??????????\n\n?????? <font color='blue'>???? ???? : 0, 1</font> ?????? ???????? ??????%s\n", Util.separator));
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.binary_3(String.valueOf(data.get(ORDER)));
				setFocusCell(table, i, LABLE_0);
				perfCheckOk = false;
				return null;
			}else if((perfs[i].getFunctionCode().equals("1")||perfs[i].getFunctionCode().equals("2"))
				&& ((label1 == null) || (label1.equalsIgnoreCase("") || (label1.length() < 1)))){
//				StringBuilder sb = new StringBuilder();
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ???? : 1</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));			
//				sb.append(String.format("\n???????? 1, 2???? ???????? ??????????\n\n?????? <font color='blue'>???? ???? : 0, 1</font> ?????? ???????? ??????%s\n", Util.separator));
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
			
			
			// ???? ???? --------------------------------------------------------------------------			
			HashMap multiStatusMap = new HashMap();
			
			if ((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if ((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					if((String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
//						StringBuilder sb = new StringBuilder();
//						sb.append("<font color='red'>Collection Validation Exception</font>\n");
//						sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ????</font>?? <font color='blue'>???? ????</font> ?????? ?????? ????????%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
//						sb.append(String.format("?????? ?????? <font color='blue'>???? ????</font> ?????? ?????? <font color='blue'>???? ????</font> ?? ???? ????????%s\n", Util.separator));
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
//				sb.append("<font color='red'>Collection Validation Exception</font>\n");
//				sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ????</font> ?????? ?????? ????????%s\n", String.valueOf(data.get(ORDER)), Util.separator));
//				sb.append(String.format("\n???? ?????? <font color='blue'>%s</font> ???? ???? ???? ???? : <font color='red'>%s</font>%s\n",String.valueOf(data.get(ORDER)) , String.valueOf(data.get(LABLE_STATUS)).trim() ,Util.separator));
//				if(e instanceof NumberFormatException) {
//					sb.append(String.format("\n???? ???? ???? ???? : <font color='blue'>????1; ????1; ????2; ????2; ????3; ????3; ...</font>%s%s\n", Util.separator, Util.separator));
//				}
//				
//				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				MessageUtil_en.multiStatus_2(String.valueOf(data.get(ORDER)), String.valueOf(data.get(LABLE_STATUS)).trim(), e);
				setFocusCell(table, i, LABLE_STATUS);
				perfCheckOk = false;
				return null;
			}
			perfs[i].setMultiStatusMap(multiStatusMap);
						
			
			// ?????? ???? -------------------------------------------------------------------------------
			if((label0 != null) && (!label0.equalsIgnoreCase("") && (label0.length() >= 1))) {
				if((label1 != null) && (!label1.equalsIgnoreCase("") && (label1.length() >= 1))) {
					
					// ?????? ?????? ???? ?????????? ???? ???? ?? ???? ????
					if((multiStatusMap.size() >= 1) || (String.valueOf(data.get(LABLE_STATUS)).length() >= 1)) {
//						StringBuilder sb = new StringBuilder();
//						sb.append("<font color='red'>Collection Validation Exception</font>\n");
//						sb.append(String.format("?????? <font color='blue'>%s</font> ???? <font color='blue'>???? ????</font>?? <font color='blue'>???? ????</font> ?????? ?????? ????????%s\n\n", String.valueOf(data.get(ORDER)), Util.separator));
//						sb.append(String.format("?????? ?????? <font color='blue'>???? ????</font> ?????? ?????? <font color='blue'>???? ????</font> ?? ???? ????????%s\n", Util.separator));
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
				
				// inspect NumberFormatException : ???? ?????? ??-???? ???????? ?????????? ????
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
		
	public void sortingAndValidation(){
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Inspection Thread is Working</font>\n");
		sb.append(String.format("Table sorting and Validation is currently being carried out%s\n", Util.separator));
		sb.append(String.format("\nNo other requests can be processed during the operation%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public void addingMK119Perf() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>MK119 Adding Thread is Working</font>\n");
		sb.append(String.format("Currently working on adding MK119 performance%s\n", Util.separator));
		sb.append(String.format("\nNo other requests can be processed during the operation%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * ?????? ?????? JTable?? ?????? ?????? ?????? ?????? ??????????.
	 * ???? ?????? ?????? ?????????? ?????? ????
	 */	
	public static void setCellContentCenter(JTable table) {
		// DefaultTableCellHeaderRenderer ???? (?????? ?????? ????)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer?? ?????? ?????? ?????? ????
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ?????? ???????? ColumnModel?? ??????
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(ORDER).setCellRenderer(tScheduleCellRenderer); // ????
//		tcmSchedule.getColumn(PERF_NAME).setCellRenderer(tScheduleCellRenderer); // ??????
		tcmSchedule.getColumn(FUNCTION_CODE).setCellRenderer(tScheduleCellRenderer); // ????????
		tcmSchedule.getColumn(REGISTER_ADDRESS).setCellRenderer(tScheduleCellRenderer); // Register ????
		tcmSchedule.getColumn(MODBUS_ADDRESS).setCellRenderer(tScheduleCellRenderer); // Modbus ????
		tcmSchedule.getColumn(DATA_TYPE).setCellRenderer(tScheduleCellRenderer); // ?????? ????
		tcmSchedule.getColumn(INTERVAL).setCellRenderer(tScheduleCellRenderer); // ????????
		tcmSchedule.getColumn(MEASURE).setCellRenderer(tScheduleCellRenderer); // ????
		tcmSchedule.getColumn(SCALE_FUNCTION).setCellRenderer(tScheduleCellRenderer); // ??????
		tcmSchedule.getColumn(LAST_VALUE).setCellRenderer(tScheduleCellRenderer); // ?????? ???? ??
		tcmSchedule.getColumn(LABLE_0).setCellRenderer(tScheduleCellRenderer); // ???? ???? : 0
		tcmSchedule.getColumn(LABLE_1).setCellRenderer(tScheduleCellRenderer); // ???? ???? : 1		
//		tcmSchedule.getColumn(LABLE_STATUS).setCellRenderer(tScheduleCellRenderer); // ???? ????
	}
}
