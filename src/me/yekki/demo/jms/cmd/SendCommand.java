package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.Constants;
import me.yekki.demo.jms.JMSClient;
import me.yekki.demo.jms.Producer;

import javax.jms.JMSException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SendCommand extends Thread {

    private AppConfig config;
    private int total;

    public SendCommand(AppConfig config, int total) {

        this.config = config;
        this.total = total;

        if (total <= 0) total = 1;
    }

    @Override
    public void run() {

        final Serializable msg = config.getMessageContent();

        JMSClient.MessageCalculator calculator = JMSClient.MessageCalculator.newInstance(total, config.getProperty(Constants.SENDER_THREADS_KEY, 1));

        try {

            ExecutorService es = Executors.newCachedThreadPool();

            for ( int i = 1; i <= calculator.getThreadCount(); i++ ) {

                Sender cmd = null;

                if ( i == calculator.getThreadCount() && calculator.getLeftMessageCount() != 0) {
                    cmd = new Sender(config, msg, calculator.getLeftMessageCount());
                }
                else {
                    cmd = new Sender(config, msg, calculator.getMessageCountPerThread());
                }

                es.submit(cmd);
            }

            es.shutdown();

            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ie) {
        }
    }

    public class Sender implements Runnable {

        private AppConfig config;
        private Serializable msg;
        private int count;

        public Sender(AppConfig config, Serializable msg, int count ) {

            this.config = config;
            this.msg = msg;
            this.count = count;
        }

        @Override
        public void run() {

            try (Producer p = JMSClient.newProducer(config)) {

                p.send(msg, count);

            } catch (JMSException je) {
                je.printStackTrace();
            }
        }
    }
}