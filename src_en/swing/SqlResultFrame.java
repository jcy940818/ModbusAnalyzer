package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import src_en.database.DbUtil;
import src_en.util.FileDownloader;
import src_en.util.JSystemFileChooser;
import src_en.util.TableFilter;
import src_en.util.TableUtil;
import src_en.util.Util;

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
		
		// ???????? ???????? ????????????.
		TableLoadThread = new TableUtil(this, table, rs, sqlServerInfo, queryDetail, agentType);
		TableLoadThread.start();
		
		scrollPane.setViewportView(table); // ?????? ???? ?????? ???? ???????? ??????						
		
		
		//  ?????? ???? ?????? ????????????????????????????????????????????????????
		// ???????? ?????? ???? ?? ?? ???? actualPanel?? ?????? ???????? ??????.
		// ?? ?? ???????? ContentPane ?????? Layout ?????? ?????? BoderLayout ???? ????????
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	actualPanel.setBounds(contentPane.getBounds());            	                       
            }
        });	
						
		setLocationRelativeTo(null); // ???????? ???? ?????????? ????????.		
	}
	
	// ?????? ???????? ?????? ???? ???? ?? ????????
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

	// ???? ???????? ?????????? ?????? ???? ?????? ????
	public void showExecuteQuery() {		
		new MessageFrame(String.format("<html><font color='blue'>%s</font> Executed query</html>", this.sqlServerInfo), this.executeQueryDetail);		
	}
	
	// ???? ?????? ???? ?????? ???????? ???????? ????
	public Thread getTabelLoadThread() {
		return this.TableLoadThread;
	}	
	
	// ?????? ???? ?? ?????? ??????
	class MyKeyListener extends KeyAdapter{	
		public void keyPressed(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) showFilter();
		}		
		
		public void keyReleased(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) showFilter();		
		}		
	}
	
	// ?????? ???? ?????? ?????? ??????
	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 if (e.getButton() == 1) {  } // ???? ????
			 if (e.getClickCount() == 2) { showFilter(); } // ???? ????
			 if (e.getButton() == 3) { userChoice(); } // ?????? ????				
		}
	}
	
	public void userChoice() {				
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='blue'>SQL Result Table</font>\n");
		msg.append("What function would you like to use?\n");

		int menu = Util.showOption(msg.toString(), new String[] { "Table Filter", "Check the Query", "Save the Table", "Cancel" }, JOptionPane.QUESTION_MESSAGE);
				
		switch (menu) {
			case -1: // ???????? ?????? ???????? ???? ?????????? ?????? ??
				return;
			case 0: // ?????? ????
				showFilter();
				break;
			case 1: // ???? ???? ????
				showExecuteQuery();
				break;
			case 2: // ???? ???????? ????
				saveTable();
				break;
			case 3: // ?? ??
				return;
		}
		
	}
	
	// ?????? ???? ?????? ---------------------------------------------------
	public void showFilter() {		
		if(this.isShowFilter()) {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("Table Filter Already Exists") + Util.separator + "\n");
			sb.append("The table filter function is already in use" + Util.separator + "\n");
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
			msg.append("<font color='red'>Can't save File</font>\n");
			msg.append("Currently downloading the file you requested first" + Util.separator + Util.separator + "\n\n");
			msg.append(String.format("Path : %s%s\n", FileDownloader.currentDownloadFile, Util.separator));
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (rowCount >= 1000000) {
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>Can't save File</font>\n");			
			msg.append("The number of rows in the table exceeds 1,000,000 and cannot be downloaded" + Util.separator + Util.separator + "\n");			
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		} else if (rowCount >= 300000) {
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>File saving warning</font>\n");
			msg.append("Currently, the number of rows in the table you want to download exceeds 300,000" + Util.separator + Util.separator + "\n\n");
			msg.append("You can download it, but I don't recommend it\n\n");
			msg.append("Do you want to download the current table?\n");
			option = Util.showConfirm(msg.toString());

			if (option == JOptionPane.YES_OPTION) {
				// ???????? ?????? ????????????
				String FilePath = Util.getFilePath();
				if (FilePath != null) saveResultTable(table, FilePath);
			} else {
				Util.showMessage("<font color='red'>Cancel downloading the file</font>"
						+ "\n"
						+ "The download of the file has been canceled"
						+ Util.separator
						+ "\n", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			// ???????? ?????? ????????????
			String FilePath = Util.getFilePath();
			if (FilePath != null) saveResultTable(table, FilePath);
		}
	}
	
	public void saveResultTable(JTable table, String savePath) {
		// ???????? ?????? ????????????
		FileDownThread = new FileDownloader(table, savePath);
		FileDownThread.start();
	}
	

	// FileChooser ?????? ?????? ?????????? ??????
	public static void setFileChooserFont(Component[] comp) {
		java.awt.Font font = new java.awt.Font("???? ????", java.awt.Font.PLAIN, 14);
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof Container)
				setFileChooserFont(((Container) comp[i]).getComponents());
			try {
				comp[i].setFont(font);
			} catch (Exception e) {
				// ???????? ???? ??????
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
