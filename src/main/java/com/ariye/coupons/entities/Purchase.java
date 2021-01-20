package com.ariye.coupons.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "purchases")
@SuppressWarnings("serial")
public class Purchase implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Coupon coupon;

	private int amount;

	@Column(name = "time_stamp")
	private Timestamp timestamp;

	/*
	 * - Full ctor
	 */
	public Purchase(Long id, User user, Coupon coupon, int amount, Timestamp timestamp) {
		this.id = id;
		this.user = user;
		this.coupon = coupon;
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public Purchase() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Coupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
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

	@Override
	public String toString() {
		return "\nPurchase [id=" + id + ", user=" + user.getId() + ", coupon=" + coupon.getName() + ", amount=" + amount
				+ ", timestamp=" + timestamp + "]";
	}

}
