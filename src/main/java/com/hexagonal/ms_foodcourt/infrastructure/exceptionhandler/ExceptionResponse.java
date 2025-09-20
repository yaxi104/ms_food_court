package com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    RESTAURANT_ALREADY_EXISTS("Restaurant already exists"),
    BAD_REQUEST_MESSAGE("The request contains invalid data. Please check the submitted fields and try again"),
    UNATHORIZED_MESSAGE("Authentication is required to access this resource"),
    USER_NOT_EXISTS("The user does not exist as an owner");
    private String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}