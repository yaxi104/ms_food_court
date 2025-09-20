package com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler;

import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ControllerAdvisorTest {

    private final ControllerAdvisor controllerAdvisor = new ControllerAdvisor();

    private static final String MESSAGE = "Message";

    @Test
    void handleRestaurantAlreadyExistsExceptionReturnsConflict() {
        RestaurantAlreadyExistsException ex = new RestaurantAlreadyExistsException();

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleRestaurantAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.RESTAURANT_ALREADY_EXISTS.getMessage(), response.getBody().get(MESSAGE));
    }


    @Test
    void handleUserNotExistsExceptionReturnsConflict() {
        UserNotExistsException ex = new UserNotExistsException();

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserNotExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.USER_NOT_EXISTS.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleValidationErrorsReturnsBadRequest() {
        MethodArgumentNotValidException ex = org.mockito.Mockito.mock(MethodArgumentNotValidException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }
}