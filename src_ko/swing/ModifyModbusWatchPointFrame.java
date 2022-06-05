package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.modbus.ModbusWatchPoint;
import src_ko.util.Util;

public class ModifyModbusWatchPointFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JPanel panel;
	private JButton mk119Button;
	private JTable table; // frame마다 XML 인스턴스를 가져야 하므로 table 필드는 static 속성을 가질 수 없다
	private JComboBox fc_var;	
	private JComboBox dataType_var;
	private JComboBox dataFormat_var;
	private JRadioButton addr_reg_hex;
	private JRadioButton addr_reg_dec;
	private JRadioButton addr_modbus_dec;	
	private JTextField addr_reg_hex_var;
	private JTextField addr_reg_dec_var;
	private JTextField addr_modbus_dec_var;
	
	private JButton addButton;
	private JButton resetButton;
	private ActionListener radioListener;
	private JTextField pointName_var;
	private JLabel scale;
	private JTextField scale_var;
	private JLabel dataFormat;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyModbusWatchPointFrame frame = new ModifyModbusWatchPointFrame();
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
	public ModifyModbusWatchPointFrame() {
		ModifyModbusWatchPointFrame.isExist = true;
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
		
		setBounds(100, 100, 790, 515);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Modify Modbus Point");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 300, 55);
		actualPanel.add(currentFunction);
		
		panel = new JPanel();
		panel.setBackground(new Color(220, 220, 220));
		panel.setBounds(10, 65, 742, 391);
		panel.setLayout(null);
		actualPanel.add(panel);
		
		JLabel pointName = new JLabel("포인트 이름");
		pointName.setHorizontalAlignment(SwingConstants.LEFT);
		pointName.setForeground(Color.BLACK);
		pointName.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		pointName.setBackground(Color.WHITE);
		pointName.setBounds(31, 15, 212, 30);
		panel.add(pointName);
		
		JLabel fc = new JLabel("기능 코드");
		fc.setBounds(31, 65, 212, 30);
		panel.add(fc);
		fc.setHorizontalAlignment(SwingConstants.LEFT);
		fc.setForeground(Color.BLACK);
		fc.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc.setBackground(Color.WHITE);
		
		radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();
				
				switch (b.getText().trim()) {					
					case "모드버스 주소 ( DEC )":
						addr_modbus_dec_var.setEnabled(true);
						addr_reg_dec_var.setEnabled(false);
						addr_reg_hex_var.setEnabled(false);
						
						addr_modbus_dec_var.setBackground(Color.WHITE);
						addr_reg_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_hex_var.setBackground(new Color(220, 220, 220));
						
						addr_modbus_dec_var.setText(null);
						addr_reg_dec_var.setText(null);
						addr_reg_hex_var.setText(null);
						break;
		
					case "레지스터 주소 ( DEC )" :
						addr_modbus_dec_var.setEnabled(false);
						addr_reg_dec_var.setEnabled(true);
						addr_reg_hex_var.setEnabled(false);
						
						addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_dec_var.setBackground(Color.WHITE);
						addr_reg_hex_var.setBackground(new Color(220, 220, 220));
						
						addr_modbus_dec_var.setText(null);
						addr_reg_dec_var.setText(null);
						addr_reg_hex_var.setText(null);
						break;
						
					case "레지스터 주소 ( HEX )":
						addr_modbus_dec_var.setEnabled(false);
						addr_reg_dec_var.setEnabled(false);
						addr_reg_hex_var.setEnabled(true);
						
						addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_hex_var.setBackground(Color.WHITE);
						
						addr_modbus_dec_var.setText(null);
						addr_reg_dec_var.setText(null);
						addr_reg_hex_var.setText(null);
						break;
				}
							
			}						
		};
		
		addr_modbus_dec = new JRadioButton(" 모드버스 주소 ( DEC )");
		addr_modbus_dec.setSelected(false);
		addr_modbus_dec.setFocusPainted(false);
		addr_modbus_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_modbus_dec.setForeground(Color.BLACK);
		addr_modbus_dec.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_modbus_dec.setBackground(new Color(220, 220, 220));
		addr_modbus_dec.setBounds(8, 115, 235, 30);
		addr_modbus_dec.addActionListener(radioListener);
		panel.add(addr_modbus_dec);
		
		addr_reg_dec = new JRadioButton(" 레지스터 주소 ( DEC )");
		addr_reg_dec.setSelected(true);
		addr_reg_dec.setFocusPainted(false);
		addr_reg_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_dec.setForeground(Color.BLACK);
		addr_reg_dec.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_reg_dec.setBackground(new Color(220, 220, 220));
		addr_reg_dec.setBounds(8, 155, 235, 30);
		addr_reg_dec.addActionListener(radioListener);
		panel.add(addr_reg_dec);
		
		addr_reg_hex = new JRadioButton(" 레지스터 주소 ( HEX )");
		addr_reg_hex.setSelected(false);
		addr_reg_hex.setFocusPainted(false);
		addr_reg_hex.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_hex.setForeground(Color.BLACK);
		addr_reg_hex.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_reg_hex.setBackground(new Color(220, 220, 220));
		addr_reg_hex.setBounds(8, 195, 235, 30);
		addr_reg_hex.addActionListener(radioListener);
		panel.add(addr_reg_hex);
		
		ButtonGroup group = new ButtonGroup();
		group.add(addr_modbus_dec);
		group.add(addr_reg_dec);
		group.add(addr_reg_hex);
		
		JLabel dataType = new JLabel("데이터 타입");
		dataType.setHorizontalAlignment(SwingConstants.LEFT);
		dataType.setForeground(Color.BLACK);
		dataType.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType.setBackground(Color.WHITE);
		dataType.setBounds(31, 245, 212, 30);
		panel.add(dataType);
		
		fc_var = new JComboBox();
		fc_var.setBorder(new LineBorder(Color.BLACK, 2));
		fc_var.setModel(new DefaultComboBoxModel(new String[] {"FC 01", "FC 02", "FC 03", "FC 04"}));
		fc_var.setForeground(Color.BLACK);
		fc_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc_var.setBackground(Color.WHITE);
		fc_var.setBounds(259, 65, 190, 30);
		fc_var.setSelectedIndex(2);
		fc_var.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int fc = Integer.parseInt(fc_var.getSelectedItem().toString().split(" ")[1]);
				
				if(fc >= 3) {
					dataType_var.setModel(new DefaultComboBoxModel(
							new String[] {									
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
				}else {
					dataType_var.setModel(new DefaultComboBoxModel(
							new String[] {
									"BINARY"
									}));
				}
				
				syncAddr();
				dataType_var.setSelectedIndex(0);
				
			}
		});
		
		scale = new JLabel("보정식");
		scale.setHorizontalAlignment(SwingConstants.LEFT);
		scale.setForeground(Color.BLACK);
		scale.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		scale.setBackground(Color.WHITE);
		scale.setBounds(31, 295, 212, 30);
		panel.add(scale);
		
		dataFormat = new JLabel("데이터 형식");
		dataFormat.setHorizontalAlignment(SwingConstants.LEFT);
		dataFormat.setForeground(Color.BLACK);
		dataFormat.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataFormat.setBackground(Color.WHITE);
		dataFormat.setBounds(31, 345, 212, 30);
		panel.add(dataFormat);
		panel.add(fc_var);
		
		addr_modbus_dec_var = new JTextField();
		addr_modbus_dec_var.setEnabled(false);
		addr_modbus_dec_var.setForeground(Color.BLUE);
		addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
		addr_modbus_dec_var.setDisabledTextColor(Color.BLUE);
		addr_modbus_dec_var.setBorder(new LineBorder(Color.BLACK, 2));
		addr_modbus_dec_var.setHorizontalAlignment(SwingConstants.LEFT);		
		addr_modbus_dec_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_modbus_dec_var.setColumns(10);
		addr_modbus_dec_var.setBounds(259, 115, 190, 30);
		addr_modbus_dec_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(addr_modbus_dec_var);
		
		addr_reg_dec_var = new JTextField();
		addr_reg_dec_var.setEnabled(true);
		addr_reg_dec_var.setForeground(Color.BLUE);
		addr_reg_dec_var.setBackground(Color.WHITE);
		addr_reg_dec_var.setDisabledTextColor(Color.BLUE);
		addr_reg_dec_var.setBorder(new LineBorder(Color.BLACK, 2));
		addr_reg_dec_var.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_dec_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_reg_dec_var.setColumns(10);		
		addr_reg_dec_var.setBounds(259, 155, 190, 30);
		addr_reg_dec_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(addr_reg_dec_var);
		
		addr_reg_hex_var = new JTextField();
		addr_reg_hex_var.setEnabled(false);
		addr_reg_hex_var.setForeground(Color.BLUE);
		addr_reg_hex_var.setBackground(new Color(220, 220, 220));
		addr_reg_hex_var.setDisabledTextColor(Color.BLUE);
		addr_reg_hex_var.setBorder(new LineBorder(Color.BLACK, 2));
		addr_reg_hex_var.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_hex_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));		
		addr_reg_hex_var.setBounds(261, 195, 188, 30);
		addr_reg_hex_var.setColumns(10);
		addr_reg_hex_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(addr_reg_hex_var);
		
		dataType_var = new JComboBox();
		dataType_var.setBorder(new LineBorder(Color.BLACK, 2));
		dataType_var.setForeground(Color.BLACK);
		dataType_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType_var.setBackground(Color.WHITE);
		dataType_var.setBounds(259, 245, 401, 30);
		dataType_var.setModel(new DefaultComboBoxModel(
				new String[] {
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
		dataType_var.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = dataType_var.getSelectedItem().toString().toUpperCase().trim();
				if(dataType.length() < 1 || dataType.equals("")) dataType_var.setSelectedIndex(0);
				
				int step = 1;
				
				if(dataType.startsWith("BIN") || dataType.startsWith("TWO")) {
					step  = 1;			
				}else if(dataType.startsWith("FOUR")) {
					step = 2;
				}else if(dataType.startsWith("EIGHT")) {
					step = 4;
				}else {
					step = 1;
				}
				
				
			}
		});
		panel.add(dataType_var);
		
		pointName_var = new JTextField();
		pointName_var.setHorizontalAlignment(SwingConstants.LEFT);
		pointName_var.setForeground(Color.BLUE);
		pointName_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		pointName_var.setEnabled(true);		
		pointName_var.setColumns(10);
		pointName_var.setBorder(new LineBorder(Color.BLACK, 2));
		pointName_var.setBackground(Color.WHITE);
		pointName_var.setBounds(259, 15, 401, 30);
		panel.add(pointName_var);
		
		scale_var = new JTextField();
		scale_var.setHorizontalAlignment(SwingConstants.LEFT);
		scale_var.setForeground(Color.BLUE);
		scale_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		scale_var.setEnabled(true);
		scale_var.setColumns(10);
		scale_var.setBorder(new LineBorder(Color.BLACK, 2));
		scale_var.setBackground(Color.WHITE);
		scale_var.setBounds(259, 295, 190, 30);
		panel.add(scale_var);
		
		dataFormat_var = new JComboBox();
		dataFormat_var.setForeground(Color.BLACK);
		dataFormat_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataFormat_var.setBorder(new LineBorder(Color.BLACK, 2));
		dataFormat_var.setBackground(Color.WHITE);
		dataFormat_var.setBounds(259, 345, 401, 30);
		dataFormat_var.setModel(new DefaultComboBoxModel(
				new String[] {
						"1 ( 이진 상태 )",
						"2 ( 다중 상태 )",
						"3 ( 아날로그 데이터 )"
						}));
		panel.add(dataFormat_var);
		
		addButton = new JButton();
		addButton.setBounds(668, 10, 84, 32);
		addButton.setText("추 가");
		addButton.setForeground(new Color(0, 128, 0));
		addButton.setBackground(Color.WHITE);
		addButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		addButton.setFocusPainted(false);		
		addButton.setBorder(UIManager.getBorder("Button.border"));
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkFormValidation()) {
					try {
						ArrayList<ModbusWatchPoint> pointList = null;
						
						if(pointList != null) {
							ModbusMonitor_Panel.addPointList(pointList);
							ModbusMonitor_Panel.doTableFilter();
							
							StringBuilder sb = new StringBuilder();
							sb.append(String.format("%s%s%s\n", Util.colorGreen("Modbus Point Added Successfully"), Util.separator, Util.separator));
							
							sb.append(Util.separator + Util.separator + Util.separator + "\n");
							Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		actualPanel.add(addButton);
		
		resetButton = new JButton();
		resetButton.setBounds(565, 10, 97, 32);
		resetButton.setText("초기화");
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		resetButton.setFocusPainted(false);		
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetForm();
			}
		});
		actualPanel.add(resetButton);
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
		
		addr_reg_dec_var.requestFocus();
	}
	
	public int syncAddr() {
		int fc = Integer.parseInt(fc_var.getSelectedItem().toString().split(" ")[1]); 
		int startAddress = 0;
		String modbusAddress = "";
		String addr;
		
		switch(fc) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		
		try {
		
		if(addr_modbus_dec.isSelected()) {
			addr = addr_modbus_dec_var.getText().trim();
			if(addr.length() < 1 || addr.equals("")) {
				addr_modbus_dec_var.setText(null);
				addr_reg_dec_var.setText(null);
				addr_reg_hex_var.setText(null);
				return -1;
			}
			startAddress = Integer.parseInt(addr);
			startAddress = (startAddress % 10000) - 1;
			if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
			
		}else if(addr_reg_dec.isSelected()) {
			addr = addr_reg_dec_var.getText().trim();
			if(addr.length() < 1 || addr.equals("")) {
				addr_modbus_dec_var.setText(null);
				addr_reg_dec_var.setText(null);
				addr_reg_hex_var.setText(null);
				return -1;
			}
			startAddress = Integer.parseInt(addr);
			if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
			
		}else {
			addr = addr_reg_hex_var.getText().trim();
			if(addr.length() < 1 || addr.equals("")) {
				addr_modbus_dec_var.setText(null);
				addr_reg_dec_var.setText(null);
				addr_reg_hex_var.setText(null);
				return -1;
			}
			if(addr.startsWith("0x")||addr.startsWith("0X")) {
				startAddress = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16); 				
			}else {
				addr_reg_hex_var.setText("0x" + addr);
				startAddress = Integer.parseInt(addr.replaceAll("0x", "").replaceAll("0X", ""),16);
			}
			
			if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
		}
		
		String modbusAddr = String.format("%s%04d", modbusAddress, (startAddress & 0xffff) + 1);
		String registerAddr_Hex = String.format("0x%04X", startAddress);
		
		if(addr_modbus_dec.isSelected()) {
//			addr_modbus_dec_var.setText(modbusAddr);
			addr_reg_dec_var.setText(String.valueOf(startAddress));
			addr_reg_hex_var.setText(registerAddr_Hex);
			
		}else if(addr_reg_dec.isSelected()) {
			addr_modbus_dec_var.setText(modbusAddr);
//			addr_reg_dec_var.setText(String.valueOf(startAddress));
			addr_reg_hex_var.setText(registerAddr_Hex);
			
		}else {
			addr_modbus_dec_var.setText(modbusAddr);
			addr_reg_dec_var.setText(String.valueOf(startAddress));
//			addr_reg_hex_var.setText(registerAddr_Hex);
		}
		
		addr_modbus_dec_var.setForeground(Color.BLUE);
		addr_modbus_dec_var.setDisabledTextColor(Color.BLUE);
		addr_reg_dec_var.setForeground(Color.BLUE);
		addr_reg_dec_var.setDisabledTextColor(Color.BLUE);
		addr_reg_hex_var.setForeground(Color.BLUE);
		addr_reg_hex_var.setDisabledTextColor(Color.BLUE);
		return startAddress;
		
		}catch(NumberFormatException e) {
			if(addr_modbus_dec.isSelected()) {
//				addr_modbus_dec_var.setText("유효하지 않은 주소");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_dec_var.setText("유효하지 않은 주소");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_hex_var.setText("유효하지 않은 주소");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
				
			}else if(addr_reg_dec.isSelected()) {
				addr_modbus_dec_var.setText("유효하지 않은 주소");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
//				addr_reg_dec_var.setText("유효하지 않은 주소");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_hex_var.setText("유효하지 않은 주소");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
				
			}else {
				addr_modbus_dec_var.setText("유효하지 않은 주소");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_dec_var.setText("유효하지 않은 주소");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
//				addr_reg_hex_var.setText("유효하지 않은 주소");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
			}
			
			return -1;
		}
		
	}
	
	public void resetForm() {
		fc_var.setSelectedIndex(2);
		addr_modbus_dec_var.setText(null);
		addr_reg_dec_var.setText(null);
		addr_reg_hex_var.setText(null);		
		dataType_var.setSelectedIndex(0);
	}
	
	public boolean checkFormValidation() {
		boolean formValid = true;
		formValid = formValid && !(addr_modbus_dec_var.getForeground() == Color.RED);
		formValid = formValid && !(addr_reg_dec_var.getForeground() == Color.RED);
		formValid = formValid && !(addr_reg_hex_var.getForeground() == Color.RED);
		formValid = formValid && !(addr_modbus_dec_var.getText().length() < 1 || addr_modbus_dec_var.getText().equals(""));
		formValid = formValid && !(addr_reg_dec_var.getText().length() < 1 || addr_reg_dec_var.getText().equals(""));
		formValid = formValid && !(addr_reg_hex_var.getText().length() < 1 || addr_reg_hex_var.getText().equals(""));
		formValid = formValid && !(addr_modbus_dec_var.getText().trim().equals("유효하지 않은 주소"));
		formValid = formValid && !(addr_reg_dec_var.getText().trim().equals("유효하지 않은 주소"));
		formValid = formValid && !(addr_reg_hex_var.getText().trim().equals("유효하지 않은 주소"));
		if(!formValid) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "추가하실 모드버스 포인트의 시작 " + Util.colorBlue("주소(Address)") +  " 정보를 입력해주세요"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return formValid;
	}
	
	public ArrayList<ModbusWatchPoint> getPointList(boolean get) {
		int fc = Integer.parseInt(fc_var.getSelectedItem().toString().split(" ")[1]);
		
		String dataType = dataType_var.getSelectedItem().toString().toUpperCase().trim();
		int step = 1;
		if(dataType.startsWith("BIN") || dataType.startsWith("TWO")) {
			step  = 1;			
		}else if(dataType.startsWith("FOUR")) {
			step = 2;
		}else if(dataType.startsWith("EIGHT")) {
			step = 4;
		}
		
		try {
			
			return null;
		}catch(Exception ex) {			
			return null;
		}
	}

	public String getModbusAddr(int functionCode, int registerAddr) {
		String modbusAddress = "";
		switch(functionCode) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		return String.format("%s%04d", modbusAddress, (registerAddr & 0xffff) + 1);
	}
	
	public String getRegisterAddrHex(int registerAddr) {
		return String.format("0x%04X", registerAddr);
	}
	
	public void useTable(boolean enabled) {
		if(enabled) {
			this.setSize(new Dimension(790, 785));
			panel.setSize(new Dimension(742, 661));
		}else {
			this.setSize(new Dimension(790, 515));
			panel.setSize(new Dimension(742, 391));
		}
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Modify Modbus Watch Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Modify Modbus Watch Point 프레임이 이미 열려있습니다" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	@Override
	public void dispose() {
		ModifyModbusWatchPointFrame.isExist = false;
		super.dispose();
	}
}

