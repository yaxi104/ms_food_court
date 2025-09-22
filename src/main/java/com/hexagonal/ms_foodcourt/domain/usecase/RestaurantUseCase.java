package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.RestaurantResult;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_PROPIETARIO;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPort userFeignPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserFeignPort userFeignPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPort = userFeignPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        Optional<UserAuth> user = userFeignPort.getUserByIdAuth(restaurant.getOwnerId());
        UserAuth userPresent = user.orElseThrow(UserNotExistsException::new);

        if (!userPresent.getId().equals(restaurant.getOwnerId()) || !userPresent.getRole().equals(ROLE_PROPIETARIO)) {
            throw new UserNotExistsException();
        }
        ValidateRequest.checkName(restaurant.getName());
        ValidateRequest.checkNit(restaurant.getNit());
        ValidateRequest.checkNotBlank(restaurant.getAddress());
        ValidateRequest.checkNumberPhone(restaurant.getPhoneNumber());
        ValidateRequest.checkUrl(restaurant.getUrlLogo());
        ValidateRequest.checkNullNumber(restaurant.getOwnerId());

        restaurantPersistencePort.findByNit(restaurant.getNit()).ifPresent(nit -> {
            throw new RestaurantAlreadyExistsException();
        });
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Page<RestaurantResult> getListRestaurant(int page, int size) {
        Pageable pageable = PageableHelper.getPageable(page, size, "name");
        return restaurantPersistencePort.getListRestaurant(pageable);
    }

}
