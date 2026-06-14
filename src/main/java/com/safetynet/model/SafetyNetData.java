package com.safetynet.model;

import lombok.Data;

import java.util.List;

/**
 * Représente la structure complète du fichier de données JSON.
 * Sert de modèle de mapping pour Jackson lors du chargement des données.
 */
@Data
public class SafetyNetData {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}
