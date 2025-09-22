package com.hexagonal.ms_foodcourt.application.handler;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {

    void saveRestaurant(RestaurantRequest restaurantRequest);

    Page<RestaurantResponse> getListRestaurants(int page, int size);
}
