/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.l7tech.custom.delay.console;

import com.l7tech.custom.delay.DelayAssertion;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionUI;

import javax.swing.*;
import java.io.Serializable;

public class DelayUI implements CustomAssertionUI, Serializable {
    public AssertionEditor getEditor(CustomAssertion customAssertion) {
        if (!(customAssertion instanceof DelayAssertion)) {
            throw new IllegalArgumentException(DelayAssertion.class + " type is required");
        }
        return new DelayAssertionPropertiesPanel((DelayAssertion) customAssertion);
    }

    public ImageIcon getSmallIcon() {
        return new ImageIcon(getClass().getClassLoader().getResource("policy16.gif"));
    }

    public ImageIcon getLargeIcon() {
        return new ImageIcon(getClass().getClassLoader().getResource("policy32.gif"));
    }
}

