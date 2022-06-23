package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import src_ko.util.Util;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ModbusMonitorFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private int fontSize = 18;
	
	// ¿äÃ» Á¤º¸ ÄÄÆ÷³ÍÆ®
	public static JRadioButton radio_modbusTCP; // TCP ¶óµð¿À ¹öÆ°
	public static JRadioButton radio_modbusRTU; // RTU ¶óµð¿À ¹öÆ°
	public static JComboBox addrTypeComboBox; // ÁÖ¼Ò Çü½Ä ÄÞº¸¹Ú½º
	public static JTextField transactionId_text; // Æ®·£Àè¼Ç ¾ÆÀÌµð ÅØ½ºÆ® ÇÊµå
	public static JComboBox unitID_comboBox; // Àåºñ ¹øÈ£ ÄÞº¸¹Ú½º
	public static JTextField timeout_text; // Å¸ÀÓ¾Æ¿ô ÅØ½ºÆ® ÇÊµå
	public static JTextField maxCount_text; // ÃÖ´ë ¿äÃ» °³¼ö ÅØ½ºÆ® ÇÊµå
	
	ButtonGroup radioGroup = null;
	
	private Rectangle r = new Rectangle(100, 100, 1080, 720);	
	private JLabel lblNewLabel;
	private JComboBox comboBox;
	private JLabel label;
	private JTextField textField;
	private JLabel lblTiemout;
	private JLabel lblReqMaxCount;
	private JTextField textField_1;
	private JLabel lblFontSize;
	private JTextField fontSize_text;
	private JButton resetButton;
	
	private JPanel reqFormPanel;
	private JLabel fc_label;
	
	private JComboBox fc_comboBox;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModbusMonitorFrame frame = new ModbusMonitorFrame();
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
	public ModbusMonitorFrame() {
		ModbusMonitorFrame.isExist = true;
		setTitle("Modbus Monitor");
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
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Modbus Monitor");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 267, 49);
		actualPanel.add(currentFunction);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(0, 122, 1044, 539);
		actualPanel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setForeground(Color.BLACK);
		textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
		scrollPane.setViewportView(textArea);
		
		radio_modbusTCP = new JRadioButton("Modbus TCP");
		radio_modbusTCP.setFocusPainted(false);
		radio_modbusTCP.setForeground(Color.BLACK);
		radio_modbusTCP.setBackground(Color.WHITE);
		radio_modbusTCP.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusTCP.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));		
		radio_modbusTCP.setBounds(262, 11, 135, 23);
		actualPanel.add(radio_modbusTCP);
		
		radio_modbusRTU = new JRadioButton("Modbus RTU");
		radio_modbusRTU.setFocusPainted(false);
		radio_modbusRTU.setForeground(Color.BLACK);
		radio_modbusRTU.setBackground(Color.WHITE);
		radio_modbusRTU.setSelected(true);
		radio_modbusRTU.setHorizontalAlignment(SwingConstants.LEFT);
		radio_modbusRTU.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));		
		radio_modbusRTU.setBounds(262, 45, 135, 23);
		actualPanel.add(radio_modbusRTU);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(radio_modbusTCP);
		radioGroup.add(radio_modbusRTU);
		
		addrTypeComboBox = new JComboBox();
		addrTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"Modbus (DEC)",
						"Register (DEC)", 
						"Register (HEX)"
						}));
		addrTypeComboBox.setSelectedIndex(0);
		addrTypeComboBox.setForeground(Color.BLACK);
		addrTypeComboBox.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
		addrTypeComboBox.setBackground(Color.WHITE);
		addrTypeComboBox.setBounds(405, 40, 150, 30);
		actualPanel.add(addrTypeComboBox);
		
		JLabel addrFormat = new JLabel("Address Format");
		addrFormat.setBackground(Color.WHITE);
		addrFormat.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		addrFormat.setForeground(Color.BLACK);
		addrFormat.setBounds(406, 10, 150, 24);
		actualPanel.add(addrFormat);
		
		lblNewLabel = new JLabel("Transaction ID");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		lblNewLabel.setBounds(576, 10, 120, 24);
		actualPanel.add(lblNewLabel);
		
		transactionId_text = new JTextField();
		transactionId_text = new JTextField();
		transactionId_text.setForeground(Color.BLUE);
		transactionId_text.setText("1");
		transactionId_text.setHorizontalAlignment(SwingConstants.LEFT);
		transactionId_text.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
		transactionId_text.setColumns(10);
		transactionId_text.setBorder(UIManager.getBorder("TextField.border"));
		transactionId_text.setBounds(575, 40, 120, 30);
		actualPanel.add(transactionId_text);
		
		
		String[] unitIdValue = new String[255];
		for(int i = 0; i < 255; i++) {
			unitIdValue[i] = String.valueOf(i+1) + "¹ø";
		}	
		
		label = new JLabel("Unit ID");
		label.setForeground(Color.BLACK);
		label.setBackground(Color.WHITE);
		label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		label.setBounds(717, 10, 90, 24);
		actualPanel.add(label);
		
		unitID_comboBox = new JComboBox();
		unitID_comboBox.setForeground(Color.BLACK);
		unitID_comboBox.setBackground(Color.WHITE);
		unitID_comboBox.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));				
		unitID_comboBox.setModel(new DefaultComboBoxModel(unitIdValue));
		unitID_comboBox.setBounds(716, 40, 90, 30);
		actualPanel.add(unitID_comboBox);
		
		timeout_text = new JTextField();
		timeout_text.setForeground(Color.BLUE);
		timeout_text.setText("5000");
		timeout_text.setHorizontalAlignment(SwingConstants.LEFT);
		timeout_text.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
		timeout_text.setColumns(10);
		timeout_text.setBorder(UIManager.getBorder("TextField.border"));
		timeout_text.setBounds(825, 40, 90, 30);		
		actualPanel.add(timeout_text);
		
		lblTiemout = new JLabel("Timeout");
		lblTiemout.setForeground(Color.BLACK);
		lblTiemout.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		lblTiemout.setBackground(Color.WHITE);
		lblTiemout.setBounds(826, 10, 90, 24);
		actualPanel.add(lblTiemout);
		
		lblReqMaxCount = new JLabel("Max Count");
		lblReqMaxCount.setForeground(Color.BLACK);
		lblReqMaxCount.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		lblReqMaxCount.setBackground(Color.WHITE);
		lblReqMaxCount.setBounds(934, 10, 100, 24);
		actualPanel.add(lblReqMaxCount);
		
		maxCount_text = new JTextField();
		maxCount_text.setForeground(Color.BLUE);
		maxCount_text.setText("125");
		maxCount_text.setHorizontalAlignment(SwingConstants.LEFT);
		maxCount_text.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
		maxCount_text.setColumns(10);
		maxCount_text.setBorder(UIManager.getBorder("TextField.border"));
		maxCount_text.setBounds(933, 40, 100, 30);
		actualPanel.add(maxCount_text);
		
		reqFormPanel = new JPanel();
		reqFormPanel.setBorder(new LineBorder(Color.BLACK, 2));
		reqFormPanel.setBackground(Color.LIGHT_GRAY);
		reqFormPanel.setBounds(0, 76, 1044, 48);
		reqFormPanel.setLayout(null);
		actualPanel.add(reqFormPanel);
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
    			addComponentListener(new ComponentAdapter() {
    				@Override
    				public void componentResized(ComponentEvent e) {
    					scrollPane.setSize(contentPane.getWidth() - (scrollPane.getX() + 20), contentPane.getHeight() - (scrollPane.getY() + 20));
    					reqFormPanel.setSize(scrollPane.getWidth(), reqFormPanel.getHeight());
    					super.componentResized(e);
    				}
    			});
            }
        });
		resetButton = new JButton("ÃÊ±âÈ­");
		resetButton.setBounds(744, 10, 100, 30);
		resetButton.setFocusPainted(false);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		resetButton.setForeground(Color.BLACK);
		reqFormPanel.add(resetButton);
		
		fc_label = new JLabel("FunctionCode");
		fc_label.setHorizontalAlignment(SwingConstants.LEFT);
		fc_label.setForeground(Color.BLACK);
		fc_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		fc_label.setBackground(Color.WHITE);
		fc_label.setBounds(11, 12, 111, 24);
		reqFormPanel.add(fc_label);
		
		fc_comboBox = new JComboBox();
		fc_comboBox.setBounds(127, 7, 88, 35);
		fc_comboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"FC 01",
						"FC 02", 
						"FC 03",
						"FC 04"
						}));
		fc_comboBox.setSelectedIndex(0);
		fc_comboBox.setForeground(Color.BLACK);
		fc_comboBox.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		fc_comboBox.setBackground(Color.WHITE);
		reqFormPanel.add(fc_comboBox);
		
		lblFontSize = new JLabel("Font Size");
		lblFontSize.setBounds(78, 46, 90, 24);
		lblFontSize.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFontSize.setForeground(Color.BLACK);
		lblFontSize.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		lblFontSize.setBackground(Color.WHITE);
		actualPanel.add(lblFontSize);
		
		fontSize_text = new JTextField();
		fontSize_text.setBounds(174, 46, 80, 25);
		fontSize_text.setText("18");
		fontSize_text.setHorizontalAlignment(SwingConstants.LEFT);
		fontSize_text.setForeground(Color.BLACK);
		fontSize_text.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
		fontSize_text.setColumns(10);
		fontSize_text.setBorder(UIManager.getBorder("TextField.border"));
		fontSize_text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					int size = Integer.parseInt(fontSize_text.getText().trim());
					fontSize = size;
					textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
				}catch(Exception ex) {
					ex.printStackTrace();
					textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					int size = Integer.parseInt(fontSize_text.getText().trim());
					fontSize = size;
					textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
				}catch(Exception ex) {
					ex.printStackTrace();
					textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
				}
			}
			
		});
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				textArea.requestFocus();
			}
		});
		actualPanel.add(fontSize_text);
		
		// ÇÁ·¹ÀÓÀÌ È­¸é °¡¿îµ¥¿¡¼­ »ý¼ºµÈ´Ù
		setLocationRelativeTo(null);
		setVisible(true);
		
		textArea.requestFocus();
	}

	@Override
	public void dispose() {
		ModbusMonitorFrame.isExist = false;
		super.dispose();
	}
}