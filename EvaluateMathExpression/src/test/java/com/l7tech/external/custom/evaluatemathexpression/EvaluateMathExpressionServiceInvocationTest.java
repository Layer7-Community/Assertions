/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.l7tech.external.custom.evaluatemathexpression;

import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/**
 * Test the EvaluateMathExpressionAssertion
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class EvaluateMathExpressionServiceInvocationTest {

    private static final Logger log = Logger.getLogger(EvaluateMathExpressionServiceInvocationTest.class.getName());

    private EvaluateMathExpressionAssertion evaluateMathExpressionAssertion;
    private EvaluateMathExpressionServiceInvocation serverEvaluateMathExpressionAssertion;
    
    @Mock
    private CustomPolicyContext mockPolicyEnforcementContext ;

    @Test
    public void testSimple() {

        when(mockPolicyEnforcementContext.getVariable("aa")).thenReturn("50");
        when(mockPolicyEnforcementContext.getVariable("bb")).thenReturn("5");
        when(mockPolicyEnforcementContext.getVariable("cc")).thenReturn("8");
        when(mockPolicyEnforcementContext.getVariable("dd")).thenReturn("2");
        when(mockPolicyEnforcementContext.expandVariable(eq("(((${aa} / ${bb}) * 8) % 2 )"), anyMap())).thenReturn("(((50 / 5) * 8) % 2 )");
        when(mockPolicyEnforcementContext.getVariable("myoutputvariable")).thenReturn("0");

        evaluateMathExpressionAssertion = new EvaluateMathExpressionAssertion();
        evaluateMathExpressionAssertion.setExpression("(((${aa} / ${bb}) * 8) % 2 )");
        evaluateMathExpressionAssertion.setOutputVariable("myoutputvariable");

        serverEvaluateMathExpressionAssertion = new EvaluateMathExpressionServiceInvocation();
        serverEvaluateMathExpressionAssertion.setCustomAssertion(evaluateMathExpressionAssertion);

        serverEvaluateMathExpressionAssertion.checkRequest(mockPolicyEnforcementContext);
        Object value = mockPolicyEnforcementContext.getVariable("myoutputvariable");
        Assert.assertNotNull(value);

        log.info("This is the output of ((50 / 5) * 8) % 2 = " + value.toString());
        Assert.assertEquals("0", value.toString());

    }

    @Test
    public void testComplex() {
        when(mockPolicyEnforcementContext.getVariable("aa")).thenReturn("90");
        when(mockPolicyEnforcementContext.expandVariable(eq("sin( ${aa} )"), anyMap())).thenReturn("sin( 90 )");
        when(mockPolicyEnforcementContext.getVariable("myoutputvariable")).thenReturn("1");

        evaluateMathExpressionAssertion = new EvaluateMathExpressionAssertion();
        evaluateMathExpressionAssertion.setExpression("sin( ${aa} )");
        evaluateMathExpressionAssertion.setOutputVariable("myoutputvariable");

        serverEvaluateMathExpressionAssertion = new EvaluateMathExpressionServiceInvocation();
        serverEvaluateMathExpressionAssertion.setCustomAssertion(evaluateMathExpressionAssertion);
        serverEvaluateMathExpressionAssertion.checkRequest(mockPolicyEnforcementContext);

        Object value = mockPolicyEnforcementContext.getVariable("myoutputvariable");
        Assert.assertNotNull(value);
        log.info("This is the output of sin(90)=" + value.toString());
        Assert.assertEquals("1", value.toString());
    }

    @Test
    public void testBadExpression() {
        String badExpression = "${incompleteVariable + 1";
        when(mockPolicyEnforcementContext.expandVariable(eq(badExpression), anyMap())).thenReturn(badExpression);

        evaluateMathExpressionAssertion = new EvaluateMathExpressionAssertion();
        evaluateMathExpressionAssertion.setExpression(badExpression);
        evaluateMathExpressionAssertion.setOutputVariable("myoutputvariable");

        serverEvaluateMathExpressionAssertion = new EvaluateMathExpressionServiceInvocation();
        serverEvaluateMathExpressionAssertion.setCustomAssertion(evaluateMathExpressionAssertion);

        CustomAssertionStatus status = serverEvaluateMathExpressionAssertion.checkRequest(mockPolicyEnforcementContext);
        Assert.assertEquals(CustomAssertionStatus.FAILED, status);
    }

    @Test
    public void testDivideByZero() {
        String divideByZero = "1/${zero}";
        when(mockPolicyEnforcementContext.getVariable("zero")).thenReturn("0");
        when(mockPolicyEnforcementContext.expandVariable(eq(divideByZero), anyMap())).thenReturn("1/0");

        evaluateMathExpressionAssertion = new EvaluateMathExpressionAssertion();
        evaluateMathExpressionAssertion.setExpression(divideByZero);
        evaluateMathExpressionAssertion.setOutputVariable("myoutputvariable");

        serverEvaluateMathExpressionAssertion = new EvaluateMathExpressionServiceInvocation();
        serverEvaluateMathExpressionAssertion.setCustomAssertion(evaluateMathExpressionAssertion);

        CustomAssertionStatus status = serverEvaluateMathExpressionAssertion.checkRequest(mockPolicyEnforcementContext);
        Assert.assertEquals(CustomAssertionStatus.FAILED, status);

    }

}
