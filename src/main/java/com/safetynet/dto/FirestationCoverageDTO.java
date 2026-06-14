package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO représentant la couverture d'une caserne de pompiers.
 * Inclut la liste des personnes couvertes et le décompte adultes/enfants.
 */
@Data
@AllArgsConstructor
public class FirestationCoverageDTO {
    private List<PersonInfoDTO> persons;
    private int adultCount;
    private int childCount;
}
