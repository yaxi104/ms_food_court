package com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.infrastructure.exception.NoAuthenticatedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "Message";

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantAlreadyExistsException(
            RestaurantAlreadyExistsException restaurantAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_ALREADY_EXISTS.getMessage()));
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserNotExistsException(
            UserNotExistsException userNotExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_NOT_EXISTS.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage()));
    }

    @ExceptionHandler(NoAuthenticatedUserException.class)
    public ResponseEntity<Map<String, String>> handleNoAuthenticatedUserException(NoAuthenticatedUserException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap(MESSAGE, ExceptionResponse.UNATHORIZED_MESSAGE.getMessage()));
    }

}