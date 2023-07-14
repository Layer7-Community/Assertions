package com.l7tech.custom.assertions.sshcommand.server;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.ethz.ssh2.Session;
import com.l7tech.custom.assertions.sshcommand.SshCommandAssertion;

import com.l7tech.gateway.common.audit.LoggingAudit;
import com.l7tech.gateway.common.audit.Messages;
import com.l7tech.policy.assertion.ext.*;
import com.l7tech.policy.assertion.ext.message.CustomContentType;
import com.l7tech.policy.assertion.ext.message.CustomMessage;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import com.l7tech.policy.assertion.ext.message.format.CustomMessageFormat;
import com.l7tech.policy.assertion.ext.password.SecurePasswordServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nilic
 * Date: 3/7/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SshCommandServiceInvocation extends ServiceInvocation {

    private static final Logger logger = Logger.getLogger(SshCommandServiceInvocation.class.getName());

    private SshCommandAssertion assertion;

    @Override
    public void setCustomAssertion(CustomAssertion customAssertion) {
        super.setCustomAssertion(customAssertion);
        assert(customAssertion instanceof SshCommandAssertion);
        assertion = (SshCommandAssertion)customAssertion;
    }

    public CustomAssertionStatus checkRequest(final CustomPolicyContext context) {
        final LoggingAudit audit = new LoggingAudit(logger);
        ServiceFinder serviceFinder = (ServiceFinder) context.getContext().get("serviceFinder");
        SecurePasswordServices securePasswordServices = serviceFinder.lookupService(SecurePasswordServices.class);

        final Map<String, Object> vars = Collections.unmodifiableMap(context.getVariableMap(assertion.getVariablesUsed()));

        String username = context.expandVariable(assertion.getUsername(), vars);
        String host = context.expandVariable(assertion.getHost(), vars);
        int port = Integer.parseInt(context.expandVariable(assertion.getPort(), vars));
        String password = null;
        if(assertion.getPasswordId() != null) {
            try {
                password = securePasswordServices.decryptPassword(assertion.getPasswordId());
            } catch (ServiceException se) {
                return CustomAssertionStatus.FAILED;
            }
        }

        Connection conn = null;
        Session session = null;
        try {
            conn = new Connection(host, port);
            conn.connect(new ServerHostKeyVerifier() {
                public boolean verifyServerHostKey(String hostname, int port, String algorithm, byte[] key) {
                    return true;
                }
            }, 10000, 10000);

            boolean authenticated = false;
            if(assertion.isUsePrivateKey()) {
                String privateKeyText = context.expandVariable(assertion.getPrivateKey(), vars);
                if(password != null) {
                    authenticated = conn.authenticateWithPublicKey(username, privateKeyText.toCharArray(), password);
                } else {
                    authenticated = conn.authenticateWithPublicKey(username, privateKeyText.toCharArray(), null);
                }
            } else  {
                authenticated = conn.authenticateWithPassword(username, password);
            }

            if(!authenticated) {
                conn.close();
                audit.logAndAudit(Messages.EXCEPTION_WARNING_WITH_MORE_INFO, "Failed to authenticate with the remote server.");
                return CustomAssertionStatus.FAILED;
            }

            session = conn.openSession();

            StringBuilder command = new StringBuilder();
            command.append(context.expandVariable(assertion.getCommand(), vars));
            for(String argument : assertion.getArguments()) {
                command.append(" ");
                argument = argument.replace("'", "\\'");
                command.append("'");
                command.append(context.expandVariable(argument, vars));
                command.append("'");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayOutputStream baosErr = new ByteArrayOutputStream();

            session.execCommand(command.toString());

            long maxWait = 0;
            try {
                String value = context.expandVariable("${gateway." + SshCommandAssertion.CPROP_MAXIMUM_COMMAND_WAIT_TIME + "}");
                if(value == null) {
                    maxWait = 300L * 100;
                } else {
                    maxWait = Long.parseLong(value) * 100;
                }
            } catch(NumberFormatException nfe) {
                maxWait = 300L * 100;
            }

            boolean timedOut = true;
            int status = 1;
            long startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - startTime < maxWait) {
                int resultMask = session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EXIT_STATUS |
                        ChannelCondition.STDERR_DATA | ChannelCondition.STDOUT_DATA |
                        ChannelCondition.EOF,
                        maxWait - (System.currentTimeMillis() - startTime));

                if((resultMask & ChannelCondition.STDOUT_DATA) > 0) {
                    byte[] bytes = new byte[4096];
                    while(true) {
                        int bytesRead = session.getStdout().read(bytes);
                        if(bytesRead == -1) {
                            break;
                        }
                        baos.write(bytes, 0, bytesRead);
                    }
                }
                if((resultMask & ChannelCondition.STDERR_DATA) > 0) {
                    byte[] bytes = new byte[4096];
                    while(true) {
                        int bytesRead = session.getStderr().read(bytes);
                        if(bytesRead == -1) {
                            break;
                        }
                        baosErr.write(bytes, 0, bytesRead);
                    }
                }

                if((resultMask & ChannelCondition.EXIT_STATUS) > 0) {
                    timedOut = false;
                    status = session.getExitStatus();

                    byte[] bytes = new byte[4096];
                    while(true) {
                        int bytesRead = session.getStdout().read(bytes);
                        if(bytesRead == -1) {
                            break;
                        }
                        baos.write(bytes, 0, bytesRead);
                    }

                    while(true) {
                        int bytesRead = session.getStderr().read(bytes);
                        if(bytesRead == -1) {
                            break;
                        }
                        baosErr.write(bytes, 0, bytesRead);
                    }
                    break;
                } else if((resultMask & (ChannelCondition.CLOSED | ChannelCondition.TIMEOUT)) > 0) {
                    break;
                }
            }

            if(timedOut) {
                audit.logAndAudit(Messages.EXCEPTION_WARNING_WITH_MORE_INFO, "The remote command took too long to complete (max wait = " + maxWait + ")");
                return CustomAssertionStatus.FAILED;
            }

            String errorMessage = new String(baosErr.toByteArray());
            context.setVariable("ssh.exitStatus", Integer.toString(status));
            context.setVariable("ssh.errorMessage", errorMessage);
            if(status != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Exit Status: ");
                sb.append(Integer.toString(status));
                sb.append("\nError Output: ");
                sb.append(errorMessage);
                audit.logAndAudit(Messages.EXCEPTION_WARNING_WITH_MORE_INFO, sb.toString());

                if(assertion.isFailIfExitStatusNot0()) {
                    return CustomAssertionStatus.FAILED;
                }
            }

            CustomMessageFormat<InputStream> iStreamFormat;
            iStreamFormat = context.getFormats().getStreamFormat();
            CustomContentType contentType = null;
            contentType = context.createContentType("text/plain");

            if(assertion.getTargetName().toLowerCase().equals("request")) {
                CustomMessage customMessage = context.getMessage("request");
                iStreamFormat.overwrite(customMessage, new ByteArrayInputStream(baos.toByteArray()));
                customMessage.setContentType(contentType);
                customMessage.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
            } else if(assertion.getTargetName().toLowerCase().equals("response")) {
                CustomMessage customMessage = context.getMessage("response");
                iStreamFormat.overwrite(customMessage, new ByteArrayInputStream(baos.toByteArray()));
                customMessage.setContentType(contentType);
                customMessage.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
            } else {
                CustomMessage customMessage = context.getMessage(assertion.getTargetMessageVariable());
                iStreamFormat.overwrite(customMessage, new ByteArrayInputStream(baos.toByteArray()));
                customMessage.setContentType(contentType);
                customMessage.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
            }

            return CustomAssertionStatus.NONE;
        } catch(IOException ioe) {
            audit.logAndAudit(Messages.EXCEPTION_WARNING_WITH_MORE_INFO, ioe.toString());
            return CustomAssertionStatus.FAILED;
        } catch(Exception e) {
            audit.logAndAudit(Messages.EXCEPTION_WARNING_WITH_MORE_INFO, e.toString());
            return CustomAssertionStatus.FAILED;
        } finally {
            if(session != null) {
                session.close();
            }

            if(conn != null) {
                conn.close();
            }
        }
    }
}
