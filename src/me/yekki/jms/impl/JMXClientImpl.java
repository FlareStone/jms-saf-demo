package me.yekki.jms.impl;

import me.yekki.jms.AppConfig;
import me.yekki.jms.JMXClient;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

public class JMXClientImpl implements JMXClient {

    protected JMXServiceURL serviceURL;
    protected JMXConnector connector;
    protected MBeanServerConnection connection;
    protected ObjectName service;

    public JMXClientImpl(AppConfig config) {
        Hashtable<String, Object> env = new Hashtable<>(config.getEnvironment());
        URL url = null;

        try {

            String urlStr = (String)env.get(Context.PROVIDER_URL);
            url = new URL(urlStr.replace("t3","http"));
            serviceURL = new JMXServiceURL("t3", url.getHost(), url.getPort(), "/jndi/weblogic.management.mbeanservers.domainruntime");
            env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
            env.put("jmx.remote.x.request.waiting.timeout", new Long(TIMEOUT));
            connector = JMXConnectorFactory.connect(serviceURL, env);
            assert connector != null;
            connection = connector.getMBeanServerConnection();
            service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
            assert service != null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MBeanServerConnection getConnection() {

        return connection;
    }

    @Override
    public ObjectName getService() {

        return service;
    }

    @Override
    public ObjectName getServerRuntime(String name) {

        ObjectName serverRuntime = null;

        try {
            ObjectName[] serverRuntimes = (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");

            for (ObjectName runtime:serverRuntimes) {

                if (name.equals(runtime.getKeyProperty("Name"))) {

                    serverRuntime = runtime;
                    break;
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return serverRuntime;
    }

    @Override
    public ObjectName getJMSServer(String serverName, String jmsServerName) {

        ObjectName jmsServer = null;

        try {
            ObjectName serverRuntime = getServerRuntime(serverName);

            assert serverRuntime != null;

            ObjectName jmsRuntime = (ObjectName) connection.getAttribute(serverRuntime, "JMSRuntime");
            ObjectName[] jmsServers = (ObjectName[]) connection.getAttribute(jmsRuntime, "JMSServers");

            for (ObjectName js: jmsServers) {
                if (js.getKeyProperty("Name").equals(jmsServerName)) {

                    jmsServer = js;
                    break;
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return jmsServer;
    }

    public ObjectName getDestination(String serverName, String jmsServerName, String destName) {

        ObjectName destination = null;

        try {
            ObjectName jmsServerRuntime = getJMSServer(serverName, jmsServerName);

            assert jmsServerRuntime != null;

            ObjectName[] destinations = (ObjectName[]) connection.getAttribute(jmsServerRuntime, "Destinations");
            for (ObjectName dest: destinations) {

                if (dest.getKeyProperty("Name").equals(destName)) {

                    destination = dest;
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return destination;
    }

    @Override
    public void close() {
        try {
            connector.close();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public Object invoke(ObjectName name, String operationName,
                         Object params[], String signature[]) {

        Object ret = null;

        try {
            ret = connection.invoke(name, operationName, params, signature);
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return ret;
    }
}
