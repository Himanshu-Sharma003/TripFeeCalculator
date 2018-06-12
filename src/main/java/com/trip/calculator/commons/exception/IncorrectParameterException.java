package com.trip.calculator.commons.exception;

import com.trip.calculator.cache.TripChargeCacheManager;

/**
 * Thrown to indicate incorrect numbers of
 * operands for an operator.
 */

/**
 * @author Himanshu Sharma
 *
 */
public class IncorrectParameterException extends Exception {

	/**
	 * The Constant serialVersionUID.
	 **/
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new incorrect parameter exception.
	 *
	 * @param message
	 *            : Custom Message.
	 */
	public IncorrectParameterException(String message) {
		super(message);

	}

}
