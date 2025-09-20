package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;

import java.util.regex.Pattern;

import static com.hexagonal.ms_foodcourt.domain.utils.Constanst.MAX_LENGTH;
import static com.hexagonal.ms_foodcourt.domain.utils.Constanst.PATTERN_NAME;
import static com.hexagonal.ms_foodcourt.domain.utils.Constanst.PATTERN_NUMBER_PHONE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constanst.PATTERN_ONLY_NUMBER;
import static com.hexagonal.ms_foodcourt.domain.utils.Constanst.PATTERN_URL;

public class ValidateRequest {

    private ValidateRequest() {
    }

    public static void checkNotBlank(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException();
        }
    }

    public static void checkNullNumber(Long value) {
        if (value == null) {
            throw new BadRequestException();
        }
    }

    public static void checkPattern(String value, String pattern) {
        if (!Pattern.matches(pattern, value)) {
            throw new BadRequestException();
        }
    }

    public static void checkName(String name) {
        checkNotBlank(name);
        checkPattern(name, PATTERN_NAME);
    }

    public static void checkNit(String nit) {
        checkNotBlank(nit);
        checkPattern(nit, PATTERN_ONLY_NUMBER);
    }

    public static void checkNumberPhone(String numberPhone) {
        checkNotBlank(numberPhone);
        checkMaxLength(numberPhone);
        checkPattern(numberPhone, PATTERN_NUMBER_PHONE);
    }

    public static void checkLogo(String logo) {
        checkNotBlank(logo);
        checkPattern(logo, PATTERN_URL);
    }

    private static void checkMaxLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new BadRequestException();
        }
    }
}
