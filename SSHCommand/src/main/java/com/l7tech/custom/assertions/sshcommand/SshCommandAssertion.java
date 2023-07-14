package com.l7tech.custom.assertions.sshcommand;

import com.l7tech.objectmodel.EntityType;
import com.l7tech.objectmodel.Goid;
import com.l7tech.policy.assertion.*;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.targetable.CustomMessageTargetable;
import com.l7tech.policy.assertion.ext.targetable.CustomMessageTargetableSupport;
import com.l7tech.policy.variable.Syntax;
import com.l7tech.policy.variable.VariableMetadata;
import com.l7tech.util.GoidUpgradeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nilic
 * Date: 3/7/14
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class SshCommandAssertion implements CustomAssertion, CustomMessageTargetable, UsesVariables, SetsVariables {

    protected static final Logger logger = Logger.getLogger(SshCommandAssertion.class.getName());

    public static String CPROP_MAXIMUM_COMMAND_WAIT_TIME = "ssh.command.maxwait";
    private static final String PROPERTY_DIALOG_TITLE = "SSH Command Properties";

    private final List<VariableMetadata> variablesSet = new ArrayList<VariableMetadata>();
    private String host;
    private String port = "22";
    private boolean usePrivateKey = false;
    private String username;
    private String privateKey;
    private Goid passwordGoid = null;
    private String passwordId = null;
    private String command;
    private String[] arguments;
    private boolean failIfExitStatusNot0 = true;
    private CustomMessageTargetableSupport customMessageTargetableSupport = new CustomMessageTargetableSupport("response");

    public String getName() {
        return "SSH Command";
    }

    public String[] getVariablesUsed() {
        ArrayList<String[]> variablesList = new ArrayList<String[]>();

        variablesList.add(Syntax.getReferencedNames(username));
        variablesList.add(Syntax.getReferencedNames(host));
        variablesList.add(Syntax.getReferencedNames(port));
        if(privateKey != null) {
            variablesList.add(Syntax.getReferencedNames(privateKey));
        }
        variablesList.add(Syntax.getReferencedNames(command));

        if(arguments != null) {
            for(String argument : arguments) {
                variablesList.add(Syntax.getReferencedNames(argument));
            }
        }

        int numVariables = 0;
        for(String[] variables : variablesList) {
            numVariables += variables.length;
        }

        String[] retVal = new String[numVariables];
        int x = 0;
        for(String[] variables : variablesList) {
            System.arraycopy(variables, 0, retVal, x, variables.length);
            x += variables.length;
        }

        return retVal; //Syntax.getReferencedNames(...);
    }

    public VariableMetadata[] getVariablesSet() {
        for (VariableMetadata variable :  customMessageTargetableSupport.getVariablesSet())
            variablesSet.add(variable);

        variablesSet.add(new VariableMetadata("ssh.exitStatus", false, false, null, true));
        variablesSet.add(new VariableMetadata("ssh.errorMessage", false, false, null, true));
        return variablesSet.toArray(new VariableMetadata[variablesSet.size()]);
    }

    public boolean initializesRequest() {
        return customMessageTargetableSupport.getTargetName().equals("request");
    }

    public boolean needsInitializedRequest() {
        return false;
    }

    public boolean initializesResponse() {
        return customMessageTargetableSupport.getTargetName().equals("response");
    }

    public boolean needsInitializedResponse() {
        return false;
    }

    public String getPropertiesDialogTitle() {
        return PROPERTY_DIALOG_TITLE;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isUsePrivateKey() {
        return usePrivateKey;
    }

    public void setUsePrivateKey(boolean usePrivateKey) {
        this.usePrivateKey = usePrivateKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Goid getPasswordGoid() {
        return passwordGoid;
    }

    public String getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(String passwordId) {
        this.passwordId = passwordId;
    }

    public void setPasswordGoid(Goid passwordGoid) {
        this.passwordGoid = passwordGoid;
    }

    public void setPasswordOid(Long passwordOid) {
        if (passwordOid != null) {
            this.passwordGoid = GoidUpgradeMapper.mapOid(EntityType.SECURE_PASSWORD, passwordOid);
        } else {
            this.passwordGoid = null;
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean isFailIfExitStatusNot0() {
        return failIfExitStatusNot0;
    }

    public void setFailIfExitStatusNot0(boolean failIfExitStatusNot0) {
        this.failIfExitStatusNot0 = failIfExitStatusNot0;
    }

    @Override
    public String getTargetMessageVariable() {
        return customMessageTargetableSupport.getTargetMessageVariable();
    }
    @Override
    public void setTargetMessageVariable(String otherMessageVariable) {
        customMessageTargetableSupport.setTargetMessageVariable(
                otherMessageVariable);
    }

    @Override
    public String getTargetName() {
        return customMessageTargetableSupport.getTargetName();
    }

    public void setTargetName(String targetName) {
        this.customMessageTargetableSupport = new CustomMessageTargetableSupport(targetName);
    }

    @Override
    public boolean isTargetModifiedByGateway() {
        return customMessageTargetableSupport.isTargetModifiedByGateway();
    }

}
