package me.yekki.jms.cmd;


import me.yekki.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.JMSCommand;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class HelpCommand extends JMSCommand {

    private AppConfig config;

    public HelpCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void execute() {

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        org.apache.commons.cli.Option[] optionArray = config.getCommandLine().getOptions();

        for ( Option o:optionArray) {

            options.addOption(o);
        }

        formatter.printHelp("Main", options);
    }
}
