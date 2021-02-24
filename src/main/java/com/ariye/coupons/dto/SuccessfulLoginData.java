package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class SuccessfulLoginData {

    private long id;
    private String token;
    private UserType userType;

    public SuccessfulLoginData(long id, String token, UserType userType) {
        super();
        this.id = id;
        this.token = token;
        this.userType = userType;
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

    @Override
    public String toString() {
        return "\nSuccessfulLoginData [id=" + id + ", token=" + token + ", userType=" + userType + "]";
    }


}
