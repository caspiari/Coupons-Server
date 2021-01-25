package com.ariye.coupons.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ariye.coupons.dao.CouponsDao;
import com.ariye.coupons.dao.UsersDao;
import com.ariye.coupons.dto.SuccessfulLoginData;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.dto.UserLoginDetails;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

@Controller
public class UsersController {

	@Autowired
	private UsersDao iUsersDao;
	@Autowired
	private CompaniesController companiesController;
	@Autowired
	private CacheController cacheController;
	
	@Autowired
	CouponsDao couponsDao;
	
	private static final String ENCRIPTION_TOKEN_SALT = "ASFDSDGFDSFGSSD-54675467#$%^";

	public long createUser(UserDto userDto) throws ApplicationException {
		this.validateUpdateUser(userDto); // Same validations...
		User user = new User(userDto);
		String password = String.valueOf(user.getPassword().hashCode());
		user.setPassword(password);
		try {
			user = this.iUsersDao.save(user);
			long id = user.getId();
			return id;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create user failed " + user.toString());
		}
	}

	public UserDto getUser(long id, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			id = userLoginData.getId();
		}
		if (!(this.isUserExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "User id");
		}
		try {
			UserDto userDto = this.iUsersDao.getById(id);
			return userDto;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user failed. Id: " + id);
		}
	}

	// For inner use
	User getEntity(long id) throws ApplicationException {
		if (!(this.isUserExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "User id");
		}
		try {
			User user = this.iUsersDao.findById(id).get();
			return user;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user entity failed. Id: " + id);
		}
	}

	public void updateUser(UserDto userDto, UserLoginData userLoginData) throws ApplicationException {
		this.validateUpdateUser(userDto);
		if (userLoginData.getUserType() != UserType.ADMIN) {
			userDto.setId(userLoginData.getId());
			userDto.setUserType(userLoginData.getUserType());
			userDto.setCompanyId(userLoginData.getCompanyId());
		}
		User user = this.createUserFromDto(userDto, userLoginData);
		String password = String.valueOf(user.getPassword().hashCode());
		user.setPassword(password);
		try {
			this.iUsersDao.save(user);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update user failed " + userDto.toString());
		}
	}

	public void deleteUser(long id, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
		}
		try {
			this.iUsersDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete user failed. Id: " + id);
		}
	}

	public UserDto getUserByUsername(String username, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
		}
		this.validateEmail(username);
		UserDto userDto;
		try {
			userDto = iUsersDao.findByUsername(username);
			return userDto;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user by username failed " + username);
		}
	}

	public List<UserDto> getAllUsers(UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
		}
		try {
			List<UserDto> users = (List<UserDto>) this.iUsersDao.getAll();
			return users;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all users failed");
		}
	}

	public SuccessfulLoginData login(UserLoginDetails userLoginDetails) throws ApplicationException {
		String username = userLoginDetails.getUsername();
		this.validateEmail(username);
		String password = String.valueOf(userLoginDetails.getPassword().hashCode());
		userLoginDetails.setPassword(password);
		UserLoginData userLoginData;
		try {
			userLoginData = iUsersDao.login(username, password);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"Login failed for " + userLoginDetails.getUsername());
		}
		if (userLoginData == null) {
			throw new ApplicationException(ErrorType.INVALID_LOGIN_DETAILS);
		}
		String token = generateToken(username);
		this.cacheController.put(token, userLoginData);
		SuccessfulLoginData successfulLoginData = new SuccessfulLoginData(token, userLoginData.getUserType());
		return successfulLoginData;
	}

	public String generateToken(String username) {
		String text = username + Calendar.getInstance().getTime().toString() + ENCRIPTION_TOKEN_SALT;
		int token = text.hashCode();
		return String.valueOf(token);
	}

	private User createUserFromDto(UserDto userDto, UserLoginData userLoginData) throws ApplicationException {
		Company company = this.companiesController.getCompany(userDto.getCompanyId(), userLoginData);
		User user = new User(userDto.getId(), userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
				userDto.getPassword(), userDto.getUserType(), company);
		return user;
	}
	

/////////////// Validations:

	// not private because being used by another controller
	boolean isUserExist(long id) throws ApplicationException {
		try {
			return this.iUsersDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is user exist failed " + id);
		}
	}

	// not private because being used by another controller
	void validateEmail(String email) throws ApplicationException {
		if (email.substring(0, email.indexOf("@")).length() < 2) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL);
		}
		if (email.substring(email.indexOf('@'), email.indexOf('.')).length() < 3) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL);
		}
		if (email.substring(email.indexOf('.')).length() < 3) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL);
		}
		String email_pattern = "^[a-zA-Z0-9_#$%&�*+/=?^.-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,7}$";
		Pattern pat = Pattern.compile(email_pattern);
		Matcher mat = pat.matcher(email);
		if (!(mat.matches())) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL);
		}
	}

	private void validateUpdateUser(UserDto userDto) throws ApplicationException {
		// if (this.usersDao.isUsernameExist(user.getUsername())) { <-Apply
		// after third layer
		// throw new Exception("Username already exist");
		// }
		if (userDto.getUsername() == null) {
			throw new ApplicationException(ErrorType.MUST_INSERT_A_VALUE, "User name");
		}
		if (userDto.getFirstName() == null) {
			throw new ApplicationException(ErrorType.MUST_ENTER_NAME, "First name");
		}
		if (userDto.getLastName() == null) {
			throw new ApplicationException(ErrorType.MUST_ENTER_NAME, "Last name");
		}
		if (userDto.getPassword() == null) {
			throw new ApplicationException(ErrorType.MUST_INSERT_A_VALUE, "Password");
		}
		this.validateEmail(userDto.getUsername());
		if (userDto.getFirstName().length() < 2) {
			throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT, "First name");
		}
		if (userDto.getLastName().length() < 2) {
			throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT, "Last name");
		}
		if (userDto.getPassword().length() < 6) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD);
		}
	}

}
