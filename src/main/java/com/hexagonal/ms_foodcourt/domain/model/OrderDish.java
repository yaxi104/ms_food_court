package com.hexagonal.ms_foodcourt.domain.model;

public class OrderDish {

    private Long idOrder;
    private Long idDish;
    private Integer quantity;

    public OrderDish(Long idOrder, Long idDish, Integer quantity) {
        this.idOrder = idOrder;
        this.idDish = idDish;
        this.quantity = quantity;
    }

    public OrderDish() {
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
