package me.yekki.demo.jms;

public class ClientFactory implements Constants {

    private ClientFactory() {

    }

    public static Producer newProducer(String configFile) {

        Producer producer = new Producer(configFile);
        return producer;
    }

    public static Producer newProducer() {

        return newProducer(DEFAULT_CONFIG_FILE);
    }

    public static Consumer newConsumer(String configFile) {

        Consumer consumer = new Consumer(configFile);

        return consumer;
    }

    public static Consumer newConsumer() {

        Consumer consumer = new Consumer(DEFAULT_CONFIG_FILE);

        return consumer;
    }
}
