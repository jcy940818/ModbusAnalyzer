package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.util.Util;

public class TemplateFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable table; // frame마다 XML 인스턴스를 가져야 하므로 table 필드는 static 속성을 가질 수 없다

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TemplateFrame frame = new TemplateFrame();
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
	public TemplateFrame() {
		TemplateFrame.isExist = true;
		setTitle("Template Frame Title");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
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
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Template Frame");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(FontManager.getFont(Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 267, 55);
		actualPanel.add(currentFunction);
		
				
		mk119Button = new JButton(new Util().getMK2Resource());
		mk119Button.setForeground(Color.BLACK);
		mk119Button.setText(" Button");
		mk119Button.setFont(FontManager.getFont(Font.BOLD, 17));
		mk119Button.setFocusPainted(false);
		mk119Button.setContentAreaFilled(false);
		mk119Button.setBorder(UIManager.getBorder("Button.border"));
		mk119Button.setBackground(Color.WHITE);
		mk119Button.setBounds(850, 11, 189, 36);		
		actualPanel.add(mk119Button);
		
			
		// 프레임이 화면 가운데에서 생성된다
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		TemplateFrame.isExist = false;
		super.dispose();
	}
	
}
