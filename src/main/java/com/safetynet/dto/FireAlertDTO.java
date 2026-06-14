package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO représentant la réponse d'une alerte incendie pour une adresse.
 * Inclut le numéro de la caserne desservant l'adresse et la liste des résidents.
 */
@Data
@AllArgsConstructor
public class FireAlertDTO {
    private String station;
    private List<ResidentDTO> residents;
}
