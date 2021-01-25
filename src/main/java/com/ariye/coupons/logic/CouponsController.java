package com.ariye.coupons.logic;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.CouponsDao;
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
public class CouponsController {

    @Autowired
    private CouponsDao couponsDao;
    @Autowired
    UsersController usersController;
    @Autowired
    private CompaniesController companiesController;
    @Autowired
    private  PurchasesController purchasesController;

    public long createCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        this.validateCreateCoupon(couponDto, userLoginData);
        if (this.isCouponExistByName(couponDto.getName())) {
            throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS, "Coupon name");
        }
        Coupon coupon = this.createCouponFromDto(couponDto, userLoginData);
        try {
            coupon = this.couponsDao.save(coupon);
            long id = coupon.getId();
            return id;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create coupon failed " + couponDto.toString());
        }
    }

    public CouponDto getCoupon(long id) throws ApplicationException {
        if (!(this.isCouponExist(id))) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
        }
        try {
            CouponDto couponDto = this.couponsDao.getById(id);
            return couponDto;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupon failed " + id);
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
            this.couponsDao.save(coupon);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon failed " + couponDto.toString());
        }
    }

    // Default access modifier because being used from purchases controller
    void updateCouponAmount(Coupon coupon, Purchase purchase) throws ApplicationException {
        coupon.setAmount(coupon.getAmount() - purchase.getAmount());
        try {
            this.couponsDao.save(coupon);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon amount failed " + coupon.toString());
        }
    }

    public void deleteCoupon(long id, UserLoginData userLoginData) throws ApplicationException {
        if (!(this.isCouponExist(id))) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
        }
        try {
            if (userLoginData.getUserType() != UserType.ADMIN) {
                Coupon coupon = this.couponsDao.findById(id).get();
                CouponDto couponDto = new CouponDto(coupon);
                if (couponDto.getCompanyId() != userLoginData.getCompanyId()) {
                    throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
                }
            }
            this.couponsDao.deleteById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete coupon failed " + id);
        }
    }

	public List<CouponDto> getCouponsByCompanyId(long id) throws ApplicationException {
		try {
			List<CouponDto> coupons = this.couponsDao.getByCompanyId(id);
            return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by company id failed " + id);
		}
	}

	public List<CouponDto> getCouponsByType(CouponType couponType) throws ApplicationException {
		try {
			List<CouponDto> coupons = this.couponsDao.getByCategory(couponType);
			return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by type failed " + couponType);
		}
	}

    public List<CouponDto> getPurchasedCouponsByMaxPrice(long userId, float maxPrice, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            userId = userLoginData.getId();
        }
        if (maxPrice < 1) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Max price must be a positive number");
        }
        try {
            List<CouponDto> coupons = couponsDao.getByMaxPrice(userId, maxPrice);
            return coupons;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchased coupons by max price failed " + userId);
        }
    }

    public List<CouponDto> getAllCoupons() throws ApplicationException {
        try {
            List<CouponDto> coupons = this.couponsDao.getAll();
            return coupons;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all coupons failed");
        }
    }

	public void deleteExpiredCoupons() throws ApplicationException {
		try {
			this.purchasesController.deleteExpiredPurchases();
            Date now = new Date(System.currentTimeMillis());
            this.couponsDao.deleteExpiredCoupons(now);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired coupons failed");
		}
	}

	@PostConstruct
    void checkDeleteExpired() throws ApplicationException {
        this.deleteExpiredCoupons();
    }

    private Coupon createCouponFromDto(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        Company company = this.companiesController.getCompany(couponDto.getCompanyId(), userLoginData);
        Coupon coupon = new Coupon(couponDto.getName(), couponDto.getDescription(), couponDto.getPrice(), company,
                couponDto.getStartDate(), couponDto.getEndDate(), couponDto.getCategory(), couponDto.getAmount());
        return coupon;
    }

    // Validations:

    private boolean isCouponExistByName(String name) throws ApplicationException {
        try {
            CouponDto couponDto = this.couponsDao.getByName(name);
            if (couponDto != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon exist by name failed" + name);
        }
    }

    private boolean isCouponExist(long id) throws ApplicationException {
        try {
            return this.couponsDao.existsById(id);
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon exist failed " + id);
        }
    }

    // Default access modifier because being used by another controller
    boolean isCouponAvailable(long id) throws ApplicationException {
        try {
            Coupon coupon = couponsDao.findById(id).get();
            if (coupon.getAmount() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon available failed");
        }
    }

    private void validateCreateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        // if (couponsDao.isCouponNameExist(coupon.getName())) { -Apply after third layer
        // throw new Exception("Coupon name already exist");
        // }
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
        if (couponDto.getPrice() < 0) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Price must be positive");
        }
        if (couponDto.getEndDate().before(Calendar.getInstance().getTime())) {
            throw new ApplicationException(ErrorType.INVALID_DATES);
        }
        if (couponDto.getEndDate().before(couponDto.getStartDate())) {
            throw new ApplicationException(ErrorType.INVALID_DATES);
        }
        if (couponDto.getAmount() < 0) {
            throw new ApplicationException(ErrorType.INVALID_AMOUNT, "Must be positive");
        }
    }


    private Coupon validateUpdateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
        this.validateCreateCoupon(couponDto, userLoginData);
        Coupon coupon;
        try {
            coupon = this.couponsDao.findById(couponDto.getId()).get();
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Validate update coupon failed " + couponDto.toString());
        }
        if (userLoginData.getUserType() == UserType.COMPANY) {
            if (userLoginData.getCompanyId() != coupon.getCompany().getId()) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "This coupon belongs to another company" + userLoginData.toString());
            }
        }
        return coupon;
    }

}
