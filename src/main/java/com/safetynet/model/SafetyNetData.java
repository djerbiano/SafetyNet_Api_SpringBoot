package com.safetynet.model;

import lombok.Data;

import java.util.List;

@Data
public class SafetyNetData {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}
