package me.yekki.jms;

import me.yekki.JMSClientException;

public interface Command extends Runnable, Constants{
    public void execute() throws JMSClientException;
}
