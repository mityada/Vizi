package ru.ifmo.vizi.base.widgets;

import java.awt.*;

/**
 * Rectangular shape.
 * This shape may have border.
 * Corners of rectangle may be rounded with specified radius.
 *
 * @author  Georgiy Korneev
 * @version $Id: Rect.java,v 1.3 2004/06/07 13:51:03 geo Exp $
 */
public final class Rect extends Shape {
    /**
     * Corners radius.
     */
    int radius;

    /**
     * Whether to use the Bresenham algorithm.
     */
    private final static boolean USE_BRESENHAM = true;

    /**
     * Creates a new rectangle with specified style set, empty message
     * and square corneres.
     *
     * @param styleSet shape's style set.
     */
    public Rect(ShapeStyle[] styleSet) {
        this(styleSet, "", 0);
    }

    /**
     * Creates a new rectangle with specifed style set, message and
     * square corners.
     *
     * @param styleSet shape's style set.
     * @param message message to show.
     */
    public Rect(ShapeStyle[] styleSet, String message) {
        this(styleSet, message, 0);
    }

    /**
     * Creates a new rectangle with specifed style set, message and
     * rounded corners.
     *
     * @param styleSet shape's style set.
     * @param message message to show.
     * @param radius corners radius.
     */
    public Rect(ShapeStyle[] styleSet, String message, int radius) {
        super(styleSet, message);
        this.radius = radius;
        setStyle(0);
    }

    /**
     * Returns minimum shape size to contain message with specified size.
     *
     * @param size message size.
     *
     * @return minimum shape size.
     */
    protected Dimension fit(Dimension size) {
        double pwidth = size.width + 0.5 * radius;
        double pheight = size.height + 0.5 * radius;

        if (look.getAspectStatus(style)) {
            double aspect = look.getAspect(style);
            if (pwidth <= pheight * aspect) {
                pwidth = pheight * aspect;
            } else {
                pheight = pwidth / aspect;
            }
        }
        return new Dimension(
                (int) Math.round(pwidth),
                (int) Math.round(pheight)
        );
    }

    /**
     * Gets rectangle's corners' radius.
     *
     * @return rectangle's corners' radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets rectangle's corners' radius.
     *
     * @param radius rectangle's corners' radius.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Paints this component.
     *
     * @param g graphics context for painting.
     */
    public void paint(Graphics g) {
        Dimension size = getSize();
        int w = size.width - 1;
        int h = size.height - 1;
        if (USE_BRESENHAM && (radius > 0)) {            
            if (look.getFillStatus(style)) {
                g.setColor(look.getFillColor(style));

                Bresenham.fillEllipse(g, w - 2 * radius, 1, 2 * radius, 2 * radius);
                Bresenham.fillEllipse(g, 1, 1, 2 * radius, 2 * radius);
                Bresenham.fillEllipse(g, 1, h - 2 * radius, 2 * radius, 2 * radius);
                Bresenham.fillEllipse(g, w - 2 * radius, h - 2 * radius, 2 * radius, 2 * radius);

                g.fillRect(radius, 1, w - 2 * radius, h);
                g.fillRect(1, radius, w, h - 2 * radius);
            }           
            if (look.getBorderStatus(style)) {
                g.setColor(look.getBorderColor(style));
                Bresenham.drawEllipse(g, w - 2 * radius, 1, 2 * radius, 2 * radius, true, false, false, false);
                Bresenham.drawEllipse(g, 1, 1, 2 * radius, 2 * radius, false, true, false, false);
                Bresenham.drawEllipse(g, 1, h - 2 * radius, 2 * radius, 2 * radius, false, false, true, false);
                Bresenham.drawEllipse(g, w - 2 * radius, h - 2 * radius, 2 * radius, 2 * radius, false, false, false, true);

                g.setColor(look.getBorderColor(style));
                g.drawLine(radius, 0, w - radius, 0);
                g.drawLine(radius, h, w - radius, h);
                g.drawLine(0, radius, 0, h - radius); 
                g.drawLine(w, radius, w, h - radius);
            }
        } else {
            if (look.getFillStatus(style)) {
                g.setColor(look.getFillColor(style));
                g.fillRoundRect(0, 0, size.width - 1, size.height - 1, radius * 2, radius * 2);
            }
            if (look.getBorderStatus(style)) {
                g.setColor(look.getBorderColor(style));
                g.drawRoundRect(0, 0, size.width - 1, size.height - 1, radius * 2, radius * 2);
            }
        }
        super.paint(g);
    }
}
