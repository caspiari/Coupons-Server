package com.ariye.coupons.dao;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.dto.PurchaseDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PurchasesDao extends CrudRepository<Purchase, Long> {

    @Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.user.id, p.coupon.id, p.amount, p.timestamp, p.coupon.name, p.coupon.company.name,"
            + "p.user.username) from Purchase p where p.id = ?1")
    PurchaseDto getById(long id);

    @Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.user.id, p.coupon.id, p.amount, p.timestamp, p.coupon.name, p.coupon.company.name,"
            + "p.user.username) from Purchase p")
    List<PurchaseDto> getAll();

    @Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.user.id, p.coupon.id, p.amount, p.timestamp, p.coupon.name, p.coupon.company.name,"
            + "p.user.username) from Purchase p where p.coupon.company.id = ?1")
    List<PurchaseDto> findAllByCompanyID(long id);

    @Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.user.id, p.coupon.id, p.amount, p.timestamp, p.coupon.name, p.coupon.company.name,"
            + "p.user.username) from Purchase p where p.user.id = ?1")
    List<PurchaseDto> findAllByUserId(long id);

}