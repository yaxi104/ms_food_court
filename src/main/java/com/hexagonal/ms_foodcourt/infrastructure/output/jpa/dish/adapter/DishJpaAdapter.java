package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.mapper.IDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;

    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(Dish dish) {
        dishRepository.save(dishEntityMapper.toEntity(dish));
    }

    @Override
    public Optional<Dish> findById(Long idDish) {
        return dishRepository.findById(idDish).map(dishEntityMapper::toDish);
    }

    @Override
    public Optional<Dish> findByNameAndRestaurantId(String name, Long idRestaurante) {
        return dishRepository.findByNameAndRestaurantId(name, idRestaurante).map(dishEntityMapper::toDish);
    }

    @Override
    public Page<Dish> listDishes(Long restaurantId, Long categoryId, Pageable pageable) {
        Page<DishEntity> dishEntities;

        if (categoryId != null) {
            dishEntities = dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        } else {
            dishEntities = dishRepository.findByRestaurantId(restaurantId, pageable);
        }

        return dishEntities.map(dishEntityMapper::toDish);
    }

    @Override
    public Long countValidDishesByRestaurant(List<Long> dishIds, Long restaurantId) {
        return dishRepository.countValidDishesByRestaurant(dishIds, restaurantId);
    }
}
