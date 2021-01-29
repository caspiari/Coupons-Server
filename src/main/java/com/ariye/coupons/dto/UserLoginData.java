package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class UserLoginData {

    private long id;
    private UserType userType;
    private Long companyId;
    private long loginTime;

    public UserLoginData(long id, UserType userType, Long companyId) {
        this.id = id;
        this.userType = userType;
        this.companyId = companyId;
    }

    public UserLoginData(long id, UserType userType, Long companyId, long loginTime) {
        this(id, userType, companyId);
        this.loginTime = loginTime;
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

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "\nUserLoginData [id=" + id + ", userType=" + userType + ", companyId=" + companyId + "]";
    }


}
