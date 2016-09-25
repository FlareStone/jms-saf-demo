package me.yekki.jms;


import me.yekki.JMSClientException;

public abstract class JMSCommand implements Command {

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
