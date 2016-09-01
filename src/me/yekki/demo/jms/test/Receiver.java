package me.yekki.demo.jms.test;

import me.yekki.demo.jms.ClientFactory;
import me.yekki.demo.jms.Consumer;
import me.yekki.demo.jms.Producer;

import javax.jms.JMSException;
import java.util.List;


public class Receiver {
    public static void main(String[] args) throws Exception {


        Consumer consumer = ClientFactory.newConsumer();

        //consumer.receiveAll().forEach(it->System.out.println(it));

        consumer.getConsumer().setMessageListener(msg->{

            try {
                System.out.println(msg.getBody(String.class));
            }
            catch (JMSException je) {
                je.printStackTrace();
            }
        });

        new java.io.InputStreamReader(System.in).read();
    }
}
