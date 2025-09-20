package com.hexagonal.ms_foodcourt.domain.utils;

public class Constanst {

    private Constanst() {
    }

    /*    PATTERNS*/
    public static final String PATTERN_NAME = ".*[a-zA-Z]+.*";
    public static final String PATTERN_NUMBER_PHONE = "^\\+?\\d{7,13}$";
    public static final String PATTERN_ONLY_NUMBER = "\\d+";
    public static final Integer MAX_LENGTH = 13;
    public static final String PATTERN_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
}
