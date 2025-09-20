package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.util;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FeignTokenInterceptorTest {

    private HttpServletRequest httpServletRequest;
    private FeignTokenInterceptor interceptor;
    private RequestTemplate requestTemplate;

    @BeforeEach
    void setUp() {
        httpServletRequest = mock(HttpServletRequest.class);
        interceptor = new FeignTokenInterceptor(httpServletRequest);
        requestTemplate = mock(RequestTemplate.class);
    }

    @Test
    void applyShouldAddAuthorizationHeaderWhenTokenIsPresent() {
        String token = "Bearer valid.jwt.token";
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        interceptor.apply(requestTemplate);

        verify(requestTemplate).header("Authorization", token);
    }

    @Test
    void applyShouldNotAddHeaderWhenTokenIsNull() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        interceptor.apply(requestTemplate);

        verify(requestTemplate, never()).header(eq("Authorization"), anyString());
    }
}
