package me.yekki.demo.jms;

public interface Constants {

    //keys
    static final String PRINCIPAL_KEY="PRINCIPAL";
    static final String CREDENTIAL_KEY="CREDENTIAL";
    static final String PROVIDER_URL_KEY="PROVIDER_URL";
    static final String CONNECTON_FACTORY_KEY="CONNECTION_FACTORY";
    static final String DESTINATION_KEY="DESTINATION";
    static final String BATCH_INTERVAL_IN_MILLIS_KEY="BATCH_INTERVAL_IN_MILLIS";
    static final String VERBOSE_PER_MSG_COUNT_KEY="VERBOSE_PER_MSG_COUNT";

    //config files
    static final String JMS_CONFIG_FILE="jms.properties";
    static final String APP_CONFIG_FILE="app.properties";

    //default values
    static final long DEFAULT_BATCH_INTERVAL_IN_MILLIS=10;
    static final long DEFAULT_VERBOSE_PER_MSG_COUNT=10000;
}
