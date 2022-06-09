package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
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

import common.modbus.ModbusWatchPoint;
import common.perf.PerfLabelStatusBean;
import common.util.TableUtil;
import src_ko.agent.Perf;
import src_ko.util.JavaScript;
import src_ko.util.Util;

public class ModifyModbusWatchPointFrame extends JFrame {

	public static boolean isExist = false;
	
	private static ArrayList<ModbusWatchPoint> pointList = null;
	private ModbusWatchPoint selectedPoint = null;
	
	private JPanel contentPane;
	private JPanel panel;
	
	private JButton modifyOneButton;
	private JButton modifyAllButton;
	private JButton resetButton;
	private ActionListener radioListener;
	
	private JButton addRow;
	private JButton deleteRow;
	
	// 레이블
	private JLabel pointName;
	private JLabel fc;
	private JRadioButton addr_modbus_dec;
	private JRadioButton addr_reg_dec;
	private JRadioButton addr_reg_hex;
	private JLabel dataType;
	private JLabel measure;
	private JLabel scale;
	private JLabel dataFormat;
	private JLabel statusLabel;
	
	// 필드
	private JTextField pointName_var;
	private JComboBox fc_var;	
	private JTextField addr_modbus_dec_var;
	private JTextField addr_reg_dec_var;
	private JTextField addr_reg_hex_var;
	private JComboBox dataType_var;
	private JTextField measure_var;
	private JTextField scale_var;
	private JComboBox dataFormat_var;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton autoMeasureButton;
	private JButton bitOperationButton;
	
	// 체크 박스
	private JCheckBox c_pointName;
	private JCheckBox c_fc;
	private JCheckBox c_addr;
	private JCheckBox c_dataType;
	private JCheckBox c_measure;
	private JCheckBox c_scale;
	private JCheckBox c_dataForamt;
	private JButton showPointList;
	
	private int frameWidth = 790;
	private int frameHeight = 610;
	private int panelWidth = 742;
	private int panelHeight = 437;
	
	private JScrollPane pointScrollPane;
	private static JTable pointTable;
	private JLabel searchLabel;
	private static JTextField search_TextField;	
	private JButton addModifyPoint;
	private JButton deleteModifyPoint;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyModbusWatchPointFrame frame = new ModifyModbusWatchPointFrame(null);
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
	public ModifyModbusWatchPointFrame(ArrayList<ModbusWatchPoint> modifyPointList) {
		this.pointList = modifyPointList;
		if(modifyPointList != null && modifyPointList.size() > 0) {
			this.selectedPoint = modifyPointList.get(0);
		}
		ModifyModbusWatchPointFrame.isExist = true;
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
		
		setBounds(100, 100, 1285, 610);
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
		panel.setBounds(10, 114, panelWidth, panelHeight);
		panel.setLayout(null);
		actualPanel.add(panel);
		
		pointName = new JLabel("포인트 이름");
		pointName.setHorizontalAlignment(SwingConstants.LEFT);
		pointName.setForeground(Color.BLACK);
		pointName.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		pointName.setBackground(Color.WHITE);
		pointName.setBounds(31, 15, 212, 30);
		panel.add(pointName);
		
		fc = new JLabel("기능 코드");
		fc.setBounds(31, 65, 212, 30);
		fc.setHorizontalAlignment(SwingConstants.LEFT);
		fc.setForeground(Color.BLACK);
		fc.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		fc.setBackground(Color.WHITE);
		panel.add(fc);
		
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
						break;
		
					case "레지스터 주소 ( DEC )" :
						addr_modbus_dec_var.setEnabled(false);
						addr_reg_dec_var.setEnabled(true);
						addr_reg_hex_var.setEnabled(false);
						
						addr_modbus_dec_var.setBackground(new Color(220, 220, 220));
						addr_reg_dec_var.setBackground(Color.WHITE);
						addr_reg_hex_var.setBackground(new Color(220, 220, 220));
						break;
						
					case "레지스터 주소 ( HEX )":
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
		
		dataType = new JLabel("데이터 타입");
		dataType.setHorizontalAlignment(SwingConstants.LEFT);
		dataType.setForeground(Color.BLACK);
		dataType.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataType.setBackground(Color.WHITE);
		dataType.setBounds(31, 245, 212, 30);
		panel.add(dataType);
		
		measure = new JLabel("측정 단위");
		measure.setHorizontalAlignment(SwingConstants.LEFT);
		measure.setForeground(Color.BLACK);
		measure.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		measure.setBackground(Color.WHITE);
		measure.setBounds(31, 295, 212, 30);
		panel.add(measure);
		
		scale = new JLabel("보정식");
		scale.setHorizontalAlignment(SwingConstants.LEFT);
		scale.setForeground(Color.BLACK);
		scale.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		scale.setBackground(Color.WHITE);
		scale.setBounds(31, 345, 212, 30);
		panel.add(scale);
		
		dataFormat = new JLabel("데이터 형식");
		dataFormat.setHorizontalAlignment(SwingConstants.LEFT);
		dataFormat.setForeground(Color.BLACK);
		dataFormat.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataFormat.setBackground(Color.WHITE);
		dataFormat.setBounds(31, 395, 212, 30);
		panel.add(dataFormat);
		
		
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
		
		fc_var = new JComboBox();
		fc_var.setBorder(new LineBorder(Color.BLACK, 2));
		fc_var.setModel(new DefaultComboBoxModel(
				new String[] {
						"FC 01", 
						"FC 02", 
						"FC 03", 
						"FC 04"
						}));
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
			}
		});
		panel.add(dataType_var);
		
		measure_var = new JTextField();
		measure_var.setText(null);
		measure_var.setHorizontalAlignment(SwingConstants.LEFT);
		measure_var.setForeground(Color.BLUE);
		measure_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		measure_var.setEnabled(true);
		measure_var.setColumns(10);
		measure_var.setBorder(new LineBorder(Color.BLACK, 2));
		measure_var.setBackground(Color.WHITE);
		measure_var.setBounds(259, 295, 190, 30);
		panel.add(measure_var);
		
		scale_var = new JTextField();
		scale_var.setHorizontalAlignment(SwingConstants.LEFT);
		scale_var.setForeground(Color.BLUE);
		scale_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		scale_var.setEnabled(true);
		scale_var.setColumns(10);
		scale_var.setBorder(new LineBorder(Color.BLACK, 2));
		scale_var.setBackground(Color.WHITE);
		scale_var.setBounds(259, 345, 190, 30);
		scale_var.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					
					String scale = scale_var.getText().trim();
					try {
						JavaScript.eval(scale, "1");
						scale_var.setForeground(Color.BLUE);
					}catch(Exception ex) {
						scale_var.setForeground(Color.RED);
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				try {
					
					String scale = scale_var.getText().trim();
					try {
						JavaScript.eval(scale, "1");
						scale_var.setForeground(Color.BLUE);
					}catch(Exception ex) {
						scale_var.setForeground(Color.RED);
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(scale_var);
		
		dataFormat_var = new JComboBox();
		dataFormat_var.setForeground(Color.BLACK);
		dataFormat_var.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		dataFormat_var.setBorder(new LineBorder(Color.BLACK, 2));
		dataFormat_var.setBackground(Color.WHITE);
		dataFormat_var.setBounds(259, 395, 401, 30);
		dataFormat_var.setModel(new DefaultComboBoxModel(
				new String[] {
						"1 ( 이진 상태 )",
						"2 ( 다중 상태 )",
						"3 ( 아날로그 데이터 )"
						}));
		dataFormat_var.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int dataFormat = Integer.parseInt(dataFormat_var.getSelectedItem().toString().split(" ")[0].trim());
				Dimension frameSize = null;
				Dimension panelSize = null;
				
				switch(dataFormat) {
				case 1 :
					statusLabel.setText("이진 상태");
					frameHeight = 764;
					panelHeight = 590;
					frameSize = new Dimension(frameWidth, frameHeight);
					panelSize = new Dimension(panelWidth, panelHeight);
					scrollPane.setSize(677, 80);
					pointScrollPane.setSize(482, panelHeight);
					break;
					
				case 2 :
					statusLabel.setText("다중 상태");
					frameHeight = 831;
					panelHeight = 658;
					frameSize = new Dimension(frameWidth, frameHeight);
					panelSize = new Dimension(panelWidth, panelHeight);
					scrollPane.setSize(677, 155);
					pointScrollPane.setSize(482, panelHeight);
					break;
					
				case 3 :
					statusLabel.setText("아날로그 데이터");
					frameHeight = 610;
					panelHeight = 437;
					frameSize = new Dimension(frameWidth, frameHeight);
					panelSize = new Dimension(panelWidth, panelHeight);
					pointScrollPane.setSize(482, panelHeight);
					break;
				}
				
				setFrameAndPanelSize(frameSize, panelSize);
				resetTable(table, dataFormat);
			}
		});
		panel.add(dataFormat_var);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(8, 441, 718, 7);
		panel.add(separator);
		
		statusLabel = new JLabel("이진 상태");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		statusLabel.setBackground(Color.WHITE);
		statusLabel.setBounds(27, 456, 212, 30);
		panel.add(statusLabel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(50, 496, 677, 80);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setCellSelectionEnabled(true);
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
		scrollPane.setViewportView(table);
		
		showPointList = new JButton();
		showPointList.setText("포인트 리스트 열기");
		showPointList.setForeground(Color.BLACK);
		showPointList.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		showPointList.setFocusPainted(false);
		showPointList.setBorder(UIManager.getBorder("Button.border"));
		showPointList.setBackground(Color.WHITE);
		showPointList.setBounds(347, 10, 190, 32);
		showPointList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(showPointList.getText().contains("열기")) {
					showPointList.setText("포인트 리스트 닫기");
					frameWidth = 1285;
					setSize(new Dimension(frameWidth, frameHeight));
					
				}else {
					showPointList.setText("포인트 리스트 열기");
					frameWidth = 790;
					setSize(new Dimension(frameWidth, frameHeight));
					
					
				}
			}
		});
		actualPanel.add(showPointList);
		
		modifyOneButton = new JButton();
		modifyOneButton.setBounds(647, 10, 105, 32);
		modifyOneButton.setText("단일 수정");
		modifyOneButton.setForeground(new Color(0, 128, 0));
		modifyOneButton.setBackground(Color.WHITE);
		modifyOneButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		modifyOneButton.setFocusPainted(false);		
		modifyOneButton.setBorder(UIManager.getBorder("Button.border"));
		modifyOneButton.addActionListener(new ActionListener() {
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
		actualPanel.add(modifyOneButton);
		
		resetButton = new JButton();
		resetButton.setBounds(542, 10, 100, 32);
		resetButton.setText("초기화");
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		resetButton.setFocusPainted(false);		
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initModbusPointInfo(selectedPoint);
			}
		});
		actualPanel.add(resetButton);
		
		addModifyPoint = new JButton();
		addModifyPoint.setText("추 가");
		addModifyPoint.setForeground(Color.BLACK);
		addModifyPoint.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		addModifyPoint.setFocusPainted(false);
		addModifyPoint.setBorder(UIManager.getBorder("Button.border"));
		addModifyPoint.setBackground(Color.WHITE);
		addModifyPoint.setBounds(962, 10, 85, 32);
		addModifyPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ModbusWatchPoint> pointList = getSelectedPoint(ModbusMonitor_Panel.pointTable);
				addPointList(pointList);
				doTableFilter();
			}
		});
		actualPanel.add(addModifyPoint);
		
		deleteModifyPoint = new JButton();
		deleteModifyPoint.setText("삭 제");
		deleteModifyPoint.setForeground(Color.BLACK);
		deleteModifyPoint.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		deleteModifyPoint.setFocusPainted(false);
		deleteModifyPoint.setBorder(UIManager.getBorder("Button.border"));
		deleteModifyPoint.setBackground(Color.WHITE);
		deleteModifyPoint.setBounds(1052, 10, 85, 32);
		deleteModifyPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ModbusWatchPoint> selectedPointList = getSelectedPoint(pointTable);

				if(selectedPointList == null || selectedPointList.size() < 1) {
					return;
					
				}else{
					
					for (ModbusWatchPoint wp : selectedPointList) {
						pointList.remove(wp);
					}
					
					doTableFilter();
				}
				
			}
		});
		actualPanel.add(deleteModifyPoint);
		
		modifyAllButton = new JButton();
		modifyAllButton.setText("일괄 수정");
		modifyAllButton.setForeground(Color.RED);
		modifyAllButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		modifyAllButton.setFocusPainted(false);
		modifyAllButton.setBorder(UIManager.getBorder("Button.border"));
		modifyAllButton.setBackground(Color.WHITE);
		modifyAllButton.setBounds(1142, 10, 105, 32);
		actualPanel.add(modifyAllButton);
		
		
		addRow = new JButton();
		addRow.setText("추 가");
		addRow.setForeground(Color.BLACK);
		addRow.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		addRow.setFocusPainted(false);
		addRow.setBorder(UIManager.getBorder("Button.border"));
		addRow.setBackground(Color.WHITE);
		addRow.setBounds(539, 454, 90, 32);
		addRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRecord(table);
			}
		});
		panel.add(addRow);
		
		deleteRow = new JButton();
		deleteRow.setText("삭 제");
		deleteRow.setForeground(Color.BLACK);
		deleteRow.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		deleteRow.setFocusPainted(false);
		deleteRow.setBorder(UIManager.getBorder("Button.border"));
		deleteRow.setBackground(Color.WHITE);
		deleteRow.setBounds(636, 454, 90, 32);
		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRow(table);
			}
		});
		panel.add(deleteRow);
		
		autoMeasureButton = new JButton();
		autoMeasureButton.setText("자동 생성");
		autoMeasureButton.setForeground(Color.BLACK);
		autoMeasureButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		autoMeasureButton.setFocusPainted(false);
		autoMeasureButton.setBorder(UIManager.getBorder("Button.border"));
		autoMeasureButton.setBackground(Color.WHITE);
		autoMeasureButton.setBounds(456, 295, 110, 32);
		autoMeasureButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pointName = pointName_var.getText().trim();
				
				if(pointName == null || pointName.length() < 1 || pointName.equals("")) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s%s%s\n", Util.colorRed("Unable to Resolve Point Name"), Util.separator, Util.separator));
					sb.append(String.format("%s", "모드버스 포인트의 " + Util.colorBlue("이름(Name)") +  " 정보를 입력해주세요"));
					sb.append(Util.separator + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					measure_var.setText(null);
					pointName_var.requestFocus();
					return;
				}else {
					String autoMeasure = Perf.createMeasure(pointName);
					
					if(autoMeasure == null || autoMeasure.length() < 1 || autoMeasure.equals("")) {
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s%s%s\n", Util.colorRed("Failed to Automatically Create Measure"), Util.separator, Util.separator));
						sb.append(String.format("%s", "현재 입력된 모드버스 포인트 이름에 적절한 " + Util.colorBlue("단위(Measure)") +  " 생성에 실패하였습니다"));
						sb.append(Util.separator + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						
						pointName_var.requestFocus();
						return;
					}
					
					measure_var.setText(autoMeasure);
					measure_var.requestFocus();
				}
				
			}
		});
		panel.add(autoMeasureButton);
		
		bitOperationButton = new JButton();
		bitOperationButton.setText("비트 연산");
		bitOperationButton.setForeground(Color.BLACK);
		bitOperationButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		bitOperationButton.setFocusPainted(false);
		bitOperationButton.setBorder(UIManager.getBorder("Button.border"));
		bitOperationButton.setBackground(Color.WHITE);
		bitOperationButton.setBounds(456, 345, 110, 32);
		bitOperationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String scale = scale_var.getText().trim();
				
				if(scale == null || scale.length() < 1 || scale.equals("")) {
					return;
				}else if(scale.contains(">>") || scale.contains("&")) {
					return;
				}else {
					scale_var.setText("(x>>" + scale + ")&1");
				}
			}
		});
		panel.add(bitOperationButton);
		
		c_pointName = new JCheckBox("포인트 이름");
		c_pointName.setSelected(true);
		c_pointName.setFocusPainted(false);
		c_pointName.setBackground(Color.WHITE);
		c_pointName.setHorizontalAlignment(SwingConstants.LEFT);
		c_pointName.setForeground(Color.BLACK);
		c_pointName.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_pointName.setBounds(21, 76, 115, 32);
		c_pointName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_pointName);
		
		c_fc = new JCheckBox("기능코드");
		c_fc.setSelected(true);
		c_fc.setFocusPainted(false);
		c_fc.setHorizontalAlignment(SwingConstants.LEFT);
		c_fc.setForeground(Color.BLACK);
		c_fc.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_fc.setBackground(Color.WHITE);
		c_fc.setBounds(143, 76, 95, 32);
		c_fc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_fc);
		
		c_addr = new JCheckBox("주소");
		c_addr.setSelected(true);
		c_addr.setFocusPainted(false);
		c_addr.setHorizontalAlignment(SwingConstants.LEFT);
		c_addr.setForeground(Color.BLACK);
		c_addr.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_addr.setBackground(Color.WHITE);
		c_addr.setBounds(236, 76, 65, 32);
		c_addr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_addr);
		
		c_dataType = new JCheckBox("데이터 타입");
		c_dataType.setSelected(true);
		c_dataType.setFocusPainted(false);
		c_dataType.setHorizontalAlignment(SwingConstants.LEFT);
		c_dataType.setForeground(Color.BLACK);
		c_dataType.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_dataType.setBackground(Color.WHITE);
		c_dataType.setBounds(301, 76, 115, 32);
		c_dataType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_dataType);
		
		c_measure = new JCheckBox("측정 단위");
		c_measure.setSelected(true);
		c_measure.setFocusPainted(false);
		c_measure.setHorizontalAlignment(SwingConstants.LEFT);
		c_measure.setForeground(Color.BLACK);
		c_measure.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_measure.setBackground(Color.WHITE);
		c_measure.setBounds(421, 76, 97, 32);
		c_measure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_measure);
		
		c_scale = new JCheckBox("보정식");
		c_scale.setSelected(true);
		c_scale.setFocusPainted(false);
		c_scale.setHorizontalAlignment(SwingConstants.LEFT);
		c_scale.setForeground(Color.BLACK);
		c_scale.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_scale.setBackground(Color.WHITE);
		c_scale.setBounds(523, 76, 84, 32);
		c_scale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_scale);
		
		c_dataForamt = new JCheckBox("데이터 형식");
		c_dataForamt.setSelected(true);
		c_dataForamt.setFocusPainted(false);
		c_dataForamt.setHorizontalAlignment(SwingConstants.LEFT);
		c_dataForamt.setForeground(Color.BLACK);
		c_dataForamt.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		c_dataForamt.setBackground(Color.WHITE);
		c_dataForamt.setBounds(609, 76, 115, 32);
		c_dataForamt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUseField();
			}
		});
		actualPanel.add(c_dataForamt);
		
		pointScrollPane = new JScrollPane();
		pointScrollPane.setBounds(765, 114, 482, 437);
		pointScrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		actualPanel.add(pointScrollPane);
		
		pointTable = new JTable();
		pointTable.addKeyListener(new KeyAdapter() {			
			public void keyPressed(KeyEvent e) { 
				int row = pointTable.getSelectedRow();
				ModbusWatchPoint wp = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
				selectedPoint = wp;
				initModbusPointInfo(wp);
			}
			public void keyReleased(KeyEvent e) { 
				int row = pointTable.getSelectedRow();
				ModbusWatchPoint wp = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
				selectedPoint = wp;
				initModbusPointInfo(wp);
			}
		});
		pointTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) { 
					// 왼쪽 클릭
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint wp = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					selectedPoint = wp;
					initModbusPointInfo(wp);
				} 
				
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
				}
				if (e.getButton() == 3) {
					// 오른쪽 클릭
					int row = pointTable.getSelectedRow();
					ModbusWatchPoint wp = (ModbusWatchPoint) pointTable.getValueAt(row, 1);
					selectedPoint = wp;
					initModbusPointInfo(wp);
					ModbusWatchPoint.showInfo(wp);
				}
			}
		});
		
		pointScrollPane.setViewportView(pointTable);
		resetPointTable(pointTable);
		addPointRecord(pointTable, this.pointList);
		
		searchLabel = new JLabel("검 색");
		searchLabel.setHorizontalAlignment(SwingConstants.LEFT);
		searchLabel.setBackground(Color.WHITE);
		searchLabel.setForeground(Color.BLACK);
		searchLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		searchLabel.setBounds(778, 77, 56, 30);
		actualPanel.add(searchLabel);
		
		search_TextField = new JTextField();
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.setBackground(Color.WHITE);
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setForeground(Color.BLACK);
		search_TextField.setFont(new Font("맑은 고딕", Font.PLAIN, 17));
		search_TextField.setBounds(837, 79, 410, 29);
		search_TextField.setColumns(10);
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
		
		
		
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
		
		initModbusPointInfo(selectedPoint);
		pointName_var.requestFocus();
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
			sb.append(String.format("%s", "모드버스 포인트의 " + Util.colorBlue("주소(Address)") +  " 정보를 확인해주세요"));
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
		
		if(scale_var.getForeground() == Color.RED) {
			formValid = false;
			
		}else {
			String scale = scale_var.getText().trim();
			try {
				JavaScript.eval(scale, "1");
				scale_var.setForeground(Color.BLUE);
			}catch(Exception e) {
				formValid = false;
				scale_var.setForeground(Color.RED);
			}
		}
		
		if(!formValid) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorRed("Form Validation Error"), Util.separator, Util.separator));
			sb.append(String.format("%s", "모드버스 포인트의 " + Util.colorBlue("보정식(Scale Formula)") +  " 정보를 확인해주세요"));
			sb.append(Util.separator + Util.separator + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			scale_var.requestFocus();
			return formValid;
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
	
	public void setFrameAndPanelSize(Dimension frameSize, Dimension panelSize) {
		this.setSize(frameSize);
		panel.setSize(panelSize);
	}
	
	public static void existsFrame() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Modify Modbus Watch Point Frame Already Exists") + Util.separator + "\n");
		sb.append("Modify Modbus Watch Point 프레임이 이미 열려있습니다" + Util.separator + "\n");
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public void resetTable(JTable table, int dataFormat){
		if(dataFormat == 1) {
			addRow.setEnabled(false);
			addRow.setVisible(false);
			deleteRow.setEnabled(false);
			deleteRow.setVisible(false);
			
			table.setModel(new DefaultTableModel(
					new Object[][] {
						{0, null},
						{1, null},
					},
					new String[] { "값", "내 용"}) {
					boolean[] columnEditables = new boolean[] {
							false, // 필 드 : 수정 불가
							true, // 내 용 : 수정 불가						
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
			});
		}else {
			addRow.setEnabled(true);
			addRow.setVisible(true);
			deleteRow.setEnabled(true);
			deleteRow.setVisible(true);
			
			table.setModel(new DefaultTableModel(
					null,
					new String[] { "값", "내 용"}) {
					boolean[] columnEditables = new boolean[] {
							true, // 필 드 : 수정 불가
							true, // 내 용 : 수정 불가						
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
			});
		}
		
		setTableStyle(table);
	}
	
	public static void setTableStyle(JTable table){
		// 테이블 헤더 설정
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 값
		table.getColumnModel().getColumn(1).setPreferredWidth(500); // 내 용
		
		// 셀 크기 임의 변경 불가
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
		table.getTableHeader().setResizingAllowed(false); // 컬럼 와이드 크기 임의 변경 불가
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순서
	}
	
	/**
	 * 	레코드 추가 메소드 
	 */
	public static void addRecord(JTable table, String... content) {
		try {
			DefaultTableModel model = (DefaultTableModel)table.getModel();

			Vector recored = new Vector();
			
			if(content != null) {
				for(String str : content) {
					recored.add(str);
				}
			}
			
			model.addRow(recored);
		} catch (Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	/**
	 * 레코드 삭제 메소드
	 */
	public void deleteRow(JTable table) {
		int[] index = table.getSelectedRows();	
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		if(index.length < 0) {
			// 선택 된 행이 없거나
			if(table.getRowCount()==0) {
				// 테이블에 행이 없을 경우 아무것도 수행하지 않음
				return;
			}
		}
				
		for(int i = 0; i < index.length; i++) {
			model.removeRow(index[0]);			
		}
	}
	
	public void resetForm() {
		c_pointName.setSelected(true);
		c_fc.setSelected(true);
		c_addr.setSelected(true);
		c_dataType.setSelected(true);
		c_measure.setSelected(true);
		c_scale.setSelected(true);
		c_dataForamt.setSelected(true);
		checkUseField();
		
		pointName_var.setText(null);
		fc_var.setSelectedIndex(2);
		addr_modbus_dec_var.setText(null);
		addr_reg_dec_var.setText(null);
		addr_reg_hex_var.setText(null);
		dataType_var.setSelectedIndex(0);
		measure_var.setText(null);
		scale_var.setText(null);
		dataFormat_var.setSelectedIndex(2);
		
		pointName_var.requestFocus();
	}
	
	public void initModbusPointInfo(ModbusWatchPoint point) {
		try {
			if(point == null) {
				resetForm();
				return;
			}
			
			c_pointName.setSelected(true);
			c_fc.setSelected(true);
			c_addr.setSelected(true);
			c_dataType.setSelected(true);
			c_measure.setSelected(true);
			c_scale.setSelected(true);
			c_dataForamt.setSelected(true);
			checkUseField();
			
			pointName_var.setText(point.getDisplayName());
			fc_var.setSelectedIndex(point.getFunctionCode() - 1);
			addr_reg_dec_var.setText("" + point.getRegisterAddr());
			syncAddr();
			dataType_var.setSelectedItem(point.getDataType());
			measure_var.setText(point.getMeasure());
			scale_var.setText(point.getScaleFunction());
			
			switch(point.getDataFormat()) {
				case 1 :
					dataFormat_var.setSelectedIndex(0);
					table.setValueAt(point.getBinLabel()[0], 0, 1);
					table.setValueAt(point.getBinLabel()[1], 1, 1);
					break;
					
				case 2 :
					dataFormat_var.setSelectedIndex(1);
					PerfLabelStatusBean[] labels =  point.getStatusLabels();
					if(labels != null) {
						for(PerfLabelStatusBean label : labels) {
							addRecord(table, "" + label.value, "" + label.label);
						}
					}
					break;
					
				case 3 :
					dataFormat_var.setSelectedIndex(2);
					break;
					
				default :
					dataFormat_var.setSelectedIndex(2);
					break;
			}
						
		}catch(Exception e) {
			e.printStackTrace();
			resetForm();
		}
	}
	
	public void checkUseField() {
		// 포인트 이름
		pointName.setEnabled(c_pointName.isSelected());
		pointName.setVisible(c_pointName.isSelected());
		pointName_var.setEnabled(c_pointName.isSelected());
		pointName_var.setVisible(c_pointName.isSelected());
		
		// 기능 코드
		fc.setEnabled(c_fc.isSelected());
		fc.setVisible(c_fc.isSelected());
		fc_var.setEnabled(c_fc.isSelected());
		fc_var.setVisible(c_fc.isSelected());
		
		// 모드버스 주소 (DEC)
		addr_modbus_dec.setEnabled(c_addr.isSelected());
		addr_modbus_dec.setVisible(c_addr.isSelected());
		addr_modbus_dec_var.setEnabled(c_addr.isSelected());
		addr_modbus_dec_var.setVisible(c_addr.isSelected());
		
		// 레지스터 주소 (DEC)
		addr_reg_dec.setEnabled(c_addr.isSelected());
		addr_reg_dec.setVisible(c_addr.isSelected());
		addr_reg_dec_var.setEnabled(c_addr.isSelected());
		addr_reg_dec_var.setVisible(c_addr.isSelected());
		
		// 레지스터 주소 (HEX)
		addr_reg_hex.setEnabled(c_addr.isSelected());
		addr_reg_hex.setVisible(c_addr.isSelected());
		addr_reg_hex_var.setEnabled(c_addr.isSelected());
		addr_reg_hex_var.setVisible(c_addr.isSelected());
		
		// 데이터 타입
		dataType.setEnabled(c_dataType.isSelected());
		dataType.setVisible(c_dataType.isSelected());
		dataType_var.setEnabled(c_dataType.isSelected());
		dataType_var.setVisible(c_dataType.isSelected());
		
		// 측정 단위
		measure.setEnabled(c_measure.isSelected());
		measure.setVisible(c_measure.isSelected());
		measure_var.setEnabled(c_measure.isSelected());
		measure_var.setVisible(c_measure.isSelected());
		autoMeasureButton.setEnabled(c_measure.isSelected());
		autoMeasureButton.setVisible(c_measure.isSelected());
		
		// 보정식
		scale.setEnabled(c_scale.isSelected());
		scale.setVisible(c_scale.isSelected());
		scale_var.setEnabled(c_scale.isSelected());
		scale_var.setVisible(c_scale.isSelected());
		bitOperationButton.setEnabled(c_scale.isSelected());
		bitOperationButton.setVisible(c_scale.isSelected());
		
		// 데이터 형식
		dataFormat.setEnabled(c_dataForamt.isSelected());
		dataFormat.setVisible(c_dataForamt.isSelected());
		dataFormat_var.setEnabled(c_dataForamt.isSelected());
		dataFormat_var.setVisible(c_dataForamt.isSelected());
		statusLabel.setEnabled(c_dataForamt.isSelected());
		statusLabel.setVisible(c_dataForamt.isSelected());
		scrollPane.setEnabled(c_dataForamt.isSelected());
		scrollPane.setVisible(c_dataForamt.isSelected());
		addRow.setEnabled(c_dataForamt.isSelected());
		addRow.setVisible(c_dataForamt.isSelected());
		deleteRow.setEnabled(c_dataForamt.isSelected());
		deleteRow.setVisible(c_dataForamt.isSelected());
	}
	
	/**
	 * 	레코드 추가
	 */
	public static void addPointRecord(JTable table, ArrayList<ModbusWatchPoint> modbusWps) {
		if(table == null || modbusWps == null) return;
		
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			Collections.sort(modbusWps);
			
			for(int i = 0; i < modbusWps.size(); i++) {
				
				ModbusWatchPoint modbusWp = modbusWps.get(i);
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
				/* column[1] */ record.add(modbusWp); // 모드버스 포인트
				
				model.addRow(record);
			}

			if(pointList != null) {
				int total = pointList.size();
				int searched = table.getRowCount();
				String text = String.format("모드버스 포인트  ( %d / %d )", searched, total);
				TableUtil.setTableHeader(table, 1, text);
			}else {
				TableUtil.setTableHeader(table, 1, "모드버스 포인트");
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
	public void deletePointRecord(JTable table, int... index) {
		ArrayList<ModbusWatchPoint> selectedPointList = getSelectedPoint(table);

		if (selectedPointList == null || selectedPointList.size() < 1) {
			return;
		} else {

			for (ModbusWatchPoint wp : selectedPointList) {
				pointList.remove(wp);
			}

			doTableFilter();
		}
	}
	
	public static void resetPointTable(JTable table){
		
		table.setModel(new DefaultTableModel(
				new Object[][] {
					
				},
				new String[] {
						"순 서",
						"모드버스 포인트"
					}) {
				boolean[] columnEditables = new boolean[] {
						false, // 순 서 : 수정 불가
						false, // 모드버스 포인트 : 수정 불가
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setPointTableStyle(table);
	}
	
	public static void setPointTableStyle(JTable table) {
		
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 15));
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(5); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(350); // 모드버스 포인트		
				
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 모드버스 포인트
	}
	
	public static ArrayList<ModbusWatchPoint> getSelectedPoint(JTable table) {
		ArrayList<ModbusWatchPoint> selectedPointlist = new ArrayList<ModbusWatchPoint>();
		int[] selectedIndex = table.getSelectedRows();
		for(int i = 0; i < selectedIndex.length; i++) {
			ModbusWatchPoint wp = (ModbusWatchPoint) table.getValueAt(selectedIndex[i], 1);
			selectedPointlist.add(wp);
		}
		return selectedPointlist;
	}
	
	public static void doTableFilter() {
		if(search_TextField == null || pointList == null) return;
		
		ArrayList<ModbusWatchPoint> filterList = new ArrayList<ModbusWatchPoint>();
		String text = search_TextField.getText();
		
		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));
		
		if(noSearch) {
			resetPointTable(pointTable);
			addPointRecord(pointTable, pointList);
			return;
		}
		
		if(!noSearch) {
			text = text.toUpperCase().trim();
		}
		
		for(int i = 0; i < pointList.size(); i++) {
			ModbusWatchPoint modbusWp = pointList.get(i);
			boolean isContain = false;
			
			if(!noSearch) {
				String searchElement = modbusWp.toString().toUpperCase();
				
				if(text.contains(",")) {
					String[] textToken = text.split(",");
					for(int i2 = 0; i2 < textToken.length; i2++) {
						String token = textToken[i2].trim();
						if(searchElement.contains(token)) {
							isContain = true;
						}
					}
				}else if(searchElement.contains(text)) {
					isContain = true;
				}
			}else {
				isContain = true;
			}
			
			
			if(isContain) {
				filterList.add(modbusWp);
			}
			
		}// for loop

		resetPointTable(pointTable);
		addPointRecord(pointTable, filterList);
	}
	
	public static void addPointList(ArrayList<ModbusWatchPoint> list) {
		if(list == null || list.size() < 1) return;
		
		for(ModbusWatchPoint wp : list) {
			if(!pointList.contains(wp)) {
				pointList.add(wp);	
			}
		}
		Collections.sort(pointList);
	}
	
	@Override
	public void dispose() {
		ModifyModbusWatchPointFrame.isExist = false;
		super.dispose();
	}
}

