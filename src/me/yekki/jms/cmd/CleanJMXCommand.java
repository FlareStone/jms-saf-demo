package me.yekki.jms.cmd;

import me.yekki.jms.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.JMXCommand;
import me.yekki.jmx.administration.JMSAdministration;

import javax.management.ObjectName;

public class CleanJMXCommand extends JMXCommand {

    public CleanJMXCommand(AppConfig config) {

        super(config);
        super.init(false, true);
    }

    @Override
    public void execute() throws JMSClientException {

        try {
            JMSAdministration jmsadmin = new JMSAdministration(jmxWrapper);
            ObjectName dest = jmsadmin.getJMSDestinationRuntime("AdminServer", "DemoJMSServer", "DemoSystemModule!DemoQueue");
            jmsadmin.deleteMessagesFromJmsDestination(dest, "");
        }
        catch (Exception e) {
            throw new JMSClientException("Failed to execute clean command:" + e.getMessage());
        }

    }
}
