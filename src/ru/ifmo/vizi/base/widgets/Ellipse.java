package ru.ifmo.vizi.base.widgets;

import java.awt.*;

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
     * Whether to use the Bresenham algorithm.
     */
    private final static boolean USE_BRESENHAM = true;

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
        Dimension size = getSize();
        int w = size.width - 2;
        int h = size.height - 2;
        if (USE_BRESENHAM) {
        	if (look.getFillStatus(style)) {
        		g.setColor(look.getFillColor(style));
        		Bresenham.fillEllipse(g, 1, 1, w, h);
        	}
        	if (look.getBorderStatus(style)) {
        		g.setColor(look.getBorderColor(style));
        		Bresenham.drawEllipse(g, 1, 1, w, h);
        	}
        } else {
        	if (look.getFillStatus(style)) {
        		g.setColor(look.getFillColor(style));
        		g.fillOval(1, 1, w, h);
        	}
        	if (look.getBorderStatus(style)) {
        		g.setColor(look.getBorderColor(style));
        		g.drawOval(1, 1, w, h);
        	}
        }
        super.paint(g);
    }
}
