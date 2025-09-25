package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;

import java.util.Objects;

public class UserValidate {

    private UserValidate() {
    }

    public static void checkEmployee(Long idRestaurant, IUserFeignPort userFeignPort, IUserSessionPort userSessionPort) {
        User userEmployee = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        if (!Objects.equals(userEmployee.getRestaurantId(), idRestaurant)) {
            throw new UserForbiddenException();
        }
    }
}
