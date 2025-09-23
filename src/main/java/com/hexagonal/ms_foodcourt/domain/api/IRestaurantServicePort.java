package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);

    PageResult<RestaurantResult> getListRestaurant(Integer page, Integer size);
}
