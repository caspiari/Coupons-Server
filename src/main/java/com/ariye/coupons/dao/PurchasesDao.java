package com.ariye.coupons.dao;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.dto.PurchaseDto;

public interface PurchasesDao extends CrudRepository<Purchase, Long>{
	
	//insert into users (first_name, last_name, password, user_type, username, company_id) 
	//values('aaa', 'bbb', '123', 'COMPANY', 'ggg@hhhh.cv', 1);
	
	@Query("select new com.ariye.coupons.dto.PurchaseDto(p.coupon.name, p.coupon.company.name, p.user.username, p.amount, p.timestamp) from Purchase p where p.id = ?1")
	PurchaseDto getById(long id);
	
	@Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.user.id, p.coupon.id, p.amount, p.timestamp, p.coupon.company.name) from Purchase p")
	List<PurchaseDto> getAll();
	
	@Query("select p from Purchase p where coupon.id in (select id from Coupon where company.id = :companyId)")
	List<Purchase> findAllByCompanyID(@Param("companyId") long id);
	
	List<Purchase> findAllByUserId(long id);
	
}