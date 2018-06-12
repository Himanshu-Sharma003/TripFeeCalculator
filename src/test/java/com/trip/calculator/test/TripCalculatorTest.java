package com.trip.calculator.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trip.calculator.TripCalculator;
import com.trip.calculator.common.Test;
import com.trip.calculator.commons.CSVUtils;
import com.trip.calculator.model.Trip;

public class TripCalculatorTest {
	@Test
	public void test_no_quote() {

		String line = "Stop1,Stop2,3.5";
		List<String> result = CSVUtils.parseLine(line);

		if (result == null || result.isEmpty()) {
			throw new AssertionError("Parse Result is empty");
		}
		if (result.size() != 3) {
			throw new AssertionError("Incorrect Parse Record");
		}
		if (!"Stop1".equalsIgnoreCase(result.get(0)) || !"Stop2".equalsIgnoreCase(result.get(1))
				|| !"3.5".equalsIgnoreCase(result.get(2))) {
			throw new AssertionError("Incorret Parse Result");
		}

	}

	@Test
	public void test_single_quote() throws Exception {

		String line = "Stop'1,Stop2,3.5";
		List<String> result = CSVUtils.parseLine(line);

		if (result == null || result.isEmpty()) {
			throw new AssertionError("Parse Result is empty");
		}
		if (result.size() != 3) {
			throw new AssertionError("Incorrect Parse Record");
		}
		if (!"Stop'1".equalsIgnoreCase(result.get(0)) || !"Stop2".equalsIgnoreCase(result.get(1))
				|| !"3.5".equalsIgnoreCase(result.get(2))) {
			throw new AssertionError("Incorret Parse Result");
		}

	}

	@Test
	public void test_completed_trip() throws Exception {

		String line = "1, 22-01-2018 13:00:00, ON, Stop1, 12345";
		List<String> row = CSVUtils.parseLine(line);
		Map<String, Trip> tripMap = new HashMap<>();
		TripCalculator.processRow(tripMap, row);
		line = "3, 22-01-2018 13:05:00, OFF, Stop2, 12345";
		row = CSVUtils.parseLine(line);
		TripCalculator.processRow(tripMap, row);
		if (tripMap == null || tripMap.isEmpty()) {
			throw new AssertionError("Trip not Parsed.");
		}
		for (String key : tripMap.keySet()) {
			Trip trip = tripMap.get(key);

			if (trip == null || trip.getStatus() == null || !"COMPLETED".equalsIgnoreCase(trip.getStatus())) {
				throw new AssertionError("Exception:  Empty or Incorrect trip Status.");
			}
		}

	}
	
	@Test
	public void test_cancelled_trip() throws Exception {

		String line = "5, 22-01-2018 13:15:00, ON, Stop1, 131313";
		List<String> row = CSVUtils.parseLine(line);
		Map<String, Trip> tripMap = new HashMap<>();
		TripCalculator.processRow(tripMap, row);
		line = "6, 22-01-2018 13:15:05, OFF, Stop1, 131313";
		row = CSVUtils.parseLine(line);
		TripCalculator.processRow(tripMap, row);
		if (tripMap == null || tripMap.isEmpty()) {
			throw new AssertionError("Trip not Parsed.");
		}
		for (String key : tripMap.keySet()) {
			Trip trip = tripMap.get(key);

			if (trip == null || trip.getStatus() == null || !"CANCELLED".equalsIgnoreCase(trip.getStatus())) {
				throw new AssertionError("Exception:  Empty or Incorrect trip Status.");
			}
		}
	}
	
	@Test
	public void test_incomplete_trip() throws Exception {

		String line = "7, 22-01-2018 13:17:00, ON, Stop2, 7777777";
		List<String> row = CSVUtils.parseLine(line);
		Map<String, Trip> tripMap = new HashMap<>();
		TripCalculator.processRow(tripMap, row);
		if (tripMap == null || tripMap.isEmpty()) {
			throw new AssertionError("Trip not Parsed.");
		}
		for (String key : tripMap.keySet()) {
			Trip trip = tripMap.get(key);

			if (trip == null || trip.getStatus() == null || !"INCOMPLETE".equalsIgnoreCase(trip.getStatus())) {
				throw new AssertionError("Exception:  Empty or Incorrect trip Status.");
			}
		}

	}

}
