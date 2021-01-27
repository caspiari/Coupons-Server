package com.ariye.coupons.dailyTask;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.CouponsController;

@Component
@EnableScheduling
public class DailyTask {

    @Autowired
    CouponsController couponsController;

    @Scheduled(cron = "0 30 1 * * *")
    public void deleteExpiredCoupons() throws ApplicationException {
        // TODO Auto-generated method stub
        couponsController.deleteExpiredCoupons();
    }

}