//package com.ariye.coupons.dailyTask;
//import java.util.Calendar;
//import java.sql.Date;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ariye.coupons.entities.Coupon;
//import com.ariye.coupons.enums.ErrorType;
//import com.ariye.coupons.exeptions.ApplicationException;
//import com.ariye.coupons.logic.CouponsController;
//
//public class DailyTask extends TimerTask {
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
//	
//}