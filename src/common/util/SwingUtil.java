package common.util;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

public class SwingUtil {
	
	public static MouseWheelListener getComboBoxWheelListener() {
		return new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				
				JComboBox comboBox = (JComboBox)e.getSource();
				int itemIndex = comboBox.getSelectedIndex();
				
				try {
					if(e.getWheelRotation() < 0) {
						
						if(itemIndex >= comboBox.getItemCount() - 1) 
							return;
						else
							comboBox.setSelectedIndex(++itemIndex);
						
	                }else{
	                	if(itemIndex <= 0) 
	                		return;
	                	else
	                		comboBox.setSelectedIndex(--itemIndex);
	                }
	                
				}catch(Exception ex) {
					ex.printStackTrace();
					comboBox.setSelectedIndex(itemIndex);
				}
			}
		};
	}
	
	
	
	public static MouseWheelListener getPassNullComboBoxWheelListener() {
		return new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				
				JComboBox comboBox = (JComboBox)e.getSource();
				int itemIndex = comboBox.getSelectedIndex();
				
				try {
					if(e.getWheelRotation() < 0) {
						
						if(itemIndex >= comboBox.getItemCount() - 1) {
							return;
						}else {
							boolean isEmpty = comboBox.getItemAt(itemIndex+1).toString().trim().isEmpty();
							
							if(!isEmpty) {
								comboBox.setSelectedIndex(itemIndex+1);
							}else {
								comboBox.setSelectedIndex(itemIndex+2);
							}
						}
						
	                }else{
	                	if(itemIndex <= 0) {
	                		return;
	                	}else {
	                		boolean isEmpty = comboBox.getItemAt(itemIndex-1).toString().trim().isEmpty();
	                		
	                		if(!isEmpty) {
								comboBox.setSelectedIndex(itemIndex-1);
							}else {
								comboBox.setSelectedIndex(itemIndex-2);
							}
	                	}
	                	
	                }
	                
				}catch(Exception ex) {
					ex.printStackTrace();
					comboBox.setSelectedIndex(itemIndex);
				}
			}
		};
	}
	
		
}
