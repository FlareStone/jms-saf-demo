package me.yekki.jms.app.impl;

import me.yekki.JMSClientException;
import me.yekki.jms.app.AppConfig;
import me.yekki.jms.app.JMXCommand;
import me.yekki.jmx.utils.JMXWrapperRemote;

import javax.naming.Context;
import java.util.Hashtable;

public abstract class JMXCommandImpl extends Thread implements JMXCommand {

    protected AppConfig config;

    protected JMXWrapperRemote jmxWrapper;

    protected String username;

    protected String password;

    protected String url;

    public JMXCommandImpl(AppConfig config) {

        this.config = config;
        Hashtable<String, String> env = new Hashtable<>(config.getEnvironment());
        url = env.get(Context.PROVIDER_URL);
        username = env.get(Context.SECURITY_PRINCIPAL);
        password = env.get(Context.SECURITY_CREDENTIALS);
        jmxWrapper = new JMXWrapperRemote();
    }

    public void connect(boolean isEdit, boolean isDomainRuntime) throws JMSClientException {

        try {
            jmxWrapper.connectToAdminServer(isEdit, isDomainRuntime, username, password, url);
        }
        catch (Exception e) {
            throw new JMSClientException("Failed to connect admin server:" + e.getMessage());
        }
    }

    public void disconnect() throws JMSClientException {

        try {
            jmxWrapper.disconnectFromAdminServer(true);
        }
        catch (Exception e) {
            throw new JMSClientException("Failed to disconnect from admin server:" + e.getMessage());
        }
    }
}
