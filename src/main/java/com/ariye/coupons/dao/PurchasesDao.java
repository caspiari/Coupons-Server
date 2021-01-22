package com.ariye.coupons.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.dto.PurchaseDto;

public interface PurchasesDao extends CrudRepository<Purchase, Long>{
	
//	@Query("select new com.bytestree.restful.dto.CustomEmployeeRs(e.firstName, e.lastName, e.department.name) from Employee e")
//	@Query("select new com.ariye.coupons.dto.PurchaseDto(p.id, p.amount) from Purchase p where id = 1")
	@Query("select new com.ariye.coupons.dto.PurchaseDto(c.name, p.amount, p.timestamp) from Purchase p join Coupon c on p.coupon.id = c.id where p.id = ?1")
	PurchaseDto getByIdForCustomer(long id);

	
	@Query("select p from Purchase p where coupon.id in (select id from Coupon where company.id = :companyId)")
	List<Purchase> findAllByCompanyID(@Param("companyId") long id);
	
	List<Purchase> findAllByUserId(long id);
	
}
