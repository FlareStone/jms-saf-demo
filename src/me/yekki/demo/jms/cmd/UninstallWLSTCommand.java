package me.yekki.demo.jms.cmd;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.WLSTClient;

public class UninstallWLSTCommand extends Thread {


    private AppConfig config;

    public UninstallWLSTCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        WLSTClient wlst = WLSTClient.newWLSTClient(config);
        StringBuffer script = new StringBuffer("cd('/')\n");
        script.append("cmo.destroyJMSSystemResource(getMBean('/SystemResources/DemoSystemModule'))\n")
                .append("cmo.destroyJMSServer(getMBean('/Deployments/DemoJMSServer'))\n");

        wlst.connect();
        wlst.startEdit();
        wlst.execute(script.toString());
        wlst.active();
        wlst.disConnect();
    }

}
