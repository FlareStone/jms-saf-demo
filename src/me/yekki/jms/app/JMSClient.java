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

        switch (role) {
            case Sender:
                int total = 1;
                if (cmd.hasOption("n")) total = Integer.parseInt(cmd.getOptionValue("n"));
                thread = new SendCommand(config, total);
                break;
            case Cleaner:
                thread =  new CleanJMXCommand(config);
                break;
            case Uninstaller:
                thread =  new UninstallJMXCommand(config);
                break;
            case StoreAdmin:
                thread =  new StoreAdminCommand();
                break;
            case Installer:
                thread = new InstallJMXCommand(config);
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

    public static JMSClient newJMSClient(AppConfig config) {

        return new JMSClientImpl(config);
    }
}
