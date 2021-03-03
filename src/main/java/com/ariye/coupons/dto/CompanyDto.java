package com.ariye.coupons.dto;

import com.ariye.coupons.entities.Company;

public class CompanyDto {

    private long id;
    private String name;
    private String address;
    private String phone;

    public CompanyDto(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public CompanyDto(long id, String name, String address, String phone) {
        this(name, address, phone);
        this.id = id;
    }

    public CompanyDto() {
    }

    public CompanyDto(Company company) {
        this(company.getId(), company.getName(), company.getAddress(), company.getPhone());
    }

    @Override
    public String toString() {
        return "CompanyDto [id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
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

}
