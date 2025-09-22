package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.mapper.IDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository.IDishRepository;
import com.hexagonal.ms_foodcourt.util.TestDataDishFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    private DishJpaAdapter dishJpaAdapter;
    private final Long restaurantId = 1L;
    private final Long categoryId = 2L;
    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("price").ascending());

    @BeforeEach
    void setUp() {
        dishJpaAdapter = new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Test
    void saveDishSuccess() {
        Dish dish = TestDataDishFactory.mockDish();
        DishEntity entity = TestDataDishFactory.mockDishEntity();

        when(dishEntityMapper.toEntity(dish)).thenReturn(entity);

        dishJpaAdapter.saveDish(dish);

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(entity);
    }

    @Test
    void findByIdNotFoundTest() {
        Long dishId = 1L;
        DishEntity entity = TestDataDishFactory.mockDishEntity();
        Dish domainDish = TestDataDishFactory.mockDish();

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(entity));
        when(dishEntityMapper.toDish(entity)).thenReturn(domainDish);

        Optional<Dish> result = dishJpaAdapter.findById(dishId);

        assertTrue(result.isPresent());
        assertEquals(domainDish, result.get());
        verify(dishRepository).findById(dishId);
        verify(dishEntityMapper).toDish(entity);
    }

    @Test
    void findByIdReturnEmpty() {
        Long dishId = 1L;
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        Optional<Dish> result = dishJpaAdapter.findById(dishId);

        assertTrue(result.isEmpty());
        verify(dishRepository).findById(dishId);
        verify(dishEntityMapper, never()).toDish(any());
    }

    @Test
    void findByNameAndRestaurantIdSuccess() {
        String name = "Arroz con Pollo";

        DishEntity entity = TestDataDishFactory.mockDishEntity();
        Dish domainDish = TestDataDishFactory.mockDish();

        when(dishRepository.findByNameAndRestaurantId(name, restaurantId)).thenReturn(Optional.of(entity));
        when(dishEntityMapper.toDish(entity)).thenReturn(domainDish);

        Optional<Dish> result = dishJpaAdapter.findByNameAndRestaurantId(name, restaurantId);

        assertTrue(result.isPresent());
        assertEquals(domainDish, result.get());
        verify(dishRepository).findByNameAndRestaurantId(name, restaurantId);
        verify(dishEntityMapper).toDish(entity);
    }

    @Test
    void findByNameAndRestaurantIdEmpty() {
        String name = "Arroz con Pollo";

        when(dishRepository.findByNameAndRestaurantId(name, restaurantId)).thenReturn(Optional.empty());

        Optional<Dish> result = dishJpaAdapter.findByNameAndRestaurantId(name, restaurantId);

        assertTrue(result.isEmpty());
        verify(dishRepository).findByNameAndRestaurantId(name, restaurantId);
        verify(dishEntityMapper, never()).toDish(any());
    }

    @Test
    void shouldListDishesFilteredByCategory() {
        DishEntity entity = new DishEntity();
        Dish domainDish = new Dish();

        Page<DishEntity> entityPage = new PageImpl<>(List.of(entity));
        when(dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable)).thenReturn(entityPage);
        when(dishEntityMapper.toDish(entity)).thenReturn(domainDish);

        Page<Dish> result = dishJpaAdapter.listDishes(restaurantId, categoryId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(domainDish, result.getContent().get(0));
        verify(dishRepository).findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
    }

    @Test
    void shouldListDishesWithoutCategoryFilter() {
        DishEntity entity = new DishEntity();
        Dish domainDish = new Dish();

        Page<DishEntity> entityPage = new PageImpl<>(List.of(entity));
        when(dishRepository.findByRestaurantId(restaurantId, pageable)).thenReturn(entityPage);
        when(dishEntityMapper.toDish(entity)).thenReturn(domainDish);

        Page<Dish> result = dishJpaAdapter.listDishes(restaurantId, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(domainDish, result.getContent().get(0));
        verify(dishRepository).findByRestaurantId(restaurantId, pageable);
    }
}