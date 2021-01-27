package com.ariye.coupons.dto;

import com.ariye.coupons.enums.UserType;

public class UserDto {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UserType userType;
    private Long companyId;

    /**
     * - Ctor for creation
     */
    public UserDto(String username, String firstName, String lastName, String password, UserType userType, Long companyId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType;
        this.companyId = companyId;
    }

    /**
     * - Full ctor
     */
    public UserDto(long id, String username, String firstName, String lastName, String password, UserType userType, Long companyId) {
        this(username, firstName, lastName, password, userType, companyId);
        this.id = id;

    }

    public UserDto() {
    }

    @Override
    public String toString() {
        return "UserDto [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
                + ", password=" + password + ", userType=" + userType + ", companyId=" + companyId + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
