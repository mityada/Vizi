package ru.ifmo.vizi.base.widgets;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Multiline message.
 * Lines are separated by <code>\n</code>.
 *
 * @author  Georgiy Korneev
 * @version $Id: Message.java,v 1.1 2003/12/24 10:50:43 geo Exp $
 */
public final class Message {
    /**
     * Align message to the left.
     */
    public final static double LEFT = 0;

    /**
     * Align message at center.
     */
    public final static double CENTER = 0.5;

    /**
     * Align message at right.
     */
    public final static double RIGHT = 1;

    /**
     * Virtical aspect of the font.
     */
    private final static double VERTICAL_ASPECT = 0.8;

    /**
     * Original message.
     */
    private final String message;

    /**
     * Message lines.
     */
    private final String[] lines;

    /**
     * Message align.
     */
    private double align;

    /**
     * Creates a new multiline message.
     *
     * @param message source message.
     * @param align message aling.
     */
    public Message(String message, double align) {
        this.message = message;
        StringTokenizer tokenizer = new StringTokenizer(message, "\n");
        lines = new String[tokenizer.countTokens()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = tokenizer.nextToken();
        }
        this.align = align;
    }

    /**
     * Gets original message.
     *
     * @return original message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Calculates size of this message in font with specified metrics.
     *
     * @param metrics font metrics.
     * @param padding text padding.
     *
     * @return size of this message.
     */
    public Dimension calculateSize(FontMetrics metrics, double padding) {
        Dimension size = new Dimension();
        size.width = 0;
        for (int i = 0; i < lines.length; i++) {
            int width = metrics.stringWidth(lines[i]);
            if (size.width < width) {
                size.width = width;
            }
        }
        size.height = (int) (metrics.getHeight() * VERTICAL_ASPECT * (lines.length - 1)) + metrics.getHeight();

        size.height += (int) Math.round(metrics.getHeight() * padding);
        size.width += (int) Math.round(metrics.charWidth('8') * padding);
        return size;
    }

    /**
     * Draws messages on the graphics context.
     *
     * @param g graphics context to draw message on.
     * @param x x-coordinate of the center of the message.
     * @param y y-coordinate of the center of the message.
     */
    public void drawAtCenter(Graphics g, int x, int y) {
        Dimension size = calculateSize(g.getFontMetrics(), 0);
        draw(g, x - size.width / 2, y - size.height / 2);
    }

    /**
     * Draws messages on the graphics context.
     *
     * @param g graphics context to draw message on.
     * @param x x-coordinate of the upper-left corner of the message.
     * @param y y-coordinate of the upper-left corner of the message.
     */
    public void draw(Graphics g, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int width = calculateSize(metrics, 0).width;
        int fontHeight = (int) (metrics.getHeight() * VERTICAL_ASPECT);

        y = y + metrics.getAscent();
        for (int i = 0; i < lines.length; i++) {
            int w = metrics.stringWidth(lines[i]);
            g.drawString(lines[i], x + (int) (align * (width - w)), y);
            y += fontHeight;
        }
    }

    /**
     * Sets message align.
     *
     * @param align message align.
     */
    public void setAlign(double align) {
        this.align = align;
    }

    /**
     * Gets message align.
     *
     * @return message align.
     */
    public double getAlign() {
        return align;
    }
}
