package com.ariye.coupons.logic;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.PurchasesDao;
import com.ariye.coupons.dto.PurchaseDto;
import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Company;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

@Controller
public class PurchasesController {

	@Autowired
	private PurchasesDao purchasesDao;
	@Autowired
	private CouponsController couponsController;
	@Autowired
	private UsersController usersController;

//	public Long createPurchase(PurchaseDto purchaseDto, UserLoginData userLoginData) throws ApplicationException {
//		if (userLoginData.getUserType() != UserType.ADMIN) {
//			purchaseDto.setUserId(userLoginData.getId());
//		}
//		Timestamp now = new Timestamp(System.currentTimeMillis());
//		purchaseDto.setTimestamp(now);
//		Purchase purchase = this.createPurchaseFromDto(purchaseDto, userLoginData);
//		Coupon coupon = purchase.getCoupon();
//		this.validateCreatePurchase(purchase, coupon);
//		this.couponsController.updateCouponAmount(coupon, purchase);
//		try {
//			purchase = this.purchasesDao.save(purchase);
//			long id = purchase.getId();
//			return id;
//		} catch (Exception e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
//					"Create purchase failed " + purchaseDto.toString());
//		}
//	}

	public PurchaseDto getPurchase(long id) throws ApplicationException {
		if (!(this.isPurchaseExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Purchase id");
		}
		try {
			PurchaseDto purchaseDto = this.purchasesDao.getById(id);
			return purchaseDto;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchase failed " + id);
		}
	}

	public void deletePurchase(long id, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
		}
		if (!(this.isPurchaseExist(id))) {
			throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Purchase id");
		}
		try {
			this.purchasesDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Delete purchase failed " + id);
		}

	}

	public List<PurchaseDto> getAllPurchases(UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
		}
		try {
			List<PurchaseDto> purchases = (List<PurchaseDto>) this.purchasesDao.getAll();
			return purchases;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Get all purchases failed");
		}
	}

	@JsonIgnore
	public List<Purchase> getAllPurchasesByUserId(long userId, UserLoginData userLoginData)	throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			userId = userLoginData.getId();
		}
		try {
			List<Purchase> purchases = this.purchasesDao.findAllByUserId(userId);
			return purchases;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Get all purchases by user id failed " + userId);
		}
	}

	@JsonIgnore
	public List<Purchase> getAllPurchasesByCompanyId(long companyId, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			if (userLoginData.getUserType() == UserType.COMPANY) {
				companyId = userLoginData.getCompanyId();
			} else {
				throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
			}
		}
		try {
			List<Purchase> purchases = this.purchasesDao.findAllByCompanyID(companyId);
			return purchases;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases by company id failed " + companyId);
		}
	}

//	private Purchase createPurchaseFromDto(PurchaseDto purchaseDto, UserLoginData userLoginData) throws ApplicationException {
//		try {
//			UserDto userDto = this.usersController.getUser(purchaseDto.getUserId(), userLoginData);
//			User user = new User(userDto);
//			Coupon coupon = this.couponsController.getCoupon(purchaseDto.getCouponId());
//			Purchase purchase = new Purchase(purchaseDto.getId(), user, coupon, purchaseDto.getAmount(),
//					purchaseDto.getTimestamp());
//			return purchase;
//		} catch (Exception e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase from dto failed " + purchaseDto.toString());
//		}
//	}
//
//	private PurchaseDto createDtoFromPurchase(Purchase purchase) throws ApplicationException {
//		try {
//			User user = purchase.getUser();
//			Coupon coupon = purchase.getCoupon();
//			PurchaseDto purchaseDto = new PurchaseDto(purchase.getId(), user.getId(), coupon.getId(), purchase.getAmount(), purchase.getTimestamp());
//			return purchaseDto;
//		} catch (Exception e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,	"Create dto from purchase failed " + purchase.toString());
//		}
//	}
	
	@PostConstruct
	void checkGetAll() {
		System.out.println(this.purchasesDao.getAll());
	}

	// Validations:

	private boolean isPurchaseExist(long id) throws ApplicationException {
		try {
			return this.purchasesDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Is purchase exist failed " + id);
		}
	}

	private void validateCreatePurchase(Purchase purchase, Coupon coupon) throws ApplicationException {
		if (purchase.getAmount() > coupon.getAmount()) {
			throw new ApplicationException(ErrorType.NOT_ENOUGH_COUPONS_LEFT);
		}
		if (coupon.getEndDate().before(purchase.getTimestamp())) {
			throw new ApplicationException(ErrorType.COUPON_EXPIERED);
		}
	}


}
