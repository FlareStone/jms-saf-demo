package me.yekki.jms.cmd;

import me.yekki.JMSClientException;
import me.yekki.jms.AppConfig;
import me.yekki.jms.JMXCommand;
import me.yekki.jmx.utils.JMXWrapper;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MonitorJMXCommand extends JMXCommand {

    public MonitorJMXCommand(AppConfig config) {

        super(config);
        super.init(false, true);
    }

    @Override
    public void execute() throws JMSClientException {

        try {
            String m = config.getProperty("MONITOR", "");
            String[] meta = m.split(":");

            if (null != meta && meta.length >= 1) {

                String cz = meta[0];
                String methodStr = cz.substring(cz.lastIndexOf(".") + 1);
                String clazzStr = cz.substring(0, cz.lastIndexOf("."));

                Class clazz = Class.forName(clazzStr);

                Constructor constructor = clazz.getConstructor(JMXWrapper.class, PrintStream.class);
                Object monitor = constructor.newInstance(jmxWrapper, System.out);

                String[] args = (meta.length > 1) ? meta[1].split("_") : null;

                Class[] argsClass = new Class[args.length];

                for (int i = 0; i < argsClass.length; i++) {
                    argsClass[i] = String.class;
                }

                Method method = clazz.getMethod(methodStr, argsClass);

                method.invoke(monitor, args);
            }
        }
        catch (Exception e) {

            throw new JMSClientException("Failed to execute monitor command:" + e.getMessage());
        }
    }
}
