package com.ariye.coupons.logic;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ariye.coupons.entities.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.IUsersDao;
import com.ariye.coupons.dto.SuccessfulLoginData;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.dto.UserLoginDetails;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

@Controller
public class UsersController {

    @Autowired
    private IUsersDao usersDao;
    @Autowired
    private CompaniesController companiesController;
    @Autowired
    private CacheController cacheController;

    private static final String ENCRYPTION_TOKEN_SALT = "ASFDSDGFDSFGSSD-54675467#$%^";

    public long createUser(UserDto userDto, UserLoginData userLoginData) throws ApplicationException {
        try {
            Company company = this.validateUpdateUser(userDto, userLoginData); //same validations
            User user = new User(userDto);
            String password = String.valueOf(user.getPassword().hashCode());
            user.setPassword(password);
            user.setCompany(company);
            user = this.usersDao.save(user);
            return user.getId();
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create user failed " + userDto.toString());
        }
    }

    public UserDto getUserDto(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            id = userLoginData.getId();
        }
        try {
            UserDto userDto = this.usersDao.getUserDtoById(id);
            if (userDto == null) {
                throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "User id");
            }
            return userDto;
        } catch (ApplicationException e) {
            throw e;
        }  catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user failed. Id: " + id);
        }
    }

    User getUser(long id) throws ApplicationException {
        try {
            return this.usersDao.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "User id");
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user entity failed for: " + id);
        }
    }

    public void updateUser(UserDto userDto, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            userDto.setId(userLoginData.getId());
            userDto.setUserType(userLoginData.getUserType());
        }
        Company company = this.validateUpdateUser(userDto, userLoginData);
        User user = this.getUser(userDto.getId());
        if(!userDto.getPassword().equals(user.getPassword())) {
            String password = String.valueOf(userDto.getPassword().hashCode());
            user.setPassword(password);
        }
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserType(userDto.getUserType());
        user.setCompany(company);
        try {
            this.usersDao.save(user);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update user failed " + userDto.toString());
        }
    }

    public void deleteUser(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            this.usersDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete user failed. Id: " + id);
        }
    }

    private UserDto getUserDtoByUsername(String username) throws ApplicationException {
        this.validateEmail(username);
        try {
            UserDto userDto = usersDao.findByUsername(username);
            if (userDto == null) {
                throw new ApplicationException(ErrorType.USERNAME_DOES_NOT_EXIST);
            }
            return userDto;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user by username failed " + username);
        }
    }

    public List<UserDto> getAllUsers(UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            List<UserDto> usersDtos = this.usersDao.getAll();
            return usersDtos;
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
            userLoginData = usersDao.login(username, password);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Login failed for " + userLoginDetails.getUsername());
        }
        if (userLoginData == null) {
            getUserDtoByUsername(userLoginDetails.getUsername()); // If the user name is wrong it will throw 'User doesn't exist' exception.
            throw new ApplicationException(ErrorType.WRONG_PASSWORD);
        }
        String token = generateToken(username);
        long now = System.currentTimeMillis();
        userLoginData.setLoginTime(now);
        this.cacheController.put(token, userLoginData);
        SuccessfulLoginData successfulLoginData = new SuccessfulLoginData(userLoginData.getId(), token, userLoginData.getUserType(), userLoginData.getCompanyId());
        return successfulLoginData;
    }

    public void logout(String token) {
        this.cacheController.delete(token);
    }

    private String generateToken(String username) {
        String text = username + Calendar.getInstance().getTime().toString() + ENCRYPTION_TOKEN_SALT;
        int token = text.hashCode();
        return String.valueOf(token);
    }


/////////////// Validations:

    void validateEmail(String email) throws ApplicationException {
        String email_pattern = "^[a-zA-Z0-9_#$%&�*+/=?^.-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,7}$";
        Pattern pat = Pattern.compile(email_pattern);
        Matcher mat = pat.matcher(email);
        if (!(mat.matches())) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
        if (email.length() < 8) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
        if (email.substring(0, email.indexOf("@")).length() < 2) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
        if (email.substring(email.indexOf('@'), email.indexOf('.')).length() < 3) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
        if (email.substring(email.indexOf('.')).length() < 3) {
            throw new ApplicationException(ErrorType.INVALID_EMAIL);
        }
    }

    private Company validateUpdateUser(UserDto userDto, UserLoginData userLoginData) throws ApplicationException {
        try {
            Long id = this.usersDao.getIdByUsernameAndId(userDto.getUsername(), userDto.getId());
            if (id != null) {
                throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS, "Username");
            }
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
            if (userDto.getUserType() == UserType.COMPANY) {
                Company company = companiesController.getEntity(userDto.getCompanyId(), userLoginData);
                return company;
            }
            return null;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Validate update user failed " + userDto.toString());
        }
    }

}
