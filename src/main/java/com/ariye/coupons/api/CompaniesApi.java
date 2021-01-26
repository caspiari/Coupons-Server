package com.ariye.coupons.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ariye.coupons.dto.CompanyDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {

    @Autowired
    private CompaniesController companiesController;

    @PostMapping
    public long createCompany(@RequestBody CompanyDto companyDto, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        long id = this.companiesController.createCompany(companyDto, userLoginData);
        return id;
    }

    @GetMapping("/{companyId}")
    public CompanyDto getCompany(@PathVariable("companyId") long id, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        CompanyDto companyDto = this.companiesController.getCompanyDto(id, userLoginData);
        return companyDto;
    }

    @PutMapping
    public void updateCompany(@RequestBody CompanyDto companyDto, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.companiesController.updateCompany(companyDto, userLoginData);
    }

    @DeleteMapping("/{companyId}")
    public void deleteCompany(@PathVariable("companyId") long id, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.companiesController.deleteCompany(id, userLoginData);
    }

    @GetMapping
    public List<Company> getAllCompanies(HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        List<Company> companies = this.companiesController.getAllCompanies(userLoginData);
        return companies;
    }

}
