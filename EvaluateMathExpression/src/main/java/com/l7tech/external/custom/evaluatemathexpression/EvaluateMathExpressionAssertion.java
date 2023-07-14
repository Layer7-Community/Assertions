/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.l7tech.external.custom.evaluatemathexpression;

import com.l7tech.policy.assertion.SetsVariables;
import com.l7tech.policy.assertion.UsesVariables;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.variable.ContextVariablesUtils;
import com.l7tech.policy.variable.VariableMetadata;

/**
 *
 */
public class EvaluateMathExpressionAssertion implements CustomAssertion, UsesVariables, SetsVariables {

    private static final String ASSERTION_NAME = "Evaluate Math Expression";
    private String expression = null;
    private String outputVariable = null;
    private int precision = 0;

    @Override
    public String getName() {
        return ASSERTION_NAME;
    }

    @Override
    public VariableMetadata[] getVariablesSet() {
        return new VariableMetadata[]{
                new VariableMetadata(outputVariable, false, false, null, true)
        };
    }

    @Override
    public String[] getVariablesUsed() {
        return ContextVariablesUtils.getReferencedNames(expression);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getOutputVariable() {
        return outputVariable;
    }

    public void setOutputVariable(String outputVariable) {
        this.outputVariable = outputVariable;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
