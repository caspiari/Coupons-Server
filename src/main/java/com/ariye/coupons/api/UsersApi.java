package com.ariye.coupons.api;

import com.ariye.coupons.dto.SuccessfulLoginData;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.dto.UserLoginDetails;
import com.ariye.coupons.entities.*;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ariye.coupons.logic.CacheController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersApi {

	@Autowired
	UsersController usersController;
	@Autowired
	CacheController cacheController;

	@PostMapping
	public long createUser(@RequestBody UserDto userDto) throws ApplicationException {
		long id = this.usersController.createUser(userDto);
		return id;
	}

	@GetMapping("/{userId}")
	public User getUser(@PathVariable("userId") long id, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		User user = this.usersController.getUser(id, userLoginData);
		return user;
	}

	@PutMapping
	public void updateUser(@RequestBody UserDto userDto, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		this.usersController.updateUser(userDto, userLoginData);
	}

	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") long id, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		this.usersController.deleteUser(id, userLoginData);
	}

	@GetMapping("/byUsername")
	public User getUserByUsername(@RequestParam("username") String username, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		User user = this.usersController.getUserByUsername(username, userLoginData);
		return user;
	}

	@GetMapping
	public List<User> getAllUsers() throws ApplicationException {
		List<User> users = usersController.getAllUsers();
		return users;
	}

	@PostMapping("/login")
	public SuccessfulLoginData login(@RequestBody UserLoginDetails userLoginDetails) throws ApplicationException {
		SuccessfulLoginData successfulLoginData = this.usersController.login(userLoginDetails);
		return successfulLoginData;
	}

}
