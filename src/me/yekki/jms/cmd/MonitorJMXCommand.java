package me.yekki.jms.cmd;

import me.yekki.jms.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.JMXCommand;
import me.yekki.jmx.utils.JMXWrapper;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class MonitorJMXCommand extends JMXCommand {

    private static Logger logger = Logger.getLogger(MonitorJMXCommand.class.getName());


    public MonitorJMXCommand(AppConfig config) {

        super(config);
        super.init(false, true);
    }

    public void execute() {

        config.getMonitors().forEach((m)->{
            invoke(jmxWrapper, m);
        });
    }

    public void invoke(JMXWrapper jmxWrapper, String monitor) {

        try {
            String[] meta = monitor.split(":");

            if (null != meta && meta.length >= 1) {

                String cz = meta[0];
                String methodStr = cz.substring(cz.lastIndexOf(".") + 1);
                String clazzStr = cz.substring(0, cz.lastIndexOf("."));

                Class clazz = Class.forName(clazzStr);

                Constructor constructor = clazz.getConstructor(JMXWrapper.class, PrintStream.class);
                Object inst = constructor.newInstance(jmxWrapper, System.out);

                String[] args = (meta.length > 1) ? meta[1].split("_") : null;

                Class[] argsClass = new Class[args.length];

                for (int i = 0; i < argsClass.length; i++) {
                    argsClass[i] = String.class;
                }

                Method method = clazz.getMethod(methodStr, argsClass);

                method.invoke(inst, args);
            }
        }
        catch (Exception e) {

            logger.info("Failed to execute monitor command:" + e.getMessage());
        }
    }
}
