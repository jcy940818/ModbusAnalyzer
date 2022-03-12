package src_ko.util;

import java.awt.Color;
import java.awt.Component;

class Fade extends Thread {
	Component component;
	Color color;
	int R;
	int G;
	int B;

	public Fade(Component c) {
		this.component = c;
		this.R = c.getBackground().getRed();
		this.G = c.getBackground().getGreen();
		this.B = c.getBackground().getBlue();
	}

	public void run() {
		for (int i = 0; i < 256; i += 1) {
			color = new Color(R, G, B, i);
			component.setBackground(color);
			try {
				Thread.sleep(60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 255; i >= 0; i -= 1) {
			color = new Color(R, G, B, i);
			component.setBackground(color);
			try {
				Thread.sleep(60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
