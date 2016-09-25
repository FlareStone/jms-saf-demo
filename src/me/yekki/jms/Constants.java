package me.yekki.jms;

import me.yekki.jms.cmd.*;

public interface Constants {

    //keys
    static final String PRINCIPAL_KEY = "PRINCIPAL";
    static final String CREDENTIAL_KEY = "CREDENTIAL";
    static final String PROVIDER_URL_KEY = "PROVIDER_URL";
    static final String CONNECTON_FACTORY_KEY = "CONNECTION_FACTORY";
    static final String DESTINATION_KEY = "DESTINATION";
    static final String MESSAGE_SIZE_KEY = "MESSAGE_SIZE";
    static final String FILE_STORE_PATH_KEY = "FILE_STORE_PATH";
    static final String MESSAGE_CONTENT_KEY = "MESSAGE_CONTENT";
    static final String MESSAGE_TYPE_KEY="MESSAGE_TYPE";
    static final String MESSAGE_FILENAME_KEY="MESSAGE_FILENAME";
    static final String SENDER_CONFIG_FILE_KEY = "SENDER_CONFIG_FILE";
    static final String RECEIVER_CONFIG_FILE_KEY = "RECEIVER_CONFIG_FILE";
     static final String SENDER_THREADS_KEY = "SENDER_THREADS";
     static final String DELIVERY_MODE_KEY = "DELIVERY_MODE";
    static final String APP_CONFIG_FILE = "app.properties";

    //config files
    static final String JMS_CONFIG_FILE = "jms.properties";

    public static enum Role {
        Receiver(RECEIVER_CONFIG_FILE_KEY, null), Cleaner(RECEIVER_CONFIG_FILE_KEY, CleanJMXCommand.class), Sender(Constants.SENDER_CONFIG_FILE_KEY, SendCommand.class), Monitor(RECEIVER_CONFIG_FILE_KEY, MonitorJMXCommand.class), Helper(null, null), Installer(RECEIVER_CONFIG_FILE_KEY, InstallJMXCommand.class), Uninstaller(RECEIVER_CONFIG_FILE_KEY, UninstallJMXCommand.class), StoreAdmin(null, StoreAdminCommand.class);

        private String configFileKey;
        private Class commandClass;

        Role(String configFileKey, Class commandClass) {

            this.configFileKey = configFileKey;
            this.commandClass = commandClass;
        }

        public String getConfigFileKey() {

            return configFileKey;
        }

        public Class getCommandClass() {

            return commandClass;
        }

        public static Role getRole(String optArg) {

            switch (optArg) {
                case "r":
                    return Receiver;
                case "s":
                    return Sender;
                case "c":
                    return Cleaner;
                case "a":
                    return StoreAdmin;
                case "u":
                    return Uninstaller;
                case "i":
                    return Installer;
                case "m":
                    return Monitor;
                default:
                    return Helper;
            }
        }
    }
}