package com.trip.calculator.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trip {
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd HH:mm:ss";
	// Passenger Id
	private String passengerId;
	// Travel Start date and time
	private Date startDate;
	// Travel End date and time
	private Date endDate;
	// Start travel Stop Id
	private String fromStopId;
	// Travel ends Stop Id
	private String toStopId;
	// Charge
	private BigDecimal chargeAmount;
	// Trip State i.e COMPLETED, INCOMPLETED
	private String status;

	public Trip() {
		super();
	}

	public Trip(String passengerId, Date startDate, String fromStopId, String status) {
		super();
		this.passengerId = passengerId;
		this.startDate = startDate;
		this.fromStopId = fromStopId;
		this.status = status;
	}

	public Trip(String passengerId, Date endDate, String toStopId) {
		super();
		this.passengerId = passengerId;
		this.endDate = endDate;
		this.toStopId = toStopId;
		this.status = "INCOMPLETE";
	}

	/**
	 * @return the passangerId
	 */
	public String getPassengerId() {
		return passengerId;
	}

	/**
	 * @param passangerId
	 *            the passangerId to set
	 */
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the fromStopId
	 */
	public String getFromStopId() {
		return fromStopId;
	}

	/**
	 * @param fromStopId
	 *            the fromStopId to set
	 */
	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}

	/**
	 * @return the toStopId
	 */
	public String getToStopId() {
		return toStopId;
	}

	/**
	 * @param toStopId
	 *            the toStopId to set
	 */
	public void setToStopId(String toStopId) {
		this.toStopId = toStopId;
	}

	/**
	 * @return the chargeAmount
	 */
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	/**
	 * @param chargeAmount
	 *            the chargeAmount to set
	 */
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(startDate) + " , "
				+ new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(endDate) + " , " + fromStopId + " , " + toStopId
				+ " , $" + chargeAmount + " , " + passengerId + " , " + status;
	}

}
