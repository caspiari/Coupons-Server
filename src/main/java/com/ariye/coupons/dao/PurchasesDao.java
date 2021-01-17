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
//import com.ariye.coupons.entities.Purchase;
//import com.ariye.coupons.entities.User;
//import com.ariye.coupons.enums.ErrorType;
//import com.ariye.coupons.enums.UserType;
//import com.ariye.coupons.exeptions.ApplicationException;
//import com.ariye.coupons.logic.CouponsController;
//import com.ariye.coupons.utils.JdbcUtils;
//
//@Repository
//public class PurchasesDao {
//
//	public Long createPurchase(Purchase purchase) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "INSERT INTO purchases (user_id, coupon_id, amount,"
//								+ " time_stamp) VALUES(?,?,?,?)";
//			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
//			preparedStatement.setLong(1, purchase.getUserId());
//			preparedStatement.setLong(2, purchase.getCouponId());
//			preparedStatement.setInt(3, purchase.getAmount());
//			preparedStatement.setTimestamp(4, purchase.getTimestamp());
//			preparedStatement.executeUpdate();
//			ResultSet resultSet = preparedStatement.getGeneratedKeys();
//			if (resultSet.next()) {
//				long id = resultSet.getLong(1);
//				resultSet.close();
//				return id;
//			}
//			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't generate a Purchase ID " + purchase.toString());
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase failed " + purchase.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	private Purchase createPurchaseFromResultSet(ResultSet resultSet) throws SQLException {
//		Purchase purchase = new Purchase();
//		purchase.setId(resultSet.getLong("id"));
//		purchase.setUserId(resultSet.getLong("user_id"));
//		purchase.setCouponId(resultSet.getLong("coupon_id"));
//		purchase.setAmount(resultSet.getInt("amount"));
//		purchase.setTimestamp(resultSet.getTimestamp("time_stamp"));
//		return purchase;
//	}
//
//	public Purchase getPurchase(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM purchases WHERE id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			if (resultSet.next()) {
//				Purchase purchase = createPurchaseFromResultSet(resultSet);
//				return purchase;
//			}
//			return null; // =No such purchase found
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchase failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public void updatePurchase(Purchase purchase) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "update purchases set user_id=?, coupon_id=?, "
//					+ "amount=?, time_stamp=? where id = ?;";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, purchase.getUserId());
//			preparedStatement.setLong(2, purchase.getCouponId());
//			preparedStatement.setInt(3, purchase.getAmount());
//			preparedStatement.setTimestamp(4, purchase.getTimestamp());
//			preparedStatement.setLong(5, purchase.getId());
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update purchase failed " + purchase.toString());
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public void deletePurchase(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from purchases where id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete purchase failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Purchase> getAllPurchases() throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Purchase> purchases = new ArrayList<Purchase>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM purchases";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				purchases.add(createPurchaseFromResultSet(resultSet));
//			}
//			return purchases;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases failed ");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Purchase> getAllPurchasesByUserId(long userId) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Purchase> purchases = new ArrayList<Purchase>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "SELECT * FROM purchases where user_id=?;";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, userId);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				purchases.add(createPurchaseFromResultSet(resultSet));
//			}
//			return purchases;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases by user id failed " + userId);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public List<Purchase> getAllPurchasesByCompanyId(long companyId) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		List<Purchase> purchases = new ArrayList<Purchase>();
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "select * from purchases where coupon_id in (select id from "
//								+ "coupons where company_id = ?);";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, companyId);
//			preparedStatement.executeQuery();
//			ResultSet resultSet = preparedStatement.getResultSet();
//			while (resultSet.next()) {
//				purchases.add(createPurchaseFromResultSet(resultSet));
//			}
//			return purchases;
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases by company id failed " + companyId);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//	public void deletePurchasesByCompanyId(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from purchases where coupon_id in (select id from coupons where company_id = ?);";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete purchases by company id failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public void deletePurchasesByCouponId(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from purchases where coupon_id=?";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete purchases by coupon id failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public void deletePurchasesByUserId(long id) throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from purchases where user_id=?;";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setLong(1, id);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete purchases by user id failed " + id);
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//	
//	public void deleteExpiredPurchases() throws ApplicationException {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		Date date = new Date(System.currentTimeMillis());
//		try {
//			connection = JdbcUtils.getConnection();
//			String sqlStatement = "delete from purchases where coupon_id in (select id from coupons where end_date < ?);";
//			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setDate(1, date);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired purchases failed");
//		} finally {
//			JdbcUtils.closeResources(connection, preparedStatement);
//		}
//	}
//
//}
