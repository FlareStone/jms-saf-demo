package me.yekki.jms;

import me.yekki.jms.cmd.HelpCommand;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.SystemUtils;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import static me.yekki.jms.Constants.Role.Sender;

public class CommandLineApp {

    private static Logger logger = Logger.getLogger(CommandLineApp.class.getName());
    private static org.apache.commons.cli.CommandLine cmd;
    private static Options options;

    public static void main(String... args) throws ParseException {

        options = buildOptions();
        cmd = (new DefaultParser()).parse(options, args);
        AppConfig config = AppConfig.getInstance(cmd);

        Instant start = Instant.now();

        execute(config);

        Instant end = Instant.now();

        logger.info(String.format("%s executed, elapsed:%sms", config.getRole(), Duration.between(start, end).toMillis()));
    }

    private static Options buildOptions() {

        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("shows this message")
                .build();

        Option roleOpt = Option.builder("r")
                .longOpt("role")
                .hasArg()
                .desc("s:sender, c:cleaner, h:helper, i:installer, m:monitor, u:uninstaller, a:storeadmin")
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

    public static void execute(AppConfig config) {

        Constants.Role role = config.getRole();
        Thread thread = null;

        try {
            Class clz = role.getCommandClass();

            if (clz != null) {

                switch (role) {
                    case Sender:
                        role.setDescription(String.format("(Type:%s, Mode:%s, Count:%d, Threads:%d)", config.getMessageType(), config.getDeliveryModeDesc(), config.getMessageCount(), config.getSenderThreadCount()));
                        break;
                    default:
                        role.setDescription("");
                }

                Constructor constructor = clz.getConstructor(AppConfig.class);
                thread = (Thread)constructor.newInstance(config);
            } else {
                thread = new HelpCommand(config);
            }

            thread.start();
            thread.join();
        }
        catch (Exception e) {

            logger.info("Failed to execute command:" + e.getMessage());
        }
    }
}
