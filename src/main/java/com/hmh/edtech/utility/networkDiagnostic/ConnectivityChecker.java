package com.hmh.edtech.utility.networkDiagnostic;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

public class ConnectivityChecker {
	@Inject
	Logger logger;

	@Inject
	private IConfiguration config;

	@Inject
	private Instance<ConnectivityRunner> runnerInstance;

	public void checkConnectivityOnNetworkTargetsUsingSocket() throws Exception {

		ExecutorService pool = null;
		try {
			pool = Executors.newFixedThreadPool(config.getThreads());
			Collection<Callable<RunnerResult>> callers = new ArrayList<Callable<RunnerResult>>();

			for (INetworkTarget target : config.getNetworkTargets()) {
				ConnectivityRunner runner = runnerInstance.get();
				callers.add(runner.setTarget(target));
			}
			List<Future<RunnerResult>> fs = pool.invokeAll(callers);
			for (Future<RunnerResult> f : fs) {
				RunnerResult r = f.get();
				logger.info(r.toString());
			}
		} finally {
			if (pool != null)
				pool.shutdown();
		}
	}

	private static class RunnerResult {
		public long duration;

	}

	private static class ConnectivityRunner implements Callable<RunnerResult> {
		private INetworkTarget tar;
		@Inject
		private IConfiguration config;
		@Inject
		private Logger logger;

		public ConnectivityRunner setTarget(INetworkTarget target) {
			this.tar = target;
			return this;
		}

		public RunnerResult call() throws Exception {
			return runSocketBasedCheck();
		}

		public RunnerResult runSocketBasedCheck() {
			logger.trace("Checking [" + tar.getTargetSpec() + "]...");
			Socket s = null;
			InetAddress ias[];
			RunnerResult rr = new RunnerResult();

			Date now = new Date();
			rr.duration = now.getTime();

			try {
				ias = InetAddress.getAllByName(tar.getURL());
				if (ias.length > 1) {
					String ips = null;
					for (InetAddress ia : ias) {
						if (ips == null)
							ips = ia.getHostAddress();
						else
							ips += ", " + ia.getHostAddress();
					}
					logger.trace("[" + tar.getTargetSpec()
							+ "] has multiple IPs: " + ips);
				}
			} catch (UnknownHostException ue) {
				logger.warn("Checking  [" + tar.getTargetSpec()
						+ "]  for connectivity.......host unknown!");
				return rr;
			}

			for (InetAddress currentIP : ias) {
				try {
					if (currentIP instanceof Inet4Address) {
						logger.trace("Start checking  ["
								+ tar.getOriginalTargetSpec() + "] -> IP ["
								+ currentIP.getHostAddress() + "] on port "
								+ tar.getPort() + "....");
						InetSocketAddress addr = new InetSocketAddress(
								currentIP, tar.getPort());

						s = new Socket();
						s.connect(addr, this.config.getTimeout() * 1000);
						logger.info("Checking  [" + tar.getOriginalTargetSpec()
								+ "] -> IP [" + currentIP.getHostAddress()
								+ "] on port " + tar.getPort()
								+ " for connectivity.......good!"
								+ "\uD83D\uDC4D");
					} else
						logger.trace("Skip IP v6 address.  ["
								+ tar.getOriginalTargetSpec() + "] -> IP ["
								+ currentIP.getHostAddress() + "] "
								+ tar.getPort() + "....");

				} catch (java.net.ConnectException ce) {
					String msg = "Checking  [" + tar.getOriginalTargetSpec()
							+ "] -> IP [" + currentIP.getHostAddress()
							+ "] on port " + tar.getPort()
							+ " for connectivity.......host not reachable!";
					logger.warn(msg);
					logger.trace(msg, ce);
					;
				} catch (java.net.SocketTimeoutException ste) {
					String msg = "Checking  [" + tar.getOriginalTargetSpec()
							+ "] -> IP [" + currentIP.getHostAddress()
							+ "] on port " + tar.getPort()
							+ " for connectivity.......host not reachable!";
					logger.warn(msg);
					logger.trace(msg, ste);
				} catch (Exception e) {
					String msg = "Checking  [" + tar.getOriginalTargetSpec()
							+ "] -> IP [" + currentIP.getHostAddress()
							+ "] on port " + tar.getPort()
							+ " for connectivity.......unexpected error!";
					logger.error(msg, e);
				} finally {
					if (s != null)
						try {
							s.close();
						} catch (IOException e) {
							logger.error("Closing socket error", e);
						}
				}
			}
			return rr;
		}

		public boolean runHTTPBasedCheck() {
			boolean rv = false;
			Socket s = null;
			try {
				s = new Socket(tar.getURL(), tar.getPort());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (s != null)
					try {
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			return rv;
		}
	}
}
