package com.trip.calculator.commons;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Utility Class contain helper methods.
 */

/**
 * @author Himanshu Sharma
 *
 */
public class LoggingHelper {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(LoggingHelper.class.getName());

	/**
	 * Log messages on Log Handler.
	 *
	 * @param className
	 *            : Class Name
	 * @param level
	 *            : Logging Level
	 * @param methodName
	 *            : Method Name
	 * @param message
	 *            : Message
	 */
	public static void log(final String className, final Level level, final String methodName, final String message) {
		if (LOGGER.isLoggable(level)) {
			LOGGER.logp(level, className, methodName, message);
		}
	}

	/**
	 * SetUp Method, loads log configuration.
	 */
	public static void setUp() {
		try {
			LogManager.getLogManager().readConfiguration(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties"));
		} catch (SecurityException | IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

}
