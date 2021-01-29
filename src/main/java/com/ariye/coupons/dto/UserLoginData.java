package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class UserLoginData {

    private long id;
    private UserType userType;
    private Long companyId;
    private long loginTimeInMillis;

    public UserLoginData(long id, UserType userType, Long companyId, long loginTimeInMillis) {
        this.id = id;
        this.userType = userType;
        this.companyId = companyId;
        this.loginTimeInMillis = loginTimeInMillis;
    }

    public long getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public long getLoginTimeInMillis() {
        return loginTimeInMillis;
    }

    public void setLoginTimeInMillis(long loginTimeInMillis) {
        this.loginTimeInMillis = loginTimeInMillis;
    }

    @Override
    public String toString() {
        return "\nUserLoginData [id=" + id + ", userType=" + userType + ", companyId=" + companyId + "]";
    }


}
