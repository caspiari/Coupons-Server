package com.ariye.coupons.dto;

import java.util.Date;

public class PurchaseDto {

    private long id;
    private long userId;
    private long couponId;
    private short amount;
    private Date timestamp;
    private String couponName;
    private String companyName;
    private String username;

    public PurchaseDto() {
    }

    /**
     * - Full ctor
     */
    public PurchaseDto(long id, long userId, long couponId, short amount, Date timestamp, String couponName,
                       String companyName, String username) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.amount = amount;
        this.timestamp = timestamp; // If it does problem, change to: = new Timestamp(timestamp.getTime());
        this.couponName = couponName;
        this.companyName = companyName;
        this.username = username;
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

    public short getAmount() {
        return amount;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
