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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ariye.coupons.dto.CouponDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.*;
import com.ariye.coupons.enums.CouponType;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {

    @Autowired
    private CouponsController couponsController;

    @PostMapping
    public long createCoupon(@RequestBody CouponDto couponDto, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        long id = this.couponsController.createCoupon(couponDto, userLoginData);
        return id;
    }

    @GetMapping("/{couponId}")
    public CouponDto getCoupon(@PathVariable("couponId") long id) throws ApplicationException {
        CouponDto couponDto = this.couponsController.getCoupon(id);
        return couponDto;
    }

    @PutMapping
    public void updateCoupon(@RequestBody CouponDto couponDto, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.couponsController.updateCoupon(couponDto, userLoginData);
    }

    @DeleteMapping("/{couponId}")
    public void deleteCoupon(@PathVariable("couponId") long id, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        this.couponsController.deleteCoupon(id, userLoginData);
    }

    @GetMapping("/byCompanyId/{companyId}")
    public List<CouponDto> getCouponsByCompanyId(@PathVariable("companyId") long companyId) throws ApplicationException {
        List<CouponDto> coupons = this.couponsController.getCouponsByCompanyId(companyId);
        return coupons;
    }

    @GetMapping
    public List<CouponDto> getAllCoupons() throws ApplicationException {
        List<CouponDto> coupons = this.couponsController.getAllCoupons();
        return coupons;
    }

    @GetMapping("/byType")
    public List<CouponDto> getCouponsByType(@RequestParam("type") CouponType type) throws ApplicationException {
        List<CouponDto> coupons = this.couponsController.getCouponsByType(type);
        return coupons;
    }

    @GetMapping("/byMaxPrice")
    public List<CouponDto> getPurchasedCouponsByMaxPrice(@RequestParam("userId") long userId,
                                                         @RequestParam("maxPrice") float maxPrice, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        List<CouponDto> coupons = this.couponsController.getPurchasedCouponsByMaxPrice(userId, maxPrice, userLoginData);
        return coupons;
    }

}
