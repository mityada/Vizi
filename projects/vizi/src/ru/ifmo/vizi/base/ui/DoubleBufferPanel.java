package ru.ifmo.vizi.base.ui;

import java.awt.*;
import javax.swing.*;

/**
 * Panel with double-buffering ability.
 *
 * @author  Georgiy Korneev
 * @version $Id: DoubleBufferPanel.java,v 1.2 2004/06/07 14:06:24 geo Exp $
 */
public class DoubleBufferPanel extends JPanel {
    /**
     * Buffer image.
     */
    private Image buffer;

     /**
      * Invalidates buffer.
      */
     public void invalidate() {
         super.invalidate();
         buffer = null;
     }

     /**
      * Repains whole panel.
      *
      * @param g graphics to paint on.
      */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Paints to buffer and shows it on the screen.
     *
     * @param g graphics to paint on.
     */
    public void paint(Graphics g) {
        int width = getSize().width;
        int height = getSize().height;
        if(buffer == null) {
           buffer = createImage(width, height);
        }

        Graphics bg = buffer.getGraphics();
        bg.clearRect(0, 0, width, height);
        bg.setClip(0, 0, width, height);
        super.paint(bg);
        g.drawImage(buffer, 0, 0, null);
        bg.dispose();
    }
}
