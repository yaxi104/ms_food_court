package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(0);
        pageInfo.setSize(2);
        pageInfo.setSortBy("name");

        RestaurantEntity entity1 = new RestaurantEntity();
        entity1.setId(1L);
        entity1.setName("Restaurant A");

        RestaurantEntity entity2 = new RestaurantEntity();
        entity2.setId(2L);
        entity2.setName("Restaurant B");

        List<RestaurantEntity> entities = List.of(entity1, entity2);
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(pageInfo.getSortBy()));
        Page<RestaurantEntity> entityPage = new PageImpl<>(entities, pageable, 5);

        when(restaurantRepository.findAllByOrderByNameAsc(pageable)).thenReturn(entityPage);

        RestaurantResult result1 = new RestaurantResult();
        result1.setId(1L);
        result1.setName("Restaurant A");

        RestaurantResult result2 = new RestaurantResult();
        result2.setId(2L);
        result2.setName("Restaurant B");

        List<RestaurantResult> expectedResults = List.of(result1, result2);

        when(restaurantEntityMapper.toDtoList(entities)).thenReturn(expectedResults);

        PageResult<RestaurantResult> pageResult = restaurantJpaAdapter.getListRestaurant(pageInfo);

        assertNotNull(pageResult);
        assertEquals(5, pageResult.getTotalElements());
        assertEquals(3, pageResult.getTotalPages());
        assertFalse(pageResult.isLast());
        assertEquals(expectedResults, pageResult.getContent());

        verify(restaurantRepository).findAllByOrderByNameAsc(pageable);
        verify(restaurantEntityMapper, times(1)).toDtoList(entities);
    }

}