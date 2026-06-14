package com.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Représente le dossier médical d'une personne.
 * Contient les informations médicales incluant médicaments et allergies.
 * Le format de la date de naissance est MM/jj/aaaa (ex: 03/06/1984).
 */
@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private String birthdate;

    private List<String> medications;
    private List<String> allergies;

}
