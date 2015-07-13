package com.hmh.edtech.utility.networkDiagnostic;

public interface IBrowserTarget extends ITarget {
	public enum BROWSER_TYPE {IE, FIREFOX, CHROME, SAFARI};
	public BROWSER_TYPE getBrowserType();
}
