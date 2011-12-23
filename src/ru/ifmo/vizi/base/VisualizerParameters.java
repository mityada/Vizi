package ru.ifmo.vizi.base;

import java.util.Locale;
import java.awt.*;
import javax.swing.*;

/**
 * Visualizer parameters.
 * This class most likely will be modified, so do not use its structure.
 *
 * @author  Georgiy Korneev
 * @version $Id: VisualizerParameters.java,v 1.2 2003/12/26 14:10:01 geo Exp $
 */
public final class VisualizerParameters {
    /**
     * Visualizer locale.
     */
    final Locale locale;

    /**
     * Visualizer configuration.
     */
    final Configuration config;

    /**
     * The forefather of visualizer.
     */
    final JFrame forefather;

    /**
     * Crates a new visualizer parameters.
     * @param config visualizer configuration.
     * @param locale visualizer locale.
     * @param forefather the fprefather of visualizer
     */
    VisualizerParameters(Configuration config, Locale locale, JFrame forefather) {
        this.config = config;
        this.locale = locale;
        this.forefather = forefather;
    }

    /**
     * Returns the forefather of the application (applet).
     * Never returns <code>null</code>.
     *
     * @return forefather of the visualizer.
     */
    public Frame getForefather() {
        return forefather;
    }
}
