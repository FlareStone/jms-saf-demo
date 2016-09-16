package me.yekki.demo.jms;

public interface Constants {

    //keys
    static final String PRINCIPAL_KEY = "PRINCIPAL";
    static final String CREDENTIAL_KEY = "CREDENTIAL";
    static final String PROVIDER_URL_KEY = "PROVIDER_URL";
    static final String CONNECTON_FACTORY_KEY = "CONNECTION_FACTORY";
    static final String DESTINATION_KEY = "DESTINATION";

    static final String BATCH_INTERVAL_IN_MILLIS_KEY = "BATCH_INTERVAL_IN_MILLIS";
    static final String MESSAGE_SIZE_KEY = "MESSAGE_SIZE";
    static final String MESSAGE_CONTENT_KEY = "MESSAGE_CONTENT";
    static final String MESSAGE_TYPE_KEY="MESSAGE_TYPE";
    static final String MESSAGE_FILENAME_KEY="MESSAGE_FILENAME";
    static final String SENDER_CONFIG_FILE_KEY = "SENDER_CONFIG_FILE";
    static final String RECEIVER_CONFIG_FILE_KEY = "RECEIVER_CONFIG_FILE";
    static final String COMPRESSION_THRESHOLD_KEY = "COMPRESSION_THRESHOLD";
    static final String SENDER_THREADS_KEY = "SENDER_THREADS";
    static final String CLEANER_THREADS_KEY = "CLEANER_THREADS";
    static final String DELIVERY_MODE_KEY = "DELIVERY_MODE";
    static final String APP_CONFIG_FILE = "app.properties";

    //config files
    static final String JMS_CONFIG_FILE = "jms.properties";
}