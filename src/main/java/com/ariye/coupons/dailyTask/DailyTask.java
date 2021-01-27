package com.ariye.coupons.dailyTask;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.CouponsController;

@Component
@EnableScheduling
public class DailyTask {

    @Autowired
    CouponsController couponsController;

    @Scheduled(cron = "0 3 15 * * *")
    public void deleteExpiredCoupons() throws ApplicationException {
        couponsController.deleteExpiredCoupons();
    }

}