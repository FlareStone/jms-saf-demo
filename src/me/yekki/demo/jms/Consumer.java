package me.yekki.demo.jms;

import javax.jms.JMSConsumer;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Consumer extends JMSClient {

    public JMSConsumer getConsumer();
    public Serializable receive();
    public Serializable receive(long timeoutInMillis);
    public List<Serializable> receiveAll();
    public Serializable receiveNoWait();
}
