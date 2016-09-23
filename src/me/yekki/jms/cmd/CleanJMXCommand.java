package me.yekki.jms.cmd;

import me.yekki.jms.*;
import me.yekki.jms.impl.JMXCommandImpl;
import me.yekki.jmx.administration.JMSAdministration;

import javax.management.ObjectName;

public class CleanJMXCommand extends JMXCommandImpl {

    public CleanJMXCommand(AppConfig config) {

        super(config);
    }

    @Override
    public void run() {

        try {
            connect(false, true);
            JMSAdministration jmsadmin = new JMSAdministration(jmxWrapper);
            ObjectName dest = jmsadmin.getJMSDestinationRuntime("DemoSystemModule!DemoQueue","DemoJMSServer","AdminServer");
            jmsadmin.deleteMessagesFromJmsDestination(dest, "");
            disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
