package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Constants;
import me.yekki.demo.jms.JMSClient;

import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

public class AbstractJMSClient implements JMSClient, Constants, AutoCloseable {

    protected static Logger logger = Logger.getLogger(AbstractJMSClient.class.getName());

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected Hashtable<String, String> env;
    protected Context ctx;

    protected long batchIntervalInMillis;

    protected AbstractJMSClient() {
        setBatchIntervalInMillis(BATCH_INTERVAL_IN_MILLIS);
    }

    public void setBatchIntervalInMillis(long batchIntervalInMillis) {

        this.batchIntervalInMillis = batchIntervalInMillis;
    }

    public long getBatchIntervalInMillis() {

        return batchIntervalInMillis;
    }

    public void init(String configFile) {

        env = getEnvironment(configFile);

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

    public Context getInitialContext() {

        return ctx;
    }

    public JMSContext getJMSContext() {

        return context;
    }

    public ConnectionFactory getConnectionFactory() {

        return connectionFactory;
    }

    @PreDestroy
    @Override
    public void close() throws JMSException {

        context.close();
    }

    public Hashtable<String, String> getEnvironment(String configFile) {

        if (env == null) {
            File propertiesFile = new File("config/" + configFile);
            Properties props = new Properties();

            if (propertiesFile.exists()) {

                logger.info(String.format("Configuring with properties file=[%s]", propertiesFile.getAbsolutePath()));

                try {
                    FileInputStream propFileStream = new FileInputStream(propertiesFile);
                    props.load(propFileStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                env = new Hashtable<String, String>();

                String providerUrl = (String)props.getProperty(PROVIDER_URL_KEY);

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

            } else {

                throw new RuntimeException("failed to load default.properties.");
            }
        }

        return env;
    }
}
