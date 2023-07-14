package com.l7tech.custom.assertions.sshcommand.console;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * Created with IntelliJ IDEA.
 * User: nilic
 * Date: 3/7/14
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandArgumentDialog extends JDialog{
    private JTextField valueField;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel mainPanel;
    private boolean confirmed = false;

    public CommandArgumentDialog(Frame owner, String title, String value) {
        super(owner, title, true);
        initComponents(value);
    }

    public CommandArgumentDialog(Dialog owner, String title, String value) {
        super(owner, title, true);
        initComponents(value);
    }

    private void initComponents(String value) {
        setContentPane(mainPanel);
        setModal(true);

        valueField.setText((value == null) ? "" : value);

        equalizeButtonSizes(okButton, cancelButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                confirmed = true;
                setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                confirmed = false;
                setVisible(false);
            }
        });

        getRootPane().setDefaultButton(okButton);
        pack();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getValue() {
        return valueField.getText();
    }

    public static void main(String[] args) {
        CommandArgumentDialog dialog = new CommandArgumentDialog((Frame)null, "Add Argument", "arg1");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * Sets all of the buttons to be the same size. This is done
     * dynamically by setting each button's preferred and maximum
     * sizes after the buttons are created.
     * <p/>
     * Limitations:
     * Button images are not considered into the calcualtion, nor
     * the alignment.
     * <p/>
     * The first button is used to determine the font size, that is
     * same font is assumed for all the buttons.
     *
     * @param buttons the array of buttons to eqalize the size for.
     */
    private static void equalizeButtonSizes(javax.swing.AbstractButton... buttons) {
        if (buttons == null || buttons.length == 0) {
            return;
        }
        // Get the largest width and height
        Dimension maxSize = new Dimension(0, 0);
        Rectangle2D textBounds;
        FontMetrics metrics =
                buttons[0].getFontMetrics(buttons[0].getFont());
        Graphics g = buttons[0].getGraphics();

        for (int i = 0; i < buttons.length; i++) {
            textBounds = metrics.getStringBounds(buttons[i].getText(), g);
            maxSize.width =
                    Math.max(maxSize.width, (int)textBounds.getWidth());
            maxSize.height =
                    Math.max(maxSize.height, (int)textBounds.getHeight());
        }

        if (buttons[0].getBorder() != null) {
            Insets insets =
                    buttons[0].getBorder().getBorderInsets(buttons[0]);
            maxSize.width += insets.left + insets.right;
            maxSize.height += insets.top + insets.bottom;
        }

        // reset preferred and maximum size since GridBaglayout takes both
        // into account
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPreferredSize((Dimension)maxSize.clone());
            buttons[i].setMaximumSize((Dimension)maxSize.clone());
        }
    }
}
