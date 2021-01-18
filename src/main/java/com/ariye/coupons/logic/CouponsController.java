package com.ariye.coupons.logic;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Controller
public class CouponsController {

	@Autowired
	private ICouponsDao iCouponsDao;
	@Autowired
	UsersController usersController;
	@Autowired
	private CompaniesController companiesController;

	public long createCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
		if (this.isCouponExistByName(couponDto.getName())) {
			throw new ApplicationException(ErrorType.NAME_ALREADY_EXISTS, "Coupon name");
		}
		Coupon coupon = this.createCouponFromCouponDto(couponDto, userLoginData);
		this.validateUpdateCoupon(coupon);// <- Same validations
		try {
			coupon = this.iCouponsDao.save(coupon);
			long id = coupon.getId();
			return id;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create coupon failed " + couponDto.toString());
		}
	}
	
	@JsonIgnore
	public Coupon getCoupon(long id) throws ApplicationException {
		if (!(this.isCouponExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
		}
		try {
			Coupon coupon = this.iCouponsDao.findById(id).get();
			return coupon;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupon failed " + id);
		}
	}

	// because it can also be updated by customer when
	// he buys coupon.. (for amount update).
	// I also made change in get purchases by company id
	// and all purchases and users api
	public void updateCoupon(CouponDto couponDto, UserLoginData userLoginData) throws ApplicationException {
		Coupon coupon = this.createCouponFromCouponDto(couponDto, userLoginData);
		coupon.setId(couponDto.getId());
		validateUpdateCoupon(coupon);
		try {
			this.iCouponsDao.save(coupon);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon failed " + couponDto.toString());
		}
	}

	// Used only from purchases controller
	void updateCouponAmount(Coupon coupon, Purchase purchase) throws ApplicationException {
		coupon.setAmount(coupon.getAmount() - purchase.getAmount());
		try {
			this.iCouponsDao.save(coupon);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "update coupon failed " + coupon.toString());
		}
	}

	public void deleteCoupon(long id, UserLoginData userLoginData) throws ApplicationException {
		if (!(this.isCouponExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Coupon id");
		}
		try {
			if (userLoginData.getUserType() != UserType.ADMIN) {
				Coupon coupon = this.iCouponsDao.findById(id).get();
				CouponDto couponDto = new CouponDto(coupon);
				if (couponDto.getCompanyId() != userLoginData.getCompanyId()) {
					throw new ApplicationException(ErrorType.INVALID_LOGIN_DETAILS);
				}
			}
			this.iCouponsDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete coupon failed " + id);
		}
	}

	@JsonIgnore
	public List<Coupon> getCouponsByCompanyId(long companyId) throws ApplicationException {
//		if (!(this.companiesController.isCompanyExist(companyId))) {
//			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Company id");
//		}
		try {
			List<Coupon> coupons = this.iCouponsDao.findByCompanyId(companyId);
			return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by company id failed " + companyId);
		}
	}

	@JsonIgnore
	public List<Coupon> getCouponsByType(CouponType couponType) throws ApplicationException {
		try {
			List<Coupon> coupons = this.iCouponsDao.findByCategory(couponType);
			return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get coupons by type failed " + couponType);
		}
	}

	@JsonIgnore
	public List<Coupon> getPurchasedCouponsByMaxPrice(long userId, float maxPrice, UserLoginData userLoginData)
			throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			userId = userLoginData.getId();
		}
		if (maxPrice < 1) {
			throw new ApplicationException(ErrorType.INVALID_VALUE, "Max price must be a positive number");
		}
		try {
			List<Coupon> coupons = iCouponsDao.findByMaxPrice(userId, maxPrice);
			return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"Get purchased coupons by max price failed " + userId);
		}
	}

	@JsonIgnore
	public List<Coupon> getAllCoupons() throws ApplicationException {
		try {
			List<Coupon> coupons = (List<Coupon>) this.iCouponsDao.findAll();
			return coupons;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all coupons failed");
		}
	}

	public void deleteExpiredCoupons() throws ApplicationException {
		try {
			long now = System.currentTimeMillis();
			this.iCouponsDao.deleteAllByEndDateLessThan(now);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired coupons failed");
		}
	}

	private Coupon createCouponFromCouponDto(CouponDto couponDto, UserLoginData userLoginData)
			throws ApplicationException {
		Company company = null;
		if (userLoginData.getUserType() == UserType.CUSTOMER) {
			throw new ApplicationException(ErrorType.INVALID_LOGIN_DETAILS);
		}
		company = this.companiesController.getCompany(couponDto.getCompanyId(), userLoginData);
		Coupon coupon = new Coupon(couponDto.getName(), couponDto.getDescription(), couponDto.getPrice(), company,
				couponDto.getStartDate(), couponDto.getEndDate(), couponDto.getCategory(), couponDto.getAmount());
		return coupon;
	}

	// Validations:

	private boolean isCouponExistByName(String name) throws ApplicationException {
		try {
			if (this.iCouponsDao.findCouponByName(name) != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon exist by name failed" + name);
		}
	}

	private boolean isCouponExist(long id) throws ApplicationException {
		try {
			return this.iCouponsDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon exist failed " + id);
		}
	}

	// not public because being used by another controller
	boolean isCouponAvailable(long id) throws ApplicationException {
		try {
			Coupon coupon = iCouponsDao.findById(id).get();
			if (coupon.getAmount() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is coupon available failed");
		}
	}

	private void validateUpdateCoupon(Coupon coupon) throws ApplicationException {
		// if (couponsDao.isCouponNameExist(coupon.getName())) { -Apply after
		// third layer
		// throw new Exception("Coupon name already exist");
		// }
		if (coupon.getName() == null) {
			throw new ApplicationException(ErrorType.MUST_ENTER_NAME);
		}
		if (coupon.getDescription() == null) {
			throw new ApplicationException(ErrorType.MUST_INSERT_A_VALUE, "Description");
		}
		if (coupon.getStartDate() == null) {
			throw new ApplicationException(ErrorType.INVALID_DATES, "Start date is null");
		}
		if (coupon.getEndDate() == null) {
			throw new ApplicationException(ErrorType.INVALID_DATES, "End date is null");
		}
		if (coupon.getName().length() < 2) {
			throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT);
		}
		if (coupon.getPrice() < 0) {
			throw new ApplicationException(ErrorType.INVALID_VALUE, "Price must be positive");
		}
		if (coupon.getEndDate().before(Calendar.getInstance().getTime())) {
			throw new ApplicationException(ErrorType.INVALID_DATES);
		}
		if (coupon.getEndDate().before(coupon.getStartDate())) {
			throw new ApplicationException(ErrorType.INVALID_DATES);
		}
		if (coupon.getAmount() < 0) {
			throw new ApplicationException(ErrorType.INVALID_AMOUNT, "Must be positive");
		}
	}

}
