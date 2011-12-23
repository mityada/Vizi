package ru.ifmo.vizi.base.widgets;

import java.awt.*;

/**
 * Draws ellipses using Bresenham's algorithms.
 *
 * @author Georgiy Korneev
 * @version $Id: Bresenham.java,v 1.3 2004/03/18 11:35:07 geo Exp $
 */
public class Bresenham {

    /**
     * Draws an ellipse.
     * @param g graphics to draw ellipse on.
     * @param x the x coordinate of the upper left corner of 
     *      the ellipse to be drawn.
     * @param y the y coordinate of the upper left corner of 
     *      the ellipse to be drawn.
     * @param w the width of the ellipse to be drawn.
     * @param h the height of the ellipse to be drawn.
     */
    public static void drawEllipse(Graphics g, int x, int y, int w, int h) {
        drawEllipse(g, x, y, w, h, true, true, true, true);
    }

    /**
     * Draws an ellipse.
     * @param g graphics to draw ellipse on.
     * @param x the x coordinate of the upper left corner of 
     *      the ellipse to be drawn.
     * @param y the y coordinate of the upper left corner of 
     *      the ellipse to be drawn.
     * @param w the width of the ellipse to be drawn.
     * @param h the height of the ellipse to be drawn.
     * @param drawFirst specifies whether draw top right part of 
     *      the ellipse or not.
     * @param drawSecond specifies whether draw top left part of 
     *      the ellipse or not.
     * @param drawThird specifies whether draw bottom left part of 
     *      the ellipse or not.
     * @param drawForth specifies whether draw bottom right part of 
     *      the ellipse or not
     */
    public static void drawEllipse(Graphics g, int x, int y, int w, int h, boolean drawFirst, boolean drawSecond, boolean drawThird, boolean drawForth) {
        int cx1 = x + (w - 1) / 2;
        int cy1 = y + (h - 1) / 2;
        int cx2 = x + w / 2;
        int cy2 = y + h / 2;

        int cx = 0;
        int cy = h / 2;

        int bx = 2;
        int by = -1;

        while (cy >= 0) {
            if (drawFirst) g.fillRect(cx2 + cx, cy1 - cy, 1, 1);
            if (drawSecond) g.fillRect(cx1 - cx, cy1 - cy, 1, 1);
            if (drawThird)  g.fillRect(cx1 - cx, cy2 + cy, 1, 1);
            if (drawForth) g.fillRect(cx2 + cx, cy2 + cy, 1, 1);

            long a = q(cx + w / 2, cy + h / 2, w, h, bx, by);
            long b = q(cx + w / 2, cy + h / 2, w, h, bx - 1, by - 2);

            if (a < 0) {
                cx++;
            } else if (b >= 0) {
                cy--;
            } else {
                cx++;
                cy--;
            }
        }
    }

    /**
     * Fills an ellipse.
     * @param g graphics to draw ellipse on.
     * @param x the x coordinate of the upper left corner 
     *      of the ellipse to be drawn.
     * @param y the y coordinate of the upper left corner 
     *      of the ellipse to be drawn.
     * @param w the width of the ellipse to be drawn.
     * @param h the height of the ellipse to be drawn.
     */
    public static void fillEllipse(Graphics g, int x, int y, int w, int h) {
        int cx1 = x + (w - 1) / 2;
        int cy1 = y + (h - 1) / 2;
        int cx2 = x + w / 2;
        int cy2 = y + h / 2;

        int cx = 0;
        int cy = h / 2;

        int bx = 2;
        int by = -1;

        while (cy >= 0) {
            g.drawLine(cx1 - cx, cy2 + cy, cx2 + cx, cy2 + cy);
            g.drawLine(cx1 - cx, cy1 - cy, cx2 + cx, cy1 - cy);

            long a = q(cx + w / 2, cy + h / 2, w, h, bx, by);
            long b = q(cx + w / 2, cy + h / 2, w, h, bx - 1, by - 2);

            if (a < 0) {
                cx++;
            } else if (b >= 0) {
                cy--;
            } else {
                cx++;
                cy--;
            }
        }
    }

    private static long s(long x) {
        return x * x;
    }

    private static long q(long x, long y, long w, long h, long dx, long dy) {
        long tx = dx + 2 * x;
        long ty = dy + 2 * y;
        return s(h) * s(w) + s(h) * tx * (tx - 2 * w) + s(w) * ty * (ty - 2 * h);
    }
}
