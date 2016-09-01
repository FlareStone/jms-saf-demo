package me.yekki.demo.jms.test;

import me.yekki.demo.jms.ClientFactory;
import me.yekki.demo.jms.Producer;

import javax.jms.JMSException;
import java.util.stream.IntStream;

public class Sender {

    public static void main(String[] args) throws Exception {

        Producer producer = ClientFactory.newProducer("saf.properties");
        //producer.send("hello", 10);

        IntStream.range(0, 100).forEach(i-> {

            try {
                producer.send(String.format("No.%d hello", i));

                if (i%100000== 0) {
                    System.out.println("sent 10000 messages");
                }
            }
            catch ( JMSException je) {

            }
        });
      /*  while(true) {

            producer.send("hello");
            producer.sleep(100);
        }
*/
       // System.out.println("finished!");
    }
}
