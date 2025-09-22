package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateRequestTest {

    @ParameterizedTest
    @NullAndEmptySource
    void checkNotBlankShouldThrowExceptionWhenNull(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNotBlank(arg));
    }

    @Test
    void checkNotBlankShouldPassWithValidValue() {
        assertDoesNotThrow(() -> ValidateRequest.checkNotBlank("Texto"));
    }

    @Test
    void checkNullNumberShouldThrowExceptionWhenNull() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNullNumber(null));
    }

    @Test
    void checkNullNumberShouldPassWithValidValue() {
        assertDoesNotThrow(() -> ValidateRequest.checkNullNumber(123L));
    }

    @Test
    void checkPatternShouldThrowExceptionWhenPatternDoesNotMatch() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPattern("abc123", "\\d+"));
    }

    @Test
    void checkPatternShouldPassWhenPatternMatches() {
        assertDoesNotThrow(() -> ValidateRequest.checkPattern("12345", "\\d+"));
    }

    @Test
    void checkNameShouldPassWithValidName() {
        assertDoesNotThrow(() -> ValidateRequest.checkName("Carlos 41"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"11423", "Hola -"})
    void checkNameShouldThrowExceptionWithInvalidName(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkName(arg));
    }

    @Test
    void checkNitShouldPassWithValidNit() {
        assertDoesNotThrow(() -> ValidateRequest.checkNit("1234567890"));
    }

    @Test
    void checkNitShouldThrowExceptionWithLetters() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNit("abc123"));
    }

    @Test
    void checkNumberPhoneShouldPassWithValidNumber() {
        assertDoesNotThrow(() -> ValidateRequest.checkNumberPhone("+573001234567"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123ABC", "+573001234567999", "41518+288"})
    void checkNumberPhoneShouldThrowExceptionWithInvalidPattern(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNumberPhone(arg));
    }

    @Test
    void checkLogoShouldPassWithValidUrl() {
        assertDoesNotThrow(() -> ValidateRequest.checkUrl("https://example.com/logo.png"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Logo"})
    void checkLogoShouldThrowExceptionWithInvalidUrl(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkUrl(arg));
    }

}

