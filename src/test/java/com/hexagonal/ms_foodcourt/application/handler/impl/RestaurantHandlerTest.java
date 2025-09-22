package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {
    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Test
    void saveRestaurantSuccessTest() {
        var restaurantRequest = TestDataRestaurantFactory.mockRestaurantRequest();
        var mockRestaurant = TestDataRestaurantFactory.mockRestaurant();

        when(restaurantRequestMapper.toRestaurant(restaurantRequest)).thenReturn(mockRestaurant);

        restaurantHandler.saveRestaurant(restaurantRequest);

        verify(restaurantRequestMapper).toRestaurant(restaurantRequest);
        verify(restaurantServicePort).saveRestaurant(mockRestaurant);

    }

}