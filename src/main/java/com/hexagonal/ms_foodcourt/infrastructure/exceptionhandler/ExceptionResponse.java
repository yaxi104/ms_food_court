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
    ORDER_NOT_STATUS_READY("Order must be in 'IN_PREPARATION' status before it can be marked as 'READY"),
    ORDER_NOT_STATUS_DELIVERED("Order must be in 'LISTO' status before it can be marked as 'ENTREGADO"),
    CATEGORY_ALREADY_EXISTS("Category already exists"),
    SQS_SEND_ERROR("Ocurred error to send SQS"),
    PIN_INCORRECT("The PIN you entered is not valid"),
    ORDER_NOT_STATUS_CANCELED("We're sorry, your order is already being prepared and cannot be canceled"),
    ORDER_NOT_FOUND("The dish not found");

    private String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}