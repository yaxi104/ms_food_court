package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public Optional<Restaurant> findByNit(String nit) {
        return restaurantRepository.findByNit(nit)
                .map(restaurantEntityMapper::toRestaurant);
    }
}
