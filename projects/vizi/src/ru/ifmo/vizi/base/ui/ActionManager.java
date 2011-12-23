package ru.ifmo.vizi.base.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Manages actions.
 *
 * @author  Georgiy Korneev
 * @version $Id: ActionManager.java,v 1.2 2004/06/07 14:00:39 geo Exp $
 */
public final class ActionManager extends KeyAdapter implements ActionListener, KeyListener {
    /**
     * Empty arguments list.
     */
    private final static Class[] EMPTY_ARGS_TYPE = new Class[0];

    /**
     * Empty arguments.
     */
    private final static Object[] EMPTY_ARGS = new Object[0];

    /**
     * Maps action name ({@link String}) to ({@link Action}).
     */
    private final Hashtable actions = new Hashtable();

    /**
     * Deep-throug key listener.
     */
    private final DeepKeyListener listener = new DeepKeyListener();

    /**
     * Maps {@link Key} to action names {@link String}.
     */
    private final Hashtable keyMappings = new Hashtable();

    /**
     * Sets handler for specified action.
     * @param action name of the action to set handler for.
     * @param object object to invoke method on.
     * @param method name of the method to invoke.
     */
    public void setHandler(String action, Object object, String method) {
        Action a = new Action(object, method);
        synchronized (actions) {
            actions.put(action, a);
        }
    }

    /**
     * Notifies that action is performed.
     *
     * @param action name of performed action.
     */
    public void actionPerformed(String action) {
        if (action != null) {
            Action a;
            synchronized (actions) {
                a = (Action) actions.get(action);
            }
            if (a != null) {
                a.invoke();
            }
        }
    }

    /**
     * Is called when action is performed.
     * @param event action event.
     */
    public void actionPerformed(ActionEvent event) {
        actionPerformed(event.getActionCommand());
    }

    /**
     * Listens key from specified component.
     * @param component component to listen key on.
     */
    public void listenComponent(Component component) {
        listener.add(component);
    }

    /**
     * Maps key without modifiers to action.
     *
     * @param code key code.
     * @param action name of the action.
     */
    public void setKeyMapping(int code, String action) {
        setKeyMapping(code, 0, action);
    }

    /**
     * Maps key to action.
     * @param code key code.
     * @param modifiers key modifiers.
     * @param action name of the action.
     */
    public void setKeyMapping(int code, int modifiers, String action) {
        synchronized (keyMappings) {
            keyMappings.put(new Key(code, modifiers), action);
        }
    }

    /**
     * Gets action name by key event.
     *
     * @param event key event.
     * @return action name.
     */
    private String getAction(KeyEvent event) {
        synchronized (keyMappings) {
            return (String) keyMappings.get(new Key(event));
        }
    }

    /**
     * Is called when key is pressed.
     * @param event key event.
     */
    public void keyPressed(KeyEvent event) {
        actionPerformed(getAction(event));
    }

    /**
     * Object and method pair.
     */
    public final static class Action {
        /**
         * Object to invoke method on.
         */
        private final Object object;

        /**
         * Method to invoke.
         */
        private final Method method;

        /**
         * Creates a new action.
         * @param object action object.
         * @param method action method.
         */
        public Action(Object object, String method) {
            this.object = object;
            try {
                this.method = object.getClass().getMethod(method, EMPTY_ARGS_TYPE);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        /**
         * Invokes method on object.
         */
        public void invoke() {
            try {
                method.invoke(object, EMPTY_ARGS);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
            }
        }
    }

    /**
     * Listents key events on the component and all its descendants.
     */
    private final class DeepKeyListener implements ContainerListener {
        public void componentAdded(ContainerEvent e) {
            add(e.getChild());
        }

        public void componentRemoved(ContainerEvent e) {
            remove(e.getChild());
        }

        /**
         * Adds this listener to the component.
         *
         * @param component component to add this listener to.
         */
        public void add(Component component) {
            if (component instanceof Container) {
                Container container = (Container) component;
                container.addContainerListener(this);
                Component[] children = container.getComponents();
                for (int i = 0; i < children.length; i++) {
                    add(children[i]);
                }
            }
            component.addKeyListener(ActionManager.this);
        }

        /**
         * Removes this listener form the component.
         *
         * @param component component toremove this listener to.
         */
        public void remove(Component component) {
            if (component instanceof Container) {
                Container container = (Container) component;
                container.removeContainerListener(this);
                Component[] children = container.getComponents();
                for (int i = 0; i < children.length; i++) {
                    remove(children[i]);
                }
            }
            component.removeKeyListener(ActionManager.this);
        }
    }

    /**
     * Key description.
     */
    private final static class Key {
        /**
         * Key code.
         */
        public final int code;

        /**
         * Key modifiers.
         */
        public final int modifiers;

        /**
         * Creates a new key description.
         * @param code key code.
         * @param modifiers key modifiers.
         */
        public Key(int code, int modifiers) {
            this.code = code;
            this.modifiers = modifiers;
        }

        /**
         * Creates a new key description.
         * @param event event to get key info from.
         */
        public Key(KeyEvent event) {
            this(event.getKeyCode(), event.getModifiers());
        }

        public boolean equals(Object object) {
            if (object instanceof Key) {
                Key key = (Key) object;
                return code == key.code && modifiers == key.modifiers;
            } else {
                return false;
            }
        }

        /**
         * Counts hashcode.
         * @return hashcode.
         */
        public int hashCode() {
            return code | modifiers;
        }
    }
}
