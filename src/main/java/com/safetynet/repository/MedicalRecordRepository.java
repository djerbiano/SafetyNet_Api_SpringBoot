package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepository {
    private final List<MedicalRecord> medicalRecords;

    public MedicalRecordRepository(JsonDataLoader jsonDataLoader) {
        this.medicalRecords = jsonDataLoader.getSafetyNetData().getMedicalrecords();
    }

    public List<MedicalRecord> findAll() {
        return medicalRecords;
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream().filter(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName)).findFirst();
    }

    public void save(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }

    public boolean update(MedicalRecord updated) {
        for (int i = 0; i < medicalRecords.size(); i++) {
            MedicalRecord m = medicalRecords.get(i);
            if (m.getFirstName().equals(updated.getFirstName()) && m.getLastName().equals(updated.getLastName())) {
                medicalRecords.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String firtsName, String lastName) {
        return medicalRecords.removeIf(m -> m.getFirstName().equals(firtsName) && m.getLastName().equals(lastName));
    }
}
