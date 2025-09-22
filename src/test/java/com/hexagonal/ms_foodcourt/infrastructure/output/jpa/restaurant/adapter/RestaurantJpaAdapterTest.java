package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @Test
    void saveResturanteSuccessTest() {
        var mockEntity = TestDataRestaurantFactory.mockRestaurantEntity();
        var mockRestaurant = TestDataRestaurantFactory.mockRestaurant();

        when(restaurantEntityMapper.toEntity(mockRestaurant)).thenReturn(mockEntity);

        restaurantJpaAdapter.saveRestaurant(mockRestaurant);

        verify(restaurantRepository).save(mockEntity);

    }

    @Test
    void findByNitTest() {
        String nitMock = "123456";
        RestaurantEntity restaurantEntity = TestDataRestaurantFactory.mockRestaurantEntity();
        Restaurant mockRestaurant = TestDataRestaurantFactory.mockRestaurant();

        when(restaurantRepository.findByNit(nitMock)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurant(restaurantEntity)).thenReturn(mockRestaurant);

        Optional<Restaurant> result = restaurantJpaAdapter.findByNit(nitMock);

        assertTrue(result.isPresent());
        assertEquals(mockRestaurant, result.get());
        verify(restaurantRepository).findByNit(nitMock);
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity);

    }

    @Test
    void findByNITWhenRestaurantDoesNotExistTest() {
        String nitMock = "1234567";
        when(restaurantRepository.findByNit(nitMock)).thenReturn(Optional.empty());

        Optional<Restaurant> result = restaurantJpaAdapter.findByNit(nitMock);

        assertTrue(result.isEmpty());
        verify(restaurantRepository).findByNit(nitMock);
        verify(restaurantEntityMapper, never()).toRestaurant(any());
    }

    @Test
    void existsByIdAndOwnerIdSucces() {
        Long restaurantId = 1L;
        Long ownerId = 100L;

        when(restaurantRepository.existsByIdAndOwnerId(restaurantId, ownerId)).thenReturn(true);

        boolean exists = restaurantJpaAdapter.existsByIdAndOwnerId(restaurantId, ownerId);

        assertTrue(exists);
        verify(restaurantRepository).existsByIdAndOwnerId(restaurantId, ownerId);
    }

    @Test
    void existsByIdAndOwnerIdNotExist() {
        Long restaurantId = 1L;
        Long ownerId = 100L;

        when(restaurantRepository.existsByIdAndOwnerId(restaurantId, ownerId)).thenReturn(false);

        boolean exists = restaurantJpaAdapter.existsByIdAndOwnerId(restaurantId, ownerId);

        assertFalse(exists);
        verify(restaurantRepository).existsByIdAndOwnerId(restaurantId, ownerId);
    }
}