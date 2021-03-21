package com.ariye.coupons.api;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ariye.coupons.dto.PurchaseDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {

    @Autowired
    private PurchasesController purchasesController;

    @PostMapping
    public long createPurchase(@RequestBody PurchaseDto purchaseDto, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        long id = purchasesController.createPurchase(purchaseDto, userLoginData);
        return id;
    }

    @GetMapping("/{purchaseId}")
    public PurchaseDto getPurchase(@PathVariable("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        PurchaseDto purchaseDto = purchasesController.getPurchase(id, userLoginData);
        return purchaseDto;
    }

    @DeleteMapping("/{purchaseId}")
    public void deletePurchase(@PathVariable("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        purchasesController.deletePurchase(id, userLoginData);
    }

    @GetMapping
    public List<PurchaseDto> getAllPurchases(HttpServletRequest request) throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        List<PurchaseDto> purchases = purchasesController.getAllPurchases(userLoginData);
        return purchases;
    }

    @GetMapping("/byUserId")
    public List<PurchaseDto> getAllPurchasesByUserId(@RequestParam("id") long userId, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        List<PurchaseDto> purchases = purchasesController.getAllPurchasesByUserId(userId, userLoginData);
        return purchases;
    }

    @GetMapping("/byCompanyId")
    public List<PurchaseDto> getAllPurchasesByCompanyId(@RequestParam("id") long companyId, HttpServletRequest request)
            throws ApplicationException {
        UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
        List<PurchaseDto> purchases = purchasesController.getAllPurchasesByCompanyId(companyId, userLoginData);
        return purchases;
    }

}
