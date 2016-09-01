package me.yekki.demo.jms;

import me.yekki.demo.jms.impl.ConsumerImpl;
import me.yekki.demo.jms.impl.ProducerImpl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;
import java.util.Hashtable;

import static me.yekki.demo.jms.Constants.DEFAULT_CONFIG_FILE;

public interface JMSClient {

    public void init(String configFile);
    public void close() throws JMSException;

    public void setBatchIntervalInMillis(long batchIntervalInMillis);
    public long getBatchIntervalInMillis();
    public Context getInitialContext();
    public JMSContext getJMSContext();
    public ConnectionFactory getConnectionFactory();
    public Hashtable<String, String> getEnvironment(String configFile);

    public static Producer newProducer(String configFile) {

        Producer producer = new ProducerImpl(configFile);
        return producer;
    }

    public static Producer newProducer() {

        return newProducer(DEFAULT_CONFIG_FILE);
    }

    public static Consumer newConsumer(String configFile) {

        Consumer consumer = new ConsumerImpl(configFile);

        return consumer;
    }

    public static Consumer newConsumer() {

        return newConsumer(DEFAULT_CONFIG_FILE);
    }
}
