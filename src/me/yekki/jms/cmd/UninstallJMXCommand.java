package me.yekki.jms.cmd;

import me.yekki.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.JMXCommand;
import me.yekki.jmx.creation_extension.JMSConfiguration;
import me.yekki.jmx.utils.WLSJMXException;

public class UninstallJMXCommand extends JMXCommand {

    public UninstallJMXCommand(AppConfig config) {

        super(config);
        super.init(true, false);
    }

    @Override
    public void execute() throws JMSClientException {

        try {

            JMSConfiguration jc = new JMSConfiguration(jmxWrapper);
            jc.destroyJMSModule("DemoSystemModule");
            jc.destroyJMSServer("DemoJMSServer");
            jc.destroyFileStore("DemoFileStore");
        }
        catch (WLSJMXException e) {
           throw new JMSClientException(e.getMessage());
        }
    }
}
