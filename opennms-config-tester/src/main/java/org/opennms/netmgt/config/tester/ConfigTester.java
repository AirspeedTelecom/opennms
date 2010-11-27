package org.opennms.netmgt.config.tester;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.opennms.core.utils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigTester implements ApplicationContextAware {
	private ApplicationContext m_context;
	private Map<String, String> m_configs;

	public Map<String, String> getConfigs() {
		return m_configs;
	}

	public void setConfigs(Map<String, String> configs) {
		m_configs = configs;
	}

	public ApplicationContext getApplicationContext() {
		return m_context;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		m_context = context;
	}

	public void testConfig(String name) {
		checkConfigNameValid(name);
		
		m_context.getBean(m_configs.get(name));
	}

	private void checkConfigNameValid(String name) {
		if (!m_configs.containsKey(name)) {
			throw new IllegalArgumentException("config '" + name + "' is not a known config name");
		}
	}

	public static void main(String[] argv) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		
		Logger.getLogger("org.springframework").setLevel(Level.WARN);
		
		ApplicationContext context = BeanUtils.getFactory("configTesterContext", ClassPathXmlApplicationContext.class);
		ConfigTester tester = context.getBean("configTester", ConfigTester.class);

		final CommandLineParser parser = new PosixParser();

		final Options options = new Options();
		options.addOption("h", "help",          false, "print this help and exit");
		options.addOption("a", "all",         	false, "check all supported configuration files");
		options.addOption("l", "list",   		false, "list supported configuration files and exit");
		options.addOption("v", "verbose", 		false, "list each configuration file as it is tested");

		final CommandLine line;
		try {
			line = parser.parse(options, argv, false);
		} catch (ParseException e) {
			System.err.println("Invalid usage: " + e.getMessage());
			System.err.println("Run 'config-tester -h' for help.");
			System.exit(1);
			
			return; // not reached; here to eliminate warning on line being uninitialized
		}

		if ((line.hasOption('l') || line.hasOption('h') || line.hasOption('a')) && line.getArgList().size() > 0) {
			System.err.println("Invalid usage: No arguments allowed when using the '-a', '-h', or '-l' options.");
			System.err.println("Run 'config-tester -h' for help.");
			System.exit(1);
		}
		
		boolean verbose = line.hasOption('v');
		
		if (line.hasOption('l')) {
			System.out.println("Supported configuration files: ");
			for (String configFile : tester.getConfigs().keySet()) {
				System.out.println("    " + configFile);
			}
			System.out.println("Note: not all OpenNMS configuration files are currently supported.");
			System.exit(0);
		}

		if (line.hasOption('h')) {
			 final HelpFormatter formatter = new HelpFormatter();
			 formatter.printHelp("config-tester -a\nOR: config-tester [config files]\nOR: config-tester -l\nOR: config-tester -h", options);
			 System.exit(0);
		}
		
		if (line.hasOption('a')) {
			for (String configFile : tester.getConfigs().keySet()) {
				tester.testConfig(configFile, verbose);
			}
			System.exit(0);
		}
		
		if (line.getArgs().length == 0) {
			System.err.println("Invalid usage: too few arguments.  Use the '-h' option for help.");
			System.exit(1);
		}
		
		for (String configFile : line.getArgs()) {
			tester.testConfig(configFile, verbose);
		}
	}

	private void testConfig(String configFile, boolean verbose) {
		if (verbose) {
			System.out.print("Testing " + configFile + " ... ");
		}
		
		long start = System.currentTimeMillis();
		testConfig(configFile);
		long end = System.currentTimeMillis();
		
		if (verbose) {
			System.out.println("OK (" + (((float) (end - start)) / 1000) + "s)");
		}
	}
}
