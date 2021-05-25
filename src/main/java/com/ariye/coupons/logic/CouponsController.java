package com.ariye.coupons.logic;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.ICouponsDao;
import com.ariye.coupons.dto.CouponDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.enums.CouponType;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

import javax.annotation.PostConstruct;

@Controller
@EnableScheduling
public class CouponsController {

    @Autowired
    private ICouponsDao iCouponsDao;
    @Autowired
    private CompaniesController companiesController;

    public long createCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        this.validateCreateCoupon(couponDto, userLoginData);
        Coupon coupon = this.createCouponFromDto(couponDto, userLoginData);
        try {
            coupon = this.iCouponsDao.save(coupon);
            return coupon.getId();
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create coupon failed for: " + couponDto.toString());
        }
    }

    public CouponDto getCoupon(long id) throws ApplicationException {
        try {
            CouponDto couponDto = this.iCouponsDao.getById(id);
            if (couponDto == null) {
                throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
            }
            return couponDto;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupon failed for: " + id);
        }
    }

    Coupon getEntity(long id) throws ApplicationException {
        try {
            Coupon coupon = this.iCouponsDao.findById(id).get();
            return coupon;
        } catch (NoSuchElementException e) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupon entity failed for: " + id);
        }
    }

    public void updateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        Coupon coupon = this.validateUpdateCoupon(couponDto, userLoginData);
        coupon.setName(couponDto.getName());
        coupon.setDescription(couponDto.getDescription());
        coupon.setPrice(couponDto.getPrice());
        coupon.setStartDate(couponDto.getStartDate());
        coupon.setEndDate(couponDto.getEndDate());
        coupon.setCategory(couponDto.getCategory());
        coupon.setAmount(couponDto.getAmount());
        try {
            this.iCouponsDao.save(coupon);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon failed for: " + couponDto.toString());
        }
    }

    void updateCouponAmount(Coupon coupon, Purchase purchase) throws ApplicationException { //This method is being used by purchases controller
        coupon.setAmount(coupon.getAmount() - purchase.getAmount());
        try {
            this.iCouponsDao.save(coupon);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon amount failed for: " + coupon.toString());
        }
    }

    public void deleteCoupon(long id, UserLoginData userLoginData) throws ApplicationException {
        this.validateDeleteCoupon(id, userLoginData);
        try {
            this.iCouponsDao.deleteById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete coupon failed for: " + id);
        }
    }

    public List<CouponDto> getCouponsByCompanyId(long id) throws ApplicationException {
        try {
            List<CouponDto> couponsDtos = this.iCouponsDao.getByCompanyId(id);
            return couponsDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by company id failed for: " + id);
        }
    }

    public List<CouponDto> getCouponsByType(CouponType couponType) throws ApplicationException {
        try {
            List<CouponDto> couponsDtos = this.iCouponsDao.getByCategory(couponType);
            return couponsDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by type failed for: " + couponType);
        }
    }

    public List<CouponDto> getPurchasedCouponsByUserId(long userId, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            userId = userLoginData.getId();
        }
        try {
            List<CouponDto> couponsDtos = iCouponsDao.getByUserId(userId);
            return couponsDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchased coupons by user id failed for: " + userId);
        }
    }

    public List<CouponDto> getPurchasedCouponsByMaxPrice(long userId, float maxPrice, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            userId = userLoginData.getId();
        }
        if (maxPrice < 0) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Max price must be a positive number");
        }
        try {
            List<CouponDto> couponsDtos = iCouponsDao.getByMaxPrice(userId, maxPrice);
            return couponsDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchased coupons by max price failed for: " + userId);
        }
    }

    public List<CouponDto> getAllCoupons() throws ApplicationException {
        try {
            List<CouponDto> couponsDtos = this.iCouponsDao.getAll();
            return couponsDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all coupons failed");
        }
    }

    @Scheduled(cron = "0 30 1 * * *")
    public void deleteExpiredCoupons() throws ApplicationException {
        try {
            Date now = new Date(System.currentTimeMillis());
            this.iCouponsDao.deleteAllByEndDateBefore(now);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired coupons failed");
        }
    }

    private Coupon createCouponFromDto(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        Company company = this.companiesController.getEntity(couponDto.getCompanyId(), userLoginData);
        Coupon coupon = new Coupon(couponDto.getName(), couponDto.getDescription(), couponDto.getPrice(), company,
                couponDto.getStartDate(), couponDto.getEndDate(), couponDto.getCategory(), couponDto.getAmount());
        return coupon;
    }

/////////////// Validations:

    boolean isCouponAvailable(long id) throws ApplicationException {
        Long amount;
        try {
            amount = iCouponsDao.getAmount(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon available failed for: " + id);
        }
        if (amount == null) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
        }
        return amount > 0;
    }

    private void validateCreateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        Long id = this.iCouponsDao.getIdByNameCompanyIdAndId(couponDto.getName(), couponDto.getCompanyId(), couponDto.getId());
        if (id != null) {
            throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS);
        }
        if (userLoginData.getUserType() == UserType.CUSTOMER) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        if (couponDto.getName() == null) {
            throw new ApplicationException(ErrorType.MUST_ENTER_NAME);
        }
        if (couponDto.getDescription() == null) {
            throw new ApplicationException(ErrorType.MUST_INSERT_A_VALUE, "Description");
        }
        if (couponDto.getStartDate() == null) {
            throw new ApplicationException(ErrorType.INVALID_DATES, "Start date is null");
        }
        if (couponDto.getEndDate() == null) {
            throw new ApplicationException(ErrorType.INVALID_DATES, "End date is null");
        }
        if (couponDto.getName().length() < 2) {
            throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT);
        }
        if (couponDto.getPrice() < 1) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Price must be positive");
        }
        if (couponDto.getEndDate().before(Calendar.getInstance().getTime())) {
            throw new ApplicationException(ErrorType.INVALID_DATES);
        }
        if (couponDto.getEndDate().before(couponDto.getStartDate())) {
            throw new ApplicationException(ErrorType.INVALID_DATES);
        }
        if (couponDto.getAmount() < 1) {
            throw new ApplicationException(ErrorType.INVALID_AMOUNT, "Must be positive");
        }
    }

    private Coupon validateUpdateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        this.validateCreateCoupon(couponDto, userLoginData);
        Coupon coupon;
        try {
            coupon = this.iCouponsDao.findById(couponDto.getId()).get();
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Validate update coupon failed for: " + couponDto.toString());
        }
        if (userLoginData.getUserType() == UserType.COMPANY) {
            if (userLoginData.getCompanyId() != coupon.getCompany().getId()) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "This coupon belongs to another company" + userLoginData.toString());
            }
        }
        return coupon;
    }

    private void validateDeleteCoupon(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() == UserType.CUSTOMER) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        Long companyId;
        try {
            companyId = iCouponsDao.getCompanyId(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Validate delete coupon failed for: " + id + userLoginData.toString());
        }
        if (companyId == null) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
        }
        if (userLoginData.getUserType() == UserType.COMPANY && userLoginData.getCompanyId() != companyId) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "This coupon belongs to another company " + userLoginData.toString());
        }
    }


}
