package me.yekki.demo.jms;

import org.apache.commons.cli.*;

import javax.jms.JMSException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static me.yekki.demo.jms.Constants.Role;
import static me.yekki.demo.jms.JMSClient.MessageCalculator;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static CommandLine cmd;
    private static Options options;

    public static void main(String... args) throws Exception {

        options = buildOptions();
        cmd = (new DefaultParser()).parse(options, args);

        Role role = Role.getRole(cmd.getOptionValue("r"));

        switch (role) {
            case Sender:
                send();
                break;
            case Cleaner:
                clear();
                break;
            default:
                help();
        }
    }

    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("me.yekki.demo.jms.Main", options);
    }

    private static void clear() {

        AppConfig config = AppConfig.newConfig(Constants.RECEIVER_CONFIG_FILE_KEY);
        int threads = config.getProperty(Constants.CLEANER_THREADS_KEY, 1);

        logger.info(String.format("starting cleaner with %d threads...", threads));

        ExecutorService es = Executors.newCachedThreadPool();
        ExecutorCompletionService<Integer> service = new ExecutorCompletionService<Integer>(es);

        Instant start = Instant.now();

        IntStream.range(0, threads).forEach( i->{
            service.submit(new CleanCommand(config));
        });

        int finished = 0;
        int total = 0;
        try {
            do {
                total += service.take().get();
                finished++;

            } while (finished < threads);
        }
        catch(ExecutionException ee) {
            ee.printStackTrace();
        }
        catch(InterruptedException ie) {

        }

        es.shutdown();

        Instant end = Instant.now();

        logger.info(String.format("cleaned up %d messages, elapsed:%sms", total, Duration.between(start, end).toMillis()));
    }

    private static void send() {

        int count = 1;
        AppConfig config = AppConfig.newConfig(Constants.SENDER_CONFIG_FILE_KEY);
        String msgType = config.getProperty(Constants.MESSAGE_TYPE_KEY, "string");

        if (cmd.hasOption("n")) count = Integer.parseInt(cmd.getOptionValue("n"));

        final Serializable msg = config.getMessageContent();


        MessageCalculator calculator = MessageCalculator.newInstance(count, config.getProperty(Constants.SENDER_THREADS_KEY, 1));
        logger.info(String.format("starting sender with %d threads...", calculator.getThreadCount()));

        Instant start = Instant.now();

        try {

            ExecutorService es = Executors.newCachedThreadPool();

            for ( int i = 1; i <= calculator.getThreadCount(); i++ ) {

                SendCommand cmd = null;

                if ( i == calculator.getThreadCount() && calculator.getLeftMessageCount() != 0) {
                    cmd = new SendCommand(config, msg, calculator.getLeftMessageCount());
                }
                else {
                    cmd = new SendCommand(config, msg, calculator.getMessageCountPerThread());
                }

                es.submit(cmd);
            }

            es.shutdown();

            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ie) {
        }

        Instant end = Instant.now();

        logger.info(String.format("all %d messages are sent finished. elapsed:%sms", count, Duration.between(start, end).toMillis()));
    }

    private static Options buildOptions() {

        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("shows this message")
                .build();

        Option roleOpt = Option.builder("r")
                .longOpt("role")
                .hasArg()
                .desc("role: s:sender, c:cleaner")
                .build();

        Option countOpt = Option.builder("n")
                .hasArg()
                .longOpt("count")
                .type(Integer.class)
                .desc("count of messages")
                .build();

        Options options = new Options();
        options.addOption(helpOpt)
                .addOption(roleOpt)
                .addOption(countOpt);

        return options;
    }
}
