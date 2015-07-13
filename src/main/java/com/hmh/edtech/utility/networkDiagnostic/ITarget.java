package com.hmh.edtech.utility.networkDiagnostic;

public interface ITarget {
	enum TargetType {TYPE_WEB_ßTARGET, TYPE_BROWSER};
	TargetType getType();
	// in most cases, original target spec and target spec are the same only different when the original one is a composite
	// one with wildcard or any substitution chars e.g. "129.168.1.x"
	String getOriginalTargetSpec(); 
	String getTargetSpec();
}
