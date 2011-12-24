package ru.ifmo.vizi.base.widgets;

import java.awt.*;
import java.awt.geom.*;

/**
 * Ellipse shape.
 *
 * @author Georgiy Korneev
 * @version $Id: Ellipse.java,v 1.2 2003/12/29 10:31:24 geo Exp $
 */
public class Ellipse extends Shape {
    /**
     * Square root of 2.
     */
    private final static double SQRT2 = Math.sqrt(2);

    /**
     * Creates a new ellipse with specified style set, size and empty message.
     *
     * @param styleSet ellipse's style set.
     */
    public Ellipse(ShapeStyle[] styleSet) {
        super(styleSet, "");
    }

    /**
     * Creates a new ellipse with specifed style set, size and message.
     *
     * @param styleSet ellipse's style set.
     * @param message ellipse's message.
     */
    public Ellipse(ShapeStyle[] styleSet, String message) {
        super(styleSet, message);
    }

    /**
     * Returns minimum shape size to contain message with specified size.
     *
     * @param size message size.
     *
     * @return minimum shape size.
     */
    protected Dimension fit(Dimension size) {        
        if (look.getAspectStatus(style)) {
            double aspect = look.getAspect(style);
            double r = Math.sqrt(size.width * size.width * aspect * aspect + size.height * size.height);
            return new Dimension(
                (int) Math.round(r / aspect),
                (int) Math.round(r)
            );
        } else {
            return new Dimension(
                (int) Math.round(SQRT2 * size.width),
                (int) Math.round(SQRT2 * size.height)
            );
        }
    }

    /**
     * Paints the ellipse.
     *
     * @param g graphics context for painting.
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension size = getSize();
        int w = size.width - 2;
        int h = size.height - 2;

        Ellipse2D ellipse = new Ellipse2D.Double(1, 1, w, h);

        if (look.getFillStatus(style)) {
            g2.setPaint((Paint)look.getFillColor(style));
            g2.fill(ellipse);
        }

        if (look.getBorderStatus(style)) {
            g2.setPaint((Paint)look.getBorderColor(style));
            g2.setStroke(new BasicStroke(1));
            g2.draw(ellipse);
        }

        super.paint(g);
    }
}
