package com.trip.calculator.common;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable or disable the test case execution.
 */
/**
 * @author Himanshu Sharma
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {

	/**
	 * Enables or disables the test method execution.
	 *
	 * @return true, if successful
	 */
	// should ignore this test?
	public boolean enabled() default true;

}