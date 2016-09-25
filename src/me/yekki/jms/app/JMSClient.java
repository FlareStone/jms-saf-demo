package me.yekki.jms.app;

import me.yekki.jms.cmd.*;
import me.yekki.jms.app.impl.JMSClientImpl;
import org.apache.commons.cli.CommandLine;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.naming.Context;
import java.io.Serializable;
import java.lang.reflect.Constructor;

public interface JMSClient extends AutoCloseable, Constants {

    public void close();
    public Context getInitialContext();
    public JMSContext getJMSContext();
    public JMSConsumer getConsumer();
    public JMSProducer getProducer();
    public ConnectionFactory getConnectionFactory();
    public AppConfig getAppConfig();
    public void send(Serializable msg);

    public static void execute(Constants.Role role, CommandLine cmd) {

        AppConfig config = AppConfig.newConfig(role, cmd);
        Thread thread = null;

        Class clz = role.getCommandClass();

        try {
            if (clz == null) {
                thread = new HelpCommand(config);
            }
            else {
                Constructor constructor = clz.getConstructor(AppConfig.class);
                thread = (Thread) constructor.newInstance(config);
            }
            thread.start();
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JMSClient newJMSClient(AppConfig config) {

        return new JMSClientImpl(config);
    }
}
