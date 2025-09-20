package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.request.User;

public interface ISecurityPort {

    User getCurrentUser();
}
