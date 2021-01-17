package com.ariye.coupons.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ariye.coupons.dto.UserLoginData;

@Component
public class CacheController {

	private Map<String, UserLoginData> dataMap;
	
	public CacheController(){
		this.dataMap = new HashMap<String, UserLoginData>();
	}
	
	public void put(String token, UserLoginData userLoginData) {
		this.dataMap.put(token, userLoginData);
	}
	
	public UserLoginData get(String token) {
		UserLoginData userLoginData = this.dataMap.get(token);
		return userLoginData;
	}
	
}
