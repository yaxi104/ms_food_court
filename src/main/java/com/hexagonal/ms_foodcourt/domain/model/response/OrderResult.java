package com.hexagonal.ms_foodcourt.domain.model.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResult {

    private Long id;

    private Long idClient;

    private LocalDateTime date;

    private String status;

    private Long idRestaurant;

    private List<OrderDishResult> orderDishResponses;

    public OrderResult(Long id, Long idClient, LocalDateTime date, String status, Long idRestaurant, List<OrderDishResult> orderDishResponses) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idRestaurant = idRestaurant;
        this.orderDishResponses = orderDishResponses;
    }

    public OrderResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDishResult> getOrderDishResponses() {
        return orderDishResponses;
    }

    public void setOrderDishResponses(List<OrderDishResult> orderDishResponses) {
        this.orderDishResponses = orderDishResponses;
    }
}
