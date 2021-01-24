package com.ariye.coupons.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ariye.coupons.dto.CouponDto;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;

public interface CouponsDao extends CrudRepository<Coupon, Long> {

	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.id = ?1")
	public CouponDto getById(long id);
	
	@Query("select new com.ariye.coupons.dto.CouponDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id) from User u where u.id = ?1")
	public CouponDto getByName(String name) throws Exception;

	public List<CouponDto> getByCategory(CouponType category);
	
	public List<CouponDto> getByCompanyId(long id);
	
	@Transactional
	@Modifying
	@Query(value = "delete from Coupon c where c.endDate < :now")
	public void deleteExpiredCoupons(@Param("now") Date now);
	
	@Query(value = "select c from Coupon c where c.id in (select p.coupon "
			+ "from Purchase p where p.user.id = :userId) and c.price < :maxPrice")
	public List<Coupon> getByMaxPrice(@Param("userId") long userId, @Param("maxPrice") float maxPrice);
	
}



