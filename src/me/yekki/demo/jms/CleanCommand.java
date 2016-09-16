package me.yekki.demo.jms;

import javax.jms.JMSException;
import java.util.concurrent.Callable;

public class CleanCommand implements Callable<Integer> {

    private AppConfig config;

    public CleanCommand(AppConfig config) {
        this.config = config;
    }

    @Override
    public Integer call() {

        int count = 0;

        try (Consumer c = JMSClient.newConsumer(config)) {

            while( c.receiveNoWait() != null ) {
                count++;
                Thread.yield();
            }

        } catch (JMSException je) {
            je.printStackTrace();
        }

        return count;
    }
}
