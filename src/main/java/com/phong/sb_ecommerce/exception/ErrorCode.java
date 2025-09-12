package com.phong.sb_ecommerce.exception;

public enum ErrorCode {
    SUCCESS(1000, "Success"),
    USER_EXISTED(1001, "User already existed"),
    USER_NOT_FOUND(1002, "User not found"),
    UNCATEGORIZED(9999, "Uncategorized Error"),
    ;
    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
