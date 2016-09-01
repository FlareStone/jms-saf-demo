package me.yekki.demo.jms;

import javax.jms.JMSConsumer;
import java.util.ArrayList;
import java.util.List;

public class Consumer extends Client {

    protected JMSConsumer consumer;

    protected Consumer(String configFile) {
        super.init(configFile);
        consumer = context.createConsumer(destination);
    }

    public JMSConsumer getConsumer() {

        return consumer;
    }

    public String receive() {

        return consumer.receiveBody(String.class);
    }

    public String receive(long timeoutInMillis) {

        return consumer.receiveBody(String.class, timeoutInMillis);
    }

    public List<String> receiveAll() {

        ArrayList<String> messages = new ArrayList<>();

        while(true) {

            String message = receiveNoWait();

            if (message == null) break;

            messages.add(message);
        }

        return messages;
    }

    public String receiveNoWait() {
        return consumer.receiveBodyNoWait(String.class);
    }
}
