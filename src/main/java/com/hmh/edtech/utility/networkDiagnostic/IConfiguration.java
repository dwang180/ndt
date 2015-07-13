package com.hmh.edtech.utility.networkDiagnostic;

import java.io.IOException;
import java.util.Collection;

public interface IConfiguration {
	Collection<INetworkTarget> getNetworkTargets();
	Collection<IBrowserTarget> getBrowserTargets();
	void init(String confPath)  throws IOException;
	void init(String[] targets);
	void setTimeout(int timeout);
	int getTimeout();
	void printConfiguration();
	void setThreads(int threads);
	int getThreads();
}
