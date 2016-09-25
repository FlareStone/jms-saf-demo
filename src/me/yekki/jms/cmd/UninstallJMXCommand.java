package me.yekki.jms.cmd;

import me.yekki.jms.app.AppConfig;
import me.yekki.jms.app.impl.JMXCommandImpl;
import me.yekki.jmx.creation_extension.JMSConfiguration;

public class UninstallJMXCommand extends JMXCommandImpl {

    public UninstallJMXCommand(AppConfig config) {

        super(config);
    }

    @Override
    public void run() {

        try {
            connect(true, false);
            JMSConfiguration jc = new JMSConfiguration(jmxWrapper);
            jc.destroyJMSModule("DemoSystemModule");
            jc.destroyJMSServer("DemoJMSServer");
            jc.destroyFileStore("DemoFileStore");
            disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
