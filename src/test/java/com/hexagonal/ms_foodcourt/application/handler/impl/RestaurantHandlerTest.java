package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IRestaurantRequestMapper;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        int size = 10;

        RestaurantResult restaurantResult = new RestaurantResult();
        restaurantResult.setId(1L);
        restaurantResult.setName("Restaurante 1");

        List<RestaurantResult> restaurantResults = List.of(restaurantResult);

        PageResult<RestaurantResult> pageResult = new PageResult<>(
                restaurantResults,
                1,
                1L,
                true
        );

        RestaurantResponse response = new RestaurantResponse();
        response.setId(1L);
        response.setName("Restaurante 1");

        when(restaurantServicePort.getListRestaurant(page, size)).thenReturn(pageResult);
        when(restaurantRequestMapper.toRestaurantResponse(restaurantResult)).thenReturn(response);

        PaginatedResponse<RestaurantResponse> result = restaurantHandler.getListRestaurants(page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Restaurante 1", result.getContent().get(0).getName());
        assertEquals(1, result.getTotalPages());
        assertEquals(1L, result.getTotalElements());
        assertTrue(result.isLast());

        verify(restaurantServicePort).getListRestaurant(page, size);
        verify(restaurantRequestMapper).toRestaurantResponse(restaurantResult);
    }


}