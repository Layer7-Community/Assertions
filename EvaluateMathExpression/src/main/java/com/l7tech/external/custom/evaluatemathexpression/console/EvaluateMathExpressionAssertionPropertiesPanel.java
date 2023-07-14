/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.l7tech.external.custom.evaluatemathexpression.console;

import com.l7tech.external.custom.evaluatemathexpression.EvaluateMathExpressionAssertion;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.AssertionEditorSupport;
import com.l7tech.policy.assertion.ext.EditListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 *
 */
public class EvaluateMathExpressionAssertionPropertiesPanel extends JDialog implements AssertionEditor {
    private JTextField expressionField;
    private JTextField outputVariableField;
    private JSpinner precisionField;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel mainPanel;

    private final EvaluateMathExpressionAssertion assertion;
    private final transient AssertionEditorSupport editorSupport;

    public EvaluateMathExpressionAssertionPropertiesPanel(EvaluateMathExpressionAssertion assertion) {
        super(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, true);
        this.setTitle("Evaluate Math Assertion Properties");
        this.assertion = assertion;
        this.editorSupport = new AssertionEditorSupport(this);
        addComponents();
        this.init();
        enableDisableOkButton();
        modelToView(this.assertion);
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void edit() {
        this.setVisible(true);
    }

    @Override
    public void addEditListener(EditListener editListener) {
        this.editorSupport.addListener(editListener);
    }

    @Override
    public void removeEditListener(EditListener editListener) {
        this.editorSupport.removeListener(editListener);
    }

    private void viewToModel(EvaluateMathExpressionAssertion assertion) {
        assertion.setExpression(expressionField.getText().trim());
        assertion.setOutputVariable(outputVariableField.getText().trim());
        assertion.setPrecision((Integer) precisionField.getValue());
    }

    private void modelToView(EvaluateMathExpressionAssertion assertion) {
        expressionField.setText(assertion.getExpression() == null ? "" : assertion.getExpression());
        outputVariableField.setText(assertion.getOutputVariable() == null ? "" : assertion.getOutputVariable());
        precisionField.setValue(assertion.getPrecision());
    }

    private void init() {

        expressionField.getDocument().addDocumentListener(getDocumentListener());
        outputVariableField.getDocument().addDocumentListener(getDocumentListener());
        precisionField.setModel(new SpinnerNumberModel(0, 0, 16, 1));

        okButton.addActionListener(e -> {
            viewToModel(assertion);
            editorSupport.fireEditAccepted(assertion);
            dispose();
        });
        cancelButton.addActionListener(e -> {
            editorSupport.fireCancelled(assertion);
            dispose();
        });
    }

    private void enableDisableOkButton() {
        boolean expressionEnabled = !expressionField.getText().trim().isEmpty();
        boolean outputVariableEnabled = !outputVariableField.getText().trim().isEmpty();
        okButton.setEnabled(expressionEnabled && outputVariableEnabled);

    }

    private DocumentListener getDocumentListener() {
        return new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                enableDisableOkButton();
            }

            public void insertUpdate(DocumentEvent e) {
                enableDisableOkButton();
            }

            public void changedUpdate(DocumentEvent e) {
                //Plain text components do not fire these events
            }
        };
    }

    private void addComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        addLabels();
        addFields();
        addButtons();
    }

    private void addButtons() {
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        addToMainPanelWithGridBagConstraints(buttonsPanel, 1, 3, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

        okButton = new JButton();
        okButton.setText("OK");
        GridBagConstraints gbc = getGridBagConstraints(0, 0, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        buttonsPanel.add(okButton, gbc);

        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        gbc = getGridBagConstraints(1, 0, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        buttonsPanel.add(cancelButton, gbc);
    }

    private void addFields() {
        expressionField = new JTextField();
        addToMainPanelWithGridBagConstraints(expressionField, 1, 0, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

        outputVariableField = new JTextField();
        addToMainPanelWithGridBagConstraints(outputVariableField, 1, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

        final JPanel precisionPanel = new JPanel();
        precisionPanel.setLayout(new GridBagLayout());
        addToMainPanelWithGridBagConstraints(precisionPanel, 1, 2, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

        precisionField = new JSpinner();
        GridBagConstraints gbc = getGridBagConstraints(0, 0, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE);
        precisionPanel.add(precisionField, gbc);
    }

    private void addLabels() {
        final JLabel expressionLabel = new JLabel();
        expressionLabel.setText("Expression");
        addToMainPanelWithGridBagConstraints(expressionLabel, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);

        final JLabel outputVariableLabel = new JLabel();
        outputVariableLabel.setText("Output Variable");
        addToMainPanelWithGridBagConstraints(outputVariableLabel, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);

        final JLabel precisionLabel = new JLabel();
        precisionLabel.setText("Precision");
        addToMainPanelWithGridBagConstraints(precisionLabel, 0, 2, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }

    private void addToMainPanelWithGridBagConstraints(JComponent jComponent, int gridx, int gridy, double weightx, double weighty, int anchor, int fill) {
        GridBagConstraints gbc = getGridBagConstraints(gridx, gridy, weightx, weighty, anchor, fill);
        mainPanel.add(jComponent, gbc);
    }

    private GridBagConstraints getGridBagConstraints(int gridx, int gridy, double weightx, double weighty, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
        gbc.fill = fill;
        return gbc;
    }
}
