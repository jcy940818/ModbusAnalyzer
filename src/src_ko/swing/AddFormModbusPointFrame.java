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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.modbus.ModbusWatchPoint;
import common.util.FontManager;
import common.util.SwingUtil;
import src_ko.util.Util;

public class AddFormModbusPointFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JPanel panel;
	private JComboBox fc_var;	
	private JRadioButton addr_modbus_dec;	
	private JRadioButton addr_reg_dec;
	private JRadioButton addr_reg_hex;
	private JTextField addr_modbus_dec_var;
	private JTextField addr_reg_dec_var;
	private JTextField addr_reg_hex_var;
	private JTextField cnt_var;
	private JComboBox dataType_var;

	private JButton showContentButton;
	private JButton addButton;
	private JButton resetButton;	
	private JCheckBox incrementAddr;
	private ActionListener radioListener;	
	private JLabel addrStep;
	private JLabel label;
	private JLabel con_1;
	private JLabel con_2;
	private JLabel con_3;
	private JLabel con_4;
	private JLabel con_0;
	private JLabel con_5;
	
	private KeyAdapter saveAndCloseAdpter;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddFormModbusPointFrame frame = new AddFormModbusPointFrame();
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
	public AddFormModbusPointFrame() {
		AddFormModbusPointFrame.isExist = true;
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 790, 484);
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
		
		JLabel currentFunction = new JLabel("Add Modbus Point");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 277, 55);
		actualPanel.add(currentFunction);
		
		panel = new JPanel();
		panel.setBackground(new Color(220, 220, 220));
		panel.setBounds(10, 65, 742, 358);
		panel.setLayout(null);
		actualPanel.add(panel);
		
		JLabel fc = new JLabel("��� �ڵ�");
		fc.setBounds(35, 10, 212, 30);
		panel.add(fc);
		fc.setHorizontalAlignment(SwingConstants.LEFT);
		fc.setForeground(Color.BLACK);
		fc.setFont(FontManager.getFont(Font.BOLD, 17));
		fc.setBackground(Color.WHITE);
		
		radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();
				
				switch (b.getText().trim()) {					
					case "������ �ּ� ( DEC )":
						addr_modbus_dec_var.setEnabled(true);
						addr_reg_dec_var.setEnabled(false);
						addr_reg_hex_var.setEnabled(false);
						
						addr_modbus_dec_var.setBackground(Color.WHITE);
						addr_reg_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_hex_var.setBackground(new Color(220, 220, 220));
						break;
		
					case "�������� �ּ� ( DEC )" :
						addr_modbus_dec_var.setEnabled(false);
						addr_reg_dec_var.setEnabled(true);
						addr_reg_hex_var.setEnabled(false);
						
						addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_dec_var.setBackground(Color.WHITE);
						addr_reg_hex_var.setBackground(new Color(220, 220, 220));
						break;
						
					case "�������� �ּ� ( HEX )":
						addr_modbus_dec_var.setEnabled(false);
						addr_reg_dec_var.setEnabled(false);
						addr_reg_hex_var.setEnabled(true);
						
						addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_hex_var.setBackground(Color.WHITE);
						break;
				}
							
			}						
		};
		
		addr_modbus_dec = new JRadioButton(" ������ �ּ� ( DEC )");
		addr_modbus_dec.setSelected(false);
		addr_modbus_dec.setFocusPainted(false);
		addr_modbus_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_modbus_dec.setForeground(Color.BLACK);
		addr_modbus_dec.setFont(FontManager.getFont(Font.BOLD, 17));
		addr_modbus_dec.setBackground(new Color(220, 220, 220));
		addr_modbus_dec.setBounds(12, 60, 235, 30);
		addr_modbus_dec.addActionListener(radioListener);
		panel.add(addr_modbus_dec);
		
		addr_reg_dec = new JRadioButton(" �������� �ּ� ( DEC )");
		addr_reg_dec.setSelected(true);
		addr_reg_dec.setFocusPainted(false);
		addr_reg_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_dec.setForeground(Color.BLACK);
		addr_reg_dec.setFont(FontManager.getFont(Font.BOLD, 17));
		addr_reg_dec.setBackground(new Color(220, 220, 220));
		addr_reg_dec.setBounds(12, 110, 235, 30);
		addr_reg_dec.addActionListener(radioListener);
		panel.add(addr_reg_dec);
		
		addr_reg_hex = new JRadioButton(" �������� �ּ� ( HEX )");
		addr_reg_hex.setSelected(false);
		addr_reg_hex.setFocusPainted(false);
		addr_reg_hex.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_hex.setForeground(Color.BLACK);
		addr_reg_hex.setFont(FontManager.getFont(Font.BOLD, 17));
		addr_reg_hex.setBackground(new Color(220, 220, 220));
		addr_reg_hex.setBounds(12, 160, 235, 30);
		addr_reg_hex.addActionListener(radioListener);
		panel.add(addr_reg_hex);
		
		ButtonGroup group = new ButtonGroup();
		group.add(addr_modbus_dec);
		group.add(addr_reg_dec);
		group.add(addr_reg_hex);
		
		JLabel cnt = new JLabel("����Ʈ �߰� ����");
		cnt.setHorizontalAlignment(SwingConstants.LEFT);
		cnt.setForeground(Color.BLACK);
		cnt.setFont(FontManager.getFont(Font.BOLD, 17));
		cnt.setBackground(Color.WHITE);
		cnt.setBounds(35, 210, 212, 30);
		panel.add(cnt);
		
		JLabel dataType = new JLabel("������ Ÿ��");
		dataType.setHorizontalAlignment(SwingConstants.LEFT);
		dataType.setForeground(Color.BLACK);
		dataType.setFont(FontManager.getFont(Font.BOLD, 17));
		dataType.setBackground(Color.WHITE);
		dataType.setBounds(35, 265, 212, 30);
		panel.add(dataType);
		
		fc_var = new JComboBox();
		fc_var.setBorder(new LineBorder(Color.BLACK, 2));
		fc_var.setModel(new DefaultComboBoxModel(new String[] {"FC 01", "FC 02", "FC 03", "FC 04"}));
		fc_var.setForeground(Color.BLACK);
		fc_var.setFont(FontManager.getFont(Font.BOLD, 17));
		fc_var.setBackground(Color.WHITE);
		fc_var.setBounds(263, 10, 190, 30);
		fc_var.setSelectedIndex(2);
		fc_var.addMouseWheelListener(SwingUtil.getComboBoxWheelListener());
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
				getPointList(false);
			}
		});		
		panel.add(fc_var);
		
		addr_modbus_dec_var = new JTextField();
		addr_modbus_dec_var.setEnabled(false);
		addr_modbus_dec_var.setForeground(Color.BLUE);
		addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
		addr_modbus_dec_var.setDisabledTextColor(Color.BLUE);
		addr_modbus_dec_var.setBorder(new LineBorder(Color.BLACK, 2));
		addr_modbus_dec_var.setHorizontalAlignment(SwingConstants.LEFT);		
		addr_modbus_dec_var.setFont(FontManager.getFont(Font.BOLD, 17));
		addr_modbus_dec_var.setColumns(10);
		addr_modbus_dec_var.setBounds(263, 60, 190, 30);
		addr_modbus_dec_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
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
		addr_reg_dec_var.setFont(FontManager.getFont(Font.BOLD, 17));
		addr_reg_dec_var.setColumns(10);		
		addr_reg_dec_var.setBounds(263, 110, 190, 30);
		addr_reg_dec_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
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
		addr_reg_hex_var.setFont(FontManager.getFont(Font.BOLD, 17));		
		addr_reg_hex_var.setBounds(265, 160, 188, 30);
		addr_reg_hex_var.setColumns(10);
		addr_reg_hex_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					syncAddr();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(addr_reg_hex_var);
		
		
		cnt_var = new JTextField();
		cnt_var.setBorder(new LineBorder(Color.BLACK, 2));
		cnt_var.setHorizontalAlignment(SwingConstants.LEFT);
		cnt_var.setForeground(Color.BLUE);
		cnt_var.setBackground(Color.WHITE);
		cnt_var.setFont(FontManager.getFont(Font.BOLD, 17));
		cnt_var.setColumns(10);
		cnt_var.setBounds(263, 210, 190, 30);
		cnt_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					checkRequestCount();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					checkRequestCount();
					getPointList(false);
				}catch(Exception ex) {
					ex.printStackTrace();					
				}
			}
		});
		panel.add(cnt_var);
		
		dataType_var = new JComboBox();
		dataType_var.setBorder(new LineBorder(Color.BLACK, 2));
		dataType_var.setForeground(Color.BLACK);
		dataType_var.setFont(FontManager.getFont(Font.BOLD, 17));
		dataType_var.setBackground(Color.WHITE);
		dataType_var.setBounds(263, 265, 401, 30);
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
		dataType_var.addMouseWheelListener(SwingUtil.getPassNullComboBoxWheelListener());
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
				
				if(incrementAddr.isSelected()) {
					addrStep.setText(String.format("<html>%s&nbsp;&nbsp;&nbsp;&nbsp;( �ּ� ���� ���� �ּҺ��� %s �� ���� )</html>", Util.colorBlue("" + step), Util.colorBlue("" + step)));	
				}else {
					addrStep.setText(String.format("<html>%s&nbsp;&nbsp;&nbsp;( ������ �ּ� ��� )</html>", Util.colorRed("�ּ� ���� ������� ����"), Util.colorBlue("" + step)));
				}
				
				getPointList(false);
			}
		});		
		panel.add(dataType_var);
		
		JLabel addrIncrement = new JLabel("�ּ� ������");
		addrIncrement.setHorizontalAlignment(SwingConstants.LEFT);
		addrIncrement.setForeground(Color.BLACK);
		addrIncrement.setFont(FontManager.getFont(Font.BOLD, 17));
		addrIncrement.setBackground(Color.WHITE);
		addrIncrement.setBounds(35, 315, 212, 30);
		panel.add(addrIncrement);
		
		addrStep = new JLabel(String.format("<html>%s&nbsp;&nbsp;&nbsp;&nbsp;( �ּ� ���� ���� �ּҺ��� %s �� ���� )</html>", Util.colorBlue("1"), Util.colorBlue("1")));
		addrStep.setHorizontalAlignment(SwingConstants.LEFT);
		addrStep.setForeground(Color.BLACK);
		addrStep.setFont(FontManager.getFont(Font.BOLD, 17));
		addrStep.setBackground(Color.WHITE);
		addrStep.setBounds(263, 315, 401, 30);
		panel.add(addrStep);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(12, 358, 718, 7);
		panel.add(separator);
		
		label = new JLabel("�� ��");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setForeground(Color.BLACK);
		label.setFont(FontManager.getFont(Font.BOLD, 17));
		label.setBackground(Color.WHITE);
		label.setBounds(35, 372, 212, 30);
		panel.add(label);
		
		con_0 = new JLabel("��� �ڵ�");
		con_0.setHorizontalAlignment(SwingConstants.LEFT);
		con_0.setForeground(Color.BLACK);
		con_0.setFont(FontManager.getFont(Font.BOLD, 17));
		con_0.setBackground(Color.WHITE);
		con_0.setBounds(35, 422, 645, 30);
		panel.add(con_0);
		
		con_1 = new JLabel("������ �ּ� ( DEC )");
		con_1.setHorizontalAlignment(SwingConstants.LEFT);
		con_1.setForeground(Color.BLACK);
		con_1.setFont(FontManager.getFont(Font.BOLD, 17));
		con_1.setBackground(Color.WHITE);
		con_1.setBounds(35, 462, 645, 30);
		panel.add(con_1);
		
		con_2 = new JLabel("�������� �ּ� ( DEC )");
		con_2.setHorizontalAlignment(SwingConstants.LEFT);
		con_2.setForeground(Color.BLACK);
		con_2.setFont(FontManager.getFont(Font.BOLD, 17));
		con_2.setBackground(Color.WHITE);
		con_2.setBounds(35, 502, 645, 30);
		panel.add(con_2);
		
		con_3 = new JLabel("�������� �ּ� ( HEX )");
		con_3.setHorizontalAlignment(SwingConstants.LEFT);
		con_3.setForeground(Color.BLACK);
		con_3.setFont(FontManager.getFont(Font.BOLD, 17));
		con_3.setBackground(Color.WHITE);
		con_3.setBounds(35, 542, 645, 30);
		panel.add(con_3);
		
		con_4 = new JLabel("������ ����Ʈ �߰� ����");
		con_4.setHorizontalAlignment(SwingConstants.LEFT);
		con_4.setForeground(Color.BLACK);
		con_4.setFont(FontManager.getFont(Font.BOLD, 17));
		con_4.setBackground(Color.WHITE);
		con_4.setBounds(35, 582, 645, 30);
		panel.add(con_4);
		
		con_5 = new JLabel("( ���� ���� )");
		con_5.setHorizontalAlignment(SwingConstants.LEFT);
		con_5.setForeground(new Color(0, 128, 0));
		con_5.setFont(FontManager.getFont(Font.BOLD, 17));
		con_5.setBackground(Color.WHITE);
		con_5.setBounds(35, 622, 695, 30);
		panel.add(con_5);
		
		addButton = new JButton();
		addButton.setBounds(668, 10, 84, 32);
		addButton.setText("�� ��");
		addButton.setForeground(new Color(0, 128, 0));
		addButton.setBackground(Color.WHITE);
		addButton.setFont(FontManager.getFont(Font.BOLD, 16));
		addButton.setFocusPainted(false);		
		addButton.setBorder(UIManager.getBorder("Button.border"));
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkFormValidation()) {
					try {
						ArrayList<ModbusWatchPoint> pointList = getPointList(true);
						
						if(pointList != null) {
							ModbusMonitor_Panel.addPointList(pointList);
							ModbusMonitor_Panel.doTableFilter(false);
							ExportModbusPointFrame.updateTable();
							
							StringBuilder sb = new StringBuilder();
							sb.append(String.format("%s%s%s\n", Util.colorGreen("Modbus Point Added Successfully"), Util.separator, Util.separator));
							sb.append(String.format("������ ����Ʈ %s�� �׸��� �߰� �Ϸ��Ͽ����ϴ�", Util.colorBlue(cnt_var.getText())));
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
		
		showContentButton = new JButton();
		showContentButton.setMargin(new Insets(2, 0, 2, 0));
		showContentButton.setText("���� ����");
		showContentButton.setForeground(Color.BLACK);
		showContentButton.setFont(FontManager.getFont(Font.BOLD, 16));
		showContentButton.setFocusPainted(false);
		showContentButton.setBorder(UIManager.getBorder("Button.border"));
		showContentButton.setBackground(Color.WHITE);
		showContentButton.setBounds(458, 10, 102, 32);
		showContentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = showContentButton.getText().trim();
				
				if(text.contains("����")) {
					showContent(true);	
					showContentButton.setText("���� �ݱ�");
				}else {
					showContent(false);
					showContentButton.setText("���� ����");
				}
				
			}
		});
		actualPanel.add(showContentButton);
		
		
		resetButton = new JButton();
		resetButton.setBounds(565, 10, 97, 32);
		resetButton.setText("�ʱ�ȭ");
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 16));
		resetButton.setFocusPainted(false);		
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetForm();
			}
		});
		actualPanel.add(resetButton);
		
		incrementAddr = new JCheckBox(" �ּ� ����");
		incrementAddr.setSelected(true);
		incrementAddr.setFocusPainted(false);
		incrementAddr.setBackground(Color.WHITE);
		incrementAddr.setFont(FontManager.getFont(Font.BOLD, 17));
		incrementAddr.setForeground(Color.BLACK);
		incrementAddr.setBounds(332, 10, 118, 32);
		incrementAddr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPointList(false);
				dataType_var.setSelectedIndex(dataType_var.getSelectedIndex());
			}
		});
		actualPanel.add(incrementAddr);
		
		addKeyAdapter();
		
		// �������� ȭ�� ������� �����ȴ�
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
//				addr_modbus_dec_var.setText("��ȿ���� ���� �ּ�");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_dec_var.setText("��ȿ���� ���� �ּ�");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_hex_var.setText("��ȿ���� ���� �ּ�");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
				
			}else if(addr_reg_dec.isSelected()) {
				addr_modbus_dec_var.setText("��ȿ���� ���� �ּ�");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
//				addr_reg_dec_var.setText("��ȿ���� ���� �ּ�");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_hex_var.setText("��ȿ���� ���� �ּ�");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
				
			}else {
				addr_modbus_dec_var.setText("��ȿ���� ���� �ּ�");
				addr_modbus_dec_var.setForeground(Color.RED);
				addr_modbus_dec_var.setDisabledTextColor(Color.RED);
				
				addr_reg_dec_var.setText("��ȿ���� ���� �ּ�");				
				addr_reg_dec_var.setForeground(Color.RED);
				addr_reg_dec_var.setDisabledTextColor(Color.RED);
				
//				addr_reg_hex_var.setText("��ȿ���� ���� �ּ�");
				addr_reg_hex_var.setForeground(Color.RED);
				addr_reg_hex_var.setDisabledTextColor(Color.RED);
			}
			
			return -1;
		}
		
	}
	
	public boolean checkRequestCount() {
		try {
			int count = Integer.parseInt(cnt_var.getText().trim());
			
			if(count < 1 || count > 10000) {
				cnt_var.setForeground(Color.RED);
				return false;
			}else {
				cnt_var.setForeground(Color.BLUE);
				return true;
			}
			
		}catch(Exception e) {
			cnt_var.setForeground(Color.RED);
			return false;
		}
	}
	
	public void resetForm() {
		incrementAddr.setSelected(true);
		fc_var.setSelectedIndex(2);
		addr_modbus_dec_var.setText(null);
		addr_reg_dec_var.setText(null);
		addr_reg_hex_var.setText(null);
		cnt_var.setText(null);
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
		formValid = formValid && !(addr_modbus_dec_var.getText().trim().equals("��ȿ���� ���� �ּ�"));
		formValid = formValid && !(addr_reg_dec_var.getText().trim().equals("��ȿ���� ���� �ּ�"));
		formValid = formValid && !(addr_reg_hex_var.getText().trim().equals("��ȿ���� ���� �ּ�"));
		if(!formValid) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "�߰��Ͻ� ������ ����Ʈ�� ���� " + Util.colorBlue("�ּ�(Address)") +  " ������ Ȯ�����ּ���"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			
			if(addr_modbus_dec.isSelected()) {
				addr_modbus_dec_var.requestFocus();
			}else if(addr_reg_dec.isSelected()) {
				addr_reg_dec_var.requestFocus();
			}else {
				addr_reg_hex_var.requestFocus();
			}
			
			return formValid;
		}
		
		formValid = formValid && !(cnt_var.getText().length() < 1 || cnt_var.getText().equals(""));
		formValid = formValid && !(cnt_var.getForeground() == Color.RED);
		if(!formValid) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "�߰��Ͻ� ������ ����Ʈ " + Util.colorBlue("����(Count)") + " ������ Ȯ�����ּ���"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return formValid;
		}
		
		return formValid;
	}
	
	public void showContent(boolean enabled) {
		if(enabled) {
			this.setSize(new Dimension(790, 785));
			panel.setSize(new Dimension(742, 661));
		}else {
			this.setSize(new Dimension(790, 484));
			panel.setSize(new Dimension(742, 358));
		}
	}
	
	public ArrayList<ModbusWatchPoint> getPointList(boolean get){
		if(incrementAddr.isSelected()) {
			return getIncrementAddrPointList(get);
		}else{
			return getSameAddrPointList(get);
		}
	}
	
	public ArrayList<ModbusWatchPoint> getIncrementAddrPointList(boolean get) {
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
			int addr = syncAddr();
			if(addr < 0) throw new Exception();
			if(!checkRequestCount()) throw new Exception();
			int count = Integer.parseInt(cnt_var.getText().trim());
			
			int[] addrArray = new int[count];
			addrArray[0] = addr;
			
			for(int i = 1; i < addrArray.length; i++) {
				addrArray[i] = addrArray[i-1] + step;
			}
			
			con_0.setText(String.format("<html>����ڵ� %s��, ������ Ÿ�� %s ��(��) ����ϰ�</html>", Util.colorBlue("" + fc), Util.colorBlue(dataType)));
			con_1.setText(String.format("<html>������ �ּ� ( DEC ) : %s ~ %s</html>", Util.colorBlue(getModbusAddr(fc, addrArray[0])), Util.colorBlue(getModbusAddr(fc, addrArray[count-1]))));
			con_2.setText(String.format("<html>�������� �ּ� ( DEC ) : %s ~ %s</html>", Util.colorBlue("" + addrArray[0]), Util.colorBlue("" + addrArray[count-1])));
			con_3.setText(String.format("<html>�������� �ּ� ( HEX ) : %s ~ %s</html>", Util.colorBlue(getRegisterAddrHex(addrArray[0])), Util.colorBlue(getRegisterAddrHex(addrArray[count-1]))));
			con_4.setText(String.format("<html>�ּ� ���� ���� �ּҺ��� %s�� �����Ǵ� %s���� ������ ����Ʈ �߰�</html>", Util.colorBlue("" + step), Util.colorBlue("" + count)));
			con_5.setText(String.format("<html>( ���� �� ���� �ּ� ����� ǥ�� ����� �ٸ� ��, �� ���� ��� ��� ������ �����Դϴ� )</html>"));
			
			if(!get) return null;
			
			ArrayList<ModbusWatchPoint> list = new ArrayList<ModbusWatchPoint>();
			for(int i = 0; i < addrArray.length; i++) {
				ModbusWatchPoint wp = new ModbusWatchPoint();
				wp.displayName = "";
				wp.scaleFunc = "x";
				wp.interval = 60;
				wp.measure = "";
				wp.dataFormat = 3;
				String counter = fc + "_" + getRegisterAddrHex(addrArray[i]) + "_" + dataType;
				wp.setCounter(counter);
				wp.init();
				list.add(wp);
			}
			
			return list;
		}catch(Exception ex) {			
			con_0.setText("��� �ڵ�");
			con_1.setText("������ �ּ� ( DEC )");
			con_2.setText("�������� �ּ� ( DEC )");
			con_3.setText("�������� �ּ� ( HEX )");
			con_4.setText("������ ����Ʈ �߰� ����");
			con_5.setText("( ���� ���� )");
			return null;
		}
	}
	
	public ArrayList<ModbusWatchPoint> getSameAddrPointList(boolean get) {
		int fc = Integer.parseInt(fc_var.getSelectedItem().toString().split(" ")[1]);
		
		String dataType = dataType_var.getSelectedItem().toString().toUpperCase().trim();
		
		try {
			int addr = syncAddr();
			if(addr < 0) throw new Exception();
			if(!checkRequestCount()) throw new Exception();
			int count = Integer.parseInt(cnt_var.getText().trim());
			
			int[] addrArray = new int[count];
			
			for(int i = 0; i < addrArray.length; i++) {
				addrArray[i] = addr;
			}
			
			con_0.setText(String.format("<html>����ڵ� %s��, ������ Ÿ�� %s ��(��) ����ϰ�</html>", Util.colorBlue("" + fc), Util.colorBlue(dataType)));
			con_1.setText(String.format("<html>������ �ּ� ( DEC ) : %s</html>", Util.colorBlue(getModbusAddr(fc, addr))));
			con_2.setText(String.format("<html>�������� �ּ� ( DEC ) : %s</html>", Util.colorBlue("" + addr)));
			con_3.setText(String.format("<html>�������� �ּ� ( HEX ) : %s</html>", Util.colorBlue(getRegisterAddrHex(addr))));
			con_4.setText(String.format("<html>������ �ּҸ� ����ϴ� %s���� ������ ����Ʈ �߰�</html>", Util.colorBlue("" + count)));
			con_5.setText(String.format("<html>( ���� �� ���� �ּ� ����� ǥ�� ����� �ٸ� ��, �� ���� ��� ��� ������ �����Դϴ� )</html>"));
			
			if(!get) return null;
			
			ArrayList<ModbusWatchPoint> list = new ArrayList<ModbusWatchPoint>();
			for(int i = 0; i < addrArray.length; i++) {
				ModbusWatchPoint wp = new ModbusWatchPoint();
				wp.displayName = "";
				wp.scaleFunc = "x";
				wp.interval = 60;
				wp.measure = "";
				wp.dataFormat = 3;
				String counter = fc + "_" + getRegisterAddrHex(addrArray[i]) + "_" + dataType;
				wp.setCounter(counter);
				wp.init();
				list.add(wp);
			}
			
			return list;
		}catch(Exception ex) {			
			con_0.setText("��� �ڵ�");
			con_1.setText("������ �ּ� ( DEC )");
			con_2.setText("�������� �ּ� ( DEC )");
			con_3.setText("�������� �ּ� ( HEX )");
			con_4.setText("������ ����Ʈ �߰� ����");
			con_5.setText("( ���� ���� )");
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
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Add Modbus Watch Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Add Modbus Watch Point �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public void addKeyAdapter() {
		if(saveAndCloseAdpter == null) {
			saveAndCloseAdpter = new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					try {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							dispose();					
						}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {					
							addButton.doClick();
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			};
		}
		
		fc_var.addKeyListener(saveAndCloseAdpter);
		addr_modbus_dec.addKeyListener(saveAndCloseAdpter);	
		addr_reg_dec.addKeyListener(saveAndCloseAdpter);
		addr_reg_hex.addKeyListener(saveAndCloseAdpter);
		addr_modbus_dec_var.addKeyListener(saveAndCloseAdpter);
		addr_reg_dec_var.addKeyListener(saveAndCloseAdpter);
		addr_reg_hex_var.addKeyListener(saveAndCloseAdpter);
		cnt_var.addKeyListener(saveAndCloseAdpter);
		dataType_var.addKeyListener(saveAndCloseAdpter);
	}
	
	@Override
	public void dispose() {
		AddFormModbusPointFrame.isExist = false;
		super.dispose();
	}
}

