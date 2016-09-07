package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Producer;
import me.yekki.demo.jms.Utils;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import java.io.Serializable;

public class ProducerImpl extends JMSClientImpl implements Producer {

    protected JMSProducer producer;
    protected long batchIntervalInMillis;
    protected long messageVerboseIntervalInCount;

    public ProducerImpl() {

        super.init(getProperties().getProperty(SENDER_CONFIG_FILE_KEY));

        producer = context.createProducer();
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        batchIntervalInMillis = Utils.getProperty(getProperties(), BATCH_INTERVAL_IN_MILLIS_KEY);
        messageVerboseIntervalInCount = Utils.getProperty(getProperties(), MESSAGE_VERBOSE_INTERVAL_IN_COUNT_KEY);
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

        for ( int i = 0; i < count; i++ ) {
            send(msg);
            if ( intervalTimeInMillis != 0 ) Utils.sleep(intervalTimeInMillis);

            if ( ( messageVerboseIntervalInCount != 0 ) && (i % messageVerboseIntervalInCount == 0) && i > messageVerboseIntervalInCount ) logger.info(String.format("sent %d messages.", messageVerboseIntervalInCount));
        }
    }
}
