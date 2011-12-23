package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.Configuration;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Modal dialog.
 *
 * @author  Georgiy Korneev
 * @version $Id: ModalDialog.java,v 1.2 2003/12/26 14:11:22 geo Exp $
 */
public class ModalDialog extends JDialog {
    /**
     * Owner of this dialog.
     */
    private final JFrame owner;

    /**
     * Creates a new modal dialog.
     * @param owner owner of this dialog.
     * @param title dialog title.
     */
    public ModalDialog(JFrame owner, String title) {
        super(owner, title, true);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        this.owner = owner;
    }

    /**
     * Centers this dialog.
     */
    public void center() {
        center(this, owner);
    }

    /**
     * Centers frame relative to the component.
     *
     * @param frame frame to center.
     * @param component component to center relative to.
     */
    public static void center(Window frame, Component component) {
        Component parent = component;
        int dx = 0;
        int dy = 0;
        while (parent.getParent() != null) {
            Point p = parent.getLocation();
            dx += p.x;
            dy += p.y;
            parent = parent.getParent();
        }

        Point l = parent.getLocation();
        Dimension d = component.getSize();
        Dimension s = frame.getSize();
        frame.setLocation(
            l.x + dx + (d.width - s.width) / 2,
            l.y + dy + (d.height - s.height) / 2
        );
    }

    /**
     * Handles window events.
     *
     * @param e event to handle.
     */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
        }
    }

    /**
     * Gets owner of this dialog.
     *
     * @return owner of this dialog.
     */
    public JFrame getOwner() {
        return owner;
    }
}
