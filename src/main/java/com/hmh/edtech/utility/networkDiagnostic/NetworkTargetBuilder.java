package com.hmh.edtech.utility.networkDiagnostic;

import java.util.ArrayList;
import java.util.Collection;

public class NetworkTargetBuilder {
	private String spec;
	
	// might be useful later to keep track of targets here
	private Collection<INetworkTarget> allCompositeTargets = new ArrayList<INetworkTarget>();
	private Collection<INetworkTarget> currentTargets = new ArrayList<INetworkTarget>();
	
	public NetworkTargetBuilder withSpec(String targetSpec)
	{
		spec = targetSpec;
		return this;
	}
	//"129.235.12.5-25"
	//"129.235.12.1-255 to cover the wildcard case
	
	public Collection<INetworkTarget> build()
	{
		if (spec.endsWith("*"))
		{
			// create individual targets with the same spec
			for (int i = 1; i < 256; i++)
			{
				INetworkTarget tar = new NetworkTargetImpl(spec, spec.replace("*", String.valueOf(i)));
				currentTargets.add(tar);
			}	
			allCompositeTargets.addAll(currentTargets);
		}
		else
		{
			currentTargets.add(new NetworkTargetImpl(spec));
		}
		return currentTargets;
	}

	public void reset() {
		currentTargets.clear();
	}	
	
	public Collection<INetworkTarget> getAllCompositeTarget()
	{
		return this.allCompositeTargets;
	}
}
