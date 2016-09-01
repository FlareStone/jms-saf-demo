package me.yekki.demo.jms;

import javax.jms.JMSConsumer;
import java.util.List;

public interface Consumer extends JMSClient {

    public JMSConsumer getConsumer();
    public String receive();
    public String receive(long timeoutInMillis);
    public List<String> receiveAll();
    public String receiveNoWait();
}
