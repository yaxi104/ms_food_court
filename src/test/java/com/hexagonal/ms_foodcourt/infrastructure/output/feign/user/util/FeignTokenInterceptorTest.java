package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.util;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FeignTokenInterceptorTest {

    private FeignTokenInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new FeignTokenInterceptor();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldAddAuthorizationHeaderWhenTokenExists() {
        String token = "Bearer test-token";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn(token);

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().containsKey("Authorization"));
        assertEquals(1, template.headers().get("Authorization").size());
        assertTrue(template.headers().get("Authorization").contains(token));
    }

    @Test
    void shouldNotAddAuthorizationHeaderWhenTokenIsMissing() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertFalse(template.headers().containsKey("Authorization"));
    }

    @Test
    void shouldNotFailWhenRequestAttributesAreNotServletAttributes() {
        RequestAttributes mockAttributes = mock(RequestAttributes.class);
        RequestContextHolder.setRequestAttributes(mockAttributes);

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().isEmpty());
    }
}