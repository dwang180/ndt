package com.hmh.edtech.utility.networkDiagnostic;

import java.util.StringTokenizer;

public class NetworkTargetImpl extends AbstractTarget implements INetworkTarget{
	private String url;
	private int port = 80;
	private String method = "GET";
	
	public NetworkTargetImpl(String compositeTargetSpec, String targetSpec)
	{
		super(compositeTargetSpec, targetSpec);
		parseTarget();
	}

	public NetworkTargetImpl(String targetSpec)
	{
		super(targetSpec);
		parseTarget();
	}

	private void parseTarget()
	{
		int index = 0;
		StringTokenizer t = new StringTokenizer(getTargetSpec(), ":");
		while (t.hasMoreTokens() )
		{
			String token = t.nextToken();			
			if (index == 0)
				url = token;
			else if (index == 1)
				port = Integer.parseInt(token);
			else if (index == 2)
				method = token;
			index++;
		}
	}
	
	public String getURL()
	{
		return url;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public String getMethod()
	{
		return method;
	}
}
