package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Consumer;

import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerImpl extends JMSClientImpl implements Consumer {

    protected JMSConsumer consumer;

    public ConsumerImpl() {

        super.init(getProperties().getProperty(RECEIVER_CONFIG_FILE_KEY));
        consumer = context.createConsumer(destination);
    }

    @Override
    public JMSConsumer getConsumer() {

        return consumer;
    }

    @Override
    public Serializable receive() {

        return consumer.receiveBody(String.class);
    }

    @Override
    public Serializable receive(long timeoutInMillis) {

        return consumer.receiveBody(String.class, timeoutInMillis);
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
        return consumer.receiveBodyNoWait(String.class);
    }

    @Override
    public void close() throws JMSException {
        consumer.close();
        super.close();
    }
}
