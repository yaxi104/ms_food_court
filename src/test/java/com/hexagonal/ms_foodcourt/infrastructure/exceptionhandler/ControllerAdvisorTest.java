package com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.CategoryNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.DishAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.infrastructure.exception.NoAuthenticatedUserException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

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
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleBadRequestExceptionReturnsBadRequest() {
        BadRequestException ex = mock(BadRequestException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleBadRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleUserNonFoundExceptionReturnsUnathorizate() {
        NoAuthenticatedUserException ex = mock(NoAuthenticatedUserException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleNoAuthenticatedUserException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.UNATHORIZED_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleDishNotFoundExceptionReturnsBadRequest() {
        DishNotFoundException ex = mock(DishNotFoundException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleDishNotFoundException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.DISH_NOT_FOUND.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleUserForbiddenExceptionReturnsForbidden() {
        UserForbiddenException ex = mock(UserForbiddenException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserNonFoundException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.FORBIDDEN_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleDishAlreadyExistsExceptionReturnsBadRequest() {
        DishAlreadyExistsException ex = mock(DishAlreadyExistsException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleDishAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.DISH_ALREADY_EXISTS.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleCategoryNotFoundExceptionReturnsBadRequest() {
        CategoryNotFoundException ex = mock(CategoryNotFoundException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleCategoryNotFoundException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.CATEGORY_NOT_FOUND.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleDishNotRestaurantExceptionReturnsConflict() {
        DishNotRestaurantException ex = mock(DishNotRestaurantException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleDishNotRestaurantException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.DISH_NOT_RESTAURANT.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleOrdenByIdClientExistsExceptionExceptionReturnsConflict() {
        OrdenByIdClientExistsException ex = mock(OrdenByIdClientExistsException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleOrdenByIdClientExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.ORDER_CLIENT_EXISTS.getMessage(), response.getBody().get(MESSAGE));
    }
}