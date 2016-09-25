package me.yekki.jms.cmd;

import me.yekki.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.Constants;
import me.yekki.jms.JMXCommand;
import me.yekki.jmx.creation_extension.JMSConfiguration;

import javax.management.ObjectName;

public class InstallJMXCommand extends JMXCommand {

    public InstallJMXCommand(AppConfig config) {
        super(config);
        super.init(true, false);
    }

    @Override
    public void execute() throws JMSClientException {
        try {
            JMSConfiguration jc = new JMSConfiguration(jmxWrapper);
            ObjectName filestore = jc.createFileStore("DemoFileStore", "AdminServer", config.getProperty(Constants.FILE_STORE_PATH_KEY, null));
            jc.createAnewJMSServer("DemoJMSServer", filestore, "AdminServer");
            jc.createJMSModule("DemoSystemModule", "Server", "AdminServer");
            jc.createJmsConnectionFactory("DemoSystemModule", "DemoConnectionFactory", "democf");
            jc.createJMSSubDeployment("DemoSystemModule", "DemoSubDeployment", "DemoJMSServer");
            jc.createQueue("DemoSystemModule", "DemoQueue", "demoqueue", "DemoSubDeployment");
        }
        catch (Exception e) {
            throw new JMSClientException("Failed to execute install command:" + e.getMessage());
        }
    }
}
