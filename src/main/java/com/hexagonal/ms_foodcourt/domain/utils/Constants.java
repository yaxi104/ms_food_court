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
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_PROPIETARIO = "PROPIETARIO";
    public static final String ROLE_EMPLEADO = "EMPLEADO";
    public static final String ROLE_CLIENTE = "CLIENTE";

    /* STATUS*/
    public static final String TRUE_STATUS = "true";
    public static final String FALSE_STATUS = "false";

    /* STATUS ORDER*/
    public static final String EN_PREPARACION = "EN_PREPARACION";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String LISTO = "LISTO";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";

    /*  PAGE CONFIG*/
    public static final Integer DEFAULT_PAGE = 0;
    public static final Integer DEFAULT_SIZE = 10;
    public static final Integer MAX_SIZE = 100;

}
