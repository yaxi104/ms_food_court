package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        saveRestaurantError();
    }

    @Test
    void saveRestaurantOwnerNotExistsRoleTest() {
        UserAuth userMock = TestDataRestaurantFactory.mockUserAuth();
        userMock.setId(1L);
        userMock.setRole(ROLE_ADMIN);
        when(userFeignPort.getUserByIdAuth(1L)).thenReturn(Optional.of(userMock));
        saveRestaurantError();
    }

    private void saveRestaurantError() {
        var mockRestaurant = TestDataRestaurantFactory.mockRestaurant();
        UserNotExistsException ex = assertThrows(UserNotExistsException.class, () -> {
            restaurantUseCase.saveRestaurant(mockRestaurant);
        });

        assertNotNull(ex);
    }

    @Test
    void getListRestaurantPagedResults() {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        RestaurantResult restaurant1 = new RestaurantResult();
        restaurant1.setName("Sushi Place");

        RestaurantResult restaurant2 = new RestaurantResult();
        restaurant2.setName("Burger Spot");

        Page<RestaurantResult> expectedPage = new PageImpl<>(List.of(restaurant1, restaurant2), pageable, 2);

        when(restaurantPersistencePort.getListRestaurant(pageable)).thenReturn(expectedPage);

        Page<RestaurantResult> actualPage = restaurantUseCase.getListRestaurant(page, size);

        assertNotNull(actualPage);
        assertEquals(2, actualPage.getContent().size());
        assertEquals("Sushi Place", actualPage.getContent().get(0).getName());
        assertEquals("Burger Spot", actualPage.getContent().get(1).getName());

        verify(restaurantPersistencePort).getListRestaurant(pageable);
    }
}