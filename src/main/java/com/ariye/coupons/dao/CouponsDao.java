//package com.ariye.coupons.dao;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import com.ariye.coupons.entities.Coupon;
//import com.ariye.coupons.enums.CouponType;
//import com.ariye.coupons.enums.ErrorType;
//import com.ariye.coupons.exeptions.ApplicationException;
//import com.ariye.coupons.utils.JdbcUtils;
//
//@Repository
//public class CouponsDao {
//
//	public Long createCoupon(Coupon coupon) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "INSERT INTO coupons (name, price, description, start_date, end_date, category, amount, company_id) VALUES(?,?,?,?,?,?,?,?)";
//			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
//			preparedStatement.setString(1, coupon.getName());
//			preparedStatement.setFloat(2, coupon.getPrice());
//			preparedStatement.setString(3, coupon.getDescription());
//			preparedStatement.setDate(4, coupon.getStartDate());
//			preparedStatement.setDate(5, coupon.getEndDate());
//			preparedStatement.setString(6, coupon.getCategory().name());
//			preparedStatement.setInt(7, coupon.getAmount());
//			preparedStatement.setObject(8, coupon.getCompanyId());
//			preparedStatement.executeUpdate();
//			ResultSet resultSet = preparedStatement.getGeneratedKeys();
//			if (resultSet.next()) {
//				long id = resultSet.getLong(1);
//				return id;
//			}
//			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't generate a Coupon ID " + coupon.toString());
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create coupon failed " + coupon.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	private Coupon createCouponFromResultSet(ResultSet resultSet) throws SQLException {
//		Coupon coupon = new Coupon();
//		coupon.setId(resultSet.getLong("id"));
//		coupon.setName(resultSet.getString("name"));
//		coupon.setPrice(resultSet.getFloat("price"));
//		coupon.setDescription(resultSet.getString("description"));
//		coupon.setStartDate(resultSet.getDate("start_date"));
//		coupon.setEndDate(resultSet.getDate("end_date"));
//		CouponType type = CouponType.valueOf(resultSet.getString("category"));
//		coupon.setCategory(type);
//		coupon.setAmount(resultSet.getInt("amount"));
//		coupon.setCompanyId((Long) resultSet.getObject("company_id"));
//		return coupon;
//	}
//
//	public void deleteCoupon(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from coupons where id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete coupon failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public void updateCoupon(Coupon coupon) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "update coupons set name=?, price=?, description=?, start_date=?, end_date=?, "
//								+ "	category=?, amount=?, company_id=? where id=?;";
//			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
//			preparedStatement.setString(1, coupon.getName());
//			preparedStatement.setFloat(2, coupon.getPrice());
//			preparedStatement.setString(3, coupon.getDescription());
//			preparedStatement.setDate(4, coupon.getStartDate());
//			preparedStatement.setDate(5, coupon.getEndDate());
//			preparedStatement.setString(6, coupon.getCategory().name());
//			preparedStatement.setInt(7, coupon.getAmount());
//			preparedStatement.setLong(8, coupon.getCompanyId());
//			preparedStatement.setLong(9, coupon.getId());
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon failed " + coupon.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public Coupon getCoupon(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM coupons WHERE id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				Coupon coupon = createCouponFromResultSet(resultSet);
//				return coupon;
//			}
//			return null;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupon failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Coupon> getAllCoupons() throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Coupon> coupons = new ArrayList<Coupon>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM coupons";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				coupons.add(createCouponFromResultSet(resultSet));
//			}
//			return coupons;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all coupons failed ");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public boolean isCouponNameExist(String name) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT name FROM coupons where name = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, name);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				return true;
//			}
//			return false;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon name exist failed " + name);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public boolean isCouponAvailable(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT amount FROM coupons where id = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next() && resultSet.getInt("amount") > 0) {
//				return true;
//			}
//			return false;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon available failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Coupon> getCouponsByCompanyId(long companyId) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Coupon> coupons = new ArrayList<Coupon>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM coupons where company_id = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, companyId);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				coupons.add(createCouponFromResultSet(resultSet));
//			}
//			return coupons;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by company id failed " + companyId);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Coupon> getCouponsByType(CouponType type) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Coupon> coupons = new ArrayList<Coupon>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM coupons where category = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setString(1, type.name());
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				coupons.add(createCouponFromResultSet(resultSet));
//			}
//			return coupons;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by type failed " + type);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public List<Coupon> getPurchasedCouponsByMaxPrice(long userId, float maxPrice) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Coupon> coupons = new ArrayList<Coupon>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM coupons where price < ? and id in (select coupon_id from "
//								+ "purchases where user_id = ?);";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setFloat(1, maxPrice);
//			preparedStatement.setLong(2, userId);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				coupons.add(createCouponFromResultSet(resultSet));
//			}
//			return coupons;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchased coupons by max price failed " + userId);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public void deleteCouponsByCompanyId(long companyId) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete FROM coupons where company_id = ?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, companyId);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete coupons by company id failed " + companyId);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public void deleteExpiredCoupons () throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		Date date = new Date(System.currentTimeMillis());
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement2 = "delete from coupons where end_date < ?;";
//			preparedStatement = connection.prepareStatement(sqlStatement2);
//			preparedStatement.setDate(1, date);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired coupons failed");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//}
