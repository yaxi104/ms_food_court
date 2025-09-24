package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.handler.IRestaurantHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantMapper;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantMapper restaurantRequestMapper;

    @Override
    public void saveRestaurant(RestaurantRequest restaurantRequest) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(restaurantRequest));
    }

    @Override
    public PaginatedResponse<RestaurantResponse> getListRestaurants(Integer page, Integer size) {
        PageResult<RestaurantResult> resultPage = restaurantServicePort.getListRestaurant(page, size);

        List<RestaurantResponse> responseList = restaurantRequestMapper.toRestaurantResponseList(resultPage.getContent());

        return new PaginatedResponse<>(
                responseList,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.isLast()
        );
    }

}
