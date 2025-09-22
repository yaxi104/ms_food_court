package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.CategoryNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.DishAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.util.TestDataDishFactory;
import com.hexagonal.ms_foodcourt.util.TestDataUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DishUseCaseTest {
    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserFeignPort userFeignPort;
    private IUserSessionPort userSessionPort;
    private ICategoryPersistencePort categoryPersistencePort;
    private DishUseCase dishUseCase;

    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userFeignPort = mock(IUserFeignPort.class);
        userSessionPort = mock(IUserSessionPort.class);
        categoryPersistencePort = mock(ICategoryPersistencePort.class);

        dishUseCase = new DishUseCase(dishPersistencePort, restaurantPersistencePort, userFeignPort, userSessionPort, categoryPersistencePort);
    }

    @Test
    void saveDishSuccess() {
        Dish dish = TestDataDishFactory.mockDish();

        User user = TestDataUserFactory.mockUser();

        Category category = new Category(1L, "Comida rapida", "mock");
        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.of(category));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("test@example.com");
        when(userFeignPort.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(restaurantPersistencePort.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(dishPersistencePort.findByNameAndRestaurantId("Pizza", 1L)).thenReturn(Optional.empty());

        dishUseCase.saveDish(dish);
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void saveDishWhenDishExistsThrowsDishAlreadyExistsException() {
        Dish dish = TestDataDishFactory.mockDish();

        User user = new User();
        user.setId(10L);

        Category category = new Category(1L, "Comida rapida", "mock");
        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.of(category));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("test@example.com");
        when(userFeignPort.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(restaurantPersistencePort.existsByIdAndOwnerId(1L, 10L)).thenReturn(true);
        when(dishPersistencePort.findByNameAndRestaurantId("Pizza", 1L)).thenReturn(Optional.of(dish));

        assertThrows(DishAlreadyExistsException.class, () -> dishUseCase.saveDish(dish));
    }

    @Test
    void updateDishSuccess() {
        Dish dishToUpdate = TestDataDishFactory.mockDish();
        Dish dishDb = TestDataDishFactory.mockDish();

        User user = TestDataUserFactory.mockUser();

        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dishDb));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("user@example.com");
        when(userFeignPort.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(restaurantPersistencePort.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);

        dishUseCase.updateDish(dishToUpdate);

        verify(dishPersistencePort).saveDish(dishDb);
    }

    @Test
    void toggleStatusDishSuccess() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setActive("false");

        Dish dishDb = new Dish();
        dishDb.setId(1L);
        dishDb.setActive("true");
        dishDb.setRestaurantId(1L);

        User user = new User();
        user.setId(10L);

        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dishDb));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("user@example.com");
        when(userFeignPort.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(restaurantPersistencePort.existsByIdAndOwnerId(1L, 10L)).thenReturn(true);

        dishUseCase.toggleStatusDish(dish);

        assertEquals("false", dishDb.getActive());
        verify(dishPersistencePort).saveDish(dishDb);
    }

    @Test
    void validateOwnerWhenUserNotOwnerThrowsUserForbiddenException() {
        Dish dishDb = TestDataDishFactory.mockDish();

        User user = new User();
        user.setId(1L);

        Category category = new Category(1L, "Comida rapida", "mock");
        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.of(category));
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dishDb));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("user@example.com");
        when(userFeignPort.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(restaurantPersistencePort.existsByIdAndOwnerId(1L, 1L)).thenReturn(false);

        assertThrows(UserForbiddenException.class, () -> {
            dishUseCase.saveDish(dishDb);
        });
    }

    @Test
    void validateCategoryTes() {
        Dish dishDb = TestDataDishFactory.mockDish();

        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            dishUseCase.saveDish(dishDb);
        });
    }

    @Test
    void getListDishSuccessTest() {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());

        Dish dish1 = new Dish(1L, "Hamburguesa", 15000, "Con papas", "https://img.com/1", categoryId, restaurantId, "true");
        Dish dish2 = new Dish(2L, "Pizza", 20000, "Personal", "https://img.com/2", categoryId, restaurantId, "true");

        List<Dish> dishList = List.of(dish1, dish2);
        Page<Dish> dishPage = new PageImpl<>(dishList, pageable, dishList.size());

        when(dishPersistencePort.listDishes(restaurantId, categoryId, pageable)).thenReturn(dishPage);

        Page<Dish> result = dishUseCase.getListDish(restaurantId, categoryId, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Hamburguesa", result.getContent().get(0).getName());
        assertEquals("Pizza", result.getContent().get(1).getName());

        verify(dishPersistencePort).listDishes(restaurantId, categoryId, pageable);
    }
}