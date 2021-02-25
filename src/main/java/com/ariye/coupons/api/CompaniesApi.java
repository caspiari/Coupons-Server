package com.ariye.coupons.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ariye.coupons.dto.CompanyDto;
import com.ariye.coupons.dto.UserLoginData;
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
    public CompanyDto getCompany(@PathVariable("companyId") long id) throws ApplicationException {
        CompanyDto companyDto = this.companiesController.getCompanyDto(id);
        return companyDto;
    }

    @PutMapping
    public void updateCompany(@RequestBody CompanyDto companyDto, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.companiesController.updateCompany(companyDto, userLoginData);
    }

    @DeleteMapping("/{companyId}")
    public void deleteCompany(@PathVariable("companyId") long id, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.companiesController.deleteCompany(id, userLoginData);
    }

    @GetMapping
    public List<CompanyDto> getAllCompanies() throws ApplicationException {
        List<CompanyDto> companies = this.companiesController.getAllCompanies();
        return companies;
    }

}
