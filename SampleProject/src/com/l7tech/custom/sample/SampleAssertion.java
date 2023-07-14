package com.l7tech.custom.sample;

import com.l7tech.policy.assertion.SetsVariables;
import com.l7tech.policy.assertion.UsesVariables;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.variable.VariableMetadata;
import com.l7tech.policy.variable.ContextVariablesUtils;

/**
 * This class represents the properties of the assertion. This class
 * is loaded by Policy Manager at design time. If a custom UI is not
 * provided, then Policy Manager presents a generic dialog for setting
 * property values using the getter and setter methods of the private members.
 */
 
@SuppressWarnings("serial")
public class SampleAssertion implements CustomAssertion, UsesVariables, SetsVariables{
    
    // The name of a context variable set by this assertion. This member is
	// referenced by the getVariablesSet method of the SetsVariables interface
	// and by the doWork method of the ServiceInvocation class. Using a
	// constant like this prevents this member from being displayed as a 
	// property in the generic assertion dialog presented by Policy Manager.
	// If you want users to be able to set the name of the variable to be set
	// by the assertion, you could make this a private variable accessible
	// through getter and setter methods.
    public final String OUTPUT_STRING_VARIABLE_NAME = "outputString";
    	   
    // For the most part, private members that you define here will correspond
    // to properties of the assertion that you expose through getter and setter
    // methods below.
    
    // Sample assertion properties 
	private String inputStringOne = "Hello ";
	private String inputStringTwo = "World";
	
	/**
	 * The following are getter and setter methods for the private members of this assertion.
 	 */
 	 
	public void setInputStringOne(String inputStringOne) {
		this.inputStringOne = inputStringOne;
	}

	public String getInputStringOne() {
		return inputStringOne;
	}

	public void setInputStringTwo(String inputStringTwo) {
		this.inputStringTwo = inputStringTwo;
	}

	public String getInputStringTwo() {
		return inputStringTwo;
	}

	// Implemented method of the CustomAssertion interface.
	// Policy Manager calls this method to get the name of
	// the assertion as it should be displayed in the policy
	public String getName() {
		return "Apply sample string manipulation";
	}

	// Implemented method of the UsesVariables interface.
	// Policy Manager calls this method to determine what context
	// variables, if any, are referenced by the assertion. Policy
	// Manager uses this information during policy validation, and
	// warns users if this custom assertion is referencing context
	// variables that have not been previously declared in policy.	
	public String[] getVariablesUsed() {
        if (inputStringOne == null && inputStringTwo == null) return new String[]{};
        return ContextVariablesUtils.getReferencedNames(inputStringOne + inputStringTwo);
	}

	// Implemented method of the SetsVariables interface.
	// Policy Manager calls this method to determine what context
	// variables, if any, are set by the assertion. Policy Manager
	// will display these variables in a tool tip when users hover
	// their mouse of the assertion in the Policy Editor.	
	public VariableMetadata[] getVariablesSet() {
		return new VariableMetadata[]{new VariableMetadata(this.OUTPUT_STRING_VARIABLE_NAME)};
	}

}
