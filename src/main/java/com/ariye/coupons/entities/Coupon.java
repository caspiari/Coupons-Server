package com.ariye.coupons.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ariye.coupons.enums.CouponType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coupons")
@SuppressWarnings("serial")
public class Coupon implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private float price;

	@ManyToOne(fetch = FetchType.EAGER)
	private Company company;

	private Date startDate;

	private Date endDate;

	@Enumerated(EnumType.STRING)
	private CouponType category;

	private int amount;

	@OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Purchase> purchases;

	public Coupon() {
	}

	public Coupon(String name, String description, float price, Company company, Date startDate, Date endDate,
			CouponType category, int amount) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.company = company;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = category;
		this.amount = amount;
	}

	public Coupon(long id, String name, String description, float price, Company company, Date startDate, Date endDate,
			CouponType category, int amount) {
		this(name, description, price, company, startDate, endDate, category, amount);
		this.id = id;
	}

	/*
	 * - Full ctor
	 */
	public Coupon(long id, String name, String description, float price, Company company, Date startDate, Date endDate,
			CouponType category, int amount, List<Purchase> purchases) {
		this(name, description, price, company, startDate, endDate, category, amount);
		this.id = id;
		this.purchases = purchases;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public CouponType getCategory() {
		return category;
	}

	public void setCategory(CouponType category) {
		this.category = category;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	@Override
	public String toString() {
		return "\nCoupon [id=" + id + ", company=" + company.getName() + ", name=" + name + ", description="
				+ description + ", price=" + price + ", startDate=" + startDate + ", endDate=" + endDate + ", category="
				+ category + ", amount=" + amount + "]";
	}

}
