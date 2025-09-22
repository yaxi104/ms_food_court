package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    Optional<Restaurant> findByNit(String nit);

    boolean existsByIdAndOwnerId(Long idRestaurant, Long idOwner);

    Page<RestaurantResult> getListRestaurant(Pageable pageable);
}
