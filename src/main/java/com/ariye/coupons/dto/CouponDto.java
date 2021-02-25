package com.ariye.coupons.dto;

import java.util.Date;

import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;

public class CouponDto {

    private long id;
    private long companyId;
    private String companyName;
    private String name;
    private String description;
    private float price;
    private Date startDate;
    private Date endDate;
    private CouponType category;
    private long amount;

    public CouponDto() {
    }

    public CouponDto(long id, long companyId, String companyName, String name, String description, float price, Date startDate, Date endDate,
                     CouponType category, long amount) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.name = name;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.amount = amount;
    }

    public CouponDto(Coupon coupon) {
        this.id = coupon.getId();
        this.companyId = coupon.getCompany().getId();
        this.companyName = coupon.getCompany().getName();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.price = coupon.getPrice();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.category = coupon.getCategory();
        this.amount = coupon.getAmount();
    }

    @Override
    public String toString() {
        return "CouponDto{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", category=" + category +
                ", amount=" + amount +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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


}
