package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.CategoryNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.DishAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.TRUE_STATUS;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IUserFeignPort userFeignPort,
                       IUserSessionPort userSessionPort,
                       ICategoryPersistencePort categoryPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void saveDish(Dish dish) {
        Long categoryId = dish.getCategoryId();
        ValidateRequest.checkId(categoryId);
        categoryPersistencePort.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        ValidateRequest.checkNotBlank(dish.getName());
        ValidateRequest.checkPositive(dish.getPrice());
        ValidateRequest.checkNotBlank(dish.getDescription());
        ValidateRequest.checkUrl(dish.getImageUrl());
        ValidateRequest.checkId(dish.getRestaurantId());
        validateOwner(dish);

        dish.setActive(TRUE_STATUS);

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

    @Override
    public void toggleStatusDish(Dish dish) {
        Long id = dish.getId();
        ValidateRequest.checkId(id);
        ValidateRequest.checkStatus(dish.getActive());
        Dish dishDb = dishPersistencePort.findById(id).orElseThrow(DishNotFoundException::new);
        validateOwner(dishDb);
        dishDb.setActive(dish.getActive());
        dishPersistencePort.saveDish(dishDb);
    }

    @Override
    public PageResult<Dish> getListDish(Long restaurantId, Long categoryId, Integer page, Integer size) {
        PageInfo pageInfo = PageableHelper.getPageable(page, size, "price");
        return dishPersistencePort.listDishes(restaurantId, categoryId, pageInfo);
    }


    private void validateOwner(Dish dish) {
        User userOwner = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        if (!restaurantPersistencePort.existsByIdAndOwnerId(dish.getRestaurantId(), userOwner.getId())) {
            throw new UserForbiddenException();
        }
    }
}
