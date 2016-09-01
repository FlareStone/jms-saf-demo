package me.yekki.demo.jms;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;

public interface Browser extends JMSClient {

    int getQueueSize() throws JMSException;
    void browser(java.util.function.Consumer consumer) throws JMSException;
    QueueBrowser getBrowser();
}
