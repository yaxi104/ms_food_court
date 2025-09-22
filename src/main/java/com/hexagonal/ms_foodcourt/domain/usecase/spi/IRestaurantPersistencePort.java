package com.hexagonal.ms_foodcourt.domain.usecase.spi;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;

import java.util.Optional;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    Optional<Restaurant> findByNit(String nit);

    boolean existsByIdAndOwnerId(Long idRestaurant, Long idOwner);
}
