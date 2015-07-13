package com.hmh.edtech.utility.networkDiagnostic;

public interface INetworkTarget extends ITarget {
	public String getURL();
	public int getPort();
	public String getMethod();
}
