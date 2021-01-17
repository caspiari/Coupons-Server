package com.ariye.coupons.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ariye.coupons.entities.Company;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.utils.JdbcUtils;

@Repository
public class CompaniesDao {

	public Long createCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO companies (name, address, phone_number) VALUES(?,?,?)";
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getAddress());
			preparedStatement.setString(3, company.getPhone());
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long id = resultSet.getLong(1);
				return id;
			}
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't generate a Company ID " + company.toString());
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create company failed " + company.toString());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public void deleteCompany(long id) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "delete from companies where id=?";
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete company failed " + id);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public void updateCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "update companies set name = ?, address = ?, phone_number = ? where id=?;";
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getAddress());
			preparedStatement.setString(3, company.getPhone());
			preparedStatement.setLong(4, company.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update company failed " + company.toString());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	private Company createCompanyFromResultSet(ResultSet resultSet) throws SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("id"));
		company.setName(resultSet.getString("name"));
		company.setAddress(resultSet.getString("address"));
		company.setPhone(resultSet.getString("phone_number"));
		return company;
	}

	public Company getCompany(long id) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM companies WHERE id=?";
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, id);
			preparedStatement.executeQuery();
			ResultSet resultSet = preparedStatement.getResultSet();
			if (resultSet.next()) {
				Company company = createCompanyFromResultSet(resultSet);
				return company;
			}
			return null;
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company failed " + id);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public List<Company> getAllCompanies() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		List<Company> companies = new ArrayList<Company>();
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM companies";
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.executeQuery();
			ResultSet resultSet = preparedStatement.getResultSet();
			while (resultSet.next())  {
				companies.add(createCompanyFromResultSet(resultSet));
			}
			return companies;
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all companies failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public boolean isCompanyNameExist(String name) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT name FROM companies where name = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, name);
			preparedStatement.executeQuery();
			ResultSet resultSet = preparedStatement.getResultSet();
			if (resultSet.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is company name exist failed " + name);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

}
