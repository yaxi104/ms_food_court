package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.mapper.IDishRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}