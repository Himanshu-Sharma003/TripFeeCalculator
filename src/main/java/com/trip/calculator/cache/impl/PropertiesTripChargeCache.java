package com.trip.calculator.cache.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;

import com.trip.calculator.cache.ITripChargeCache;
import com.trip.calculator.commons.CSVUtils;
import com.trip.calculator.commons.LoggingHelper;
import com.trip.calculator.commons.exception.IncorrectParameterException;
import com.trip.calculator.model.MaxCharge;

/**
 * Caches values stored in properties file. PropertiesCache provide access to
 * key/value by implementing Singleton design pattern.
 */
public class PropertiesTripChargeCache implements ITripChargeCache {

	/**
	 * Decimal Scale for BigDecimal.
	 **/
	private static final Integer scale = Integer.parseInt(ConfigurationCache.getInstance().get("decimal.place.precision"));

	/**
	 * Rounding mode for BigDecimal.
	 */
	private static int roundingMode = Integer.valueOf(ConfigurationCache.getInstance().get("rounding.mode.ROUND_HALF_UP"));
	/**
	 * Static Instance, eager initialization.
	 **/
	private static PropertiesTripChargeCache cache = new PropertiesTripChargeCache();

	/**
	 * Cache Charge from Station A to Station B.
	 */
	private Map<String, Map<String, BigDecimal>> stationsMap = new HashMap<>();
	
	/**
	 * Contain Maximum charge from a particular station.
	 */
	private Map<String,MaxCharge> maxChargeCache = new HashMap<>();

	/**
	 * Instantiates a new properties cache.
	 */
	private PropertiesTripChargeCache() {
		super();
		loadData();
	}

	/**
	 * Static method that returns the instance.
	 *
	 * @return PropertiesCache
	 */
	public static PropertiesTripChargeCache getInstance() {
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#get(java.lang.String)
	 */
	public BigDecimal getTripCharge(final String input1, final String input2) {
		Map<String,BigDecimal> tempMap = stationsMap.get(input1);
		// If Start and End station is same.
		if(!input1.isEmpty() && input1.equalsIgnoreCase(input2)){
			return new BigDecimal("0.00");
		}
		
		if(!tempMap.isEmpty() && tempMap.get(input2) != null) {
			return tempMap.get(input2);
		}
		// If Key not found
		 return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#containsKey(java.lang.String)
	 */
	public Boolean containsKey(final String key) {
		return stationsMap.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.calculator.cache.ICache#getAllKeys()
	 */
	public Set<String> getAllKeys() {
		return stationsMap.keySet();
	}

	/**
	 * Load properties from Config.properties file.
	 * @throws IncorrectParameterException 
	 */
	private void loadData() {
		LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.FINEST, "loadData",
				"Loading Trip Change Reference Data File");
		boolean firstLine = true;
		// Read input file
		URL resource = Thread.currentThread().getContextClassLoader().getResource("reference/tripCharges.csv");

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(resource.toURI()));
			while (scanner.hasNext()) {
				if(firstLine){
					scanner.nextLine();
					firstLine=false;
					continue;
				}
				List<String> line = CSVUtils.parseLine(scanner.nextLine());
				if (line.size() < 3) {
					throw new IllegalStateException("Insufficient columns for Reference data");
				}
				populateStationCache(line);
			}
		} catch (FileNotFoundException | URISyntaxException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		} finally {
			scanner.close();
		}

		LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.FINEST, "loadData",
				"Trip Charge Reference data file loaded successfully.");

	}
	
	/**
	 * Populates the Station Cache by processing Input String.
	 * @param input
	 */
	private void populateStationCache(List<String>input){
		String station1 = input.get(0).trim();
		String station2 =input.get(1).trim();
		BigDecimal charge = new BigDecimal(input.get(2).trim()).setScale(scale, roundingMode);
		addEntry(station1,station2,charge);
		addEntry(station2,station1,charge);
	}

	/**
	 * 
	 * @param fromStation
	 * @param toStation
	 * @param charge
	 */
	private void addEntry(String fromStation, String toStation, BigDecimal charge){
		Map<String, BigDecimal> map = stationsMap.get(fromStation);
		//populate Map
		if (map == null || map.isEmpty()) {
			map = new HashMap<>();
			map.put(toStation,charge);
			stationsMap.put(fromStation, map);
		}
		if(map.get(toStation) == null){
			map.put(toStation, charge);
		}
		MaxCharge maxCharge =  maxChargeCache.get(fromStation);
		if( maxCharge == null || maxCharge.getCharge().compareTo(charge) < 0){
			maxChargeCache.put(fromStation, new MaxCharge(toStation,charge));
		}
	}

	@Override
	public MaxCharge getMaxTripCharge(String key) {
		return maxChargeCache.get(key);
	}

}
