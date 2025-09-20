package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.handler.IRestaurantHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    @Override
    public void saveRestaurant(RestaurantRequest restaurantRequest) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(restaurantRequest));
    }

}
