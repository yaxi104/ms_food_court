package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
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

    @Test
    void getListRestaurantsSucccessTest() {
        int page = 0;
        int size = 2;

        RestaurantResult result1 = new RestaurantResult();
        result1.setName("Sushi Place");

        RestaurantResult result2 = new RestaurantResult();
        result2.setName("Burger Joint");

        Page<RestaurantResult> resultPage = new PageImpl<>(List.of(result1, result2));

        RestaurantResponse response1 = new RestaurantResponse();
        response1.setName("Sushi Place");

        RestaurantResponse response2 = new RestaurantResponse();
        response2.setName("Burger Joint");

        when(restaurantServicePort.getListRestaurant(page, size)).thenReturn(resultPage);
        when(restaurantRequestMapper.toRestaurantResponse(result1)).thenReturn(response1);
        when(restaurantRequestMapper.toRestaurantResponse(result2)).thenReturn(response2);

        Page<RestaurantResponse> responsePage = restaurantHandler.getListRestaurants(page, size);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        assertEquals("Sushi Place", responsePage.getContent().get(0).getName());
        assertEquals("Burger Joint", responsePage.getContent().get(1).getName());

        verify(restaurantServicePort).getListRestaurant(page, size);
        verify(restaurantRequestMapper, times(1)).toRestaurantResponse(result1);
        verify(restaurantRequestMapper, times(1)).toRestaurantResponse(result2);
    }


}