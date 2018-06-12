package com.trip.calculator;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.trip.calculator.common.Test;
import com.trip.calculator.test.TripCalculatorTest;

/**
 * This is test executor class, which executes test case register with the
 * executor and are enable ( Enabled/Disabled by custom @test annotation).
 *
 * @author Himanshu Sharma
 */
public class TestExecutor {

	/**
	 * Contains count of test case count ,passed , failed and ignored.
	 */
	private static Integer passed = 0, failed = 0, count = 0, ignore = 0;

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(TestExecutor.class.getName());

	/**
	 * Contains list of all Test Classes to execute.
	 */
	private static List<Class<?>> classList = new ArrayList<>();

	static {
		classList.add(TripCalculatorTest.class);
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		for (Class<?> obj : classList) {
			// Process @Test
			for (final Method method : obj.getDeclaredMethods()) {
				// if method is annotated with @Test
				if (method.isAnnotationPresent(Test.class)) {
					Annotation annotation = method.getAnnotation(Test.class);
					Test test = (Test) annotation;
					// if enabled = true (default)
					if (test.enabled()) {
						try {
							method.invoke(obj.newInstance());
							LOGGER.info(String.format("%s - Test '%s' - passed %n", ++count, method.getName()));
							passed++;
						} catch (Throwable ex) {
							LOGGER.info(String.format("%s - Test '%s' - failed: %s %n", ++count, method.getName(),
									ex.getCause()));
							failed++;
						}
					} else {
						LOGGER.info(String.format("%s - Test '%s' - ignored%n", ++count, method.getName()));
						ignore++;
					}
				}
			}
			LOGGER.info(String.format("%nResult : Total : %d, Passed: %d, Failed %d, Ignore %d%n", count, passed,
					failed, ignore));
		}
	}

}
