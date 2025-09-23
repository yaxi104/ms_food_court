package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, 10})
    void checkNotBlankShouldPassWithValidValue(Integer arg) {
        PageInfo pageable = PageableHelper.getPageable(arg, arg, "name");
        assertNotNull(pageable);
    }

}