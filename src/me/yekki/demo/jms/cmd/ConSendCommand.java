package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.Constants;
import me.yekki.demo.jms.JMSClient;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConSendCommand extends Thread {

    private AppConfig config;
    private int total;

    public ConSendCommand(AppConfig config, int total) {

        this.config = config;
        this.total = total;

        if (total <= 0) total = 1;
    }

    @Override
    public void run() {


        String msgType = config.getProperty(Constants.MESSAGE_TYPE_KEY, "string");

        final Serializable msg = config.getMessageContent();

        JMSClient.MessageCalculator calculator = JMSClient.MessageCalculator.newInstance(total, config.getProperty(Constants.SENDER_THREADS_KEY, 1));

        try {

            ExecutorService es = Executors.newCachedThreadPool();

            for ( int i = 1; i <= calculator.getThreadCount(); i++ ) {

                SendCommand cmd = null;

                if ( i == calculator.getThreadCount() && calculator.getLeftMessageCount() != 0) {
                    cmd = new SendCommand(config, msg, calculator.getLeftMessageCount());
                }
                else {
                    cmd = new SendCommand(config, msg, calculator.getMessageCountPerThread());
                }

                es.submit(cmd);
            }

            es.shutdown();

            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ie) {
        }
    }
}