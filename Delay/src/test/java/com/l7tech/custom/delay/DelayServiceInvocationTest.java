/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.l7tech.custom.delay;

import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Test the DelayServiceInvocation class
 */
@RunWith(MockitoJUnitRunner.class)
public class DelayServiceInvocationTest {

    @Mock
    private CustomPolicyContext mockPolicyEnforcementContext;

    private DelayServiceInvocation delayServiceInvocation;

    private static final String EXPECTED_DELAY = "100";

    @Before
    public void setUp() {
        final DelayAssertion delayAssertion = new DelayAssertion();
        delayAssertion.setDelayMilliSec("1");
        delayServiceInvocation = new DelayServiceInvocation();
        delayServiceInvocation.setCustomAssertion(delayAssertion);
    }

    @Test
    public void testDelay() {
        when(mockPolicyEnforcementContext.expandVariable(anyString(), any())).thenReturn(EXPECTED_DELAY);
        long startTime = System.currentTimeMillis();
        assertEquals(CustomAssertionStatus.NONE, delayServiceInvocation.checkRequest(mockPolicyEnforcementContext));
        assertTrue((System.currentTimeMillis() - startTime) >= Integer.parseInt(EXPECTED_DELAY));
    }

    @Test
    public void testNegativeInputValue() {
        String negativeDelay = "-100";
        when(mockPolicyEnforcementContext.expandVariable(anyString(), any())).thenReturn(negativeDelay);
        assertEquals(CustomAssertionStatus.FALSIFIED, delayServiceInvocation.checkRequest(mockPolicyEnforcementContext));
    }
}
