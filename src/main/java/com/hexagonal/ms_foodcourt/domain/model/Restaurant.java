package com.hexagonal.ms_foodcourt.domain.model;

public class Restaurant {

    private Long id;

    private String name;

    private String nit;

    private String address;

    private String phoneNumber;

    private String urlLogo;

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Restaurant(Long id, String name, String nit, String address, String phoneNumber, String urlLogo, Long ownerId) {
        this.id = id;
        this.name = name;
        this.nit = nit;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.urlLogo = urlLogo;
        this.ownerId = ownerId;
    }

    public Restaurant() {
    }
}
