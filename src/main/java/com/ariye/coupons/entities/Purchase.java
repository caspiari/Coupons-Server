package com.ariye.coupons.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchases")
public class Purchase implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;

    private short amount;

    @Column(name = "time_stamp")
    private Date timestamp;

    /**
     * - Ctor without id
     */
    public Purchase(User user, Coupon coupon, short amount, Date timestamp) {
        this.user = user;
        this.coupon = coupon;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * - Full ctor
     */
    public Purchase(long id, User user, Coupon coupon, short amount, Date timestamp) {
        this(user, coupon, amount, timestamp);
        this.id = id;
    }

    public Purchase() {
    }


    @Override
    public String toString() {
        return "\nPurchase [id=" + id + ", user=" + user.getId() + ", coupon=" + coupon.getName() + ", amount=" + amount
                + ", timestamp=" + timestamp + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

}
