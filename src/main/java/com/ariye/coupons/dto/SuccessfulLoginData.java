package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class SuccessfulLoginData {

    private long id;
    private String token;
    private UserType userType;
    private Long companyId;

    public SuccessfulLoginData(long id, String token, UserType userType, Long companyId) {
        this.id = id;
        this.token = token;
        this.userType = userType;
        this.companyId = companyId;
    }

    public SuccessfulLoginData() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "SuccessfulLoginData{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userType=" + userType +
                ", companyId=" + companyId +
                '}';
    }
}
