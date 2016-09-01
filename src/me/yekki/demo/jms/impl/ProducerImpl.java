package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Producer;
import me.yekki.demo.jms.Utils;

import javax.jms.JMSException;
import javax.jms.JMSProducer;

public class ProducerImpl extends AbstractJMSClient implements Producer {


    protected JMSProducer producer;

    public ProducerImpl(String configFile) {

        super.init(configFile);
        producer = context.createProducer();
    }

    public JMSProducer getProducer() {
        return producer;
    }

    public void send(String msg) throws JMSException {

        producer.send(destination, msg);
    }

    public void send(String msg, int count) throws JMSException {

        send(msg,count, batchIntervalInMillis);
    }

    public void send(String msg, int count, long intervalTimeInMillis) throws JMSException {

        for ( int i = 0; i < count; i++ ) {
            send(msg);
            if ( intervalTimeInMillis != 0 ) Utils.sleep(intervalTimeInMillis);
        }
    }
}
