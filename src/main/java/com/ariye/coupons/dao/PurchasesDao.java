package com.ariye.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.entities.Coupon;

public interface PurchasesDao extends CrudRepository<Purchase, Long>{

	@Query("select p from Purchase p where coupon.id in (select id from Coupon where company.id = :companyId)")
	List<Purchase> findAllByCompanyID(@Param("companyId") long id);
	
	List<Purchase> findAllByUserId(long id);
}
