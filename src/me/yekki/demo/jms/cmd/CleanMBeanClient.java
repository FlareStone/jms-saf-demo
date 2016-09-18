package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.MBeanClient;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class CleanMBeanClient extends Thread {

    private AppConfig config;

    public CleanMBeanClient(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        MBeanClient mBeanClient = new MBeanClient(config);
        MBeanServerConnection connection = mBeanClient.getConnection();

        try {
            ObjectName service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
            ObjectName[] serverRuntimes = (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
            ObjectName serverRuntime = null;

            for (ObjectName runtime:serverRuntimes) {

                if ("AdminServer".equals(runtime.getKeyProperty("Name"))) {

                    serverRuntime = runtime;
                    break;
                }
            }

            ObjectName jmsRuntime = (ObjectName) connection.getAttribute(serverRuntime, "JMSRuntime");
            ObjectName[] jmsServers = (ObjectName[]) connection.getAttribute(jmsRuntime, "JMSServers");

            for (ObjectName jmsServer: jmsServers) {
                if ("DemoJMSServer".equals(jmsServer.getKeyProperty("Name"))) {
                    ObjectName[] destinations = (ObjectName[]) connection.getAttribute(jmsServer, "Destinations");
                    for (ObjectName destination: destinations) {

                        if ("DemoSystemModule!DemoQueue".equals(destination.getKeyProperty("Name"))) {

                            connection.invoke(destination, "deleteMessages", new Object[] {""}, new String[] {"java.lang.String"});
                            break;
                        }
                    }
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
