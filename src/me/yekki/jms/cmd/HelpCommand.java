package me.yekki.jms.cmd;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class HelpCommand extends Thread {

    private CommandLine cmd;

    public HelpCommand(CommandLine cmd) {

        this.cmd = cmd;
    }

    @Override
    public void run() {

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        org.apache.commons.cli.Option[] optionArray = cmd.getOptions();

        for ( Option o:optionArray) {

            options.addOption(o);
        }

        formatter.printHelp("Main", options);
    }
}
