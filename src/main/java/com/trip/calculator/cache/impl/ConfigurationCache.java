package com.trip.calculator.cache.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import com.trip.calculator.commons.LoggingHelper;

/**
 * Caches values stored in config.properteis file..
 */
public class ConfigurationCache{

	/**
	 * Static Instance, eager initialization.
	 **/
	private static ConfigurationCache cache = new ConfigurationCache();

	/**
	 * Holds Key/Value pair.
	 */
	private final Properties properties = new Properties();

	/**
	 * Instantiates a new properties cache.
	 */
	private ConfigurationCache() {
		super();
		loadProperties();
	}

	/**
	 * Static method that returns the instance.
	 *
	 * @return PropertiesCache
	 */
	public static ConfigurationCache getInstance() {
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#get(java.lang.String)
	 */
	public String get(final String input) {
		return properties.getProperty(input);
	}

	/**
	 * Load properties from Config.properties file.
	 */
	private void loadProperties() {
		LoggingHelper.log(ConfigurationCache.class.getName(), Level.FINEST, "loadProperties", "Loading Config.Properties File");
		InputStream inputStream = null;
		try {

			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			if (inputStream == null) {
				throw new FileNotFoundException("property file config.properties not found in the classpath");
			}
			properties.load(inputStream);
			LoggingHelper.log(ConfigurationCache.class.getName(), Level.FINEST, "loadProperties",
					"Config.Properties file loaded successfully.");
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
	}


}
