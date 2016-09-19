package me.yekki.demo.jms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

import static me.yekki.demo.jms.Constants.Role;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static CommandLine cmd;
    private static Options options;

    public static void main(String... args) throws Exception {

        options = buildOptions();
        cmd = (new DefaultParser()).parse(options, args);

        Instant start = Instant.now();

        Role role = Role.getRole(cmd.getOptionValue("r"));

        JMSClient.execute(role, cmd);

        Instant end = Instant.now();

        logger.info(String.format("%s executed, elapsed:%sms", role, Duration.between(start, end).toMillis()));

    }

    private static Options buildOptions() {

        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("shows this message")
                .build();

        Option roleOpt = Option.builder("r")
                .longOpt("role")
                .hasArg()
                .desc("s:sender, c:cleaner, h:helper, i:installer, u:uninstaller, a:storeadmin")
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
