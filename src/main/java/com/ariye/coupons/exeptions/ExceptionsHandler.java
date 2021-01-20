package com.ariye.coupons.exeptions;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import com.maxim.beans.ErrorBean;
//import com.maxim.enums.ErrorTypes;
//import com.ariye.enums.ApplicationException;


// Exception handler class
@RestControllerAdvice
public class ExceptionsHandler {

	//	Response - Object in Spring..
	@ExceptionHandler
	@ResponseBody
	// Variable name is throwable in order to remember that it handles Exception and Error
	public ErrorBean toResponse(Throwable throwable, HttpServletResponse response) {
		
		//	ErrorBean errorBean;
		if(throwable instanceof ApplicationException) {
		
			ApplicationException appException = (ApplicationException) throwable;

			ErrorTypes errorType = appException.getErrorType(); 
			int errorNumber = errorType.getErrorCode();
			String errorMessage = errorType.getErrorMessage();
			String errorName = errorType.getErrorName();
			response.setStatus(errorNumber);	

			ErrorBean errorBean = new ErrorBean(errorNumber, errorName ,errorMessage); 
			if(appException.getErrorType().isShowStackTrace()) {
				appException.printStackTrace();
			}
			
			return errorBean;
		}
		
		response.setStatus(600);

		String errorMessage = throwable.getMessage();
		ErrorBean errorBean = new ErrorBean(601, "General error", errorMessage);
		throwable.printStackTrace();

		return errorBean;
	}

}


//	@ExceptionHandler(ApplicationException.class)
//	public ErrorBean applicationExceptionHandler(HttpServletResponse response, ApplicationException applicationExction) {
//
//		ErrorType errorType = applicationExction.getErrorType(); 
//		int errorNumber = errorType.getErrorNumber();
//		String errorMessage = errorType.getErrorMessage();
//		String errorName = errorType.getErrorName();
//
//		ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage, errorName); 
//		response.setStatus(errorNumber);
//
//		//		check is critical - parameter in exceptions that we created
//		if(applicationExction.getErrorType().isShowStackTrace()) {
//			applicationExction.printStackTrace();
//		}
//
//		return errorBean;
//	}
//
//	@ExceptionHandler(Exception.class)
//	public ErrorBean ExceptionHandler(HttpServletResponse response, Exception exception) {
//
//		int errorNumber = 601;
//		String errorMessage = exception.getMessage();
//
//		ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage, "GENERAL ERROR");
//		response.setStatus(errorNumber);
//		exception.printStackTrace();
//
//		return errorBean;
//	}


