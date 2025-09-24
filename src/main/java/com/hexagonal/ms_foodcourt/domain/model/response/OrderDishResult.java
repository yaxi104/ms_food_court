package com.hexagonal.ms_foodcourt.domain.model.response;

public class OrderDishResult {

    private Long idDish;
    private String nameDish;
    private Integer quantity;

    public OrderDishResult(Long idDish, String nameDish, Integer quantity) {
        this.idDish = idDish;
        this.nameDish = nameDish;
        this.quantity = quantity;
    }

    public OrderDishResult() {
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public String getNameDish() {
        return nameDish;
    }

    public void setNameDish(String nameDish) {
        this.nameDish = nameDish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
