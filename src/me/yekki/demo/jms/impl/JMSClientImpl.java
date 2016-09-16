package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.JMSClient;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class JMSClientImpl implements JMSClient {

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected Context ctx;
    protected AppConfig config;

    public JMSClientImpl(AppConfig config) {

        this.config = config;

        Hashtable<String, String> env = config.getEnvironment();

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
    public AppConfig getAppConfig() {

        return config;
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
}
