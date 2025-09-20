package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.request.User;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserFeignPort userFeignPort;

    @Test
    void saveRestaurantSuccessTest() {
        Restaurant mockRestaurant = TestDataFactory.mockRestaurant();
        User userMock = TestDataFactory.mockUser();
        when(userFeignPort.getUserByid(1L)).thenReturn(Optional.of(userMock));
        when(restaurantPersistencePort.findByNit(mockRestaurant.getNit())).thenReturn(Optional.empty());
        restaurantUseCase.saveRestaurant(mockRestaurant);

        verify(restaurantPersistencePort, times(1)).saveRestaurant(mockRestaurant);
    }

    @Test
    void saveRestaurantExistTest() {
        Restaurant mockRestaurant = TestDataFactory.mockRestaurant();
        User userMock = TestDataFactory.mockUser();
        when(userFeignPort.getUserByid(1L)).thenReturn(Optional.of(userMock));
        when(restaurantPersistencePort.findByNit(mockRestaurant.getNit())).thenReturn(Optional.of(mockRestaurant));

        RestaurantAlreadyExistsException ex = assertThrows(RestaurantAlreadyExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }

    @Test
    void saveRestaurantOwnerNotExistsIdTest() {
        User userMock = new User();
        userMock.setId(2L);
        saveRestaurantError(userMock);
    }

    @Test
    void saveRestaurantOwnerNotExistsRoleTest() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setRole("CLIENT");
        saveRestaurantError(userMock);
    }

    private void saveRestaurantError(User userMock) {
        var mockRestaurant = TestDataFactory.mockRestaurant();
        when(userFeignPort.getUserByid(1L)).thenReturn(Optional.of(userMock));

        UserNotExistsException ex = assertThrows(UserNotExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }
}