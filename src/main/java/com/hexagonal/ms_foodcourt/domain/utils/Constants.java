package com.hexagonal.ms_foodcourt.domain.utils;

public class Constants {

    private Constants() {
    }

    /*    PATTERNS*/
    public static final String PATTERN_NAME = "^(?=.*[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ])[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9 ]+$";
    public static final String PATTERN_NUMBER_PHONE = "^\\+?\\d{7,13}$";
    public static final String PATTERN_ONLY_NUMBER = "\\d+";
    public static final Integer MAX_LENGTH = 13;
    public static final String PATTERN_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    /* ROLE */
    public static final String ADMIN = "ADMIN";
    public static final String PROPIETARIO = "PROPIETARIO";
    public static final String EMPLEADO = "EMPLEADO";

    /* STATUS*/

    public static final String TRUE_STATUS = "true";
    public static final String FALSE_STATUS = "false";

}
