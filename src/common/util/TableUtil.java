package common.util;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableUtil {
	
	public static void setTableHeader(JTable table, int column, String text) {
		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setHeaderValue(text);
		th.repaint();
	}
	
	public static void setFocusMultipleRows(JTable table, int... rows) {
		if(table == null || rows == null || rows.length < 1) return;

		int columnCount = table.getColumnCount();
		int firstRow = rows[0];
		
		for(int i = 0; i < columnCount; i++) {
			table.changeSelection(firstRow, i, true, false);
		}
		
		for(int i = 0; i < rows.length; i++) {
			table.getSelectionModel().addSelectionInterval(rows[i], rows[i]);
		}
	}
	
}
