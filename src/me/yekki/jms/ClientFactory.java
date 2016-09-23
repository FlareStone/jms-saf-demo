package me.yekki.jms;

import me.yekki.jms.impl.JMSClientImpl;
import me.yekki.jms.impl.JMXClientImpl;

public class ClientFactory {

    public static JMXClient newJMXClient(AppConfig config) {

        return new JMXClientImpl(config);
    }

    public static JMSClient newJMSClient(AppConfig config) {

        return new JMSClientImpl(config);
    }
}
