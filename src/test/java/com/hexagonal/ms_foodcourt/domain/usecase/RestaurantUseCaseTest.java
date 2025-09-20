package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private IUserPersistencePort userPersistencePort;

    @Test
    void saveRestaurantSuccessTest() {
        var mockRestaurant = TestDataFactory.mockRestaurant();

        when(userPersistencePort.existsByIdAndRole(1L, "PROPIETARIO")).thenReturn(true);
        restaurantUseCase.saveRestaurant(mockRestaurant);

        verify(userPersistencePort).existsByIdAndRole(1L, "PROPIETARIO");
        verify(restaurantPersistencePort, times(1)).saveRestaurant(mockRestaurant);

    }

    @Test
    void saveRestaurantOwnerNotExistsTest() {
        var mockRestaurant = TestDataFactory.mockRestaurant();
        when(userPersistencePort.existsByIdAndRole(1L, "PROPIETARIO")).thenReturn(false);

        UserNotExistsException ex = assertThrows(UserNotExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }
}