package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import com.safetynet.model.SafetyNetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {
    @Mock
    private JsonDataLoader jsonDataLoader;
    private FirestationRepository firestationRepository;

    @BeforeEach
    void setUp() {
        Firestation f1 = new Firestation();
        f1.setAddress("1509 Culver St");
        f1.setStation("3");
        Firestation f2 = new Firestation();
        f2.setAddress("29 15th St");
        f2.setStation("2");
        ArrayList<Firestation> firestations = new ArrayList<>();
        firestations.add(f1);
        firestations.add(f2);
        SafetyNetData safetyNetData = new SafetyNetData();
        safetyNetData.setPersons(new ArrayList<>());
        safetyNetData.setFirestations(firestations);
        safetyNetData.setMedicalrecords(new ArrayList<>());
        when(jsonDataLoader.getSafetyNetData()).thenReturn(safetyNetData);
        firestationRepository = new FirestationRepository(jsonDataLoader);
    }

    @Test
    void findAll_shouldReturnAllFirestations() {
        assertThat(firestationRepository.findAll()).hasSize(2);
    }

    @Test
    void findByStation_shouldReturnFirestations_whenExists() {
        assertThat(firestationRepository.findByStation("3")).hasSize(1);
    }

    @Test
    void findByStation_shouldReturnEmpty_whenNotExists() {
        assertThat(firestationRepository.findByStation("99")).isEmpty();
    }

    @Test
    void findByAddress_shouldReturnFirestation_whenExists() {
        Optional<Firestation> result = firestationRepository.findByAddress("1509 Culver St");
        assertThat(result).isPresent();
        assertThat(result.get().getStation()).isEqualTo("3");
    }

    @Test
    void findByAddress_shouldReturnEmpty_whenNotExists() {
        assertThat(firestationRepository.findByAddress("Unknown")).isEmpty();
    }

    @Test
    void save_shouldAddFirestation() {
        Firestation f = new Firestation();
        f.setAddress("New Address");
        f.setStation("5");
        firestationRepository.save(f);
        assertThat(firestationRepository.findAll()).hasSize(3);
    }

    @Test
    void update_shouldUpdateStation_whenExists() {
        Firestation updated = new Firestation();
        updated.setAddress("1509 Culver St");
        updated.setStation("9");
        boolean result = firestationRepository.update(updated);
        assertThat(result).isTrue();
        assertThat(firestationRepository.findByAddress("1509 Culver St").get().getStation()).isEqualTo("9");
    }

    @Test
    void update_shouldReturnFalse_whenNotExists() {
        Firestation unknown = new Firestation();
        unknown.setAddress("Unknown");
        unknown.setStation("1");
        assertThat(firestationRepository.update(unknown)).isFalse();
    }

    @Test
    void delete_shouldRemoveFirestation_whenExists() {
        boolean result = firestationRepository.delete("1509 Culver St");
        assertThat(result).isTrue();
        assertThat(firestationRepository.findAll()).hasSize(1);
    }

    @Test
    void delete_shouldReturnFalse_whenNotExists() {
        assertThat(firestationRepository.delete("Unknown")).isFalse();
    }
}
