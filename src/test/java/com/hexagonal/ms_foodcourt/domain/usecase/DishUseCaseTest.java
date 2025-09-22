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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        User user = new User();
        user.setId(1L);

        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            dishUseCase.saveDish(dishDb);
        });
    }
}