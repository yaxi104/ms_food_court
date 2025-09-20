package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void saveUserSuccessTest() {
        var mockEntity = TestDataFactory.mockRestaurantEntity();
        var mockRestaurant = TestDataFactory.mockRestaurant();

        when(restaurantRepository.findByNit("123456")).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toEntity(mockRestaurant)).thenReturn(mockEntity);

        restaurantJpaAdapter.saveRestaurant(mockRestaurant);

        verify(restaurantRepository).save(mockEntity);

    }

    @Test
    void saveUserExistsTest() {
        when(restaurantRepository.findByNit("123456")).thenReturn(Optional.of(TestDataFactory.mockRestaurantEntity()));
        var mockRestaurant = TestDataFactory.mockRestaurant();
        assertThrows(RestaurantAlreadyExistsException.class, () -> restaurantJpaAdapter.saveRestaurant(mockRestaurant));
    }
}