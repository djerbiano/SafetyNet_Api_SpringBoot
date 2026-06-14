package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO représentant les informations de base d'une personne.
 */
@Data
@AllArgsConstructor
public class PersonInfoDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
