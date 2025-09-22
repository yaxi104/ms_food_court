package com.hexagonal.ms_foodcourt.util;

import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;

import java.time.LocalDate;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_PROPIETARIO;


public class TestDataUserFactory {

    private TestDataUserFactory() {
    }

    public static User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setIdNumber("1234");
        user.setPhoneNumber("+573167549634");
        user.setDateBirth(LocalDate.of(2000, 9, 17));
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoleId(1L);
        return user;
    }

    public static UserFeign mockUserFeign() {
        UserFeign user = new UserFeign();
        user.setId(1L);
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setIdNumber("1234");
        user.setPhoneNumber("+573167549634");
        user.setDateBirth(LocalDate.of(2000, 9, 17));
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoleId(1L);
        return user;
    }

    public static UserAuth mockUserAuth() {
        UserAuth user = new UserAuth();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(ROLE_PROPIETARIO);
        return user;
    }

}
