package me.yekki.demo.jms.impl;

import me.yekki.demo.jms.AppConfig;
import me.yekki.demo.jms.Constants;
import me.yekki.demo.jms.WLSTClient;
import org.python.core.PyException;
import org.python.util.InteractiveInterpreter;
import weblogic.management.scripting.utils.WLSTInterpreter;

import javax.naming.Context;
import java.util.Hashtable;

public class WLSTClientImpl implements WLSTClient {

    protected InteractiveInterpreter interpreter;
    protected String username;
    protected String password;
    protected String url;

    public WLSTClientImpl(AppConfig config) {
        interpreter = new WLSTInterpreter();
        Hashtable<String, String> env = config.getEnvironment();
        username = env.get(Context.SECURITY_PRINCIPAL);
        password = env.get(Context.SECURITY_CREDENTIALS);
        url = env.get(Context.PROVIDER_URL);
    }

    @Override
    public boolean validateConnection() {
        try {
            this.connect();
            this.disConnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void startEdit() {

        execute("edit()\nstartEdit()\n");
    }

    @Override
    public void active() {
        execute("save()\nactivate(block='true')\n");
    }

    @Override
    public void connect() {

        execute(String.format("connect('%s','%s','%s')\n", username, password, url));
    }

    @Override
    public void disConnect() {
        execute("disconnect(force='true')\n");
    }

    @Override
    public void execute(String script) {

        interpreter.exec(script);
    }
}
