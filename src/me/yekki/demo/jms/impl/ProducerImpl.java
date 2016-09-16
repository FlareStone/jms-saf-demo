package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.Producer;
import weblogic.jms.extensions.WLMessageProducer;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import java.io.Serializable;

public class ProducerImpl extends JMSClientImpl implements Producer {

    protected JMSProducer producer;
    protected long batchIntervalInMillis;

    // The compression threshold value can be set to zero or higher. Setting it to zero will cause every message body to be compressed.
    protected int compressionThreshold;

    //NON_PERSISTENT=1, PERSISTENT=2
    protected int deliveryMode;

    public ProducerImpl(AppConfig config) {

        super(config);

        batchIntervalInMillis = config.getProperty(BATCH_INTERVAL_IN_MILLIS_KEY, 0);
        deliveryMode = config.getProperty(DELIVERY_MODE_KEY, 1);


        producer = context.createProducer();

        producer.setDeliveryMode(deliveryMode);
    }

    @Override
    public JMSProducer getProducer() {
        return producer;
    }

    @Override
    public void send(Serializable msg) throws JMSException {

        producer.send(destination, msg);
    }

    @Override
    public void send(Serializable msg, int count) throws JMSException {

        send(msg, count, batchIntervalInMillis);
    }

    @Override
    public void send(Serializable msg, int count, long intervalTimeInMillis) throws JMSException {

        for ( int i = 1; i <= count; i++ ) {
            send(msg);
            if ( intervalTimeInMillis != 0 ) {
                try {
                    Thread.sleep(intervalTimeInMillis);
                }
                catch (InterruptedException ie) {}
            }
        }
    }
}
