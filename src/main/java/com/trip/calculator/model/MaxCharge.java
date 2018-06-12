package com.trip.calculator.model;

import java.math.BigDecimal;

public class MaxCharge {
	/** From Station */
	String fromStation;
	/** To station */
	String toStation;
	/** Charge traveling between fromStation to toStation. */
	BigDecimal charge;

	public MaxCharge() {
		super();
	}

	public MaxCharge(String toStation, BigDecimal charge) {
		super();
		this.toStation = toStation;
		this.charge = charge;
	}

	/**
	 * @return the fromStation
	 */
	public String getFromStation() {
		return fromStation;
	}

	/**
	 * @param fromStation
	 *            the fromStation to set
	 */
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	/**
	 * @return the toStation
	 */
	public String getToStation() {
		return toStation;
	}

	/**
	 * @param toStation
	 *            the toStation to set
	 */
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	/**
	 * @return the charge
	 */
	public BigDecimal getCharge() {
		return charge;
	}

	/**
	 * @param charge
	 *            the charge to set
	 */
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

}
