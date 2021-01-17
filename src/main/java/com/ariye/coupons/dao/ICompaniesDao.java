package com.ariye.coupons.dao;

import java.sql.SQLException;

import org.springframework.data.repository.CrudRepository;

import com.ariye.coupons.entities.Company;

public interface ICompaniesDao extends CrudRepository<Company, Long>{
	
	public Company findCompanyByName(String name) throws SQLException;
	
}
