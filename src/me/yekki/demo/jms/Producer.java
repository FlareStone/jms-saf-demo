package me.yekki.demo.jms;

import javax.jms.JMSException;
import javax.jms.JMSProducer;

public class Producer extends Client {

    protected JMSProducer producer;

    protected Producer(String configFile) {

        super.init(configFile);
        producer = context.createProducer();
    }

    public JMSProducer getProducer() {
        return producer;
    }

    public void send(String msg) throws JMSException{

        producer.send(destination, msg);
    }

    public void send(String msg, int count) throws JMSException {

        send(msg,count, batchIntervalInMillis);
    }

    public void send(String msg, int count, long intervalTimeInMillis) throws JMSException {

        for ( int i = 0; i < count; i++ ) {
            send(msg);
            sleep(intervalTimeInMillis);
        }
    }
}
