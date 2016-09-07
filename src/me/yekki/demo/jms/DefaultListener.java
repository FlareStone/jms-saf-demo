package me.yekki.demo.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class DefaultListener implements MessageListener {

    private Consumer consumer;
     private static Logger logger = Logger.getLogger(DefaultListener.class.getName());
    private long messageVerboseIntervalInCount;
    private AtomicInteger counter;

    public DefaultListener(Consumer consumer, AtomicInteger counter) {
        this.counter = counter;
        this.consumer = consumer;
        this.messageVerboseIntervalInCount = Utils.getProperty(consumer.getProperties(), Constants.MESSAGE_VERBOSE_INTERVAL_IN_COUNT_KEY);
    }

    @Override
    public void onMessage(Message message) {

        if ( counter.incrementAndGet() % messageVerboseIntervalInCount == 0  && counter.get() > messageVerboseIntervalInCount) logger.info(String.format("%d messages are received.", messageVerboseIntervalInCount));
    }
}
