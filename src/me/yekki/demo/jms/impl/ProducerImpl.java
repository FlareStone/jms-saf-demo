package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.Producer;
import me.yekki.demo.jms.Utils;

import javax.jms.JMSException;
import javax.jms.JMSProducer;

public class ProducerImpl extends JMSClientImpl implements Producer {

    protected JMSProducer producer;
    protected long batchIntervalInMillis;
    protected long verbosePerMsgCount;

    public ProducerImpl(String configFile) {

        super.init(configFile);
        producer = context.createProducer();
        batchIntervalInMillis = Utils.getProperty(getProperties(), BATCH_INTERVAL_IN_MILLIS_KEY, DEFAULT_BATCH_INTERVAL_IN_MILLIS);
        verbosePerMsgCount = Utils.getProperty(getProperties(), VERBOSE_PER_MSG_COUNT_KEY, DEFAULT_VERBOSE_PER_MSG_COUNT);
    }

    public JMSProducer getProducer() {
        return producer;
    }

    public void send(String msg) throws JMSException {

        producer.send(destination, msg);
    }

    public void send(String msg, int count) throws JMSException {

        send(msg, count, batchIntervalInMillis);
    }

    public void send(String msg, int count, long intervalTimeInMillis) throws JMSException {

        for ( int i = 0; i < count; i++ ) {
            send(msg);
            if ( intervalTimeInMillis != 0 ) Utils.sleep(intervalTimeInMillis);

            if ( ( verbosePerMsgCount != 0 ) && (i % verbosePerMsgCount == 0) ) logger.info(String.format("sent %d messages.", verbosePerMsgCount));
        }
    }
}
