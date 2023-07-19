package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import common.util.FontManager;
import moon.Moon;

public class LanguagePanel extends JPanel {		
	
	ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton koreanButton;
	private JRadioButton englihButton;
	
	/**
	 * Create the panel.
	 */
	public LanguagePanel(String language) {
		setBackground(Color.WHITE);
		setLayout(null);

		koreanButton = new JRadioButton("Korean");
		koreanButton.setHorizontalAlignment(SwingConstants.LEFT);
		koreanButton.setFocusPainted(false);
		koreanButton.setBackground(Color.WHITE);
		koreanButton.setFont(FontManager.getFont(Font.BOLD, 22));
		koreanButton.setForeground(Color.LIGHT_GRAY);
		koreanButton.setBounds(0, 0, 100, 55);		
		koreanButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Moon.currentLanguage.equals(Moon.KO)) return;
				Moon.showFrame(Moon.KO);
				koreanButton.setSelected(false);
				englihButton.setSelected(true);
			}
		});		
		add(koreanButton);

		englihButton = new JRadioButton("English");
		englihButton.setHorizontalAlignment(SwingConstants.LEFT);
		englihButton.setFocusPainted(false);
		englihButton.setBackground(Color.WHITE);
		englihButton.setFont(FontManager.getFont(Font.BOLD, 22));
		englihButton.setForeground(Color.LIGHT_GRAY);
		englihButton.setBounds(105, 0, 110, 55);
		englihButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Moon.currentLanguage.equals(Moon.EN)) return;
				Moon.showFrame(Moon.EN);	
				koreanButton.setSelected(true);
				englihButton.setSelected(false);
			}
		});
		add(englihButton);

		buttonGroup.add(koreanButton);
		buttonGroup.add(englihButton);				
		
		if(language.equals(Moon.KO)) {
			koreanButton.setSelected(true);
			koreanButton.setForeground(new Color(0, 128, 0));
		}else if(language.equals(Moon.EN)){
			englihButton.setSelected(true);
			englihButton.setForeground(new Color(0, 128, 0));
		}else {
			koreanButton.setSelected(true);
			koreanButton.setForeground(new Color(0, 128, 0));
		}
		
		setVisible(true);
	}

}
