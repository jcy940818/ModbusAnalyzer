package src_ko.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import common.util.FontManager;
import src_ko.database.DbUtil;
import src_ko.exception.TableLoadException;
import src_ko.info.ONION_Info;
import src_ko.swing.MessageFrame;
import src_ko.swing.ProgressFrame;
import src_ko.swing.SqlResultFrame;


public class TableUtil extends Thread{
	
	private static final int limitRowCount = 1000000;
	
	private SqlResultFrame sqlResultFrame;
	private JTable table;
	private ResultSet rs; // TableUtil �ν��Ͻ����� �������� ResultSet �ʵ带 ���´�
	private String sqlServerInfo;
	private String queryDetail;	
	private String agentType;
	
	public TableUtil(SqlResultFrame sqlResultFrame,JTable table, ResultSet rs, String sqlServerInfo , String queryDetail, String agentType) {
		this.sqlResultFrame = sqlResultFrame;
		this.table = table;
		this.rs = rs;
		this.sqlServerInfo = sqlServerInfo;
		this.queryDetail = queryDetail;
		this.agentType = agentType;
	}
	
	public void run() {
		try {					
			
			if(this.agentType.equalsIgnoreCase("databaseAccess")) {
				this.updateTable(table, rs);
			}else if(this.agentType.equalsIgnoreCase("storedProcedure")){
				this.updateProcedureTable(table, rs);
			}else {
				this.updateTable(table, rs);
			}
			
			
			rs.close();
			
			if(rs.isClosed()) {
				rs = null;
			}else {
				
			}
			
		}catch(SQLException e) {											
			Util.showMessage(String.format("<font color='red'>SQL Exception</font>\n%s%s%s\n", e.getMessage(), Util.longSeparator, Util.longSeparator, System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		}catch(TableLoadException e) {
			Util.showMessage(String.format("<font color='red'>��� ���̺� �ε� ���</font>\n%s&nbsp;&nbsp;&nbsp;%s", e.getMessage(), System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		}catch (Exception e) {
			Util.showMessage(String.format("<font color='red'>Unknown Exception</font>\n%s%s%s\n", e.getMessage(), Util.longSeparator, Util.longSeparator, System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		} finally {
			rs = null;			
			// SQL �۾��� ����
			// �ش� �ڵ� ������ n�� �� ��ȸ ������ ����ǰ� endSQL() �޼ҵ尡 ����Ǵ� ������ �־���
			// DbUtil.endSQL(); 			
		}
	}
	
		
	public void updateTable(JTable table, ResultSet rs) throws SQLException, TableLoadException {
		
		// ���� Ŀ�ؼ��� MK119 Ŀ�ؼ� �϶��� ���� n�� ���̺��� ��ȸ�� ������ ����ڿ��� �����
		if(DbUtil.checkMK119Db(ONION_Info.getMk119Connection())) {
			int rowCount = DbUtil.getRowCount(rs);
			
			// ��� ���̺��� ��(Row Count) : 100���� �̻� �� ��� ����ڿ��� ��ȭ���� ǥ��
			if(rowCount >= limitRowCount) {
				StringBuilder msg = new StringBuilder(); 
				msg.append(String.format("<font color='red'>SQL Warning</font>\n��� ���̺��� �� ������ �ʹ� �����ϴ�%s%s\n\n", Util.longSeparator,Util.longSeparator));
				msg.append(String.format("��� ���̺� �� ����(Row Count) : <font color='blue'>%d</font>\n\n", rowCount));
				msg.append("( �ش� ��ȭ���ڴ� ��� ���̺��� �� ������ 1,000,000�� �̻� �� ��� ��Ÿ���ϴ� )" + Util.longSeparator + "\n");								
				msg.append("( ��� ���̺��� �� ������ 1,000,000�� �̻��� ��� <font color='red'>.xlsx ���Ϸ� ���� �� �� �����ϴ�</font> )\n");
				
				int menu =  Util.showOption(msg.toString(), new String[] {"��� ����","���� n�� �� ��ȸ","���� ���� ���븸 Ȯ��" ,"���� ���"}, JOptionPane.WARNING_MESSAGE);				
				
				switch (menu) {
				case 0: // ��� ����
					break;
				case 1: // ���� n�� �� ��ȸ
					String newRowCountString = (String)Util.showInput(String.format("<font color='blue'>���̺� ���� n�� �� ��ȸ</font>\n��ȸ�Ͻ� ���� ����(Row Count)�� �Է����ּ���%s%s\n", Util.longSeparator, Util.longSeparator), JOptionPane.QUESTION_MESSAGE);
					if(newRowCountString == null) throw new SQLException("��ȸ �� ���̺��� ���� n�� �� ����(Row Count) �Է��� ����Ͽ����ϴ�");
					
					// ����ڿ��� �Է¹��� ��ȸ �� ���̺��� ���� n�� �� ����
					int newRowCount;
					
					try {						
						newRowCount = Integer.parseInt(newRowCountString);						
					}catch(NumberFormatException e) {
						rs.close();
						
						if(rs.isClosed()) {
							rs = null;
							System.out.println("[ TableUtil.updateTable() : ���� n�� ���̺� ��ȸ �Է¿� �����Ͽ� ResultSet �ν��Ͻ� ��ȯ �Ϸ� ]");
						}
						
						throw new SQLException("��ȸ�Ͻ� ���̺� ���� n�� �� ���� �Է� ������ 1 �̻��� ������ �Է� �Ͻ� �� �ֽ��ϴ�");
					}
							
					SqlResultFrame.DisposeFrame(sqlResultFrame); // ������ ���� �۾����̴� SqlResultFrame �ν��Ͻ� Dispose					
										
					if(DbUtil.getSELECT().contains("TOP")) {
						String[] selectToken = DbUtil.getSELECT().split(" ");
						
						StringBuilder newSelect = new StringBuilder(String.format("SELECT TOP %d ", newRowCount));  
						
						for(int i = 3; i < selectToken.length; i++) {
							
							if(selectToken[i].equalsIgnoreCase("")) continue; // �����̸� ����
							selectToken[i] = selectToken[i].trim().replaceAll(" ", "");
							
							if(selectToken[i].contains(",")) {
								newSelect.append(selectToken[i]);
							}else {
								newSelect.append(" " + selectToken[i] + " ");
							}
						}
											
						DbUtil.setSELECT(newSelect.toString());
					}else {
						DbUtil.setSELECT(DbUtil.getSELECT().replace("SELECT", String.format("SELECT TOP %d ", newRowCount)));
					}
																							
					System.out.println(String.format("[ TableUtil.updateTable() n�� �� ��ȸ ���� : %s ]", DbUtil.getQuery()));
					DbUtil.executeQuery(this.sqlServerInfo ,DbUtil.getQuery());
					return;
				case 2 : // ���� ���� ���븸 Ȯ�� 
					new MessageFrame(String.format("<html><font color='blue'>%s</font> ���� ���� Ȯ��</html>", this.sqlServerInfo), queryDetail);
					return;
				case -1 : // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� ������ ��
				case 3: // ���� ���
					rs.close();
					if(rs.isClosed())System.out.println("\n[ TableUtil.updateTable() : ������� ��û�� ���� ��� ���̺� �ε� �۾� ��� �� ResultSet �ν��Ͻ� ��ȯ�� �����Ͽ����ϴ� ]\n");
					throw new SQLException("��� ���̺� �ε� �۾��� ����Ͽ����ϴ�");		
				}
			}
		}// Ŀ�ؼ� �˻� �� ���� n�� �� ���̺� ��ȸ �˻�
		
		// ���̺� ��� ����
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// ���̺� �� ����
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowMargin(3);
		table.setRowHeight(25);
				
		// ���̺� �ʱ�ȭ
			
		table.setModel(new DefaultTableModel(TableUtil.getOrderedResultSetTable(rs), DbUtil.getOrderedColumnNames(rs)) {
			// ���̺� �� ���� ���� ����
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});
		
		// �� ũ�� ���� ���� �Ұ�, �� ��ġ �̵� �Ұ�
		table.getTableHeader().setReorderingAllowed(false); // �� ��ġ ����
//		table.getTableHeader().setResizingAllowed(false); // �� ���̵� ũ��� ���� �����ϴ� 
				
		TableUtil.setFirtColumnCenter(table); // ù��° �÷� ���� ��� ����		//		
		autoColSize(table, table.getModel());
		resizeColumnWidth(table); // ���� �÷��� ���뿡 �°� ũ�⸦ �������ش�.
		
		// �ϼ��� ��� ���̺��� �����ش� SqlResultFrame.setVisible(true)
		SqlResultFrame.ResultSetloadSuccess(sqlResultFrame);
		
		// ���α׷����� ���� �۾��� ����Ǿ����� �˸���
		DbUtil.endSQL();
	}	
	
	
	
	public void updateProcedureTable(JTable table, ResultSet rs) throws SQLException, TableLoadException {
			
			// ���� Ŀ�ؼ��� MK119 Ŀ�ؼ� �϶��� ���� n�� ���̺��� ��ȸ�� ������ ����ڿ��� �����
			if(DbUtil.checkMK119Db(ONION_Info.getMk119Connection())) {
				int rowCount = DbUtil.getRowCount(rs);
				
				// ��� ���̺��� ��(Row Count) : 100���� �̻� �� ��� ����ڿ��� ��ȭ���� ǥ��
				if(rowCount >= limitRowCount) {
					StringBuilder msg = new StringBuilder(); 
					msg.append(String.format("<font color='red'>SQL Warning</font>\n��� ���̺��� �� ������ �ʹ� �����ϴ�%s%s\n\n", Util.longSeparator,Util.longSeparator));
					msg.append(String.format("��� ���̺� �� ����(Row Count) : <font color='blue'>%d</font>\n\n", rowCount));
					msg.append("( �ش� ��ȭ���ڴ� ��� ���̺��� �� ������ 1,000,000�� �̻� �� ��� ��Ÿ���ϴ� )" + Util.longSeparator + "\n");								
					msg.append("( ��� ���̺��� �� ������ 1,000,000�� �̻��� ��� <font color='red'>.xlsx ���Ϸ� ���� �� �� �����ϴ�</font> )\n");
					
					int menu =  Util.showOption(msg.toString(), new String[] {"��� ����", "���� ���� ���븸 Ȯ��" ,"���� ���"}, JOptionPane.WARNING_MESSAGE);				
					
					switch (menu) {
					case 0: // ��� ����
						break;
					case 1 : // ���� ���� ���븸 Ȯ�� 
						new MessageFrame(String.format("<html><font color='blue'>%s</font> ���� ���� Ȯ��</html>", this.sqlServerInfo), queryDetail);
						DbUtil.endSQL();
						return;
					case -1 : // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� ������ ��
					case 2: // ���� ���
						rs.close();
						throw new SQLException("��� ���̺� �ε� �۾��� ����Ͽ����ϴ�");		
					}
				}
			}// Ŀ�ؼ� �˻� �� ���� n�� �� ���̺� ��ȸ �˻�
			
			// ���̺� ��� ����
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getTableHeader().setBackground(new Color(255, 255, 153));
			table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
			
			// ���̺� �� ����
			table.setBorder(new EmptyBorder(0, 3, 0, 0));
			table.setFont(FontManager.getFont(Font.PLAIN, 15));
			table.setRowMargin(3);
			table.setRowHeight(25);
					
			// ���̺� �ʱ�ȭ
				
			table.setModel(new DefaultTableModel(TableUtil.getOrderedResultSetTable(rs), DbUtil.getOrderedColumnNames(rs)) {
				// ���̺� �� ���� ���� ����
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
			// �� ũ�� ���� ���� �Ұ�, �� ��ġ �̵� �Ұ�
			table.getTableHeader().setReorderingAllowed(false); // �� ��ġ ����
	//		table.getTableHeader().setResizingAllowed(false); // �� ���̵� ũ��� ���� �����ϴ� 
					
			TableUtil.setFirtColumnCenter(table); // ù��° �÷� ���� ��� ����		
			autoColSize(table, table.getModel());
			resizeProcedureColumnWidth(table);
			
			// �ϼ��� ��� ���̺��� �����ش� SqlResultFrame.setVisible(true)
			SqlResultFrame.ResultSetloadSuccess(sqlResultFrame);
			
			// ���α׷����� ���� �۾��� ����Ǿ����� �˸���
			DbUtil.endSQL();
		}
	
	
	
	
	
	
	
	
	
	// ���̺� ����� ���뿡 �°� ���̵带 �������ش�.
	private static void autoColSize(JTable table, TableModel model) {
		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;
		int font = 11;

		for (int i = 0, n = model.getColumnCount(); i < n; i++) {

			column = table.getColumnModel().getColumn(i);
			try {
				headerWidth = column.getHeaderValue().toString().length() * font;
				column.setPreferredWidth(headerWidth);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	
	// ���̺� �÷��� ���̵� ũ�⸦ ���� ���뿡 �°� �����ش�.
	public static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
//		    for (int column = 0; column < table.getColumnCount(); column++) { // ��� ����
		for (int column = 0; column < 1; column++) { // ù��° �ุ
			int width = 50; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width + 3);
		}
	}
	
	
	// ���̺� �÷��� ���̵� ũ�⸦ ���� ���뿡 �°� �����ش�.
	public static void resizeProcedureColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) { // ��� ����
			int width = 90;
			
			// ù��° �÷�(����)�� 50
			width = (column != 0) ? 90 : 50;						
			
			for (int row = 0; row < table.getRowCount(); row++) {								
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			
			if(column != 0) width = width + 5; // ù��° ���� �����ϰ� �ʺ� ������ �ش�
			
			columnModel.getColumn(column).setPreferredWidth(width + 3);
		}
	}
	
	
	/**
	 * ResultSet ����� 2���� �迭 ���·� ����
	 */
	public static Object[][] getResultSetTable(ResultSet rs) throws SQLException {
		rs.beforeFirst();
		
		// ��� ���̺� ����		
		int resultRowCount = DbUtil.getRowCount(rs);
		int resultColumnCount = DbUtil.getColumnCount(rs);

		Object[][] rowData = new Object[resultRowCount][resultColumnCount];
		
		int index = 0;

		while (rs.next()) {
			rowData[index] = new Object[resultColumnCount];

			for (int i = 0; i < resultColumnCount; i++) {
				rowData[index][i] = String.valueOf(rs.getObject(i + 1));
			}

			index++;
		}
		
		rs.beforeFirst();
		return rowData;
	}
	
	/**
	 * ���� �÷��� ���� ���� ResultSet ����� 2���� �迭 ���·� ����
	 */
	public static Object[][] getOrderedResultSetTable(ResultSet rs) throws SQLException, TableLoadException {
					
		// SqlResultFrame�� ǥ�����ֱ� ���� ���̺� ���ε� ���൵�� ǥ�����ش�.
		ProgressFrame p = new ProgressFrame();
		
		try {
			rs.beforeFirst();
			
			// ��� ���̺� ����		
			int resultRowCount = DbUtil.getRowCount(rs);
			int resultColumnCount = DbUtil.getOrderedColumnNames(rs).length;
	
			Object[][] rowData = new Object[resultRowCount][resultColumnCount];
			
			int index = 0;
	
			while (rs.next()) {
				if(!p.isRunningProgress()) {
					throw new TableLoadException("��� ���̺� �ε� �۾��� ��ҵǾ����ϴ�");
				}
				
				rowData[index] = new Object[resultColumnCount];
	
				for (int i = 0; i < resultColumnCount; i++) {
					if(i == 0) {
						rowData[index][i] = String.valueOf(rs.getRow());
					} else {
						rowData[index][i] = String.valueOf(rs.getObject(i));	
					}
	
					// ���α׷��� �������� ���൵ ������Ʈ : 100%�� �����ϸ� �ڵ����� ���൵ ǥ�� �������� ����ȴ�.
					p.updateProgress(ProgressFrame.getPercent(rs.getRow(), resultRowCount));				
				}
	
				index++;
			}
			
			rs.beforeFirst();		
			return rowData;
		}catch(SQLException e) {
			// �۾��� SQLException�� �߻��ϸ� ProgressFrame�� �����ϰ� ���ܸ� ����
			p.dispose();
			throw e;
		}
	}
	
	
	
	/**
	 * ���ڷ� �Ѱ��� JTable�� ù��° �÷��� ������ ��� �������ش�.
	 * �ַ� �ε��� �÷��� ǥ�����ֱ� ���ؼ� ����
	 */
	public static void setFirtColumnCenter(JTable table) {
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();

		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
	}
	
}
