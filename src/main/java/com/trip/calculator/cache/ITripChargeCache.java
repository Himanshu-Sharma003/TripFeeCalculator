package com.trip.calculator.cache;

import java.util.Set;

/**
 * Interface for Caching.
 *
 * @author Himanshu Sharma.
 */
public interface ITripChargeCache {

	/**
	 * Fetch trip charges between point A to point B.
	 * 
	 * @param key1: Station A
	 * @param key2: Station B
	 * @return
	 */
	Object getTripCharge(String key1, String key2);
	
	/**
	 * Fetch Maximum trip changes from the given station.
	 * @param key : Station 
	 * @return
	 */
	Object getMaxTripCharge(String key);

	/**
	 * Checks if Key exists in given cache.
	 *
	 * @param key
	 *            : Key
	 * @return the boolean
	 */
	Boolean containsKey(String key);

	/**
	 * Collection of all the keys from cache.
	 *
	 * @return the all keys
	 */
	Set<String> getAllKeys();

}