package ru.ifmo.vizi.base;

import ru.ifmo.vizi.base.auto.AutomataListener;
import ru.ifmo.vizi.base.auto.AutomataWithListener;
import ru.ifmo.vizi.base.auto.AutomataController;

import ru.ifmo.vizi.base.ui.ActionManager;
import ru.ifmo.vizi.base.ui.Hinter;
import ru.ifmo.vizi.base.ui.CommentPane;
import ru.ifmo.vizi.base.ui.DoubleBufferPanel;
import ru.ifmo.vizi.base.ui.AboutDialog;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * Base class for visualizers.
 *
 * @author  Georgiy Korneev
 * @version $Id: Base.java,v 1.5 2006/05/16 10:15:20 geo Exp $
 */
public abstract class Base implements AutomataListener {
    /**
     * Arguments of visualizer's constructor.
     */
    private final static Class[] CONSTRUCTOR_ARGS = {
        VisualizerParameters.class,
    };

    /**
     * Visualizer configuration.
     */
    protected final Configuration config;

    /**
     * Visualizer locale.
     */
    protected final Locale locale;

    /**
     * Client pane is used for visualising of the algorithm.
     * You must draw steps of the algorithm on the client pane.
     */
    protected final Container clientPane;

    /**
     * Background color of the {@link #clientPane}.
     */
    protected final Color backgroundColor;

    /**
     * Foreground color of the {@link #clientPane}.
     */
    protected final Color foregroundColor;

    /**
     * Action manager.
     */
    protected final ActionManager actions;

    /**
     * Forefather of this visualizer.
     */
    protected final JFrame forefather;

    /**
     * Comment pane.
     */
    private CommentPane commentPane;

    /**
     * Wrapped automata.
     */
    private AutomataWithListener automata;

    /**
     * Hinter (root pane).
     */
    protected final Hinter hinter;

    /**
     * Creates a new visualizator base.
     * @param parameters vesualizer parameters.
     */
    protected Base(VisualizerParameters parameters) {
        this.config = parameters.config;
        this.locale = parameters.locale;
        this.forefather = parameters.forefather;

        foregroundColor = config.getColor("client-foreground");
        backgroundColor = config.getColor("client-background");

        hinter = new Hinter(config);
        clientPane = new ClientPane();

        actions = new ActionManager();
        actions.listenComponent(hinter);
    }

    /**
     * Creates visualizer interface.
     * Must be called at the end visualizer's constructor.
     * @param automata wrapped automata.
     */
    protected final void createInterface(AutomataWithListener automata) {
        this.automata = automata;
        this.automata.addListener(this);

        hinter.getContentPane().setLayout(new BorderLayout());

        hinter.getContentPane().add(clientPane, BorderLayout.CENTER);
        commentPane = new CommentPane(config, "comment");
        hinter.getContentPane().add(createBottomPane(), BorderLayout.SOUTH);

        hinter.setPreferredSize(
            config.getInteger("visualizer-width"),
            config.getInteger("visualizer-height")
        );
        hinter.doLayout();

        actions.setKeyMapping(KeyEvent.VK_RIGHT, "next-step");
        actions.setKeyMapping(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_MASK, "next-big-step");
        actions.setKeyMapping(KeyEvent.VK_LEFT, "prev-step");
        actions.setKeyMapping(KeyEvent.VK_LEFT, KeyEvent.SHIFT_MASK, "prev-big-step");
        actions.setKeyMapping(KeyEvent.VK_F9, KeyEvent.CTRL_MASK, "restart");
        actions.setKeyMapping(KeyEvent.VK_F9, "toggle-exec-mode");
        actions.setKeyMapping(KeyEvent.VK_F1, "about");

        AutomataController controller = automata.getController();
        actions.setHandler("next-step", controller, "doNextStep");
        actions.setHandler("next-big-step", controller, "doNextBigStep");
        actions.setHandler("prev-step", controller, "doPrevStep");
        actions.setHandler("prev-big-step", controller, "doPrevBigStep");
        actions.setHandler("restart", controller, "doRestart");
        actions.setHandler("toggle-exec-mode", controller, "toggleExecutionMode");
        actions.setHandler("about", this, "showAboutDialog");

        stateChanged();
    }

    /**
     * Creates pane with comment and controls panes.
     *
     * @return created pane.
     */
    private Component createBottomPane() {
        Panel panel = new Panel(new BorderLayout());
        panel.add(commentPane, BorderLayout.NORTH);

        final Component controlsPane = createControlsPane();
        controlsPane.setForeground(config.getColor("controls-foreground"));
        controlsPane.setBackground(config.getColor("controls-background"));
        controlsPane.setFont(config.getFont("controls-font", clientPane.getFont()));

        panel.add(controlsPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * This method must create panel with visualizer controls.
     * Called from {@link #createInterface}.
     *
     * @return created pane.
     */
    protected abstract Component createControlsPane();

    /**
     * This method must paint current state of the algorithm.
     *
     * @param g graphics context to use for painting.
     * @param width graphics width.
     * @param height graphics height.
     */
    protected void paintClient(Graphics g, int width, int height) {}

    /**
     * Invoked the client pane shoud be layouted.
     *
     * @param clientWidth client pane width.
     * @param clientHeight client pane height.
     */
    protected void layoutClientPane(int clientWidth, int clientHeight) {}

    /**
     * Updates visualizer state.
     *
     * @param updateComment whether to update comment.
     */
    protected void update(boolean updateComment) {
        if (updateComment && automata != null) {
            setComment(automata.getComment());
        }
        hinter.repaint();
        clientPane.repaint();
    }

    /**
     * Sets comment string.
     *
     * @param comment New comment.
     */
    public void setComment(String comment) {
        commentPane.setComment(comment);
    }

    /**
     * Invoked when automata state changed.
     */
    public void stateChanged() {
        automata.drawState();
        update(true);
    }

    /**
     * Shows about dialog.
     */
    public void showAboutDialog() {
        automata.getController().setExecutionMode(AutomataController.MANUAL_MODE);
        AboutDialog aboutDialog = new AboutDialog(config, forefather);

        aboutDialog.center();
        aboutDialog.setVisible(true);
    }

    /**
     * Loads visualizer configuration.
     * @param locale locale to load configuration for.
     * @return visualizer configuration.
     */
    static Configuration loadConfiguration(Locale locale) {
        return new Configuration("META-INF/visualizer", locale);
    }

    /**
     * Creates a new visualizer.
     * @param locale visualizer locale.
     * @param forefather the forefather of visualizer
     * @return root container of the created visualizer.
     */
    static Container createVisualizer(Locale locale, JFrame forefather) {
        Configuration config = loadConfiguration(locale);
        VisualizerParameters parameters = new VisualizerParameters(config, locale, forefather);
        String className = config.getParameter("visualizer-class");

        try {
            Class clazz = Class.forName(className);
            if (!Base.class.isAssignableFrom(clazz)) {
                System.err.println("Invalid visualizer class " + className);
                return null;
            }
            Constructor constructor = clazz.getConstructor(CONSTRUCTOR_ARGS);
            Base visualizer = (Base) constructor.newInstance(
                new Object[]{parameters}
            );
            return visualizer.hinter;
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find class " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Access denied to " + className);
        } catch (InstantiationException e) {
            System.err.println("Cannot instantiate " + className);
        } catch (IllegalArgumentException e) {
            System.err.println("Fatal: incorrect argument");
        } catch (InvocationTargetException e) {
            System.err.println("Constructor thrown exception:");
            e.getTargetException().printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor not found");
        } catch (SecurityException e) {
            System.err.println("Permission denied");
        }
        return null;
    }

    /**
     * Panel with buffered painting and resize detection.
     */
    private final class ClientPane extends DoubleBufferPanel {
        /**
         * Creates a new {@link ClientPane}.
         */
        private ClientPane() {
            setBackground(backgroundColor);
            setFont(config.getFont("client-font"));
            setLayout(null);
        }

        /**
         * Paints this {@link ClientPane}.
         * @param g graphics to paint on.
         */
        public void paint(Graphics g) {
            super.paint(g);            
            paintClient(g, getSize().width, getSize().height);
        }

        /**
         * Sets bound of this {@link ClientPane}.
         * @param x <x>x</x>-coordinate of the upper-left corner.
         * @param y <x>y</x>-coordinate of the upper-left corner.
         * @param width width of the pane.
         * @param height height of the pane.
         */
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            doLayout();
        }

        /**
         * Layouts this pane.
         */
        public void doLayout() {
            super.doLayout();
            layoutClientPane(getSize().width, getSize().height);
        }
    }
}
