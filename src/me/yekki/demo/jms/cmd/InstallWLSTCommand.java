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

        String script = String.join("\n",
                "cd('/')",
                "cmo.createJMSServer('DemoJMSServer')",
                "cd('/JMSServers/DemoJMSServer')",
                "set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))",
                "cd('/')",
                "cmo.createJMSSystemResource('DemoSystemModule')",
                "cd('/JMSSystemResources/DemoSystemModule')",
                "set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')",
                "cmo.createConnectionFactory('DemoConnectionFactory')",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')",
                "cmo.setJNDIName('democf')",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/SecurityParams/DemoConnectionFactory')",
                "cmo.setAttachJMSXUserId(false)",
                "cmo.setSecurityPolicy('ThreadBased')",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/ClientParams/DemoConnectionFactory')",
                "cmo.setClientIdPolicy('Restricted')",
                "cmo.setSubscriptionSharingPolicy('Exclusive')",
                "cmo.setMessagesMaximum(10)",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/TransactionParams/DemoConnectionFactory')",
                "cmo.setXAConnectionFactoryEnabled(true)",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')",
                "cmo.setDefaultTargetingEnabled(true)",
                "cd('/JMSSystemResources/DemoSystemModule')",
                "cmo.createSubDeployment('DemoQueue')",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')",
                "cmo.createQueue('DemoQueue')",
                "cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/Queues/DemoQueue')",
                "cmo.setJNDIName('demoqueue')",
                "cmo.setSubDeploymentName('DemoQueue')",
                "cd('/JMSSystemResources/DemoSystemModule/SubDeployments/DemoQueue')",
                "set('Targets',jarray.array([ObjectName('com.bea:Name=DemoJMSServer,Type=JMSServer')], ObjectName))"
                );
        wlst.connect();
        wlst.startEdit();
        wlst.execute(script);
        wlst.active();
        wlst.disConnect();
    }
}
