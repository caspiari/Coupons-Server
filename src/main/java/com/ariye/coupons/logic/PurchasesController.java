package com.ariye.coupons.logic;

import java.sql.Date;
import java.util.List;

import com.ariye.coupons.dto.CouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ariye.coupons.dao.IPurchasesDao;
import com.ariye.coupons.dto.PurchaseDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Coupon;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.entities.User;
import com.ariye.coupons.enums.ErrorType;
import com.ariye.coupons.enums.UserType;
import com.ariye.coupons.exeptions.ApplicationException;

import javax.annotation.PostConstruct;


@Controller
public class PurchasesController {

    @Autowired
    private IPurchasesDao purchasesDao;
    @Autowired
    private CouponsController couponsController;
    @Autowired
    private UsersController usersController;


    public Long createPurchase(PurchaseDto purchaseDto, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            purchaseDto.setUserId(userLoginData.getId());
        }
        Date now = new Date(System.currentTimeMillis());
        purchaseDto.setTimestamp(now);
        Purchase purchase = this.createPurchaseFromDto(purchaseDto);
        Coupon coupon = purchase.getCoupon();
        this.validateCreatePurchase(purchase, coupon);
        this.couponsController.updateCouponAmount(coupon, purchase);
        try {
            purchase = this.purchasesDao.save(purchase);
            return purchase.getId();
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase failed " + purchaseDto.toString());
        }
    }

    public PurchaseDto getPurchase(long id, UserLoginData userLoginData) throws ApplicationException {
        try {
            PurchaseDto purchaseDto = this.purchasesDao.getById(id);
            validateGetPurchase(purchaseDto, userLoginData);
            return purchaseDto;
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                throw e;
            }
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchase failed " + id);
        }
    }

    public void deletePurchase(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            if (!(this.purchasesDao.existsById(id))) {
                throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Purchase id");
            }
            this.purchasesDao.deleteById(id);
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                throw e;
            }
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Delete purchase failed " + id);
        }

    }

    public List<PurchaseDto> getAllPurchases(UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        try {
            List<PurchaseDto> purchasesDtos = this.purchasesDao.getAll();
            return purchasesDtos;
        } catch (Exception e) {
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Get all purchases failed");
        }
    }

    public List<PurchaseDto> getAllPurchasesByUserId(long userId, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            userId = userLoginData.getId();
        }
        try {
            List<PurchaseDto> purchasesDtos = this.purchasesDao.findAllByUserId(userId);
            return purchasesDtos;
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
            List<PurchaseDto> purchasesDtos = this.purchasesDao.findAllByCompanyID(companyId);
            return purchasesDtos;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get all purchases by company id failed " + companyId);
        }
    }

    /**
     * - This method is used for creation. Return entity with null id
     */
    private Purchase createPurchaseFromDto(PurchaseDto purchaseDto) throws ApplicationException {
        User user = this.usersController.getUser(purchaseDto.getUserId());
        Coupon coupon = this.couponsController.getEntity(purchaseDto.getCouponId());
        Purchase purchase = new Purchase(user, coupon, purchaseDto.getAmount(), purchaseDto.getTimestamp());
        return purchase;
    }

    // Validations:

    private void validateCreatePurchase(Purchase purchase, Coupon coupon) throws ApplicationException {
        if (purchase.getAmount() > coupon.getAmount()) {
            throw new ApplicationException(ErrorType.NOT_ENOUGH_COUPONS_LEFT);
        }
        if (coupon.getEndDate().before(purchase.getTimestamp())) {
            throw new ApplicationException(ErrorType.COUPON_EXPIERED);
        }
        if (purchase.getUser().getUserType() != UserType.CUSTOMER) {
            throw new ApplicationException(ErrorType.INVALID_VALUE, "Only customers can purchase coupons");
        }
    }

    private void validateGetPurchase(PurchaseDto purchaseDto, UserLoginData userLoginData) throws ApplicationException {
        if (purchaseDto == null) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Purchase id");
        }
        if (userLoginData.getUserType() == UserType.CUSTOMER) {
            if (userLoginData.getId() != purchaseDto.getUserId()) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "Purchase belongs to someone else " + userLoginData.toString());
            }
        } else if (userLoginData.getUserType() == UserType.COMPANY) {
            CouponDto couponDto = this.couponsController.getCoupon(purchaseDto.getCouponId());
            if (couponDto.getCompanyId() != userLoginData.getCompanyId()) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "Purchase belongs to someone else " + userLoginData.toString());
            }
        }
    }


}
