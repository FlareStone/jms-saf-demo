package me.yekki.demo.jms;

import javax.jms.JMSException;
import javax.jms.JMSProducer;

public interface Producer extends JMSClient {

    public JMSProducer getProducer();
    public void send(String msg) throws JMSException;
    public void send(String msg, int count) throws JMSException;
    public void send(String msg, int count, long intervalTimeInMillis) throws JMSException;
}
