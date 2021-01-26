package com.ariye.coupons.exeptions;

import com.ariye.coupons.enums.ErrorType;

public class ApplicationException extends Exception {

    private ErrorType errorType;

    public ApplicationException(Exception e, ErrorType errorType, String message) {
        super(errorType.getErrorMessage() + ". " + message, e);
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message) {
        super(errorType.getErrorMessage() + ". " + message);
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ApplicationException() {
        super();
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "ApplicationException [errorType=" + errorType + ", getMessage()=" + getMessage() +
                ", Cause: " + getCause() + "]";
    }

}
