package ru.ifmo.vizi.base.ui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.text.AttributedCharacterIterator;
import javax.swing.*;
import java.util.StringTokenizer;
import ru.ifmo.vizi.base.Configuration;

/**
 * Pane the shows comments. Comments a auto-wrapped.
 * <p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *          <th>Default</th>
 *      </th>
 *      <tr>
 *          <td>comment-height</td>
 *          <td>Comment pane height</td>
 *          <td>0</td>
 *      </tr>
 *      <tr>
 *          <td>comment-foreground</td>
 *          <td>Comment pane foreground color</td>
 *          <td>0x0000000</td>
 *      </tr>
 *      <tr>
 *          <td>comment-background</td>
 *          <td>Comment pane background color</td>
 *          <td>0xfffffff</td>
 *      </tr>
 *      <tr>
 *          <td>comment-*</td>
 *          <td>Comment pane font configuration. See {@link Configuration#getFont}</td>
 *      </tr>
 * </table>
 *
 * @author  Georgiy Korneev
 * @version $Id: CommentPane.java,v 1.2 2003/12/26 14:11:22 geo Exp $
 */
public final class CommentPane extends JPanel {
    /**
     * Comment pane height.
     */
    private final int height;

    /**
     * Current comment.
     */
    private String comment;

    /**
     * Draw buffer.
     */
    private Image buffer;

    /**
     * Constructs a new comment pane.
     *
     * @param config configuration source.
     * @param name name of the comment pane.
     */
    public CommentPane(Configuration config, String name) {
        setForeground(config.getColor(name + "-foreground"));
        setBackground(config.getColor(name + "-background"));
        setFont(config.getFont(name + "-font"));

        if (config.hasParameter(name + "-lines")) {
            height = (int) (getFontMetrics(getFont()).getHeight() * (config.getInteger(name + "-lines") + 0.3));
        } else {
            height = config.getInteger(name + "-height");
        }
    }

    /**
     * Sets new comment.
     *
     * @param comment comment to show.
     */
    public void setComment(String comment) {
        this.comment = comment;
        this.repaint();
    }

    /**
     * Gets prefferred size of the comment pane.
     *
     * @return preferred size.
     */
    public Dimension getPreferredSize() {
        return new Dimension(100, height);
    }

    /**
     * Gets minimum size of the comment pane.
     *
     * @return minimum size.
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Gets maximium size of the comment pane.
     *
     * @return maximum size.
     */
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * Updates comment pane.
     *
     * @param g graphic to update for.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Paints comment paint.
     *
     * @param g graphics to paint on.
     */
    public void paint(Graphics g) {
        Dimension size = getSize();
        final int width  = size.width;
        final int height = size.height;

        if (buffer == null) {
            buffer = createImage(width, height);
        }
        Image buffer = this.buffer;
        Graphics bg = buffer.getGraphics();

        super.paint(bg);
        
        Graphics2D bg2d = (Graphics2D) bg;
        
        bg2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        
        bg2d.clearRect(0, 0, width - 1, height - 1);
        bg2d.draw(new Line2D.Double(0, 0, width - 1, 0));

        if (comment != null) {
            StringTokenizer tokenizer = new StringTokenizer(comment);
            FontMetrics metrics = bg.getFontMetrics();

            String str = "";
            int x = 10;
            int y = metrics.getHeight();
            if (tokenizer.hasMoreTokens()) {
                str = tokenizer.nextToken();
            }
            while (tokenizer.hasMoreTokens()) {
                final String token = tokenizer.nextToken();
                if (metrics.stringWidth(str + ' ' + token) > width - 2 * x) {
                    bg2d.drawString(str, x, y);
                    y += metrics.getHeight();
                    str = token;
                } else {
                    str += ' ' + token;
                }
            }
            if (0 != str.length()) {
                bg2d.drawString(str.trim(), x, y);
            }
        }
        bg2d.dispose();

        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Sets new bounds of comment pane.
     *
     * @param x the new <i>x</i>-coordinate.
     * @param y the new <i>y</i>-coordinate/
     * @param width the new <code>width</code>.
     * @param height the new <code>height</code>.
     */
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        buffer = null;
    }
}

