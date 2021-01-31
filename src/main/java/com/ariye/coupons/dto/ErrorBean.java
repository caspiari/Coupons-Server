package com.ariye.coupons.dto;

public class ErrorBean {

    private int errorNumber;
    private String errorName;
    private String errorMessage;


    public ErrorBean(int errorNumber, String errorName, String errorMessage) {
        this.errorNumber = errorNumber;
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    public ErrorBean() {
    }


    public int getErrorNumber() {
        return errorNumber;
    }


    public String getErrorName() {
        return errorName;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    @Override
    public String toString() {
        return "ErrorBean [errorNumber=" + errorNumber + ", errorName=" + errorName + ", errorMessage=" + errorMessage
                + "]";
    }


}
