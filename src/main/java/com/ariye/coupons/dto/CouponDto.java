package com.ariye.coupons.dto;

import java.util.Date;

import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;

public class CouponDto {

	private Long id;
	private String name;
	private String description;
	private float price;
	private Date startDate;
	private Date endDate;
	private CouponType category;
	private long amount;
	private long companyId;

	public CouponDto() {
	}

	public CouponDto(Long id, String name, String description, float price, Date startDate, Date endDate,
			CouponType category, long amount, long companyId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = category;
		this.amount = amount;
		this.companyId = companyId;
	}
	
	public CouponDto(Coupon coupon) {
		this(coupon.getId(), coupon.getName(), coupon.getDescription(), coupon.getPrice(), coupon.getStartDate(),
				coupon.getEndDate(), coupon.getCategory(), coupon.getAmount(), coupon.getCompany().getId());
	}
	
	@Override
	public String toString() {
		return "CouponDto [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", category=" + category + ", amount=" + amount
				+ ", companyId=" + companyId + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

}
