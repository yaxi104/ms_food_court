package com.hexagonal.ms_foodcourt.domain.usecase.spi;

import com.hexagonal.ms_foodcourt.domain.model.User;

public interface ISecurityPort {

    User getCurrentUser();
}
