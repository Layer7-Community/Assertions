/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.l7tech.external.custom.evaluatemathexpression;

import com.l7tech.external.custom.evaluatemathexpression.console.EvaluateMathExpressionAssertionPropertiesPanel;
import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionUI;

import javax.swing.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 */
public class EvaluateMathExpressionUI implements CustomAssertionUI, Serializable {

    @Override
    public AssertionEditor getEditor(CustomAssertion customAssertion) {
        if(!(customAssertion instanceof EvaluateMathExpressionAssertion)) {
            throw new IllegalArgumentException(EvaluateMathExpressionAssertion.class +" type is required");
        }
        return new EvaluateMathExpressionAssertionPropertiesPanel((EvaluateMathExpressionAssertion) customAssertion);
    }

    @Override
    public ImageIcon getSmallIcon() {
        return new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Properties16.gif")));
    }

    @Override
    public ImageIcon getLargeIcon() {
        return new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Properties16.gif")));
    }
}
