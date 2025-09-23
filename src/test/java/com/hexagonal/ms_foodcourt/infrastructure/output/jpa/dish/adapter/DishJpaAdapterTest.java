package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.PageResult;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(0);
        pageInfo.setSize(2);
        pageInfo.setSortBy("price");

        Pageable expectedPageable = PageRequest.of(0, 2, Sort.by("price"));

        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setId(1L);
        dishEntity1.setName("Dish 1");

        DishEntity dishEntity2 = new DishEntity();
        dishEntity2.setId(2L);
        dishEntity2.setName("Dish 2");

        List<DishEntity> entities = List.of(dishEntity1, dishEntity2);
        Page<DishEntity> page = new PageImpl<>(entities, expectedPageable, 5);

        Long categoryId = 2L;
        when(dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, expectedPageable)).thenReturn(page);
        when(dishEntityMapper.toDish(dishEntity1)).thenReturn(new Dish.Builder().id(1L).name("Dish 1").build());
        when(dishEntityMapper.toDish(dishEntity2)).thenReturn(new Dish.Builder().id(2L).name("Dish 2").build());

        PageResult<Dish> pageResult = dishJpaAdapter.listDishes(restaurantId, categoryId, pageInfo);

        assertNotNull(pageResult);
        assertEquals(2, pageResult.getContent().size());
        assertEquals(5, pageResult.getTotalElements());
        assertEquals(3, pageResult.getTotalPages());
        assertFalse(pageResult.isLast());

        verify(dishRepository).findByRestaurantIdAndCategoryId(restaurantId, categoryId, expectedPageable);
    }

    @Test
    void shouldListDishesTest() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(0);
        pageInfo.setSize(2);
        pageInfo.setSortBy("price");

        Pageable expectedPageable = PageRequest.of(0, 2, Sort.by("price"));

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Dish 1");

        List<DishEntity> entities = List.of(dishEntity);
        Page<DishEntity> page = new PageImpl<>(entities, expectedPageable, 1);

        when(dishRepository.findByRestaurantId(restaurantId, expectedPageable)).thenReturn(page);
        when(dishEntityMapper.toDish(dishEntity)).thenReturn(new Dish.Builder().id(1L).name("Dish 1").build());

        PageResult<Dish> pageResult = dishJpaAdapter.listDishes(restaurantId, null, pageInfo);

        assertNotNull(pageResult);
        assertEquals(1, pageResult.getContent().size());
        assertEquals(1, pageResult.getTotalElements());
        assertEquals(1, pageResult.getTotalPages());
        assertTrue(pageResult.isLast());

        verify(dishRepository).findByRestaurantId(restaurantId, expectedPageable);
    }
}