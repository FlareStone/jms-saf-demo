package me.yekki.demo.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.logging.Logger;

public class Utils {

    protected static Logger logger = Logger.getLogger(Utils.class.getName());

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getProperty(Properties props, String key) {

        if (props == null) return -1l;

        String value = props.getProperty(key);

        if (value == null || "".equals(value)) return -1l;

        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ne) {

            return -1l;
        }
    }

    public static long getProperty(Properties props, String key, long defaultValue) {

        long retValue = getProperty(props, key);

        if (retValue==-1l) return retValue = defaultValue;

        return retValue;
    }

    public static Properties loadProperties(String configFile) {

        File propertiesFile = new File("config/" + configFile);
        Properties props = new Properties();

        if (propertiesFile.exists()) {

            logger.info(String.format("loading properties file=[%s]", propertiesFile.getAbsolutePath()));

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

    public static String parseDuration(Duration d) {
        long h = d.toHours(); // 得到小时数
        String sh = h<10 ? "0" + h : "" + h; // 如果只有一位数，加上个0

        // 为了得到后面的分，秒，毫秒，我们要将小时减掉，否则取分钟的时候会连小时算进去
        d = d.minusHours(h);

        long min = d.toMinutes(); // 得到分钟
        String smin = min<10 ? "0" + min : "" + min;
        d = d.minusMinutes(min); // 减掉分钟
        long s = d.getSeconds(); // 得到秒，注意这里是getSeconds，没有toSeconds方法
        String ss = s<10 ? "0" + s : "" + s;
        d = d.minusSeconds(s); // 减掉秒
        long m = d.toMillis(); // 得到毫秒
        String sm = m<10 ? "00" + m : (m<100 ? "0" + m : "" + m);
        return sh + ":" + smin + ":" + ss + "," + sm;
    }


}
