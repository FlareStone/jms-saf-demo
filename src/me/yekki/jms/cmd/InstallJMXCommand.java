package me.yekki.jms.cmd;


import me.yekki.jms.app.AppConfig;
import me.yekki.jms.app.Constants;
import me.yekki.jms.app.impl.JMXCommandImpl;
import me.yekki.jmx.creation_extension.JMSConfiguration;


import javax.management.ObjectName;

public class InstallJMXCommand extends JMXCommandImpl {

    public InstallJMXCommand(AppConfig config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            connect(true, false);
            JMSConfiguration jc = new JMSConfiguration(jmxWrapper);
            ObjectName filestore = jc.createFileStore("DemoFileStore", "AdminServer", config.getProperty(Constants.FILE_STORE_PATH_KEY, null));
            jc.createAnewJMSServer("DemoJMSServer", filestore, "AdminServer");
            jc.createJMSModule("DemoSystemModule", "Server", "AdminServer");
            jc.createJmsConnectionFactory("DemoSystemModule", "DemoConnectionFactory", "democf");
            jc.createJMSSubDeployment("DemoSystemModule", "DemoSubDeployment", "DemoJMSServer");
            jc.createQueue("DemoSystemModule", "DemoQueue", "demoqueue", "DemoSubDeployment");
            disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
