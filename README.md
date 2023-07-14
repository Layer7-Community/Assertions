# Assertions
This repository is for sharing unsupported examples of custom assertions created using the [Layer7 Custom Assertion SDK](https://techdocs.broadcom.com/us/en/ca-enterprise-software/layer7-api-management/api-gateway/11-0/policy-assertions/custom-assertions.html).

We recommend that users update dependent libraries to address vulnerabilities, and otherwise perform their own security assessment of these assertions before using them in a production environment.

This repository provides the source code for these assertions. Users must be prepared to build these assertions from the source code themselves. Different assertions may provide for different methods (e.g. ant, gradle, etc.) or no method of building with the examples, but users should be able to adapt the source could to build methods and environments that they prefer.

## Here is a list of available assertions

|Name|Brief Description|
|-----|-----------------|
|[**Sample**](./Sample)|This is a sample of how each assertion should be structured in the repository and should be used as a guideline for all contributions|
|[**SampleProject**](./SampleProject)|This is an example of a custom assertion project created using the default settings of the [custom assertion plugin for Eclipse](https://github.com/Layer7-Community/Utilities/tree/main/custom-assertion-plugin).|
|-----|-----------------|
|[**Delay**](./Delay/)|This assertion adds a delay of X milliseconds to a policy.|
|[**EvaluateMathExpression**](./EvaluateMathExpression/)|This assertion can be used to evaluate math expressions in policy.|
|[**InjectionFilter**](./InjectionFilter/)|This assertion provides a more configurable approach to protecting against various injection attacks using a managed list of regular expression filters.|
|[**SSHCommand**](./SSHCommand/)|This assertion allows users to execute commands on a remote server via a SSH session.|

## Feedback
We welcome your feedback on these assertions, and especially if they helped you in your daily work life! We 
are also available via the [Layer7 Communities](https://community.broadcom.com/enterprisesoftware/communities/communityhomeblogs?CommunityKey=0f580f5f-30a4-41de-a75c-e5f433325a18).

## IMPORTANT
If any assertion has an issue, please do not contact Broadcom support. These assertions are provided as-is. Please communicate via comments, pull requests and emails to the author of the assertion if you have any issues or questions.

## Contribution Guidelines
To contribute assertions, create a pull request with your updates. All pull requests require at least one reviewer to approve before the contribution will be merged to the main branch. Please ensure that all contributions follow the structure of the "Sample" folder.
Each new assertion should:
- Be located in it's own folder
- Include a description in the README.md file in the folder with a description of the assertion along with instructions on how to use the assertion including any prerequisites
- Update the README.md on the main folder to add a name and brief description of the assertion

**Enjoy!**
