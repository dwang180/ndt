package com.hmh.edtech.utility.networkDiagnostic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;

@Singleton
public class NewNetworkAwareConfigurationImpl implements IConfiguration {
	@Inject
	private Logger logger;
	
	@Inject NetworkTargetBuilder targetBuilder;
	
	private Collection<INetworkTarget> nts = new ArrayList<INetworkTarget>();
	private Collection<IBrowserTarget> bts = new ArrayList<IBrowserTarget>();
	private int timeout = 3; // in seconds. default: 3 seconds
	private int threads = 1;
	
	public NewNetworkAwareConfigurationImpl()
	{}
	
	public void init(String[] targets) {
		for (String t : targets)
		{
			if (!t.isEmpty())
			{
				this.addTarget(t);
			}
		}
	}

	public void init(String configFilePath) throws IOException
	{
		String line = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath)));
		
		try
		{
			line = reader.readLine();
			while (line != null)
			{				
				line = line.trim();
				if (line.isEmpty())
				{
					line = reader.readLine();
					continue;
				}
				
				addTarget(line);
				line = reader.readLine();
			}
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException ie)
				{
					logger.error("Closing file reader on configurationn file failed", ie);
				}
			}
		}
	}
	
	private void addTarget(String targetSpec) {
		String ts = targetSpec.trim();
		if (!ts.startsWith("#"))
		{
			if (ts.startsWith("b:") || ts.startsWith("browser:"))
			{
				bts.add(new BrowserTargetImpl(ts));
			}
			else if (ts.startsWith("t") || ts.startsWith("timeout"))
			{
				int offset = ts.indexOf("=");
				String val = ts.substring(offset + 1);
				if (val == null || val.isEmpty())
				{
					logger.error("Timeout settings invalid format");
					throw new RuntimeException("Timeout settings invalid format");
				}
				timeout = Integer.parseInt(val);
			}
			else
			{
				// use the network target builder here
				nts.addAll(targetBuilder.withSpec(ts).build());
				targetBuilder.reset();
			}
		}
	}

	public void printConfiguration()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Configuration: \n");
		sb.append("\t timeout:" + this.timeout + " seconds\n");
		sb.append("\t number of threads:" + this.threads + "\n");
		sb.append("\t network targets to check for connectivity:\n");
		for (INetworkTarget target : this.getNetworkTargets())
		{
			sb.append("\t\t " + target.getTargetSpec() + "\n");
		}
		logger.info(sb.toString());
	}
	
	public Collection<INetworkTarget> getNetworkTargets() {
		return this.nts;
	}

	public Collection<IBrowserTarget> getBrowserTargets() {
		return this.bts;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setThreads(int threads)
	{
		this.threads = threads;
	}
	
	public int getThreads() {
		return threads;
	}
}
