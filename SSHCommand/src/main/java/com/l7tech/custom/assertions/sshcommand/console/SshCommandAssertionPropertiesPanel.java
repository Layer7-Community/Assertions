package com.l7tech.custom.assertions.sshcommand.console;

//import com.l7tech.console.SsmApplication;
import com.l7tech.custom.assertions.sshcommand.SshCommandAssertion;
import com.l7tech.gui.util.DialogDisplayer;
import com.l7tech.gui.util.FileChooserUtil;
import com.l7tech.gui.util.Utilities;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.AssertionEditorSupport;
import com.l7tech.policy.assertion.ext.EditListener;
import com.l7tech.policy.assertion.ext.commonui.CommonUIServices;
import com.l7tech.policy.assertion.ext.commonui.CustomSecurePasswordPanel;
import com.l7tech.util.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nilic
 * Date: 3/7/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SshCommandAssertionPropertiesPanel extends JDialog implements AssertionEditor {
    private JTabbedPane tabbedPane1;
    private JRadioButton usernamePasswordRadioButton;
    private JRadioButton privateKeyRadioButton;
    private JPanel usernamePanel;
    private JTextField usernameField;
    private JPanel privateKeyPanel;
    private JScrollPane privateKeyScrollPane;
    private JTextArea privateKeyField;
    private JCheckBox privateKeyRequiresPasswordCheckbox;
    private JButton loadPrivateKeyFromFileButton;
    private JTextField hostField;
    private JTextField portField;
    private JTextField commandField;
    private JList argumentsList;
    private DefaultListModel argumentsListModel;
    private JButton addArgumentButton;
    private JButton editArgumentButton;
    private JButton removeArgumentButton;
    private JCheckBox exitStatusFailCheckBox;
    private JComboBox outputComboBox;
    private JLabel variableNameLabel;
    private JTextField variableNameField;
    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton okButton;
    private JPanel passwordPanelHolder;

    private SshCommandAssertion assertion;
    private AssertionEditorSupport editorSupport;
    private Map consoleContext;

    private CustomSecurePasswordPanel customSecurePasswordPanel;

    public SshCommandAssertionPropertiesPanel(SshCommandAssertion assertion, Map consoleContext) {
        super(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, true);
        this.setTitle("SSH Command Properties");
        this.assertion = assertion;
        this.consoleContext = consoleContext;
        this.editorSupport = new AssertionEditorSupport(this);

        initComponents();
        setData(assertion);

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    protected void initComponents() {

        customSecurePasswordPanel = this.getCommonUIServices().createPasswordComboBoxPanel(this);
        customSecurePasswordPanel.addListener(new CustomSecurePasswordPanel.ManagePasswordsDialogClosedListener() {
            @Override
            public void onClosed() {
                // Reload the security token combo box.
                //
                customSecurePasswordPanel.reloadComboBox();
            }
        });
        passwordPanelHolder.setLayout(new BorderLayout());
        passwordPanelHolder.add(customSecurePasswordPanel.getPanel(), BorderLayout.CENTER);

        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                if (!validateData())
                    return;
                getData(assertion);
                editorSupport.fireEditAccepted(assertion);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                editorSupport.fireCancelled(assertion);
               dispose();
            }
        });
        usernamePasswordRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                enableDisableFields();
            }
        });
        privateKeyRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                enableDisableFields();
            }
        });
        privateKeyRequiresPasswordCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                enableDisableFields();
            }
        });

        loadPrivateKeyFromFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                readFromFile();
            }
        });

        argumentsListModel = new DefaultListModel();
        argumentsList.setModel(argumentsListModel);
        argumentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addArgumentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CommandArgumentDialog dialog = new CommandArgumentDialog(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, "Add Argument", "");
                Utilities.centerOnScreen(dialog);
                dialog.setVisible(true);

                if(dialog.isConfirmed()) {
                    argumentsListModel.addElement(dialog.getValue());
                }
            }
        });

        editArgumentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(argumentsList.getSelectedIndex() == -1) {
                    return;
                }

                CommandArgumentDialog dialog = new CommandArgumentDialog(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, "Edit Argument", (String)argumentsList.getSelectedValue());
                Utilities.centerOnScreen(dialog);
                dialog.setVisible(true);

                if(dialog.isConfirmed()) {
                    argumentsListModel.setElementAt(dialog.getValue(), argumentsList.getSelectedIndex());
                }
            }
        });

        removeArgumentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(argumentsList.getSelectedIndex() == -1) {
                    return;
                }

                argumentsListModel.removeElementAt(argumentsList.getSelectedIndex());
            }
        });

        outputComboBox.setModel(new DefaultComboBoxModel(new String[] {"Default Request", "Default Response", "Other Context Variable"}));
        outputComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                variableNameLabel.setEnabled(outputComboBox.getSelectedIndex() == 2);
                variableNameField.setEnabled(outputComboBox.getSelectedIndex() == 2);
            }
        });
    }

    private void readFromFile() {
//        SsmApplication.doWithJFileChooser(new FileChooserUtil.FileChooserUser() {
//            @Override
//            public void useFileChooser(JFileChooser fc) {
//                doRead(fc);
//            }
//        });
    }

    private void doRead(JFileChooser dlg) {
        if (JFileChooser.APPROVE_OPTION != dlg.showOpenDialog(this)) {
            return;
        }

        String filename = dlg.getSelectedFile().getAbsolutePath();
        try {
            privateKeyField.setText(new String(IOUtils.slurpFile(new File(filename))));
        } catch(IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enableDisableFields() {
        privateKeyScrollPane.setEnabled(privateKeyRadioButton.isSelected());
        privateKeyField.setEnabled(privateKeyRadioButton.isSelected());
        privateKeyRequiresPasswordCheckbox.setEnabled(privateKeyRadioButton.isSelected());
        loadPrivateKeyFromFileButton.setEnabled(privateKeyRadioButton.isSelected());
        customSecurePasswordPanel.setEnabled(usernamePasswordRadioButton.isSelected() || privateKeyRequiresPasswordCheckbox.isSelected());
    }

    public void setData(SshCommandAssertion assertion) {
        if(assertion.isUsePrivateKey()) {
            privateKeyRadioButton.setSelected(true);
            privateKeyField.setText(assertion.getPrivateKey() == null ? "" : assertion.getPrivateKey());
        } else {
            usernamePasswordRadioButton.setSelected(true);
        }
        usernameField.setText(assertion.getUsername() == null ? "" : assertion.getUsername());

        enableDisableFields();

        if(assertion.getHost() != null) {
            hostField.setText(assertion.getHost().trim());
        }
        if(assertion.getPort() != null) {
            portField.setText(assertion.getPort().trim());
        }

        if(assertion.getPasswordId() != null) {
            customSecurePasswordPanel.getPasswordComboBox().setSelectedItem(assertion.getPasswordId());
        }

        commandField.setText(assertion.getCommand() == null ? "" : assertion.getCommand());
        argumentsListModel.removeAllElements();
        String[] arguments = assertion.getArguments();
        if(arguments != null) {
            for(String argument : arguments) {
                argumentsListModel.addElement(argument);
            }
        }

        exitStatusFailCheckBox.setSelected(assertion.isFailIfExitStatusNot0());

        if(assertion.getTargetName().toLowerCase().equals("request")) {
            outputComboBox.setSelectedIndex(0);
        } else if(assertion.getTargetName().toLowerCase().equals("response")) {
            outputComboBox.setSelectedIndex(1);
        } else {
            outputComboBox.setSelectedIndex(2);
            variableNameField.setText(assertion.getTargetMessageVariable());
        }
    }

    private boolean validateData() {
        if(usernameField.getText().trim().length() == 0) {
            displayErrorMessage("Username field is empty.");
            return false;
        }
        if(hostField.getText().trim().length() == 0) {
            displayErrorMessage("Host field is empty.");
            return false;
        }
        if(portField.getText().trim().length() == 0) {
            displayErrorMessage("Port field is empty.");
            return false;
        }
        if(commandField.getText().trim().length() == 0) {
            displayErrorMessage("Command field is empty.");
            return false;
        }
        if(outputComboBox.getSelectedIndex() == 2 && variableNameField.getText().trim().length() == 0) {
            displayErrorMessage("The output variable name field is empty.");
            return false;
        }
        return true;
    }

    public SshCommandAssertion getData(SshCommandAssertion assertion) {
        if(usernamePasswordRadioButton.isSelected()) {
            assertion.setUsePrivateKey(false);
        } else {
            assertion.setUsePrivateKey(true);
            assertion.setPrivateKey(privateKeyField.getText());
        }
        assertion.setUsername(usernameField.getText().trim());

        assertion.setHost(hostField.getText().trim());
        assertion.setPort(portField.getText().trim());

        if(usernamePasswordRadioButton.isSelected() || privateKeyRequiresPasswordCheckbox.isSelected()) {
            assertion.setPasswordId(customSecurePasswordPanel.getSelectedItem());
        } else {
            assertion.setPasswordId(null);
        }

        assertion.setCommand(commandField.getText());
        String[] arguments = new String[argumentsListModel.size()];
        for(int i = 0;i < argumentsListModel.size();i++) {
            arguments[i] = (String)argumentsListModel.elementAt(i);
        }
        assertion.setArguments(arguments);

        assertion.setFailIfExitStatusNot0(exitStatusFailCheckBox.isSelected());

        if(outputComboBox.getSelectedIndex() == 0) {
            assertion.setTargetName("request");
        } else if(outputComboBox.getSelectedIndex() == 1) {
            assertion.setTargetName("response");
        } else {
            assertion.setTargetMessageVariable(variableNameField.getText().trim());
        }

        return assertion;
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

    private void displayErrorMessage(String message) {
        DialogDisplayer.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE, null);
    }

    private CommonUIServices getCommonUIServices() {
        return (CommonUIServices) consoleContext.get(CommonUIServices.CONSOLE_CONTEXT_KEY);
    }
}
