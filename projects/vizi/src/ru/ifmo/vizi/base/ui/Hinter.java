package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.Configuration;
import ru.ifmo.vizi.base.timer.TimerTask;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Panel with hints support.
 * <p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *          <th>Default</th>
 *      </th>
 *      <tr>
 *          <td>hint-*</td>
 *          <td>hint font configuration</td>
 *      </tr>
 *      <tr>
 *          <td>hint-background</td>
 *          <td>Hint background color</td>
 *          <td>0xffffe0</td>
 *      </tr>
 *      <tr>
 *          <td>hint-foreground</td>
 *          <td>Hint foreground color</td>
 *          <td>0x000000</td>
 *      </tr>
 *      <tr>
 *          <td>hint-vgap</td>
 *          <td>Vertical gap between text and border</td>
 *          <td>-1</td>
 *      </tr>
 *      <tr>
 *          <td>hint-hgap</td>
 *          <td>Horizontal gap between text and border</td>
 *          <td>2</td>
 *      </tr>
 *      <tr>
 *          <td>hint-delay</td>
 *          <td>Delay befor hint is displayed (in milliseconds)</td>
 *          <td>1000</td>
 *      </tr>
 * </table>
 *
 * @author  Georgiy Korneev
 * @version $Id: Hinter.java,v 1.5 2005/02/28 11:44:06 geo Exp $
 */
public final class Hinter extends Container {
    /**
     * Mouse pointer is outside of the control.
     */
    private final static int OUTSIDE = 0;

    /**
     * Mouse pointer is inside of the control, but hint is not shown.
     */
    private final static int INSIDE = 1;

    /**
     * Hint is visible, mouse pointer is inside of the control.
     */
    private final static int SHOW = 2;

    /**
     * Known hints.
     */
    private final Hashtable hints;

    /**
     * Hint font.
     */
    private final Font hintFont;

    /**
     * Hint font.
     */
    private final Font hotKeyFont;

    /**
     * Hint background color.
     */
    private final Color hintBackground;

    /**
     * Hint foreground color.
     */
    private final Color hintForeground;

    /**
     * Hot key foreground color.
     */
    private final Color hotKeyForeground;

    /**
     * Horizontal gap between text and hint.
     */
    private final int hgap;

    /**
     * Vertical gap between text and hint.
     */
    private final int vgap;

    /**
     * Delay before hint is shown (in millisconds).
     */
    private final int delay;

    /**
     * Real contentPane to show.
     */
    private final Container contentPane;

    /**
     * Real hint ot show.
     */
    private final HintComponent hintComponent;

    /**
     * Hint font metrics.
     */
    private final FontMetrics fontMetrics;

    /**
     * Hint font ascent.
     */
    private final int fontAscent;

    /**
     * Hint font size.
     */
    private final int fontHeight;

    /**
     * Preferred size.
     */
    private Dimension preferredSize;

    /**
     * Visualizer configuration.
     */
    private final Configuration config;

    /**
     * Creates a new hint panel.
     *
     * @param config panel configuration.
     */
    public Hinter(Configuration config) {
        this.config = config;

        hintFont = config.getFont("hint-font");
        fontMetrics = getFontMetrics(hintFont);
        fontAscent = fontMetrics.getAscent();
        fontHeight = fontMetrics.getHeight() * 8 / 10;

        hotKeyFont = config.getFont("hint-hotKeyFont", hintFont);

        hintBackground = config.getColor("hint-background");
        hintForeground = config.getColor("hint-foreground");
        hotKeyForeground = config.getColor("hint-hotKeyForeground");
        hgap = config.getInteger("hint-hgap");
        vgap = config.getInteger("hint-vgap");
        delay = config.getInteger("hint-delay");

        hints = new Hashtable();

        hintComponent = new HintComponent();
        add(hintComponent);

        contentPane = new DoubleBufferPanel();
        add(contentPane);
    }

    /**
     * Gets content pane.
     *
     * @return content pane.
     */
    public Container getContentPane() {
        return contentPane;
    }

    /**
     * Throws runtime exception if layout is not <code>null</code>.
     * Use <code>getContentPane().setLayout(...)</code> instead.
     *
     * @param layout layout manager.
     */
    public void setLayout(LayoutManager layout) {
        if (null != layout) {
            throw new IllegalArgumentException("Use getContentPane().setLayout(...) instead");
        }
    }

    /**
     * Adds component to hinted panel.
     *
     * @param comp the contentPane to be added.
     * @param constraints an object expressing layout constraints
     *      for this contentPane
     * @param index the position in the container's list at which to
     *      insert the contentPane, where <code>-1</code> means append
     *      to the end.
     * @throws IllegalArgumentException on each call.
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        /*
        if (comp == hintComponent || comp == contentPane) {
            super.addImpl(comp, constraints, index);
        } else {
            throw new IllegalArgumentException("Use getContentPane().add(...) instead");
        }
        */
    }


    /**
     * Moves and resizes this component. The new location of the top-left
     * corner is specified by <code>x</code> and <code>y</code>, and the
     * new size is specified by <code>width</code> and <code>height</code>.
     *
     * @param x the new <i>x</i>-coordinate of this component.
     * @param y the new <i>y</i>-coordinate of this component.
     * @param width the new <code>width</code> of this component.
     * @param height the new <code>height</code> of this component.
     */
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        contentPane.setBounds(0, 0, width, height);
    }

    /**
     * Adds hint for the specified child component.
     * If specified hint is <code>null</code>, removes hint.
     *
     * @param component component to add hint for.
     * @param hint hint to set.
     */
    private void setHint(Component component, String hint, String hotKey) {
        synchronized (hints) {
            Hint h = (Hint) hints.remove(component);
            if (h != null) {
                h.destroy();
            }

            if (hint != null) {
                hints.put(component, new Hint(component, hint, hotKey));
            }
        }
    }

    /**
     * Gets hinter preferred size.
     *
     * @return hinter preferred size.
     */
    public Dimension getPreferredSize() {
        if (preferredSize != null) {
            return preferredSize;
        } else {
            return contentPane.getPreferredSize();
        }
    }

    /**
     * Sets hinter preferred size.
     *
     * @param width preferred width.
     * @param height preferred height.
     */
    public void setPreferredSize(int width, int height) {
        preferredSize = new Dimension(width, height);
    }

    /**
     * Sets hint for the component.
     * If hint is <code>null</code> removes hint.
     *
     * @param component contentPane to set hint for.
     * @param hint hint message.
     */
    public static void applyHint(Component component, String hint) {
        applyHint(component, hint, null);
    }

    /**
     * Sets hint for the component.
     * If hint is <code>null</code> removes hint.
     *
     * @param component contentPane to set hint for.
     * @param hint hint message.
     * @param hotKey hotKey message.
     */
    public static void applyHint(Component component, String hint, String hotKey) {
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof Hinter) {
                Hinter panel = (Hinter) parent;
                panel.setHint(component, hint, hotKey);
                return;
            }
            parent = parent.getParent();
        }
    }

    /**
     * Component that shows hints.
     */
    private final class HintComponent extends Panel {
        /**
         * Current owner.
         */
        private Hint owner;

        /**
         * Hint size.
         */
        private Dimension size;

        /**
         * Drawing buffer.
         */
        private Image buffer;

        /**
         * Creates a new hint.
         */
        public void HintComponent() {
            setVisible(false);
        }

        /**
         * Shows hint.
         *
         * @param owner owning hint.
         * @param message message to show.
         * @param location location to show hint for.
         */
        public void showHint(Hint owner, String message, String hotKey, Point location) {
            setBackground(hintBackground);
            setForeground(hintForeground);
            this.owner = owner;

            size = new Dimension();
            size.width = 0;

            StringTokenizer tokenizer = new StringTokenizer(message, "\n");
            String[] lines = new String[tokenizer.countTokens()];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = tokenizer.nextToken();
                int width = fontMetrics.stringWidth(lines[i]);
                if (size.width < width) {
                    size.width = width;
                }
            }
            if (hotKey != null) {
                int width = fontMetrics.stringWidth(hotKey);
                if (size.width < width) {
                    size.width = width;
                }
            }
            size.width += hgap * 2 + 2;
            size.height = fontHeight * (lines.length - 1) + fontMetrics.getHeight() + vgap * 2 + 2;
            if (hotKey != null) {
                size.height += fontMetrics.getHeight();
            }

            buffer = createImage(size.width, size.height);
            Graphics bg = buffer.getGraphics();

            bg.clearRect(0, 0, size.width - 1, size.height - 1);
            int y = vgap + 1 + fontAscent;
            for (int i = 0; i < lines.length; i++) {
                bg.drawString(lines[i], hgap + 1, y);
                y += fontHeight;
            }
            bg.drawRect(0, 0, size.width - 1, size.height - 1);

            if (hotKey != null) {
                bg.setColor(hotKeyForeground);
                bg.setFont(hotKeyFont);

                int h = vgap + size.height - fontMetrics.getHeight();
                bg.drawLine(1, h, size.width - 2, h);
                bg.drawString(hotKey, hgap + 1, h + fontAscent);
            }

            bg.dispose();

            // Sets hint location
            final Dimension parentSize = Hinter.this.getSize();
            if (location.x + size.width >= parentSize.width) {
                location.x = parentSize.width - size.width - 1;
            }
            if (location.y <= size.height) {
                // Show hint at bottom.
                location.y += 16;
            } else {
                location.y -= size.height;
            }

            setSize(new Dimension(size));
            setLocation(location.x, location.y);

            setVisible(true);
        }

        /**
         * Hides hint.
         *
         * @param owner owner of the hint to hide.
         */
        public void hideHint(Hint owner) {
            if (this.owner == owner) {
                setVisible(false);
            }
        }

        /**
         * Gets hint preferred size.
         *
         * @return hint preferred size.
         */
        public Dimension getPreferredSize() {
            return new Dimension(size);
        }

        /**
         * Gets hint minimum size.
         *
         * @return hint minimum size.
         */
        public Dimension getMinimumSize() {
            return new Dimension(size);
        }

        /**
         * Gets hint maximum size.
         *
         * @return hint maximum size.
         */
        public Dimension getMaximumSize() {
            return new Dimension(size);
        }

        /**
         * Updates hint.
         *
         * @param g graphics to paint hint on.
         */
        public void update(Graphics g) {
            paint(g);
        }

        /**
         * Paints hint.
         *
         * @param g graphics to paint hint on.
         */
        public void paint(Graphics g) {
            g.drawImage(buffer, 0, 0, null);
        }
    }

    /**
     * Hint implementation.
     */
    private final class Hint 
        implements MouseListener, MouseMotionListener, FocusListener, ActionListener
    {
        /**
         * Component to show hint for.
         */
        private final Component component;

        /**
         * Hint state. One of {@link #OUTSIDE}, {@link #INSIDE} or
         * {@link #SHOW}.
         */
        private int state;

        /**
         * Message to show.
         */
        private final String message;

        /**
         * Hot key.
         */
        private final String hotKey;

        /**
         * Current mouse position.
         */
        private final Point mousePosition;

        /**
         * Timer task.
         */
        private final TimerTask task;

        /**
         * Creates a new hint.
         *
         * @param component component to show hint for.
         * @param message message to show.
         * @param hotKey hotKey to show.
         */
        public Hint(Component component, String message, String hotKey) {
            this.component = component;
            this.message = message;
            this.hotKey = hotKey;

            state = OUTSIDE;
            mousePosition = new Point();

            task = new TimerTask();

            setFont(hintFont);

            component.addMouseListener(this);
            component.addMouseMotionListener(this);
            component.addFocusListener(this);
            task.addActionListener(this);
        }

        /**
         * Sets new hint state.
         *
         * @param state new state.
         * @param event initial mouse event.
         */
        private void setState(int state, MouseEvent event) {
            if (event != null) {
                mousePosition.setLocation(event.getPoint());
            }
            this.state = state;
            if (state == OUTSIDE) {
                hintComponent.hideHint(this);
            } else if (state == INSIDE) {
                hintComponent.hideHint(this);
                task.setRelativeTime(delay);
            } else if (state == SHOW) {
                if (Hinter.this.isAncestorOf(component)) {
                    Component c = component;
                    Point p = new Point(mousePosition);
                    while (c != Hinter.this) {
                        Point pt = c.getLocation();
                        p.translate(pt.x, pt.y);
                        c = c.getParent();
                    }
                    hintComponent.showHint(this, message, hotKey, p);
                }
            }
        }

        /**
         * Invoked when mouse has been moved.
         *
         * @param event mouse event.
         */
        public void mouseMoved(MouseEvent event) {
            if (state == INSIDE || state == SHOW) {
                setState(INSIDE, event);
            }
        }

        /**
         * Invoked when mouse has been dragged.
         *
         * @param event mouse event.
         */
        public void mouseDragged(MouseEvent event) {
            mouseMoved(event);
        }

        /**
         * Invoked when mouse has been clicked.
         *
         * @param event mouse event.
         */
        public void mouseClicked(MouseEvent event) {
            setState(INSIDE, event);
        }

        /**
         * Invoked when mouse has entered component.
         *
         * @param event mouse event.
         */
        public void mouseEntered(MouseEvent event) {
            setState(INSIDE, event);
        }

        /**
         * Invoked when mouse has exited component.
         *
         * @param event mouse event.
         */
        public void mouseExited(MouseEvent event) {
            setState(OUTSIDE, event);
        }

        /**
         * Invoked when mouse button has been pressed.
         *
         * @param event mouse event.
         */
        public void mousePressed(MouseEvent event) {
            setState(INSIDE, event);
        }

        /**
         * Invoked when mouse button has been released.
         *
         * @param event mouse event.
         */
        public void mouseReleased(MouseEvent event) {
        }


        /**
         * Invoked when component gains focus.
         *
         * @param event focus event.
         */
        public void focusGained(FocusEvent event) {
        }

        /**
         * Invoked when component losts focus.
         *
         * @param event focus event.
         */
        public void focusLost(FocusEvent event) {
            setState(OUTSIDE, null);
        }

        /**
         * Invoked when timer ticks.
         *
         * @param event action event.
         */
        public void actionPerformed(ActionEvent event) {
            if (state == INSIDE) {
                setState(SHOW, null);
            }
        }

        /**
         * Destroys this hint.
         */
        private void destroy() {
            hintComponent.hideHint(this);
            setState(OUTSIDE, null);
            component.removeMouseListener(this);
            component.removeMouseMotionListener(this);
            component.removeFocusListener(this);
            task.removeActionListener(this);
        }
    }
}
