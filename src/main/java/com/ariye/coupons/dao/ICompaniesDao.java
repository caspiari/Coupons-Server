package com.ariye.coupons.dao;

import java.sql.SQLException;
import java.util.List;

import com.ariye.coupons.dto.CompanyDto;
import com.ariye.coupons.dto.CouponDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ariye.coupons.entities.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompaniesDao extends CrudRepository<Company, Long> {

    @Query("select new com.ariye.coupons.dto.CompanyDto(c.id, c.name, c.address, c.phone) from Company c where c.id = ?1")
    CompanyDto getById(long id);

    @Query("select id from Company c where c.name = ?1 and c.id != ?2")
    Long getIdByNameAndId(String name, long id);//Used for validation

    @Query("select new com.ariye.coupons.dto.CompanyDto(c.id, c.name, c.address, c.phone) from Company c")
    List<CompanyDto> getAll();

    Company getByName(String name);

    boolean existsByName(String name);
}
