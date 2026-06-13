package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import com.safetynet.repository.FirestationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {
    @Mock
    private FirestationRepository firestationRepository;
    @Mock
    private JsonDataLoader jsonDataLoader;
    private FirestationService firestationService;
    private Firestation firestation;

    @BeforeEach
    void setUp() {
        firestationService = new FirestationService(firestationRepository, jsonDataLoader);
        firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");
    }

    @Test
    void add_shouldSaveAndSaveData() {
        Firestation result = firestationService.add(firestation);
        verify(firestationRepository).save(firestation);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(firestation);
    }

    @Test
    void update_shouldUpdateAndSaveData_whenExists() {
        when(firestationRepository.update(firestation)).thenReturn(true);
        Firestation result = firestationService.update(firestation);
        verify(firestationRepository).update(firestation);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(firestation);
    }

    @Test
    void update_shouldThrowAndNotSave_whenNotExists() {
        when(firestationRepository.update(firestation)).thenReturn(false);
        assertThatThrownBy(() -> firestationService.update(firestation)).isInstanceOf(RuntimeException.class).hasMessage("Adresse non trouvée");
        verify(jsonDataLoader, never()).saveData();
    }

    @Test
    void delete_shouldDeleteAndSaveData_whenExists() {
        when(firestationRepository.delete("1509 Culver St")).thenReturn(true);
        firestationService.delete("1509 Culver St");
        verify(firestationRepository).delete("1509 Culver St");
        verify(jsonDataLoader).saveData();
    }

    @Test
    void delete_shouldThrowAndNotSave_whenNotExists() {
        when(firestationRepository.delete("Unknown")).thenReturn(false);
        assertThatThrownBy(() -> firestationService.delete("Unknown")).isInstanceOf(RuntimeException.class).hasMessage("Adresse non trouvée");
        verify(jsonDataLoader, never()).saveData();
    }
}
