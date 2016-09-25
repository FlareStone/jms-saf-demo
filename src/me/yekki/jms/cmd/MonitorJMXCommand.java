package me.yekki.jms.cmd;

import me.yekki.JMSClientException;
import me.yekki.jms.app.AppConfig;
import me.yekki.jms.app.impl.JMXCommandImpl;
import me.yekki.jmx.creation_extension.JMSConfiguration;
import me.yekki.jmx.monitor.JMSMonitor;
import me.yekki.jmx.monitor.ServerAndProcessMonitor;
import me.yekki.jmx.utils.JMXWrapper;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MonitorJMXCommand extends JMXCommandImpl {

    public MonitorJMXCommand(AppConfig config) {

        super(config);
    }

    @Override
    public void run() {

        try {
            connect(false, true);
            monitor();
            disconnect();
        }
        catch (JMSClientException e) {
            e.printStackTrace();
        }
    }
}
