package ru.ifmo.vizi.base;

import java.awt.*;
import java.util.*;

/**
 * Represents visualizer configuration.
 *
 * <p>
 * Font configuration parameters for {@link #getFont(String)} and {@link #getFont(String, Font)}:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *      </th>
 *      <tr>
 *          <td><code><b>prefix</b>-face</code></td>
 *          <td>Font face. One of</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-size</code></td>
 *          <td>Font size</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-style</code></td>
 *          <td>Font style</td>
 *      </tr>
 * </table>
 * </p><p>
 * Valid font names:
 * <ul>
 *      <li>Monospaced</li>
 *      <li>Serif</li>
 *      <li>SansSerif</li>
 *      <li>Symbol</li>
 *      <li>Dialog</li>
 * </ul>
 * </p><p>
 * Valid font styles:
 * <ul>
 *      <li>plain</li>
 *      <li>bold</li>
 *      <li>italic</li>
 *      <li>bold italic</li>
 * </ul>
 * </p>
 *
 * @author  Georgiy Korneev
 * @version $Id: Configuration.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public final class Configuration {
    /**
     * Configuration parameters.
     */
    private final Properties parameters;

    /**
     * Creates a new configuration out of the bundle.
     *
     * @param bundle name of the bundle to load parameters from.
     * @param locale locale to load bundle for.
     */
    public Configuration(String bundle, Locale locale) {
        parameters = new Properties();
        loadParameters("ru.ifmo.vizi.base.DefaultConfiguration", locale);
        loadParameters("ru.ifmo.vizi.base.Localization", locale);

        if (bundle != null) {
            loadParameters(bundle, locale);
        }
    }

    /**
     * Returns a color value represented by the parameter.
     *
     * @param name name of the parameter.
     * @return configured color.
     * @throws RuntimeException if configured color is invalid or not found.
     */
    public Color getColor(String name) {
        try {
            return new Color(Integer.parseInt(getParameter(name), 16));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid color in parameter " + name);
        }
    }

    /**
     * Returns a color value represented by the parameter.
     * Default value is returned if parameter does not exist.
     *
     * @param name name of the parameter.
     * @param def default color, in 0xRRGGBB format.
     * @return configured color.
     * @throws RuntimeException if configured color is invalid.
     */
    public Color getColor(String name, int def) {
        return getColor(name, new Color(def));
    }

    /**
     * Returns a color value represented by the parameter.
     * Default value is returned if the parameter does not exist.
     *
     * @param name name of the parameter.
     * @param def default color.
     * @return configured color.
     * @throws RuntimeException if configured color is invalid.
     */
    public Color getColor(String name, Color def) {
        if (hasParameter(name)) {
            return getColor(name);
        } else {
            return def;
        }
    }

    /**
     * Returns an integer value represented by the parameter.
     *
     * @param name name of the parameter.
     * @return configured value.
     * @throws RuntimeException if configured color is invalid or not found.
     */
    public int getInteger(String name) {
        try {
            return Integer.parseInt(getParameter(name));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid integer in parameter " + name);
        }
    }

    /**
     * Returns an integer value represented by the parameter.
     * Default value is returned if the parameter not exist.
     *
     * @param name name of the parameter.
     * @param def default integer to be returned.
     * @return configured integer.
     * @throws RuntimeException if configured integer is invalid.
     */
    public int getInteger(String name, int def) {
        if (hasParameter(name)) {
            return getInteger(name);
        } else {
            return def;
        }
    }

    /**
     * Returns a double value represented by the parameter.
     *
     * @param name name of the parameter.
     * @return configured double.
     * @throws RuntimeException if configured double is invalid.
     */
    public double getDouble(String name) {
        try {
            return Double.valueOf(getParameter(name)).doubleValue();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid integer in parameter " + name);
        }
    }

    /**
     * Returns a double value represented by the parameter.
     * Default value is returned if the parameter does not exist.
     *
     * @param name name of the parameter.
     * @param def default double to be returned.
     * @return configured double.
     * @throws RuntimeException if configured double is invalid.
     */
    public double getDouble(String name, double def) {
        if (hasParameter(name)) {
            return getDouble(name);
        } else {
            return def;
        }
    }

    /**
     * Returns a boolean value represented by the parameter.
     *
     * @param name name of the parameter.
     * @return configured boolean.
     * @throws RuntimeException if configured boolean is invalid or not exist.
     */
    public boolean getBoolean(String name) {
        String value = getParameter(name);
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
            return true;
        } else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
            return false;
        } else {
            throw new RuntimeException("Invalid boolean in parameter " + name);
        }
    }

    /**
     * Returns a boolean value represented by the parameter.
     * Default value is returned if the parameter does not exist.
     *
     * @param name name of the parameter.
     * @param def default boolean to be returned.
     * @return configured boolean.
     * @throws RuntimeException if configured boolean is invalid.
     */
    public boolean getBoolean(String name, boolean def) {
        if (hasParameter(name)) {
            return getBoolean(name);
        } else {
            return def;
        }
    }

    /**
     * Returns a font style represented by the parameter.
     *
     * @param name name of the parameter.
     * @return configured foont style.
     * @throws RuntimeException if configured style does not exist.
     */
    private int getFontStyle(String name) {
        int style = Font.PLAIN;
        String fontStyle = getParameter(name).toUpperCase();
        if (fontStyle.indexOf("BOLD") != -1) {
            style |= Font.BOLD;
        }
        if (fontStyle.indexOf("ITALIC") != -1) {
            style |= Font.ITALIC;
        }
        return style;
    }

    /**
     * Laod a font from the configuration.
     *
     * @param prefix prefix of the paramters.
     * @return configured font.
     * @throws RuntimeException if configured font is invalid or incomplete.
     */
    public Font getFont(String prefix) {
        return new Font(
                getParameter(prefix + "-face"),
                getFontStyle(prefix + "-style"),
                getInteger(prefix + "-size")
        );
    }

    /**
     * Loads a font from the configuration.
     *
     * @param  prefix prefix of the parameters.
     * @param def default font.
     * @return configured font style.
     * @throws RuntimeException if configured font is invalid.
     */
    public Font getFont(String prefix, Font def) {
        return new Font(
                getParameter(prefix + "-face", def.getName()),
                hasParameter(prefix + "-style")
                    ? getFontStyle(prefix + "-style")
                    : def.getStyle(),
                getInteger(prefix + "-size", def.getSize())
        );
    }

    /**
     * Tests whether specified parameter exists.
     *
     * @param name name of the parameter to test.
     * @return whether specified parameter exists.
     */
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }

    /**
     * Gets parameter with specified name.
     *
     * @param name the name of the parameter.
     * @return value of specified parameter.
     */
    public final String getParameter(String name) {
        String value = parameters.getProperty(name);
        if (value == null) {
            throw new RuntimeException("Parameter '" + name + "' not found");
        }
        return value;
    }

    /**
     * Gets parameter with specified name.
     * Default value is returned if the parameter not exist.
     *
     * @param name the name of the parameter.
     * @param def default string to be returned.
     * @return value of specified parameter.
     */
    public final String getParameter(String name, String def) {
        return parameters.getProperty(name, def);
    }

    /**
     * Parses parameters string.
     *
     * @param prefix string name prefix.
     * @param str string to process.
     */
    private void parseParameter(String prefix, String str) {
        int p = 0;
        for (int i = str.indexOf('!'); i >= 0; i = str.indexOf('!', i + 1)) {
            if (i < str.length() - 1 && str.charAt(i + 1) == '!') {
                i++;
            } else {
                parseChunk(prefix, str.substring(p, i));
                p = i + 1;
            }
        }
        parseChunk(prefix, str.substring(p));
    }

    /**
     * Parses parameter chunk.
     *
     * @param prefix parameter name prefix.
     * @param chunk chunk to parse
     */
    private void parseChunk(String prefix, String chunk) {
        int i = chunk.indexOf(':');

        while (i > 0 && i < chunk.length() - 1 && chunk.charAt(i + 1) == ':') {
            i = chunk.indexOf('!', i + 1);
        }

        final String key;
        final String value;
        if (i < 0) {
            key = prefix;
            value = chunk;
        } else {
            key = prefix + "-" + chunk.substring(0, i).trim();
            value = chunk.substring(i + 1).trim();
        }
        if (value.length() > 0) {
            if (parameters.containsKey(key)) {
                System.err.println("Duplicate parameter: " + key);
            } else {
                parameters.put(key, convertString(value));
            }
        }
    }

    /**
     * Removes special characters from the string.
     *
     * @param str string to remove characters from.
     *
     * @return result string.
     */
    private static String convertString(String str) {
        StringBuffer result = new StringBuffer(str.length());
        char[] chars = str.toCharArray();
        int i = 0;
        while (i < chars.length - 1) {
            switch (chars[i]) {
                case '!':
                    result.append('!');
                    i += 2;
                    break;
                case ':':
                    result.append(':');
                    if (chars[i + 1] == ':') {
                        i += 2;
                    } else {
                        i += 1;
                    }
                    break;
                case '\\':
                    switch (chars[i + 1]) {
                        case 'n':
                            result.append('\n');
                            break;
                        case '\\':
                            result.append('\\');
                            break;
                        default:
                            result.append(chars[i + 1]);
                    }
                    i += 2;
                    break;
                default:
                    result.append(chars[i]);
                    i += 1;
            }
        }
        if (i < chars.length) {
            result.append(chars[i]);
        }
        return new String(result);
    }

    /**
     * Loads parameters from specified bundle.
     *
     * @param bundleName name of the bundle to load parameters from.
     * @param locale locale to use.
     */
    private void loadParameters(String bundleName, Locale locale) {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(bundleName, locale);
        } catch (MissingResourceException e) {
            try {
                bundle = ResourceBundle.getBundle(bundleName, Locale.US);
            } catch (MissingResourceException ex) {
                System.err.println("Cannot find bundle " + bundleName);
            }
        }

        if (bundle != null) {
            for (Enumeration e = bundle.getKeys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();
                if (key.startsWith("load-bundle")) {
                    loadParameters(bundle.getString(key), locale);
                } else {
                    parseParameter(key, bundle.getString(key));
                }
            }
        }
    }
}
