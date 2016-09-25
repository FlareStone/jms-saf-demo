package me.yekki.jms;


import me.yekki.jms.utils.SizableObject;
import org.apache.commons.cli.CommandLine;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Properties;

import static me.yekki.jms.Constants.MessageType.Text;


public class AppConfig implements Constants {

    private Properties properties;
    private CommandLine cmd;
    private Role role;
    private Context ctx;
    Hashtable<String, String> env;

    private static AppConfig config;

    public static AppConfig newConfig(CommandLine cmd) {

        if (config == null) {
            Constants.Role role = Constants.Role.getRole(cmd.getOptionValue("r"));
            config = new AppConfig(role, cmd);
        }

        return config;
    }

    private AppConfig(Role aRole, CommandLine aCmd) {

        this.cmd = aCmd;
        this.role = aRole;

        properties = loadProperties(APP_CONFIG_FILE);

        if (role.getConfigFileKey() == null || "".equals(role.getConfigFileKey())) return;

        String configFile = properties.getProperty(role.getConfigFileKey());

        if (Files.exists(Paths.get("config/" + configFile))) {
            properties.putAll(loadProperties(configFile));
            String providerUrl = properties.getProperty(PROVIDER_URL_KEY, "");

            //saf only support single thread
            if (providerUrl.startsWith("file:")) properties.setProperty(SENDER_THREADS_KEY, "1");
        }
    }

    public Role getRole() {

        return role;
    }

    public CommandLine getCommandLine() {

        return cmd;
    }

    private Properties loadProperties(String configFile) {

        File propertiesFile = new File("config/" + configFile);
        Properties props = new Properties();

        if (propertiesFile.exists()) {

            try {
                FileInputStream propFileStream = new FileInputStream(propertiesFile);
                props.load(propFileStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return props;

        } else {

            throw new RuntimeException(String.format("failed to load properties file=[%s]", propertiesFile.getAbsolutePath()));
        }
    }

    public long getProperty(String key, long defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || "".equals(value)) return defaultValue;

        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ne) {

            return defaultValue;
        }
    }

    public String getProperty(String key, String defaultValue) {

        return properties.getProperty(key, defaultValue);
    }

    public boolean getProperty(String key, boolean defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || "".equals(value)) return defaultValue;

        try {

            return Boolean.parseBoolean(value);
        }
        catch (NumberFormatException ne) {

            return defaultValue;
        }
    }

    public int getProperty(String key, int defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || "".equals(value)) return defaultValue;

        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ne) {

            return defaultValue;
        }
    }

    public Hashtable<String, String> getEnvironment() {

        if ( env == null) {
            env = new Hashtable<>();

            String providerUrl = properties.getProperty(PROVIDER_URL_KEY);

            if (null != providerUrl && providerUrl.startsWith("file:")) {
                env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jms.safclient.jndi.InitialContextFactoryImpl");
            } else {
                env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            }

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
                ctx = new InitialContext(env);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        return ctx;
    }

    public MessageType getMessageType() {
        return MessageType.getMessageType(getProperty(MESSAGE_TYPE_KEY, Text.name()));
    }

    public Serializable getMessageContent() {

        MessageType msgType = getMessageType();

        Serializable msg = null;

        switch (msgType) {

            case Text:
                msg = getProperty(MESSAGE_CONTENT_KEY, "");
                break;
            case Binary:
                int size = getProperty(MESSAGE_SIZE_KEY, 1);
                msg = SizableObject.buildObject(size);
                break;
            case File:

                try {
                    msg = new String(Files.readAllBytes(Paths.get(getProperty(MESSAGE_FILENAME_KEY, null))));
                } catch (IOException io) {
                    io.printStackTrace();
                }

                break;
            default:
                throw new IllegalArgumentException("Illegal message type:" + msgType);
        }

        return msg;
    }

    public int getMessageCount() {

        int count = 0;

        if (getCommandLine().hasOption("n")) count = Integer.parseInt(getCommandLine().getOptionValue("n"));

        return count;
    }

    public String getDeliveryMode() {
        int mode = getProperty(DELIVERY_MODE_KEY, -1);
        switch (mode) {
            case 1:
                return "Non Persistent";
            case 2:
                return "Persistent";
            default:
                throw new IllegalArgumentException("Illegal message delivery mode:" + mode);
        }
    }
}
