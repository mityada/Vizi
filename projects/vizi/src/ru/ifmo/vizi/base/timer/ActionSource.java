package ru.ifmo.vizi.base.timer;

import java.awt.AWTEventMulticaster;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action source.
 *
 * @author  Georgiy Korneev
 * @version $Id: ActionSource.java,v 1.1 2005/02/28 11:45:07 geo Exp $
 */
public interface ActionSource {
    /**
     * Timer event id.
     */
    public final static int TIMER_EVENT = AWTEvent.RESERVED_ID_MAX + 100;

    /**
     * Sets the command name for the action event fired 
     * by this action source. By default this action command is 
     * set to "<tt>timer</tt>".
     *
     * @param command a string used to set the button's 
     *      action command.
     */
    public void setActionCommand(String command);

    /**
     * Gets the command name of the action event fired by this timer.
     *
     * @return the command name of the action event fired by this timer.
     */
    public String getActionCommand();

    /**
     * Adds the specified action listener to receive action events from
     * this source. Action events occur when interval is expired.
     *
     * @param         listener the action listener.
     * @see           #removeActionListener
     */ 
    public void addActionListener(ActionListener listener);

    /**
     * Removes the specified action listener so that it no longer 
     * receives action events from this source.

     * @param         listener the action listener.
     * @see           #addActionListener
     */ 
    public void removeActionListener(ActionListener listener);
}