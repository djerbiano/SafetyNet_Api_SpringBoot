package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;
import com.safetynet.model.SafetyNetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        Person john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAddress("1509 Culver St");
        john.setCity("Culver");
        john.setPhone("841-874-6512");
        john.setEmail("jaboyd@email.com");

        Person jacob = new Person();
        jacob.setFirstName("Jacob");
        jacob.setLastName("Boyd");
        jacob.setAddress("1509 Culver St");
        jacob.setCity("Culver");
        jacob.setPhone("841-874-6513");
        jacob.setEmail("drk@email.com");

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(john);
        persons.add(jacob);

        SafetyNetData safetyNetData = new SafetyNetData();
        safetyNetData.setPersons(persons);
        safetyNetData.setFirestations(new ArrayList<>());
        safetyNetData.setMedicalrecords(new ArrayList<>());

        when(jsonDataLoader.getSafetyNetData()).thenReturn(safetyNetData);
        personRepository = new PersonRepository(jsonDataLoader);
    }

    @Test
    void findAll_shouldReturnAllPersons() {
        assertThat(personRepository.findAll()).hasSize(2);
    }

    @Test
    void findByAddress_shouldReturnPersonsAtAddress() {
        assertThat(personRepository.findByAddress("1509 Culver St")).hasSize(2);
    }

    @Test
    void findByCity_shouldReturnPersonsInCity() {
        assertThat(personRepository.findByCity("Culver")).hasSize(2);
    }

    @Test
    void findByLastName_shouldReturnPersonsWithLastName() {
        assertThat(personRepository.findByLastName("Boyd")).hasSize(2);
    }

    @Test
    void save_shouldAddPerson() {
        Person newPerson = new Person();
        newPerson.setFirstName("Jane");
        newPerson.setLastName("Doe");
        personRepository.save(newPerson);
        assertThat(personRepository.findAll()).hasSize(3);
    }

    @Test
    void update_shouldUpdatePerson_whenExists() {
        Person updated = new Person();
        updated.setFirstName("John");
        updated.setLastName("Boyd");
        updated.setPhone("000-000-0000");
        updated.setAddress("1509 Culver St");
        updated.setCity("Culver");
        updated.setEmail("jaboyd@email.com");
        boolean result = personRepository.update(updated);
        assertThat(result).isTrue();
        assertThat(personRepository.findByFirstNameAndLastName("John", "Boyd").get().getPhone()).isEqualTo("000-000-0000");
    }

    @Test
    void update_shouldReturnFalse_whenNotExists() {
        Person unknown = new Person();
        unknown.setFirstName("Unknown");
        unknown.setLastName("Person");
        assertThat(personRepository.update(unknown)).isFalse();
    }

    @Test
    void delete_shouldRemovePerson_whenExists() {
        boolean result = personRepository.delete("John", "Boyd");
        assertThat(result).isTrue();
        assertThat(personRepository.findAll()).hasSize(1);
    }

    @Test
    void delete_shouldReturnFalse_whenNotExists() {
        assertThat(personRepository.delete("Unknown", "Person")).isFalse();
    }
}
