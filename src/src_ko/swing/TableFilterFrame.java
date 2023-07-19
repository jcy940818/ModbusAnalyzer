package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.util.TableFilter;
import src_ko.util.Util;

public class TableFilterFrame extends JFrame {

	public static boolean isExist = false;
	
	SqlResultFrame resultFrame;
	
	private boolean isSave = false;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public TableFilterFrame(SqlResultFrame frame,JTable table) {
		
		resultFrame = frame;
		resultFrame.setShowFilter(true);
		
		TableFilterFrame.isExist = true;
		
		this.table = table;
		
		addKeyListener(new MyKeyListener());
		setTitle("ModbusAnalyzer");
		setIconImage(new Util().getIconResource().getImage());				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 508, 180);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(new LineBorder(new Color(255, 140, 0), 5));
		actualPanel.setForeground(new Color(0, 0, 0));
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel lblTableFilter = new JLabel("Table Filter");
		lblTableFilter.setForeground(Color.BLACK);
		lblTableFilter.setHorizontalAlignment(SwingConstants.LEFT);
		lblTableFilter.setFont(FontManager.getFont(Font.BOLD, 20));
		lblTableFilter.setBackground(Color.WHITE);
		lblTableFilter.setBounds(12, 10, 186, 54);
		lblTableFilter.setIcon(new Util().getSubLogoResource());
		actualPanel.add(lblTableFilter);
		
		textField = new JTextField();
		textField.setForeground(Color.BLACK);
		textField.setFont(FontManager.getFont(Font.PLAIN, 18));
		textField.setBackground(Color.WHITE);
		textField.setBounds(22, 80, 447, 34);
		textField.addFocusListener(Util.focusListener);
		textField.setColumns(10);
		textField.addKeyListener(new MyKeyListener());
		textField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				isSave = true;
				dispose();
			}
		});
		actualPanel.add(textField);
		
		new TableFilter(table, textField);
		
		// 프레임이 화면 가운데에서 생성된다		
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		TableFilterFrame.isExist = false;
		
		if(!isSave) {
			textField.setText(null);
			this.table.setRowSorter(null);
		}
		
		resultFrame.setShowFilter(false);
		super.dispose();
	}
	
	// 사용자 정의 키 이벤트 리스너
	class MyKeyListener extends KeyAdapter{	
		public void keyPressed(KeyEvent e) {			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				isSave = false;				
				dispose();
			}
		}		
		
		public void keyReleased(KeyEvent e) {			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				isSave = false;
				dispose();
			}
		}		
	}
	
}
