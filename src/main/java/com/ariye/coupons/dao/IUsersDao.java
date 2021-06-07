package com.ariye.coupons.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.entities.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsersDao extends CrudRepository<User, Long> {

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id, c.name) from User u left join Company c on u.company.id = c.id where u.id = ?1")
    UserDto getUserDtoById(long id);

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id, c.name) from User u left join Company c on u.company.id = c.id where u.company.id = ?1")
    List<UserDto> getByComapnyId(long id);

    @Query("select id from User u where u.username = ?1 and u.id != ?2")
    Long getIdByUsernameAndId(String username, long id);//Used for validation

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id, c.name) from User u left join Company c on u.company.id = c.id where u.username = ?1")
    UserDto findByUsername(String username);

    @Query("select new com.ariye.coupons.dto.UserDto(u.id, u.username, u.firstName, u.lastName, u.password, u.userType, u.company.id, c.name) from User u left join Company c on u.company.id = c.id")
    List<UserDto> getAll();

    @Query("select new com.ariye.coupons.dto.UserLoginData(u.id, u.userType, u.company.id) from User u where u.username= :username and u.password= :password")
    UserLoginData login(@Param("username") String username, @Param("password") String password);

}