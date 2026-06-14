package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO représentant les informations détaillées d'une personne.
 * Inclut les informations personnelles et médicales complètes.
 */
@Data
@AllArgsConstructor
public class PersonDetailDTO {
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}
