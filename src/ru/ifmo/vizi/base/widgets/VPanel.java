package ru.ifmo.vizi.base.widgets;

import java.awt.*;

/**
 * Ellipse shape.
 *
 * @author Georgiy Korneev
 * @version $Id: VPanel.java,v 1.1 2004/06/07 13:51:36 geo Exp $
 */
public class VPanel extends Container {
    /**
     * Panel insets.
     */
    private Insets insets;

    /**
     * Creates a new panel using <code>null</code> layout manager
     * and zero insets.
     */
    public VPanel() {
        this(null, null);
    }

    /**
     * Creates a new panel with specified layout manager
     * and zero insets.
     *
     * @param layout layout manager for this panel.
     *
     * @see java.awt.LayoutManager
     */
    public VPanel(LayoutManager layout) {
        this(layout, null);
    }

    /**
     * Creates a new panel with specified layout manager
     * and insets.
     *
     * @param layout layout manager for this panel.
     * @param insets insets for this panel.
     *
     * @see java.awt.LayoutManager
     * @see java.awt.Insets
     */
    public VPanel(LayoutManager layout, Insets insets) {
        setLayout(layout);
        setInsets(insets);
    }


    /**
     * Gets insets of this panel. 
     *
     * @return    the insets of this panel.
     *
     * @see       java.awt.Insets
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * Sets insets for this panel.
     * @param insets insets to set.
     */
    public void setInsets(Insets insets) {
        if (insets != null) {
            this.insets = insets;
        } else {
            this.insets = new Insets(0, 0, 0, 0);
        }
    }
}
