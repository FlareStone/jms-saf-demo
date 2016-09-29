package me.yekki.jms;


public abstract class JMSCommand extends Thread implements Constants {

    abstract public void execute() throws JMSClientException;

    @Override
    public void run() {

        try {
            execute();
        }
        catch(JMSClientException e) {
            e.printStackTrace();
        }
    }
}
