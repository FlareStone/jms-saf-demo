package me.yekki.jms;

import me.yekki.jms.cmd.HelpCommand;
import org.apache.commons.cli.CommandLine;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import static me.yekki.jms.Constants.Role.Sender;

public class JMSClient implements AutoCloseable, Constants {

    protected ConnectionFactory connectionFactory;
    protected Destination destination;
    protected JMSContext context;
    protected AppConfig config;
    protected JMSConsumer consumer;
    protected JMSProducer producer;

    public static Role execute(CommandLine cmd) {

        AppConfig config = AppConfig.newConfig(cmd);
        Role role = config.getRole();
        Thread thread = null;

        Class clz = config.getRole().getCommandClass();

        if (role == Sender) {
            MessageType msgType = config.getMessageType();
            int msgCount = config.getMessageCount();
            int threadCount = config.getProperty(Constants.SENDER_THREADS_KEY, -1);
            String model = config.getDeliveryMode();
            role.setDescription(String.format("(Type:%s, Mode:%s, Count:%d, Threads:%d)", msgType, model, msgCount, threadCount));
        }

        try {
            if (clz == null) {
                thread = new Thread(new HelpCommand(config));
            }
            else {
                Constructor constructor = clz.getConstructor(AppConfig.class);
                thread = new Thread((Runnable) constructor.newInstance(config));
            }
            thread.start();
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }

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

    public void close(){

        if (consumer != null) consumer.close();
        if (context != null) context.close();
    }
}
