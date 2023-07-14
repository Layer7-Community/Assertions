/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.l7tech.custom.delay.console;

import com.l7tech.custom.delay.DelayAssertion;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.AssertionEditorSupport;
import com.l7tech.policy.assertion.ext.EditListener;
import com.l7tech.policy.variable.ContextVariablesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DelayAssertionPropertiesPanel extends JDialog implements AssertionEditor {
    private JPanel mainPanel;
    private JTextField delayTextField;
    private JButton okButton;
    private JButton cancelButton;

    private DelayAssertion delayAssertion;
    private AssertionEditorSupport editorSupport;

    public DelayAssertionPropertiesPanel(DelayAssertion delayAssertion) {
        super(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, true);

        // create and configure components
        setTitle("Delay Assertion Properties");
        this.delayAssertion = delayAssertion;
        editorSupport = new AssertionEditorSupport(this);
        mainPanel = new JPanel(new BorderLayout());
        delayTextField = new JTextField();
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");

        // add listeners
        addListeners();

        // Layout Components
        layoutComponents();

        // populate data
        modelToView(this.delayAssertion);
    }

    private void addListeners() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                onCancel();
            }
        });
        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());
        registerESCAndEnterKeys();
    }

    private void layoutComponents() {
        // Label and delay text field
        final JPanel delayFieldPanel = new JPanel(new BorderLayout());
        mainPanel.add(delayFieldPanel, BorderLayout.NORTH);
        delayFieldPanel.add(new JLabel("Delay (in Milliseconds)"), BorderLayout.WEST);
        final Dimension preferredSize = delayTextField.getPreferredSize();
        preferredSize.width = 200;
        delayTextField.setPreferredSize(preferredSize);
        delayFieldPanel.add(delayTextField, BorderLayout.CENTER);

        // OK and Cancel buttons
        final JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        mainPanel.add(okCancelPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        pack();
    }

    public void edit() {
        this.setVisible(true);
    }

    public void addEditListener(EditListener listener) {
        this.editorSupport.addListener(listener);
    }

    public void removeEditListener(EditListener listener) {
        this.editorSupport.removeListener(listener);
    }

    // listen for ESC key and click cancelButton.
    private void registerESCAndEnterKeys() {
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

        actionMap.put("ESCAPE", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        actionMap.put("ENTER", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
    }

    private String getText() {
        return delayTextField.getText().trim();
    }

    private void viewToModel(DelayAssertion model) {
        model.setDelayMilliSec(getText());
    }

    private void modelToView(DelayAssertion model) {
        delayTextField.setText(model.getDelayMilliSec());
    }

    private void onOk() {
        String delayStr = getText();

        if (ContextVariablesUtils.getReferencedNames(delayStr).length > 0 || DelayAssertion.getValidDelayOrNone(delayStr).isPresent()) {
            viewToModel(delayAssertion);
            editorSupport.fireEditAccepted(delayAssertion);
            dispose();
        } else {
            JOptionPane.showMessageDialog(DelayAssertionPropertiesPanel.this, "Input Value must be a non-negative integer or a context variable.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        editorSupport.fireCancelled(delayAssertion);
        dispose();
    }

    public static void main(String[] args) {
        new DelayAssertionPropertiesPanel(new DelayAssertion()).setVisible(true);
    }
}

