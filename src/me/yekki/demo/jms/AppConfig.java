package me.yekki.demo.jms;


import org.apache.openjpa.util.UnsupportedException;

import javax.naming.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;
;

public class AppConfig implements Constants {

    private Properties properties;

    public static AppConfig newConfig(Role role) {

        return new AppConfig(role.getConfigFileKey());
    }

    private AppConfig(String configFileKey) {

        properties = loadProperties(APP_CONFIG_FILE);

        String configFile = properties.getProperty(configFileKey);

        if (Files.exists(Paths.get("config/" + configFile))) {
            properties.putAll(loadProperties(configFile));
        }
        else {
            throw new RuntimeException(String.format("failed to load properties file=[%s]", configFile));
        }
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

    public void showSettings() {


    }

    public Hashtable<String, String> getEnvironment() {


        Hashtable<String, String> env = new Hashtable<>();

        String providerUrl = properties.getProperty(PROVIDER_URL_KEY);

        if ( null != providerUrl && providerUrl.startsWith("file:")) {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jms.safclient.jndi.InitialContextFactoryImpl");
        }
        else {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        }

        env.put(Context.PROVIDER_URL, properties.getProperty(PROVIDER_URL_KEY));
        env.put(Context.SECURITY_PRINCIPAL, properties.getProperty(PRINCIPAL_KEY));
        env.put(Context.SECURITY_CREDENTIALS, properties.getProperty(CREDENTIAL_KEY));
        env.put(CONNECTON_FACTORY_KEY, properties.getProperty(CONNECTON_FACTORY_KEY));
        env.put(DESTINATION_KEY, properties.getProperty(DESTINATION_KEY));

        return env;
    }

    public Serializable getMessageContent() {

        String msgType = getProperty(Constants.MESSAGE_TYPE_KEY, "string");

        Serializable msg = null;

        switch (msgType) {

            case "string":
                msg = getProperty(Constants.MESSAGE_CONTENT_KEY, "");
                break;
            case "binary":
                long size = getProperty(Constants.MESSAGE_SIZE_KEY, -1l);
                msg = SizableObject.buildObject(size);
                break;
            case "file":

                try {
                    //TODO: check file exist
                    msg = new String(Files.readAllBytes(Paths.get(getProperty(Constants.MESSAGE_FILENAME_KEY, null))));
                } catch (IOException io) {

                    io.printStackTrace();
                }

                break;
            default:

                throw new UnsupportedException("wrong message type");
        }

        return msg;
    }

    public static void main(String... args) {

        Role role = Role.getRole("c");
        System.out.println(role.name());
    }
}
