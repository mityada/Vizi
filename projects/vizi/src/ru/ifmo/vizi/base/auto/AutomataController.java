package ru.ifmo.vizi.base.auto;

import ru.ifmo.vizi.base.timer.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlles automata.
 * <p>
 * Automata controller may be enabled or disabled via {@link #setEnabled} 
 * method. If controller is disabled then it ignores calls to 
 * {@link #doNextStep}, {@link #doPrevStep}, {@link #doNextBigStep}, 
 * {@link #doPrevBigStep} and {@link #doRestart} (but not {@link #rewind})
 * methods. Controlles is enabled by default.
 *
 * @author  Georgiy Korneev
 * @version $Id: AutomataController.java,v 1.3 2006/05/16 10:15:47 geo Exp $
 */
public class AutomataController implements AutomataListener, ActionListener {
    /**
     * Manual execution mode.
     */
    public final static int MANUAL_MODE = 0;

    /**
     * Automatic execution mode.
     */
    public final static int AUTO_MODE = 1;

    /**
     * Whether the controller is enabled.
     */
    private boolean enabled;

    /**
     * Controlled automata.
     */
    private final BaseAutomataWithListener automata;

    /**
     * Timer for delays in automatic mode.
     */
    private final Timer timer;

    /**
     * Current mode.
     */
    private int executionMode;

    /**
     * Creates a new automata controller.
     *
     * @param automata controlled automata.
     */
    public AutomataController(BaseAutomataWithListener automata) {
        this.automata = automata;
        automata.addListener(this);

        timer = new Timer(1000);
        timer.addActionListener(this);

        setExecutionMode(MANUAL_MODE);
        enabled = true;
    }

    /**
     * Sets exection mode (on of {@link #MANUAL_MODE} or {@link #AUTO_MODE}).
     *
     * @param executionMode mode to set.
     *
     * @throws IllegalArgumentException if invalida mode is specified.
     */
    public void setExecutionMode(int executionMode) {
        if (executionMode == AUTO_MODE) {
            timer.start();
            automata.stepForward(0);
        } else if (executionMode == MANUAL_MODE) {
            timer.stop();
        } else {
            throw new IllegalArgumentException("Illegal mode " + executionMode);
        }
        this.executionMode = executionMode;
        automata.fireStateChanged();
    }

    /**
     * Gets exection mode (on of {@link #MANUAL_MODE} or {@link #AUTO_MODE}).
     *
     * @return execution mode.
     */
    public int getExecutionMode() {
        return executionMode;
    }

    /**
     * Toggles execution mode.
     */
    public void toggleExecutionMode() {
        if (getExecutionMode() == AutomataController.AUTO_MODE) {
            setExecutionMode(AutomataController.MANUAL_MODE);
        } else {
            setExecutionMode(AutomataController.AUTO_MODE);
        }
    }

    /**
     * Sets interval between steps in automation mode.
     *
     * @param interval interval to set (in milliseconds).
     */
    public void setAutoInterval(int interval) {
        timer.setInterval(interval);
    }

    /**
     * Gets interval between steps in automation mode.
     *
     * @return interval (in milliseconds).
     */
    public int setAutoInterval() {
        return timer.getInterval();
    }

    /**
     * Invoked when automata state changed.
     */
    public void stateChanged() {
        if (executionMode == AUTO_MODE && (automata.isAtEnd() || automata.isAtStart())) {
            setExecutionMode(MANUAL_MODE);
        }
    }

    /**
     * Processes action events.
     *
     * @param event event to process.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == timer && executionMode == AUTO_MODE) {
            automata.stepForward(0);
        }
    }

    /**
     * Makes small step forward and sets execution mode to manual.
     */
    public void doNextStep() {
        setExecutionMode(MANUAL_MODE);
        if (enabled && !automata.isAtEnd()) {
            automata.stepForward(0);
        }
    }

    /**
     * Makes big step forward and sets execution mode to manual.
     */
    public void doNextBigStep() {
        setExecutionMode(MANUAL_MODE);
        if (enabled && !automata.isAtEnd()) {
            automata.stepForward(1);
        }
    }

    /**
     * Makes small step backward and sets execution mode to manual.
     */
    public void doPrevStep() {
        setExecutionMode(MANUAL_MODE);
        if (enabled && !automata.isAtStart()) {
            automata.stepBackward(0);
        }
    }

    /**
     * Makes big step backward and sets execution mode to manual.
     */
    public void doPrevBigStep() {
        setExecutionMode(MANUAL_MODE);
        if (enabled && !automata.isAtStart()) {
            automata.stepBackward(1);
        }
    }

    /**
     * Restarts automata and sets execution mode to manual.
     */
    public void doRestart() {
        setExecutionMode(MANUAL_MODE);
        while (enabled && !automata.isAtStart()) {
            automata.stepBackward(Integer.MAX_VALUE);
        }
    }

    /**
     * Restarts automata and rewinds it to the specified step.
     *
     * @param step step to revind automata to.
     */
    public void rewind(int step) {
        setExecutionMode(MANUAL_MODE);
        doRestart();
        while (!automata.isAtEnd() && automata.getStep() < step) {
            automata.stepForward(0);
        }
    }

    /**
     * Sets enabled mode.
     *
     * @param enabled enabled mode to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets whether controller is enabled.
     *
     * @return whether controller is enabled.
     */
    public boolean getEnabled() {
        return enabled;
    }
}
