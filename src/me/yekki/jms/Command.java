package me.yekki.jms;

public interface Command extends Runnable, Constants{
    public void execute() throws JMSClientException;
}
