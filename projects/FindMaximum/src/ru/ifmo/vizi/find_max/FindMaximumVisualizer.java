package ru.ifmo.vizi.find_max;

import ru.ifmo.vizi.base.ui.*;
import ru.ifmo.vizi.base.*;
import ru.ifmo.vizi.base.widgets.Rect;
import ru.ifmo.vizi.base.widgets.ShapeStyle;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.*;
import java.util.Stack;

/**
 * Find maximum applet.
 *
 * @author  Georgiy Korneev
 * @version $Id: FindMaximumVisualizer.java,v 1.8 2006/04/05 12:05:28 geo Exp $
 */
public final class FindMaximumVisualizer 
    extends Base 
    implements AdjustmentListener
{
    /**
     * Find maximum automata instance.
     */
    private final FindMaximum auto;

    /**
     * Find maximum automata data.
     */
    private final FindMaximum.Data data;

    /**
     * Cells with array elements.
     * Vector of {@link Rect}.
     */
    private final Stack cells;

    /**
     * Number of elements in array.
     */
    private final AdjustablePanel elements;

    /**
     * Maximal array value.
     */
    private final int maxValue;

    /**
     * Maximal array value string.
     */
    private final String maxValueString;

    /**
     * Rectangle that contains maximum value.
     */
    private final Rect rectMax;

    /**
     * Max message tamplate.
     */
    private final String maxMessage;

    /**
     * Array shape style set.
     */
    private final ShapeStyle[] styleSet;

    /**
     * Save/load dialog.
     */
    private SaveLoadDialog saveLoadDialog;

    /**
     * Creates a new Find Maximum visualizer.
     *
     * @param parameters visualizer parameters.
     */
    public FindMaximumVisualizer(VisualizerParameters parameters) {
        super(parameters);
        auto = new FindMaximum(locale);
        data = auto.d;
        data.visualizer = this;
        cells = new Stack();

        maxMessage = config.getParameter("max-message");

        styleSet = ShapeStyle.loadStyleSet(config, "array");
        rectMax = new Rect(
                new ShapeStyle[]{new ShapeStyle(config, "max-style", styleSet[0])},
                "max"
        );
        clientPane.add(rectMax);
        rectMax.adjustSize();
        rectMax.setLocation(10, 10);

        elements = new AdjustablePanel(config, "elements");
        elements.addAdjustmentListener(this);

        maxValue = config.getInteger("max-value");
        maxValueString = config.getParameter("max-value-string", Integer.toString(maxValue));
        setArraySize(elements.getValue());
        randomize();

        createInterface(auto);
    }

    /**
     * This method creates panel with visualizer controls.
     *
     * @return controls pane.
     */
    public Component createControlsPane() {
        Container panel = new JPanel(new BorderLayout());

        panel.add(new AutoControlsPane(config, auto, forefather, false), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new HintedButton(config, "button-random"){
            protected void click() {
                randomize();
            }
        });
        if (config.getBoolean("button-ShowSaveLoad")) { 
            bottomPanel.add(new HintedButton(config, "button-SaveLoad") {
                protected void click() {
                    saveLoadDialog.center();
                    StringBuffer buffer = new StringBuffer();
                    int[] a = auto.d.a;
                    buffer.append("/* ").append(
                        I18n.message(
                            config.getParameter("ArrayLengthComment"), 
                            new Integer(elements.getMinimum()),
                            new Integer(elements.getMaximum())
                        )
                    ).append(" */\n");

                    buffer.append("ArrayLength = ").append(a.length).append("\n");

                    buffer.append("/* ").append(
                        I18n.message(
                            config.getParameter("ElementsComment"), 
                            new Integer(0),
                            new Integer(maxValue)
                        )
                    ).append(" */\n");

                    buffer.append("Elements = ");
                    for (int i = 0; i < a.length; i++) {
                        buffer.append(a[i]).append(" ");
                    }

                    buffer.append("\n/* ").append(
                        config.getParameter("StepComment")
                    ).append(" */\n");
                    buffer.append("Step = ").append(auto.getStep());
                    saveLoadDialog.show(buffer.toString());
                }
            });
        }
        bottomPanel.add(elements);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        saveLoadDialog = new SaveLoadDialog(config, forefather) {
            public boolean load(String text) throws Exception {
                SmartTokenizer tokenizer = new SmartTokenizer(text, config);
                tokenizer.expect("ArrayLength");
                tokenizer.expect("=");
                int[] a = new int[tokenizer.nextInt(
                    elements.getMinimum(), 
                    elements.getMaximum()
                )];

                tokenizer.expect("Elements");
                tokenizer.expect("=");
                for (int i = 0; i < a.length; i++) {
                    a[i] = tokenizer.nextInt(0, maxValue);
                }

                tokenizer.expect("Step");
                tokenizer.expect("=");
                int step = tokenizer.nextInt();
                tokenizer.expectEOF();

                setArraySize(a.length);
                auto.d.a = a;                
                auto.getController().rewind(step);

                return true;
            }
        };

        return panel;
    }

    /**
     * Adjusts array size to match current model size.
     */
    private void adjustArraySize() {
        int size = auto.d.a.length;
        while (cells.size() < size) {
            Rect rect = new Rect(styleSet);
            cells.push(rect);
            clientPane.add(rect);
        }
        while (cells.size() > size) {
            clientPane.remove((Component) cells.pop());
        }
        clientPane.doLayout();
    }

    /**
     * Sets new array size.
     *
     * @param size new array size.
     */
    private void setArraySize(int size) {
        auto.d.a = new int[size];
        elements.setValue(data.a.length);
        adjustArraySize();
    }

    /**
     * Randomizes array values.
     */
    private void randomize() {
        for (int i = 0; i < data.a.length; i++) {
            data.a[i] = (int) (Math.random() * maxValue) + 1;
        }
        auto.getController().doRestart();
    }

    /**
     * Invoked on adjustment event.
     *
     * @param event event to process.
     */
    public void adjustmentValueChanged(AdjustmentEvent event) {
        if (event.getSource() == elements) {
            setArraySize(event.getValue());
            randomize();
        }
    }


    /**
     * Updates array view.
     *
     * @param activeCell current active cell.
     * @param activeStyle style of active cell.
     */
    public void updateArray(int activeCell, int activeStyle) {
        rectMax.setMessage(I18n.message(maxMessage, new Integer(data.max)));
        rectMax.adjustSize();

        for (int i = 0; i < data.a.length; i++) {
            Rect rect = (Rect) cells.elementAt(i);
            rect.setMessage(Integer.toString(data.a[i]));
            rect.setStyle(i == activeCell ? activeStyle : 0);
        }
        update(true);
    }

    /**
     * Invoked when client pane shoud be layouted.
     *
     * @param clientWidth client pane width.
     * @param clientHeight client pane height.
     */
    protected void layoutClientPane(int clientWidth, int clientHeight) {
        int n = cells.size();

        Rectangle mb = rectMax.getBounds();

        int width = Math.round(clientWidth / (n + 1));
        int height = Math.min(width, (clientHeight - mb.x - mb.height) * 10 / 13);
        int y = mb.x + mb.height + height / 10;
        int x = (clientWidth - width * n) / 2;

        for (int i = 0; i < n; i++) {
            Rect rect = (Rect) cells.elementAt(i);
            rect.setBounds(x + i * width, y, width + 1, height + 1);
            rect.adjustFontSize(maxValueString);
        }
    }
}

