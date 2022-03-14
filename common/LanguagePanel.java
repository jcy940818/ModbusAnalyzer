package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import moon.Moon;

public class LanguagePanel extends JPanel {

	public static JRadioButton koreanButton;
	public static JRadioButton englihButton;
	public static ButtonGroup radioButtonGroup;

	/**
	 * Create the panel.
	 */
	public LanguagePanel() {
		setBackground(Color.WHITE);
		setLayout(null);

		koreanButton = new JRadioButton("Korean");
		koreanButton.setHorizontalAlignment(SwingConstants.LEFT);
		koreanButton.setFocusPainted(false);
		koreanButton.setBackground(Color.WHITE);
		koreanButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		koreanButton.setForeground(new Color(0, 128, 0));
		koreanButton.setBounds(0, 0, 100, 55);		
		koreanButton.addActionListener(radioListener);
		koreanButton.setSelected(true);
		add(koreanButton);

		englihButton = new JRadioButton("Englih");
		englihButton.setHorizontalAlignment(SwingConstants.LEFT);
		englihButton.setFocusPainted(false);
		englihButton.setBackground(Color.WHITE);
		englihButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		englihButton.setForeground(Color.DARK_GRAY);
		englihButton.setBounds(105, 0, 95, 55);
		englihButton.addActionListener(radioListener);
		add(englihButton);

		radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(koreanButton);
		radioButtonGroup.add(englihButton);

		setVisible(true);
	}	

	public static ActionListener radioListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JRadioButton b = (JRadioButton) e.getSource();
			String language = b.getText();

			if (language.contains("Korean")) {
				koreanButton.setForeground(new Color(0, 128, 0));
				englihButton.setForeground(Color.DARK_GRAY);

				Moon.showFrame(Moon.KO);
				System.out.println("ko !");
				return;
			} else {
				englihButton.setForeground(new Color(0, 128, 0));
				koreanButton.setForeground(Color.DARK_GRAY);

				Moon.showFrame(Moon.EN);
				System.out.println("en !");
				return;
			}

		}
	};

}
