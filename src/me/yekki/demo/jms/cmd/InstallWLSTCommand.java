package me.yekki.demo.jms.cmd;


import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.WLSTClient;

public class InstallWLSTCommand extends Thread {


    private AppConfig config;

    public InstallWLSTCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        WLSTClient wlst = WLSTClient.newWLSTClient(config);
        StringBuffer script = new StringBuffer("cd('/')\n");

        script.append("cmo.createJMSServer('DemoJMSServer')\n")
                .append("cd('/JMSServers/DemoJMSServer')\n")
                .append("set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))\n")
                .append("cd('/')\n")
                .append("cmo.createJMSSystemResource('DemoSystemModule')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule')\n")
                .append("set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')\n")
                .append("cmo.createConnectionFactory('DemoConnectionFactory')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')\n")
                .append("cmo.setJNDIName('democf')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/SecurityParams/DemoConnectionFactory')\n")
                .append("cmo.setAttachJMSXUserId(false)\n")
                .append("cmo.setSecurityPolicy('ThreadBased')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/ClientParams/DemoConnectionFactory')\n")
                .append("cmo.setClientIdPolicy('Restricted')\n")
                .append("cmo.setSubscriptionSharingPolicy('Exclusive')\n")
                .append("cmo.setMessagesMaximum(10)\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/TransactionParams/DemoConnectionFactory')\n")
                .append("cmo.setXAConnectionFactoryEnabled(true)\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')\n")
                .append("cmo.setDefaultTargetingEnabled(true)\n")
                .append("cd('/JMSSystemResources/DemoSystemModule')\n")
                .append("cmo.createSubDeployment('DemoQueue')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')\n")
                .append("cmo.createQueue('DemoQueue')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/Queues/DemoQueue')\n")
                .append("cmo.setJNDIName('demoqueue')\n")
                .append("cmo.setSubDeploymentName('DemoQueue')\n")
                .append("cd('/JMSSystemResources/DemoSystemModule/SubDeployments/DemoQueue')\n")
                .append("set('Targets',jarray.array([ObjectName('com.bea:Name=DemoJMSServer,Type=JMSServer')], ObjectName))\n");

        wlst.connect();
        wlst.startEdit();
        wlst.execute(script.toString());
        wlst.active();
        wlst.disConnect();
    }
}
