package me.yekki.jms.app.impl;

import me.yekki.JMSClientException;
import me.yekki.jms.app.AppConfig;
import me.yekki.jms.app.JMXCommand;
import me.yekki.jmx.utils.JMXWrapper;
import me.yekki.jmx.utils.JMXWrapperRemote;

import javax.naming.Context;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    public void monitor() throws JMSClientException {

        try {
            String m = config.getProperty("MONITOR", "");
            String[] meta = m.split(":");

            if (null != meta && meta.length >= 1) {

                String cz = meta[0];
                String methodStr = cz.substring(cz.lastIndexOf(".") + 1);
                String clazzStr = cz.substring(0, cz.lastIndexOf("."));

                Class clazz = Class.forName(clazzStr);

                Constructor constructor = clazz.getConstructor(JMXWrapper.class, PrintStream.class);
                Object monitor = constructor.newInstance(jmxWrapper, System.out);

                String[] args = (meta.length > 1) ? meta[1].split("_") : null;

                Class[] argsClass = new Class[args.length];

                for (int i = 0; i < argsClass.length; i++) {
                    argsClass[i] = String.class;
                }

                Method method = clazz.getMethod(methodStr, argsClass);

                method.invoke(monitor, args);
            }
        }
        catch (Exception e) {

            throw new JMSClientException("Failed to invoke monitor:" + e.getMessage());
        }
    }
}
