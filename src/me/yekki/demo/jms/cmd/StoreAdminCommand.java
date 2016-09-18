package me.yekki.demo.jms.cmd;


public class StoreAdminCommand extends Thread {

    public StoreAdminCommand() {

    }

    @Override
    public void run() {

        weblogic.store.Admin.main(null);
    }
}
