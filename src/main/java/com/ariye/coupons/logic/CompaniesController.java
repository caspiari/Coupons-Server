package com.ariye.coupons.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.CompaniesDao;
import com.ariye.coupons.dto.CompanyDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.PostConstruct;

@Controller
public class CompaniesController {

    @Autowired
    private CompaniesDao companiesDao;

    public long createCompany(CompanyDto companyDto, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        Company company = this.createCompanyFromDto(companyDto);
        this.validateUpdateCompany(company); // <--Same validations
        String name = company.getName();
        try {
            if (this.companiesDao.existsByName(name)) {
                throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS);
            }
            company = this.companiesDao.save(company);
            return company.getId();
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                throw e;
            }
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create company failed " + company.toString());
        }
    }

    public void deleteCompany(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        if (!(this.isCompanyExist(id))) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
        }
        try {
            this.companiesDao.deleteById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete company failed " + id);
        }
    }

    public CompanyDto getCompany(Long id, UserLoginData userLoginData) throws ApplicationException {
        id = this.validateGetCompany(id, userLoginData);
        try {
            CompanyDto companyDto = this.companiesDao.getById(id);
            return companyDto;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company failed " + id);
        }
    }

    Company getEntity(Long id, UserLoginData userLoginData) throws ApplicationException {
        id = this.validateGetCompany(id, userLoginData);
        try {
            Company company = this.companiesDao.findById(id).get();
            return company;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company entity failed " + id);
        }
    }

    public void updateCompany(CompanyDto companyDto, UserLoginData userLoginData) throws ApplicationException {
        Company company = this.createCompanyFromDto(companyDto);
        company.setId(companyDto.getId());
        if (userLoginData.getUserType() != UserType.ADMIN) {
            company.setId(userLoginData.getCompanyId());
        }
        validateUpdateCompany(company);
        try {
            this.companiesDao.save(company);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update company failed " + companyDto.toString());
        }
    }

    public List<CompanyDto> getAllCompanies(UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            List<CompanyDto> companies = this.companiesDao.getAll();
            return companies;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all companies failed");
        }
    }

    public CompanyDto getCompanyByName(String name, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            CompanyDto companyDto = this.companiesDao.getByName(name);
            return companyDto;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company by name failed " + name);
        }
    }

    /**
     * - Returns entity with null id
     */
    private Company createCompanyFromDto(CompanyDto companyDto) {
        Company company = new Company(companyDto.getName(), companyDto.getAddress(), companyDto.getPhone());
        return company;
    }

///////////////// Validations:

    boolean isCompanyExist(Long id) throws ApplicationException {
        if (id == null) {
            return false;
        }
        try {
            return this.companiesDao.existsById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is company exist failed" + id);
        }
    }

    private long validateGetCompany(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            id = userLoginData.getCompanyId();
        }
        if (!(this.isCompanyExist(id))) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
        }
        return id;
    }

    private void validateUpdateCompany(Company company) throws ApplicationException {
        // if (iCompaniesDao.isCompanyNameExist(company.getName())) { <->Apply after third layer
        // 		throw new Exception("Company name already exist");
        // }
        if (company.getName() == null) {
            throw new ApplicationException(ErrorType.MUST_ENTER_NAME);
        }
        if (company.getName().length() < 2) {
            throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT);
        }
        if (company.getAddress() == null) {
            throw new ApplicationException(ErrorType.MUST_ENTER_ADDRESS);
        }
        if (company.getAddress().length() < 9) {
            throw new ApplicationException(ErrorType.INVALID_ADDRESS);
        }
        if (company.getPhone() == null) {
            throw new ApplicationException(ErrorType.MUST_INSERT_A_VALUE, "Phone number");
        }
        if (company.getPhone().length() < 9) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Phone number");
        }
    }

}
