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

        String script = String.join("\n",
                "cmo.destroyJMSSystemResource(getMBean('/SystemResources/DemoSystemModule'))",
                "cmo.destroyJMSServer(getMBean('/Deployments/DemoJMSServer'))"
                );
        wlst.connect();
        wlst.startEdit();
        wlst.execute(script);
        wlst.active();
        wlst.disConnect();
    }

}
