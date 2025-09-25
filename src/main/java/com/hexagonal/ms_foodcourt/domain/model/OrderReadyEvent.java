package com.hexagonal.ms_foodcourt.domain.model;

public class OrderReadyEvent {

    private String phoneNumber;
    private String message;

    public OrderReadyEvent(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public OrderReadyEvent() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
