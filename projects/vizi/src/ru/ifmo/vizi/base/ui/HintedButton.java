package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.Configuration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Simple button with hint.
 * <p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *      </th>
 *      <tr>
 *          <td><b>name</b></td>
 *          <td>button caption</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-hint</td>
 *          <td>Button hint</td>
 *      </tr>
 * </table>
 *
 * @author  Georgiy Korneev
 * @version $Id: HintedButton.java,v 1.4 2006/05/15 11:44:50 geo Exp $
 */
public class HintedButton extends JButton implements ActionListener {
    /**
     * Creates a new hinted button without caption and hint.
     */
    protected HintedButton() {
        enableEvents(
            AWTEvent.ACTION_EVENT_MASK |
            AWTEvent.COMPONENT_EVENT_MASK
        );
    }

    /**
     * Creates a new hinted button with caption specified in parameter
     * <link>name</link> and hint specified in parameter <link>name</link>-hint.
     *
     * @param config visualizer configuration.
     * @param name button name.
     */
    public HintedButton(Configuration config, String name) {
        this();

        setActionCommand(name);
        addActionListener(this);

        //setLabel(config.getParameter(name));
        setText(config.getParameter(name));
        setHint(
            config.getParameter(name + "-hint"),
            config.getParameter(name + "-hotKey", null)
        );
    }

    /**
     * Hint to show.
     */
    private String hint;

    /**
     * Hot key.
     */
    private String hotKey;

    /**
     * Sets new hint for this button.
     *
     * @param hint hint to set.
     */
    protected void setHint(String hint) {
        this.hint = hint;
        applyHint();
    }

    /**
     * Sets a new hint and hot key for this button.
     *
     * @param hint hint to set.
     * @param hotKey hotKey to set.
     */
    public void setHint(String hint, String hotKey) {
        this.hotKey = hotKey;
        setHint(hint);
    }

    /**
     * Applies hint, if needed.
     */
    private void applyHint() {
        //if (isVisible()) {
        //    Hinter.applyHint(this, hint, hotKey);
        //} else {
        //    Hinter.applyHint(this, null, null);
        //}
        this.setToolTipText(hint);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e action event.
     */
    //protected final void processActionEvent(ActionEvent e) {
    //    super.processActionEvent(e);
    //    click();
    //}
    public void actionPerformed(ActionEvent e) {
        click();
    }

    /**
     * Invoked when component state changes.
     *
     * @param e the component event
     */
    @Override
    protected final void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        applyHint();
    }

    /**
     * Invoked when user has clicked on button.
     */
    protected void click() {
    }
}
