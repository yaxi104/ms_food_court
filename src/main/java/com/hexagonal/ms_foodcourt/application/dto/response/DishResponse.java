package com.hexagonal.ms_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponse {

    private Long id;

    private String name;

    private Integer price;

    private String description;

    private String imageUrl;

    private Long categoryId;

    private Long restaurantId;

    private String active;

}
