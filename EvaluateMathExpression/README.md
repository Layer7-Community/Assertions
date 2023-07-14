# About
This assertion can be used to evaluate math expressions in policy.

# Build
In order to build the assertion run `./gradlew clean build`
 
This will compile, test, and create the jar file. It will be available in `build/libs`
 
# Adding Libraries
In order to build the custom assertions the Layer7 API Gateway API jar is required to be put into the lib directory.
 
# Run
Docker version of Gateway greatly helps us to deploy assertions / RESTMAN bundles quickly. Please follow the steps below to run the container Gateway prepopulated with this example assertion and example services.
1) Open Shell or Command Prompt and navigate to the directory where this repository is cloned.
2) Build the Assertion
   ```
     gradle build
   ```
3) Ensure your docker environment is properly setup.
4) Provide the Layer7 API Gateway license at `docker/license.xml`.
5) Execute the below docker-compose command to run the Layer7 API Gateway container.
   ```
     docker-compose up
   ```
   * Provided `docker-compose.yml` ensures pulling the latest Layer7 API Gateway image from the Docker Hub public repository and deploys the assertion that was just build.
6) Wait until the Gateway container is started and is ready to receive messages.
 
# Usage
This assertion can evaluate a mathematical expression. The expression may contain context variables, parantheses, and common math functions. The result is stored in a context variable. The precision of the result is also adjustable. 
Available functions include:
* <b>ABS</b>: the absolute value of the parameter
* <b>ACOS</b>: arccos or cos<sup>-1</sup> of the parameter
* <b>ASIN</b>: arcsin or sin<sup>-1</sup> of the parameter
* <b>ATAN</b>: arctan or tan<sup>-1</sup> of the parameter
* <b>CBRT</b>: the cube root of the parameter
* <b>CEIL</b>: the ceiling of the parameter
* <b>COS</b>: cosine of the parameter
* <b>COSH</b>: hyperbolic cosine of the parameter
* <b>EXP</b>: raises the constant <i>e</i> to the power of the parameter
* <b>EXPM1</b>: raises the constant <i>e</i> to the power of the parameter and subtracts 1
* <b>FLOOR</b>: the floor of the paramter
* <b>LOG</b>: the natural logarithm of the parameter
* <b>SIN</b>: sine of the parameter
* <b>SINH</b>: hyperbolic sine of the parameter
* <b>SQRT</b>: square root of the parameter
* <b>TAN</b>: tangent of the parameter
* <b>TANH</b>: hyperbolic tangent of the parameter