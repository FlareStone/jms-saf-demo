package me.yekki.jms;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public interface JMXClient extends Constants {

    public static final long TIMEOUT=10000L;

    public MBeanServerConnection getConnection();
    public ObjectName getService();
    public ObjectName getServerRuntime(String name);
    public ObjectName getJMSServer(String serverName, String jmsServerName);
    public ObjectName getDestination(String serverName, String jmsServerName, String destName);
    public Object invoke(ObjectName name, String operationName, Object params[], String signature[]);
    public void close();
}
