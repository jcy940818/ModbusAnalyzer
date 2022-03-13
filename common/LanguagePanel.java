package common;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.Font;

public class LanguagePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public LanguagePanel() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JRadioButton koreanButton = new JRadioButton("Korean");
		koreanButton.setFocusPainted(false);
		koreanButton.setBackground(Color.WHITE);
		koreanButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		koreanButton.setForeground(Color.BLACK);
		koreanButton.setBounds(0, 0, 100, 40);
		add(koreanButton);
		
		JRadioButton englihButton = new JRadioButton("Englih");
		englihButton.setFocusPainted(false);
		englihButton.setBackground(Color.WHITE);
		englihButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		englihButton.setForeground(Color.BLACK);
		englihButton.setBounds(104, 0, 95, 40);
		add(englihButton);

	}
}
