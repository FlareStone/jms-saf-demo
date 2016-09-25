package me.yekki.jms.cmd;


import me.yekki.jms.app.AppConfig;

public class StoreAdminCommand extends Thread {

    private AppConfig config;

    public StoreAdminCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {
        weblogic.store.Admin.main(null);
    }
}
