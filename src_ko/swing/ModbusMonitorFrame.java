package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import src_ko.util.Util;

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
	
	// ¸ðµå¹ö½º ¸ð´ÏÅÍ Æû ÄÄÆ÷³ÍÆ®
	private JComboBox fc_comboBox; // ±â´ÉÄÚµå : ÄÞº¸¹Ú½º
	private JTextField startAddr_textField; // ½ÃÀÛÁÖ¼Ò : ÅØ½ºÆ® ÇÊµå
	private JButton method_Button; // Àü¼Û¹æ¹ý : ¹öÆ°
	private JTextField method_textField; // Àü¼Û¹æ¹ý : ÅØ½ºÆ® ÇÊµå
	private JComboBox dataType_comboBox; // µ¥ÀÌÅÍÅ¸ÀÔ : ÄÞº¸¹Ú½º
	private JTextField fontSize_text; // ±ÛÀÚÅ©±â : ÅØ½ºÆ® ÇÊµå
	private JButton sendButton; // Àü¼Û ¹öÆ° : ¹öÆ°
	private JButton resetButton; // ¸®¼Â ¹öÆ° : ¹öÆ°
	
	private Rectangle r = new Rectangle(100, 100, 1080, 720);
	private JLabel transactionID_label;
	private JLabel unitID_label;
	private JLabel timeout_label;
	private JLabel maxCount_label;
	private JLabel fontSize_label;
	private JLabel fc_label;
	private JPanel reqFormPanel;
	private JLabel range_label;
	private JLabel dataType_label;
	ButtonGroup radioGroup = null;
	
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
		currentFunction.setBounds(0, 0, 267, 55);
		actualPanel.add(currentFunction);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(0, 154, 1044, 507);
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
		
		JLabel addrFormat_label = new JLabel("Address Format");
		addrFormat_label.setBackground(Color.WHITE);
		addrFormat_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		addrFormat_label.setForeground(Color.BLACK);
		addrFormat_label.setBounds(406, 10, 150, 24);
		actualPanel.add(addrFormat_label);
		
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
		
		transactionID_label = new JLabel("Transaction ID");
		transactionID_label.setBackground(Color.WHITE);
		transactionID_label.setForeground(Color.BLACK);
		transactionID_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		transactionID_label.setBounds(576, 10, 120, 24);
		actualPanel.add(transactionID_label);
		
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
		
		unitID_label = new JLabel("Unit ID");
		unitID_label.setForeground(Color.BLACK);
		unitID_label.setBackground(Color.WHITE);
		unitID_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		unitID_label.setBounds(717, 10, 90, 24);
		actualPanel.add(unitID_label);
		
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
		
		timeout_label = new JLabel("Timeout");
		timeout_label.setForeground(Color.BLACK);
		timeout_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		timeout_label.setBackground(Color.WHITE);
		timeout_label.setBounds(826, 10, 90, 24);
		actualPanel.add(timeout_label);
		
		maxCount_label = new JLabel("Max Count");
		maxCount_label.setForeground(Color.BLACK);
		maxCount_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		maxCount_label.setBackground(Color.WHITE);
		maxCount_label.setBounds(934, 10, 100, 24);
		actualPanel.add(maxCount_label);
		
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
		reqFormPanel.setBounds(0, 76, 1044, 80);
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
		
		fc_label = new JLabel("Function Code");
		fc_label.setHorizontalAlignment(SwingConstants.LEFT);
		fc_label.setForeground(Color.BLACK);
		fc_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		fc_label.setBackground(Color.WHITE);
		fc_label.setBounds(15, 10, 124, 24);
		reqFormPanel.add(fc_label);
		
		fc_comboBox = new JComboBox();
		fc_comboBox.setBounds(14, 43, 125, 30);
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
		
		JLabel startAddr_label = new JLabel("Start Address");
		startAddr_label.setHorizontalAlignment(SwingConstants.LEFT);
		startAddr_label.setForeground(Color.BLACK);
		startAddr_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		startAddr_label.setBackground(Color.WHITE);
		startAddr_label.setBounds(157, 10, 117, 24);
		reqFormPanel.add(startAddr_label);
		
		startAddr_textField = new JTextField();
		startAddr_textField.setText("0");
		startAddr_textField.setHorizontalAlignment(SwingConstants.LEFT);
		startAddr_textField.setForeground(Color.BLUE);
		startAddr_textField.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		startAddr_textField.setColumns(10);
		startAddr_textField.setBorder(UIManager.getBorder("TextField.border"));
		startAddr_textField.setBounds(156, 43, 120, 30);
		reqFormPanel.add(startAddr_textField);
		
		range_label = new JLabel("~");
		range_label.setHorizontalAlignment(SwingConstants.CENTER);
		range_label.setForeground(Color.BLACK);
		range_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		range_label.setBackground(Color.WHITE);
		range_label.setBounds(267, 45, 40, 24);
		range_label.setEnabled(false);
		range_label.setVisible(false);
		reqFormPanel.add(range_label);
		
		method_Button = new JButton("Req Count");
		method_Button.setMargin(new Insets(2, 0, 2, 0));
		method_Button.setFocusPainted(false);
		method_Button.setForeground(Color.BLACK);
		method_Button.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		method_Button.setBackground(Color.LIGHT_GRAY);
		method_Button.setBounds(300, 8, 120, 30);
		method_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(method_Button.getText().equals("Req Count")) {
					method_Button.setText("End Address");
					range_label.setEnabled(true);
					range_label.setVisible(true);
				}else {
					method_Button.setText("Req Count");
					range_label.setEnabled(false);
					range_label.setVisible(false);
				}
				
			}
		});
		reqFormPanel.add(method_Button);
		
		method_textField = new JTextField();
		method_textField.setText("0");
		method_textField.setHorizontalAlignment(SwingConstants.LEFT);
		method_textField.setForeground(Color.BLUE);
		method_textField.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		method_textField.setColumns(10);
		method_textField.setBorder(UIManager.getBorder("TextField.border"));
		method_textField.setBounds(300, 43, 120, 30);
		reqFormPanel.add(method_textField);
		
		dataType_label = new JLabel("Data Type");
		dataType_label.setHorizontalAlignment(SwingConstants.LEFT);
		dataType_label.setForeground(Color.BLACK);
		dataType_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		dataType_label.setBackground(Color.WHITE);
		dataType_label.setBounds(436, 10, 117, 24);
		reqFormPanel.add(dataType_label);
		
		dataType_comboBox = new JComboBox();
		dataType_comboBox.setMaximumRowCount(30);
		dataType_comboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						"BINARY",
						"",
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
		dataType_comboBox.setSelectedIndex(0);
		dataType_comboBox.setForeground(Color.BLACK);
		dataType_comboBox.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		dataType_comboBox.setBackground(Color.WHITE);
		dataType_comboBox.setBounds(434, 43, 382, 30);
		reqFormPanel.add(dataType_comboBox);
		
		fontSize_label = new JLabel("Font Size");
		fontSize_label.setBounds(828, 10, 100, 24);
		fontSize_label.setHorizontalAlignment(SwingConstants.RIGHT);
		fontSize_label.setForeground(Color.BLACK);
		fontSize_label.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		fontSize_label.setBackground(Color.WHITE);
		reqFormPanel.add(fontSize_label);
		
		fontSize_text = new JTextField();
		fontSize_text.setBounds(935, 7, 100, 30);
		fontSize_text.setText("18");
		fontSize_text.setHorizontalAlignment(SwingConstants.LEFT);
		fontSize_text.setForeground(Color.BLACK);
		fontSize_text.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
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
		reqFormPanel.add(fontSize_text);
		
		sendButton = new JButton("Send");
		sendButton.setForeground(Color.BLUE);
		sendButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		sendButton.setFocusPainted(false);
		sendButton.setBackground(Color.WHITE);
		sendButton.setBounds(828, 43, 100, 30);
		reqFormPanel.add(sendButton);
		
		resetButton = new JButton("Reset");
		resetButton.setBounds(935, 43, 100, 30);
		resetButton.setFocusPainted(false);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		resetButton.setForeground(Color.RED);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				textArea.requestFocus();
			}
		});
		reqFormPanel.add(resetButton);
		
		
		
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