package me.yekki.jms.cmd;

import me.yekki.jms.AppConfig;
import me.yekki.jms.JMSCommand;

public class StoreAdminCommand extends JMSCommand {

    private AppConfig config;

    public StoreAdminCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void execute() {
        weblogic.store.Admin.main(null);
    }
}
