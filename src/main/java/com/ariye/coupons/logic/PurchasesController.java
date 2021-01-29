package com.ariye.coupons.logic;

import java.sql.Date;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


@Controller
public class PurchasesController {

    @Autowired
    private PurchasesDao purchasesDao;
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
            long id = purchase.getId();
            return id;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase failed " + purchaseDto.toString());
        }
    }

    public PurchaseDto getPurchase(long id, UserLoginData userLoginData) throws ApplicationException {
        if (!(this.purchasesDao.existsById(id))) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST, "Purchase id");
        }
        try {
            PurchaseDto purchaseDto = this.purchasesDao.getById(id);
            if (userLoginData.getId() != purchaseDto.getUserId()) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, "Purchase belongs to someone else " + userLoginData.toString());
            }
            return purchaseDto;
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                throw e;
            }
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Get purchase failed " + id);
        }
    }

    @PostConstruct
    void checkGet() {
        System.out.println(purchasesDao.getById(2));
    }

    public void deletePurchase(long id, UserLoginData userLoginData) throws ApplicationException {
        if (userLoginData.getUserType() != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED_OPERATION, userLoginData.toString());
        }
        if (!(this.purchasesDao.existsById(id))) {
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

    public List<PurchaseDto> getAllPurchasesByUserId(long userId, UserLoginData userLoginData) throws ApplicationException {
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
    private Purchase createPurchaseFromDto(PurchaseDto purchaseDto) throws ApplicationException {
        try {
            User user = this.usersController.getEntity(purchaseDto.getUserId());
            Coupon coupon = this.couponsController.getEntity(purchaseDto.getCouponId());
            Purchase purchase = new Purchase(user, coupon, purchaseDto.getAmount(), purchaseDto.getTimestamp());
            return purchase;
        } catch (Exception e) {
            throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Create purchase from dto failed " + purchaseDto.toString());
        }
    }

    // Validations:

    private void validateCreatePurchase(Purchase purchase, Coupon coupon) throws ApplicationException {
        if (purchase.getAmount() > coupon.getAmount()) {
            throw new ApplicationException(ErrorType.NOT_ENOUGH_COUPONS_LEFT);
        }
        if (coupon.getEndDate().before(purchase.getTimestamp())) {
            throw new ApplicationException(ErrorType.COUPON_EXPIERED);
        }
    }


}
