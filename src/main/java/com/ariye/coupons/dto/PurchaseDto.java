package com.ariye.coupons.dto;

import java.util.Date;
import java.sql.Timestamp;


public class PurchaseDto {

	private long id;
	private long userId;
	private long couponId;
	private String couponName;
	private String companyName;
	private long amount;
	private Timestamp timestamp;
	
	public PurchaseDto(long id, long amount) {
		super();
		this.id = id;
		this.amount = amount;
	}

	public PurchaseDto() {
	}
	
	/**
	 * - Full ctor
	 * @param id
	 * @param userId
	 * @param couponId
	 * @param couponName
	 * @param companyName
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(long id, long userId, long couponId, String couponName, String companyName, long amount,
			Timestamp timestamp) {
		super();
		this.id = id;
		this.userId = userId;
		this.couponId = couponId;
		this.couponName = couponName;
		this.companyName = companyName;
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	/**
	 * - Ctor for get method for customer
	 * @param couponName
	 * @param companyName
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(String couponName, String companyName, long amount, Date timestamp) {
		this.couponName = couponName;
		this.companyName = companyName;
		this.amount = amount;
		this.timestamp = new Timestamp(timestamp.getTime());
	}

	/**
	 * - Ctor for 'get' method for company and admin
	 * @param userId
	 * @param couponName
	 * @param companyName
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(long userId, String couponName, String companyName, long amount, Timestamp timestamp) {
		this(couponName, companyName, amount, timestamp);
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "PurchaseDto [id=" + id + ", userId=" + userId + ", couponId=" + couponId + ", couponName=" + couponName
				+ ", companyName=" + companyName + ", amount=" + amount + ", timestamp=" + timestamp + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
