package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IDishRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.PageResult;
import com.hexagonal.ms_foodcourt.util.TestDataDishFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DishHandlerTest {

    private IDishServicePort dishServicePort;
    private IDishRequestMapper dishRequestMapper;
    private DishHandler dishHandler;

    @BeforeEach
    void setUp() {
        dishServicePort = mock(IDishServicePort.class);
        dishRequestMapper = mock(IDishRequestMapper.class);
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
    void getListDishesSuccessTest() {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 10;

        PageResult<Dish> pageResult = new PageResult<>(TestDataDishFactory.mockDishes(), 1, 1L, true);

        DishResponse expectedResponse = new DishResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Dish 1");

        Mockito.when(dishServicePort.getListDish(restaurantId, categoryId, page, size))
                .thenReturn(pageResult);

        Mockito.when(dishRequestMapper.toDishResponse(Mockito.any(Dish.class)))
                .thenReturn(expectedResponse);

        PaginatedResponse<DishResponse> result = dishHandler.getListDishes(restaurantId, categoryId, page, size);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Dish 1", result.getContent().get(0).getName());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(1L, result.getTotalElements());
        Assertions.assertTrue(result.isLast());

        Mockito.verify(dishServicePort).getListDish(restaurantId, categoryId, page, size);
        Mockito.verify(dishRequestMapper).toDishResponse(Mockito.any(Dish.class));
    }
}