package me.yekki.jms;


import me.yekki.jms.utils.SizableObject;
import org.apache.commons.cli.CommandLine;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AppConfig implements Constants {

    private static Logger logger = Logger.getLogger(AppConfig.class.getName());

    private Properties properties;
    private CommandLine cmd;
    private Role role;
    private Context ctx;
    private Hashtable<String, String> env;

    private static AppConfig INSTANCE;

    public static AppConfig getInstance(CommandLine cmd) {

        if (INSTANCE == null) {
            synchronized (AppConfig.class) {
                if (INSTANCE == null) INSTANCE = new AppConfig(cmd);
            }
        }

        return INSTANCE;
    }

    private AppConfig(CommandLine cmd) {

        this.cmd = cmd;
        this.role = Constants.Role.getRole(cmd.getOptionValue("r"));

        properties = new Properties();

        loadProperties(APP_CONFIG_FILE);

        Optional.ofNullable(role.getConfigFileKey()).ifPresent((key)->{

            loadProperties(properties.getProperty(key));

            Optional.ofNullable(properties.getProperty(PROVIDER_URL_KEY)).ifPresent((url)->{
                if (url.startsWith("file:")) properties.setProperty(SENDER_THREADS_KEY, "1");
            });
        });
    }

    public Role getRole() {

        return role;
    }

    public CommandLine getCommandLine() {

        return cmd;
    }

    public Properties getProperties() {

        return properties;
    }

    private void loadProperties(String configFile) {

        assert configFile != null;

        Path path = Paths.get("config", configFile);

        if (Files.exists(path)) {

            try {
                Properties props = new Properties();
                props.load(Files.newInputStream(path));
                properties.putAll(props);
            }
            catch (IOException ioe) {
                logger.info("Failed to load properties file:" + ioe.getMessage());
            }
        }
    }

    public Hashtable<String, String> getEnvironment() {

        if ( env == null) {

            env = new Hashtable<>();

            String providerUrl = properties.getProperty(PROVIDER_URL_KEY);

            assert providerUrl != null;

            if (providerUrl.startsWith("file:"))
                env.put(Context.INITIAL_CONTEXT_FACTORY, SAF_INITIAL_CONTEXT_FACTORY);
            else
                env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);

            env.put(Context.PROVIDER_URL, properties.getProperty(PROVIDER_URL_KEY));
            env.put(Context.SECURITY_PRINCIPAL, properties.getProperty(PRINCIPAL_KEY));
            env.put(Context.SECURITY_CREDENTIALS, properties.getProperty(CREDENTIAL_KEY));
            env.put(CONNECTON_FACTORY_KEY, properties.getProperty(CONNECTON_FACTORY_KEY));
            env.put(DESTINATION_KEY, properties.getProperty(DESTINATION_KEY));
        }

        return env;
    }

    public Context getInitialContext() {

        if (ctx == null) {
            try {
                ctx = new InitialContext(getEnvironment());
            } catch (NamingException e) {
                logger.info("Failed to new initial context:" + e.getMessage());
                Runtime.getRuntime().exit(-1);
            }
        }

        return ctx;
    }

    public MessageType getMessageType() {

        return Optional.ofNullable(MessageType.getMessageType(properties.getProperty(MESSAGE_TYPE_KEY))).orElse(MessageType.Text);
    }

    public Serializable getMessageContent() {

        MessageType msgType = getMessageType();

        Serializable msg = null;

        switch (msgType) {

            case Text:
                msg = Optional.of(properties.getProperty(MESSAGE_CONTENT_KEY)).get();
                break;
            case Binary:
                msg = SizableObject.buildObject(Integer.parseInt(Optional.of(properties.getProperty(MESSAGE_SIZE_KEY)).get()));
                break;
            case File:
                Path path = Paths.get(properties.getProperty(MESSAGE_FILENAME_KEY));
                try {
                    if (Files.exists(path)) msg = new String(Files.readAllBytes(path));
                } catch (IOException io) {
                    logger.info("Failed to load message file:" + path);
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal message type:" + msgType);
        }

        assert msg != null;

        return msg;
    }

    public int getMessageCount() {

        int count = 0;

        if (getCommandLine().hasOption("n")) count = Integer.parseInt(getCommandLine().getOptionValue("n"));

        return count;
    }

    public String getSenderProfile() {
        MessageType msgType = getMessageType();
        int msgCount = getMessageCount();
        int threadCount = getSenderThreadCount();
        String model = getDeliveryModeDesc();
        return String.format("(Type:%s, Mode:%s, Count:%d, Threads:%d)", msgType, model, msgCount, threadCount);
    }

    public String getDeliveryModeDesc() {
        int mode = getDeliveryMode();
        switch (mode) {
            case 1:
                return "Non Persistent";
            case 2:
                return "Persistent";
            default:
                throw new IllegalArgumentException("Illegal message delivery mode:" + mode);
        }
    }

    public int getDeliveryMode() {

        return Integer.parseInt(Optional.of(properties.getProperty(DELIVERY_MODE_KEY)).get());
    }

    public String getFileStorePath() {

        return Optional.of(properties.getProperty(FILE_STORE_PATH_KEY)).get();
    }

    public int getSenderThreadCount() {

        return Integer.parseInt(Optional.of(properties.getProperty(SENDER_THREADS_KEY)).get());
    }

    public List<String> getMonitors() {

        Predicate<Map.Entry> filter = (e)->{ return e.getKey().toString().startsWith(MONITOR_KEY);};
        Stream<String> stream = properties.entrySet().stream().filter(filter).map(e->{return (String)e.getValue();});

        return stream.collect(Collectors.toList());
    }
}
