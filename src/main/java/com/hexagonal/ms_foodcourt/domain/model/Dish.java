package com.hexagonal.ms_foodcourt.domain.model;

public class Dish {

    private Long id;

    private String name;

    private Integer price;

    private String description;

    private String imageUrl;

    private Long categoryId;

    private Long restaurantId;

    private String active;

    public Dish() {
    }

    private Dish(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.description = builder.description;
        this.imageUrl = builder.imageUrl;
        this.categoryId = builder.categoryId;
        this.restaurantId = builder.restaurantId;
        this.active = builder.active;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer price;
        private String description;
        private String imageUrl;
        private Long categoryId;
        private Long restaurantId;
        private String active;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder price(Integer price) { this.price = price; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public Builder restaurantId(Long restaurantId) { this.restaurantId = restaurantId; return this; }
        public Builder active(String active) { this.active = active; return this; }

        public Dish build() {
            return new Dish(this);
        }
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
