package com.hexagonal.ms_foodcourt.domain.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PageableHelperTest {

    @Test
    void constructorIsPrivate() throws Exception {
        Constructor<PageableHelper> constructor = PageableHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        PageableHelper instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void checkNotBlankShouldPassWithValidValue() {
        Pageable pageable = PageableHelper.getPageable(0, 10, "name");
        assertNotNull(pageable);
    }

}