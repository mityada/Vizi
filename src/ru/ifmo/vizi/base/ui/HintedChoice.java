package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Simple choice contol with hint.
 * May load its contents from configuration.
 * <p></p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *      </th>
 *      <tr>
 *          <td><b>name</b>-hint</td>
 *          <td>choice hint</td>
 *      </tr>
 * </table>
 * <p></p>
 * Use inner &lt;message&gt; tags to specify the choice options:
 * <table border="1">
 *      <tr>
 *          <th>param</th>
 *          <th>description</th>
 *          <th>message-*</th>
 *      </th>
 *      <tr>
 *          <td>contents-0</td>
 *          <td>choice option #0</td>
 *          <td>your localized message #0</td>
 *      </tr>
 *      <tr>
 *          <td>contents-1</td>
 *          <td>choice option #1</td>
 *          <td>your localized message #1</td>
 *      </tr>
 *      <tr>
 *          <td>...</td>
 *          <td>...</td>
 *          <td>...</td>
 *      </tr>
 *      <tr>
 *          <td>contents-&lt;N&gt;</td>
 *          <td>choice option #&lt;N&gt;</td>
 *          <td>your localized message #&lt;N&gt;</td>
 *      </tr>
 * </table>
 *
 * @author Maxim Buzdalov
 * @version $Id: HintedChoice.java,v 1.1 2006/05/15 11:44:17 geo Exp $
 */
public class HintedChoice extends Choice {
    /**
     * The hint string.
     */
    private String hint;

    /**
     * Creates a new hinted choice without hint
     * and with no choice options loaded.
     */
    public HintedChoice() {}

    /**
     * Creates a new hinted choice control
     * with contents specified in contents list parameter
     * and hint specified in hint parameter
     *
     * @param config visualizer configuration.
     * @param name choice control name.
     */
    public HintedChoice(Configuration config, String name) {
        enableEvents(
                AWTEvent.ACTION_EVENT_MASK |
                AWTEvent.COMPONENT_EVENT_MASK
        );

        for (int number = 0; config.hasParameter(name + "-contents-" + number); number++) {
            add(config.getParameter(name + "-contents-" + number));
        }

        setHint(config.getParameter(name + "-hint"));
    }

    /**
     * Sets new text to be shown as a hint.
     * @param hint hint to set.
     */
    public void setHint(String hint) {
        this.hint = hint;
        applyHint();
    }

    /**
     * Applies hint, if needed.
     */
    private void applyHint() {
        if (isVisible()) {
            Hinter.applyHint(this, hint);
        } else {
            Hinter.applyHint(this, null);
        }
    }


    /**
     * Invoked when a component event is processed and component state is changed.
     * @param e the event.
     */
    protected final void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        applyHint();
    }
}
