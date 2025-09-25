package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PinSecurityAdapterTest {

    private PinSecurityAdapter pinSecurityAdapter;

    @BeforeEach
    void setUp() {
        pinSecurityAdapter = new PinSecurityAdapter();
    }

    @Test
    void testGetPinReturns6DigitNumberTest() {
        String pin = pinSecurityAdapter.getPin();

        assertNotNull(pin, "El PIN no debe ser nulo");
        assertEquals(6, pin.length(), "El PIN debe tener 6 dígitos");
        assertTrue(pin.matches("\\d{6}"), "El PIN debe contener solo dígitos");
    }

    @Test
    void testGetHashPinReturnsHashedValueTest() {
        String pin = "123456";
        String hash = pinSecurityAdapter.getHashPin(pin);

        assertNotNull(hash, "El hash no debe ser nulo");
        assertNotEquals(pin, hash, "El hash debe ser distinto al PIN plano");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"), "El hash debe ser un hash válido de BCrypt");
    }

    @Test
    void testCheckPinReturnsTrueForCorrectPinTest() {
        String pin = "654321";
        String hash = pinSecurityAdapter.getHashPin(pin);

        assertTrue(pinSecurityAdapter.checkPin(pin, hash), "Debe validar correctamente un PIN correcto");
    }

    @Test
    void testCheckPinReturnsFalseForIncorrectPinTest() {
        String realPin = "111111";
        String hash = pinSecurityAdapter.getHashPin(realPin);
        String wrongPin = "000000";

        assertFalse(pinSecurityAdapter.checkPin(wrongPin, hash), "Debe rechazar un PIN incorrecto");
    }
}