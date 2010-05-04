package org.opennms.core.utils;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;

public class LogUtils {
    public static void tracef(final Object logee, final String format, final Object... args) {
        tracef(logee, null, format, args);
    }

    public static void tracef(final Object logee, final Throwable throwable, final String format, final Object... args) {
        Logger log = getLogger(logee);
        if (log.isTraceEnabled()) {
            if (throwable == null) {
                log.trace(String.format(format, args));
            } else {
                log.trace(String.format(format, args), throwable);
            }
        }
    }

    public static void debugf(final Object logee, final String format, final Object... args) {
        debugf(logee, null, format, args);
    }

    public static void debugf(final Object logee, final Throwable throwable, final String format, final Object... args) {
        Logger log = getLogger(logee);
        if (log.isDebugEnabled()) {
            if (throwable == null) {
                log.debug(String.format(format, args));
            } else {
                log.debug(String.format(format, args), throwable);
            }
        }
    }

    public static void infof(final Object logee, final String format, final Object... args) {
        infof(logee, null, format, args);
    }

    public static void infof(final Object logee, final Throwable throwable, final String format, final Object... args) {
        Logger log = getLogger(logee);
        if (log.isInfoEnabled()) {
            if (throwable == null) {
                log.info(String.format(format, args));
            } else {
                log.info(String.format(format, args), throwable);
            }
        }
    }

    public static void warnf(final Object logee, final String format, final Object... args) {
        warnf(logee, null, format, args);
    }

    public static void warnf(final Object logee, final Throwable throwable, final String format, final Object... args) {
        Logger log = getLogger(logee);
        if (log.isWarnEnabled()) {
            if (throwable == null) {
                log.warn(String.format(format, args));
            } else {
                log.warn(String.format(format, args), throwable);
            }
        }
    }

    public static void errorf(final Object logee, final String format, final Object... args) {
        errorf(logee, null, format, args);
    }

    public static void errorf(final Object logee, final Throwable throwable, final String format, final Object... args) {
        Logger log = getLogger(logee);
        if (log.isErrorEnabled()) {
            if (throwable == null) {
                log.error(String.format(format, args));
            } else {
                log.error(String.format(format, args), throwable);
            }
        }
    }

    public static void fatalf(final Object logee, final String format, final Object... args) {
        errorf(logee, null, format, args);
    }

    public static void fatalf(final Object logee, final Throwable throwable, final String format, final Object... args) {
        errorf(logee, null, format, args);
    }

    public static void logToConsole() {
    	final Properties logConfig = new Properties();
    	logConfig.setProperty("log4j.reset", "true");
    	logConfig.setProperty("log4j.rootCategory", "INFO, CONSOLE");
    	logConfig.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
    	logConfig.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
    	logConfig.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d %-5p [%t] %c: %m%n");
    	PropertyConfigurator.configure(logConfig);
    }

    public static void logToFile(final String file) {
    	final Properties logConfig = new Properties();
    	logConfig.setProperty("log4j.reset", "true");
    	logConfig.setProperty("log4j.rootCategory", "INFO, FILE");
    	logConfig.setProperty("log4j.appender.FILE", "org.apache.log4j.RollingFileAppender");
    	logConfig.setProperty("log4j.appender.FILE.MaxFileSize", "100MB");
    	logConfig.setProperty("log4j.appender.FILE.MaxBackupIndex", "4");
    	logConfig.setProperty("log4j.appender.FILE.File", file);
    	logConfig.setProperty("log4j.appender.FILE.layout", "org.apache.log4j.PatternLayout");
    	logConfig.setProperty("log4j.appender.FILE.layout.ConversionPattern", "%d %-5p [%t] %c: %m%n");
    	PropertyConfigurator.configure(logConfig);
    }
    
	public static void enableDebugging() {
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.ALL);
	}

	private static Logger getLogger(final Object logee) {
        Logger log;
        if (logee instanceof String) {
            log = ThreadCategory.getSlf4jInstance((String)logee);
        } else if (logee instanceof Class<?>) {
            log = ThreadCategory.getSlf4jInstance((Class<?>)logee);
        } else {
            log = ThreadCategory.getSlf4jInstance(logee.getClass());
        }
        return log;
    }

}
