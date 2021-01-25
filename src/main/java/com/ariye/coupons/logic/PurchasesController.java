package com.ariye.coupons.logic;

import java.sql.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import com.ariye.coupons.dto.CouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.PurchasesDao;
import com.ariye.coupons.dto.PurchaseDto;
import com.ariye.coupons.dto.UserLoginData;
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
//		Date now = new Date(System.currentTimeMillis());
//		purchaseDto.setTimestamp(now);
//		Purchase purchase = this.createPurchaseFromDto(purchaseDto);
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

	public List<PurchaseDto> getAllPurchasesByUserId(long userId, UserLoginData userLoginData)	throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			userId = userLoginData.getId();
		}
		try {
			List<PurchaseDto> purchases = this.purchasesDao.findAllByUserId(userId);
			return purchases;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Get all purchases by user id failed " + userId);
		}
	}

	public List<PurchaseDto> getAllPurchasesByCompanyId(long companyId, UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData.getUserType() != UserType.ADMIN) {
			if (userLoginData.getUserType() == UserType.COMPANY) {
				companyId = userLoginData.getCompanyId();
			} else {
				throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
			}
		}
		try {
			List<PurchaseDto> purchases = this.purchasesDao.findAllByCompanyID(companyId);
			return purchases;
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases by company id failed " + companyId);
		}
	}

	/**
	 * - This method is used for creation. Return entity with null id
	 */
//	private Purchase createPurchaseFromDto(PurchaseDto purchaseDto) throws ApplicationException {
//		try {
//			User user = this.usersController.getEntity(purchaseDto.getUserId());
//			CouponDto couponDto = this.couponsController.getCoupon(purchaseDto.getCouponId());
//			Coupon coupon = this.couponsController.createCouponFro
//			Purchase purchase = new Purchase(user, coupon, purchaseDto.getAmount(), purchaseDto.getTimestamp());
//			return purchase;
//		} catch (Exception e) {
//			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase from dto failed " + purchaseDto.toString());
//		}
//	}

	public void deleteExpiredPurchases() throws ApplicationException {
	    Date now = new Date(System.currentTimeMillis());
	    try {
            this.purchasesDao.deleteExpiredPurchases(now);
        } catch (Exception e) {
	        throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Delete expired purchases failed");
        }
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
