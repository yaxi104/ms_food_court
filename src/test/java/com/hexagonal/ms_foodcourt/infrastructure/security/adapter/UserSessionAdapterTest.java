package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import com.hexagonal.ms_foodcourt.infrastructure.exception.NoAuthenticatedUserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserSessionAdapterTest {


    private final UserSessionAdapter adapter = new UserSessionAdapter();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserEmailReturnsAuthenticatedUsername() {
        String email = "test@example.com";
        var auth = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String result = adapter.getCurrentUserEmail();

        assertEquals(email, result);
    }

    @Test
    void getCurrentUserEmailThrowsWhenNoAuthentication() {
        SecurityContextHolder.clearContext();

        assertThrows(NoAuthenticatedUserException.class, adapter::getCurrentUserEmail);
    }

    @Test
    void getCurrentUserEmailThrowsWhenAuthenticationIsNotAuthenticated() {
        var unauthenticated = new UsernamePasswordAuthenticationToken("user@example.com", null);
        unauthenticated.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(unauthenticated);

        assertThrows(NoAuthenticatedUserException.class, adapter::getCurrentUserEmail);
    }
}
