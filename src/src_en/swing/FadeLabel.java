package src_en.swing;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import src_en.util.Util;

public class FadeLabel extends JLabel {

    private float alpha;
    private BufferedImage background;

    public FadeLabel() {
        setIcon(new Util().getOnionItemResource(1));            
        setAlpha(1f);
    }

    public void setAlpha(float value) {
        if (alpha != value) {
            float old = alpha;
            alpha = value;
            firePropertyChange("alpha", old, alpha);
            repaint();
        }
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public Dimension getPreferredSize() {
        return background == null ? super.getPreferredSize() : new Dimension(background.getWidth(), background.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        // This is one of the few times I would directly override paint
        // This makes sure that the entire paint chain is now using
        // the alpha composite, including borders and child components
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paint(g2d);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // This is one of the few times that doing this before the super call
        // will work...
        if (background != null) {
            int x = (getWidth() - background.getWidth()) / 2;
            int y = (getHeight() - background.getHeight()) / 2;
            g.drawImage(background, x, y, this);
        }
        super.paintComponent(g);
    }
}