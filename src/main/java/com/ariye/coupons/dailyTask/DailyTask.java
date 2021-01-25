//package com.ariye.coupons.dailyTask;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.stereotype.Component;
//
//import java.util.TimerTask;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.ariye.coupons.exeptions.ApplicationException;
//import com.ariye.coupons.logic.CouponsController;
//
//  @Component //Or use this: https://www.baeldung.com/spring-scheduled-tasks
//public class DailyTask extends TimerTask {
//
//	@Autowired
//	CouponsController couponsController;
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		try {
//			couponsController.deleteExpiredCoupons();
//		} catch (ApplicationException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Daily task is running");
//	}
//	@PostConstruct
//	public void init() {
//		scheduleTimerTask..()
//	}
//
//}