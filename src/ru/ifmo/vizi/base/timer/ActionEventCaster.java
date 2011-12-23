package ru.ifmo.vizi.base.timer;

import java.awt.AWTEventMulticaster;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action event caster.
 *
 * @author  Georgiy Korneev
 * @version $Id: ActionEventCaster.java,v 1.1 2005/02/28 11:45:07 geo Exp $
 */
public class ActionEventCaster implements ActionSource {
    /**
     * Timer event id.
     */
    public final static int TIMER_EVENT = AWTEvent.RESERVED_ID_MAX + 100;

    /**
     * Chained action listener.
     */
    private ActionListener actionListener;

    /**
     * Action event command.
     */
    private String actionCommand;

    /**
     * Action event source.
     */
    private Object actionSource;

    /**
     * Creates a new action event caster.
     */
    public ActionEventCaster() {
        setActionSource(this);
    }

    /**
     * Component for event dispatching.
     */
    private final Component eventDispatcher = new Component() {
        {
            enableEvents(AWTEvent.ACTION_EVENT_MASK);
        }

       /**
        * Processes AWT event.
        * @param event event to process.
        */
       protected void processEvent(AWTEvent event) {
           if (event instanceof ActionEvent) {
               processActionEvent((ActionEvent) event);
           } else {
               super.processEvent(event);
           }
       }

       /**
        * Processes timer event.
        * @param event event to process.
        */
       protected void processActionEvent(ActionEvent event) {
           if (actionListener != null) {
               actionListener.actionPerformed(new ActionEvent(
                    getActionSource(),
                    TIMER_EVENT,
                    getActionCommand()
               ));
           }
       }
    };

    /**
     * Sets the command name for the action event fired 
     * by this timer. By default this action command is 
     * set to "<tt>timer</tt>".
     * @param command a string used to set the button's 
     *      action command.
     */
    public void setActionCommand(String command) {        
        actionCommand = command;
    }

    /**
     * Gets the command name of the action event fired by this timer.
     * @return the command name of the action event fired by this timer.
     */
    public String getActionCommand() {
        return actionCommand == null ? "timer" : actionCommand;
    }

    /**
     * Gets the source of the action event fired by this timer.
     * @return the source of the action event fired by this timer.
     */
    public Object getActionSource() {
        return actionSource;
    }

    /**
     * Gets the source of the action event fired by this timer.
     * @return the source of the action event fired by this timer.
     */
    public void setActionSource(Object actionSource) {
        this.actionSource = actionSource;
    }

    /**
     * Adds the specified action listener to receive action events from
     * this task. Action events occur when interval is expired.
     * @param         listener the action listener.
     * @see           #removeActionListener
     */ 
    public synchronized void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
    }

    /**
     * Removes the specified action listener so that it no longer 
     * receives action events from this timer.
     * @param         listener the action listener.
     * @see           #addActionListener
     */ 
    public synchronized void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }

    /**
     * Creates and dispatches new {@link ActionEvent}.
     */
    protected void dispatchEvent() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ActionEvent(
            eventDispatcher,
            TIMER_EVENT,
            getActionCommand()
        ));
    }
}