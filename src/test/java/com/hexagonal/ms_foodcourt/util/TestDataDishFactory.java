package com.hexagonal.ms_foodcourt.util;

import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;

import java.util.List;


public class TestDataDishFactory {

    private TestDataDishFactory() {
    }

    public static Dish mockDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(100);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.url");
        dish.setCategoryId(1L);
        dish.setRestaurantId(1L);
        return dish;
    }

    public static DishRequest mockDishRequest() {
        DishRequest dish = new DishRequest();
        dish.setName("Pizza");
        dish.setPrice(100);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.url");
        dish.setCategoryId(1L);
        dish.setRestaurantId(1L);
        return dish;
    }

    public static DishUpdateRequest mockDishUpdateRequest() {
        DishUpdateRequest dish = new DishUpdateRequest();
        dish.setId(1L);
        dish.setPrice(100);
        dish.setDescription("Delicious pizza");
        return dish;
    }

    public static DishToggleStatusRequest mockDishToggleStatusRequest() {
        DishToggleStatusRequest dish = new DishToggleStatusRequest();
        dish.setId(1L);
        dish.setActive("true");
        return dish;
    }

    public static DishEntity mockDishEntity() {
        DishEntity dish = new DishEntity();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(100);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.url");
        dish.setCategoryId(1L);
        dish.setRestaurantId(1L);
        return dish;
    }

    public static List<Dish> mockDishes() {
        return List.of(
                new Dish.Builder()
                        .id(1L)
                        .name("Dish 1")
                        .price(10000)
                        .description("Delicious")
                        .imageUrl("https://img.com/1")
                        .categoryId(2L)
                        .restaurantId(1L)
                        .active("true")
                        .build()
        );
    }

}
