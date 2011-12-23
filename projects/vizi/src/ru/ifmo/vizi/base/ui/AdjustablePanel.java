package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <code>AdjustablePanel</code> allows user to input an integer value
 * between minimum and maximum values.
 *
 * Value is incremented (decremented) by <i>unit increment</i> each time user
 * is clicked on increment (decrement) button.
 * If interval between clicks less than <i>block interval</i> milliseconds, 
 * the value is incremened (decremented) by <i>block increment</i>.
 *
 * <p>
 * Configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *      </th><tr>
 *          <td><b>name</b>-hint</td>
 *          <td>Spin panel hint</td>
 *      </tr><tr>
 *          <td><b>name</b>-caption</td>
 *          <td>Caption template</td>
 *      </tr><tr>
 *          <td><b>name</b>-value</td>
 *          <td>Initial value</td>
 *      </tr><tr>
 *          <td><b>name</b>-minimum</td>
 *          <td>Minimum value</td>
 *      </tr><tr>
 *          <td><b>name</b>-maximum</td>
 *          <td>Maximum value</td>
 *      </tr><tr>
 *          <td><b>name</b>-unitIncrement</td>
 *          <td>Unit increment</td>
 *      </tr><tr>
 *          <td><b>name</b>-blockIncrement</td>
 *          <td>Block increment</td>
 *      </tr><tr>
 *          <td><b>name</b>-blockInterval</td>
 *          <td>Block interval</td>
 *      </tr><tr>
 *          <td><b>name</b>-incrementButton</td>
 *          <td>Increment button configuration (see {@link HintedButton})</td>
 *      </tr><tr>
 *          <td><b>name</b>-decrementButton</td>
 *          <td>Decrement button configuration (see {@link HintedButton})</td>
 *      </tr>
 * </table>
 *
 * @see #getUnitIncrement()
 * @see #getBlockIncrement()
 * @see #setUnitIncrement(int)
 * @see #setBlockIncrement(int)
 * @see #getBlockInterval()
 * @see #setBlockInterval(int)
 *
 * @author  Georgiy Korneev
 * @version $Id: AdjustablePanel.java,v 1.2 2004/05/13 08:25:53 geo Exp $
 */
public class AdjustablePanel 
    extends JPanel 
    implements Adjustable, ActionListener 
{
    /**
     * Current value.
     */
    private int value;

    /**
     * Minimum value.
     */
    private int minimum;

    /**
     * Maximum value.
     */
    private int maximum;

    /**
     * Unit increment.
     */
    private int unitIncrement;

    /**
     * Block increment.
     */
    private int blockIncrement;

    /**
     * Increment button.
     */
    private final JButton incrementButton;

    /**
     * Decrement batton.
     */
    private final JButton decrementButton;

    /**
     * Current value label.
     */
    private final JLabel label;

    /**
     * Listener to notify than value is changed.
     */
    private AdjustmentListener listener;

    /**
     * Message template for current value label.
     */
    private String captionTemplate;

    /**
     * Time of the previouse press.
     */
    private long pressTime;

    /**
     * Interval between clics when block step is used.
     */
    private int blockInterval = 500;

    /**
     * Default constructor.
     */
    public AdjustablePanel() {
        super(new BorderLayout());
        label = new JLabel("", JLabel.CENTER);
        add(label, BorderLayout.CENTER);

        incrementButton = new JButton();
        decrementButton = new JButton();
        incrementButton.addActionListener(this);
        decrementButton.addActionListener(this);
        add(incrementButton, BorderLayout.EAST);
        add(decrementButton, BorderLayout.WEST);

        captionTemplate = "";

        minimum = Integer.MIN_VALUE;
        maximum = Integer.MAX_VALUE;
        setValue(Integer.MIN_VALUE);
    }

    /**
     * Creates a new adjustable panel with specified configuration.
     *
     * @param config panel configuration.
     */
    public AdjustablePanel(Configuration config, String name) {
        super(new BorderLayout());
        label = new JLabel("", JLabel.CENTER) {
            public void paint(Graphics g) {
                double a = (getValue() - getMinimum()) / 
                    (double) (getMaximum() - getMinimum());
                Dimension size = getSize();
                int l = (int) (size.width * a + 0.5);

                g.drawLine(0, 0, l, 0);
                g.drawLine(0, size.height - 1, l, size.height - 1);
                super.paint(g);
            }
        };
        add(label, BorderLayout.CENTER);

        incrementButton = new HintedButton(config, name + "-incrementButton");
        decrementButton = new HintedButton(config, name + "-decrementButton");
        incrementButton.addActionListener(this);
        decrementButton.addActionListener(this);
        add(incrementButton, BorderLayout.EAST);
        add(decrementButton, BorderLayout.WEST);

        captionTemplate = config.getParameter(name + "-caption");

        Hinter.applyHint(label, config.getParameter(name + "-hint"));

        unitIncrement = config.getInteger(name + "-unitIncrement");
        blockIncrement = config.getInteger(name + "-blockIncrement");

        blockInterval = config.getInteger(name + "-blockInterval");

        maximum = config.getInteger(name + "-maximum");
        minimum = config.getInteger(name + "-minimum");

        setValue(config.getInteger(name + "-value"));
    }

    /*
     * Sets configuration.
     * @param config configuration.
     */
     /*
    public void setConfiguration(ConfigurationNode config) {
        config.getNode("incButton").configureBean(incrementButton);
        config.getNode("decButton").configureBean(decrementButton);
        captionTemplate = config.getAttribute("caption");
    }
    */

    /**
     * Gets the current value.
     *
     * @return current value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets a new current value.
     *
     * @param value new current value.
     */
    public void setValue(int value) {
        setValue(value, AdjustmentEvent.TRACK);
    }

    /**
     * Sets the new current value.
     *
     * @param value new current value.
     * @param type adjustment type.
     *
     * @see AdjustmentEvent
     */
    private void setValue(int value, int type) {
        if (value > maximum) {
            value = maximum;
        }
        if (value < minimum) {
            value = minimum;
        }

        decrementButton.setEnabled(value > minimum);
        incrementButton.setEnabled(value < maximum);

        label.setText(I18n.message(captionTemplate, new Integer(value)));

        if (this.value != value) {
            this.value = value;
            if (listener != null) {
                listener.adjustmentValueChanged(new AdjustmentEvent(this,
                        AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED, type, value));
            }
            label.repaint();
        }
    }

    /**
     * Gets the minimum value.
     *
     * @return minimum value.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Sets a new minumum value.
     *
     * @param minimum new minumum value.
     */
    public void setMinimum(int minimum) {
        this.minimum = minimum;
        setValue(value);
    }

    /**
     * Gets the maximum value.
     *
     * @return maximum value.
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Sets a new maximum value.
     *
     * @param maximum new maximum value.
     */
    public void setMaximum(int maximum) {
        this.maximum = maximum;
        setValue(value);
    }

    /**
     * Gets the unit increment.
     *
     * @return unit increment.
     */
    public int getUnitIncrement() {
        return unitIncrement;
    }

    /**
     * Sets a new unit increment.
     * @param increment new unit increment.
     */
    public void setUnitIncrement(int increment) {
        this.unitIncrement = increment;
    }

    /**
     * Gets the block increment.
     *
     * @return block increment.
     */
    public int getBlockIncrement() {
        return blockIncrement;
    }

    /**
     * Sets a new block increment.
     * @param increment new block increment.
     */
    public void setBlockIncrement(int increment) {
        this.blockIncrement = increment;
    }

    /**
     * Gets the block interval.
     *
     * @return block interval.
     */
    public int getBlockInterval() {
        return blockInterval;
    }

    /**
     * Sets a new block interval.
     * @param interval new block interval.
     */
    public void setBlockInterval(int interval) {
        this.blockInterval = interval;
    }

    /**
     * Add a listener to recieve adjustment events when the value changes.
     *
     * @param listener the listener to recieve events.
     *
     * @see AdjustmentEvent
     */
    public void addAdjustmentListener(AdjustmentListener listener) {
        this.listener = AWTEventMulticaster.add(this.listener, listener);
    }

    /**
     * Removes an adjustment listener.
     *
     * @param listener the listener being removed.
     */
    public void removeAdjustmentListener(AdjustmentListener listener) {
        this.listener = AWTEventMulticaster.remove(this.listener, listener);
    }

    /**
     * Gets the orientation of this panel object.
     */
    public int getOrientation() {
        return Adjustable.HORIZONTAL;
    }

    /**
     * @deprecated ignored, do not use.
     *
     * @param value the length of the indicator
     */
    public void setVisibleAmount(int value) {
    }

    /**
     * @deprecated always returns zero.
     */
    public int getVisibleAmount() {
        return 0;
    }

    /**
     * Invoked when an action occurs.
     * @param event event to process.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == incrementButton) {
            if (System.currentTimeMillis() - pressTime > blockInterval) {
                setValue(value + unitIncrement, AdjustmentEvent.UNIT_INCREMENT);
            } else {
                setValue(value + blockIncrement, AdjustmentEvent.BLOCK_INCREMENT);
            }
            pressTime = System.currentTimeMillis();
        } else if (event.getSource() == decrementButton) {
            if (System.currentTimeMillis() - pressTime > blockInterval) {
                setValue(value - unitIncrement, AdjustmentEvent.UNIT_DECREMENT);
            } else {
                setValue(value - blockIncrement, AdjustmentEvent.BLOCK_DECREMENT);
            }
            pressTime = System.currentTimeMillis();
        }
    }
}
