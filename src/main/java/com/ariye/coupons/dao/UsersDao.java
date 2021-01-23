package com.ariye.coupons.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.UserType;

public interface UsersDao extends CrudRepository<User, Long>{

	@Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id) from User u where u.id = ?1")
	public UserDto getById(long id);
	
	@Query("select u from User u where u.username =?1")
	public User findByUsername(String username);
	
	@Query("select new com.ariye.coupons.dto.UserLoginData(id, userType, company.id) from User u where u.username= :username and u.password= :password")
	public UserLoginData login(@Param("username") String username, @Param("password") String password);
	
}
