package com.hexagonal.ms_foodcourt.application.handler;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;

public interface IRestaurantHandler {

    void saveRestaurant(RestaurantRequest restaurantRequest);
}
