package com.ariye.coupons.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.dto.CouponDto;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;

public interface CouponsDao extends CrudRepository<Coupon, Long> {

	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.id = ?1")
	public CouponDto getById(long id);
	
	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.name = ?1")
	public CouponDto getByName(String name) throws Exception;

	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.company.id = ?1")
	public List<CouponDto> getByCompanyId(long id) throws Exception;

	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.category = ?1")
	public List<CouponDto> getByCategory(CouponType category);

	@Query(value = "select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c where c.id in (select p.coupon from Purchase p where p.user.id = :userId) and c.price < :maxPrice")
	public List<CouponDto> getByMaxPrice(@Param("userId") long userId, @Param("maxPrice") float maxPrice);

	@Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount, c.company.id) from Coupon c")
	public List<CouponDto> getAll();

	@Query("delete from Coupon c where c.endDate < ?1")
	public void deleteExpiredCoupons(Date now);
}



