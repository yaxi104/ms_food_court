package com.hexagonal.ms_foodcourt.domain.usecase.spi;

import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;

import java.util.Optional;

public interface IUserFeignPort {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByid(Long id);

    Optional<UserAuth> getUserByIdAuth(Long id);
}
