package me.yekki.demo.jms;

import me.yekki.demo.jms.impl.BrowserImpl;
import me.yekki.demo.jms.impl.ConsumerImpl;
import me.yekki.demo.jms.impl.ProducerImpl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;
import java.util.Properties;

public interface JMSClient extends AutoCloseable, Constants {

    public void init(String configFile);
    public void close() throws JMSException;

    public Context getInitialContext();
    public JMSContext getJMSContext();
    public ConnectionFactory getConnectionFactory();
    public Properties getProperties();

    public static Producer newProducer(String configFile) {

        Producer producer = new ProducerImpl(configFile);
        return producer;
    }

    public static Producer newProducer() {

        return newProducer(JMS_CONFIG_FILE);
    }

    public static Consumer newConsumer(String configFile) {

        Consumer consumer = new ConsumerImpl(configFile);

        return consumer;
    }

    public static Consumer newConsumer() {

        return newConsumer(JMS_CONFIG_FILE);
    }

    public static Browser newBrowser(String configFile) {

        Browser browser = new BrowserImpl(configFile);

        return browser;
    }

    public static Browser newBrowser() {

        return newBrowser(JMS_CONFIG_FILE);
    }
}
