package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.Consumer;

import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerImpl extends JMSClientImpl implements Consumer {

    protected JMSConsumer consumer;

    public ConsumerImpl(AppConfig config) {
        super(config);
        consumer = context.createConsumer(destination);
    }

    @Override
    public JMSConsumer getConsumer() {

        return consumer;
    }

    @Override
    public Serializable receive() {

        return consumer.receiveBody(Serializable.class);
    }

    @Override
    public Serializable receive(long timeoutInMillis) {

        return consumer.receiveBody(Serializable.class, timeoutInMillis);
    }

    @Override
    public List<Serializable> receiveAll() {

        ArrayList<Serializable> messages = new ArrayList<>();

        while(true) {

            Serializable message = receiveNoWait();

            if (message == null) break;

            messages.add(message);
        }

        return messages;
    }

    @Override
    public Serializable receiveNoWait() {
        return consumer.receiveBodyNoWait(Serializable.class);
    }

    @Override
    public void close() throws JMSException {
        consumer.close();
        super.close();
    }
}
