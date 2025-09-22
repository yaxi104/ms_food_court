package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.DishAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IUserFeignPort userFeignPort,
                       IUserSessionPort userSessionPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;

    }

    @Override
    public void saveDish(Dish dish) {
        ValidateRequest.checkNotBlank(dish.getName());
        ValidateRequest.checkPositive(dish.getPrice());
        ValidateRequest.checkNotBlank(dish.getDescription());
        ValidateRequest.checkUrl(dish.getImageUrl());
        ValidateRequest.checkNotBlank(dish.getCategory());
        ValidateRequest.checkId(dish.getRestaurantId());
        validateOwner(dish);

        dish.setActive(true);

        dishPersistencePort.findByNameAndRestaurantId(dish.getName(), dish.getRestaurantId()).ifPresent(d -> {
            throw new DishAlreadyExistsException();
        });

        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDish(Dish dish) {
        ValidateRequest.checkId(dish.getId());
        Dish dishDb = dishPersistencePort.findById(dish.getId()).orElseThrow(DishNotFoundException::new);
        validateOwner(dishDb);
        Integer price = dish.getPrice();
        if (price != null) {
            ValidateRequest.checkPositive(price);
            dishDb.setPrice(price);
        }

        String description = dish.getDescription();
        if (description != null && !description.isBlank()) {
            dishDb.setDescription(dish.getDescription());
        }
        dishPersistencePort.saveDish(dishDb);
    }

    private void validateOwner(Dish dish) {
        User userOwner = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        if (!restaurantPersistencePort.existsByIdAndOwnerId(dish.getRestaurantId(), userOwner.getId())) {
            throw new UserForbiddenException();
        }
    }
}
