package com.ariye.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.User;

public interface UsersDao extends CrudRepository<User, Long> {

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id) from User u where u.id = ?1")
    public UserDto getById(long id);

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id) from User u where u.username = ?1")
    public UserDto findByUsername(String username);

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id) from User u")
    public List<UserDto> getAll();

    @Query("select new com.ariye.coupons.dto.UserLoginData(u.id, u.userType, u.company.id) from User u where u.username= :username and u.password= :password")
    public UserLoginData login(@Param("username") String username, @Param("password") String password);

}
