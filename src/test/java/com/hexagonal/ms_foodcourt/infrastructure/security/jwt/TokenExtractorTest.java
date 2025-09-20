package com.hexagonal.ms_foodcourt.infrastructure.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenExtractorTest {

    private TokenExtractor tokenExtractor;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        tokenExtractor = new TokenExtractor();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void extractTokenWhenAuthorizationHeaderIsValid() {
        String expectedToken = "abc.def.ghi";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expectedToken);

        String result = tokenExtractor.extractToken(request);

        assertEquals(expectedToken, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Basic abc123"})
    void extractTokenWhenAuthorizationHeaderIsMissingNull(String arg) {
        when(request.getHeader("Authorization")).thenReturn(arg);

        String result = tokenExtractor.extractToken(request);

        assertNull(result);
    }

}