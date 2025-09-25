package com.hexagonal.ms_foodcourt.domain.model;

import java.time.LocalDateTime;

public class Order {

    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private String status;
    private Long idChef;
    private Long idRestaurant;
    private String pin;

    public Order() {
    }

    public Order(Long id, Long idClient, LocalDateTime date, Long idChef, String status, Long idRestaurant, String pin) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.idChef = idChef;
        this.status = status;
        this.idRestaurant = idRestaurant;
        this.pin = pin;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdChef() {
        return idChef;
    }

    public void setIdChef(Long idChef) {
        this.idChef = idChef;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
