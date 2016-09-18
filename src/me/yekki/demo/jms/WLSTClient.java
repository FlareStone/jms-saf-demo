package me.yekki.demo.jms;

import me.yekki.demo.jms.impl.WLSTClientImpl;
import org.python.core.PyException;
import org.python.util.InteractiveInterpreter;

public interface WLSTClient extends Constants {

    public void connect();

    public void disConnect();

    public void execute(String script);

    public void startEdit();

    public boolean validateConnection();

    public void active();

    public static WLSTClient newWLSTClient(AppConfig config) {

        return new WLSTClientImpl(config);
    }
}
