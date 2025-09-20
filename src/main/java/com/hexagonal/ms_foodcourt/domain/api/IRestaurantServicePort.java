package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);

}
