package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.Configuration;
import ru.ifmo.vizi.base.I18n;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @deprecated Use {@link AdjustablePanel} instead.
 *
 * <code>SpinPanel</code> allows user to input double value between
 * minimal and maximal values with specified step.
 * @see #getStep()
 * @see #getMaxValue()
 * @see #getMinValue()
 *
 * <p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *          <th>Default</th>
 *      </th>
 *      <tr>
 *          <td><b>name</b>-hint</td>
 *          <td>Spin panel hint</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-caption</td>
 *          <td>Caption template</td>
 *          <td>{0}</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-value</td>
 *          <td>Default value</td>
 *          <td>0</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-value-min</td>
 *          <td>Minimal value</td>
 *          <td>0</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-value-max</td>
 *          <td>Maximal value</td>
 *          <td>0</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-step</td>
 *          <td>Value step</td>
 *          <td>1</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-button-less</td>
 *          <td>Decrease button caption</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-button-less-hint</td>
 *          <td>Decrease button hint</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-button-more</td>
 *          <td>Increase button caption</td>
 *      </tr>
 *      <tr>
 *          <td><b>name</b>-button-more-hint</td>
 *          <td>Increase button hint</td>
 *      </tr>
 * </table>
 *
 * @author  Georgiy Korneev
 * @version $Id: SpinPanel.java,v 1.3 2004/03/23 16:24:30 geo Exp $
 */
public abstract class SpinPanel extends JPanel implements ActionListener {
    /**
     * Increase value button.
     */
    private final HintedButton btnMore;

    /**
     * Decrease value batton.
     */
    private final HintedButton btnLess;

    /**
     * Current value captionTemplate.
     */
    private final JLabel lblCaption;

    /**
     * Panel hint.
     */
    private final String hint;

    /**
     * Message template for current value label.
     */
    private final String captionTemplate;

    /**
     * Maximal allowed value.
     */
    private double maxValue;

    /**
     * Minimal allowed value.
     */
    private double minValue;

    /**
     * Increase/decrease step.
     */
    private double step;

    /**
     * Big increase/decrease step.
     */
    private double bigStep;

    /**
     * Delay between clicks when big step performed.
     */
    private double bigStepDelay;

    /**
     * Current value.
     */
    private double value;

    /**
     * Time, when last click was performed.
     */
    private long lastClick;

    /**
     * Creates a new <code>SpinPanel</code> with specified name.
     *
     * @param config configuration source.
     * @param name name of the parameter to get configuration from.
     * @deprecated use {@link AdjustablePanel} instead.
     */
    public SpinPanel(Configuration config, String name) {
        System.err.println("SpinPanel is deprecated");
        setLayout(new BorderLayout());

        btnLess = new HintedButton(config, name + "-button-less");
        btnLess.addActionListener(this);
        btnMore = new HintedButton(config, name + "-button-more");
        btnMore.addActionListener(this);

        lblCaption = new JLabel((String)null, JLabel.CENTER);

        add(btnLess, BorderLayout.WEST);
        add(lblCaption, BorderLayout.CENTER);
        add(btnMore, BorderLayout.EAST);

        hint = config.getParameter(name + "-hint");
        Hinter.applyHint(lblCaption, hint);

        captionTemplate = config.getParameter(name + "-caption");
        step = config.getDouble(name + "-step");
        bigStep = config.getDouble(name + "-big-step", step);
        bigStepDelay = config.getInteger(name + "-big-step-delay", 1000);
        value = config.getDouble(name + "-value");
        minValue = config.getDouble(name + "-value-min");
        maxValue = config.getDouble(name + "-value-max");

        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        update(false);
    }

    /**
     * Returns current value.
     *
     * @return a <code>double</code> that represents of current value.
     *
     * @see #getIntValue
     * @see #setValue
     */
    public double getDoubleValue() {
        return value;
    }

    /**
     * Returns current value rounded  to the nearest integer..
     *
     * @return a <code>int</code> that represents current value.
     *
     * @see #getDoubleValue
     * @see #setValue
     */
    public int getIntValue() {
        return (int) (value + 0.5);
    }

    /**
     * Sets current value.
     *
     * @param  value the value to ste.
     *
     * @see #getDoubleValue
     * @see #getIntValue
     */
    public void setValue(double value) {
        this.value = value;
        update(false);
    }

    /**
     * Gets maximal allowed value.
     *
     * @return maximal allowed value.
     *
     * @see #setMaxValue
     * @see #getMinValue
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets maximal allowed value.
     *
     * @param value new maximal allowed value.
     *
     * @see #getMaxValue
     * @see #setMinValue
     */
    public void setMaxValue(double value) {
        maxValue = value;
        update(false);
    }

    /**
     * Gets minimal allowed value.
     *
     * @return minimal allowed value.
     *
     * @see #setMinValue
     * @see #getMaxValue
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimal allowed value.
     *
     * @param value new minimal allowed value.
     *
     * @see #getMinValue
     * @see #setMaxValue
     */
    public void setMinValue(double value) {
        minValue = value;
        update(false);
    }

    /**
     * Gets step value.
     *
     * @return step value.
     *
     * @see #setStep
     */
    public double getStep() {
        return step;
    }

    /**
     * Sets the step.
     *
     * @param step new step.
     *
     * @see #getStep
     */
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * Gets big step value.
     *
     * @return step value.
     *
     * @see #setBigStep
     */
    public double getBigStep() {
        return bigStep;
    }

    /**
     * Sets the big step.
     *
     * @param step new big step.
     *
     * @see #getBigStep
     */
    public void setBigStep(double step) {
        this.bigStep = step;
    }

    /**
     * Updates panel state.
     *
     * @param notify whether to notify user.
     */
    private void update(boolean notify) {
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }

        btnLess.setEnabled(value > minValue);
        btnMore.setEnabled(value < maxValue);

        lblCaption.setText(I18n.message(captionTemplate, new Double(value)));

        if (notify) {
            click(value);
        }
    }

    /**
     * Invoked when panel value changed.
     *
     * @param value new value.
     */
    protected abstract void click(double value);

    /**
     * Invoked when increase/descrese button is pressed.
     * @param event event to process.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        int sign = 0;
        if (source == btnLess) {
            sign = -1;
        } else if (source == btnMore) {
            sign = 1;
        }
        long time = System.currentTimeMillis();
        if (time - lastClick < bigStepDelay) {
            value += sign * bigStep;
        } else {
            value += sign * step;
        }
        lastClick = time;
        update(true);
    }

    /**
     * Invoked when component state changes.
     *
     * @param e the component event
     */
    protected final void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        if (isVisible()) {
            Hinter.applyHint(lblCaption, hint);
        } else {
            Hinter.applyHint(lblCaption, null);
        }
    }
}
