package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Role;

import java.util.Optional;

public interface IRoleFeignPort {
    Optional<Role> getRoleById(Long id);

    Optional<Role> getRoleByName(String name);

}
