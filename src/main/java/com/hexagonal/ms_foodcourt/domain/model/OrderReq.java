package com.hexagonal.ms_foodcourt.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderReq {

    private Long idClient;
    private Long idRestaurant;
    private List<OrderDish> orderDishList;
    private LocalDateTime date;
    private String status;

    public OrderReq(Long idClient, Long idRestaurant, List<OrderDish> orderDishList, LocalDateTime date, String status) {
        this.idClient = idClient;
        this.idRestaurant = idRestaurant;
        this.orderDishList = orderDishList;
        this.date = date;
        this.status = status;
    }

    public OrderReq() {
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public List<OrderDish> getOrderDishList() {
        return orderDishList;
    }

    public void setOrderDishList(List<OrderDish> orderDishList) {
        this.orderDishList = orderDishList;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
