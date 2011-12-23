package ru.ifmo.vizi.base;

import java.awt.*;
import javax.swing.*;
import java.util.Locale;

/**
 * Applet-based view for the Vizi.
 *
 * @author  Georgiy Korneev
 * @version $Id: AppletView.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public final class AppletView extends JApplet {
    /**
     * Applet parameters info.
     */
    private final static String[][] PARAMETERS_INFO = new String[][] {
        {"locale", "EN or RU", "Visualizer's locale"},
        {"country", "", "Country-specific part of locale"},
    };

    /**
     * Initializes {@link Base}.
     */
    public void init() {
        Locale locale = getLocale();
        setLayout(new BorderLayout());

        Container forefather = this.getParent();
        while (forefather != null && !(forefather instanceof JFrame)) {
            forefather = forefather.getParent();
        }
        if (forefather == null) {
            forefather = new JFrame();
        }

        Component component = Base.createVisualizer(locale, (JFrame) forefather);

        add(component, BorderLayout.CENTER);
        component.requestFocus();
    }

    /**
     * Returns info about the applet.
     * @return information about the applet.
     */
    public String getAppletInfo() {
        Configuration config = Base.loadConfiguration(getLocale());
        return new StringBuffer()
            .append(config.getParameter("about-algorithm").replace('\n', ' ')).append("\n")
            .append(config.getParameter("about-author-caption")).append(" ")
            .append(config.getParameter("about-author")).append(" (")
            .append(config.getParameter("about-author-email")).append(")\n")
            .append(config.getParameter("about-supervisor-caption")).append(" ")
            .append(config.getParameter("about-supervisor")).append(" (")
            .append(config.getParameter("about-supervisor-email")).append(")\n")
            .append(config.getParameter("about-technology-caption")).append(" ")
            .append(config.getParameter("about-technology")).append(" (")
            .append(config.getParameter("about-technology-email")).append(")\n")
            .append(config.getParameter("about-copyright")).append("\n")
            .toString();
    }

    /**
     * Returns information about applet's parameters.
     * @return information about applet's parameters.
     */
    public String[][] getParameterInfo() {
        return PARAMETERS_INFO;
    }

    public Locale getLocale() {
        final String language = getParameter("locale");

        if (language != null) {
            final String country = getParameter("country");
            if (country != null) {
                return new Locale(language, country);
            } else {
                return new Locale(language, "");
            }
        } else {
            return super.getLocale();
        }
    }
}
