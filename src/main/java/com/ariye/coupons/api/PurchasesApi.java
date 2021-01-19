package com.ariye.coupons.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ariye.coupons.dto.PurchaseDto;
import com.ariye.coupons.dto.UserLoginData;
import com.ariye.coupons.entities.Purchase;
import com.ariye.coupons.exeptions.ApplicationException;
import com.ariye.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {

	@Autowired
	PurchasesController purchasesController;

	@PostMapping
	public long createPurchase(@RequestBody PurchaseDto purchaseDto, HttpServletRequest request)
			throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		long id = purchasesController.createPurchase(purchaseDto, userLoginData);
		return id;
	}

	@GetMapping("/{purchaseId}")
	public Purchase getPurchase(@PathVariable("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		Purchase purchase = purchasesController.getPurchase(id, userLoginData);
		return purchase;
	}

	@DeleteMapping("/{purchaseId}")
	public void deletePurchase(@PathVariable("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		purchasesController.deletePurchase(id, userLoginData);
	}

	@GetMapping
	public List<Purchase> getAllPurchases(HttpServletRequest request) throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		List<Purchase> purchases = purchasesController.getAllPurchases(userLoginData);
		return purchases;
	}

	@GetMapping("/byUserId/{id}")
	public List<Purchase> getAllPurchasesByUserId(@PathVariable("id") long userId, HttpServletRequest request)
			throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		List<Purchase> purchases = purchasesController.getAllPurchasesByUserId(userId, userLoginData);
		return purchases;
	}

	@GetMapping("/byCompanyId/{id}")
	public List<Purchase> getAllPurchasesByCompanyId(@PathVariable("id") long companyId, HttpServletRequest request)
			throws ApplicationException {
		UserLoginData userLoginData = (UserLoginData) request.getAttribute("userLoginData");
		List<Purchase> purchases = purchasesController.getAllPurchasesByCompanyId(companyId, userLoginData);
		return purchases;
	}

}
