package com.ariye.coupons.dto;

import java.sql.Timestamp;


public class PurchaseDto {

	private long id;
	private long userId;
	private long couponId;
	private int amount;
	private Timestamp timestamp;
	
	/**
	 * - Full ctor
	 */
	public PurchaseDto(long id, long userId, long couponId, int amount, Timestamp timestamp) {
		this(userId, couponId, amount);
		this.id = id;
		this.timestamp = timestamp;
	}
	
	public PurchaseDto(long userId, long couponId, int amount) {
		this.userId = userId;
		this.couponId = couponId;
		this.amount = amount;
	}

	public PurchaseDto() {
		super();
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
