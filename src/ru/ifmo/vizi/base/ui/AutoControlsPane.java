package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.Configuration;
import ru.ifmo.vizi.base.auto.AutomataWithListener;
import ru.ifmo.vizi.base.auto.AutomataListener;
import ru.ifmo.vizi.base.auto.AutomataController;

import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

import javax.swing.*;

/**
 * Panel with automata-related contorls.
 *
 * @author Georgiy Korneev
 * @version $Id: AutoControlsPane.java,v 1.3 2004/06/07 14:01:13 geo Exp $
 */
public class AutoControlsPane 
    extends JPanel 
    implements ActionListener, AutomataListener, AdjustmentListener
{
    /**
     * Controlled automata.
     */
    private final AutomataWithListener automata;

    /**
     * Automata controller.
     */
    private final AutomataController controller;

    /**
     * Big step forward button.
     */
    private final JButton nextBigStepButton;

    /**
     * Big step backward button.
     */
    private final JButton prevBigStepButton;

    /**
     * Step forward button.
     */
    private final JButton nextStepButton;

    /**
     * Step backward button.
     */
    private final JButton prevStepButton;

    /**
     * Restart button.
     */
    private final JButton restartButton;

    /**
     * Switch to automatic mode button.
     */
    private final MultiButton autoButton;

    /**
     * About button.
     */
    private final JButton aboutButton;


    /**
     * About dialog.
     */
    private final AboutDialog aboutDialog;

    /**
     * Delay panel.
     */
    private final AdjustablePanel delayPanel;

    /**
     * Creates a new controls pane.
     *
     * @param config configuration source.
     * @param automata wraped automata.
     * @param forefather the forefather of application (applet) that creates this button
     * @param bigStepButtons whether or not create big step buttons.
     */
    public AutoControlsPane(Configuration config, 
        AutomataWithListener automata, JFrame forefather, 
        boolean bigStepButtons
    ) {        
        this.automata = automata;
        automata.addListener(this);

        controller = automata.getController();

        aboutDialog = new AboutDialog(config, forefather);

        prevBigStepButton = (bigStepButtons) 
                ? addButton(config, "button-prev-big")
                : null;
        prevStepButton = addButton(config, "button-prev");
        nextStepButton = addButton(config, "button-next");
        nextBigStepButton = (bigStepButtons) 
                ? addButton(config, "button-next-big")
                : null;


        restartButton = addButton(config, "button-restart");

        autoButton = new MultiButton(config, new String[]{"button-auto", "button-stop"});
        autoButton.addActionListener(this);
        add(autoButton);

        add(delayPanel = new AdjustablePanel(config, "delay"));
        delayPanel.addAdjustmentListener(this);

        aboutButton = addButton(config, "button-about");

        stateChanged();
        controller.setAutoInterval(delayPanel.getValue());
    }

    /**
     * Creates a new button, registers itself as action listener
     * and adds it to container.
     *
     * @param config configuration.
     * @param name button name.
     *
     * @return created button.
     */
    private JButton addButton(Configuration config, String name) {
        JButton button = new HintedButton(config, name);
        button.addActionListener(this);
        add(button);
        return button;
    }

    /**
     * Processes action events.
     * @param event event to process.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == nextBigStepButton) {
            controller.doNextBigStep();
        } else if (source == prevBigStepButton) {
            controller.doPrevBigStep();
        } else if (source == nextStepButton) {
            controller.doNextStep();
        } else if (source == prevStepButton) {
            controller.doPrevStep();
        } else if (source == restartButton) {
            controller.doRestart();
        } else if (source == autoButton) {
            controller.toggleExecutionMode();
            stateChanged();
        } else if (source == aboutButton) {
            controller.setExecutionMode(AutomataController.MANUAL_MODE);
            aboutDialog.center();
            aboutDialog.setVisible(true);
        }
    }
    
    /**
     * Invoked when automata state changed.
     */
    public void stateChanged() {
        if (nextBigStepButton != null) {
            nextBigStepButton.setEnabled(!automata.isAtEnd());
        }
        if (prevBigStepButton != null) {
            prevBigStepButton.setEnabled(!automata.isAtStart());
        }

        nextStepButton.setEnabled(!automata.isAtEnd());
        prevStepButton.setEnabled(!automata.isAtStart());
        restartButton.setEnabled(!automata.isAtStart());

        autoButton.setState(controller.getExecutionMode());
        autoButton.setEnabled(!automata.isAtEnd());
    }

    /**
     * Invoked when delay panel value changed.
     *
     * @param event event to process.
     */
    public void adjustmentValueChanged(AdjustmentEvent event) {
        if (event.getSource() == delayPanel) {
            controller.setAutoInterval(delayPanel.getValue());
        }
    }
}
