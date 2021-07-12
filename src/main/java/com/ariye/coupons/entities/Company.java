package com.ariye.coupons.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "companies")
@SuppressWarnings("serial")
public class Company implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;

    private String phone;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Coupon> coupons;


    /**
     * - Ctor for creation - without id, users and coupons
     */
    public Company(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    /**
     * - Full ctor
     */
    public Company(long id, String name, String address, String phone, List<User> users, List<Coupon> coupons) {
        this(name, address, phone);
        this.id = id;
        this.users = users;
        this.coupons = coupons;
    }

    public Company() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "\nCompany [id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
    }

}
