package com.safetynet.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AgeUtil() {
    }

    public static int calculateAge(String birthdate) {
        LocalDate birth = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public static boolean isChild(String birthdate) {
        return calculateAge(birthdate) <= 18;
    }
}
