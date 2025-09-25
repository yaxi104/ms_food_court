package com.hexagonal.ms_foodcourt.domain.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateHelperTest {

    @Test
    void constructorIsPrivate() throws Exception {
        Constructor<DateHelper> constructor = DateHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        DateHelper instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void dateBogotaTest() {
        LocalDateTime localDateTime = DateHelper.dateBogota();
        assertNotNull(localDateTime);
    }

}