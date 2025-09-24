package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;

import java.util.List;
import java.util.regex.Pattern;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.CANCELADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ENTREGADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.FALSE_STATUS;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.MAX_LENGTH;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PATTERN_NAME;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PATTERN_NUMBER_PHONE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PATTERN_ONLY_NUMBER;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PATTERN_URL;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.TRUE_STATUS;

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

    public static void checkUrl(String logo) {
        checkNotBlank(logo);
        checkPattern(logo, PATTERN_URL);
    }


    public static void checkPositive(Integer number) {
        if (number == null || number <= 0) {
            throw new BadRequestException();
        }
    }

    public static void checkId(Long number) {
        if (number == null || number <= 0) {
            throw new BadRequestException();
        }
    }

    private static void checkMaxLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new BadRequestException();
        }
    }

    public static void checkStatus(String active) {
        ValidateRequest.checkNotBlank(active);
        if (!active.equals(TRUE_STATUS) && !active.equals(FALSE_STATUS)) {
            throw new BadRequestException();
        }
    }

    public static void checkStatusOrderValid(String status) {
        List<String> statusValidList = List.of(EN_PREPARACION, PENDIENTE, LISTO, ENTREGADO, CANCELADO);

        if (!statusValidList.contains(status)) {
            throw new BadRequestException();
        }
    }

}
