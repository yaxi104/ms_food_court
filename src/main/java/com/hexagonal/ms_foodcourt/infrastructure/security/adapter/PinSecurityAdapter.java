package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import com.hexagonal.ms_foodcourt.domain.spi.IPinSecurityPort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PinSecurityAdapter implements IPinSecurityPort {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int PIN_LENGTH = 6;

    @Override
    public String getPin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < PIN_LENGTH; i++) {
            int digito = secureRandom.nextInt(10);
            pin.append(digito);
        }
        return pin.toString();
    }

    @Override
    public String getHashPin(String pin) {
        return BCrypt.hashpw(pin, BCrypt.gensalt());
    }

    @Override
    public boolean checkPin(String pinInput, String hashPin) {
        return BCrypt.checkpw(pinInput, hashPin);
    }

}
