package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.request.User;

import java.util.Optional;

public interface IUserFeignPort {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByid(Long id);
}
