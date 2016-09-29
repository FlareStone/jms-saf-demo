package me.yekki.jms;

import me.yekki.jms.cmd.HelpCommand;
import org.apache.commons.cli.CommandLine;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.logging.Logger;

import static me.yekki.jms.Constants.Role.Sender;

public class JMSClient implements AutoCloseable, Constants {

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected AppConfig config;
    protected JMSConsumer consumer;
    protected JMSProducer producer;

    protected static Logger logger = Logger.getLogger(JMSClient.class.getName());

    public static JMSClient newJMSClient(AppConfig config) {

        return new JMSClient(config);
    }

    private JMSClient(AppConfig config) {

        this.config = config;

        Role role = config.getRole();
        Hashtable<String, String> env = config.getEnvironment();
        Context ctx = config.getInitialContext();

        try {
            connectionFactory = (ConnectionFactory) ctx.lookup(env.get(CONNECTON_FACTORY_KEY));

            destination = (Destination) ctx.lookup(env.get(DESTINATION_KEY));

            context = connectionFactory.createContext();

            switch (role) {
                case Sender:
                    producer = context.createProducer();
                    producer.setDeliveryMode(config.getDeliveryMode());
                    break;
                case Receiver:
                    consumer = context.createConsumer(destination);
            }
        }
        catch (NamingException ne) {
            logger.info("Failed to initial JMSClient:" + ne.getMessage());
        }
    }

    public AppConfig getAppConfig() {

        return config;
    }

    public JMSContext getJMSContext() {

        return context;
    }

    public ConnectionFactory getConnectionFactory() {

        return connectionFactory;
    }

    public JMSConsumer getConsumer() {

        return consumer;
    }

    public JMSProducer getProducer() {

        return producer;
    }

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
