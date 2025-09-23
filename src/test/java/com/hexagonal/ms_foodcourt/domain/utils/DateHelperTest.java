package com.hexagonal.ms_foodcourt.domain.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateHelperTest {

    @Test
    void constructorIsPrivate() throws Exception {
        Constructor<ValidateRequest> constructor = ValidateRequest.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        ValidateRequest instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void dateBogotaTest() {
        LocalDateTime localDateTime = DateHelper.dateBogota();
        assertNotNull(localDateTime);
    }

}