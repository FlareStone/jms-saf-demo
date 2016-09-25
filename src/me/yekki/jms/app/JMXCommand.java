package me.yekki.jms.app;


import me.yekki.JMSClientException;

public interface JMXCommand extends Runnable, Constants {

    public void connect(boolean isEdit, boolean isDomainRuntime) throws JMSClientException;
    public void disconnect() throws JMSClientException;
    public void monitor() throws JMSClientException;
}
