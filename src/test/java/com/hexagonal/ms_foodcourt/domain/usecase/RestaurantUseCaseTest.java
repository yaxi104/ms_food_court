package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_ADMIN;
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
        Restaurant mockRestaurant = TestDataRestaurantFactory.mockRestaurant();
        UserAuth userMock = TestDataRestaurantFactory.mockUserAuth();
        when(userFeignPort.getUserByIdAuth(1L)).thenReturn(Optional.of(userMock));
        when(restaurantPersistencePort.findByNit(mockRestaurant.getNit())).thenReturn(Optional.empty());
        restaurantUseCase.saveRestaurant(mockRestaurant);

        verify(restaurantPersistencePort, times(1)).saveRestaurant(mockRestaurant);
    }

    @Test
    void saveRestaurantExistTest() {
        Restaurant mockRestaurant = TestDataRestaurantFactory.mockRestaurant();
        UserAuth userMock = TestDataRestaurantFactory.mockUserAuth();
        when(userFeignPort.getUserByIdAuth(1L)).thenReturn(Optional.of(userMock));
        when(restaurantPersistencePort.findByNit(mockRestaurant.getNit())).thenReturn(Optional.of(mockRestaurant));

        RestaurantAlreadyExistsException ex = assertThrows(RestaurantAlreadyExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }

    @Test
    void saveRestaurantOwnerNotExistsIdTest() {
        UserAuth userMock = TestDataRestaurantFactory.mockUserAuth();
        userMock.setId(2L);
        when(userFeignPort.getUserByIdAuth(1L)).thenReturn(Optional.of(userMock));
        saveRestaurantError(userMock);
    }

    @Test
    void saveRestaurantOwnerNotExistsRoleTest() {
        UserAuth userMock = TestDataRestaurantFactory.mockUserAuth();
        userMock.setId(1L);
        userMock.setRole(ROLE_ADMIN);
        when(userFeignPort.getUserByIdAuth(1L)).thenReturn(Optional.of(userMock));
        saveRestaurantError(userMock);
    }

    private void saveRestaurantError(UserAuth userMock) {
        var mockRestaurant = TestDataRestaurantFactory.mockRestaurant();
        UserNotExistsException ex = assertThrows(UserNotExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }
}