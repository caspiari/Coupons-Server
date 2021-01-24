package com.ariye.coupons.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", nullable = false)
	private UserType userType;

	@ManyToOne(fetch = FetchType.EAGER)
	private Company company;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	@JsonIgnore
	List<Purchase> purchases;

	/**
	 * - Ctor for creation - without id and purchases
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param userType
	 * @param company
	 */
	public User(String username, String firstName, String lastName, String password, UserType userType,	Company company) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.userType = userType;
		this.company = company;
	}

	/**
	 * - Ctor without purchases
	 */
	public User(long id, String username, String firstName, String lastName, String password, UserType userType, Company company) {
		this(username, firstName, lastName, password, userType, company);
		this.id = id;
	}
	
	/**
	 * - This ctor sets the company and purchases null
	 */
	public User(UserDto userDto) {
		this(userDto.getId(),userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(), userDto.getPassword(), userDto.getUserType(), null);
	}
	
	/**
	 * - Full ctor
	 */
	public User(long id, String username, String firstName, String lastName, String password, UserType userType, Company company, List<Purchase> purchases) {
		this(id, username, firstName, lastName, password, userType, company);
		this.purchases = purchases;
	}

	public User() {
	}

	@Override
	public String toString() {
		return "\nUser [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", password=" + password + ", userType=" + userType + ", company=" + company.getName() + "]";
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
	
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public List<Purchase> getPurchases() {
		return purchases;
	}
	
	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

}
