package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private JsonDataLoader jsonDataLoader;
    private MedicalRecordService medicalRecordService;
    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecordService = new MedicalRecordService(medicalRecordRepository, jsonDataLoader);
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));
    }

    @Test
    void add_shouldSaveAndSaveData() {
        MedicalRecord result = medicalRecordService.add(medicalRecord);
        verify(medicalRecordRepository).save(medicalRecord);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(medicalRecord);
    }

    @Test
    void update_shouldUpdateAndSaveData_whenExists() {
        when(medicalRecordRepository.update(medicalRecord)).thenReturn(true);
        MedicalRecord result = medicalRecordService.update(medicalRecord);
        verify(medicalRecordRepository).update(medicalRecord);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(medicalRecord);
    }

    @Test
    void update_shouldThrowAndNotSave_whenNotExists() {
        when(medicalRecordRepository.update(medicalRecord)).thenReturn(false);
        assertThatThrownBy(() -> medicalRecordService.update(medicalRecord)).isInstanceOf(RuntimeException.class).hasMessage("Dossier médical non trouvé");
        verify(jsonDataLoader, never()).saveData();
    }

    @Test
    void delete_shouldDeleteAndSaveData_whenExists() {
        when(medicalRecordRepository.delete("John", "Boyd")).thenReturn(true);
        medicalRecordService.delete("John", "Boyd");
        verify(medicalRecordRepository).delete("John", "Boyd");
        verify(jsonDataLoader).saveData();
    }

    @Test
    void delete_shouldThrowAndNotSave_whenNotExists() {
        when(medicalRecordRepository.delete("Unknown", "Person")).thenReturn(false);
        assertThatThrownBy(() -> medicalRecordService.delete("Unknown", "Person")).isInstanceOf(RuntimeException.class).hasMessage("Dossier médical non trouvé");
        verify(jsonDataLoader, never()).saveData();
    }
}
