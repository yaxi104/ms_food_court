package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter;

import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public Optional<Restaurant> findByNit(String nit) {
        return restaurantRepository.findByNit(nit)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public boolean existsByIdAndOwnerId(Long idRestaurant, Long idOwner) {
        return restaurantRepository.existsByIdAndOwnerId(idRestaurant, idOwner);
    }

    @Override
    public PageResult<RestaurantResult> getListRestaurant(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(pageInfo.getSortBy()));
        Page<RestaurantEntity> page = restaurantRepository.findAllByOrderByNameAsc(pageable);
        List<RestaurantResult> content = restaurantEntityMapper.toDtoList(page.getContent());
        return new PageResult<>(content, page.getTotalPages(), page.getTotalElements(), page.isLast());
    }
}
