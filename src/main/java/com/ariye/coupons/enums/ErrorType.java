package com.ariye.coupons.enums;

public enum ErrorType {

    GENERAL_ERROR(601, "General error", true),
    LOGIN_FAILED(603, "Login failed. Please try again.", false),
    NAME_ALREADY_EXISTS(604, "The name you chose already exists. Please enter another name", false),
    MUST_ENTER_NAME(605, "Can not insert an null/empty name", false),
    MUST_ENTER_ADDRESS(606, "Can not insert an null/empty address", false),
    ID_DOES_NOT_EXIST(607, "This ID does'nt exist", false),
    //	NON_REPLACEABLE_NAME("Cannot change the name", false),
    INVALID_PASSWORD(608, "Password must contain at least 6 charaters, only UpperCase, letters and digits", false),
    NOT_ENOUGH_COUPONS_LEFT(609, "Not enough coupons left to purchase the amount requested", false),
    COUPON_EXPIERED(610, "The coupon is expiered", false),
    COUPON_TITLE_EXIST(611, "The title of this coupon is already exists, please change the title", false),
    INVALID_VALUE(612, "Invalid value", false),
    INVALID_EMAIL(613, "Email address is InValid, Please enter a valid email address", false),
    INVALID_AMOUNT(614, "Amount must be more than 0", false),
    INVALID_DATES(615, "The dates you've entered are wrong", false),
    MUST_INSERT_A_VALUE(616, "Must insert a value", false),
    ID_MUST_BE_POSITIVE(617, "ID must be positive", false),
    TYPE_DOES_NOT_EXIST(618, "This type doesn't exist", false),
    NAME_IS_TOO_SHORT(619, "Name must be at least 3 letters", false),
    USERNAME_DOES_NOT_EXIST(620, "This username doesn't exist", false),
    INVALID_ADDRESS(621, "Invalid address", false),
    INVALID_LOGIN_DETAILS(622, "Invalid login details", false),
    WRONG_PASSWORD(623, "Wrong password", false),
    UNAUTHORIZED_OPERATION(624, "You have no access to this action", true);


    private int errorNumber;
    private String errorMessage;
    private boolean isShowStackTrace;

    private ErrorType(int errorNumber, String internalMessage, boolean isShowStackTrace) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
        this.isShowStackTrace = isShowStackTrace;
    }

    private ErrorType(int errorNumber, String internalMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isShowStackTrace() {
        return this.isShowStackTrace;
    }

    public int getErrorNumber() {
        return this.errorNumber;
    }

}
