package com.ariye.coupons;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.CouponsController;


@SpringBootApplication
public class Main {
	
	public static void main(String[] args) throws ApplicationException {
		
		
		SpringApplication.run(Main.class, args);
		
	}
	
	
	
}
