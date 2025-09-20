package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.request.User;

import java.util.Optional;

public interface IUserPersistencePort {

    Optional<User> findByEmail(String email);

    boolean existsByIdAndRole(Long id, String role);
}
