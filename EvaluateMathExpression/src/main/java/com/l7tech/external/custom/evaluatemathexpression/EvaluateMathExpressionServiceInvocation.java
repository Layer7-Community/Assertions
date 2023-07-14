/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.l7tech.external.custom.evaluatemathexpression;

import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.ServiceInvocation;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Server side implementation of the EvaluateMathExpressionAssertion.
 *
 * @see EvaluateMathExpressionAssertion
 */
public class EvaluateMathExpressionServiceInvocation extends ServiceInvocation {

    @Override
    public CustomAssertionStatus checkRequest(CustomPolicyContext customPolicyContext) {
        if (!isAssertionValid(customAssertion)) {
            return CustomAssertionStatus.FAILED;
        }

        EvaluateMathExpressionAssertion assertion = (EvaluateMathExpressionAssertion) customAssertion;

        String[] variablesUsed = assertion.getVariablesUsed();
        Map<String, Object> vars = customPolicyContext.getVariableMap(variablesUsed);
        String expression = customPolicyContext.expandVariable(assertion.getExpression(), vars);

        double value;
        try {
            value = new ExpressionBuilder(expression).build().evaluate();
        } catch (Exception e) {
            auditWarn("There was a problem trying to evaluate the expression: " + expression +
                    ". This was caused because: " + e.getMessage());
            return CustomAssertionStatus.FAILED;
        }

        StringBuilder sb = new StringBuilder("#");
        if (assertion.getPrecision() > 0) {
            sb.append('.');
            for (int i = 0; i < assertion.getPrecision(); i++) {
                sb.append('#');
            }
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        customPolicyContext.setVariable(assertion.getOutputVariable(), df.format(value));
        return CustomAssertionStatus.NONE;
    }

    private boolean isAssertionValid(CustomAssertion assertion) {
        if (assertion instanceof EvaluateMathExpressionAssertion) {
            return true;
        } else {
            auditWarn(String.format("customAssertion must be of type [{%s}], but it is of type [{%s}] instead",
                    EvaluateMathExpressionAssertion.class.getSimpleName(),
                    customAssertion.getClass().getSimpleName()));
            return false;
        }
    }
}
