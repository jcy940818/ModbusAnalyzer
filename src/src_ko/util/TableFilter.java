package src_ko.util;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilter extends JPanel {

	public TableFilter(JTable table, JTextField textField) {

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());

		table.setRowSorter(rowSorter);

		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					String text = textField.getText();
	
					if (text.trim().length() == 0) {
						rowSorter.setRowFilter(null);
					} else {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
				}catch(Exception exception) {
					exception.printStackTrace();
					System.out.println(exception.getMessage());
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					String text = textField.getText();

					if (text.trim().length() == 0) {
						rowSorter.setRowFilter(null);
					} else {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
				}catch(Exception exception) {
					exception.printStackTrace();
					System.out.println(exception.getMessage());
				}				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,																			
			}
		});
				
	}
}