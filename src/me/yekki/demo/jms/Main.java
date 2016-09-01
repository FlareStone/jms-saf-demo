package me.yekki.demo.jms;

import org.apache.commons.cli.*;

import javax.jms.JMSException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {

    protected static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String... args) throws Exception {

        CommandLineParser parser = new DefaultParser();

        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("shows this message")
                .build();

        Option receiverOpt = Option.builder("r")
                .longOpt("role")
                .hasArg()
                .desc("role: s:sender, r:receiver")
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

        Option intervalOpt = Option.builder("i")
                .hasArg()
                .type(Long.class)
                .longOpt("interval-in-millis")
                .desc("interval in millis for batch operations")
                .build();

        Options options = new Options();
        options.addOption(intervalOpt)
                .addOption(receiverOpt)
                .addOption(countOpt)
                .addOption(msgOpt)
                .addOption(helpOpt)
                .addOption(configOpt);

        CommandLine cmd = parser.parse(options, args);
        String configFile = cmd.getOptionValue("c");


        if (null == configFile || "".equals(configFile)) configFile = Constants.DEFAULT_CONFIG_FILE;

        if (cmd.hasOption("r")) {

            String role = cmd.getOptionValue("r");

            if (role.equals("s")) {

                Producer producer = JMSClient.newProducer(configFile);

                int count = 1;

                if (cmd.hasOption("n")) count = Integer.parseInt(cmd.getOptionValue("n"));

                String msg = "";

                if (cmd.hasOption("m")) msg = cmd.getOptionValue("m");

                long interval = Constants.BATCH_INTERVAL_IN_MILLIS;

                if (cmd.hasOption("i")) interval = Long.parseLong(cmd.getOptionValue("i"));

                logger.info(String.format("starting sender...(count:%d, interval:%dms)", count, interval));

                producer.send(msg, count, interval);

                producer.close();

                logger.info(String.format("sent %d messages.", count));
            } else if (role.equals("r")) {

                logger.info("starting receiver...");
                Consumer consumer = JMSClient.newConsumer(configFile);
                final AtomicInteger counter = new AtomicInteger();

                consumer.getConsumer().setMessageListener(msg -> {

                    try {
                        System.out.println(msg.getBody(String.class));
                        counter.incrementAndGet();
                    } catch (JMSException je) {
                        je.printStackTrace();
                    }
                });

                new java.io.InputStreamReader(System.in).read();

                consumer.close();

                logger.info(String.format("got %d messages.", counter.get()));
            } else if (role.equals("b")) {

                logger.info("starting browser...");
                Browser browser = JMSClient.newBrowser(configFile);
                logger.info("message count:" + browser.getQueueSize());
                browser.close();
            }
            else {
                help(options);
            }
        } else {

           help(options);
        }

    }

    public static void help(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("me.yekki.demo.jms.Main", options);
    }
}
