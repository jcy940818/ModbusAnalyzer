package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import moon.Moon;

public class LanguagePanel extends JPanel {		
	
	ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * Create the panel.
	 */
	public LanguagePanel(String selctedButton) {
		setBackground(Color.WHITE);
		setLayout(null);

		JRadioButton koreanButton = new JRadioButton("Korean");
		koreanButton.setHorizontalAlignment(SwingConstants.LEFT);
		koreanButton.setFocusPainted(false);
		koreanButton.setBackground(Color.WHITE);
		koreanButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		koreanButton.setForeground(new Color(0, 128, 0));
		koreanButton.setBounds(0, 0, 100, 55);		
		koreanButton.addActionListener(radioListener);		
		add(koreanButton);

		JRadioButton englihButton = new JRadioButton("English");
		englihButton.setHorizontalAlignment(SwingConstants.LEFT);
		englihButton.setFocusPainted(false);
		englihButton.setBackground(Color.WHITE);
		englihButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		englihButton.setForeground(Color.DARK_GRAY);
		englihButton.setBounds(105, 0, 110, 55);
		englihButton.addActionListener(radioListener);
		add(englihButton);

		buttonGroup.add(koreanButton);
		buttonGroup.add(englihButton);
		
		if(selctedButton.equals(Moon.KO)) {
			koreanButton.setSelected(true);
		}else if(selctedButton.equals(Moon.EN)){
			englihButton.setSelected(true);
		}
		
		setVisible(true);
	}

	public ActionListener radioListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			Enumeration btnEnum = buttonGroup.getElements();
			
			while(btnEnum.hasMoreElements()) {
				JRadioButton btn = (JRadioButton)btnEnum.nextElement();								
				
				if(btn.isSelected() && btn.getText().equalsIgnoreCase(Moon.KO)) {
					System.out.println("¼±ÅÃµÈ ¹öÆ° : " + btn.getText());			
					
					btn.setSelected(false);
					Moon.showFrame(Moon.KO);
					
				}else if(btn.isSelected() && btn.getText().equalsIgnoreCase(Moon.EN)) {
					System.out.println("¼±ÅÃµÈ ¹öÆ° : " + btn.getText());
					
					btn.setSelected(false);
					Moon.showFrame(Moon.EN);
				}else {
									
				}
				
			}
			
		}
	};

}
