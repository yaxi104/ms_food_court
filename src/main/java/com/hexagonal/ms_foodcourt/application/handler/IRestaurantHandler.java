package com.hexagonal.ms_foodcourt.application.handler;

import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;

public interface IRestaurantHandler {

    void saveRestaurant(RestaurantRequest restaurantRequest);

    PaginatedResponse<RestaurantResponse> getListRestaurants(Integer page, Integer size);
}
