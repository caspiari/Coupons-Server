package com.ariye.coupons.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.dto.CouponDto;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.enums.CouponType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ICouponsDao extends CrudRepository<Coupon, Long> {

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c where c.id = ?1")
    CouponDto getById(long id);

    boolean existsByNameAndCompanyId(String name, long companyId);

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c where c.company.id = ?1")
    List<CouponDto> getByCompanyId(long id) throws Exception;

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c where c.category = ?1")
    List<CouponDto> getByCategory(CouponType category);

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c where c.id in " +
                    "(select p.coupon from Purchase p where p.user.id = :userId)")
    List<CouponDto> getByUserId(long userId);

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c where c.id in " +
                    "(select p.coupon from Purchase p where p.user.id = :userId) and c.price < :maxPrice")
    List<CouponDto> getByMaxPrice(@Param("userId") long userId, @Param("maxPrice") float maxPrice);

    @Query("select new com.ariye.coupons.dto.CouponDto(c.id, c.company.id, c.company.name, c.name, c.description, c.price, c.startDate, c.endDate, c.category, c.amount) from Coupon c")
    List<CouponDto> getAll();

    @Transactional
    void deleteAllByEndDateBefore(Date now);

    @Query("select amount from Coupon where id = ?1")
    Long getAmount(long id);
}



