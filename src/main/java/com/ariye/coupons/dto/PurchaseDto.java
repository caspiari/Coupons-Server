package com.ariye.coupons.dto;

import java.sql.Timestamp;


public class PurchaseDto {

	private long id;
	private long userId;
	private long couponId;
	private String couponName;
	private int amount;
	private Timestamp timestamp;
	
	public PurchaseDto(long id, int amount) {
		super();
		this.id = id;
		this.amount = amount;
	}

	public PurchaseDto() {
	}
	
	/**
	 * - Full ctor
	 */
	public PurchaseDto(long id, long userId, long couponId, String couponName, int amount, Timestamp timestamp) {
		this(userId, couponName, amount, timestamp);
		this.id = id;
		this.couponId = couponId;
	}
	/**
	 * - Ctor for 'get' method for customer
	 * @param couponName
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(String couponName, int amount, Timestamp timestamp) {
		this.couponName = couponName;
		this.amount = amount;
		this.timestamp = timestamp;
	}
	/**
	 * - Ctor for 'get' method for company and admin
	 * @param userId
	 * @param couponName
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(long userId, String couponName, int amount, Timestamp timestamp) {
		this(couponName, amount, timestamp);
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PurchaseDto [id=" + id + ", userId=" + userId + ", couponId=" + couponId + ", couponName=" + couponName
				+ ", amount=" + amount + ", timestamp=" + timestamp + "]";
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
