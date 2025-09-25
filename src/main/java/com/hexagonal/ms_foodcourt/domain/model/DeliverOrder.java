package com.hexagonal.ms_foodcourt.domain.model;

public class DeliverOrder {

    private Long orderId;

    private String pin;

    public DeliverOrder(Long orderId, String pin) {
        this.orderId = orderId;
        this.pin = pin;
    }

    public DeliverOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
