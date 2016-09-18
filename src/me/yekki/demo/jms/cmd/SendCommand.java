package me.yekki.demo.jms.cmd;


import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.JMSClient;
import me.yekki.demo.jms.Producer;

import javax.jms.JMSException;
import java.io.Serializable;

public class SendCommand implements Runnable {

    private AppConfig config;
    private Serializable msg;
    private int count;

    public SendCommand(AppConfig config, Serializable msg, int count ) {

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
