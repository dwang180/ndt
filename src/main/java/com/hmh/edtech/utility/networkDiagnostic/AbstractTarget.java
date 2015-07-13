package com.hmh.edtech.utility.networkDiagnostic;


public abstract class AbstractTarget implements ITarget{
	private String originalTargetSpec;
	private String targetSpec;

	public AbstractTarget(String targetSpec) {
		super();
		this.originalTargetSpec = targetSpec;
		this.targetSpec = targetSpec;
	}

	public AbstractTarget(String original, String targetSpec) {
		super();
		this.originalTargetSpec = original;
		this.targetSpec = targetSpec;
	}

	public TargetType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTargetSpec() {
		return targetSpec;
	}

	public String getOriginalTargetSpec() {
		return this.originalTargetSpec;
	}
}