package src_ko.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import common.modbus.ModbusWatchPoint;
import src_ko.util.JavaScript;

class ScanCellRenderer extends DefaultTableCellRenderer {
	
	private String expression = null;
	
	public ScanCellRenderer() { };
	
	public ScanCellRenderer(String search){
		this.expression = search;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
		String scanReulst = null;
		
			try {
				if(table.getValueAt(row, 1) instanceof ModbusWatchPoint) {
					ModbusWatchPoint point = (ModbusWatchPoint)table.getValueAt(row, 1);
					scanReulst = point.getData().getPureValue().toString();
				}else {
					scanReulst = table.getValueAt(row, 3).toString();	
				}
			}catch(Exception ex) {
				// Å×ÀÌºí ¼¿¿¡ ³»¿ëÀÌ ¾øÀ» °æ¿ì (Á¦¾î ¼º°ø °á°ú µî)						
				return c;
			}
		
			try {												
				if (scanReulst.contains("Exception") ||
					scanReulst.contains("Data Mismatch") ||
					scanReulst.contains("Response Timeout") ||
					scanReulst.contains("Unprocessable") ||					
					scanReulst.contains("CRC Error") ||
					scanReulst.contains("Unknown")) {
					// Not Good
					if (isSelected) {
						c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
						c.setBackground(new Color(0, 120, 215));
						c.setForeground(new Color(255, 255, 255));
						return c;
					}else{
						c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
						c.setForeground(Color.BLACK);
						c.setBackground(Color.RED);
						return c;
					}
				}else {
					// Good
					if (isSelected) {				
						c.setBackground(new Color(0, 120, 215));
						c.setForeground(new Color(255, 255, 255));
					}else{
						c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
						c.setForeground(new Color(0, 0, 0));
						c.setBackground(Color.WHITE);
					}
				}
			
			try {
				// Search Value Scan
				if(expression != null) {
					boolean result = false;					
					
					result = (boolean)JavaScript.eval(expression.trim() , scanReulst);
					
					if(result) {
						if (isSelected) {
							c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
							c.setBackground(new Color(0, 120, 215));
							c.setForeground(new Color(255, 255, 255));
						}else{				
							c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
							c.setForeground(Color.BLACK);
							c.setBackground(Color.GREEN);
						}
					}
				}
			}catch(Exception e) {
				// Search Value Scan Expression Error
				e.printStackTrace();
			}
	
			return c;			
			
		}catch(Exception e) {
			e.printStackTrace();
			return c;
		}
			
	}
		
}