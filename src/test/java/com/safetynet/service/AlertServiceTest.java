package com.safetynet.service;

import com.safetynet.dto.*;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.repository.FirestationRepository;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    private AlertService alertService;
    private Person john;
    private Person tenley;
    private Firestation firestation;
    private MedicalRecord mrJohn;
    private MedicalRecord mrTenley;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(personRepository, firestationRepository, medicalRecordRepository);
        john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAddress("1509 Culver St");
        john.setCity("Culver");
        john.setPhone("841-874-6512");
        john.setEmail("jaboyd@email.com");
        tenley = new Person();
        tenley.setFirstName("Tenley");
        tenley.setLastName("Boyd");
        tenley.setAddress("1509 Culver St");
        tenley.setCity("Culver");
        tenley.setPhone("841-874-6512");
        tenley.setEmail("tenz@email.com");
        firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");
        mrJohn = new MedicalRecord();
        mrJohn.setFirstName("John");
        mrJohn.setLastName("Boyd");
        mrJohn.setBirthdate("03/06/1984"); // adulte
        mrJohn.setMedications(List.of("aznol:350mg"));
        mrJohn.setAllergies(List.of("nillacilan"));
        mrTenley = new MedicalRecord();
        mrTenley.setFirstName("Tenley");
        mrTenley.setLastName("Boyd");
        mrTenley.setBirthdate("02/18/2012"); // enfant
        mrTenley.setMedications(List.of());
        mrTenley.setAllergies(List.of("peanut"));
    }

    @Test
    void getCoverageByStation_shouldReturnPersonsAndCounts() {
        when(firestationRepository.findByStation("3")).thenReturn(List.of(firestation));
        when(personRepository.findAll()).thenReturn(List.of(john, tenley));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        when(medicalRecordRepository.findByFirstNameAndLastName("Tenley", "Boyd")).thenReturn(Optional.of(mrTenley));
        FirestationCoverageDTO result = alertService.getCoverageByStation("3");
        assertThat(result.getPersons()).hasSize(2);
        assertThat(result.getAdultCount()).isEqualTo(1);
        assertThat(result.getChildCount()).isEqualTo(1);
    }

    @Test
    void getChildrenByAddress_shouldReturnOnlyChildren() {
        when(personRepository.findByAddress("1509 Culver St")).thenReturn(List.of(john, tenley));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        when(medicalRecordRepository.findByFirstNameAndLastName("Tenley", "Boyd")).thenReturn(Optional.of(mrTenley));
        List<ChildAlertDTO> result = alertService.getChildrenByAddress("1509 Culver St");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Tenley");
        assertThat(result.get(0).getHouseholdMembers()).contains("John Boyd");
    }

    @Test
    void getChildrenByAddress_shouldReturnEmpty_whenNoChildren() {
        when(personRepository.findByAddress("1509 Culver St")).thenReturn(List.of(john));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        List<ChildAlertDTO> result = alertService.getChildrenByAddress("1509 Culver St");
        assertThat(result).isEmpty();
    }

    @Test
    void getPhonesByStation_shouldReturnPhones() {
        when(firestationRepository.findByStation("3")).thenReturn(List.of(firestation));
        when(personRepository.findAll()).thenReturn(List.of(john, tenley));
        List<String> result = alertService.getPhonesByStation("3");
        assertThat(result).hasSize(2);
        assertThat(result).contains("841-874-6512");
    }

    @Test
    void getResidentsByAddress_shouldReturnStationAndResidents() {
        when(firestationRepository.findByAddress("1509 Culver St")).thenReturn(Optional.of(firestation));
        when(personRepository.findByAddress("1509 Culver St")).thenReturn(List.of(john));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        FireAlertDTO result = alertService.getResidentsByAddress("1509 Culver St");
        assertThat(result.getStation()).isEqualTo("3");
        assertThat(result.getResidents()).hasSize(1);
        assertThat(result.getResidents().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void getHouseholdsByStations_shouldReturnGroupedByAddress() {
        when(firestationRepository.findByStation("3")).thenReturn(List.of(firestation));
        when(personRepository.findByAddress("1509 Culver St")).thenReturn(List.of(john));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        Map<String, List<ResidentDTO>> result = alertService.getHouseholdsByStations(List.of("3"));
        assertThat(result).containsKey("1509 Culver St");
        assertThat(result.get("1509 Culver St")).hasSize(1);
    }

    @Test
    void getPersonInfoByLastName_shouldReturnAllPersonsWithLastName() {
        when(personRepository.findByLastName("Boyd")).thenReturn(List.of(john, tenley));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(Optional.of(mrJohn));
        when(medicalRecordRepository.findByFirstNameAndLastName("Tenley", "Boyd")).thenReturn(Optional.of(mrTenley));
        List<PersonDetailDTO> result = alertService.getPersonInfoByLastName("Boyd");
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("jaboyd@email.com");
    }

    @Test
    void getEmailsByCity_shouldReturnEmails() {
        when(personRepository.findByCity("Culver")).thenReturn(List.of(john, tenley));
        List<String> result = alertService.getEmailsByCity("Culver");
        assertThat(result).hasSize(2);
        assertThat(result).contains("jaboyd@email.com", "tenz@email.com");
    }
}
