package com.safetynet.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private AgeUtil() {
    }

    public static int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            return 0;
        }
        LocalDate birth = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public static boolean isChild(String birthdate) {
        return calculateAge(birthdate) <= 18;
    }
}
