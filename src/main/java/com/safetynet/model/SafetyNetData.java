package com.safetynet.model;

import java.util.List;

import lombok.Data;

@Data
public class SafetyNetData {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}
