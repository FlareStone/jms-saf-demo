package me.yekki.demo.jms;

import org.apache.commons.cli.*;

import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static CommandLine cmd;
    private static Options options;

    public static void main(String... args) throws Exception {

        options = buildOptions();
        cmd = (new DefaultParser()).parse(options, args);

        if (cmd.hasOption("r")) {

            String role = cmd.getOptionValue("r");

            switch (role) {
                case "s":
                    send();
                    break;
                case "r":
                    receive();
                    break;
                case "b":
                    browse();
                    break;
                case "c":
                    clear();
                    break;
                default:
                    help();
            }
        }
        else {
            help();
        }
    }

    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("me.yekki.demo.jms.Main", options);
    }

    private static void clear() {

        logger.info("starting cleaner...");

        int count = 0;

        Instant start = Instant.now();

        try(Consumer consumer = JMSClient.newConsumer();) {
            JMSConsumer jconsumer = consumer.getConsumer();
            while( jconsumer.receiveNoWait() != null ) {
                count++;
            }
        }
        catch (JMSException je) {
            je.printStackTrace();
        }

        Instant end = Instant.now();

        logger.info(String.format("cleaned up %d messages, elapsed:%sms", count, Duration.between(start, end).toMillis()));
    }

    private static void browse() {

        logger.info("starting browser...");
        try(Browser browser = JMSClient.newBrowser();) {
            logger.info("message count:" + browser.getQueueSize());
        }
        catch (JMSException je) {
            je.printStackTrace();
        }
    }

    private static void receive() {

        logger.info("starting receiver...");

        final AtomicInteger counter = new AtomicInteger();

        try(Consumer consumer = JMSClient.newConsumer();) {

            consumer.getConsumer().setMessageListener(new DefaultListener(consumer, counter));
            new java.io.InputStreamReader(System.in).read();
        }
        catch (JMSException je) {
            je.printStackTrace();
        }
        catch (IOException io) {

        }

        logger.info(String.format("all %d messages are received.", counter.get()));
    }

    private static void send() {

        int count = 1;

        if (cmd.hasOption("n")) count = Integer.parseInt(cmd.getOptionValue("n"));


        logger.info(String.format("starting sender...(message count:%d)", count));

        Instant start = Instant.now();

        try (Producer producer = JMSClient.newProducer();){

            long size = Utils.getProperty(producer.getProperties(), Constants.MESSAGE_SIZE_KEY);
            Serializable msg = null;

            if (size == -1l) {
                msg = producer.getProperties().getProperty(Constants.MESSAGE_CONTENT_KEY);
            } else {
                msg = SizableObject.buildObject(size);
            }

            producer.send(msg, count);
        }
        catch (JMSException je ) {

            je.printStackTrace();
        }

        Instant end = Instant.now();

        logger.info(String.format("all %d messages are sent finished. elapsed:%sms", count, Duration.between(start, end).toMillis()));
    }

    private static Options buildOptions () {

        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("shows this message")
                .build();

        Option receiverOpt = Option.builder("r")
                .longOpt("role")
                .hasArg()
                .desc("role: s:sender, r:receiver, c:cleaner")
                .build();

        Option countOpt = Option.builder("n")
                .hasArg()
                .longOpt("count")
                .type(Integer.class)
                .desc("count of messages")
                .build();

        Option msgOpt = Option.builder("m")
                .hasArg()
                .longOpt("message")
                .desc("text message content")
                .build();

        Option configOpt = Option.builder("c")
                .hasArg()
                .longOpt("config")
                .desc("config file")
                .build();

        Options options = new Options();
        options.addOption(configOpt)
                .addOption(receiverOpt)
                .addOption(countOpt)
                .addOption(msgOpt)
                .addOption(helpOpt);

        return options;
    }
}
