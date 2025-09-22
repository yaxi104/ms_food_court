package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
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

    @Test
    void getListRestaurantTest() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());

        RestaurantEntity entity1 = new RestaurantEntity();
        entity1.setName("A");

        RestaurantEntity entity2 = new RestaurantEntity();
        entity2.setName("B");

        Page<RestaurantEntity> entityPage = new PageImpl<>(List.of(entity1, entity2));

        RestaurantResult result1 = new RestaurantResult();
        result1.setName("A");

        RestaurantResult result2 = new RestaurantResult();
        result2.setName("B");

        when(restaurantRepository.findAllByOrderByNameAsc(pageable)).thenReturn(entityPage);
        when(restaurantEntityMapper.toResturantResult(entity1)).thenReturn(result1);
        when(restaurantEntityMapper.toResturantResult(entity2)).thenReturn(result2);

        Page<RestaurantResult> resultPage = restaurantJpaAdapter.getListRestaurant(pageable);

        assertEquals(2, resultPage.getContent().size());
        assertEquals("A", resultPage.getContent().get(0).getName());
        assertEquals("B", resultPage.getContent().get(1).getName());

        verify(restaurantRepository).findAllByOrderByNameAsc(pageable);
        verify(restaurantEntityMapper).toResturantResult(entity1);
        verify(restaurantEntityMapper).toResturantResult(entity2);
    }
}