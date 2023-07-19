package src_en.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_en.database.DbUtil;
import src_en.util.Util;

public class ProgressFrame extends JFrame {

	JPanel contentPane;
	JLabel lblNewLabel;
	JLabel iconLabel;
	JLabel progressLabel;
	JProgressBar progressBar;
	
	private double progress;
	private boolean isRunning = true;
	
	
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		ProgressFrame p = new ProgressFrame();
//		p.go();
//	}

	/**
	 * Create the frame.
	 */
	public ProgressFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("ModbusAnalyzer");			
		setIconImage(new Util().getIconResource().getImage());
		setBounds(100, 100, 611, 200);
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
		
		lblNewLabel = new JLabel("SQL : Table Loading..");
		lblNewLabel.setFont(FontManager.getFont(Font.BOLD, 21));
		lblNewLabel.setBounds(105, 32, 224, 55);
		contentPane.add(lblNewLabel);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setOpaque(true);
		progressBar.setBackground(Color.WHITE);
		progressBar.setBounds(23, 107, 556, 34);
		contentPane.add(progressBar);
		
		progressLabel = new JLabel("0%");
		progressLabel.setBounds(327, 32, 121, 55);
		progressLabel.setFont(FontManager.getFont(Font.BOLD, 25));
		contentPane.add(progressLabel);	
		
		setVisible(true);		
		setLocationRelativeTo(null);		
	}
	
	public void go() {
		try {
			for(int i = 0; i <= 3000000; i++) {
				updateProgress(getPercent(i, 3000000));
				Thread.sleep(0);
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setRunningProgress(boolean isRunnung) {
		this.isRunning = isRunnung;
	}
	
	public boolean isRunningProgress() {
		return this.isRunning;
	}
	
	public void updateProgress(double progress) {
		if(progress >= 100) this.dispose(); // ���൵�� 100%�� �Ǹ� �ڵ����� �������� ����ȴ�.
		progressLabel.setText(String.format("%.1f", progress) + "%");
		progressBar.setValue((int)progress);
		this.progress = progress;
	}
	
	public static double getPercent(double current, double total) {
		return ((double)current/(double)total)*100;
	}
	
	public void dispose() {
		if(!(this.progress >= 100)) this.setRunningProgress(false);
		// ����ڰ� PorgressFrame ���� �� ���� �۾��� ���������� �����ϱ����Ͽ� endSQL()�� ����������
		// ����� �������� ��� ���̺� �ε� �۾� �Ϸ�� dispose() �޼ҵ尡 ������ ����ȴ�.
		// ��ɻ� ������ ������, �α׿� SQL ���� ������ ������ ������ ������ �ִ�
		DbUtil.endSQL();
		this.setVisible(false);
		super.dispose();		
	}	
	
}
