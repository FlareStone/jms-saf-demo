package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.JMSClient;
import me.yekki.demo.jms.Utils;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

public class JMSClientImpl implements JMSClient {

    protected static Logger logger = Logger.getLogger(JMSClientImpl.class.getName());

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected Context ctx;
    protected Properties properties = Utils.loadProperties(APP_CONFIG_FILE);

    @Override
    public void init(String configFile) {

        Hashtable<String, String> env = getEnvironment(configFile);

        try {
            ctx = new InitialContext(env);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        try {
            connectionFactory = (ConnectionFactory) ctx.lookup(env.get(CONNECTON_FACTORY_KEY));

            destination = (Destination) ctx.lookup(env.get(DESTINATION_KEY));

            context = connectionFactory.createContext();
        }
        catch (NamingException ne) {
            ne.printStackTrace();
        }
    }

    @Override
    public Properties getProperties() {

        return properties;
    }

    @Override
    public Context getInitialContext() {

        return ctx;
    }

    @Override
    public JMSContext getJMSContext() {

        return context;
    }

    @Override
    public ConnectionFactory getConnectionFactory() {

        return connectionFactory;
    }

    @Override
    public void close() throws JMSException {

        context.close();
    }

    protected Hashtable<String, String> getEnvironment(String configFile) {

        Properties props = Utils.loadProperties(configFile);

        Hashtable<String, String> env = new Hashtable<>();

        String providerUrl = props.getProperty(PROVIDER_URL_KEY);

        if ( null != providerUrl && providerUrl.startsWith("file:")) {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jms.safclient.jndi.InitialContextFactoryImpl");
        }
        else {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        }

        env.put(Context.PROVIDER_URL, props.getProperty(PROVIDER_URL_KEY));
        env.put(Context.SECURITY_PRINCIPAL, props.getProperty(PRINCIPAL_KEY));
        env.put(Context.SECURITY_CREDENTIALS, props.getProperty(CREDENTIAL_KEY));
        env.put(CONNECTON_FACTORY_KEY, props.getProperty(CONNECTON_FACTORY_KEY));
        env.put(DESTINATION_KEY, props.getProperty(DESTINATION_KEY));

        return env;
    }
}
