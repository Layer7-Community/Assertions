package com.l7tech.custom.sample;

import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.ServiceInvocation;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import java.util.logging.Logger;

/**
 * This class processes the assertion at runtime when the checkRequest() method is called.
 */
 
public class SampleServiceInvocation extends ServiceInvocation {

	// This logger allows the assertion to generate log events that can be
	// captured by the gateway's log sinks.
    private static final Logger logger = Logger.getLogger(SampleServiceInvocation.class.getName());
    
    // This member references the assertion properties at runtime.
    // It is set in the setCustomAssertion() method below.
    private SampleAssertion assertion;
    
    // This method is called at runtime to pass the assertion properties.
    public void setCustomAssertion(CustomAssertion customAssertion) {
        super.setCustomAssertion(customAssertion);
        assert (customAssertion instanceof SampleAssertion);
        assertion = (SampleAssertion) customAssertion;
    }
	    
    // Override and implement this method for SSG server-side processing of the input data. Use 
    // the MessageTargetable interface to configure assertion to target request, response or other 
    // message-typed variable. This method replaces onRequest(...) and onResponse(...), which are 
    // obsolete and have been deprecated. For backwards compatibility, the default request and 
    // response are now located into the context map. You can access them using the following 
    // keys; (request and response)
    @Override
    public CustomAssertionStatus checkRequest(final CustomPolicyContext customPolicyContext) {

    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Entering checkRequest");

    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Value of inputStringOne: " + assertion.getInputStringOne());
    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Value of inputStringTwo: " + assertion.getInputStringTwo());
		
		String inputStringOne = customPolicyContext.expandVariable(assertion.getInputStringOne());
		String inputStringTwo = customPolicyContext.expandVariable(assertion.getInputStringTwo());
	
    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Value of expanded inputStringOne: " + inputStringOne);
    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Value of expanded inputStringTwo: " + inputStringTwo);
	
		String resultString = inputStringOne.toUpperCase() + inputStringTwo.toUpperCase();
		
    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Value of result string: " + resultString);
		
		customPolicyContext.setVariable(assertion.OUTPUT_STRING_VARIABLE_NAME, resultString);
		
    	logger.fine("CUSTOM ASSERTION [SampleAssertion]: Exiting checkRequest");

		return CustomAssertionStatus.NONE;
    }
    
}