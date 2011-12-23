package ru.ifmo.vizi.base.widgets;

import ru.ifmo.vizi.base.Configuration;

import java.awt.*;

/**
 * Shape style.
 *
 * <p>
 * Shape style configuration paramters for
 * {@link #ShapeStyle(Configuration, String)} and
 * {@link #ShapeStyle(Configuration, String, ShapeStyle)}.
 * Returns a configured shape style. If default value is <code>null</code>,
 * predefinded style used instead.
 * <p>
 * Used configuration parameters:
 * <table border="1">
 *      <tr>
 *          <th>Name</th>
 *          <th>Description</th>
 *      </th>
 *      <tr>
 *          <td><code><b>prefix</b>-text-color</code></td>
 *          <td>Shape's text color</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-font</code></td>
 *          <td>Shape's text font</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-text-align</code></td>
 *          <td>Align of the text lines in the multi-line message</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-message-align</code></td>
 *          <td>Align of the message in shape</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-border-color</code></td>
 *          <td>Shapes's border color</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-border-status</code></td>
 *          <td>Shape's border status</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-fill-color</code></td>
 *          <td>Shapes's interior color</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-fill-status</code></td>
 *          <td>Shape's interior status</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-aspect</code></td>
 *          <td>Shape's aspect</td>
 *      </tr>
 *      <tr>
 *          <td><code><b>prefix</b>-aspect-status</code></td>
 *          <td>Shape's aspect status</td>
 *      </tr>
 * </table>
 * </p><p>
 * Text and message aligns are a real number in range [0..1]. 
 * The free space is distributed to the left and to the right 
 * of the message with weights <i>align</i> and <i>(1 - align)</i> 
 * respectively.
 * So <i>align = 0</i> corresponds to the left align,
 * <i>align = 1</i> corresponds to the right align and
 * <i>align = 0.5</i> corresponds to the center align.
 * </p><p>
 * Padding is a positive real number. Horizontal (vertical) text indent 
 * is font width (height) multiplied by aspect.
 * </p><p>
 * Aspect is a positive real number &mdash; desired shape aspect
 * ration (width-to-height ratio).
 * </p>
 *
 * @author  Georgiy Korneev
 * @version $Id: ShapeStyle.java,v 1.2 2004/03/17 11:26:31 geo Exp $
 */
public class ShapeStyle {
    /**
     * Shape's text color.
     */
    private final Color textColor;

    /**
     * Shape's text font.
     */
    private final Font textFont;

    /**
     * Text align in message.
     */
    private final double textAlign;

    /**
     * Message's align.
     */
    private final double messageAlign;

    /**
     * Shape's padding.
     */
    private final double padding;

    /**
     * Shape's border color.
     */
    private final Color borderColor;

    /**
     * Shape border status.
     */
    private final boolean borderStatus;

    /**
     * Shapes's interior color.
     */
    private final Color fillColor;

    /**
     * Shape fill status.
     */
    private final boolean fillStatus;

    /**
     * Shape aspect.
     */
    private final double aspect;

    /**
     * Shape aspect status.
     */
    private final boolean aspectStatus;

    /**
     * Loads a shape style from configuration.
     *
     * @param config visualizer's configuration.
     * @param prefix configuration parameters prefix.
     * @throws RuntimeException if illegal or incomplete shape style configured.
     */
    public ShapeStyle(Configuration config, String prefix) {
        textColor = config.getColor(prefix + "-text-color");
        textFont = config.getFont(prefix + "-font");
        textAlign = config.getDouble(prefix + "-text-align");
        messageAlign = config.getDouble(prefix + "-message-align", 0.5);
        padding = config.getDouble(prefix + "-padding");

        borderStatus = config.getBoolean(prefix + "-border-status");
        borderColor = borderStatus
                ? config.getColor(prefix + "-border-color")
                : null;

        fillStatus = config.getBoolean(prefix + "-fill-status");
        fillColor = fillStatus
                ? config.getColor(prefix + "-fill-color")
                : null;
        aspectStatus = config.getBoolean(prefix + "-aspect-status");
        aspect = aspectStatus
                ? config.getDouble(prefix + "-aspect")
                : 0;
    }

    /**
     * Loads a shape style from configuration.
     *
     * @param config visualizer's configuration.
     * @param prefix configuration parameters prefix.
     * @param def default shape style.
     * @throws RuntimeException if illegal shape style configured.
     */
    public ShapeStyle(Configuration config, String prefix, ShapeStyle def) {
        textColor = config.getColor(prefix + "-text-color", def.getTextColor());
        textFont = config.getFont(prefix + "-font", def.getTextFont());
        textAlign = config.getDouble(prefix + "-text-align", def.getTextAlign());
        messageAlign = config.getDouble(prefix + "-message-align", def.getMessageAlign());
        padding = config.getDouble(prefix + "-padding", def.getPadding());
        borderColor = config.getColor(prefix + "-border-color", def.getBorderColor());
        borderStatus = config.getBoolean(prefix + "-border-status", def.getBorderStatus());
        fillColor = config.getColor(prefix + "-fill-color", def.getFillColor());
        fillStatus = config.getBoolean(prefix + "-fill-status", def.getFillStatus());
        aspect = config.getDouble(prefix + "-aspect", def.getAspect());
        aspectStatus = config.getBoolean(prefix + "-aspect-status", def.getAspectStatus());
    }

    /**
     * Creates a new shape style.
     *
     * @param textColor color for the shape's text.
     * @param textFont font for the shape's text.
     * @param textAlign align for the text in message.
     * @param messageAlign align for the message.
     * @param padding padding for the shape.
     * @param borderColor color for the shape's border.
     * @param borderStatus whether shape has a border.
     * @param fillColor color for the shape's interior.
     * @param fillStatus whether to fill shape's interior.
     * @param aspect shape's aspect.
     * @param aspectStatus shape's aspect status.
     */
    public ShapeStyle(Color textColor, Font textFont,
                      double textAlign, double messageAlign, 
                      double padding, 
                      Color borderColor, boolean borderStatus,
                      Color fillColor, boolean fillStatus,
                      double aspect, boolean aspectStatus)
    {
        this.textColor = textColor;
        this.textFont = textFont;
        this.textAlign = textAlign;
        this.messageAlign = messageAlign;
        this.padding = padding;
        this.borderColor = borderColor;
        this.borderStatus = borderStatus;
        this.fillColor = fillColor;
        this.fillStatus = fillStatus;
        this.aspect = aspect;
        this.aspectStatus = aspectStatus;
    }

    /**
     * Gets color for the shape's text.
     *
     * @return color for the shape's text.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Gets font for the shape's text.
     *
     * @return font for the shape's text.
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * Gets align for the text in message.
     *
     * @return align for the text in message.
     */
    public double getTextAlign() {
        return textAlign;
    }

    /**
     * Gets align for the message.
     *
     * @return align for the message.
     */
    public double getMessageAlign() {
        return messageAlign;
    }

    /**
     * Gets shape's padding.
     *
     * @return shape's padding.
     */
    public double getPadding() {
        return padding;
    }

    /**
     * Gets color for the shape's border.
     *
     * @return color for the shape's border.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Gets color for the shape's interior.
     *
     * @return color for the shape's interior.
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Gets shape's border status.
     *
     * @return shape's border status.
     */
    public boolean getBorderStatus() {
        return borderStatus;
    }

    /**
     * Gets shapes's interior status.
     *
     * @return shapes's interior status.
     */
    public boolean getFillStatus() {
        return fillStatus;
    }

    /**
     * Gets shape's aspect.
     *
     * @return shape's aspect.
     */
    public double getAspect() {
        return aspect;
    }

    /**
     * Gets shape's aspect status.
     *
     * @return shape's aspect status.
     */
    public boolean getAspectStatus() {
        return aspectStatus;
    }

    /**
     * Loads styleset.
     *
     * @param config visualizer's configuration.
     * @param prefix prefix of the style set's parameters.
     * @return loaded style set.
     */
    public static ShapeStyle[] loadStyleSet(Configuration config, String prefix) {
        if (config.getInteger(prefix + "-styles") > 0) {
            return loadStyleSet(config, prefix, new ShapeStyle(config, prefix + "-style0"));
        } else {
            return new ShapeStyle[0];
        }
    }

    /**
     * Loads style set.
     *
     * @param config visualizer's configuration.
     * @param prefix prefix of the style set's parameters.
     * @param def default style.
     * @return loaded style set.
     */
    public static ShapeStyle[] loadStyleSet(Configuration config, String prefix, ShapeStyle def) {
        ShapeStyle[] styleSet = new ShapeStyle[config.getInteger(prefix + "-styles")];
        for (int i = 0; i < styleSet.length; i++) {
            styleSet[i] = new ShapeStyle(config, prefix + "-style" + i, def);
        }
        return styleSet;
    }
}
