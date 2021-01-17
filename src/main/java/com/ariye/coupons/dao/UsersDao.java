//package com.ariye.coupons.dao;
//
//import java.sql.Connection;
//import com.ariye.coupons.exeptions.ApplicationException;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.SQLTimeoutException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import com.ariye.coupons.entities.User;
//import com.ariye.coupons.entities.data.UserLoginData;
//import com.ariye.coupons.entities.data.UserLoginDetails;
//import com.ariye.coupons.enums.ErrorType;
//import com.ariye.coupons.enums.UserType;
//import com.ariye.coupons.exeptions.ApplicationException;
//import com.ariye.coupons.utils.JdbcUtils;
//
//@Repository
//public class UsersDao {
//
//	public Long createUser(User user) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "INSERT INTO users (username, password, user_type, first_name,"
//					+ " last_name, company_id) VALUES(?,?,?,?,?,?)";
//			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
//			preparedStatement.setString(1, user.getUsername());
//			preparedStatement.setString(2, user.getPassword());
//			preparedStatement.setString(3, user.getUserType().name());
//			preparedStatement.setString(4, user.getFirstName());
//			preparedStatement.setString(5, user.getLastName());
//			if (user.getCompanyId() == null) {
//				preparedStatement.setObject(6, null);
//			} else {
//				preparedStatement.setLong(6, user.getCompanyId());
//			}
//			preparedStatement.executeUpdate();
//			ResultSet resultSet = preparedStatement.getGeneratedKeys();
//			if (resultSet.next()) {
//				long id = resultSet.getLong(1);
//				resultSet.close();
//				return id;
//			}
//			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't generate a user ID " + user.toString());
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create user failed " + user.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
//		User user = new User();
//		user.setId(resultSet.getLong("id"));
//		user.setUsername(resultSet.getString("username"));
//		user.setPassword(resultSet.getString("password"));
//		UserType type = UserType.valueOf(resultSet.getString("user_type"));
//		user.setUserType(type);
//		user.setFirstName(resultSet.getString("first_name"));
//		user.setLastName(resultSet.getString("last_name"));
//		user.setCompanyId((Long) resultSet.getObject("company_id"));
//		return user;
//	}
//
//	public void deleteUser(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from users where id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete user failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public void updateUser(User user) throws ApplicationException {
//		user.setPassword(String.valueOf(user.getPassword().hashCode()));
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "update users set username = ?, password = ?, user_type = ?, first_name = ?, "
//					+ "last_name = ?, company_id = ? where id = ?;";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, user.getUsername());
//			preparedStatement.setString(2, user.getPassword());
//			preparedStatement.setString(3, user.getUserType().name());
//			preparedStatement.setString(4, user.getFirstName());
//			preparedStatement.setString(5, user.getLastName());
//			if (user.getCompanyId() == null) {
//				preparedStatement.setObject(6, null);
//			} else {
//				preparedStatement.setLong(6, user.getCompanyId());
//			}
//			preparedStatement.setLong(7, user.getId());
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update user failed " + user.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public User getUser(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM users WHERE id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				User user = createUserFromResultSet(resultSet);
//				return user;
//			}
//			return null; // -No such user found
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public User getUserByUsername(String username) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM users WHERE username=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, username);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				return createUserFromResultSet(resultSet);
//			}
//			return null; // -No such user found
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get user by name failed ");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<User> getAllUsers() throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<User> users = new ArrayList<User>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM users";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				users.add(createUserFromResultSet(resultSet));
//			}
//			return users;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all users failed");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public boolean isUsernameExist(String username) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT username FROM users where username = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, username);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				return true;
//			}
//			return false;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "IsusernameExist failed");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public UserLoginData login(UserLoginDetails userLoginDetails) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM users where username = ? AND password = ? ;";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, userLoginDetails.getUsername());
//			preparedStatement.setString(2, userLoginDetails.getPassword());
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				UserLoginData userLoginData = createUserLoginDataFromResultSet(resultSet);
//				return userLoginData;
//			} else {
//				return null;
//			}
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Login failed");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	private UserLoginData createUserLoginDataFromResultSet(ResultSet resultSet) throws ApplicationException {
//		try {
//			long id = resultSet.getLong("id");
//			UserType type = UserType.valueOf(resultSet.getString("user_type"));
//			Long companyId = (Long) resultSet.getObject("company_id");
//			UserLoginData userLoginData = new UserLoginData(id, type, companyId);
//			return userLoginData;
//		} catch (SQLException e) {
//			// TODO: handle exception
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Failed to get user");
//		}
//	}
//
//}
