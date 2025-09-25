package com.hexagonal.ms_foodcourt.domain.spi;

public interface IPinSecurityPort {

    String getPin();

    String getHashPin(String pin);

    boolean checkPin(String pinInput, String hashPin);
}
