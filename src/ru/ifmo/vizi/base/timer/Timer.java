package ru.ifmo.vizi.base.timer;

import java.awt.AWTEventMulticaster;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * <code>Timer</code> posts {@link ActionEvent}s each {@link #getInterval()}
 * milliseconds. Use {@link #addActionListener} for processing these event.
 *
 * @author  Georgiy Korneev
 * @version $Id: Timer.java,v 1.5 2005/02/28 11:44:06 geo Exp $
 */
public class Timer implements ActionSource {
    /**
     * Interval between ticks.
     */
    private int interval;

    /**
     * Whether timer is active (not stopped).
     */
    private boolean active;

    /**
     * Underlying imer task.
     */
    private final TimerTask task;

    /**
     * Constructs <code>Timer</code> with specified interval.
     *
     * @param interval interval between tics.
     */
    public Timer(int interval) {
        task = new TimerTask() {
            void tick() {
                setTime(getTime() + Timer.this.interval);
                super.tick();
            }
        };
        task.setActionSource(this);
        active = false;
        setInterval(interval);
    }

    /**
     * Starts timer.
     */
    public void start() {
        active = true;
        restart();
    }

    /**
     * Restarts timer.
     */
    public void restart() {
        task.setTime(System.currentTimeMillis() + interval);
    }

    /**
     * Stops timer. Events will not be generated unless timer is started.
     */
    public void stop() {
        active = false;
        task.cancel();
    }

    /**
     * Sets interval and restarts timer.
     *
     * @param interval new interval (in milliseconds).
     *
     * @see #getInterval
     */
    public void setInterval(int interval) {
        this.interval = interval;
        if (active) {
            restart();
        }
    }

    /**
     * Gets interval.
     *
     * @return current interval.
     *
     * @see #setInterval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Sets the command name for the action event fired 
     * by this timer. By default this action command is 
     * set to "<tt>timer</tt>".
     * @param command a string used to set the button's 
     *      action command.
     */
    public void setActionCommand(String command) {        
        task.setActionCommand(command);
    }

    /**
     * Gets the command name of the action event fired by this timer.
     * @return the command name of the action event fired by this timer.
     */
    public String getActionCommand() {
        return task.getActionCommand();
    }

    /**
     * Adds the specified action listener to receive action events from
     * this task. Action events occur when interval is expired.
     * @param         listener the action listener.
     * @see           #removeActionListener
     */ 
    public void addActionListener(ActionListener listener) {
        task.addActionListener(listener);
    }

    /**
     * Removes the specified action listener so that it no longer 
     * receives action events from this timer.
     * @param         listener the action listener.
     * @see           #addActionListener
     */ 
    public void removeActionListener(ActionListener listener) {
        task.removeActionListener(listener);
    }
}
