package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPort userFeignPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserFeignPort userFeignPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPort = userFeignPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        userFeignPort.getUserByid(restaurant.getOwnerId())
                .filter(user -> user.getId().equals(restaurant.getOwnerId()))
                .filter(user -> "PROPIETARIO".equals(user.getRole()))
                .orElseThrow(UserNotExistsException::new);

        ValidateRequest.checkName(restaurant.getName());
        ValidateRequest.checkNit(restaurant.getNit());
        ValidateRequest.checkNotBlank(restaurant.getAddress());
        ValidateRequest.checkNumberPhone(restaurant.getPhoneNumber());
        ValidateRequest.checkLogo(restaurant.getUrlLogo());
        ValidateRequest.checkNullNumber(restaurant.getOwnerId());

        if (restaurantPersistencePort.findByNit(restaurant.getNit()).isPresent()) {
            throw new RestaurantAlreadyExistsException();
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
