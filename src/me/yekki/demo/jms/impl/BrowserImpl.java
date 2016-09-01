package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Browser;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.TextMessage;
import java.util.Enumeration;
import java.util.function.Consumer;

public class BrowserImpl extends JMSClientImpl implements Browser {

    protected QueueBrowser browser;

    public BrowserImpl(String configFile) {

        super.init(configFile);

        if ( !(destination instanceof Queue)) throw new RuntimeException("the browser doesn't support topic.");

        browser = context.createBrowser((Queue)destination);
    }

    public int getQueueSize() throws JMSException {

        Enumeration messages = browser.getEnumeration();
        int count = 0;

        while ( messages.hasMoreElements()) {
            messages.nextElement();
            count++;
        }

        return count;
    }

    public void browser(Consumer consumer) throws JMSException {

        Enumeration messages = browser.getEnumeration();

        while ( messages.hasMoreElements()) {
            String message = ((TextMessage) messages.nextElement()).getText();
            consumer.accept(message);
        }
    }

    public QueueBrowser getBrowser() {

        return browser;
    }

    @PreDestroy
    public void close() throws JMSException {
        browser.close();
        super.close();
    }
}
