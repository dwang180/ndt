package com.hmh.edtech.utility.networkDiagnostic;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Network Diagnostic Tool Main
 *
 */
public class NDT {
	private static final Logger logger = LogManager
			.getLogger("Network Diagnostic Tool");

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("h", "help", false, "print this message.");
		options.addOption(
				Option.builder("n")
				.hasArg()
				.longOpt("numberthreads")
				.desc("number of threads to run the tool concurrently. Default 1 thread.")
				.build());
		options.addOption(Option
				.builder("t")
				.hasArg()
				.argName("seconds")
				.desc("time in seconds to wait before giving up establishing a connection to a network host. If it is not specified, 3 seconds will be the default value.")
				.longOpt("timeout").required(false).build());
		options.addOption(Option
				.builder("c")
				.argName("config file path")
				.hasArg()
				.desc("configuration file path. If it is not specified through this option, the default one conf.txt in the directory conf is used.")
				.longOpt("config").build());
		options.addOption(Option
				.builder("s")
				.argName("site1, site2, ...")
				.hasArgs()
				.valueSeparator(',')
				.desc("site(s) seperated by comma to test for connnectivity, e.g. sam-cdn.education.scholastic.com:80, www.google.com, etc.. If it is not specified, the ones defined in the conf.txt file will be tested.")
				.longOpt("site").build());

		CommandLineParser parser = new DefaultParser();
		CommandLine cl = null;
		try {
			cl = parser.parse(options, args);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return;
		}

		if (cl.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ndt", options);
			return;
		}

		String userDir = System.getProperty("user.dir");
		String confPath = userDir + File.separator + "conf" + File.separator
				+ "conf.txt";
		logger.info("Default configuration file path " + confPath);

		if (cl.hasOption("c")) {
			confPath = cl.getOptionValue("c");
			logger.info("Set configuration file path to " + confPath);
		}

		IConfiguration conf = WeldContext.INSTANCE.getBean(IConfiguration.class);
		ConnectivityChecker checker = WeldContext.INSTANCE.getBean(ConnectivityChecker.class);
		try {
			if (cl.hasOption("s"))
			{
				logger.info("Initialize the application with sites from the command line");
				conf.init(cl.getOptionValues("s"));
			}
			else
			{
				logger.info("Initialize the application with sites from the configuration file");
				conf.init(confPath);
			}
			if (cl.hasOption("t"))
				conf.setTimeout(Integer.parseInt(cl.getOptionValue("t")));
			if (cl.hasOption("n"))
				conf.setThreads(Integer.parseInt(cl.getOptionValue("n")));
			
			conf.printConfiguration();
			checker.checkConnectivityOnNetworkTargetsUsingSocket();
			logger.info("Completed network diagnostic scanning.");
		} catch (Exception e) {
			logger.error("Unexpected error!", e);
		}
	}
}
