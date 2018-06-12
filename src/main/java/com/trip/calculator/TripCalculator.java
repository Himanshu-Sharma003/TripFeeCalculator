package com.trip.calculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

import com.trip.calculator.cache.TripChargeCacheManager;
import com.trip.calculator.cache.impl.PropertiesTripChargeCache;
import com.trip.calculator.commons.CSVUtils;
import com.trip.calculator.commons.LoggingHelper;
import com.trip.calculator.commons.exception.IncorrectParameterException;
import com.trip.calculator.model.MaxCharge;
import com.trip.calculator.model.Trip;
import com.trip.calculator.cache.impl.ConfigurationCache;

/**
 *  This is the initiator class for the Trip Calculator. In this class trip charges
 *   for traveling between 2 station is calculated.
 */

/**
 * @author Himanshu Sharma
 */

public class TripCalculator {
	// Status Cancelled
	private static final String CANCELLED = "CANCELLED";
	// Status Incomplete
	private static final String INCOMPLETE = "INCOMPLETE";
	// Status Completed
	private static final String COMPLETED = "COMPLETED";
	// Constant
	private static final String ON = "ON";
	// Header Row
	private static final String HEADER_ROW = ConfigurationCache.getInstance().get("header.row");
	// Output File Location
	private static final String OUTPUT_TRIPS_CSV = ConfigurationCache.getInstance().get("output.file");
	// Input File location
	private static final String INPUT_TAPS_CSV = ConfigurationCache.getInstance().get("input.file");
	// Date Formate
	private static final String DD_MM_YYYY_HH_MM_SS = ConfigurationCache.getInstance().get("date.format");

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Initializing Log setting
		LoggingHelper.setUp();
		try {
			processTrips();
		} catch (IncorrectParameterException e) {
			LoggingHelper.log(TripCalculator.class.getName(), Level.SEVERE, "main", e.getMessage());
		} catch (Exception e) {
			LoggingHelper.log(TripCalculator.class.getName(), Level.SEVERE, "main",
					String.format("Unexpected error occured : %s", e.getMessage()));
		}
	}

	/**
	 * This method reads the Taps.csv and parse individual records.
	 * 
	 * @throws ParseException
	 * @throws IncorrectParameterException
	 */
	private static void processTrips() throws ParseException, IncorrectParameterException {
		Map<String, Trip> tripMap = new HashMap<>();
		LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.FINEST, "processTrips",
				"Processing Taps file");
		// Read input file
		URL resource = Thread.currentThread().getContextClassLoader().getResource(INPUT_TAPS_CSV);
		boolean firstLine = true;
		Scanner scanner = null;
		try {
			if (resource == null) {
				throw new IllegalArgumentException("Invalid Input File.");
			}
			scanner = new Scanner(new File(resource.toURI()));
			while (scanner.hasNext()) {
				if (firstLine) {
					scanner.nextLine();
					firstLine = false;
					continue;
				}
				List<String> line = CSVUtils.parseLine(scanner.nextLine());
				// Process individual Row.
				processRow(tripMap, line);
			}
			writeRecords(tripMap);
		} catch (URISyntaxException | IOException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		} finally {
			if (scanner != null)
				scanner.close();
		}

		LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.FINEST, "processTrips",
				"Taps file processed successfully");
	}

	/**
	 * This method processes each row by populating trip data into Model Obj
	 * (i.e. Trip) and tries to match trips to drive trip status.
	 * 
	 * @param tripMap:
	 *            Master Map , contains trips details as value and PassengerId
	 *            as key.
	 * @param line
	 *            : Parse row from input file.
	 * @throws ParseException
	 * @throws IncorrectParameterException
	 */
	public static void processRow(Map<String, Trip> tripMap, List<String> line)
			throws ParseException, IncorrectParameterException {
		if (line.size() < 5) {
			throw new IllegalStateException("Insufficient columns for Reference data");
		}
		Trip trip = tripMap.get(line.get(4).trim());
		// If trip is not created.
		if (trip == null) {
			if (ON.equalsIgnoreCase(line.get(2).trim())) {
				trip = new Trip(line.get(4).trim(), new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).parse(line.get(1).trim()),
						line.get(3).trim(), INCOMPLETE);
				tripMap.put(trip.getPassengerId().trim(), trip);
			} else {
				trip = new Trip(line.get(4).trim(), new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).parse(line.get(1).trim()),
						line.get(3).trim());
				tripMap.put(trip.getPassengerId().trim(), trip);
			}
		} else {
			if (ON.equalsIgnoreCase(line.get(2).trim())) {
				trip.setStartDate(new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).parse(line.get(1).trim()));
				trip.setFromStopId(line.get(3).trim());
				trip.setStatus(COMPLETED);
			} else {
				trip.setEndDate(new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).parse(line.get(1).trim()));
				trip.setToStopId(line.get(3).trim());
				trip.setStatus(COMPLETED);
			}
			BigDecimal maxChargeFromStation = TripChargeCacheManager.getTripCharge(trip.getFromStopId(),
					trip.getToStopId());
			if (maxChargeFromStation == null) {
				throw new IncorrectParameterException(String.format("Invalid from station %s or to station %s",
						trip.getFromStopId(), trip.getToStopId()));
			} else {
				trip.setChargeAmount(maxChargeFromStation);
				if (maxChargeFromStation.compareTo(new BigDecimal(0)) == 0) {
					trip.setStatus(CANCELLED);
				}
			}

		}
	}

	/**
	 * This method dumps the processed trips to output file.
	 * 
	 * @param tripMap
	 *            : Master Map , contains trips details as value and PassengerId
	 *            as key.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static void writeRecords(Map<String, Trip> tripMap) throws IOException, URISyntaxException {
		URL resource = Thread.currentThread().getContextClassLoader().getResource("output/");
		File file = new File(resource.getPath() + OUTPUT_TRIPS_CSV);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileWriter writer = new FileWriter(file.getPath());
		// Writing first record
		writer.append(HEADER_ROW).append("\n");
		LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.INFO, "writeRecords",
				HEADER_ROW);
		for (String key : tripMap.keySet()) {
			Trip trip = tripMap.get(key);
			if (INCOMPLETE.equalsIgnoreCase(trip.getStatus())) {
				MaxCharge charge = TripChargeCacheManager.getMaxTripCharge(trip.getFromStopId());
				trip.setChargeAmount(charge.getCharge());
				trip.setToStopId(charge.getToStation());
				// Setting End as Start Date for Incomplete Trip.
				trip.setEndDate(trip.getStartDate());
			}
			writer.append(tripMap.get(key).toString() + "\n");
			LoggingHelper.log(PropertiesTripChargeCache.class.getName(), Level.INFO, "writeRecords",
					tripMap.get(key).toString());
		}
		writer.close();
	}
}
