package src_ko.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.util.Util;

public class MessageFrame extends JFrame {

	JPanel contentPane;
	JLabel MessageLabel;
	JLabel iconLabel;
	JTextPane message_textPane;
	
	/**
	 * Create the frame.
	 */
	public MessageFrame(String title, String msg) {
		setResizable(false);
		setTitle("ModbusAnalyzer");			
		setIconImage(new Util().getIconResource().getImage());
				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 990, 655);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		iconLabel = new JLabel();
		iconLabel.setBounds(25, 22, 71, 75);
		ImageIcon image = new Util().getIconResource2();
		iconLabel.setIcon(image);
		contentPane.add(iconLabel);		
		
		MessageLabel = new JLabel(title);
		MessageLabel.setForeground(Color.BLACK);
		MessageLabel.setFont(FontManager.getFont(Font.BOLD, 21));
		MessageLabel.setBounds(105, 32, 855, 55);
		contentPane.add(MessageLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(64, 64, 64), 2));
		scrollPane.setBounds(25, 107, 935, 498);
		contentPane.add(scrollPane);
		
		message_textPane = new JTextPane();
		message_textPane.setEditable(false);
		message_textPane.setBackground(Color.WHITE);
		message_textPane.setFont(FontManager.getFont(Font.PLAIN, 16));
		message_textPane.setText(msg);
		scrollPane.setViewportView(message_textPane);
						
		message_textPane.setCaretPosition(0);
		
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void updateMessage(String msg) {
		message_textPane.setText(msg);
	}	
}
