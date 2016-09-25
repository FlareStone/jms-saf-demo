package me.yekki.jms.cmd;


import me.yekki.jms.app.AppConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class HelpCommand extends Thread {

    private AppConfig config;

    public HelpCommand(AppConfig config) {

        this.config = config;
    }

    @Override
    public void run() {

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        org.apache.commons.cli.Option[] optionArray = config.getCommandLine().getOptions();

        for ( Option o:optionArray) {

            options.addOption(o);
        }

        formatter.printHelp("Main", options);
    }
}
