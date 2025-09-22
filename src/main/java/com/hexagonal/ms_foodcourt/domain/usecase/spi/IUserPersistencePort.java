package com.hexagonal.ms_foodcourt.domain.usecase.spi;

import com.hexagonal.ms_foodcourt.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {

    Optional<User> findByEmail(String email);

    boolean existsByIdAndRole(Long id, String role);
}
