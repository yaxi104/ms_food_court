package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var mockEntity = TestDataFactory.mockRestaurantEntity();
        var mockRestaurant = TestDataFactory.mockRestaurant();

        when(restaurantEntityMapper.toEntity(mockRestaurant)).thenReturn(mockEntity);

        restaurantJpaAdapter.saveRestaurant(mockRestaurant);

        verify(restaurantRepository).save(mockEntity);

    }

    @Test
    void findByNitTest() {
        String nitMock = "123456";
        RestaurantEntity restaurantEntity = TestDataFactory.mockRestaurantEntity();
        Restaurant mockRestaurant = TestDataFactory.mockRestaurant();

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
}