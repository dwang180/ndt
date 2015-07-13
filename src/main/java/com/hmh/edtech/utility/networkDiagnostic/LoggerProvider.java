package com.hmh.edtech.utility.networkDiagnostic;

import javax.enterprise.inject.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.enterprise.inject.spi.InjectionPoint;;

public class LoggerProvider {

	@Produces
	public Logger getLogger(InjectionPoint point)
	{
		Logger logger = LogManager.getLogger(point.getMember().getDeclaringClass().getSimpleName());
		return logger;
	}
}
