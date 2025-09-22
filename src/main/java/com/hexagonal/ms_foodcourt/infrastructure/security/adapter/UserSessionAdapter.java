package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.infrastructure.exception.NoAuthenticatedUserException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSessionAdapter implements IUserSessionPort {

    @Override
    public String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }
        return auth.getName();
    }
}