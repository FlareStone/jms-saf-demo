package me.yekki.demo.jms;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import java.io.Serializable;

public interface Producer extends JMSClient {

    public JMSProducer getProducer();
    public void send(Serializable msg) throws JMSException;
    public void send(Serializable msg, int count) throws JMSException;
    public void send(Serializable msg, int count, long intervalTimeInMillis) throws JMSException;
}
