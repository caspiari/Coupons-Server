package com.ariye.coupons.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.UserType;

public interface IUsersDao extends CrudRepository<User, Long>{
	
	public User getByUsername(String username);
	
	@Query("select new com.ariye.coupons.dto.UserLoginData(id, userType, company.id) from User u where u.username= :username and u.password= :password")
	public UserLoginData login(@Param("username") String username, @Param("password") String password);
	
}
