package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;

import java.util.Optional;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    Optional<Restaurant> findByNit(String nit);
}
