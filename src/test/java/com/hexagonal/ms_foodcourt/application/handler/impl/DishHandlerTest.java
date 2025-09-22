package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IDishRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void getListRestaurantsSuccessTest() {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 10;

        Dish dish1 = new Dish(1L, "Bandeja paisa", 25000, "Completa", "https://img.com/1", 2L, 1L, "true");
        Dish dish2 = new Dish(2L, "Arepa", 5000, "Con queso", "https://img.com/2", 2L, 1L, "true");
        List<Dish> dishList = List.of(dish1, dish2);
        Page<Dish> dishPage = new PageImpl<>(dishList, PageRequest.of(page, size), dishList.size());

        DishResponse response1 = new DishResponse();
        response1.setName("Bandeja paisa");
        response1.setPrice(25000);
        response1.setDescription("Completa");
        response1.setImageUrl("https://img.com/1");
        DishResponse response2 = new DishResponse();
        response1.setName("Arepa");
        response1.setPrice(5000);
        response1.setDescription("Con queso");
        response1.setImageUrl("https://img.com/2");

        when(dishServicePort.getListDish(restaurantId, categoryId, page, size)).thenReturn(dishPage);
        when(dishRequestMapper.toDishResponse(dish1)).thenReturn(response1);
        when(dishRequestMapper.toDishResponse(dish2)).thenReturn(response2);

        Page<DishResponse> result = dishHandler.getListRestaurants(restaurantId, categoryId, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        verify(dishServicePort).getListDish(restaurantId, categoryId, page, size);
        verify(dishRequestMapper).toDishResponse(dish1);
        verify(dishRequestMapper).toDishResponse(dish2);
    }
}