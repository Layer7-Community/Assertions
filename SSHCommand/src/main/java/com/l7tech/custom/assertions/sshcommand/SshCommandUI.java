package com.l7tech.custom.assertions.sshcommand;

import com.l7tech.custom.assertions.sshcommand.console.SshCommandAssertionPropertiesPanel;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionUI;
import com.l7tech.policy.assertion.ext.cei.UsesConsoleContext;

import javax.swing.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nilic
 * Date: 3/7/14
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SshCommandUI implements CustomAssertionUI, UsesConsoleContext, Serializable {

    private Map consoleContext;

    public AssertionEditor getEditor(CustomAssertion customAssertion) {
        if (!(customAssertion instanceof SshCommandAssertion)) {
            throw new IllegalArgumentException(SshCommandAssertion.class +" type is required");
        }
        return new SshCommandAssertionPropertiesPanel((SshCommandAssertion) customAssertion, consoleContext);
    }

    public ImageIcon getSmallIcon() {
        return new ImageIcon(getClass().getClassLoader().getResource("Properties16.gif"));
    }

    public ImageIcon getLargeIcon() {
        return new ImageIcon(getClass().getClassLoader().getResource("Properties16.gif"));
    }

    @Override
    public void setConsoleContextUsed(Map map) {
        consoleContext = map;
    }

}
