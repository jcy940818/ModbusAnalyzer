package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import src_ko.util.Util;

public class Image_Panel extends JPanel {
	public Image_Panel() {
		setBorder(new EmptyBorder(12, 12, 12, 12));
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(new Color(255, 140, 0));

		setLayout(new BorderLayout(0, 0));
		JPanel actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(new BorderLayout(0, 0));

		JLabel imageLabel = new JLabel(new Util().getOnionScreenResource(), JLabel.CENTER);		
		actualPanel.add(imageLabel, BorderLayout.CENTER);
		imageLabel.setIcon(new Util().getOnionScreenResource());		

	}
}
