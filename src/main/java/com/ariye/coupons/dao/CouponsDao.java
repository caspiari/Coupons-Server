package com.ariye.coupons.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;

public interface CouponsDao extends CrudRepository<Coupon, Long> {

	public Coupon findCouponByName(String name) throws Exception;

	public List<Coupon> findByCategory(CouponType category);
	
	public List<Coupon> findByCompanyId(long id);
	
	@Transactional
	@Modifying
	@Query(value = "delete from Coupon c where c.endDate < :now")
	public void deleteExpiredCoupons(@Param("now") Date now);
	
	@Query(value = "select c from Coupon c where c.id in (select p.coupon "
			+ "from Purchase p where p.user.id = :userId) and c.price < :maxPrice")
	public List<Coupon> findByMaxPrice(@Param("userId") long userId, @Param("maxPrice") float maxPrice);
	
}



