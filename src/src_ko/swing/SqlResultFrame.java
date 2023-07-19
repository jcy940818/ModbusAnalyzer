package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import src_ko.database.DbUtil;
import src_ko.util.FileDownloader;
import src_ko.util.TableUtil;
import src_ko.util.Util;

public class SqlResultFrame extends JFrame {
	
	private JPanel contentPane;
	private JTable table;
	private ResultSet rs;
	
	private Thread TableLoadThread = null;
	private Thread FileDownThread = null;		
		
	TableFilterFrame filter;
	private boolean showFilter = false;
	
	private String sqlServerInfo;
	private String executeQueryDetail;
	
	/**
	 * Create the frame.
	 */
	public SqlResultFrame(String sqlServerInfo, String queryDetail, ResultSet rs, String agentType) throws SQLException {		
		this.sqlServerInfo = sqlServerInfo;
		this.executeQueryDetail = queryDetail;
		this.rs = rs;
		setTitle("ModbusAnalyzer : SQL Server " + this.sqlServerInfo);
		setBackground(Color.WHITE);
		setVisible(false);
		setIconImage(new Util().getIconResource().getImage());
		
		addMouseListener(new MyMouseListener());
		addKeyListener(new MyKeyListener());
		
		// JFrame : 1080x680
		// ContentPane : 1050x610		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(MainFrame.getMainFrame().getBounds());		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.getBounds();
		contentPane.setLayout(new BorderLayout(0, 0));
		actualPanel.setBackground(Color.DARK_GRAY);
		contentPane.add(actualPanel);
		actualPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.addMouseListener(new MyMouseListener());
		scrollPane.addKeyListener(new MyKeyListener());
		
		actualPanel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setBackground(Color.WHITE);		
		table.addMouseListener(new MyMouseListener());
		table.addKeyListener(new MyKeyListener());
		
		// ������� ���̺��� ������Ʈ�Ѵ�.
		TableLoadThread = new TableUtil(this, table, rs, sqlServerInfo, queryDetail, agentType);
		TableLoadThread.start();
		
		scrollPane.setViewportView(table); // ��ũ�� ���� �߾ӿ� ��� ���̺��� ���δ�						
		
		
		//  �ڡڡ� �߿� �̺�Ʈ �ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
		// �������� ũ�Ⱑ ���� �� �� ���� actualPanel�� ũ�⵵ �������� ���Ѵ�.
		// �� �� �������� ContentPane �ʵ��� Layout ������ �ݵ�� BoderLayout ���� �ؾ��Ѵ�
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	actualPanel.setBounds(contentPane.getBounds());            	                       
            }
        });	
						
		setLocationRelativeTo(null); // �������� ȭ�� ������� ��Ÿ����.		
	}
	
	// ������ �۾����� ���̺� �ε� ���� �� ȣ���Ѵ�
	public static void ResultSetloadSuccess(JFrame frame) {
		if(frame != null) {
			frame.setVisible(true);	
		}
	}
	
	public static void DisposeFrame(SqlResultFrame frame) {
		if(frame != null) {
			frame.dispose();			
			DbUtil.endSQL();
		}
	}

	// ���� �������� �����Ǵµ� ����� ���� ������ ǥ��
	public void showExecuteQuery() {		
		new MessageFrame(String.format("<html><font color='blue'>%s</font> ���� ���� Ȯ��</html>", this.sqlServerInfo), this.executeQueryDetail);		
	}
	
	// ��� ���̺� �ε� �۾��� �����ϴ� �����带 ��ȯ
	public Thread getTabelLoadThread() {
		return this.TableLoadThread;
	}	
	
	// ����� ���� Ű �̺�Ʈ ������
	class MyKeyListener extends KeyAdapter{	
		public void keyPressed(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) showFilter();
		}		
		
		public void keyReleased(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) showFilter();		
		}		
	}
	
	// ����� ���� ���콺 �̺�Ʈ ������
	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 if (e.getButton() == 1) {  } // ���� Ŭ��
			 if (e.getClickCount() == 2) { showFilter(); } // ���� Ŭ��
			 if (e.getButton() == 3) { userChoice(); } // ������ Ŭ��				
		}
	}
	
	public void userChoice() {				
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='blue'>SQL Result Table</font>\n");
		msg.append("� �۾��� ���� �Ͻðڽ��ϱ�?\n");

		int menu = Util.showOption(msg.toString(), new String[] { "���̺� ����", "���� ���� Ȯ��", "�ٸ� �̸����� ����", "�� ��" }, JOptionPane.QUESTION_MESSAGE);
				
		switch (menu) {
			case -1: // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� ������ ��
				return;
			case 0: // ���̺� ����
				showFilter();
				break;
			case 1: // ���� ���� Ȯ��
				showExecuteQuery();
				break;
			case 2: // �ٸ� �̸����� ����
				saveTable();
				break;
			case 3: // �� ��
				return;
		}
		
	}
	
	// ���̺� ���� �޼ҵ� ---------------------------------------------------
	public void showFilter() {		
		if(this.isShowFilter()) {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("Table Filter Already Exists") + Util.separator + "\n");
			sb.append("���̺� ���� �������� �̹� �����ֽ��ϴ�" + Util.separator + "\n");
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			filter = new TableFilterFrame(this, table);			
		}
	}
	
	public void saveTable() {
		int rowCount = table.getRowCount();
		int option = 0;
		
		if(FileDownloader.running) {
			StringBuilder msg = new StringBuilder();			
			msg.append("<font color='red'>���� �ٿ�ε� �Ұ���</font>\n");
			msg.append("���� ��û�Ͻ� ������ �Ʒ��� ��ο� �ٿ�ε� �� �Դϴ�" + Util.separator +"\n\n");
			msg.append(String.format("Path : %s%s\n", FileDownloader.currentDownloadFile, Util.separator));
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (rowCount >= 1000000) {
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>���� ���� �Ұ���</font>\n");			
			msg.append("�ٿ�ε� �Ϸ��� ���̺��� �� ������ 1,000,000���� �ʰ��Ͽ� �ٿ�ε尡 �Ұ����մϴ�" + Util.separator + "\n\n");
			msg.append("���̺� �˻� ������ �����Ͽ� �� ������ 1,000,000�� ���Ϸ� �������ּ���\n");
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		} else if (rowCount >= 300000) {
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>���� ���� ���</font>\n");
			msg.append("�ٿ�ε� �Ϸ��� ���̺��� �� ������ 300,000���� �ʰ��մϴ�" + Util.separator + "\n\n");
			msg.append("�ٿ�ε�� ���������� �������� �ʽ��ϴ�\n\n");
			msg.append("��� �ٿ�ε� �Ͻðڽ��ϱ�?\n");
			option = Util.showConfirm(msg.toString());

			if (option == JOptionPane.YES_OPTION) {
				// ������� ������ �ٿ�ε��Ѵ�
				String FilePath = Util.getFilePath();
				if (FilePath != null) saveResultTable(table, FilePath);
			} else {
				Util.showMessage("<font color='red'>���� �ٿ�ε� ���</font>\n������� ��û���� ���� �ٿ�ε带 ����Ͽ����ϴ�" + Util.separator + "\n", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			// ������� ������ �ٿ�ε��Ѵ�
			String FilePath = Util.getFilePath();
			if (FilePath != null) saveResultTable(table, FilePath);
		}
	}
	
	public void saveResultTable(JTable table, String savePath) {
		// ������� ������ �ٿ�ε��Ѵ�
		FileDownThread = new FileDownloader(table, savePath);
		FileDownThread.start();
	}
	

	// FileChooser Ŭ���� ��Ʈ�� �������ִ� �޼ҵ�
	public static void setFileChooserFont(Component[] comp) {
		java.awt.Font font = new java.awt.Font("���� ���", java.awt.Font.PLAIN, 14);
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof Container)
				setFileChooserFont(((Container) comp[i]).getComponents());
			try {
				comp[i].setFont(font);
			} catch (Exception e) {
				// �ƹ��͵� ���� �ʴ´�
			}
		}
	}
	

	public void dispose() {
		try {
			filter.dispose();			
		}catch(Exception e) {
		
		}finally {
			super.dispose();
		}
	}

	public boolean isShowFilter() {
		return showFilter;
	}

	public void setShowFilter(boolean showFilter) {
		this.showFilter = showFilter;
	}
	
}
