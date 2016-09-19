package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.JMSClient;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Hashtable;

import static me.yekki.demo.jms.Constants.Role.Receiver;
import static me.yekki.demo.jms.Constants.Role.Sender;

public class JMSClientImpl implements JMSClient {

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected Context ctx;
    protected AppConfig config;
    protected JMSConsumer consumer;
    protected JMSProducer producer;

    public JMSClientImpl(AppConfig config) {

        this.config = config;

        Role role = config.getRole();
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

            switch (role) {
                case Sender:
                    producer = context.createProducer();
                    producer.setDeliveryMode(config.getProperty(DELIVERY_MODE_KEY, 1));
                    break;
                case Receiver:
                    consumer = context.createConsumer(destination);
            }
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
    public JMSConsumer getConsumer() {

        return consumer;
    }

    @Override
    public JMSProducer getProducer() {

        return producer;
    }

    @Override
    public void send(Serializable msg) {

        if (producer != null) {
            producer.send(destination, msg);
        }
    }

    @Override
    public void close(){

        if (consumer != null) consumer.close();
        if (context != null) context.close();
    }
}
