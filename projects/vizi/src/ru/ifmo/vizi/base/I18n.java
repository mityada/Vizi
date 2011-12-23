package ru.ifmo.vizi.base;

import java.text.MessageFormat;

/**
 * Supports internalization.
 *
 * @author  Georgiy Korneev
 * @version $Id: I18n.java,v 1.2 2003/12/25 12:31:41 geo Exp $
 */
public final class I18n {
    /**
     * This class cannot be instantiated.
     */
    private I18n() {
    }

    /**
     * Formats message with array of arguments.
     * @see java.text.MessageFormat
     *
     * @param format the format string.
     * @param parameters message parameters.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object[] parameters) {
        return MessageFormat.format(format, parameters);
    }

    /**
     * Formats message with one argument.
     * @see java.text.MessageFormat
     *
     * @param format the format string.
     * @param o0 parameter number 0.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object o0) {
        return MessageFormat.format(format, new Object[]{o0});
    }

    /**
     * Formats message with two arguments.
     * @see java.text.MessageFormat
     *
     * @param format the format string.
     * @param o0 parameter number 0.
     * @param o1 parameter number 1.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object o0, Object o1) {
        return MessageFormat.format(format, new Object[]{o0, o1});
    }

    /**
     * Formats message with three arguments.
     * @see java.text.MessageFormat
     *
     * @param format The format string.
     * @param o0 parameter number 0.
     * @param o1 parameter number 1.
     * @param o2 parameter number 2.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object o0, Object o1, Object o2) {
        return MessageFormat.format(format, new Object[]{o0, o1, o2});
    }

    /**
     * Formats message with four arguments.
     * @see java.text.MessageFormat
     *
     * @param format The format string.
     * @param o0 parameter number 0.
     * @param o1 parameter number 1.
     * @param o2 parameter number 2.
     * @param o3 parameter number 3.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object o0, Object o1, Object o2, Object o3) {
        return MessageFormat.format(format, new Object[]{o0, o1, o2, o3});
    }

    /**
     * Formats message with five arguments.
     * @see java.text.MessageFormat
     *
     * @param format The format string.
     * @param o0 parameter number 0.
     * @param o1 parameter number 1.
     * @param o2 parameter number 2.
     * @param o3 parameter number 3.
     * @param o4 parameter number 4.
     *
     * @return a <code>String</code> object that contains formatted message.
     */
    public static String message(String format, Object o0, Object o1, Object o2, Object o3, Object o4) {
        return MessageFormat.format(format, new Object[]{o0, o1, o2, o3, o4});
    }
}
