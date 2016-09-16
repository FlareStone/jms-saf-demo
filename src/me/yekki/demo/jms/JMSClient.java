package me.yekki.demo.jms;

import me.yekki.demo.jms.impl.ConsumerImpl;
import me.yekki.demo.jms.impl.ProducerImpl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;

public interface JMSClient extends AutoCloseable, Constants {

    public void close() throws JMSException;

    public Context getInitialContext();
    public JMSContext getJMSContext();
    public ConnectionFactory getConnectionFactory();
    public AppConfig getAppConfig();

    public static Producer newProducer(AppConfig config) {

        Producer producer = new ProducerImpl(config);
        return producer;
    }

    public static Consumer newConsumer(AppConfig config) {

        Consumer consumer = new ConsumerImpl(config);

        return consumer;
    }
}
