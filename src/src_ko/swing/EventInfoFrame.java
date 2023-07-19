package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.agent.Event;
import src_ko.util.Inspecter;
import src_ko.util.Util;

public class EventInfoFrame extends JFrame {

	private JPanel contentPane;
	
	public static boolean isExist = false;
	private boolean isValidDuration = false;
		
	private JTextField eventName_TextField;
	private JTextField eventThreshold_TextField;
	private JTextField eventDuration_TextField;
	private JTextField eventCount_TextField;
	private JComboBox eventSeverity_ComboBox;
	private JComboBox eventOperation_ComboBox;
	private JComboBox eventMode_ComboBox;
	private JTextArea eventMessage_TextArea;
	private JCheckBox eventAutoClose_CheckBox;
	
	private JLabel eventDuration;
	private JLabel eventDuration_value2;
	private JLabel eventCount;
	private JLabel eventCount_value2;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventInfoFrame frame = new EventInfoFrame();
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
	public EventInfoFrame(Color... color) {
		EventInfoFrame.isExist = true;
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("ModbusAnalyzer");
		setIconImage(new Util().getIconResource().getImage());	
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 686);
		contentPane = new JPanel();
		
		Color borderColor = (color != null && color.length > 0 && color[0] != null) ? color[0] : new Color(255, 140, 0);
		contentPane.setBorder(new LineBorder(borderColor, 8));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel autoEvent = new JLabel("Auto Event Information");
		autoEvent.setForeground(Color.BLACK);
		autoEvent.setIcon(new Util().getSubLogoResource());
		autoEvent.setBounds(0, 0, 322, 60);
		autoEvent.setHorizontalAlignment(SwingConstants.LEFT);
		autoEvent.setFont(FontManager.getFont(Font.BOLD, 22));
		autoEvent.setBackground(Color.WHITE);
		actualPanel.add(autoEvent);
		
		JLabel eventType = new JLabel("\uC774\uBCA4\uD2B8 \uC885\uB958");
		eventType.setForeground(Color.BLACK);
		eventType.setOpaque(true);
		eventType.setHorizontalAlignment(SwingConstants.CENTER);
		eventType.setFont(FontManager.getFont(Font.BOLD, 17));
		eventType.setBackground(Color.LIGHT_GRAY);
		eventType.setBounds(12, 66, 182, 39);
		actualPanel.add(eventType);
		
		JLabel eventType_value = new JLabel("\uC131\uB2A5 \uC774\uBCA4\uD2B8");
		eventType_value.setOpaque(true);
		eventType_value.setHorizontalAlignment(SwingConstants.CENTER);
		eventType_value.setForeground(Color.BLACK);
		eventType_value.setFont(FontManager.getFont(Font.BOLD, 17));
		eventType_value.setBackground(SystemColor.control);
		eventType_value.setBounds(206, 66, 520, 39);
		actualPanel.add(eventType_value);
		
		JLabel eventName = new JLabel("\uC774\uBCA4\uD2B8 \uC774\uB984");
		eventName.setOpaque(true);
		eventName.setHorizontalAlignment(SwingConstants.CENTER);
		eventName.setForeground(Color.BLACK);
		eventName.setFont(FontManager.getFont(Font.BOLD, 17));
		eventName.setBackground(Color.LIGHT_GRAY);
		eventName.setBounds(12, 115, 182, 39);
		actualPanel.add(eventName);
		
		JLabel eventName_value = new JLabel("\uC131\uB2A5\uBA85 + ");
		eventName_value.setOpaque(true);
		eventName_value.setHorizontalAlignment(SwingConstants.CENTER);
		eventName_value.setForeground(Color.BLACK);
		eventName_value.setFont(FontManager.getFont(Font.BOLD, 17));
		eventName_value.setBackground(SystemColor.control);
		eventName_value.setBounds(206, 115, 88, 38);
		actualPanel.add(eventName_value);
		
		eventName_TextField = new JTextField();
		eventName_TextField.setForeground(Color.BLACK);
		eventName_TextField.setText("\uC774\uBCA4\uD2B8");
		eventName_TextField.setFont(FontManager.getFont(Font.BOLD, 17));
		eventName_TextField.setBackground(Color.WHITE);
		eventName_TextField.setColumns(10);
		eventName_TextField.setBounds(294, 115, 432, 39);
		eventName_TextField.addFocusListener(Util.focusListener);
		eventName_TextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				checkEventName(true);
			}			
		});
		actualPanel.add(eventName_TextField);
		
		JLabel severity = new JLabel("\uC2EC\uAC01\uB3C4");
		severity.setOpaque(true);
		severity.setHorizontalAlignment(SwingConstants.CENTER);
		severity.setForeground(Color.BLACK);
		severity.setFont(FontManager.getFont(Font.BOLD, 17));
		severity.setBackground(Color.LIGHT_GRAY);
		severity.setBounds(12, 164, 182, 39);
		actualPanel.add(severity);
		
		eventSeverity_ComboBox = new JComboBox();
		eventSeverity_ComboBox.setForeground(Color.BLACK);
		eventSeverity_ComboBox.setBackground(Color.WHITE);		
		eventSeverity_ComboBox.setFont(FontManager.getFont(Font.BOLD, 17));
		eventSeverity_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"Normal", "Warning", "Minor", "Critical", "Fatal"}));
		eventSeverity_ComboBox.setBounds(206, 164, 141, 39);
		eventSeverity_ComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				super.mouseClicked(e);
				eventSeverity_ComboBox.setForeground(Color.BLACK);
				eventSeverity_ComboBox.setBackground(Color.white);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				eventSeverity_ComboBox.setForeground(Color.BLACK);
				eventSeverity_ComboBox.setBackground(Color.white);
			}

			@Override
			public void mouseReleased(MouseEvent e) {				
				super.mouseReleased(e);
				eventSeverity_ComboBox.setForeground(Color.BLACK);
				eventSeverity_ComboBox.setBackground(Color.white);
			}			
		});		
		
		actualPanel.add(eventSeverity_ComboBox);
		
		
		
		JLabel eventCondition = new JLabel("\uC774\uBCA4\uD2B8 \uC870\uAC74");
		eventCondition.setOpaque(true);
		eventCondition.setHorizontalAlignment(SwingConstants.CENTER);
		eventCondition.setForeground(Color.BLACK);
		eventCondition.setFont(FontManager.getFont(Font.BOLD, 17));
		eventCondition.setBackground(Color.LIGHT_GRAY);
		eventCondition.setBounds(12, 213, 182, 39);
		actualPanel.add(eventCondition);
		
		eventThreshold_TextField = new JTextField();
		eventThreshold_TextField.setBackground(Color.WHITE);
		eventThreshold_TextField.setForeground(Color.BLACK);
		eventThreshold_TextField.setText("1");
		eventThreshold_TextField.setFont(FontManager.getFont(Font.BOLD, 17));
		eventThreshold_TextField.setColumns(10);
		eventThreshold_TextField.setBounds(206, 213, 141, 39);
		eventThreshold_TextField.addFocusListener(Util.focusListener);
		actualPanel.add(eventThreshold_TextField);
		eventSeverity_ComboBox.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();
				String item = temp.getSelectedItem().toString();
				
				switch(item) {
				case "Normal" :
					eventSeverity_ComboBox.setForeground(new Color(0xff, 0xff, 0xff));
					eventSeverity_ComboBox.setBackground(new Color(0x80, 0x80, 0x00));
					break;
				case "Warning" :
					eventSeverity_ComboBox.setForeground(new Color(0x00, 0x00, 0x00));
					eventSeverity_ComboBox.setBackground(new Color(0xf3, 0xdb, 0x11));
					break;
				case "Minor" :
					eventSeverity_ComboBox.setForeground(new Color(0xff, 0xff, 0xff));
					eventSeverity_ComboBox.setBackground(new Color(0xeb, 0x6f, 0x34));
					break;
				case "Critical" :
					eventSeverity_ComboBox.setForeground(new Color(0xff, 0xff, 0xff));
					eventSeverity_ComboBox.setBackground(new Color(0x80, 0x00, 0x00));
					break;
				case "Fatal" :
					eventSeverity_ComboBox.setForeground(new Color(0xff, 0xff, 0xff));
					eventSeverity_ComboBox.setBackground(new Color(0x00, 0x00, 0x00));
					break;
				}
				
				eventThreshold_TextField.requestFocus();
			}
		});
		eventThreshold_TextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				checkThreshold(true);
			}			
		});

		
		eventOperation_ComboBox = new JComboBox();
		eventOperation_ComboBox.setForeground(Color.BLACK);
		eventOperation_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"\uC774\uC0C1\uC77C \uACBD\uC6B0", "\uC774\uD558\uC77C \uACBD\uC6B0", "\uCD08\uACFC\uC77C \uACBD\uC6B0", "\uBBF8\uB9CC\uC77C \uACBD\uC6B0", "\uC77C \uACBD\uC6B0", "\uC774(\uAC00) \uC544\uB2D0 \uACBD\uC6B0"}));
		eventOperation_ComboBox.setFont(FontManager.getFont(Font.BOLD, 17));
		eventOperation_ComboBox.setBackground(Color.WHITE);
		eventOperation_ComboBox.setBounds(359, 213, 367, 39);
		actualPanel.add(eventOperation_ComboBox);
		
		JLabel eventMode = new JLabel("\uC774\uBCA4\uD2B8 \uBC1C\uC0DD \uBAA8\uB4DC");
		eventMode.setOpaque(true);
		eventMode.setHorizontalAlignment(SwingConstants.CENTER);
		eventMode.setForeground(Color.BLACK);
		eventMode.setFont(FontManager.getFont(Font.BOLD, 17));
		eventMode.setBackground(Color.LIGHT_GRAY);
		eventMode.setBounds(12, 262, 182, 39);
		actualPanel.add(eventMode);
		
		eventMode_ComboBox = new JComboBox();
		eventMode_ComboBox.setForeground(Color.BLACK);
		eventMode_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"\uC870\uAC74 \uBC1C\uC0DD\uC2DC \uD55C\uBC88\uB9CC \uC54C\uB9BC", "\uC870\uAC74\uC774 \uBC1C\uC0DD\uD560 \uB54C\uB9C8\uB2E4", "\uC9C0\uC815\uB41C \uC2DC\uAC04\uB3D9\uC548 \uC0C1\uD0DC\uAC00 \uC9C0\uC18D\uB420 \uACBD\uC6B0", "\uC9C0\uC815\uB41C \uC2DC\uAC04\uB3D9\uC548 \uC9C0\uC815\uB41C \uD69F\uC218\uB9CC\uD07C \uC870\uAC74\uC774 \uBC1C\uC0DD\uD560 \uACBD\uC6B0"}));
		eventMode_ComboBox.setFont(FontManager.getFont(Font.BOLD, 17));
		eventMode_ComboBox.setBackground(Color.WHITE);
		eventMode_ComboBox.setBounds(206, 262, 520, 39);
		eventMode_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox temp = (JComboBox)e.getSource();
				int selectedIndex = temp.getSelectedIndex();
				
				switch(selectedIndex) {
				
				case 0 :// ���� �߻��� �ѹ��� �˸�
					showDuration(false);
					showCount(false);
					break;
				case 1 : // ������ �߻��� ������
					showDuration(false);
					showCount(false);
					break;
					
				case 2 : // ������ �ð����� ���°� ���ӵ� ���
					showDuration(true);
					showCount(false);
					break;
					
				case 3 : // ������ �ð����� ������ Ƚ����ŭ ������ �߻��� ���
					showDuration(true);
					showCount(true);
					break;
				}
				
			}
		});
		actualPanel.add(eventMode_ComboBox);
		
		JPanel detailInfo_Panel = new JPanel();
		detailInfo_Panel.setBackground(SystemColor.control);
		detailInfo_Panel.setBounds(12, 309, 714, 108);
		actualPanel.add(detailInfo_Panel);
		detailInfo_Panel.setLayout(null);
		
		eventDuration = new JLabel("\uC9C0\uC18D \uC2DC\uAC04");
		eventDuration.setOpaque(true);
		eventDuration.setHorizontalAlignment(SwingConstants.CENTER);
		eventDuration.setForeground(Color.BLACK);
		eventDuration.setFont(FontManager.getFont(Font.BOLD, 17));
		eventDuration.setBackground(Color.LIGHT_GRAY);
		eventDuration.setBounds(12, 10, 182, 39);
		detailInfo_Panel.add(eventDuration);
		
		eventDuration_TextField = new JTextField();
		eventDuration_TextField.setBackground(Color.WHITE);
		eventDuration_TextField.setForeground(Color.BLACK);
		eventDuration_TextField.setText("10");
		eventDuration_TextField.setFont(FontManager.getFont(Font.BOLD, 17));
		eventDuration_TextField.setColumns(10);
		eventDuration_TextField.setBounds(206, 10, 141, 39);
		eventDuration_TextField.addFocusListener(Util.focusListener);		
		eventDuration_TextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {				
				isValidDuration = checkDuration(true);									
			}		
		});
		
		detailInfo_Panel.add(eventDuration_TextField);
		
		eventDuration_value2 = new JLabel("\uBD84");
		eventDuration_value2.setOpaque(true);
		eventDuration_value2.setHorizontalAlignment(SwingConstants.LEFT);
		eventDuration_value2.setForeground(Color.BLACK);
		eventDuration_value2.setFont(FontManager.getFont(Font.BOLD, 17));
		eventDuration_value2.setBackground(SystemColor.control);
		eventDuration_value2.setBounds(355, 10, 182, 39);
		detailInfo_Panel.add(eventDuration_value2);
		
		eventCount = new JLabel("\uC774\uC0C1 \uBC1C\uC0DD \uD69F\uC218");
		eventCount.setOpaque(true);
		eventCount.setHorizontalAlignment(SwingConstants.CENTER);
		eventCount.setForeground(Color.BLACK);
		eventCount.setFont(FontManager.getFont(Font.BOLD, 17));
		eventCount.setBackground(Color.LIGHT_GRAY);
		eventCount.setBounds(12, 59, 182, 39);
		detailInfo_Panel.add(eventCount);
		
		eventCount_TextField = new JTextField();
		eventCount_TextField.setBackground(Color.WHITE);
		eventCount_TextField.setForeground(Color.BLACK);
		eventCount_TextField.setText("10");
		eventCount_TextField.setFont(FontManager.getFont(Font.BOLD, 17));
		eventCount_TextField.setColumns(10);
		eventCount_TextField.setBounds(206, 59, 141, 39);
		eventCount_TextField.addFocusListener(Util.focusListener);
		eventCount_TextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				checkCount(true);
			}			
		});
		detailInfo_Panel.add(eventCount_TextField);
		
		eventCount_value2 = new JLabel("ȸ   ( MAX : ���� �ð� / ���� ���� �ֱ� )");
		eventCount_value2.setOpaque(true);
		eventCount_value2.setHorizontalAlignment(SwingConstants.LEFT);
		eventCount_value2.setForeground(Color.BLACK);
		eventCount_value2.setFont(FontManager.getFont(Font.BOLD, 17));
		eventCount_value2.setBackground(SystemColor.menu);
		eventCount_value2.setBounds(355, 59, 359, 39);
		detailInfo_Panel.add(eventCount_value2);
		
		JLabel eventMessage = new JLabel("\uC774\uBCA4\uD2B8 \uBA54\uC2DC\uC9C0");
		eventMessage.setOpaque(true);
		eventMessage.setHorizontalAlignment(SwingConstants.CENTER);
		eventMessage.setForeground(Color.BLACK);
		eventMessage.setFont(FontManager.getFont(Font.BOLD, 17));
		eventMessage.setBackground(Color.LIGHT_GRAY);
		eventMessage.setBounds(12, 427, 182, 151);
		actualPanel.add(eventMessage);
		
		eventMessage_TextArea = new JTextArea();
		eventMessage_TextArea.setBackground(Color.WHITE);
		eventMessage_TextArea.setForeground(Color.BLACK);
		eventMessage_TextArea.setFont(FontManager.getFont(Font.BOLD, 16));				
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(206, 427, 520, 151);	
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		scrollPane.setViewportView(eventMessage_TextArea);
		actualPanel.add(scrollPane);
		
		JLabel eventAutoClose = new JLabel("\uC790\uB3D9 \uC885\uB8CC");
		eventAutoClose.setOpaque(true);
		eventAutoClose.setHorizontalAlignment(SwingConstants.CENTER);
		eventAutoClose.setForeground(Color.BLACK);
		eventAutoClose.setFont(FontManager.getFont(Font.BOLD, 17));
		eventAutoClose.setBackground(Color.LIGHT_GRAY);
		eventAutoClose.setBounds(12, 588, 182, 39);
		actualPanel.add(eventAutoClose);
		
		eventAutoClose_CheckBox = new JCheckBox("\uC774\uBCA4\uD2B8 \uC790\uB3D9 \uBCF5\uAD6C \uC0AC\uC6A9");
		eventAutoClose_CheckBox.setForeground(Color.BLACK);
		eventAutoClose_CheckBox.setHorizontalAlignment(SwingConstants.LEFT);
		eventAutoClose_CheckBox.setBackground(SystemColor.control);
		eventAutoClose_CheckBox.setSelected(true);
		eventAutoClose_CheckBox.setFont(FontManager.getFont(Font.BOLD, 17));
		eventAutoClose_CheckBox.setBounds(206, 586, 520, 41);
		actualPanel.add(eventAutoClose_CheckBox);
		
		
		// �������� ȭ�� ������� �����ȴ�
		eventSeverity_ComboBox.setSelectedIndex(0); // �⺻ �ɰ��� : Normal
		eventMode_ComboBox.setSelectedIndex(0); // �⺻ �߻� ��� : ���� �߻��� �ѹ��� �˸�
		
		JButton saveButton = new JButton("\uC800 \uC7A5");
		saveButton.setForeground(Color.BLACK);
		saveButton.setFont(FontManager.getFont(Font.BOLD, 15));
		saveButton.setFocusPainted(false);
		saveButton.setContentAreaFilled(false);
		saveButton.setBorder(UIManager.getBorder("Button.border"));
		saveButton.setBackground(Color.WHITE);
		saveButton.setBounds(538, 13, 88, 38);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// �̺�Ʈ ���� ���� ��, Validation ����				
				boolean inspectSuccess = true;
				if(inspectSuccess) inspectSuccess = checkEventName(inspectSuccess);
				if(inspectSuccess) inspectSuccess = checkThreshold(inspectSuccess);
				if(inspectSuccess) inspectSuccess = checkDuration(inspectSuccess);
				if(inspectSuccess) inspectSuccess = checkCount(inspectSuccess);										
				if(!inspectSuccess) return;
				
				// �̺�Ʈ �̸� ----------------------------------------------
				Event.name = eventName_TextField.getText().trim();
				
				// �̺�Ʈ �޽��� ----------------------------------------------
				Event.message = eventMessage_TextArea.getText().trim();
							
				// �̺�Ʈ �ɰ��� ---------------------------------------------- 
				int severity = eventSeverity_ComboBox.getSelectedIndex();
				switch(severity) {
				case 0 : // Normal
					Event.severity = "10";
					break;
				case 1 : // Warning
					Event.severity = "20";
					break;
				case 2 : // Minor
					Event.severity = "30";
					break;
				case 3 : // Critical
					Event.severity = "40";
					break;
				case 4 : // Fatal
					Event.severity = "50";
					break;
				}
				
				// �̺�Ʈ �Ӱ� �� ----------------------------------------------
				Event.threshold = eventThreshold_TextField.getText().trim();
						
				// �̺�Ʈ ������ ----------------------------------------------
				int op = eventOperation_ComboBox.getSelectedIndex();
				switch(op) {				
				case 0 :
					Event.op = ">=";
					break;
				case 1 :
					Event.op = "<=";
					break;
				case 2 :
					Event.op = ">";
					break;
				case 3 :
					Event.op = "<";
					break;
				case 4 : 
					Event.op = "=";
					break;
				case 5 :
					Event.op = "<>";
					break;					
				}
				
				// �̺�Ʈ �߻� ��� ----------------------------------------------
				int mode = eventMode_ComboBox.getSelectedIndex();
				switch(mode) {
				case 0 : // ���� �߻��� �ѹ��� �˸�
					Event.mode = "5";
					Event.duration = String.valueOf(eventDuration_TextField.getText().trim());
					Event.count = String.valueOf(eventCount_TextField.getText().trim());
					break;
				case 1 : // ������ �߻� �� ������
					Event.mode = "4";
					Event.duration = String.valueOf(eventDuration_TextField.getText().trim());
					Event.count = String.valueOf(eventCount_TextField.getText().trim());
					break;
				case 2 : // ������ �ð����� ���°� ���ӵ� ���
					Event.mode = "1";
					Event.duration = String.valueOf(eventDuration_TextField.getText().trim());
					Event.count = String.valueOf(eventCount_TextField.getText().trim());
					break;
				case 3 : // ������ �ð����� ������ Ƚ����ŭ ������ �߻��� ���
					Event.mode = "3";
					Event.duration = String.valueOf(eventDuration_TextField.getText().trim());
					Event.count = String.valueOf(eventCount_TextField.getText().trim());
					break;					
				}
								
				// �̺�Ʈ ��� ���� ----------------------------------------------
				// �⺻ �� : �̻��
				Event.enable = "0";
				
				// �̺�Ʈ �ڵ� ���� ----------------------------------------------
				Event.autoClose = String.valueOf(eventAutoClose_CheckBox.isSelected()).toUpperCase();
				
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s%s\n", Util.colorBlue("Save Event Setting"), Util.separator));
				sb.append("�̺�Ʈ ���� ������ ����Ǿ����ϴ�" + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		actualPanel.add(saveButton);
		
		JButton resetButton = new JButton("\uCD08\uAE30\uD654");
		resetButton.setForeground(Color.BLACK);
		resetButton.setFont(FontManager.getFont(Font.BOLD, 15));
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.setBorder(UIManager.getBorder("Button.border"));
		resetButton.setBackground(Color.WHITE);
		resetButton.setBounds(638, 13, 88, 38);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// �̺�Ʈ �̸�
				eventName_TextField.setText("�̺�Ʈ");
				
				// �ɰ���
				eventSeverity_ComboBox.setSelectedIndex(0);
				
				// �Ӱ� ��
				eventThreshold_TextField.setText("1");
				
				// �̺�Ʈ �ߵ� ����
				eventOperation_ComboBox.setSelectedIndex(0);
				
				// �̺�Ʈ ���
				eventMode_ComboBox.setSelectedIndex(0);
				eventDuration_TextField.setText("10");
				eventCount_TextField.setText("10");			
				
				// �̺�Ʈ �޽���
				eventMessage_TextArea.setText("");
				
				// �̺�Ʈ �ڵ� ���� �ɼ�
				eventAutoClose_CheckBox.setSelected(true);		
				
				// �̺�Ʈ ���� �ʱ�ȭ �� ���� ù��° �ʵ�� ��Ŀ�� �̵�
				eventName_TextField.requestFocus();
			}
		});
		
		
		initEventInformation();
		
		actualPanel.add(resetButton);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	
	public void initEventInformation() {
		// �̺�Ʈ �̸�
		eventName_TextField.setText(Event.name);
		
		// �̺�Ʈ �ɰ���		
		int severity = (Integer.parseInt(Event.severity)/10) - 1;
		eventSeverity_ComboBox.setSelectedIndex(severity);
		
		// �Ӱ� ��
		eventThreshold_TextField.setText(Event.threshold);
		
		// �̺�Ʈ ����
		int operation = 0;
		switch(Event.op) {		
		case ">=" :
			operation = 0;
			break;
		case "<=" : 
			operation = 1;
			break;
		case ">" : 
			operation = 2;
			break;
		case "<" :
			operation = 3;
			break;
		case "=" :
			operation = 4;
			break;
		case "<>" :
			operation = 5;
			break;			
		}
		eventOperation_ComboBox.setSelectedIndex(operation);
		
		// �̺�Ʈ ���
		int mode = 0;
		switch(Event.mode) {
		case "5" : // ���� �߻��� �ѹ��� �˸�
			mode = 0;
			break;
		case "4" : // ������ �߻��� ������
			mode = 1;
			break;
		case "1" : // ������ �ð����� ���°� ���ӵ� ���
			mode = 2;
			break;
		case "3" : // ������ �ð����� ������ Ƚ����ŭ ������ �߻��� ���
			mode = 3;
			break;			
		}
		eventMode_ComboBox.setSelectedIndex(mode);
		
		// ���� �ð�
		eventDuration_TextField.setText(Event.duration);
		
		// �̻� �߻� Ƚ��
		eventCount_TextField.setText(Event.count);				
		
		// �̺�Ʈ �޽���
		eventMessage_TextArea.setText(Event.message);
		
		// �̺�Ʈ �ڵ� ���� �ɼ�
		eventAutoClose_CheckBox.setSelected(Boolean.parseBoolean(Event.autoClose.toLowerCase()));
	}
	
	
	// �̺�Ʈ �̸� �˻�
	public boolean checkEventName(boolean vaild) {
		try {
			if(eventName_TextField.getText().length() >= 1) {
				String name = eventName_TextField.getText().trim();				
				if(!Inspecter.isVaildName(name)) throw new Exception("�̺�Ʈ �̸� �ʵ� ����");
			}else {
				throw new Exception("�̺�Ʈ �̸� �ʵ� ����");
			}
		}catch (Exception e) {
			vaild = false;									
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s\n", Util.colorRed("Event Validation Exception")));
			
			if(e.getMessage().contains("����")) {
				sb.append(String.format("%s �ʵ� ������ �Է����ּ���%s\n",Util.colorBlue("�̺�Ʈ �̸�") ,Util.separator));					
			}else {
				sb.append(String.format("%s �ʵ忡�� �Ʒ��� Ư�� ���ڸ� ������ Ư�� ���ڴ� ���� �� �� �����ϴ�%s\n",Util.colorBlue("�̺�Ʈ �̸�") ,Util.separator));
				sb.append(String.format("\n�̺�Ʈ �̸� ���� ��� Ư�� ���� : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n" ,Util.separator));
			}
			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);			
			eventName_TextField.requestFocus();
		}finally {
			return vaild;
		}
	}
	
	
	// �̺�Ʈ �Ӱ� �� �˻�
	public boolean checkThreshold(boolean vaild) {
		try {
			if(eventThreshold_TextField.getText().length() >= 1) {
				double value = Double.parseDouble(eventThreshold_TextField.getText());
			}else {
				throw new Exception("�Ӱ� �� �ʵ� ����");
			}
		}catch (Exception e) {
			vaild = false;
			
			if(e.getMessage().contains("����")) {
				Util.showMessage(String.format("%s\n%s �ʵ� ������ �Է����ּ���%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("�Ӱ� ��") ,Util.separator), JOptionPane.ERROR_MESSAGE);
			}else {
				Util.showMessage(String.format("%s\n%s �ʵ�� ���ڸ� �Է� �Ͻ� �� �ֽ��ϴ�%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("�Ӱ� ��") ,Util.separator), JOptionPane.ERROR_MESSAGE);	
			}
									
			eventThreshold_TextField.requestFocus();
		}finally {
			return vaild;
		}
	}
	
	
	// ���� �ð� �˻�
	public boolean checkDuration(boolean vaild) {
		try {
			if(eventDuration_TextField.getText().length() >= 1) {
				int value = Integer.parseInt(eventDuration_TextField.getText());
				if(value < 1) throw new Exception("���� �ð� �ʵ� ����");
			}else {
				throw new Exception("���� �ð� �ʵ� ����");
			}
		}catch (Exception e) {
			vaild = false;
			
			if(e.getMessage().contains("����")) {				
				Util.showMessage(String.format("%s\n%s �ʵ� ������ �Է����ּ���%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("���� �ð�") ,Util.separator), JOptionPane.ERROR_MESSAGE);
			}else {
				Util.showMessage(String.format("%s\n%s �ʵ�� 1 �̻��� ���� ���� ���� �Է� �Ͻ� �� �ֽ��ϴ�%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("���� �ð�") ,Util.separator), JOptionPane.ERROR_MESSAGE);
			}			
			
			eventDuration_TextField.requestFocus();
		}finally {
			return vaild;
		}
	}
	
	
	// �̻� �߻� Ƚ�� �˻�
	public boolean checkCount(boolean vaild) {
		try {			
			if(eventCount_TextField.getText().length() >= 1) {
				int value = Integer.parseInt(eventCount_TextField.getText());				
				if(value < 1) throw new Exception("�̻� �߻� Ƚ�� �ʵ� ����");															
			}else {
				throw new Exception("�̻� �߻� Ƚ�� �ʵ� ����");
			}
		}catch (Exception e) {
			vaild = false;
			
			if(e.getMessage().contains("����")) {				
				Util.showMessage(String.format("%s\n%s �ʵ� ������ �Է����ּ���%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("�̻� �߻� Ƚ��") ,Util.separator), JOptionPane.ERROR_MESSAGE);
			}else {
				Util.showMessage(String.format("%s\n%s �ʵ�� 1 �̻��� ���� ���� ���� �Է� �Ͻ� �� �ֽ��ϴ�%s\n", Util.colorRed("Event Validation Exception"),Util.colorBlue("�̻� �߻� Ƚ��"), Util.separator), JOptionPane.ERROR_MESSAGE);
			}
			
			eventCount_TextField.requestFocus();
		}finally {
			return vaild;
		}
	}		
	
	public void showDuration(boolean isShow) {
		eventDuration.setVisible(isShow);
		eventDuration_TextField.setVisible(isShow);
		eventDuration_value2.setVisible(isShow);
		
		if(!isShow) {
			eventDuration_TextField.setText("10");
		}
	}

	public void showCount(boolean isShow) {
		eventCount.setVisible(isShow);
		eventCount_TextField.setVisible(isShow);
		eventCount_value2.setVisible(isShow);
		
		if(!isShow) {
			eventCount_TextField.setText("10");
		}
	}		
	
	
	@Override
	public void dispose() {
		EventInfoFrame.isExist = false;
		super.dispose();
	}
}
