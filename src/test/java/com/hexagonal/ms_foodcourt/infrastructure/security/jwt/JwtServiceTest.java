package com.hexagonal.ms_foodcourt.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private static final String SECRET = "mySuperSecretKeyForJwtTesting1234567890!@#"; // 256-bit
    private JwtService jwtService;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET);
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String createToken(String subject, Object role, Date expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void shouldExtractUsername() {
        String token = createToken("user@example.com", "ADMIN", new Date(System.currentTimeMillis() + 100000));
        String username = jwtService.extractUsername(token);

        assertEquals("user@example.com", username);
    }

    @Test
    void shouldExtractSingleRole() {
        String token = createToken("user@example.com", "ADMIN", new Date(System.currentTimeMillis() + 100000));
        var roles = jwtService.extractRoles(token);

        assertEquals(1, roles.size());
        assertEquals(new SimpleGrantedAuthority("ROLE_ADMIN"), roles.get(0));
    }

    @Test
    void shouldExtractMultipleRoles() {
        List<String> roleList = List.of("ADMIN", "PROPIETARIO");
        String token = createToken("user@example.com", roleList, new Date(System.currentTimeMillis() + 100000));
        var roles = jwtService.extractRoles(token);

        assertEquals(2, roles.size());
        assertTrue(roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(roles.contains(new SimpleGrantedAuthority("ROLE_PROPIETARIO")));
    }

    @Test
    void shouldReturnFalseWhenTokenNotExpired() {
        String token = createToken("user@example.com", "ADMIN", new Date(System.currentTimeMillis() + 100000));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void shouldReturnFalseWhenTokenNotExpiredInTokenExpired() {
        String token = "valid.token.example";
        JwtService jwtServiceSpy = spy(jwtService);

        Claims claimsMock = mock(Claims.class);
        Date futureDate = new Date(System.currentTimeMillis() + 10000);
        when(claimsMock.getExpiration()).thenReturn(futureDate);

        doReturn(claimsMock).when(jwtServiceSpy).extractAllClaims(token);

        boolean expired = jwtServiceSpy.isTokenExpired(token);

        assertFalse(expired, "Debe retornar false cuando la fecha de expiración es futura");
    }

    @Test
    void shouldReturnTrueWhenTokenExpirationDateIsBeforeNow() {
        String token = "some.token.example";
        JwtService jwtServiceSpy = spy(jwtService);

        Claims claimsMock = mock(Claims.class);
        Date pastDate = new Date(System.currentTimeMillis() - 10000);
        when(claimsMock.getExpiration()).thenReturn(pastDate);

        doReturn(claimsMock).when(jwtServiceSpy).extractAllClaims(token);

        boolean expired = jwtServiceSpy.isTokenExpired(token);

        assertTrue(expired, "Debe retornar true cuando la fecha de expiración es pasada");
    }

    @Test
    void shouldReturnTrueWhenTokenExpiredExceptionThrown() {
        String token = "expired.token.example";
        JwtService jwtServiceSpy = spy(jwtService);

        doThrow(new ExpiredJwtException(null, null, "Token expired"))
                .when(jwtServiceSpy).extractAllClaims(token);

        boolean expired = jwtServiceSpy.isTokenExpired(token);

        assertTrue(expired, "Debe retornar true cuando se lanza ExpiredJwtException");
    }

    @Test
    void shouldExtractAllClaims() {
        String token = createToken("user@example.com", "ADMIN", new Date(System.currentTimeMillis() + 100000));
        var claims = jwtService.extractAllClaims(token);

        assertEquals("user@example.com", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
    }
}
