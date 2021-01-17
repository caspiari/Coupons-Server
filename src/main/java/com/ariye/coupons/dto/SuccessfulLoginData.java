package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class SuccessfulLoginData {

	private String token;
	private UserType userType;
	
	public SuccessfulLoginData(String token, UserType userType) {
		super();
		this.token = token;
		this.userType = userType;
	}
	
	public SuccessfulLoginData() {
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
		return "\nSuccessfulLoginData [token=" + token + ", userType=" + userType + "]";
	}
	
	
}
