package ru.ifmo.vizi.base.widgets;

import java.awt.*;

/**
 * Current shape look.
 *
 * @author Georgiy Korneev
 * @version $Id: ShapeLook.java,v 1.3 2004/03/18 11:34:59 geo Exp $
 */
public class ShapeLook {
    /**
     * Default text align.
     */
    private final static double DEFAULT_ALIGN = 1353465;

    /**
     * Default padding.
     */
    private final static double DEFAULT_PADDING = 65433;

    /**
     * Positive status.
     */
    public final static int STATUS_SET = 1;

    /**
     * Negative status.
     */
    public final static int STATUS_CLEAR = - 1;

    /**
     * Undefined status.
     */
    public final static int STATUS_UNDEFINED = 0;

    /**
     * Shape's text color.
     */
    private Color textColor;

    /**
     * Shape's text font.
     */
    private Font textFont;

    /**
     * Text align in message.
     */
    private double textAlign = DEFAULT_ALIGN;

    /**
     * Message align.
     */
    private double messageAlign = DEFAULT_ALIGN;

    /**
     * Shape's padding.
     */
    private double padding = DEFAULT_PADDING;

    /**
     * Shape's border color.
     */
    private Color borderColor;

    /**
     * Shape border status.
     */
    private int borderStatus;

    /**
     * Shapes's interior color.
     */
    private Color fillColor;

    /**
     * Shape fill status.
     */
    private int fillStatus;

    /**
     * Shape's aspect.
     */
    private double aspect;

    /**
     * Shape aspect status.
     */
    private int aspectStatus;

    /**
     * Gets shape's text color.
     *
     * @param style default shape's style.
     *
     * @return shape's text color.
     */
    public Color getTextColor(ShapeStyle style) {
        return textColor != null ? textColor : style.getTextColor();
    }

    /**
     * Sets shape's text color.
     *
     * @param textColor shape's text color to set.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * Gets shape's text font.
     *
     * @param style default shape's style.
     *
     * @return shape's text font.
     */
    public Font getTextFont(ShapeStyle style) {
        return textFont != null ? textFont : style.getTextFont();
    }

    /**
     * Sets shape's text font.
     *
     * @param textFont shape's text font to set.
     */
    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    /**
     * Gets shape's text align.
     *
     * @param style default shape's style.
     *
     * @return shape's text align.
     */
    public double getTextAlign(ShapeStyle style) {
        return textAlign != DEFAULT_ALIGN ? textAlign : style.getTextAlign();
    }

    /**
     * Sets shape's text align.
     *
     * @param textAlign shape's text align to set.
     */
    public void setTextAlign(double textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Gets shape's message align.
     *
     * @param style default shape's style.
     *
     * @return shape's message align.
     */
    public double getMessageAlign(ShapeStyle style) {
        return textAlign != DEFAULT_ALIGN ? textAlign : style.getMessageAlign();
    }

    /**
     * Sets shape's message align.
     *
     * @param messageAlign shape's message align to set.
     */
    public void setMessageAlign(double messageAlign) {
        this.messageAlign = messageAlign;
    }

    /**
     * Gets shape's padding.
     *
     * @param style default shape's style.
     *
     * @return shape's padding.
     */
    public double getPadding(ShapeStyle style) {
        return padding != DEFAULT_PADDING ? padding : style.getPadding();
    }

    /**
     * Sets shape's padding.
     *
     * @param padding shape's padding to set.
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * Gets shape's border color.
     *
     * @param style default shape's style.
     *
     * @return shape's border color.
     */
    public Color getBorderColor(ShapeStyle style) {
        return borderColor != null ? borderColor : style.getBorderColor();
    }

    /**
     * Sets shape's border color.
     *
     * @param borderColor shape's border color to set.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Gets shape's border status.
     *
     * @param style default shape's style.
     *
     * @return shape's border stauts.
     */
    public boolean getBorderStatus(ShapeStyle style) {
        return borderStatus != STATUS_UNDEFINED
                ? borderStatus > STATUS_UNDEFINED
                : style.getBorderStatus();
    }

    /**
     * Sets shape's border status.
     *
     * @param borderStatus shape's border status to set.
     */
    public void setBorderStatus(int borderStatus) {
        this.borderStatus = borderStatus;
    }

    /**
     * Gets shape's fill color.
     *
     * @param style default shape's style.
     *
     * @return shape's fill color.
     */
    public Color getFillColor(ShapeStyle style) {
        return fillColor != null ? fillColor : style.getFillColor();
    }

    /**
     * Sets shape's fill color.
     *
     * @param fillColor shape's fill color to set.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets shape's interior status.
     *
     * @param style default shape's style.
     *
     * @return shape's interior status.
     */
    public boolean getFillStatus(ShapeStyle style) {
        return fillStatus != STATUS_UNDEFINED
                ? fillStatus > STATUS_UNDEFINED
                : style.getFillStatus();
    }

    /**
     * Sets shape's interior status.
     *
     * @param fillStatus shape's interior status to set.
     */
    public void setFillStatus(int fillStatus) {
        this.fillStatus = fillStatus;
    }

    /**
     * Gets shape's aspect.
     *
     * @param style default shape's style.
     *
     * @return shape's aspect status.
     */
    public double getAspect(ShapeStyle style) {
        return aspectStatus > STATUS_UNDEFINED ? aspect : style.getAspect();
    }

    /**
     * Sets shape's aspect.
     *
     * @param aspect shape's aspect to set.
     */
    public void setAspect(double aspect) {
        this.aspect = aspect;
    }

    /**
     * Gets shape's aspect status.
     *
     * @param style default shape's style.
     *
     * @return shape's aspect status.
     */
    public boolean getAspectStatus(ShapeStyle style) {
        return aspectStatus != STATUS_UNDEFINED
                ? aspectStatus > STATUS_UNDEFINED
                : style.getAspectStatus();
    }

    /**
     * Sets shape's aspect status.
     *
     * @param aspectStatus shape's aspect status to set.
     */
    public void setAspectStatus(int aspectStatus) {
        this.aspectStatus = aspectStatus;
    }
}
