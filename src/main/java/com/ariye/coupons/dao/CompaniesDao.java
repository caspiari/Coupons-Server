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
public interface CompaniesDao extends CrudRepository<Company, Long> {

    @Query("select new com.ariye.coupons.dto.CompanyDto(c.id, c.name, c.address, c.phone) from Company c where c.id = ?1")
    public CompanyDto getById(long id);

    @Query("select new com.ariye.coupons.dto.CompanyDto(c.id, c.name, c.address, c.phone) from Company c")
    public List<CompanyDto> getAll();

    @Query("select new com.ariye.coupons.dto.CompanyDto(c.id, c.name, c.address, c.phone) from Company c where c.name = ?1")
    public CompanyDto getByName(String name);

    public boolean existsByName(String name);
}
