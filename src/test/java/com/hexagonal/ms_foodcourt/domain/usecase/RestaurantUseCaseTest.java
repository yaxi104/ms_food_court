package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        Integer page = 0;
        Integer size = 10;

        PageInfo expectedPageInfo = PageableHelper.getPageable(page, size, "name");

        RestaurantResult restaurantResult = new RestaurantResult();
        restaurantResult.setId(1L);
        restaurantResult.setName("Restaurante 1");

        PageResult<RestaurantResult> expectedPageResult = new PageResult<>(
                List.of(restaurantResult),
                1,
                1L,
                true
        );

        when(restaurantPersistencePort.getListRestaurant(any(PageInfo.class))).thenReturn(expectedPageResult);

        PageResult<RestaurantResult> actualPageResult = restaurantUseCase.getListRestaurant(page, size);

        assertNotNull(actualPageResult);
        assertEquals(expectedPageResult.getContent().size(), actualPageResult.getContent().size());
        assertEquals(expectedPageResult.getTotalPages(), actualPageResult.getTotalPages());
        assertEquals(expectedPageResult.getTotalElements(), actualPageResult.getTotalElements());
        assertEquals(expectedPageResult.isLast(), actualPageResult.isLast());

        verify(restaurantPersistencePort).getListRestaurant(argThat(pageInfo ->
                pageInfo.getPage() == expectedPageInfo.getPage() &&
                        pageInfo.getSize() == expectedPageInfo.getSize() &&
                        pageInfo.getSortBy().equals(expectedPageInfo.getSortBy())
        ));
    }

}