package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.ClientFactory;
import me.yekki.demo.jms.JMXClient;

import javax.management.ObjectName;

/**

 WLST Version:

 WLSTClient wlst = WLSTClient.newWLSTClient(config);

 String script = String.join("\n",
 "serverRuntime()",
 "cd('/JMSRuntime/AdminServer.jms/JMSServers/DemoJMSServer/Destinations/DemoSystemModule!DemoQueue')",
 "cmo.deleteMessages('')"
 );
 wlst.connect();
 wlst.execute(script);
 wlst.disConnect();

 */



public class CleanJMXCommand extends Thread {

    private AppConfig config;

    public CleanJMXCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        JMXClient jmxClient = ClientFactory.newJMXClient(config);
        ObjectName dest = jmxClient.getDestination("AdminServer", "DemoJMSServer", "DemoSystemModule!DemoQueue");
        jmxClient.invoke(dest, "deleteMessages", new Object[] {""}, new String[] {"java.lang.String"});
        jmxClient.close();
    }
}
