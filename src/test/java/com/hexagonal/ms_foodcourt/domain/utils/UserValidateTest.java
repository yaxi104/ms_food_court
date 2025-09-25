package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserValidateTest {

    @Test
    void constructorIsPrivate() throws Exception {
        Constructor<UserValidate> constructor = UserValidate.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        UserValidate instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void checkEmployeeSuccessTest() {
        String email = "employee@restaurant.com";
        Long restaurantId = 1L;

        IUserSessionPort userSessionPort = mock(IUserSessionPort.class);
        IUserFeignPort userFeignPort = mock(IUserFeignPort.class);

        User employee = new User();
        employee.setEmail(email);
        employee.setRestaurantId(restaurantId);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(employee));

        assertDoesNotThrow(() -> UserValidate.checkEmployee(restaurantId, userFeignPort, userSessionPort));
    }

    @Test
    void checkEmployeeUserNotExistsExceptionTest() {
        String email = "unknown@user.com";
        Long restaurantId = 1L;

        IUserSessionPort userSessionPort = mock(IUserSessionPort.class);
        IUserFeignPort userFeignPort = mock(IUserFeignPort.class);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () ->
                UserValidate.checkEmployee(restaurantId, userFeignPort, userSessionPort));
    }

    @Test
    void checkEmployeeUserForbiddenExceptionTest() {
        String email = "employee@otherrestaurant.com";
        Long requiredRestaurantId = 1L;
        Long actualRestaurantId = 2L;

        IUserSessionPort userSessionPort = mock(IUserSessionPort.class);
        IUserFeignPort userFeignPort = mock(IUserFeignPort.class);

        User employee = new User();
        employee.setEmail(email);
        employee.setRestaurantId(actualRestaurantId);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(employee));

        assertThrows(UserForbiddenException.class, () ->
                UserValidate.checkEmployee(requiredRestaurantId, userFeignPort, userSessionPort));
    }
}