package ru.ifmo.vizi.base;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.util.Locale;

/**
 * Frame-based view for the Vizi.
 *
 * @author  Georgiy Korneev
 * @version $Id: FrameView.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public final class FrameView extends Applet {
    /**
     * Main procedure.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Parameters:");
        System.out.println("    [<locale> [<country>]]");

        Locale locale;
        switch (args.length) {
            case 1:
                locale = new Locale(args[0], "");
                break;
            case 2:
                locale = new Locale(args[0], args[1]);
                break;
            default:
                locale = Locale.getDefault();
                break;
        }

        Configuration config = Base.loadConfiguration(locale);

        final JFrame frame = new JFrame(
            config.getParameter("about-algorithm").replace('\n', ' '));

        frame.setLayout(new BorderLayout());
        Component component = Base.createVisualizer(locale, frame);
        frame.add(component, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();

        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension s = frame.getSize();
        frame.setLocation((ss.width - s.width) / 2, (ss.height - s.height) / 2);

        //frame.show();
        frame.setVisible(true);
        
        component.requestFocus();
    }
}
