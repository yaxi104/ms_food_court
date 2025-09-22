package com.hexagonal.ms_foodcourt.domain.model;

public class RestaurantResult {

    private String name;

    private String urlLogo;

    public RestaurantResult(String name, String urlLogo) {
        this.name = name;
        this.urlLogo = urlLogo;
    }

    public RestaurantResult() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
}
