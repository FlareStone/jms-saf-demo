package me.yekki.demo.jms;

import me.yekki.demo.jms.cmd.*;
import me.yekki.demo.jms.impl.ConsumerImpl;
import me.yekki.demo.jms.impl.ProducerImpl;
import org.apache.commons.cli.CommandLine;

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

    public static void execute(Constants.Role role, CommandLine cmd) {

        AppConfig config = AppConfig.newConfig(role);
        Thread thread = null;

        switch (role) {
            case Sender:
                int total = 1;
                if (cmd.hasOption("n")) total = Integer.parseInt(cmd.getOptionValue("n"));
                thread = new SendCommand(config, total);
                break;
            case Cleaner:
                thread =  new CleanWLSTCommand(config);
                break;
            case Uninstaller:
                thread =  new UninstallWLSTCommand(config);
                break;
            case StoreAdmin:
                thread =  new StoreAdminCommand();
                break;
            case Installer:
                thread = new InstallWLSTCommand(config);
                break;
            default:
                thread = new HelpCommand(cmd);
        }

        thread.start();

        try {
            thread.join();
        }
        catch( InterruptedException ie) {
        }
    }
}
