package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantMapper;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {
    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantMapper restaurantRequestMapper;

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
    void getListRestaurantsReturnsPaginatedResponseTest() {
        int page = 0;
        int size = 2;

        RestaurantResult restaurant1 = new RestaurantResult();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant 1");

        RestaurantResult restaurant2 = new RestaurantResult();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant 2");

        List<RestaurantResult> restaurantResults = List.of(restaurant1, restaurant2);
        PageResult<RestaurantResult> resultPage = new PageResult<>(restaurantResults, 3, 6, false);

        when(restaurantServicePort.getListRestaurant(page, size)).thenReturn(resultPage);

        RestaurantResponse response1 = new RestaurantResponse();
        response1.setId(1L);
        response1.setName("Restaurant 1");

        RestaurantResponse response2 = new RestaurantResponse();
        response2.setId(2L);
        response2.setName("Restaurant 2");

        List<RestaurantResponse> responseList = List.of(response1, response2);

        when(restaurantRequestMapper.toRestaurantResponseList(restaurantResults)).thenReturn(responseList);

        PaginatedResponse<RestaurantResponse> result = restaurantHandler.getListRestaurants(page, size);

        assertNotNull(result);
        assertEquals(responseList, result.getContent());
        assertEquals(3, result.getTotalPages());
        assertEquals(6, result.getTotalElements());
        assertFalse(result.isLast());

        verify(restaurantServicePort).getListRestaurant(page, size);
        verify(restaurantRequestMapper).toRestaurantResponseList(restaurantResults);
    }

}