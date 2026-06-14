package com.safetynet.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire pour le calcul de l'âge à partir d'une date de naissance.
 * Classe utilitaire non instanciable.
 */
public class AgeUtil {

    /**
     * Formateur de date correspondant au format du fichier JSON (MM/dd/yyyy).
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    /**
     * Constructeur privé — empêche l'instanciation de cette classe utilitaire.
     */
    private AgeUtil() {
    }


    /**
     * Calcule l'âge en années à partir d'une date de naissance.
     *
     * @param birthdate la date de naissance au format MM/dd/yyyy.
     * @return l'âge en années, ou 0 si la date de naissance est nulle ou vide.
     */
    public static int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            return 0;
        }
        LocalDate birth = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birth, LocalDate.now()).getYears();
    }


    /**
     * Détermine si une personne est un enfant (18 ans ou moins).
     *
     * @param birthdate la date de naissance au format MM/dd/yyyy.
     * @return true si la personne a 18 ans ou moins, false sinon.
     */
    public static boolean isChild(String birthdate) {
        return calculateAge(birthdate) <= 18;
    }
}
