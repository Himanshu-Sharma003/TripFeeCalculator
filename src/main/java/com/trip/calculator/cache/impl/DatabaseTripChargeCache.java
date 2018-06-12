package com.trip.calculator.cache.impl;

import java.util.Set;

import com.trip.calculator.cache.ITripChargeCache;

/**
 * Class for implementing Database caching. Currently Database caching is not in
 * scope ; hence proper implementation for methods are not provided.
 */

/**
 * @author Himanshu Sharma
 *
 */
public class DatabaseTripChargeCache implements ITripChargeCache {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#get(java.lang.String)
	 */
	@Override
	public Object getTripCharge(final String key1, String key2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#containsKey(java.lang.String)
	 */
	@Override
	public Boolean containsKey(final String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#getAllKeys()
	 */
	@Override
	public Set<String> getAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMaxTripCharge(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
