package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import src_ko.util.Util;

public class AddFormModbusPointFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable table; // frame마다 XML 인스턴스를 가져야 하므로 table 필드는 static 속성을 가질 수 없다
	private JComboBox fc_var;	
	private JComboBox dataType_var;
	private JRadioButton addr_reg_hex;
	private JRadioButton addr_reg_dec;
	private JRadioButton addr_modbus_dec;	
	private JTextField addr_reg_hex_var;
	private JTextField addr_reg_dec_var;
	private JTextField addr_modbus_dec_var;
	private JTextField cnt_var;

	private JButton addButton;
	private JButton resetButton;
	private ActionListener radioListener;	
	
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
				
		setBounds(100, 100, 720, 434);
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
		currentFunction.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 277, 55);
		actualPanel.add(currentFunction);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(220, 220, 220));
		panel.setBounds(10, 65, 672, 310);
		actualPanel.add(panel);
		panel.setLayout(null);
		
		JLabel fc = new JLabel("Function Code");
		fc.setBounds(35, 10, 198, 30);
		panel.add(fc);
		fc.setHorizontalAlignment(SwingConstants.LEFT);
		fc.setForeground(Color.BLACK);
		fc.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc.setBackground(Color.WHITE);
		
		radioListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				JRadioButton b = (JRadioButton)e.getSource();
				
				switch (b.getText()) {					
					case "Modbus Address ( DEC )":
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
		
					case "Register Address ( DEC )":
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
						
					case "Register Address ( HEX )":
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
		
		addr_modbus_dec = new JRadioButton("Modbus Address ( DEC )");
		addr_modbus_dec.setSelected(false);
		addr_modbus_dec.setFocusPainted(false);
		addr_modbus_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_modbus_dec.setForeground(Color.BLACK);
		addr_modbus_dec.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_modbus_dec.setBackground(new Color(220, 220, 220));
		addr_modbus_dec.setBounds(12, 60, 230, 30);
		addr_modbus_dec.addActionListener(radioListener);
		panel.add(addr_modbus_dec);
		
		addr_reg_dec = new JRadioButton("Register Address ( DEC )");
		addr_reg_dec.setSelected(true);
		addr_reg_dec.setFocusPainted(false);
		addr_reg_dec.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_dec.setForeground(Color.BLACK);
		addr_reg_dec.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_reg_dec.setBackground(new Color(220, 220, 220));
		addr_reg_dec.setBounds(12, 110, 230, 30);
		addr_reg_dec.addActionListener(radioListener);
		panel.add(addr_reg_dec);
		
		addr_reg_hex = new JRadioButton("Register Address ( HEX )");
		addr_reg_hex.setSelected(false);
		addr_reg_hex.setFocusPainted(false);
		addr_reg_hex.setHorizontalAlignment(SwingConstants.LEFT);
		addr_reg_hex.setForeground(Color.BLACK);
		addr_reg_hex.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_reg_hex.setBackground(new Color(220, 220, 220));
		addr_reg_hex.setBounds(12, 160, 230, 30);
		addr_reg_hex.addActionListener(radioListener);
		panel.add(addr_reg_hex);
		
		ButtonGroup group = new ButtonGroup();
		group.add(addr_modbus_dec);
		group.add(addr_reg_dec);
		group.add(addr_reg_hex);
		
		JLabel cnt = new JLabel("Count");
		cnt.setHorizontalAlignment(SwingConstants.LEFT);
		cnt.setForeground(Color.BLACK);
		cnt.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		cnt.setBackground(Color.WHITE);
		cnt.setBounds(35, 210, 198, 30);
		panel.add(cnt);
		
		JLabel dataType = new JLabel("Data Type");
		dataType.setHorizontalAlignment(SwingConstants.LEFT);
		dataType.setForeground(Color.BLACK);
		dataType.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType.setBackground(Color.WHITE);
		dataType.setBounds(35, 265, 198, 30);
		panel.add(dataType);
		
		fc_var = new JComboBox();
		fc_var.setBorder(new LineBorder(Color.BLACK, 2));
		fc_var.setModel(new DefaultComboBoxModel(new String[] {"FC 01", "FC 02", "FC 03", "FC 04"}));
		fc_var.setForeground(Color.BLACK);
		fc_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc_var.setBackground(Color.WHITE);
		fc_var.setBounds(263, 10, 190, 30);
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
		addr_modbus_dec_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		addr_modbus_dec_var.setColumns(10);
		addr_modbus_dec_var.setBounds(263, 60, 190, 30);
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
		addr_reg_dec_var.setBounds(263, 110, 190, 30);
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
		addr_reg_hex_var.setBounds(265, 160, 188, 30);
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
		
		
		cnt_var = new JTextField();
		cnt_var.setBorder(new LineBorder(Color.BLACK, 2));
		cnt_var.setHorizontalAlignment(SwingConstants.LEFT);
		cnt_var.setForeground(Color.BLUE);
		cnt_var.setBackground(Color.WHITE);
		cnt_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		cnt_var.setColumns(10);
		cnt_var.setBounds(263, 210, 190, 30);
		cnt_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					checkRequestCount();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					checkRequestCount();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(cnt_var);
		
		dataType_var = new JComboBox();
		dataType_var.setBorder(new LineBorder(Color.BLACK, 2));
		dataType_var.setForeground(Color.BLACK);
		dataType_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
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
		panel.add(dataType_var);
		
		addButton = new JButton();
		addButton.setBounds(585, 10, 97, 32);
		addButton.setText("추 가");
		addButton.setForeground(Color.BLACK);
		addButton.setBackground(Color.WHITE);
		addButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		addButton.setFocusPainted(false);		
		addButton.setBorder(UIManager.getBorder("Button.border"));
		actualPanel.add(addButton);
		
		resetButton = new JButton();
		resetButton.setBounds(476, 10, 97, 32);
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
	}
	
	public void syncAddr() {
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
				return;
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
				return;
			}
			startAddress = Integer.parseInt(addr);
			if(startAddress > 0xffff || startAddress < 0) throw new NumberFormatException();
			
		}else {
			addr = addr_reg_hex_var.getText().trim();
			if(addr.length() < 1 || addr.equals("")) {
				addr_modbus_dec_var.setText(null);
				addr_reg_dec_var.setText(null);
				addr_reg_hex_var.setText(null);
				return;
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
		}
		
	}
	
	public void checkRequestCount() {
		try {
			int count = Integer.parseInt(cnt_var.getText().trim());
			
			if(count < 1) {
				cnt_var.setForeground(Color.RED);
			}else {
				cnt_var.setForeground(Color.BLUE);	
			}
			
		}catch(Exception e) {
			cnt_var.setForeground(Color.RED);
		}
	}
	
	public void resetForm() {
		fc_var.setSelectedIndex(2);
		addr_modbus_dec_var.setText(null);
		addr_reg_dec_var.setText(null);
		addr_reg_hex_var.setText(null);
		cnt_var.setText(null);
		dataType_var.setSelectedIndex(0);
	}

	@Override
	public void dispose() {
		AddFormModbusPointFrame.isExist = false;
		super.dispose();
	}
}

