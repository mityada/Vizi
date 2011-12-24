package ru.ifmo.vizi.base.ui;

import ru.ifmo.vizi.base.*;
import ru.ifmo.vizi.base.ui.HintedButton;
import ru.ifmo.vizi.base.ui.Hinter;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.*;

/**
 * Dialog that provides routines for saving and loading data.
 * This dialog permits user to save/load data to/from file or clipboard
 * if <code>SecurityManager</code> gave the permission to do it.
 * <p>
 * Usage example:
 * <pre>
 * private final SaveLoadDialog saveLoadDialog = new SaveLoadDialog(config, forefather) {
 *     protected void load(String text) throws Exception {
 *          SmartTokenizer tokenizer = new SmartTokenizer(text);
 *          int n = tokenizer.nextInt();
 *          // ....
 *          return true;
 *     }
 * }
 *
 * // Saves current visualizer state to the sting.
 * private String save() {
 *      // ...
 * }
 *
 * void click() {
 *      saveLoadDialog.center();
 *      saveLoadDialog.show(save());
 * }
 * </pre>
 *
 * @author Georgiy Korneev, Vladimir Kotov
 * @version $Id: SaveLoadDialog.java,v 1.3 2003/12/26 14:11:22 geo Exp $
 */
public abstract class SaveLoadDialog extends ModalDialog {
    /**
     * Encapsulated text area.
     */
    private final TextArea textArea;

    /**
     * Comment pane.
     */
    private final CommentPane commentPane;

    /**
     * Intial content of the dialog.
     */
    private String initialContent;

    /**
     * Clipboard (if avaliable).
     */
    private Clipboard clipboard;

    /**
     * Creates a new save/load dialog.
     *
     * @param config the configuration.
     * @param prefix configuration prefix.
     * @param owner the owner of applet
     */
    public SaveLoadDialog(final Configuration config, final String prefix, final JFrame owner) {
        super(owner, config.getParameter(prefix + "-title"));

        Hinter hinter = new Hinter(config);
        add(hinter);

        Container panel = hinter.getContentPane();

        panel.setLayout(new BorderLayout());

        Font font = config.getFont(prefix + "-font");
        setFont(font);

        textArea = new TextArea(
            config.getInteger(prefix+"-rows", 5),
            config.getInteger(prefix+"-columns", 40)
        );

        textArea.setFont(config.getFont(prefix + "-text-font", font));
        JPanel buttonAndCommentPane = new JPanel(new BorderLayout());
        commentPane = new CommentPane(config, prefix + "-CommentPane");
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(buttonAndCommentPane, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setForeground(config.getColor("SaveLoadDialog-CommentPane-foreground"));
        bottomPanel.setBackground(config.getColor("SaveLoadDialog-CommentPane-background"));
        JPanel northPanel = new JPanel();

        northPanel.add(new HintedButton(config, prefix + "-restore") {
            protected void click() {
                resetContent();
            }
        });

        northPanel.add(new HintedButton(config, prefix + "-load") {
            protected void click() {
                try {
                    if (load(getContent())) {
                        SaveLoadDialog.this.setVisible(false);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    setComment(e.getMessage());
                }
            }
        });

        bottomPanel.add(northPanel, BorderLayout.NORTH);

        try  {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        } catch (SecurityException e) {
            clipboard = null;
        }
        if (clipboard != null) {
            JPanel southPanel = new JPanel();
            southPanel.add(new HintedButton(config, prefix + "-copy") {
                protected void click() {
                    clipboard.setContents(
                        new StringSelection(getContent()), null);
                }
            });

            southPanel.add(new HintedButton(config, prefix + "-paste") {
                protected void click() {
                    Transferable content = clipboard.getContents(this);
                    try {
                        String s = (String) content.getTransferData(DataFlavor.stringFlavor);
                        textArea.setText(s);
                    } catch (UnsupportedFlavorException e) {
                        System.err.println(e);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            });

            southPanel.add(new HintedButton(config, prefix + "-saveFile") {
                protected void click() {
                    File f = showFileDialog(
                        config.getParameter(prefix + "-saveTitle"),
                        JFileChooser.SAVE_DIALOG);

                    if (f != null) {
                        Writer writer = null;
                        try {
                            writer = new BufferedWriter(new FileWriter(f));
                            writer.write(getContent());
                        } catch (IOException e) {
                            setComment(I18n.message(
                                config.getParameter(prefix + "-CannotWriteFile"), f));
                            System.out.println(f);
                        } finally {
                            if (writer != null) {
                                try {
                                    writer.close();
                                } catch (IOException e) {}
                            }
                        }
                    }
                }
            });

            southPanel.add(new HintedButton(config, prefix + "-loadFile") {
                protected void click() {
                    File f = showFileDialog(
                        config.getParameter(prefix + "-loadTitle"),
                        JFileChooser.OPEN_DIALOG);

                    if (f != null) {
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new FileReader(f));
                            StringBuffer b = new StringBuffer();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                b.append(line).append('\n');
                            }
                            setContent(b.toString());
                        } catch (IOException e) {
                            setComment(I18n.message(
                                config.getParameter(prefix + "-CannotReadFile"), f));
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {}
                            }
                        }
                    }
                }
            });
            bottomPanel.add(southPanel, BorderLayout.SOUTH);
        }
        buttonAndCommentPane.add(commentPane, BorderLayout.CENTER);
        buttonAndCommentPane.add(bottomPanel, BorderLayout.SOUTH);
        setResizable(true);

        pack();
        center();
    }

    private File showFileDialog(String title, int mode) {
        JFileChooser dialog = new JFileChooser();
        dialog.setDialogTitle(title);
        dialog.setDialogType(mode);

        if (mode == JFileChooser.OPEN_DIALOG) {
            dialog.showOpenDialog(getOwner());
        }
        if (mode == JFileChooser.SAVE_DIALOG) {
            dialog.showSaveDialog(getOwner());
        }
        
        return dialog.getSelectedFile();
    }

    /**
     * Creates a new save/load dialog with <code>SaveLoadDialog</code> prefix.
     *
     * @param config the configuration.
     * @param owner the owner of applet
     */
    public SaveLoadDialog(Configuration config, JFrame owner) {
        this(config, "SaveLoadDialog", owner);
    }

    /**
     * Invoked when user pressed the load button.
     * Returns wherther to close Save/Load dialog.
     *
     * @param text current content of the dialog.
     * @return whether to close save/load dialog.
     *
     * @throws Exception if content is incorrect. Message from the
     *      exception will be shown on the comment pane.
     */
    protected abstract boolean load(String text) throws Exception;

    /**
     * Shows this dialog with given initial content.
     *
     * @param initialContent initital content.
     */
    public void show(String initialContent) {
        setInitialContent(initialContent);
        resetContent();
        show();
    }

    /**
     * Handles window events.
     *
     * @param e event ot handle.
     */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
        }
    }

    /**
     * Sets given comment to the dialog. It should be used to inform user
     * about wrong input data.
     *
     * @param comment the comment to set to the comment pane
     */
    public void setComment(String comment) {
        commentPane.setComment(comment);
    }

    /**
     * Sets content of the dialog.
     *
     * @param content content to set.
     */
    public void setContent(String content) {
        textArea.setText(content);
    }

    /**
     * Gets current content of the dialog.
     *
     * @return current content of the dialog.
     */
    public final String getContent() {
        return textArea.getText();
    }

    /**
     * Sets initial content of the dialog.
     *
     * @param content content to set.
     */
    public final void setInitialContent(String content) {
        initialContent = content;
    }

    /**
     * Gets initial content of the dialog.
     *
     * @return initial content of the dialog.
     */
    public final String getInitialContent() {
        return initialContent;
    }

    /**
     * Resets dialog content to initial content.
     */
    public final void resetContent() {
        setContent(initialContent);
    }
}
