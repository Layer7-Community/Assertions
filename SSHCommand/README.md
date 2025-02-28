# SSH Command

## Description
This assertion allows users to execute commands on a remote server via a SSH session.

**Note: Use this assertion with caution. This assertion connects APIs with system level access to a remote server. The gateway has no control or insight into any commands run and should be used with care. It's also especially important that users of this assertion maintain the dependent libraries to avoid vulnerabilities and weak encryption algorithms.**

## Prerequisites/Dependencies
1) IntelliJ IDE
2) [ganymed-ssh2-263.jar](https://github.com/SoftwareAG/ganymed-ssh-2/tree/ganymed-ssh2-263)
   * *Note: Older versions downloaded from Maven do not seem to be able to connect to OVA's.*


## Build Instructions
1) Grab the following Gateway dependencies from Policy Manager's lib folder:

   * layer7-api.jar
   * layer7-gateway-common.jar
   * layer7-gui.jar
   * layer7-policy.jar
   * layer7-utility.jar
   
   <br>
   If building the assertion on Window's machine, grab the dependencies from the Window's installed Policy Manager (e.g. C:\Program Files (x86)\CA Technologies - A Broadcom Company\Layer7 API Gateway Policy Manager 11.0.00.14811\lib).
   <br><br>
   If building the assertion on Linux, grab the dependencies from the Linux installed Policy Manager.
   <br><br>
2) Add the jars to the folder specified in build.gradle dependencies section (see TODO comment).
3) Run build using Gradle wrapper: `./gradlew build`
4) Go to `build/libs` folder where SshCommandAssertion.jar is built
5) Now follow the SSH Command Assertion's User guide to install.  Only need to copy over SshCommandAssertion.jar to Gateway's /opt/SecureSpan/Gateway/runtime/modules/lib.

## Additional Information
   * See user guide [here](SSH%20Command%20Assertion%20-%20User%20Guide.pdf).

## Known Limitations
* On the SSH Command properties dialog, the "Load From File" has been disabled.  It requires code that is not available.  You would have to create the method to browse for a file: https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
