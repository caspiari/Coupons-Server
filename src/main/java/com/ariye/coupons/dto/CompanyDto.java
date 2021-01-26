package com.ariye.coupons.dto;

import com.ariye.coupons.entities.Company;

public class CompanyDto {

    private Long id;
    private String name;
    private String address;
    private String phone;

    public CompanyDto(Long id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
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
