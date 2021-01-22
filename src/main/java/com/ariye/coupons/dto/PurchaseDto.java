package com.ariye.coupons.dto;

import java.util.Date;
import java.sql.Timestamp;


public class PurchaseDto {

	private long id;
	private long userId;
	private long couponId;
	private long amount;
	private Timestamp timestamp;
	private String couponName;
	private String companyName;
	private String username;
	

	public PurchaseDto() {
	}
	
	/**
	 * - Full ctor
	 * @param id
	 * @param userId
	 * @param couponId
	 * @param amount
	 * @param timestamp
	 * @param couponName
	 * @param companyName
	 * @param username
	 */
	public PurchaseDto(long id, long userId, long couponId, long amount, Timestamp timestamp, String couponName,
			String companyName, String username) {
		this.id = id;
		this.userId = userId;
		this.couponId = couponId;
		this.amount = amount;
		this.timestamp = timestamp;
		this.couponName = couponName;
		this.companyName = companyName;
		this.username = username;
	}
	
	/**
	 * - Ctor for 'get' method
	 * @param couponName
	 * @param companyName
	 * @param username
	 * @param amount
	 * @param timestamp
	 */
	public PurchaseDto(String couponName, String companyName, String username, long amount, Date timestamp) {
		this.couponName = couponName;
		this.companyName = companyName;
		this.username = username;
		this.amount = amount;
		this.timestamp = new Timestamp(timestamp.getTime());
	}
	
	/**
	 * - Ctor for 'get all' method (for admins)
	 * @param id
	 * @param userId
	 * @param couponId
	 * @param amount
	 * @param timestamp
	 * @param companyName
	 */
	public PurchaseDto(long id, long userId, long couponId, long amount, Date timestamp, String companyName) {
		super();
		this.id = id;
		this.userId = userId;
		this.couponId = couponId;
		this.amount = amount;
		this.timestamp = new Timestamp(timestamp.getTime());
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "PurchaseDto [id=" + id + ", userId=" + userId + ", couponId=" + couponId + ", amount=" + amount
				+ ", timestamp=" + timestamp + ", couponName=" + couponName + ", companyName=" + companyName
				+ ", username=" + username + "]";
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
