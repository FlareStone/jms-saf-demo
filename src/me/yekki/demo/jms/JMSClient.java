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

    public static Producer newProducer() {

        Producer producer = new ProducerImpl();
        return producer;
    }

    public static Consumer newConsumer() {

        Consumer consumer = new ConsumerImpl();

        return consumer;
    }


    public static Browser newBrowser() {

        Browser browser = new BrowserImpl();

        return browser;
    }

}
