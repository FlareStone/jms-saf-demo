package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.WLSTClient;

public class CleanWLSTCommand extends Thread {

    private AppConfig config;

    public CleanWLSTCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        WLSTClient wlst = WLSTClient.newWLSTClient(config);
        StringBuffer script = new StringBuffer("serverRuntime()\n");
        script.append("cd('/JMSRuntime/AdminServer.jms/JMSServers/DemoJMSServer/Destinations/DemoSystemModule!DemoQueue')\n")
                .append("cmo.deleteMessages('')\n");

        wlst.connect();
        wlst.execute(script.toString());
        wlst.disConnect();
    }
}
