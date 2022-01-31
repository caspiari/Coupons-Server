package com.ariye.coupons.logic;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.ICompaniesDao;
import com.ariye.coupons.dto.CompanyDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

@Controller
public class CompaniesController {

    @Autowired
    private ICompaniesDao iCompaniesDao;

    public long createCompany(CompanyDto companyDto, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        Company company = this.createCompanyFromDto(companyDto);
        this.validateCreateCompany(company);
        try {
            company = this.iCompaniesDao.save(company);
            return company.getId();
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create company failed for: " + company.toString());
        }
    }

    public void deleteCompany(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            this.iCompaniesDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete company failed for: " + id);
        }
    }

    public CompanyDto getCompany(Long id) throws ApplicationException {
        try {
            CompanyDto companyDto = this.iCompaniesDao.getById(id);
            if (companyDto == null) {
                throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
            }
            return companyDto;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company failed for: " + id);
        }
    }

    public Company getEntity(Long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            id = userLoginData.getCompanyId();
        }
        try {
            Company company = this.iCompaniesDao.findById(id).get();
            return company;
        } catch (NoSuchElementException e) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get company failed for: " + id);
        }
    }

    public List<String> getNames() throws ApplicationException {
        try {
            List<String> companiesNames = iCompaniesDao.getNames();
            return companiesNames;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get companies names failed");
        }
    }

    public void updateCompany(CompanyDto companyDto, UserLoginData userLoginData) throws ApplicationException {
        Company company = this.createCompanyFromDto(companyDto);
        company.setId(companyDto.getId());
        if (userLoginData.getUserType() != UserType.ADMIN) {
            company.setId(userLoginData.getCompanyId());
        }
        validateCreateCompany(company); // Same validations
        try {
            this.iCompaniesDao.save(company);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Update company failed for: " + companyDto.toString());
        }
    }

    public List<CompanyDto> getAllCompanies() throws ApplicationException {
        try {
            List<CompanyDto> companies = this.iCompaniesDao.getAll();
            return companies;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all companies failed");
        }
    }

    /**
     * - Returns entity with null id (Used for create company)
     */
    private Company createCompanyFromDto(CompanyDto companyDto) {
        Company company = new Company(companyDto.getName(), companyDto.getAddress(), companyDto.getPhone());
        return company;
    }

///////////////// Validations:

    private boolean isCompanyExist(Long id) throws ApplicationException {
        if (id == null) {
            return false; //For inner flow
        }
        try {
            return this.iCompaniesDao.existsById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is company exist failed for: " + id);
        }
    }

    private void validateCreateCompany(Company company) throws ApplicationException {
        Long id = this.iCompaniesDao.getIdByNameAndId(company.getName(), company.getId());
        if (id != null) {
            throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS);
        }
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
