package common.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import common.server.SystemSeverity;

public class SeverityRenderer extends DefaultTableCellRenderer {

	private int colNum = 0;
	private int fontSize = 0;
	private ArrayList<SystemSeverity> severitys = null;	
	
	public SeverityRenderer(int colNum, int fontSize, ArrayList<SystemSeverity> severitys){
		this.colNum = colNum;
		this.fontSize = fontSize;
		this.severitys = severitys;		
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if(table.getValueAt(row, this.colNum) == null) {
			return c;
		}
		
		String cellValue = table.getValueAt(row, this.colNum).toString();

		try {

			if (severitys.size() < 1) {
				if (isSelected) {
					c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
					c.setBackground(table.getSelectionBackground());
					c.setForeground(table.getSelectionForeground());
					return c;
				}else{
					c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
					c.setForeground(new Color(0, 0, 0));
					c.setBackground(Color.WHITE);
					return c;
				}
				
			}else {
				
				for(int i = 0; i < severitys.size(); i++) {
					SystemSeverity severity = severitys.get(i);
					
					if(cellValue.equalsIgnoreCase(severity.getStrSeverity())) {
						if (isSelected) {
							c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
							c.setBackground(table.getSelectionBackground());
							c.setForeground(table.getSelectionForeground());
							return c;
						} else {
							c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
							c.setBackground(new Color(severity.getnBkColor()));
							c.setForeground(new Color(severity.getnTextColor()));
							return c;
						}
					}
				}
				
				if (isSelected) {
					c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
					c.setBackground(table.getSelectionBackground());
					c.setForeground(table.getSelectionForeground());
					return c;
				}else{
					c.setFont(FontManager.getFont(Font.PLAIN, fontSize));
					c.setForeground(new Color(0, 0, 0));
					c.setBackground(Color.WHITE);
					return c;
				}
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return c;
		}
		
	}

}
