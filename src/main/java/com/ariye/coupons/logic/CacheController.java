package com.ariye.coupons.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ariye.coupons.dto.UserLoginData;

@Component
@EnableScheduling
public class CacheController { //possible to add "Time to die\leave"

    private Map<String, UserLoginData> dataMap;

    public CacheController() {
        this.dataMap = new HashMap<>();
    }

    public void put(String token, UserLoginData userLoginData) {
        this.dataMap.put(token, userLoginData);
    }

    public UserLoginData get(String token) {
        UserLoginData userLoginData = this.dataMap.get(token);
        return userLoginData;
    }

    @Scheduled(cron = "0 0 * * *")
    private void cleanCacheTask() {
        long hourInMillis = 1000 * 60 * 60;
        long now = System.currentTimeMillis();
        for (String token : this.dataMap.keySet()) {
            UserLoginData userLoginData = this.dataMap.get(token);
            if (now - userLoginData.getLoginTimeInMillis() > hourInMillis) {
                this.dataMap.remove(token);
            }
        }
    }

}
