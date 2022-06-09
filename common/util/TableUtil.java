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
	
}
