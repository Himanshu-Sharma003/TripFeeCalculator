package com.trip.calculator.cache;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.trip.calculator.cache.impl.PropertiesTripChargeCache;
import com.trip.calculator.commons.LoggingHelper;
import com.trip.calculator.model.MaxCharge;

/**
 * Factory implementation for Cache Management.
 */
/**
 * @author Himanshu Sharma
 *
 */
public class TripChargeCacheManager {

	/**
	 * Map to hold various implementation of Caching
	 */
	private static Map<Class<?>, ITripChargeCache> caches = new HashMap<>();

	/**
	 * Returns Cache instance from Map, if instance does not exits creates one
	 * by invoking getInstance method of individual Cache implementation.
	 *
	 * @param cacheClass
	 *            : Caching Implementation Class
	 * @return the cache
	 */
	private static ITripChargeCache getCache(final Class<?> cacheClass) {
		final Boolean isICacheType = isICacheType(cacheClass);
		ITripChargeCache cache = null;
		if (isICacheType) {
			if (caches.containsKey(cacheClass)) {
				cache = caches.get(cacheClass);
			} else {
				try {
					LoggingHelper.log(TripChargeCacheManager.class.getName(), Level.FINEST, "getCache",
							"Initalizing Cache : " + cacheClass.getName());
					cache = (ITripChargeCache) cacheClass.getDeclaredMethod("getInstance").invoke(null);
					caches.put(cacheClass, cache);
					LoggingHelper.log(TripChargeCacheManager.class.getName(), Level.FINEST, "getCache",
							"Cache Initalized Successfully : " + cacheClass.getName());
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
					throw new IllegalStateException(ex.getMessage(), ex);
				}
			}
		} else {
			throw new IllegalStateException(
					String.format("Class \"%s\" not supported yet.", cacheClass.getClass().getName()));
		}
		return cache;
	}

	/**
	 * Checks if the passed class is implementing ICache interface.
	 *
	 * @param cacheClass
	 *            Class type
	 * @return the boolean
	 */
	private static Boolean isICacheType(final Class<?> cacheClass) {
		Boolean isICacheType = false;
		final Class<?>[] interfaces = cacheClass.getInterfaces();
		for (final Class<?> cacheInterface : interfaces) {
			if (cacheInterface.equals(ITripChargeCache.class)) {
				isICacheType = true;
				break;
			}
		}
		return isICacheType;
	}

	/**
	 * Get charge for fromStation to toStation for specified type of Cache
	 * 
	 * @param cacheClass
	 * @param fromStation
	 * @param toStation
	 * @return
	 */
	public static Object getTripCharge(final Class<?> cacheClass, final String fromStation, final String toStation) {
		final ITripChargeCache cache = getCache(cacheClass);
		return cache.getTripCharge(fromStation, toStation);
	}

	/**
	 * Get charge for fromStation to toStation from Properties Cache.
	 * 
	 * @param fromStation
	 * @param toStation
	 * @return
	 */
	public static BigDecimal getTripCharge(final String fromStation, final String toStation) {
		final ITripChargeCache cache = getCache(PropertiesTripChargeCache.class);
		return (BigDecimal) cache.getTripCharge(fromStation, toStation);
	}

	/**
	 * 
	 * @param cacheClass
	 * @param fromStation
	 * @return
	 */
	public static MaxCharge getMaxTripCharge(final Class<?> cacheClass, final String fromStation) {
		final ITripChargeCache cache = getCache(cacheClass);
		return (MaxCharge)cache.getMaxTripCharge(fromStation);
	}
	
	/**
	 * 
	 * @param fromStation
	 * @return
	 */
	public static MaxCharge getMaxTripCharge(final String fromStation) {
		final ITripChargeCache cache = getCache(PropertiesTripChargeCache.class);
		return (MaxCharge)cache.getMaxTripCharge(fromStation);
	}
}
