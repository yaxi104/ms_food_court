package com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    RESTAURANT_ALREADY_EXISTS("Restaurant already exists"),
    BAD_REQUEST_MESSAGE("The request contains invalid data. Please check the submitted fields and try again"),
    UNATHORIZED_MESSAGE("Authentication is required to access this resource"),
    FORBIDDEN_MESSAGE("You do not have permission to access this resource"),
    DISH_NOT_RESTAURANT("Please select dishes from only one restaurant per order"),
    ORDER_CLIENT_EXISTS("You already have an active order. Please wait until it's completed"),
    CATEGORY_NOT_FOUND("The category not found"),
    DISH_NOT_FOUND("The dish not found"),
    DISH_ALREADY_EXISTS("Dish already exists"),
    USER_NOT_EXISTS("The user does not exist as an owner"),
    ORDER_NOT_STATUS_ASSIGNED("Order status is not PENDIENTE and cannot be assigned"),
    ORDER_NOT_FOUND("The dish not found");

    private String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}