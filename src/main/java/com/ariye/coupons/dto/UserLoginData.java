package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class UserLoginData {
	
	private long id;
	private UserType userType;
	private Long companyId;

	public UserLoginData(long id, UserType userType, Long companyId) {
		this.id = id;
		this.userType = userType;
		this.companyId = companyId;
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

	@Override
	public String toString() {
		return "\nUserLoginData [id=" + id + ", userType=" + userType + ", companyId=" + companyId + "]";
	}
	
	
	

}
