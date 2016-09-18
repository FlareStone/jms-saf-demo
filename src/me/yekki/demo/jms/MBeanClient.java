package me.yekki.demo.jms;

import org.python.util.InteractiveInterpreter;
import weblogic.management.scripting.utils.WLSTInterpreter;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.concurrent.Callable;

/**
 * Created by gniu on 16/9/18.
 */
public class MBeanClient {

    protected String url;
    JMXServiceURL serviceURL;
    JMXConnector connector;
    MBeanServerConnection connection;

    public MBeanClient(AppConfig config) {
        Hashtable<String, String> env = config.getEnvironment();
        URL url = null;

        try {

            String urlStr = env.get(Context.PROVIDER_URL);

            url = new URL(urlStr.replace("t3","http"));
            serviceURL = new JMXServiceURL("t3", url.getHost(), url.getPort(), "/jndi/weblogic.management.mbeanservers.domainruntime");
            env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
            connector = JMXConnectorFactory.connect(serviceURL, env);
            connection = connector.getMBeanServerConnection();

        }
        catch (MalformedURLException me) {
            me.printStackTrace();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public MBeanServerConnection getConnection() {

        return connection;
    }

    public void close() {
        try {
            connector.close();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
