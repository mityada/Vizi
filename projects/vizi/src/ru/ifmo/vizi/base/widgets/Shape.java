package ru.ifmo.vizi.base.widgets;

import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * Base class for all shapes.
 * Supports size and font size adjustments.
 *
 * @author  Georgiy Korneev
 * @version $Id: Shape.java,v 1.3 2004/06/07 13:55:50 geo Exp $
 */
public abstract class Shape extends Component {
    /**
     * Shape's message.
     */
    private Message message = new Message("", 0);

    /**
     * Shape look.
     */
    protected ShapeLook look;

    /**
     * Shape styles.
     */
    private ShapeStyle[] styleSet;

    /**
     * Current shape style.
     */
    protected ShapeStyle style;

    /**
     * Shape style index.
     */
    private int styleIndex;

    /**
     * Whether to adjust font size automatically.
     */
    private boolean adjustFontSize;

    /**
     * The message that shows messge to adjust font size to.
     */
    private Message adjustFontSizeMessage;

    /**
     * Creates a new shape with specified style set and empty message.
     *
     * @param styleSet shape's style set.
     */
    public Shape(ShapeStyle[] styleSet) {
        this(styleSet, "");
    }

    /**
     * Creates a new shape with specifed style set and message.
     *
     * @param styleSet shape's style set.
     * @param message shape's message.
     */
    public Shape(ShapeStyle[] styleSet, String message) {
        setStyleSet(styleSet);
        setMessage(message);

        this.look = new ShapeLook();

        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
    }

    /**
     * Gets current message.
     *
     * @return current message.
     */
    public final String getMessage() {
        return message.getMessage();
    }

    /**
     * Sets shape's message.
     *
     * @param message shape's message.
     */
    public final void setMessage(String message) {
        this.message = new Message(message, this.message.getAlign());
    }

    /**
     * Adjust shape's font size to show specified message.
     *
     * @param message template message.
     */
    private void adjustFontSize(Message message) {
        Dimension size = getSize();
        Font font = look.getTextFont(style);
        int l = 5;
        int r = 1000;
        while (l < r) {
            int m = (l + r + 1) / 2;
            FontMetrics metrics = getFontMetrics(new Font(font.getName(), font.getStyle(), m));
            Dimension minSize = fit(message.calculateSize(metrics, look.getPadding(style)));
            if (size.width < minSize.width || size.height < minSize.height) {
                r = m - 1;
            } else {
                l = m;
            }
        }
        look.setTextFont(new Font(font.getName(), font.getStyle(), l));
    }

    /**
     * Adjust shape's font size to show specified message.
     *
     * @param message message
     */
    public final void adjustFontSize(String message) {
        adjustFontSize(new Message(message, this.message.getAlign()));
    }

    /**
     * Adjust shape's font size to show shape's message.
     */
    public final void adjustFontSize() {
        adjustFontSize(message);
    }

    /**
     * Adjust shape size to show specified message.
     *
     * @param message message to show.
     */
    private void adjustSize(Message message) {
        setSize(fit(message.calculateSize(getFontMetrics(style.getTextFont()), look.getPadding(style))));
    }

    /**
     * Adjust shape's size to show specified message.
     *
     * @param message message to show.
     */
    public void adjustSize(String message) {
        adjustSize(new Message(message, this.message.getAlign()));
    }

    /**
     * Adjust shape's size to show shape's message.
     */
    public void adjustSize() {
        adjustSize(message);
    }

    /**
     * Returns minimum shape size to contain message with specified size.
     *
     * @param size message size.
     *
     * @return minimum shape size.
     */
    protected abstract Dimension fit(Dimension size);

    /**
     * Gets shape's look.
     *
     * @return shape's look.
     */
    public ShapeLook getLook() {
        return look;
    }

    /**
     * Sets shape's look.
     *
     * @param look look to set.
     */
    public void setLook(ShapeLook look) {
        this.look = look;
    }

    /**
     * Get shape's style set.
     *
     * @return shape's style set.
     */
    public ShapeStyle[] getStyleSet() {
        return styleSet;
    }

    /**
     * Set shape's style set.
     *
     * @param styleSet style set to set.
     */
    public void setStyleSet(ShapeStyle[] styleSet) {
        this.styleSet = styleSet;
        setStyle(styleIndex);
    }

    /**
     * Gets shape's style index.
     *
     * @return shape's style index.
     */
    public int getStyle() {
        return styleIndex;
    }

    /**
     * Set shape's style index.
     *
     * @param style styleindex to set.
     */
    public void setStyle(int style) {
        if (style < 0) {
            style = 0;
        } else if (style >= styleSet.length) {
            style = styleSet.length - 1;
        }
        styleIndex = style;
        this.style = styleSet[styleIndex];
    }

    /**
     * Gets the preferred size of this shape
     * .
     * @return shape's preferred size.
     */
    public Dimension getPreferredSize() {
        return getSize();
    }

    /**
     * Gets the minimum size of this shape
     * .
     * @return shape's minimum size.
     */
    public Dimension getMinimumSize() {
        return getSize();
    }

    /**
     * Gets the maximum size of this shape
     * .
     * @return shape's maximum size.
     */
    public Dimension getMaximumSize() {
        return getSize();
    }

    /**
     * Updates this shape.
     *
     * @param g graphics context for updating.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Paints this component.
     *
     * @param g graphics context for painting.
     */
    public void paint(Graphics g) {
        g.setFont(look.getTextFont(style));
        g.setColor(look.getTextColor(style));
        message.setAlign(look.getTextAlign(style));

        Dimension size = getSize();
        Dimension msize = message.calculateSize(g.getFontMetrics(), 0);
        message.draw(g, 
                (int) (look.getMessageAlign(style) * (size.width - msize.width)), 
                (size.height - msize.height) / 2
        );
    }

    /**
     * Sets automatically font size adjustment flag.
     *
     * @param adjustFontSize whether to adjust font size automatically;
     */
    public void setAdjustFontSize(boolean adjustFontSize) {
        this.adjustFontSize = adjustFontSize;
    }

    /**
     * Gets whether automatical font size adjustment is on.
     * 
     * @return whether automatical font size adjustment is on.
     */
    public boolean setAdjustFontSize() {
        return adjustFontSize;
    }

    /**
     * Sets automatical font size adjustment mode and message
     * to perform adjustmnet relative to.
     *
     * @param message message to perform adjustment relative to.
     */
    public void setAdjustFontSizeMessage(Message message) {
        setAdjustFontSize(true);
        this.adjustFontSizeMessage = message;
    }

    /**
     * Sets automatical font size adjustment mode and message
     * to perform adjustmnet relative to.
     *
     * @param message message to perform adjustment relative to.
     */
    public void setAdjustFontSizeMessage(String message) {
        setAdjustFontSizeMessage(new Message(message, this.message.getAlign()));
    }

    /**
     * Gets message the automatical font size adjustment
     * is performed relative to.
     *
     * @return automatical font size adjustment message.
     */
    public Message getAdjustFontSizeMessage() {
        return adjustFontSizeMessage;
    }

    /**
     * Retuns a center point of this shape.
     *
     * @return center point of this shape.
     */
    public Point getCenter() {
        Dimension size = getSize();
        return new Point(size.width / 2, size.height / 2);
    }

    /**
     * Proceses resize event from this conponent.
     *
     * @param e event to process.
     */
    public void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        if (adjustFontSize && e.getID() == ComponentEvent.COMPONENT_RESIZED) {
            Message message = getAdjustFontSizeMessage();
            if (message != null) {
                adjustFontSize(message);
            } else {
                adjustFontSize();
            }
        }
    }
}
