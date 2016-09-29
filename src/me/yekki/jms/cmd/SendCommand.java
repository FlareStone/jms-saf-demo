package me.yekki.jms.cmd;

import me.yekki.jms.AppConfig;
import me.yekki.jms.Constants;
import me.yekki.jms.JMSClient;
import me.yekki.jms.JMSCommand;
import me.yekki.jms.utils.MessageCalculator;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SendCommand extends JMSCommand {

    private AppConfig config;
    private int total;

    public SendCommand(AppConfig config) {

        this.config = config;
        this.total = config.getMessageCount();
    }

    @Override
    public void execute() {

        final Serializable msg = config.getMessageContent();

        MessageCalculator calculator = MessageCalculator.newInstance(total, config.getSenderThreadCount());

        try {

            ExecutorService es = Executors.newCachedThreadPool();

            for ( int i = 1; i <= calculator.getThreadCount(); i++ ) {

                Runnable cmd = null;

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

            JMSClient client = JMSClient.newJMSClient(config);

            for (int i = 0; i < count; i++) {
                client.send(msg);
            }

            client.close();
        }
    }
}