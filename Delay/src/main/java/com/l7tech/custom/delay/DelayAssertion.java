/*
 * Copyright (c) 2018 CA. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.l7tech.custom.delay;

import com.l7tech.policy.assertion.UsesVariables;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.variable.ContextVariablesUtils;

import java.util.Optional;

public class DelayAssertion implements CustomAssertion, UsesVariables {
    private String delayMilliSec = null;

    /**
     * Validates delay value.
     *
     * @param delayStr the delay string.
     * @return true if delay is valid. False otherwise.
     */
    public static Optional<Long> getValidDelayOrNone(String delayStr) {
        if (delayStr.isEmpty()) {
            return Optional.empty();
        }

        final long delay;
        try {
            delay = Long.parseLong(delayStr);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
        if (delay < 0) {
            return Optional.empty();
        }
        return Optional.of(delay);
    }

    @Override
    public String getName() {
        return "DelayAssertion";
    }

    @Override
    public String[] getVariablesUsed() {
        return ContextVariablesUtils.getReferencedNames(delayMilliSec);
    }

    public void setDelayMilliSec(String delayMilliSec) {
        this.delayMilliSec = delayMilliSec;
    }

    public String getDelayMilliSec() {
        return this.delayMilliSec;
    }
}