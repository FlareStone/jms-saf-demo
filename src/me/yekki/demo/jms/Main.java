package me.yekki.demo.jms;

import org.apache.commons.cli.*;
import weblogic.ejbgen.JMS;

import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static CommandLine cmd;
    private static Options options;
    private static String configFile;

    public static void main(String... args) throws Exception {

        options = buildOptions();
        cmd = (new DefaultParser()).parse(options, args);
        configFile = cmd.hasOption("c") ? cmd.getOptionValue("c") : Constants.JMS_CONFIG_FILE;

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

        try(Consumer consumer = JMSClient.newConsumer(configFile);) {
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
        try(Browser browser = JMSClient.newBrowser(configFile);) {
            logger.info("message count:" + browser.getQueueSize());
        }
        catch (JMSException je) {
            je.printStackTrace();
        }
    }

    private static void receive() {

        logger.info("starting receiver...");

        final AtomicInteger counter = new AtomicInteger();

        try(Consumer consumer = JMSClient.newConsumer(configFile);) {

            long verbosePerMsgCount = Utils.getProperty(consumer.getProperties(), Constants.VERBOSE_PER_MSG_COUNT_KEY, Constants.DEFAULT_VERBOSE_PER_MSG_COUNT);

            consumer.getConsumer().setMessageListener(msg -> {

                if ( counter.incrementAndGet() % verbosePerMsgCount == 0 ) logger.info(String.format("%d messages are received.", verbosePerMsgCount));
            });

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

        String msg = "";

        if (cmd.hasOption("m")) msg = cmd.getOptionValue("m");


        logger.info(String.format("starting sender...(message count:%d)", count));

        Instant start = Instant.now();

        try (Producer producer = JMSClient.newProducer(configFile);){
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
