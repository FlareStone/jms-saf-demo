package me.yekki.demo.jms;

import me.yekki.demo.jms.impl.ConsumerImpl;
import me.yekki.demo.jms.impl.ProducerImpl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.Context;

public interface JMSClient extends AutoCloseable, Constants {

    public void close() throws JMSException;

    public Context getInitialContext();
    public JMSContext getJMSContext();
    public ConnectionFactory getConnectionFactory();
    public AppConfig getAppConfig();

    public static Producer newProducer(AppConfig config) {

        Producer producer = new ProducerImpl(config);
        return producer;
    }

    public static Consumer newConsumer(AppConfig config) {

        Consumer consumer = new ConsumerImpl(config);

        return consumer;
    }

    public static class MessageCalculator {
        private int threads;
        private int total;
        private int messagesPerThread;
        private int leftMessages;

        private MessageCalculator(int totalMessage, int threadCount) {
            this.total = totalMessage;
            this.threads = threadCount;

            messagesPerThread = total / threads;
            leftMessages = total - threads * messagesPerThread;

            if (leftMessages > 0) {
                this.threads--;
                messagesPerThread = total / threads;
                leftMessages = total - threads * messagesPerThread;
            }

        }

        public static MessageCalculator newInstance(int total, int threads) {

            return new MessageCalculator(total, threads);
        }

        public int getLeftMessageCount() {

            return leftMessages;
        }

        public int getMessageCountPerThread() {

            return messagesPerThread;
        }

        public int getThreadCount() {

            if (leftMessages == 0)
                return threads;
            else
                return threads + 1;
        }

        public String toString() {

            return String.format("threads=%d, messages per thread=%d, left messages=%d", getThreadCount(), getMessageCountPerThread(), getLeftMessageCount());
        }

    }
}
