package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.SafetyNetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {
    @Mock
    private JsonDataLoader jsonDataLoader;
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Boyd");
        mr1.setBirthdate("03/06/1984");
        mr1.setMedications(List.of("aznol:350mg"));
        mr1.setAllergies(List.of("nillacilan"));
        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Tenley");
        mr2.setLastName("Boyd");
        mr2.setBirthdate("02/18/2012");
        mr2.setMedications(new ArrayList<>());
        mr2.setAllergies(List.of("peanut"));
        ArrayList<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(mr1);
        medicalRecords.add(mr2);
        SafetyNetData safetyNetData = new SafetyNetData();
        safetyNetData.setPersons(new ArrayList<>());
        safetyNetData.setFirestations(new ArrayList<>());
        safetyNetData.setMedicalrecords(medicalRecords);
        when(jsonDataLoader.getSafetyNetData()).thenReturn(safetyNetData);
        medicalRecordRepository = new MedicalRecordRepository(jsonDataLoader);
    }

    @Test
    void findAll_shouldReturnAllMedicalRecords() {
        assertThat(medicalRecordRepository.findAll()).hasSize(2);
    }

    @Test
    void findByFirstNameAndLastName_shouldReturnRecord_whenExists() {
        Optional<MedicalRecord> result = medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd");
        assertThat(result).isPresent();
        assertThat(result.get().getBirthdate()).isEqualTo("03/06/1984");
    }

    @Test
    void findByFirstNameAndLastName_shouldReturnEmpty_whenNotExists() {
        assertThat(medicalRecordRepository.findByFirstNameAndLastName("Unknown", "Person")).isEmpty();
    }

    @Test
    void save_shouldAddMedicalRecord() {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("Jane");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/1990");
        mr.setMedications(new ArrayList<>());
        mr.setAllergies(new ArrayList<>());
        medicalRecordRepository.save(mr);
        assertThat(medicalRecordRepository.findAll()).hasSize(3);
    }

    @Test
    void update_shouldUpdateRecord_whenExists() {
        MedicalRecord updated = new MedicalRecord();
        updated.setFirstName("John");
        updated.setLastName("Boyd");
        updated.setBirthdate("03/06/1984");
        updated.setMedications(List.of("newMed:100mg"));
        updated.setAllergies(new ArrayList<>());
        boolean result = medicalRecordRepository.update(updated);
        assertThat(result).isTrue();
        assertThat(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd").get().getMedications()).containsExactly("newMed:100mg");
    }

    @Test
    void update_shouldReturnFalse_whenNotExists() {
        MedicalRecord unknown = new MedicalRecord();
        unknown.setFirstName("Unknown");
        unknown.setLastName("Person");
        assertThat(medicalRecordRepository.update(unknown)).isFalse();
    }

    @Test
    void delete_shouldRemoveRecord_whenExists() {
        boolean result = medicalRecordRepository.delete("John", "Boyd");
        assertThat(result).isTrue();
        assertThat(medicalRecordRepository.findAll()).hasSize(1);
    }

    @Test
    void delete_shouldReturnFalse_whenNotExists() {
        assertThat(medicalRecordRepository.delete("Unknown", "Person")).isFalse();
    }
}
