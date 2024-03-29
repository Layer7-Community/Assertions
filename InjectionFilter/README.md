# About
This assertion provides a more configurable approach to protecting against various injection attacks using a managed list of regular expression filters.

# Build
Use JDK 11 and Gradle 6.7 to build this assertion. Gradle-wrapper is provided in this repository for convenience to avoid the need of installing gradle on your computer.

In order to build the assertion run `./gradlew build`.
 
This will compile, test, and create the jar file. It will be available in `build/libs`
 
# Adding Libraries
In order to build the custom assertions the Layer7 API Gateway API jar is required to be put into the lib directory.
 
# Run
Docker version of Gateway greatly helps us to deploy assertions / RESTMAN bundles quickly. Please follow the steps below to run the container Gateway prepopulated with this example assertion and example services.
1) Open Shell or Command Prompt and navigate to the directory where this repository is cloned.
2) Build the Assertion
   ```
     ./gradlew build
   ```
3) Ensure your docker environment is properly setup.
4) Provide the Layer7 API Gateway license at `docker/license.xml`.
5) Execute the below docker-compose command to run the Layer7 API Gateway container.
   ```
     docker-compose up
   ```
   * Provided `docker-compose.yml` ensures pulling the latest Layer7 API Gateway image from the Docker Hub public repository and deploys the assertion that was just built.
6) Wait until the Gateway container is started and is ready to receive messages.
 
# Usage
 In the Injection Filter assertion,  injection patterns can be specified and used in various scenarios to protect against code injection attacks. Four pre-defined Filter patterns have been created. 
 
 Once the Injection Filter Assertion is enabled, go to `Tasks -> Additional Actions -> Manage Injection Filter Patterns` to view the manage filters dialog. It will display all currently configured filters. If this is the first time entering this dialog or if there are no filters in the database, then the pre-defined filters will be loaded. From here, you can add, edit, and remove filters. When adding a filter, you enter a regular expression to match; you can enter test input values and see what would be matched. You can apply a filter to a policy by adding the "Apply Injection Filter" assertion to your policy.
