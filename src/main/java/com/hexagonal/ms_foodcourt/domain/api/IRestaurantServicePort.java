package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);

    Page<RestaurantResult> getListRestaurant(int page, int size);
}
