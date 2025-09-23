package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;

import java.util.Optional;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    Optional<Restaurant> findByNit(String nit);

    boolean existsByIdAndOwnerId(Long idRestaurant, Long idOwner);

    PageResult<RestaurantResult> getListRestaurant(PageInfo pageInfo);
}
