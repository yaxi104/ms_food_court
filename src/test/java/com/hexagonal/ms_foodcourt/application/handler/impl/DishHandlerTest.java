package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IDishMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DishHandlerTest {

    private IDishServicePort dishServicePort;
    private IDishMapper dishRequestMapper;
    private DishHandler dishHandler;

    @BeforeEach
    void setUp() {
        dishServicePort = mock(IDishServicePort.class);
        dishRequestMapper = mock(IDishMapper.class);
        dishHandler = new DishHandler(dishServicePort, dishRequestMapper);
    }

    @Test
    void saveDishSuccessTest() {
        DishRequest request = new DishRequest();
        Dish dish = new Dish();
        when(dishRequestMapper.toDish(request)).thenReturn(dish);

        dishHandler.saveDish(request);

        verify(dishRequestMapper).toDish(request);
        verify(dishServicePort).saveDish(dish);
    }

    @Test
    void updateDishSucessTest() {
        DishUpdateRequest request = new DishUpdateRequest();
        Dish dish = new Dish();
        when(dishRequestMapper.toDishUpdate(request)).thenReturn(dish);

        dishHandler.updateDish(request);

        verify(dishRequestMapper).toDishUpdate(request);
        verify(dishServicePort).updateDish(dish);
    }

    @Test
    void toggleDishStatusSuccessTest() {
        DishToggleStatusRequest request = new DishToggleStatusRequest();
        Dish dish = new Dish();
        when(dishRequestMapper.toDishToggleStatus(request)).thenReturn(dish);

        dishHandler.toggleDishStatus(request);

        verify(dishRequestMapper).toDishToggleStatus(request);
        verify(dishServicePort).toggleStatusDish(dish);
    }

    @Test
    void getListDishesReturnsPaginatedResponseTest() {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 5;

        Dish dish1 = new Dish();
        dish1.setId(10L);
        dish1.setName("Plato 1");

        Dish dish2 = new Dish();
        dish2.setId(20L);
        dish2.setName("Plato 2");

        List<Dish> dishes = List.of(dish1, dish2);

        PageResult<Dish> pageResult = new PageResult<>(dishes, 3, 12, false);

        when(dishServicePort.getListDish(restaurantId, categoryId, page, size)).thenReturn(pageResult);

        DishResponse response1 = new DishResponse();
        response1.setId(10L);
        response1.setName("Plato 1");

        DishResponse response2 = new DishResponse();
        response2.setId(20L);
        response2.setName("Plato 2");

        List<DishResponse> responseList = List.of(response1, response2);

        when(dishRequestMapper.toDishResponseList(dishes)).thenReturn(responseList);

        PaginatedResponse<DishResponse> result = dishHandler.getListDishes(restaurantId, categoryId, page, size);

        assertNotNull(result);
        assertEquals(responseList, result.getContent());
        assertEquals(3, result.getTotalPages());
        assertEquals(12, result.getTotalElements());
        assertFalse(result.isLast());

        verify(dishServicePort).getListDish(restaurantId, categoryId, page, size);
        verify(dishRequestMapper).toDishResponseList(dishes);
    }
}